package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.StaticSessionData;
/* loaded from: classes3.dex */
final class AutoValue_StaticSessionData_DeviceData extends StaticSessionData.DeviceData {
    private final int arch;
    private final int availableProcessors;
    private final long diskSpace;
    private final boolean isEmulator;
    private final String manufacturer;
    private final String model;
    private final String modelClass;
    private final int state;
    private final long totalRam;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_StaticSessionData_DeviceData(int arch, String model, int availableProcessors, long totalRam, long diskSpace, boolean isEmulator, int state, String manufacturer, String modelClass) {
        this.arch = arch;
        if (model != null) {
            this.model = model;
            this.availableProcessors = availableProcessors;
            this.totalRam = totalRam;
            this.diskSpace = diskSpace;
            this.isEmulator = isEmulator;
            this.state = state;
            if (manufacturer != null) {
                this.manufacturer = manufacturer;
                if (modelClass != null) {
                    this.modelClass = modelClass;
                    return;
                }
                throw new NullPointerException("Null modelClass");
            }
            throw new NullPointerException("Null manufacturer");
        }
        throw new NullPointerException("Null model");
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public int arch() {
        return this.arch;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public String model() {
        return this.model;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public int availableProcessors() {
        return this.availableProcessors;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public long totalRam() {
        return this.totalRam;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public long diskSpace() {
        return this.diskSpace;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public boolean isEmulator() {
        return this.isEmulator;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public int state() {
        return this.state;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public String manufacturer() {
        return this.manufacturer;
    }

    @Override // com.google.firebase.crashlytics.internal.model.StaticSessionData.DeviceData
    public String modelClass() {
        return this.modelClass;
    }

    public String toString() {
        return "DeviceData{arch=" + this.arch + ", model=" + this.model + ", availableProcessors=" + this.availableProcessors + ", totalRam=" + this.totalRam + ", diskSpace=" + this.diskSpace + ", isEmulator=" + this.isEmulator + ", state=" + this.state + ", manufacturer=" + this.manufacturer + ", modelClass=" + this.modelClass + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StaticSessionData.DeviceData)) {
            return false;
        }
        StaticSessionData.DeviceData that = (StaticSessionData.DeviceData) o;
        if (this.arch == that.arch() && this.model.equals(that.model()) && this.availableProcessors == that.availableProcessors() && this.totalRam == that.totalRam() && this.diskSpace == that.diskSpace() && this.isEmulator == that.isEmulator() && this.state == that.state() && this.manufacturer.equals(that.manufacturer()) && this.modelClass.equals(that.modelClass())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long j = this.totalRam;
        long j2 = this.diskSpace;
        return (((((((((((((((((1 * 1000003) ^ this.arch) * 1000003) ^ this.model.hashCode()) * 1000003) ^ this.availableProcessors) * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003) ^ ((int) (j2 ^ (j2 >>> 32)))) * 1000003) ^ (this.isEmulator ? 1231 : 1237)) * 1000003) ^ this.state) * 1000003) ^ this.manufacturer.hashCode()) * 1000003) ^ this.modelClass.hashCode();
    }
}
