package com.google.android.gms.internal.measurement;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzm extends zzai {
    final /* synthetic */ zzo zza;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzm(zzn zzn, String str, zzo zzo) {
        super("getValue");
        this.zza = zzo;
    }

    @Override // com.google.android.gms.internal.measurement.zzai
    public final zzap zza(zzg zzg, List<zzap> list) {
        zzh.zzh("getValue", 2, list);
        zzap zzb = zzg.zzb(list.get(0));
        zzap zzb2 = zzg.zzb(list.get(1));
        String zza = this.zza.zza(zzb.zzi());
        return zza != null ? new zzat(zza) : zzb2;
    }
}
