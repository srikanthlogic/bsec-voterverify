package androidx.camera.core;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.Rational;
import android.util.Size;
import androidx.camera.core.ForwardingImageProxy;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageSaver;
import androidx.camera.core.UseCase;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraCaptureFailure;
import androidx.camera.core.impl.CameraCaptureMetaData;
import androidx.camera.core.impl.CameraCaptureResult;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.CaptureBundle;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.CaptureProcessor;
import androidx.camera.core.impl.CaptureStage;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ConfigProvider;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.ImageCaptureConfig;
import androidx.camera.core.impl.ImageInputConfig;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.ImageReaderProxy;
import androidx.camera.core.impl.ImmediateSurface;
import androidx.camera.core.impl.MutableConfig;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.utils.Exif;
import androidx.camera.core.impl.utils.Threads;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.AsyncFunction;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.FutureChain;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.core.internal.IoConfig;
import androidx.camera.core.internal.TargetConfig;
import androidx.camera.core.internal.utils.ImageUtil;
import androidx.camera.core.internal.utils.UseCaseConfigUtil;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public final class ImageCapture extends UseCase {
    public static final int CAPTURE_MODE_MAXIMIZE_QUALITY;
    public static final int CAPTURE_MODE_MINIMIZE_LATENCY;
    private static final long CHECK_3A_TIMEOUT_IN_MS;
    public static final int ERROR_CAMERA_CLOSED;
    public static final int ERROR_CAPTURE_FAILED;
    public static final int ERROR_FILE_IO;
    public static final int ERROR_INVALID_CAMERA;
    public static final int ERROR_UNKNOWN;
    public static final int FLASH_MODE_AUTO;
    public static final int FLASH_MODE_OFF;
    public static final int FLASH_MODE_ON;
    private static final byte JPEG_QUALITY_MAXIMIZE_QUALITY_MODE;
    private static final byte JPEG_QUALITY_MINIMIZE_LATENCY_MODE;
    private static final int MAX_IMAGES;
    private final CaptureBundle mCaptureBundle;
    private final CaptureConfig mCaptureConfig;
    private DeferrableSurface mDeferrableSurface;
    private boolean mEnableCheck3AConverged;
    private ImageCaptureRequestProcessor mImageCaptureRequestProcessor;
    SafeCloseImageReaderProxy mImageReader;
    final Executor mIoExecutor;
    private CameraCaptureCallback mMetadataMatchingCaptureCallback;
    ProcessingImageReader mProcessingImageReader;
    SessionConfig.Builder mSessionConfigBuilder;
    public static final Defaults DEFAULT_CONFIG = new Defaults();
    private static final String TAG;
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final ExecutorService mExecutor = Executors.newFixedThreadPool(1, new ThreadFactory() { // from class: androidx.camera.core.ImageCapture.1
        private final AtomicInteger mId = new AtomicInteger(0);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            return new Thread(r, "CameraX-image_capture_" + this.mId.getAndIncrement());
        }
    });
    private final CaptureCallbackChecker mSessionCallbackChecker = new CaptureCallbackChecker();
    private final ImageReaderProxy.OnImageAvailableListener mClosingListener = $$Lambda$ImageCapture$NPEX6fpK2w8zMZHH6SIcxrU.INSTANCE;
    private ImageCaptureConfig mUserSettingConfig = (ImageCaptureConfig) getUseCaseConfig();
    private final int mCaptureMode = this.mUserSettingConfig.getCaptureMode();
    private int mFlashMode = this.mUserSettingConfig.getFlashMode();
    private final CaptureProcessor mCaptureProcessor = this.mUserSettingConfig.getCaptureProcessor(null);
    private final int mMaxCaptureStages = this.mUserSettingConfig.getMaxCaptureStages(2);

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface CaptureMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface FlashMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ImageCaptureError {
    }

    /* loaded from: classes.dex */
    public interface OnImageSavedCallback {
        void onError(ImageCaptureException imageCaptureException);

        void onImageSaved(OutputFileResults outputFileResults);
    }

    public static /* synthetic */ void lambda$new$0(ImageReaderProxy imageReader) {
        try {
            ImageProxy image = imageReader.acquireLatestImage();
            Log.d(TAG, "Discarding ImageProxy which was inadvertently acquired: " + image);
            if (image != null) {
                image.close();
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "Failed to acquire latest image.", e);
        }
    }

    ImageCapture(ImageCaptureConfig userConfig) {
        super(userConfig);
        Preconditions.checkArgument(this.mMaxCaptureStages >= 1, "Maximum outstanding image count must be at least 1");
        this.mCaptureBundle = this.mUserSettingConfig.getCaptureBundle(CaptureBundles.singleDefaultCaptureBundle());
        this.mIoExecutor = (Executor) Preconditions.checkNotNull(this.mUserSettingConfig.getIoExecutor(CameraXExecutors.ioExecutor()));
        int i = this.mCaptureMode;
        if (i == 0) {
            this.mEnableCheck3AConverged = true;
        } else if (i == 1) {
            this.mEnableCheck3AConverged = false;
        }
        this.mCaptureConfig = CaptureConfig.Builder.createFrom(this.mUserSettingConfig).build();
    }

    SessionConfig.Builder createPipeline(String cameraId, ImageCaptureConfig config, Size resolution) {
        Threads.checkMainThread();
        SessionConfig.Builder sessionConfigBuilder = SessionConfig.Builder.createFrom(config);
        sessionConfigBuilder.addRepeatingCameraCaptureCallback(this.mSessionCallbackChecker);
        if (config.getImageReaderProxyProvider() != null) {
            this.mImageReader = new SafeCloseImageReaderProxy(config.getImageReaderProxyProvider().newInstance(resolution.getWidth(), resolution.getHeight(), getImageFormat(), 2, 0));
            this.mMetadataMatchingCaptureCallback = new CameraCaptureCallback() { // from class: androidx.camera.core.ImageCapture.2
            };
        } else if (this.mCaptureProcessor != null) {
            this.mProcessingImageReader = new ProcessingImageReader(resolution.getWidth(), resolution.getHeight(), getImageFormat(), this.mMaxCaptureStages, this.mExecutor, getCaptureBundle(CaptureBundles.singleDefaultCaptureBundle()), this.mCaptureProcessor);
            this.mMetadataMatchingCaptureCallback = this.mProcessingImageReader.getCameraCaptureCallback();
            this.mImageReader = new SafeCloseImageReaderProxy(this.mProcessingImageReader);
        } else {
            MetadataImageReader metadataImageReader = new MetadataImageReader(resolution.getWidth(), resolution.getHeight(), getImageFormat(), 2);
            this.mMetadataMatchingCaptureCallback = metadataImageReader.getCameraCaptureCallback();
            this.mImageReader = new SafeCloseImageReaderProxy(metadataImageReader);
        }
        this.mImageCaptureRequestProcessor = new ImageCaptureRequestProcessor(2, new ImageCaptureRequestProcessor.ImageCaptor() { // from class: androidx.camera.core.-$$Lambda$ImageCapture$0LGjmnFUtCxVzUuUHAMaY4_BMjw
            @Override // androidx.camera.core.ImageCapture.ImageCaptureRequestProcessor.ImageCaptor
            public final ListenableFuture capture(ImageCapture.ImageCaptureRequest imageCaptureRequest) {
                return ImageCapture.this.lambda$createPipeline$1$ImageCapture(imageCaptureRequest);
            }
        });
        this.mImageReader.setOnImageAvailableListener(this.mClosingListener, CameraXExecutors.mainThreadExecutor());
        SafeCloseImageReaderProxy imageReaderProxy = this.mImageReader;
        DeferrableSurface deferrableSurface = this.mDeferrableSurface;
        if (deferrableSurface != null) {
            deferrableSurface.close();
        }
        this.mDeferrableSurface = new ImmediateSurface(this.mImageReader.getSurface());
        ListenableFuture<Void> terminationFuture = this.mDeferrableSurface.getTerminationFuture();
        Objects.requireNonNull(imageReaderProxy);
        terminationFuture.addListener(new Runnable() { // from class: androidx.camera.core.-$$Lambda$kBKr-WXSgibOrSk3AWcyOrTIBI0
            @Override // java.lang.Runnable
            public final void run() {
                SafeCloseImageReaderProxy.this.safeClose();
            }
        }, CameraXExecutors.mainThreadExecutor());
        sessionConfigBuilder.addNonRepeatingSurface(this.mDeferrableSurface);
        sessionConfigBuilder.addErrorListener(new SessionConfig.ErrorListener(cameraId, config, resolution) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$vo2FN-NBh2zJowDghO6l4OnBl-M
            private final /* synthetic */ String f$1;
            private final /* synthetic */ ImageCaptureConfig f$2;
            private final /* synthetic */ Size f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // androidx.camera.core.impl.SessionConfig.ErrorListener
            public final void onError(SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
                ImageCapture.this.lambda$createPipeline$2$ImageCapture(this.f$1, this.f$2, this.f$3, sessionConfig, sessionError);
            }
        });
        return sessionConfigBuilder;
    }

    public /* synthetic */ void lambda$createPipeline$2$ImageCapture(String cameraId, ImageCaptureConfig config, Size resolution, SessionConfig sessionConfig, SessionConfig.SessionError error) {
        clearPipeline();
        if (isCurrentCamera(cameraId)) {
            this.mSessionConfigBuilder = createPipeline(cameraId, config, resolution);
            updateSessionConfig(this.mSessionConfigBuilder.build());
            notifyReset();
        }
    }

    void clearPipeline() {
        Threads.checkMainThread();
        DeferrableSurface deferrableSurface = this.mDeferrableSurface;
        this.mDeferrableSurface = null;
        this.mImageReader = null;
        this.mProcessingImageReader = null;
        if (deferrableSurface != null) {
            deferrableSurface.close();
        }
    }

    @Override // androidx.camera.core.UseCase
    public UseCaseConfig.Builder<?, ?, ?> getDefaultBuilder(CameraInfo cameraInfo) {
        ImageCaptureConfig defaults = (ImageCaptureConfig) CameraX.getDefaultUseCaseConfig(ImageCaptureConfig.class, cameraInfo);
        if (defaults != null) {
            return Builder.fromConfig(defaults);
        }
        return null;
    }

    @Override // androidx.camera.core.UseCase
    protected void onCameraControlReady() {
        getCameraControl().setFlashMode(this.mFlashMode);
    }

    public int getFlashMode() {
        return this.mFlashMode;
    }

    public void setFlashMode(int flashMode) {
        this.mFlashMode = flashMode;
        if (getCamera() != null) {
            getCameraControl().setFlashMode(flashMode);
        }
    }

    public void setCropAspectRatio(Rational aspectRatio) {
        Builder builder = Builder.fromConfig((ImageCaptureConfig) getUseCaseConfig());
        if (!aspectRatio.equals(this.mUserSettingConfig.getTargetAspectRatioCustom(null))) {
            builder.setTargetAspectRatioCustom(aspectRatio);
            updateUseCaseConfig(builder.getUseCaseConfig());
            this.mUserSettingConfig = (ImageCaptureConfig) getUseCaseConfig();
        }
    }

    public int getTargetRotation() {
        return ((ImageOutputConfig) getUseCaseConfig()).getTargetRotation();
    }

    public void setTargetRotation(int rotation) {
        ImageCaptureConfig oldConfig = (ImageCaptureConfig) getUseCaseConfig();
        Builder builder = Builder.fromConfig(oldConfig);
        int oldRotation = oldConfig.getTargetRotation(-1);
        if (oldRotation == -1 || oldRotation != rotation) {
            UseCaseConfigUtil.updateTargetRotationAndRelatedConfigs(builder, rotation);
            updateUseCaseConfig(builder.getUseCaseConfig());
            this.mUserSettingConfig = (ImageCaptureConfig) getUseCaseConfig();
        }
    }

    public int getCaptureMode() {
        return this.mCaptureMode;
    }

    /* renamed from: takePicture */
    public void lambda$takePicture$3$ImageCapture(Executor executor, OnImageCapturedCallback callback) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            CameraXExecutors.mainThreadExecutor().execute(new Runnable(executor, callback) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$3TBxQKmNJ9VTGCKo1ZKi7E3ESgI
                private final /* synthetic */ Executor f$1;
                private final /* synthetic */ ImageCapture.OnImageCapturedCallback f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ImageCapture.this.lambda$takePicture$3$ImageCapture(this.f$1, this.f$2);
                }
            });
        } else {
            sendImageCaptureRequest(executor, callback);
        }
    }

    /* renamed from: takePicture */
    public void lambda$takePicture$4$ImageCapture(final OutputFileOptions outputFileOptions, final Executor executor, final OnImageSavedCallback imageSavedCallback) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            CameraXExecutors.mainThreadExecutor().execute(new Runnable(outputFileOptions, executor, imageSavedCallback) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$GKCJoIxQ7x6CHqe5VLbTJu0vu5E
                private final /* synthetic */ ImageCapture.OutputFileOptions f$1;
                private final /* synthetic */ Executor f$2;
                private final /* synthetic */ ImageCapture.OnImageSavedCallback f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ImageCapture.this.lambda$takePicture$4$ImageCapture(this.f$1, this.f$2, this.f$3);
                }
            });
            return;
        }
        final ImageSaver.OnImageSavedCallback imageSavedCallbackWrapper = new ImageSaver.OnImageSavedCallback() { // from class: androidx.camera.core.ImageCapture.3
            @Override // androidx.camera.core.ImageSaver.OnImageSavedCallback
            public void onImageSaved(OutputFileResults outputFileResults) {
                imageSavedCallback.onImageSaved(outputFileResults);
            }

            @Override // androidx.camera.core.ImageSaver.OnImageSavedCallback
            public void onError(ImageSaver.SaveError error, String message, Throwable cause) {
                int imageCaptureError = 0;
                if (AnonymousClass9.$SwitchMap$androidx$camera$core$ImageSaver$SaveError[error.ordinal()] == 1) {
                    imageCaptureError = 1;
                }
                imageSavedCallback.onError(new ImageCaptureException(imageCaptureError, message, cause));
            }
        };
        sendImageCaptureRequest(CameraXExecutors.mainThreadExecutor(), new OnImageCapturedCallback() { // from class: androidx.camera.core.ImageCapture.4
            @Override // androidx.camera.core.ImageCapture.OnImageCapturedCallback
            public void onCaptureSuccess(ImageProxy image) {
                ImageCapture.this.mIoExecutor.execute(new ImageSaver(image, outputFileOptions, image.getImageInfo().getRotationDegrees(), executor, imageSavedCallbackWrapper));
            }

            @Override // androidx.camera.core.ImageCapture.OnImageCapturedCallback
            public void onError(ImageCaptureException exception) {
                imageSavedCallback.onError(exception);
            }
        });
    }

    /* renamed from: androidx.camera.core.ImageCapture$9 */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$androidx$camera$core$ImageSaver$SaveError = new int[ImageSaver.SaveError.values().length];

        static {
            try {
                $SwitchMap$androidx$camera$core$ImageSaver$SaveError[ImageSaver.SaveError.FILE_IO_FAILED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    @Override // androidx.camera.core.UseCase
    public void onStateDetached() {
        abortImageCaptureRequests();
    }

    private void abortImageCaptureRequests() {
        this.mImageCaptureRequestProcessor.cancelRequests(new CameraClosedException("Camera is closed."));
    }

    private void sendImageCaptureRequest(Executor callbackExecutor, OnImageCapturedCallback callback) {
        CameraInternal attachedCamera = getCamera();
        if (attachedCamera == null) {
            callbackExecutor.execute(new Runnable(callback) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$pBXp0PTu2y8W2MYKbHOK5lMiVKg
                private final /* synthetic */ ImageCapture.OnImageCapturedCallback f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ImageCapture.this.lambda$sendImageCaptureRequest$5$ImageCapture(this.f$1);
                }
            });
            return;
        }
        this.mImageCaptureRequestProcessor.sendRequest(new ImageCaptureRequest(attachedCamera.getCameraInfoInternal().getSensorRotationDegrees(this.mUserSettingConfig.getTargetRotation(0)), getJpegQuality(), this.mUserSettingConfig.getTargetAspectRatioCustom(null), getViewPortCropRect(), callbackExecutor, callback));
    }

    public /* synthetic */ void lambda$sendImageCaptureRequest$5$ImageCapture(OnImageCapturedCallback callback) {
        callback.onError(new ImageCaptureException(4, "Not bound to a valid Camera [" + this + "]", null));
    }

    private int getJpegQuality() {
        int i = this.mCaptureMode;
        if (i == 0) {
            return 100;
        }
        if (i == 1) {
            return 95;
        }
        throw new IllegalStateException("CaptureMode " + this.mCaptureMode + " is invalid");
    }

    /* renamed from: takePictureInternal */
    public ListenableFuture<ImageProxy> lambda$createPipeline$1$ImageCapture(ImageCaptureRequest imageCaptureRequest) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(imageCaptureRequest) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$Yn9me8_Get6e1Qr4IQRhjwwSXbs
            private final /* synthetic */ ImageCapture.ImageCaptureRequest f$1;

            {
                this.f$1 = r2;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ImageCapture.this.lambda$takePictureInternal$9$ImageCapture(this.f$1, completer);
            }
        });
    }

    public /* synthetic */ Object lambda$takePictureInternal$9$ImageCapture(ImageCaptureRequest imageCaptureRequest, final CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mImageReader.setOnImageAvailableListener(new ImageReaderProxy.OnImageAvailableListener() { // from class: androidx.camera.core.-$$Lambda$ImageCapture$gAHopxs-tnQYHzPHmR3kdJ2NWgQ
            @Override // androidx.camera.core.impl.ImageReaderProxy.OnImageAvailableListener
            public final void onImageAvailable(ImageReaderProxy imageReaderProxy) {
                ImageCapture.lambda$takePictureInternal$6(CallbackToFutureAdapter.Completer.this, imageReaderProxy);
            }
        }, CameraXExecutors.mainThreadExecutor());
        final TakePictureState state = new TakePictureState();
        ListenableFuture<Void> future = FutureChain.from(preTakePicture(state)).transformAsync(new AsyncFunction(imageCaptureRequest) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$Q2natJasV8R7bZpCzT-XPmy4R_4
            private final /* synthetic */ ImageCapture.ImageCaptureRequest f$1;

            {
                this.f$1 = r2;
            }

            @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
            public final ListenableFuture apply(Object obj) {
                return ImageCapture.this.lambda$takePictureInternal$7$ImageCapture(this.f$1, (Void) obj);
            }
        }, this.mExecutor);
        Futures.addCallback(future, new FutureCallback<Void>() { // from class: androidx.camera.core.ImageCapture.5
            public void onSuccess(Void result) {
                ImageCapture.this.postTakePicture(state);
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable throwable) {
                ImageCapture.this.postTakePicture(state);
                completer.setException(throwable);
            }
        }, this.mExecutor);
        completer.addCancellationListener(new Runnable() { // from class: androidx.camera.core.-$$Lambda$ImageCapture$NOvpRrsYSzBN1_Z8IEVaCXMxXwc
            @Override // java.lang.Runnable
            public final void run() {
                ListenableFuture.this.cancel(true);
            }
        }, CameraXExecutors.directExecutor());
        return "takePictureInternal";
    }

    public static /* synthetic */ void lambda$takePictureInternal$6(CallbackToFutureAdapter.Completer completer, ImageReaderProxy imageReader) {
        try {
            ImageProxy image = imageReader.acquireLatestImage();
            if (image == null) {
                completer.setException(new IllegalStateException("Unable to acquire image"));
            } else if (!completer.set(image)) {
                image.close();
            }
        } catch (IllegalStateException e) {
            while (true) {
                completer.setException(e);
                return;
            }
        }
    }

    public /* synthetic */ ListenableFuture lambda$takePictureInternal$7$ImageCapture(ImageCaptureRequest imageCaptureRequest, Void v) throws Exception {
        return issueTakePicture(imageCaptureRequest);
    }

    /* loaded from: classes.dex */
    public static class ImageCaptureRequestProcessor implements ForwardingImageProxy.OnImageCloseListener {
        private final ImageCaptor mImageCaptor;
        private final int mMaxImages;
        private final Deque<ImageCaptureRequest> mPendingRequests = new ArrayDeque();
        ImageCaptureRequest mCurrentRequest = null;
        ListenableFuture<ImageProxy> mCurrentRequestFuture = null;
        int mOutstandingImages = 0;
        final Object mLock = new Object();

        /* loaded from: classes.dex */
        public interface ImageCaptor {
            ListenableFuture<ImageProxy> capture(ImageCaptureRequest imageCaptureRequest);
        }

        ImageCaptureRequestProcessor(int maxImages, ImageCaptor imageCaptor) {
            this.mMaxImages = maxImages;
            this.mImageCaptor = imageCaptor;
        }

        public void sendRequest(ImageCaptureRequest imageCaptureRequest) {
            synchronized (this.mLock) {
                this.mPendingRequests.offer(imageCaptureRequest);
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(this.mCurrentRequest != null ? 1 : 0);
                objArr[1] = Integer.valueOf(this.mPendingRequests.size());
                Log.d(ImageCapture.TAG, String.format("Send image capture request [current, pending] = [%d, %d]", objArr));
                processNextRequest();
            }
        }

        public void cancelRequests(Throwable throwable) {
            ImageCaptureRequest currentRequest;
            ListenableFuture<ImageProxy> currentRequestFuture;
            List<ImageCaptureRequest> pendingRequests;
            synchronized (this.mLock) {
                currentRequest = this.mCurrentRequest;
                this.mCurrentRequest = null;
                currentRequestFuture = this.mCurrentRequestFuture;
                this.mCurrentRequestFuture = null;
                pendingRequests = new ArrayList<>(this.mPendingRequests);
                this.mPendingRequests.clear();
            }
            if (!(currentRequest == null || currentRequestFuture == null)) {
                currentRequest.notifyCallbackError(ImageCapture.getError(throwable), throwable.getMessage(), throwable);
                currentRequestFuture.cancel(true);
            }
            for (ImageCaptureRequest request : pendingRequests) {
                request.notifyCallbackError(ImageCapture.getError(throwable), throwable.getMessage(), throwable);
            }
        }

        @Override // androidx.camera.core.ForwardingImageProxy.OnImageCloseListener
        public void onImageClose(ImageProxy image) {
            synchronized (this.mLock) {
                this.mOutstandingImages--;
                processNextRequest();
            }
        }

        void processNextRequest() {
            synchronized (this.mLock) {
                if (this.mCurrentRequest == null) {
                    if (this.mOutstandingImages >= this.mMaxImages) {
                        Log.w(ImageCapture.TAG, "Too many acquire images. Close image to be able to process next.");
                        return;
                    }
                    final ImageCaptureRequest imageCaptureRequest = this.mPendingRequests.poll();
                    if (imageCaptureRequest != null) {
                        this.mCurrentRequest = imageCaptureRequest;
                        this.mCurrentRequestFuture = this.mImageCaptor.capture(imageCaptureRequest);
                        Futures.addCallback(this.mCurrentRequestFuture, new FutureCallback<ImageProxy>() { // from class: androidx.camera.core.ImageCapture.ImageCaptureRequestProcessor.1
                            public void onSuccess(ImageProxy image) {
                                synchronized (ImageCaptureRequestProcessor.this.mLock) {
                                    Preconditions.checkNotNull(image);
                                    SingleCloseImageProxy wrappedImage = new SingleCloseImageProxy(image);
                                    wrappedImage.addOnImageCloseListener(ImageCaptureRequestProcessor.this);
                                    ImageCaptureRequestProcessor.this.mOutstandingImages++;
                                    imageCaptureRequest.dispatchImage(wrappedImage);
                                    ImageCaptureRequestProcessor.this.mCurrentRequest = null;
                                    ImageCaptureRequestProcessor.this.mCurrentRequestFuture = null;
                                    ImageCaptureRequestProcessor.this.processNextRequest();
                                }
                            }

                            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                            public void onFailure(Throwable t) {
                                synchronized (ImageCaptureRequestProcessor.this.mLock) {
                                    if (!(t instanceof CancellationException)) {
                                        imageCaptureRequest.notifyCallbackError(ImageCapture.getError(t), t != null ? t.getMessage() : "Unknown error", t);
                                    }
                                    ImageCaptureRequestProcessor.this.mCurrentRequest = null;
                                    ImageCaptureRequestProcessor.this.mCurrentRequestFuture = null;
                                    ImageCaptureRequestProcessor.this.processNextRequest();
                                }
                            }
                        }, CameraXExecutors.directExecutor());
                    }
                }
            }
        }
    }

    public String toString() {
        return "ImageCapture:" + getName();
    }

    static int getError(Throwable throwable) {
        if (throwable instanceof CameraClosedException) {
            return 3;
        }
        if (throwable instanceof CaptureFailedException) {
            return 2;
        }
        return 0;
    }

    @Override // androidx.camera.core.UseCase
    public void clear() {
        abortImageCaptureRequests();
        clearPipeline();
        this.mExecutor.shutdown();
    }

    @Override // androidx.camera.core.UseCase
    protected Size onSuggestedResolutionUpdated(Size suggestedResolution) {
        this.mSessionConfigBuilder = createPipeline(getCameraId(), this.mUserSettingConfig, suggestedResolution);
        updateSessionConfig(this.mSessionConfigBuilder.build());
        notifyActive();
        return suggestedResolution;
    }

    private ListenableFuture<Void> preTakePicture(TakePictureState state) {
        return FutureChain.from(getPreCaptureStateIfNeeded()).transformAsync(new AsyncFunction(state) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$cx3bPsi9G7LL_QgL8vl6Pua6Yyo
            private final /* synthetic */ ImageCapture.TakePictureState f$1;

            {
                this.f$1 = r2;
            }

            @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
            public final ListenableFuture apply(Object obj) {
                return ImageCapture.this.lambda$preTakePicture$10$ImageCapture(this.f$1, (CameraCaptureResult) obj);
            }
        }, this.mExecutor).transformAsync(new AsyncFunction(state) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$qxiP-lEMHbynOHrJ3C0Tk0bLYJQ
            private final /* synthetic */ ImageCapture.TakePictureState f$1;

            {
                this.f$1 = r2;
            }

            @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
            public final ListenableFuture apply(Object obj) {
                return ImageCapture.this.lambda$preTakePicture$11$ImageCapture(this.f$1, (CameraCaptureResult) obj);
            }
        }, this.mExecutor).transform($$Lambda$ImageCapture$qrLn_XI1j1x4jhqRvP925EZ_Pc0.INSTANCE, this.mExecutor);
    }

    public /* synthetic */ ListenableFuture lambda$preTakePicture$10$ImageCapture(TakePictureState state, CameraCaptureResult captureResult) throws Exception {
        state.mPreCaptureState = captureResult;
        triggerAfIfNeeded(state);
        if (isAePrecaptureRequired(state)) {
            return triggerAePrecapture(state);
        }
        return Futures.immediateFuture(null);
    }

    public /* synthetic */ ListenableFuture lambda$preTakePicture$11$ImageCapture(TakePictureState state, CameraCaptureResult v) throws Exception {
        return check3AConverged(state);
    }

    public static /* synthetic */ Void lambda$preTakePicture$12(Boolean is3AConverged) {
        return null;
    }

    void postTakePicture(TakePictureState state) {
        cancelAfAeTrigger(state);
    }

    private ListenableFuture<CameraCaptureResult> getPreCaptureStateIfNeeded() {
        return (this.mEnableCheck3AConverged || getFlashMode() == 0) ? this.mSessionCallbackChecker.checkCaptureResult(new CaptureCallbackChecker.CaptureResultChecker<CameraCaptureResult>() { // from class: androidx.camera.core.ImageCapture.6
            @Override // androidx.camera.core.ImageCapture.CaptureCallbackChecker.CaptureResultChecker
            public CameraCaptureResult check(CameraCaptureResult captureResult) {
                if (ImageCapture.DEBUG) {
                    Log.d(ImageCapture.TAG, "preCaptureState, AE=" + captureResult.getAeState() + " AF =" + captureResult.getAfState() + " AWB=" + captureResult.getAwbState());
                }
                return captureResult;
            }
        }) : Futures.immediateFuture(null);
    }

    boolean isAePrecaptureRequired(TakePictureState state) {
        int flashMode = getFlashMode();
        if (flashMode == 0) {
            return state.mPreCaptureState.getAeState() == CameraCaptureMetaData.AeState.FLASH_REQUIRED;
        }
        if (flashMode == 1) {
            return true;
        }
        if (flashMode == 2) {
            return false;
        }
        throw new AssertionError(getFlashMode());
    }

    ListenableFuture<Boolean> check3AConverged(TakePictureState state) {
        if (this.mEnableCheck3AConverged || state.mIsAePrecaptureTriggered) {
            return this.mSessionCallbackChecker.checkCaptureResult(new CaptureCallbackChecker.CaptureResultChecker<Boolean>() { // from class: androidx.camera.core.ImageCapture.7
                @Override // androidx.camera.core.ImageCapture.CaptureCallbackChecker.CaptureResultChecker
                public Boolean check(CameraCaptureResult captureResult) {
                    if (ImageCapture.DEBUG) {
                        Log.d(ImageCapture.TAG, "checkCaptureResult, AE=" + captureResult.getAeState() + " AF =" + captureResult.getAfState() + " AWB=" + captureResult.getAwbState());
                    }
                    if (ImageCapture.this.is3AConverged(captureResult)) {
                        return true;
                    }
                    return null;
                }
            }, CHECK_3A_TIMEOUT_IN_MS, false);
        }
        return Futures.immediateFuture(false);
    }

    boolean is3AConverged(CameraCaptureResult captureResult) {
        if (captureResult == null) {
            return false;
        }
        boolean isAfReady = captureResult.getAfMode() == CameraCaptureMetaData.AfMode.ON_CONTINUOUS_AUTO || captureResult.getAfMode() == CameraCaptureMetaData.AfMode.OFF || captureResult.getAfMode() == CameraCaptureMetaData.AfMode.UNKNOWN || captureResult.getAfState() == CameraCaptureMetaData.AfState.FOCUSED || captureResult.getAfState() == CameraCaptureMetaData.AfState.LOCKED_FOCUSED || captureResult.getAfState() == CameraCaptureMetaData.AfState.LOCKED_NOT_FOCUSED;
        boolean isAeReady = captureResult.getAeState() == CameraCaptureMetaData.AeState.CONVERGED || captureResult.getAeState() == CameraCaptureMetaData.AeState.FLASH_REQUIRED || captureResult.getAeState() == CameraCaptureMetaData.AeState.UNKNOWN;
        boolean isAwbReady = captureResult.getAwbState() == CameraCaptureMetaData.AwbState.CONVERGED || captureResult.getAwbState() == CameraCaptureMetaData.AwbState.UNKNOWN;
        if (!isAfReady || !isAeReady || !isAwbReady) {
            return false;
        }
        return true;
    }

    void triggerAfIfNeeded(TakePictureState state) {
        if (this.mEnableCheck3AConverged && state.mPreCaptureState.getAfMode() == CameraCaptureMetaData.AfMode.ON_MANUAL_AUTO && state.mPreCaptureState.getAfState() == CameraCaptureMetaData.AfState.INACTIVE) {
            triggerAf(state);
        }
    }

    private void triggerAf(TakePictureState state) {
        if (DEBUG) {
            Log.d(TAG, "triggerAf");
        }
        state.mIsAfTriggered = true;
        getCameraControl().triggerAf().addListener($$Lambda$ImageCapture$imelcLeIMea3rxGpuBTI2wHdPNQ.INSTANCE, CameraXExecutors.directExecutor());
    }

    public static /* synthetic */ void lambda$triggerAf$13() {
    }

    ListenableFuture<CameraCaptureResult> triggerAePrecapture(TakePictureState state) {
        if (DEBUG) {
            Log.d(TAG, "triggerAePrecapture");
        }
        state.mIsAePrecaptureTriggered = true;
        return getCameraControl().triggerAePrecapture();
    }

    void cancelAfAeTrigger(TakePictureState state) {
        if (state.mIsAfTriggered || state.mIsAePrecaptureTriggered) {
            getCameraControl().cancelAfAeTrigger(state.mIsAfTriggered, state.mIsAePrecaptureTriggered);
            state.mIsAfTriggered = false;
            state.mIsAePrecaptureTriggered = false;
        }
    }

    ListenableFuture<Void> issueTakePicture(ImageCaptureRequest imageCaptureRequest) {
        CaptureBundle captureBundle;
        if (DEBUG) {
            Log.d(TAG, "issueTakePicture");
        }
        List<ListenableFuture<Void>> futureList = new ArrayList<>();
        List<CaptureConfig> captureConfigs = new ArrayList<>();
        if (this.mProcessingImageReader != null) {
            captureBundle = getCaptureBundle(null);
            if (captureBundle == null) {
                return Futures.immediateFailedFuture(new IllegalArgumentException("ImageCapture cannot set empty CaptureBundle."));
            }
            if (captureBundle.getCaptureStages().size() > this.mMaxCaptureStages) {
                return Futures.immediateFailedFuture(new IllegalArgumentException("ImageCapture has CaptureStages > Max CaptureStage size"));
            }
            this.mProcessingImageReader.setCaptureBundle(captureBundle);
        } else {
            captureBundle = getCaptureBundle(CaptureBundles.singleDefaultCaptureBundle());
            if (captureBundle.getCaptureStages().size() > 1) {
                return Futures.immediateFailedFuture(new IllegalArgumentException("ImageCapture have no CaptureProcess set with CaptureBundle size > 1."));
            }
        }
        for (CaptureStage captureStage : captureBundle.getCaptureStages()) {
            CaptureConfig.Builder builder = new CaptureConfig.Builder();
            builder.setTemplateType(this.mCaptureConfig.getTemplateType());
            builder.addImplementationOptions(this.mCaptureConfig.getImplementationOptions());
            builder.addAllCameraCaptureCallbacks(this.mSessionConfigBuilder.getSingleCameraCaptureCallbacks());
            builder.addSurface(this.mDeferrableSurface);
            builder.addImplementationOption(CaptureConfig.OPTION_ROTATION, Integer.valueOf(imageCaptureRequest.mRotationDegrees));
            builder.addImplementationOption(CaptureConfig.OPTION_JPEG_QUALITY, Integer.valueOf(imageCaptureRequest.mJpegQuality));
            builder.addImplementationOptions(captureStage.getCaptureConfig().getImplementationOptions());
            builder.setTag(captureStage.getCaptureConfig().getTag());
            builder.addCameraCaptureCallback(this.mMetadataMatchingCaptureCallback);
            futureList.add(CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(builder, captureConfigs, captureStage) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$GXGyp2twiJatfd77vWgGABw5Y6Y
                private final /* synthetic */ CaptureConfig.Builder f$1;
                private final /* synthetic */ List f$2;
                private final /* synthetic */ CaptureStage f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                    return ImageCapture.this.lambda$issueTakePicture$14$ImageCapture(this.f$1, this.f$2, this.f$3, completer);
                }
            }));
        }
        getCameraControl().submitCaptureRequests(captureConfigs);
        return Futures.transform(Futures.allAsList(futureList), $$Lambda$ImageCapture$b_FiC4cqL3qwcK1Cdd08YNsRUTk.INSTANCE, CameraXExecutors.directExecutor());
    }

    public /* synthetic */ Object lambda$issueTakePicture$14$ImageCapture(CaptureConfig.Builder builder, List captureConfigs, CaptureStage captureStage, final CallbackToFutureAdapter.Completer completer) throws Exception {
        builder.addCameraCaptureCallback(new CameraCaptureCallback() { // from class: androidx.camera.core.ImageCapture.8
            @Override // androidx.camera.core.impl.CameraCaptureCallback
            public void onCaptureCompleted(CameraCaptureResult result) {
                completer.set(null);
            }

            @Override // androidx.camera.core.impl.CameraCaptureCallback
            public void onCaptureFailed(CameraCaptureFailure failure) {
                completer.setException(new CaptureFailedException("Capture request failed with reason " + failure.getReason()));
            }

            @Override // androidx.camera.core.impl.CameraCaptureCallback
            public void onCaptureCancelled() {
                completer.setException(new CameraClosedException("Capture request is cancelled because camera is closed"));
            }
        });
        captureConfigs.add(builder.build());
        return "issueTakePicture[stage=" + captureStage.getId() + "]";
    }

    public static /* synthetic */ Void lambda$issueTakePicture$15(List input) {
        return null;
    }

    /* loaded from: classes.dex */
    public static final class CaptureFailedException extends RuntimeException {
        CaptureFailedException(String s, Throwable e) {
            super(s, e);
        }

        CaptureFailedException(String s) {
            super(s);
        }
    }

    private CaptureBundle getCaptureBundle(CaptureBundle defaultCaptureBundle) {
        List<CaptureStage> captureStages = this.mCaptureBundle.getCaptureStages();
        if (captureStages == null || captureStages.isEmpty()) {
            return defaultCaptureBundle;
        }
        return CaptureBundles.createCaptureBundle(captureStages);
    }

    /* loaded from: classes.dex */
    public static abstract class OnImageCapturedCallback {
        public void onCaptureSuccess(ImageProxy image) {
            image.close();
        }

        public void onError(ImageCaptureException exception) {
        }
    }

    /* loaded from: classes.dex */
    public static final class Defaults implements ConfigProvider<ImageCaptureConfig> {
        private static final int DEFAULT_CAPTURE_MODE;
        private static final ImageCaptureConfig DEFAULT_CONFIG = new Builder().setCaptureMode(1).setFlashMode(2).setSurfaceOccupancyPriority(4).getUseCaseConfig();
        private static final int DEFAULT_FLASH_MODE;
        private static final int DEFAULT_SURFACE_OCCUPANCY_PRIORITY;

        @Override // androidx.camera.core.impl.ConfigProvider
        public ImageCaptureConfig getConfig(CameraInfo cameraInfo) {
            return DEFAULT_CONFIG;
        }
    }

    /* loaded from: classes.dex */
    public static final class OutputFileOptions {
        private static final Metadata EMPTY_METADATA = new Metadata();
        private final ContentResolver mContentResolver;
        private final ContentValues mContentValues;
        private final File mFile;
        private final Metadata mMetadata;
        private final OutputStream mOutputStream;
        private final Uri mSaveCollection;

        OutputFileOptions(File file, ContentResolver contentResolver, Uri saveCollection, ContentValues contentValues, OutputStream outputStream, Metadata metadata) {
            this.mFile = file;
            this.mContentResolver = contentResolver;
            this.mSaveCollection = saveCollection;
            this.mContentValues = contentValues;
            this.mOutputStream = outputStream;
            this.mMetadata = metadata == null ? EMPTY_METADATA : metadata;
        }

        public File getFile() {
            return this.mFile;
        }

        public ContentResolver getContentResolver() {
            return this.mContentResolver;
        }

        public Uri getSaveCollection() {
            return this.mSaveCollection;
        }

        public ContentValues getContentValues() {
            return this.mContentValues;
        }

        public OutputStream getOutputStream() {
            return this.mOutputStream;
        }

        public Metadata getMetadata() {
            return this.mMetadata;
        }

        /* loaded from: classes.dex */
        public static final class Builder {
            private ContentResolver mContentResolver;
            private ContentValues mContentValues;
            private File mFile;
            private Metadata mMetadata;
            private OutputStream mOutputStream;
            private Uri mSaveCollection;

            public Builder(File file) {
                this.mFile = file;
            }

            public Builder(ContentResolver contentResolver, Uri saveCollection, ContentValues contentValues) {
                this.mContentResolver = contentResolver;
                this.mSaveCollection = saveCollection;
                this.mContentValues = contentValues;
            }

            public Builder(OutputStream outputStream) {
                this.mOutputStream = outputStream;
            }

            public Builder setMetadata(Metadata metadata) {
                this.mMetadata = metadata;
                return this;
            }

            public OutputFileOptions build() {
                return new OutputFileOptions(this.mFile, this.mContentResolver, this.mSaveCollection, this.mContentValues, this.mOutputStream, this.mMetadata);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class OutputFileResults {
        private Uri mSavedUri;

        public OutputFileResults(Uri savedUri) {
            this.mSavedUri = savedUri;
        }

        public Uri getSavedUri() {
            return this.mSavedUri;
        }
    }

    /* loaded from: classes.dex */
    public static final class Metadata {
        private boolean mIsReversedHorizontal;
        private boolean mIsReversedVertical;
        private Location mLocation;

        public boolean isReversedHorizontal() {
            return this.mIsReversedHorizontal;
        }

        public void setReversedHorizontal(boolean isReversedHorizontal) {
            this.mIsReversedHorizontal = isReversedHorizontal;
        }

        public boolean isReversedVertical() {
            return this.mIsReversedVertical;
        }

        public void setReversedVertical(boolean isReversedVertical) {
            this.mIsReversedVertical = isReversedVertical;
        }

        public Location getLocation() {
            return this.mLocation;
        }

        public void setLocation(Location location) {
            this.mLocation = location;
        }
    }

    /* loaded from: classes.dex */
    public static final class TakePictureState {
        CameraCaptureResult mPreCaptureState = CameraCaptureResult.EmptyCameraCaptureResult.create();
        boolean mIsAfTriggered = false;
        boolean mIsAePrecaptureTriggered = false;

        TakePictureState() {
        }
    }

    /* loaded from: classes.dex */
    public static final class CaptureCallbackChecker extends CameraCaptureCallback {
        private static final long NO_TIMEOUT;
        private final Set<CaptureResultListener> mCaptureResultListeners = new HashSet();

        /* loaded from: classes.dex */
        public interface CaptureResultChecker<T> {
            T check(CameraCaptureResult cameraCaptureResult);
        }

        /* loaded from: classes.dex */
        public interface CaptureResultListener {
            boolean onCaptureResult(CameraCaptureResult cameraCaptureResult);
        }

        CaptureCallbackChecker() {
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureCompleted(CameraCaptureResult cameraCaptureResult) {
            deliverCaptureResultToListeners(cameraCaptureResult);
        }

        <T> ListenableFuture<T> checkCaptureResult(CaptureResultChecker<T> checker) {
            return checkCaptureResult(checker, 0, null);
        }

        <T> ListenableFuture<T> checkCaptureResult(CaptureResultChecker<T> checker, long timeoutInMs, T defValue) {
            long startTimeInMs = 0;
            if (timeoutInMs >= 0) {
                if (timeoutInMs != 0) {
                    startTimeInMs = SystemClock.elapsedRealtime();
                }
                return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(checker, startTimeInMs, timeoutInMs, defValue) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$CaptureCallbackChecker$RVxDy_zAdeqk9wi1C8KZybyVmF8
                    private final /* synthetic */ ImageCapture.CaptureCallbackChecker.CaptureResultChecker f$1;
                    private final /* synthetic */ long f$2;
                    private final /* synthetic */ long f$3;
                    private final /* synthetic */ Object f$4;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                        this.f$3 = r5;
                        this.f$4 = r7;
                    }

                    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                        return ImageCapture.CaptureCallbackChecker.this.lambda$checkCaptureResult$0$ImageCapture$CaptureCallbackChecker(this.f$1, this.f$2, this.f$3, this.f$4, completer);
                    }
                });
            }
            throw new IllegalArgumentException("Invalid timeout value: " + timeoutInMs);
        }

        public /* synthetic */ Object lambda$checkCaptureResult$0$ImageCapture$CaptureCallbackChecker(final CaptureResultChecker checker, final long startTimeInMs, final long timeoutInMs, final Object defValue, final CallbackToFutureAdapter.Completer completer) throws Exception {
            addListener(new CaptureResultListener() { // from class: androidx.camera.core.ImageCapture.CaptureCallbackChecker.1
                @Override // androidx.camera.core.ImageCapture.CaptureCallbackChecker.CaptureResultListener
                public boolean onCaptureResult(CameraCaptureResult captureResult) {
                    Object result = checker.check(captureResult);
                    if (result != null) {
                        completer.set(result);
                        return true;
                    } else if (startTimeInMs <= 0 || SystemClock.elapsedRealtime() - startTimeInMs <= timeoutInMs) {
                        return false;
                    } else {
                        completer.set(defValue);
                        return true;
                    }
                }
            });
            return "checkCaptureResult";
        }

        private void deliverCaptureResultToListeners(CameraCaptureResult captureResult) {
            synchronized (this.mCaptureResultListeners) {
                Set<CaptureResultListener> removeSet = null;
                Iterator it = new HashSet(this.mCaptureResultListeners).iterator();
                while (it.hasNext()) {
                    CaptureResultListener listener = (CaptureResultListener) it.next();
                    if (listener.onCaptureResult(captureResult)) {
                        if (removeSet == null) {
                            removeSet = new HashSet<>();
                        }
                        removeSet.add(listener);
                    }
                }
                if (removeSet != null) {
                    this.mCaptureResultListeners.removeAll(removeSet);
                }
            }
        }

        void addListener(CaptureResultListener listener) {
            synchronized (this.mCaptureResultListeners) {
                this.mCaptureResultListeners.add(listener);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ImageCaptureRequest {
        private final OnImageCapturedCallback mCallback;
        AtomicBoolean mDispatched = new AtomicBoolean(false);
        final int mJpegQuality;
        private final Executor mListenerExecutor;
        final int mRotationDegrees;
        private final Rational mTargetRatio;
        private final Rect mViewPortCropRect;

        ImageCaptureRequest(int rotationDegrees, int jpegQuality, Rational targetRatio, Rect viewPortCropRect, Executor executor, OnImageCapturedCallback callback) {
            boolean z = false;
            this.mRotationDegrees = rotationDegrees;
            this.mJpegQuality = jpegQuality;
            if (targetRatio != null) {
                Preconditions.checkArgument(!targetRatio.isZero(), "Target ratio cannot be zero");
                Preconditions.checkArgument(targetRatio.floatValue() > 0.0f ? true : z, "Target ratio must be positive");
            }
            this.mTargetRatio = targetRatio;
            this.mViewPortCropRect = viewPortCropRect;
            this.mListenerExecutor = executor;
            this.mCallback = callback;
        }

        void dispatchImage(ImageProxy image) {
            int dispatchRotation;
            if (!this.mDispatched.compareAndSet(false, true)) {
                image.close();
                return;
            }
            Size dispatchResolution = null;
            if (image.getFormat() == 256) {
                try {
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    buffer.rewind();
                    byte[] data = new byte[buffer.capacity()];
                    buffer.get(data);
                    Exif exif = Exif.createFromInputStream(new ByteArrayInputStream(data));
                    buffer.rewind();
                    dispatchResolution = new Size(exif.getWidth(), exif.getHeight());
                    dispatchRotation = exif.getRotation();
                } catch (IOException e) {
                    notifyCallbackError(1, "Unable to parse JPEG exif", e);
                    image.close();
                    return;
                }
            } else {
                dispatchRotation = this.mRotationDegrees;
            }
            ImageProxy dispatchedImageProxy = new SettableImageProxy(image, dispatchResolution, ImmutableImageInfo.create(image.getImageInfo().getTag(), image.getImageInfo().getTimestamp(), dispatchRotation));
            Rect rect = this.mViewPortCropRect;
            if (rect != null) {
                dispatchedImageProxy.setCropRect(rect);
            } else {
                Rational rational = this.mTargetRatio;
                if (rational != null) {
                    Rational dispatchRatio = this.mTargetRatio;
                    if (dispatchRotation % RotationOptions.ROTATE_180 != 0) {
                        dispatchRatio = new Rational(rational.getDenominator(), this.mTargetRatio.getNumerator());
                    }
                    Size sourceSize = new Size(dispatchedImageProxy.getWidth(), dispatchedImageProxy.getHeight());
                    if (ImageUtil.isAspectRatioValid(sourceSize, dispatchRatio)) {
                        dispatchedImageProxy.setCropRect(ImageUtil.computeCropRectFromAspectRatio(sourceSize, dispatchRatio));
                    }
                }
            }
            try {
                this.mListenerExecutor.execute(new Runnable(dispatchedImageProxy) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$ImageCaptureRequest$E13UUGhRx8PID5WbaCuwcGP87BA
                    private final /* synthetic */ ImageProxy f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageCapture.ImageCaptureRequest.this.lambda$dispatchImage$0$ImageCapture$ImageCaptureRequest(this.f$1);
                    }
                });
            } catch (RejectedExecutionException e2) {
                Log.e(ImageCapture.TAG, "Unable to post to the supplied executor.");
                image.close();
            }
        }

        public /* synthetic */ void lambda$dispatchImage$0$ImageCapture$ImageCaptureRequest(ImageProxy dispatchedImageProxy) {
            this.mCallback.onCaptureSuccess(dispatchedImageProxy);
        }

        void notifyCallbackError(int imageCaptureError, String message, Throwable cause) {
            if (this.mDispatched.compareAndSet(false, true)) {
                try {
                    this.mListenerExecutor.execute(new Runnable(imageCaptureError, message, cause) { // from class: androidx.camera.core.-$$Lambda$ImageCapture$ImageCaptureRequest$1G7WSvt8TANxhZtOyewefm68pg4
                        private final /* synthetic */ int f$1;
                        private final /* synthetic */ String f$2;
                        private final /* synthetic */ Throwable f$3;

                        {
                            this.f$1 = r2;
                            this.f$2 = r3;
                            this.f$3 = r4;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            ImageCapture.ImageCaptureRequest.this.lambda$notifyCallbackError$1$ImageCapture$ImageCaptureRequest(this.f$1, this.f$2, this.f$3);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    Log.e(ImageCapture.TAG, "Unable to post to the supplied executor.");
                }
            }
        }

        public /* synthetic */ void lambda$notifyCallbackError$1$ImageCapture$ImageCaptureRequest(int imageCaptureError, String message, Throwable cause) {
            this.mCallback.onError(new ImageCaptureException(imageCaptureError, message, cause));
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder implements UseCaseConfig.Builder<ImageCapture, ImageCaptureConfig, Builder>, ImageOutputConfig.Builder<Builder>, IoConfig.Builder<Builder> {
        private final MutableOptionsBundle mMutableConfig;

        public Builder() {
            this(MutableOptionsBundle.create());
        }

        private Builder(MutableOptionsBundle mutableConfig) {
            this.mMutableConfig = mutableConfig;
            Class<?> oldConfigClass = (Class) mutableConfig.retrieveOption(TargetConfig.OPTION_TARGET_CLASS, null);
            if (oldConfigClass == null || oldConfigClass.equals(ImageCapture.class)) {
                setTargetClass(ImageCapture.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + oldConfigClass);
        }

        public static Builder fromConfig(ImageCaptureConfig configuration) {
            return new Builder(MutableOptionsBundle.from((Config) configuration));
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public MutableConfig getMutableConfig() {
            return this.mMutableConfig;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public ImageCaptureConfig getUseCaseConfig() {
            return new ImageCaptureConfig(OptionsBundle.from(this.mMutableConfig));
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public ImageCapture build() {
            if (getMutableConfig().retrieveOption(ImageCaptureConfig.OPTION_TARGET_ASPECT_RATIO, null) == null || getMutableConfig().retrieveOption(ImageCaptureConfig.OPTION_TARGET_RESOLUTION, null) == null) {
                Integer bufferFormat = (Integer) getMutableConfig().retrieveOption(ImageCaptureConfig.OPTION_BUFFER_FORMAT, null);
                if (bufferFormat != null) {
                    Preconditions.checkArgument(getMutableConfig().retrieveOption(ImageCaptureConfig.OPTION_CAPTURE_PROCESSOR, null) == null, "Cannot set buffer format with CaptureProcessor defined.");
                    getMutableConfig().insertOption(ImageInputConfig.OPTION_INPUT_FORMAT, bufferFormat);
                } else if (getMutableConfig().retrieveOption(ImageCaptureConfig.OPTION_CAPTURE_PROCESSOR, null) != null) {
                    getMutableConfig().insertOption(ImageInputConfig.OPTION_INPUT_FORMAT, 35);
                } else {
                    getMutableConfig().insertOption(ImageInputConfig.OPTION_INPUT_FORMAT, 256);
                }
                return new ImageCapture(getUseCaseConfig());
            }
            throw new IllegalArgumentException("Cannot use both setTargetResolution and setTargetAspectRatio on the same config.");
        }

        public Builder setCaptureMode(int captureMode) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_IMAGE_CAPTURE_MODE, Integer.valueOf(captureMode));
            return this;
        }

        public Builder setFlashMode(int flashMode) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_FLASH_MODE, Integer.valueOf(flashMode));
            return this;
        }

        public Builder setCaptureBundle(CaptureBundle captureBundle) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_CAPTURE_BUNDLE, captureBundle);
            return this;
        }

        public Builder setCaptureProcessor(CaptureProcessor captureProcessor) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_CAPTURE_PROCESSOR, captureProcessor);
            return this;
        }

        public Builder setBufferFormat(int bufferImageFormat) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_BUFFER_FORMAT, Integer.valueOf(bufferImageFormat));
            return this;
        }

        public Builder setMaxCaptureStages(int maxCaptureStages) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_MAX_CAPTURE_STAGES, Integer.valueOf(maxCaptureStages));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setSupportedResolutions(List<Pair<Integer, Size[]>> resolutions) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_SUPPORTED_RESOLUTIONS, resolutions);
            return this;
        }

        @Override // androidx.camera.core.internal.TargetConfig.Builder
        public Builder setTargetClass(Class<ImageCapture> targetClass) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_TARGET_CLASS, targetClass);
            if (getMutableConfig().retrieveOption(ImageCaptureConfig.OPTION_TARGET_NAME, null) == null) {
                setTargetName(targetClass.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        @Override // androidx.camera.core.internal.TargetConfig.Builder
        public Builder setTargetName(String targetName) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_TARGET_NAME, targetName);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetAspectRatioCustom(Rational aspectRatio) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM, aspectRatio);
            getMutableConfig().removeOption(ImageCaptureConfig.OPTION_TARGET_ASPECT_RATIO);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetAspectRatio(int aspectRatio) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_TARGET_ASPECT_RATIO, Integer.valueOf(aspectRatio));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetRotation(int rotation) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_TARGET_ROTATION, Integer.valueOf(rotation));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetResolution(Size resolution) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_TARGET_RESOLUTION, resolution);
            if (resolution != null) {
                getMutableConfig().insertOption(ImageCaptureConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM, new Rational(resolution.getWidth(), resolution.getHeight()));
            }
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setDefaultResolution(Size resolution) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_DEFAULT_RESOLUTION, resolution);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setMaxResolution(Size resolution) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_MAX_RESOLUTION, resolution);
            return this;
        }

        public Builder setImageReaderProxyProvider(ImageReaderProxyProvider imageReaderProxyProvider) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_IMAGE_READER_PROXY_PROVIDER, imageReaderProxyProvider);
            return this;
        }

        @Override // androidx.camera.core.internal.IoConfig.Builder
        public Builder setIoExecutor(Executor executor) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_IO_EXECUTOR, executor);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setDefaultSessionConfig(SessionConfig sessionConfig) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_DEFAULT_SESSION_CONFIG, sessionConfig);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setDefaultCaptureConfig(CaptureConfig captureConfig) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_DEFAULT_CAPTURE_CONFIG, captureConfig);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setSessionOptionUnpacker(SessionConfig.OptionUnpacker optionUnpacker) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_SESSION_CONFIG_UNPACKER, optionUnpacker);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setCaptureOptionUnpacker(CaptureConfig.OptionUnpacker optionUnpacker) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_CAPTURE_CONFIG_UNPACKER, optionUnpacker);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setCameraSelector(CameraSelector cameraSelector) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_CAMERA_SELECTOR, cameraSelector);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setSurfaceOccupancyPriority(int priority) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_SURFACE_OCCUPANCY_PRIORITY, Integer.valueOf(priority));
            return this;
        }

        @Override // androidx.camera.core.internal.UseCaseEventConfig.Builder
        public Builder setUseCaseEventCallback(UseCase.EventCallback useCaseEventCallback) {
            getMutableConfig().insertOption(ImageCaptureConfig.OPTION_USE_CASE_EVENT_CALLBACK, useCaseEventCallback);
            return this;
        }
    }
}
