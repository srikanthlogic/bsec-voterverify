package com.facebook.imagepipeline.producers;

import bolts.Continuation;
import bolts.Task;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.imagepipeline.cache.BufferedDiskCache;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DiskCacheReadProducer implements Producer<EncodedImage> {
    public static final String ENCODED_IMAGE_SIZE = "encodedImageSize";
    public static final String EXTRA_CACHED_VALUE_FOUND = "cached_value_found";
    public static final String PRODUCER_NAME = "DiskCacheProducer";
    private final CacheKeyFactory mCacheKeyFactory;
    private final BufferedDiskCache mDefaultBufferedDiskCache;
    private final Producer<EncodedImage> mInputProducer;
    private final BufferedDiskCache mSmallImageBufferedDiskCache;

    public DiskCacheReadProducer(BufferedDiskCache defaultBufferedDiskCache, BufferedDiskCache smallImageBufferedDiskCache, CacheKeyFactory cacheKeyFactory, Producer<EncodedImage> inputProducer) {
        this.mDefaultBufferedDiskCache = defaultBufferedDiskCache;
        this.mSmallImageBufferedDiskCache = smallImageBufferedDiskCache;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        ImageRequest imageRequest = producerContext.getImageRequest();
        if (!imageRequest.isDiskCacheEnabled()) {
            maybeStartInputProducer(consumer, producerContext);
            return;
        }
        producerContext.getListener().onProducerStart(producerContext.getId(), PRODUCER_NAME);
        CacheKey cacheKey = this.mCacheKeyFactory.getEncodedCacheKey(imageRequest, producerContext.getCallerContext());
        BufferedDiskCache preferredCache = imageRequest.getCacheChoice() == ImageRequest.CacheChoice.SMALL ? this.mSmallImageBufferedDiskCache : this.mDefaultBufferedDiskCache;
        AtomicBoolean isCancelled = new AtomicBoolean(false);
        preferredCache.get(cacheKey, isCancelled).continueWith(onFinishDiskReads(consumer, producerContext));
        subscribeTaskForRequestCancellation(isCancelled, producerContext);
    }

    private Continuation<EncodedImage, Void> onFinishDiskReads(final Consumer<EncodedImage> consumer, final ProducerContext producerContext) {
        final String requestId = producerContext.getId();
        final ProducerListener listener = producerContext.getListener();
        return new Continuation<EncodedImage, Void>() { // from class: com.facebook.imagepipeline.producers.DiskCacheReadProducer.1
            @Override // bolts.Continuation
            public Void then(Task<EncodedImage> task) throws Exception {
                if (DiskCacheReadProducer.isTaskCancelled(task)) {
                    listener.onProducerFinishWithCancellation(requestId, DiskCacheReadProducer.PRODUCER_NAME, null);
                    consumer.onCancellation();
                } else if (task.isFaulted()) {
                    listener.onProducerFinishWithFailure(requestId, DiskCacheReadProducer.PRODUCER_NAME, task.getError(), null);
                    DiskCacheReadProducer.this.mInputProducer.produceResults(consumer, producerContext);
                } else {
                    EncodedImage cachedReference = task.getResult();
                    if (cachedReference != null) {
                        ProducerListener producerListener = listener;
                        String str = requestId;
                        producerListener.onProducerFinishWithSuccess(str, DiskCacheReadProducer.PRODUCER_NAME, DiskCacheReadProducer.getExtraMap(producerListener, str, true, cachedReference.getSize()));
                        listener.onUltimateProducerReached(requestId, DiskCacheReadProducer.PRODUCER_NAME, true);
                        consumer.onProgressUpdate(1.0f);
                        consumer.onNewResult(cachedReference, 1);
                        cachedReference.close();
                    } else {
                        ProducerListener producerListener2 = listener;
                        String str2 = requestId;
                        producerListener2.onProducerFinishWithSuccess(str2, DiskCacheReadProducer.PRODUCER_NAME, DiskCacheReadProducer.getExtraMap(producerListener2, str2, false, 0));
                        DiskCacheReadProducer.this.mInputProducer.produceResults(consumer, producerContext);
                    }
                }
                return null;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTaskCancelled(Task<?> task) {
        return task.isCancelled() || (task.isFaulted() && (task.getError() instanceof CancellationException));
    }

    private void maybeStartInputProducer(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        if (producerContext.getLowestPermittedRequestLevel().getValue() >= ImageRequest.RequestLevel.DISK_CACHE.getValue()) {
            consumer.onNewResult(null, 1);
        } else {
            this.mInputProducer.produceResults(consumer, producerContext);
        }
    }

    @Nullable
    static Map<String, String> getExtraMap(ProducerListener listener, String requestId, boolean valueFound, int sizeInBytes) {
        if (!listener.requiresExtraMap(requestId)) {
            return null;
        }
        if (valueFound) {
            return ImmutableMap.of("cached_value_found", String.valueOf(valueFound), "encodedImageSize", String.valueOf(sizeInBytes));
        }
        return ImmutableMap.of("cached_value_found", String.valueOf(valueFound));
    }

    private void subscribeTaskForRequestCancellation(final AtomicBoolean isCancelled, ProducerContext producerContext) {
        producerContext.addCallbacks(new BaseProducerContextCallbacks() { // from class: com.facebook.imagepipeline.producers.DiskCacheReadProducer.2
            @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
            public void onCancellationRequested() {
                isCancelled.set(true);
            }
        });
    }
}
