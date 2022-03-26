package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkf implements Runnable {
    final /* synthetic */ zzko zza;
    final /* synthetic */ zzkn zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzkf(zzkn zzkn, zzko zzko) {
        this.zzb = zzkn;
        this.zza = zzko;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzkn.zzy(this.zzb, this.zza);
        this.zzb.zzQ();
    }
}
