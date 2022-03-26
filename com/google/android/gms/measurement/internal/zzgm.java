package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzgm extends zzgl {
    private boolean zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgm(zzfs zzfs) {
        super(zzfs);
        this.zzs.zzD();
    }

    protected void zzaA() {
    }

    protected abstract boolean zzf();

    /* JADX INFO: Access modifiers changed from: protected */
    public final void zzu() {
        if (!zzx()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzx() {
        return this.zza;
    }

    public final void zzv() {
        if (this.zza) {
            throw new IllegalStateException("Can't initialize twice");
        } else if (!zzf()) {
            this.zzs.zzB();
            this.zza = true;
        }
    }

    public final void zzw() {
        if (!this.zza) {
            zzaA();
            this.zzs.zzB();
            this.zza = true;
            return;
        }
        throw new IllegalStateException("Can't initialize twice");
    }
}
