package com.google.android.gms.internal.measurement;

import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzho extends zzhu<Boolean> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzho(zzhr zzhr, String str, Boolean bool, boolean z) {
        super(zzhr, str, bool, true, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.measurement.zzhu
    final /* bridge */ /* synthetic */ Boolean zza(Object obj) {
        if (zzgv.zzc.matcher(obj).matches()) {
            return true;
        }
        if (zzgv.zzd.matcher(obj).matches()) {
            return false;
        }
        String zzc = super.zzc();
        String str = (String) obj;
        StringBuilder sb = new StringBuilder(String.valueOf(zzc).length() + 28 + str.length());
        sb.append("Invalid boolean value for ");
        sb.append(zzc);
        sb.append(": ");
        sb.append(str);
        Log.e("PhenotypeFlag", sb.toString());
        return null;
    }
}
