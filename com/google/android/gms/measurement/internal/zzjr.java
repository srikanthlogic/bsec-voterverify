package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzjr implements Runnable {
    final /* synthetic */ long zza;
    final /* synthetic */ zzjy zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjr(zzjy zzjy, long j) {
        this.zzb = zzjy;
        this.zza = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjy.zzj(this.zzb, this.zza);
    }
}
