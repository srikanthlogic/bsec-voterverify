package com.google.android.gms.common.api.internal;

import android.app.Dialog;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zam extends zabm {
    private final /* synthetic */ Dialog zaa;
    private final /* synthetic */ zan zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zam(zan zan, Dialog dialog) {
        this.zab = zan;
        this.zaa = dialog;
    }

    @Override // com.google.android.gms.common.api.internal.zabm
    public final void zaa() {
        this.zab.zaa.zab();
        if (this.zaa.isShowing()) {
            this.zaa.dismiss();
        }
    }
}
