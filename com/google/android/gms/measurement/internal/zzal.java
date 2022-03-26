package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzal implements Runnable {
    final /* synthetic */ zzgn zza;
    final /* synthetic */ zzam zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzal(zzam zzam, zzgn zzgn) {
        this.zzb = zzam;
        this.zza = zzgn;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.zzaw();
        if (zzaa.zza()) {
            this.zza.zzaz().zzp(this);
            return;
        }
        boolean zze = this.zzb.zze();
        this.zzb.zzd = 0;
        if (zze) {
            this.zzb.zzc();
        }
    }
}
