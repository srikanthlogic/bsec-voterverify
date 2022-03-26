package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zziz implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ boolean zzb;
    final /* synthetic */ zzab zzc;
    final /* synthetic */ zzab zzd;
    final /* synthetic */ zzjj zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziz(zzjj zzjj, boolean z, zzp zzp, boolean z2, zzab zzab, zzab zzab2) {
        this.zze = zzjj;
        this.zza = zzp;
        this.zzb = z2;
        this.zzc = zzab;
        this.zzd = zzab2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzab zzab;
        zzdz zzdz = this.zze.zzb;
        if (zzdz == null) {
            this.zze.zzs.zzay().zzd().zza("Discarding data. Failed to send conditional user property to service");
            return;
        }
        Preconditions.checkNotNull(this.zza);
        zzjj zzjj = this.zze;
        if (this.zzb) {
            zzab = null;
        } else {
            zzab = this.zzc;
        }
        zzjj.zzD(zzdz, zzab, this.zza);
        this.zze.zzQ();
    }
}
