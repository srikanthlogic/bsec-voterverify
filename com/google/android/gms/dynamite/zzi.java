package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
final class zzi implements DynamiteModule.VersionPolicy {
    @Override // com.google.android.gms.dynamite.DynamiteModule.VersionPolicy
    public final DynamiteModule.VersionPolicy.zza zza(Context context, String str, DynamiteModule.VersionPolicy.zzb zzb) throws DynamiteModule.LoadingException {
        DynamiteModule.VersionPolicy.zza zza = new DynamiteModule.VersionPolicy.zza();
        zza.zza = zzb.zza(context, str);
        if (zza.zza != 0) {
            zza.zzb = zzb.zza(context, str, false);
        } else {
            zza.zzb = zzb.zza(context, str, true);
        }
        if (zza.zza == 0 && zza.zzb == 0) {
            zza.zzc = 0;
        } else if (zza.zza >= zza.zzb) {
            zza.zzc = -1;
        } else {
            zza.zzc = 1;
        }
        return zza;
    }
}
