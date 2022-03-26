package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public class ActivityTransition extends AbstractSafeParcelable {
    public static final int ACTIVITY_TRANSITION_ENTER;
    public static final int ACTIVITY_TRANSITION_EXIT;
    public static final Parcelable.Creator<ActivityTransition> CREATOR = new zzl();
    private final int zza;
    private final int zzb;

    /* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SupportedActivityTransition {
    }

    public ActivityTransition(int i, int i2) {
        this.zza = i;
        this.zzb = i2;
    }

    public static void zza(int i) {
        boolean z = false;
        if (i >= 0 && i <= 1) {
            z = true;
        }
        StringBuilder sb = new StringBuilder(41);
        sb.append("Transition type ");
        sb.append(i);
        sb.append(" is not valid.");
        Preconditions.checkArgument(z, sb.toString());
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ActivityTransition)) {
            return false;
        }
        ActivityTransition activityTransition = (ActivityTransition) obj;
        return this.zza == activityTransition.zza && this.zzb == activityTransition.zzb;
    }

    public int getActivityType() {
        return this.zza;
    }

    public int getTransitionType() {
        return this.zzb;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.zza), Integer.valueOf(this.zzb));
    }

    @Override // java.lang.Object
    public String toString() {
        int i = this.zza;
        int i2 = this.zzb;
        StringBuilder sb = new StringBuilder(75);
        sb.append("ActivityTransition [mActivityType=");
        sb.append(i);
        sb.append(", mTransitionType=");
        sb.append(i2);
        sb.append(']');
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Preconditions.checkNotNull(parcel);
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, getActivityType());
        SafeParcelWriter.writeInt(parcel, 2, getTransitionType());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    /* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
    /* loaded from: classes.dex */
    public static class Builder {
        private int zza = -1;
        private int zzb = -1;

        public Builder setActivityTransition(int i) {
            ActivityTransition.zza(i);
            this.zzb = i;
            return this;
        }

        public Builder setActivityType(int i) {
            this.zza = i;
            return this;
        }

        public ActivityTransition build() {
            boolean z = true;
            Preconditions.checkState(this.zza != -1, "Activity type not set.");
            if (this.zzb == -1) {
                z = false;
            }
            Preconditions.checkState(z, "Activity transition type not set.");
            return new ActivityTransition(this.zza, this.zzb);
        }
    }
}
