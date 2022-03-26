package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzlt {
    private static final Class<?> zza;
    private static final zzmi<?, ?> zzb;
    private static final zzmi<?, ?> zzc;
    private static final zzmi<?, ?> zzd;

    static {
        Class<?> cls;
        try {
            cls = Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable th) {
            cls = null;
        }
        zza = cls;
        zzb = zzab(false);
        zzc = zzab(true);
        zzd = new zzmk();
    }

    public static zzmi<?, ?> zzA() {
        return zzc;
    }

    public static zzmi<?, ?> zzB() {
        return zzd;
    }

    public static <UT, UB> UB zzC(int i, List<Integer> list, zzkd zzkd, UB ub, zzmi<UT, UB> zzmi) {
        if (zzkd == null) {
            return ub;
        }
        if (list instanceof RandomAccess) {
            int size = list.size();
            UB ub2 = ub;
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                int intValue = list.get(i3).intValue();
                if (zzkd.zza(intValue)) {
                    if (i3 != i2) {
                        list.set(i2, Integer.valueOf(intValue));
                    }
                    i2++;
                } else {
                    ub2 = (UB) zzD(i, intValue, ub2, zzmi);
                }
            }
            if (i2 == size) {
                return ub2;
            }
            list.subList(i2, size).clear();
            return ub2;
        }
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int intValue2 = it.next().intValue();
            if (!zzkd.zza(intValue2)) {
                ub = (UB) zzD(i, intValue2, ub, zzmi);
                it.remove();
            }
        }
        return ub;
    }

    static <UT, UB> UB zzD(int i, int i2, UB ub, zzmi<UT, UB> zzmi) {
        if (ub == null) {
            ub = zzmi.zze();
        }
        zzmi.zzf(ub, i, (long) i2);
        return ub;
    }

    public static <T, FT extends zzjp<FT>> void zzE(zzjm<FT> zzjm, T t, T t2) {
        zzjm.zza(t2);
        throw null;
    }

    public static <T, UT, UB> void zzF(zzmi<UT, UB> zzmi, T t, T t2) {
        zzmi.zzh(t, zzmi.zzd(zzmi.zzc(t), zzmi.zzc(t2)));
    }

    public static void zzG(Class<?> cls) {
        Class<?> cls2;
        if (!zzjz.class.isAssignableFrom(cls) && (cls2 = zza) != null && !cls2.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
        }
    }

    public static boolean zzH(Object obj, Object obj2) {
        if (obj != obj2) {
            return obj != null && obj.equals(obj2);
        }
        return true;
    }

    public static <T> void zzI(zzlb zzlb, T t, T t2, long j) {
        zzms.zzs(t, j, zzlb.zzb(zzms.zzf(t, j), zzms.zzf(t2, j)));
    }

    public static void zzJ(int i, List<Boolean> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzc(i, list, z);
        }
    }

    public static void zzK(int i, List<zziy> list, zzjh zzjh) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zze(i, list);
        }
    }

    public static void zzL(int i, List<Double> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzg(i, list, z);
        }
    }

    public static void zzM(int i, List<Integer> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzj(i, list, z);
        }
    }

    public static void zzN(int i, List<Integer> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzl(i, list, z);
        }
    }

    public static void zzO(int i, List<Long> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzn(i, list, z);
        }
    }

    public static void zzP(int i, List<Float> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzp(i, list, z);
        }
    }

    public static void zzQ(int i, List<?> list, zzjh zzjh, zzlr zzlr) throws IOException {
        if (!(list == null || list.isEmpty())) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                zzjh.zzq(i, list.get(i2), zzlr);
            }
        }
    }

    public static void zzR(int i, List<Integer> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzs(i, list, z);
        }
    }

    public static void zzS(int i, List<Long> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzu(i, list, z);
        }
    }

    public static void zzT(int i, List<?> list, zzjh zzjh, zzlr zzlr) throws IOException {
        if (!(list == null || list.isEmpty())) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                zzjh.zzv(i, list.get(i2), zzlr);
            }
        }
    }

    public static void zzU(int i, List<Integer> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzx(i, list, z);
        }
    }

    public static void zzV(int i, List<Long> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzz(i, list, z);
        }
    }

    public static void zzW(int i, List<Integer> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzB(i, list, z);
        }
    }

    public static void zzX(int i, List<Long> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzD(i, list, z);
        }
    }

    public static void zzY(int i, List<String> list, zzjh zzjh) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzG(i, list);
        }
    }

    public static void zzZ(int i, List<Integer> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzI(i, list, z);
        }
    }

    public static int zza(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (zzjg.zzA(i << 3) + 1);
    }

    public static void zzaa(int i, List<Long> list, zzjh zzjh, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzjh.zzK(i, list, z);
        }
    }

    private static zzmi<?, ?> zzab(boolean z) {
        Class<?> cls;
        try {
            cls = Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable th) {
            cls = null;
        }
        if (cls == null) {
            return null;
        }
        try {
            return (zzmi) cls.getConstructor(Boolean.TYPE).newInstance(Boolean.valueOf(z));
        } catch (Throwable th2) {
            return null;
        }
    }

    public static int zzb(List<?> list) {
        return list.size();
    }

    public static int zzc(int i, List<zziy> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzz = size * zzjg.zzz(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            zzz += zzjg.zzt(list.get(i2));
        }
        return zzz;
    }

    public static int zzd(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zze(list) + (size * zzjg.zzz(i));
    }

    public static int zze(List<Integer> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzka) {
            zzka zzka = (zzka) list;
            i = 0;
            while (i2 < size) {
                i += zzjg.zzv(zzka.zze(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzjg.zzv(list.get(i2).intValue());
                i2++;
            }
        }
        return i;
    }

    public static int zzf(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (zzjg.zzA(i << 3) + 4);
    }

    public static int zzg(List<?> list) {
        return list.size() * 4;
    }

    public static int zzh(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (zzjg.zzA(i << 3) + 8);
    }

    public static int zzi(List<?> list) {
        return list.size() * 8;
    }

    public static int zzj(int i, List<zzlg> list, zzlr zzlr) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i2 += zzjg.zzu(i, list.get(i3), zzlr);
        }
        return i2;
    }

    public static int zzk(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzl(list) + (size * zzjg.zzz(i));
    }

    public static int zzl(List<Integer> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzka) {
            zzka zzka = (zzka) list;
            i = 0;
            while (i2 < size) {
                i += zzjg.zzv(zzka.zze(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzjg.zzv(list.get(i2).intValue());
                i2++;
            }
        }
        return i;
    }

    public static int zzm(int i, List<Long> list, boolean z) {
        if (list.size() == 0) {
            return 0;
        }
        return zzn(list) + (list.size() * zzjg.zzz(i));
    }

    public static int zzn(List<Long> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzkv) {
            zzkv zzkv = (zzkv) list;
            i = 0;
            while (i2 < size) {
                i += zzjg.zzB(zzkv.zza(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzjg.zzB(list.get(i2).longValue());
                i2++;
            }
        }
        return i;
    }

    public static int zzo(int i, Object obj, zzlr zzlr) {
        if (!(obj instanceof zzkm)) {
            return zzjg.zzA(i << 3) + zzjg.zzx((zzlg) obj, zzlr);
        }
        int zzA = zzjg.zzA(i << 3);
        int zza2 = ((zzkm) obj).zza();
        return zzA + zzjg.zzA(zza2) + zza2;
    }

    public static int zzp(int i, List<?> list, zzlr zzlr) {
        int i2;
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzz = zzjg.zzz(i) * size;
        for (int i3 = 0; i3 < size; i3++) {
            Object obj = list.get(i3);
            if (obj instanceof zzkm) {
                i2 = zzjg.zzw((zzkm) obj);
            } else {
                i2 = zzjg.zzx((zzlg) obj, zzlr);
            }
            zzz += i2;
        }
        return zzz;
    }

    public static int zzq(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzr(list) + (size * zzjg.zzz(i));
    }

    public static int zzr(List<Integer> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzka) {
            zzka zzka = (zzka) list;
            i = 0;
            while (i2 < size) {
                int zze = zzka.zze(i2);
                i += zzjg.zzA((zze >> 31) ^ (zze + zze));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                int intValue = list.get(i2).intValue();
                i += zzjg.zzA((intValue >> 31) ^ (intValue + intValue));
                i2++;
            }
        }
        return i;
    }

    public static int zzs(int i, List<Long> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzt(list) + (size * zzjg.zzz(i));
    }

    public static int zzt(List<Long> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzkv) {
            zzkv zzkv = (zzkv) list;
            i = 0;
            while (i2 < size) {
                long zza2 = zzkv.zza(i2);
                i += zzjg.zzB((zza2 >> 63) ^ (zza2 + zza2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                long longValue = list.get(i2).longValue();
                i += zzjg.zzB((longValue >> 63) ^ (longValue + longValue));
                i2++;
            }
        }
        return i;
    }

    public static int zzu(int i, List<?> list) {
        int i2;
        int i3;
        int size = list.size();
        int i4 = 0;
        if (size == 0) {
            return 0;
        }
        int zzz = zzjg.zzz(i) * size;
        if (list instanceof zzko) {
            zzko zzko = (zzko) list;
            while (i4 < size) {
                Object zzf = zzko.zzf(i4);
                if (zzf instanceof zziy) {
                    i3 = zzjg.zzt((zziy) zzf);
                } else {
                    i3 = zzjg.zzy((String) zzf);
                }
                zzz += i3;
                i4++;
            }
        } else {
            while (i4 < size) {
                Object obj = list.get(i4);
                if (obj instanceof zziy) {
                    i2 = zzjg.zzt((zziy) obj);
                } else {
                    i2 = zzjg.zzy((String) obj);
                }
                zzz += i2;
                i4++;
            }
        }
        return zzz;
    }

    public static int zzv(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzw(list) + (size * zzjg.zzz(i));
    }

    public static int zzw(List<Integer> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzka) {
            zzka zzka = (zzka) list;
            i = 0;
            while (i2 < size) {
                i += zzjg.zzA(zzka.zze(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzjg.zzA(list.get(i2).intValue());
                i2++;
            }
        }
        return i;
    }

    public static int zzx(int i, List<Long> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzy(list) + (size * zzjg.zzz(i));
    }

    public static int zzy(List<Long> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzkv) {
            zzkv zzkv = (zzkv) list;
            i = 0;
            while (i2 < size) {
                i += zzjg.zzB(zzkv.zza(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzjg.zzB(list.get(i2).longValue());
                i2++;
            }
        }
        return i;
    }

    public static zzmi<?, ?> zzz() {
        return zzb;
    }
}
