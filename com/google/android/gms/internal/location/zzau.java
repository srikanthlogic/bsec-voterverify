package com.google.android.gms.internal.location;

import android.location.Location;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.zzbc;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public final class zzau extends zzbc {
    private final ListenerHolder<LocationListener> zza;

    public zzau(ListenerHolder<LocationListener> listenerHolder) {
        this.zza = listenerHolder;
    }

    public final synchronized void zzc() {
        this.zza.clear();
    }

    @Override // com.google.android.gms.location.zzbd
    public final synchronized void zzd(Location location) {
        this.zza.notifyListener(new zzat(this, location));
    }
}
