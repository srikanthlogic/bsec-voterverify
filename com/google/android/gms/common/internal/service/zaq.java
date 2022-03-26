package com.google.android.gms.common.internal.service;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.RemoteCall;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.common.internal.zaaa;
import com.google.android.gms.common.internal.zaac;
import com.google.android.gms.internal.base.zae;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaq extends GoogleApi<Api.ApiOptions.NoOptions> implements zaac {
    private static final Api.ClientKey<zar> zaa = new Api.ClientKey<>();
    private static final Api.AbstractClientBuilder<zar, Api.ApiOptions.NoOptions> zab = new zas();
    private static final Api<Api.ApiOptions.NoOptions> zac = new Api<>("ClientTelemetry.API", zab, zaa);

    public zaq(Context context) {
        super(context, zac, Api.ApiOptions.NO_OPTIONS, GoogleApi.Settings.DEFAULT_SETTINGS);
    }

    @Override // com.google.android.gms.common.internal.zaac
    public final Task<Void> zaa(zaaa zaaa) {
        return doBestEffortWrite(TaskApiCall.builder().setFeatures(zae.zaa).setAutoResolveMissingFeatures(false).run(new RemoteCall(zaaa) { // from class: com.google.android.gms.common.internal.service.zap
            private final zaaa zaa;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zaa = r1;
            }

            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                zaq.zaa(this.zaa, (zar) obj, (TaskCompletionSource) obj2);
            }
        }).build());
    }

    public static final /* synthetic */ void zaa(zaaa zaaa, zar zar, TaskCompletionSource taskCompletionSource) throws RemoteException {
        ((zak) zar.getService()).zaa(zaaa);
        taskCompletionSource.setResult(null);
    }
}
