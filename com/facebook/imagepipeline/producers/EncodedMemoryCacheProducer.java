package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import java.util.Map;
/* loaded from: classes.dex */
public class EncodedMemoryCacheProducer implements Producer<EncodedImage> {
    public static final String EXTRA_CACHED_VALUE_FOUND = "cached_value_found";
    public static final String PRODUCER_NAME = "EncodedMemoryCacheProducer";
    private final CacheKeyFactory mCacheKeyFactory;
    private final Producer<EncodedImage> mInputProducer;
    private final MemoryCache<CacheKey, PooledByteBuffer> mMemoryCache;

    public EncodedMemoryCacheProducer(MemoryCache<CacheKey, PooledByteBuffer> memoryCache, CacheKeyFactory cacheKeyFactory, Producer<EncodedImage> inputProducer) {
        this.mMemoryCache = memoryCache;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("EncodedMemoryCacheProducer#produceResults");
            }
            String requestId = producerContext.getId();
            ProducerListener listener = producerContext.getListener();
            listener.onProducerStart(requestId, PRODUCER_NAME);
            CacheKey cacheKey = this.mCacheKeyFactory.getEncodedCacheKey(producerContext.getImageRequest(), producerContext.getCallerContext());
            CloseableReference<PooledByteBuffer> cachedReference = this.mMemoryCache.get(cacheKey);
            Map<String, String> map = null;
            if (cachedReference != null) {
                EncodedImage cachedEncodedImage = new EncodedImage(cachedReference);
                if (listener.requiresExtraMap(requestId)) {
                    map = ImmutableMap.of("cached_value_found", "true");
                }
                listener.onProducerFinishWithSuccess(requestId, PRODUCER_NAME, map);
                listener.onUltimateProducerReached(requestId, PRODUCER_NAME, true);
                consumer.onProgressUpdate(1.0f);
                consumer.onNewResult(cachedEncodedImage, 1);
                EncodedImage.closeSafely(cachedEncodedImage);
                CloseableReference.closeSafely(cachedReference);
            } else if (producerContext.getLowestPermittedRequestLevel().getValue() >= ImageRequest.RequestLevel.ENCODED_MEMORY_CACHE.getValue()) {
                listener.onProducerFinishWithSuccess(requestId, PRODUCER_NAME, listener.requiresExtraMap(requestId) ? ImmutableMap.of("cached_value_found", "false") : null);
                listener.onUltimateProducerReached(requestId, PRODUCER_NAME, false);
                consumer.onNewResult(null, 1);
                CloseableReference.closeSafely(cachedReference);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            } else {
                Consumer consumerOfInputProducer = new EncodedMemoryCacheConsumer(consumer, this.mMemoryCache, cacheKey, producerContext.getImageRequest().isMemoryCacheEnabled());
                if (listener.requiresExtraMap(requestId)) {
                    map = ImmutableMap.of("cached_value_found", "false");
                }
                listener.onProducerFinishWithSuccess(requestId, PRODUCER_NAME, map);
                this.mInputProducer.produceResults(consumerOfInputProducer, producerContext);
                CloseableReference.closeSafely(cachedReference);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            }
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    /* loaded from: classes.dex */
    private static class EncodedMemoryCacheConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final boolean mIsMemoryCacheEnabled;
        private final MemoryCache<CacheKey, PooledByteBuffer> mMemoryCache;
        private final CacheKey mRequestedCacheKey;

        public EncodedMemoryCacheConsumer(Consumer<EncodedImage> consumer, MemoryCache<CacheKey, PooledByteBuffer> memoryCache, CacheKey requestedCacheKey, boolean isMemoryCacheEnabled) {
            super(consumer);
            this.mMemoryCache = memoryCache;
            this.mRequestedCacheKey = requestedCacheKey;
            this.mIsMemoryCacheEnabled = isMemoryCacheEnabled;
        }

        public void onNewResultImpl(EncodedImage newResult, int status) {
            boolean isTracing;
            try {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.beginSection("EncodedMemoryCacheProducer#onNewResultImpl");
                }
                if (!isNotLast(status) && newResult != null && !statusHasAnyFlag(status, 10) && newResult.getImageFormat() != ImageFormat.UNKNOWN) {
                    CloseableReference<PooledByteBuffer> ref = newResult.getByteBufferRef();
                    if (ref != null) {
                        CloseableReference<PooledByteBuffer> cachedResult = null;
                        if (this.mIsMemoryCacheEnabled) {
                            cachedResult = this.mMemoryCache.cache(this.mRequestedCacheKey, ref);
                        }
                        CloseableReference.closeSafely(ref);
                        if (cachedResult != null) {
                            EncodedImage cachedEncodedImage = new EncodedImage(cachedResult);
                            cachedEncodedImage.copyMetaDataFrom(newResult);
                            CloseableReference.closeSafely(cachedResult);
                            getConsumer().onProgressUpdate(1.0f);
                            getConsumer().onNewResult(cachedEncodedImage, status);
                            EncodedImage.closeSafely(cachedEncodedImage);
                            if (!isTracing) {
                                return;
                            }
                            return;
                        }
                    }
                    getConsumer().onNewResult(newResult, status);
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.endSection();
                        return;
                    }
                    return;
                }
                getConsumer().onNewResult(newResult, status);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            } finally {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            }
        }
    }
}
