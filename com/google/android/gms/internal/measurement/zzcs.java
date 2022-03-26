package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
public final class zzcs extends zzdt {
    final /* synthetic */ Boolean zza;
    final /* synthetic */ zzee zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzcs(zzee zzee, Boolean bool) {
        super(zzee, true);
        this.zzb = zzee;
        this.zza = bool;
    }

    @Override // com.google.android.gms.internal.measurement.zzdt
    final void zza() throws RemoteException {
        if (this.zza != null) {
            ((zzcc) Preconditions.checkNotNull(this.zzb.zzj)).setMeasurementEnabled(this.zza.booleanValue(), this.zzh);
        } else {
            ((zzcc) Preconditions.checkNotNull(this.zzb.zzj)).clearMeasurementEnabled(this.zzh);
        }
    }
}
