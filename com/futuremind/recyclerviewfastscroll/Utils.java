package com.futuremind.recyclerviewfastscroll;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
/* loaded from: classes.dex */
public class Utils {
    public static float getViewRawY(View view) {
        int[] location = {0, (int) view.getY()};
        ((View) view.getParent()).getLocationInWindow(location);
        return (float) location[1];
    }

    public static float getViewRawX(View view) {
        int[] location = {(int) view.getX(), 0};
        ((View) view.getParent()).getLocationInWindow(location);
        return (float) location[0];
    }

    public static float getValueInRange(float min, float max, float value) {
        return Math.min(Math.max(min, value), max);
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
