package com.google.android.gms.measurement;

import android.app.Service;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.gms.measurement.internal.zzjo;
import com.google.android.gms.measurement.internal.zzjp;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class AppMeasurementService extends Service implements zzjo {
    private zzjp<AppMeasurementService> zza;

    private final zzjp<AppMeasurementService> zzd() {
        if (this.zza == null) {
            this.zza = new zzjp<>(this);
        }
        return this.zza;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return zzd().zzb(intent);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        zzd().zze();
    }

    @Override // android.app.Service
    public void onDestroy() {
        zzd().zzf();
        super.onDestroy();
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        zzd().zzg(intent);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        zzd().zza(intent, flags, startId);
        return 2;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        zzd().zzj(intent);
        return true;
    }

    @Override // com.google.android.gms.measurement.internal.zzjo
    public final void zza(Intent intent) {
        AppMeasurementReceiver.completeWakefulIntent(intent);
    }

    @Override // com.google.android.gms.measurement.internal.zzjo
    public final void zzb(JobParameters jobParameters, boolean z) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.measurement.internal.zzjo
    public final boolean zzc(int i) {
        return stopSelfResult(i);
    }
}
