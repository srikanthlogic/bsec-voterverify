package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class Camera2CaptureCallbacks {
    private Camera2CaptureCallbacks() {
    }

    public static CameraCaptureSession.CaptureCallback createNoOpCallback() {
        return new NoOpSessionCaptureCallback();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CameraCaptureSession.CaptureCallback createComboCallback(List<CameraCaptureSession.CaptureCallback> callbacks) {
        return new ComboSessionCaptureCallback(callbacks);
    }

    public static CameraCaptureSession.CaptureCallback createComboCallback(CameraCaptureSession.CaptureCallback... callbacks) {
        return createComboCallback(Arrays.asList(callbacks));
    }

    /* loaded from: classes.dex */
    static final class NoOpSessionCaptureCallback extends CameraCaptureSession.CaptureCallback {
        NoOpSessionCaptureCallback() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureBufferLost(CameraCaptureSession session, CaptureRequest request, Surface surface, long frame) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceCompleted(CameraCaptureSession session, int sequenceId, long frame) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frame) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ComboSessionCaptureCallback extends CameraCaptureSession.CaptureCallback {
        private final List<CameraCaptureSession.CaptureCallback> mCallbacks = new ArrayList();

        ComboSessionCaptureCallback(List<CameraCaptureSession.CaptureCallback> callbacks) {
            for (CameraCaptureSession.CaptureCallback callback : callbacks) {
                if (!(callback instanceof NoOpSessionCaptureCallback)) {
                    this.mCallbacks.add(callback);
                }
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureBufferLost(CameraCaptureSession session, CaptureRequest request, Surface surface, long frame) {
            for (CameraCaptureSession.CaptureCallback callback : this.mCallbacks) {
                callback.onCaptureBufferLost(session, request, surface, frame);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            for (CameraCaptureSession.CaptureCallback callback : this.mCallbacks) {
                callback.onCaptureCompleted(session, request, result);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            for (CameraCaptureSession.CaptureCallback callback : this.mCallbacks) {
                callback.onCaptureFailed(session, request, failure);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
            for (CameraCaptureSession.CaptureCallback callback : this.mCallbacks) {
                callback.onCaptureProgressed(session, request, partialResult);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId) {
            for (CameraCaptureSession.CaptureCallback callback : this.mCallbacks) {
                callback.onCaptureSequenceAborted(session, sequenceId);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceCompleted(CameraCaptureSession session, int sequenceId, long frame) {
            for (CameraCaptureSession.CaptureCallback callback : this.mCallbacks) {
                callback.onCaptureSequenceCompleted(session, sequenceId, frame);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frame) {
            for (CameraCaptureSession.CaptureCallback callback : this.mCallbacks) {
                callback.onCaptureStarted(session, request, timestamp, frame);
            }
        }
    }
}
