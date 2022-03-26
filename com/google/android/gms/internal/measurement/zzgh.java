package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgh extends zzjz<zzgh, zzgg> implements zzlh {
    private static final zzgh zza;
    private int zze;
    private long zzf;
    private String zzg = "";
    private String zzh = "";
    private long zzi;
    private float zzj;
    private double zzk;

    static {
        zzgh zzgh = new zzgh();
        zza = zzgh;
        zzjz.zzbG(zzgh.class, zzgh);
    }

    private zzgh() {
    }

    public static zzgg zzd() {
        return zza.zzbu();
    }

    public static /* synthetic */ zzgh zze() {
        return zza;
    }

    public static /* synthetic */ void zzh(zzgh zzgh, long j) {
        zzgh.zze |= 1;
        zzgh.zzf = j;
    }

    public static /* synthetic */ void zzi(zzgh zzgh, String str) {
        str.getClass();
        zzgh.zze |= 2;
        zzgh.zzg = str;
    }

    public static /* synthetic */ void zzj(zzgh zzgh, String str) {
        str.getClass();
        zzgh.zze |= 4;
        zzgh.zzh = str;
    }

    public static /* synthetic */ void zzk(zzgh zzgh) {
        zzgh.zze &= -5;
        zzgh.zzh = zza.zzh;
    }

    public static /* synthetic */ void zzm(zzgh zzgh, long j) {
        zzgh.zze |= 8;
        zzgh.zzi = j;
    }

    public static /* synthetic */ void zzn(zzgh zzgh) {
        zzgh.zze &= -9;
        zzgh.zzi = 0;
    }

    public static /* synthetic */ void zzo(zzgh zzgh, double d) {
        zzgh.zze |= 32;
        zzgh.zzk = d;
    }

    public static /* synthetic */ void zzp(zzgh zzgh) {
        zzgh.zze &= -33;
        zzgh.zzk = 0.0d;
    }

    public final double zza() {
        return this.zzk;
    }

    public final long zzb() {
        return this.zzi;
    }

    public final long zzc() {
        return this.zzf;
    }

    public final String zzf() {
        return this.zzg;
    }

    public final String zzg() {
        return this.zzh;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001ဂ\u0000\u0002ဈ\u0001\u0003ဈ\u0002\u0004ဂ\u0003\u0005ခ\u0004\u0006က\u0005", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk"});
        }
        if (i2 == 3) {
            return new zzgh();
        }
        if (i2 == 4) {
            return new zzgg(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }

    public final boolean zzq() {
        return (this.zze & 32) != 0;
    }

    public final boolean zzr() {
        return (this.zze & 8) != 0;
    }

    public final boolean zzs() {
        return (this.zze & 1) != 0;
    }

    public final boolean zzt() {
        return (this.zze & 4) != 0;
    }
}
