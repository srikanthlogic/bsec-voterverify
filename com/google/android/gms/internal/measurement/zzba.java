package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzba implements Comparator<zzap> {
    final /* synthetic */ zzai zza;
    final /* synthetic */ zzg zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzba(zzai zzai, zzg zzg) {
        this.zza = zzai;
        this.zzb = zzg;
    }

    @Override // java.util.Comparator
    public final /* bridge */ /* synthetic */ int compare(zzap zzap, zzap zzap2) {
        zzap zzap3 = zzap;
        zzap zzap4 = zzap2;
        zzai zzai = this.zza;
        zzg zzg = this.zzb;
        if (zzap3 instanceof zzau) {
            return !(zzap4 instanceof zzau) ? 1 : 0;
        }
        if (zzap4 instanceof zzau) {
            return -1;
        }
        if (zzai == null) {
            return zzap3.zzi().compareTo(zzap4.zzi());
        }
        return (int) zzh.zza(zzai.zza(zzg, Arrays.asList(zzap3, zzap4)).zzh().doubleValue());
    }
}
