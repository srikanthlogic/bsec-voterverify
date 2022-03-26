package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.internal.common.zzi;
import java.util.HashMap;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public final class zzg extends GmsClientSupervisor {
    private final Context zzb;
    private final Handler zzc;
    private final HashMap<GmsClientSupervisor.zza, zzi> zza = new HashMap<>();
    private final ConnectionTracker zzd = ConnectionTracker.getInstance();
    private final long zze = 5000;
    private final long zzf = 300000;

    public zzg(Context context) {
        this.zzb = context.getApplicationContext();
        this.zzc = new zzi(context.getMainLooper(), new zzh(this));
    }

    @Override // com.google.android.gms.common.internal.GmsClientSupervisor
    public final boolean zza(GmsClientSupervisor.zza zza, ServiceConnection serviceConnection, String str) {
        boolean zza2;
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zza) {
            zzi zzi = this.zza.get(zza);
            if (zzi == null) {
                zzi = new zzi(this, zza);
                zzi.zza(serviceConnection, serviceConnection, str);
                zzi.zza(str);
                this.zza.put(zza, zzi);
            } else {
                this.zzc.removeMessages(0, zza);
                if (!zzi.zza(serviceConnection)) {
                    zzi.zza(serviceConnection, serviceConnection, str);
                    int zzb = zzi.zzb();
                    if (zzb == 1) {
                        serviceConnection.onServiceConnected(zzi.zze(), zzi.zzd());
                    } else if (zzb == 2) {
                        zzi.zza(str);
                    }
                } else {
                    String valueOf = String.valueOf(zza);
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 81);
                    sb.append("Trying to bind a GmsServiceConnection that was already connected before.  config=");
                    sb.append(valueOf);
                    throw new IllegalStateException(sb.toString());
                }
            }
            zza2 = zzi.zza();
        }
        return zza2;
    }

    @Override // com.google.android.gms.common.internal.GmsClientSupervisor
    protected final void zzb(GmsClientSupervisor.zza zza, ServiceConnection serviceConnection, String str) {
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zza) {
            zzi zzi = this.zza.get(zza);
            if (zzi == null) {
                String valueOf = String.valueOf(zza);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 50);
                sb.append("Nonexistent connection status for service config: ");
                sb.append(valueOf);
                throw new IllegalStateException(sb.toString());
            } else if (zzi.zza(serviceConnection)) {
                zzi.zza(serviceConnection, str);
                if (zzi.zzc()) {
                    this.zzc.sendMessageDelayed(this.zzc.obtainMessage(0, zza), this.zze);
                }
            } else {
                String valueOf2 = String.valueOf(zza);
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 76);
                sb2.append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=");
                sb2.append(valueOf2);
                throw new IllegalStateException(sb2.toString());
            }
        }
    }
}
