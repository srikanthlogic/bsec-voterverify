package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzes extends zzjz<zzes, zzer> implements zzlh {
    private static final zzes zza;
    private int zze;
    private int zzf;
    private String zzg = "";
    private zzel zzh;
    private boolean zzi;
    private boolean zzj;
    private boolean zzk;

    static {
        zzes zzes = new zzes();
        zza = zzes;
        zzjz.zzbG(zzes.class, zzes);
    }

    private zzes() {
    }

    public static zzer zzc() {
        return zza.zzbu();
    }

    public static /* synthetic */ zzes zzd() {
        return zza;
    }

    public static /* synthetic */ void zzf(zzes zzes, String str) {
        zzes.zze |= 2;
        zzes.zzg = str;
    }

    public final int zza() {
        return this.zzf;
    }

    public final zzel zzb() {
        zzel zzel = this.zzh;
        return zzel == null ? zzel.zzb() : zzel;
    }

    public final String zze() {
        return this.zzg;
    }

    public final boolean zzg() {
        return this.zzi;
    }

    public final boolean zzh() {
        return this.zzj;
    }

    public final boolean zzi() {
        return this.zzk;
    }

    public final boolean zzj() {
        return (this.zze & 1) != 0;
    }

    public final boolean zzk() {
        return (this.zze & 32) != 0;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001င\u0000\u0002ဈ\u0001\u0003ဉ\u0002\u0004ဇ\u0003\u0005ဇ\u0004\u0006ဇ\u0005", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk"});
        }
        if (i2 == 3) {
            return new zzes();
        }
        if (i2 == 4) {
            return new zzer(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
