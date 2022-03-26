package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzka extends zzam {
    final /* synthetic */ zzkb zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzka(zzkb zzkb, zzgn zzgn) {
        super(zzgn);
        this.zza = zzkb;
    }

    @Override // com.google.android.gms.measurement.internal.zzam
    public final void zzc() {
        this.zza.zza();
        this.zza.zzs.zzay().zzj().zza("Starting upload from DelayedRunnable");
        this.zza.zzf.zzV();
    }
}
