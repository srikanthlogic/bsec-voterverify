package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzcf;
/* compiled from: com.google.android.gms:play-services-measurement-sdk@@19.0.1 */
/* loaded from: classes.dex */
final class zzm implements Runnable {
    final /* synthetic */ zzcf zza;
    final /* synthetic */ AppMeasurementDynamiteService zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzm(AppMeasurementDynamiteService appMeasurementDynamiteService, zzcf zzcf) {
        this.zzb = appMeasurementDynamiteService;
        this.zza = zzcf;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zza.zzv().zzO(this.zza, this.zzb.zza.zzI());
    }
}
