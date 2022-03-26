package com.google.android.gms.internal.measurement;

import javax.annotation.CheckForNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzid<T> implements zzib<T> {
    @CheckForNull
    volatile zzib<T> zza;
    volatile boolean zzb;
    @CheckForNull
    T zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzid(zzib<T> zzib) {
        if (zzib != null) {
            this.zza = zzib;
            return;
        }
        throw null;
    }

    public final String toString() {
        Object obj = this.zza;
        if (obj == null) {
            String valueOf = String.valueOf(this.zzc);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 25);
            sb.append("<supplier that returned ");
            sb.append(valueOf);
            sb.append(">");
            obj = sb.toString();
        }
        String valueOf2 = String.valueOf(obj);
        StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 19);
        sb2.append("Suppliers.memoize(");
        sb2.append(valueOf2);
        sb2.append(")");
        return sb2.toString();
    }

    @Override // com.google.android.gms.internal.measurement.zzib
    public final T zza() {
        if (!this.zzb) {
            synchronized (this) {
                if (!this.zzb) {
                    zzib<T> zzib = this.zza;
                    zzib.getClass();
                    T zza = zzib.zza();
                    this.zzc = zza;
                    this.zzb = true;
                    this.zza = null;
                    return zza;
                }
            }
        }
        return this.zzc;
    }
}
