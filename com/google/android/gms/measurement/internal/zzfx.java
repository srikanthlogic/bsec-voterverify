package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzfx implements Callable<List<zzks>> {
    final /* synthetic */ String zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ String zzc;
    final /* synthetic */ zzgk zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfx(zzgk zzgk, String str, String str2, String str3) {
        this.zzd = zzgk;
        this.zza = str;
        this.zzb = str2;
        this.zzc = str3;
    }

    @Override // java.util.concurrent.Callable
    public final /* bridge */ /* synthetic */ List<zzks> call() throws Exception {
        this.zzd.zza.zzA();
        return this.zzd.zza.zzi().zzv(this.zza, this.zzb, this.zzc);
    }
}
