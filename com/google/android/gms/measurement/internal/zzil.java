package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzil implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ boolean zzb;
    final /* synthetic */ zzkq zzc;
    final /* synthetic */ zzjj zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzil(zzjj zzjj, zzp zzp, boolean z, zzkq zzkq) {
        this.zzd = zzjj;
        this.zza = zzp;
        this.zzb = z;
        this.zzc = zzkq;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzkq zzkq;
        zzdz zzdz = this.zzd.zzb;
        if (zzdz == null) {
            this.zzd.zzs.zzay().zzd().zza("Discarding data. Failed to set user property");
            return;
        }
        Preconditions.checkNotNull(this.zza);
        zzjj zzjj = this.zzd;
        if (this.zzb) {
            zzkq = null;
        } else {
            zzkq = this.zzc;
        }
        zzjj.zzD(zzdz, zzkq, this.zza);
        this.zzd.zzQ();
    }
}
