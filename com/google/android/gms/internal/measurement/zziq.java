package com.google.android.gms.internal.measurement;

import java.util.Comparator;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zziq implements Comparator<zziy> {
    @Override // java.util.Comparator
    public final /* bridge */ /* synthetic */ int compare(zziy zziy, zziy zziy2) {
        zziy zziy3 = zziy;
        zziy zziy4 = zziy2;
        zzio zzio = new zzio(zziy3);
        zzio zzio2 = new zzio(zziy4);
        while (zzio.hasNext() && zzio2.hasNext()) {
            int zza = zzip.zza(zzio.zza() & 255, zzio2.zza() & 255);
            if (zza != 0) {
                return zza;
            }
        }
        return zzip.zza(zziy3.zzd(), zziy4.zzd());
    }
}
