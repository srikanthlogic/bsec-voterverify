package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfu extends zzjz<zzfu, zzft> implements zzlh {
    private static final zzfu zza;
    private int zze;
    private String zzf = "";
    private String zzg = "";
    private zzfi zzh;

    static {
        zzfu zzfu = new zzfu();
        zza = zzfu;
        zzjz.zzbG(zzfu.class, zzfu);
    }

    private zzfu() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001\u0003ဉ\u0002", new Object[]{"zze", "zzf", "zzg", "zzh"});
        }
        if (i2 == 3) {
            return new zzfu();
        }
        if (i2 == 4) {
            return new zzft(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
