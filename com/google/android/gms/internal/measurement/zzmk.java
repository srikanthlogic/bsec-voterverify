package com.google.android.gms.internal.measurement;

import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzmk extends zzmi<zzmj, zzmj> {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final /* bridge */ /* synthetic */ int zza(zzmj zzmj) {
        return zzmj.zza();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final /* bridge */ /* synthetic */ int zzb(zzmj zzmj) {
        return zzmj.zzb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final /* bridge */ /* synthetic */ zzmj zzc(Object obj) {
        return ((zzjz) obj).zzc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final /* bridge */ /* synthetic */ zzmj zzd(zzmj zzmj, zzmj zzmj2) {
        zzmj zzmj3 = zzmj2;
        if (zzmj3.equals(zzmj.zzc())) {
            return zzmj;
        }
        return zzmj.zzd(zzmj, zzmj3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final /* bridge */ /* synthetic */ zzmj zze() {
        return zzmj.zze();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final /* bridge */ /* synthetic */ void zzf(zzmj zzmj, int i, long j) {
        zzmj.zzh(i << 3, Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final void zzg(Object obj) {
        ((zzjz) obj).zzc.zzf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final /* bridge */ /* synthetic */ void zzh(Object obj, zzmj zzmj) {
        ((zzjz) obj).zzc = zzmj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzmi
    public final /* bridge */ /* synthetic */ void zzi(zzmj zzmj, zzjh zzjh) throws IOException {
        zzmj.zzi(zzjh);
    }
}
