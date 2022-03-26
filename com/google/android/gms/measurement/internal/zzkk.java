package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkk implements zzkt {
    final /* synthetic */ zzkn zza;

    public zzkk(zzkn zzkn) {
        this.zza = zzkn;
    }

    @Override // com.google.android.gms.measurement.internal.zzkt
    public final void zza(String str, String str2, Bundle bundle) {
        if (!TextUtils.isEmpty(str)) {
            this.zza.zzaz().zzp(new zzkj(this, str, "_err", bundle));
        } else if (this.zza.zzn != null) {
            this.zza.zzn.zzay().zzd().zzb("AppId not known when logging event", "_err");
        }
    }
}
