package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzio implements Runnable {
    final /* synthetic */ AtomicReference zza;
    final /* synthetic */ zzp zzb;
    final /* synthetic */ zzjj zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzio(zzjj zzjj, AtomicReference atomicReference, zzp zzp) {
        this.zzc = zzjj;
        this.zza = atomicReference;
        this.zzb = zzp;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        synchronized (this.zza) {
            try {
            } catch (RemoteException e) {
                this.zzc.zzs.zzay().zzd().zzb("Failed to get app instance id", e);
                atomicReference = this.zza;
            }
            if (!this.zzc.zzs.zzm().zzc().zzk()) {
                this.zzc.zzs.zzay().zzl().zza("Analytics storage consent denied; will not get app instance id");
                ((zze) this.zzc).zzs.zzq().zzN(null);
                this.zzc.zzs.zzm().zze.zzb(null);
                this.zza.set(null);
                this.zza.notify();
                return;
            }
            zzdz zzdz = this.zzc.zzb;
            if (zzdz == null) {
                this.zzc.zzs.zzay().zzd().zza("Failed to get app instance id");
                this.zza.notify();
                return;
            }
            Preconditions.checkNotNull(this.zzb);
            this.zza.set(zzdz.zzd(this.zzb));
            String str = (String) this.zza.get();
            if (str != null) {
                ((zze) this.zzc).zzs.zzq().zzN(str);
                this.zzc.zzs.zzm().zze.zzb(str);
            }
            this.zzc.zzQ();
            atomicReference = this.zza;
            atomicReference.notify();
        }
    }
}
