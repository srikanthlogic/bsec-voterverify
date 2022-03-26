package com.google.android.gms.tasks;

import java.util.concurrent.CancellationException;
/* compiled from: com.google.android.gms:play-services-tasks@@17.2.0 */
/* loaded from: classes.dex */
final class zzo implements Runnable {
    private final /* synthetic */ Task zza;
    private final /* synthetic */ zzp zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzo(zzp zzp, Task task) {
        this.zzb = zzp;
        this.zza = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            Task then = this.zzb.zzb.then(this.zza.getResult());
            if (then == null) {
                this.zzb.onFailure(new NullPointerException("Continuation returned null"));
                return;
            }
            then.addOnSuccessListener(TaskExecutors.zza, this.zzb);
            then.addOnFailureListener(TaskExecutors.zza, this.zzb);
            then.addOnCanceledListener(TaskExecutors.zza, this.zzb);
        } catch (RuntimeExecutionException e) {
            if (e.getCause() instanceof Exception) {
                this.zzb.onFailure((Exception) e.getCause());
            } else {
                this.zzb.onFailure(e);
            }
        } catch (CancellationException e2) {
            this.zzb.onCanceled();
        } catch (Exception e3) {
            this.zzb.onFailure(e3);
        }
    }
}
