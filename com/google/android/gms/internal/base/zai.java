package com.google.android.gms.internal.base;

import android.graphics.drawable.Drawable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zai extends Drawable.ConstantState {
    int zaa;
    int zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zai(zai zai) {
        if (zai != null) {
            this.zaa = zai.zaa;
            this.zab = zai.zab;
        }
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public final Drawable newDrawable() {
        return new zaf(this);
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public final int getChangingConfigurations() {
        return this.zaa;
    }
}
