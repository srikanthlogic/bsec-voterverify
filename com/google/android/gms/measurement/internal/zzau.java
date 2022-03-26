package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzau implements Parcelable.Creator<zzat> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzat zzat, Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, zzat.zza, false);
        SafeParcelWriter.writeParcelable(parcel, 3, zzat.zzb, i, false);
        SafeParcelWriter.writeString(parcel, 4, zzat.zzc, false);
        SafeParcelWriter.writeLong(parcel, 5, zzat.zzd);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ zzat createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        long j = 0;
        zzar zzar = null;
        String str2 = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            int fieldId = SafeParcelReader.getFieldId(readHeader);
            if (fieldId == 2) {
                str = SafeParcelReader.createString(parcel, readHeader);
            } else if (fieldId == 3) {
                zzar = (zzar) SafeParcelReader.createParcelable(parcel, readHeader, zzar.CREATOR);
            } else if (fieldId == 4) {
                str2 = SafeParcelReader.createString(parcel, readHeader);
            } else if (fieldId != 5) {
                SafeParcelReader.skipUnknownField(parcel, readHeader);
            } else {
                j = SafeParcelReader.readLong(parcel, readHeader);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzat(str, zzar, str2, j);
    }

    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ zzat[] newArray(int i) {
        return new zzat[i];
    }
}
