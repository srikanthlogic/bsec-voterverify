package com.google.firebase.analytics;

import java.util.concurrent.Callable;
/* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
/* loaded from: classes3.dex */
final class zzb implements Callable<String> {
    final /* synthetic */ FirebaseAnalytics zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzb(FirebaseAnalytics firebaseAnalytics) {
        this.zza = firebaseAnalytics;
    }

    @Override // java.util.concurrent.Callable
    public final /* bridge */ /* synthetic */ String call() throws Exception {
        return this.zza.zzb.zzk();
    }
}
