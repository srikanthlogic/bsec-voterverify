package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
import kotlinx.coroutines.DebugKt;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhj implements zzkt {
    final /* synthetic */ zzhv zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhj(zzhv zzhv) {
        this.zza = zzhv;
    }

    @Override // com.google.android.gms.measurement.internal.zzkt
    public final void zza(String str, String str2, Bundle bundle) {
        if (!TextUtils.isEmpty(str)) {
            this.zza.zzE(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_err", bundle, str);
        } else {
            this.zza.zzC(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_err", bundle);
        }
    }
}
