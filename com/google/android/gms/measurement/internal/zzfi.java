package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzo;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzfi implements zzo {
    final /* synthetic */ String zza;
    final /* synthetic */ zzfj zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfi(zzfj zzfj, String str) {
        this.zzb = zzfj;
        this.zza = str;
    }

    @Override // com.google.android.gms.internal.measurement.zzo
    public final String zza(String str) {
        Map map = (Map) this.zzb.zzc.get(this.zza);
        if (map == null || !map.containsKey(str)) {
            return null;
        }
        return (String) map.get(str);
    }
}
