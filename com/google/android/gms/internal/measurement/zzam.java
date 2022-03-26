package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public class zzam implements zzap, zzal {
    final Map<String, zzap> zza = new HashMap();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzam)) {
            return false;
        }
        return this.zza.equals(((zzam) obj).zza);
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (!this.zza.isEmpty()) {
            for (String str : this.zza.keySet()) {
                sb.append(String.format("%s: %s,", str, this.zza.get(str)));
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        sb.append("}");
        return sb.toString();
    }

    public final List<String> zzb() {
        return new ArrayList(this.zza.keySet());
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public zzap zzbK(String str, zzg zzg, List<zzap> list) {
        if ("toString".equals(str)) {
            return new zzat(toString());
        }
        return zzaj.zza(this, new zzat(str), zzg, list);
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final zzap zzd() {
        zzam zzam = new zzam();
        for (Map.Entry<String, zzap> entry : this.zza.entrySet()) {
            if (entry.getValue() instanceof zzal) {
                zzam.zza.put(entry.getKey(), entry.getValue());
            } else {
                zzam.zza.put(entry.getKey(), entry.getValue().zzd());
            }
        }
        return zzam;
    }

    @Override // com.google.android.gms.internal.measurement.zzal
    public final zzap zzf(String str) {
        return this.zza.containsKey(str) ? this.zza.get(str) : zzf;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Boolean zzg() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Double zzh() {
        return Double.valueOf(Double.NaN);
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final String zzi() {
        return "[object Object]";
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Iterator<zzap> zzl() {
        return zzaj.zzb(this.zza);
    }

    @Override // com.google.android.gms.internal.measurement.zzal
    public final void zzr(String str, zzap zzap) {
        if (zzap == null) {
            this.zza.remove(str);
        } else {
            this.zza.put(str, zzap);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzal
    public final boolean zzt(String str) {
        return this.zza.containsKey(str);
    }
}
