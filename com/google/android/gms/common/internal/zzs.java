package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.zzj;
import com.google.android.gms.common.zzl;
import com.google.android.gms.common.zzq;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.common.zzb;
import com.google.android.gms.internal.common.zzd;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public final class zzs extends zzb implements zzr {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IGoogleCertificatesApi");
    }

    @Override // com.google.android.gms.common.internal.zzr
    public final boolean zza(zzq zzq, IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel a_ = a_();
        zzd.zza(a_, zzq);
        zzd.zza(a_, iObjectWrapper);
        Parcel zza = zza(5, a_);
        boolean zza2 = zzd.zza(zza);
        zza.recycle();
        return zza2;
    }

    @Override // com.google.android.gms.common.internal.zzr
    public final zzl zza(zzj zzj) throws RemoteException {
        Parcel a_ = a_();
        zzd.zza(a_, zzj);
        Parcel zza = zza(6, a_);
        zzl zzl = (zzl) zzd.zza(zza, zzl.CREATOR);
        zza.recycle();
        return zzl;
    }

    @Override // com.google.android.gms.common.internal.zzr
    public final boolean zza() throws RemoteException {
        Parcel zza = zza(7, a_());
        boolean zza2 = zzd.zza(zza);
        zza.recycle();
        return zza2;
    }
}
