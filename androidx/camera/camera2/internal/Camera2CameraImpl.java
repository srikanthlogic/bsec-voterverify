package androidx.camera.camera2.internal;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import androidx.camera.camera2.internal.Camera2CameraImpl;
import androidx.camera.camera2.internal.SynchronizedCaptureSessionOpener;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.camera2.internal.compat.CameraManagerCompat;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.CameraInfoInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.CameraStateRegistry;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.ImmediateSurface;
import androidx.camera.core.impl.LiveDataObservable;
import androidx.camera.core.impl.Observable;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseAttachState;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public final class Camera2CameraImpl implements CameraInternal {
    private static final int ERROR_NONE;
    private final CameraAvailability mCameraAvailability;
    private final Camera2CameraControl mCameraControlInternal;
    CameraDevice mCameraDevice;
    final Camera2CameraInfoImpl mCameraInfoInternal;
    private final CameraManagerCompat mCameraManager;
    private final CameraStateRegistry mCameraStateRegistry;
    CaptureSession mCaptureSession;
    private final SynchronizedCaptureSessionOpener.Builder mCaptureSessionOpenerBuilder;
    private final CaptureSessionRepository mCaptureSessionRepository;
    private final Executor mExecutor;
    private MeteringRepeatingSession mMeteringRepeatingSession;
    private final StateCallback mStateCallback;
    private final UseCaseAttachState mUseCaseAttachState;
    ListenableFuture<Void> mUserReleaseFuture;
    CallbackToFutureAdapter.Completer<Void> mUserReleaseNotifier;
    private static final String TAG;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    volatile InternalState mState = InternalState.INITIALIZED;
    private final LiveDataObservable<CameraInternal.State> mObservableState = new LiveDataObservable<>();
    int mCameraDeviceError = 0;
    SessionConfig mCameraControlSessionConfig = SessionConfig.defaultEmptySessionConfig();
    final AtomicInteger mReleaseRequestCount = new AtomicInteger(0);
    final Map<CaptureSession, ListenableFuture<Void>> mReleasedCaptureSessions = new LinkedHashMap();
    final Set<CaptureSession> mConfiguringForClose = new HashSet();

    /* loaded from: classes.dex */
    public enum InternalState {
        INITIALIZED,
        PENDING_OPEN,
        OPENING,
        OPENED,
        CLOSING,
        REOPENING,
        RELEASING,
        RELEASED
    }

    public Camera2CameraImpl(CameraManagerCompat cameraManager, String cameraId, CameraStateRegistry cameraStateRegistry, Executor executor, Handler schedulerHandler) throws CameraUnavailableException {
        this.mCameraManager = cameraManager;
        this.mCameraStateRegistry = cameraStateRegistry;
        ScheduledExecutorService executorScheduler = CameraXExecutors.newHandlerExecutor(schedulerHandler);
        this.mExecutor = CameraXExecutors.newSequentialExecutor(executor);
        this.mStateCallback = new StateCallback(this.mExecutor, executorScheduler);
        this.mUseCaseAttachState = new UseCaseAttachState(cameraId);
        this.mObservableState.postValue(CameraInternal.State.CLOSED);
        this.mCaptureSessionRepository = new CaptureSessionRepository(this.mExecutor);
        try {
            CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(cameraId);
            this.mCameraControlInternal = new Camera2CameraControl(cameraCharacteristics, executorScheduler, this.mExecutor, new ControlUpdateListenerInternal());
            this.mCameraInfoInternal = new Camera2CameraInfoImpl(cameraId, cameraCharacteristics, this.mCameraControlInternal);
            this.mCaptureSessionOpenerBuilder = new SynchronizedCaptureSessionOpener.Builder(this.mExecutor, executorScheduler, schedulerHandler, this.mCaptureSessionRepository, this.mCameraInfoInternal.getSupportedHardwareLevel());
            this.mCaptureSession = new CaptureSession();
            this.mCameraAvailability = new CameraAvailability(cameraId);
            this.mCameraStateRegistry.registerCamera(this, this.mExecutor, this.mCameraAvailability);
            this.mCameraManager.registerAvailabilityCallback(this.mExecutor, this.mCameraAvailability);
        } catch (CameraAccessExceptionCompat e) {
            throw CameraUnavailableExceptionHelper.createFrom(e);
        }
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public void open() {
        this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$tFCuzMJVSd_6gQqkOS71yo2JPHA
            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.this.openInternal();
            }
        });
    }

    public void openInternal() {
        int i = AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$Camera2CameraImpl$InternalState[this.mState.ordinal()];
        boolean z = true;
        if (i == 1) {
            openCameraDevice();
        } else if (i != 2) {
            debugLog("open() ignored due to being in state: " + this.mState);
        } else {
            setState(InternalState.REOPENING);
            if (!isSessionCloseComplete() && this.mCameraDeviceError == 0) {
                if (this.mCameraDevice == null) {
                    z = false;
                }
                Preconditions.checkState(z, "Camera Device should be open if session close is not complete");
                setState(InternalState.OPENED);
                openCaptureSession();
            }
        }
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public void close() {
        this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$UC0M7m80_HJTLdlzE6dfb6TvRHY
            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.this.closeInternal();
            }
        });
    }

    public void closeInternal() {
        debugLog("Closing camera.");
        int i = AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$Camera2CameraImpl$InternalState[this.mState.ordinal()];
        boolean z = false;
        if (i == 3) {
            setState(InternalState.CLOSING);
            closeCamera(false);
        } else if (i == 4 || i == 5) {
            boolean canFinish = this.mStateCallback.cancelScheduledReopen();
            setState(InternalState.CLOSING);
            if (canFinish) {
                Preconditions.checkState(isSessionCloseComplete());
                finishClose();
            }
        } else if (i != 6) {
            debugLog("close() ignored due to being in state: " + this.mState);
        } else {
            if (this.mCameraDevice == null) {
                z = true;
            }
            Preconditions.checkState(z);
            setState(InternalState.INITIALIZED);
        }
    }

    private void configAndClose(boolean abortInFlightCaptures) {
        CaptureSession dummySession = new CaptureSession();
        this.mConfiguringForClose.add(dummySession);
        resetCaptureSession(abortInFlightCaptures);
        SurfaceTexture surfaceTexture = new SurfaceTexture(0);
        surfaceTexture.setDefaultBufferSize(640, 480);
        Surface surface = new Surface(surfaceTexture);
        Runnable closeAndCleanupRunner = new Runnable(surface, surfaceTexture) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$uk0LTTpHa2F5WWH3KTX1TkFW3ps
            private final /* synthetic */ Surface f$0;
            private final /* synthetic */ SurfaceTexture f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.lambda$configAndClose$0(this.f$0, this.f$1);
            }
        };
        SessionConfig.Builder builder = new SessionConfig.Builder();
        builder.addNonRepeatingSurface(new ImmediateSurface(surface));
        builder.setTemplateType(1);
        debugLog("Start configAndClose.");
        dummySession.open(builder.build(), (CameraDevice) Preconditions.checkNotNull(this.mCameraDevice), this.mCaptureSessionOpenerBuilder.build()).addListener(new Runnable(dummySession, closeAndCleanupRunner) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$3S3aTdh40yChUwuO91TuA3gtQpQ
            private final /* synthetic */ CaptureSession f$1;
            private final /* synthetic */ Runnable f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.this.lambda$configAndClose$1$Camera2CameraImpl(this.f$1, this.f$2);
            }
        }, this.mExecutor);
    }

    public static /* synthetic */ void lambda$configAndClose$0(Surface surface, SurfaceTexture surfaceTexture) {
        surface.release();
        surfaceTexture.release();
    }

    /* renamed from: releaseDummySession */
    public void lambda$configAndClose$1$Camera2CameraImpl(CaptureSession dummySession, Runnable closeAndCleanupRunner) {
        this.mConfiguringForClose.remove(dummySession);
        releaseSession(dummySession, false).addListener(closeAndCleanupRunner, CameraXExecutors.directExecutor());
    }

    boolean isSessionCloseComplete() {
        return this.mReleasedCaptureSessions.isEmpty() && this.mConfiguringForClose.isEmpty();
    }

    void finishClose() {
        Preconditions.checkState(this.mState == InternalState.RELEASING || this.mState == InternalState.CLOSING);
        Preconditions.checkState(this.mReleasedCaptureSessions.isEmpty());
        this.mCameraDevice = null;
        if (this.mState == InternalState.CLOSING) {
            setState(InternalState.INITIALIZED);
            return;
        }
        this.mCameraManager.unregisterAvailabilityCallback(this.mCameraAvailability);
        setState(InternalState.RELEASED);
        CallbackToFutureAdapter.Completer<Void> completer = this.mUserReleaseNotifier;
        if (completer != null) {
            completer.set(null);
            this.mUserReleaseNotifier = null;
        }
    }

    void closeCamera(boolean abortInFlightCaptures) {
        boolean z = this.mState == InternalState.CLOSING || this.mState == InternalState.RELEASING || (this.mState == InternalState.REOPENING && this.mCameraDeviceError != 0);
        Preconditions.checkState(z, "closeCamera should only be called in a CLOSING, RELEASING or REOPENING (with error) state. Current state: " + this.mState + " (error: " + getErrorMessage(this.mCameraDeviceError) + ")");
        if (Build.VERSION.SDK_INT <= 23 || Build.VERSION.SDK_INT >= 29 || !isLegacyDevice() || this.mCameraDeviceError != 0) {
            resetCaptureSession(abortInFlightCaptures);
        } else {
            configAndClose(abortInFlightCaptures);
        }
        this.mCaptureSession.cancelIssuedCaptureRequests();
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public ListenableFuture<Void> release() {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$I4ICaEGwGpnGTK2_pERRZYTeBII
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return Camera2CameraImpl.this.lambda$release$3$Camera2CameraImpl(completer);
            }
        });
    }

    public /* synthetic */ Object lambda$release$3$Camera2CameraImpl(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new Runnable(completer) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$--IbQFfVbZBU_9CA8XMO1i0xvXA
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.this.lambda$release$2$Camera2CameraImpl(this.f$1);
            }
        });
        return "Release[request=" + this.mReleaseRequestCount.getAndIncrement() + "]";
    }

    public /* synthetic */ void lambda$release$2$Camera2CameraImpl(CallbackToFutureAdapter.Completer completer) {
        Futures.propagate(releaseInternal(), completer);
    }

    private ListenableFuture<Void> releaseInternal() {
        ListenableFuture<Void> future = getOrCreateUserReleaseFuture();
        boolean z = true;
        switch (this.mState) {
            case INITIALIZED:
            case PENDING_OPEN:
                if (this.mCameraDevice != null) {
                    z = false;
                }
                Preconditions.checkState(z);
                setState(InternalState.RELEASING);
                Preconditions.checkState(isSessionCloseComplete());
                finishClose();
                break;
            case CLOSING:
            case OPENING:
            case REOPENING:
            case RELEASING:
                boolean canFinish = this.mStateCallback.cancelScheduledReopen();
                setState(InternalState.RELEASING);
                if (canFinish) {
                    Preconditions.checkState(isSessionCloseComplete());
                    finishClose();
                    break;
                }
                break;
            case OPENED:
                setState(InternalState.RELEASING);
                closeCamera(true);
                break;
            default:
                debugLog("release() ignored due to being in state: " + this.mState);
                break;
        }
        return future;
    }

    private ListenableFuture<Void> getOrCreateUserReleaseFuture() {
        if (this.mUserReleaseFuture == null) {
            if (this.mState != InternalState.RELEASED) {
                this.mUserReleaseFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$fnN5ewxA8yqwlEbnC6o6ytxg7Og
                    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                        return Camera2CameraImpl.this.lambda$getOrCreateUserReleaseFuture$4$Camera2CameraImpl(completer);
                    }
                });
            } else {
                this.mUserReleaseFuture = Futures.immediateFuture(null);
            }
        }
        return this.mUserReleaseFuture;
    }

    public /* synthetic */ Object lambda$getOrCreateUserReleaseFuture$4$Camera2CameraImpl(CallbackToFutureAdapter.Completer completer) throws Exception {
        Preconditions.checkState(this.mUserReleaseNotifier == null, "Camera can only be released once, so release completer should be null on creation.");
        this.mUserReleaseNotifier = completer;
        return "Release[camera=" + this + "]";
    }

    ListenableFuture<Void> releaseSession(final CaptureSession captureSession, boolean abortInFlightCaptures) {
        captureSession.close();
        ListenableFuture<Void> releaseFuture = captureSession.release(abortInFlightCaptures);
        debugLog("Releasing session in state " + this.mState.name());
        this.mReleasedCaptureSessions.put(captureSession, releaseFuture);
        Futures.addCallback(releaseFuture, new FutureCallback<Void>() { // from class: androidx.camera.camera2.internal.Camera2CameraImpl.1
            public void onSuccess(Void result) {
                Camera2CameraImpl.this.mReleasedCaptureSessions.remove(captureSession);
                int i = AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$Camera2CameraImpl$InternalState[Camera2CameraImpl.this.mState.ordinal()];
                if (i != 2) {
                    if (i != 5) {
                        if (i != 7) {
                            return;
                        }
                    } else if (Camera2CameraImpl.this.mCameraDeviceError == 0) {
                        return;
                    }
                }
                if (Camera2CameraImpl.this.isSessionCloseComplete() && Camera2CameraImpl.this.mCameraDevice != null) {
                    Camera2CameraImpl.this.mCameraDevice.close();
                    Camera2CameraImpl.this.mCameraDevice = null;
                }
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
            }
        }, CameraXExecutors.directExecutor());
        return releaseFuture;
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public Observable<CameraInternal.State> getCameraState() {
        return this.mObservableState;
    }

    @Override // androidx.camera.core.UseCase.StateChangeCallback
    public void onUseCaseActive(UseCase useCase) {
        Preconditions.checkNotNull(useCase);
        this.mExecutor.execute(new Runnable(useCase) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$TqOLYs7O5pFuyEaRANkVEpl_k3U
            private final /* synthetic */ UseCase f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.this.lambda$onUseCaseActive$5$Camera2CameraImpl(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$onUseCaseActive$5$Camera2CameraImpl(UseCase useCase) {
        debugLog("Use case " + useCase + " ACTIVE");
        try {
            UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
            useCaseAttachState.setUseCaseActive(useCase.getName() + useCase.hashCode(), useCase.getSessionConfig());
            UseCaseAttachState useCaseAttachState2 = this.mUseCaseAttachState;
            useCaseAttachState2.updateUseCase(useCase.getName() + useCase.hashCode(), useCase.getSessionConfig());
            updateCaptureSessionConfig();
        } catch (NullPointerException e) {
            debugLog("Failed to set already detached use case active");
        }
    }

    @Override // androidx.camera.core.UseCase.StateChangeCallback
    public void onUseCaseInactive(UseCase useCase) {
        Preconditions.checkNotNull(useCase);
        this.mExecutor.execute(new Runnable(useCase) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$L-qXzxII6_fhY_FN_HX18hvqHpE
            private final /* synthetic */ UseCase f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.this.lambda$onUseCaseInactive$6$Camera2CameraImpl(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$onUseCaseInactive$6$Camera2CameraImpl(UseCase useCase) {
        debugLog("Use case " + useCase + " INACTIVE");
        UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
        useCaseAttachState.setUseCaseInactive(useCase.getName() + useCase.hashCode());
        updateCaptureSessionConfig();
    }

    @Override // androidx.camera.core.UseCase.StateChangeCallback
    public void onUseCaseUpdated(UseCase useCase) {
        Preconditions.checkNotNull(useCase);
        this.mExecutor.execute(new Runnable(useCase) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$vO-c9aUYb6eNCRc13DH-Z2rnVcg
            private final /* synthetic */ UseCase f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.this.lambda$onUseCaseUpdated$7$Camera2CameraImpl(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$onUseCaseUpdated$7$Camera2CameraImpl(UseCase useCase) {
        debugLog("Use case " + useCase + " UPDATED");
        UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
        useCaseAttachState.updateUseCase(useCase.getName() + useCase.hashCode(), useCase.getSessionConfig());
        updateCaptureSessionConfig();
    }

    @Override // androidx.camera.core.UseCase.StateChangeCallback
    public void onUseCaseReset(UseCase useCase) {
        Preconditions.checkNotNull(useCase);
        this.mExecutor.execute(new Runnable(useCase) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$tV0aW446OWa1ioZ6NZspALhNXAg
            private final /* synthetic */ UseCase f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.this.lambda$onUseCaseReset$8$Camera2CameraImpl(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$onUseCaseReset$8$Camera2CameraImpl(UseCase useCase) {
        debugLog("Use case " + useCase + " RESET");
        UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
        useCaseAttachState.updateUseCase(useCase.getName() + useCase.hashCode(), useCase.getSessionConfig());
        resetCaptureSession(false);
        updateCaptureSessionConfig();
        if (this.mState == InternalState.OPENED) {
            openCaptureSession();
        }
    }

    boolean isUseCaseAttached(UseCase useCase) {
        try {
            return ((Boolean) CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(useCase) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$QLwD2cUYUUmwLmvxBgG5eOGnncc
                private final /* synthetic */ UseCase f$1;

                {
                    this.f$1 = r2;
                }

                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                    return Camera2CameraImpl.this.lambda$isUseCaseAttached$10$Camera2CameraImpl(this.f$1, completer);
                }
            }).get()).booleanValue();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Unable to check if use case is attached.", e);
        }
    }

    public /* synthetic */ Object lambda$isUseCaseAttached$10$Camera2CameraImpl(UseCase useCase, CallbackToFutureAdapter.Completer completer) throws Exception {
        try {
            this.mExecutor.execute(new Runnable(completer, useCase) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$mSWTvsc0ebaIZRsbXQgjplLn5L4
                private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;
                private final /* synthetic */ UseCase f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Camera2CameraImpl.this.lambda$isUseCaseAttached$9$Camera2CameraImpl(this.f$1, this.f$2);
                }
            });
            return "isUseCaseAttached";
        } catch (RejectedExecutionException e) {
            completer.setException(new RuntimeException("Unable to check if use case is attached. Camera executor shut down."));
            return "isUseCaseAttached";
        }
    }

    public /* synthetic */ void lambda$isUseCaseAttached$9$Camera2CameraImpl(CallbackToFutureAdapter.Completer completer, UseCase useCase) {
        UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
        completer.set(Boolean.valueOf(useCaseAttachState.isUseCaseAttached(useCase.getName() + useCase.hashCode())));
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public void attachUseCases(Collection<UseCase> useCases) {
        if (!useCases.isEmpty()) {
            this.mCameraControlInternal.setActive(true);
            this.mExecutor.execute(new Runnable(useCases) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$nOBAVkSAnTyDsQ_vejcv0JSoMKQ
                private final /* synthetic */ Collection f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Camera2CameraImpl.this.lambda$attachUseCases$11$Camera2CameraImpl(this.f$1);
                }
            });
        }
    }

    /* renamed from: tryAttachUseCases */
    public void lambda$attachUseCases$11$Camera2CameraImpl(Collection<UseCase> toAdd) {
        List<UseCase> useCasesToAttach = new ArrayList<>();
        for (UseCase useCase : toAdd) {
            UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
            if (!useCaseAttachState.isUseCaseAttached(useCase.getName() + useCase.hashCode())) {
                try {
                    UseCaseAttachState useCaseAttachState2 = this.mUseCaseAttachState;
                    useCaseAttachState2.setUseCaseAttached(useCase.getName() + useCase.hashCode(), useCase.getSessionConfig());
                    useCasesToAttach.add(useCase);
                } catch (NullPointerException e) {
                    debugLog("Failed to attach a detached use case");
                }
            }
        }
        if (!useCasesToAttach.isEmpty()) {
            debugLog("Use cases [" + TextUtils.join(", ", useCasesToAttach) + "] now ATTACHED");
            notifyStateAttachedToUseCases(useCasesToAttach);
            addOrRemoveMeteringRepeatingUseCase();
            updateCaptureSessionConfig();
            resetCaptureSession(false);
            if (this.mState == InternalState.OPENED) {
                openCaptureSession();
            } else {
                openInternal();
            }
            updateCameraControlPreviewAspectRatio(useCasesToAttach);
        }
    }

    private void notifyStateAttachedToUseCases(List<UseCase> useCases) {
        CameraXExecutors.mainThreadExecutor().execute(new Runnable(useCases) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$ZhXyRKdDTGDSNtphxGVvobAvnWY
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.lambda$notifyStateAttachedToUseCases$12(this.f$0);
            }
        });
    }

    public static /* synthetic */ void lambda$notifyStateAttachedToUseCases$12(List useCases) {
        Iterator it = useCases.iterator();
        while (it.hasNext()) {
            ((UseCase) it.next()).onStateAttached();
        }
    }

    private void notifyStateDetachedToUseCases(List<UseCase> useCases) {
        CameraXExecutors.mainThreadExecutor().execute(new Runnable(useCases) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$c55wJ4MPpfiWkHRPerKOlb9dxzY
            private final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraImpl.lambda$notifyStateDetachedToUseCases$13(this.f$0);
            }
        });
    }

    public static /* synthetic */ void lambda$notifyStateDetachedToUseCases$13(List useCases) {
        Iterator it = useCases.iterator();
        while (it.hasNext()) {
            ((UseCase) it.next()).onStateDetached();
        }
    }

    private void updateCameraControlPreviewAspectRatio(Collection<UseCase> useCases) {
        for (UseCase useCase : useCases) {
            if (useCase instanceof Preview) {
                Size resolution = (Size) Preconditions.checkNotNull(useCase.getAttachedSurfaceResolution());
                this.mCameraControlInternal.setPreviewAspectRatio(new Rational(resolution.getWidth(), resolution.getHeight()));
                return;
            }
        }
    }

    private void clearCameraControlPreviewAspectRatio(Collection<UseCase> removedUseCases) {
        for (UseCase useCase : removedUseCases) {
            if (useCase instanceof Preview) {
                this.mCameraControlInternal.setPreviewAspectRatio(null);
                return;
            }
        }
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public void detachUseCases(Collection<UseCase> useCases) {
        if (!useCases.isEmpty()) {
            this.mExecutor.execute(new Runnable(useCases) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$GaWnsjdQQY6l5FHUcSCQs9qTwWw
                private final /* synthetic */ Collection f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Camera2CameraImpl.this.lambda$detachUseCases$14$Camera2CameraImpl(this.f$1);
                }
            });
        }
    }

    /* renamed from: tryDetachUseCases */
    public void lambda$detachUseCases$14$Camera2CameraImpl(Collection<UseCase> toRemove) {
        List<UseCase> useCasesToDetach = new ArrayList<>();
        for (UseCase useCase : toRemove) {
            UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
            if (useCaseAttachState.isUseCaseAttached(useCase.getName() + useCase.hashCode())) {
                UseCaseAttachState useCaseAttachState2 = this.mUseCaseAttachState;
                useCaseAttachState2.setUseCaseDetached(useCase.getName() + useCase.hashCode());
                useCasesToDetach.add(useCase);
            }
        }
        if (!useCasesToDetach.isEmpty()) {
            debugLog("Use cases [" + TextUtils.join(", ", useCasesToDetach) + "] now DETACHED for camera");
            clearCameraControlPreviewAspectRatio(useCasesToDetach);
            notifyStateDetachedToUseCases(useCasesToDetach);
            addOrRemoveMeteringRepeatingUseCase();
            if (this.mUseCaseAttachState.getAttachedSessionConfigs().isEmpty()) {
                this.mCameraControlInternal.setActive(false);
                resetCaptureSession(false);
                this.mCaptureSession = new CaptureSession();
                closeInternal();
                return;
            }
            updateCaptureSessionConfig();
            resetCaptureSession(false);
            if (this.mState == InternalState.OPENED) {
                openCaptureSession();
            }
        }
    }

    private void addOrRemoveMeteringRepeatingUseCase() {
        SessionConfig sessionConfig = this.mUseCaseAttachState.getAttachedBuilder().build();
        CaptureConfig captureConfig = sessionConfig.getRepeatingCaptureConfig();
        int sizeRepeatingSurfaces = captureConfig.getSurfaces().size();
        int sizeSessionSurfaces = sessionConfig.getSurfaces().size();
        if (sessionConfig.getSurfaces().isEmpty()) {
            return;
        }
        if (captureConfig.getSurfaces().isEmpty()) {
            if (this.mMeteringRepeatingSession == null) {
                this.mMeteringRepeatingSession = new MeteringRepeatingSession();
            }
            addMeteringRepeating();
        } else if (sizeSessionSurfaces == 1 && sizeRepeatingSurfaces == 1) {
            removeMeteringRepeating();
        } else if (sizeRepeatingSurfaces >= 2) {
            removeMeteringRepeating();
        } else {
            Log.d(TAG, "mMeteringRepeating is ATTACHED, SessionConfig Surfaces: " + sizeSessionSurfaces + ", CaptureConfig Surfaces: " + sizeRepeatingSurfaces);
        }
    }

    private void removeMeteringRepeating() {
        if (this.mMeteringRepeatingSession != null) {
            UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
            useCaseAttachState.setUseCaseDetached(this.mMeteringRepeatingSession.getName() + this.mMeteringRepeatingSession.hashCode());
            UseCaseAttachState useCaseAttachState2 = this.mUseCaseAttachState;
            useCaseAttachState2.setUseCaseInactive(this.mMeteringRepeatingSession.getName() + this.mMeteringRepeatingSession.hashCode());
            this.mMeteringRepeatingSession.clear();
            this.mMeteringRepeatingSession = null;
        }
    }

    private void addMeteringRepeating() {
        if (this.mMeteringRepeatingSession != null) {
            UseCaseAttachState useCaseAttachState = this.mUseCaseAttachState;
            useCaseAttachState.setUseCaseAttached(this.mMeteringRepeatingSession.getName() + this.mMeteringRepeatingSession.hashCode(), this.mMeteringRepeatingSession.getSessionConfig());
            UseCaseAttachState useCaseAttachState2 = this.mUseCaseAttachState;
            useCaseAttachState2.setUseCaseActive(this.mMeteringRepeatingSession.getName() + this.mMeteringRepeatingSession.hashCode(), this.mMeteringRepeatingSession.getSessionConfig());
        }
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public CameraInfoInternal getCameraInfoInternal() {
        return this.mCameraInfoInternal;
    }

    void openCameraDevice() {
        this.mStateCallback.cancelScheduledReopen();
        if (!this.mCameraAvailability.isCameraAvailable() || !this.mCameraStateRegistry.tryOpenCamera(this)) {
            debugLog("No cameras available. Waiting for available camera before opening camera.");
            setState(InternalState.PENDING_OPEN);
            return;
        }
        setState(InternalState.OPENING);
        debugLog("Opening camera.");
        try {
            this.mCameraManager.openCamera(this.mCameraInfoInternal.getCameraId(), this.mExecutor, createDeviceStateCallback());
        } catch (CameraAccessExceptionCompat e) {
            debugLog("Unable to open camera due to " + e.getMessage());
            if (e.getReason() == 10001) {
                setState(InternalState.INITIALIZED);
            }
        }
    }

    void updateCaptureSessionConfig() {
        SessionConfig.ValidatingBuilder validatingBuilder = this.mUseCaseAttachState.getActiveAndAttachedBuilder();
        if (validatingBuilder.isValid()) {
            validatingBuilder.add(this.mCameraControlSessionConfig);
            this.mCaptureSession.setSessionConfig(validatingBuilder.build());
        }
    }

    void openCaptureSession() {
        Preconditions.checkState(this.mState == InternalState.OPENED);
        SessionConfig.ValidatingBuilder validatingBuilder = this.mUseCaseAttachState.getAttachedBuilder();
        if (!validatingBuilder.isValid()) {
            debugLog("Unable to create capture session due to conflicting configurations");
        } else {
            Futures.addCallback(this.mCaptureSession.open(validatingBuilder.build(), (CameraDevice) Preconditions.checkNotNull(this.mCameraDevice), this.mCaptureSessionOpenerBuilder.build()), new FutureCallback<Void>() { // from class: androidx.camera.camera2.internal.Camera2CameraImpl.2
                public void onSuccess(Void result) {
                }

                @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                public void onFailure(Throwable t) {
                    if (t instanceof CameraAccessException) {
                        Camera2CameraImpl camera2CameraImpl = Camera2CameraImpl.this;
                        camera2CameraImpl.debugLog("Unable to configure camera due to " + t.getMessage());
                    } else if (t instanceof CancellationException) {
                        Camera2CameraImpl.this.debugLog("Unable to configure camera cancelled");
                    } else if (t instanceof DeferrableSurface.SurfaceClosedException) {
                        SessionConfig sessionConfig = Camera2CameraImpl.this.findSessionConfigForSurface(((DeferrableSurface.SurfaceClosedException) t).getDeferrableSurface());
                        if (sessionConfig != null) {
                            Camera2CameraImpl.this.postSurfaceClosedError(sessionConfig);
                        }
                    } else if (t instanceof TimeoutException) {
                        Log.e(Camera2CameraImpl.TAG, "Unable to configure camera " + Camera2CameraImpl.this.mCameraInfoInternal.getCameraId() + ", timeout!");
                    } else {
                        throw new RuntimeException(t);
                    }
                }
            }, this.mExecutor);
        }
    }

    private boolean isLegacyDevice() {
        return ((Camera2CameraInfoImpl) getCameraInfoInternal()).getSupportedHardwareLevel() == 2;
    }

    SessionConfig findSessionConfigForSurface(DeferrableSurface surface) {
        for (SessionConfig sessionConfig : this.mUseCaseAttachState.getAttachedSessionConfigs()) {
            if (sessionConfig.getSurfaces().contains(surface)) {
                return sessionConfig;
            }
        }
        return null;
    }

    void postSurfaceClosedError(SessionConfig sessionConfig) {
        Executor executor = CameraXExecutors.mainThreadExecutor();
        List<SessionConfig.ErrorListener> errorListeners = sessionConfig.getErrorListeners();
        if (!errorListeners.isEmpty()) {
            debugLog("Posting surface closed", new Throwable());
            executor.execute(new Runnable(sessionConfig) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$SKFpv3KJmtAVJ_gIPpIKFcKhpZs
                private final /* synthetic */ SessionConfig f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SessionConfig.ErrorListener.this.onError(this.f$1, SessionConfig.SessionError.SESSION_ERROR_SURFACE_NEEDS_RESET);
                }
            });
        }
    }

    void resetCaptureSession(boolean abortInFlightCaptures) {
        Preconditions.checkState(this.mCaptureSession != null);
        debugLog("Resetting Capture Session");
        CaptureSession oldCaptureSession = this.mCaptureSession;
        SessionConfig previousSessionConfig = oldCaptureSession.getSessionConfig();
        List<CaptureConfig> unissuedCaptureConfigs = oldCaptureSession.getCaptureConfigs();
        this.mCaptureSession = new CaptureSession();
        this.mCaptureSession.setSessionConfig(previousSessionConfig);
        this.mCaptureSession.issueCaptureRequests(unissuedCaptureConfigs);
        releaseSession(oldCaptureSession, abortInFlightCaptures);
    }

    private CameraDevice.StateCallback createDeviceStateCallback() {
        List<CameraDevice.StateCallback> allStateCallbacks = new ArrayList<>(this.mUseCaseAttachState.getAttachedBuilder().build().getDeviceStateCallbacks());
        allStateCallbacks.add(this.mStateCallback);
        allStateCallbacks.add(this.mCaptureSessionRepository.getCameraStateCallback());
        return CameraDeviceStateCallbacks.createComboCallback(allStateCallbacks);
    }

    private boolean checkAndAttachRepeatingSurface(CaptureConfig.Builder captureConfigBuilder) {
        if (!captureConfigBuilder.getSurfaces().isEmpty()) {
            Log.w(TAG, "The capture config builder already has surface inside.");
            return false;
        }
        for (SessionConfig sessionConfig : this.mUseCaseAttachState.getActiveAndAttachedSessionConfigs()) {
            List<DeferrableSurface> surfaces = sessionConfig.getRepeatingCaptureConfig().getSurfaces();
            if (!surfaces.isEmpty()) {
                for (DeferrableSurface surface : surfaces) {
                    captureConfigBuilder.addSurface(surface);
                }
            }
        }
        if (!captureConfigBuilder.getSurfaces().isEmpty()) {
            return true;
        }
        Log.w(TAG, "Unable to find a repeating surface to attach to CaptureConfig");
        return false;
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public CameraControlInternal getCameraControlInternal() {
        return this.mCameraControlInternal;
    }

    void submitCaptureRequests(List<CaptureConfig> captureConfigs) {
        List<CaptureConfig> captureConfigsWithSurface = new ArrayList<>();
        for (CaptureConfig captureConfig : captureConfigs) {
            CaptureConfig.Builder builder = CaptureConfig.Builder.from(captureConfig);
            if (!captureConfig.getSurfaces().isEmpty() || !captureConfig.isUseRepeatingSurface() || checkAndAttachRepeatingSurface(builder)) {
                captureConfigsWithSurface.add(builder.build());
            }
        }
        debugLog("Issue capture request");
        this.mCaptureSession.issueCaptureRequests(captureConfigsWithSurface);
    }

    public String toString() {
        return String.format(Locale.US, "Camera@%x[id=%s]", Integer.valueOf(hashCode()), this.mCameraInfoInternal.getCameraId());
    }

    void debugLog(String msg) {
        debugLog(msg, null);
    }

    private void debugLog(String msg, Throwable throwable) {
        if (DEBUG) {
            String msgString = String.format("{%s} %s", toString(), msg);
            if (throwable == null) {
                Log.d(TAG, msgString);
            } else {
                Log.d(TAG, msgString, throwable);
            }
        }
    }

    @Override // androidx.camera.core.Camera
    public CameraControl getCameraControl() {
        return getCameraControlInternal();
    }

    @Override // androidx.camera.core.Camera
    public CameraInfo getCameraInfo() {
        return getCameraInfoInternal();
    }

    void setState(InternalState state) {
        CameraInternal.State publicState;
        debugLog("Transitioning camera internal state: " + this.mState + " --> " + state);
        this.mState = state;
        switch (state) {
            case INITIALIZED:
                publicState = CameraInternal.State.CLOSED;
                break;
            case CLOSING:
                publicState = CameraInternal.State.CLOSING;
                break;
            case OPENED:
                publicState = CameraInternal.State.OPEN;
                break;
            case OPENING:
            case REOPENING:
                publicState = CameraInternal.State.OPENING;
                break;
            case PENDING_OPEN:
                publicState = CameraInternal.State.PENDING_OPEN;
                break;
            case RELEASING:
                publicState = CameraInternal.State.RELEASING;
                break;
            case RELEASED:
                publicState = CameraInternal.State.RELEASED;
                break;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }
        this.mCameraStateRegistry.markCameraState(this, publicState);
        this.mObservableState.postValue(publicState);
    }

    static String getErrorMessage(int errorCode) {
        if (errorCode == 0) {
            return "ERROR_NONE";
        }
        if (errorCode == 1) {
            return "ERROR_CAMERA_IN_USE";
        }
        if (errorCode == 2) {
            return "ERROR_MAX_CAMERAS_IN_USE";
        }
        if (errorCode == 3) {
            return "ERROR_CAMERA_DISABLED";
        }
        if (errorCode == 4) {
            return "ERROR_CAMERA_DEVICE";
        }
        if (errorCode != 5) {
            return "UNKNOWN ERROR";
        }
        return "ERROR_CAMERA_SERVICE";
    }

    /* loaded from: classes.dex */
    public final class StateCallback extends CameraDevice.StateCallback {
        private static final int REOPEN_DELAY_MS;
        private final Executor mExecutor;
        ScheduledFuture<?> mScheduledReopenHandle;
        private ScheduledReopen mScheduledReopenRunnable;
        private final ScheduledExecutorService mScheduler;

        StateCallback(Executor executor, ScheduledExecutorService scheduler) {
            Camera2CameraImpl.this = this$0;
            this.mExecutor = executor;
            this.mScheduler = scheduler;
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            Camera2CameraImpl.this.debugLog("CameraDevice.onOpened()");
            Camera2CameraImpl camera2CameraImpl = Camera2CameraImpl.this;
            camera2CameraImpl.mCameraDevice = cameraDevice;
            camera2CameraImpl.updateDefaultRequestBuilderToCameraControl(cameraDevice);
            Camera2CameraImpl.this.mCameraDeviceError = 0;
            int i = AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$Camera2CameraImpl$InternalState[Camera2CameraImpl.this.mState.ordinal()];
            if (i == 2 || i == 7) {
                Preconditions.checkState(Camera2CameraImpl.this.isSessionCloseComplete());
                Camera2CameraImpl.this.mCameraDevice.close();
                Camera2CameraImpl.this.mCameraDevice = null;
            } else if (i == 4 || i == 5) {
                Camera2CameraImpl.this.setState(InternalState.OPENED);
                Camera2CameraImpl.this.openCaptureSession();
            } else {
                throw new IllegalStateException("onOpened() should not be possible from state: " + Camera2CameraImpl.this.mState);
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            Camera2CameraImpl.this.debugLog("CameraDevice.onClosed()");
            boolean z = true;
            Preconditions.checkState(Camera2CameraImpl.this.mCameraDevice == null, "Unexpected onClose callback on camera device: " + cameraDevice);
            int i = AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$Camera2CameraImpl$InternalState[Camera2CameraImpl.this.mState.ordinal()];
            if (i != 2) {
                if (i != 5) {
                    if (i != 7) {
                        throw new IllegalStateException("Camera closed while in state: " + Camera2CameraImpl.this.mState);
                    }
                } else if (Camera2CameraImpl.this.mCameraDeviceError != 0) {
                    Preconditions.checkState(this.mScheduledReopenRunnable == null);
                    if (this.mScheduledReopenHandle != null) {
                        z = false;
                    }
                    Preconditions.checkState(z);
                    this.mScheduledReopenRunnable = new ScheduledReopen(this.mExecutor);
                    Camera2CameraImpl.this.debugLog("Camera closed due to error: " + Camera2CameraImpl.getErrorMessage(Camera2CameraImpl.this.mCameraDeviceError) + ". Attempting re-open in " + REOPEN_DELAY_MS + "ms: " + this.mScheduledReopenRunnable);
                    this.mScheduledReopenHandle = this.mScheduler.schedule(this.mScheduledReopenRunnable, 700, TimeUnit.MILLISECONDS);
                    return;
                } else {
                    Camera2CameraImpl.this.openCameraDevice();
                    return;
                }
            }
            Preconditions.checkState(Camera2CameraImpl.this.isSessionCloseComplete());
            Camera2CameraImpl.this.finishClose();
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            Camera2CameraImpl.this.debugLog("CameraDevice.onDisconnected()");
            onError(cameraDevice, 1);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int error) {
            Camera2CameraImpl camera2CameraImpl = Camera2CameraImpl.this;
            camera2CameraImpl.mCameraDevice = cameraDevice;
            camera2CameraImpl.mCameraDeviceError = error;
            int i = AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$Camera2CameraImpl$InternalState[Camera2CameraImpl.this.mState.ordinal()];
            if (i != 2) {
                if (i == 3 || i == 4 || i == 5) {
                    handleErrorOnOpen(cameraDevice, error);
                    return;
                } else if (i != 7) {
                    throw new IllegalStateException("onError() should not be possible from state: " + Camera2CameraImpl.this.mState);
                }
            }
            Log.e(Camera2CameraImpl.TAG, "CameraDevice.onError(): " + cameraDevice.getId() + " with error: " + Camera2CameraImpl.getErrorMessage(error));
            Camera2CameraImpl.this.closeCamera(false);
        }

        private void handleErrorOnOpen(CameraDevice cameraDevice, int error) {
            boolean z = Camera2CameraImpl.this.mState == InternalState.OPENING || Camera2CameraImpl.this.mState == InternalState.OPENED || Camera2CameraImpl.this.mState == InternalState.REOPENING;
            Preconditions.checkState(z, "Attempt to handle open error from non open state: " + Camera2CameraImpl.this.mState);
            if (error == 1 || error == 2 || error == 4) {
                reopenCameraAfterError();
                return;
            }
            Log.e(Camera2CameraImpl.TAG, "Error observed on open (or opening) camera device " + cameraDevice.getId() + ": " + Camera2CameraImpl.getErrorMessage(error));
            Camera2CameraImpl.this.setState(InternalState.CLOSING);
            Camera2CameraImpl.this.closeCamera(false);
        }

        private void reopenCameraAfterError() {
            Preconditions.checkState(Camera2CameraImpl.this.mCameraDeviceError != 0, "Can only reopen camera device after error if the camera device is actually in an error state.");
            Camera2CameraImpl.this.setState(InternalState.REOPENING);
            Camera2CameraImpl.this.closeCamera(false);
        }

        boolean cancelScheduledReopen() {
            if (this.mScheduledReopenHandle == null) {
                return false;
            }
            Camera2CameraImpl camera2CameraImpl = Camera2CameraImpl.this;
            camera2CameraImpl.debugLog("Cancelling scheduled re-open: " + this.mScheduledReopenRunnable);
            this.mScheduledReopenRunnable.cancel();
            this.mScheduledReopenRunnable = null;
            this.mScheduledReopenHandle.cancel(false);
            this.mScheduledReopenHandle = null;
            return true;
        }

        /* loaded from: classes.dex */
        public class ScheduledReopen implements Runnable {
            private boolean mCancelled = false;
            private Executor mExecutor;

            ScheduledReopen(Executor executor) {
                StateCallback.this = this$1;
                this.mExecutor = executor;
            }

            void cancel() {
                this.mCancelled = true;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraImpl$StateCallback$ScheduledReopen$as759NiVIxRtxg9ZN460DXMl1dI
                    @Override // java.lang.Runnable
                    public final void run() {
                        Camera2CameraImpl.StateCallback.ScheduledReopen.this.lambda$run$0$Camera2CameraImpl$StateCallback$ScheduledReopen();
                    }
                });
            }

            public /* synthetic */ void lambda$run$0$Camera2CameraImpl$StateCallback$ScheduledReopen() {
                if (!this.mCancelled) {
                    Preconditions.checkState(Camera2CameraImpl.this.mState == InternalState.REOPENING);
                    Camera2CameraImpl.this.openCameraDevice();
                }
            }
        }
    }

    void updateDefaultRequestBuilderToCameraControl(CameraDevice cameraDevice) {
        try {
            this.mCameraControlInternal.setDefaultRequestBuilder(cameraDevice.createCaptureRequest(this.mCameraControlInternal.getDefaultTemplate()));
        } catch (CameraAccessException e) {
            Log.e(TAG, "fail to create capture request.", e);
        }
    }

    /* loaded from: classes.dex */
    public final class CameraAvailability extends CameraManager.AvailabilityCallback implements CameraStateRegistry.OnOpenAvailableListener {
        private boolean mCameraAvailable = true;
        private final String mCameraId;

        CameraAvailability(String cameraId) {
            Camera2CameraImpl.this = this$0;
            this.mCameraId = cameraId;
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraAvailable(String cameraId) {
            if (this.mCameraId.equals(cameraId)) {
                this.mCameraAvailable = true;
                if (Camera2CameraImpl.this.mState == InternalState.PENDING_OPEN) {
                    Camera2CameraImpl.this.openCameraDevice();
                }
            }
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraUnavailable(String cameraId) {
            if (this.mCameraId.equals(cameraId)) {
                this.mCameraAvailable = false;
            }
        }

        @Override // androidx.camera.core.impl.CameraStateRegistry.OnOpenAvailableListener
        public void onOpenAvailable() {
            if (Camera2CameraImpl.this.mState == InternalState.PENDING_OPEN) {
                Camera2CameraImpl.this.openCameraDevice();
            }
        }

        boolean isCameraAvailable() {
            return this.mCameraAvailable;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class ControlUpdateListenerInternal implements CameraControlInternal.ControlUpdateCallback {
        ControlUpdateListenerInternal() {
            Camera2CameraImpl.this = this$0;
        }

        @Override // androidx.camera.core.impl.CameraControlInternal.ControlUpdateCallback
        public void onCameraControlUpdateSessionConfig(SessionConfig sessionConfig) {
            Camera2CameraImpl.this.mCameraControlSessionConfig = (SessionConfig) Preconditions.checkNotNull(sessionConfig);
            Camera2CameraImpl.this.updateCaptureSessionConfig();
        }

        @Override // androidx.camera.core.impl.CameraControlInternal.ControlUpdateCallback
        public void onCameraControlCaptureRequests(List<CaptureConfig> captureConfigs) {
            Camera2CameraImpl.this.submitCaptureRequests((List) Preconditions.checkNotNull(captureConfigs));
        }
    }
}
