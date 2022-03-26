package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzf {
    final zzax zza = new zzax();
    final zzg zzc = new zzg(null, this.zza);
    final zzg zzb = this.zzc.zza();
    final zzj zzd = new zzj();

    public zzf() {
        this.zzc.zzg("require", new zzw(this.zzd));
        this.zzd.zza("internal.platform", zze.zza);
        this.zzc.zzg("runtime.counter", new zzah(Double.valueOf(0.0d)));
    }

    public final zzap zza(zzg zzg, zzgt... zzgtArr) {
        zzap zzap = zzap.zzf;
        for (zzgt zzgt : zzgtArr) {
            zzap = zzi.zza(zzgt);
            zzh.zzc(this.zzc);
            if ((zzap instanceof zzaq) || (zzap instanceof zzao)) {
                zzap = this.zza.zza(zzg, zzap);
            }
        }
        return zzap;
    }
}
