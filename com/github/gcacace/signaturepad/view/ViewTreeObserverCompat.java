package com.github.gcacace.signaturepad.view;

import android.os.Build;
import android.view.ViewTreeObserver;
/* loaded from: classes.dex */
public class ViewTreeObserverCompat {
    public static void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (Build.VERSION.SDK_INT >= 16) {
            observer.removeOnGlobalLayoutListener(victim);
        } else {
            observer.removeGlobalOnLayoutListener(victim);
        }
    }
}
