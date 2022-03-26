package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zziy implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ boolean zzb;
    final /* synthetic */ zzat zzc;
    final /* synthetic */ String zzd;
    final /* synthetic */ zzjj zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziy(zzjj zzjj, boolean z, zzp zzp, boolean z2, zzat zzat, String str) {
        this.zze = zzjj;
        this.zza = zzp;
        this.zzb = z2;
        this.zzc = zzat;
        this.zzd = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzat zzat;
        zzdz zzdz = this.zze.zzb;
        if (zzdz == null) {
            this.zze.zzs.zzay().zzd().zza("Discarding data. Failed to send event to service");
            return;
        }
        Preconditions.checkNotNull(this.zza);
        zzjj zzjj = this.zze;
        if (this.zzb) {
            zzat = null;
        } else {
            zzat = this.zzc;
        }
        zzjj.zzD(zzdz, zzat, this.zza);
        this.zze.zzQ();
    }
}
