package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zaaj extends zaay {
    private final /* synthetic */ ConnectionResult zaa;
    private final /* synthetic */ zaag zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zaaj(zaag zaag, zaaw zaaw, ConnectionResult connectionResult) {
        super(zaaw);
        this.zab = zaag;
        this.zaa = connectionResult;
    }

    @Override // com.google.android.gms.common.api.internal.zaay
    public final void zaa() {
        this.zab.zaa.zab(this.zaa);
    }
}
