package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.Callable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzki implements Callable<String> {
    final /* synthetic */ zzp zza;
    final /* synthetic */ zzkn zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzki(zzkn zzkn, zzp zzp) {
        this.zzb = zzkn;
        this.zza = zzp;
    }

    @Override // java.util.concurrent.Callable
    public final /* bridge */ /* synthetic */ String call() throws Exception {
        if (this.zzb.zzh((String) Preconditions.checkNotNull(this.zza.zza)).zzk() && zzag.zzb(this.zza.zzv).zzk()) {
            return this.zzb.zzd(this.zza).zzu();
        }
        this.zzb.zzay().zzj().zza("Analytics storage consent denied. Returning null app instance id");
        return null;
    }
}
