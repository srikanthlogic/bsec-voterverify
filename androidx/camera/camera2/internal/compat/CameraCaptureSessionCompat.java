package androidx.camera.camera2.internal.compat;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.os.Handler;
import android.view.Surface;
import androidx.camera.core.impl.utils.MainThreadAsyncHandler;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class CameraCaptureSessionCompat {
    private final CameraCaptureSessionCompatImpl mImpl;

    /* loaded from: classes.dex */
    public interface CameraCaptureSessionCompatImpl {
        int captureBurstRequests(List<CaptureRequest> list, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

        int captureSingleRequest(CaptureRequest captureRequest, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

        int setRepeatingBurstRequests(List<CaptureRequest> list, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

        int setSingleRepeatingRequest(CaptureRequest captureRequest, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;

        CameraCaptureSession unwrap();
    }

    private CameraCaptureSessionCompat(CameraCaptureSession captureSession, Handler compatHandler) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.mImpl = new CameraCaptureSessionCompatApi28Impl(captureSession);
        } else {
            this.mImpl = CameraCaptureSessionCompatBaseImpl.create(captureSession, compatHandler);
        }
    }

    public static CameraCaptureSessionCompat toCameraCaptureSessionCompat(CameraCaptureSession captureSession) {
        return toCameraCaptureSessionCompat(captureSession, MainThreadAsyncHandler.getInstance());
    }

    public static CameraCaptureSessionCompat toCameraCaptureSessionCompat(CameraCaptureSession captureSession, Handler compatHandler) {
        return new CameraCaptureSessionCompat(captureSession, compatHandler);
    }

    public CameraCaptureSession toCameraCaptureSession() {
        return this.mImpl.unwrap();
    }

    public int captureBurstRequests(List<CaptureRequest> requests, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        return this.mImpl.captureBurstRequests(requests, executor, listener);
    }

    public int captureSingleRequest(CaptureRequest request, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        return this.mImpl.captureSingleRequest(request, executor, listener);
    }

    public int setRepeatingBurstRequests(List<CaptureRequest> requests, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        return this.mImpl.setRepeatingBurstRequests(requests, executor, listener);
    }

    public int setSingleRepeatingRequest(CaptureRequest request, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        return this.mImpl.setSingleRepeatingRequest(request, executor, listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class CaptureCallbackExecutorWrapper extends CameraCaptureSession.CaptureCallback {
        private final Executor mExecutor;
        final CameraCaptureSession.CaptureCallback mWrappedCallback;

        public CaptureCallbackExecutorWrapper(Executor executor, CameraCaptureSession.CaptureCallback wrappedCallback) {
            this.mExecutor = executor;
            this.mWrappedCallback = wrappedCallback;
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(final CameraCaptureSession session, final CaptureRequest request, final long timestamp, final long frameNumber) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CaptureCallbackExecutorWrapper.1
                @Override // java.lang.Runnable
                public void run() {
                    CaptureCallbackExecutorWrapper.this.mWrappedCallback.onCaptureStarted(session, request, timestamp, frameNumber);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureProgressed(final CameraCaptureSession session, final CaptureRequest request, final CaptureResult partialResult) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CaptureCallbackExecutorWrapper.2
                @Override // java.lang.Runnable
                public void run() {
                    CaptureCallbackExecutorWrapper.this.mWrappedCallback.onCaptureProgressed(session, request, partialResult);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(final CameraCaptureSession session, final CaptureRequest request, final TotalCaptureResult result) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CaptureCallbackExecutorWrapper.3
                @Override // java.lang.Runnable
                public void run() {
                    CaptureCallbackExecutorWrapper.this.mWrappedCallback.onCaptureCompleted(session, request, result);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureFailed(final CameraCaptureSession session, final CaptureRequest request, final CaptureFailure failure) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CaptureCallbackExecutorWrapper.4
                @Override // java.lang.Runnable
                public void run() {
                    CaptureCallbackExecutorWrapper.this.mWrappedCallback.onCaptureFailed(session, request, failure);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceCompleted(final CameraCaptureSession session, final int sequenceId, final long frameNumber) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CaptureCallbackExecutorWrapper.5
                @Override // java.lang.Runnable
                public void run() {
                    CaptureCallbackExecutorWrapper.this.mWrappedCallback.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceAborted(final CameraCaptureSession session, final int sequenceId) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CaptureCallbackExecutorWrapper.6
                @Override // java.lang.Runnable
                public void run() {
                    CaptureCallbackExecutorWrapper.this.mWrappedCallback.onCaptureSequenceAborted(session, sequenceId);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureBufferLost(final CameraCaptureSession session, final CaptureRequest request, final Surface target, final long frameNumber) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.CaptureCallbackExecutorWrapper.7
                @Override // java.lang.Runnable
                public void run() {
                    CaptureCallbackExecutorWrapper.this.mWrappedCallback.onCaptureBufferLost(session, request, target, frameNumber);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class StateCallbackExecutorWrapper extends CameraCaptureSession.StateCallback {
        private final Executor mExecutor;
        final CameraCaptureSession.StateCallback mWrappedCallback;

        public StateCallbackExecutorWrapper(Executor executor, CameraCaptureSession.StateCallback wrappedCallback) {
            this.mExecutor = executor;
            this.mWrappedCallback = wrappedCallback;
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(final CameraCaptureSession session) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.StateCallbackExecutorWrapper.1
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onConfigured(session);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(final CameraCaptureSession session) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.StateCallbackExecutorWrapper.2
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onConfigureFailed(session);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onReady(final CameraCaptureSession session) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.StateCallbackExecutorWrapper.3
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onReady(session);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onActive(final CameraCaptureSession session) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.StateCallbackExecutorWrapper.4
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onActive(session);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onCaptureQueueEmpty(final CameraCaptureSession session) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.StateCallbackExecutorWrapper.5
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onCaptureQueueEmpty(session);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(final CameraCaptureSession session) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.StateCallbackExecutorWrapper.6
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onClosed(session);
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onSurfacePrepared(final CameraCaptureSession session, final Surface surface) {
            this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat.StateCallbackExecutorWrapper.7
                @Override // java.lang.Runnable
                public void run() {
                    StateCallbackExecutorWrapper.this.mWrappedCallback.onSurfacePrepared(session, surface);
                }
            });
        }
    }
}
