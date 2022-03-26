package androidx.camera.core.impl;

import androidx.camera.core.CameraInfo;
import androidx.camera.core.impl.Config;
/* loaded from: classes.dex */
public interface ConfigProvider<C extends Config> {
    C getConfig(CameraInfo cameraInfo);
}
