package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzis implements Runnable {
    final /* synthetic */ zzp zza;
    final /* synthetic */ Bundle zzb;
    final /* synthetic */ zzjj zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzis(zzjj zzjj, zzp zzp, Bundle bundle) {
        this.zzc = zzjj;
        this.zza = zzp;
        this.zzb = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzdz zzdz = this.zzc.zzb;
        if (zzdz == null) {
            this.zzc.zzs.zzay().zzd().zza("Failed to send default event parameters to service");
            return;
        }
        try {
            Preconditions.checkNotNull(this.zza);
            zzdz.zzr(this.zzb, this.zza);
        } catch (RemoteException e) {
            this.zzc.zzs.zzay().zzd().zzb("Failed to send default event parameters to service", e);
        }
    }
}
