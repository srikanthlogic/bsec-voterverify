package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzfu implements Runnable {
    final /* synthetic */ zzab zza;
    final /* synthetic */ zzp zzb;
    final /* synthetic */ zzgk zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfu(zzgk zzgk, zzab zzab, zzp zzp) {
        this.zzc = zzgk;
        this.zza = zzab;
        this.zzb = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzc.zza.zzA();
        if (this.zza.zzc.zza() == null) {
            this.zzc.zza.zzN(this.zza, this.zzb);
        } else {
            this.zzc.zza.zzS(this.zza, this.zzb);
        }
    }
}
