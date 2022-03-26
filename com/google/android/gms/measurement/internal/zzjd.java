package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzjd implements Runnable {
    final /* synthetic */ zzdz zza;
    final /* synthetic */ zzji zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjd(zzji zzji, zzdz zzdz) {
        this.zzb = zzji;
        this.zza = zzdz;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzb) {
            this.zzb.zzb = false;
            if (!this.zzb.zza.zzL()) {
                this.zzb.zza.zzs.zzay().zzj().zza("Connected to service");
                this.zzb.zza.zzJ(this.zza);
            }
        }
    }
}
