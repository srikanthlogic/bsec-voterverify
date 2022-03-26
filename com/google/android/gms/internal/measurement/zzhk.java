package com.google.android.gms.internal.measurement;

import android.net.Uri;
import androidx.collection.ArrayMap;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhk {
    private static final ArrayMap<String, Uri> zza = new ArrayMap<>();

    public static synchronized Uri zza(String str) {
        Uri uri;
        synchronized (zzhk.class) {
            uri = zza.get("com.google.android.gms.measurement");
            if (uri == null) {
                String valueOf = String.valueOf(Uri.encode("com.google.android.gms.measurement"));
                uri = Uri.parse(valueOf.length() != 0 ? "content://com.google.android.gms.phenotype/".concat(valueOf) : new String("content://com.google.android.gms.phenotype/"));
                zza.put("com.google.android.gms.measurement", uri);
            }
        }
        return uri;
    }
}
