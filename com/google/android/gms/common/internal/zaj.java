package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.SparseIntArray;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaj {
    private final SparseIntArray zaa;
    private GoogleApiAvailabilityLight zab;

    public zaj() {
        this(GoogleApiAvailability.getInstance());
    }

    public zaj(GoogleApiAvailabilityLight googleApiAvailabilityLight) {
        this.zaa = new SparseIntArray();
        Preconditions.checkNotNull(googleApiAvailabilityLight);
        this.zab = googleApiAvailabilityLight;
    }

    public final int zaa(Context context, Api.Client client) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(client);
        if (!client.requiresGooglePlayServices()) {
            return 0;
        }
        int minApkVersion = client.getMinApkVersion();
        int zaa = zaa(context, minApkVersion);
        if (zaa != -1) {
            return zaa;
        }
        int i = 0;
        while (true) {
            if (i < this.zaa.size()) {
                int keyAt = this.zaa.keyAt(i);
                if (keyAt > minApkVersion && this.zaa.get(keyAt) == 0) {
                    zaa = 0;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (zaa == -1) {
            zaa = this.zab.isGooglePlayServicesAvailable(context, minApkVersion);
        }
        this.zaa.put(minApkVersion, zaa);
        return zaa;
    }

    public final int zaa(Context context, int i) {
        return this.zaa.get(i, -1);
    }

    public final void zaa() {
        this.zaa.clear();
    }
}
