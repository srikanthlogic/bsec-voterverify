package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.memory.BitmapPool;
import com.facebook.imageutils.BitmapUtil;
/* loaded from: classes.dex */
public class ArtBitmapFactory extends PlatformBitmapFactory {
    private final BitmapPool mBitmapPool;

    public ArtBitmapFactory(BitmapPool bitmapPool) {
        this.mBitmapPool = bitmapPool;
    }

    @Override // com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory
    public CloseableReference<Bitmap> createBitmapInternal(int width, int height, Bitmap.Config bitmapConfig) {
        Bitmap bitmap = this.mBitmapPool.get(BitmapUtil.getSizeInByteForBitmap(width, height, bitmapConfig));
        Preconditions.checkArgument(bitmap.getAllocationByteCount() >= (width * height) * BitmapUtil.getPixelSizeForBitmapConfig(bitmapConfig));
        bitmap.reconfigure(width, height, bitmapConfig);
        return CloseableReference.of(bitmap, this.mBitmapPool);
    }
}
