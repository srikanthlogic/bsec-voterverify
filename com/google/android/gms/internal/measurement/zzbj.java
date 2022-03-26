package com.google.android.gms.internal.measurement;

import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzbj extends zzaw {
    @Override // com.google.android.gms.internal.measurement.zzaw
    public final zzap zza(String str, zzg zzg, List<zzap> list) {
        if (str == null || str.isEmpty() || !zzg.zzh(str)) {
            throw new IllegalArgumentException(String.format("Command not found: %s", str));
        }
        zzap zzd = zzg.zzd(str);
        if (zzd instanceof zzai) {
            return ((zzai) zzd).zza(zzg, list);
        }
        throw new IllegalArgumentException(String.format("Function %s is not defined", str));
    }
}
