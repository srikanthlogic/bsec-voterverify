package com.google.android.gms.common.api.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zar implements Runnable {
    private final /* synthetic */ zas zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zar(zas zas) {
        this.zaa = zas;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zaa.zam.lock();
        try {
            this.zaa.zah();
        } finally {
            this.zaa.zam.unlock();
        }
    }
}
