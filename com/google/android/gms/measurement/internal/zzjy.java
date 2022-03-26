package com.google.android.gms.measurement.internal;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.internal.measurement.zzby;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjy extends zzf {
    protected final zzjx zza = new zzjx(this);
    protected final zzjw zzb = new zzjw(this);
    protected final zzju zzc = new zzju(this);
    private Handler zzd;

    public zzjy(zzfs zzfs) {
        super(zzfs);
    }

    public static /* bridge */ /* synthetic */ void zzj(zzjy zzjy, long j) {
        zzjy.zzg();
        zzjy.zzm();
        zzjy.zzs.zzay().zzj().zzb("Activity paused, time", Long.valueOf(j));
        zzjy.zzc.zza(j);
        if (zzjy.zzs.zzf().zzu()) {
            zzjy.zzb.zzb(j);
        }
    }

    public static /* bridge */ /* synthetic */ void zzl(zzjy zzjy, long j) {
        zzjy.zzg();
        zzjy.zzm();
        zzjy.zzs.zzay().zzj().zzb("Activity resumed, time", Long.valueOf(j));
        if (zzjy.zzs.zzf().zzu() || zzjy.zzs.zzm().zzl.zzb()) {
            zzjy.zzb.zzc(j);
        }
        zzjy.zzc.zzb();
        zzjx zzjx = zzjy.zza;
        zzjx.zza.zzg();
        if (zzjx.zza.zzs.zzJ()) {
            zzjx.zzb(zzjx.zza.zzs.zzav().currentTimeMillis(), false);
        }
    }

    public final void zzm() {
        zzg();
        if (this.zzd == null) {
            this.zzd = new zzby(Looper.getMainLooper());
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzf
    protected final boolean zzf() {
        return false;
    }
}
