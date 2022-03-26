package com.google.android.gms.internal.measurement;

import androidx.exifinterface.media.ExifInterface;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzah implements zzap {
    private final Double zza;

    public zzah(Double d) {
        this.zza = d == null ? Double.valueOf(Double.NaN) : d;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzah)) {
            return false;
        }
        return this.zza.equals(((zzah) obj).zza);
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    public final String toString() {
        return zzi();
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final zzap zzbK(String str, zzg zzg, List<zzap> list) {
        if ("toString".equals(str)) {
            return new zzat(zzi());
        }
        throw new IllegalArgumentException(String.format("%s.%s is not a function.", zzi(), str));
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final zzap zzd() {
        return new zzah(this.zza);
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Boolean zzg() {
        boolean z = false;
        if (!Double.isNaN(this.zza.doubleValue()) && this.zza.doubleValue() != 0.0d) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Double zzh() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final String zzi() {
        int i;
        if (Double.isNaN(this.zza.doubleValue())) {
            return "NaN";
        }
        if (Double.isInfinite(this.zza.doubleValue())) {
            return this.zza.doubleValue() > 0.0d ? "Infinity" : "-Infinity";
        }
        BigDecimal stripTrailingZeros = BigDecimal.valueOf(this.zza.doubleValue()).stripTrailingZeros();
        DecimalFormat decimalFormat = new DecimalFormat("0E0");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        if (stripTrailingZeros.scale() > 0) {
            i = stripTrailingZeros.precision() - 1;
        } else {
            i = stripTrailingZeros.scale() - 1;
        }
        decimalFormat.setMinimumFractionDigits(i);
        String format = decimalFormat.format(stripTrailingZeros);
        int indexOf = format.indexOf(ExifInterface.LONGITUDE_EAST);
        if (indexOf <= 0) {
            return format;
        }
        int parseInt = Integer.parseInt(format.substring(indexOf + 1));
        if ((parseInt >= 0 || parseInt <= -7) && (parseInt < 0 || parseInt >= 21)) {
            return format.replace("E-", "e-").replace(ExifInterface.LONGITUDE_EAST, "e+");
        }
        return stripTrailingZeros.toPlainString();
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Iterator<zzap> zzl() {
        return null;
    }
}
