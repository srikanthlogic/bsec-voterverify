package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfm extends zzjz<zzfm, zzfl> implements zzlh {
    private static final zzfm zza;
    private int zze;
    private int zzf;
    private long zzg;

    static {
        zzfm zzfm = new zzfm();
        zza = zzfm;
        zzjz.zzbG(zzfm.class, zzfm);
    }

    private zzfm() {
    }

    public static zzfl zzc() {
        return zza.zzbu();
    }

    public static /* synthetic */ void zze(zzfm zzfm, int i) {
        zzfm.zze |= 1;
        zzfm.zzf = i;
    }

    public static /* synthetic */ void zzf(zzfm zzfm, long j) {
        zzfm.zze |= 2;
        zzfm.zzg = j;
    }

    public final int zza() {
        return this.zzf;
    }

    public final long zzb() {
        return this.zzg;
    }

    public final boolean zzg() {
        return (this.zze & 2) != 0;
    }

    public final boolean zzh() {
        return (this.zze & 1) != 0;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001င\u0000\u0002ဂ\u0001", new Object[]{"zze", "zzf", "zzg"});
        }
        if (i2 == 3) {
            return new zzfm();
        }
        if (i2 == 4) {
            return new zzfl(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
