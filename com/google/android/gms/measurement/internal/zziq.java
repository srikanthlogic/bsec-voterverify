package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zziq implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ zzjj zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziq(zzjj zzjj, zzp zzp) {
        this.zzb = zzjj;
        this.zza = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzdz zzdz = this.zzb.zzb;
        if (zzdz == null) {
            this.zzb.zzs.zzay().zzd().zza("Discarding data. Failed to send app launch");
            return;
        }
        try {
            Preconditions.checkNotNull(this.zza);
            zzdz.zzj(this.zza);
            ((zze) this.zzb).zzs.zzi().zzm();
            this.zzb.zzD(zzdz, null, this.zza);
            this.zzb.zzQ();
        } catch (RemoteException e) {
            this.zzb.zzs.zzay().zzd().zzb("Failed to send app launch to the service", e);
        }
    }
}
