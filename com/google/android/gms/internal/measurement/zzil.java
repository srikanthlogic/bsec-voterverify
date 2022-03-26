package com.google.android.gms.internal.measurement;

import com.google.common.base.Ascii;
import java.io.IOException;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzil {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(byte[] bArr, int i, zzik zzik) throws zzkj {
        int zzj = zzj(bArr, i, zzik);
        int i2 = zzik.zza;
        if (i2 < 0) {
            throw zzkj.zzd();
        } else if (i2 > bArr.length - zzj) {
            throw zzkj.zzf();
        } else if (i2 == 0) {
            zzik.zzc = zziy.zzb;
            return zzj;
        } else {
            zzik.zzc = zziy.zzl(bArr, zzj, i2);
            return zzj + i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(byte[] bArr, int i) {
        return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(zzlr zzlr, byte[] bArr, int i, int i2, int i3, zzik zzik) throws IOException {
        zzlj zzlj = (zzlj) zzlr;
        Object zze = zzlj.zze();
        int zzc = zzlj.zzc(zze, bArr, i, i2, i3, zzik);
        zzlj.zzf(zze);
        zzik.zzc = zze;
        return zzc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(zzlr zzlr, byte[] bArr, int i, int i2, zzik zzik) throws IOException {
        byte b;
        int i3;
        int i4 = i + 1;
        byte b2 = bArr[i];
        if (b2 < 0) {
            i3 = zzk(b2, bArr, i4, zzik);
            b = zzik.zza;
        } else {
            i3 = i4;
            b = b2;
        }
        if (b < 0 || b > i2 - i3) {
            throw zzkj.zzf();
        }
        Object zze = zzlr.zze();
        int i5 = (b == 1 ? 1 : 0) + i3;
        zzlr.zzh(zze, bArr, i3, i5, zzik);
        zzlr.zzf(zze);
        zzik.zzc = zze;
        return i5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(zzlr<?> zzlr, int i, byte[] bArr, int i2, int i3, zzkg<?> zzkg, zzik zzik) throws IOException {
        int zzd = zzd(zzlr, bArr, i2, i3, zzik);
        zzkg.add(zzik.zzc);
        while (zzd < i3) {
            int zzj = zzj(bArr, zzd, zzik);
            if (i != zzik.zza) {
                break;
            }
            zzd = zzd(zzlr, bArr, zzj, i3, zzik);
            zzkg.add(zzik.zzc);
        }
        return zzd;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzf(byte[] bArr, int i, zzkg<?> zzkg, zzik zzik) throws IOException {
        zzka zzka = (zzka) zzkg;
        int zzj = zzj(bArr, i, zzik);
        int i2 = zzik.zza + zzj;
        while (zzj < i2) {
            zzj = zzj(bArr, zzj, zzik);
            zzka.zzh(zzik.zza);
        }
        if (zzj == i2) {
            return zzj;
        }
        throw zzkj.zzf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzg(byte[] bArr, int i, zzik zzik) throws zzkj {
        int zzj = zzj(bArr, i, zzik);
        int i2 = zzik.zza;
        if (i2 < 0) {
            throw zzkj.zzd();
        } else if (i2 == 0) {
            zzik.zzc = "";
            return zzj;
        } else {
            zzik.zzc = new String(bArr, zzj, i2, zzkh.zza);
            return zzj + i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzh(byte[] bArr, int i, zzik zzik) throws zzkj {
        int zzj = zzj(bArr, i, zzik);
        int i2 = zzik.zza;
        if (i2 < 0) {
            throw zzkj.zzd();
        } else if (i2 == 0) {
            zzik.zzc = "";
            return zzj;
        } else {
            zzik.zzc = zzmx.zzd(bArr, zzj, i2);
            return zzj + i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzi(int i, byte[] bArr, int i2, int i3, zzmj zzmj, zzik zzik) throws zzkj {
        if ((i >>> 3) != 0) {
            int i4 = i & 7;
            if (i4 == 0) {
                int zzm = zzm(bArr, i2, zzik);
                zzmj.zzh(i, Long.valueOf(zzik.zzb));
                return zzm;
            } else if (i4 == 1) {
                zzmj.zzh(i, Long.valueOf(zzn(bArr, i2)));
                return i2 + 8;
            } else if (i4 == 2) {
                int zzj = zzj(bArr, i2, zzik);
                int i5 = zzik.zza;
                if (i5 < 0) {
                    throw zzkj.zzd();
                } else if (i5 <= bArr.length - zzj) {
                    if (i5 == 0) {
                        zzmj.zzh(i, zziy.zzb);
                    } else {
                        zzmj.zzh(i, zziy.zzl(bArr, zzj, i5));
                    }
                    return zzj + i5;
                } else {
                    throw zzkj.zzf();
                }
            } else if (i4 == 3) {
                int i6 = (i & -8) | 4;
                zzmj zze = zzmj.zze();
                int i7 = 0;
                while (true) {
                    if (i2 >= i3) {
                        break;
                    }
                    int zzj2 = zzj(bArr, i2, zzik);
                    int i8 = zzik.zza;
                    if (i8 == i6) {
                        i7 = i8;
                        i2 = zzj2;
                        break;
                    }
                    i2 = zzi(i8, bArr, zzj2, i3, zze, zzik);
                    i7 = i8;
                }
                if (i2 > i3 || i7 != i6) {
                    throw zzkj.zze();
                }
                zzmj.zzh(i, zze);
                return i2;
            } else if (i4 == 5) {
                zzmj.zzh(i, Integer.valueOf(zzb(bArr, i2)));
                return i2 + 4;
            } else {
                throw zzkj.zzb();
            }
        } else {
            throw zzkj.zzb();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzj(byte[] bArr, int i, zzik zzik) {
        int i2 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            return zzk(b, bArr, i2, zzik);
        }
        zzik.zza = b;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzk(int i, byte[] bArr, int i2, zzik zzik) {
        int i3 = i & 127;
        int i4 = i2 + 1;
        byte b = bArr[i2];
        if (b >= 0) {
            zzik.zza = i3 | (b << 7);
            return i4;
        }
        int i5 = i3 | ((b & Byte.MAX_VALUE) << 7);
        int i6 = i4 + 1;
        byte b2 = bArr[i4];
        if (b2 >= 0) {
            zzik.zza = i5 | (b2 << Ascii.SO);
            return i6;
        }
        int i7 = i5 | ((b2 & Byte.MAX_VALUE) << 14);
        int i8 = i6 + 1;
        byte b3 = bArr[i6];
        if (b3 >= 0) {
            zzik.zza = i7 | (b3 << Ascii.NAK);
            return i8;
        }
        int i9 = i7 | ((b3 & Byte.MAX_VALUE) << 21);
        int i10 = i8 + 1;
        byte b4 = bArr[i8];
        if (b4 >= 0) {
            zzik.zza = i9 | (b4 << Ascii.FS);
            return i10;
        }
        int i11 = i9 | ((b4 & Byte.MAX_VALUE) << 28);
        while (true) {
            int i12 = i10 + 1;
            if (bArr[i10] < 0) {
                i10 = i12;
            } else {
                zzik.zza = i11;
                return i12;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzl(int i, byte[] bArr, int i2, int i3, zzkg<?> zzkg, zzik zzik) {
        zzka zzka = (zzka) zzkg;
        int zzj = zzj(bArr, i2, zzik);
        zzka.zzh(zzik.zza);
        while (zzj < i3) {
            int zzj2 = zzj(bArr, zzj, zzik);
            if (i != zzik.zza) {
                break;
            }
            zzj = zzj(bArr, zzj2, zzik);
            zzka.zzh(zzik.zza);
        }
        return zzj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzm(byte[] bArr, int i, zzik zzik) {
        int i2 = i + 1;
        long j = (long) bArr[i];
        if (j < 0) {
            int i3 = i2 + 1;
            byte b = bArr[i2];
            long j2 = (j & 127) | (((long) (b & Byte.MAX_VALUE)) << 7);
            int i4 = 7;
            while (b < 0) {
                int i5 = i3 + 1;
                byte b2 = bArr[i3];
                i4 += 7;
                j2 |= ((long) (b2 & Byte.MAX_VALUE)) << i4;
                b = b2;
                i3 = i5;
            }
            zzik.zzb = j2;
            return i3;
        }
        zzik.zzb = j;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long zzn(byte[] bArr, int i) {
        return ((((long) bArr[i + 7]) & 255) << 56) | (((long) bArr[i]) & 255) | ((((long) bArr[i + 1]) & 255) << 8) | ((((long) bArr[i + 2]) & 255) << 16) | ((((long) bArr[i + 3]) & 255) << 24) | ((((long) bArr[i + 4]) & 255) << 32) | ((((long) bArr[i + 5]) & 255) << 40) | ((((long) bArr[i + 6]) & 255) << 48);
    }
}
