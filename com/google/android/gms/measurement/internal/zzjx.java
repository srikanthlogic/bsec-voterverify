package com.google.android.gms.measurement.internal;

import android.app.ActivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.internal.measurement.zzny;
import kotlinx.coroutines.DebugKt;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjx {
    final /* synthetic */ zzjy zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjx(zzjy zzjy) {
        this.zza = zzjy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza() {
        this.zza.zzg();
        if (this.zza.zzs.zzm().zzk(this.zza.zzs.zzav().currentTimeMillis())) {
            this.zza.zzs.zzm().zzg.zza(true);
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(runningAppProcessInfo);
            if (runningAppProcessInfo.importance == 100) {
                this.zza.zzs.zzay().zzj().zza("Detected application was in foreground");
                zzc(this.zza.zzs.zzav().currentTimeMillis(), false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzb(long j, boolean z) {
        this.zza.zzg();
        this.zza.zzm();
        if (this.zza.zzs.zzm().zzk(j)) {
            this.zza.zzs.zzm().zzg.zza(true);
        }
        this.zza.zzs.zzm().zzj.zzb(j);
        if (this.zza.zzs.zzm().zzg.zzb()) {
            zzc(j, z);
        }
    }

    final void zzc(long j, boolean z) {
        this.zza.zzg();
        if (this.zza.zzs.zzJ()) {
            this.zza.zzs.zzm().zzj.zzb(j);
            this.zza.zzs.zzay().zzj().zzb("Session started, time", Long.valueOf(this.zza.zzs.zzav().elapsedRealtime()));
            Long valueOf = Long.valueOf(j / 1000);
            ((zze) this.zza).zzs.zzq().zzX(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_sid", valueOf, j);
            this.zza.zzs.zzm().zzg.zza(false);
            Bundle bundle = new Bundle();
            bundle.putLong("_sid", valueOf.longValue());
            if (this.zza.zzs.zzf().zzs(null, zzdw.zzae) && z) {
                bundle.putLong("_aib", 1);
            }
            ((zze) this.zza).zzs.zzq().zzG(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_s", j, bundle);
            zzny.zzc();
            if (this.zza.zzs.zzf().zzs(null, zzdw.zzai)) {
                String zza = this.zza.zzs.zzm().zzo.zza();
                if (!TextUtils.isEmpty(zza)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("_ffr", zza);
                    ((zze) this.zza).zzs.zzq().zzG(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_ssr", j, bundle2);
                }
            }
        }
    }
}
