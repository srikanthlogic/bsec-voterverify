package com.google.android.gms.location;

import android.content.Intent;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public final class LocationResult extends AbstractSafeParcelable implements ReflectedParcelable {
    private final List<Location> zzb;
    static final List<Location> zza = Collections.emptyList();
    public static final Parcelable.Creator<LocationResult> CREATOR = new zzbg();

    public LocationResult(List<Location> list) {
        this.zzb = list;
    }

    public static LocationResult create(List<Location> list) {
        if (list == null) {
            list = zza;
        }
        return new LocationResult(list);
    }

    public static LocationResult extractResult(Intent intent) {
        if (!hasResult(intent)) {
            return null;
        }
        return (LocationResult) intent.getParcelableExtra("com.google.android.gms.location.EXTRA_LOCATION_RESULT");
    }

    public static boolean hasResult(Intent intent) {
        if (intent == null) {
            return false;
        }
        return intent.hasExtra("com.google.android.gms.location.EXTRA_LOCATION_RESULT");
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (!(obj instanceof LocationResult)) {
            return false;
        }
        LocationResult locationResult = (LocationResult) obj;
        if (locationResult.zzb.size() != this.zzb.size()) {
            return false;
        }
        Iterator<Location> it = this.zzb.iterator();
        for (Location location : locationResult.zzb) {
            if (it.next().getTime() != location.getTime()) {
                return false;
            }
        }
        return true;
    }

    public Location getLastLocation() {
        int size = this.zzb.size();
        if (size == 0) {
            return null;
        }
        return this.zzb.get(size - 1);
    }

    public List<Location> getLocations() {
        return this.zzb;
    }

    @Override // java.lang.Object
    public int hashCode() {
        int i = 17;
        for (Location location : this.zzb) {
            long time = location.getTime();
            i = (i * 31) + ((int) (time ^ (time >>> 32)));
        }
        return i;
    }

    @Override // java.lang.Object
    public String toString() {
        String valueOf = String.valueOf(this.zzb);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
        sb.append("LocationResult[locations: ");
        sb.append(valueOf);
        sb.append("]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeTypedList(parcel, 1, getLocations(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
