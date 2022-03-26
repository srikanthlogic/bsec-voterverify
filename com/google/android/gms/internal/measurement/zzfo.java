package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfo extends zzjz<zzfo, zzfn> implements zzlh {
    private static final zzfo zza;
    private int zze;
    private zzkg<zzfs> zzf = zzbA();
    private String zzg = "";
    private long zzh;
    private long zzi;
    private int zzj;

    static {
        zzfo zzfo = new zzfo();
        zza = zzfo;
        zzjz.zzbG(zzfo.class, zzfo);
    }

    private zzfo() {
    }

    public static zzfn zze() {
        return zza.zzbu();
    }

    public static /* synthetic */ void zzj(zzfo zzfo, int i, zzfs zzfs) {
        zzfs.getClass();
        zzfo.zzv();
        zzfo.zzf.set(i, zzfs);
    }

    public static /* synthetic */ void zzk(zzfo zzfo, zzfs zzfs) {
        zzfs.getClass();
        zzfo.zzv();
        zzfo.zzf.add(zzfs);
    }

    public static /* synthetic */ void zzm(zzfo zzfo, Iterable iterable) {
        zzfo.zzv();
        zzih.zzbq(iterable, zzfo.zzf);
    }

    public static /* synthetic */ void zzo(zzfo zzfo, int i) {
        zzfo.zzv();
        zzfo.zzf.remove(i);
    }

    public static /* synthetic */ void zzp(zzfo zzfo, String str) {
        str.getClass();
        zzfo.zze |= 1;
        zzfo.zzg = str;
    }

    public static /* synthetic */ void zzq(zzfo zzfo, long j) {
        zzfo.zze |= 2;
        zzfo.zzh = j;
    }

    public static /* synthetic */ void zzr(zzfo zzfo, long j) {
        zzfo.zze |= 4;
        zzfo.zzi = j;
    }

    private final void zzv() {
        zzkg<zzfs> zzkg = this.zzf;
        if (!zzkg.zzc()) {
            this.zzf = zzjz.zzbB(zzkg);
        }
    }

    public final int zza() {
        return this.zzj;
    }

    public final int zzb() {
        return this.zzf.size();
    }

    public final long zzc() {
        return this.zzi;
    }

    public final long zzd() {
        return this.zzh;
    }

    public final zzfs zzg(int i) {
        return this.zzf.get(i);
    }

    public final String zzh() {
        return this.zzg;
    }

    public final List<zzfs> zzi() {
        return this.zzf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0001\u0000\u0001\u001b\u0002ဈ\u0000\u0003ဂ\u0001\u0004ဂ\u0002\u0005င\u0003", new Object[]{"zze", "zzf", zzfs.class, "zzg", "zzh", "zzi", "zzj"});
        }
        if (i2 == 3) {
            return new zzfo();
        }
        if (i2 == 4) {
            return new zzfn(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }

    public final boolean zzs() {
        return (this.zze & 8) != 0;
    }

    public final boolean zzt() {
        return (this.zze & 4) != 0;
    }

    public final boolean zzu() {
        return (this.zze & 2) != 0;
    }
}
