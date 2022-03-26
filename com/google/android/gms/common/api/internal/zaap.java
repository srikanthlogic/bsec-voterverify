package com.google.android.gms.common.api.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public abstract class zaap implements Runnable {
    private final /* synthetic */ zaaf zaa;

    private zaap(zaaf zaaf) {
        this.zaa = zaaf;
    }

    protected abstract void zaa();

    @Override // java.lang.Runnable
    public void run() {
        this.zaa.zab.lock();
        try {
            if (!Thread.interrupted()) {
                zaa();
            }
        } catch (RuntimeException e) {
            this.zaa.zaa.zaa(e);
        } finally {
            this.zaa.zab.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zaap(zaaf zaaf, zaae zaae) {
        this(zaaf);
    }
}
