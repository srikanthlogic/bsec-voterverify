package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GoogleApiManager;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zabe implements Runnable {
    private final /* synthetic */ int zaa;
    private final /* synthetic */ GoogleApiManager.zaa zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zabe(GoogleApiManager.zaa zaa, int i) {
        this.zab = zaa;
        this.zaa = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GoogleApiManager.zaa.zaa(this.zab, this.zaa);
    }
}
