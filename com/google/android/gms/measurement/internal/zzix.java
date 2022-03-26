package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzix implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ zzjj zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzix(zzjj zzjj, zzp zzp) {
        this.zzb = zzjj;
        this.zza = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzdz zzdz = this.zzb.zzb;
        if (zzdz == null) {
            this.zzb.zzs.zzay().zzd().zza("Failed to send consent settings to service");
            return;
        }
        try {
            Preconditions.checkNotNull(this.zza);
            zzdz.zzp(this.zza);
            this.zzb.zzQ();
        } catch (RemoteException e) {
            this.zzb.zzs.zzay().zzd().zzb("Failed to send consent settings to the service", e);
        }
    }
}
