package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhc implements Runnable {
    final /* synthetic */ AtomicReference zza;
    final /* synthetic */ boolean zzb;
    final /* synthetic */ zzhv zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhc(zzhv zzhv, AtomicReference atomicReference, boolean z) {
        this.zzc = zzhv;
        this.zza = atomicReference;
        this.zzb = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ((zze) this.zzc).zzs.zzt().zzx(this.zza, this.zzb);
    }
}
