package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzlb {
    public static final int zza(int i, Object obj, Object obj2) {
        zzla zzla = (zzla) obj;
        zzkz zzkz = (zzkz) obj2;
        if (zzla.isEmpty()) {
            return 0;
        }
        Iterator it = zzla.entrySet().iterator();
        if (!it.hasNext()) {
            return 0;
        }
        Map.Entry entry = (Map.Entry) it.next();
        entry.getKey();
        entry.getValue();
        throw null;
    }

    public static final Object zzb(Object obj, Object obj2) {
        zzla zzla = (zzla) obj;
        zzla zzla2 = (zzla) obj2;
        if (!zzla2.isEmpty()) {
            if (!zzla.zze()) {
                zzla = zzla.zzb();
            }
            zzla.zzd(zzla2);
        }
        return zzla;
    }
}
