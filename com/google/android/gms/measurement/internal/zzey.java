package com.google.android.gms.measurement.internal;

import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.internal.measurement.zzbr;
import com.google.firebase.analytics.FirebaseAnalytics;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
final class zzey implements Runnable {
    final /* synthetic */ zzbr zza;
    final /* synthetic */ ServiceConnection zzb;
    final /* synthetic */ zzez zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzey(zzez zzez, zzbr zzbr, ServiceConnection serviceConnection) {
        this.zzc = zzez;
        this.zza = zzbr;
        this.zzb = serviceConnection;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        zzez zzez = this.zzc;
        zzfa zzfa = zzez.zza;
        String str2 = zzez.zzb;
        zzbr zzbr = this.zza;
        ServiceConnection serviceConnection = this.zzb;
        zzfa.zza.zzaz().zzg();
        Bundle bundle = new Bundle();
        bundle.putString("package_name", str2);
        Bundle bundle2 = null;
        try {
            Bundle zzd = zzbr.zzd(bundle);
            if (zzd == null) {
                zzfa.zza.zzay().zzd().zza("Install Referrer Service returned a null response");
            } else {
                bundle2 = zzd;
            }
        } catch (Exception e) {
            zzfa.zza.zzay().zzd().zzb("Exception occurred while retrieving the Install Referrer", e.getMessage());
        }
        zzfa.zza.zzaz().zzg();
        zzfs.zzO();
        if (bundle2 != null) {
            long j = bundle2.getLong("install_begin_timestamp_seconds", 0) * 1000;
            if (j == 0) {
                zzfa.zza.zzay().zzk().zza("Service response is missing Install Referrer install timestamp");
            } else {
                String string = bundle2.getString("install_referrer");
                if (string == null || string.isEmpty()) {
                    zzfa.zza.zzay().zzd().zza("No referrer defined in Install Referrer response");
                } else {
                    zzfa.zza.zzay().zzj().zzb("InstallReferrer API result", string);
                    zzku zzv = zzfa.zza.zzv();
                    if (string.length() != 0) {
                        str = "?".concat(string);
                    } else {
                        str = new String("?");
                    }
                    Bundle zzs = zzv.zzs(Uri.parse(str));
                    if (zzs == null) {
                        zzfa.zza.zzay().zzd().zza("No campaign params defined in Install Referrer result");
                    } else {
                        String string2 = zzs.getString(FirebaseAnalytics.Param.MEDIUM);
                        if (string2 != null && !"(not set)".equalsIgnoreCase(string2) && !"organic".equalsIgnoreCase(string2)) {
                            long j2 = bundle2.getLong("referrer_click_timestamp_seconds", 0) * 1000;
                            if (j2 == 0) {
                                zzfa.zza.zzay().zzd().zza("Install Referrer is missing click timestamp for ad campaign");
                            } else {
                                zzs.putLong("click_timestamp", j2);
                            }
                        }
                        if (j == zzfa.zza.zzm().zzd.zza()) {
                            zzfa.zza.zzay().zzj().zza("Logging Install Referrer campaign from module while it may have already been logged.");
                        }
                        if (zzfa.zza.zzJ()) {
                            zzfa.zza.zzm().zzd.zzb(j);
                            zzfa.zza.zzay().zzj().zzb("Logging Install Referrer campaign from gmscore with ", "referrer API v2");
                            zzs.putString("_cis", "referrer API v2");
                            zzfa.zza.zzq().zzE(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_cmp", zzs, str2);
                        }
                    }
                }
            }
        }
        ConnectionTracker.getInstance().unbindService(zzfa.zza.zzau(), serviceConnection);
    }
}
