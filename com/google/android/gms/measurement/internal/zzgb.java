package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzgb implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ zzgk zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgb(zzgk zzgk, zzp zzp) {
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
        zzkn.zzd(zzp);
    }
}
