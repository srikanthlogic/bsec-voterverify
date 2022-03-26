package com.google.android.gms.common.api.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public abstract class zaay {
    private final zaaw zaa;

    /* JADX INFO: Access modifiers changed from: protected */
    public zaay(zaaw zaaw) {
        this.zaa = zaaw;
    }

    protected abstract void zaa();

    public final void zaa(zaaz zaaz) {
        zaaz.zaf.lock();
        try {
            if (zaaz.zan == this.zaa) {
                zaa();
            }
        } finally {
            zaaz.zaf.unlock();
        }
    }
}
