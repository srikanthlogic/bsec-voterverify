package com.google.android.gms.common.internal.service;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zad extends zai {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zad(zae zae, GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    @Override // com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl
    protected final /* synthetic */ void doExecute(zah zah) throws RemoteException {
        ((zao) zah.getService()).zaa(new zag(this));
    }
}
