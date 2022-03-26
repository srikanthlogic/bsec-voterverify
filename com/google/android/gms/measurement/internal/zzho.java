package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzho implements Runnable {
    final /* synthetic */ Boolean zza;
    final /* synthetic */ zzhv zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzho(zzhv zzhv, Boolean bool) {
        this.zzb = zzhv;
        this.zza = bool;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zzZ(this.zza, true);
    }
}
