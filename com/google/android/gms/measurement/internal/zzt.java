package com.google.android.gms.measurement.internal;

import androidx.collection.ArrayMap;
import com.google.android.gms.internal.measurement.zzfj;
import com.google.android.gms.internal.measurement.zzfk;
import com.google.android.gms.internal.measurement.zzfl;
import com.google.android.gms.internal.measurement.zzfm;
import com.google.android.gms.internal.measurement.zzgc;
import com.google.android.gms.internal.measurement.zzgd;
import com.google.android.gms.internal.measurement.zzge;
import com.google.android.gms.internal.measurement.zzgf;
import com.google.android.gms.internal.measurement.zzoe;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzt {
    final /* synthetic */ zzz zza;
    private String zzb;
    private boolean zzc;
    private zzgd zzd;
    private BitSet zze;
    private BitSet zzf;
    private Map<Integer, Long> zzg;
    private Map<Integer, List<Long>> zzh;

    public /* synthetic */ zzt(zzz zzz, String str, zzs zzs) {
        this.zza = zzz;
        this.zzb = str;
        this.zzc = true;
        this.zze = new BitSet();
        this.zzf = new BitSet();
        this.zzg = new ArrayMap();
        this.zzh = new ArrayMap();
    }

    public static /* bridge */ /* synthetic */ BitSet zzb(zzt zzt) {
        return zzt.zze;
    }

    public final zzfk zza(int i) {
        ArrayList arrayList;
        List list;
        zzfj zzb = zzfk.zzb();
        zzb.zza(i);
        zzb.zzc(this.zzc);
        zzgd zzgd = this.zzd;
        if (zzgd != null) {
            zzb.zzd(zzgd);
        }
        zzgc zzf = zzgd.zzf();
        zzf.zzb(zzkp.zzs(this.zze));
        zzf.zzd(zzkp.zzs(this.zzf));
        Map<Integer, Long> map = this.zzg;
        if (map == null) {
            arrayList = null;
        } else {
            ArrayList arrayList2 = new ArrayList(map.size());
            for (Integer num : this.zzg.keySet()) {
                int intValue = num.intValue();
                Long l = this.zzg.get(Integer.valueOf(intValue));
                if (l != null) {
                    zzfl zzc = zzfm.zzc();
                    zzc.zzb(intValue);
                    zzc.zza(l.longValue());
                    arrayList2.add(zzc.zzaA());
                }
            }
            arrayList = arrayList2;
        }
        if (arrayList != null) {
            zzf.zza(arrayList);
        }
        Map<Integer, List<Long>> map2 = this.zzh;
        if (map2 == null) {
            list = Collections.emptyList();
        } else {
            ArrayList arrayList3 = new ArrayList(map2.size());
            for (Integer num2 : this.zzh.keySet()) {
                zzge zzd = zzgf.zzd();
                zzd.zzb(num2.intValue());
                List<Long> list2 = this.zzh.get(num2);
                if (list2 != null) {
                    Collections.sort(list2);
                    zzd.zza(list2);
                }
                arrayList3.add((zzgf) zzd.zzaA());
            }
            list = arrayList3;
        }
        zzf.zzc(list);
        zzb.zzb(zzf);
        return zzb.zzaA();
    }

    public final void zzc(zzx zzx) {
        int zza = zzx.zza();
        Boolean bool = zzx.zzd;
        if (bool != null) {
            this.zzf.set(zza, bool.booleanValue());
        }
        Boolean bool2 = zzx.zze;
        if (bool2 != null) {
            this.zze.set(zza, bool2.booleanValue());
        }
        if (zzx.zzf != null) {
            Map<Integer, Long> map = this.zzg;
            Integer valueOf = Integer.valueOf(zza);
            Long l = map.get(valueOf);
            long longValue = zzx.zzf.longValue() / 1000;
            if (l == null || longValue > l.longValue()) {
                this.zzg.put(valueOf, Long.valueOf(longValue));
            }
        }
        if (zzx.zzg != null) {
            Map<Integer, List<Long>> map2 = this.zzh;
            Integer valueOf2 = Integer.valueOf(zza);
            List<Long> list = map2.get(valueOf2);
            if (list == null) {
                list = new ArrayList<>();
                this.zzh.put(valueOf2, list);
            }
            if (zzx.zzc()) {
                list.clear();
            }
            zzoe.zzc();
            if (this.zza.zzs.zzf().zzs(this.zzb, zzdw.zzY) && zzx.zzb()) {
                list.clear();
            }
            zzoe.zzc();
            if (this.zza.zzs.zzf().zzs(this.zzb, zzdw.zzY)) {
                Long valueOf3 = Long.valueOf(zzx.zzg.longValue() / 1000);
                if (!list.contains(valueOf3)) {
                    list.add(valueOf3);
                    return;
                }
                return;
            }
            list.add(Long.valueOf(zzx.zzg.longValue() / 1000));
        }
    }

    public /* synthetic */ zzt(zzz zzz, String str, zzgd zzgd, BitSet bitSet, BitSet bitSet2, Map map, Map map2, zzs zzs) {
        this.zza = zzz;
        this.zzb = str;
        this.zze = bitSet;
        this.zzf = bitSet2;
        this.zzg = map;
        this.zzh = new ArrayMap();
        for (Integer num : map2.keySet()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add((Long) map2.get(num));
            this.zzh.put(num, arrayList);
        }
        this.zzc = false;
        this.zzd = zzgd;
    }
}
