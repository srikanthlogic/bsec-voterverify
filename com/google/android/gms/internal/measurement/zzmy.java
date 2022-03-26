package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public enum zzmy {
    DOUBLE(zzmz.DOUBLE, 1),
    FLOAT(zzmz.FLOAT, 5),
    INT64(zzmz.LONG, 0),
    UINT64(zzmz.LONG, 0),
    INT32(zzmz.INT, 0),
    FIXED64(zzmz.LONG, 1),
    FIXED32(zzmz.INT, 5),
    BOOL(zzmz.BOOLEAN, 0),
    STRING(zzmz.STRING, 2),
    GROUP(zzmz.MESSAGE, 3),
    MESSAGE(zzmz.MESSAGE, 2),
    BYTES(zzmz.BYTE_STRING, 2),
    UINT32(zzmz.INT, 0),
    ENUM(zzmz.ENUM, 0),
    SFIXED32(zzmz.INT, 5),
    SFIXED64(zzmz.LONG, 1),
    SINT32(zzmz.INT, 0),
    SINT64(zzmz.LONG, 0);
    
    private final zzmz zzt;

    zzmy(zzmz zzmz, int i) {
        this.zzt = zzmz;
    }

    public final zzmz zza() {
        return this.zzt;
    }
}
