package com.facebook.imagepipeline.nativecode;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
/* loaded from: classes.dex */
public class NativeRoundingFilter {
    private static native void nativeToCircleFilter(Bitmap bitmap, boolean z);

    private static native void nativeToCircleWithBorderFilter(Bitmap bitmap, int i, int i2, boolean z);

    static {
        NativeFiltersLoader.load();
    }

    public static void toCircle(Bitmap bitmap) {
        toCircle(bitmap, false);
    }

    public static void toCircle(Bitmap bitmap, boolean antiAliased) {
        Preconditions.checkNotNull(bitmap);
        nativeToCircleFilter(bitmap, antiAliased);
    }

    public static void toCircleWithBorder(Bitmap bitmap, int colorARGB, int borderWidthPx, boolean antiAliased) {
        Preconditions.checkNotNull(bitmap);
        nativeToCircleWithBorderFilter(bitmap, colorARGB, borderWidthPx, antiAliased);
    }
}
