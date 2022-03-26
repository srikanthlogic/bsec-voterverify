package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzgg implements Runnable {
    final /* synthetic */ zzkq zza;
    final /* synthetic */ zzp zzb;
    final /* synthetic */ zzgk zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgg(zzgk zzgk, zzkq zzkq, zzp zzp) {
        this.zzc = zzgk;
        this.zza = zzkq;
        this.zzb = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzc.zza.zzA();
        if (this.zza.zza() == null) {
            this.zzc.zza.zzO(this.zza, this.zzb);
        } else {
            this.zzc.zza.zzU(this.zza, this.zzb);
        }
    }
}
