package com.google.android.gms.measurement.internal;

import android.os.Bundle;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzej {
    public final String zza;
    public final String zzb;
    public final long zzc;
    public final Bundle zzd;

    public zzej(String str, String str2, Bundle bundle, long j) {
        this.zza = str;
        this.zzb = str2;
        this.zzd = bundle;
        this.zzc = j;
    }

    public static zzej zzb(zzat zzat) {
        return new zzej(zzat.zza, zzat.zzc, zzat.zzb.zzc(), zzat.zzd);
    }

    public final String toString() {
        String str = this.zzb;
        String str2 = this.zza;
        String valueOf = String.valueOf(this.zzd);
        int length = String.valueOf(str).length();
        StringBuilder sb = new StringBuilder(length + 21 + String.valueOf(str2).length() + String.valueOf(valueOf).length());
        sb.append("origin=");
        sb.append(str);
        sb.append(",name=");
        sb.append(str2);
        sb.append(",params=");
        sb.append(valueOf);
        return sb.toString();
    }

    public final zzat zza() {
        return new zzat(this.zza, new zzar(new Bundle(this.zzd)), this.zzb, this.zzc);
    }
}
