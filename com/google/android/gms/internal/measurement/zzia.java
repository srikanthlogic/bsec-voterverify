package com.google.android.gms.internal.measurement;

import javax.annotation.CheckForNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzia<T> extends zzhz<T> {
    private final T zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzia(T t) {
        this.zza = t;
    }

    @Override // java.lang.Object
    public final boolean equals(@CheckForNull Object obj) {
        if (obj instanceof zzia) {
            return this.zza.equals(((zzia) obj).zza);
        }
        return false;
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return this.zza.hashCode() + 1502476572;
    }

    @Override // java.lang.Object
    public final String toString() {
        String valueOf = String.valueOf(this.zza);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 13);
        sb.append("Optional.of(");
        sb.append(valueOf);
        sb.append(")");
        return sb.toString();
    }

    @Override // com.google.android.gms.internal.measurement.zzhz
    public final T zza() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzhz
    public final boolean zzb() {
        return true;
    }
}
