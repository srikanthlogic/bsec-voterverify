package com.google.android.gms.measurement.internal;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzkd extends zzkc {
    private boolean zza;

    public zzkd(zzkn zzkn) {
        super(zzkn);
        this.zzf.zzL();
    }

    public final void zzY() {
        if (!zzaa()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    public final boolean zzaa() {
        return this.zza;
    }

    protected abstract boolean zzb();

    public final void zzZ() {
        if (!this.zza) {
            zzb();
            this.zzf.zzG();
            this.zza = true;
            return;
        }
        throw new IllegalStateException("Can't initialize twice");
    }
}
