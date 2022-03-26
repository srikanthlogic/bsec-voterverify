package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfk extends zzjz<zzfk, zzfj> implements zzlh {
    private static final zzfk zza;
    private int zze;
    private int zzf;
    private zzgd zzg;
    private zzgd zzh;
    private boolean zzi;

    static {
        zzfk zzfk = new zzfk();
        zza = zzfk;
        zzjz.zzbG(zzfk.class, zzfk);
    }

    private zzfk() {
    }

    public static zzfj zzb() {
        return zza.zzbu();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzf(zzfk zzfk, int i) {
        zzfk.zze |= 1;
        zzfk.zzf = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzg(zzfk zzfk, zzgd zzgd) {
        zzgd.getClass();
        zzfk.zzg = zzgd;
        zzfk.zze |= 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzh(zzfk zzfk, zzgd zzgd) {
        zzfk.zzh = zzgd;
        zzfk.zze |= 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzi(zzfk zzfk, boolean z) {
        zzfk.zze |= 8;
        zzfk.zzi = z;
    }

    public final int zza() {
        return this.zzf;
    }

    public final zzgd zzd() {
        zzgd zzgd = this.zzg;
        return zzgd == null ? zzgd.zzh() : zzgd;
    }

    public final zzgd zze() {
        zzgd zzgd = this.zzh;
        return zzgd == null ? zzgd.zzh() : zzgd;
    }

    public final boolean zzj() {
        return this.zzi;
    }

    public final boolean zzk() {
        return (this.zze & 1) != 0;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    protected final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001င\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004ဇ\u0003", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi"});
        }
        if (i2 == 3) {
            return new zzfk();
        }
        if (i2 == 4) {
            return new zzfj(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }

    public final boolean zzm() {
        return (this.zze & 8) != 0;
    }

    public final boolean zzn() {
        return (this.zze & 4) != 0;
    }
}
