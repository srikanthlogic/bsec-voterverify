package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgk extends zzjz<zzgk, zzgj> implements zzlh {
    private static final zzgk zza;
    private zzkg<zzgm> zze = zzbA();

    static {
        zzgk zzgk = new zzgk();
        zza = zzgk;
        zzjz.zzbG(zzgk.class, zzgk);
    }

    private zzgk() {
    }

    public static zzgk zzc() {
        return zza;
    }

    public final int zza() {
        return this.zze.size();
    }

    public final List<zzgm> zzd() {
        return this.zze;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"zze", zzgm.class});
        }
        if (i2 == 3) {
            return new zzgk();
        }
        if (i2 == 4) {
            return new zzgj(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
