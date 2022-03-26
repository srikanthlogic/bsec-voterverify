package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzfo;
import com.google.android.gms.internal.measurement.zzfy;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkm {
    zzfy zza;
    List<Long> zzb;
    List<zzfo> zzc;
    long zzd;
    final /* synthetic */ zzkn zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzkm(zzkn zzkn, zzkl zzkl) {
        this.zze = zzkn;
    }

    private static final long zzb(zzfo zzfo) {
        return ((zzfo.zzd() / 1000) / 60) / 60;
    }

    public final boolean zza(long j, zzfo zzfo) {
        Preconditions.checkNotNull(zzfo);
        if (this.zzc == null) {
            this.zzc = new ArrayList();
        }
        if (this.zzb == null) {
            this.zzb = new ArrayList();
        }
        if (this.zzc.size() > 0 && zzb(this.zzc.get(0)) != zzb(zzfo)) {
            return false;
        }
        long zzbt = this.zzd + ((long) zzfo.zzbt());
        this.zze.zzg();
        if (zzbt >= ((long) Math.max(0, zzdw.zzh.zza(null).intValue()))) {
            return false;
        }
        this.zzd = zzbt;
        this.zzc.add(zzfo);
        this.zzb.add(Long.valueOf(j));
        int size = this.zzc.size();
        this.zze.zzg();
        if (size >= Math.max(1, zzdw.zzi.zza(null).intValue())) {
            return false;
        }
        return true;
    }
}
