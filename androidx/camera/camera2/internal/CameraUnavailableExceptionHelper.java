package androidx.camera.camera2.internal;

import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.core.CameraUnavailableException;
/* loaded from: classes.dex */
public final class CameraUnavailableExceptionHelper {
    private CameraUnavailableExceptionHelper() {
    }

    public static CameraUnavailableException createFrom(CameraAccessExceptionCompat e) {
        int errorCode;
        int reason = e.getReason();
        if (reason == 1) {
            errorCode = 1;
        } else if (reason == 2) {
            errorCode = 2;
        } else if (reason == 3) {
            errorCode = 3;
        } else if (reason == 4) {
            errorCode = 4;
        } else if (reason == 5) {
            errorCode = 5;
        } else if (reason != 10001) {
            errorCode = 0;
        } else {
            errorCode = 6;
        }
        return new CameraUnavailableException(errorCode, e);
    }
}
