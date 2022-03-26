package com.google.android.gms.common;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public final class ConnectionResult extends AbstractSafeParcelable {
    public static final int API_DISABLED;
    public static final int API_UNAVAILABLE;
    public static final int CANCELED;
    public static final int DEVELOPER_ERROR;
    @Deprecated
    public static final int DRIVE_EXTERNAL_STORAGE_REQUIRED;
    public static final int INTERNAL_ERROR;
    public static final int INTERRUPTED;
    public static final int INVALID_ACCOUNT;
    public static final int LICENSE_CHECK_FAILED;
    public static final int NETWORK_ERROR;
    public static final int RESOLUTION_ACTIVITY_NOT_FOUND;
    public static final int RESOLUTION_REQUIRED;
    public static final int RESTRICTED_PROFILE;
    public static final int SERVICE_DISABLED;
    public static final int SERVICE_INVALID;
    public static final int SERVICE_MISSING;
    public static final int SERVICE_MISSING_PERMISSION;
    public static final int SERVICE_UPDATING;
    public static final int SERVICE_VERSION_UPDATE_REQUIRED;
    public static final int SIGN_IN_FAILED;
    public static final int SIGN_IN_REQUIRED;
    public static final int SUCCESS;
    public static final int TIMEOUT;
    public static final int UNKNOWN;
    private final int zza;
    private final int zzb;
    private final PendingIntent zzc;
    private final String zzd;
    public static final ConnectionResult RESULT_SUCCESS = new ConnectionResult(0);
    public static final Parcelable.Creator<ConnectionResult> CREATOR = new zza();

    public ConnectionResult(int i, int i2, PendingIntent pendingIntent, String str) {
        this.zza = i;
        this.zzb = i2;
        this.zzc = pendingIntent;
        this.zzd = str;
    }

    public ConnectionResult(int i) {
        this(i, null, null);
    }

    public ConnectionResult(int i, PendingIntent pendingIntent) {
        this(i, pendingIntent, null);
    }

    public ConnectionResult(int i, PendingIntent pendingIntent, String str) {
        this(1, i, pendingIntent, str);
    }

    public final void startResolutionForResult(Activity activity, int i) throws IntentSender.SendIntentException {
        if (hasResolution()) {
            activity.startIntentSenderForResult(((PendingIntent) Preconditions.checkNotNull(this.zzc)).getIntentSender(), i, null, 0, 0, 0);
        }
    }

    public final boolean hasResolution() {
        return (this.zzb == 0 || this.zzc == null) ? false : true;
    }

    public final boolean isSuccess() {
        return this.zzb == 0;
    }

    public final int getErrorCode() {
        return this.zzb;
    }

    public final PendingIntent getResolution() {
        return this.zzc;
    }

    public final String getErrorMessage() {
        return this.zzd;
    }

    public static String zza(int i) {
        if (i == 99) {
            return "UNFINISHED";
        }
        if (i == 1500) {
            return "DRIVE_EXTERNAL_STORAGE_REQUIRED";
        }
        switch (i) {
            case -1:
                return "UNKNOWN";
            case 0:
                return "SUCCESS";
            case 1:
                return "SERVICE_MISSING";
            case 2:
                return "SERVICE_VERSION_UPDATE_REQUIRED";
            case 3:
                return "SERVICE_DISABLED";
            case 4:
                return "SIGN_IN_REQUIRED";
            case 5:
                return "INVALID_ACCOUNT";
            case 6:
                return "RESOLUTION_REQUIRED";
            case 7:
                return "NETWORK_ERROR";
            case 8:
                return "INTERNAL_ERROR";
            case 9:
                return "SERVICE_INVALID";
            case 10:
                return "DEVELOPER_ERROR";
            case 11:
                return "LICENSE_CHECK_FAILED";
            default:
                switch (i) {
                    case 13:
                        return "CANCELED";
                    case 14:
                        return "TIMEOUT";
                    case 15:
                        return "INTERRUPTED";
                    case 16:
                        return "API_UNAVAILABLE";
                    case 17:
                        return "SIGN_IN_FAILED";
                    case 18:
                        return "SERVICE_UPDATING";
                    case 19:
                        return "SERVICE_MISSING_PERMISSION";
                    case 20:
                        return "RESTRICTED_PROFILE";
                    case 21:
                        return "API_VERSION_UPDATE_REQUIRED";
                    case 22:
                        return "RESOLUTION_ACTIVITY_NOT_FOUND";
                    case 23:
                        return "API_DISABLED";
                    default:
                        StringBuilder sb = new StringBuilder(31);
                        sb.append("UNKNOWN_ERROR_CODE(");
                        sb.append(i);
                        sb.append(")");
                        return sb.toString();
                }
        }
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ConnectionResult)) {
            return false;
        }
        ConnectionResult connectionResult = (ConnectionResult) obj;
        if (this.zzb != connectionResult.zzb || !Objects.equal(this.zzc, connectionResult.zzc) || !Objects.equal(this.zzd, connectionResult.zzd)) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.zzb), this.zzc, this.zzd);
    }

    @Override // java.lang.Object
    public final String toString() {
        return Objects.toStringHelper(this).add("statusCode", zza(this.zzb)).add("resolution", this.zzc).add("message", this.zzd).toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zza);
        SafeParcelWriter.writeInt(parcel, 2, getErrorCode());
        SafeParcelWriter.writeParcelable(parcel, 3, getResolution(), i, false);
        SafeParcelWriter.writeString(parcel, 4, getErrorMessage(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
