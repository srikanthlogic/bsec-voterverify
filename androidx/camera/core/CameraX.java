package androidx.camera.core;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import androidx.arch.core.util.Function;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.impl.CameraDeviceSurfaceManager;
import androidx.camera.core.impl.CameraFactory;
import androidx.camera.core.impl.CameraInfoInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.CameraRepository;
import androidx.camera.core.impl.CameraThreadConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.AsyncFunction;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.FutureChain;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.os.HandlerCompat;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public final class CameraX {
    private static final String TAG;
    private static final long WAIT_INITIALIZED_TIMEOUT;
    private Application mApplication;
    private final Executor mCameraExecutor;
    private CameraFactory mCameraFactory;
    private final CameraXConfig mCameraXConfig;
    private UseCaseConfigFactory mDefaultConfigFactory;
    private final Handler mSchedulerHandler;
    private final HandlerThread mSchedulerThread;
    private CameraDeviceSurfaceManager mSurfaceManager;
    static final Object INSTANCE_LOCK = new Object();
    static CameraX sInstance = null;
    private static CameraXConfig.Provider sConfigProvider = null;
    private static ListenableFuture<Void> sInitializeFuture = Futures.immediateFailedFuture(new IllegalStateException("CameraX is not initialized."));
    private static ListenableFuture<Void> sShutdownFuture = Futures.immediateFuture(null);
    final CameraRepository mCameraRepository = new CameraRepository();
    private final Object mInitializeLock = new Object();
    private InternalInitState mInitState = InternalInitState.UNINITIALIZED;
    private ListenableFuture<Void> mShutdownInternalFuture = Futures.immediateFuture(null);

    /* loaded from: classes.dex */
    public enum InternalInitState {
        UNINITIALIZED,
        INITIALIZING,
        INITIALIZED,
        SHUTDOWN
    }

    CameraX(CameraXConfig cameraXConfig) {
        this.mCameraXConfig = (CameraXConfig) Preconditions.checkNotNull(cameraXConfig);
        Executor executor = cameraXConfig.getCameraExecutor(null);
        Handler schedulerHandler = cameraXConfig.getSchedulerHandler(null);
        this.mCameraExecutor = executor == null ? new CameraExecutor() : executor;
        if (schedulerHandler == null) {
            this.mSchedulerThread = new HandlerThread("CameraX-scheduler", 10);
            this.mSchedulerThread.start();
            this.mSchedulerHandler = HandlerCompat.createAsync(this.mSchedulerThread.getLooper());
            return;
        }
        this.mSchedulerThread = null;
        this.mSchedulerHandler = schedulerHandler;
    }

    public static boolean hasCamera(CameraSelector cameraSelector) {
        try {
            cameraSelector.select(checkInitialized().getCameraRepository().getCameras());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static CameraInternal getCameraWithCameraSelector(CameraSelector cameraSelector) {
        return cameraSelector.select(checkInitialized().getCameraRepository().getCameras());
    }

    public static int getDefaultLensFacing() {
        checkInitialized();
        Integer lensFacingCandidate = null;
        Iterator<Integer> it = Arrays.asList(1, 0).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Integer lensFacing = it.next();
            if (hasCamera(new CameraSelector.Builder().requireLensFacing(lensFacing.intValue()).build())) {
                lensFacingCandidate = lensFacing;
                break;
            }
        }
        if (lensFacingCandidate != null) {
            return lensFacingCandidate.intValue();
        }
        throw new IllegalStateException("Unable to get default lens facing.");
    }

    public static CameraInfoInternal getCameraInfo(String cameraId) {
        return checkInitialized().getCameraRepository().getCamera(cameraId).getCameraInfoInternal();
    }

    public static CameraDeviceSurfaceManager getSurfaceManager() {
        return checkInitialized().getCameraDeviceSurfaceManager();
    }

    public static <C extends UseCaseConfig<?>> C getDefaultUseCaseConfig(Class<C> configType, CameraInfo cameraInfo) {
        return (C) checkInitialized().getDefaultConfigFactory().getConfig(configType, cameraInfo);
    }

    public static ListenableFuture<Void> initialize(Context context, CameraXConfig cameraXConfig) {
        ListenableFuture<Void> listenableFuture;
        synchronized (INSTANCE_LOCK) {
            Preconditions.checkNotNull(context);
            configureInstanceLocked(new CameraXConfig.Provider() { // from class: androidx.camera.core.-$$Lambda$CameraX$1mx06IrInQqXqFxYm74hARcHc64
                @Override // androidx.camera.core.CameraXConfig.Provider
                public final CameraXConfig getCameraXConfig() {
                    return CameraX.lambda$initialize$0(CameraXConfig.this);
                }
            });
            initializeInstanceLocked(context);
            listenableFuture = sInitializeFuture;
        }
        return listenableFuture;
    }

    public static /* synthetic */ CameraXConfig lambda$initialize$0(CameraXConfig cameraXConfig) {
        return cameraXConfig;
    }

    public static void configureInstance(CameraXConfig cameraXConfig) {
        synchronized (INSTANCE_LOCK) {
            configureInstanceLocked(new CameraXConfig.Provider() { // from class: androidx.camera.core.-$$Lambda$CameraX$wMZjJsgpnlorhgxL81349SKdSDE
                @Override // androidx.camera.core.CameraXConfig.Provider
                public final CameraXConfig getCameraXConfig() {
                    return CameraX.lambda$configureInstance$1(CameraXConfig.this);
                }
            });
        }
    }

    public static /* synthetic */ CameraXConfig lambda$configureInstance$1(CameraXConfig cameraXConfig) {
        return cameraXConfig;
    }

    private static void configureInstanceLocked(CameraXConfig.Provider configProvider) {
        Preconditions.checkNotNull(configProvider);
        Preconditions.checkState(sConfigProvider == null, "CameraX has already been configured. To use a different configuration, shutdown() must be called.");
        sConfigProvider = configProvider;
    }

    private static void initializeInstanceLocked(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkState(sInstance == null, "CameraX already initialized.");
        Preconditions.checkNotNull(sConfigProvider);
        CameraX cameraX = new CameraX(sConfigProvider.getCameraXConfig());
        sInstance = cameraX;
        sInitializeFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(context) { // from class: androidx.camera.core.-$$Lambda$CameraX$pfTQmIZkMo7eYt2wbNhnn4BBWWM
            private final /* synthetic */ Context f$1;

            {
                this.f$1 = r2;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return CameraX.lambda$initializeInstanceLocked$3(CameraX.this, this.f$1, completer);
            }
        });
    }

    public static /* synthetic */ Object lambda$initializeInstanceLocked$3(final CameraX cameraX, Context context, final CallbackToFutureAdapter.Completer completer) throws Exception {
        synchronized (INSTANCE_LOCK) {
            Futures.addCallback(FutureChain.from(sShutdownFuture).transformAsync(new AsyncFunction(context) { // from class: androidx.camera.core.-$$Lambda$CameraX$zGjevt7sTF6HDnDgHdMdO95T0_g
                private final /* synthetic */ Context f$1;

                {
                    this.f$1 = r2;
                }

                @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
                public final ListenableFuture apply(Object obj) {
                    Void r3 = (Void) obj;
                    return CameraX.this.initInternal(this.f$1);
                }
            }, CameraXExecutors.directExecutor()), new FutureCallback<Void>() { // from class: androidx.camera.core.CameraX.1
                public void onSuccess(Void result) {
                    completer.set(null);
                }

                @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                public void onFailure(Throwable t) {
                    Log.w(CameraX.TAG, "CameraX initialize() failed", t);
                    synchronized (CameraX.INSTANCE_LOCK) {
                        if (CameraX.sInstance == cameraX) {
                            CameraX.shutdownLocked();
                        }
                    }
                    completer.setException(t);
                }
            }, CameraXExecutors.directExecutor());
        }
        return "CameraX-initialize";
    }

    public static ListenableFuture<Void> shutdown() {
        ListenableFuture<Void> shutdownLocked;
        synchronized (INSTANCE_LOCK) {
            sConfigProvider = null;
            shutdownLocked = shutdownLocked();
        }
        return shutdownLocked;
    }

    static ListenableFuture<Void> shutdownLocked() {
        if (sInstance == null) {
            return sShutdownFuture;
        }
        CameraX cameraX = sInstance;
        sInstance = null;
        sShutdownFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.core.-$$Lambda$CameraX$RpN6H_GOvlkTaYOxBF7oFPOEV_4
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return CameraX.lambda$shutdownLocked$5(CameraX.this, completer);
            }
        });
        return sShutdownFuture;
    }

    public static /* synthetic */ Object lambda$shutdownLocked$5(CameraX cameraX, CallbackToFutureAdapter.Completer completer) throws Exception {
        synchronized (INSTANCE_LOCK) {
            sInitializeFuture.addListener(new Runnable(completer) { // from class: androidx.camera.core.-$$Lambda$CameraX$4dbkF7X2OvYAcnCqEbjT-6phEWo
                private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Futures.propagate(CameraX.this.shutdownInternal(), this.f$1);
                }
            }, CameraXExecutors.directExecutor());
        }
        return "CameraX shutdown";
    }

    public static Context getContext() {
        return checkInitialized().mApplication;
    }

    public static boolean isInitialized() {
        boolean z;
        synchronized (INSTANCE_LOCK) {
            z = sInstance != null && sInstance.isInitializedInternal();
        }
        return z;
    }

    public CameraFactory getCameraFactory() {
        CameraFactory cameraFactory = this.mCameraFactory;
        if (cameraFactory != null) {
            return cameraFactory;
        }
        throw new IllegalStateException("CameraX not initialized yet.");
    }

    private static CameraX checkInitialized() {
        CameraX cameraX = waitInitialized();
        Preconditions.checkState(cameraX.isInitializedInternal(), "Must call CameraX.initialize() first");
        return cameraX;
    }

    public static ListenableFuture<CameraX> getOrCreateInstance(Context context) {
        ListenableFuture<CameraX> instanceFuture;
        Preconditions.checkNotNull(context, "Context must not be null.");
        synchronized (INSTANCE_LOCK) {
            boolean isConfigured = sConfigProvider != null;
            instanceFuture = getInstanceLocked();
            if (instanceFuture.isDone()) {
                try {
                    try {
                        instanceFuture.get();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Unexpected thread interrupt. Should not be possible since future is already complete.", e);
                    }
                } catch (ExecutionException e2) {
                    shutdownLocked();
                    instanceFuture = null;
                }
            }
            if (instanceFuture == null) {
                Application app = (Application) context.getApplicationContext();
                if (!isConfigured) {
                    CameraXConfig.Provider configProvider = getConfigProvider(app);
                    if (configProvider != null) {
                        configureInstanceLocked(configProvider);
                    } else {
                        throw new IllegalStateException("CameraX is not configured properly. The most likely cause is you did not include a default implementation in your build such as 'camera-camera2'.");
                    }
                }
                initializeInstanceLocked(app);
                instanceFuture = getInstanceLocked();
            }
        }
        return instanceFuture;
    }

    private static CameraXConfig.Provider getConfigProvider(Application app) {
        if (app instanceof CameraXConfig.Provider) {
            return (CameraXConfig.Provider) app;
        }
        try {
            return (CameraXConfig.Provider) Class.forName(app.getResources().getString(R.string.androidx_camera_default_config_provider)).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Resources.NotFoundException | ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            Log.e(TAG, "Failed to retrieve default CameraXConfig.Provider from resources", e);
            return null;
        }
    }

    private static ListenableFuture<CameraX> getInstance() {
        ListenableFuture<CameraX> instanceLocked;
        synchronized (INSTANCE_LOCK) {
            instanceLocked = getInstanceLocked();
        }
        return instanceLocked;
    }

    private static ListenableFuture<CameraX> getInstanceLocked() {
        CameraX cameraX = sInstance;
        if (cameraX == null) {
            return Futures.immediateFailedFuture(new IllegalStateException("Must call CameraX.initialize() first"));
        }
        return Futures.transform(sInitializeFuture, new Function() { // from class: androidx.camera.core.-$$Lambda$CameraX$I0yV-40rhyMeVUFgVxnImXbjM_0
            @Override // androidx.arch.core.util.Function
            public final Object apply(Object obj) {
                return CameraX.lambda$getInstanceLocked$6(CameraX.this, (Void) obj);
            }
        }, CameraXExecutors.directExecutor());
    }

    public static /* synthetic */ CameraX lambda$getInstanceLocked$6(CameraX cameraX, Void nullVoid) {
        return cameraX;
    }

    private static CameraX waitInitialized() {
        try {
            return getInstance().get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IllegalStateException(e);
        }
    }

    public CameraDeviceSurfaceManager getCameraDeviceSurfaceManager() {
        CameraDeviceSurfaceManager cameraDeviceSurfaceManager = this.mSurfaceManager;
        if (cameraDeviceSurfaceManager != null) {
            return cameraDeviceSurfaceManager;
        }
        throw new IllegalStateException("CameraX not initialized yet.");
    }

    public CameraRepository getCameraRepository() {
        return this.mCameraRepository;
    }

    private UseCaseConfigFactory getDefaultConfigFactory() {
        UseCaseConfigFactory useCaseConfigFactory = this.mDefaultConfigFactory;
        if (useCaseConfigFactory != null) {
            return useCaseConfigFactory;
        }
        throw new IllegalStateException("CameraX not initialized yet.");
    }

    public ListenableFuture<Void> initInternal(Context context) {
        ListenableFuture<Void> future;
        synchronized (this.mInitializeLock) {
            Preconditions.checkState(this.mInitState == InternalInitState.UNINITIALIZED, "CameraX.initInternal() should only be called once per instance");
            this.mInitState = InternalInitState.INITIALIZING;
            future = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(this.mCameraExecutor, context) { // from class: androidx.camera.core.-$$Lambda$CameraX$D33DcGTFi_XlxKFIr5BiyQWdPUc
                private final /* synthetic */ Executor f$1;
                private final /* synthetic */ Context f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                    return CameraX.this.lambda$initInternal$8$CameraX(this.f$1, this.f$2, completer);
                }
            });
        }
        return future;
    }

    public /* synthetic */ Object lambda$initInternal$8$CameraX(Executor cameraExecutor, Context context, CallbackToFutureAdapter.Completer completer) throws Exception {
        cameraExecutor.execute(new Runnable(context, cameraExecutor, completer) { // from class: androidx.camera.core.-$$Lambda$CameraX$PC4SOFGjuqUVT4bexY644vLmWFE
            private final /* synthetic */ Context f$1;
            private final /* synthetic */ Executor f$2;
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CameraX.this.lambda$initInternal$7$CameraX(this.f$1, this.f$2, this.f$3);
            }
        });
        return "CameraX initInternal";
    }

    public /* synthetic */ void lambda$initInternal$7$CameraX(Context context, Executor cameraExecutor, CallbackToFutureAdapter.Completer completer) {
        CameraFactory.Provider cameraFactoryProvider;
        try {
            InitializationException initException = null;
            try {
                this.mApplication = (Application) context.getApplicationContext();
                cameraFactoryProvider = this.mCameraXConfig.getCameraFactoryProvider(null);
            } catch (InitializationException e) {
                initException = e;
                synchronized (this.mInitializeLock) {
                    try {
                        this.mInitState = InternalInitState.INITIALIZED;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            } catch (RuntimeException e2) {
                InitializationException initException2 = new InitializationException(e2);
                synchronized (this.mInitializeLock) {
                    try {
                        this.mInitState = InternalInitState.INITIALIZED;
                        completer.setException(initException2);
                        return;
                    } catch (Throwable th2) {
                        throw th2;
                    }
                }
            }
            if (cameraFactoryProvider != null) {
                this.mCameraFactory = cameraFactoryProvider.newInstance(context, CameraThreadConfig.create(this.mCameraExecutor, this.mSchedulerHandler));
                CameraDeviceSurfaceManager.Provider surfaceManagerProvider = this.mCameraXConfig.getDeviceSurfaceManagerProvider(null);
                if (surfaceManagerProvider != null) {
                    this.mSurfaceManager = surfaceManagerProvider.newInstance(context);
                    UseCaseConfigFactory.Provider configFactoryProvider = this.mCameraXConfig.getUseCaseConfigFactoryProvider(null);
                    if (configFactoryProvider != null) {
                        this.mDefaultConfigFactory = configFactoryProvider.newInstance(context);
                        if (cameraExecutor instanceof CameraExecutor) {
                            ((CameraExecutor) cameraExecutor).init(this.mCameraFactory);
                        }
                        this.mCameraRepository.init(this.mCameraFactory);
                        synchronized (this.mInitializeLock) {
                            try {
                                this.mInitState = InternalInitState.INITIALIZED;
                            } catch (Throwable th3) {
                                throw th3;
                            }
                        }
                        if (0 == 0) {
                            completer.set(null);
                            return;
                        }
                        completer.setException(initException);
                        return;
                    }
                    throw new InitializationException(new IllegalArgumentException("Invalid app configuration provided. Missing UseCaseConfigFactory."));
                }
                throw new InitializationException(new IllegalArgumentException("Invalid app configuration provided. Missing CameraDeviceSurfaceManager."));
            }
            throw new InitializationException(new IllegalArgumentException("Invalid app configuration provided. Missing CameraFactory."));
        } catch (Throwable th4) {
            synchronized (this.mInitializeLock) {
                try {
                    this.mInitState = InternalInitState.INITIALIZED;
                    if (0 != 0) {
                        completer.setException(null);
                    } else {
                        completer.set(null);
                    }
                    throw th4;
                } catch (Throwable th5) {
                    throw th5;
                }
            }
        }
    }

    /* renamed from: androidx.camera.core.CameraX$2 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$androidx$camera$core$CameraX$InternalInitState = new int[InternalInitState.values().length];

        static {
            try {
                $SwitchMap$androidx$camera$core$CameraX$InternalInitState[InternalInitState.UNINITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$camera$core$CameraX$InternalInitState[InternalInitState.INITIALIZING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$camera$core$CameraX$InternalInitState[InternalInitState.INITIALIZED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$camera$core$CameraX$InternalInitState[InternalInitState.SHUTDOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ListenableFuture<Void> shutdownInternal() {
        synchronized (this.mInitializeLock) {
            int i = AnonymousClass2.$SwitchMap$androidx$camera$core$CameraX$InternalInitState[this.mInitState.ordinal()];
            if (i == 1) {
                this.mInitState = InternalInitState.SHUTDOWN;
                return Futures.immediateFuture(null);
            } else if (i != 2) {
                if (i == 3) {
                    this.mInitState = InternalInitState.SHUTDOWN;
                    this.mShutdownInternalFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.core.-$$Lambda$CameraX$Jy8U7dredZwgS3-mPX7i5cgr_aE
                        @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                        public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                            return CameraX.this.lambda$shutdownInternal$10$CameraX(completer);
                        }
                    });
                }
                return this.mShutdownInternalFuture;
            } else {
                throw new IllegalStateException("CameraX could not be shutdown when it is initializing.");
            }
        }
    }

    public /* synthetic */ Object lambda$shutdownInternal$10$CameraX(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mCameraRepository.deinit().addListener(new Runnable(completer) { // from class: androidx.camera.core.-$$Lambda$CameraX$xyZRXHQOdk_4PkY-4Z_bL27lyqs
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CameraX.this.lambda$shutdownInternal$9$CameraX(this.f$1);
            }
        }, this.mCameraExecutor);
        return "CameraX shutdownInternal";
    }

    public /* synthetic */ void lambda$shutdownInternal$9$CameraX(CallbackToFutureAdapter.Completer completer) {
        if (this.mSchedulerThread != null) {
            Executor executor = this.mCameraExecutor;
            if (executor instanceof CameraExecutor) {
                ((CameraExecutor) executor).deinit();
            }
            this.mSchedulerThread.quit();
            completer.set(null);
        }
    }

    private boolean isInitializedInternal() {
        boolean z;
        synchronized (this.mInitializeLock) {
            z = this.mInitState == InternalInitState.INITIALIZED;
        }
        return z;
    }
}
