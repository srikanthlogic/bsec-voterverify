package com.google.android.gms.tasks;
/* compiled from: com.google.android.gms:play-services-tasks@@17.2.0 */
/* loaded from: classes.dex */
final class zzg implements Runnable {
    private final /* synthetic */ zzh zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzg(zzh zzh) {
        this.zza = zzh;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zza.zzb) {
            if (this.zza.zzc != null) {
                this.zza.zzc.onCanceled();
            }
        }
    }
}
