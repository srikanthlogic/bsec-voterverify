package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.StaticSessionData;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class AutoValue_StaticSessionData extends StaticSessionData {
    private final StaticSessionData.AppData appData;
    private final StaticSessionData.DeviceData deviceData;
    private final StaticSessionData.OsData osData;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_StaticSessionData(StaticSessionData.AppData appData, StaticSessionData.OsData osData, StaticSessionData.DeviceData deviceData) {
        if (appData != null) {
            this.appData = appData;
            if (osData != null) {
                this.osData = osData;
                if (deviceData != null) {
                    this.deviceData = deviceData;
                    return;
                }
                throw new NullPointerException("Null deviceData");
            }
            throw new NullPointerException("Null osData");
        }
        throw new NullPointerException("Null appData");
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData
    public StaticSessionData.AppData appData() {
        return this.appData;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData
    public StaticSessionData.OsData osData() {
        return this.osData;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData
    public StaticSessionData.DeviceData deviceData() {
        return this.deviceData;
    }

    public String toString() {
        return "StaticSessionData{appData=" + this.appData + ", osData=" + this.osData + ", deviceData=" + this.deviceData + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StaticSessionData)) {
            return false;
        }
        StaticSessionData that = (StaticSessionData) o;
        if (!this.appData.equals(that.appData()) || !this.osData.equals(that.osData()) || !this.deviceData.equals(that.deviceData())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.appData.hashCode()) * 1000003) ^ this.osData.hashCode()) * 1000003) ^ this.deviceData.hashCode();
    }
}
