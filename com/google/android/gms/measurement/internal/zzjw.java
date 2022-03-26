package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.google.android.gms.internal.measurement.zzoh;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjw {
    protected long zza;
    protected long zzb;
    final /* synthetic */ zzjy zzc;
    private final zzam zzd;

    public zzjw(zzjy zzjy) {
        this.zzc = zzjy;
        this.zzd = new zzjv(this, this.zzc.zzs);
        long elapsedRealtime = zzjy.zzs.zzav().elapsedRealtime();
        this.zza = elapsedRealtime;
        this.zzb = elapsedRealtime;
    }

    public final void zza() {
        this.zzd.zzb();
        this.zza = 0;
        this.zzb = 0;
    }

    public final void zzb(long j) {
        this.zzd.zzb();
    }

    public final void zzc(long j) {
        this.zzc.zzg();
        this.zzd.zzb();
        this.zza = j;
        this.zzb = j;
    }

    public final boolean zzd(boolean z, boolean z2, long j) {
        this.zzc.zzg();
        this.zzc.zza();
        zzoh.zzc();
        if (!this.zzc.zzs.zzf().zzs(null, zzdw.zzaj)) {
            this.zzc.zzs.zzm().zzj.zzb(this.zzc.zzs.zzav().currentTimeMillis());
        } else if (this.zzc.zzs.zzJ()) {
            this.zzc.zzs.zzm().zzj.zzb(this.zzc.zzs.zzav().currentTimeMillis());
        }
        long j2 = j - this.zza;
        if (z || j2 >= 1000) {
            if (!z2) {
                j2 = j - this.zzb;
                this.zzb = j;
            }
            this.zzc.zzs.zzay().zzj().zzb("Recording user engagement, ms", Long.valueOf(j2));
            Bundle bundle = new Bundle();
            bundle.putLong("_et", j2);
            zzku.zzJ(((zze) this.zzc).zzs.zzs().zzj(!this.zzc.zzs.zzf().zzu()), bundle, true);
            if (!this.zzc.zzs.zzf().zzs(null, zzdw.zzT) && z2) {
                bundle.putLong("_fr", 1);
            }
            if (!this.zzc.zzs.zzf().zzs(null, zzdw.zzT) || !z2) {
                ((zze) this.zzc).zzs.zzq().zzF(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_e", bundle);
            }
            this.zza = j;
            this.zzd.zzb();
            this.zzd.zzd(3600000);
            return true;
        }
        this.zzc.zzs.zzay().zzj().zzb("Screen exposed for less than 1000 ms. Event not sent. time", Long.valueOf(j2));
        return false;
    }
}
