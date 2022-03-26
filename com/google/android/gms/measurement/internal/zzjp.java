package com.google.android.gms.measurement.internal;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.internal.zzjo;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjp<T extends Context & zzjo> {
    private final T zza;

    public zzjp(T t) {
        Preconditions.checkNotNull(t);
        this.zza = t;
    }

    private final zzei zzk() {
        return zzfs.zzp(this.zza, null, null).zzay();
    }

    public final int zza(Intent intent, int i, int i2) {
        zzfs zzp = zzfs.zzp(this.zza, null, null);
        zzei zzay = zzp.zzay();
        if (intent == null) {
            zzay.zzk().zza("AppMeasurementService started with null intent");
            return 2;
        }
        String action = intent.getAction();
        zzp.zzaw();
        zzay.zzj().zzc("Local AppMeasurementService called. startId, action", Integer.valueOf(i2), action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            zzh(new Runnable(i2, zzay, intent) { // from class: com.google.android.gms.measurement.internal.zzjl
                public final /* synthetic */ int zzb;
                public final /* synthetic */ zzei zzc;
                public final /* synthetic */ Intent zzd;

                {
                    this.zzb = r2;
                    this.zzc = r3;
                    this.zzd = r4;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    zzjp.this.zzc(this.zzb, this.zzc, this.zzd);
                }
            });
        }
        return 2;
    }

    public final IBinder zzb(Intent intent) {
        if (intent == null) {
            zzk().zzd().zza("onBind called with null intent");
            return null;
        }
        String action = intent.getAction();
        if ("com.google.android.gms.measurement.START".equals(action)) {
            return new zzgk(zzkn.zzt(this.zza), null);
        }
        zzk().zzk().zzb("onBind received unknown action", action);
        return null;
    }

    public final /* synthetic */ void zzc(int i, zzei zzei, Intent intent) {
        if (this.zza.zzc(i)) {
            zzei.zzj().zzb("Local AppMeasurementService processed last upload request. StartId", Integer.valueOf(i));
            zzk().zzj().zza("Completed wakeful intent.");
            this.zza.zza(intent);
        }
    }

    public final /* synthetic */ void zzd(zzei zzei, JobParameters jobParameters) {
        zzei.zzj().zza("AppMeasurementJobService processed last upload request.");
        this.zza.zzb(jobParameters, false);
    }

    public final void zze() {
        zzfs zzp = zzfs.zzp(this.zza, null, null);
        zzei zzay = zzp.zzay();
        zzp.zzaw();
        zzay.zzj().zza("Local AppMeasurementService is starting up");
    }

    public final void zzf() {
        zzfs zzp = zzfs.zzp(this.zza, null, null);
        zzei zzay = zzp.zzay();
        zzp.zzaw();
        zzay.zzj().zza("Local AppMeasurementService is shutting down");
    }

    public final void zzg(Intent intent) {
        if (intent == null) {
            zzk().zzd().zza("onRebind called with null intent");
            return;
        }
        zzk().zzj().zzb("onRebind called. action", intent.getAction());
    }

    public final void zzh(Runnable runnable) {
        zzkn zzt = zzkn.zzt(this.zza);
        zzt.zzaz().zzp(new zzjn(this, zzt, runnable));
    }

    public final boolean zzi(JobParameters jobParameters) {
        zzfs zzp = zzfs.zzp(this.zza, null, null);
        zzei zzay = zzp.zzay();
        String string = jobParameters.getExtras().getString("action");
        zzp.zzaw();
        zzay.zzj().zzb("Local AppMeasurementJobService called. action", string);
        if (!"com.google.android.gms.measurement.UPLOAD".equals(string)) {
            return true;
        }
        zzh(new Runnable(zzay, jobParameters) { // from class: com.google.android.gms.measurement.internal.zzjm
            public final /* synthetic */ zzei zzb;
            public final /* synthetic */ JobParameters zzc;

            {
                this.zzb = r2;
                this.zzc = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                zzjp.this.zzd(this.zzb, this.zzc);
            }
        });
        return true;
    }

    public final boolean zzj(Intent intent) {
        if (intent == null) {
            zzk().zzd().zza("onUnbind called with null intent");
            return true;
        }
        zzk().zzj().zzb("onUnbind called for intent. action", intent.getAction());
        return true;
    }
}
