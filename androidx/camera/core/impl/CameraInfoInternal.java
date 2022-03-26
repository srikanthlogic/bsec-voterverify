package androidx.camera.core.impl;

import androidx.camera.core.CameraInfo;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface CameraInfoInternal extends CameraInfo {
    void addSessionCaptureCallback(Executor executor, CameraCaptureCallback cameraCaptureCallback);

    String getCameraId();

    Integer getLensFacing();

    void removeSessionCaptureCallback(CameraCaptureCallback cameraCaptureCallback);
}
