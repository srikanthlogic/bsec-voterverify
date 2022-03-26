package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.database.ContentObserver;
import android.util.Log;
import androidx.core.content.PermissionChecker;
import javax.annotation.Nullable;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhh implements zzhe {
    private static zzhh zza;
    @Nullable
    private final Context zzb;
    @Nullable
    private final ContentObserver zzc;

    private zzhh() {
        this.zzb = null;
        this.zzc = null;
    }

    private zzhh(Context context) {
        this.zzb = context;
        this.zzc = new zzhg(this, null);
        context.getContentResolver().registerContentObserver(zzgv.zza, true, this.zzc);
    }

    public static zzhh zza(Context context) {
        zzhh zzhh;
        synchronized (zzhh.class) {
            if (zza == null) {
                zza = PermissionChecker.checkSelfPermission(context, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0 ? new zzhh(context) : new zzhh();
            }
            zzhh = zza;
        }
        return zzhh;
    }

    public static synchronized void zze() {
        synchronized (zzhh.class) {
            if (!(zza == null || zza.zzb == null || zza.zzc == null)) {
                zza.zzb.getContentResolver().unregisterContentObserver(zza.zzc);
            }
            zza = null;
        }
    }

    /* renamed from: zzc */
    public final String zzb(String str) {
        if (this.zzb == null) {
            return null;
        }
        try {
            return (String) zzhc.zza(new zzhd(str) { // from class: com.google.android.gms.internal.measurement.zzhf
                public final /* synthetic */ String zzb;

                {
                    this.zzb = r2;
                }

                @Override // com.google.android.gms.internal.measurement.zzhd
                public final Object zza() {
                    return zzhh.this.zzd(this.zzb);
                }
            });
        } catch (IllegalStateException | SecurityException e) {
            String valueOf = String.valueOf(str);
            Log.e("GservicesLoader", valueOf.length() != 0 ? "Unable to read GServices for: ".concat(valueOf) : new String("Unable to read GServices for: "), e);
            return null;
        }
    }

    public final /* synthetic */ String zzd(String str) {
        return zzgv.zza(this.zzb.getContentResolver(), str, null);
    }
}
