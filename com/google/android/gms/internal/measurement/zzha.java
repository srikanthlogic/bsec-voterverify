package com.google.android.gms.internal.measurement;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import androidx.collection.ArrayMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzha implements zzhe {
    private final ContentResolver zzc;
    private final Uri zzd;
    private volatile Map<String, String> zzg;
    private static final Map<Uri, zzha> zzb = new ArrayMap();
    public static final String[] zza = {"key", "value"};
    private final ContentObserver zze = new zzgz(this, null);
    private final Object zzf = new Object();
    private final List<zzhb> zzh = new ArrayList();

    private zzha(ContentResolver contentResolver, Uri uri) {
        if (contentResolver == null) {
            throw null;
        } else if (uri != null) {
            this.zzc = contentResolver;
            this.zzd = uri;
            contentResolver.registerContentObserver(uri, false, this.zze);
        } else {
            throw null;
        }
    }

    public static zzha zza(ContentResolver contentResolver, Uri uri) {
        zzha zzha;
        synchronized (zzha.class) {
            zzha = zzb.get(uri);
            if (zzha == null) {
                try {
                    zzha zzha2 = new zzha(contentResolver, uri);
                    try {
                        zzb.put(uri, zzha2);
                    } catch (SecurityException e) {
                    }
                    zzha = zzha2;
                } catch (SecurityException e2) {
                }
            }
        }
        return zzha;
    }

    public static synchronized void zze() {
        synchronized (zzha.class) {
            for (zzha zzha : zzb.values()) {
                zzha.zzc.unregisterContentObserver(zzha.zze);
            }
            zzb.clear();
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzhe
    public final /* bridge */ /* synthetic */ Object zzb(String str) {
        return zzc().get(str);
    }

    public final Map<String, String> zzc() {
        Map<String, String> map;
        Map<String, String> map2 = this.zzg;
        if (map2 == null) {
            synchronized (this.zzf) {
                map2 = this.zzg;
                if (map2 == null) {
                    StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
                    try {
                        map = (Map) zzhc.zza(new zzhd() { // from class: com.google.android.gms.internal.measurement.zzgy
                            @Override // com.google.android.gms.internal.measurement.zzhd
                            public final Object zza() {
                                return zzha.this.zzd();
                            }
                        });
                        StrictMode.setThreadPolicy(allowThreadDiskReads);
                    } catch (SQLiteException | IllegalStateException | SecurityException e) {
                        Log.e("ConfigurationContentLoader", "PhenotypeFlag unable to load ContentProvider, using default values");
                        StrictMode.setThreadPolicy(allowThreadDiskReads);
                        map = null;
                    }
                    this.zzg = map;
                    map2 = map;
                }
            }
        }
        if (map2 != null) {
            return map2;
        }
        return Collections.emptyMap();
    }

    public final /* synthetic */ Map zzd() {
        Map map;
        Cursor query = this.zzc.query(this.zzd, zza, null, null, null);
        if (query == null) {
            return Collections.emptyMap();
        }
        try {
            int count = query.getCount();
            if (count == 0) {
                return Collections.emptyMap();
            }
            if (count <= 256) {
                map = new ArrayMap(count);
            } else {
                map = new HashMap(count, 1.0f);
            }
            while (query.moveToNext()) {
                map.put(query.getString(0), query.getString(1));
            }
            return map;
        } finally {
            query.close();
        }
    }

    public final void zzf() {
        synchronized (this.zzf) {
            this.zzg = null;
            zzhu.zze();
        }
        synchronized (this) {
            for (zzhb zzhb : this.zzh) {
                zzhb.zza();
            }
        }
    }
}
