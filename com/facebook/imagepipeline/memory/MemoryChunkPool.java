package com.facebook.imagepipeline.memory;

import android.util.SparseIntArray;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.imagepipeline.memory.BasePool;
/* loaded from: classes.dex */
public abstract class MemoryChunkPool extends BasePool<MemoryChunk> {
    private final int[] mBucketSizes;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.facebook.imagepipeline.memory.BasePool
    public abstract MemoryChunk alloc(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public MemoryChunkPool(MemoryTrimmableRegistry memoryTrimmableRegistry, PoolParams poolParams, PoolStatsTracker memoryChunkPoolStatsTracker) {
        super(memoryTrimmableRegistry, poolParams, memoryChunkPoolStatsTracker);
        SparseIntArray bucketSizes = poolParams.bucketSizes;
        this.mBucketSizes = new int[bucketSizes.size()];
        int i = 0;
        while (true) {
            int[] iArr = this.mBucketSizes;
            if (i < iArr.length) {
                iArr[i] = bucketSizes.keyAt(i);
                i++;
            } else {
                initialize();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMinBufferSize() {
        return this.mBucketSizes[0];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void free(MemoryChunk value) {
        Preconditions.checkNotNull(value);
        value.close();
    }

    @Override // com.facebook.imagepipeline.memory.BasePool
    protected int getSizeInBytes(int bucketedSize) {
        return bucketedSize;
    }

    @Override // com.facebook.imagepipeline.memory.BasePool
    protected int getBucketedSize(int requestSize) {
        if (requestSize > 0) {
            int[] iArr = this.mBucketSizes;
            for (int bucketedSize : iArr) {
                if (bucketedSize >= requestSize) {
                    return bucketedSize;
                }
            }
            return requestSize;
        }
        throw new BasePool.InvalidSizeException(Integer.valueOf(requestSize));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getBucketedSizeForValue(MemoryChunk value) {
        Preconditions.checkNotNull(value);
        return value.getSize();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isReusable(MemoryChunk value) {
        Preconditions.checkNotNull(value);
        return !value.isClosed();
    }
}
