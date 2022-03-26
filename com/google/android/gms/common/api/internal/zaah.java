package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaah implements BaseGmsClient.ConnectionProgressReportCallbacks {
    private final WeakReference<zaaf> zaa;
    private final Api<?> zab;
    private final boolean zac;

    public zaah(zaaf zaaf, Api<?> api, boolean z) {
        this.zaa = new WeakReference<>(zaaf);
        this.zab = api;
        this.zac = z;
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks
    public final void onReportServiceBinding(ConnectionResult connectionResult) {
        zaaf zaaf = this.zaa.get();
        if (zaaf != null) {
            Preconditions.checkState(Looper.myLooper() == zaaf.zaa.zad.getLooper(), "onReportServiceBinding must be called on the GoogleApiClient handler thread");
            zaaf.zab.lock();
            try {
                if (zaaf.zab(0)) {
                    if (!connectionResult.isSuccess()) {
                        zaaf.zab(connectionResult, this.zab, this.zac);
                    }
                    if (zaaf.zad()) {
                        zaaf.zae();
                    }
                }
            } finally {
                zaaf.zab.unlock();
            }
        }
    }

    public static /* synthetic */ boolean zaa(zaah zaah) {
        return zaah.zac;
    }
}
