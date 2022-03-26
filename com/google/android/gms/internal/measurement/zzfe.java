package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfe extends zzjz<zzfe, zzfd> implements zzlh {
    private static final zzfe zza;
    private int zze;
    private String zzf = "";
    private String zzg = "";

    static {
        zzfe zzfe = new zzfe();
        zza = zzfe;
        zzjz.zzbG(zzfe.class, zzfe);
    }

    private zzfe() {
    }

    public final String zzb() {
        return this.zzf;
    }

    public final String zzc() {
        return this.zzg;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001", new Object[]{"zze", "zzf", "zzg"});
        }
        if (i2 == 3) {
            return new zzfe();
        }
        if (i2 == 4) {
            return new zzfd(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
