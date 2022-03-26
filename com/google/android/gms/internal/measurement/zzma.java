package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzma implements Iterator<Map.Entry> {
    final /* synthetic */ zzme zza;
    private int zzb = -1;
    private boolean zzc;
    private Iterator<Map.Entry> zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzma(zzme zzme, zzlz zzlz) {
        this.zza = zzme;
    }

    private final Iterator<Map.Entry> zza() {
        if (this.zzd == null) {
            this.zzd = this.zza.zzc.entrySet().iterator();
        }
        return this.zzd;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (this.zzb + 1 >= this.zza.zzb.size()) {
            return !this.zza.zzc.isEmpty() && zza().hasNext();
        }
        return true;
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ Map.Entry next() {
        this.zzc = true;
        int i = this.zzb + 1;
        this.zzb = i;
        if (i < this.zza.zzb.size()) {
            return (Map.Entry) this.zza.zzb.get(this.zzb);
        }
        return zza().next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        if (this.zzc) {
            this.zzc = false;
            this.zza.zzn();
            if (this.zzb < this.zza.zzb.size()) {
                zzme zzme = this.zza;
                int i = this.zzb;
                this.zzb = i - 1;
                Object unused = zzme.zzl(i);
                return;
            }
            zza().remove();
            return;
        }
        throw new IllegalStateException("remove() was called before next()");
    }
}
