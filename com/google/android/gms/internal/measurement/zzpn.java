package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzpn implements zzpm {
    public static final zzhu<Boolean> zza;
    public static final zzhu<Long> zzb;

    static {
        zzhr zzhr = new zzhr(zzhk.zza("com.google.android.gms.measurement"));
        zza = zzhr.zze("measurement.module.pixie.ees", false);
        zzb = zzhr.zzc("measurement.id.module.pixie.ees", 0);
    }

    @Override // com.google.android.gms.internal.measurement.zzpm
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.zzpm
    public final boolean zzb() {
        return zza.zzb().booleanValue();
    }
}
