package com.google.android.gms.internal.measurement;

import java.util.Iterator;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzmm implements Iterator<String> {
    final Iterator<String> zza;
    final /* synthetic */ zzmn zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzmm(zzmn zzmn) {
        this.zzb = zzmn;
        this.zza = this.zzb.zza.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zza.hasNext();
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ String next() {
        return this.zza.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
