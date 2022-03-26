package com.google.android.gms.internal.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ClientIdentity;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.location.LocationRequest;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public final class zzbb implements Parcelable.Creator<zzba> {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ zzba createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        List<ClientIdentity> list = zzba.zza;
        LocationRequest locationRequest = null;
        boolean z = false;
        long j = Long.MAX_VALUE;
        String str = null;
        String str2 = null;
        String str3 = null;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            int fieldId = SafeParcelReader.getFieldId(readHeader);
            if (fieldId != 1) {
                switch (fieldId) {
                    case 5:
                        list = SafeParcelReader.createTypedList(parcel, readHeader, ClientIdentity.CREATOR);
                        continue;
                    case 6:
                        str = SafeParcelReader.createString(parcel, readHeader);
                        continue;
                    case 7:
                        z = SafeParcelReader.readBoolean(parcel, readHeader);
                        continue;
                    case 8:
                        z2 = SafeParcelReader.readBoolean(parcel, readHeader);
                        continue;
                    case 9:
                        z3 = SafeParcelReader.readBoolean(parcel, readHeader);
                        continue;
                    case 10:
                        str2 = SafeParcelReader.createString(parcel, readHeader);
                        continue;
                    case 11:
                        z4 = SafeParcelReader.readBoolean(parcel, readHeader);
                        continue;
                    case 12:
                        z5 = SafeParcelReader.readBoolean(parcel, readHeader);
                        continue;
                    case 13:
                        str3 = SafeParcelReader.createString(parcel, readHeader);
                        continue;
                    case 14:
                        j = SafeParcelReader.readLong(parcel, readHeader);
                        continue;
                    default:
                        SafeParcelReader.skipUnknownField(parcel, readHeader);
                        continue;
                }
            } else {
                locationRequest = (LocationRequest) SafeParcelReader.createParcelable(parcel, readHeader, LocationRequest.CREATOR);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzba(locationRequest, list, str, z, z2, z3, str2, z4, z5, str3, j);
    }

    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ zzba[] newArray(int i) {
        return new zzba[i];
    }
}
