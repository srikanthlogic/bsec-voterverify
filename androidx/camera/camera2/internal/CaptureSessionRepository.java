package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraDevice;
import androidx.camera.camera2.internal.CaptureSessionRepository;
import androidx.camera.core.impl.DeferrableSurface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class CaptureSessionRepository {
    final Executor mExecutor;
    final Object mLock = new Object();
    final Set<SynchronizedCaptureSession> mCaptureSessions = new LinkedHashSet();
    final Set<SynchronizedCaptureSession> mClosingCaptureSession = new LinkedHashSet();
    final Set<SynchronizedCaptureSession> mCreatingCaptureSessions = new LinkedHashSet();
    final Map<SynchronizedCaptureSession, List<DeferrableSurface>> mDeferrableSurfaceMap = new HashMap();
    private final CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() { // from class: androidx.camera.camera2.internal.CaptureSessionRepository.1
        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice camera) {
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice camera, int error) {
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice camera) {
            CaptureSessionRepository.this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$CaptureSessionRepository$1$rRS948den_N41nH0jLG7y-VuOvA
                @Override // java.lang.Runnable
                public final void run() {
                    CaptureSessionRepository.AnonymousClass1.this.lambda$onDisconnected$0$CaptureSessionRepository$1();
                }
            });
        }

        public /* synthetic */ void lambda$onDisconnected$0$CaptureSessionRepository$1() {
            LinkedHashSet<SynchronizedCaptureSession> sessions = new LinkedHashSet<>();
            synchronized (CaptureSessionRepository.this.mLock) {
                sessions.addAll(new LinkedHashSet<>(CaptureSessionRepository.this.mCreatingCaptureSessions));
                sessions.addAll(new LinkedHashSet<>(CaptureSessionRepository.this.mCaptureSessions));
            }
            CaptureSessionRepository.forceOnClosed(sessions);
        }
    };

    public CaptureSessionRepository(Executor executor) {
        this.mExecutor = executor;
    }

    public CameraDevice.StateCallback getCameraStateCallback() {
        return this.mCameraStateCallback;
    }

    static void forceOnClosed(Set<SynchronizedCaptureSession> sessions) {
        for (SynchronizedCaptureSession session : sessions) {
            session.getStateCallback().onClosed(session);
        }
    }

    public List<SynchronizedCaptureSession> getCaptureSessions() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mCaptureSessions);
        }
        return arrayList;
    }

    public List<SynchronizedCaptureSession> getClosingCaptureSession() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mClosingCaptureSession);
        }
        return arrayList;
    }

    public List<SynchronizedCaptureSession> getCreatingCaptureSessions() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mCreatingCaptureSessions);
        }
        return arrayList;
    }

    public Map<SynchronizedCaptureSession, List<DeferrableSurface>> registerDeferrableSurface(SynchronizedCaptureSession synchronizedCaptureSession, List<DeferrableSurface> deferrableSurfaces) {
        HashMap hashMap;
        synchronized (this.mLock) {
            this.mDeferrableSurfaceMap.put(synchronizedCaptureSession, deferrableSurfaces);
            hashMap = new HashMap(this.mDeferrableSurfaceMap);
        }
        return hashMap;
    }

    public void unregisterDeferrableSurface(SynchronizedCaptureSession synchronizedCaptureSession) {
        synchronized (this.mLock) {
            this.mDeferrableSurfaceMap.remove(synchronizedCaptureSession);
        }
    }

    public void onCreateCaptureSession(SynchronizedCaptureSession synchronizedCaptureSession) {
        synchronized (this.mLock) {
            this.mCreatingCaptureSessions.add(synchronizedCaptureSession);
        }
    }

    public void onCaptureSessionConfigureFail(SynchronizedCaptureSession synchronizedCaptureSession) {
        synchronized (this.mLock) {
            this.mCreatingCaptureSessions.remove(synchronizedCaptureSession);
        }
    }

    public void onCaptureSessionCreated(SynchronizedCaptureSession synchronizedCaptureSession) {
        synchronized (this.mLock) {
            this.mCaptureSessions.add(synchronizedCaptureSession);
            this.mCreatingCaptureSessions.remove(synchronizedCaptureSession);
        }
    }

    public void onCaptureSessionClosed(SynchronizedCaptureSession synchronizedCaptureSession) {
        synchronized (this.mLock) {
            this.mCaptureSessions.remove(synchronizedCaptureSession);
            this.mClosingCaptureSession.remove(synchronizedCaptureSession);
        }
    }

    public void onCaptureSessionClosing(SynchronizedCaptureSession synchronizedCaptureSession) {
        synchronized (this.mLock) {
            this.mClosingCaptureSession.add(synchronizedCaptureSession);
        }
    }
}
