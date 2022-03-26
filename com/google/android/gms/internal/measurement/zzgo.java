package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgo extends zzjz<zzgo, zzgn> implements zzlh {
    private static final zzgo zza;
    private int zze;
    private zzkg<zzgt> zzf = zzbA();
    private zzgk zzg;

    static {
        zzgo zzgo = new zzgo();
        zza = zzgo;
        zzjz.zzbG(zzgo.class, zzgo);
    }

    private zzgo() {
    }

    public final zzgk zza() {
        zzgk zzgk = this.zzg;
        return zzgk == null ? zzgk.zzc() : zzgk;
    }

    public final List<zzgt> zzc() {
        return this.zzf;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001\u001b\u0002ဉ\u0000", new Object[]{"zze", "zzf", zzgt.class, "zzg"});
        }
        if (i2 == 3) {
            return new zzgo();
        }
        if (i2 == 4) {
            return new zzgn(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
