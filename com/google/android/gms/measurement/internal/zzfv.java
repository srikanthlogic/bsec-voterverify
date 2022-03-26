package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzfv implements Runnable {
    final /* synthetic */ zzab zza;
    final /* synthetic */ zzgk zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfv(zzgk zzgk, zzab zzab) {
        this.zzb = zzgk;
        this.zza = zzab;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zza.zzA();
        if (this.zza.zzc.zza() == null) {
            this.zzb.zza.zzM(this.zza);
        } else {
            this.zzb.zza.zzR(this.zza);
        }
    }
}
