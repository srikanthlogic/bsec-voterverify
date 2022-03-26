package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzgi implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ zzgk zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgi(zzgk zzgk, zzp zzp) {
        this.zzb = zzgk;
        this.zza = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zza.zzA();
        this.zzb.zza.zzK(this.zza);
    }
}
