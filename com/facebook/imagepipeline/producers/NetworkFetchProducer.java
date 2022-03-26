package com.facebook.imagepipeline.producers;

import android.os.SystemClock;
import com.facebook.common.memory.ByteArrayPool;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.memory.PooledByteBufferOutputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.common.BytesRange;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.NetworkFetcher;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class NetworkFetchProducer implements Producer<EncodedImage> {
    public static final String INTERMEDIATE_RESULT_PRODUCER_EVENT = "intermediate_result";
    public static final String PRODUCER_NAME = "NetworkFetchProducer";
    private static final int READ_SIZE = 16384;
    static final long TIME_BETWEEN_PARTIAL_RESULTS_MS = 100;
    private final ByteArrayPool mByteArrayPool;
    private final NetworkFetcher mNetworkFetcher;
    private final PooledByteBufferFactory mPooledByteBufferFactory;

    public NetworkFetchProducer(PooledByteBufferFactory pooledByteBufferFactory, ByteArrayPool byteArrayPool, NetworkFetcher networkFetcher) {
        this.mPooledByteBufferFactory = pooledByteBufferFactory;
        this.mByteArrayPool = byteArrayPool;
        this.mNetworkFetcher = networkFetcher;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext context) {
        context.getListener().onProducerStart(context.getId(), PRODUCER_NAME);
        final FetchState fetchState = this.mNetworkFetcher.createFetchState(consumer, context);
        this.mNetworkFetcher.fetch(fetchState, new NetworkFetcher.Callback() { // from class: com.facebook.imagepipeline.producers.NetworkFetchProducer.1
            @Override // com.facebook.imagepipeline.producers.NetworkFetcher.Callback
            public void onResponse(InputStream response, int responseLength) throws IOException {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.beginSection("NetworkFetcher->onResponse");
                }
                NetworkFetchProducer.this.onResponse(fetchState, response, responseLength);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            }

            @Override // com.facebook.imagepipeline.producers.NetworkFetcher.Callback
            public void onFailure(Throwable throwable) {
                NetworkFetchProducer.this.onFailure(fetchState, throwable);
            }

            @Override // com.facebook.imagepipeline.producers.NetworkFetcher.Callback
            public void onCancellation() {
                NetworkFetchProducer.this.onCancellation(fetchState);
            }
        });
    }

    protected void onResponse(FetchState fetchState, InputStream responseData, int responseContentLength) throws IOException {
        PooledByteBufferOutputStream pooledOutputStream;
        if (responseContentLength > 0) {
            pooledOutputStream = this.mPooledByteBufferFactory.newOutputStream(responseContentLength);
        } else {
            pooledOutputStream = this.mPooledByteBufferFactory.newOutputStream();
        }
        byte[] ioArray = this.mByteArrayPool.get(16384);
        while (true) {
            try {
                int length = responseData.read(ioArray);
                if (length < 0) {
                    this.mNetworkFetcher.onFetchCompletion(fetchState, pooledOutputStream.size());
                    handleFinalResult(pooledOutputStream, fetchState);
                    return;
                } else if (length > 0) {
                    pooledOutputStream.write(ioArray, 0, length);
                    maybeHandleIntermediateResult(pooledOutputStream, fetchState);
                    fetchState.getConsumer().onProgressUpdate(calculateProgress(pooledOutputStream.size(), responseContentLength));
                }
            } finally {
                this.mByteArrayPool.release(ioArray);
                pooledOutputStream.close();
            }
        }
    }

    protected static float calculateProgress(int downloaded, int total) {
        return total > 0 ? ((float) downloaded) / ((float) total) : 1.0f - ((float) Math.exp(((double) (-downloaded)) / 50000.0d));
    }

    protected void maybeHandleIntermediateResult(PooledByteBufferOutputStream pooledOutputStream, FetchState fetchState) {
        long nowMs = SystemClock.uptimeMillis();
        if (shouldPropagateIntermediateResults(fetchState) && nowMs - fetchState.getLastIntermediateResultTimeMs() >= TIME_BETWEEN_PARTIAL_RESULTS_MS) {
            fetchState.setLastIntermediateResultTimeMs(nowMs);
            fetchState.getListener().onProducerEvent(fetchState.getId(), PRODUCER_NAME, INTERMEDIATE_RESULT_PRODUCER_EVENT);
            notifyConsumer(pooledOutputStream, fetchState.getOnNewResultStatusFlags(), fetchState.getResponseBytesRange(), fetchState.getConsumer());
        }
    }

    protected void handleFinalResult(PooledByteBufferOutputStream pooledOutputStream, FetchState fetchState) {
        Map<String, String> extraMap = getExtraMap(fetchState, pooledOutputStream.size());
        ProducerListener listener = fetchState.getListener();
        listener.onProducerFinishWithSuccess(fetchState.getId(), PRODUCER_NAME, extraMap);
        listener.onUltimateProducerReached(fetchState.getId(), PRODUCER_NAME, true);
        notifyConsumer(pooledOutputStream, fetchState.getOnNewResultStatusFlags() | 1, fetchState.getResponseBytesRange(), fetchState.getConsumer());
    }

    protected static void notifyConsumer(PooledByteBufferOutputStream pooledOutputStream, int status, @Nullable BytesRange responseBytesRange, Consumer<EncodedImage> consumer) {
        CloseableReference<PooledByteBuffer> result = CloseableReference.of(pooledOutputStream.toByteBuffer());
        EncodedImage encodedImage = null;
        try {
            encodedImage = new EncodedImage(result);
            encodedImage.setBytesRange(responseBytesRange);
            encodedImage.parseMetaData();
            consumer.onNewResult(encodedImage, status);
        } finally {
            EncodedImage.closeSafely(encodedImage);
            CloseableReference.closeSafely(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFailure(FetchState fetchState, Throwable e) {
        fetchState.getListener().onProducerFinishWithFailure(fetchState.getId(), PRODUCER_NAME, e, null);
        fetchState.getListener().onUltimateProducerReached(fetchState.getId(), PRODUCER_NAME, false);
        fetchState.getConsumer().onFailure(e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCancellation(FetchState fetchState) {
        fetchState.getListener().onProducerFinishWithCancellation(fetchState.getId(), PRODUCER_NAME, null);
        fetchState.getConsumer().onCancellation();
    }

    private boolean shouldPropagateIntermediateResults(FetchState fetchState) {
        if (!fetchState.getContext().isIntermediateResultExpected()) {
            return false;
        }
        return this.mNetworkFetcher.shouldPropagate(fetchState);
    }

    @Nullable
    private Map<String, String> getExtraMap(FetchState fetchState, int byteSize) {
        if (!fetchState.getListener().requiresExtraMap(fetchState.getId())) {
            return null;
        }
        return this.mNetworkFetcher.getExtraMap(fetchState, byteSize);
    }
}
