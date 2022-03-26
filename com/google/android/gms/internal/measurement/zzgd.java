package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgd extends zzjz<zzgd, zzgc> implements zzlh {
    private static final zzgd zza;
    private zzkf zze = zzby();
    private zzkf zzf = zzby();
    private zzkg<zzfm> zzg = zzbA();
    private zzkg<zzgf> zzh = zzbA();

    static {
        zzgd zzgd = new zzgd();
        zza = zzgd;
        zzjz.zzbG(zzgd.class, zzgd);
    }

    private zzgd() {
    }

    public static zzgc zzf() {
        return zza.zzbu();
    }

    public static zzgd zzh() {
        return zza;
    }

    public static /* synthetic */ void zzo(zzgd zzgd, Iterable iterable) {
        zzkf zzkf = zzgd.zze;
        if (!zzkf.zzc()) {
            zzgd.zze = zzjz.zzbz(zzkf);
        }
        zzih.zzbq(iterable, zzgd.zze);
    }

    public static /* synthetic */ void zzq(zzgd zzgd, Iterable iterable) {
        zzkf zzkf = zzgd.zzf;
        if (!zzkf.zzc()) {
            zzgd.zzf = zzjz.zzbz(zzkf);
        }
        zzih.zzbq(iterable, zzgd.zzf);
    }

    public static /* synthetic */ void zzs(zzgd zzgd, Iterable iterable) {
        zzgd.zzw();
        zzih.zzbq(iterable, zzgd.zzg);
    }

    public static /* synthetic */ void zzt(zzgd zzgd, int i) {
        zzgd.zzw();
        zzgd.zzg.remove(i);
    }

    public static /* synthetic */ void zzu(zzgd zzgd, Iterable iterable) {
        zzgd.zzx();
        zzih.zzbq(iterable, zzgd.zzh);
    }

    public static /* synthetic */ void zzv(zzgd zzgd, int i) {
        zzgd.zzx();
        zzgd.zzh.remove(i);
    }

    private final void zzw() {
        zzkg<zzfm> zzkg = this.zzg;
        if (!zzkg.zzc()) {
            this.zzg = zzjz.zzbB(zzkg);
        }
    }

    private final void zzx() {
        zzkg<zzgf> zzkg = this.zzh;
        if (!zzkg.zzc()) {
            this.zzh = zzjz.zzbB(zzkg);
        }
    }

    public final int zza() {
        return this.zzg.size();
    }

    public final int zzb() {
        return this.zzf.size();
    }

    public final int zzc() {
        return this.zzh.size();
    }

    public final int zzd() {
        return this.zze.size();
    }

    public final zzfm zze(int i) {
        return this.zzg.get(i);
    }

    public final zzgf zzi(int i) {
        return this.zzh.get(i);
    }

    public final List<zzfm> zzj() {
        return this.zzg;
    }

    public final List<Long> zzk() {
        return this.zzf;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0004\u0000\u0001\u0015\u0002\u0015\u0003\u001b\u0004\u001b", new Object[]{"zze", "zzf", "zzg", zzfm.class, "zzh", zzgf.class});
        }
        if (i2 == 3) {
            return new zzgd();
        }
        if (i2 == 4) {
            return new zzgc(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }

    public final List<zzgf> zzm() {
        return this.zzh;
    }

    public final List<Long> zzn() {
        return this.zze;
    }
}
