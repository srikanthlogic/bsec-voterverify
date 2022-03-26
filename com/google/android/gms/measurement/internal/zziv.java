package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zziv extends zzam {
    final /* synthetic */ zzjj zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zziv(zzjj zzjj, zzgn zzgn) {
        super(zzgn);
        this.zza = zzjj;
    }

    @Override // com.google.android.gms.measurement.internal.zzam
    public final void zzc() {
        this.zza.zzs.zzay().zzk().zza("Tasks have been queued for a long time");
    }
}
