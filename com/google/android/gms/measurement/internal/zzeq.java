package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.gms.common.internal.Preconditions;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzeq extends BroadcastReceiver {
    static final String zza = zzeq.class.getName();
    private final zzkn zzb;
    private boolean zzc;
    private boolean zzd;

    public zzeq(zzkn zzkn) {
        Preconditions.checkNotNull(zzkn);
        this.zzb = zzkn;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        this.zzb.zzB();
        String action = intent.getAction();
        this.zzb.zzay().zzj().zzb("NetworkBroadcastReceiver received action", action);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            boolean zzc = this.zzb.zzl().zzc();
            if (this.zzd != zzc) {
                this.zzd = zzc;
                this.zzb.zzaz().zzp(new zzep(this, zzc));
                return;
            }
            return;
        }
        this.zzb.zzay().zzk().zzb("NetworkBroadcastReceiver received unknown action", action);
    }

    public final void zzb() {
        this.zzb.zzB();
        this.zzb.zzaz().zzg();
        if (!this.zzc) {
            this.zzb.zzau().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.zzd = this.zzb.zzl().zzc();
            this.zzb.zzay().zzj().zzb("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.zzd));
            this.zzc = true;
        }
    }

    public final void zzc() {
        this.zzb.zzB();
        this.zzb.zzaz().zzg();
        this.zzb.zzaz().zzg();
        if (this.zzc) {
            this.zzb.zzay().zzj().zza("Unregistering connectivity change receiver");
            this.zzc = false;
            this.zzd = false;
            try {
                this.zzb.zzau().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                this.zzb.zzay().zzd().zzb("Failed to unregister the network broadcast receiver", e);
            }
        }
    }
}
