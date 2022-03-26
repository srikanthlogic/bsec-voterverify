package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zab implements PendingResult.StatusListener {
    private final /* synthetic */ Batch zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zab(Batch batch) {
        this.zaa = batch;
    }

    @Override // com.google.android.gms.common.api.PendingResult.StatusListener
    public final void onComplete(Status status) {
        Status status2;
        synchronized (this.zaa.zaf) {
            if (!this.zaa.isCanceled()) {
                if (status.isCanceled()) {
                    this.zaa.zad = true;
                } else if (!status.isSuccess()) {
                    this.zaa.zac = true;
                }
                Batch.zab(this.zaa);
                if (this.zaa.zab == 0) {
                    if (this.zaa.zad) {
                        zab.super.cancel();
                    } else {
                        if (this.zaa.zac) {
                            status2 = new Status(13);
                        } else {
                            status2 = Status.RESULT_SUCCESS;
                        }
                        this.zaa.setResult(new BatchResult(status2, this.zaa.zae));
                    }
                }
            }
        }
    }
}
