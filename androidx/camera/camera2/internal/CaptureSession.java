package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;
import android.view.Surface;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.camera2.impl.CameraEventCallbacks;
import androidx.camera.camera2.internal.SynchronizedCaptureSession;
import androidx.camera.camera2.internal.SynchronizedCaptureSessionStateCallbacks;
import androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat;
import androidx.camera.camera2.internal.compat.params.SessionConfigurationCompat;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.DeferrableSurfaces;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.utils.futures.AsyncFunction;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.FutureChain;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class CaptureSession {
    private static final String TAG;
    private static final long TIMEOUT_GET_SURFACE_IN_MS;
    volatile Config mCameraEventOnRepeatingOptions;
    CallbackToFutureAdapter.Completer<Void> mReleaseCompleter;
    ListenableFuture<Void> mReleaseFuture;
    volatile SessionConfig mSessionConfig;
    State mState;
    SynchronizedCaptureSession mSynchronizedCaptureSession;
    SynchronizedCaptureSessionOpener mSynchronizedCaptureSessionOpener;
    final Object mStateLock = new Object();
    private final List<CaptureConfig> mCaptureConfigs = new ArrayList();
    private final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() { // from class: androidx.camera.camera2.internal.CaptureSession.1
        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
        }
    };
    private Map<DeferrableSurface, Surface> mConfiguredSurfaceMap = new HashMap();
    List<DeferrableSurface> mConfiguredDeferrableSurfaces = Collections.emptyList();
    private final StateCallback mCaptureSessionStateCallback = new StateCallback();

    /* loaded from: classes.dex */
    public enum State {
        UNINITIALIZED,
        INITIALIZED,
        GET_SURFACE,
        OPENING,
        OPENED,
        CLOSED,
        RELEASING,
        RELEASED
    }

    public CaptureSession() {
        this.mState = State.UNINITIALIZED;
        this.mState = State.INITIALIZED;
    }

    public SessionConfig getSessionConfig() {
        SessionConfig sessionConfig;
        synchronized (this.mStateLock) {
            sessionConfig = this.mSessionConfig;
        }
        return sessionConfig;
    }

    public void setSessionConfig(SessionConfig sessionConfig) {
        synchronized (this.mStateLock) {
            switch (this.mState) {
                case UNINITIALIZED:
                    throw new IllegalStateException("setSessionConfig() should not be possible in state: " + this.mState);
                case INITIALIZED:
                case GET_SURFACE:
                case OPENING:
                    this.mSessionConfig = sessionConfig;
                    break;
                case OPENED:
                    this.mSessionConfig = sessionConfig;
                    if (this.mConfiguredSurfaceMap.keySet().containsAll(sessionConfig.getSurfaces())) {
                        Log.d(TAG, "Attempting to submit CaptureRequest after setting");
                        issueRepeatingCaptureRequests();
                        break;
                    } else {
                        Log.e(TAG, "Does not have the proper configured lists");
                        return;
                    }
                case CLOSED:
                case RELEASING:
                case RELEASED:
                    throw new IllegalStateException("Session configuration cannot be set on a closed/released session.");
            }
        }
    }

    public ListenableFuture<Void> open(SessionConfig sessionConfig, CameraDevice cameraDevice, SynchronizedCaptureSessionOpener opener) {
        synchronized (this.mStateLock) {
            if (AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$CaptureSession$State[this.mState.ordinal()] != 2) {
                Log.e(TAG, "Open not allowed in state: " + this.mState);
                return Futures.immediateFailedFuture(new IllegalStateException("open() should not allow the state: " + this.mState));
            }
            this.mState = State.GET_SURFACE;
            this.mConfiguredDeferrableSurfaces = new ArrayList(sessionConfig.getSurfaces());
            this.mSynchronizedCaptureSessionOpener = opener;
            ListenableFuture<Void> openFuture = FutureChain.from(this.mSynchronizedCaptureSessionOpener.startWithDeferrableSurface(this.mConfiguredDeferrableSurfaces, TIMEOUT_GET_SURFACE_IN_MS)).transformAsync(new AsyncFunction(sessionConfig, cameraDevice) { // from class: androidx.camera.camera2.internal.-$$Lambda$CaptureSession$2IbSQd39wMeo2dJgmFG1rvePLoM
                private final /* synthetic */ SessionConfig f$1;
                private final /* synthetic */ CameraDevice f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
                public final ListenableFuture apply(Object obj) {
                    return CaptureSession.this.lambda$open$0$CaptureSession(this.f$1, this.f$2, (List) obj);
                }
            }, this.mSynchronizedCaptureSessionOpener.getExecutor());
            Futures.addCallback(openFuture, new FutureCallback<Void>() { // from class: androidx.camera.camera2.internal.CaptureSession.2
                public void onSuccess(Void result) {
                }

                @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                public void onFailure(Throwable t) {
                    CaptureSession.this.mSynchronizedCaptureSessionOpener.stop();
                }
            }, this.mSynchronizedCaptureSessionOpener.getExecutor());
            return Futures.nonCancellationPropagating(openFuture);
        }
    }

    /* renamed from: openCaptureSession */
    public ListenableFuture<Void> lambda$open$0$CaptureSession(List<Surface> configuredSurfaces, SessionConfig sessionConfig, CameraDevice cameraDevice) {
        synchronized (this.mStateLock) {
            int i = AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$CaptureSession$State[this.mState.ordinal()];
            if (!(i == 1 || i == 2)) {
                if (i == 3) {
                    try {
                        DeferrableSurfaces.incrementAll(this.mConfiguredDeferrableSurfaces);
                        this.mConfiguredSurfaceMap.clear();
                        for (int i2 = 0; i2 < configuredSurfaces.size(); i2++) {
                            this.mConfiguredSurfaceMap.put(this.mConfiguredDeferrableSurfaces.get(i2), configuredSurfaces.get(i2));
                        }
                        List<Surface> uniqueConfiguredSurface = new ArrayList<>(new HashSet(configuredSurfaces));
                        this.mState = State.OPENING;
                        Log.d(TAG, "Opening capture session.");
                        SynchronizedCaptureSession.StateCallback callbacks = SynchronizedCaptureSessionStateCallbacks.createComboCallback(this.mCaptureSessionStateCallback, new SynchronizedCaptureSessionStateCallbacks.Adapter(sessionConfig.getSessionStateCallbacks()));
                        List<CaptureConfig> presetList = new Camera2ImplConfig(sessionConfig.getImplementationOptions()).getCameraEventCallback(CameraEventCallbacks.createEmptyCallback()).createComboCallback().onPresetSession();
                        CaptureConfig.Builder captureConfigBuilder = CaptureConfig.Builder.from(sessionConfig.getRepeatingCaptureConfig());
                        for (CaptureConfig config : presetList) {
                            captureConfigBuilder.addImplementationOptions(config.getImplementationOptions());
                        }
                        List<OutputConfigurationCompat> outputConfigList = new ArrayList<>();
                        for (Surface surface : uniqueConfiguredSurface) {
                            outputConfigList.add(new OutputConfigurationCompat(surface));
                        }
                        SessionConfigurationCompat sessionConfigCompat = this.mSynchronizedCaptureSessionOpener.createSessionConfigurationCompat(0, outputConfigList, callbacks);
                        try {
                            CaptureRequest captureRequest = Camera2CaptureRequestBuilder.buildWithoutTarget(captureConfigBuilder.build(), cameraDevice);
                            if (captureRequest != null) {
                                sessionConfigCompat.setSessionParameters(captureRequest);
                            }
                            return this.mSynchronizedCaptureSessionOpener.openCaptureSession(cameraDevice, sessionConfigCompat);
                        } catch (CameraAccessException e) {
                            return Futures.immediateFailedFuture(e);
                        }
                    } catch (DeferrableSurface.SurfaceClosedException e2) {
                        this.mConfiguredDeferrableSurfaces.clear();
                        return Futures.immediateFailedFuture(e2);
                    }
                } else if (i != 5) {
                    return Futures.immediateFailedFuture(new CancellationException("openCaptureSession() not execute in state: " + this.mState));
                }
            }
            return Futures.immediateFailedFuture(new IllegalStateException("openCaptureSession() should not be possible in state: " + this.mState));
        }
    }

    public void close() {
        synchronized (this.mStateLock) {
            int i = AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$CaptureSession$State[this.mState.ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i != 4) {
                            if (i == 5) {
                                if (this.mSessionConfig != null) {
                                    List<CaptureConfig> configList = new Camera2ImplConfig(this.mSessionConfig.getImplementationOptions()).getCameraEventCallback(CameraEventCallbacks.createEmptyCallback()).createComboCallback().onDisableSession();
                                    if (!configList.isEmpty()) {
                                        try {
                                            issueCaptureRequests(setupConfiguredSurface(configList));
                                        } catch (IllegalStateException e) {
                                            Log.e(TAG, "Unable to issue the request before close the capture session", e);
                                        }
                                    }
                                }
                            }
                        }
                        SynchronizedCaptureSessionOpener synchronizedCaptureSessionOpener = this.mSynchronizedCaptureSessionOpener;
                        Preconditions.checkNotNull(synchronizedCaptureSessionOpener, "The Opener shouldn't null in state:" + this.mState);
                        this.mSynchronizedCaptureSessionOpener.stop();
                        this.mState = State.CLOSED;
                        this.mSessionConfig = null;
                        this.mCameraEventOnRepeatingOptions = null;
                    } else {
                        SynchronizedCaptureSessionOpener synchronizedCaptureSessionOpener2 = this.mSynchronizedCaptureSessionOpener;
                        Preconditions.checkNotNull(synchronizedCaptureSessionOpener2, "The Opener shouldn't null in state:" + this.mState);
                        this.mSynchronizedCaptureSessionOpener.stop();
                    }
                }
                this.mState = State.RELEASED;
            } else {
                throw new IllegalStateException("close() should not be possible in state: " + this.mState);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x004f A[Catch: all -> 0x00a8, TryCatch #1 {, blocks: (B:4:0x0003, B:5:0x000d, B:7:0x0012, B:10:0x0018, B:12:0x001f, B:13:0x0026, B:14:0x002b, B:16:0x004f, B:17:0x0053, B:19:0x0057, B:20:0x0062, B:21:0x0064, B:23:0x0066, B:24:0x0083, B:25:0x0088, B:26:0x00a0, B:27:0x00a1), top: B:35:0x0003, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0057 A[Catch: all -> 0x00a8, TryCatch #1 {, blocks: (B:4:0x0003, B:5:0x000d, B:7:0x0012, B:10:0x0018, B:12:0x001f, B:13:0x0026, B:14:0x002b, B:16:0x004f, B:17:0x0053, B:19:0x0057, B:20:0x0062, B:21:0x0064, B:23:0x0066, B:24:0x0083, B:25:0x0088, B:26:0x00a0, B:27:0x00a1), top: B:35:0x0003, inners: #0 }] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public ListenableFuture<Void> release(boolean abortInFlightCaptures) {
        synchronized (this.mStateLock) {
            switch (this.mState) {
                case UNINITIALIZED:
                    throw new IllegalStateException("release() should not be possible in state: " + this.mState);
                case INITIALIZED:
                    this.mState = State.RELEASED;
                    return Futures.immediateFuture(null);
                case GET_SURFACE:
                    SynchronizedCaptureSessionOpener synchronizedCaptureSessionOpener = this.mSynchronizedCaptureSessionOpener;
                    Preconditions.checkNotNull(synchronizedCaptureSessionOpener, "The Opener shouldn't null in state:" + this.mState);
                    this.mSynchronizedCaptureSessionOpener.stop();
                    this.mState = State.RELEASED;
                    return Futures.immediateFuture(null);
                case OPENING:
                    this.mState = State.RELEASING;
                    SynchronizedCaptureSessionOpener synchronizedCaptureSessionOpener2 = this.mSynchronizedCaptureSessionOpener;
                    Preconditions.checkNotNull(synchronizedCaptureSessionOpener2, "The Opener shouldn't null in state:" + this.mState);
                    if (this.mSynchronizedCaptureSessionOpener.stop()) {
                        finishClose();
                        return Futures.immediateFuture(null);
                    }
                    if (this.mReleaseFuture == null) {
                        this.mReleaseFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.internal.-$$Lambda$CaptureSession$IHB0q0Z7BHupgNRbPs5f9BLmzgo
                            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                                return CaptureSession.this.lambda$release$1$CaptureSession(completer);
                            }
                        });
                    }
                    return this.mReleaseFuture;
                case OPENED:
                case CLOSED:
                    if (this.mSynchronizedCaptureSession != null) {
                        if (abortInFlightCaptures) {
                            try {
                                this.mSynchronizedCaptureSession.abortCaptures();
                            } catch (CameraAccessException e) {
                                Log.e(TAG, "Unable to abort captures.", e);
                            }
                        }
                        this.mSynchronizedCaptureSession.close();
                    }
                    this.mState = State.RELEASING;
                    SynchronizedCaptureSessionOpener synchronizedCaptureSessionOpener22 = this.mSynchronizedCaptureSessionOpener;
                    Preconditions.checkNotNull(synchronizedCaptureSessionOpener22, "The Opener shouldn't null in state:" + this.mState);
                    if (this.mSynchronizedCaptureSessionOpener.stop()) {
                    }
                    if (this.mReleaseFuture == null) {
                    }
                    return this.mReleaseFuture;
                case RELEASING:
                    if (this.mReleaseFuture == null) {
                    }
                    return this.mReleaseFuture;
                default:
                    return Futures.immediateFuture(null);
            }
        }
    }

    public /* synthetic */ Object lambda$release$1$CaptureSession(CallbackToFutureAdapter.Completer completer) throws Exception {
        String str;
        synchronized (this.mStateLock) {
            Preconditions.checkState(this.mReleaseCompleter == null, "Release completer expected to be null");
            this.mReleaseCompleter = completer;
            str = "Release[session=" + this + "]";
        }
        return str;
    }

    void clearConfiguredSurfaces() {
        DeferrableSurfaces.decrementAll(this.mConfiguredDeferrableSurfaces);
        this.mConfiguredDeferrableSurfaces.clear();
    }

    public void issueCaptureRequests(List<CaptureConfig> captureConfigs) {
        synchronized (this.mStateLock) {
            switch (this.mState) {
                case UNINITIALIZED:
                    throw new IllegalStateException("issueCaptureRequests() should not be possible in state: " + this.mState);
                case INITIALIZED:
                case GET_SURFACE:
                case OPENING:
                    this.mCaptureConfigs.addAll(captureConfigs);
                    break;
                case OPENED:
                    this.mCaptureConfigs.addAll(captureConfigs);
                    issuePendingCaptureRequest();
                    break;
                case CLOSED:
                case RELEASING:
                case RELEASED:
                    throw new IllegalStateException("Cannot issue capture request on a closed/released session.");
            }
        }
    }

    public List<CaptureConfig> getCaptureConfigs() {
        List<CaptureConfig> unmodifiableList;
        synchronized (this.mStateLock) {
            unmodifiableList = Collections.unmodifiableList(this.mCaptureConfigs);
        }
        return unmodifiableList;
    }

    State getState() {
        State state;
        synchronized (this.mStateLock) {
            state = this.mState;
        }
        return state;
    }

    void finishClose() {
        if (this.mState == State.RELEASED) {
            Log.d(TAG, "Skipping finishClose due to being state RELEASED.");
            return;
        }
        this.mState = State.RELEASED;
        this.mSynchronizedCaptureSession = null;
        clearConfiguredSurfaces();
        CallbackToFutureAdapter.Completer<Void> completer = this.mReleaseCompleter;
        if (completer != null) {
            completer.set(null);
            this.mReleaseCompleter = null;
        }
    }

    void issueRepeatingCaptureRequests() {
        if (this.mSessionConfig == null) {
            Log.d(TAG, "Skipping issueRepeatingCaptureRequests for no configuration case.");
            return;
        }
        CaptureConfig captureConfig = this.mSessionConfig.getRepeatingCaptureConfig();
        try {
            Log.d(TAG, "Issuing request for session.");
            CaptureConfig.Builder captureConfigBuilder = CaptureConfig.Builder.from(captureConfig);
            this.mCameraEventOnRepeatingOptions = mergeOptions(new Camera2ImplConfig(this.mSessionConfig.getImplementationOptions()).getCameraEventCallback(CameraEventCallbacks.createEmptyCallback()).createComboCallback().onRepeating());
            if (this.mCameraEventOnRepeatingOptions != null) {
                captureConfigBuilder.addImplementationOptions(this.mCameraEventOnRepeatingOptions);
            }
            CaptureRequest captureRequest = Camera2CaptureRequestBuilder.build(captureConfigBuilder.build(), this.mSynchronizedCaptureSession.getDevice(), this.mConfiguredSurfaceMap);
            if (captureRequest == null) {
                Log.d(TAG, "Skipping issuing empty request for session.");
            } else {
                this.mSynchronizedCaptureSession.setSingleRepeatingRequest(captureRequest, createCamera2CaptureCallback(captureConfig.getCameraCaptureCallbacks(), this.mCaptureCallback));
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "Unable to access camera: " + e.getMessage());
            Thread.dumpStack();
        }
    }

    void issuePendingCaptureRequest() {
        if (!this.mCaptureConfigs.isEmpty()) {
            try {
                issueBurstCaptureRequest(this.mCaptureConfigs);
            } finally {
                this.mCaptureConfigs.clear();
            }
        }
    }

    void issueBurstCaptureRequest(List<CaptureConfig> captureConfigs) {
        if (!captureConfigs.isEmpty()) {
            try {
                CameraBurstCaptureCallback callbackAggregator = new CameraBurstCaptureCallback();
                List<CaptureRequest> captureRequests = new ArrayList<>();
                Log.d(TAG, "Issuing capture request.");
                for (CaptureConfig captureConfig : captureConfigs) {
                    if (captureConfig.getSurfaces().isEmpty()) {
                        Log.d(TAG, "Skipping issuing empty capture request.");
                    } else {
                        boolean surfacesValid = true;
                        Iterator<DeferrableSurface> it = captureConfig.getSurfaces().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            DeferrableSurface surface = it.next();
                            if (!this.mConfiguredSurfaceMap.containsKey(surface)) {
                                Log.d(TAG, "Skipping capture request with invalid surface: " + surface);
                                surfacesValid = false;
                                break;
                            }
                        }
                        if (surfacesValid) {
                            CaptureConfig.Builder captureConfigBuilder = CaptureConfig.Builder.from(captureConfig);
                            if (this.mSessionConfig != null) {
                                captureConfigBuilder.addImplementationOptions(this.mSessionConfig.getRepeatingCaptureConfig().getImplementationOptions());
                            }
                            if (this.mCameraEventOnRepeatingOptions != null) {
                                captureConfigBuilder.addImplementationOptions(this.mCameraEventOnRepeatingOptions);
                            }
                            captureConfigBuilder.addImplementationOptions(captureConfig.getImplementationOptions());
                            CaptureRequest captureRequest = Camera2CaptureRequestBuilder.build(captureConfigBuilder.build(), this.mSynchronizedCaptureSession.getDevice(), this.mConfiguredSurfaceMap);
                            if (captureRequest == null) {
                                Log.d(TAG, "Skipping issuing request without surface.");
                                return;
                            }
                            List<CameraCaptureSession.CaptureCallback> cameraCallbacks = new ArrayList<>();
                            for (CameraCaptureCallback callback : captureConfig.getCameraCaptureCallbacks()) {
                                CaptureCallbackConverter.toCaptureCallback(callback, cameraCallbacks);
                            }
                            callbackAggregator.addCamera2Callbacks(captureRequest, cameraCallbacks);
                            captureRequests.add(captureRequest);
                        }
                    }
                }
                if (!captureRequests.isEmpty()) {
                    this.mSynchronizedCaptureSession.captureBurstRequests(captureRequests, callbackAggregator);
                } else {
                    Log.d(TAG, "Skipping issuing burst request due to no valid request elements");
                }
            } catch (CameraAccessException e) {
                Log.e(TAG, "Unable to access camera: " + e.getMessage());
                Thread.dumpStack();
            }
        }
    }

    public void cancelIssuedCaptureRequests() {
        if (!this.mCaptureConfigs.isEmpty()) {
            for (CaptureConfig captureConfig : this.mCaptureConfigs) {
                for (CameraCaptureCallback cameraCaptureCallback : captureConfig.getCameraCaptureCallbacks()) {
                    cameraCaptureCallback.onCaptureCancelled();
                }
            }
            this.mCaptureConfigs.clear();
        }
    }

    private CameraCaptureSession.CaptureCallback createCamera2CaptureCallback(List<CameraCaptureCallback> cameraCaptureCallbacks, CameraCaptureSession.CaptureCallback... additionalCallbacks) {
        List<CameraCaptureSession.CaptureCallback> camera2Callbacks = new ArrayList<>(cameraCaptureCallbacks.size() + additionalCallbacks.length);
        for (CameraCaptureCallback callback : cameraCaptureCallbacks) {
            camera2Callbacks.add(CaptureCallbackConverter.toCaptureCallback(callback));
        }
        Collections.addAll(camera2Callbacks, additionalCallbacks);
        return Camera2CaptureCallbacks.createComboCallback(camera2Callbacks);
    }

    private static Config mergeOptions(List<CaptureConfig> captureConfigList) {
        MutableOptionsBundle options = MutableOptionsBundle.create();
        for (CaptureConfig captureConfig : captureConfigList) {
            Config newOptions = captureConfig.getImplementationOptions();
            for (Config.Option<?> option : newOptions.listOptions()) {
                Object newValue = newOptions.retrieveOption(option, null);
                if (options.containsOption(option)) {
                    Object oldValue = options.retrieveOption(option, null);
                    if (!Objects.equals(oldValue, newValue)) {
                        Log.d(TAG, "Detect conflicting option " + option.getId() + " : " + newValue + " != " + oldValue);
                    }
                } else {
                    options.insertOption(option, newValue);
                }
            }
        }
        return options;
    }

    /* loaded from: classes.dex */
    public final class StateCallback extends SynchronizedCaptureSession.StateCallback {
        StateCallback() {
            CaptureSession.this = this$0;
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onConfigured(SynchronizedCaptureSession session) {
            synchronized (CaptureSession.this.mStateLock) {
                switch (CaptureSession.this.mState) {
                    case UNINITIALIZED:
                    case INITIALIZED:
                    case GET_SURFACE:
                    case OPENED:
                    case RELEASED:
                        throw new IllegalStateException("onConfigured() should not be possible in state: " + CaptureSession.this.mState);
                    case OPENING:
                        CaptureSession.this.mState = State.OPENED;
                        CaptureSession.this.mSynchronizedCaptureSession = session;
                        if (CaptureSession.this.mSessionConfig != null) {
                            List<CaptureConfig> list = new Camera2ImplConfig(CaptureSession.this.mSessionConfig.getImplementationOptions()).getCameraEventCallback(CameraEventCallbacks.createEmptyCallback()).createComboCallback().onEnableSession();
                            if (!list.isEmpty()) {
                                CaptureSession.this.issueBurstCaptureRequest(CaptureSession.this.setupConfiguredSurface(list));
                            }
                        }
                        Log.d(CaptureSession.TAG, "Attempting to send capture request onConfigured");
                        CaptureSession.this.issueRepeatingCaptureRequests();
                        CaptureSession.this.issuePendingCaptureRequest();
                        break;
                    case CLOSED:
                        CaptureSession.this.mSynchronizedCaptureSession = session;
                        break;
                    case RELEASING:
                        session.close();
                        break;
                }
                Log.d(CaptureSession.TAG, "CameraCaptureSession.onConfigured() mState=" + CaptureSession.this.mState);
            }
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onReady(SynchronizedCaptureSession session) {
            synchronized (CaptureSession.this.mStateLock) {
                if (AnonymousClass3.$SwitchMap$androidx$camera$camera2$internal$CaptureSession$State[CaptureSession.this.mState.ordinal()] != 1) {
                    Log.d(CaptureSession.TAG, "CameraCaptureSession.onReady() " + CaptureSession.this.mState);
                } else {
                    throw new IllegalStateException("onReady() should not be possible in state: " + CaptureSession.this.mState);
                }
            }
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onClosed(SynchronizedCaptureSession session) {
            synchronized (CaptureSession.this.mStateLock) {
                if (CaptureSession.this.mState != State.UNINITIALIZED) {
                    Log.d(CaptureSession.TAG, "CameraCaptureSession.onClosed()");
                    CaptureSession.this.finishClose();
                } else {
                    throw new IllegalStateException("onClosed() should not be possible in state: " + CaptureSession.this.mState);
                }
            }
        }

        @Override // androidx.camera.camera2.internal.SynchronizedCaptureSession.StateCallback
        public void onConfigureFailed(SynchronizedCaptureSession session) {
            synchronized (CaptureSession.this.mStateLock) {
                switch (CaptureSession.this.mState) {
                    case UNINITIALIZED:
                    case INITIALIZED:
                    case GET_SURFACE:
                    case OPENED:
                    case RELEASED:
                        throw new IllegalStateException("onConfigureFailed() should not be possible in state: " + CaptureSession.this.mState);
                    case OPENING:
                    case CLOSED:
                    case RELEASING:
                        CaptureSession.this.finishClose();
                        break;
                }
                Log.e(CaptureSession.TAG, "CameraCaptureSession.onConfigureFailed() " + CaptureSession.this.mState);
            }
        }
    }

    List<CaptureConfig> setupConfiguredSurface(List<CaptureConfig> list) {
        List<CaptureConfig> ret = new ArrayList<>();
        for (CaptureConfig c : list) {
            CaptureConfig.Builder builder = CaptureConfig.Builder.from(c);
            builder.setTemplateType(1);
            for (DeferrableSurface deferrableSurface : this.mSessionConfig.getRepeatingCaptureConfig().getSurfaces()) {
                builder.addSurface(deferrableSurface);
            }
            ret.add(builder.build());
        }
        return ret;
    }
}
