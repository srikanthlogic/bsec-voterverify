package com.facebook.drawee.view;

import android.view.View;
import android.view.ViewGroup;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class AspectRatioMeasure {

    /* loaded from: classes.dex */
    public static class Spec {
        public int height;
        public int width;
    }

    public static void updateMeasureSpec(Spec spec, float aspectRatio, @Nullable ViewGroup.LayoutParams layoutParams, int widthPadding, int heightPadding) {
        if (aspectRatio > 0.0f && layoutParams != null) {
            if (shouldAdjust(layoutParams.height)) {
                spec.height = View.MeasureSpec.makeMeasureSpec(View.resolveSize((int) ((((float) (View.MeasureSpec.getSize(spec.width) - widthPadding)) / aspectRatio) + ((float) heightPadding)), spec.height), 1073741824);
            } else if (shouldAdjust(layoutParams.width)) {
                spec.width = View.MeasureSpec.makeMeasureSpec(View.resolveSize((int) ((((float) (View.MeasureSpec.getSize(spec.height) - heightPadding)) * aspectRatio) + ((float) widthPadding)), spec.width), 1073741824);
            }
        }
    }

    private static boolean shouldAdjust(int layoutDimension) {
        return layoutDimension == 0 || layoutDimension == -2;
    }
}
