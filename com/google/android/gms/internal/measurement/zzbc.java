package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzbc extends zzaw {
    /* JADX INFO: Access modifiers changed from: protected */
    public zzbc() {
        this.zza.add(zzbl.AND);
        this.zza.add(zzbl.NOT);
        this.zza.add(zzbl.OR);
    }

    @Override // com.google.android.gms.internal.measurement.zzaw
    public final zzap zza(String str, zzg zzg, List<zzap> list) {
        zzbl zzbl = zzbl.ADD;
        int ordinal = zzh.zze(str).ordinal();
        if (ordinal == 1) {
            zzh.zzh(zzbl.AND.name(), 2, list);
            zzap zzb = zzg.zzb(list.get(0));
            if (!zzb.zzg().booleanValue()) {
                return zzb;
            }
            return zzg.zzb(list.get(1));
        } else if (ordinal == 47) {
            zzh.zzh(zzbl.NOT.name(), 1, list);
            return new zzaf(Boolean.valueOf(!zzg.zzb(list.get(0)).zzg().booleanValue()));
        } else if (ordinal != 50) {
            return super.zzb(str);
        } else {
            zzh.zzh(zzbl.OR.name(), 2, list);
            zzap zzb2 = zzg.zzb(list.get(0));
            if (zzb2.zzg().booleanValue()) {
                return zzb2;
            }
            return zzg.zzb(list.get(1));
        }
    }
}
