package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzjg implements Runnable {
    final /* synthetic */ zzji zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjg(zzji zzji) {
        this.zza = zzji;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjj zzjj = this.zza.zza;
        Context zzau = zzjj.zzs.zzau();
        this.zza.zza.zzs.zzaw();
        zzjj.zzo(zzjj, new ComponentName(zzau, "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
