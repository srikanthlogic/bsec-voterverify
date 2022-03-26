package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzci;
/* compiled from: com.google.android.gms:play-services-measurement-sdk@@19.0.1 */
/* loaded from: classes.dex */
final class zzo implements zzgt {
    public final zzci zza;
    final /* synthetic */ AppMeasurementDynamiteService zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzo(AppMeasurementDynamiteService appMeasurementDynamiteService, zzci zzci) {
        this.zzb = appMeasurementDynamiteService;
        this.zza = zzci;
    }

    @Override // com.google.android.gms.measurement.internal.zzgt
    public final void onEvent(String str, String str2, Bundle bundle, long j) {
        try {
            this.zza.zze(str, str2, bundle, j);
        } catch (RemoteException e) {
            zzfs zzfs = this.zzb.zza;
            if (zzfs != null) {
                zzfs.zzay().zzk().zzb("Event listener threw exception", e);
            }
        }
    }
}
