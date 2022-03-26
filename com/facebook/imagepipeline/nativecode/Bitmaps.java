package com.facebook.imagepipeline.nativecode;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
/* loaded from: classes.dex */
public class Bitmaps {
    private static native void nativeCopyBitmap(Bitmap bitmap, int i, Bitmap bitmap2, int i2, int i3);

    static {
        ImagePipelineNativeLoader.load();
    }

    public static void copyBitmap(Bitmap dest, Bitmap src) {
        boolean z = true;
        Preconditions.checkArgument(src.getConfig() == dest.getConfig());
        Preconditions.checkArgument(dest.isMutable());
        Preconditions.checkArgument(dest.getWidth() == src.getWidth());
        if (dest.getHeight() != src.getHeight()) {
            z = false;
        }
        Preconditions.checkArgument(z);
        nativeCopyBitmap(dest, dest.getRowBytes(), src, src.getRowBytes(), dest.getHeight());
    }
}
