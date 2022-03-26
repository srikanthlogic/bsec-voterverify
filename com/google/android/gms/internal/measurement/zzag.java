package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzag implements zzap {
    private final zzap zza;
    private final String zzb;

    public zzag() {
        zzap zzap = zzf;
        throw null;
    }

    public zzag(String str) {
        this.zza = zzf;
        this.zzb = str;
    }

    public zzag(String str, zzap zzap) {
        this.zza = zzap;
        this.zzb = str;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzag)) {
            return false;
        }
        zzag zzag = (zzag) obj;
        return this.zzb.equals(zzag.zzb) && this.zza.equals(zzag.zza);
    }

    public final int hashCode() {
        return (this.zzb.hashCode() * 31) + this.zza.hashCode();
    }

    public final zzap zzb() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final zzap zzbK(String str, zzg zzg, List<zzap> list) {
        throw new IllegalStateException("Control does not have functions");
    }

    public final String zzc() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final zzap zzd() {
        return new zzag(this.zzb, this.zza.zzd());
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Boolean zzg() {
        throw new IllegalStateException("Control is not a boolean");
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Double zzh() {
        throw new IllegalStateException("Control is not a double");
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final String zzi() {
        throw new IllegalStateException("Control is not a String");
    }

    @Override // com.google.android.gms.internal.measurement.zzap
    public final Iterator<zzap> zzl() {
        return null;
    }
}
