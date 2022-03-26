package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.util.Pools;
import com.facebook.imagepipeline.memory.BitmapPool;
import com.facebook.imageutils.BitmapUtil;
/* loaded from: classes.dex */
public class OreoDecoder extends DefaultDecoder {
    public OreoDecoder(BitmapPool bitmapPool, int maxNumThreads, Pools.SynchronizedPool decodeBuffers) {
        super(bitmapPool, maxNumThreads, decodeBuffers);
    }

    @Override // com.facebook.imagepipeline.platform.DefaultDecoder
    public int getBitmapSize(int width, int height, BitmapFactory.Options options) {
        if (hasColorGamutMismatch(options)) {
            return width * height * 8;
        }
        return BitmapUtil.getSizeInByteForBitmap(width, height, options.inPreferredConfig);
    }

    private static boolean hasColorGamutMismatch(BitmapFactory.Options options) {
        return (options.outColorSpace == null || !options.outColorSpace.isWideGamut() || options.inPreferredConfig == Bitmap.Config.RGBA_F16) ? false : true;
    }
}
