package com.google.android.gms.common.internal.service;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.base.zab;
import com.google.android.gms.internal.base.zad;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zan extends zab implements zao {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zan(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.service.ICommonService");
    }

    @Override // com.google.android.gms.common.internal.service.zao
    public final void zaa(zam zam) throws RemoteException {
        Parcel zaa = zaa();
        zad.zaa(zaa, zam);
        zac(1, zaa);
    }
}
