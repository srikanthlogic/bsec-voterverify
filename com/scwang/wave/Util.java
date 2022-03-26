package com.scwang.wave;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
/* loaded from: classes3.dex */
public class Util {
    public static int getColor(Context context, int colorId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColor(colorId);
        }
        return context.getResources().getColor(colorId);
    }

    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(1, dpVal, Resources.getSystem().getDisplayMetrics());
    }
}
