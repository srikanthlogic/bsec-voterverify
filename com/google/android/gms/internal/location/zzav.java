package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public final class zzav {
    private final zzbg<zzam> zza;
    private final Context zzb;
    private boolean zzc = false;
    private final Map<ListenerHolder.ListenerKey<LocationListener>, zzau> zzd = new HashMap();
    private final Map<ListenerHolder.ListenerKey, zzas> zze = new HashMap();
    private final Map<ListenerHolder.ListenerKey<LocationCallback>, zzar> zzf = new HashMap();

    public zzav(Context context, zzbg<zzam> zzbg) {
        this.zzb = context;
        this.zza = zzbg;
    }

    public final Location zza(String str) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        return ((zzh) this.zza).zza().zzn(str);
    }

    @Deprecated
    public final Location zzb() throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        return ((zzh) this.zza).zza().zzm();
    }

    public final LocationAvailability zzc() throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        return ((zzh) this.zza).zza().zzs(this.zzb.getPackageName());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void zzd(LocationRequest locationRequest, ListenerHolder<LocationListener> listenerHolder, zzai zzai) throws RemoteException {
        zzau zzau;
        zzau zzau2;
        zzi.zzp(((zzh) this.zza).zza);
        ListenerHolder.ListenerKey<LocationListener> listenerKey = listenerHolder.getListenerKey();
        if (listenerKey == null) {
            zzau = null;
        } else {
            synchronized (this.zzd) {
                zzau2 = this.zzd.get(listenerKey);
                if (zzau2 == null) {
                    zzau2 = new zzau(listenerHolder);
                }
                this.zzd.put(listenerKey, zzau2);
            }
            zzau = zzau2;
        }
        if (zzau != null) {
            ((zzh) this.zza).zza().zzo(new zzbc(1, zzba.zza(null, locationRequest), zzau, null, null, zzai));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void zze(zzba zzba, ListenerHolder<LocationCallback> listenerHolder, zzai zzai) throws RemoteException {
        zzar zzar;
        zzar zzar2;
        zzi.zzp(((zzh) this.zza).zza);
        ListenerHolder.ListenerKey<LocationCallback> listenerKey = listenerHolder.getListenerKey();
        if (listenerKey == null) {
            zzar = null;
        } else {
            synchronized (this.zzf) {
                zzar zzar3 = this.zzf.get(listenerKey);
                if (zzar3 == null) {
                    zzar2 = new zzar(listenerHolder);
                } else {
                    zzar2 = zzar3;
                }
                this.zzf.put(listenerKey, zzar2);
            }
            zzar = zzar2;
        }
        if (zzar != null) {
            ((zzh) this.zza).zza().zzo(new zzbc(1, zzba, null, null, zzar, zzai));
        }
    }

    public final void zzf(zzba zzba, PendingIntent pendingIntent, zzai zzai) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        ((zzh) this.zza).zza().zzo(zzbc.zzb(zzba, pendingIntent, zzai));
    }

    public final void zzg(LocationRequest locationRequest, PendingIntent pendingIntent, zzai zzai) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        ((zzh) this.zza).zza().zzo(zzbc.zzb(zzba.zza(null, locationRequest), pendingIntent, zzai));
    }

    public final void zzh(ListenerHolder.ListenerKey<LocationListener> listenerKey, zzai zzai) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        Preconditions.checkNotNull(listenerKey, "Invalid null listener key");
        synchronized (this.zzd) {
            zzau remove = this.zzd.remove(listenerKey);
            if (remove != null) {
                remove.zzc();
                ((zzh) this.zza).zza().zzo(zzbc.zza(remove, zzai));
            }
        }
    }

    public final void zzi(ListenerHolder.ListenerKey<LocationCallback> listenerKey, zzai zzai) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        Preconditions.checkNotNull(listenerKey, "Invalid null listener key");
        synchronized (this.zzf) {
            zzar remove = this.zzf.remove(listenerKey);
            if (remove != null) {
                remove.zzc();
                ((zzh) this.zza).zza().zzo(zzbc.zzc(remove, zzai));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void zzj(PendingIntent pendingIntent, zzai zzai) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        ((zzh) this.zza).zza().zzo(new zzbc(2, null, null, pendingIntent, null, zzai));
    }

    public final void zzk(boolean z) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        ((zzh) this.zza).zza().zzp(z);
        this.zzc = z;
    }

    public final void zzl(Location location) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        ((zzh) this.zza).zza().zzq(location);
    }

    public final void zzm(zzai zzai) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        ((zzh) this.zza).zza().zzr(zzai);
    }

    public final void zzn() throws RemoteException {
        synchronized (this.zzd) {
            for (zzau zzau : this.zzd.values()) {
                if (zzau != null) {
                    ((zzh) this.zza).zza().zzo(zzbc.zza(zzau, null));
                }
            }
            this.zzd.clear();
        }
        synchronized (this.zzf) {
            for (zzar zzar : this.zzf.values()) {
                if (zzar != null) {
                    ((zzh) this.zza).zza().zzo(zzbc.zzc(zzar, null));
                }
            }
            this.zzf.clear();
        }
        synchronized (this.zze) {
            for (zzas zzas : this.zze.values()) {
                if (zzas != null) {
                    ((zzh) this.zza).zza().zzu(new zzl(2, null, zzas, null));
                }
            }
            this.zze.clear();
        }
    }

    public final void zzo() throws RemoteException {
        if (this.zzc) {
            zzk(false);
        }
    }
}
