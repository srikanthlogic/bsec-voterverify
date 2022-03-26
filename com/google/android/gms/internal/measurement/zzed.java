package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
public final class zzed implements Application.ActivityLifecycleCallbacks {
    final /* synthetic */ zzee zza;

    public zzed(zzee zzee) {
        this.zza = zzee;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        this.zza.zzU(new zzdw(this, bundle, activity));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityDestroyed(Activity activity) {
        this.zza.zzU(new zzec(this, activity));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityPaused(Activity activity) {
        this.zza.zzU(new zzdz(this, activity));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityResumed(Activity activity) {
        this.zza.zzU(new zzdy(this, activity));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        zzbz zzbz = new zzbz();
        this.zza.zzU(new zzeb(this, activity, zzbz));
        Bundle zzb = zzbz.zzb(50);
        if (zzb != null) {
            bundle.putAll(zzb);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStarted(Activity activity) {
        this.zza.zzU(new zzdx(this, activity));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStopped(Activity activity) {
        this.zza.zzU(new zzea(this, activity));
    }
}
