package com.google.android.gms.internal.measurement;

import javax.annotation.CheckForNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhx<T> extends zzhz<T> {
    static final zzhx<Object> zza = new zzhx<>();

    private zzhx() {
    }

    @Override // java.lang.Object
    public final boolean equals(@CheckForNull Object obj) {
        return obj == this;
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return 2040732332;
    }

    @Override // java.lang.Object
    public final String toString() {
        return "Optional.absent()";
    }

    @Override // com.google.android.gms.internal.measurement.zzhz
    public final T zza() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    @Override // com.google.android.gms.internal.measurement.zzhz
    public final boolean zzb() {
        return false;
    }
}
