package com.google.android.gms.common.api.internal;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.GoogleApiManager;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zabj implements Runnable {
    private final /* synthetic */ ConnectionResult zaa;
    private final /* synthetic */ GoogleApiManager.zac zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zabj(GoogleApiManager.zac zac, ConnectionResult connectionResult) {
        this.zab = zac;
        this.zaa = connectionResult;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GoogleApiManager.zaa zaa = (GoogleApiManager.zaa) GoogleApiManager.this.zap.get(this.zab.zac);
        if (zaa != null) {
            if (this.zaa.isSuccess()) {
                this.zab.zaf = true;
                if (this.zab.zab.requiresSignIn()) {
                    this.zab.zaa();
                    return;
                }
                try {
                    this.zab.zab.getRemoteService(null, this.zab.zab.getScopesForConnectionlessNonSignIn());
                } catch (SecurityException e) {
                    Log.e("GoogleApiManager", "Failed to get service from broker. ", e);
                    this.zab.zab.disconnect("Failed to get service from broker.");
                    zaa.onConnectionFailed(new ConnectionResult(10));
                }
            } else {
                zaa.onConnectionFailed(this.zaa);
            }
        }
    }
}
