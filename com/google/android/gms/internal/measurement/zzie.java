package com.google.android.gms.internal.measurement;

import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.CheckForNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzie<T> implements Serializable, zzib {
    final T zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzie(T t) {
        this.zza = t;
    }

    @Override // java.lang.Object
    public final boolean equals(@CheckForNull Object obj) {
        if (!(obj instanceof zzie)) {
            return false;
        }
        T t = this.zza;
        T t2 = ((zzie) obj).zza;
        if (t == t2 || t.equals(t2)) {
            return true;
        }
        return false;
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zza});
    }

    @Override // java.lang.Object
    public final String toString() {
        String valueOf = String.valueOf(this.zza);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 22);
        sb.append("Suppliers.ofInstance(");
        sb.append(valueOf);
        sb.append(")");
        return sb.toString();
    }

    @Override // com.google.android.gms.internal.measurement.zzib
    public final T zza() {
        return this.zza;
    }
}
