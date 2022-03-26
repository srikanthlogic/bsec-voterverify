package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.gms.internal.measurement.zzbq;
import com.google.android.gms.internal.measurement.zzbr;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzez implements ServiceConnection {
    final /* synthetic */ zzfa zza;
    private final String zzb;

    public zzez(zzfa zzfa, String str) {
        this.zza = zzfa;
        this.zzb = str;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder != null) {
            try {
                zzbr zzb = zzbq.zzb(iBinder);
                if (zzb == null) {
                    this.zza.zza.zzay().zzk().zza("Install Referrer Service implementation was not found");
                    return;
                }
                this.zza.zza.zzay().zzj().zza("Install Referrer Service connected");
                this.zza.zza.zzaz().zzp(new zzey(this, zzb, this));
            } catch (RuntimeException e) {
                this.zza.zza.zzay().zzk().zzb("Exception occurred while calling Install Referrer API", e);
            }
        } else {
            this.zza.zza.zzay().zzk().zza("Install Referrer connection returned with null binder");
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        this.zza.zza.zzay().zzj().zza("Install Referrer Service disconnected");
    }
}
