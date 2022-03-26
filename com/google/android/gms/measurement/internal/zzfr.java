package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfr implements Runnable {
    final /* synthetic */ zzgu zza;
    final /* synthetic */ zzfs zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfr(zzfs zzfs, zzgu zzgu) {
        this.zzb = zzfs;
        this.zza = zzgu;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzfs.zzA(this.zzb, this.zza);
        this.zzb.zzH(this.zza.zzg);
    }
}
