package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzpl;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzgd implements Runnable {
    final /* synthetic */ zzat zza;
    final /* synthetic */ zzp zzb;
    final /* synthetic */ zzgk zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgd(zzgk zzgk, zzat zzat, zzp zzp) {
        this.zzc = zzgk;
        this.zza = zzat;
        this.zzb = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzat zzb = this.zzc.zzb(this.zza, this.zzb);
        zzpl.zzc();
        if (this.zzc.zza.zzg().zzs(null, zzdw.zzav)) {
            this.zzc.zzw(zzb, this.zzb);
        } else {
            this.zzc.zzB(zzb, this.zzb);
        }
    }
}
