package com.facebook.imagepipeline.cache;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.image.CloseableImage;
/* loaded from: classes.dex */
public class BitmapCountingMemoryCacheFactory {
    public static CountingMemoryCache<CacheKey, CloseableImage> get(Supplier<MemoryCacheParams> bitmapMemoryCacheParamsSupplier, MemoryTrimmableRegistry memoryTrimmableRegistry) {
        return get(bitmapMemoryCacheParamsSupplier, memoryTrimmableRegistry, new BitmapMemoryCacheTrimStrategy());
    }

    public static CountingMemoryCache<CacheKey, CloseableImage> get(Supplier<MemoryCacheParams> bitmapMemoryCacheParamsSupplier, MemoryTrimmableRegistry memoryTrimmableRegistry, CountingMemoryCache.CacheTrimStrategy trimStrategy) {
        CountingMemoryCache<CacheKey, CloseableImage> countingCache = new CountingMemoryCache<>(new ValueDescriptor<CloseableImage>() { // from class: com.facebook.imagepipeline.cache.BitmapCountingMemoryCacheFactory.1
            public int getSizeInBytes(CloseableImage value) {
                return value.getSizeInBytes();
            }
        }, trimStrategy, bitmapMemoryCacheParamsSupplier);
        memoryTrimmableRegistry.registerMemoryTrimmable(countingCache);
        return countingCache;
    }
}
