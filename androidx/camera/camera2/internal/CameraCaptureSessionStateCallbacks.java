package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class CameraCaptureSessionStateCallbacks {
    private CameraCaptureSessionStateCallbacks() {
    }

    public static CameraCaptureSession.StateCallback createNoOpCallback() {
        return new NoOpSessionStateCallback();
    }

    public static CameraCaptureSession.StateCallback createComboCallback(List<CameraCaptureSession.StateCallback> callbacks) {
        if (callbacks.isEmpty()) {
            return createNoOpCallback();
        }
        if (callbacks.size() == 1) {
            return callbacks.get(0);
        }
        return new ComboSessionStateCallback(callbacks);
    }

    public static CameraCaptureSession.StateCallback createComboCallback(CameraCaptureSession.StateCallback... callbacks) {
        return createComboCallback(Arrays.asList(callbacks));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class NoOpSessionStateCallback extends CameraCaptureSession.StateCallback {
        NoOpSessionStateCallback() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession session) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onActive(CameraCaptureSession session) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(CameraCaptureSession session) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onReady(CameraCaptureSession session) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onCaptureQueueEmpty(CameraCaptureSession session) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onSurfacePrepared(CameraCaptureSession session, Surface surface) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession session) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ComboSessionStateCallback extends CameraCaptureSession.StateCallback {
        private final List<CameraCaptureSession.StateCallback> mCallbacks = new ArrayList();

        ComboSessionStateCallback(List<CameraCaptureSession.StateCallback> callbacks) {
            for (CameraCaptureSession.StateCallback callback : callbacks) {
                if (!(callback instanceof NoOpSessionStateCallback)) {
                    this.mCallbacks.add(callback);
                }
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession session) {
            for (CameraCaptureSession.StateCallback callback : this.mCallbacks) {
                callback.onConfigured(session);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onActive(CameraCaptureSession session) {
            for (CameraCaptureSession.StateCallback callback : this.mCallbacks) {
                callback.onActive(session);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(CameraCaptureSession session) {
            for (CameraCaptureSession.StateCallback callback : this.mCallbacks) {
                callback.onClosed(session);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onReady(CameraCaptureSession session) {
            for (CameraCaptureSession.StateCallback callback : this.mCallbacks) {
                callback.onReady(session);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onCaptureQueueEmpty(CameraCaptureSession session) {
            for (CameraCaptureSession.StateCallback callback : this.mCallbacks) {
                callback.onCaptureQueueEmpty(session);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onSurfacePrepared(CameraCaptureSession session, Surface surface) {
            for (CameraCaptureSession.StateCallback callback : this.mCallbacks) {
                callback.onSurfacePrepared(session, surface);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession session) {
            for (CameraCaptureSession.StateCallback callback : this.mCallbacks) {
                callback.onConfigureFailed(session);
            }
        }
    }
}
