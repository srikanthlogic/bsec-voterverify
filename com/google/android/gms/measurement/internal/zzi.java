package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzcf;
/* compiled from: com.google.android.gms:play-services-measurement-sdk@@19.0.1 */
/* loaded from: classes.dex */
final class zzi implements Runnable {
    final /* synthetic */ zzcf zza;
    final /* synthetic */ zzat zzb;
    final /* synthetic */ String zzc;
    final /* synthetic */ AppMeasurementDynamiteService zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzi(AppMeasurementDynamiteService appMeasurementDynamiteService, zzcf zzcf, zzat zzat, String str) {
        this.zzd = appMeasurementDynamiteService;
        this.zza = zzcf;
        this.zzb = zzat;
        this.zzc = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzd.zza.zzt().zzB(this.zza, this.zzb, this.zzc);
    }
}
