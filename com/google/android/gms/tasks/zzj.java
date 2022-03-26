package com.google.android.gms.tasks;
/* compiled from: com.google.android.gms:play-services-tasks@@17.2.0 */
/* loaded from: classes.dex */
final class zzj implements Runnable {
    private final /* synthetic */ Task zza;
    private final /* synthetic */ zzi zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzj(zzi zzi, Task task) {
        this.zzb = zzi;
        this.zza = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzb.zzb) {
            if (this.zzb.zzc != null) {
                this.zzb.zzc.onComplete(this.zza);
            }
        }
    }
}
