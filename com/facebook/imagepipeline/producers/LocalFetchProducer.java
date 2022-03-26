package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.Closeables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class LocalFetchProducer implements Producer<EncodedImage> {
    private final Executor mExecutor;
    private final PooledByteBufferFactory mPooledByteBufferFactory;

    protected abstract EncodedImage getEncodedImage(ImageRequest imageRequest) throws IOException;

    protected abstract String getProducerName();

    public LocalFetchProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory) {
        this.mExecutor = executor;
        this.mPooledByteBufferFactory = pooledByteBufferFactory;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        final ProducerListener listener = producerContext.getListener();
        final String requestId = producerContext.getId();
        final ImageRequest imageRequest = producerContext.getImageRequest();
        final StatefulProducerRunnable cancellableProducerRunnable = new StatefulProducerRunnable<EncodedImage>(consumer, getProducerName(), listener, requestId) { // from class: com.facebook.imagepipeline.producers.LocalFetchProducer.1
            @Override // com.facebook.common.executors.StatefulRunnable
            @Nullable
            public EncodedImage getResult() throws Exception {
                EncodedImage encodedImage = LocalFetchProducer.this.getEncodedImage(imageRequest);
                if (encodedImage == null) {
                    listener.onUltimateProducerReached(requestId, LocalFetchProducer.this.getProducerName(), false);
                    return null;
                }
                encodedImage.parseMetaData();
                listener.onUltimateProducerReached(requestId, LocalFetchProducer.this.getProducerName(), true);
                return encodedImage;
            }

            public void disposeResult(EncodedImage result) {
                EncodedImage.closeSafely(result);
            }
        };
        producerContext.addCallbacks(new BaseProducerContextCallbacks() { // from class: com.facebook.imagepipeline.producers.LocalFetchProducer.2
            @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
            public void onCancellationRequested() {
                cancellableProducerRunnable.cancel();
            }
        });
        this.mExecutor.execute(cancellableProducerRunnable);
    }

    protected EncodedImage getByteBufferBackedEncodedImage(InputStream inputStream, int length) throws IOException {
        CloseableReference<PooledByteBuffer> ref = null;
        try {
            if (length <= 0) {
                ref = CloseableReference.of(this.mPooledByteBufferFactory.newByteBuffer(inputStream));
            } else {
                ref = CloseableReference.of(this.mPooledByteBufferFactory.newByteBuffer(inputStream, length));
            }
            return new EncodedImage(ref);
        } finally {
            Closeables.closeQuietly(inputStream);
            CloseableReference.closeSafely(ref);
        }
    }

    protected EncodedImage getEncodedImage(InputStream inputStream, int length) throws IOException {
        return getByteBufferBackedEncodedImage(inputStream, length);
    }
}
