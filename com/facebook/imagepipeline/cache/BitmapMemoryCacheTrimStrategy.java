package com.facebook.imagepipeline.cache;

import android.os.Build;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
/* loaded from: classes.dex */
public class BitmapMemoryCacheTrimStrategy implements CountingMemoryCache.CacheTrimStrategy {
    private static final String TAG;

    /* renamed from: com.facebook.imagepipeline.cache.BitmapMemoryCacheTrimStrategy$1 */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$common$memory$MemoryTrimType = new int[MemoryTrimType.values().length];

        static {
            try {
                $SwitchMap$com$facebook$common$memory$MemoryTrimType[MemoryTrimType.OnCloseToDalvikHeapLimit.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$facebook$common$memory$MemoryTrimType[MemoryTrimType.OnAppBackgrounded.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$facebook$common$memory$MemoryTrimType[MemoryTrimType.OnSystemMemoryCriticallyLowWhileAppInForeground.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$facebook$common$memory$MemoryTrimType[MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$facebook$common$memory$MemoryTrimType[MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    @Override // com.facebook.imagepipeline.cache.CountingMemoryCache.CacheTrimStrategy
    public double getTrimRatio(MemoryTrimType trimType) {
        int i = AnonymousClass1.$SwitchMap$com$facebook$common$memory$MemoryTrimType[trimType.ordinal()];
        if (i != 1) {
            if (i == 2 || i == 3 || i == 4 || i == 5) {
                return 1.0d;
            }
            FLog.wtf(TAG, "unknown trim type: %s", trimType);
            return 0.0d;
        } else if (Build.VERSION.SDK_INT >= 21) {
            return MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio();
        } else {
            return 0.0d;
        }
    }
}
