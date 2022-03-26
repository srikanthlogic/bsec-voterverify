package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhf implements Runnable {
    final /* synthetic */ Bundle zza;
    final /* synthetic */ zzhv zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhf(zzhv zzhv, Bundle bundle) {
        this.zzb = zzhv;
        this.zza = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzkq zzkq;
        String str;
        String str2;
        zzhv zzhv = this.zzb;
        Bundle bundle = this.zza;
        zzhv.zzg();
        zzhv.zza();
        Preconditions.checkNotNull(bundle);
        Preconditions.checkNotEmpty(bundle.getString(AppMeasurementSdk.ConditionalUserProperty.NAME));
        if (zzhv.zzs.zzJ()) {
            if (zzhv.zzs.zzf().zzs(null, zzdw.zzap)) {
                zzkq = new zzkq(bundle.getString(AppMeasurementSdk.ConditionalUserProperty.NAME), 0, null, "");
            } else {
                zzkq = new zzkq(bundle.getString(AppMeasurementSdk.ConditionalUserProperty.NAME), 0, null, null);
            }
            try {
                zzku zzv = zzhv.zzs.zzv();
                String string = bundle.getString("app_id");
                String string2 = bundle.getString(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME);
                Bundle bundle2 = bundle.getBundle(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS);
                if (zzhv.zzs.zzf().zzs(null, zzdw.zzap)) {
                    str = "";
                } else {
                    str = bundle.getString("origin");
                }
                zzat zzz = zzv.zzz(string, string2, bundle2, str, bundle.getLong(AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP), true, true);
                String string3 = bundle.getString("app_id");
                if (zzhv.zzs.zzf().zzs(null, zzdw.zzap)) {
                    str2 = "";
                } else {
                    str2 = bundle.getString("origin");
                }
                ((zze) zzhv).zzs.zzt().zzE(new zzab(string3, str2, zzkq, bundle.getLong(AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP), bundle.getBoolean(AppMeasurementSdk.ConditionalUserProperty.ACTIVE), bundle.getString(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME), null, bundle.getLong(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT), null, bundle.getLong(AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE), zzz));
            } catch (IllegalArgumentException e) {
            }
        } else {
            zzhv.zzs.zzay().zzj().zza("Conditional property not cleared since app measurement is disabled");
        }
    }
}
