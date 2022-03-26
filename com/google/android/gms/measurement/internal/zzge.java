package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzge implements Runnable {
    final /* synthetic */ zzat zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ zzgk zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzge(zzgk zzgk, zzat zzat, String str) {
        this.zzc = zzgk;
        this.zza = zzat;
        this.zzb = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzc.zza.zzA();
        this.zzc.zza.zzE(this.zza, this.zzb);
    }
}
