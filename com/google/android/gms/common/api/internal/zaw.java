package com.google.android.gms.common.api.internal;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaw implements OnCompleteListener<TResult> {
    private final /* synthetic */ TaskCompletionSource zaa;
    private final /* synthetic */ zav zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaw(zav zav, TaskCompletionSource taskCompletionSource) {
        this.zab = zav;
        this.zaa = taskCompletionSource;
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public final void onComplete(Task<TResult> task) {
        this.zab.zab.remove(this.zaa);
    }
}
