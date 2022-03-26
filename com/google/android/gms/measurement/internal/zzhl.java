package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhl implements Runnable {
    final /* synthetic */ AtomicReference zza;
    final /* synthetic */ zzhv zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhl(zzhv zzhv, AtomicReference atomicReference) {
        this.zzb = zzhv;
        this.zza = atomicReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zza) {
            this.zza.set(Long.valueOf(this.zzb.zzs.zzf().zzi(((zze) this.zzb).zzs.zzh().zzl(), zzdw.zzL)));
            this.zza.notify();
        }
    }
}
