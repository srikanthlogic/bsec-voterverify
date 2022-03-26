package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzax {
    final Map<String, zzaw> zza = new HashMap();
    final zzbj zzb = new zzbj();

    public zzax() {
        zzb(new zzav());
        zzb(new zzay());
        zzb(new zzaz());
        zzb(new zzbc());
        zzb(new zzbh());
        zzb(new zzbi());
        zzb(new zzbk());
    }

    public final zzap zza(zzg zzg, zzap zzap) {
        zzaw zzaw;
        zzh.zzc(zzg);
        if (!(zzap instanceof zzaq)) {
            return zzap;
        }
        zzaq zzaq = (zzaq) zzap;
        ArrayList<zzap> zzc = zzaq.zzc();
        String zzb = zzaq.zzb();
        if (this.zza.containsKey(zzb)) {
            zzaw = this.zza.get(zzb);
        } else {
            zzaw = this.zzb;
        }
        return zzaw.zza(zzb, zzg, zzc);
    }

    final void zzb(zzaw zzaw) {
        for (zzbl zzbl : zzaw.zza) {
            this.zza.put(zzbl.zzb().toString(), zzaw);
        }
    }
}
