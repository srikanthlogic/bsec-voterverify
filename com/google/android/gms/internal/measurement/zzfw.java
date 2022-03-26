package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfw extends zzjz<zzfw, zzfv> implements zzlh {
    private static final zzfw zza;
    private zzkg<zzfy> zze = zzbA();

    static {
        zzfw zzfw = new zzfw();
        zza = zzfw;
        zzjz.zzbG(zzfw.class, zzfw);
    }

    private zzfw() {
    }

    public static zzfv zza() {
        return zza.zzbu();
    }

    public static /* synthetic */ zzfw zzb() {
        return zza;
    }

    public static /* synthetic */ void zze(zzfw zzfw, zzfy zzfy) {
        zzfy.getClass();
        zzkg<zzfy> zzkg = zzfw.zze;
        if (!zzkg.zzc()) {
            zzfw.zze = zzjz.zzbB(zzkg);
        }
        zzfw.zze.add(zzfy);
    }

    public final zzfy zzc(int i) {
        return this.zze.get(0);
    }

    public final List<zzfy> zzd() {
        return this.zze;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"zze", zzfy.class});
        }
        if (i2 == 3) {
            return new zzfw();
        }
        if (i2 == 4) {
            return new zzfv(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
