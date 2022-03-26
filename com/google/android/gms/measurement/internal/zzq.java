package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.ArrayList;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzq implements Parcelable.Creator<zzp> {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ zzp createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        boolean z = true;
        boolean z2 = false;
        long j = 0;
        String str = null;
        String str2 = "";
        long j2 = -2147483648L;
        boolean z3 = true;
        int i = 0;
        boolean z4 = false;
        long j3 = 0;
        long j4 = 0;
        long j5 = 0;
        long j6 = 0;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        Boolean bool = null;
        ArrayList<String> arrayList = null;
        String str9 = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 3:
                    str3 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 4:
                    str4 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 5:
                    str5 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 6:
                    j = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 7:
                    j3 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 8:
                    str6 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 9:
                    z = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 10:
                    z2 = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 11:
                    j2 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 12:
                    str7 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 13:
                    j4 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 14:
                    j5 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 15:
                    i = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 16:
                    z3 = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 17:
                case 20:
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
                case 18:
                    z4 = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 19:
                    str8 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 21:
                    bool = SafeParcelReader.readBooleanObject(parcel, readHeader);
                    break;
                case 22:
                    j6 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 23:
                    arrayList = SafeParcelReader.createStringList(parcel, readHeader);
                    break;
                case 24:
                    str9 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 25:
                    str2 = SafeParcelReader.createString(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzp(str, str3, str4, str5, j, j3, str6, z, z2, j2, str7, j4, j5, i, z3, z4, str8, bool, j6, arrayList, str9, str2);
    }

    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ zzp[] newArray(int i) {
        return new zzp[i];
    }
}
