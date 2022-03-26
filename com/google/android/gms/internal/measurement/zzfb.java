package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfb extends zzjv<zzfc, zzfb> implements zzlh {
    private zzfb() {
        super(zzfc.zza);
    }

    public final int zza() {
        return ((zzfc) this.zza).zzb();
    }

    public final zzfa zzb(int i) {
        return ((zzfc) this.zza).zzd(i);
    }

    public final zzfb zzc() {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        ((zzfc) this.zza).zzk = zzfc.zzbA();
        return this;
    }

    public final zzfb zzd(int i, zzez zzez) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zzfc.zzm((zzfc) this.zza, i, zzez.zzaA());
        return this;
    }

    public final List<zzeh> zze() {
        return Collections.unmodifiableList(((zzfc) this.zza).zzi());
    }

    public /* synthetic */ zzfb(zzey zzey) {
        super(zzfc.zza);
    }
}
