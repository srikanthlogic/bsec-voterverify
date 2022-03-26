package androidx.camera.core.impl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_DeviceProperties extends DeviceProperties {
    private final String manufacturer;
    private final String model;
    private final int sdkVersion;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_DeviceProperties(String manufacturer, String model, int sdkVersion) {
        if (manufacturer != null) {
            this.manufacturer = manufacturer;
            if (model != null) {
                this.model = model;
                this.sdkVersion = sdkVersion;
                return;
            }
            throw new NullPointerException("Null model");
        }
        throw new NullPointerException("Null manufacturer");
    }

    @Override // androidx.camera.core.impl.DeviceProperties
    public String manufacturer() {
        return this.manufacturer;
    }

    @Override // androidx.camera.core.impl.DeviceProperties
    public String model() {
        return this.model;
    }

    @Override // androidx.camera.core.impl.DeviceProperties
    public int sdkVersion() {
        return this.sdkVersion;
    }

    public String toString() {
        return "DeviceProperties{manufacturer=" + this.manufacturer + ", model=" + this.model + ", sdkVersion=" + this.sdkVersion + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceProperties)) {
            return false;
        }
        DeviceProperties that = (DeviceProperties) o;
        if (!this.manufacturer.equals(that.manufacturer()) || !this.model.equals(that.model()) || this.sdkVersion != that.sdkVersion()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.manufacturer.hashCode()) * 1000003) ^ this.model.hashCode()) * 1000003) ^ this.sdkVersion;
    }
}
