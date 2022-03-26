package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzif implements Runnable {
    final /* synthetic */ zzij zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzif(zzij zzij) {
        this.zza = zzij;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzij zzij = this.zza;
        zzij.zza = zzij.zzh;
    }
}
