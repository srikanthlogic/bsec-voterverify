package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzge extends zzjv<zzgf, zzge> implements zzlh {
    private zzge() {
        super(zzgf.zza);
    }

    public final zzge zza(Iterable<? extends Long> iterable) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzgf.zzh((zzgf) this.zza, iterable);
        return this;
    }

    public final zzge zzb(int i) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzgf.zzg((zzgf) this.zza, i);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzge(zzff zzff) {
        super(zzgf.zza);
    }
}
