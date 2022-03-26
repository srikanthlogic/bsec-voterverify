package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzjq implements Runnable {
    final /* synthetic */ long zza;
    final /* synthetic */ zzjy zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjq(zzjy zzjy, long j) {
        this.zzb = zzjy;
        this.zza = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjy.zzl(this.zzb, this.zza);
    }
}
