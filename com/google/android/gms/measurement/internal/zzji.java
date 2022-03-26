package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.alcorlink.camera.AlErrorCode;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzji implements ServiceConnection, BaseGmsClient.BaseConnectionCallbacks, BaseGmsClient.BaseOnConnectionFailedListener {
    final /* synthetic */ zzjj zza;
    private volatile boolean zzb;
    private volatile zzee zzc;

    public zzji(zzjj zzjj) {
        this.zza = zzjj;
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient.BaseConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnected");
        synchronized (this) {
            try {
                Preconditions.checkNotNull(this.zzc);
                this.zza.zzs.zzaz().zzp(new zzjf(this, this.zzc.getService()));
            } catch (DeadObjectException | IllegalStateException e) {
                this.zzc = null;
                this.zzb = false;
            }
        }
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient.BaseOnConnectionFailedListener
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionFailed");
        zzei zzl = this.zza.zzs.zzl();
        if (zzl != null) {
            zzl.zzk().zzb("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.zzb = false;
            this.zzc = null;
        }
        this.zza.zzs.zzaz().zzp(new zzjh(this));
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient.BaseConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionSuspended");
        this.zza.zzs.zzay().zzc().zza("Service connection suspended");
        this.zza.zzs.zzaz().zzp(new zzjg(this));
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x007b A[Catch: all -> 0x0065, TRY_LEAVE, TryCatch #2 {, blocks: (B:6:0x0009, B:7:0x001c, B:10:0x001f, B:12:0x002b, B:14:0x0035, B:15:0x0039, B:17:0x003f, B:19:0x0053, B:22:0x0068, B:24:0x007b, B:25:0x007d, B:27:0x0095, B:28:0x00a5), top: B:36:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0095 A[Catch: all -> 0x0065, TRY_ENTER, TryCatch #2 {, blocks: (B:6:0x0009, B:7:0x001c, B:10:0x001f, B:12:0x002b, B:14:0x0035, B:15:0x0039, B:17:0x003f, B:19:0x0053, B:22:0x0068, B:24:0x007b, B:25:0x007d, B:27:0x0095, B:28:0x00a5), top: B:36:0x0007 }] */
    @Override // android.content.ServiceConnection
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceConnected");
        synchronized (this) {
            if (iBinder == null) {
                this.zzb = false;
                this.zza.zzs.zzay().zzd().zza("Service connected with null binder");
                return;
            }
            zzdz zzdz = null;
            try {
                String interfaceDescriptor = iBinder.getInterfaceDescriptor();
                if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                    IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
                    if (queryLocalInterface instanceof zzdz) {
                        zzdz = (zzdz) queryLocalInterface;
                    } else {
                        zzdz = new zzdx(iBinder);
                    }
                    try {
                        this.zza.zzs.zzay().zzj().zza("Bound to IMeasurementService interface");
                    } catch (RemoteException e) {
                        this.zza.zzs.zzay().zzd().zza("Service connect failed to get IMeasurementService");
                        if (zzdz != null) {
                        }
                    }
                } else {
                    this.zza.zzs.zzay().zzd().zzb("Got binder with a wrong descriptor", interfaceDescriptor);
                }
            } catch (RemoteException e2) {
            }
            if (zzdz != null) {
                this.zzb = false;
                try {
                    ConnectionTracker.getInstance().unbindService(this.zza.zzs.zzau(), this.zza.zza);
                } catch (IllegalArgumentException e3) {
                }
            } else {
                this.zza.zzs.zzaz().zzp(new zzjd(this, zzdz));
            }
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceDisconnected");
        this.zza.zzs.zzay().zzc().zza("Service disconnected");
        this.zza.zzs.zzaz().zzp(new zzje(this, componentName));
    }

    public final void zzb(Intent intent) {
        this.zza.zzg();
        Context zzau = this.zza.zzs.zzau();
        ConnectionTracker instance = ConnectionTracker.getInstance();
        synchronized (this) {
            if (this.zzb) {
                this.zza.zzs.zzay().zzj().zza("Connection attempt already in progress");
                return;
            }
            this.zza.zzs.zzay().zzj().zza("Using local app measurement service");
            this.zzb = true;
            instance.bindService(zzau, intent, this.zza.zza, AlErrorCode.ERR_INVALID_PARAM);
        }
    }

    public final void zzc() {
        this.zza.zzg();
        Context zzau = this.zza.zzs.zzau();
        synchronized (this) {
            if (this.zzb) {
                this.zza.zzs.zzay().zzj().zza("Connection attempt already in progress");
            } else if (this.zzc == null || (!this.zzc.isConnecting() && !this.zzc.isConnected())) {
                this.zzc = new zzee(zzau, Looper.getMainLooper(), this, this);
                this.zza.zzs.zzay().zzj().zza("Connecting to remote service");
                this.zzb = true;
                Preconditions.checkNotNull(this.zzc);
                this.zzc.checkAvailabilityAndConnect();
            } else {
                this.zza.zzs.zzay().zzj().zza("Already awaiting connection attempt");
            }
        }
    }

    public final void zzd() {
        if (this.zzc != null && (this.zzc.isConnected() || this.zzc.isConnecting())) {
            this.zzc.disconnect();
        }
        this.zzc = null;
    }
}
