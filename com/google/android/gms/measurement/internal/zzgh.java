package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzgh implements Callable<List<zzks>> {
    final /* synthetic */ String zza;
    final /* synthetic */ zzgk zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgh(zzgk zzgk, String str) {
        this.zzb = zzgk;
        this.zza = str;
    }

    @Override // java.util.concurrent.Callable
    public final /* bridge */ /* synthetic */ List<zzks> call() throws Exception {
        this.zzb.zza.zzA();
        return this.zzb.zza.zzi().zzu(this.zza);
    }
}
