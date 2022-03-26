package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraCaptureCallbacks;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class CaptureCallbackConverter {
    private CaptureCallbackConverter() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CameraCaptureSession.CaptureCallback toCaptureCallback(CameraCaptureCallback cameraCaptureCallback) {
        if (cameraCaptureCallback == null) {
            return null;
        }
        List<CameraCaptureSession.CaptureCallback> list = new ArrayList<>();
        toCaptureCallback(cameraCaptureCallback, list);
        if (list.size() == 1) {
            return list.get(0);
        }
        return Camera2CaptureCallbacks.createComboCallback(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void toCaptureCallback(CameraCaptureCallback cameraCaptureCallback, List<CameraCaptureSession.CaptureCallback> captureCallbackList) {
        if (cameraCaptureCallback instanceof CameraCaptureCallbacks.ComboCameraCaptureCallback) {
            for (CameraCaptureCallback callback : ((CameraCaptureCallbacks.ComboCameraCaptureCallback) cameraCaptureCallback).getCallbacks()) {
                toCaptureCallback(callback, captureCallbackList);
            }
        } else if (cameraCaptureCallback instanceof CaptureCallbackContainer) {
            captureCallbackList.add(((CaptureCallbackContainer) cameraCaptureCallback).getCaptureCallback());
        } else {
            captureCallbackList.add(new CaptureCallbackAdapter(cameraCaptureCallback));
        }
    }
}
