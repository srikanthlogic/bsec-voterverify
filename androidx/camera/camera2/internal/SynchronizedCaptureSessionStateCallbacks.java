package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.view.Surface;
import androidx.camera.camera2.internal.SynchronizedCaptureSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SynchronizedCaptureSessionStateCallbacks extends SynchronizedCaptureSession.StateCallback {
    private final List<SynchronizedCaptureSession.StateCallback> mCallbacks = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SynchronizedCaptureSession.StateCallback createComboCallback(SynchronizedCaptureSession.StateCallback... callbacks) {
        return new SynchronizedCaptureSessionStateCallbacks(Arrays.asList(callbacks));
    }

    SynchronizedCaptureSessionStateCallbacks(List<SynchronizedCaptureSession.StateCallback> callbacks) {
        this.mCallbacks.addAll(callbacks);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onSurfacePrepared(SynchronizedCaptureSession session, Surface surface) {
        for (SynchronizedCaptureSession.StateCallback callback : this.mCallbacks) {
            callback.onSurfacePrepared(session, surface);
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onReady(SynchronizedCaptureSession session) {
        for (SynchronizedCaptureSession.StateCallback callback : this.mCallbacks) {
            callback.onReady(session);
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onActive(SynchronizedCaptureSession session) {
        for (SynchronizedCaptureSession.StateCallback callback : this.mCallbacks) {
            callback.onActive(session);
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onCaptureQueueEmpty(SynchronizedCaptureSession session) {
        for (SynchronizedCaptureSession.StateCallback callback : this.mCallbacks) {
            callback.onCaptureQueueEmpty(session);
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onConfigured(SynchronizedCaptureSession session) {
        for (SynchronizedCaptureSession.StateCallback callback : this.mCallbacks) {
            callback.onConfigured(session);
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onConfigureFailed(SynchronizedCaptureSession session) {
        for (SynchronizedCaptureSession.StateCallback callback : this.mCallbacks) {
            callback.onConfigureFailed(session);
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onClosed(SynchronizedCaptureSession session) {
        for (SynchronizedCaptureSession.StateCallback callback : this.mCallbacks) {
            callback.onClosed(session);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Adapter extends SynchronizedCaptureSession.StateCallback {
        private final CameraCaptureSession.StateCallback mCameraCaptureSessionStateCallback;

        Adapter(CameraCaptureSession.StateCallback cameraCaptureSessionStateCallback) {
            this.mCameraCaptureSessionStateCallback = cameraCaptureSessionStateCallback;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Adapter(List<CameraCaptureSession.StateCallback> callbackList) {
            this(CameraCaptureSessionStateCallbacks.createComboCallback(callbackList));
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onSurfacePrepared(SynchronizedCaptureSession session, Surface surface) {
            this.mCameraCaptureSessionStateCallback.onSurfacePrepared(session.toCameraCaptureSessionCompat().toCameraCaptureSession(), surface);
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onReady(SynchronizedCaptureSession session) {
            this.mCameraCaptureSessionStateCallback.onReady(session.toCameraCaptureSessionCompat().toCameraCaptureSession());
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onActive(SynchronizedCaptureSession session) {
            this.mCameraCaptureSessionStateCallback.onActive(session.toCameraCaptureSessionCompat().toCameraCaptureSession());
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onCaptureQueueEmpty(SynchronizedCaptureSession session) {
            this.mCameraCaptureSessionStateCallback.onCaptureQueueEmpty(session.toCameraCaptureSessionCompat().toCameraCaptureSession());
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onConfigured(SynchronizedCaptureSession session) {
            this.mCameraCaptureSessionStateCallback.onConfigured(session.toCameraCaptureSessionCompat().toCameraCaptureSession());
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onConfigureFailed(SynchronizedCaptureSession session) {
            this.mCameraCaptureSessionStateCallback.onConfigureFailed(session.toCameraCaptureSessionCompat().toCameraCaptureSession());
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onClosed(SynchronizedCaptureSession session) {
            this.mCameraCaptureSessionStateCallback.onClosed(session.toCameraCaptureSessionCompat().toCameraCaptureSession());
        }
    }
}
