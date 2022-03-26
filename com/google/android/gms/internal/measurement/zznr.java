package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zznr implements zznq {
    public static final zzhu<Boolean> zza;
    public static final zzhu<Boolean> zzb;
    public static final zzhu<Boolean> zzc;
    public static final zzhu<Long> zzd;
    public static final zzhu<Long> zze;

    static {
        zzhr zzhr = new zzhr(zzhk.zza("com.google.android.gms.measurement"));
        zza = zzhr.zze("measurement.client.consent_state_v1", true);
        zzb = zzhr.zze("measurement.client.3p_consent_state_v1", true);
        zzc = zzhr.zze("measurement.service.consent_state_v1_W36", true);
        zzd = zzhr.zzc("measurement.id.service.consent_state_v1_W36", 0);
        zze = zzhr.zzc("measurement.service.storage_consent_support_version", 203600);
    }

    @Override // com.google.android.gms.internal.measurement.zznq
    public final long zza() {
        return zze.zzb().longValue();
    }
}
