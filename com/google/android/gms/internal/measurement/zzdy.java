package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
final class zzdy extends zzdt {
    final /* synthetic */ Activity zza;
    final /* synthetic */ zzed zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzdy(zzed zzed, Activity activity) {
        super(zzed.zza, true);
        this.zzb = zzed;
        this.zza = activity;
    }

    @Override // com.google.android.gms.internal.measurement.zzdt
    final void zza() throws RemoteException {
        ((zzcc) Preconditions.checkNotNull(this.zzb.zza.zzj)).onActivityResumed(ObjectWrapper.wrap(this.zza), this.zzi);
    }
}
