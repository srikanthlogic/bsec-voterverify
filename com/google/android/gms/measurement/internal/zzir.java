package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzir implements Runnable {
    final /* synthetic */ zzic zza;
    final /* synthetic */ zzjj zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzir(zzjj zzjj, zzic zzic) {
        this.zzb = zzjj;
        this.zza = zzic;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzdz zzdz = this.zzb.zzb;
        if (zzdz == null) {
            this.zzb.zzs.zzay().zzd().zza("Failed to send current screen to service");
            return;
        }
        try {
            zzic zzic = this.zza;
            if (zzic == null) {
                zzdz.zzq(0, null, null, this.zzb.zzs.zzau().getPackageName());
            } else {
                zzdz.zzq(zzic.zzc, zzic.zza, zzic.zzb, this.zzb.zzs.zzau().getPackageName());
            }
            this.zzb.zzQ();
        } catch (RemoteException e) {
            this.zzb.zzs.zzay().zzd().zzb("Failed to send current screen to the service", e);
        }
    }
}
