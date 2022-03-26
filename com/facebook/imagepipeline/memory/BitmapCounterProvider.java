package com.facebook.imagepipeline.memory;
/* loaded from: classes.dex */
public class BitmapCounterProvider {
    private static final long KB = 1024;
    private static final long MB = 1048576;
    private static volatile BitmapCounter sBitmapCounter;
    private static final Class<?> TAG = BitmapCounterProvider.class;
    public static final int MAX_BITMAP_TOTAL_SIZE = getMaxSizeHardCap();
    private static int sMaxBitmapCount = BitmapCounterConfig.DEFAULT_MAX_BITMAP_COUNT;

    private static int getMaxSizeHardCap() {
        int maxMemory = (int) Math.min(Runtime.getRuntime().maxMemory(), 2147483647L);
        if (((long) maxMemory) > 16777216) {
            return (maxMemory / 4) * 3;
        }
        return maxMemory / 2;
    }

    public static void initialize(BitmapCounterConfig bitmapCounterConfig) {
        if (sBitmapCounter == null) {
            sMaxBitmapCount = bitmapCounterConfig.getMaxBitmapCount();
            return;
        }
        throw new IllegalStateException("BitmapCounter has already been created! `BitmapCounterProvider.initialize(...)` should only be called before `BitmapCounterProvider.get()` or not at all!");
    }

    public static BitmapCounter get() {
        if (sBitmapCounter == null) {
            synchronized (BitmapCounterProvider.class) {
                if (sBitmapCounter == null) {
                    sBitmapCounter = new BitmapCounter(sMaxBitmapCount, MAX_BITMAP_TOTAL_SIZE);
                }
            }
        }
        return sBitmapCounter;
    }
}
