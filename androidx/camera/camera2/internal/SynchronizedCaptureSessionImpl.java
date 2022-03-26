package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import androidx.camera.camera2.internal.compat.params.SessionConfigurationCompat;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.AsyncFunction;
import androidx.camera.core.impl.utils.futures.FutureChain;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
/* loaded from: classes.dex */
public class SynchronizedCaptureSessionImpl extends SynchronizedCaptureSessionBaseImpl {
    CallbackToFutureAdapter.Completer<Void> mClosingDeferrableSurfaceCompleter;
    private final ListenableFuture<Void> mClosingDeferrableSurfaceFuture;
    private List<DeferrableSurface> mDeferrableSurfaces;
    private final Set<String> mEnabledFeature;
    private boolean mHasSubmittedRepeating;
    ListenableFuture<Void> mOpeningCaptureSession;
    CallbackToFutureAdapter.Completer<Void> mStartStreamingCompleter;
    private final ListenableFuture<Void> mStartStreamingFuture;
    ListenableFuture<List<Surface>> mStartingSurface;
    private static final String TAG;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final Object mObjectLock = new Object();
    private final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() { // from class: androidx.camera.camera2.internal.SynchronizedCaptureSessionImpl.1
        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            if (SynchronizedCaptureSessionImpl.this.mStartStreamingCompleter != null) {
                SynchronizedCaptureSessionImpl.this.mStartStreamingCompleter.set(null);
                SynchronizedCaptureSessionImpl.this.mStartStreamingCompleter = null;
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId) {
            if (SynchronizedCaptureSessionImpl.this.mStartStreamingCompleter != null) {
                SynchronizedCaptureSessionImpl.this.mStartStreamingCompleter.setCancelled();
                SynchronizedCaptureSessionImpl.this.mStartStreamingCompleter = null;
            }
        }
    };

