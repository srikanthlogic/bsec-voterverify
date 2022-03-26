package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzbn;
import com.google.android.gms.internal.measurement.zzbo;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzdy extends zzbn implements zzdz {
    public zzdy() {
        super("com.google.android.gms.measurement.internal.IMeasurementService");
    }

    @Override // com.google.android.gms.internal.measurement.zzbn
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                zzk((zzat) zzbo.zza(parcel, zzat.CREATOR), (zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                return true;
            case 2:
                zzt((zzkq) zzbo.zza(parcel, zzkq.CREATOR), (zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                return true;
            case 3:
            case 8:
            default:
                return false;
            case 4:
                zzj((zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                return true;
            case 5:
                zzl((zzat) zzbo.zza(parcel, zzat.CREATOR), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                return true;
            case 6:
                zzs((zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                return true;
            case 7:
                List<zzkq> zze = zze((zzp) zzbo.zza(parcel, zzp.CREATOR), zzbo.zzf(parcel));
                parcel2.writeNoException();
                parcel2.writeTypedList(zze);
                return true;
            case 9:
                byte[] zzu = zzu((zzat) zzbo.zza(parcel, zzat.CREATOR), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeByteArray(zzu);
                return true;
            case 10:
                zzq(parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                return true;
            case 11:
                String zzd = zzd((zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                parcel2.writeString(zzd);
                return true;
            case 12:
                zzn((zzab) zzbo.zza(parcel, zzab.CREATOR), (zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                return true;
            case 13:
                zzo((zzab) zzbo.zza(parcel, zzab.CREATOR));
                parcel2.writeNoException();
                return true;
            case 14:
                List<zzkq> zzh = zzh(parcel.readString(), parcel.readString(), zzbo.zzf(parcel), (zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(zzh);
                return true;
            case 15:
                List<zzkq> zzi = zzi(parcel.readString(), parcel.readString(), parcel.readString(), zzbo.zzf(parcel));
                parcel2.writeNoException();
                parcel2.writeTypedList(zzi);
                return true;
            case 16:
                List<zzab> zzf = zzf(parcel.readString(), parcel.readString(), (zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(zzf);
                return true;
            case 17:
                List<zzab> zzg = zzg(parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeTypedList(zzg);
                return true;
            case 18:
                zzm((zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                return true;
            case 19:
                zzr((Bundle) zzbo.zza(parcel, Bundle.CREATOR), (zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                return true;
            case 20:
                zzp((zzp) zzbo.zza(parcel, zzp.CREATOR));
                parcel2.writeNoException();
                return true;
        }
    }
}
