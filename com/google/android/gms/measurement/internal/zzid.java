package com.google.android.gms.measurement.internal;

import android.os.Bundle;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzid implements Runnable {
    final /* synthetic */ Bundle zza;
    final /* synthetic */ zzic zzb;
    final /* synthetic */ zzic zzc;
    final /* synthetic */ long zzd;
    final /* synthetic */ zzij zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzid(zzij zzij, Bundle bundle, zzic zzic, zzic zzic2, long j) {
        this.zze = zzij;
        this.zza = bundle;
        this.zzb = zzic;
        this.zzc = zzic2;
        this.zzd = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzij.zzp(this.zze, this.zza, this.zzb, this.zzc, this.zzd);
    }
}
