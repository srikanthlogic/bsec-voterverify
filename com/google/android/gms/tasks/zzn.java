package com.google.android.gms.tasks;
/* compiled from: com.google.android.gms:play-services-tasks@@17.2.0 */
/* loaded from: classes.dex */
final class zzn implements Runnable {
    private final /* synthetic */ Task zza;
    private final /* synthetic */ zzm zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(zzm zzm, Task task) {
        this.zzb = zzm;
        this.zza = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzb.zzb) {
            if (this.zzb.zzc != null) {
                this.zzb.zzc.onSuccess(this.zza.getResult());
            }
        }
    }
}
