package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzig implements Runnable {
    final /* synthetic */ long zza;
    final /* synthetic */ zzij zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzig(zzij zzij, long j) {
        this.zzb = zzij;
        this.zza = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ((zze) this.zzb).zzs.zzd().zzf(this.zza);
        this.zzb.zza = null;
    }
}
