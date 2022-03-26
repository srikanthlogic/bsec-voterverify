package androidx.camera.camera2.internal.compat;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.os.Build;
import android.os.Handler;
import androidx.camera.camera2.internal.compat.params.SessionConfigurationCompat;
import androidx.camera.core.impl.utils.MainThreadAsyncHandler;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class CameraDeviceCompat {
    public static final int SESSION_OPERATION_MODE_CONSTRAINED_HIGH_SPEED;
    public static final int SESSION_OPERATION_MODE_NORMAL;
    private final CameraDeviceCompatImpl mImpl;

    /* loaded from: classes.dex */
    public interface CameraDeviceCompatImpl {
        void createCaptureSession(SessionConfigurationCompat sessionConfigurationCompat) throws CameraAccessException;

        CameraDevice unwrap();
    }

    private CameraDeviceCompat(CameraDevice cameraDevice, Handler compatHandler) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.mImpl = new CameraDeviceCompatApi28Impl(cameraDevice);
        } else if (Build.VERSION.SDK_INT >= 24) {
            this.mImpl = CameraDeviceCompatApi24Impl.create(cameraDevice, compatHandler);
        } else if (Build.VERSION.SDK_INT >= 23) {
            this.mImpl = CameraDeviceCompatApi23Impl.create(cameraDevice, compatHandler);
        } else {
            this.mImpl = CameraDeviceCompatBaseImpl.create(cameraDevice, compatHandler);
        }
    }

    public static CameraDeviceCompat toCameraDeviceCompat(CameraDevice captureSession) {
        return toCameraDeviceCompat(captureSession, MainThreadAsyncHandler.getInstance());
    }

    public static CameraDeviceCompat toCameraDeviceCompat(CameraDevice cameraDevice, Handler compatHandler) {
        return new CameraDeviceCompat(cameraDevice, compatHandler);
    }

    public CameraDevice toCameraDevice() {
        return this.mImpl.unwrap();
    }

    public void createCaptureSession(SessionConfigurationCompat config) throws CameraAccessException {
        this.mImpl.createCaptureSession(config);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class StateCallbackExecutorWrapper extends CameraDevice.StateCallback {
        private final Executor mExecutor;
        final CameraDevice.StateCallback mWrappedCallback;

        public StateCallbackExecutorWrapper(Executor executor, CameraDevice.StateCallback wrappedCallback) {
            this.mExecutor = executor;
            this.mWrappedCallback = wrappedCallback;
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(final CameraDevice camera) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraDeviceCompat.StateCallbackExecutorWrapper.1
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onOpened(camera);
                }
            });
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(final CameraDevice camera) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraDeviceCompat.StateCallbackExecutorWrapper.2
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onDisconnected(camera);
                }
            });
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(final CameraDevice camera, final int error) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraDeviceCompat.StateCallbackExecutorWrapper.3
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onError(camera, error);
                }
            });
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(final CameraDevice camera) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraDeviceCompat.StateCallbackExecutorWrapper.4
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onClosed(camera);
                }
            });
        }
    }
}
