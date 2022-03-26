package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.core.os.EnvironmentCompat;
import com.google.android.gms.common.internal.GmsClientSupervisor;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public final class zzh implements Handler.Callback {
    private final /* synthetic */ zzg zza;

    private zzh(zzg zzg) {
        this.zza = zzg;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            synchronized (this.zza.zza) {
                GmsClientSupervisor.zza zza = (GmsClientSupervisor.zza) message.obj;
                zzi zzi = (zzi) this.zza.zza.get(zza);
                if (zzi != null && zzi.zzc()) {
                    if (zzi.zza()) {
                        zzi.zzb("GmsClientSupervisor");
                    }
                    this.zza.zza.remove(zza);
                }
            }
            return true;
        } else if (i != 1) {
            return false;
        } else {
            synchronized (this.zza.zza) {
                GmsClientSupervisor.zza zza2 = (GmsClientSupervisor.zza) message.obj;
                zzi zzi2 = (zzi) this.zza.zza.get(zza2);
                if (zzi2 != null && zzi2.zzb() == 3) {
                    String valueOf = String.valueOf(zza2);
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 47);
                    sb.append("Timeout waiting for ServiceConnection callback ");
                    sb.append(valueOf);
                    Log.e("GmsClientSupervisor", sb.toString(), new Exception());
                    ComponentName zze = zzi2.zze();
                    if (zze == null) {
                        zze = zza2.zzb();
                    }
                    if (zze == null) {
                        zze = new ComponentName((String) Preconditions.checkNotNull(zza2.zza()), EnvironmentCompat.MEDIA_UNKNOWN);
                    }
                    zzi2.onServiceDisconnected(zze);
                }
            }
            return true;
        }
    }
}
