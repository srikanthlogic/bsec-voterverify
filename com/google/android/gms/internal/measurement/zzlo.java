package com.google.android.gms.internal.measurement;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzlo {
    private static final zzlo zza = new zzlo();
    private final ConcurrentMap<Class<?>, zzlr<?>> zzc = new ConcurrentHashMap();
    private final zzls zzb = new zzky();

    private zzlo() {
    }

    public static zzlo zza() {
        return zza;
    }

    public final <T> zzlr<T> zzb(Class<T> cls) {
        zzkh.zzf(cls, "messageType");
        zzlr<T> zzlr = (zzlr<T>) this.zzc.get(cls);
        if (zzlr == null) {
            zzlr = this.zzb.zza(cls);
            zzkh.zzf(cls, "messageType");
            zzkh.zzf(zzlr, "schema");
            zzlr putIfAbsent = this.zzc.putIfAbsent(cls, zzlr);
            if (putIfAbsent != null) {
                return putIfAbsent;
            }
        }
        return zzlr;
    }
}
