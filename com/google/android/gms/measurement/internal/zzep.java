package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzep implements Runnable {
    final /* synthetic */ boolean zza;
    final /* synthetic */ zzeq zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzep(zzeq zzeq, boolean z) {
        this.zzb = zzeq;
        this.zza = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zzb.zzI(this.zza);
    }
}
