package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjv extends zzam {
    final /* synthetic */ zzjw zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzjv(zzjw zzjw, zzgn zzgn) {
        super(zzgn);
        this.zza = zzjw;
    }

    @Override // com.google.android.gms.measurement.internal.zzam
    public final void zzc() {
        zzjw zzjw = this.zza;
        zzjw.zzc.zzg();
        zzjw.zzd(false, false, zzjw.zzc.zzs.zzav().elapsedRealtime());
        ((zze) zzjw.zzc).zzs.zzd().zzf(zzjw.zzc.zzs.zzav().elapsedRealtime());
    }
}
