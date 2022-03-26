package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.internal.Preconditions;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zacm implements Runnable {
    private final /* synthetic */ Result zaa;
    private final /* synthetic */ zacn zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zacm(zacn zacn, Result result) {
        this.zab = zacn;
        this.zaa = result;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            try {
                BasePendingResult.zaa.set(true);
                this.zab.zah.sendMessage(this.zab.zah.obtainMessage(0, ((ResultTransform) Preconditions.checkNotNull(this.zab.zaa)).onSuccess(this.zaa)));
                BasePendingResult.zaa.set(false);
                zacn zacn = this.zab;
                zacn.zaa(this.zaa);
                GoogleApiClient googleApiClient = (GoogleApiClient) this.zab.zag.get();
                if (googleApiClient != null) {
                    googleApiClient.zab(this.zab);
                }
            } catch (RuntimeException e) {
                this.zab.zah.sendMessage(this.zab.zah.obtainMessage(1, e));
                BasePendingResult.zaa.set(false);
                zacn zacn2 = this.zab;
                zacn.zaa(this.zaa);
                GoogleApiClient googleApiClient2 = (GoogleApiClient) this.zab.zag.get();
                if (googleApiClient2 != null) {
                    googleApiClient2.zab(this.zab);
                }
            }
        } catch (Throwable th) {
            BasePendingResult.zaa.set(false);
            zacn zacn3 = this.zab;
            zacn.zaa(this.zaa);
            GoogleApiClient googleApiClient3 = (GoogleApiClient) this.zab.zag.get();
            if (googleApiClient3 != null) {
                googleApiClient3.zab(this.zab);
            }
            throw th;
        }
    }
}
