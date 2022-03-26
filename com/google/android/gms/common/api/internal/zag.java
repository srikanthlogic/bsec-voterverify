package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zag extends zac<Boolean> {
    private final ListenerHolder.ListenerKey<?> zac;

    public zag(ListenerHolder.ListenerKey<?> listenerKey, TaskCompletionSource<Boolean> taskCompletionSource) {
        super(4, taskCompletionSource);
        this.zac = listenerKey;
    }

    @Override // com.google.android.gms.common.api.internal.zac
    public final void zab(GoogleApiManager.zaa<?> zaa) throws RemoteException {
        zabv remove = zaa.zac().remove(this.zac);
        if (remove != null) {
            remove.zab.unregisterListener(zaa.zab(), this.zab);
            remove.zaa.clearListener();
            return;
        }
        this.zab.trySetResult(false);
    }

    @Override // com.google.android.gms.common.api.internal.zad
    public final Feature[] zac(GoogleApiManager.zaa<?> zaa) {
        zabv zabv = zaa.zac().get(this.zac);
        if (zabv == null) {
            return null;
        }
        return zabv.zaa.getRequiredFeatures();
    }

    @Override // com.google.android.gms.common.api.internal.zad
    public final boolean zad(GoogleApiManager.zaa<?> zaa) {
        zabv zabv = zaa.zac().get(this.zac);
        return zabv != null && zabv.zaa.zaa();
    }

    @Override // com.google.android.gms.common.api.internal.zac, com.google.android.gms.common.api.internal.zab
    public final /* bridge */ /* synthetic */ void zaa(zav zav, boolean z) {
    }

    @Override // com.google.android.gms.common.api.internal.zac, com.google.android.gms.common.api.internal.zab
    public final /* bridge */ /* synthetic */ void zaa(Exception exc) {
        super.zaa(exc);
    }

    @Override // com.google.android.gms.common.api.internal.zac, com.google.android.gms.common.api.internal.zab
    public final /* bridge */ /* synthetic */ void zaa(Status status) {
        super.zaa(status);
    }
}
