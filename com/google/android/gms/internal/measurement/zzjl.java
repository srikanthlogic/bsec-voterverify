package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjl {
    private static volatile zzjl zzc;
    private static volatile zzjl zzd;
    private final Map<zzjk, zzjx<?, ?>> zze;
    private static volatile boolean zzb = false;
    static final zzjl zza = new zzjl(true);

    zzjl() {
        this.zze = new HashMap();
    }

    public static zzjl zza() {
        zzjl zzjl = zzc;
        if (zzjl == null) {
            synchronized (zzjl.class) {
                zzjl = zzc;
                if (zzjl == null) {
                    zzjl = zza;
                    zzc = zzjl;
                }
            }
        }
        return zzjl;
    }

    public final <ContainingType extends zzlg> zzjx<ContainingType, ?> zzc(ContainingType containingtype, int i) {
        return (zzjx<ContainingType, ?>) this.zze.get(new zzjk(containingtype, i));
    }

    zzjl(boolean z) {
        this.zze = Collections.emptyMap();
    }

    public static zzjl zzb() {
        zzjl zzjl = zzd;
        if (zzjl != null) {
            return zzjl;
        }
        synchronized (zzjl.class) {
            zzjl zzjl2 = zzd;
            if (zzjl2 != null) {
                return zzjl2;
            }
            zzjl zzb2 = zzjt.zzb(zzjl.class);
            zzd = zzb2;
            return zzb2;
        }
    }
}
