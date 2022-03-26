package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzit extends zzam {
    final /* synthetic */ zzjj zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzit(zzjj zzjj, zzgn zzgn) {
        super(zzgn);
        this.zza = zzjj;
    }

    @Override // com.google.android.gms.measurement.internal.zzam
    public final void zzc() {
        zzjj zzjj = this.zza;
        zzjj.zzg();
        if (zzjj.zzL()) {
            zzjj.zzs.zzay().zzj().zza("Inactivity, disconnecting from the service");
            zzjj.zzs();
        }
    }
}