    public SynchronizedCaptureSessionImpl(Set<String> enabledFeature, CaptureSessionRepository repository, Executor executor, ScheduledExecutorService scheduledExecutorService, Handler compatHandler) {
        super(repository, executor, scheduledExecutorService, compatHandler);
        this.mEnabledFeature = enabledFeature;
        if (enabledFeature.contains("wait_for_request")) {
            this.mStartStreamingFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.internal.-$$Lambda$SynchronizedCaptureSessionImpl$jyQAveySUCZWY5ISV0AhM2DhYt0
                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                    return SynchronizedCaptureSessionImpl.this.lambda$new$0$SynchronizedCaptureSessionImpl(completer);
                }
            });
        } else {
            this.mStartStreamingFuture = Futures.immediateFuture(null);
        }
        if (this.mEnabledFeature.contains("deferrableSurface_close")) {
            this.mClosingDeferrableSurfaceFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.internal.-$$Lambda$SynchronizedCaptureSessionImpl$WP0Dt0CneE2u6JPoDDGhCbiX1Ho
                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                    return SynchronizedCaptureSessionImpl.this.lambda$new$1$SynchronizedCaptureSessionImpl(completer);
                }
            });
        } else {
            this.mClosingDeferrableSurfaceFuture = Futures.immediateFuture(null);
        }
    }

    public /* synthetic */ Object lambda$new$0$SynchronizedCaptureSessionImpl(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mStartStreamingCompleter = completer;
        return "StartStreamingFuture[session=" + this + "]";
    }

    public /* synthetic */ Object lambda$new$1$SynchronizedCaptureSessionImpl(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mClosingDeferrableSurfaceCompleter = completer;
        return "ClosingDeferrableSurfaceFuture[session=" + this + "]";
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl, androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener.OpenerImpl
    public ListenableFuture<Void> openCaptureSession(CameraDevice cameraDevice, SessionConfigurationCompat sessionConfigurationCompat) {
        ListenableFuture<Void> nonCancellationPropagating;
        synchronized (this.mObjectLock) {
            this.mOpeningCaptureSession = FutureChain.from(Futures.successfulAsList(getBlockerFuture("wait_for_request", this.mCaptureSessionRepository.getClosingCaptureSession()))).transformAsync(new AsyncFunction(cameraDevice, sessionConfigurationCompat) { // from class: androidx.camera.camera2.internal.-$$Lambda$SynchronizedCaptureSessionImpl$m2Xqfw_ydv7rK_tiDA4dZKmwX50
                private final /* synthetic */ CameraDevice f$1;
                private final /* synthetic */ SessionConfigurationCompat f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
                public final ListenableFuture apply(Object obj) {
                    return SynchronizedCaptureSessionImpl.this.lambda$openCaptureSession$2$SynchronizedCaptureSessionImpl(this.f$1, this.f$2, (List) obj);
                }
            }, CameraXExecutors.directExecutor());
            nonCancellationPropagating = Futures.nonCancellationPropagating(this.mOpeningCaptureSession);
        }
        return nonCancellationPropagating;
    }

    public /* synthetic */ ListenableFuture lambda$openCaptureSession$2$SynchronizedCaptureSessionImpl(CameraDevice cameraDevice, SessionConfigurationCompat sessionConfigurationCompat, List v) throws Exception {
        return super.openCaptureSession(cameraDevice, sessionConfigurationCompat);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl, androidx.camera.camera2.internal.SynchronizedCaptureSession
    public ListenableFuture<Void> getSynchronizedBlocker(String feature) {
        char c;
        int hashCode = feature.hashCode();
        if (hashCode != -1937525425) {
            if (hashCode == -529927828 && feature.equals("deferrableSurface_close")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (feature.equals("wait_for_request")) {
                c = 0;
            }
            c = 65535;
        }
        if (c == 0) {
            return Futures.nonCancellationPropagating(this.mStartStreamingFuture);
        }
        if (c != 1) {
            return super.getSynchronizedBlocker(feature);
        }
        return Futures.nonCancellationPropagating(this.mClosingDeferrableSurfaceFuture);
    }

    private List<ListenableFuture<Void>> getBlockerFuture(String feature, List<SynchronizedCaptureSession> sessions) {
        List<ListenableFuture<Void>> futureList = new ArrayList<>();
        for (SynchronizedCaptureSession session : sessions) {
            futureList.add(session.getSynchronizedBlocker(feature));
        }
        return futureList;
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl, androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener.OpenerImpl
    public ListenableFuture<List<Surface>> startWithDeferrableSurface(List<DeferrableSurface> deferrableSurfaces, long timeout) {
        ListenableFuture<List<Surface>> nonCancellationPropagating;
        synchronized (this.mObjectLock) {
            this.mDeferrableSurfaces = deferrableSurfaces;
            List<ListenableFuture<Void>> futureList = Collections.emptyList();
            if (this.mEnabledFeature.contains("force_close")) {
                Map<SynchronizedCaptureSession, List<DeferrableSurface>> registeredSurfaceMap = this.mCaptureSessionRepository.registerDeferrableSurface(this, deferrableSurfaces);
                List<SynchronizedCaptureSession> sessionsWithSameSurface = new ArrayList<>();
                for (Map.Entry<SynchronizedCaptureSession, List<DeferrableSurface>> entry : registeredSurfaceMap.entrySet()) {
                    if (entry.getKey() != this && !Collections.disjoint(entry.getValue(), this.mDeferrableSurfaces)) {
                        sessionsWithSameSurface.add(entry.getKey());
                    }
                }
                futureList = getBlockerFuture("deferrableSurface_close", sessionsWithSameSurface);
            }
            this.mStartingSurface = FutureChain.from(Futures.successfulAsList(futureList)).transformAsync(new AsyncFunction(deferrableSurfaces, timeout) { // from class: androidx.camera.camera2.internal.-$$Lambda$SynchronizedCaptureSessionImpl$wl1PsdoNzau59EdJhZcEGOmtTgk
                private final /* synthetic */ List f$1;
                private final /* synthetic */ long f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
                public final ListenableFuture apply(Object obj) {
                    return SynchronizedCaptureSessionImpl.this.lambda$startWithDeferrableSurface$3$SynchronizedCaptureSessionImpl(this.f$1, this.f$2, (List) obj);
                }
            }, getExecutor());
            nonCancellationPropagating = Futures.nonCancellationPropagating(this.mStartingSurface);
        }
        return nonCancellationPropagating;
    }

    public /* synthetic */ ListenableFuture lambda$startWithDeferrableSurface$3$SynchronizedCaptureSessionImpl(List deferrableSurfaces, long timeout, List v) throws Exception {
        return super.startWithDeferrableSurface(deferrableSurfaces, timeout);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl, androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener.OpenerImpl
    public boolean stop() {
        boolean stop;
        synchronized (this.mObjectLock) {
            if (isCameraCaptureSessionOpen()) {
                closeConfiguredDeferrableSurfaces();
            } else {
                if (this.mOpeningCaptureSession != null) {
                    this.mOpeningCaptureSession.cancel(true);
                }
                if (this.mStartingSurface != null) {
                    this.mStartingSurface.cancel(true);
                }
                stopDeferrableSurface();
            }
            stop = super.stop();
        }
        return stop;
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl, androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int setSingleRepeatingRequest(CaptureRequest request, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        int singleRepeatingRequest;
        if (!this.mEnabledFeature.contains("wait_for_request")) {
            return super.setSingleRepeatingRequest(request, listener);
        }
        synchronized (this.mObjectLock) {
            this.mHasSubmittedRepeating = true;
            singleRepeatingRequest = super.setSingleRepeatingRequest(request, Camera2CaptureCallbacks.createComboCallback(this.mCaptureCallback, listener));
        }
        return singleRepeatingRequest;
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl, androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onConfigured(SynchronizedCaptureSession session) {
        SynchronizedCaptureSession s;
        SynchronizedCaptureSession s2;
        debugLog("Session onConfigured()");
        if (this.mEnabledFeature.contains("force_close")) {
            Set<SynchronizedCaptureSession> staleCreatingSessions = new LinkedHashSet<>();
            Iterator<SynchronizedCaptureSession> it = this.mCaptureSessionRepository.getCreatingCaptureSessions().iterator();
            while (it.hasNext() && (s2 = it.next()) != session) {
                staleCreatingSessions.add(s2);
            }
            forceOnConfigureFailed(staleCreatingSessions);
        }
        super.onConfigured(session);
        if (this.mEnabledFeature.contains("force_close")) {
            Set<SynchronizedCaptureSession> openedSessions = new LinkedHashSet<>();
            Iterator<SynchronizedCaptureSession> it2 = this.mCaptureSessionRepository.getCaptureSessions().iterator();
            while (it2.hasNext() && (s = it2.next()) != session) {
                openedSessions.add(s);
            }
            forceOnClosed(openedSessions);
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl, androidx.camera.camera2.internal.SynchronizedCaptureSession
    public void close() {
        debugLog("Session call close()");
        if (this.mEnabledFeature.contains("wait_for_request")) {
            synchronized (this.mObjectLock) {
                if (!this.mHasSubmittedRepeating) {
                    this.mStartStreamingFuture.cancel(true);
                }
            }
        }
        this.mStartStreamingFuture.addListener(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$SynchronizedCaptureSessionImpl$ej6SnusDBaFvWFR_5VwoLwriBIE
            @Override // java.lang.Runnable
            public final void run() {
                SynchronizedCaptureSessionImpl.this.lambda$close$4$SynchronizedCaptureSessionImpl();
            }
        }, getExecutor());
    }

    public /* synthetic */ void lambda$close$4$SynchronizedCaptureSessionImpl() {
        debugLog("Session call super.close()");
        super.close();
    }

    void closeConfiguredDeferrableSurfaces() {
        synchronized (this.mObjectLock) {
            if (this.mDeferrableSurfaces == null) {
                debugLog("deferrableSurface == null, maybe forceClose, skip close");
                return;
            }
            if (this.mEnabledFeature.contains("deferrableSurface_close")) {
                for (DeferrableSurface deferrableSurface : this.mDeferrableSurfaces) {
                    deferrableSurface.close();
                }
                debugLog("deferrableSurface closed");
                stopDeferrableSurface();
            }
        }
    }

    void stopDeferrableSurface() {
        if (this.mEnabledFeature.contains("deferrableSurface_close")) {
            this.mCaptureSessionRepository.unregisterDeferrableSurface(this);
            CallbackToFutureAdapter.Completer<Void> completer = this.mClosingDeferrableSurfaceCompleter;
            if (completer != null) {
                completer.set(null);
            }
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl, androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onClosed(SynchronizedCaptureSession session) {
        closeConfiguredDeferrableSurfaces();
        debugLog("onClosed()");
        super.onClosed(session);
    }

    static void forceOnClosed(Set<SynchronizedCaptureSession> sessions) {
        for (SynchronizedCaptureSession session : sessions) {
            session.getStateCallback().onClosed(session);
        }
    }

    private void forceOnConfigureFailed(Set<SynchronizedCaptureSession> sessions) {
        for (SynchronizedCaptureSession session : sessions) {
            session.getStateCallback().onConfigureFailed(session);
        }
    }

    void debugLog(String message) {
        if (DEBUG) {
            Log.d(TAG, "[" + this + "] " + message);
        }
    }
}
