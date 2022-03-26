package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhn implements Runnable {
    final /* synthetic */ AtomicReference zza;
    final /* synthetic */ zzhv zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhn(zzhv zzhv, AtomicReference atomicReference) {
        this.zzb = zzhv;
        this.zza = atomicReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zza) {
            this.zza.set(Double.valueOf(this.zzb.zzs.zzf().zza(((zze) this.zzb).zzs.zzh().zzl(), zzdw.zzN)));
            this.zza.notify();
        }
    }
}
