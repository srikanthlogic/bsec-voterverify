package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhu implements Application.ActivityLifecycleCallbacks {
    final /* synthetic */ zzhv zza;

    public /* synthetic */ zzhu(zzhv zzhv, zzht zzht) {
        this.zza = zzhv;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        zzfs zzfs;
        try {
            try {
                this.zza.zzs.zzay().zzj().zza("onActivityCreated");
                Intent intent = activity.getIntent();
                if (intent == null) {
                    zzfs = ((zze) this.zza).zzs;
                } else {
                    Uri data = intent.getData();
                    if (data != null && data.isHierarchical()) {
                        this.zza.zzs.zzv();
                        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
                        boolean z = true;
                        String str = true != (("android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra)) ? true : "android-app://com.google.appcrawler".equals(stringExtra)) ? DebugKt.DEBUG_PROPERTY_VALUE_AUTO : "gs";
                        String queryParameter = data.getQueryParameter("referrer");
                        if (bundle != null) {
                            z = false;
                        }
                        this.zza.zzs.zzaz().zzp(new zzhs(this, z, data, str, queryParameter));
                        zzfs = ((zze) this.zza).zzs;
                    }
                    zzfs = ((zze) this.zza).zzs;
                }
            } catch (RuntimeException e) {
                this.zza.zzs.zzay().zzd().zzb("Throwable caught in onActivityCreated", e);
                zzfs = ((zze) this.zza).zzs;
            }
            zzfs.zzs().zzr(activity, bundle);
        } catch (Throwable th) {
            ((zze) this.zza).zzs.zzs().zzr(activity, bundle);
            throw th;
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityDestroyed(Activity activity) {
        ((zze) this.zza).zzs.zzs().zzs(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityPaused(Activity activity) {
        ((zze) this.zza).zzs.zzs().zzt(activity);
        zzjy zzu = ((zze) this.zza).zzs.zzu();
        zzu.zzs.zzaz().zzp(new zzjr(zzu, zzu.zzs.zzav().elapsedRealtime()));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityResumed(Activity activity) {
        zzjy zzu = ((zze) this.zza).zzs.zzu();
        zzu.zzs.zzaz().zzp(new zzjq(zzu, zzu.zzs.zzav().elapsedRealtime()));
        ((zze) this.zza).zzs.zzs().zzu(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        ((zze) this.zza).zzs.zzs().zzv(activity, bundle);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStopped(Activity activity) {
    }
}
