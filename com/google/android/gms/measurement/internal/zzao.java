package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Iterator;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzao {
    final String zza;
    final String zzb;
    final String zzc;
    final long zzd;
    final long zze;
    final zzar zzf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzao(zzfs zzfs, String str, String str2, String str3, long j, long j2, Bundle bundle) {
        zzar zzar;
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotEmpty(str3);
        this.zza = str2;
        this.zzb = str3;
        this.zzc = true == TextUtils.isEmpty(str) ? null : str;
        this.zzd = j;
        this.zze = j2;
        if (j2 != 0 && j2 > j) {
            zzfs.zzay().zzk().zzb("Event created with reverse previous/current timestamps. appId", zzei.zzn(str2));
        }
        if (bundle == null || bundle.isEmpty()) {
            zzar = new zzar(new Bundle());
        } else {
            Bundle bundle2 = new Bundle(bundle);
            Iterator<String> it = bundle2.keySet().iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next == null) {
                    zzfs.zzay().zzd().zza("Param name can't be null");
                    it.remove();
                } else {
                    Object zzA = zzfs.zzv().zzA(next, bundle2.get(next));
                    if (zzA == null) {
                        zzfs.zzay().zzk().zzb("Param value can't be null", zzfs.zzj().zzd(next));
                        it.remove();
                    } else {
                        zzfs.zzv().zzN(bundle2, next, zzA);
                    }
                }
            }
            zzar = new zzar(bundle2);
        }
        this.zzf = zzar;
    }

    public final String toString() {
        String str = this.zza;
        String str2 = this.zzb;
        String valueOf = String.valueOf(this.zzf);
        int length = String.valueOf(str).length();
        StringBuilder sb = new StringBuilder(length + 33 + String.valueOf(str2).length() + String.valueOf(valueOf).length());
        sb.append("Event{appId='");
        sb.append(str);
        sb.append("', name='");
        sb.append(str2);
        sb.append("', params=");
        sb.append(valueOf);
        sb.append('}');
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzao zza(zzfs zzfs, long j) {
        return new zzao(zzfs, this.zzc, this.zza, this.zzb, this.zzd, j, this.zzf);
    }

    private zzao(zzfs zzfs, String str, String str2, String str3, long j, long j2, zzar zzar) {
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotEmpty(str3);
        Preconditions.checkNotNull(zzar);
        this.zza = str2;
        this.zzb = str3;
        this.zzc = true == TextUtils.isEmpty(str) ? null : str;
        this.zzd = j;
        this.zze = j2;
        if (j2 != 0 && j2 > j) {
            zzfs.zzay().zzk().zzc("Event created with reverse previous/current timestamps. appId, name", zzei.zzn(str2), zzei.zzn(str3));
        }
        this.zzf = zzar;
    }
}
