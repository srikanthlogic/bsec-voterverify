package com.google.android.datatransport.cct.internal;

import android.util.SparseArray;
/* loaded from: classes.dex */
public enum QosTier {
    DEFAULT(0),
    UNMETERED_ONLY(1),
    UNMETERED_OR_DAILY(2),
    FAST_IF_RADIO_AWAKE(3),
    NEVER(4),
    UNRECOGNIZED(-1);
    
    private static final SparseArray<QosTier> valueMap = new SparseArray<>();
    private final int value;

    static {
        valueMap.put(0, DEFAULT);
        valueMap.put(1, UNMETERED_ONLY);
        valueMap.put(2, UNMETERED_OR_DAILY);
        valueMap.put(3, FAST_IF_RADIO_AWAKE);
        valueMap.put(4, NEVER);
        valueMap.put(-1, UNRECOGNIZED);
    }

    QosTier(int value) {
        this.value = value;
    }

    public final int getNumber() {
        return this.value;
    }

    public static QosTier forNumber(int value) {
        if (value == 0) {
            return DEFAULT;
        }
        if (value == 1) {
            return UNMETERED_ONLY;
        }
        if (value == 2) {
            return UNMETERED_OR_DAILY;
        }
        if (value == 3) {
            return FAST_IF_RADIO_AWAKE;
        }
        if (value != 4) {
            return null;
        }
        return NEVER;
    }
}
