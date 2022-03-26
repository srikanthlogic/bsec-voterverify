package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import sun.misc.Unsafe;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzlj<T> implements zzlr<T> {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzms.zzg();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzlg zzg;
    private final boolean zzh;
    private final boolean zzi;
    private final int[] zzj;
    private final int zzk;
    private final int zzl;
    private final zzku zzm;
    private final zzmi<?, ?> zzn;
    private final zzjm<?> zzo;
    private final zzll zzp;
    private final zzlb zzq;

    /* JADX WARN: Multi-variable type inference failed */
    private zzlj(int[] iArr, int[] iArr2, Object[] objArr, int i, int i2, zzlg zzlg, boolean z, boolean z2, int[] iArr3, int i3, int i4, zzll zzll, zzku zzku, zzmi<?, ?> zzmi, zzjm<?> zzjm, zzlb zzlb) {
        this.zzc = iArr;
        this.zzd = iArr2;
        this.zze = objArr;
        this.zzf = i;
        this.zzi = zzlg;
        boolean z3 = false;
        if (zzmi != 0 && zzmi.zzc((zzlg) i2)) {
            z3 = true;
        }
        this.zzh = z3;
        this.zzj = z2;
        this.zzk = iArr3;
        this.zzl = i3;
        this.zzp = i4;
        this.zzm = zzll;
        this.zzn = zzku;
        this.zzo = zzmi;
        this.zzg = i2;
        this.zzq = zzjm;
    }

    private static int zzA(int i) {
        return (i >>> 20) & 255;
    }

    private final int zzB(int i) {
        return this.zzc[i + 1];
    }

    private static <T> long zzC(T t, long j) {
        return ((Long) zzms.zzf(t, j)).longValue();
    }

    private final zzkd zzD(int i) {
        int i2 = i / 3;
        return (zzkd) this.zzd[i2 + i2 + 1];
    }

    private final zzlr zzE(int i) {
        int i2 = i / 3;
        int i3 = i2 + i2;
        zzlr zzlr = (zzlr) this.zzd[i3];
        if (zzlr != null) {
            return zzlr;
        }
        zzlr<T> zzb2 = zzlo.zza().zzb((Class) this.zzd[i3 + 1]);
        this.zzd[i3] = zzb2;
        return zzb2;
    }

    private final Object zzF(int i) {
        int i2 = i / 3;
        return this.zzd[i2 + i2];
    }

    private static Field zzG(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException e) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            String name = cls.getName();
            String arrays = Arrays.toString(declaredFields);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 40 + String.valueOf(name).length() + String.valueOf(arrays).length());
            sb.append("Field ");
            sb.append(str);
            sb.append(" for ");
            sb.append(name);
            sb.append(" not found. Known fields are ");
            sb.append(arrays);
            throw new RuntimeException(sb.toString());
        }
    }

    private final void zzH(T t, T t2, int i) {
        long zzB = (long) (zzB(i) & 1048575);
        if (zzM(t2, i)) {
            Object zzf = zzms.zzf(t, zzB);
            Object zzf2 = zzms.zzf(t2, zzB);
            if (zzf != null && zzf2 != null) {
                zzms.zzs(t, zzB, zzkh.zzg(zzf, zzf2));
                zzJ(t, i);
            } else if (zzf2 != null) {
                zzms.zzs(t, zzB, zzf2);
                zzJ(t, i);
            }
        }
    }

    private final void zzI(T t, T t2, int i) {
        Object obj;
        int zzB = zzB(i);
        int i2 = this.zzc[i];
        long j = (long) (zzB & 1048575);
        if (zzP(t2, i2, i)) {
            if (zzP(t, i2, i)) {
                obj = zzms.zzf(t, j);
            } else {
                obj = null;
            }
            Object zzf = zzms.zzf(t2, j);
            if (obj != null && zzf != null) {
                zzms.zzs(t, j, zzkh.zzg(obj, zzf));
                zzK(t, i2, i);
            } else if (zzf != null) {
                zzms.zzs(t, j, zzf);
                zzK(t, i2, i);
            }
        }
    }

    private final void zzJ(T t, int i) {
        int zzy = zzy(i);
        long j = (long) (1048575 & zzy);
        if (j != 1048575) {
            zzms.zzq(t, j, (1 << (zzy >>> 20)) | zzms.zzc(t, j));
        }
    }

    private final void zzK(T t, int i, int i2) {
        zzms.zzq(t, (long) (zzy(i2) & 1048575), i);
    }

    private final boolean zzL(T t, T t2, int i) {
        return zzM(t, i) == zzM(t2, i);
    }

    private final boolean zzM(T t, int i) {
        int zzy = zzy(i);
        long j = (long) (zzy & 1048575);
        if (j != 1048575) {
            return (zzms.zzc(t, j) & (1 << (zzy >>> 20))) != 0;
        }
        int zzB = zzB(i);
        long j2 = (long) (zzB & 1048575);
        switch (zzA(zzB)) {
            case 0:
                return zzms.zza(t, j2) != 0.0d;
            case 1:
                return zzms.zzb(t, j2) != 0.0f;
            case 2:
                return zzms.zzd(t, j2) != 0;
            case 3:
                return zzms.zzd(t, j2) != 0;
            case 4:
                return zzms.zzc(t, j2) != 0;
            case 5:
                return zzms.zzd(t, j2) != 0;
            case 6:
                return zzms.zzc(t, j2) != 0;
            case 7:
                return zzms.zzw(t, j2);
            case 8:
                Object zzf = zzms.zzf(t, j2);
                if (zzf instanceof String) {
                    return !((String) zzf).isEmpty();
                }
                if (zzf instanceof zziy) {
                    return !zziy.zzb.equals(zzf);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzms.zzf(t, j2) != null;
            case 10:
                return !zziy.zzb.equals(zzms.zzf(t, j2));
            case 11:
                return zzms.zzc(t, j2) != 0;
            case 12:
                return zzms.zzc(t, j2) != 0;
            case 13:
                return zzms.zzc(t, j2) != 0;
            case 14:
                return zzms.zzd(t, j2) != 0;
            case 15:
                return zzms.zzc(t, j2) != 0;
            case 16:
                return zzms.zzd(t, j2) != 0;
            case 17:
                return zzms.zzf(t, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zzN(T t, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return zzM(t, i);
        }
        return (i3 & i4) != 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean zzO(Object obj, int i, zzlr zzlr) {
        return zzlr.zzj(zzms.zzf(obj, (long) (i & 1048575)));
    }

    private final boolean zzP(T t, int i, int i2) {
        return zzms.zzc(t, (long) (zzy(i2) & 1048575)) == i;
    }

    private static <T> boolean zzQ(T t, long j) {
        return ((Boolean) zzms.zzf(t, j)).booleanValue();
    }

    private final void zzR(T t, zzjh zzjh) throws IOException {
        int i;
        int i2;
        if (!this.zzh) {
            int length = this.zzc.length;
            Unsafe unsafe = zzb;
            int i3 = 1048575;
            int i4 = 1048575;
            int i5 = 0;
            int i6 = 0;
            while (i5 < length) {
                int zzB = zzB(i5);
                int i7 = this.zzc[i5];
                int zzA = zzA(zzB);
                if (zzA <= 17) {
                    int i8 = this.zzc[i5 + 2];
                    i = i8 & i3;
                    if (i != i4) {
                        i6 = unsafe.getInt(t, (long) i);
                    } else {
                        i = i4;
                    }
                    i2 = 1 << (i8 >>> 20);
                } else {
                    i = i4;
                    i2 = 0;
                }
                long j = (long) (zzB & i3);
                switch (zzA) {
                    case 0:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzf(i7, zzms.zza(t, j));
                            break;
                        }
                    case 1:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzo(i7, zzms.zzb(t, j));
                            break;
                        }
                    case 2:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzt(i7, unsafe.getLong(t, j));
                            break;
                        }
                    case 3:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzJ(i7, unsafe.getLong(t, j));
                            break;
                        }
                    case 4:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzr(i7, unsafe.getInt(t, j));
                            break;
                        }
                    case 5:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzm(i7, unsafe.getLong(t, j));
                            break;
                        }
                    case 6:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzk(i7, unsafe.getInt(t, j));
                            break;
                        }
                    case 7:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzb(i7, zzms.zzw(t, j));
                            break;
                        }
                    case 8:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzT(i7, unsafe.getObject(t, j), zzjh);
                            break;
                        }
                    case 9:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzv(i7, unsafe.getObject(t, j), zzE(i5));
                            break;
                        }
                    case 10:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzd(i7, (zziy) unsafe.getObject(t, j));
                            break;
                        }
                    case 11:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzH(i7, unsafe.getInt(t, j));
                            break;
                        }
                    case 12:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzi(i7, unsafe.getInt(t, j));
                            break;
                        }
                    case 13:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzw(i7, unsafe.getInt(t, j));
                            break;
                        }
                    case 14:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzy(i7, unsafe.getLong(t, j));
                            break;
                        }
                    case 15:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzA(i7, unsafe.getInt(t, j));
                            break;
                        }
                    case 16:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzC(i7, unsafe.getLong(t, j));
                            break;
                        }
                    case 17:
                        if ((i2 & i6) == 0) {
                            break;
                        } else {
                            zzjh.zzq(i7, unsafe.getObject(t, j), zzE(i5));
                            break;
                        }
                    case 18:
                        zzlt.zzL(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 19:
                        zzlt.zzP(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 20:
                        zzlt.zzS(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 21:
                        zzlt.zzaa(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 22:
                        zzlt.zzR(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 23:
                        zzlt.zzO(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 24:
                        zzlt.zzN(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 25:
                        zzlt.zzJ(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 26:
                        zzlt.zzY(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh);
                        break;
                    case 27:
                        zzlt.zzT(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, zzE(i5));
                        break;
                    case 28:
                        zzlt.zzK(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh);
                        break;
                    case 29:
                        zzlt.zzZ(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 30:
                        zzlt.zzM(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 31:
                        zzlt.zzU(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 32:
                        zzlt.zzV(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 33:
                        zzlt.zzW(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 34:
                        zzlt.zzX(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, false);
                        break;
                    case 35:
                        zzlt.zzL(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 36:
                        zzlt.zzP(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 37:
                        zzlt.zzS(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 38:
                        zzlt.zzaa(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 39:
                        zzlt.zzR(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 40:
                        zzlt.zzO(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 41:
                        zzlt.zzN(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 42:
                        zzlt.zzJ(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 43:
                        zzlt.zzZ(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 44:
                        zzlt.zzM(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 45:
                        zzlt.zzU(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 46:
                        zzlt.zzV(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 47:
                        zzlt.zzW(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 48:
                        zzlt.zzX(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, true);
                        break;
                    case 49:
                        zzlt.zzQ(this.zzc[i5], (List) unsafe.getObject(t, j), zzjh, zzE(i5));
                        break;
                    case 50:
                        zzS(zzjh, i7, unsafe.getObject(t, j), i5);
                        break;
                    case 51:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzf(i7, zzn(t, j));
                            break;
                        }
                    case 52:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzo(i7, zzo(t, j));
                            break;
                        }
                    case 53:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzt(i7, zzC(t, j));
                            break;
                        }
                    case 54:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzJ(i7, zzC(t, j));
                            break;
                        }
                    case 55:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzr(i7, zzr(t, j));
                            break;
                        }
                    case 56:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzm(i7, zzC(t, j));
                            break;
                        }
                    case 57:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzk(i7, zzr(t, j));
                            break;
                        }
                    case 58:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzb(i7, zzQ(t, j));
                            break;
                        }
                    case 59:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzT(i7, unsafe.getObject(t, j), zzjh);
                            break;
                        }
                    case 60:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzv(i7, unsafe.getObject(t, j), zzE(i5));
                            break;
                        }
                    case 61:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzd(i7, (zziy) unsafe.getObject(t, j));
                            break;
                        }
                    case 62:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzH(i7, zzr(t, j));
                            break;
                        }
                    case 63:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzi(i7, zzr(t, j));
                            break;
                        }
                    case 64:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzw(i7, zzr(t, j));
                            break;
                        }
                    case 65:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzy(i7, zzC(t, j));
                            break;
                        }
                    case 66:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzA(i7, zzr(t, j));
                            break;
                        }
                    case 67:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzC(i7, zzC(t, j));
                            break;
                        }
                    case 68:
                        if (!zzP(t, i7, i5)) {
                            break;
                        } else {
                            zzjh.zzq(i7, unsafe.getObject(t, j), zzE(i5));
                            break;
                        }
                }
                i5 += 3;
                i4 = i;
                i3 = 1048575;
            }
            zzmi<?, ?> zzmi = this.zzn;
            zzmi.zzi(zzmi.zzc(t), zzjh);
            return;
        }
        this.zzo.zza(t);
        throw null;
    }

    private static final void zzT(int i, Object obj, zzjh zzjh) throws IOException {
        if (obj instanceof String) {
            zzjh.zzF(i, (String) obj);
        } else {
            zzjh.zzd(i, (zziy) obj);
        }
    }

    static zzmj zzd(Object obj) {
        zzjz zzjz = (zzjz) obj;
        zzmj zzmj = zzjz.zzc;
        if (zzmj != zzmj.zzc()) {
            return zzmj;
        }
        zzmj zze = zzmj.zze();
        zzjz.zzc = zze;
        return zze;
    }

    public static <T> zzlj<T> zzk(Class<T> cls, zzld zzld, zzll zzll, zzku zzku, zzmi<?, ?> zzmi, zzjm<?> zzjm, zzlb zzlb) {
        if (zzld instanceof zzlq) {
            return zzl((zzlq) zzld, zzll, zzku, zzmi, zzjm, zzlb);
        }
        zzmf zzmf = (zzmf) zzld;
        throw null;
    }

    static <T> zzlj<T> zzl(zzlq zzlq, zzll zzll, zzku zzku, zzmi<?, ?> zzmi, zzjm<?> zzjm, zzlb zzlb) {
        int i;
        int i2;
        int i3;
        int i4;
        int[] iArr;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int[] iArr2;
        int i13;
        String str;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        Field field;
        char charAt;
        int i22;
        Field field2;
        Field field3;
        int i23;
        char charAt2;
        int i24;
        char charAt3;
        int i25;
        char charAt4;
        int i26;
        int i27;
        int i28;
        int i29;
        int i30;
        int i31;
        int i32;
        char charAt5;
        int i33;
        char charAt6;
        char charAt7;
        char charAt8;
        char charAt9;
        char charAt10;
        char charAt11;
        int i34;
        char charAt12;
        int i35;
        char charAt13;
        boolean z = zzlq.zzc() == 2;
        String zzd = zzlq.zzd();
        int length = zzd.length();
        char c = 55296;
        if (zzd.charAt(0) >= 55296) {
            int i36 = 1;
            while (true) {
                i = i36 + 1;
                if (zzd.charAt(i36) < 55296) {
                    break;
                }
                i36 = i;
            }
        } else {
            i = 1;
        }
        int i37 = i + 1;
        int charAt14 = zzd.charAt(i);
        if (charAt14 >= 55296) {
            int i38 = charAt14 & 8191;
            int i39 = 13;
            while (true) {
                i35 = i37 + 1;
                charAt13 = zzd.charAt(i37);
                if (charAt13 < 55296) {
                    break;
                }
                i38 |= (charAt13 & 8191) << i39;
                i39 += 13;
                i37 = i35;
            }
            charAt14 = i38 | (charAt13 << i39);
            i37 = i35;
        }
        if (charAt14 == 0) {
            iArr = zza;
            i7 = 0;
            i6 = 0;
            i5 = 0;
            i3 = 0;
            i2 = 0;
            i4 = i37;
            i9 = 0;
            i8 = 0;
        } else {
            int i40 = i37 + 1;
            int charAt15 = zzd.charAt(i37);
            if (charAt15 >= 55296) {
                int i41 = charAt15 & 8191;
                int i42 = 13;
                while (true) {
                    i34 = i40 + 1;
                    charAt12 = zzd.charAt(i40);
                    if (charAt12 < 55296) {
                        break;
                    }
                    i41 |= (charAt12 & 8191) << i42;
                    i42 += 13;
                    i40 = i34;
                }
                charAt15 = i41 | (charAt12 << i42);
                i40 = i34;
            }
            int i43 = i40 + 1;
            int charAt16 = zzd.charAt(i40);
            if (charAt16 >= 55296) {
                int i44 = charAt16 & 8191;
                int i45 = 13;
                while (true) {
                    i26 = i43 + 1;
                    charAt11 = zzd.charAt(i43);
                    if (charAt11 < 55296) {
                        break;
                    }
                    i44 |= (charAt11 & 8191) << i45;
                    i45 += 13;
                    i43 = i26;
                }
                charAt16 = i44 | (charAt11 << i45);
            } else {
                i26 = i43;
            }
            int i46 = i26 + 1;
            char charAt17 = zzd.charAt(i26);
            if (charAt17 >= 55296) {
                int i47 = charAt17 & 8191;
                int i48 = 13;
                while (true) {
                    i27 = i46 + 1;
                    charAt10 = zzd.charAt(i46);
                    if (charAt10 < 55296) {
                        break;
                    }
                    i47 |= (charAt10 & 8191) << i48;
                    i48 += 13;
                    i46 = i27;
                }
                i28 = (charAt10 << i48) | i47;
            } else {
                i27 = i46;
                i28 = charAt17;
            }
            int i49 = i27 + 1;
            int charAt18 = zzd.charAt(i27);
            if (charAt18 >= 55296) {
                int i50 = charAt18 & 8191;
                int i51 = 13;
                while (true) {
                    i29 = i49 + 1;
                    charAt9 = zzd.charAt(i49);
                    if (charAt9 < 55296) {
                        break;
                    }
                    i50 |= (charAt9 & 8191) << i51;
                    i51 += 13;
                    i49 = i29;
                }
                charAt18 = (charAt9 << i51) | i50;
            } else {
                i29 = i49;
            }
            int i52 = i29 + 1;
            i6 = zzd.charAt(i29);
            if (i6 >= 55296) {
                int i53 = i6 & 8191;
                int i54 = 13;
                while (true) {
                    i30 = i52 + 1;
                    charAt8 = zzd.charAt(i52);
                    if (charAt8 < 55296) {
                        break;
                    }
                    i53 |= (charAt8 & 8191) << i54;
                    i54 += 13;
                    i52 = i30;
                }
                i6 = (charAt8 << i54) | i53;
            } else {
                i30 = i52;
            }
            int i55 = i30 + 1;
            int charAt19 = zzd.charAt(i30);
            if (charAt19 >= 55296) {
                int i56 = charAt19 & 8191;
                int i57 = 13;
                while (true) {
                    i31 = i55 + 1;
                    charAt7 = zzd.charAt(i55);
                    if (charAt7 < 55296) {
                        break;
                    }
                    i56 |= (charAt7 & 8191) << i57;
                    i57 += 13;
                    i55 = i31;
                }
                charAt19 = (charAt7 << i57) | i56;
            } else {
                i31 = i55;
            }
            int i58 = i31 + 1;
            int charAt20 = zzd.charAt(i31);
            if (charAt20 >= 55296) {
                int i59 = charAt20 & 8191;
                int i60 = 13;
                while (true) {
                    i33 = i58 + 1;
                    charAt6 = zzd.charAt(i58);
                    if (charAt6 < 55296) {
                        break;
                    }
                    i59 |= (charAt6 & 8191) << i60;
                    i60 += 13;
                    i58 = i33;
                }
                charAt20 = i59 | (charAt6 << i60);
                i58 = i33;
            }
            i4 = i58 + 1;
            int charAt21 = zzd.charAt(i58);
            if (charAt21 >= 55296) {
                int i61 = charAt21 & 8191;
                int i62 = 13;
                while (true) {
                    i32 = i4 + 1;
                    charAt5 = zzd.charAt(i4);
                    if (charAt5 < 55296) {
                        break;
                    }
                    i61 |= (charAt5 & 8191) << i62;
                    i62 += 13;
                    i4 = i32;
                }
                charAt21 = i61 | (charAt5 << i62);
                i4 = i32;
            }
            iArr = new int[charAt21 + charAt19 + charAt20];
            i8 = charAt15 + charAt15 + charAt16;
            i3 = i28;
            i2 = charAt18;
            i7 = charAt15;
            i9 = charAt19;
            i5 = charAt21;
        }
        Unsafe unsafe = zzb;
        Object[] zze = zzlq.zze();
        Class<?> cls = zzlq.zza().getClass();
        int[] iArr3 = new int[i6 * 3];
        Object[] objArr = new Object[i6 + i6];
        int i63 = i5 + i9;
        int i64 = i8;
        int i65 = i5;
        int i66 = i63;
        int i67 = 0;
        int i68 = 0;
        while (i4 < length) {
            int i69 = i4 + 1;
            char charAt22 = zzd.charAt(i4);
            if (charAt22 >= c) {
                int i70 = charAt22 & 8191;
                int i71 = 13;
                int i72 = i69;
                while (true) {
                    i25 = i72 + 1;
                    charAt4 = zzd.charAt(i72);
                    if (charAt4 < c) {
                        break;
                    }
                    i70 |= (charAt4 & 8191) << i71;
                    i71 += 13;
                    i72 = i25;
                }
                i10 = i70 | (charAt4 << i71);
                i11 = i25;
            } else {
                i10 = charAt22;
                i11 = i69;
            }
            int i73 = i11 + 1;
            int charAt23 = zzd.charAt(i11);
            if (charAt23 >= c) {
                int i74 = charAt23 & 8191;
                int i75 = 13;
                int i76 = i73;
                while (true) {
                    i24 = i76 + 1;
                    charAt3 = zzd.charAt(i76);
                    if (charAt3 < c) {
                        break;
                    }
                    i74 |= (charAt3 & 8191) << i75;
                    i75 += 13;
                    i76 = i24;
                }
                charAt23 = i74 | (charAt3 << i75);
                i12 = i24;
            } else {
                i12 = i73;
            }
            int i77 = charAt23 & 255;
            if ((charAt23 & 1024) != 0) {
                iArr[i68] = i67;
                i68++;
            }
            if (i77 >= 51) {
                int i78 = i12 + 1;
                int charAt24 = zzd.charAt(i12);
                int i79 = i78;
                char c2 = 55296;
                if (charAt24 >= 55296) {
                    int i80 = charAt24 & 8191;
                    int i81 = 13;
                    int i82 = i79;
                    while (true) {
                        i23 = i82 + 1;
                        charAt2 = zzd.charAt(i82);
                        if (charAt2 < c2) {
                            break;
                        }
                        i80 |= (charAt2 & 8191) << i81;
                        i81 += 13;
                        i82 = i23;
                        c2 = 55296;
                    }
                    charAt24 = i80 | (charAt2 << i81);
                    i79 = i23;
                }
                int i83 = i77 - 51;
                i13 = i68;
                if (i83 == 9 || i83 == 17) {
                    int i84 = i67 / 3;
                    i22 = i64 + 1;
                    objArr[i84 + i84 + 1] = zze[i64];
                } else if (i83 != 12 || z) {
                    i22 = i64;
                } else {
                    int i85 = i67 / 3;
                    i22 = i64 + 1;
                    objArr[i85 + i85 + 1] = zze[i64];
                }
                int i86 = charAt24 + charAt24;
                Object obj = zze[i86];
                i64 = i22;
                if (obj instanceof Field) {
                    field2 = (Field) obj;
                } else {
                    field2 = zzG(cls, (String) obj);
                    zze[i86] = field2;
                }
                iArr2 = iArr3;
                i15 = (int) unsafe.objectFieldOffset(field2);
                int i87 = i86 + 1;
                Object obj2 = zze[i87];
                if (obj2 instanceof Field) {
                    field3 = (Field) obj2;
                } else {
                    field3 = zzG(cls, (String) obj2);
                    zze[i87] = field3;
                }
                i14 = (int) unsafe.objectFieldOffset(field3);
                str = zzd;
                i17 = i79;
                i16 = 0;
            } else {
                i13 = i68;
                iArr2 = iArr3;
                int i88 = i64 + 1;
                Field zzG = zzG(cls, (String) zze[i64]);
                if (!(i77 == 9 || i77 == 17)) {
                    if (i77 == 27 || i77 == 49) {
                        int i89 = i67 / 3;
                        i20 = i88 + 1;
                        objArr[i89 + i89 + 1] = zze[i88];
                    } else if (i77 == 12 || i77 == 30 || i77 == 44) {
                        if (!z) {
                            int i90 = i67 / 3;
                            i20 = i88 + 1;
                            objArr[i90 + i90 + 1] = zze[i88];
                        } else {
                            i20 = i88;
                        }
                    } else if (i77 == 50) {
                        int i91 = i65 + 1;
                        iArr[i65] = i67;
                        int i92 = i67 / 3;
                        int i93 = i92 + i92;
                        i20 = i88 + 1;
                        objArr[i93] = zze[i88];
                        if ((charAt23 & 2048) != 0) {
                            objArr[i93 + 1] = zze[i20];
                            i20++;
                        }
                        i65 = i91;
                    } else {
                        i20 = i88;
                    }
                    i15 = (int) unsafe.objectFieldOffset(zzG);
                    if ((charAt23 & 4096) == 4096 || i77 > 17) {
                        str = zzd;
                        i17 = i12;
                        i16 = 0;
                        i14 = 1048575;
                    } else {
                        int i94 = i12 + 1;
                        int charAt25 = zzd.charAt(i12);
                        if (charAt25 >= 55296) {
                            int i95 = charAt25 & 8191;
                            int i96 = 13;
                            while (true) {
                                i21 = i94 + 1;
                                charAt = zzd.charAt(i94);
                                if (charAt < 55296) {
                                    break;
                                }
                                i95 |= (charAt & 8191) << i96;
                                i96 += 13;
                                i94 = i21;
                            }
                            charAt25 = i95 | (charAt << i96);
                        } else {
                            i21 = i94;
                        }
                        int i97 = i7 + i7 + (charAt25 / 32);
                        Object obj3 = zze[i97];
                        str = zzd;
                        if (obj3 instanceof Field) {
                            field = (Field) obj3;
                        } else {
                            field = zzG(cls, (String) obj3);
                            zze[i97] = field;
                        }
                        i14 = (int) unsafe.objectFieldOffset(field);
                        i16 = charAt25 % 32;
                        i17 = i21;
                    }
                    if (i77 >= 18 || i77 > 49) {
                        i64 = i20;
                    } else {
                        iArr[i66] = i15;
                        i66++;
                        i64 = i20;
                    }
                }
                int i98 = i67 / 3;
                objArr[i98 + i98 + 1] = zzG.getType();
                i20 = i88;
                i15 = (int) unsafe.objectFieldOffset(zzG);
                if ((charAt23 & 4096) == 4096) {
                }
                str = zzd;
                i17 = i12;
                i16 = 0;
                i14 = 1048575;
                if (i77 >= 18) {
                }
                i64 = i20;
            }
            int i99 = i67 + 1;
            iArr2[i67] = i10;
            int i100 = i99 + 1;
            if ((charAt23 & 512) != 0) {
                i18 = 536870912;
            } else {
                i18 = 0;
            }
            if ((charAt23 & 256) != 0) {
                i19 = 268435456;
            } else {
                i19 = 0;
            }
            iArr2[i99] = i15 | (i77 << 20) | i18 | i19;
            iArr2[i100] = (i16 << 20) | i14;
            i4 = i17;
            i67 = i100 + 1;
            length = length;
            zzd = str;
            i68 = i13;
            iArr3 = iArr2;
            c = 55296;
        }
        return new zzlj<>(iArr3, objArr, i3, i2, zzlq.zza(), z, false, iArr, i5, i63, zzll, zzku, zzmi, zzjm, zzlb, null);
    }

    private static <T> double zzn(T t, long j) {
        return ((Double) zzms.zzf(t, j)).doubleValue();
    }

    private static <T> float zzo(T t, long j) {
        return ((Float) zzms.zzf(t, j)).floatValue();
    }

    private final int zzp(T t) {
        int i;
        Unsafe unsafe = zzb;
        int i2 = 1048575;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < this.zzc.length; i5 += 3) {
            int zzB = zzB(i5);
            int i6 = this.zzc[i5];
            int zzA = zzA(zzB);
            if (zzA <= 17) {
                int i7 = this.zzc[i5 + 2];
                int i8 = i7 & 1048575;
                i = 1 << (i7 >>> 20);
                if (i8 != i2) {
                    i4 = unsafe.getInt(t, (long) i8);
                    i2 = i8;
                }
            } else {
                i = 0;
            }
            long j = (long) (zzB & 1048575);
            switch (zzA) {
                case 0:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzB(unsafe.getLong(t, j));
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzB(unsafe.getLong(t, j));
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzv(unsafe.getInt(t, j));
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 6:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 7:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + 1;
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if ((i4 & i) != 0) {
                        Object object = unsafe.getObject(t, j);
                        if (object instanceof zziy) {
                            int zzA2 = zzjg.zzA(i6 << 3);
                            int zzd = ((zziy) object).zzd();
                            i3 += zzA2 + zzjg.zzA(zzd) + zzd;
                            break;
                        } else {
                            i3 += zzjg.zzA(i6 << 3) + zzjg.zzy((String) object);
                            break;
                        }
                    } else {
                        break;
                    }
                case 9:
                    if ((i4 & i) != 0) {
                        i3 += zzlt.zzo(i6, unsafe.getObject(t, j), zzE(i5));
                        break;
                    } else {
                        break;
                    }
                case 10:
                    if ((i4 & i) != 0) {
                        int zzA3 = zzjg.zzA(i6 << 3);
                        int zzd2 = ((zziy) unsafe.getObject(t, j)).zzd();
                        i3 += zzA3 + zzjg.zzA(zzd2) + zzd2;
                        break;
                    } else {
                        break;
                    }
                case 11:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzA(unsafe.getInt(t, j));
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzv(unsafe.getInt(t, j));
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 14:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzA(i6 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 15:
                    if ((i4 & i) != 0) {
                        int i9 = unsafe.getInt(t, j);
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzA((i9 >> 31) ^ (i9 + i9));
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if ((i4 & i) != 0) {
                        long j2 = unsafe.getLong(t, j);
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzB((j2 >> 63) ^ (j2 + j2));
                        break;
                    } else {
                        break;
                    }
                case 17:
                    if ((i4 & i) != 0) {
                        i3 += zzjg.zzu(i6, (zzlg) unsafe.getObject(t, j), zzE(i5));
                        break;
                    } else {
                        break;
                    }
                case 18:
                    i3 += zzlt.zzh(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 19:
                    i3 += zzlt.zzf(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 20:
                    i3 += zzlt.zzm(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 21:
                    i3 += zzlt.zzx(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 22:
                    i3 += zzlt.zzk(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 23:
                    i3 += zzlt.zzh(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 24:
                    i3 += zzlt.zzf(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 25:
                    i3 += zzlt.zza(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 26:
                    i3 += zzlt.zzu(i6, (List) unsafe.getObject(t, j));
                    break;
                case 27:
                    i3 += zzlt.zzp(i6, (List) unsafe.getObject(t, j), zzE(i5));
                    break;
                case 28:
                    i3 += zzlt.zzc(i6, (List) unsafe.getObject(t, j));
                    break;
                case 29:
                    i3 += zzlt.zzv(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 30:
                    i3 += zzlt.zzd(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 31:
                    i3 += zzlt.zzf(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 32:
                    i3 += zzlt.zzh(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 33:
                    i3 += zzlt.zzq(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 34:
                    i3 += zzlt.zzs(i6, (List) unsafe.getObject(t, j), false);
                    break;
                case 35:
                    int zzi = zzlt.zzi((List) unsafe.getObject(t, j));
                    if (zzi > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzi) + zzi;
                        break;
                    } else {
                        break;
                    }
                case 36:
                    int zzg = zzlt.zzg((List) unsafe.getObject(t, j));
                    if (zzg > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzg) + zzg;
                        break;
                    } else {
                        break;
                    }
                case 37:
                    int zzn = zzlt.zzn((List) unsafe.getObject(t, j));
                    if (zzn > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzn) + zzn;
                        break;
                    } else {
                        break;
                    }
                case 38:
                    int zzy = zzlt.zzy((List) unsafe.getObject(t, j));
                    if (zzy > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzy) + zzy;
                        break;
                    } else {
                        break;
                    }
                case 39:
                    int zzl = zzlt.zzl((List) unsafe.getObject(t, j));
                    if (zzl > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzl) + zzl;
                        break;
                    } else {
                        break;
                    }
                case 40:
                    int zzi2 = zzlt.zzi((List) unsafe.getObject(t, j));
                    if (zzi2 > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzi2) + zzi2;
                        break;
                    } else {
                        break;
                    }
                case 41:
                    int zzg2 = zzlt.zzg((List) unsafe.getObject(t, j));
                    if (zzg2 > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzg2) + zzg2;
                        break;
                    } else {
                        break;
                    }
                case 42:
                    int zzb2 = zzlt.zzb((List) unsafe.getObject(t, j));
                    if (zzb2 > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzb2) + zzb2;
                        break;
                    } else {
                        break;
                    }
                case 43:
                    int zzw = zzlt.zzw((List) unsafe.getObject(t, j));
                    if (zzw > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzw) + zzw;
                        break;
                    } else {
                        break;
                    }
                case 44:
                    int zze = zzlt.zze((List) unsafe.getObject(t, j));
                    if (zze > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zze) + zze;
                        break;
                    } else {
                        break;
                    }
                case 45:
                    int zzg3 = zzlt.zzg((List) unsafe.getObject(t, j));
                    if (zzg3 > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzg3) + zzg3;
                        break;
                    } else {
                        break;
                    }
                case 46:
                    int zzi3 = zzlt.zzi((List) unsafe.getObject(t, j));
                    if (zzi3 > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzi3) + zzi3;
                        break;
                    } else {
                        break;
                    }
                case 47:
                    int zzr = zzlt.zzr((List) unsafe.getObject(t, j));
                    if (zzr > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzr) + zzr;
                        break;
                    } else {
                        break;
                    }
                case 48:
                    int zzt = zzlt.zzt((List) unsafe.getObject(t, j));
                    if (zzt > 0) {
                        i3 += zzjg.zzz(i6) + zzjg.zzA(zzt) + zzt;
                        break;
                    } else {
                        break;
                    }
                case 49:
                    i3 += zzlt.zzj(i6, (List) unsafe.getObject(t, j), zzE(i5));
                    break;
                case 50:
                    zzlb.zza(i6, unsafe.getObject(t, j), zzF(i5));
                    break;
                case 51:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzB(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzB(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzv(zzr(t, j));
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + 1;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (zzP(t, i6, i5)) {
                        Object object2 = unsafe.getObject(t, j);
                        if (object2 instanceof zziy) {
                            int zzA4 = zzjg.zzA(i6 << 3);
                            int zzd3 = ((zziy) object2).zzd();
                            i3 += zzA4 + zzjg.zzA(zzd3) + zzd3;
                            break;
                        } else {
                            i3 += zzjg.zzA(i6 << 3) + zzjg.zzy((String) object2);
                            break;
                        }
                    } else {
                        break;
                    }
                case 60:
                    if (zzP(t, i6, i5)) {
                        i3 += zzlt.zzo(i6, unsafe.getObject(t, j), zzE(i5));
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (zzP(t, i6, i5)) {
                        int zzA5 = zzjg.zzA(i6 << 3);
                        int zzd4 = ((zziy) unsafe.getObject(t, j)).zzd();
                        i3 += zzA5 + zzjg.zzA(zzd4) + zzd4;
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzA(zzr(t, j));
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzv(zzr(t, j));
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzA(i6 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (zzP(t, i6, i5)) {
                        int zzr2 = zzr(t, j);
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzA((zzr2 >> 31) ^ (zzr2 + zzr2));
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zzP(t, i6, i5)) {
                        long zzC = zzC(t, j);
                        i3 += zzjg.zzA(i6 << 3) + zzjg.zzB((zzC >> 63) ^ (zzC + zzC));
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (zzP(t, i6, i5)) {
                        i3 += zzjg.zzu(i6, (zzlg) unsafe.getObject(t, j), zzE(i5));
                        break;
                    } else {
                        break;
                    }
            }
        }
        zzmi<?, ?> zzmi = this.zzn;
        int zza2 = i3 + zzmi.zza(zzmi.zzc(t));
        if (!this.zzh) {
            return zza2;
        }
        this.zzo.zza(t);
        throw null;
    }

    private final int zzq(T t) {
        Unsafe unsafe = zzb;
        int i = 0;
        for (int i2 = 0; i2 < this.zzc.length; i2 += 3) {
            int zzB = zzB(i2);
            int zzA = zzA(zzB);
            int i3 = this.zzc[i2];
            long j = (long) (zzB & 1048575);
            if (zzA >= zzjr.DOUBLE_LIST_PACKED.zza() && zzA <= zzjr.SINT64_LIST_PACKED.zza()) {
                int i4 = this.zzc[i2 + 2];
            }
            switch (zzA) {
                case 0:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzB(zzms.zzd(t, j));
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzB(zzms.zzd(t, j));
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzv(zzms.zzc(t, j));
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 6:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 7:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + 1;
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (zzM(t, i2)) {
                        Object zzf = zzms.zzf(t, j);
                        if (zzf instanceof zziy) {
                            int zzA2 = zzjg.zzA(i3 << 3);
                            int zzd = ((zziy) zzf).zzd();
                            i += zzA2 + zzjg.zzA(zzd) + zzd;
                            break;
                        } else {
                            i += zzjg.zzA(i3 << 3) + zzjg.zzy((String) zzf);
                            break;
                        }
                    } else {
                        break;
                    }
                case 9:
                    if (zzM(t, i2)) {
                        i += zzlt.zzo(i3, zzms.zzf(t, j), zzE(i2));
                        break;
                    } else {
                        break;
                    }
                case 10:
                    if (zzM(t, i2)) {
                        int zzA3 = zzjg.zzA(i3 << 3);
                        int zzd2 = ((zziy) zzms.zzf(t, j)).zzd();
                        i += zzA3 + zzjg.zzA(zzd2) + zzd2;
                        break;
                    } else {
                        break;
                    }
                case 11:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzA(zzms.zzc(t, j));
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzv(zzms.zzc(t, j));
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 14:
                    if (zzM(t, i2)) {
                        i += zzjg.zzA(i3 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 15:
                    if (zzM(t, i2)) {
                        int zzc = zzms.zzc(t, j);
                        i += zzjg.zzA(i3 << 3) + zzjg.zzA((zzc >> 31) ^ (zzc + zzc));
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (zzM(t, i2)) {
                        long zzd3 = zzms.zzd(t, j);
                        i += zzjg.zzA(i3 << 3) + zzjg.zzB((zzd3 >> 63) ^ (zzd3 + zzd3));
                        break;
                    } else {
                        break;
                    }
                case 17:
                    if (zzM(t, i2)) {
                        i += zzjg.zzu(i3, (zzlg) zzms.zzf(t, j), zzE(i2));
                        break;
                    } else {
                        break;
                    }
                case 18:
                    i += zzlt.zzh(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 19:
                    i += zzlt.zzf(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 20:
                    i += zzlt.zzm(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 21:
                    i += zzlt.zzx(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 22:
                    i += zzlt.zzk(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 23:
                    i += zzlt.zzh(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 24:
                    i += zzlt.zzf(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 25:
                    i += zzlt.zza(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 26:
                    i += zzlt.zzu(i3, (List) zzms.zzf(t, j));
                    break;
                case 27:
                    i += zzlt.zzp(i3, (List) zzms.zzf(t, j), zzE(i2));
                    break;
                case 28:
                    i += zzlt.zzc(i3, (List) zzms.zzf(t, j));
                    break;
                case 29:
                    i += zzlt.zzv(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 30:
                    i += zzlt.zzd(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 31:
                    i += zzlt.zzf(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 32:
                    i += zzlt.zzh(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 33:
                    i += zzlt.zzq(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 34:
                    i += zzlt.zzs(i3, (List) zzms.zzf(t, j), false);
                    break;
                case 35:
                    int zzi = zzlt.zzi((List) unsafe.getObject(t, j));
                    if (zzi > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzi) + zzi;
                        break;
                    } else {
                        break;
                    }
                case 36:
                    int zzg = zzlt.zzg((List) unsafe.getObject(t, j));
                    if (zzg > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzg) + zzg;
                        break;
                    } else {
                        break;
                    }
                case 37:
                    int zzn = zzlt.zzn((List) unsafe.getObject(t, j));
                    if (zzn > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzn) + zzn;
                        break;
                    } else {
                        break;
                    }
                case 38:
                    int zzy = zzlt.zzy((List) unsafe.getObject(t, j));
                    if (zzy > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzy) + zzy;
                        break;
                    } else {
                        break;
                    }
                case 39:
                    int zzl = zzlt.zzl((List) unsafe.getObject(t, j));
                    if (zzl > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzl) + zzl;
                        break;
                    } else {
                        break;
                    }
                case 40:
                    int zzi2 = zzlt.zzi((List) unsafe.getObject(t, j));
                    if (zzi2 > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzi2) + zzi2;
                        break;
                    } else {
                        break;
                    }
                case 41:
                    int zzg2 = zzlt.zzg((List) unsafe.getObject(t, j));
                    if (zzg2 > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzg2) + zzg2;
                        break;
                    } else {
                        break;
                    }
                case 42:
                    int zzb2 = zzlt.zzb((List) unsafe.getObject(t, j));
                    if (zzb2 > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzb2) + zzb2;
                        break;
                    } else {
                        break;
                    }
                case 43:
                    int zzw = zzlt.zzw((List) unsafe.getObject(t, j));
                    if (zzw > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzw) + zzw;
                        break;
                    } else {
                        break;
                    }
                case 44:
                    int zze = zzlt.zze((List) unsafe.getObject(t, j));
                    if (zze > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zze) + zze;
                        break;
                    } else {
                        break;
                    }
                case 45:
                    int zzg3 = zzlt.zzg((List) unsafe.getObject(t, j));
                    if (zzg3 > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzg3) + zzg3;
                        break;
                    } else {
                        break;
                    }
                case 46:
                    int zzi3 = zzlt.zzi((List) unsafe.getObject(t, j));
                    if (zzi3 > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzi3) + zzi3;
                        break;
                    } else {
                        break;
                    }
                case 47:
                    int zzr = zzlt.zzr((List) unsafe.getObject(t, j));
                    if (zzr > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzr) + zzr;
                        break;
                    } else {
                        break;
                    }
                case 48:
                    int zzt = zzlt.zzt((List) unsafe.getObject(t, j));
                    if (zzt > 0) {
                        i += zzjg.zzz(i3) + zzjg.zzA(zzt) + zzt;
                        break;
                    } else {
                        break;
                    }
                case 49:
                    i += zzlt.zzj(i3, (List) zzms.zzf(t, j), zzE(i2));
                    break;
                case 50:
                    zzlb.zza(i3, zzms.zzf(t, j), zzF(i2));
                    break;
                case 51:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzB(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzB(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzv(zzr(t, j));
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + 1;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (zzP(t, i3, i2)) {
                        Object zzf2 = zzms.zzf(t, j);
                        if (zzf2 instanceof zziy) {
                            int zzA4 = zzjg.zzA(i3 << 3);
                            int zzd4 = ((zziy) zzf2).zzd();
                            i += zzA4 + zzjg.zzA(zzd4) + zzd4;
                            break;
                        } else {
                            i += zzjg.zzA(i3 << 3) + zzjg.zzy((String) zzf2);
                            break;
                        }
                    } else {
                        break;
                    }
                case 60:
                    if (zzP(t, i3, i2)) {
                        i += zzlt.zzo(i3, zzms.zzf(t, j), zzE(i2));
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (zzP(t, i3, i2)) {
                        int zzA5 = zzjg.zzA(i3 << 3);
                        int zzd5 = ((zziy) zzms.zzf(t, j)).zzd();
                        i += zzA5 + zzjg.zzA(zzd5) + zzd5;
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzA(zzr(t, j));
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + zzjg.zzv(zzr(t, j));
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + 4;
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzA(i3 << 3) + 8;
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (zzP(t, i3, i2)) {
                        int zzr2 = zzr(t, j);
                        i += zzjg.zzA(i3 << 3) + zzjg.zzA((zzr2 >> 31) ^ (zzr2 + zzr2));
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zzP(t, i3, i2)) {
                        long zzC = zzC(t, j);
                        i += zzjg.zzA(i3 << 3) + zzjg.zzB((zzC >> 63) ^ (zzC + zzC));
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (zzP(t, i3, i2)) {
                        i += zzjg.zzu(i3, (zzlg) zzms.zzf(t, j), zzE(i2));
                        break;
                    } else {
                        break;
                    }
            }
        }
        zzmi<?, ?> zzmi = this.zzn;
        return i + zzmi.zza(zzmi.zzc(t));
    }

    private static <T> int zzr(T t, long j) {
        return ((Integer) zzms.zzf(t, j)).intValue();
    }

    private final <K, V> int zzs(T t, byte[] bArr, int i, int i2, int i3, long j, zzik zzik) throws IOException {
        Unsafe unsafe = zzb;
        Object zzF = zzF(i3);
        Object object = unsafe.getObject(t, j);
        if (!((zzla) object).zze()) {
            zzla<K, V> zzb2 = zzla.zza().zzb();
            zzlb.zzb(zzb2, object);
            unsafe.putObject(t, j, zzb2);
        }
        zzkz zzkz = (zzkz) zzF;
        throw null;
    }

    private final int zzt(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzik zzik) throws IOException {
        boolean z;
        Object obj;
        Object obj2;
        Unsafe unsafe = zzb;
        long j2 = (long) (this.zzc[i8 + 2] & 1048575);
        switch (i7) {
            case 51:
                if (i5 == 1) {
                    unsafe.putObject(t, j, Double.valueOf(Double.longBitsToDouble(zzil.zzn(bArr, i))));
                    unsafe.putInt(t, j2, i4);
                    return i + 8;
                }
                break;
            case 52:
                if (i5 == 5) {
                    unsafe.putObject(t, j, Float.valueOf(Float.intBitsToFloat(zzil.zzb(bArr, i))));
                    unsafe.putInt(t, j2, i4);
                    return i + 4;
                }
                break;
            case 53:
            case 54:
                if (i5 == 0) {
                    int zzm = zzil.zzm(bArr, i, zzik);
                    unsafe.putObject(t, j, Long.valueOf(zzik.zzb));
                    unsafe.putInt(t, j2, i4);
                    return zzm;
                }
                break;
            case 55:
            case 62:
                if (i5 == 0) {
                    int zzj = zzil.zzj(bArr, i, zzik);
                    unsafe.putObject(t, j, Integer.valueOf(zzik.zza));
                    unsafe.putInt(t, j2, i4);
                    return zzj;
                }
                break;
            case 56:
            case 65:
                if (i5 == 1) {
                    unsafe.putObject(t, j, Long.valueOf(zzil.zzn(bArr, i)));
                    unsafe.putInt(t, j2, i4);
                    return i + 8;
                }
                break;
            case 57:
            case 64:
                if (i5 == 5) {
                    unsafe.putObject(t, j, Integer.valueOf(zzil.zzb(bArr, i)));
                    unsafe.putInt(t, j2, i4);
                    return i + 4;
                }
                break;
            case 58:
                if (i5 == 0) {
                    int zzm2 = zzil.zzm(bArr, i, zzik);
                    if (zzik.zzb != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    unsafe.putObject(t, j, Boolean.valueOf(z));
                    unsafe.putInt(t, j2, i4);
                    return zzm2;
                }
                break;
            case 59:
                if (i5 == 2) {
                    int zzj2 = zzil.zzj(bArr, i, zzik);
                    int i9 = zzik.zza;
                    if (i9 == 0) {
                        unsafe.putObject(t, j, "");
                    } else if ((i6 & 536870912) == 0 || zzmx.zzf(bArr, zzj2, zzj2 + i9)) {
                        unsafe.putObject(t, j, new String(bArr, zzj2, i9, zzkh.zza));
                        zzj2 += i9;
                    } else {
                        throw zzkj.zzc();
                    }
                    unsafe.putInt(t, j2, i4);
                    return zzj2;
                }
                break;
            case 60:
                if (i5 == 2) {
                    int zzd = zzil.zzd(zzE(i8), bArr, i, i2, zzik);
                    if (unsafe.getInt(t, j2) == i4) {
                        obj = unsafe.getObject(t, j);
                    } else {
                        obj = null;
                    }
                    if (obj == null) {
                        unsafe.putObject(t, j, zzik.zzc);
                    } else {
                        unsafe.putObject(t, j, zzkh.zzg(obj, zzik.zzc));
                    }
                    unsafe.putInt(t, j2, i4);
                    return zzd;
                }
                break;
            case 61:
                if (i5 == 2) {
                    int zza2 = zzil.zza(bArr, i, zzik);
                    unsafe.putObject(t, j, zzik.zzc);
                    unsafe.putInt(t, j2, i4);
                    return zza2;
                }
                break;
            case 63:
                if (i5 == 0) {
                    int zzj3 = zzil.zzj(bArr, i, zzik);
                    int i10 = zzik.zza;
                    zzkd zzD = zzD(i8);
                    if (zzD == null || zzD.zza(i10)) {
                        unsafe.putObject(t, j, Integer.valueOf(i10));
                        unsafe.putInt(t, j2, i4);
                    } else {
                        zzd(t).zzh(i3, Long.valueOf((long) i10));
                    }
                    return zzj3;
                }
                break;
            case 66:
                if (i5 == 0) {
                    int zzj4 = zzil.zzj(bArr, i, zzik);
                    unsafe.putObject(t, j, Integer.valueOf(zzjc.zzb(zzik.zza)));
                    unsafe.putInt(t, j2, i4);
                    return zzj4;
                }
                break;
            case 67:
                if (i5 == 0) {
                    int zzm3 = zzil.zzm(bArr, i, zzik);
                    unsafe.putObject(t, j, Long.valueOf(zzjc.zzc(zzik.zzb)));
                    unsafe.putInt(t, j2, i4);
                    return zzm3;
                }
                break;
            case 68:
                if (i5 == 3) {
                    int zzc = zzil.zzc(zzE(i8), bArr, i, i2, (i3 & -8) | 4, zzik);
                    if (unsafe.getInt(t, j2) == i4) {
                        obj2 = unsafe.getObject(t, j);
                    } else {
                        obj2 = null;
                    }
                    if (obj2 == null) {
                        unsafe.putObject(t, j, zzik.zzc);
                    } else {
                        unsafe.putObject(t, j, zzkh.zzg(obj2, zzik.zzc));
                    }
                    unsafe.putInt(t, j2, i4);
                    return zzc;
                }
                break;
        }
        return i;
    }

    private final int zzu(T t, byte[] bArr, int i, int i2, zzik zzik) throws IOException {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        Unsafe unsafe;
        int i8;
        int i9;
        int i10;
        int i11;
        Unsafe unsafe2;
        int i12;
        Unsafe unsafe3;
        int i13;
        int i14;
        int i15;
        int i16;
        zzlj<T> zzlj = this;
        T t2 = t;
        byte[] bArr2 = bArr;
        int i17 = i2;
        zzik zzik2 = zzik;
        Unsafe unsafe4 = zzb;
        int i18 = -1;
        int i19 = 1048575;
        int i20 = i;
        int i21 = 1048575;
        int i22 = -1;
        int i23 = 0;
        int i24 = 0;
        while (i20 < i17) {
            int i25 = i20 + 1;
            byte b = bArr2[i20];
            if (b < 0) {
                i4 = zzil.zzk(b, bArr2, i25, zzik2);
                i3 = zzik2.zza;
            } else {
                i3 = b;
                i4 = i25;
            }
            int i26 = i3 >>> 3;
            int i27 = i3 & 7;
            if (i26 > i22) {
                i5 = zzlj.zzx(i26, i23 / 3);
            } else {
                i5 = zzlj.zzw(i26);
            }
            if (i5 == i18) {
                i6 = i4;
                i7 = i26;
                unsafe = unsafe4;
                i8 = i18;
                i9 = 0;
            } else {
                int i28 = zzlj.zzc[i5 + 1];
                int zzA = zzA(i28);
                long j = (long) (i28 & i19);
                if (zzA <= 17) {
                    int i29 = zzlj.zzc[i5 + 2];
                    boolean z = true;
                    int i30 = 1 << (i29 >>> 20);
                    int i31 = i29 & 1048575;
                    if (i31 != i21) {
                        if (i21 != 1048575) {
                            i10 = i28;
                            i11 = i5;
                            long j2 = (long) i21;
                            unsafe3 = unsafe4;
                            unsafe3.putInt(t2, j2, i24);
                        } else {
                            i10 = i28;
                            i11 = i5;
                            unsafe3 = unsafe4;
                        }
                        if (i31 != 1048575) {
                            i24 = unsafe3.getInt(t2, (long) i31);
                        }
                        unsafe2 = unsafe3;
                        i21 = i31;
                    } else {
                        i10 = i28;
                        i11 = i5;
                        unsafe2 = unsafe4;
                    }
                    switch (zzA) {
                        case 0:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 1) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                zzms.zzo(t2, j, Double.longBitsToDouble(zzil.zzn(bArr2, i4)));
                                i20 = i4 + 8;
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i12;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        case 1:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 5) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                zzms.zzp(t2, j, Float.intBitsToFloat(zzil.zzb(bArr2, i4)));
                                i20 = i4 + 4;
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i12;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        case 2:
                        case 3:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 0) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                int zzm = zzil.zzm(bArr2, i4, zzik2);
                                unsafe2.putLong(t, j, zzik2.zzb);
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i12;
                                i20 = zzm;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        case 4:
                        case 11:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 0) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                i20 = zzil.zzj(bArr2, i4, zzik2);
                                unsafe2.putInt(t2, j, zzik2.zza);
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i12;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        case 5:
                        case 14:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 1) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                unsafe2.putLong(t, j, zzil.zzn(bArr2, i4));
                                i20 = i4 + 8;
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i12;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        case 6:
                        case 13:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 5) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                unsafe2.putInt(t2, j, zzil.zzb(bArr2, i4));
                                i20 = i4 + 4;
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                i23 = i12;
                                break;
                            }
                        case 7:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 0) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                i20 = zzil.zzm(bArr2, i4, zzik2);
                                if (zzik2.zzb == 0) {
                                    z = false;
                                }
                                zzms.zzm(t2, j, z);
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                i23 = i12;
                                break;
                            }
                        case 8:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 2) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                if ((i10 & 536870912) == 0) {
                                    i20 = zzil.zzg(bArr2, i4, zzik2);
                                } else {
                                    i20 = zzil.zzh(bArr2, i4, zzik2);
                                }
                                unsafe2.putObject(t2, j, zzik2.zzc);
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                i23 = i12;
                                break;
                            }
                        case 9:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 2) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                i20 = zzil.zzd(zzlj.zzE(i12), bArr2, i4, i2, zzik2);
                                Object object = unsafe2.getObject(t2, j);
                                if (object == null) {
                                    unsafe2.putObject(t2, j, zzik2.zzc);
                                } else {
                                    unsafe2.putObject(t2, j, zzkh.zzg(object, zzik2.zzc));
                                }
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i19 = 1048575;
                                i22 = i7;
                                i18 = -1;
                                i17 = i2;
                                i23 = i12;
                                break;
                            }
                        case 10:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 2) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                i20 = zzil.zza(bArr2, i4, zzik2);
                                unsafe2.putObject(t2, j, zzik2.zzc);
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i12;
                                i22 = i7;
                                i19 = 1048575;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        case 12:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 0) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                i20 = zzil.zzj(bArr2, i4, zzik2);
                                unsafe2.putInt(t2, j, zzik2.zza);
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i12;
                                i22 = i7;
                                i19 = 1048575;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        case 15:
                            i12 = i11;
                            i7 = i26;
                            if (i27 != 0) {
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                i20 = zzil.zzj(bArr2, i4, zzik2);
                                unsafe2.putInt(t2, j, zzjc.zzb(zzik2.zza));
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i12;
                                i22 = i7;
                                i19 = 1048575;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        case 16:
                            if (i27 != 0) {
                                i12 = i11;
                                i7 = i26;
                                i6 = i4;
                                unsafe = unsafe2;
                                i9 = i12;
                                i8 = -1;
                                break;
                            } else {
                                int zzm2 = zzil.zzm(bArr2, i4, zzik2);
                                unsafe2.putLong(t, j, zzjc.zzc(zzik2.zzb));
                                i24 |= i30;
                                unsafe4 = unsafe2;
                                i23 = i11;
                                i20 = zzm2;
                                i22 = i26;
                                i19 = 1048575;
                                i18 = -1;
                                i17 = i2;
                                break;
                            }
                        default:
                            i12 = i11;
                            i7 = i26;
                            i6 = i4;
                            unsafe = unsafe2;
                            i9 = i12;
                            i8 = -1;
                            break;
                    }
                } else {
                    i7 = i26;
                    if (zzA == 27) {
                        if (i27 == 2) {
                            zzkg zzkg = (zzkg) unsafe4.getObject(t2, j);
                            if (!zzkg.zzc()) {
                                int size = zzkg.size();
                                if (size == 0) {
                                    i13 = 10;
                                } else {
                                    i13 = size + size;
                                }
                                zzkg = zzkg.zzd(i13);
                                unsafe4.putObject(t2, j, zzkg);
                            }
                            i20 = zzil.zze(zzlj.zzE(i5), i3, bArr, i4, i2, zzkg, zzik);
                            i24 = i24;
                            unsafe4 = unsafe4;
                            i23 = i5;
                            i19 = 1048575;
                            i22 = i7;
                            i18 = -1;
                            i17 = i2;
                        } else {
                            i14 = i4;
                            i15 = i21;
                            i16 = i24;
                            unsafe = unsafe4;
                            i9 = i5;
                            i8 = -1;
                            i6 = i14;
                            i24 = i16;
                            i21 = i15;
                        }
                    } else if (zzA <= 49) {
                        unsafe = unsafe4;
                        i8 = -1;
                        i9 = i5;
                        i20 = zzv(t, bArr, i4, i2, i3, i7, i27, i5, (long) i28, zzA, j, zzik);
                        if (i20 != i4) {
                            zzlj = this;
                            t2 = t;
                            bArr2 = bArr;
                            i17 = i2;
                            zzik2 = zzik;
                            i18 = -1;
                            i23 = i9;
                            i22 = i7;
                            i24 = i24;
                            i21 = i21;
                            unsafe4 = unsafe;
                            i19 = 1048575;
                        } else {
                            i6 = i20;
                            i24 = i24;
                            i21 = i21;
                        }
                    } else {
                        i14 = i4;
                        i16 = i24;
                        i15 = i21;
                        unsafe = unsafe4;
                        i9 = i5;
                        i8 = -1;
                        if (zzA != 50) {
                            i20 = zzt(t, bArr, i14, i2, i3, i7, i27, i28, zzA, j, i9, zzik);
                            if (i20 != i14) {
                                zzlj = this;
                                t2 = t;
                                bArr2 = bArr;
                                i17 = i2;
                                zzik2 = zzik;
                                i18 = -1;
                                i23 = i9;
                                i22 = i7;
                                i24 = i16;
                                i21 = i15;
                                unsafe4 = unsafe;
                                i19 = 1048575;
                            } else {
                                i6 = i20;
                                i24 = i16;
                                i21 = i15;
                            }
                        } else if (i27 == 2) {
                            i20 = zzs(t, bArr, i14, i2, i9, j, zzik);
                            if (i20 != i14) {
                                zzlj = this;
                                t2 = t;
                                bArr2 = bArr;
                                i17 = i2;
                                zzik2 = zzik;
                                i18 = -1;
                                i23 = i9;
                                i22 = i7;
                                i24 = i16;
                                i21 = i15;
                                unsafe4 = unsafe;
                                i19 = 1048575;
                            } else {
                                i6 = i20;
                                i24 = i16;
                                i21 = i15;
                            }
                        } else {
                            i6 = i14;
                            i24 = i16;
                            i21 = i15;
                        }
                    }
                }
            }
            i20 = zzil.zzi(i3, bArr, i6, i2, zzd(t), zzik);
            zzlj = this;
            t2 = t;
            bArr2 = bArr;
            i17 = i2;
            zzik2 = zzik;
            i18 = i8;
            i23 = i9;
            i22 = i7;
            unsafe4 = unsafe;
            i19 = 1048575;
        }
        if (i21 != 1048575) {
            unsafe4.putInt(t, (long) i21, i24);
        }
        if (i20 == i2) {
            return i20;
        }
        throw zzkj.zze();
    }

    private final int zzv(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzik zzik) throws IOException {
        boolean z;
        boolean z2;
        boolean z3;
        int i8;
        zzkg zzkg = (zzkg) zzb.getObject(t, j2);
        if (!zzkg.zzc()) {
            int size = zzkg.size();
            zzkg = zzkg.zzd(size == 0 ? 10 : size + size);
            zzb.putObject(t, j2, zzkg);
        }
        switch (i7) {
            case 18:
            case 35:
                if (i5 == 2) {
                    zzji zzji = (zzji) zzkg;
                    int zzj = zzil.zzj(bArr, i, zzik);
                    int i9 = zzik.zza + zzj;
                    while (zzj < i9) {
                        zzji.zze(Double.longBitsToDouble(zzil.zzn(bArr, zzj)));
                        zzj += 8;
                    }
                    if (zzj == i9) {
                        return zzj;
                    }
                    throw zzkj.zzf();
                } else if (i5 == 1) {
                    zzji zzji2 = (zzji) zzkg;
                    zzji2.zze(Double.longBitsToDouble(zzil.zzn(bArr, i)));
                    int i10 = i + 8;
                    while (i10 < i2) {
                        int zzj2 = zzil.zzj(bArr, i10, zzik);
                        if (i3 != zzik.zza) {
                            return i10;
                        }
                        zzji2.zze(Double.longBitsToDouble(zzil.zzn(bArr, zzj2)));
                        i10 = zzj2 + 8;
                    }
                    return i10;
                }
                break;
            case 19:
            case 36:
                if (i5 == 2) {
                    zzjs zzjs = (zzjs) zzkg;
                    int zzj3 = zzil.zzj(bArr, i, zzik);
                    int i11 = zzik.zza + zzj3;
                    while (zzj3 < i11) {
                        zzjs.zze(Float.intBitsToFloat(zzil.zzb(bArr, zzj3)));
                        zzj3 += 4;
                    }
                    if (zzj3 == i11) {
                        return zzj3;
                    }
                    throw zzkj.zzf();
                } else if (i5 == 5) {
                    zzjs zzjs2 = (zzjs) zzkg;
                    zzjs2.zze(Float.intBitsToFloat(zzil.zzb(bArr, i)));
                    int i12 = i + 4;
                    while (i12 < i2) {
                        int zzj4 = zzil.zzj(bArr, i12, zzik);
                        if (i3 != zzik.zza) {
                            return i12;
                        }
                        zzjs2.zze(Float.intBitsToFloat(zzil.zzb(bArr, zzj4)));
                        i12 = zzj4 + 4;
                    }
                    return i12;
                }
                break;
            case 20:
            case 21:
            case 37:
            case 38:
                if (i5 == 2) {
                    zzkv zzkv = (zzkv) zzkg;
                    int zzj5 = zzil.zzj(bArr, i, zzik);
                    int i13 = zzik.zza + zzj5;
                    while (zzj5 < i13) {
                        zzj5 = zzil.zzm(bArr, zzj5, zzik);
                        zzkv.zzg(zzik.zzb);
                    }
                    if (zzj5 == i13) {
                        return zzj5;
                    }
                    throw zzkj.zzf();
                } else if (i5 == 0) {
                    zzkv zzkv2 = (zzkv) zzkg;
                    int zzm = zzil.zzm(bArr, i, zzik);
                    zzkv2.zzg(zzik.zzb);
                    while (zzm < i2) {
                        int zzj6 = zzil.zzj(bArr, zzm, zzik);
                        if (i3 != zzik.zza) {
                            return zzm;
                        }
                        zzm = zzil.zzm(bArr, zzj6, zzik);
                        zzkv2.zzg(zzik.zzb);
                    }
                    return zzm;
                }
                break;
            case 22:
            case 29:
            case 39:
            case 43:
                if (i5 == 2) {
                    return zzil.zzf(bArr, i, zzkg, zzik);
                }
                if (i5 == 0) {
                    return zzil.zzl(i3, bArr, i, i2, zzkg, zzik);
                }
                break;
            case 23:
            case 32:
            case 40:
            case 46:
                if (i5 == 2) {
                    zzkv zzkv3 = (zzkv) zzkg;
                    int zzj7 = zzil.zzj(bArr, i, zzik);
                    int i14 = zzik.zza + zzj7;
                    while (zzj7 < i14) {
                        zzkv3.zzg(zzil.zzn(bArr, zzj7));
                        zzj7 += 8;
                    }
                    if (zzj7 == i14) {
                        return zzj7;
                    }
                    throw zzkj.zzf();
                } else if (i5 == 1) {
                    zzkv zzkv4 = (zzkv) zzkg;
                    zzkv4.zzg(zzil.zzn(bArr, i));
                    int i15 = i + 8;
                    while (i15 < i2) {
                        int zzj8 = zzil.zzj(bArr, i15, zzik);
                        if (i3 != zzik.zza) {
                            return i15;
                        }
                        zzkv4.zzg(zzil.zzn(bArr, zzj8));
                        i15 = zzj8 + 8;
                    }
                    return i15;
                }
                break;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i5 == 2) {
                    zzka zzka = (zzka) zzkg;
                    int zzj9 = zzil.zzj(bArr, i, zzik);
                    int i16 = zzik.zza + zzj9;
                    while (zzj9 < i16) {
                        zzka.zzh(zzil.zzb(bArr, zzj9));
                        zzj9 += 4;
                    }
                    if (zzj9 == i16) {
                        return zzj9;
                    }
                    throw zzkj.zzf();
                } else if (i5 == 5) {
                    zzka zzka2 = (zzka) zzkg;
                    zzka2.zzh(zzil.zzb(bArr, i));
                    int i17 = i + 4;
                    while (i17 < i2) {
                        int zzj10 = zzil.zzj(bArr, i17, zzik);
                        if (i3 != zzik.zza) {
                            return i17;
                        }
                        zzka2.zzh(zzil.zzb(bArr, zzj10));
                        i17 = zzj10 + 4;
                    }
                    return i17;
                }
                break;
            case 25:
            case 42:
                if (i5 == 2) {
                    zzim zzim = (zzim) zzkg;
                    int zzj11 = zzil.zzj(bArr, i, zzik);
                    int i18 = zzik.zza + zzj11;
                    while (zzj11 < i18) {
                        zzj11 = zzil.zzm(bArr, zzj11, zzik);
                        if (zzik.zzb != 0) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        zzim.zze(z3);
                    }
                    if (zzj11 == i18) {
                        return zzj11;
                    }
                    throw zzkj.zzf();
                } else if (i5 == 0) {
                    zzim zzim2 = (zzim) zzkg;
                    int zzm2 = zzil.zzm(bArr, i, zzik);
                    if (zzik.zzb != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    zzim2.zze(z);
                    while (zzm2 < i2) {
                        int zzj12 = zzil.zzj(bArr, zzm2, zzik);
                        if (i3 != zzik.zza) {
                            return zzm2;
                        }
                        zzm2 = zzil.zzm(bArr, zzj12, zzik);
                        if (zzik.zzb != 0) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        zzim2.zze(z2);
                    }
                    return zzm2;
                }
                break;
            case 26:
                if (i5 == 2) {
                    if ((j & 536870912) == 0) {
                        int zzj13 = zzil.zzj(bArr, i, zzik);
                        int i19 = zzik.zza;
                        if (i19 >= 0) {
                            if (i19 == 0) {
                                zzkg.add("");
                            } else {
                                zzkg.add(new String(bArr, zzj13, i19, zzkh.zza));
                                zzj13 += i19;
                            }
                            while (zzj13 < i2) {
                                int zzj14 = zzil.zzj(bArr, zzj13, zzik);
                                if (i3 != zzik.zza) {
                                    return zzj13;
                                }
                                zzj13 = zzil.zzj(bArr, zzj14, zzik);
                                int i20 = zzik.zza;
                                if (i20 < 0) {
                                    throw zzkj.zzd();
                                } else if (i20 == 0) {
                                    zzkg.add("");
                                } else {
                                    zzkg.add(new String(bArr, zzj13, i20, zzkh.zza));
                                    zzj13 += i20;
                                }
                            }
                            return zzj13;
                        }
                        throw zzkj.zzd();
                    }
                    int zzj15 = zzil.zzj(bArr, i, zzik);
                    int i21 = zzik.zza;
                    if (i21 >= 0) {
                        if (i21 == 0) {
                            zzkg.add("");
                        } else {
                            int i22 = zzj15 + i21;
                            if (zzmx.zzf(bArr, zzj15, i22)) {
                                zzkg.add(new String(bArr, zzj15, i21, zzkh.zza));
                                zzj15 = i22;
                            } else {
                                throw zzkj.zzc();
                            }
                        }
                        while (zzj15 < i2) {
                            int zzj16 = zzil.zzj(bArr, zzj15, zzik);
                            if (i3 != zzik.zza) {
                                return zzj15;
                            }
                            zzj15 = zzil.zzj(bArr, zzj16, zzik);
                            int i23 = zzik.zza;
                            if (i23 < 0) {
                                throw zzkj.zzd();
                            } else if (i23 == 0) {
                                zzkg.add("");
                            } else {
                                int i24 = zzj15 + i23;
                                if (zzmx.zzf(bArr, zzj15, i24)) {
                                    zzkg.add(new String(bArr, zzj15, i23, zzkh.zza));
                                    zzj15 = i24;
                                } else {
                                    throw zzkj.zzc();
                                }
                            }
                        }
                        return zzj15;
                    }
                    throw zzkj.zzd();
                }
                break;
            case 27:
                if (i5 == 2) {
                    return zzil.zze(zzE(i6), i3, bArr, i, i2, zzkg, zzik);
                }
                break;
            case 28:
                if (i5 == 2) {
                    int zzj17 = zzil.zzj(bArr, i, zzik);
                    int i25 = zzik.zza;
                    if (i25 < 0) {
                        throw zzkj.zzd();
                    } else if (i25 <= bArr.length - zzj17) {
                        if (i25 == 0) {
                            zzkg.add(zziy.zzb);
                        } else {
                            zzkg.add(zziy.zzl(bArr, zzj17, i25));
                            zzj17 += i25;
                        }
                        while (zzj17 < i2) {
                            int zzj18 = zzil.zzj(bArr, zzj17, zzik);
                            if (i3 != zzik.zza) {
                                return zzj17;
                            }
                            zzj17 = zzil.zzj(bArr, zzj18, zzik);
                            int i26 = zzik.zza;
                            if (i26 < 0) {
                                throw zzkj.zzd();
                            } else if (i26 > bArr.length - zzj17) {
                                throw zzkj.zzf();
                            } else if (i26 == 0) {
                                zzkg.add(zziy.zzb);
                            } else {
                                zzkg.add(zziy.zzl(bArr, zzj17, i26));
                                zzj17 += i26;
                            }
                        }
                        return zzj17;
                    } else {
                        throw zzkj.zzf();
                    }
                }
                break;
            case 30:
            case 44:
                if (i5 == 2) {
                    i8 = zzil.zzf(bArr, i, zzkg, zzik);
                } else if (i5 == 0) {
                    i8 = zzil.zzl(i3, bArr, i, i2, zzkg, zzik);
                }
                zzjz zzjz = (zzjz) t;
                zzmj zzmj = zzjz.zzc;
                if (zzmj == zzmj.zzc()) {
                    zzmj = null;
                }
                Object zzC = zzlt.zzC(i4, zzkg, zzD(i6), zzmj, this.zzn);
                if (zzC == null) {
                    return i8;
                }
                zzjz.zzc = (zzmj) zzC;
                return i8;
            case 33:
            case 47:
                if (i5 == 2) {
                    zzka zzka3 = (zzka) zzkg;
                    int zzj19 = zzil.zzj(bArr, i, zzik);
                    int i27 = zzik.zza + zzj19;
                    while (zzj19 < i27) {
                        zzj19 = zzil.zzj(bArr, zzj19, zzik);
                        zzka3.zzh(zzjc.zzb(zzik.zza));
                    }
                    if (zzj19 == i27) {
                        return zzj19;
                    }
                    throw zzkj.zzf();
                } else if (i5 == 0) {
                    zzka zzka4 = (zzka) zzkg;
                    int zzj20 = zzil.zzj(bArr, i, zzik);
                    zzka4.zzh(zzjc.zzb(zzik.zza));
                    while (zzj20 < i2) {
                        int zzj21 = zzil.zzj(bArr, zzj20, zzik);
                        if (i3 != zzik.zza) {
                            return zzj20;
                        }
                        zzj20 = zzil.zzj(bArr, zzj21, zzik);
                        zzka4.zzh(zzjc.zzb(zzik.zza));
                    }
                    return zzj20;
                }
                break;
            case 34:
            case 48:
                if (i5 == 2) {
                    zzkv zzkv5 = (zzkv) zzkg;
                    int zzj22 = zzil.zzj(bArr, i, zzik);
                    int i28 = zzik.zza + zzj22;
                    while (zzj22 < i28) {
                        zzj22 = zzil.zzm(bArr, zzj22, zzik);
                        zzkv5.zzg(zzjc.zzc(zzik.zzb));
                    }
                    if (zzj22 == i28) {
                        return zzj22;
                    }
                    throw zzkj.zzf();
                } else if (i5 == 0) {
                    zzkv zzkv6 = (zzkv) zzkg;
                    int zzm3 = zzil.zzm(bArr, i, zzik);
                    zzkv6.zzg(zzjc.zzc(zzik.zzb));
                    while (zzm3 < i2) {
                        int zzj23 = zzil.zzj(bArr, zzm3, zzik);
                        if (i3 != zzik.zza) {
                            return zzm3;
                        }
                        zzm3 = zzil.zzm(bArr, zzj23, zzik);
                        zzkv6.zzg(zzjc.zzc(zzik.zzb));
                    }
                    return zzm3;
                }
                break;
            default:
                if (i5 == 3) {
                    zzlr zzE = zzE(i6);
                    int i29 = (i3 & -8) | 4;
                    int zzc = zzil.zzc(zzE, bArr, i, i2, i29, zzik);
                    zzkg.add(zzik.zzc);
                    while (zzc < i2) {
                        int zzj24 = zzil.zzj(bArr, zzc, zzik);
                        if (i3 != zzik.zza) {
                            return zzc;
                        }
                        zzc = zzil.zzc(zzE, bArr, zzj24, i2, i29, zzik);
                        zzkg.add(zzik.zzc);
                    }
                    return zzc;
                }
                break;
        }
        return i;
    }

    private final int zzw(int i) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzz(i, 0);
    }

    private final int zzx(int i, int i2) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzz(i, i2);
    }

    private final int zzy(int i) {
        return this.zzc[i + 2];
    }

    private final int zzz(int i, int i2) {
        int length = (this.zzc.length / 3) - 1;
        while (i2 <= length) {
            int i3 = (length + i2) >>> 1;
            int i4 = i3 * 3;
            int i5 = this.zzc[i4];
            if (i == i5) {
                return i4;
            }
            if (i < i5) {
                length = i3 - 1;
            } else {
                i2 = i3 + 1;
            }
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final int zza(T t) {
        return this.zzi ? zzq(t) : zzp(t);
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final int zzb(T t) {
        int length = this.zzc.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2 += 3) {
            int zzB = zzB(i2);
            int i3 = this.zzc[i2];
            long j = (long) (1048575 & zzB);
            int i4 = 37;
            switch (zzA(zzB)) {
                case 0:
                    i = (i * 53) + zzkh.zzc(Double.doubleToLongBits(zzms.zza(t, j)));
                    break;
                case 1:
                    i = (i * 53) + Float.floatToIntBits(zzms.zzb(t, j));
                    break;
                case 2:
                    i = (i * 53) + zzkh.zzc(zzms.zzd(t, j));
                    break;
                case 3:
                    i = (i * 53) + zzkh.zzc(zzms.zzd(t, j));
                    break;
                case 4:
                    i = (i * 53) + zzms.zzc(t, j);
                    break;
                case 5:
                    i = (i * 53) + zzkh.zzc(zzms.zzd(t, j));
                    break;
                case 6:
                    i = (i * 53) + zzms.zzc(t, j);
                    break;
                case 7:
                    i = (i * 53) + zzkh.zza(zzms.zzw(t, j));
                    break;
                case 8:
                    i = (i * 53) + ((String) zzms.zzf(t, j)).hashCode();
                    break;
                case 9:
                    Object zzf = zzms.zzf(t, j);
                    if (zzf != null) {
                        i4 = zzf.hashCode();
                    }
                    i = (i * 53) + i4;
                    break;
                case 10:
                    i = (i * 53) + zzms.zzf(t, j).hashCode();
                    break;
                case 11:
                    i = (i * 53) + zzms.zzc(t, j);
                    break;
                case 12:
                    i = (i * 53) + zzms.zzc(t, j);
                    break;
                case 13:
                    i = (i * 53) + zzms.zzc(t, j);
                    break;
                case 14:
                    i = (i * 53) + zzkh.zzc(zzms.zzd(t, j));
                    break;
                case 15:
                    i = (i * 53) + zzms.zzc(t, j);
                    break;
                case 16:
                    i = (i * 53) + zzkh.zzc(zzms.zzd(t, j));
                    break;
                case 17:
                    Object zzf2 = zzms.zzf(t, j);
                    if (zzf2 != null) {
                        i4 = zzf2.hashCode();
                    }
                    i = (i * 53) + i4;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    i = (i * 53) + zzms.zzf(t, j).hashCode();
                    break;
                case 50:
                    i = (i * 53) + zzms.zzf(t, j).hashCode();
                    break;
                case 51:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzkh.zzc(Double.doubleToLongBits(zzn(t, j)));
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + Float.floatToIntBits(zzo(t, j));
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzkh.zzc(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzkh.zzc(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzr(t, j);
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzkh.zzc(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzr(t, j);
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzkh.zza(zzQ(t, j));
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + ((String) zzms.zzf(t, j)).hashCode();
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzms.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzms.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzr(t, j);
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzr(t, j);
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzr(t, j);
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzkh.zzc(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzr(t, j);
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzkh.zzc(zzC(t, j));
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (zzP(t, i3, i2)) {
                        i = (i * 53) + zzms.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i * 53) + this.zzn.zzc(t).hashCode();
        if (!this.zzh) {
            return hashCode;
        }
        this.zzo.zza(t);
        throw null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:149:0x0533, code lost:
        if (r6 == 1048575) goto L_0x053b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0535, code lost:
        r28.putInt(r12, (long) r6, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x053b, code lost:
        r0 = r9.zzk;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x053f, code lost:
        if (r0 >= r9.zzl) goto L_0x0568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0541, code lost:
        r4 = r9.zzj[r0];
        r5 = r9.zzc[r4];
        r5 = com.google.android.gms.internal.measurement.zzms.zzf(r12, (long) (r9.zzB(r4) & 1048575));
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0553, code lost:
        if (r5 != null) goto L_0x0556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x055a, code lost:
        if (r9.zzD(r4) != null) goto L_0x055f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x055c, code lost:
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x055f, code lost:
        r5 = (com.google.android.gms.internal.measurement.zzla) r5;
        r0 = (com.google.android.gms.internal.measurement.zzkz) r9.zzF(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0567, code lost:
        throw null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x0568, code lost:
        if (r7 != 0) goto L_0x0574;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x056c, code lost:
        if (r2 != r34) goto L_0x056f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0573, code lost:
        throw com.google.android.gms.internal.measurement.zzkj.zze();
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0576, code lost:
        if (r2 > r34) goto L_0x057b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0578, code lost:
        if (r1 != r7) goto L_0x057b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x057a, code lost:
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x057f, code lost:
        throw com.google.android.gms.internal.measurement.zzkj.zze();
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final int zzc(T t, byte[] bArr, int i, int i2, int i3, zzik zzik) throws IOException {
        Unsafe unsafe;
        int i4;
        T t2;
        zzlj<T> zzlj;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        T t3;
        int i13;
        zzik zzik2;
        int i14;
        int i15;
        int i16;
        char c;
        int i17;
        int i18;
        int i19;
        boolean z;
        int i20;
        int i21;
        int i22;
        zzlj<T> zzlj2 = this;
        T t4 = t;
        byte[] bArr2 = bArr;
        int i23 = i2;
        int i24 = i3;
        zzik zzik3 = zzik;
        Unsafe unsafe2 = zzb;
        int i25 = i;
        int i26 = 0;
        int i27 = 0;
        int i28 = 0;
        int i29 = -1;
        int i30 = 1048575;
        while (true) {
            if (i25 < i23) {
                int i31 = i25 + 1;
                byte b = bArr2[i25];
                if (b < 0) {
                    i31 = zzil.zzk(b, bArr2, i31, zzik3);
                    i7 = zzik3.zza;
                } else {
                    i7 = b;
                }
                int i32 = i7 >>> 3;
                int i33 = i7 & 7;
                if (i32 > i29) {
                    i8 = zzlj2.zzx(i32, i27 / 3);
                } else {
                    i8 = zzlj2.zzw(i32);
                }
                if (i8 == -1) {
                    i9 = i32;
                    i5 = i31;
                    i10 = i7;
                    i11 = i28;
                    unsafe = unsafe2;
                    i12 = 0;
                } else {
                    int i34 = zzlj2.zzc[i8 + 1];
                    int zzA = zzA(i34);
                    long j = (long) (i34 & 1048575);
                    if (zzA <= 17) {
                        int i35 = zzlj2.zzc[i8 + 2];
                        int i36 = 1 << (i35 >>> 20);
                        int i37 = i35 & 1048575;
                        if (i37 != i30) {
                            if (i30 != 1048575) {
                                unsafe2.putInt(t4, (long) i30, i28);
                            }
                            i14 = i37;
                            i15 = unsafe2.getInt(t4, (long) i37);
                        } else {
                            i14 = i30;
                            i15 = i28;
                        }
                        switch (zzA) {
                            case 0:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i5 = i31;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 1) {
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    zzms.zzo(t4, j, Double.longBitsToDouble(zzil.zzn(bArr2, i5)));
                                    i25 = i5 + 8;
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 1:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i5 = i31;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 5) {
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    zzms.zzp(t4, j, Float.intBitsToFloat(zzil.zzb(bArr2, i5)));
                                    i25 = i5 + 4;
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 2:
                            case 3:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i5 = i31;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 0) {
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    int zzm = zzil.zzm(bArr2, i5, zzik3);
                                    unsafe2.putLong(t, j, zzik3.zzb);
                                    i28 = i15 | i36;
                                    i25 = zzm;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 4:
                            case 11:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i5 = i31;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 0) {
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    i25 = zzil.zzj(bArr2, i5, zzik3);
                                    unsafe2.putInt(t4, j, zzik3.zza);
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 5:
                            case 14:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 1) {
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    unsafe2.putLong(t, j, zzil.zzn(bArr2, i31));
                                    i25 = i31 + 8;
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 6:
                            case 13:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 5) {
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    unsafe2.putInt(t4, j, zzil.zzb(bArr2, i31));
                                    i25 = i31 + 4;
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 7:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 0) {
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    i25 = zzil.zzm(bArr2, i31, zzik3);
                                    if (zzik3.zzb != 0) {
                                        z = true;
                                    } else {
                                        z = false;
                                    }
                                    zzms.zzm(t4, j, z);
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 8:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 2) {
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    if ((i34 & 536870912) == 0) {
                                        i25 = zzil.zzg(bArr2, i31, zzik3);
                                    } else {
                                        i25 = zzil.zzh(bArr2, i31, zzik3);
                                    }
                                    unsafe2.putObject(t4, j, zzik3.zzc);
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 9:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 2) {
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    i25 = zzil.zzd(zzlj2.zzE(i16), bArr2, i31, i23, zzik3);
                                    if ((i15 & i36) == 0) {
                                        unsafe2.putObject(t4, j, zzik3.zzc);
                                    } else {
                                        unsafe2.putObject(t4, j, zzkh.zzg(unsafe2.getObject(t4, j), zzik3.zzc));
                                    }
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 10:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 2) {
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    i25 = zzil.zza(bArr2, i31, zzik3);
                                    unsafe2.putObject(t4, j, zzik3.zzc);
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 12:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 0) {
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    i25 = zzil.zzj(bArr2, i31, zzik3);
                                    int i38 = zzik3.zza;
                                    zzkd zzD = zzlj2.zzD(i16);
                                    if (zzD != null && !zzD.zza(i38)) {
                                        zzd(t).zzh(i18, Long.valueOf((long) i38));
                                        i28 = i15;
                                        i27 = i16;
                                        i26 = i18;
                                        i30 = i17;
                                        i29 = i19;
                                        i24 = i3;
                                        break;
                                    } else {
                                        unsafe2.putInt(t4, j, i38);
                                        i28 = i15 | i36;
                                        i27 = i16;
                                        i26 = i18;
                                        i30 = i17;
                                        i29 = i19;
                                        i24 = i3;
                                        break;
                                    }
                                }
                                break;
                            case 15:
                                bArr2 = bArr;
                                i16 = i8;
                                c = 65535;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 0) {
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    i25 = zzil.zzj(bArr2, i31, zzik3);
                                    unsafe2.putInt(t4, j, zzjc.zzb(zzik3.zza));
                                    i28 = i15 | i36;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                            case 16:
                                if (i33 != 0) {
                                    i16 = i8;
                                    c = 65535;
                                    i17 = i14;
                                    i18 = i7;
                                    i19 = i32;
                                    i5 = i31;
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    bArr2 = bArr;
                                    int zzm2 = zzil.zzm(bArr2, i31, zzik3);
                                    unsafe2.putLong(t, j, zzjc.zzc(zzik3.zzb));
                                    i28 = i15 | i36;
                                    i25 = zzm2;
                                    i27 = i8;
                                    i26 = i7;
                                    i30 = i14;
                                    i29 = i32;
                                    i24 = i3;
                                    break;
                                }
                            default:
                                i16 = i8;
                                c = 65535;
                                i5 = i31;
                                i17 = i14;
                                i18 = i7;
                                i19 = i32;
                                if (i33 != 3) {
                                    i12 = i16;
                                    unsafe = unsafe2;
                                    i10 = i18;
                                    i9 = i19;
                                    i11 = i15;
                                    i30 = i17;
                                    break;
                                } else {
                                    i25 = zzil.zzc(zzlj2.zzE(i16), bArr, i5, i2, (i19 << 3) | 4, zzik);
                                    if ((i15 & i36) == 0) {
                                        unsafe2.putObject(t4, j, zzik3.zzc);
                                    } else {
                                        unsafe2.putObject(t4, j, zzkh.zzg(unsafe2.getObject(t4, j), zzik3.zzc));
                                    }
                                    i28 = i15 | i36;
                                    bArr2 = bArr;
                                    i23 = i2;
                                    i27 = i16;
                                    i26 = i18;
                                    i30 = i17;
                                    i29 = i19;
                                    i24 = i3;
                                    break;
                                }
                        }
                    } else if (zzA != 27) {
                        i11 = i28;
                        i21 = i30;
                        if (zzA <= 49) {
                            i9 = i32;
                            i12 = i8;
                            unsafe = unsafe2;
                            i10 = i7;
                            i25 = zzv(t, bArr, i31, i2, i7, i32, i33, i8, (long) i34, zzA, j, zzik);
                            if (i25 != i31) {
                                zzlj2 = this;
                                t4 = t;
                                bArr2 = bArr;
                                i29 = i9;
                                i23 = i2;
                                i24 = i3;
                                zzik3 = zzik;
                                i27 = i12;
                                i28 = i11;
                                i30 = i21;
                                i26 = i10;
                                unsafe2 = unsafe;
                            } else {
                                i5 = i25;
                                i30 = i21;
                            }
                        } else {
                            i22 = i31;
                            i9 = i32;
                            i12 = i8;
                            unsafe = unsafe2;
                            i10 = i7;
                            if (zzA != 50) {
                                i25 = zzt(t, bArr, i22, i2, i10, i9, i33, i34, zzA, j, i12, zzik);
                                if (i25 != i22) {
                                    zzlj2 = this;
                                    t4 = t;
                                    bArr2 = bArr;
                                    i29 = i9;
                                    i23 = i2;
                                    i24 = i3;
                                    zzik3 = zzik;
                                    i27 = i12;
                                    i28 = i11;
                                    i30 = i21;
                                    i26 = i10;
                                    unsafe2 = unsafe;
                                } else {
                                    i5 = i25;
                                    i30 = i21;
                                }
                            } else if (i33 == 2) {
                                i25 = zzs(t, bArr, i22, i2, i12, j, zzik);
                                if (i25 != i22) {
                                    zzlj2 = this;
                                    t4 = t;
                                    bArr2 = bArr;
                                    i29 = i9;
                                    i23 = i2;
                                    i24 = i3;
                                    zzik3 = zzik;
                                    i27 = i12;
                                    i28 = i11;
                                    i30 = i21;
                                    i26 = i10;
                                    unsafe2 = unsafe;
                                } else {
                                    i5 = i25;
                                    i30 = i21;
                                }
                            } else {
                                i5 = i22;
                                i30 = i21;
                            }
                        }
                    } else if (i33 == 2) {
                        zzkg zzkg = (zzkg) unsafe2.getObject(t4, j);
                        if (!zzkg.zzc()) {
                            int size = zzkg.size();
                            if (size == 0) {
                                i20 = 10;
                            } else {
                                i20 = size + size;
                            }
                            zzkg = zzkg.zzd(i20);
                            unsafe2.putObject(t4, j, zzkg);
                        }
                        i26 = i7;
                        i25 = zzil.zze(zzlj2.zzE(i8), i26, bArr, i31, i2, zzkg, zzik);
                        i23 = i2;
                        i27 = i8;
                        i29 = i32;
                        i28 = i28;
                        i30 = i30;
                        bArr2 = bArr;
                        i24 = i3;
                    } else {
                        i11 = i28;
                        i21 = i30;
                        i22 = i31;
                        i12 = i8;
                        unsafe = unsafe2;
                        i10 = i7;
                        i9 = i32;
                        i5 = i22;
                        i30 = i21;
                    }
                }
                i4 = i3;
                if (i10 != i4 || i4 == 0) {
                    if (this.zzh) {
                        zzik2 = zzik;
                        if (zzik2.zzd != zzjl.zza()) {
                            i13 = i9;
                            if (zzik2.zzd.zzc(this.zzg, i13) == null) {
                                i25 = zzil.zzi(i10, bArr, i5, i2, zzd(t), zzik);
                                t3 = t;
                                i23 = i2;
                                i26 = i10;
                                zzlj2 = this;
                                zzik3 = zzik2;
                                i29 = i13;
                                t4 = t3;
                                i27 = i12;
                                i28 = i11;
                                unsafe2 = unsafe;
                                bArr2 = bArr;
                                i24 = i4;
                            } else {
                                zzjw zzjw = (zzjw) t;
                                throw null;
                            }
                        } else {
                            t3 = t;
                            i13 = i9;
                        }
                    } else {
                        t3 = t;
                        i13 = i9;
                        zzik2 = zzik;
                    }
                    i25 = zzil.zzi(i10, bArr, i5, i2, zzd(t), zzik);
                    i23 = i2;
                    i26 = i10;
                    zzlj2 = this;
                    zzik3 = zzik2;
                    i29 = i13;
                    t4 = t3;
                    i27 = i12;
                    i28 = i11;
                    unsafe2 = unsafe;
                    bArr2 = bArr;
                    i24 = i4;
                } else {
                    zzlj = this;
                    t2 = t;
                    i26 = i10;
                    i6 = i11;
                }
            } else {
                unsafe = unsafe2;
                i4 = i24;
                t2 = t4;
                zzlj = zzlj2;
                i5 = i25;
                i6 = i28;
            }
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final T zze() {
        return (T) ((zzjz) this.zzg).zzl(4, null, null);
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final void zzf(T t) {
        int i;
        int i2 = this.zzk;
        while (true) {
            i = this.zzl;
            if (i2 >= i) {
                break;
            }
            long zzB = (long) (zzB(this.zzj[i2]) & 1048575);
            Object zzf = zzms.zzf(t, zzB);
            if (zzf != null) {
                ((zzla) zzf).zzc();
                zzms.zzs(t, zzB, zzf);
            }
            i2++;
        }
        int length = this.zzj.length;
        while (i < length) {
            this.zzm.zza(t, (long) this.zzj[i]);
            i++;
        }
        this.zzn.zzg(t);
        if (this.zzh) {
            this.zzo.zzb(t);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final void zzg(T t, T t2) {
        if (t2 != null) {
            for (int i = 0; i < this.zzc.length; i += 3) {
                int zzB = zzB(i);
                long j = (long) (1048575 & zzB);
                int i2 = this.zzc[i];
                switch (zzA(zzB)) {
                    case 0:
                        if (zzM(t2, i)) {
                            zzms.zzo(t, j, zzms.zza(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        if (zzM(t2, i)) {
                            zzms.zzp(t, j, zzms.zzb(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (zzM(t2, i)) {
                            zzms.zzr(t, j, zzms.zzd(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (zzM(t2, i)) {
                            zzms.zzr(t, j, zzms.zzd(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (zzM(t2, i)) {
                            zzms.zzq(t, j, zzms.zzc(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        if (zzM(t2, i)) {
                            zzms.zzr(t, j, zzms.zzd(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 6:
                        if (zzM(t2, i)) {
                            zzms.zzq(t, j, zzms.zzc(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (zzM(t2, i)) {
                            zzms.zzm(t, j, zzms.zzw(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 8:
                        if (zzM(t2, i)) {
                            zzms.zzs(t, j, zzms.zzf(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 9:
                        zzH(t, t2, i);
                        break;
                    case 10:
                        if (zzM(t2, i)) {
                            zzms.zzs(t, j, zzms.zzf(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (zzM(t2, i)) {
                            zzms.zzq(t, j, zzms.zzc(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 12:
                        if (zzM(t2, i)) {
                            zzms.zzq(t, j, zzms.zzc(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 13:
                        if (zzM(t2, i)) {
                            zzms.zzq(t, j, zzms.zzc(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 14:
                        if (zzM(t2, i)) {
                            zzms.zzr(t, j, zzms.zzd(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 15:
                        if (zzM(t2, i)) {
                            zzms.zzq(t, j, zzms.zzc(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 16:
                        if (zzM(t2, i)) {
                            zzms.zzr(t, j, zzms.zzd(t2, j));
                            zzJ(t, i);
                            break;
                        } else {
                            break;
                        }
                    case 17:
                        zzH(t, t2, i);
                        break;
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                        this.zzm.zzb(t, t2, j);
                        break;
                    case 50:
                        zzlt.zzI(this.zzq, t, t2, j);
                        break;
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                        if (zzP(t2, i2, i)) {
                            zzms.zzs(t, j, zzms.zzf(t2, j));
                            zzK(t, i2, i);
                            break;
                        } else {
                            break;
                        }
                    case 60:
                        zzI(t, t2, i);
                        break;
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                        if (zzP(t2, i2, i)) {
                            zzms.zzs(t, j, zzms.zzf(t2, j));
                            zzK(t, i2, i);
                            break;
                        } else {
                            break;
                        }
                    case 68:
                        zzI(t, t2, i);
                        break;
                }
            }
            zzlt.zzF(this.zzn, t, t2);
            if (this.zzh) {
                zzlt.zzE(this.zzo, t, t2);
                return;
            }
            return;
        }
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final void zzh(T t, byte[] bArr, int i, int i2, zzik zzik) throws IOException {
        if (this.zzi) {
            zzu(t, bArr, i, i2, zzik);
        } else {
            zzc(t, bArr, i, i2, 0, zzik);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final boolean zzi(T t, T t2) {
        boolean z;
        int length = this.zzc.length;
        for (int i = 0; i < length; i += 3) {
            int zzB = zzB(i);
            long j = (long) (zzB & 1048575);
            switch (zzA(zzB)) {
                case 0:
                    if (zzL(t, t2, i) && Double.doubleToLongBits(zzms.zza(t, j)) == Double.doubleToLongBits(zzms.zza(t2, j))) {
                        continue;
                    }
                    return false;
                case 1:
                    if (zzL(t, t2, i) && Float.floatToIntBits(zzms.zzb(t, j)) == Float.floatToIntBits(zzms.zzb(t2, j))) {
                        continue;
                    }
                    return false;
                case 2:
                    if (zzL(t, t2, i) && zzms.zzd(t, j) == zzms.zzd(t2, j)) {
                        continue;
                    }
                    return false;
                case 3:
                    if (zzL(t, t2, i) && zzms.zzd(t, j) == zzms.zzd(t2, j)) {
                        continue;
                    }
                    return false;
                case 4:
                    if (zzL(t, t2, i) && zzms.zzc(t, j) == zzms.zzc(t2, j)) {
                        continue;
                    }
                    return false;
                case 5:
                    if (zzL(t, t2, i) && zzms.zzd(t, j) == zzms.zzd(t2, j)) {
                        continue;
                    }
                    return false;
                case 6:
                    if (zzL(t, t2, i) && zzms.zzc(t, j) == zzms.zzc(t2, j)) {
                        continue;
                    }
                    return false;
                case 7:
                    if (zzL(t, t2, i) && zzms.zzw(t, j) == zzms.zzw(t2, j)) {
                        continue;
                    }
                    return false;
                case 8:
                    if (zzL(t, t2, i) && zzlt.zzH(zzms.zzf(t, j), zzms.zzf(t2, j))) {
                        continue;
                    }
                    return false;
                case 9:
                    if (zzL(t, t2, i) && zzlt.zzH(zzms.zzf(t, j), zzms.zzf(t2, j))) {
                        continue;
                    }
                    return false;
                case 10:
                    if (zzL(t, t2, i) && zzlt.zzH(zzms.zzf(t, j), zzms.zzf(t2, j))) {
                        continue;
                    }
                    return false;
                case 11:
                    if (zzL(t, t2, i) && zzms.zzc(t, j) == zzms.zzc(t2, j)) {
                        continue;
                    }
                    return false;
                case 12:
                    if (zzL(t, t2, i) && zzms.zzc(t, j) == zzms.zzc(t2, j)) {
                        continue;
                    }
                    return false;
                case 13:
                    if (zzL(t, t2, i) && zzms.zzc(t, j) == zzms.zzc(t2, j)) {
                        continue;
                    }
                    return false;
                case 14:
                    if (zzL(t, t2, i) && zzms.zzd(t, j) == zzms.zzd(t2, j)) {
                        continue;
                    }
                    return false;
                case 15:
                    if (zzL(t, t2, i) && zzms.zzc(t, j) == zzms.zzc(t2, j)) {
                        continue;
                    }
                    return false;
                case 16:
                    if (zzL(t, t2, i) && zzms.zzd(t, j) == zzms.zzd(t2, j)) {
                        continue;
                    }
                    return false;
                case 17:
                    if (zzL(t, t2, i) && zzlt.zzH(zzms.zzf(t, j), zzms.zzf(t2, j))) {
                        continue;
                    }
                    return false;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    z = zzlt.zzH(zzms.zzf(t, j), zzms.zzf(t2, j));
                    break;
                case 50:
                    z = zzlt.zzH(zzms.zzf(t, j), zzms.zzf(t2, j));
                    break;
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                    long zzy = (long) (zzy(i) & 1048575);
                    if (zzms.zzc(t, zzy) == zzms.zzc(t2, zzy) && zzlt.zzH(zzms.zzf(t, j), zzms.zzf(t2, j))) {
                        continue;
                    }
                    return false;
                default:
            }
            if (!z) {
                return false;
            }
        }
        if (!this.zzn.zzc(t).equals(this.zzn.zzc(t2))) {
            return false;
        }
        if (!this.zzh) {
            return true;
        }
        this.zzo.zza(t);
        this.zzo.zza(t2);
        throw null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.measurement.zzlr
    public final boolean zzj(T t) {
        int i;
        int i2;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.zzk) {
            int i6 = this.zzj[i5];
            int i7 = this.zzc[i6];
            int zzB = zzB(i6);
            int i8 = this.zzc[i6 + 2];
            int i9 = i8 & 1048575;
            int i10 = 1 << (i8 >>> 20);
            if (i9 == i3) {
                i2 = i3;
                i = i4;
            } else if (i9 != 1048575) {
                i = zzb.getInt(t, (long) i9);
                i2 = i9;
            } else {
                i = i4;
                i2 = i9;
            }
            if (!((268435456 & zzB) == 0 || zzN(t, i6, i2, i, i10))) {
                return false;
            }
            int zzA = zzA(zzB);
            if (zzA != 9 && zzA != 17) {
                if (zzA != 27) {
                    if (zzA == 60 || zzA == 68) {
                        if (zzP(t, i7, i6) && !zzO(t, zzB, zzE(i6))) {
                            return false;
                        }
                    } else if (zzA != 49) {
                        if (zzA == 50 && !((zzla) zzms.zzf(t, (long) (zzB & 1048575))).isEmpty()) {
                            zzkz zzkz = (zzkz) zzF(i6);
                            throw null;
                        }
                    }
                }
                List list = (List) zzms.zzf(t, (long) (zzB & 1048575));
                if (!list.isEmpty()) {
                    zzlr zzE = zzE(i6);
                    for (int i11 = 0; i11 < list.size(); i11++) {
                        if (!zzE.zzj(list.get(i11))) {
                            return false;
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            } else if (zzN(t, i6, i2, i, i10) && !zzO(t, zzB, zzE(i6))) {
                return false;
            }
            i5++;
            i3 = i2;
            i4 = i;
        }
        if (!this.zzh) {
            return true;
        }
        this.zzo.zza(t);
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final void zzm(T t, zzjh zzjh) throws IOException {
        if (!this.zzi) {
            zzR(t, zzjh);
        } else if (!this.zzh) {
            int length = this.zzc.length;
            for (int i = 0; i < length; i += 3) {
                int zzB = zzB(i);
                int i2 = this.zzc[i];
                switch (zzA(zzB)) {
                    case 0:
                        if (zzM(t, i)) {
                            zzjh.zzf(i2, zzms.zza(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        if (zzM(t, i)) {
                            zzjh.zzo(i2, zzms.zzb(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (zzM(t, i)) {
                            zzjh.zzt(i2, zzms.zzd(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (zzM(t, i)) {
                            zzjh.zzJ(i2, zzms.zzd(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (zzM(t, i)) {
                            zzjh.zzr(i2, zzms.zzc(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        if (zzM(t, i)) {
                            zzjh.zzm(i2, zzms.zzd(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 6:
                        if (zzM(t, i)) {
                            zzjh.zzk(i2, zzms.zzc(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (zzM(t, i)) {
                            zzjh.zzb(i2, zzms.zzw(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 8:
                        if (zzM(t, i)) {
                            zzT(i2, zzms.zzf(t, (long) (zzB & 1048575)), zzjh);
                            break;
                        } else {
                            break;
                        }
                    case 9:
                        if (zzM(t, i)) {
                            zzjh.zzv(i2, zzms.zzf(t, (long) (zzB & 1048575)), zzE(i));
                            break;
                        } else {
                            break;
                        }
                    case 10:
                        if (zzM(t, i)) {
                            zzjh.zzd(i2, (zziy) zzms.zzf(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (zzM(t, i)) {
                            zzjh.zzH(i2, zzms.zzc(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 12:
                        if (zzM(t, i)) {
                            zzjh.zzi(i2, zzms.zzc(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 13:
                        if (zzM(t, i)) {
                            zzjh.zzw(i2, zzms.zzc(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 14:
                        if (zzM(t, i)) {
                            zzjh.zzy(i2, zzms.zzd(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 15:
                        if (zzM(t, i)) {
                            zzjh.zzA(i2, zzms.zzc(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 16:
                        if (zzM(t, i)) {
                            zzjh.zzC(i2, zzms.zzd(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 17:
                        if (zzM(t, i)) {
                            zzjh.zzq(i2, zzms.zzf(t, (long) (zzB & 1048575)), zzE(i));
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        zzlt.zzL(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 19:
                        zzlt.zzP(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 20:
                        zzlt.zzS(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 21:
                        zzlt.zzaa(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 22:
                        zzlt.zzR(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 23:
                        zzlt.zzO(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 24:
                        zzlt.zzN(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 25:
                        zzlt.zzJ(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 26:
                        zzlt.zzY(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh);
                        break;
                    case 27:
                        zzlt.zzT(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, zzE(i));
                        break;
                    case 28:
                        zzlt.zzK(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh);
                        break;
                    case 29:
                        zzlt.zzZ(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 30:
                        zzlt.zzM(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 31:
                        zzlt.zzU(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 32:
                        zzlt.zzV(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 33:
                        zzlt.zzW(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 34:
                        zzlt.zzX(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, false);
                        break;
                    case 35:
                        zzlt.zzL(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 36:
                        zzlt.zzP(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 37:
                        zzlt.zzS(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 38:
                        zzlt.zzaa(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 39:
                        zzlt.zzR(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 40:
                        zzlt.zzO(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 41:
                        zzlt.zzN(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 42:
                        zzlt.zzJ(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 43:
                        zzlt.zzZ(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 44:
                        zzlt.zzM(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 45:
                        zzlt.zzU(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 46:
                        zzlt.zzV(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 47:
                        zzlt.zzW(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 48:
                        zzlt.zzX(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, true);
                        break;
                    case 49:
                        zzlt.zzQ(this.zzc[i], (List) zzms.zzf(t, (long) (zzB & 1048575)), zzjh, zzE(i));
                        break;
                    case 50:
                        zzS(zzjh, i2, zzms.zzf(t, (long) (zzB & 1048575)), i);
                        break;
                    case 51:
                        if (zzP(t, i2, i)) {
                            zzjh.zzf(i2, zzn(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 52:
                        if (zzP(t, i2, i)) {
                            zzjh.zzo(i2, zzo(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 53:
                        if (zzP(t, i2, i)) {
                            zzjh.zzt(i2, zzC(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 54:
                        if (zzP(t, i2, i)) {
                            zzjh.zzJ(i2, zzC(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 55:
                        if (zzP(t, i2, i)) {
                            zzjh.zzr(i2, zzr(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 56:
                        if (zzP(t, i2, i)) {
                            zzjh.zzm(i2, zzC(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 57:
                        if (zzP(t, i2, i)) {
                            zzjh.zzk(i2, zzr(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 58:
                        if (zzP(t, i2, i)) {
                            zzjh.zzb(i2, zzQ(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 59:
                        if (zzP(t, i2, i)) {
                            zzT(i2, zzms.zzf(t, (long) (zzB & 1048575)), zzjh);
                            break;
                        } else {
                            break;
                        }
                    case 60:
                        if (zzP(t, i2, i)) {
                            zzjh.zzv(i2, zzms.zzf(t, (long) (zzB & 1048575)), zzE(i));
                            break;
                        } else {
                            break;
                        }
                    case 61:
                        if (zzP(t, i2, i)) {
                            zzjh.zzd(i2, (zziy) zzms.zzf(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 62:
                        if (zzP(t, i2, i)) {
                            zzjh.zzH(i2, zzr(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 63:
                        if (zzP(t, i2, i)) {
                            zzjh.zzi(i2, zzr(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 64:
                        if (zzP(t, i2, i)) {
                            zzjh.zzw(i2, zzr(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 65:
                        if (zzP(t, i2, i)) {
                            zzjh.zzy(i2, zzC(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 66:
                        if (zzP(t, i2, i)) {
                            zzjh.zzA(i2, zzr(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 67:
                        if (zzP(t, i2, i)) {
                            zzjh.zzC(i2, zzC(t, (long) (zzB & 1048575)));
                            break;
                        } else {
                            break;
                        }
                    case 68:
                        if (zzP(t, i2, i)) {
                            zzjh.zzq(i2, zzms.zzf(t, (long) (zzB & 1048575)), zzE(i));
                            break;
                        } else {
                            break;
                        }
                }
            }
            zzmi<?, ?> zzmi = this.zzn;
            zzmi.zzi(zzmi.zzc(t), zzjh);
        } else {
            this.zzo.zza(t);
            throw null;
        }
    }

    private final <K, V> void zzS(zzjh zzjh, int i, Object obj, int i2) throws IOException {
        if (obj != null) {
            zzkz zzkz = (zzkz) zzF(i2);
            throw null;
        }
    }
}
