package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.Arrays;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzmj {
    private static final zzmj zza = new zzmj(0, new int[0], new Object[0], false);
    private int zzb;
    private int[] zzc;
    private Object[] zzd;
    private int zze;
    private boolean zzf;

    private zzmj() {
        this(0, new int[8], new Object[8], true);
    }

    private zzmj(int i, int[] iArr, Object[] objArr, boolean z) {
        this.zze = -1;
        this.zzb = i;
        this.zzc = iArr;
        this.zzd = objArr;
        this.zzf = z;
    }

    public static zzmj zzc() {
        return zza;
    }

    public static zzmj zzd(zzmj zzmj, zzmj zzmj2) {
        int i = zzmj.zzb + zzmj2.zzb;
        int[] copyOf = Arrays.copyOf(zzmj.zzc, i);
        System.arraycopy(zzmj2.zzc, 0, copyOf, zzmj.zzb, zzmj2.zzb);
        Object[] copyOf2 = Arrays.copyOf(zzmj.zzd, i);
        System.arraycopy(zzmj2.zzd, 0, copyOf2, zzmj.zzb, zzmj2.zzb);
        return new zzmj(i, copyOf, copyOf2, true);
    }

    public static zzmj zze() {
        return new zzmj(0, new int[8], new Object[8], true);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof zzmj)) {
            return false;
        }
        zzmj zzmj = (zzmj) obj;
        int i = this.zzb;
        if (i == zzmj.zzb) {
            int[] iArr = this.zzc;
            int[] iArr2 = zzmj.zzc;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    Object[] objArr = this.zzd;
                    Object[] objArr2 = zzmj.zzd;
                    int i3 = this.zzb;
                    for (int i4 = 0; i4 < i3; i4++) {
                        if (objArr[i4].equals(objArr2[i4])) {
                        }
                    }
                    return true;
                } else if (iArr[i2] != iArr2[i2]) {
                    break;
                } else {
                    i2++;
                }
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = this.zzb;
        int i2 = (i + 527) * 31;
        int[] iArr = this.zzc;
        int i3 = 17;
        int i4 = 17;
        for (int i5 = 0; i5 < i; i5++) {
            i4 = (i4 * 31) + iArr[i5];
        }
        int i6 = (i2 + i4) * 31;
        Object[] objArr = this.zzd;
        int i7 = this.zzb;
        for (int i8 = 0; i8 < i7; i8++) {
            i3 = (i3 * 31) + objArr[i8].hashCode();
        }
        return i6 + i3;
    }

    public final int zza() {
        int i = this.zze;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzb; i3++) {
            int i4 = this.zzc[i3];
            int i5 = i4 >>> 3;
            int i6 = i4 & 7;
            if (i6 == 0) {
                i2 += zzjg.zzA(i5 << 3) + zzjg.zzB(((Long) this.zzd[i3]).longValue());
            } else if (i6 == 1) {
                ((Long) this.zzd[i3]).longValue();
                i2 += zzjg.zzA(i5 << 3) + 8;
            } else if (i6 == 2) {
                int zzA = zzjg.zzA(i5 << 3);
                int zzd = ((zziy) this.zzd[i3]).zzd();
                i2 += zzA + zzjg.zzA(zzd) + zzd;
            } else if (i6 == 3) {
                int zzz = zzjg.zzz(i5);
                i2 += zzz + zzz + ((zzmj) this.zzd[i3]).zza();
            } else if (i6 == 5) {
                ((Integer) this.zzd[i3]).intValue();
                i2 += zzjg.zzA(i5 << 3) + 4;
            } else {
                throw new IllegalStateException(zzkj.zza());
            }
        }
        this.zze = i2;
        return i2;
    }

    public final int zzb() {
        int i = this.zze;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzb; i3++) {
            int i4 = this.zzc[i3];
            int zzA = zzjg.zzA(8);
            int zzd = ((zziy) this.zzd[i3]).zzd();
            i2 += zzA + zzA + zzjg.zzA(16) + zzjg.zzA(i4 >>> 3) + zzjg.zzA(24) + zzjg.zzA(zzd) + zzd;
        }
        this.zze = i2;
        return i2;
    }

    public final void zzf() {
        this.zzf = false;
    }

    public final void zzg(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < this.zzb; i2++) {
            zzli.zzb(sb, i, String.valueOf(this.zzc[i2] >>> 3), this.zzd[i2]);
        }
    }

    public final void zzh(int i, Object obj) {
        int i2;
        if (this.zzf) {
            int i3 = this.zzb;
            int[] iArr = this.zzc;
            if (i3 == iArr.length) {
                if (i3 < 4) {
                    i2 = 8;
                } else {
                    i2 = i3 >> 1;
                }
                int i4 = i3 + i2;
                this.zzc = Arrays.copyOf(iArr, i4);
                this.zzd = Arrays.copyOf(this.zzd, i4);
            }
            int[] iArr2 = this.zzc;
            int i5 = this.zzb;
            iArr2[i5] = i;
            this.zzd[i5] = obj;
            this.zzb = i5 + 1;
            return;
        }
        throw new UnsupportedOperationException();
    }

    public final void zzi(zzjh zzjh) throws IOException {
        if (this.zzb != 0) {
            for (int i = 0; i < this.zzb; i++) {
                int i2 = this.zzc[i];
                Object obj = this.zzd[i];
                int i3 = i2 >>> 3;
                int i4 = i2 & 7;
                if (i4 == 0) {
                    zzjh.zzt(i3, ((Long) obj).longValue());
                } else if (i4 == 1) {
                    zzjh.zzm(i3, ((Long) obj).longValue());
                } else if (i4 == 2) {
                    zzjh.zzd(i3, (zziy) obj);
                } else if (i4 == 3) {
                    zzjh.zzE(i3);
                    ((zzmj) obj).zzi(zzjh);
                    zzjh.zzh(i3);
                } else if (i4 == 5) {
                    zzjh.zzk(i3, ((Integer) obj).intValue());
                } else {
                    throw new RuntimeException(zzkj.zza());
                }
            }
        }
    }
}
