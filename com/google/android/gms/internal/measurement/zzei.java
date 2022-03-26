package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzei extends zzjv<zzej, zzei> implements zzlh {
    private zzei() {
        super(zzej.zzd());
    }

    public final int zza() {
        return ((zzej) this.zza).zza();
    }

    public final zzei zzb(String str) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzej.zzi((zzej) this.zza, str);
        return this;
    }

    public final zzei zzc(int i, zzel zzel) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzej.zzj((zzej) this.zza, i, zzel);
        return this;
    }

    public final zzel zzd(int i) {
        return ((zzej) this.zza).zze(i);
    }

    public final String zze() {
        return ((zzej) this.zza).zzg();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzei(zzef zzef) {
        super(zzej.zzd());
    }
}
