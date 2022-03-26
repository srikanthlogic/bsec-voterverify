package com.google.android.gms.signin.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.internal.base.zab;
import com.google.android.gms.internal.base.zad;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaf extends zab implements zag {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zaf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.signin.internal.ISignInService");
    }

    @Override // com.google.android.gms.signin.internal.zag
    public final void zaa(int i) throws RemoteException {
        Parcel zaa = zaa();
        zaa.writeInt(i);
        zab(7, zaa);
    }

    @Override // com.google.android.gms.signin.internal.zag
    public final void zaa(IAccountAccessor iAccountAccessor, int i, boolean z) throws RemoteException {
        Parcel zaa = zaa();
        zad.zaa(zaa, iAccountAccessor);
        zaa.writeInt(i);
        zad.zaa(zaa, z);
        zab(9, zaa);
    }

    @Override // com.google.android.gms.signin.internal.zag
    public final void zaa(zaj zaj, zae zae) throws RemoteException {
        Parcel zaa = zaa();
        zad.zaa(zaa, zaj);
        zad.zaa(zaa, zae);
        zab(12, zaa);
    }
}
