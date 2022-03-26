package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzie implements Runnable {
    final /* synthetic */ zzic zza;
    final /* synthetic */ zzic zzb;
    final /* synthetic */ long zzc;
    final /* synthetic */ boolean zzd;
    final /* synthetic */ zzij zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzie(zzij zzij, zzic zzic, zzic zzic2, long j, boolean z) {
        this.zze = zzij;
        this.zza = zzic;
        this.zzb = zzic2;
        this.zzc = j;
        this.zzd = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zze.zzB(this.zza, this.zzb, this.zzc, this.zzd, null);
    }
}
