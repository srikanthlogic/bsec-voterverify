package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzjh implements Runnable {
    final /* synthetic */ zzji zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjh(zzji zzji) {
        this.zza = zzji;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.zza.zzb = null;
        this.zza.zza.zzP();
    }
}
