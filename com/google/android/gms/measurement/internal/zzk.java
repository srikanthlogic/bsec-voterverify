package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-sdk@@19.0.1 */
/* loaded from: classes.dex */
final class zzk implements Runnable {
    final /* synthetic */ zzn zza;
    final /* synthetic */ AppMeasurementDynamiteService zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzk(AppMeasurementDynamiteService appMeasurementDynamiteService, zzn zzn) {
        this.zzb = appMeasurementDynamiteService;
        this.zza = zzn;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zza.zzq().zzS(this.zza);
    }
}
