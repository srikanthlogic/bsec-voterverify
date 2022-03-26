package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.zak;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaaq implements zak {
    private final /* synthetic */ zaar zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaaq(zaar zaar) {
        this.zaa = zaar;
    }

    @Override // com.google.android.gms.common.internal.zak
    public final boolean isConnected() {
        return this.zaa.isConnected();
    }

    @Override // com.google.android.gms.common.internal.zak
    public final Bundle getConnectionHint() {
        return null;
    }
}
