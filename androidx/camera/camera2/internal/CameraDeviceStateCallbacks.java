package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraDevice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class CameraDeviceStateCallbacks {
    private CameraDeviceStateCallbacks() {
    }

    public static CameraDevice.StateCallback createNoOpCallback() {
        return new NoOpDeviceStateCallback();
    }

    public static CameraDevice.StateCallback createComboCallback(List<CameraDevice.StateCallback> callbacks) {
        if (callbacks.isEmpty()) {
            return createNoOpCallback();
        }
        if (callbacks.size() == 1) {
            return callbacks.get(0);
        }
        return new ComboDeviceStateCallback(callbacks);
    }

    public static CameraDevice.StateCallback createComboCallback(CameraDevice.StateCallback... callbacks) {
        return createComboCallback(Arrays.asList(callbacks));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class NoOpDeviceStateCallback extends CameraDevice.StateCallback {
        NoOpDeviceStateCallback() {
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int error) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ComboDeviceStateCallback extends CameraDevice.StateCallback {
        private final List<CameraDevice.StateCallback> mCallbacks = new ArrayList();

        ComboDeviceStateCallback(List<CameraDevice.StateCallback> callbacks) {
            for (CameraDevice.StateCallback callback : callbacks) {
                if (!(callback instanceof NoOpDeviceStateCallback)) {
                    this.mCallbacks.add(callback);
                }
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            for (CameraDevice.StateCallback callback : this.mCallbacks) {
                callback.onOpened(cameraDevice);
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            for (CameraDevice.StateCallback callback : this.mCallbacks) {
                callback.onClosed(cameraDevice);
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            for (CameraDevice.StateCallback callback : this.mCallbacks) {
                callback.onDisconnected(cameraDevice);
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int error) {
            for (CameraDevice.StateCallback callback : this.mCallbacks) {
                callback.onError(cameraDevice, error);
            }
        }
    }
}
