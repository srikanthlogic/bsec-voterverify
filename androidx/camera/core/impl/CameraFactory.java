package androidx.camera.core.impl;

import android.content.Context;
import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.InitializationException;
import java.util.Set;
/* loaded from: classes.dex */
public interface CameraFactory {

    /* loaded from: classes.dex */
    public interface Provider {
        CameraFactory newInstance(Context context, CameraThreadConfig cameraThreadConfig) throws InitializationException;
    }

    Set<String> getAvailableCameraIds() throws CameraUnavailableException;

    CameraInternal getCamera(String str) throws CameraUnavailableException;
}
