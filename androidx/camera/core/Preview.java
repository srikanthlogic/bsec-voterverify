package androidx.camera.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Pair;
import android.util.Rational;
import android.util.Size;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraCaptureResult;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.CaptureProcessor;
import androidx.camera.core.impl.CaptureStage;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ConfigProvider;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.ImageInfoProcessor;
import androidx.camera.core.impl.ImageInputConfig;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.MutableConfig;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.utils.Threads;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.core.internal.CameraCaptureResultImageInfo;
import androidx.camera.core.internal.TargetConfig;
import androidx.camera.core.internal.ThreadConfig;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class Preview extends UseCase {
    public static final Defaults DEFAULT_CONFIG = new Defaults();
    private static final Executor DEFAULT_SURFACE_PROVIDER_EXECUTOR = CameraXExecutors.mainThreadExecutor();
    private static final String TAG;
    private Size mLatestResolution;
    private Handler mProcessingPreviewHandler;
    private HandlerThread mProcessingPreviewThread;
    private DeferrableSurface mSessionDeferrableSurface;
    SurfaceProvider mSurfaceProvider;
    private CallbackToFutureAdapter.Completer<Pair<SurfaceProvider, Executor>> mSurfaceProviderCompleter;
    Executor mSurfaceProviderExecutor = DEFAULT_SURFACE_PROVIDER_EXECUTOR;

    /* loaded from: classes.dex */
    public interface SurfaceProvider {
        void onSurfaceRequested(SurfaceRequest surfaceRequest);
    }

    Preview(PreviewConfig config) {
        super(config);
    }

    SessionConfig.Builder createPipeline(final String cameraId, final PreviewConfig config, final Size resolution) {
        Threads.checkMainThread();
        SessionConfig.Builder sessionConfigBuilder = SessionConfig.Builder.createFrom(config);
        CaptureProcessor captureProcessor = config.getCaptureProcessor(null);
        DeferrableSurface deferrableSurface = this.mSessionDeferrableSurface;
        if (deferrableSurface != null) {
            deferrableSurface.close();
        }
        SurfaceRequest surfaceRequest = new SurfaceRequest(resolution, getCamera(), getViewPortCropRect());
        setUpSurfaceProviderWrap(surfaceRequest);
        if (captureProcessor != null) {
            CaptureStage captureStage = new CaptureStage.DefaultCaptureStage();
            if (this.mProcessingPreviewThread == null) {
                this.mProcessingPreviewThread = new HandlerThread("CameraX-preview_processing");
                this.mProcessingPreviewThread.start();
                this.mProcessingPreviewHandler = new Handler(this.mProcessingPreviewThread.getLooper());
            }
            ProcessingSurface processingSurface = new ProcessingSurface(resolution.getWidth(), resolution.getHeight(), config.getInputFormat(), this.mProcessingPreviewHandler, captureStage, captureProcessor, surfaceRequest.getDeferrableSurface());
            sessionConfigBuilder.addCameraCaptureCallback(processingSurface.getCameraCaptureCallback());
            this.mSessionDeferrableSurface = processingSurface;
            sessionConfigBuilder.setTag(Integer.valueOf(captureStage.getId()));
        } else {
            final ImageInfoProcessor processor = config.getImageInfoProcessor(null);
            if (processor != null) {
                sessionConfigBuilder.addCameraCaptureCallback(new CameraCaptureCallback() { // from class: androidx.camera.core.Preview.1
                    @Override // androidx.camera.core.impl.CameraCaptureCallback
                    public void onCaptureCompleted(CameraCaptureResult cameraCaptureResult) {
                        super.onCaptureCompleted(cameraCaptureResult);
                        if (processor.process(new CameraCaptureResultImageInfo(cameraCaptureResult))) {
                            Preview.this.notifyUpdated();
                        }
                    }
                });
            }
            this.mSessionDeferrableSurface = surfaceRequest.getDeferrableSurface();
        }
        sessionConfigBuilder.addSurface(this.mSessionDeferrableSurface);
        sessionConfigBuilder.addErrorListener(new SessionConfig.ErrorListener() { // from class: androidx.camera.core.Preview.2
            @Override // androidx.camera.core.impl.SessionConfig.ErrorListener
            public void onError(SessionConfig sessionConfig, SessionConfig.SessionError error) {
                if (Preview.this.isCurrentCamera(cameraId)) {
                    Preview.this.updateSessionConfig(Preview.this.createPipeline(cameraId, config, resolution).build());
                    Preview.this.notifyReset();
                }
            }
        });
        return sessionConfigBuilder;
    }

    private void setUpSurfaceProviderWrap(final SurfaceRequest surfaceRequest) {
        Futures.addCallback(CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.core.-$$Lambda$Preview$-2Y6pxk1WRlWdi3uDuYQ1iY_5tY
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return Preview.this.lambda$setUpSurfaceProviderWrap$0$Preview(completer);
            }
        }), new FutureCallback<Pair<SurfaceProvider, Executor>>() { // from class: androidx.camera.core.Preview.3
            public void onSuccess(Pair<SurfaceProvider, Executor> result) {
                if (result != null) {
                    SurfaceProvider surfaceProvider = (SurfaceProvider) result.first;
                    Executor executor = (Executor) result.second;
                    if (surfaceProvider != null && executor != null) {
                        executor.execute(new Runnable(surfaceRequest) { // from class: androidx.camera.core.-$$Lambda$Preview$3$4Z76LKiBtW3vebT-a72VPXQU9_4
                            private final /* synthetic */ SurfaceRequest f$1;

                            {
                                this.f$1 = r2;
                            }

                            @Override // java.lang.Runnable
                            public final void run() {
                                Preview.AnonymousClass3.lambda$onSuccess$0(Preview.SurfaceProvider.this, this.f$1);
                            }
                        });
                    }
                }
            }

            public static /* synthetic */ void lambda$onSuccess$0(SurfaceProvider surfaceProvider, SurfaceRequest surfaceRequest2) {
                surfaceProvider.onSurfaceRequested(surfaceRequest2);
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
                surfaceRequest.getDeferrableSurface().close();
            }
        }, CameraXExecutors.directExecutor());
    }

    public /* synthetic */ Object lambda$setUpSurfaceProviderWrap$0$Preview(CallbackToFutureAdapter.Completer completer) throws Exception {
        CallbackToFutureAdapter.Completer<Pair<SurfaceProvider, Executor>> completer2 = this.mSurfaceProviderCompleter;
        if (completer2 != null) {
            completer2.setCancelled();
        }
        this.mSurfaceProviderCompleter = completer;
        SurfaceProvider surfaceProvider = this.mSurfaceProvider;
        if (surfaceProvider == null) {
            return "surface provider and executor future";
        }
        this.mSurfaceProviderCompleter.set(new Pair<>(surfaceProvider, this.mSurfaceProviderExecutor));
        this.mSurfaceProviderCompleter = null;
        return "surface provider and executor future";
    }

    public void setSurfaceProvider(Executor executor, SurfaceProvider surfaceProvider) {
        Threads.checkMainThread();
        if (surfaceProvider == null) {
            this.mSurfaceProvider = null;
            notifyInactive();
            return;
        }
        this.mSurfaceProvider = surfaceProvider;
        this.mSurfaceProviderExecutor = executor;
        notifyActive();
        onSurfaceProviderAvailable();
        DeferrableSurface deferrableSurface = this.mSessionDeferrableSurface;
        if (deferrableSurface != null) {
            deferrableSurface.close();
        }
        notifyReset();
    }

    private void onSurfaceProviderAvailable() {
        CallbackToFutureAdapter.Completer<Pair<SurfaceProvider, Executor>> completer = this.mSurfaceProviderCompleter;
        if (completer != null) {
            completer.set(new Pair<>(this.mSurfaceProvider, this.mSurfaceProviderExecutor));
            this.mSurfaceProviderCompleter = null;
        } else if (this.mLatestResolution != null) {
            updateConfigAndOutput(getCameraId(), (PreviewConfig) getUseCaseConfig(), this.mLatestResolution);
        }
    }

    public void setSurfaceProvider(SurfaceProvider surfaceProvider) {
        setSurfaceProvider(DEFAULT_SURFACE_PROVIDER_EXECUTOR, surfaceProvider);
    }

    private void updateConfigAndOutput(String cameraId, PreviewConfig config, Size resolution) {
        updateSessionConfig(createPipeline(cameraId, config, resolution).build());
    }

    public int getTargetRotation() {
        return ((PreviewConfig) getUseCaseConfig()).getTargetRotation();
    }

    public String toString() {
        return "Preview:" + getName();
    }

    @Override // androidx.camera.core.UseCase
    public UseCaseConfig.Builder<?, ?, ?> getDefaultBuilder(CameraInfo cameraInfo) {
        PreviewConfig defaults = (PreviewConfig) CameraX.getDefaultUseCaseConfig(PreviewConfig.class, cameraInfo);
        if (defaults != null) {
            return Builder.fromConfig(defaults);
        }
        return null;
    }

    @Override // androidx.camera.core.UseCase
    public void clear() {
        notifyInactive();
        DeferrableSurface deferrableSurface = this.mSessionDeferrableSurface;
        if (deferrableSurface != null) {
            deferrableSurface.close();
            this.mSessionDeferrableSurface.getTerminationFuture().addListener(new Runnable() { // from class: androidx.camera.core.-$$Lambda$Preview$TGDLF-nNGOwNWIdohdCaiYobnS4
                @Override // java.lang.Runnable
                public final void run() {
                    Preview.this.lambda$clear$1$Preview();
                }
            }, CameraXExecutors.directExecutor());
        }
        CallbackToFutureAdapter.Completer<Pair<SurfaceProvider, Executor>> completer = this.mSurfaceProviderCompleter;
        if (completer != null) {
            completer.setCancelled();
            this.mSurfaceProviderCompleter = null;
        }
    }

    public /* synthetic */ void lambda$clear$1$Preview() {
        HandlerThread handlerThread = this.mProcessingPreviewThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            this.mProcessingPreviewThread = null;
        }
    }

    @Override // androidx.camera.core.UseCase
    public void onDestroy() {
        this.mSurfaceProvider = null;
    }

    @Override // androidx.camera.core.UseCase
    protected Size onSuggestedResolutionUpdated(Size suggestedResolution) {
        this.mLatestResolution = suggestedResolution;
        updateConfigAndOutput(getCameraId(), (PreviewConfig) getUseCaseConfig(), this.mLatestResolution);
        return this.mLatestResolution;
    }

    /* loaded from: classes.dex */
    public static final class Defaults implements ConfigProvider<PreviewConfig> {
        private static final int DEFAULT_SURFACE_OCCUPANCY_PRIORITY;
        private static final Size DEFAULT_MAX_RESOLUTION = CameraX.getSurfaceManager().getPreviewSize();
        private static final PreviewConfig DEFAULT_CONFIG = new Builder().setMaxResolution(DEFAULT_MAX_RESOLUTION).setSurfaceOccupancyPriority(2).getUseCaseConfig();

        @Override // androidx.camera.core.impl.ConfigProvider
        public PreviewConfig getConfig(CameraInfo cameraInfo) {
            return DEFAULT_CONFIG;
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder implements UseCaseConfig.Builder<Preview, PreviewConfig, Builder>, ImageOutputConfig.Builder<Builder>, ThreadConfig.Builder<Builder> {
        private final MutableOptionsBundle mMutableConfig;

        public Builder() {
            this(MutableOptionsBundle.create());
        }

        private Builder(MutableOptionsBundle mutableConfig) {
            this.mMutableConfig = mutableConfig;
            Class<?> oldConfigClass = (Class) mutableConfig.retrieveOption(TargetConfig.OPTION_TARGET_CLASS, null);
            if (oldConfigClass == null || oldConfigClass.equals(Preview.class)) {
                setTargetClass(Preview.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + oldConfigClass);
        }

        public static Builder fromConfig(PreviewConfig configuration) {
            return new Builder(MutableOptionsBundle.from((Config) configuration));
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public MutableConfig getMutableConfig() {
            return this.mMutableConfig;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public PreviewConfig getUseCaseConfig() {
            return new PreviewConfig(OptionsBundle.from(this.mMutableConfig));
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public Preview build() {
            if (getMutableConfig().retrieveOption(PreviewConfig.OPTION_TARGET_ASPECT_RATIO, null) == null || getMutableConfig().retrieveOption(PreviewConfig.OPTION_TARGET_RESOLUTION, null) == null) {
                if (getMutableConfig().retrieveOption(PreviewConfig.OPTION_PREVIEW_CAPTURE_PROCESSOR, null) != null) {
                    getMutableConfig().insertOption(ImageInputConfig.OPTION_INPUT_FORMAT, 35);
                } else {
                    getMutableConfig().insertOption(ImageInputConfig.OPTION_INPUT_FORMAT, 34);
                }
                return new Preview(getUseCaseConfig());
            }
            throw new IllegalArgumentException("Cannot use both setTargetResolution and setTargetAspectRatio on the same config.");
        }

        @Override // androidx.camera.core.internal.TargetConfig.Builder
        public Builder setTargetClass(Class<Preview> targetClass) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_TARGET_CLASS, targetClass);
            if (getMutableConfig().retrieveOption(PreviewConfig.OPTION_TARGET_NAME, null) == null) {
                setTargetName(targetClass.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        @Override // androidx.camera.core.internal.TargetConfig.Builder
        public Builder setTargetName(String targetName) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_TARGET_NAME, targetName);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetAspectRatioCustom(Rational aspectRatio) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM, aspectRatio);
            getMutableConfig().removeOption(PreviewConfig.OPTION_TARGET_ASPECT_RATIO);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetAspectRatio(int aspectRatio) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_TARGET_ASPECT_RATIO, Integer.valueOf(aspectRatio));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetRotation(int rotation) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_TARGET_ROTATION, Integer.valueOf(rotation));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetResolution(Size resolution) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_RESOLUTION, resolution);
            if (resolution != null) {
                getMutableConfig().insertOption(PreviewConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM, new Rational(resolution.getWidth(), resolution.getHeight()));
            }
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setDefaultResolution(Size resolution) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_DEFAULT_RESOLUTION, resolution);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setMaxResolution(Size resolution) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_MAX_RESOLUTION, resolution);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setSupportedResolutions(List<Pair<Integer, Size[]>> resolutions) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_SUPPORTED_RESOLUTIONS, resolutions);
            return this;
        }

        @Override // androidx.camera.core.internal.ThreadConfig.Builder
        public Builder setBackgroundExecutor(Executor executor) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_BACKGROUND_EXECUTOR, executor);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setDefaultSessionConfig(SessionConfig sessionConfig) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_DEFAULT_SESSION_CONFIG, sessionConfig);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setDefaultCaptureConfig(CaptureConfig captureConfig) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_DEFAULT_CAPTURE_CONFIG, captureConfig);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setSessionOptionUnpacker(SessionConfig.OptionUnpacker optionUnpacker) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_SESSION_CONFIG_UNPACKER, optionUnpacker);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setCaptureOptionUnpacker(CaptureConfig.OptionUnpacker optionUnpacker) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_CAPTURE_CONFIG_UNPACKER, optionUnpacker);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setSurfaceOccupancyPriority(int priority) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_SURFACE_OCCUPANCY_PRIORITY, Integer.valueOf(priority));
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setCameraSelector(CameraSelector cameraSelector) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_CAMERA_SELECTOR, cameraSelector);
            return this;
        }

        @Override // androidx.camera.core.internal.UseCaseEventConfig.Builder
        public Builder setUseCaseEventCallback(UseCase.EventCallback useCaseEventCallback) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_USE_CASE_EVENT_CALLBACK, useCaseEventCallback);
            return this;
        }

        public Builder setImageInfoProcessor(ImageInfoProcessor processor) {
            getMutableConfig().insertOption(PreviewConfig.IMAGE_INFO_PROCESSOR, processor);
            return this;
        }

        public Builder setCaptureProcessor(CaptureProcessor captureProcessor) {
            getMutableConfig().insertOption(PreviewConfig.OPTION_PREVIEW_CAPTURE_PROCESSOR, captureProcessor);
            return this;
        }
    }
}
