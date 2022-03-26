package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import java.util.Map;
/* loaded from: classes.dex */
public class BitmapMemoryCacheProducer implements Producer<CloseableReference<CloseableImage>> {
    public static final String EXTRA_CACHED_VALUE_FOUND = "cached_value_found";
    public static final String PRODUCER_NAME = "BitmapMemoryCacheProducer";
    private final CacheKeyFactory mCacheKeyFactory;
    private final Producer<CloseableReference<CloseableImage>> mInputProducer;
    private final MemoryCache<CacheKey, CloseableImage> mMemoryCache;

    public BitmapMemoryCacheProducer(MemoryCache<CacheKey, CloseableImage> memoryCache, CacheKeyFactory cacheKeyFactory, Producer<CloseableReference<CloseableImage>> inputProducer) {
        this.mMemoryCache = memoryCache;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext producerContext) {
        boolean isTracing;
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("BitmapMemoryCacheProducer#produceResults");
            }
            ProducerListener listener = producerContext.getListener();
            String requestId = producerContext.getId();
            listener.onProducerStart(requestId, getProducerName());
            CacheKey cacheKey = this.mCacheKeyFactory.getBitmapCacheKey(producerContext.getImageRequest(), producerContext.getCallerContext());
            CloseableReference<CloseableImage> cachedReference = this.mMemoryCache.get(cacheKey);
            Map<String, String> map = null;
            if (cachedReference != null) {
                boolean isFinal = cachedReference.get().getQualityInfo().isOfFullQuality();
                if (isFinal) {
                    listener.onProducerFinishWithSuccess(requestId, getProducerName(), listener.requiresExtraMap(requestId) ? ImmutableMap.of("cached_value_found", "true") : null);
                    listener.onUltimateProducerReached(requestId, getProducerName(), true);
                    consumer.onProgressUpdate(1.0f);
                }
                consumer.onNewResult(cachedReference, BaseConsumer.simpleStatusForIsLast(isFinal));
                cachedReference.close();
                if (isFinal) {
                    if (!isTracing) {
                        return;
                    }
                    return;
                }
            }
            if (producerContext.getLowestPermittedRequestLevel().getValue() >= ImageRequest.RequestLevel.BITMAP_MEMORY_CACHE.getValue()) {
                listener.onProducerFinishWithSuccess(requestId, getProducerName(), listener.requiresExtraMap(requestId) ? ImmutableMap.of("cached_value_found", "false") : null);
                listener.onUltimateProducerReached(requestId, getProducerName(), false);
                consumer.onNewResult(null, 1);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                    return;
                }
                return;
            }
            Consumer<CloseableReference<CloseableImage>> wrappedConsumer = wrapConsumer(consumer, cacheKey, producerContext.getImageRequest().isMemoryCacheEnabled());
            String producerName = getProducerName();
            if (listener.requiresExtraMap(requestId)) {
                map = ImmutableMap.of("cached_value_found", "false");
            }
            listener.onProducerFinishWithSuccess(requestId, producerName, map);
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("mInputProducer.produceResult");
            }
            this.mInputProducer.produceResults(wrappedConsumer, producerContext);
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    protected Consumer<CloseableReference<CloseableImage>> wrapConsumer(Consumer<CloseableReference<CloseableImage>> consumer, final CacheKey cacheKey, final boolean isMemoryCacheEnabled) {
        return new DelegatingConsumer<CloseableReference<CloseableImage>, CloseableReference<CloseableImage>>(consumer) { // from class: com.facebook.imagepipeline.producers.BitmapMemoryCacheProducer.1
            public void onNewResultImpl(CloseableReference<CloseableImage> newResult, int status) {
                CloseableReference<CloseableImage> currentCachedResult;
                boolean isTracing;
                try {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.beginSection("BitmapMemoryCacheProducer#onNewResultImpl");
                    }
                    boolean isLast = isLast(status);
                    if (newResult == null) {
                        if (isLast) {
                            getConsumer().onNewResult(null, status);
                        }
                        if (!isTracing) {
                            return;
                        }
                        return;
                    }
                    if (!newResult.get().isStateful() && !statusHasFlag(status, 8)) {
                        if (!isLast && (currentCachedResult = BitmapMemoryCacheProducer.this.mMemoryCache.get(cacheKey)) != null) {
                            QualityInfo newInfo = newResult.get().getQualityInfo();
                            QualityInfo cachedInfo = currentCachedResult.get().getQualityInfo();
                            if (cachedInfo.isOfFullQuality() || cachedInfo.getQuality() >= newInfo.getQuality()) {
                                getConsumer().onNewResult(currentCachedResult, status);
                                CloseableReference.closeSafely(currentCachedResult);
                                if (FrescoSystrace.isTracing()) {
                                    FrescoSystrace.endSection();
                                    return;
                                }
                                return;
                            }
                            CloseableReference.closeSafely(currentCachedResult);
                        }
                        CloseableReference<CloseableImage> newCachedResult = null;
                        if (isMemoryCacheEnabled) {
                            newCachedResult = BitmapMemoryCacheProducer.this.mMemoryCache.cache(cacheKey, newResult);
                        }
                        if (isLast) {
                            getConsumer().onProgressUpdate(1.0f);
                        }
                        getConsumer().onNewResult(newCachedResult != null ? newCachedResult : newResult, status);
                        CloseableReference.closeSafely(newCachedResult);
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
        };
    }

    protected String getProducerName() {
        return PRODUCER_NAME;
    }
}
