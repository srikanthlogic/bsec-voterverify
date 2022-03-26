package com.google.android.gms.measurement.internal;

import android.os.Handler;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzby;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzam {
    private static volatile Handler zza;
    private final zzgn zzb;
    private final Runnable zzc;
    private volatile long zzd;

    public zzam(zzgn zzgn) {
        Preconditions.checkNotNull(zzgn);
        this.zzb = zzgn;
        this.zzc = new zzal(this, zzgn);
    }

    private final Handler zzf() {
        Handler handler;
        if (zza != null) {
            return zza;
        }
        synchronized (zzam.class) {
            if (zza == null) {
                zza = new zzby(this.zzb.zzau().getMainLooper());
            }
            handler = zza;
        }
        return handler;
    }

    public final void zzb() {
        this.zzd = 0;
        zzf().removeCallbacks(this.zzc);
    }

    public abstract void zzc();

    public final void zzd(long j) {
        zzb();
        if (j >= 0) {
            this.zzd = this.zzb.zzav().currentTimeMillis();
            if (!zzf().postDelayed(this.zzc, j)) {
                this.zzb.zzay().zzd().zzb("Failed to schedule delayed post. time", Long.valueOf(j));
            }
        }
    }

    public final boolean zze() {
        return this.zzd != 0;
    }
}
