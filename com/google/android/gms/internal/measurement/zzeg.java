package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzeg extends zzjv<zzeh, zzeg> implements zzlh {
    private zzeg() {
        super(zzeh.zza);
    }

    public final int zza() {
        return ((zzeh) this.zza).zzb();
    }

    public final int zzb() {
        return ((zzeh) this.zza).zzc();
    }

    public final zzeg zzc(int i, zzei zzei) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzeh.zzj((zzeh) this.zza, i, zzei.zzaA());
        return this;
    }

    public final zzeg zzd(int i, zzer zzer) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzeh.zzi((zzeh) this.zza, i, zzer.zzaA());
        return this;
    }

    public final zzej zze(int i) {
        return ((zzeh) this.zza).zze(i);
    }

    public final zzes zzf(int i) {
        return ((zzeh) this.zza).zzf(i);
    }

    public /* synthetic */ zzeg(zzef zzef) {
        super(zzeh.zza);
    }
}
