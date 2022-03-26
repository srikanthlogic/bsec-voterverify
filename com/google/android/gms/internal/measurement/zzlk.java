package com.google.android.gms.internal.measurement;

import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzlk<T> implements zzlr<T> {
    private final zzlg zza;
    private final zzmi<?, ?> zzb;
    private final boolean zzc;
    private final zzjm<?> zzd;

    private zzlk(zzmi<?, ?> zzmi, zzjm<?> zzjm, zzlg zzlg) {
        this.zzb = zzmi;
        this.zzc = zzjm.zzc(zzlg);
        this.zzd = zzjm;
        this.zza = zzlg;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> zzlk<T> zzc(zzmi<?, ?> zzmi, zzjm<?> zzjm, zzlg zzlg) {
        return new zzlk<>(zzmi, zzjm, zzlg);
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final int zza(T t) {
        zzmi<?, ?> zzmi = this.zzb;
        int zzb = zzmi.zzb(zzmi.zzc(t));
        if (!this.zzc) {
            return zzb;
        }
        this.zzd.zza(t);
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final int zzb(T t) {
        int hashCode = this.zzb.zzc(t).hashCode();
        if (!this.zzc) {
            return hashCode;
        }
        this.zzd.zza(t);
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final T zze() {
        return (T) this.zza.zzbC().zzaC();
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final void zzf(T t) {
        this.zzb.zzg(t);
        this.zzd.zzb(t);
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final void zzg(T t, T t2) {
        zzlt.zzF(this.zzb, t, t2);
        if (this.zzc) {
            zzlt.zzE(this.zzd, t, t2);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final void zzh(T t, byte[] bArr, int i, int i2, zzik zzik) throws IOException {
        zzjz zzjz = (zzjz) t;
        if (zzjz.zzc == zzmj.zzc()) {
            zzjz.zzc = zzmj.zze();
        }
        zzjw zzjw = (zzjw) t;
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final boolean zzi(T t, T t2) {
        if (!this.zzb.zzc(t).equals(this.zzb.zzc(t2))) {
            return false;
        }
        if (!this.zzc) {
            return true;
        }
        this.zzd.zza(t);
        this.zzd.zza(t2);
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final boolean zzj(T t) {
        this.zzd.zza(t);
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zzlr
    public final void zzm(T t, zzjh zzjh) throws IOException {
        this.zzd.zza(t);
        throw null;
    }
}
