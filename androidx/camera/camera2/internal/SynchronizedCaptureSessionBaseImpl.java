package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import androidx.camera.camera2.internal.SynchronizedCaptureSession;
import androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener;
import androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat;
import androidx.camera.camera2.internal.compat.CameraDeviceCompat;
import androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat;
import androidx.camera.camera2.internal.compat.params.SessionConfigurationCompat;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.DeferrableSurfaces;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.AsyncFunction;
import androidx.camera.core.impl.utils.futures.FutureChain;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
/* loaded from: classes.dex */
public class SynchronizedCaptureSessionBaseImpl extends SynchronizedCaptureSession.StateCallback implements SynchronizedCaptureSession, SynchronizedCaptureSessionOpener.OpenerImpl {
    CameraCaptureSessionCompat mCameraCaptureSessionCompat;
    final CaptureSessionRepository mCaptureSessionRepository;
    SynchronizedCaptureSession.StateCallback mCaptureSessionStateCallback;
    final Handler mCompatHandler;
    final Executor mExecutor;
    CallbackToFutureAdapter.Completer<Void> mOpenCaptureSessionCompleter;
    ListenableFuture<Void> mOpenCaptureSessionFuture;
    private final ScheduledExecutorService mScheduledExecutorService;
    private ListenableFuture<List<Surface>> mStartingSurface;
    private static final String TAG;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    final Object mLock = new Object();
    private boolean mClosed = false;
    private boolean mOpenerDisabled = false;

