package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkh implements zzek {
    final /* synthetic */ zzkn zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzkh(zzkn zzkn) {
        this.zza = zzkn;
    }

    @Override // com.google.android.gms.measurement.internal.zzek
    public final void zza(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        this.zza.zzH(str, i, th, bArr, map);
    }
}
