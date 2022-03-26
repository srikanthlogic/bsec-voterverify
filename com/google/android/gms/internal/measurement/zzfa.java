package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfa extends zzjz<zzfa, zzez> implements zzlh {
    private static final zzfa zza;
    private int zze;
    private String zzf = "";
    private boolean zzg;
    private boolean zzh;
    private int zzi;

    static {
        zzfa zzfa = new zzfa();
        zza = zzfa;
        zzjz.zzbG(zzfa.class, zzfa);
    }

    private zzfa() {
    }

    public static /* synthetic */ zzfa zzb() {
        return zza;
    }

    public static /* synthetic */ void zzd(zzfa zzfa, String str) {
        str.getClass();
        zzfa.zze |= 1;
        zzfa.zzf = str;
    }

    public final int zza() {
        return this.zzi;
    }

    public final String zzc() {
        return this.zzf;
    }

    public final boolean zze() {
        return this.zzg;
    }

    public final boolean zzf() {
        return this.zzh;
    }

    public final boolean zzg() {
        return (this.zze & 8) != 0;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဇ\u0001\u0003ဇ\u0002\u0004င\u0003", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi"});
        }
        if (i2 == 3) {
            return new zzfa();
        }
        if (i2 == 4) {
            return new zzez(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
