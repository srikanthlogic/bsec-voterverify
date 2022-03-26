package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.PendingResultUtil;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.TimeUnit;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zar implements PendingResult.StatusListener {
    private final /* synthetic */ PendingResult zaa;
    private final /* synthetic */ TaskCompletionSource zab;
    private final /* synthetic */ PendingResultUtil.ResultConverter zac;
    private final /* synthetic */ PendingResultUtil.zaa zad;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zar(PendingResult pendingResult, TaskCompletionSource taskCompletionSource, PendingResultUtil.ResultConverter resultConverter, PendingResultUtil.zaa zaa) {
        this.zaa = pendingResult;
        this.zab = taskCompletionSource;
        this.zac = resultConverter;
        this.zad = zaa;
    }

    @Override // com.google.android.gms.common.api.PendingResult.StatusListener
    public final void onComplete(Status status) {
        if (status.isSuccess()) {
            this.zab.setResult(this.zac.convert(this.zaa.await(0, TimeUnit.MILLISECONDS)));
            return;
        }
        this.zab.setException(this.zad.zaa(status));
    }
}
