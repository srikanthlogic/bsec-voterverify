package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhh implements Runnable {
    final /* synthetic */ AtomicReference zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ String zzc;
    final /* synthetic */ zzhv zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhh(zzhv zzhv, AtomicReference atomicReference, String str, String str2, String str3) {
        this.zzd = zzhv;
        this.zza = atomicReference;
        this.zzb = str2;
        this.zzc = str3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzd.zzs.zzt().zzw(this.zza, null, this.zzb, this.zzc);
    }
}
