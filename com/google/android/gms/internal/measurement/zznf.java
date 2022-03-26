package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zznf implements zzne {
    public static final zzhu<Boolean> zza;
    public static final zzhu<Boolean> zzb;

    static {
        zzhr zzhr = new zzhr(zzhk.zza("com.google.android.gms.measurement"));
        zza = zzhr.zze("measurement.androidId.delete_feature", true);
        zzb = zzhr.zze("measurement.log_androidId_enabled", false);
    }

    @Override // com.google.android.gms.internal.measurement.zzne
    public final boolean zza() {
        return zza.zzb().booleanValue();
    }
}
