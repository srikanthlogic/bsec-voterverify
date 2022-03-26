package com.facebook.imagepipeline.memory;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class BucketsBitmapPool extends BasePool<Bitmap> implements BitmapPool {
    public BucketsBitmapPool(MemoryTrimmableRegistry memoryTrimmableRegistry, PoolParams poolParams, PoolStatsTracker poolStatsTracker) {
        super(memoryTrimmableRegistry, poolParams, poolStatsTracker);
        initialize();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.facebook.imagepipeline.memory.BasePool
    public Bitmap alloc(int size) {
        return Bitmap.createBitmap(1, (int) Math.ceil(((double) size) / 2.0d), Bitmap.Config.RGB_565);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void free(Bitmap value) {
        Preconditions.checkNotNull(value);
        value.recycle();
    }

    @Override // com.facebook.imagepipeline.memory.BasePool
    protected int getBucketedSize(int requestSize) {
        return requestSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getBucketedSizeForValue(Bitmap value) {
        Preconditions.checkNotNull(value);
        return value.getAllocationByteCount();
    }

    @Override // com.facebook.imagepipeline.memory.BasePool
    protected int getSizeInBytes(int bucketedSize) {
        return bucketedSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isReusable(Bitmap value) {
        Preconditions.checkNotNull(value);
        return !value.isRecycled() && value.isMutable();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.facebook.imagepipeline.memory.BasePool
    @Nullable
    public Bitmap getValue(Bucket<Bitmap> bucket) {
        Bitmap result = (Bitmap) super.getValue((Bucket<Object>) bucket);
        if (result != null) {
            result.eraseColor(0);
        }
        return result;
    }
}
