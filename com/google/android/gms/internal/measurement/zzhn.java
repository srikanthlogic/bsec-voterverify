package com.google.android.gms.internal.measurement;

import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhn extends zzhu<Long> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhn(zzhr zzhr, String str, Long l, boolean z) {
        super(zzhr, str, l, true, null);
    }

    @Override // com.google.android.gms.internal.measurement.zzhu
    final /* bridge */ /* synthetic */ Long zza(Object obj) {
        try {
            return Long.valueOf(Long.parseLong((String) obj));
        } catch (NumberFormatException e) {
            String zzc = super.zzc();
            String str = (String) obj;
            StringBuilder sb = new StringBuilder(String.valueOf(zzc).length() + 25 + str.length());
            sb.append("Invalid long value for ");
            sb.append(zzc);
            sb.append(": ");
            sb.append(str);
            Log.e("PhenotypeFlag", sb.toString());
            return null;
        }
    }
}
