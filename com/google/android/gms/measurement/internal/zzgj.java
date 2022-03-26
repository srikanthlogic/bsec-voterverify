package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzgj implements Runnable {
    final /* synthetic */ String zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ String zzc;
    final /* synthetic */ long zzd;
    final /* synthetic */ zzgk zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgj(zzgk zzgk, String str, String str2, String str3, long j) {
        this.zze = zzgk;
        this.zza = str;
        this.zzb = str2;
        this.zzc = str3;
        this.zzd = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str = this.zza;
        if (str == null) {
            this.zze.zza.zzq().zzs().zzy(this.zzb, null);
            return;
        }
        this.zze.zza.zzq().zzs().zzy(this.zzb, new zzic(this.zzc, str, this.zzd));
    }
}
