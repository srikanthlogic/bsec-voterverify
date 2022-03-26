package com.google.android.gms.internal.measurement;

import android.content.Context;
import javax.annotation.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgx extends zzhs {
    private final Context zza;
    private final zzib<zzhz<zzhi>> zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgx(Context context, @Nullable zzib<zzhz<zzhi>> zzib) {
        if (context != null) {
            this.zza = context;
            this.zzb = zzib;
            return;
        }
        throw new NullPointerException("Null context");
    }

    public final boolean equals(Object obj) {
        zzib<zzhz<zzhi>> zzib;
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzhs) {
            zzhs zzhs = (zzhs) obj;
            if (this.zza.equals(zzhs.zza()) && ((zzib = this.zzb) != null ? zzib.equals(zzhs.zzb()) : zzhs.zzb() == null)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int i;
        int hashCode = (this.zza.hashCode() ^ 1000003) * 1000003;
        zzib<zzhz<zzhi>> zzib = this.zzb;
        if (zzib == null) {
            i = 0;
        } else {
            i = zzib.hashCode();
        }
        return hashCode ^ i;
    }

    public final String toString() {
        String valueOf = String.valueOf(this.zza);
        String valueOf2 = String.valueOf(this.zzb);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 46 + String.valueOf(valueOf2).length());
        sb.append("FlagsContext{context=");
        sb.append(valueOf);
        sb.append(", hermeticFileOverrides=");
        sb.append(valueOf2);
        sb.append("}");
        return sb.toString();
    }

    @Override // com.google.android.gms.internal.measurement.zzhs
    final Context zza() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzhs
    @Nullable
    final zzib<zzhz<zzhi>> zzb() {
        return this.zzb;
    }
}
