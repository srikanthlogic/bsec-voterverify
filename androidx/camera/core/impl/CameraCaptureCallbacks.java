package androidx.camera.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class CameraCaptureCallbacks {
    private CameraCaptureCallbacks() {
    }

    public static CameraCaptureCallback createNoOpCallback() {
        return new NoOpCameraCaptureCallback();
    }

    static CameraCaptureCallback createComboCallback(List<CameraCaptureCallback> callbacks) {
        if (callbacks.isEmpty()) {
            return createNoOpCallback();
        }
        if (callbacks.size() == 1) {
            return callbacks.get(0);
        }
        return new ComboCameraCaptureCallback(callbacks);
    }

    public static CameraCaptureCallback createComboCallback(CameraCaptureCallback... callbacks) {
        return createComboCallback(Arrays.asList(callbacks));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class NoOpCameraCaptureCallback extends CameraCaptureCallback {
        NoOpCameraCaptureCallback() {
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureCompleted(CameraCaptureResult cameraCaptureResult) {
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureFailed(CameraCaptureFailure failure) {
        }
    }

    /* loaded from: classes.dex */
    public static final class ComboCameraCaptureCallback extends CameraCaptureCallback {
        private final List<CameraCaptureCallback> mCallbacks = new ArrayList();

        ComboCameraCaptureCallback(List<CameraCaptureCallback> callbacks) {
            for (CameraCaptureCallback callback : callbacks) {
                if (!(callback instanceof NoOpCameraCaptureCallback)) {
                    this.mCallbacks.add(callback);
                }
            }
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureCompleted(CameraCaptureResult result) {
            for (CameraCaptureCallback callback : this.mCallbacks) {
                callback.onCaptureCompleted(result);
            }
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureFailed(CameraCaptureFailure failure) {
            for (CameraCaptureCallback callback : this.mCallbacks) {
                callback.onCaptureFailed(failure);
            }
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureCancelled() {
            for (CameraCaptureCallback callback : this.mCallbacks) {
                callback.onCaptureCancelled();
            }
        }

        public List<CameraCaptureCallback> getCallbacks() {
            return this.mCallbacks;
        }
    }
}
