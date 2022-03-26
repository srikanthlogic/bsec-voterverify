package androidx.camera.core.impl;

import android.content.Context;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.InitializationException;
/* loaded from: classes.dex */
public interface UseCaseConfigFactory {

    /* loaded from: classes.dex */
    public interface Provider {
        UseCaseConfigFactory newInstance(Context context) throws InitializationException;
    }

    <C extends UseCaseConfig<?>> C getConfig(Class<C> cls, CameraInfo cameraInfo);
}
