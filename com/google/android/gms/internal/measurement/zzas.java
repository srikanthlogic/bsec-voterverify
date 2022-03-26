package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzas implements Iterator<zzap> {
    final /* synthetic */ zzat zza;
    private int zzb = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzas(zzat zzat) {
        this.zza = zzat;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzb < this.zza.zza.length();
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ zzap next() {
        if (this.zzb < this.zza.zza.length()) {
            String str = this.zza.zza;
            int i = this.zzb;
            this.zzb = i + 1;
            return new zzat(String.valueOf(str.charAt(i)));
        }
        throw new NoSuchElementException();
    }
}
