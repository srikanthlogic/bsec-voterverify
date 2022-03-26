package com.google.android.gms.measurement.internal;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.google.firebase.analytics.FirebaseAnalytics;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzr {
    private final zzfs zza;

    public zzr(zzfs zzfs) {
        this.zza = zzfs;
    }

    public final void zza(String str, Bundle bundle) {
        String str2;
        this.zza.zzaz().zzg();
        if (!this.zza.zzJ()) {
            if (bundle.isEmpty()) {
                str2 = null;
            } else {
                if (true == str.isEmpty()) {
                    str = DebugKt.DEBUG_PROPERTY_VALUE_AUTO;
                }
                Uri.Builder builder = new Uri.Builder();
                builder.path(str);
                for (String str3 : bundle.keySet()) {
                    builder.appendQueryParameter(str3, bundle.getString(str3));
                }
                str2 = builder.build().toString();
            }
            if (!TextUtils.isEmpty(str2)) {
                this.zza.zzm().zzp.zzb(str2);
                this.zza.zzm().zzq.zzb(this.zza.zzav().currentTimeMillis());
            }
        }
    }

    public final void zzb() {
        String str;
        this.zza.zzaz().zzg();
        if (zzd()) {
            if (zze()) {
                this.zza.zzm().zzp.zzb(null);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.SOURCE, "(not set)");
                bundle.putString(FirebaseAnalytics.Param.MEDIUM, "(not set)");
                bundle.putString("_cis", "intent");
                bundle.putLong("_cc", 1);
                this.zza.zzq().zzF(DebugKt.DEBUG_PROPERTY_VALUE_AUTO, "_cmpx", bundle);
            } else {
                String zza = this.zza.zzm().zzp.zza();
                if (TextUtils.isEmpty(zza)) {
                    this.zza.zzay().zzh().zza("Cache still valid but referrer not found");
                } else {
                    long zza2 = ((this.zza.zzm().zzq.zza() / 3600000) - 1) * 3600000;
                    Uri parse = Uri.parse(zza);
                    Bundle bundle2 = new Bundle();
                    Pair pair = new Pair(parse.getPath(), bundle2);
                    for (String str2 : parse.getQueryParameterNames()) {
                        bundle2.putString(str2, parse.getQueryParameter(str2));
                    }
                    ((Bundle) pair.second).putLong("_cc", zza2);
                    if (pair.first == null) {
                        str = "app";
                    } else {
                        str = (String) pair.first;
                    }
                    this.zza.zzq().zzF(str, "_cmp", (Bundle) pair.second);
                }
                this.zza.zzm().zzp.zzb(null);
            }
            this.zza.zzm().zzq.zzb(0);
        }
    }

    public final void zzc() {
        if (zzd() && zze()) {
            this.zza.zzm().zzp.zzb(null);
        }
    }

    final boolean zzd() {
        return this.zza.zzm().zzq.zza() > 0;
    }

    final boolean zze() {
        if (zzd() && this.zza.zzav().currentTimeMillis() - this.zza.zzm().zzq.zza() > this.zza.zzf().zzi(null, zzdw.zzQ)) {
            return true;
        }
        return false;
    }
}
