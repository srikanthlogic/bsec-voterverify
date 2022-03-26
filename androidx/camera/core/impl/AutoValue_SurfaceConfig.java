package androidx.camera.core.impl;

import androidx.camera.core.impl.SurfaceConfig;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_SurfaceConfig extends SurfaceConfig {
    private final SurfaceConfig.ConfigSize configSize;
    private final SurfaceConfig.ConfigType configType;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_SurfaceConfig(SurfaceConfig.ConfigType configType, SurfaceConfig.ConfigSize configSize) {
        if (configType != null) {
            this.configType = configType;
            if (configSize != null) {
                this.configSize = configSize;
                return;
            }
            throw new NullPointerException("Null configSize");
        }
        throw new NullPointerException("Null configType");
    }

    @Override // androidx.camera.core.impl.SurfaceConfig
    public SurfaceConfig.ConfigType getConfigType() {
        return this.configType;
    }

    @Override // androidx.camera.core.impl.SurfaceConfig
    public SurfaceConfig.ConfigSize getConfigSize() {
        return this.configSize;
    }

    public String toString() {
        return "SurfaceConfig{configType=" + this.configType + ", configSize=" + this.configSize + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SurfaceConfig)) {
            return false;
        }
        SurfaceConfig that = (SurfaceConfig) o;
        if (!this.configType.equals(that.getConfigType()) || !this.configSize.equals(that.getConfigSize())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.configType.hashCode()) * 1000003) ^ this.configSize.hashCode();
    }
}
