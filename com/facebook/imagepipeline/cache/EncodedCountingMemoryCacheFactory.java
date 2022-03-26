package com.facebook.imagepipeline.cache;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.PooledByteBuffer;
/* loaded from: classes.dex */
public class EncodedCountingMemoryCacheFactory {
    public static CountingMemoryCache<CacheKey, PooledByteBuffer> get(Supplier<MemoryCacheParams> encodedMemoryCacheParamsSupplier, MemoryTrimmableRegistry memoryTrimmableRegistry) {
        CountingMemoryCache<CacheKey, PooledByteBuffer> countingCache = new CountingMemoryCache<>(new ValueDescriptor<PooledByteBuffer>() { // from class: com.facebook.imagepipeline.cache.EncodedCountingMemoryCacheFactory.1
            public int getSizeInBytes(PooledByteBuffer value) {
                return value.size();
            }
        }, new NativeMemoryCacheTrimStrategy(), encodedMemoryCacheParamsSupplier);
        memoryTrimmableRegistry.registerMemoryTrimmable(countingCache);
        return countingCache;
    }
}
