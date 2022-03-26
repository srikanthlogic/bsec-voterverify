package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzbb {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static zzap zza(String str, zzae zzae, zzg zzg, List<zzap> list) {
        char c;
        String str2;
        double d;
        zzai zzai;
        int i = 0;
        switch (str.hashCode()) {
            case -1776922004:
                if (str.equals("toString")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case -1354795244:
                if (str.equals("concat")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1274492040:
                if (str.equals("filter")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -934873754:
                if (str.equals("reduce")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -895859076:
                if (str.equals("splice")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -678635926:
                if (str.equals("forEach")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -467511597:
                if (str.equals("lastIndexOf")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -277637751:
                if (str.equals("unshift")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 107868:
                if (str.equals("map")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 111185:
                if (str.equals("pop")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 3267882:
                if (str.equals("join")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 3452698:
                if (str.equals("push")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 3536116:
                if (str.equals("some")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 3536286:
                if (str.equals("sort")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 96891675:
                if (str.equals("every")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 109407362:
                if (str.equals("shift")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 109526418:
                if (str.equals("slice")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 965561430:
                if (str.equals("reduceRight")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 1099846370:
                if (str.equals("reverse")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 1943291465:
                if (str.equals("indexOf")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        double d2 = 0.0d;
        switch (c) {
            case 0:
                zzap zzd = zzae.zzd();
                if (!list.isEmpty()) {
                    for (zzap zzap : list) {
                        zzap zzb = zzg.zzb(zzap);
                        if (!(zzb instanceof zzag)) {
                            zzae zzae2 = (zzae) zzd;
                            int zzc = zzae2.zzc();
                            if (zzb instanceof zzae) {
                                zzae zzae3 = (zzae) zzb;
                                Iterator<Integer> zzk = zzae3.zzk();
                                while (zzk.hasNext()) {
                                    Integer next = zzk.next();
                                    zzae2.zzq(next.intValue() + zzc, zzae3.zze(next.intValue()));
                                }
                            } else {
                                zzae2.zzq(zzc, zzb);
                            }
                        } else {
                            throw new IllegalStateException("Failed evaluation of arguments");
                        }
                    }
                }
                return zzd;
            case 1:
                zzh.zzh("every", 1, list);
                zzap zzb2 = zzg.zzb(list.get(0));
                if (!(zzb2 instanceof zzao)) {
                    throw new IllegalArgumentException("Callback should be a method");
                } else if (zzae.zzc() == 0) {
                    return zzap.zzk;
                } else {
                    return zzb(zzae, zzg, (zzao) zzb2, false, true).zzc() != zzae.zzc() ? zzap.zzl : zzap.zzk;
                }
            case 2:
                zzh.zzh("filter", 1, list);
                zzap zzb3 = zzg.zzb(list.get(0));
                if (!(zzb3 instanceof zzao)) {
                    throw new IllegalArgumentException("Callback should be a method");
                } else if (zzae.zzb() == 0) {
                    return new zzae();
                } else {
                    zzap zzd2 = zzae.zzd();
                    zzae zzb4 = zzb(zzae, zzg, (zzao) zzb3, null, true);
                    zzae zzae4 = new zzae();
                    Iterator<Integer> zzk2 = zzb4.zzk();
                    while (zzk2.hasNext()) {
                        zzae4.zzq(zzae4.zzc(), ((zzae) zzd2).zze(zzk2.next().intValue()));
                    }
                    return zzae4;
                }
            case 3:
                zzh.zzh("forEach", 1, list);
                zzap zzb5 = zzg.zzb(list.get(0));
                if (!(zzb5 instanceof zzao)) {
                    throw new IllegalArgumentException("Callback should be a method");
                } else if (zzae.zzb() == 0) {
                    return zzap.zzf;
                } else {
                    zzb(zzae, zzg, (zzao) zzb5, null, null);
                    return zzap.zzf;
                }
            case 4:
                zzh.zzj("indexOf", 2, list);
                zzap zzap2 = zzap.zzf;
                if (!list.isEmpty()) {
                    zzap2 = zzg.zzb(list.get(0));
                }
                if (list.size() > 1) {
                    double zza = zzh.zza(zzg.zzb(list.get(1)).zzh().doubleValue());
                    if (zza >= ((double) zzae.zzc())) {
                        return new zzah(Double.valueOf(-1.0d));
                    }
                    if (zza < 0.0d) {
                        d2 = ((double) zzae.zzc()) + zza;
                    } else {
                        d2 = zza;
                    }
                }
                Iterator<Integer> zzk3 = zzae.zzk();
                while (zzk3.hasNext()) {
                    int intValue = zzk3.next().intValue();
                    double d3 = (double) intValue;
                    if (d3 >= d2 && zzh.zzl(zzae.zze(intValue), zzap2)) {
                        return new zzah(Double.valueOf(d3));
                    }
                }
                return new zzah(Double.valueOf(-1.0d));
            case 5:
                zzh.zzj("join", 1, list);
                if (zzae.zzc() == 0) {
                    return zzap.zzm;
                }
                if (list.size() > 0) {
                    zzap zzb6 = zzg.zzb(list.get(0));
                    str2 = ((zzb6 instanceof zzan) || (zzb6 instanceof zzau)) ? "" : zzb6.zzi();
                } else {
                    str2 = ",";
                }
                return new zzat(zzae.zzj(str2));
            case 6:
                zzh.zzj("lastIndexOf", 2, list);
                zzap zzap3 = zzap.zzf;
                if (!list.isEmpty()) {
                    zzap3 = zzg.zzb(list.get(0));
                }
                double zzc2 = (double) (zzae.zzc() - 1);
                if (list.size() > 1) {
                    zzap zzb7 = zzg.zzb(list.get(1));
                    if (Double.isNaN(zzb7.zzh().doubleValue())) {
                        zzc2 = (double) (zzae.zzc() - 1);
                    } else {
                        zzc2 = zzh.zza(zzb7.zzh().doubleValue());
                    }
                    if (zzc2 < 0.0d) {
                        zzc2 += (double) zzae.zzc();
                    }
                }
                if (zzc2 < 0.0d) {
                    return new zzah(Double.valueOf(-1.0d));
                }
                for (int min = (int) Math.min((double) zzae.zzc(), zzc2); min >= 0; min--) {
                    if (zzae.zzs(min) && zzh.zzl(zzae.zze(min), zzap3)) {
                        return new zzah(Double.valueOf((double) min));
                    }
                }
                return new zzah(Double.valueOf(-1.0d));
            case 7:
                zzh.zzh("map", 1, list);
                zzap zzb8 = zzg.zzb(list.get(0));
                if (!(zzb8 instanceof zzao)) {
                    throw new IllegalArgumentException("Callback should be a method");
                } else if (zzae.zzc() == 0) {
                    return new zzae();
                } else {
                    return zzb(zzae, zzg, (zzao) zzb8, null, null);
                }
            case '\b':
                zzh.zzh("pop", 0, list);
                int zzc3 = zzae.zzc();
                if (zzc3 == 0) {
                    return zzap.zzf;
                }
                int i2 = zzc3 - 1;
                zzap zze = zzae.zze(i2);
                zzae.zzp(i2);
                return zze;
            case '\t':
                if (!list.isEmpty()) {
                    for (zzap zzap4 : list) {
                        zzae.zzq(zzae.zzc(), zzg.zzb(zzap4));
                    }
                }
                return new zzah(Double.valueOf((double) zzae.zzc()));
            case '\n':
                return zzc(zzae, zzg, list, true);
            case 11:
                return zzc(zzae, zzg, list, false);
            case '\f':
                zzh.zzh("reverse", 0, list);
                int zzc4 = zzae.zzc();
                if (zzc4 != 0) {
                    while (i < zzc4 / 2) {
                        if (zzae.zzs(i)) {
                            zzap zze2 = zzae.zze(i);
                            zzae.zzq(i, null);
                            int i3 = (zzc4 - 1) - i;
                            if (zzae.zzs(i3)) {
                                zzae.zzq(i, zzae.zze(i3));
                            }
                            zzae.zzq(i3, zze2);
                        }
                        i++;
                    }
                }
                return zzae;
            case '\r':
                zzh.zzh("shift", 0, list);
                if (zzae.zzc() == 0) {
                    return zzap.zzf;
                }
                zzap zze3 = zzae.zze(0);
                zzae.zzp(0);
                return zze3;
            case 14:
                zzh.zzj("slice", 2, list);
                if (list.isEmpty()) {
                    return zzae.zzd();
                }
                double zzc5 = (double) zzae.zzc();
                double zza2 = zzh.zza(zzg.zzb(list.get(0)).zzh().doubleValue());
                if (zza2 < 0.0d) {
                    d = Math.max(zza2 + zzc5, 0.0d);
                } else {
                    d = Math.min(zza2, zzc5);
                }
                if (list.size() == 2) {
                    double zza3 = zzh.zza(zzg.zzb(list.get(1)).zzh().doubleValue());
                    if (zza3 < 0.0d) {
                        zzc5 = Math.max(zzc5 + zza3, 0.0d);
                    } else {
                        zzc5 = Math.min(zzc5, zza3);
                    }
                }
                zzae zzae5 = new zzae();
                for (int i4 = (int) d; ((double) i4) < zzc5; i4++) {
                    zzae5.zzq(zzae5.zzc(), zzae.zze(i4));
                }
                return zzae5;
            case 15:
                zzh.zzh("some", 1, list);
                zzap zzb9 = zzg.zzb(list.get(0));
                if (!(zzb9 instanceof zzai)) {
                    throw new IllegalArgumentException("Callback should be a method");
                } else if (zzae.zzc() == 0) {
                    return zzap.zzl;
                } else {
                    zzai zzai2 = (zzai) zzb9;
                    Iterator<Integer> zzk4 = zzae.zzk();
                    while (zzk4.hasNext()) {
                        int intValue2 = zzk4.next().intValue();
                        if (zzae.zzs(intValue2) && zzai2.zza(zzg, Arrays.asList(zzae.zze(intValue2), new zzah(Double.valueOf((double) intValue2)), zzae)).zzg().booleanValue()) {
                            return zzap.zzk;
                        }
                    }
                    return zzap.zzl;
                }
            case 16:
                zzh.zzj("sort", 1, list);
                if (zzae.zzc() >= 2) {
                    List<zzap> zzm = zzae.zzm();
                    if (!list.isEmpty()) {
                        zzap zzb10 = zzg.zzb(list.get(0));
                        if (zzb10 instanceof zzai) {
                            zzai = (zzai) zzb10;
                        } else {
                            throw new IllegalArgumentException("Comparator should be a method");
                        }
                    } else {
                        zzai = null;
                    }
                    Collections.sort(zzm, new zzba(zzai, zzg));
                    zzae.zzn();
                    for (zzap zzap5 : zzm) {
                        zzae.zzq(i, zzap5);
                        i++;
                    }
                }
                return zzae;
            case 17:
                if (list.isEmpty()) {
                    return new zzae();
                }
                int zza4 = (int) zzh.zza(zzg.zzb(list.get(0)).zzh().doubleValue());
                if (zza4 < 0) {
                    zza4 = Math.max(0, zza4 + zzae.zzc());
                } else if (zza4 > zzae.zzc()) {
                    zza4 = zzae.zzc();
                }
                int zzc6 = zzae.zzc();
                zzae zzae6 = new zzae();
                if (list.size() > 1) {
                    int max = Math.max(0, (int) zzh.zza(zzg.zzb(list.get(1)).zzh().doubleValue()));
                    if (max > 0) {
                        for (int i5 = zza4; i5 < Math.min(zzc6, zza4 + max); i5++) {
                            zzae6.zzq(zzae6.zzc(), zzae.zze(zza4));
                            zzae.zzp(zza4);
                        }
                    }
                    if (list.size() > 2) {
                        for (int i6 = 2; i6 < list.size(); i6++) {
                            zzap zzb11 = zzg.zzb(list.get(i6));
                            if (!(zzb11 instanceof zzag)) {
                                zzae.zzo((zza4 + i6) - 2, zzb11);
                            } else {
                                throw new IllegalArgumentException("Failed to parse elements to add");
                            }
                        }
                    }
                } else {
                    while (zza4 < zzc6) {
                        zzae6.zzq(zzae6.zzc(), zzae.zze(zza4));
                        zzae.zzq(zza4, null);
                        zza4++;
                    }
                }
                return zzae6;
            case 18:
                zzh.zzh("toString", 0, list);
                return new zzat(zzae.zzj(","));
            case 19:
                if (!list.isEmpty()) {
                    zzae zzae7 = new zzae();
                    for (zzap zzap6 : list) {
                        zzap zzb12 = zzg.zzb(zzap6);
                        if (!(zzb12 instanceof zzag)) {
                            zzae7.zzq(zzae7.zzc(), zzb12);
                        } else {
                            throw new IllegalStateException("Argument evaluation failed");
                        }
                    }
                    int zzc7 = zzae7.zzc();
                    Iterator<Integer> zzk5 = zzae.zzk();
                    while (zzk5.hasNext()) {
                        Integer next2 = zzk5.next();
                        zzae7.zzq(next2.intValue() + zzc7, zzae.zze(next2.intValue()));
                    }
                    zzae.zzn();
                    Iterator<Integer> zzk6 = zzae7.zzk();
                    while (zzk6.hasNext()) {
                        Integer next3 = zzk6.next();
                        zzae.zzq(next3.intValue(), zzae7.zze(next3.intValue()));
                    }
                }
                return new zzah(Double.valueOf((double) zzae.zzc()));
            default:
                throw new IllegalArgumentException("Command not supported");
        }
    }

    private static zzae zzb(zzae zzae, zzg zzg, zzai zzai, Boolean bool, Boolean bool2) {
        zzae zzae2 = new zzae();
        Iterator<Integer> zzk = zzae.zzk();
        while (zzk.hasNext()) {
            int intValue = zzk.next().intValue();
            if (zzae.zzs(intValue)) {
                zzap zza = zzai.zza(zzg, Arrays.asList(zzae.zze(intValue), new zzah(Double.valueOf((double) intValue)), zzae));
                if (zza.zzg().equals(bool)) {
                    return zzae2;
                }
                if (bool2 == null || zza.zzg().equals(bool2)) {
                    zzae2.zzq(intValue, zza);
                }
            }
        }
        return zzae2;
    }

    private static zzap zzc(zzae zzae, zzg zzg, List<zzap> list, boolean z) {
        zzap zzap;
        int i;
        int i2;
        zzh.zzi("reduce", 1, list);
        zzh.zzj("reduce", 2, list);
        zzap zzb = zzg.zzb(list.get(0));
        if (zzb instanceof zzai) {
            if (list.size() == 2) {
                zzap = zzg.zzb(list.get(1));
                if (zzap instanceof zzag) {
                    throw new IllegalArgumentException("Failed to parse initial value");
                }
            } else if (zzae.zzc() != 0) {
                zzap = null;
            } else {
                throw new IllegalStateException("Empty array with no initial value error");
            }
            zzai zzai = (zzai) zzb;
            int zzc = zzae.zzc();
            if (z) {
                i = 0;
            } else {
                i = zzc - 1;
            }
            int i3 = -1;
            if (z) {
                i2 = zzc - 1;
            } else {
                i2 = 0;
            }
            if (true == z) {
                i3 = 1;
            }
            if (zzap == null) {
                zzap = zzae.zze(i);
                i += i3;
            }
            while ((i2 - i) * i3 >= 0) {
                if (zzae.zzs(i)) {
                    zzap = zzai.zza(zzg, Arrays.asList(zzap, zzae.zze(i), new zzah(Double.valueOf((double) i)), zzae));
                    if (zzap instanceof zzag) {
                        throw new IllegalStateException("Reduce operation failed");
                    }
                }
                i += i3;
            }
            return zzap;
        }
        throw new IllegalArgumentException("Callback should be a method");
    }
}
