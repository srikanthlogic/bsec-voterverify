package com.google.android.gms.internal.location;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.zzbq;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public final class zzad extends zzae {
    final /* synthetic */ zzbq zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzad(zzaf zzaf, GoogleApiClient googleApiClient, zzbq zzbq) {
        super(googleApiClient);
        this.zza = zzbq;
    }

    @Override // com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl
    protected final /* bridge */ /* synthetic */ void doExecute(zzaz zzaz) throws RemoteException {
        zzaz.zzw(this.zza, this);
    }
}
