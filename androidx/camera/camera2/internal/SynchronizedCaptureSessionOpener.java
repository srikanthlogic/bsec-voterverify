package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraDevice;
import android.os.Build;
import android.os.Handler;
import android.view.Surface;
import androidx.camera.camera2.internal.SynchronizedCaptureSession;
import androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat;
import androidx.camera.camera2.internal.compat.params.SessionConfigurationCompat;
import androidx.camera.core.impl.DeferrableSurface;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
/* loaded from: classes.dex */
public final class SynchronizedCaptureSessionOpener {
    static final String FEATURE_DEFERRABLE_SURFACE_CLOSE;
    static final String FEATURE_FORCE_CLOSE;
    static final String FEATURE_WAIT_FOR_REQUEST;
    private final OpenerImpl mImpl;

    /* loaded from: classes.dex */
    public interface OpenerImpl {
        SessionConfigurationCompat createSessionConfigurationCompat(int i, List<OutputConfigurationCompat> list, SynchronizedCaptureSession.StateCallback stateCallback);

        Executor getExecutor();

        ListenableFuture<Void> openCaptureSession(CameraDevice cameraDevice, SessionConfigurationCompat sessionConfigurationCompat);

        ListenableFuture<List<Surface>> startWithDeferrableSurface(List<DeferrableSurface> list, long j);

        boolean stop();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SynchronizedSessionFeature {
    }

    SynchronizedCaptureSessionOpener(OpenerImpl impl) {
        this.mImpl = impl;
    }

    public ListenableFuture<Void> openCaptureSession(CameraDevice cameraDevice, SessionConfigurationCompat sessionConfigurationCompat) {
        return this.mImpl.openCaptureSession(cameraDevice, sessionConfigurationCompat);
    }

    public SessionConfigurationCompat createSessionConfigurationCompat(int sessionType, List<OutputConfigurationCompat> outputsCompat, SynchronizedCaptureSession.StateCallback stateCallback) {
        return this.mImpl.createSessionConfigurationCompat(sessionType, outputsCompat, stateCallback);
    }

    public ListenableFuture<List<Surface>> startWithDeferrableSurface(List<DeferrableSurface> deferrableSurfaces, long timeout) {
        return this.mImpl.startWithDeferrableSurface(deferrableSurfaces, timeout);
    }

    public boolean stop() {
        return this.mImpl.stop();
    }

    public Executor getExecutor() {
        return this.mImpl.getExecutor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Builder {
        private final CaptureSessionRepository mCaptureSessionRepository;
        private final Handler mCompatHandler;
        private final Set<String> mEnableFeature = new HashSet();
        private final Executor mExecutor;
        private final ScheduledExecutorService mScheduledExecutorService;
        private final int mSupportedHardwareLevel;

        public Builder(Executor executor, ScheduledExecutorService scheduledExecutorService, Handler compatHandler, CaptureSessionRepository captureSessionRepository, int supportedHardwareLevel) {
            this.mExecutor = executor;
            this.mScheduledExecutorService = scheduledExecutorService;
            this.mCompatHandler = compatHandler;
            this.mCaptureSessionRepository = captureSessionRepository;
            this.mSupportedHardwareLevel = supportedHardwareLevel;
            if (Build.VERSION.SDK_INT < 23) {
                this.mEnableFeature.add(SynchronizedCaptureSessionOpener.FEATURE_FORCE_CLOSE);
            }
            if (this.mSupportedHardwareLevel == 2 || Build.VERSION.SDK_INT <= 23) {
                this.mEnableFeature.add(SynchronizedCaptureSessionOpener.FEATURE_DEFERRABLE_SURFACE_CLOSE);
            }
            if (this.mSupportedHardwareLevel == 2) {
                this.mEnableFeature.add(SynchronizedCaptureSessionOpener.FEATURE_WAIT_FOR_REQUEST);
            }
        }

        public SynchronizedCaptureSessionOpener build() {
            if (this.mEnableFeature.isEmpty()) {
                return new SynchronizedCaptureSessionOpener(new SynchronizedCaptureSessionBaseImpl(this.mCaptureSessionRepository, this.mExecutor, this.mScheduledExecutorService, this.mCompatHandler));
            }
            return new SynchronizedCaptureSessionOpener(new SynchronizedCaptureSessionImpl(this.mEnableFeature, this.mCaptureSessionRepository, this.mExecutor, this.mScheduledExecutorService, this.mCompatHandler));
        }
    }
}
