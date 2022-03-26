package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzin implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ zzjj zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzin(zzjj zzjj, zzp zzp) {
        this.zzb = zzjj;
        this.zza = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzdz zzdz = this.zzb.zzb;
        if (zzdz == null) {
            this.zzb.zzs.zzay().zzd().zza("Failed to reset data on the service: not connected to service");
            return;
        }
        try {
            Preconditions.checkNotNull(this.zza);
            zzdz.zzm(this.zza);
        } catch (RemoteException e) {
            this.zzb.zzs.zzay().zzd().zzb("Failed to reset data on the service: remote exception", e);
        }
        this.zzb.zzQ();
    }
}
