package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
public final class zzdr extends zzdt {
    final /* synthetic */ Long zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ String zzc;
    final /* synthetic */ Bundle zzd;
    final /* synthetic */ boolean zze;
    final /* synthetic */ boolean zzf;
    final /* synthetic */ zzee zzg;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzdr(zzee zzee, Long l, String str, String str2, Bundle bundle, boolean z, boolean z2) {
        super(zzee, true);
        this.zzg = zzee;
        this.zza = l;
        this.zzb = str;
        this.zzc = str2;
        this.zzd = bundle;
        this.zze = z;
        this.zzf = z2;
    }

    @Override // com.google.android.gms.internal.measurement.zzdt
    final void zza() throws RemoteException {
        Long l = this.zza;
        ((zzcc) Preconditions.checkNotNull(this.zzg.zzj)).logEvent(this.zzb, this.zzc, this.zzd, this.zze, this.zzf, l == null ? this.zzh : l.longValue());
    }
}
