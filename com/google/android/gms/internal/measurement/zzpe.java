package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzpe implements zzpd {
    public static final zzhu<Boolean> zza;
    public static final zzhu<Long> zzb;

    static {
        zzhr zzhr = new zzhr(zzhk.zza("com.google.android.gms.measurement"));
        zza = zzhr.zze("measurement.validation.internal_limits_internal_event_params", false);
        zzb = zzhr.zzc("measurement.id.validation.internal_limits_internal_event_params", 0);
    }

    @Override // com.google.android.gms.internal.measurement.zzpd
    public final boolean zza() {
        return zza.zzb().booleanValue();
    }
}
