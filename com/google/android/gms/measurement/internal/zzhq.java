package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhq implements Runnable {
    final /* synthetic */ zzag zza;
    final /* synthetic */ int zzb;
    final /* synthetic */ long zzc;
    final /* synthetic */ boolean zzd;
    final /* synthetic */ zzhv zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhq(zzhv zzhv, zzag zzag, int i, long j, boolean z) {
        this.zze = zzhv;
        this.zza = zzag;
        this.zzb = i;
        this.zzc = j;
        this.zzd = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zze.zzU(this.zza);
        zzhv.zzv(this.zze, this.zza, this.zzb, this.zzc, false, this.zzd);
    }
}
