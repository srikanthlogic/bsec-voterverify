package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgy implements Runnable {
    final /* synthetic */ zzhv zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgy(zzhv zzhv) {
        this.zza = zzhv;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.zzb.zzb();
    }
}
