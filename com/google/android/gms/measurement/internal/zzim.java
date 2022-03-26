package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzim implements Runnable {
    final /* synthetic */ AtomicReference zza;
    final /* synthetic */ zzp zzb;
    final /* synthetic */ boolean zzc;
    final /* synthetic */ zzjj zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzim(zzjj zzjj, AtomicReference atomicReference, zzp zzp, boolean z) {
        this.zzd = zzjj;
        this.zza = atomicReference;
        this.zzb = zzp;
        this.zzc = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        zzdz zzdz;
        synchronized (this.zza) {
            try {
                zzdz = this.zzd.zzb;
            } catch (RemoteException e) {
                this.zzd.zzs.zzay().zzd().zzb("Failed to get all user properties; remote exception", e);
                atomicReference = this.zza;
            }
            if (zzdz == null) {
                this.zzd.zzs.zzay().zzd().zza("Failed to get all user properties; not connected to service");
                this.zza.notify();
                return;
            }
            Preconditions.checkNotNull(this.zzb);
            this.zza.set(zzdz.zze(this.zzb, this.zzc));
            this.zzd.zzQ();
            atomicReference = this.zza;
            atomicReference.notify();
        }
    }
}
