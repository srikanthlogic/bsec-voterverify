package androidx.camera.core.impl;
/* loaded from: classes.dex */
public abstract class SurfaceConfig {

    /* loaded from: classes.dex */
    public enum ConfigType {
        PRIV,
        YUV,
        JPEG,
        RAW
    }

    public abstract ConfigSize getConfigSize();

    public abstract ConfigType getConfigType();

    public static SurfaceConfig create(ConfigType type, ConfigSize size) {
        return new AutoValue_SurfaceConfig(type, size);
    }

    public final boolean isSupported(SurfaceConfig surfaceConfig) {
        ConfigType configType = surfaceConfig.getConfigType();
        if (surfaceConfig.getConfigSize().getId() > getConfigSize().getId() || configType != getConfigType()) {
            return false;
        }
        return true;
    }

    /* loaded from: classes.dex */
    public enum ConfigSize {
        ANALYSIS(0),
        PREVIEW(1),
        RECORD(2),
        MAXIMUM(3),
        NOT_SUPPORT(4);
        
        final int mId;

        ConfigSize(int id) {
            this.mId = id;
        }

        int getId() {
            return this.mId;
        }
    }
}
