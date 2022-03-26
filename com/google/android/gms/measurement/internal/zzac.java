package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzac implements Parcelable.Creator<zzab> {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ zzab createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        long j = 0;
        String str = null;
        boolean z = false;
        long j2 = 0;
        long j3 = 0;
        String str2 = null;
        zzkq zzkq = null;
        String str3 = null;
        zzat zzat = null;
        zzat zzat2 = null;
        zzat zzat3 = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 3:
                    str2 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 4:
                    zzkq = (zzkq) SafeParcelReader.createParcelable(parcel, readHeader, zzkq.CREATOR);
                    break;
                case 5:
                    j = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 6:
                    z = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 7:
                    str3 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 8:
                    zzat = (zzat) SafeParcelReader.createParcelable(parcel, readHeader, zzat.CREATOR);
                    break;
                case 9:
                    j2 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 10:
                    zzat2 = (zzat) SafeParcelReader.createParcelable(parcel, readHeader, zzat.CREATOR);
                    break;
                case 11:
                    j3 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 12:
                    zzat3 = (zzat) SafeParcelReader.createParcelable(parcel, readHeader, zzat.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzab(str, str2, zzkq, j, z, str3, zzat, j2, zzat2, j3, zzat3);
    }

    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ zzab[] newArray(int i) {
        return new zzab[i];
    }
}
