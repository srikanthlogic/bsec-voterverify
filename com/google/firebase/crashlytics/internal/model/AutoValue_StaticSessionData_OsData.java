package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.StaticSessionData;
/* loaded from: classes3.dex */
final class AutoValue_StaticSessionData_OsData extends StaticSessionData.OsData {
    private final boolean isRooted;
    private final String osCodeName;
    private final String osRelease;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_StaticSessionData_OsData(String osRelease, String osCodeName, boolean isRooted) {
        if (osRelease != null) {
            this.osRelease = osRelease;
            if (osCodeName != null) {
                this.osCodeName = osCodeName;
                this.isRooted = isRooted;
                return;
            }
            throw new NullPointerException("Null osCodeName");
        }
        throw new NullPointerException("Null osRelease");
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.OsData
    public String osRelease() {
        return this.osRelease;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.OsData
    public String osCodeName() {
        return this.osCodeName;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.OsData
    public boolean isRooted() {
        return this.isRooted;
    }

    public String toString() {
        return "OsData{osRelease=" + this.osRelease + ", osCodeName=" + this.osCodeName + ", isRooted=" + this.isRooted + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StaticSessionData.OsData)) {
            return false;
        }
        StaticSessionData.OsData that = (StaticSessionData.OsData) o;
        if (!this.osRelease.equals(that.osRelease()) || !this.osCodeName.equals(that.osCodeName()) || this.isRooted != that.isRooted()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.osRelease.hashCode()) * 1000003) ^ this.osCodeName.hashCode()) * 1000003) ^ (this.isRooted ? 1231 : 1237);
    }
}
