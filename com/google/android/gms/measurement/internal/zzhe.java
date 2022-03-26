package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhe implements Runnable {
    final /* synthetic */ Bundle zza;
    final /* synthetic */ zzhv zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhe(zzhv zzhv, Bundle bundle) {
        this.zzb = zzhv;
        this.zza = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzhv zzhv = this.zzb;
        Bundle bundle = this.zza;
        zzhv.zzg();
        zzhv.zza();
        Preconditions.checkNotNull(bundle);
        String string = bundle.getString(AppMeasurementSdk.ConditionalUserProperty.NAME);
        String string2 = bundle.getString("origin");
        Preconditions.checkNotEmpty(string);
        Preconditions.checkNotEmpty(string2);
        Preconditions.checkNotNull(bundle.get("value"));
        if (!zzhv.zzs.zzJ()) {
            zzhv.zzs.zzay().zzj().zza("Conditional property not set since app measurement is disabled");
            return;
        }
        zzkq zzkq = new zzkq(string, bundle.getLong(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_TIMESTAMP), bundle.get("value"), string2);
        try {
            zzat zzz = zzhv.zzs.zzv().zzz(bundle.getString("app_id"), bundle.getString(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_NAME), bundle.getBundle(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_PARAMS), string2, 0, true, true);
            ((zze) zzhv).zzs.zzt().zzE(new zzab(bundle.getString("app_id"), string2, zzkq, bundle.getLong(AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP), false, bundle.getString(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME), zzhv.zzs.zzv().zzz(bundle.getString("app_id"), bundle.getString(AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_NAME), bundle.getBundle(AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS), string2, 0, true, true), bundle.getLong(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT), zzz, bundle.getLong(AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE), zzhv.zzs.zzv().zzz(bundle.getString("app_id"), bundle.getString(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME), bundle.getBundle(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS), string2, 0, true, true)));
        } catch (IllegalArgumentException e) {
        }
    }
}