    public SynchronizedCaptureSessionBaseImpl(CaptureSessionRepository repository, Executor executor, ScheduledExecutorService scheduledExecutorService, Handler compatHandler) {
        this.mCaptureSessionRepository = repository;
        this.mCompatHandler = compatHandler;
        this.mExecutor = executor;
        this.mScheduledExecutorService = scheduledExecutorService;
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public SynchronizedCaptureSession.StateCallback getStateCallback() {
        return this;
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public ListenableFuture<Void> getSynchronizedBlocker(String feature) {
        return Futures.immediateFuture(null);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener.OpenerImpl
    public ListenableFuture<Void> openCaptureSession(CameraDevice cameraDevice, SessionConfigurationCompat sessionConfigurationCompat) {
        synchronized (this.mLock) {
            if (this.mOpenerDisabled) {
                return Futures.immediateFailedFuture(new CancellationException("Opener is disabled"));
            }
            this.mCaptureSessionRepository.onCreateCaptureSession(this);
            this.mOpenCaptureSessionFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(CameraDeviceCompat.toCameraDeviceCompat(cameraDevice, this.mCompatHandler), sessionConfigurationCompat) { // from class: androidx.camera.camera2.internal.-$$Lambda$SynchronizedCaptureSessionBaseImpl$iAm21KFIg56Hoq7CJ3MzQ_eHlJE
                private final /* synthetic */ CameraDeviceCompat f$1;
                private final /* synthetic */ SessionConfigurationCompat f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                    return SynchronizedCaptureSessionBaseImpl.this.lambda$openCaptureSession$0$SynchronizedCaptureSessionBaseImpl(this.f$1, this.f$2, completer);
                }
            });
            return Futures.nonCancellationPropagating(this.mOpenCaptureSessionFuture);
        }
    }

    public /* synthetic */ Object lambda$openCaptureSession$0$SynchronizedCaptureSessionBaseImpl(CameraDeviceCompat cameraDeviceCompat, SessionConfigurationCompat sessionConfigurationCompat, CallbackToFutureAdapter.Completer completer) throws Exception {
        String str;
        synchronized (this.mLock) {
            Preconditions.checkState(this.mOpenCaptureSessionCompleter == null, "The openCaptureSessionCompleter can only set once!");
            this.mOpenCaptureSessionCompleter = completer;
            cameraDeviceCompat.createCaptureSession(sessionConfigurationCompat);
            str = "openCaptureSession[session=" + this + "]";
        }
        return str;
    }

    boolean isCameraCaptureSessionOpen() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mOpenCaptureSessionFuture != null;
        }
        return z;
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener.OpenerImpl
    public SessionConfigurationCompat createSessionConfigurationCompat(int sessionType, List<OutputConfigurationCompat> outputsCompat, SynchronizedCaptureSession.StateCallback stateCallback) {
        this.mCaptureSessionStateCallback = stateCallback;
        return new SessionConfigurationCompat(sessionType, outputsCompat, getExecutor(), new CameraCaptureSession.StateCallback() { // from class: androidx.camera.camera2.internal.SynchronizedCaptureSessionBaseImpl.1
            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onReady(CameraCaptureSession session) {
                SynchronizedCaptureSessionBaseImpl.this.createCaptureSessionCompat(session);
                SynchronizedCaptureSessionBaseImpl synchronizedCaptureSessionBaseImpl = SynchronizedCaptureSessionBaseImpl.this;
                synchronizedCaptureSessionBaseImpl.onReady(synchronizedCaptureSessionBaseImpl);
            }

            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onActive(CameraCaptureSession session) {
                SynchronizedCaptureSessionBaseImpl.this.createCaptureSessionCompat(session);
                SynchronizedCaptureSessionBaseImpl synchronizedCaptureSessionBaseImpl = SynchronizedCaptureSessionBaseImpl.this;
                synchronizedCaptureSessionBaseImpl.onActive(synchronizedCaptureSessionBaseImpl);
            }

            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onCaptureQueueEmpty(CameraCaptureSession session) {
                SynchronizedCaptureSessionBaseImpl.this.createCaptureSessionCompat(session);
                SynchronizedCaptureSessionBaseImpl synchronizedCaptureSessionBaseImpl = SynchronizedCaptureSessionBaseImpl.this;
                synchronizedCaptureSessionBaseImpl.onCaptureQueueEmpty(synchronizedCaptureSessionBaseImpl);
            }

            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onSurfacePrepared(CameraCaptureSession session, Surface surface) {
                SynchronizedCaptureSessionBaseImpl.this.createCaptureSessionCompat(session);
                SynchronizedCaptureSessionBaseImpl synchronizedCaptureSessionBaseImpl = SynchronizedCaptureSessionBaseImpl.this;
                synchronizedCaptureSessionBaseImpl.onSurfacePrepared(synchronizedCaptureSessionBaseImpl, surface);
            }

            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onConfigured(CameraCaptureSession session) {
                try {
                    SynchronizedCaptureSessionBaseImpl.this.createCaptureSessionCompat(session);
                    SynchronizedCaptureSessionBaseImpl.this.onConfigured(SynchronizedCaptureSessionBaseImpl.this);
                    synchronized (SynchronizedCaptureSessionBaseImpl.this.mLock) {
                        try {
                            Preconditions.checkNotNull(SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter, "OpenCaptureSession completer should not null");
                            SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter.set(null);
                            SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter = null;
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    synchronized (SynchronizedCaptureSessionBaseImpl.this.mLock) {
                        try {
                            Preconditions.checkNotNull(SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter, "OpenCaptureSession completer should not null");
                            SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter.set(null);
                            SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter = null;
                            throw th2;
                        } catch (Throwable th3) {
                            throw th3;
                        }
                    }
                }
            }

            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onConfigureFailed(CameraCaptureSession session) {
                try {
                    SynchronizedCaptureSessionBaseImpl.this.createCaptureSessionCompat(session);
                    SynchronizedCaptureSessionBaseImpl.this.onConfigureFailed(SynchronizedCaptureSessionBaseImpl.this);
                    synchronized (SynchronizedCaptureSessionBaseImpl.this.mLock) {
                        try {
                            Preconditions.checkNotNull(SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter, "OpenCaptureSession completer should not null");
                            SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter.setException(new IllegalStateException("onConfigureFailed"));
                            SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter = null;
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    synchronized (SynchronizedCaptureSessionBaseImpl.this.mLock) {
                        try {
                            Preconditions.checkNotNull(SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter, "OpenCaptureSession completer should not null");
                            SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter.setException(new IllegalStateException("onConfigureFailed"));
                            SynchronizedCaptureSessionBaseImpl.this.mOpenCaptureSessionCompleter = null;
                            throw th2;
                        } catch (Throwable th3) {
                            throw th3;
                        }
                    }
                }
            }

            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onClosed(CameraCaptureSession session) {
                SynchronizedCaptureSessionBaseImpl.this.createCaptureSessionCompat(session);
                SynchronizedCaptureSessionBaseImpl synchronizedCaptureSessionBaseImpl = SynchronizedCaptureSessionBaseImpl.this;
                synchronizedCaptureSessionBaseImpl.onClosed(synchronizedCaptureSessionBaseImpl);
            }
        });
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener.OpenerImpl
    public Executor getExecutor() {
        return this.mExecutor;
    }

    void createCaptureSessionCompat(CameraCaptureSession session) {
        if (this.mCameraCaptureSessionCompat == null) {
            this.mCameraCaptureSessionCompat = CameraCaptureSessionCompat.toCameraCaptureSessionCompat(session, this.mCompatHandler);
        }
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener.OpenerImpl
    public ListenableFuture<List<Surface>> startWithDeferrableSurface(List<DeferrableSurface> deferrableSurfaces, long timeout) {
        synchronized (this.mLock) {
            if (this.mOpenerDisabled) {
                return Futures.immediateFailedFuture(new CancellationException("Opener is disabled"));
            }
            this.mStartingSurface = FutureChain.from(DeferrableSurfaces.surfaceListWithTimeout(deferrableSurfaces, false, timeout, getExecutor(), this.mScheduledExecutorService)).transformAsync(new AsyncFunction(deferrableSurfaces) { // from class: androidx.camera.camera2.internal.-$$Lambda$SynchronizedCaptureSessionBaseImpl$5ncqzCi3JU34OQRVJlJ4HfEus5I
                private final /* synthetic */ List f$1;

                {
                    this.f$1 = r2;
                }

                @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
                public final ListenableFuture apply(Object obj) {
                    return SynchronizedCaptureSessionBaseImpl.this.lambda$startWithDeferrableSurface$1$SynchronizedCaptureSessionBaseImpl(this.f$1, (List) obj);
                }
            }, getExecutor());
            return Futures.nonCancellationPropagating(this.mStartingSurface);
        }
    }

    public /* synthetic */ ListenableFuture lambda$startWithDeferrableSurface$1$SynchronizedCaptureSessionBaseImpl(List deferrableSurfaces, List surfaces) throws Exception {
        debugLog("getSurface...done");
        if (surfaces.contains(null)) {
            return Futures.immediateFailedFuture(new DeferrableSurface.SurfaceClosedException("Surface closed", (DeferrableSurface) deferrableSurfaces.get(surfaces.indexOf(null))));
        }
        if (surfaces.isEmpty()) {
            return Futures.immediateFailedFuture(new IllegalArgumentException("Unable to open capture session without surfaces"));
        }
        return Futures.immediateFuture(surfaces);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener.OpenerImpl
    public boolean stop() {
        boolean z;
        synchronized (this.mLock) {
            z = true;
            if (!this.mOpenerDisabled) {
                if (this.mStartingSurface != null) {
                    this.mStartingSurface.cancel(true);
                }
                this.mOpenerDisabled = true;
            }
            if (isCameraCaptureSessionOpen()) {
                z = false;
            }
        }
        return z;
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public CameraCaptureSessionCompat toCameraCaptureSessionCompat() {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat);
        return this.mCameraCaptureSessionCompat;
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public CameraDevice getDevice() {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat);
        return this.mCameraCaptureSessionCompat.toCameraCaptureSession().getDevice();
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int captureSingleRequest(CaptureRequest request, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        return this.mCameraCaptureSessionCompat.captureSingleRequest(request, getExecutor(), listener);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int captureBurstRequests(List<CaptureRequest> requests, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        return this.mCameraCaptureSessionCompat.captureBurstRequests(requests, getExecutor(), listener);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int setSingleRepeatingRequest(CaptureRequest request, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        return this.mCameraCaptureSessionCompat.setSingleRepeatingRequest(request, getExecutor(), listener);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int setRepeatingBurstRequests(List<CaptureRequest> requests, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        return this.mCameraCaptureSessionCompat.setRepeatingBurstRequests(requests, getExecutor(), listener);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int captureSingleRequest(CaptureRequest request, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        return this.mCameraCaptureSessionCompat.captureSingleRequest(request, executor, listener);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int captureBurstRequests(List<CaptureRequest> requests, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        return this.mCameraCaptureSessionCompat.captureBurstRequests(requests, executor, listener);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int setSingleRepeatingRequest(CaptureRequest request, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        return this.mCameraCaptureSessionCompat.setSingleRepeatingRequest(request, executor, listener);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public int setRepeatingBurstRequests(List<CaptureRequest> requests, Executor executor, CameraCaptureSession.CaptureCallback listener) throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        return this.mCameraCaptureSessionCompat.setRepeatingBurstRequests(requests, executor, listener);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public void stopRepeating() throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        this.mCameraCaptureSessionCompat.toCameraCaptureSession().stopRepeating();
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public void abortCaptures() throws CameraAccessException {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        this.mCameraCaptureSessionCompat.toCameraCaptureSession().abortCaptures();
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession
    public void close() {
        Preconditions.checkNotNull(this.mCameraCaptureSessionCompat, "Need to call openCaptureSession before using this API.");
        this.mCaptureSessionRepository.onCaptureSessionClosing(this);
        this.mCameraCaptureSessionCompat.toCameraCaptureSession().close();
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onReady(SynchronizedCaptureSession session) {
        this.mCaptureSessionStateCallback.onReady(session);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onActive(SynchronizedCaptureSession session) {
        this.mCaptureSessionStateCallback.onActive(session);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onCaptureQueueEmpty(SynchronizedCaptureSession session) {
        this.mCaptureSessionStateCallback.onCaptureQueueEmpty(session);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onSurfacePrepared(SynchronizedCaptureSession session, Surface surface) {
        this.mCaptureSessionStateCallback.onSurfacePrepared(session, surface);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onConfigured(SynchronizedCaptureSession session) {
        this.mCaptureSessionRepository.onCaptureSessionCreated(this);
        this.mCaptureSessionStateCallback.onConfigured(session);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onConfigureFailed(SynchronizedCaptureSession session) {
        this.mCaptureSessionRepository.onCaptureSessionConfigureFail(this);
        this.mCaptureSessionStateCallback.onConfigureFailed(session);
    }

    @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
    public void onClosed(SynchronizedCaptureSession session) {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                this.mClosed = true;
                Preconditions.checkNotNull(this.mOpenCaptureSessionFuture, "Need to call openCaptureSession before using this API.");
                this.mOpenCaptureSessionFuture.addListener(new Runnable(session) { // from class: androidx.camera.camera2.internal.-$$Lambda$SynchronizedCaptureSessionBaseImpl$yxhW1Wsa26wLSufbPdGlBwCdzW8
                    private final /* synthetic */ SynchronizedCaptureSession f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        SynchronizedCaptureSessionBaseImpl.this.lambda$onClosed$2$SynchronizedCaptureSessionBaseImpl(this.f$1);
                    }
                }, CameraXExecutors.directExecutor());
            }
        }
    }

    public /* synthetic */ void lambda$onClosed$2$SynchronizedCaptureSessionBaseImpl(SynchronizedCaptureSession session) {
        this.mCaptureSessionRepository.onCaptureSessionClosed(this);
        this.mCaptureSessionStateCallback.onClosed(session);
    }

    private void debugLog(String message) {
        if (DEBUG) {
            Log.d(TAG, "[" + this + "] " + message);
        }
    }
}
