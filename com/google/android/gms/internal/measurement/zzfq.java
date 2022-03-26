package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfq extends zzjz<zzfq, zzfp> implements zzlh {
    private static final zzfq zza;
    private int zze;
    private String zzf = "";
    private long zzg;

    static {
        zzfq zzfq = new zzfq();
        zza = zzfq;
        zzjz.zzbG(zzfq.class, zzfq);
    }

    private zzfq() {
    }

    public static zzfp zza() {
        return zza.zzbu();
    }

    public static /* synthetic */ zzfq zzb() {
        return zza;
    }

    public static /* synthetic */ void zzc(zzfq zzfq, String str) {
        str.getClass();
        zzfq.zze |= 1;
        zzfq.zzf = str;
    }

    public static /* synthetic */ void zzd(zzfq zzfq, long j) {
        zzfq.zze |= 2;
        zzfq.zzg = j;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဂ\u0001", new Object[]{"zze", "zzf", "zzg"});
        }
        if (i2 == 3) {
            return new zzfq();
        }
        if (i2 == 4) {
            return new zzfp(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
