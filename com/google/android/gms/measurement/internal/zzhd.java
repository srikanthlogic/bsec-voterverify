package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhd implements Runnable {
    final /* synthetic */ long zza;
    final /* synthetic */ zzhv zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhd(zzhv zzhv, long j) {
        this.zzb = zzhv;
        this.zza = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zzK(this.zza, true);
        ((zze) this.zzb).zzs.zzt().zzu(new AtomicReference<>());
    }
}
