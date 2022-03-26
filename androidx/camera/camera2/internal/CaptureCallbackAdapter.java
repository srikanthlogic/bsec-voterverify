package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraCaptureFailure;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class CaptureCallbackAdapter extends CameraCaptureSession.CaptureCallback {
    private final CameraCaptureCallback mCameraCaptureCallback;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CaptureCallbackAdapter(CameraCaptureCallback cameraCaptureCallback) {
        if (cameraCaptureCallback != null) {
            this.mCameraCaptureCallback = cameraCaptureCallback;
            return;
        }
        throw new NullPointerException("cameraCaptureCallback is null");
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);
        this.mCameraCaptureCallback.onCaptureCompleted(new Camera2CameraCaptureResult(request.getTag(), result));
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
        super.onCaptureFailed(session, request, failure);
        this.mCameraCaptureCallback.onCaptureFailed(new CameraCaptureFailure(CameraCaptureFailure.Reason.ERROR));
    }
}
