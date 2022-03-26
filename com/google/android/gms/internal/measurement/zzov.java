package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzov implements zzou {
    public static final zzhu<Boolean> zza;
    public static final zzhu<Boolean> zzb;
    public static final zzhu<Boolean> zzc;
    public static final zzhu<Long> zzd;

    static {
        zzhr zzhr = new zzhr(zzhk.zza("com.google.android.gms.measurement"));
        zza = zzhr.zze("measurement.sdk.collection.enable_extend_user_property_size", true);
        zzb = zzhr.zze("measurement.sdk.collection.last_deep_link_referrer2", true);
        zzc = zzhr.zze("measurement.sdk.collection.last_deep_link_referrer_campaign2", false);
        zzd = zzhr.zzc("measurement.id.sdk.collection.last_deep_link_referrer2", 0);
    }

    @Override // com.google.android.gms.internal.measurement.zzou
    public final boolean zza() {
        return zza.zzb().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.zzou
    public final boolean zzb() {
        return zzc.zzb().booleanValue();
    }
}
