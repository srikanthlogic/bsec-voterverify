package com.google.firebase.analytics.connector;

import java.util.concurrent.Executor;
/* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
/* loaded from: classes3.dex */
public final /* synthetic */ class zzb implements Executor {
    public static final /* synthetic */ zzb zza = new zzb();

    private /* synthetic */ zzb() {
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        runnable.run();
    }
}
