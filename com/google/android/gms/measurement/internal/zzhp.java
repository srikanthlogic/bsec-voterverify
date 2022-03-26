package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhp implements Runnable {
    final /* synthetic */ zzag zza;
    final /* synthetic */ long zzb;
    final /* synthetic */ int zzc;
    final /* synthetic */ long zzd;
    final /* synthetic */ boolean zze;
    final /* synthetic */ zzhv zzf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhp(zzhv zzhv, zzag zzag, long j, int i, long j2, boolean z) {
        this.zzf = zzhv;
        this.zza = zzag;
        this.zzb = j;
        this.zzc = i;
        this.zzd = j2;
        this.zze = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzf.zzU(this.zza);
        this.zzf.zzK(this.zzb, false);
        zzhv.zzv(this.zzf, this.zza, this.zzc, this.zzd, true, this.zze);
    }
}
