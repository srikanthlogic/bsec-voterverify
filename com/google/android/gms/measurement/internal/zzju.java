package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzju {
    final /* synthetic */ zzjy zza;
    private zzjt zzb;

    public zzju(zzjy zzjy) {
        this.zza = zzjy;
    }

    public final void zza(long j) {
        this.zzb = new zzjt(this, this.zza.zzs.zzav().currentTimeMillis(), j);
        this.zza.zzd.postDelayed(this.zzb, 2000);
    }

    public final void zzb() {
        this.zza.zzg();
        if (this.zzb != null) {
            this.zza.zzd.removeCallbacks(this.zzb);
        }
        this.zza.zzs.zzm().zzl.zza(false);
    }
}
