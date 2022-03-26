package androidx.camera.core;

import android.util.Log;
import android.util.Pair;
import android.util.Rational;
import android.util.Size;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.UseCase;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ConfigProvider;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.ImageAnalysisConfig;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.ImmediateSurface;
import androidx.camera.core.impl.MutableConfig;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.utils.Threads;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.internal.TargetConfig;
import androidx.camera.core.internal.ThreadConfig;
import androidx.camera.core.internal.utils.UseCaseConfigUtil;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ImageAnalysis extends UseCase {
    public static final Defaults DEFAULT_CONFIG = new Defaults();
    private static final int NON_BLOCKING_IMAGE_DEPTH;
    public static final int STRATEGY_BLOCK_PRODUCER;
    public static final int STRATEGY_KEEP_ONLY_LATEST;
    private static final String TAG;
    private final Object mAnalysisLock = new Object();
    private DeferrableSurface mDeferrableSurface;
    final ImageAnalysisAbstractAnalyzer mImageAnalysisAbstractAnalyzer;
    private Analyzer mSubscribedAnalyzer;

    /* loaded from: classes.dex */
    public interface Analyzer {
        void analyze(ImageProxy imageProxy);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BackpressureStrategy {
    }

    ImageAnalysis(ImageAnalysisConfig config) {
        super(config);
        if (((ImageAnalysisConfig) getUseCaseConfig()).getBackpressureStrategy() == 1) {
            this.mImageAnalysisAbstractAnalyzer = new ImageAnalysisBlockingAnalyzer();
        } else {
            this.mImageAnalysisAbstractAnalyzer = new ImageAnalysisNonBlockingAnalyzer(config.getBackgroundExecutor(CameraXExecutors.highPriorityExecutor()));
        }
    }

    SessionConfig.Builder createPipeline(String cameraId, ImageAnalysisConfig config, Size resolution) {
        SafeCloseImageReaderProxy imageReaderProxy;
        Threads.checkMainThread();
        Executor backgroundExecutor = (Executor) Preconditions.checkNotNull(config.getBackgroundExecutor(CameraXExecutors.highPriorityExecutor()));
        int imageQueueDepth = config.getBackpressureStrategy() == 1 ? config.getImageQueueDepth() : 4;
        if (config.getImageReaderProxyProvider() != null) {
            imageReaderProxy = new SafeCloseImageReaderProxy(config.getImageReaderProxyProvider().newInstance(resolution.getWidth(), resolution.getHeight(), getImageFormat(), imageQueueDepth, 0));
        } else {
            imageReaderProxy = new SafeCloseImageReaderProxy(ImageReaderProxys.createIsolatedReader(resolution.getWidth(), resolution.getHeight(), getImageFormat(), imageQueueDepth));
        }
        tryUpdateRelativeRotation();
        this.mImageAnalysisAbstractAnalyzer.open();
        imageReaderProxy.setOnImageAvailableListener(this.mImageAnalysisAbstractAnalyzer, backgroundExecutor);
        SessionConfig.Builder sessionConfigBuilder = SessionConfig.Builder.createFrom(config);
        DeferrableSurface deferrableSurface = this.mDeferrableSurface;
        if (deferrableSurface != null) {
            deferrableSurface.close();
        }
        this.mDeferrableSurface = new ImmediateSurface(imageReaderProxy.getSurface());
        ListenableFuture<Void> terminationFuture = this.mDeferrableSurface.getTerminationFuture();
        Objects.requireNonNull(imageReaderProxy);
        terminationFuture.addListener(new Runnable() { // from class: androidx.camera.core.-$$Lambda$kBKr-WXSgibOrSk3AWcyOrTIBI0
            @Override // java.lang.Runnable
            public final void run() {
                SafeCloseImageReaderProxy.this.safeClose();
            }
        }, CameraXExecutors.mainThreadExecutor());
        sessionConfigBuilder.addSurface(this.mDeferrableSurface);
        sessionConfigBuilder.addErrorListener(new SessionConfig.ErrorListener(cameraId, config, resolution) { // from class: androidx.camera.core.-$$Lambda$ImageAnalysis$Gd6VUhKl5XOmwdAgw0vhwsuiNpE
            private final /* synthetic */ String f$1;
            private final /* synthetic */ ImageAnalysisConfig f$2;
            private final /* synthetic */ Size f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // androidx.camera.core.impl.SessionConfig.ErrorListener
            public final void onError(SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
                ImageAnalysis.this.lambda$createPipeline$0$ImageAnalysis(this.f$1, this.f$2, this.f$3, sessionConfig, sessionError);
            }
        });
        return sessionConfigBuilder;
    }

    public /* synthetic */ void lambda$createPipeline$0$ImageAnalysis(String cameraId, ImageAnalysisConfig config, Size resolution, SessionConfig sessionConfig, SessionConfig.SessionError error) {
        clearPipeline();
        if (isCurrentCamera(cameraId)) {
            updateSessionConfig(createPipeline(cameraId, config, resolution).build());
            notifyReset();
        }
    }

    void clearPipeline() {
        Threads.checkMainThread();
        this.mImageAnalysisAbstractAnalyzer.close();
        DeferrableSurface deferrableSurface = this.mDeferrableSurface;
        if (deferrableSurface != null) {
            deferrableSurface.close();
            this.mDeferrableSurface = null;
        }
    }

    public void clearAnalyzer() {
        synchronized (this.mAnalysisLock) {
            this.mImageAnalysisAbstractAnalyzer.setAnalyzer(null, null);
            this.mImageAnalysisAbstractAnalyzer.close();
            if (this.mSubscribedAnalyzer != null) {
                notifyInactive();
            }
            this.mSubscribedAnalyzer = null;
        }
    }

    public int getTargetRotation() {
        return ((ImageAnalysisConfig) getUseCaseConfig()).getTargetRotation();
    }

    public void setTargetRotation(int rotation) {
        ImageAnalysisConfig oldConfig = (ImageAnalysisConfig) getUseCaseConfig();
        Builder builder = Builder.fromConfig(oldConfig);
        int oldRotation = oldConfig.getTargetRotation(-1);
        if (oldRotation == -1 || oldRotation != rotation) {
            UseCaseConfigUtil.updateTargetRotationAndRelatedConfigs(builder, rotation);
            updateUseCaseConfig(builder.getUseCaseConfig());
            try {
                tryUpdateRelativeRotation();
            } catch (Exception e) {
                Log.w(TAG, "Unable to get camera id for the use case.");
            }
        }
    }

    public void setAnalyzer(Executor executor, Analyzer analyzer) {
        synchronized (this.mAnalysisLock) {
            this.mImageAnalysisAbstractAnalyzer.open();
            this.mImageAnalysisAbstractAnalyzer.setAnalyzer(executor, new Analyzer(analyzer) { // from class: androidx.camera.core.-$$Lambda$ImageAnalysis$SK4XUin3LEuOy83vccLKZvMIwq8
                private final /* synthetic */ ImageAnalysis.Analyzer f$1;

                {
                    this.f$1 = r2;
                }

                @Override // androidx.camera.core.ImageAnalysis.Analyzer
                public final void analyze(ImageProxy imageProxy) {
                    ImageAnalysis.this.lambda$setAnalyzer$1$ImageAnalysis(this.f$1, imageProxy);
                }
            });
            if (this.mSubscribedAnalyzer == null) {
                notifyActive();
            }
            this.mSubscribedAnalyzer = analyzer;
        }
    }

    public /* synthetic */ void lambda$setAnalyzer$1$ImageAnalysis(Analyzer analyzer, ImageProxy image) {
        if (getViewPortCropRect() != null) {
            image.setCropRect(getViewPortCropRect());
        }
        analyzer.analyze(image);
    }

    public int getBackpressureStrategy() {
        return ((ImageAnalysisConfig) getUseCaseConfig()).getBackpressureStrategy();
    }

    public int getImageQueueDepth() {
        return ((ImageAnalysisConfig) getUseCaseConfig()).getImageQueueDepth();
    }

    public String toString() {
        return "ImageAnalysis:" + getName();
    }

    @Override // androidx.camera.core.UseCase
    public void clear() {
        clearPipeline();
    }

    @Override // androidx.camera.core.UseCase
    public void onDestroy() {
        clearAnalyzer();
    }

    @Override // androidx.camera.core.UseCase
    public UseCaseConfig.Builder<?, ?, ?> getDefaultBuilder(CameraInfo cameraInfo) {
        ImageAnalysisConfig defaults = (ImageAnalysisConfig) CameraX.getDefaultUseCaseConfig(ImageAnalysisConfig.class, cameraInfo);
        if (defaults != null) {
            return Builder.fromConfig(defaults);
        }
        return null;
    }

    @Override // androidx.camera.core.UseCase
    protected Size onSuggestedResolutionUpdated(Size suggestedResolution) {
        updateSessionConfig(createPipeline(getCameraId(), (ImageAnalysisConfig) getUseCaseConfig(), suggestedResolution).build());
        return suggestedResolution;
    }

    private void tryUpdateRelativeRotation() {
        this.mImageAnalysisAbstractAnalyzer.setRelativeRotation(getCamera().getCameraInfoInternal().getSensorRotationDegrees(((ImageOutputConfig) getUseCaseConfig()).getTargetRotation(0)));
    }

    /* loaded from: classes.dex */
    public static final class Defaults implements ConfigProvider<ImageAnalysisConfig> {
        private static final int DEFAULT_BACKPRESSURE_STRATEGY;
        private static final int DEFAULT_IMAGE_QUEUE_DEPTH;
        private static final int DEFAULT_SURFACE_OCCUPANCY_PRIORITY;
        private static final Size DEFAULT_TARGET_RESOLUTION = new Size(640, 480);
        private static final Size DEFAULT_MAX_RESOLUTION = new Size(1920, 1080);
        private static final ImageAnalysisConfig DEFAULT_CONFIG = new Builder().setBackpressureStrategy(0).setImageQueueDepth(6).setDefaultResolution(DEFAULT_TARGET_RESOLUTION).setMaxResolution(DEFAULT_MAX_RESOLUTION).setSurfaceOccupancyPriority(1).getUseCaseConfig();

        @Override // androidx.camera.core.impl.ConfigProvider
        public ImageAnalysisConfig getConfig(CameraInfo cameraInfo) {
            return DEFAULT_CONFIG;
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder implements ImageOutputConfig.Builder<Builder>, ThreadConfig.Builder<Builder>, UseCaseConfig.Builder<ImageAnalysis, ImageAnalysisConfig, Builder> {
        private final MutableOptionsBundle mMutableConfig;

        public Builder() {
            this(MutableOptionsBundle.create());
        }

        private Builder(MutableOptionsBundle mutableConfig) {
            this.mMutableConfig = mutableConfig;
            Class<?> oldConfigClass = (Class) mutableConfig.retrieveOption(TargetConfig.OPTION_TARGET_CLASS, null);
            if (oldConfigClass == null || oldConfigClass.equals(ImageAnalysis.class)) {
                setTargetClass(ImageAnalysis.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + oldConfigClass);
        }

        public static Builder fromConfig(ImageAnalysisConfig configuration) {
            return new Builder(MutableOptionsBundle.from((Config) configuration));
        }

        public Builder setBackpressureStrategy(int strategy) {
            getMutableConfig().insertOption(ImageAnalysisConfig.OPTION_BACKPRESSURE_STRATEGY, Integer.valueOf(strategy));
            return this;
        }

        public Builder setImageQueueDepth(int depth) {
            getMutableConfig().insertOption(ImageAnalysisConfig.OPTION_IMAGE_QUEUE_DEPTH, Integer.valueOf(depth));
            return this;
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public MutableConfig getMutableConfig() {
            return this.mMutableConfig;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public ImageAnalysisConfig getUseCaseConfig() {
            return new ImageAnalysisConfig(OptionsBundle.from(this.mMutableConfig));
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public ImageAnalysis build() {
            if (getMutableConfig().retrieveOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO, null) == null || getMutableConfig().retrieveOption(ImageOutputConfig.OPTION_TARGET_RESOLUTION, null) == null) {
                return new ImageAnalysis(getUseCaseConfig());
            }
            throw new IllegalArgumentException("Cannot use both setTargetResolution and setTargetAspectRatio on the same config.");
        }

        @Override // androidx.camera.core.internal.TargetConfig.Builder
        public Builder setTargetClass(Class<ImageAnalysis> targetClass) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_TARGET_CLASS, targetClass);
            if (getMutableConfig().retrieveOption(UseCaseConfig.OPTION_TARGET_NAME, null) == null) {
                setTargetName(targetClass.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        @Override // androidx.camera.core.internal.TargetConfig.Builder
        public Builder setTargetName(String targetName) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_TARGET_NAME, targetName);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetAspectRatioCustom(Rational aspectRatio) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM, aspectRatio);
            getMutableConfig().removeOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetAspectRatio(int aspectRatio) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO, Integer.valueOf(aspectRatio));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetRotation(int rotation) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_ROTATION, Integer.valueOf(rotation));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetResolution(Size resolution) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_RESOLUTION, resolution);
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM, new Rational(resolution.getWidth(), resolution.getHeight()));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setDefaultResolution(Size resolution) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_DEFAULT_RESOLUTION, resolution);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setMaxResolution(Size resolution) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_MAX_RESOLUTION, resolution);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setSupportedResolutions(List<Pair<Integer, Size[]>> resolutions) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_SUPPORTED_RESOLUTIONS, resolutions);
            return this;
        }

        @Override // androidx.camera.core.internal.ThreadConfig.Builder
        public Builder setBackgroundExecutor(Executor executor) {
            getMutableConfig().insertOption(ThreadConfig.OPTION_BACKGROUND_EXECUTOR, executor);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setDefaultSessionConfig(SessionConfig sessionConfig) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_DEFAULT_SESSION_CONFIG, sessionConfig);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setDefaultCaptureConfig(CaptureConfig captureConfig) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_DEFAULT_CAPTURE_CONFIG, captureConfig);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setSessionOptionUnpacker(SessionConfig.OptionUnpacker optionUnpacker) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_SESSION_CONFIG_UNPACKER, optionUnpacker);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setCaptureOptionUnpacker(CaptureConfig.OptionUnpacker optionUnpacker) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_CAPTURE_CONFIG_UNPACKER, optionUnpacker);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setSurfaceOccupancyPriority(int priority) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_SURFACE_OCCUPANCY_PRIORITY, Integer.valueOf(priority));
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setCameraSelector(CameraSelector cameraSelector) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_CAMERA_SELECTOR, cameraSelector);
            return this;
        }

        @Override // androidx.camera.core.internal.UseCaseEventConfig.Builder
        public Builder setUseCaseEventCallback(UseCase.EventCallback useCaseEventCallback) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_USE_CASE_EVENT_CALLBACK, useCaseEventCallback);
            return this;
        }

        public Builder setImageReaderProxyProvider(ImageReaderProxyProvider imageReaderProxyProvider) {
            getMutableConfig().insertOption(ImageAnalysisConfig.OPTION_IMAGE_READER_PROXY_PROVIDER, imageReaderProxyProvider);
            return this;
        }
    }
}
