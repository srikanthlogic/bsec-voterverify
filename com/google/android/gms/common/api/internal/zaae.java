package com.google.android.gms.common.api.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaae implements Runnable {
    private final /* synthetic */ zaaf zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaae(zaaf zaaf) {
        this.zaa = zaaf;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zaa.zad.cancelAvailabilityErrorNotifications(this.zaa.zac);
    }
}
