package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
final class zzeb extends zzdt {
    final /* synthetic */ Activity zza;
    final /* synthetic */ zzbz zzb;
    final /* synthetic */ zzed zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzeb(zzed zzed, Activity activity, zzbz zzbz) {
        super(zzed.zza, true);
        this.zzc = zzed;
        this.zza = activity;
        this.zzb = zzbz;
    }

    @Override // com.google.android.gms.internal.measurement.zzdt
    final void zza() throws RemoteException {
        ((zzcc) Preconditions.checkNotNull(this.zzc.zza.zzj)).onActivitySaveInstanceState(ObjectWrapper.wrap(this.zza), this.zzb, this.zzi);
    }
}
