package com.google.android.gms.internal.measurement;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.text.Typography;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzat implements Iterable<zzap>, zzap {
    private final String zza;

    public zzat(String str) {
        if (str != null) {
            this.zza = str;
            return;
        }
        throw new IllegalArgumentException("StringValue cannot be null.");
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzat)) {
            return false;
        }
        return this.zza.equals(((zzat) obj).zza);
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return this.zza.hashCode();
    }

    @Override // java.lang.Iterable
    public final Iterator<zzap> iterator() {
        return new zzas(this);
    }

    @Override // java.lang.Object
    public final String toString() {
        String str = this.zza;
        StringBuilder sb = new StringBuilder(str.length() + 2);
        sb.append(Typography.quote);
        sb.append(str);
        sb.append(Typography.quote);
        return sb.toString();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0410  */
    @Override // com.google.android.gms.internal.measurement.zzap
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final zzap zzbK(String str, zzg zzg, List<zzap> list) {
        char c;
        int i;
        int i2;
        String str2;
        String str3;
        double d;
        zzap zzap;
        String str4;
        int indexOf;
        double d2;
        double d3;
        double d4;
        long j;
        int i3;
        int i4;
        int i5;
        int i6;
        if ("charAt".equals(str) || "concat".equals(str) || "hasOwnProperty".equals(str) || "indexOf".equals(str) || "lastIndexOf".equals(str) || "match".equals(str) || "replace".equals(str) || FirebaseAnalytics.Event.SEARCH.equals(str) || "slice".equals(str) || "split".equals(str) || "substring".equals(str) || "toLowerCase".equals(str) || "toLocaleLowerCase".equals(str) || "toString".equals(str) || "toUpperCase".equals(str) || "toLocaleUpperCase".equals(str) || "trim".equals(str)) {
            switch (str.hashCode()) {
                case -1789698943:
                    if (str.equals("hasOwnProperty")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -1776922004:
                    if (str.equals("toString")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case -1464939364:
                    if (str.equals("toLocaleLowerCase")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case -1361633751:
                    if (str.equals("charAt")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1354795244:
                    if (str.equals("concat")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -1137582698:
                    if (str.equals("toLowerCase")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case -906336856:
                    if (str.equals(FirebaseAnalytics.Event.SEARCH)) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -726908483:
                    if (str.equals("toLocaleUpperCase")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case -467511597:
                    if (str.equals("lastIndexOf")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -399551817:
                    if (str.equals("toUpperCase")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case 3568674:
                    if (str.equals("trim")) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case 103668165:
                    if (str.equals("match")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 109526418:
                    if (str.equals("slice")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 109648666:
                    if (str.equals("split")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case 530542161:
                    if (str.equals("substring")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case 1094496948:
                    if (str.equals("replace")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 1943291465:
                    if (str.equals("indexOf")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            String str5 = "";
            String str6 = "undefined";
            double d5 = 0.0d;
            switch (c) {
                case 0:
                    zzh.zzj("charAt", 1, list);
                    if (list.size() > 0) {
                        i = (int) zzh.zza(zzg.zzb(list.get(0)).zzh().doubleValue());
                    } else {
                        i = 0;
                    }
                    String str7 = this.zza;
                    if (i < 0 || i >= str7.length()) {
                        return zzap.zzm;
                    }
                    return new zzat(String.valueOf(str7.charAt(i)));
                case 1:
                    if (list.size() != 0) {
                        StringBuilder sb = new StringBuilder(this.zza);
                        for (int i7 = 0; i7 < list.size(); i7++) {
                            sb.append(zzg.zzb(list.get(i7)).zzi());
                        }
                        return new zzat(sb.toString());
                    }
                    break;
                case 2:
                    zzh.zzh("hasOwnProperty", 1, list);
                    String str8 = this.zza;
                    zzap zzb = zzg.zzb(list.get(0));
                    if ("length".equals(zzb.zzi())) {
                        return zzaf.zzk;
                    }
                    double doubleValue = zzb.zzh().doubleValue();
                    return (doubleValue != Math.floor(doubleValue) || (i2 = (int) doubleValue) < 0 || i2 >= str8.length()) ? zzaf.zzl : zzaf.zzk;
                case 3:
                    zzh.zzj("indexOf", 2, list);
                    String str9 = this.zza;
                    if (list.size() <= 0) {
                        str2 = str6;
                    } else {
                        str2 = zzg.zzb(list.get(0)).zzi();
                    }
                    if (list.size() >= 2) {
                        d5 = zzg.zzb(list.get(1)).zzh().doubleValue();
                    }
                    return new zzah(Double.valueOf((double) str9.indexOf(str2, (int) zzh.zza(d5))));
                case 4:
                    zzh.zzj("lastIndexOf", 2, list);
                    String str10 = this.zza;
                    if (list.size() <= 0) {
                        str3 = str6;
                    } else {
                        str3 = zzg.zzb(list.get(0)).zzi();
                    }
                    double doubleValue2 = list.size() < 2 ? Double.NaN : zzg.zzb(list.get(1)).zzh().doubleValue();
                    if (Double.isNaN(doubleValue2)) {
                        d = Double.POSITIVE_INFINITY;
                    } else {
                        d = zzh.zza(doubleValue2);
                    }
                    return new zzah(Double.valueOf((double) str10.lastIndexOf(str3, (int) d)));
                case 5:
                    zzh.zzj("match", 1, list);
                    String str11 = this.zza;
                    if (list.size() > 0) {
                        str5 = zzg.zzb(list.get(0)).zzi();
                    }
                    Matcher matcher = Pattern.compile(str5).matcher(str11);
                    return matcher.find() ? new zzae(Arrays.asList(new zzat(matcher.group()))) : zzap.zzg;
                case 6:
                    zzh.zzj("replace", 2, list);
                    zzap zzap2 = zzap.zzf;
                    if (list.size() > 0) {
                        str6 = zzg.zzb(list.get(0)).zzi();
                        if (list.size() > 1) {
                            zzap = zzg.zzb(list.get(1));
                            str4 = str6;
                            String str12 = this.zza;
                            indexOf = str12.indexOf(str4);
                            if (indexOf >= 0) {
                                if (zzap instanceof zzai) {
                                    zzap = ((zzai) zzap).zza(zzg, Arrays.asList(new zzat(str4), new zzah(Double.valueOf((double) indexOf)), this));
                                }
                                String substring = str12.substring(0, indexOf);
                                String zzi = zzap.zzi();
                                String substring2 = str12.substring(indexOf + str4.length());
                                StringBuilder sb2 = new StringBuilder(String.valueOf(substring).length() + String.valueOf(zzi).length() + String.valueOf(substring2).length());
                                sb2.append(substring);
                                sb2.append(zzi);
                                sb2.append(substring2);
                                return new zzat(sb2.toString());
                            }
                        }
                    }
                    zzap = zzap2;
                    str4 = str6;
                    String str122 = this.zza;
                    indexOf = str122.indexOf(str4);
                    if (indexOf >= 0) {
                    }
                    break;
                case 7:
                    zzh.zzj(FirebaseAnalytics.Event.SEARCH, 1, list);
                    if (list.size() > 0) {
                        str6 = zzg.zzb(list.get(0)).zzi();
                    }
                    Matcher matcher2 = Pattern.compile(str6).matcher(this.zza);
                    if (matcher2.find()) {
                        return new zzah(Double.valueOf((double) matcher2.start()));
                    }
                    return new zzah(Double.valueOf(-1.0d));
                case '\b':
                    zzh.zzj("slice", 2, list);
                    String str13 = this.zza;
                    if (list.size() > 0) {
                        d2 = zzg.zzb(list.get(0)).zzh().doubleValue();
                    } else {
                        d2 = 0.0d;
                    }
                    double zza = zzh.zza(d2);
                    if (zza < 0.0d) {
                        d3 = Math.max(((double) str13.length()) + zza, 0.0d);
                    } else {
                        d3 = Math.min(zza, (double) str13.length());
                    }
                    int i8 = (int) d3;
                    double zza2 = zzh.zza(list.size() > 1 ? zzg.zzb(list.get(1)).zzh().doubleValue() : (double) str13.length());
                    if (zza2 < 0.0d) {
                        d4 = Math.max(((double) str13.length()) + zza2, 0.0d);
                    } else {
                        d4 = Math.min(zza2, (double) str13.length());
                    }
                    return new zzat(str13.substring(i8, Math.max(0, ((int) d4) - i8) + i8));
                case '\t':
                    zzh.zzj("split", 2, list);
                    String str14 = this.zza;
                    if (str14.length() == 0) {
                        return new zzae(Arrays.asList(this));
                    }
                    ArrayList arrayList = new ArrayList();
                    if (list.size() == 0) {
                        arrayList.add(this);
                    } else {
                        String zzi2 = zzg.zzb(list.get(0)).zzi();
                        if (list.size() > 1) {
                            j = zzh.zzd(zzg.zzb(list.get(1)).zzh().doubleValue());
                        } else {
                            j = 2147483647L;
                        }
                        if (j == 0) {
                            return new zzae();
                        }
                        String[] split = str14.split(Pattern.quote(zzi2), ((int) j) + 1);
                        int length = split.length;
                        if (!zzi2.equals(str5) || length <= 0) {
                            i4 = length;
                            i3 = 0;
                        } else {
                            boolean equals = split[0].equals(str5);
                            i4 = length - 1;
                            i3 = equals;
                            if (!split[i4].equals(str5)) {
                                i4 = length;
                                i3 = equals;
                            }
                        }
                        if (((long) length) > j) {
                            i4--;
                        }
                        while (i3 < i4) {
                            arrayList.add(new zzat(split[i3 == 1 ? 1 : 0]));
                            i3 = (i3 == 1 ? 1 : 0) + 1;
                        }
                    }
                    return new zzae(arrayList);
                case '\n':
                    zzh.zzj("substring", 2, list);
                    String str15 = this.zza;
                    if (list.size() > 0) {
                        i5 = (int) zzh.zza(zzg.zzb(list.get(0)).zzh().doubleValue());
                    } else {
                        i5 = 0;
                    }
                    if (list.size() > 1) {
                        i6 = (int) zzh.zza(zzg.zzb(list.get(1)).zzh().doubleValue());
                    } else {
                        i6 = str15.length();
                    }
                    int min = Math.min(Math.max(i5, 0), str15.length());
                    int min2 = Math.min(Math.max(i6, 0), str15.length());
                    return new zzat(str15.substring(Math.min(min, min2), Math.max(min, min2)));
                case 11:
                    zzh.zzh("toLocaleUpperCase", 0, list);
                    return new zzat(this.zza.toUpperCase());
                case '\f':
                    zzh.zzh("toLocaleLowerCase", 0, list);
                    return new zzat(this.zza.toLowerCase());
                case '\r':
                    zzh.zzh("toLowerCase", 0, list);
                    return new zzat(this.zza.toLowerCase(Locale.ENGLISH));
                case 14:
                    zzh.zzh("toString", 0, list);
                    break;
                case 15:
                    zzh.zzh("toUpperCase", 0, list);
                    return new zzat(this.zza.toUpperCase(Locale.ENGLISH));
                case 16:
                    zzh.zzh("toUpperCase", 0, list);
                    return new zzat(this.zza.trim());
                default:
                    throw new IllegalArgumentException("Command not supported");
            }
            return this;
        }
        throw new IllegalArgumentException(String.format("%s is not a String function", str));
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final zzap zzd() {
        return new zzat(this.zza);
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Boolean zzg() {
        return Boolean.valueOf(!this.zza.isEmpty());
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Double zzh() {
        if (this.zza.isEmpty()) {
            return Double.valueOf(0.0d);
        }
        try {
            return Double.valueOf(this.zza);
        } catch (NumberFormatException e) {
            return Double.valueOf(Double.NaN);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final String zzi() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Iterator<zzap> zzl() {
        return new zzar(this);
    }
}
