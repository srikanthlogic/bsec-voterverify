package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzje implements Runnable {
    final /* synthetic */ ComponentName zza;
    final /* synthetic */ zzji zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzje(zzji zzji, ComponentName componentName) {
        this.zzb = zzji;
        this.zza = componentName;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjj.zzo(this.zzb.zza, this.zza);
    }
}
