package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzl extends zzam {
    private final zzab zzb;

    public zzl(zzab zzab) {
        this.zzb = zzab;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.google.android.gms.internal.measurement.zzam, com.google.android.gms.internal.measurement.zzap
    public final zzap zzbK(String str, zzg zzg, List<zzap> list) {
        char c;
        switch (str.hashCode()) {
            case 21624207:
                if (str.equals("getEventName")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 45521504:
                if (str.equals("getTimestamp")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 146575578:
                if (str.equals("getParamValue")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 700587132:
                if (str.equals("getParams")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 920706790:
                if (str.equals("setParamValue")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1570616835:
                if (str.equals("setEventName")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            zzh.zzh("getEventName", 0, list);
            return new zzat(this.zzb.zzb().zzd());
        } else if (c == 1) {
            zzh.zzh("getParamValue", 1, list);
            return zzi.zzb(this.zzb.zzb().zzc(zzg.zzb(list.get(0)).zzi()));
        } else if (c == 2) {
            zzh.zzh("getParams", 0, list);
            Map<String, Object> zze = this.zzb.zzb().zze();
            zzam zzam = new zzam();
            for (String str2 : zze.keySet()) {
                zzam.zzr(str2, zzi.zzb(zze.get(str2)));
            }
            return zzam;
        } else if (c == 3) {
            zzh.zzh("getTimestamp", 0, list);
            return new zzah(Double.valueOf((double) this.zzb.zzb().zza()));
        } else if (c == 4) {
            zzh.zzh("setEventName", 1, list);
            zzap zzb = zzg.zzb(list.get(0));
            if (zzf.equals(zzb) || zzg.equals(zzb)) {
                throw new IllegalArgumentException("Illegal event name");
            }
            this.zzb.zzb().zzf(zzb.zzi());
            return new zzat(zzb.zzi());
        } else if (c != 5) {
            return super.zzbK(str, zzg, list);
        } else {
            zzh.zzh("setParamValue", 2, list);
            String zzi = zzg.zzb(list.get(0)).zzi();
            zzap zzb2 = zzg.zzb(list.get(1));
            this.zzb.zzb().zzg(zzi, zzh.zzf(zzb2));
            return zzb2;
        }
    }
}
