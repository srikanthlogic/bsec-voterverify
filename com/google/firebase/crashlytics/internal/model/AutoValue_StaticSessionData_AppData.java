package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.StaticSessionData;
/* loaded from: classes3.dex */
final class AutoValue_StaticSessionData_AppData extends StaticSessionData.AppData {
    private final String appIdentifier;
    private final int deliveryMechanism;
    private final String installUuid;
    private final String unityVersion;
    private final String versionCode;
    private final String versionName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_StaticSessionData_AppData(String appIdentifier, String versionCode, String versionName, String installUuid, int deliveryMechanism, String unityVersion) {
        if (appIdentifier != null) {
            this.appIdentifier = appIdentifier;
            if (versionCode != null) {
                this.versionCode = versionCode;
                if (versionName != null) {
                    this.versionName = versionName;
                    if (installUuid != null) {
                        this.installUuid = installUuid;
                        this.deliveryMechanism = deliveryMechanism;
                        this.unityVersion = unityVersion;
                        return;
                    }
                    throw new NullPointerException("Null installUuid");
                }
                throw new NullPointerException("Null versionName");
            }
            throw new NullPointerException("Null versionCode");
        }
        throw new NullPointerException("Null appIdentifier");
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.AppData
    public String appIdentifier() {
        return this.appIdentifier;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.AppData
    public String versionCode() {
        return this.versionCode;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.AppData
    public String versionName() {
        return this.versionName;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.AppData
    public String installUuid() {
        return this.installUuid;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.AppData
    public int deliveryMechanism() {
        return this.deliveryMechanism;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.AppData
    public String unityVersion() {
        return this.unityVersion;
    }

    public String toString() {
        return "AppData{appIdentifier=" + this.appIdentifier + ", versionCode=" + this.versionCode + ", versionName=" + this.versionName + ", installUuid=" + this.installUuid + ", deliveryMechanism=" + this.deliveryMechanism + ", unityVersion=" + this.unityVersion + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StaticSessionData.AppData)) {
            return false;
        }
        StaticSessionData.AppData that = (StaticSessionData.AppData) o;
        if (this.appIdentifier.equals(that.appIdentifier()) && this.versionCode.equals(that.versionCode()) && this.versionName.equals(that.versionName()) && this.installUuid.equals(that.installUuid()) && this.deliveryMechanism == that.deliveryMechanism()) {
            String str = this.unityVersion;
            if (str == null) {
                if (that.unityVersion() == null) {
                    return true;
                }
            } else if (str.equals(that.unityVersion())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int h$ = ((((((((((1 * 1000003) ^ this.appIdentifier.hashCode()) * 1000003) ^ this.versionCode.hashCode()) * 1000003) ^ this.versionName.hashCode()) * 1000003) ^ this.installUuid.hashCode()) * 1000003) ^ this.deliveryMechanism) * 1000003;
        String str = this.unityVersion;
        return h$ ^ (str == null ? 0 : str.hashCode());
    }
}
