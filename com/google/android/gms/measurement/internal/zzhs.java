package com.google.android.gms.measurement.internal;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
final class zzhs implements Runnable {
    final /* synthetic */ boolean zza;
    final /* synthetic */ Uri zzb;
    final /* synthetic */ String zzc;
    final /* synthetic */ String zzd;
    final /* synthetic */ zzhu zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhs(zzhu zzhu, boolean z, Uri uri, String str, String str2) {
        this.zze = zzhu;
        this.zza = z;
        this.zzb = uri;
        this.zzc = str;
        this.zzd = str2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Bundle bundle;
        Bundle zzs;
        zzhu zzhu = this.zze;
        boolean z = this.zza;
        Uri uri = this.zzb;
        String str = this.zzc;
        String str2 = this.zzd;
        zzhu.zza.zzg();
        try {
            zzku zzv = zzhu.zza.zzs.zzv();
            if (TextUtils.isEmpty(str2)) {
                bundle = null;
            } else if (str2.contains("gclid") || str2.contains("utm_campaign") || str2.contains("utm_source") || str2.contains("utm_medium")) {
                String valueOf = String.valueOf(str2);
                bundle = zzv.zzs(Uri.parse(valueOf.length() != 0 ? "https://google.com/search?".concat(valueOf) : new String("https://google.com/search?")));
                if (bundle != null) {
                    bundle.putString("_cis", "referrer");
                }
            } else {
                zzv.zzs.zzay().zzc().zza("Activity created with data 'referrer' without required params");
                bundle = null;
            }
            if (z && (zzs = zzhu.zza.zzs.zzv().zzs(uri)) != null) {
                zzs.putString("_cis", "intent");
                if (!zzs.containsKey("gclid") && bundle != null && bundle.containsKey("gclid")) {
                    zzs.putString("_cer", String.format("gclid=%s", bundle.getString("gclid")));
                }
                zzhu.zza.zzF(str, "_cmp", zzs);
                zzhu.zza.zzb.zza(str, zzs);
            }
            if (!TextUtils.isEmpty(str2)) {
                zzhu.zza.zzs.zzay().zzc().zzb("Activity created with referrer", str2);
                if (zzhu.zza.zzs.zzf().zzs(null, zzdw.zzaa)) {
                    if (bundle != null) {
                        zzhu.zza.zzF(str, "_cmp", bundle);
                        zzhu.zza.zzb.zza(str, bundle);
                    } else {
                        zzhu.zza.zzs.zzay().zzc().zzb("Referrer does not contain valid parameters", str2);
                    }
                    zzhu.zza.zzV(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_ldl", null, true);
                } else if (!str2.contains("gclid") || (!str2.contains("utm_campaign") && !str2.contains("utm_source") && !str2.contains("utm_medium") && !str2.contains("utm_term") && !str2.contains("utm_content"))) {
                    zzhu.zza.zzs.zzay().zzc().zza("Activity created with data 'referrer' without required params");
                } else if (!TextUtils.isEmpty(str2)) {
                    zzhu.zza.zzV(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_ldl", str2, true);
                }
            }
        } catch (RuntimeException e) {
            zzhu.zza.zzs.zzay().zzd().zzb("Throwable caught in handleReferrerForOnActivityCreated", e);
        }
    }
}
