package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgb extends zzjz<zzgb, zzfz> implements zzlh {
    private static final zzgb zza;
    private int zze;
    private int zzf = 1;
    private zzkg<zzfq> zzg = zzbA();

    static {
        zzgb zzgb = new zzgb();
        zza = zzgb;
        zzjz.zzbG(zzgb.class, zzgb);
    }

    private zzgb() {
    }

    public static zzfz zza() {
        return zza.zzbu();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzc(zzgb zzgb, zzfq zzfq) {
        zzfq.getClass();
        zzkg<zzfq> zzkg = zzgb.zzg;
        if (!zzkg.zzc()) {
            zzgb.zzg = zzjz.zzbB(zzkg);
        }
        zzgb.zzg.add(zzfq);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzjz
    public final Object zzl(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzbF(zza, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001ဌ\u0000\u0002\u001b", new Object[]{"zze", "zzf", zzga.zza, "zzg", zzfq.class});
        }
        if (i2 == 3) {
            return new zzgb();
        }
        if (i2 == 4) {
            return new zzfz(null);
        }
        if (i2 != 5) {
            return null;
        }
        return zza;
    }
}
