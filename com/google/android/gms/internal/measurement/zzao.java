package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzao extends zzai implements zzal {
    protected final List<String> zza;
    protected final List<zzap> zzb;
    protected zzg zzc;

    private zzao(zzao zzao) {
        super(zzao.zzd);
        this.zza = new ArrayList(zzao.zza.size());
        this.zza.addAll(zzao.zza);
        this.zzb = new ArrayList(zzao.zzb.size());
        this.zzb.addAll(zzao.zzb);
        this.zzc = zzao.zzc;
    }

    @Override // com.google.android.gms.internal.measurement.zzai
    public final zzap zza(zzg zzg, List<zzap> list) {
        zzg zza = this.zzc.zza();
        for (int i = 0; i < this.zza.size(); i++) {
            if (i < list.size()) {
                zza.zze(this.zza.get(i), zzg.zzb(list.get(i)));
            } else {
                zza.zze(this.zza.get(i), zzf);
            }
        }
        for (zzap zzap : this.zzb) {
            zzap zzb = zza.zzb(zzap);
            if (zzb instanceof zzaq) {
                zzb = zza.zzb(zzap);
            }
            if (zzb instanceof zzag) {
                return ((zzag) zzb).zzb();
            }
        }
        return zzap.zzf;
    }

    @Override // com.google.android.gms.internal.measurement.zzai, com.google.android.gms.internal.measurement.zzap
    public final zzap zzd() {
        return new zzao(this);
    }

    public zzao(String str, List<zzap> list, List<zzap> list2, zzg zzg) {
        super(str);
        this.zza = new ArrayList();
        this.zzc = zzg;
        if (!list.isEmpty()) {
            for (zzap zzap : list) {
                this.zza.add(zzap.zzi());
            }
        }
        this.zzb = new ArrayList(list2);
    }
}
