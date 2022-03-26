package com.facebook.imagepipeline.memory;

import com.facebook.common.memory.MemoryTrimmableRegistry;
/* loaded from: classes.dex */
public class BufferMemoryChunkPool extends MemoryChunkPool {
    public BufferMemoryChunkPool(MemoryTrimmableRegistry memoryTrimmableRegistry, PoolParams poolParams, PoolStatsTracker bufferMemoryChunkPoolStatsTracker) {
        super(memoryTrimmableRegistry, poolParams, bufferMemoryChunkPoolStatsTracker);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.MemoryChunkPool, com.facebook.imagepipeline.memory.BasePool
    /* renamed from: alloc */
    public MemoryChunk alloc2(int bucketedSize) {
        return new BufferMemoryChunk(bucketedSize);
    }
}
