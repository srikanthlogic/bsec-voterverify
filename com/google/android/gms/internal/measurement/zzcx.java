package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.measurement.dynamite.ModuleDescriptor;
import com.google.android.gms.measurement.internal.zzfk;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
public final class zzcx extends zzdt {
    final /* synthetic */ String zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ Context zzc;
    final /* synthetic */ Bundle zzd;
    final /* synthetic */ zzee zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzcx(zzee zzee, String str, String str2, Context context, Bundle bundle) {
        super(zzee, true);
        this.zze = zzee;
        this.zza = str;
        this.zzb = str2;
        this.zzc = context;
        this.zzd = bundle;
    }

    @Override // com.google.android.gms.internal.measurement.zzdt
    public final void zza() {
        String str;
        String str2;
        boolean z;
        try {
            zzee zzee = this.zze;
            String str3 = null;
            if (zzee.zzV(this.zza, this.zzb)) {
                str = this.zzb;
                str2 = this.zza;
                str3 = this.zze.zzd;
            } else {
                str2 = null;
                str = null;
            }
            Preconditions.checkNotNull(this.zzc);
            zzee zzee2 = this.zze;
            zzee2.zzj = zzee2.zzf(this.zzc, true);
            if (this.zze.zzj == null) {
                Log.w(this.zze.zzd, "Failed to connect to measurement client.");
                return;
            }
            int localVersion = DynamiteModule.getLocalVersion(this.zzc, ModuleDescriptor.MODULE_ID);
            int remoteVersion = DynamiteModule.getRemoteVersion(this.zzc, ModuleDescriptor.MODULE_ID);
            int max = Math.max(localVersion, remoteVersion);
            if (remoteVersion < localVersion) {
                z = true;
            } else {
                z = false;
            }
            ((zzcc) Preconditions.checkNotNull(this.zze.zzj)).initialize(ObjectWrapper.wrap(this.zzc), new zzcl(42097, (long) max, z, str3, str2, str, this.zzd, zzfk.zza(this.zzc)), this.zzh);
        } catch (Exception e) {
            this.zze.zzS(e, true, false);
        }
    }
}
