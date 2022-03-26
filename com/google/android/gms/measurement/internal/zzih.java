package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzih implements Runnable {
    final /* synthetic */ zzic zza;
    final /* synthetic */ long zzb;
    final /* synthetic */ zzij zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzih(zzij zzij, zzic zzic, long j) {
        this.zzc = zzij;
        this.zza = zzic;
        this.zzb = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzc.zzC(this.zza, false, this.zzb);
        zzij zzij = this.zzc;
        zzij.zza = null;
        ((zze) zzij).zzs.zzt().zzG(null);
    }
}
