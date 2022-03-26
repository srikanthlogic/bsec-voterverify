package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgm extends zzjz<zzgm, zzgl> implements zzlh {
    private static final zzgm zza;
    private int zze;
    private String zzf = "";
    private zzkg<zzgt> zzg = zzbA();

    static {
        zzgm zzgm = new zzgm();
        zza = zzgm;
        zzjz.zzbG(zzgm.class, zzgm);
    }

    private zzgm() {
    }

    public final String zzb() {
        return this.zzf;
    }

    public final List<zzgt> zzc() {
        return this.zzg;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001ဈ\u0000\u0002\u001b", new Object[]{"zze", "zzf", "zzg", zzgt.class});
        }
        if (i2 == 3) {
            return new zzgm();
        }
        if (i2 == 4) {
            return new zzgl(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
