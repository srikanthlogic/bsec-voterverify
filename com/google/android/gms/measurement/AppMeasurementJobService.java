package com.google.android.gms.measurement;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import com.google.android.gms.measurement.internal.zzjo;
import com.google.android.gms.measurement.internal.zzjp;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class AppMeasurementJobService extends JobService implements zzjo {
    private zzjp<AppMeasurementJobService> zza;

    private final zzjp<AppMeasurementJobService> zzd() {
        if (this.zza == null) {
            this.zza = new zzjp<>(this);
        }
        return this.zza;
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

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters params) {
        zzd().zzi(params);
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        zzd().zzj(intent);
        return true;
    }

    @Override // com.google.android.gms.measurement.internal.zzjo
    public final void zza(Intent intent) {
    }

    @Override // com.google.android.gms.measurement.internal.zzjo
    public final void zzb(JobParameters jobParameters, boolean z) {
        jobFinished(jobParameters, false);
    }

    @Override // com.google.android.gms.measurement.internal.zzjo
    public final boolean zzc(int i) {
        throw new UnsupportedOperationException();
    }
}
