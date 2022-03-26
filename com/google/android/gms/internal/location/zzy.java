package com.google.android.gms.internal.location;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
final class zzy extends zzah {
    private final BaseImplementation.ResultHolder<Status> zza;

    public zzy(BaseImplementation.ResultHolder<Status> resultHolder) {
        this.zza = resultHolder;
    }

    @Override // com.google.android.gms.internal.location.zzai
    public final void zzb(zzaa zzaa) {
        this.zza.setResult(zzaa.getStatus());
    }

    @Override // com.google.android.gms.internal.location.zzai
    public final void zzc() {
    }
}
