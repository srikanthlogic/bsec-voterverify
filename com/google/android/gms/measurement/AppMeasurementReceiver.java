package com.google.android.gms.measurement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;
import com.google.android.gms.measurement.internal.zzfb;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class AppMeasurementReceiver extends WakefulBroadcastReceiver implements zzfb.zza {
    private zzfb zza;

    public BroadcastReceiver.PendingResult doGoAsync() {
        return goAsync();
    }

    @Override // com.google.android.gms.measurement.internal.zzfb.zza
    public void doStartService(Context context, Intent service) {
        startWakefulService(context, service);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (this.zza == null) {
            this.zza = new zzfb(this);
        }
        this.zza.zza(context, intent);
    }
}
