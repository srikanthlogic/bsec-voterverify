package com.google.android.gms.measurement;

import android.os.Bundle;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.internal.zzfs;
import com.google.android.gms.measurement.internal.zzgs;
import com.google.android.gms.measurement.internal.zzgt;
import com.google.android.gms.measurement.internal.zzhv;
import com.google.android.gms.measurement.internal.zzkq;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zza extends zzd {
    private final zzfs zza;
    private final zzhv zzb;

    public zza(zzfs zzfs) {
        super(null);
        Preconditions.checkNotNull(zzfs);
        this.zza = zzfs;
        this.zzb = zzfs.zzq();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final int zza(String str) {
        this.zzb.zzh(str);
        return 25;
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final long zzb() {
        return this.zza.zzv().zzq();
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Boolean zzc() {
        return this.zzb.zzi();
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Double zzd() {
        return this.zzb.zzj();
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Integer zze() {
        return this.zzb.zzl();
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Long zzf() {
        return this.zzb.zzm();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final String zzh() {
        return this.zzb.zzo();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final String zzi() {
        return this.zzb.zzp();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final String zzj() {
        return this.zzb.zzq();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final String zzk() {
        return this.zzb.zzo();
    }

    @Override // com.google.android.gms.measurement.zzd
    public final String zzl() {
        return this.zzb.zzr();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final List<Bundle> zzm(String str, String str2) {
        return this.zzb.zzs(str, str2);
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Map<String, Object> zzn(boolean z) {
        List<zzkq> zzt = this.zzb.zzt(z);
        ArrayMap arrayMap = new ArrayMap(zzt.size());
        for (zzkq zzkq : zzt) {
            Object zza = zzkq.zza();
            if (zza != null) {
                arrayMap.put(zzkq.zzb, zza);
            }
        }
        return arrayMap;
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final Map<String, Object> zzo(String str, String str2, boolean z) {
        return this.zzb.zzu(str, str2, z);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzp(String str) {
        this.zza.zzd().zzd(str, this.zza.zzav().elapsedRealtime());
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzq(String str, String str2, Bundle bundle) {
        this.zza.zzq().zzz(str, str2, bundle);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzr(String str) {
        this.zza.zzd().zze(str, this.zza.zzav().elapsedRealtime());
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzs(String str, String str2, Bundle bundle) {
        this.zzb.zzC(str, str2, bundle);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzt(String str, String str2, Bundle bundle, long j) {
        this.zzb.zzD(str, str2, bundle, true, false, j);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzu(zzgt zzgt) {
        this.zzb.zzI(zzgt);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzv(Bundle bundle) {
        this.zzb.zzO(bundle);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzw(zzgs zzgs) {
        this.zzb.zzS(zzgs);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzx(zzgt zzgt) {
        this.zzb.zzY(zzgt);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final Object zzg(int i) {
        if (i == 0) {
            return this.zzb.zzr();
        }
        if (i == 1) {
            return this.zzb.zzm();
        }
        if (i == 2) {
            return this.zzb.zzj();
        }
        if (i != 3) {
            return this.zzb.zzi();
        }
        return this.zzb.zzl();
    }
}
