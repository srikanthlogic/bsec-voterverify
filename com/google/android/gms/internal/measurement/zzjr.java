package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public enum zzjr {
    DOUBLE(0, 1, zzkk.DOUBLE),
    FLOAT(1, 1, zzkk.FLOAT),
    INT64(2, 1, zzkk.LONG),
    UINT64(3, 1, zzkk.LONG),
    INT32(4, 1, zzkk.INT),
    FIXED64(5, 1, zzkk.LONG),
    FIXED32(6, 1, zzkk.INT),
    BOOL(7, 1, zzkk.BOOLEAN),
    STRING(8, 1, zzkk.STRING),
    MESSAGE(9, 1, zzkk.MESSAGE),
    BYTES(10, 1, zzkk.BYTE_STRING),
    UINT32(11, 1, zzkk.INT),
    ENUM(12, 1, zzkk.ENUM),
    SFIXED32(13, 1, zzkk.INT),
    SFIXED64(14, 1, zzkk.LONG),
    SINT32(15, 1, zzkk.INT),
    SINT64(16, 1, zzkk.LONG),
    GROUP(17, 1, zzkk.MESSAGE),
    DOUBLE_LIST(18, 2, zzkk.DOUBLE),
    FLOAT_LIST(19, 2, zzkk.FLOAT),
    INT64_LIST(20, 2, zzkk.LONG),
    UINT64_LIST(21, 2, zzkk.LONG),
    INT32_LIST(22, 2, zzkk.INT),
    FIXED64_LIST(23, 2, zzkk.LONG),
    FIXED32_LIST(24, 2, zzkk.INT),
    BOOL_LIST(25, 2, zzkk.BOOLEAN),
    STRING_LIST(26, 2, zzkk.STRING),
    MESSAGE_LIST(27, 2, zzkk.MESSAGE),
    BYTES_LIST(28, 2, zzkk.BYTE_STRING),
    UINT32_LIST(29, 2, zzkk.INT),
    ENUM_LIST(30, 2, zzkk.ENUM),
    SFIXED32_LIST(31, 2, zzkk.INT),
    SFIXED64_LIST(32, 2, zzkk.LONG),
    SINT32_LIST(33, 2, zzkk.INT),
    SINT64_LIST(34, 2, zzkk.LONG),
    DOUBLE_LIST_PACKED(35, 3, zzkk.DOUBLE),
    FLOAT_LIST_PACKED(36, 3, zzkk.FLOAT),
    INT64_LIST_PACKED(37, 3, zzkk.LONG),
    UINT64_LIST_PACKED(38, 3, zzkk.LONG),
    INT32_LIST_PACKED(39, 3, zzkk.INT),
    FIXED64_LIST_PACKED(40, 3, zzkk.LONG),
    FIXED32_LIST_PACKED(41, 3, zzkk.INT),
    BOOL_LIST_PACKED(42, 3, zzkk.BOOLEAN),
    UINT32_LIST_PACKED(43, 3, zzkk.INT),
    ENUM_LIST_PACKED(44, 3, zzkk.ENUM),
    SFIXED32_LIST_PACKED(45, 3, zzkk.INT),
    SFIXED64_LIST_PACKED(46, 3, zzkk.LONG),
    SINT32_LIST_PACKED(47, 3, zzkk.INT),
    SINT64_LIST_PACKED(48, 3, zzkk.LONG),
    GROUP_LIST(49, 2, zzkk.MESSAGE),
    MAP(50, 4, zzkk.VOID);
    
    private static final zzjr[] zzZ;
    private final zzkk zzab;
    private final int zzac;
    private final Class<?> zzad;

    static {
        zzjr[] values = values();
        int length = values.length;
        zzZ = new zzjr[length];
        for (zzjr zzjr : values) {
            zzZ[zzjr.zzac] = zzjr;
        }
    }

    zzjr(int i, int i2, zzkk zzkk) {
        this.zzac = i;
        this.zzab = zzkk;
        zzkk zzkk2 = zzkk.VOID;
        int i3 = i2 - 1;
        if (i3 == 1) {
            this.zzad = zzkk.zza();
        } else if (i3 != 3) {
            this.zzad = null;
        } else {
            this.zzad = zzkk.zza();
        }
        if (i2 == 1) {
            zzkk.ordinal();
        }
    }

    public final int zza() {
        return this.zzac;
    }
}
