package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzcf;
/* compiled from: com.google.android.gms:play-services-measurement-sdk@@19.0.1 */
/* loaded from: classes.dex */
final class zzj implements Runnable {
    final /* synthetic */ zzcf zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ String zzc;
    final /* synthetic */ boolean zzd;
    final /* synthetic */ AppMeasurementDynamiteService zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzj(AppMeasurementDynamiteService appMeasurementDynamiteService, zzcf zzcf, String str, String str2, boolean z) {
        this.zze = appMeasurementDynamiteService;
        this.zza = zzcf;
        this.zzb = str;
        this.zzc = str2;
        this.zzd = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zze.zza.zzt().zzy(this.zza, this.zzb, this.zzc, this.zzd);
    }
}
