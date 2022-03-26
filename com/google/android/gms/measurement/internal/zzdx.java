package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzbm;
import com.google.android.gms.internal.measurement.zzbo;
import java.util.ArrayList;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzdx extends zzbm implements zzdz {
    public zzdx(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final String zzd(zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzp);
        Parcel zzb = zzb(11, zza);
        String readString = zzb.readString();
        zzb.recycle();
        return readString;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzkq> zze(zzp zzp, boolean z) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzp);
        zzbo.zzc(zza, z);
        Parcel zzb = zzb(7, zza);
        ArrayList createTypedArrayList = zzb.createTypedArrayList(zzkq.CREATOR);
        zzb.recycle();
        return createTypedArrayList;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzab> zzf(String str, String str2, zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zza.writeString(str);
        zza.writeString(str2);
        zzbo.zzd(zza, zzp);
        Parcel zzb = zzb(16, zza);
        ArrayList createTypedArrayList = zzb.createTypedArrayList(zzab.CREATOR);
        zzb.recycle();
        return createTypedArrayList;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzab> zzg(String str, String str2, String str3) throws RemoteException {
        Parcel zza = zza();
        zza.writeString(null);
        zza.writeString(str2);
        zza.writeString(str3);
        Parcel zzb = zzb(17, zza);
        ArrayList createTypedArrayList = zzb.createTypedArrayList(zzab.CREATOR);
        zzb.recycle();
        return createTypedArrayList;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzkq> zzh(String str, String str2, boolean z, zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zza.writeString(str);
        zza.writeString(str2);
        zzbo.zzc(zza, z);
        zzbo.zzd(zza, zzp);
        Parcel zzb = zzb(14, zza);
        ArrayList createTypedArrayList = zzb.createTypedArrayList(zzkq.CREATOR);
        zzb.recycle();
        return createTypedArrayList;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final List<zzkq> zzi(String str, String str2, String str3, boolean z) throws RemoteException {
        Parcel zza = zza();
        zza.writeString(null);
        zza.writeString(str2);
        zza.writeString(str3);
        zzbo.zzc(zza, z);
        Parcel zzb = zzb(15, zza);
        ArrayList createTypedArrayList = zzb.createTypedArrayList(zzkq.CREATOR);
        zzb.recycle();
        return createTypedArrayList;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzj(zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzp);
        zzc(4, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzk(zzat zzat, zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzat);
        zzbo.zzd(zza, zzp);
        zzc(1, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzl(zzat zzat, String str, String str2) throws RemoteException {
        throw null;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzm(zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzp);
        zzc(18, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzn(zzab zzab, zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzab);
        zzbo.zzd(zza, zzp);
        zzc(12, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzo(zzab zzab) throws RemoteException {
        throw null;
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzp(zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzp);
        zzc(20, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzq(long j, String str, String str2, String str3) throws RemoteException {
        Parcel zza = zza();
        zza.writeLong(j);
        zza.writeString(str);
        zza.writeString(str2);
        zza.writeString(str3);
        zzc(10, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzr(Bundle bundle, zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, bundle);
        zzbo.zzd(zza, zzp);
        zzc(19, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzs(zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzp);
        zzc(6, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final void zzt(zzkq zzkq, zzp zzp) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzkq);
        zzbo.zzd(zza, zzp);
        zzc(2, zza);
    }

    @Override // com.google.android.gms.measurement.internal.zzdz
    public final byte[] zzu(zzat zzat, String str) throws RemoteException {
        Parcel zza = zza();
        zzbo.zzd(zza, zzat);
        zza.writeString(str);
        Parcel zzb = zzb(9, zza);
        byte[] createByteArray = zzb.createByteArray();
        zzb.recycle();
        return createByteArray;
    }
}
