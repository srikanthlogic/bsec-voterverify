package com.facebook.imagepipeline.memory;

import com.facebook.common.memory.MemoryTrimmableRegistry;
/* loaded from: classes.dex */
public class NativeMemoryChunkPool extends MemoryChunkPool {
    public NativeMemoryChunkPool(MemoryTrimmableRegistry memoryTrimmableRegistry, PoolParams poolParams, PoolStatsTracker nativeMemoryChunkPoolStatsTracker) {
        super(memoryTrimmableRegistry, poolParams, nativeMemoryChunkPoolStatsTracker);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.MemoryChunkPool, com.facebook.imagepipeline.memory.BasePool
    /* renamed from: alloc */
    public MemoryChunk alloc2(int bucketedSize) {
        return new NativeMemoryChunk(bucketedSize);
    }
}
