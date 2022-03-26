package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhg extends ContentObserver {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhg(zzhh zzhh, Handler handler) {
        super(null);
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        zzhu.zze();
    }
}
