package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;
import androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface SynchronizedCaptureSession {
    void abortCaptures() throws CameraAccessException;

    int captureBurstRequests(List<CaptureRequest> list, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

    int captureBurstRequests(List<CaptureRequest> list, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

    int captureSingleRequest(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

    int captureSingleRequest(CaptureRequest captureRequest, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

    void close();

    CameraDevice getDevice();

    StateCallback getStateCallback();

    ListenableFuture<Void> getSynchronizedBlocker(String str);

    int setRepeatingBurstRequests(List<CaptureRequest> list, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

    int setRepeatingBurstRequests(List<CaptureRequest> list, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

    int setSingleRepeatingRequest(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

    int setSingleRepeatingRequest(CaptureRequest captureRequest, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

    void stopRepeating() throws CameraAccessException;

    CameraCaptureSessionCompat toCameraCaptureSessionCompat();

    /* loaded from: classes.dex */
    public static abstract class StateCallback {
        public void onReady(SynchronizedCaptureSession session) {
        }

        public void onActive(SynchronizedCaptureSession session) {
        }

        public void onCaptureQueueEmpty(SynchronizedCaptureSession session) {
        }

        public void onSurfacePrepared(SynchronizedCaptureSession session, Surface surface) {
        }

        public void onConfigured(SynchronizedCaptureSession session) {
        }

        public void onConfigureFailed(SynchronizedCaptureSession session) {
        }

        public void onClosed(SynchronizedCaptureSession session) {
        }
    }
}
