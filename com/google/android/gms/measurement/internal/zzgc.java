package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzgc implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ zzgk zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgc(zzgk zzgk, zzp zzp) {
        this.zzb = zzgk;
        this.zza = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zza.zzA();
        zzkn zzkn = this.zzb.zza;
        zzp zzp = this.zza;
        zzkn.zzaz().zzg();
        zzkn.zzB();
        Preconditions.checkNotEmpty(zzp.zza);
        zzag zzb = zzag.zzb(zzp.zzv);
        zzag zzh = zzkn.zzh(zzp.zza);
        zzkn.zzay().zzj().zzc("Setting consent, package, consent", zzp.zza, zzb);
        zzkn.zzT(zzp.zza, zzb);
        if (zzb.zzm(zzh)) {
            zzkn.zzP(zzp);
        }
    }
}
