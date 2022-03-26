package com.google.android.gms.common.api.internal;

import com.google.android.gms.signin.internal.zak;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zacf implements Runnable {
    private final /* synthetic */ zak zaa;
    private final /* synthetic */ zace zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zacf(zace zace, zak zak) {
        this.zab = zace;
        this.zaa = zak;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zab.zab(this.zaa);
    }
}
