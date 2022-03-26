package com.google.android.gms.internal.measurement;

import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhp extends zzhu<Double> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhp(zzhr zzhr, String str, Double d, boolean z) {
        super(zzhr, "measurement.test.double_flag", d, true, null);
    }

    @Override // com.google.android.gms.internal.measurement.zzhu
    final /* bridge */ /* synthetic */ Double zza(Object obj) {
        try {
            return Double.valueOf(Double.parseDouble((String) obj));
        } catch (NumberFormatException e) {
            String zzc = super.zzc();
            String str = (String) obj;
            StringBuilder sb = new StringBuilder(String.valueOf(zzc).length() + 27 + str.length());
            sb.append("Invalid double value for ");
            sb.append(zzc);
            sb.append(": ");
            sb.append(str);
            Log.e("PhenotypeFlag", sb.toString());
            return null;
        }
    }
}
