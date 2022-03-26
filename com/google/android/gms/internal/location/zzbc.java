package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.location.zzaz;
import com.google.android.gms.location.zzba;
import com.google.android.gms.location.zzbd;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public final class zzbc extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzbc> CREATOR = new zzbd();
    final int zza;
    final zzba zzb;
    final zzbd zzc;
    final PendingIntent zzd;
    final zzba zze;
    final zzai zzf;

    public zzbc(int i, zzba zzba, IBinder iBinder, PendingIntent pendingIntent, IBinder iBinder2, IBinder iBinder3) {
        zzbd zzbd;
        zzba zzba2;
        this.zza = i;
        this.zzb = zzba;
        zzai zzai = null;
        if (iBinder == null) {
            zzbd = null;
        } else {
            zzbd = com.google.android.gms.location.zzbc.zzb(iBinder);
        }
        this.zzc = zzbd;
        this.zzd = pendingIntent;
        if (iBinder2 == null) {
            zzba2 = null;
        } else {
            zzba2 = zzaz.zzb(iBinder2);
        }
        this.zze = zzba2;
        if (iBinder3 != null) {
            IInterface queryLocalInterface = iBinder3.queryLocalInterface("com.google.android.gms.location.internal.IFusedLocationProviderCallback");
            if (queryLocalInterface instanceof zzai) {
                zzai = (zzai) queryLocalInterface;
            } else {
                zzai = new zzag(iBinder3);
            }
        }
        this.zzf = zzai;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0, types: [com.google.android.gms.location.zzbd, android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r9v1, types: [android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static zzbc zza(zzbd zzbd, zzai zzai) {
        ?? r9 = zzai;
        if (zzai == null) {
            r9 = 0;
        }
        return new zzbc(2, null, zzbd, null, null, r9);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static zzbc zzb(zzba zzba, PendingIntent pendingIntent, zzai zzai) {
        return new zzbc(1, zzba, null, pendingIntent, null, zzai);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0, types: [com.google.android.gms.location.zzba, android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r9v1, types: [android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static zzbc zzc(zzba zzba, zzai zzai) {
        ?? r9 = zzai;
        if (zzai == null) {
            r9 = 0;
        }
        return new zzbc(2, null, null, null, zzba, r9);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        IBinder iBinder;
        IBinder iBinder2;
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zza);
        SafeParcelWriter.writeParcelable(parcel, 2, this.zzb, i, false);
        zzbd zzbd = this.zzc;
        IBinder iBinder3 = null;
        if (zzbd == null) {
            iBinder = null;
        } else {
            iBinder = zzbd.asBinder();
        }
        SafeParcelWriter.writeIBinder(parcel, 3, iBinder, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzd, i, false);
        zzba zzba = this.zze;
        if (zzba == null) {
            iBinder2 = null;
        } else {
            iBinder2 = zzba.asBinder();
        }
        SafeParcelWriter.writeIBinder(parcel, 5, iBinder2, false);
        zzai zzai = this.zzf;
        if (zzai != null) {
            iBinder3 = zzai.asBinder();
        }
        SafeParcelWriter.writeIBinder(parcel, 6, iBinder3, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
