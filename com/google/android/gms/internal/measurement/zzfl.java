package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfl extends zzjv<zzfm, zzfl> implements zzlh {
    private zzfl() {
        super(zzfm.zza);
    }

    public final zzfl zza(long j) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzfm.zzf((zzfm) this.zza, j);
        return this;
    }

    public final zzfl zzb(int i) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzfm.zze((zzfm) this.zza, i);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzfl(zzff zzff) {
        super(zzfm.zza);
    }
}
