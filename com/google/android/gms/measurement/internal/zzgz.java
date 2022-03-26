package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzgz implements Runnable {
    final /* synthetic */ long zza;
    final /* synthetic */ zzhv zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgz(zzhv zzhv, long j) {
        this.zzb = zzhv;
        this.zza = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zzs.zzm().zzf.zzb(this.zza);
        this.zzb.zzs.zzay().zzc().zzb("Session timeout duration set", Long.valueOf(this.zza));
    }
}
