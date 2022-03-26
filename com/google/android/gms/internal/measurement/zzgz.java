package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzgz extends ContentObserver {
    final /* synthetic */ zzha zza;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzgz(zzha zzha, Handler handler) {
        super(null);
        this.zza = zzha;
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        this.zza.zzf();
    }
}
