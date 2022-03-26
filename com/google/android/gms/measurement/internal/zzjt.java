package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjt implements Runnable {
    final long zza;
    final long zzb;
    final /* synthetic */ zzju zzc;

    public zzjt(zzju zzju, long j, long j2) {
        this.zzc = zzju;
        this.zza = j;
        this.zzb = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzc.zza.zzs.zzaz().zzp(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzjs
            @Override // java.lang.Runnable
            public final void run() {
                zzjt zzjt = zzjt.this;
                zzju zzju = zzjt.zzc;
                long j = zzjt.zza;
                long j2 = zzjt.zzb;
                zzju.zza.zzg();
                zzju.zza.zzs.zzay().zzc().zza("Application going to the background");
                boolean z = true;
                zzju.zza.zzs.zzm().zzl.zza(true);
                Bundle bundle = new Bundle();
                if (!zzju.zza.zzs.zzf().zzu()) {
                    zzju.zza.zzb.zzb(j2);
                    if (zzju.zza.zzs.zzf().zzs(null, zzdw.zzag)) {
                        zzjw zzjw = zzju.zza.zzb;
                        long j3 = zzjw.zzb;
                        zzjw.zzb = j2;
                        bundle.putLong("_et", j2 - j3);
                        zzku.zzJ(((zze) zzju.zza).zzs.zzs().zzj(true), bundle, true);
                    } else {
                        z = false;
                    }
                    zzju.zza.zzb.zzd(false, z, j2);
                }
                ((zze) zzju.zza).zzs.zzq().zzG(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_ab", j, bundle);
            }
        });
    }
}
