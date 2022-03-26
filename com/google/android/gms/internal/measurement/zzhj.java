package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.alcorlink.camera.AlErrorCode;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhj {
    static volatile zzhz<Boolean> zza = zzhz.zzc();
    private static final Object zzb = new Object();

    /* JADX WARN: Can't wrap try/catch for region: R(11:17|(1:19)(8:20|(1:22)(1:23)|24|(2:26|(1:28))|37|38|39|40)|29|45|30|31|(1:33)(1:34)|37|38|39|40) */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static boolean zza(Context context, Uri uri) {
        int i;
        String authority = uri.getAuthority();
        boolean z = false;
        if (!"com.google.android.gms.phenotype".equals(authority)) {
            StringBuilder sb = new StringBuilder(String.valueOf(authority).length() + 91);
            sb.append(authority);
            sb.append(" is an unsupported authority. Only com.google.android.gms.phenotype authority is supported.");
            Log.e("PhenotypeClientHelper", sb.toString());
            return false;
        } else if (zza.zzb()) {
            return zza.zza().booleanValue();
        } else {
            synchronized (zzb) {
                if (zza.zzb()) {
                    return zza.zza().booleanValue();
                }
                if (!"com.google.android.gms".equals(context.getPackageName())) {
                    PackageManager packageManager = context.getPackageManager();
                    if (Build.VERSION.SDK_INT < 29) {
                        i = 0;
                    } else {
                        i = 268435456;
                    }
                    ProviderInfo resolveContentProvider = packageManager.resolveContentProvider("com.google.android.gms.phenotype", i);
                    if (resolveContentProvider != null) {
                        if (!"com.google.android.gms".equals(resolveContentProvider.packageName)) {
                        }
                    }
                    zza = zzhz.zzd(Boolean.valueOf(z));
                    return zza.zza().booleanValue();
                }
                if ((context.getPackageManager().getApplicationInfo("com.google.android.gms", 0).flags & AlErrorCode.ERR_INVALID_PARAM) != 0) {
                    z = true;
                }
                zza = zzhz.zzd(Boolean.valueOf(z));
                return zza.zza().booleanValue();
            }
        }
    }
}
