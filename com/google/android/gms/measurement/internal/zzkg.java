package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkg implements zzek {
    final /* synthetic */ String zza;
    final /* synthetic */ zzkn zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzkg(zzkn zzkn, String str) {
        this.zzb = zzkn;
        this.zza = str;
    }

    @Override // com.google.android.gms.measurement.internal.zzek
    public final void zza(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        this.zzb.zzJ(i, th, bArr, this.zza);
    }
}
