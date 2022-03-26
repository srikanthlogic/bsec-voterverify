package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.cache.BufferedDiskCache;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
/* loaded from: classes.dex */
public class DiskCacheWriteProducer implements Producer<EncodedImage> {
    static final String PRODUCER_NAME = "DiskCacheProducer";
    private final CacheKeyFactory mCacheKeyFactory;
    private final BufferedDiskCache mDefaultBufferedDiskCache;
    private final Producer<EncodedImage> mInputProducer;
    private final BufferedDiskCache mSmallImageBufferedDiskCache;

    public DiskCacheWriteProducer(BufferedDiskCache defaultBufferedDiskCache, BufferedDiskCache smallImageBufferedDiskCache, CacheKeyFactory cacheKeyFactory, Producer<EncodedImage> inputProducer) {
        this.mDefaultBufferedDiskCache = defaultBufferedDiskCache;
        this.mSmallImageBufferedDiskCache = smallImageBufferedDiskCache;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        maybeStartInputProducer(consumer, producerContext);
    }

    private void maybeStartInputProducer(Consumer<EncodedImage> consumerOfDiskCacheWriteProducer, ProducerContext producerContext) {
        Consumer<EncodedImage> consumer;
        if (producerContext.getLowestPermittedRequestLevel().getValue() >= ImageRequest.RequestLevel.DISK_CACHE.getValue()) {
            consumerOfDiskCacheWriteProducer.onNewResult(null, 1);
            return;
        }
        if (producerContext.getImageRequest().isDiskCacheEnabled()) {
            consumer = new DiskCacheWriteConsumer(consumerOfDiskCacheWriteProducer, producerContext, this.mDefaultBufferedDiskCache, this.mSmallImageBufferedDiskCache, this.mCacheKeyFactory);
        } else {
            consumer = consumerOfDiskCacheWriteProducer;
        }
        this.mInputProducer.produceResults(consumer, producerContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DiskCacheWriteConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final CacheKeyFactory mCacheKeyFactory;
        private final BufferedDiskCache mDefaultBufferedDiskCache;
        private final ProducerContext mProducerContext;
        private final BufferedDiskCache mSmallImageBufferedDiskCache;

        private DiskCacheWriteConsumer(Consumer<EncodedImage> consumer, ProducerContext producerContext, BufferedDiskCache defaultBufferedDiskCache, BufferedDiskCache smallImageBufferedDiskCache, CacheKeyFactory cacheKeyFactory) {
            super(consumer);
            this.mProducerContext = producerContext;
            this.mDefaultBufferedDiskCache = defaultBufferedDiskCache;
            this.mSmallImageBufferedDiskCache = smallImageBufferedDiskCache;
            this.mCacheKeyFactory = cacheKeyFactory;
        }

        public void onNewResultImpl(EncodedImage newResult, int status) {
            if (isNotLast(status) || newResult == null || statusHasAnyFlag(status, 10) || newResult.getImageFormat() == ImageFormat.UNKNOWN) {
                getConsumer().onNewResult(newResult, status);
                return;
            }
            ImageRequest imageRequest = this.mProducerContext.getImageRequest();
            CacheKey cacheKey = this.mCacheKeyFactory.getEncodedCacheKey(imageRequest, this.mProducerContext.getCallerContext());
            if (imageRequest.getCacheChoice() == ImageRequest.CacheChoice.SMALL) {
                this.mSmallImageBufferedDiskCache.put(cacheKey, newResult);
            } else {
                this.mDefaultBufferedDiskCache.put(cacheKey, newResult);
            }
            getConsumer().onNewResult(newResult, status);
        }
    }
}
