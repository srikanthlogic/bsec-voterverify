package com.google.android.gms.internal.measurement;

import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzaa {
    private String zza;
    private final long zzb;
    private final Map<String, Object> zzc = new HashMap();

    public zzaa(String str, long j, Map<String, Object> map) {
        this.zza = str;
        this.zzb = j;
        if (map != null) {
            this.zzc.putAll(map);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzaa)) {
            return false;
        }
        zzaa zzaa = (zzaa) obj;
        if (this.zzb == zzaa.zzb && this.zza.equals(zzaa.zza)) {
            return this.zzc.equals(zzaa.zzc);
        }
        return false;
    }

    public final int hashCode() {
        int hashCode = this.zza.hashCode();
        long j = this.zzb;
        return (((hashCode * 31) + ((int) (j ^ (j >>> 32)))) * 31) + this.zzc.hashCode();
    }

    public final String toString() {
        String str = this.zza;
        long j = this.zzb;
        String valueOf = String.valueOf(this.zzc);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 55 + String.valueOf(valueOf).length());
        sb.append("Event{name='");
        sb.append(str);
        sb.append("', timestamp=");
        sb.append(j);
        sb.append(", params=");
        sb.append(valueOf);
        sb.append('}');
        return sb.toString();
    }

    public final long zza() {
        return this.zzb;
    }

    /* renamed from: zzb */
    public final zzaa clone() {
        return new zzaa(this.zza, this.zzb, new HashMap(this.zzc));
    }

    public final Object zzc(String str) {
        if (this.zzc.containsKey(str)) {
            return this.zzc.get(str);
        }
        return null;
    }

    public final String zzd() {
        return this.zza;
    }

    public final Map<String, Object> zze() {
        return this.zzc;
    }

    public final void zzf(String str) {
        this.zza = str;
    }

    public final void zzg(String str, Object obj) {
        if (obj == null) {
            this.zzc.remove(str);
        } else {
            this.zzc.put(str, obj);
        }
    }
}
