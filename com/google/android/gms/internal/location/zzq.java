package com.google.android.gms.internal.location;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
final class zzq extends zzx {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzq(zzz zzz, GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    @Override // com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl
    protected final /* bridge */ /* synthetic */ void doExecute(zzaz zzaz) throws RemoteException {
        zzaz.zzK(new zzy(this));
    }
}
