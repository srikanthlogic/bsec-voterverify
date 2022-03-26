package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import java.lang.Thread;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfm implements Thread.UncaughtExceptionHandler {
    final /* synthetic */ zzfp zza;
    private final String zzb;

    public zzfm(zzfp zzfp, String str) {
        this.zza = zzfp;
        Preconditions.checkNotNull(str);
        this.zzb = str;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final synchronized void uncaughtException(Thread thread, Throwable th) {
        this.zza.zzs.zzay().zzd().zzb(this.zzb, th);
    }
}
