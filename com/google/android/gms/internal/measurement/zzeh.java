package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzeh extends zzjz<zzeh, zzeg> implements zzlh {
    private static final zzeh zza;
    private int zze;
    private int zzf;
    private zzkg<zzes> zzg = zzbA();
    private zzkg<zzej> zzh = zzbA();
    private boolean zzi;
    private boolean zzj;

    static {
        zzeh zzeh = new zzeh();
        zza = zzeh;
        zzjz.zzbG(zzeh.class, zzeh);
    }

    private zzeh() {
    }

    public static /* synthetic */ void zzi(zzeh zzeh, int i, zzes zzes) {
        zzes.getClass();
        zzkg<zzes> zzkg = zzeh.zzg;
        if (!zzkg.zzc()) {
            zzeh.zzg = zzjz.zzbB(zzkg);
        }
        zzeh.zzg.set(i, zzes);
    }

    public static /* synthetic */ void zzj(zzeh zzeh, int i, zzej zzej) {
        zzej.getClass();
        zzkg<zzej> zzkg = zzeh.zzh;
        if (!zzkg.zzc()) {
            zzeh.zzh = zzjz.zzbB(zzkg);
        }
        zzeh.zzh.set(i, zzej);
    }

    public final int zza() {
        return this.zzf;
    }

    public final int zzb() {
        return this.zzh.size();
    }

    public final int zzc() {
        return this.zzg.size();
    }

    public final zzej zze(int i) {
        return this.zzh.get(i);
    }

    public final zzes zzf(int i) {
        return this.zzg.get(i);
    }

    public final List<zzej> zzg() {
        return this.zzh;
    }

    public final List<zzes> zzh() {
        return this.zzg;
    }

    public final boolean zzk() {
        return (this.zze & 1) != 0;
    }

    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0002\u0000\u0001င\u0000\u0002\u001b\u0003\u001b\u0004ဇ\u0001\u0005ဇ\u0002", new Object[]{"zze", "zzf", "zzg", zzes.class, "zzh", zzej.class, "zzi", "zzj"});
        }
        if (i2 == 3) {
            return new zzeh();
        }
        if (i2 == 4) {
            return new zzeg(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
