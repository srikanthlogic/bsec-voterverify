package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzad implements Iterator<zzap> {
    final /* synthetic */ zzae zza;
    private int zzb = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzad(zzae zzae) {
        this.zza = zzae;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzb < this.zza.zzc();
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ zzap next() {
        if (this.zzb < this.zza.zzc()) {
            zzae zzae = this.zza;
            int i = this.zzb;
            this.zzb = i + 1;
            return zzae.zze(i);
        }
        int i2 = this.zzb;
        StringBuilder sb = new StringBuilder(32);
        sb.append("Out of bounds index: ");
        sb.append(i2);
        throw new NoSuchElementException(sb.toString());
    }
}
