package androidx.camera.camera2.internal.compat;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
class CameraCaptureSessionCompatApi28Impl extends CameraCaptureSessionCompatBaseImpl {
    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraCaptureSessionCompatApi28Impl(CameraCaptureSession captureSession) {
        super(captureSession, null);
    }

    @Override // androidx.camera.camera2.internal.compat.CameraCaptureSessionCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CameraCaptureSessionCompatImpl
    public int captureBurstRequests(List<CaptureRequest> requests, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        return this.mCameraCaptureSession.captureBurstRequests(requests, executor, listener);
    }

    @Override // androidx.camera.camera2.internal.compat.CameraCaptureSessionCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CameraCaptureSessionCompatImpl
    public int captureSingleRequest(CaptureRequest request, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        return this.mCameraCaptureSession.captureSingleRequest(request, executor, listener);
    }

    @Override // androidx.camera.camera2.internal.compat.CameraCaptureSessionCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CameraCaptureSessionCompatImpl
    public int setRepeatingBurstRequests(List<CaptureRequest> requests, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        return this.mCameraCaptureSession.setRepeatingBurstRequests(requests, executor, listener);
    }

    @Override // androidx.camera.camera2.internal.compat.CameraCaptureSessionCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CameraCaptureSessionCompatImpl
    public int setSingleRepeatingRequest(CaptureRequest request, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        return this.mCameraCaptureSession.setSingleRepeatingRequest(request, executor, listener);
    }
}
