package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zacg implements Runnable {
    private final /* synthetic */ zace zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zacg(zace zace) {
        this.zaa = zace;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zaa.zah.zaa(new ConnectionResult(4));
    }
}
