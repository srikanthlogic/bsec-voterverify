package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzbg implements zzbf {
    private final zzg zza;
    private final String zzb;

    public zzbg(zzg zzg, String str) {
        this.zza = zzg;
        this.zzb = str;
    }

    @Override // com.google.android.gms.internal.measurement.zzbf
    public final zzg zza(zzap zzap) {
        this.zza.zze(this.zzb, zzap);
        return this.zza;
    }
}
