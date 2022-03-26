package com.google.android.gms.internal.measurement;

import sun.misc.Unsafe;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzmp extends zzmr {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzmp(Unsafe unsafe) {
        super(unsafe);
    }

    @Override // com.google.android.gms.internal.measurement.zzmr
    public final double zza(Object obj, long j) {
        return Double.longBitsToDouble(zzk(obj, j));
    }

    @Override // com.google.android.gms.internal.measurement.zzmr
    public final float zzb(Object obj, long j) {
        return Float.intBitsToFloat(zzj(obj, j));
    }

    @Override // com.google.android.gms.internal.measurement.zzmr
    public final void zzc(Object obj, long j, boolean z) {
        if (zzms.zzb) {
            zzms.zzD(obj, j, r3 ? (byte) 1 : 0);
        } else {
            zzms.zzE(obj, j, r3 ? (byte) 1 : 0);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzmr
    public final void zzd(Object obj, long j, byte b) {
        if (zzms.zzb) {
            zzms.zzD(obj, j, b);
        } else {
            zzms.zzE(obj, j, b);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzmr
    public final void zze(Object obj, long j, double d) {
        zzo(obj, j, Double.doubleToLongBits(d));
    }

    @Override // com.google.android.gms.internal.measurement.zzmr
    public final void zzf(Object obj, long j, float f) {
        zzn(obj, j, Float.floatToIntBits(f));
    }

    @Override // com.google.android.gms.internal.measurement.zzmr
    public final boolean zzg(Object obj, long j) {
        if (zzms.zzb) {
            return zzms.zzt(obj, j);
        }
        return zzms.zzu(obj, j);
    }
}
