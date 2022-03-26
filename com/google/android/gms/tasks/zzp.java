package com.google.android.gms.tasks;

import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-tasks@@17.2.0 */
/* loaded from: classes.dex */
public final class zzp<TResult, TContinuationResult> implements OnCanceledListener, OnFailureListener, OnSuccessListener<TContinuationResult>, zzr<TResult> {
    private final Executor zza;
    private final SuccessContinuation<TResult, TContinuationResult> zzb;
    private final zzu<TContinuationResult> zzc;

    public zzp(Executor executor, SuccessContinuation<TResult, TContinuationResult> successContinuation, zzu<TContinuationResult> zzu) {
        this.zza = executor;
        this.zzb = successContinuation;
        this.zzc = zzu;
    }

    @Override // com.google.android.gms.tasks.zzr
    public final void zza(Task<TResult> task) {
        this.zza.execute(new zzo(this, task));
    }

    @Override // com.google.android.gms.tasks.zzr
    public final void zza() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public final void onSuccess(TContinuationResult tcontinuationresult) {
        this.zzc.zza((zzu<TContinuationResult>) tcontinuationresult);
    }

    @Override // com.google.android.gms.tasks.OnFailureListener
    public final void onFailure(Exception exc) {
        this.zzc.zza(exc);
    }

    @Override // com.google.android.gms.tasks.OnCanceledListener
    public final void onCanceled() {
        this.zzc.zza();
    }
}
