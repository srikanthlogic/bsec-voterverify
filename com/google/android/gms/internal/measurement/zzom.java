package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzom implements zzol {
    public static final zzhu<Boolean> zza;
    public static final zzhu<Boolean> zzb;
    public static final zzhu<Long> zzc;

    static {
        zzhr zzhr = new zzhr(zzhk.zza("com.google.android.gms.measurement"));
        zza = zzhr.zze("measurement.collection.efficient_engagement_reporting_enabled_2", true);
        zzb = zzhr.zze("measurement.collection.redundant_engagement_removal_enabled", false);
        zzc = zzhr.zzc("measurement.id.collection.redundant_engagement_removal_enabled", 0);
    }

    @Override // com.google.android.gms.internal.measurement.zzol
    public final boolean zza() {
        return zzb.zzb().booleanValue();
    }
}
