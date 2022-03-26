package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzci;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-sdk@@19.0.1 */
/* loaded from: classes.dex */
public final class zzn implements zzgs {
    public final zzci zza;
    final /* synthetic */ AppMeasurementDynamiteService zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(AppMeasurementDynamiteService appMeasurementDynamiteService, zzci zzci) {
        this.zzb = appMeasurementDynamiteService;
        this.zza = zzci;
    }

    @Override // com.google.android.gms.measurement.internal.zzgs
    public final void interceptEvent(String str, String str2, Bundle bundle, long j) {
        try {
            this.zza.zze(str, str2, bundle, j);
        } catch (RemoteException e) {
            zzfs zzfs = this.zzb.zza;
            if (zzfs != null) {
                zzfs.zzay().zzk().zzb("Event interceptor threw exception", e);
            }
        }
    }
}
