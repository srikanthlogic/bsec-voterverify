package com.google.firebase.crashlytics.internal.model;
/* loaded from: classes3.dex */
public abstract class StaticSessionData {
    public abstract AppData appData();

    public abstract DeviceData deviceData();

    public abstract OsData osData();

    public static StaticSessionData create(AppData appData, OsData osData, DeviceData deviceData) {
        return new AutoValue_StaticSessionData(appData, osData, deviceData);
    }

    /* loaded from: classes3.dex */
    public static abstract class AppData {
        public abstract String appIdentifier();

        public abstract int deliveryMechanism();

        public abstract String installUuid();

        public abstract String unityVersion();

        public abstract String versionCode();

        public abstract String versionName();

        public static AppData create(String appIdentifier, String versionCode, String versionName, String installUuid, int deliveryMechanism, String unityVersion) {
            return new AutoValue_StaticSessionData_AppData(appIdentifier, versionCode, versionName, installUuid, deliveryMechanism, unityVersion);
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class OsData {
        public abstract boolean isRooted();

        public abstract String osCodeName();

        public abstract String osRelease();

        public static OsData create(String osRelease, String osCodeName, boolean isRooted) {
            return new AutoValue_StaticSessionData_OsData(osRelease, osCodeName, isRooted);
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class DeviceData {
        public abstract int arch();

        public abstract int availableProcessors();

        public abstract long diskSpace();

        public abstract boolean isEmulator();

        public abstract String manufacturer();

        public abstract String model();

        public abstract String modelClass();

        public abstract int state();

        public abstract long totalRam();

        public static DeviceData create(int arch, String model, int availableProcessors, long totalRam, long diskSpace, boolean isEmulator, int state, String manufacturer, String modelClass) {
            return new AutoValue_StaticSessionData_DeviceData(arch, model, availableProcessors, totalRam, diskSpace, isEmulator, state, manufacturer, modelClass);
        }
    }
}
