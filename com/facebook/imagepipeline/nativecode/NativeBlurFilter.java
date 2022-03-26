package com.facebook.imagepipeline.nativecode;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
/* loaded from: classes.dex */
public class NativeBlurFilter {
    private static native void nativeIterativeBoxBlur(Bitmap bitmap, int i, int i2);

    static {
        NativeFiltersLoader.load();
    }

    public static void iterativeBoxBlur(Bitmap bitmap, int iterations, int blurRadius) {
        Preconditions.checkNotNull(bitmap);
        boolean z = true;
        Preconditions.checkArgument(iterations > 0);
        if (blurRadius <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        nativeIterativeBoxBlur(bitmap, iterations, blurRadius);
    }
}
