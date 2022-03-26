package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import androidx.collection.ArrayMap;
import java.util.Iterator;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhv implements zzhe {
    private static final Map<String, zzhv> zza = new ArrayMap();
    private final SharedPreferences zzb;
    private final SharedPreferences.OnSharedPreferenceChangeListener zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzhv zza(Context context, String str) {
        zzhv zzhv;
        if (!zzgw.zza()) {
            synchronized (zzhv.class) {
                zzhv = zza.get(null);
                if (zzhv == null) {
                    StrictMode.allowThreadDiskReads();
                    throw null;
                }
            }
            return zzhv;
        }
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void zzc() {
        synchronized (zzhv.class) {
            Iterator<zzhv> it = zza.values().iterator();
            if (!it.hasNext()) {
                zza.clear();
            } else {
                zzhv next = it.next();
                SharedPreferences sharedPreferences = next.zzb;
                SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = next.zzc;
                throw null;
            }
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzhe
    public final Object zzb(String str) {
        throw null;
    }
}
