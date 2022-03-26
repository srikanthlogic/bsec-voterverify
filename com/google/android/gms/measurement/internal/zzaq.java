package com.google.android.gms.measurement.internal;

import java.util.Iterator;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzaq implements Iterator<String> {
    final Iterator<String> zza;
    final /* synthetic */ zzar zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaq(zzar zzar) {
        this.zzb = zzar;
        this.zza = this.zzb.zza.keySet().iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zza.hasNext();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }

    /* renamed from: zza */
    public final String next() {
        return this.zza.next();
    }
}
