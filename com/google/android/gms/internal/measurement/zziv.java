package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.nio.charset.Charset;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public class zziv extends zziu {
    protected final byte[] zza;

    public zziv(byte[] bArr) {
        if (bArr != null) {
            this.zza = bArr;
            return;
        }
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zziy, java.lang.Object
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zziy) || zzd() != ((zziy) obj).zzd()) {
            return false;
        }
        if (zzd() == 0) {
            return true;
        }
        if (!(obj instanceof zziv)) {
            return obj.equals(this);
        }
        zziv zziv = (zziv) obj;
        int zzk = zzk();
        int zzk2 = zziv.zzk();
        if (zzk != 0 && zzk2 != 0 && zzk != zzk2) {
            return false;
        }
        int zzd = zzd();
        if (zzd > zziv.zzd()) {
            int zzd2 = zzd();
            StringBuilder sb = new StringBuilder(40);
            sb.append("Length too large: ");
            sb.append(zzd);
            sb.append(zzd2);
            throw new IllegalArgumentException(sb.toString());
        } else if (zzd > zziv.zzd()) {
            int zzd3 = zziv.zzd();
            StringBuilder sb2 = new StringBuilder(59);
            sb2.append("Ran off end of other: 0, ");
            sb2.append(zzd);
            sb2.append(", ");
            sb2.append(zzd3);
            throw new IllegalArgumentException(sb2.toString());
        } else if (!(zziv instanceof zziv)) {
            return zziv.zzf(0, zzd).equals(zzf(0, zzd));
        } else {
            byte[] bArr = this.zza;
            byte[] bArr2 = zziv.zza;
            zziv.zzc();
            int i = 0;
            int i2 = 0;
            while (i < zzd) {
                if (bArr[i] != bArr2[i2]) {
                    return false;
                }
                i++;
                i2++;
            }
            return true;
        }
    }

    @Override // com.google.android.gms.internal.measurement.zziy
    public byte zza(int i) {
        return this.zza[i];
    }

    @Override // com.google.android.gms.internal.measurement.zziy
    public byte zzb(int i) {
        return this.zza[i];
    }

    protected int zzc() {
        return 0;
    }

    @Override // com.google.android.gms.internal.measurement.zziy
    public int zzd() {
        return this.zza.length;
    }

    @Override // com.google.android.gms.internal.measurement.zziy
    protected final int zze(int i, int i2, int i3) {
        return zzkh.zzd(i, this.zza, 0, i3);
    }

    @Override // com.google.android.gms.internal.measurement.zziy
    public final zziy zzf(int i, int i2) {
        int zzj = zzj(0, i2, zzd());
        if (zzj == 0) {
            return zziy.zzb;
        }
        return new zzis(this.zza, 0, zzj);
    }

    @Override // com.google.android.gms.internal.measurement.zziy
    protected final String zzg(Charset charset) {
        return new String(this.zza, 0, zzd(), charset);
    }

    @Override // com.google.android.gms.internal.measurement.zziy
    public final void zzh(zzin zzin) throws IOException {
        ((zzjd) zzin).zzc(this.zza, 0, zzd());
    }

    @Override // com.google.android.gms.internal.measurement.zziy
    public final boolean zzi() {
        return zzmx.zzf(this.zza, 0, zzd());
    }
}
