package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
public final class zzdj extends zzdt {
    final /* synthetic */ String zza;
    final /* synthetic */ zzbz zzb;
    final /* synthetic */ zzee zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzdj(zzee zzee, String str, zzbz zzbz) {
        super(zzee, true);
        this.zzc = zzee;
        this.zza = str;
        this.zzb = zzbz;
    }

    @Override // com.google.android.gms.internal.measurement.zzdt
    final void zza() throws RemoteException {
        ((zzcc) Preconditions.checkNotNull(this.zzc.zzj)).getMaxUserProperties(this.zza, this.zzb);
    }

    @Override // com.google.android.gms.internal.measurement.zzdt
    protected final void zzb() {
        this.zzb.zzd(null);
    }
}
