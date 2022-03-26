package com.google.android.gms.measurement;

import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.internal.zzgs;
import com.google.android.gms.measurement.internal.zzgt;
import com.google.android.gms.measurement.internal.zzhw;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzb extends zzd {
    private final zzhw zza;

    public zzb(zzhw zzhw) {
        super(null);
        Preconditions.checkNotNull(zzhw);
        this.zza = zzhw;
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final int zza(String str) {
        return this.zza.zza(str);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final long zzb() {
        return this.zza.zzb();
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Boolean zzc() {
        return (Boolean) this.zza.zzg(4);
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Double zzd() {
        return (Double) this.zza.zzg(2);
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Integer zze() {
        return (Integer) this.zza.zzg(3);
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Long zzf() {
        return (Long) this.zza.zzg(1);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final Object zzg(int i) {
        return this.zza.zzg(i);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final String zzh() {
        return this.zza.zzh();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final String zzi() {
        return this.zza.zzi();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final String zzj() {
        return this.zza.zzj();
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final String zzk() {
        return this.zza.zzk();
    }

    @Override // com.google.android.gms.measurement.zzd
    public final String zzl() {
        return (String) this.zza.zzg(0);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final List<Bundle> zzm(String str, String str2) {
        return this.zza.zzm(str, str2);
    }

    @Override // com.google.android.gms.measurement.zzd
    public final Map<String, Object> zzn(boolean z) {
        return this.zza.zzo(null, null, z);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final Map<String, Object> zzo(String str, String str2, boolean z) {
        return this.zza.zzo(str, str2, z);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzp(String str) {
        this.zza.zzp(str);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzq(String str, String str2, Bundle bundle) {
        this.zza.zzq(str, str2, bundle);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzr(String str) {
        this.zza.zzr(str);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzs(String str, String str2, Bundle bundle) {
        this.zza.zzs(str, str2, bundle);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzt(String str, String str2, Bundle bundle, long j) {
        this.zza.zzt(str, str2, bundle, j);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzu(zzgt zzgt) {
        this.zza.zzu(zzgt);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzv(Bundle bundle) {
        this.zza.zzv(bundle);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzw(zzgs zzgs) {
        this.zza.zzw(zzgs);
    }

    @Override // com.google.android.gms.measurement.internal.zzhw
    public final void zzx(zzgt zzgt) {
        this.zza.zzx(zzgt);
    }
}
