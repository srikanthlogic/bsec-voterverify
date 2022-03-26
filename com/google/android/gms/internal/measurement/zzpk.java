package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzpk implements zzpj {
    public static final zzhu<Boolean> zza;
    public static final zzhu<Double> zzb;
    public static final zzhu<Long> zzc;
    public static final zzhu<Long> zzd;
    public static final zzhu<String> zze;

    static {
        zzhr zzhr = new zzhr(zzhk.zza("com.google.android.gms.measurement"));
        zza = zzhr.zze("measurement.test.boolean_flag", false);
        zzb = zzhr.zzb("measurement.test.double_flag", -3.0d);
        zzc = zzhr.zzc("measurement.test.int_flag", -2);
        zzd = zzhr.zzc("measurement.test.long_flag", -1);
        zze = zzhr.zzd("measurement.test.string_flag", "---");
    }

    @Override // com.google.android.gms.internal.measurement.zzpj
    public final double zza() {
        return zzb.zzb().doubleValue();
    }

    @Override // com.google.android.gms.internal.measurement.zzpj
    public final long zzb() {
        return zzc.zzb().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.zzpj
    public final long zzc() {
        return zzd.zzb().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.zzpj
    public final String zzd() {
        return zze.zzb();
    }

    @Override // com.google.android.gms.internal.measurement.zzpj
    public final boolean zze() {
        return zza.zzb().booleanValue();
    }
}
