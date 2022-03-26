package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzkj implements Runnable {
    final /* synthetic */ String zza;
    final /* synthetic */ String zzb = "_err";
    final /* synthetic */ Bundle zzc;
    final /* synthetic */ zzkk zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzkj(zzkk zzkk, String str, String str2, Bundle bundle) {
        this.zzd = zzkk;
        this.zza = str;
        this.zzc = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzd.zza.zzE((zzat) Preconditions.checkNotNull(this.zzd.zza.zzv().zzz(this.zza, this.zzb, this.zzc, DebugKt.DEBUG_PROPERTY_VALUE_AUTO, this.zzd.zza.zzav().currentTimeMillis(), false, true)), this.zza);
    }
}
