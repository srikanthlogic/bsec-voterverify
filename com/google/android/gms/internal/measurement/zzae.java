package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzae implements Iterable<zzap>, zzap, zzal {
    final SortedMap<Integer, zzap> zza;
    final Map<String, zzap> zzb;

    public zzae() {
        this.zza = new TreeMap();
        this.zzb = new TreeMap();
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzae)) {
            return false;
        }
        zzae zzae = (zzae) obj;
        if (zzc() != zzae.zzc()) {
            return false;
        }
        if (this.zza.isEmpty()) {
            return zzae.zza.isEmpty();
        }
        for (int intValue = this.zza.firstKey().intValue(); intValue <= this.zza.lastKey().intValue(); intValue++) {
            if (!zze(intValue).equals(zzae.zze(intValue))) {
                return false;
            }
        }
        return true;
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return this.zza.hashCode() * 31;
    }

    @Override // java.lang.Iterable
    public final Iterator<zzap> iterator() {
        return new zzad(this);
    }

    @Override // java.lang.Object
    public final String toString() {
        return zzj(",");
    }

    public final int zzb() {
        return this.zza.size();
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final zzap zzbK(String str, zzg zzg, List<zzap> list) {
        if ("concat".equals(str) || "every".equals(str) || "filter".equals(str) || "forEach".equals(str) || "indexOf".equals(str) || "join".equals(str) || "lastIndexOf".equals(str) || "map".equals(str) || "pop".equals(str) || "push".equals(str) || "reduce".equals(str) || "reduceRight".equals(str) || "reverse".equals(str) || "shift".equals(str) || "slice".equals(str) || "some".equals(str) || "sort".equals(str) || "splice".equals(str) || "toString".equals(str) || "unshift".equals(str)) {
            return zzbb.zza(str, this, zzg, list);
        }
        return zzaj.zza(this, new zzat(str), zzg, list);
    }

    public final int zzc() {
        if (this.zza.isEmpty()) {
            return 0;
        }
        return this.zza.lastKey().intValue() + 1;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final zzap zzd() {
        zzae zzae = new zzae();
        for (Map.Entry<Integer, zzap> entry : this.zza.entrySet()) {
            if (entry.getValue() instanceof zzal) {
                zzae.zza.put(entry.getKey(), entry.getValue());
            } else {
                zzae.zza.put(entry.getKey(), entry.getValue().zzd());
            }
        }
        return zzae;
    }

    public final zzap zze(int i) {
        zzap zzap;
        if (i < zzc()) {
            return (!zzs(i) || (zzap = this.zza.get(Integer.valueOf(i))) == null) ? zzf : zzap;
        }
        throw new IndexOutOfBoundsException("Attempting to get element outside of current array");
    }

    @Override // com.google.android.gms.internal.measurement.zzal
    public final zzap zzf(String str) {
        zzap zzap;
        if ("length".equals(str)) {
            return new zzah(Double.valueOf((double) zzc()));
        }
        return (!zzt(str) || (zzap = this.zzb.get(str)) == null) ? zzf : zzap;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Boolean zzg() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Double zzh() {
        if (this.zza.size() == 1) {
            return zze(0).zzh();
        }
        if (this.zza.size() <= 0) {
            return Double.valueOf(0.0d);
        }
        return Double.valueOf(Double.NaN);
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final String zzi() {
        return zzj(",");
    }

    public final String zzj(String str) {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder();
        if (!this.zza.isEmpty()) {
            for (int i = 0; i < zzc(); i++) {
                zzap zze = zze(i);
                sb.append(str);
                if (!(zze instanceof zzau) && !(zze instanceof zzan)) {
                    sb.append(zze.zzi());
                }
            }
            sb.delete(0, str.length());
        }
        return sb.toString();
    }

    public final Iterator<Integer> zzk() {
        return this.zza.keySet().iterator();
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Iterator<zzap> zzl() {
        return new zzac(this, this.zza.keySet().iterator(), this.zzb.keySet().iterator());
    }

    public final List<zzap> zzm() {
        ArrayList arrayList = new ArrayList(zzc());
        for (int i = 0; i < zzc(); i++) {
            arrayList.add(zze(i));
        }
        return arrayList;
    }

    public final void zzn() {
        this.zza.clear();
    }

    public final void zzp(int i) {
        int intValue = this.zza.lastKey().intValue();
        if (i <= intValue && i >= 0) {
            this.zza.remove(Integer.valueOf(i));
            if (i == intValue) {
                SortedMap<Integer, zzap> sortedMap = this.zza;
                int i2 = i - 1;
                Integer valueOf = Integer.valueOf(i2);
                if (!sortedMap.containsKey(valueOf) && i2 >= 0) {
                    this.zza.put(valueOf, zzap.zzf);
                    return;
                }
                return;
            }
            while (true) {
                i++;
                if (i <= this.zza.lastKey().intValue()) {
                    SortedMap<Integer, zzap> sortedMap2 = this.zza;
                    Integer valueOf2 = Integer.valueOf(i);
                    zzap zzap = sortedMap2.get(valueOf2);
                    if (zzap != null) {
                        this.zza.put(Integer.valueOf(i - 1), zzap);
                        this.zza.remove(valueOf2);
                    }
                } else {
                    return;
                }
            }
        }
    }

    @RequiresNonNull({"elements"})
    public final void zzq(int i, zzap zzap) {
        if (i > 32468) {
            throw new IllegalStateException("Array too large");
        } else if (i < 0) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Out of bounds index: ");
            sb.append(i);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (zzap == null) {
            this.zza.remove(Integer.valueOf(i));
        } else {
            this.zza.put(Integer.valueOf(i), zzap);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzal
    public final void zzr(String str, zzap zzap) {
        if (zzap == null) {
            this.zzb.remove(str);
        } else {
            this.zzb.put(str, zzap);
        }
    }

    public final boolean zzs(int i) {
        if (i >= 0 && i <= this.zza.lastKey().intValue()) {
            return this.zza.containsKey(Integer.valueOf(i));
        }
        StringBuilder sb = new StringBuilder(32);
        sb.append("Out of bounds index: ");
        sb.append(i);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @Override // com.google.android.gms.internal.measurement.zzal
    public final boolean zzt(String str) {
        return "length".equals(str) || this.zzb.containsKey(str);
    }

    public final void zzo(int i, zzap zzap) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Invalid value index: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        } else if (i >= zzc()) {
            zzq(i, zzap);
        } else {
            for (int intValue = this.zza.lastKey().intValue(); intValue >= i; intValue--) {
                SortedMap<Integer, zzap> sortedMap = this.zza;
                Integer valueOf = Integer.valueOf(intValue);
                zzap zzap2 = sortedMap.get(valueOf);
                if (zzap2 != null) {
                    zzq(intValue + 1, zzap2);
                    this.zza.remove(valueOf);
                }
            }
            zzq(i, zzap);
        }
    }

    public zzae(List<zzap> list) {
        this();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                zzq(i, list.get(i));
            }
        }
    }
}
