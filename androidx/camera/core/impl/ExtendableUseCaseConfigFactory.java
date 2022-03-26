package androidx.camera.core.impl;

import androidx.camera.core.CameraInfo;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class ExtendableUseCaseConfigFactory implements UseCaseConfigFactory {
    private final Map<Class<?>, ConfigProvider<?>> mDefaultProviders = new HashMap();

    public <C extends Config> void installDefaultProvider(Class<C> configType, ConfigProvider<C> defaultProvider) {
        this.mDefaultProviders.put(configType, defaultProvider);
    }

    @Override // androidx.camera.core.impl.UseCaseConfigFactory
    public <C extends UseCaseConfig<?>> C getConfig(Class<C> configType, CameraInfo cameraInfo) {
        ConfigProvider<?> configProvider = this.mDefaultProviders.get(configType);
        if (configProvider != null) {
            return (C) ((UseCaseConfig) configProvider.getConfig(cameraInfo));
        }
        return null;
    }
}
