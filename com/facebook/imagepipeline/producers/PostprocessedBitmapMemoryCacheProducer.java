package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.Postprocessor;
import com.facebook.imagepipeline.request.RepeatedPostprocessor;
import java.util.Map;
/* loaded from: classes.dex */
public class PostprocessedBitmapMemoryCacheProducer implements Producer<CloseableReference<CloseableImage>> {
    public static final String PRODUCER_NAME = "PostprocessedBitmapMemoryCacheProducer";
    static final String VALUE_FOUND = "cached_value_found";
    private final CacheKeyFactory mCacheKeyFactory;
    private final Producer<CloseableReference<CloseableImage>> mInputProducer;
    private final MemoryCache<CacheKey, CloseableImage> mMemoryCache;

    public PostprocessedBitmapMemoryCacheProducer(MemoryCache<CacheKey, CloseableImage> memoryCache, CacheKeyFactory cacheKeyFactory, Producer<CloseableReference<CloseableImage>> inputProducer) {
        this.mMemoryCache = memoryCache;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext producerContext) {
        ProducerListener listener = producerContext.getListener();
        String requestId = producerContext.getId();
        ImageRequest imageRequest = producerContext.getImageRequest();
        Object callerContext = producerContext.getCallerContext();
        Postprocessor postprocessor = imageRequest.getPostprocessor();
        if (!(postprocessor == null || postprocessor.getPostprocessorCacheKey() == null)) {
            listener.onProducerStart(requestId, getProducerName());
            CacheKey cacheKey = this.mCacheKeyFactory.getPostprocessedBitmapCacheKey(imageRequest, callerContext);
            CloseableReference<CloseableImage> cachedReference = this.mMemoryCache.get(cacheKey);
            Map<String, String> map = null;
            if (cachedReference != null) {
                String producerName = getProducerName();
                if (listener.requiresExtraMap(requestId)) {
                    map = ImmutableMap.of("cached_value_found", "true");
                }
                listener.onProducerFinishWithSuccess(requestId, producerName, map);
                listener.onUltimateProducerReached(requestId, PRODUCER_NAME, true);
                consumer.onProgressUpdate(1.0f);
                consumer.onNewResult(cachedReference, 1);
                cachedReference.close();
                return;
            }
            Consumer<CloseableReference<CloseableImage>> cachedConsumer = new CachedPostprocessorConsumer(consumer, cacheKey, postprocessor instanceof RepeatedPostprocessor, this.mMemoryCache, producerContext.getImageRequest().isMemoryCacheEnabled());
            String producerName2 = getProducerName();
            if (listener.requiresExtraMap(requestId)) {
                map = ImmutableMap.of("cached_value_found", "false");
            }
            listener.onProducerFinishWithSuccess(requestId, producerName2, map);
            this.mInputProducer.produceResults(cachedConsumer, producerContext);
            return;
        }
        this.mInputProducer.produceResults(consumer, producerContext);
    }

    /* loaded from: classes.dex */
    public static class CachedPostprocessorConsumer extends DelegatingConsumer<CloseableReference<CloseableImage>, CloseableReference<CloseableImage>> {
        private final CacheKey mCacheKey;
        private final boolean mIsMemoryCachedEnabled;
        private final boolean mIsRepeatedProcessor;
        private final MemoryCache<CacheKey, CloseableImage> mMemoryCache;

        public CachedPostprocessorConsumer(Consumer<CloseableReference<CloseableImage>> consumer, CacheKey cacheKey, boolean isRepeatedProcessor, MemoryCache<CacheKey, CloseableImage> memoryCache, boolean isMemoryCachedEnabled) {
            super(consumer);
            this.mCacheKey = cacheKey;
            this.mIsRepeatedProcessor = isRepeatedProcessor;
            this.mMemoryCache = memoryCache;
            this.mIsMemoryCachedEnabled = isMemoryCachedEnabled;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNewResultImpl(CloseableReference<CloseableImage> newResult, int status) {
            if (newResult == null) {
                if (isLast(status)) {
                    getConsumer().onNewResult(null, status);
                }
            } else if (!isNotLast(status) || this.mIsRepeatedProcessor) {
                CloseableReference<CloseableImage> newCachedResult = null;
                if (this.mIsMemoryCachedEnabled) {
                    newCachedResult = this.mMemoryCache.cache(this.mCacheKey, newResult);
                }
                try {
                    getConsumer().onProgressUpdate(1.0f);
                    getConsumer().onNewResult(newCachedResult != null ? newCachedResult : newResult, status);
                } finally {
                    CloseableReference.closeSafely(newCachedResult);
                }
            }
        }
    }

    protected String getProducerName() {
        return PRODUCER_NAME;
    }
}
