package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import com.google.android.gms.common.internal.Preconditions;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzew {
    final /* synthetic */ zzex zza;
    private final String zzb;
    private boolean zzc;
    private String zzd;

    public zzew(zzex zzex, String str, String str2) {
        this.zza = zzex;
        Preconditions.checkNotEmpty(str);
        this.zzb = str;
    }

    public final String zza() {
        if (!this.zzc) {
            this.zzc = true;
            this.zzd = this.zza.zza().getString(this.zzb, null);
        }
        return this.zzd;
    }

    public final void zzb(String str) {
        SharedPreferences.Editor edit = this.zza.zza().edit();
        edit.putString(this.zzb, str);
        edit.apply();
        this.zzd = str;
    }
}
