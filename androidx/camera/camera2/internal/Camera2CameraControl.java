package androidx.camera.camera2.internal;

import android.graphics.Rect;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Rational;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.camera2.internal.Camera2CameraControl;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraCaptureFailure;
import androidx.camera.core.impl.CameraCaptureResult;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
/* loaded from: classes.dex */
public final class Camera2CameraControl implements CameraControlInternal {
    private static final String TAG;
    private final AeFpsRange mAeFpsRange;
    private final CameraCharacteristics mCameraCharacteristics;
    private final CameraControlInternal.ControlUpdateCallback mControlUpdateCallback;
    final Executor mExecutor;
    private final FocusMeteringControl mFocusMeteringControl;
    final CameraControlSessionCallback mSessionCallback;
    private final TorchControl mTorchControl;
    private final ZoomControl mZoomControl;
    private final SessionConfig.Builder mSessionConfigBuilder = new SessionConfig.Builder();
    volatile Rational mPreviewAspectRatio = null;
    private volatile boolean mIsTorchOn = false;
    private volatile int mFlashMode = 2;
    private Rect mCropRect = null;
    private final CameraCaptureCallbackSet mCameraCaptureCallbackSet = new CameraCaptureCallbackSet();

    /* loaded from: classes.dex */
    public interface CaptureResultListener {
        boolean onCaptureResult(TotalCaptureResult totalCaptureResult);
    }

    public Camera2CameraControl(CameraCharacteristics cameraCharacteristics, ScheduledExecutorService scheduler, Executor executor, CameraControlInternal.ControlUpdateCallback controlUpdateCallback) {
        this.mCameraCharacteristics = cameraCharacteristics;
        this.mControlUpdateCallback = controlUpdateCallback;
        this.mExecutor = executor;
        this.mSessionCallback = new CameraControlSessionCallback(this.mExecutor);
        this.mSessionConfigBuilder.setTemplateType(getDefaultTemplate());
        this.mSessionConfigBuilder.addRepeatingCameraCaptureCallback(CaptureCallbackContainer.create(this.mSessionCallback));
        this.mSessionConfigBuilder.addRepeatingCameraCaptureCallback(this.mCameraCaptureCallbackSet);
        this.mFocusMeteringControl = new FocusMeteringControl(this, scheduler, this.mExecutor);
        this.mZoomControl = new ZoomControl(this, this.mCameraCharacteristics);
        this.mTorchControl = new TorchControl(this, this.mCameraCharacteristics);
        this.mAeFpsRange = new AeFpsRange(this.mCameraCharacteristics);
        this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$o3ojAeQw5uAR3XMBxVrGNZPoNY0
            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.updateSessionConfig();
            }
        });
    }

    public ZoomControl getZoomControl() {
        return this.mZoomControl;
    }

    public TorchControl getTorchControl() {
        return this.mTorchControl;
    }

    public void setActive(boolean isActive) {
        this.mFocusMeteringControl.setActive(isActive);
        this.mZoomControl.setActive(isActive);
        this.mTorchControl.setActive(isActive);
    }

    public void setPreviewAspectRatio(Rational previewAspectRatio) {
        this.mPreviewAspectRatio = previewAspectRatio;
    }

    public void setDefaultRequestBuilder(CaptureRequest.Builder builder) {
        this.mFocusMeteringControl.setDefaultRequestBuilder(builder);
    }

    @Override // androidx.camera.core.CameraControl
    public ListenableFuture<FocusMeteringResult> startFocusAndMetering(FocusMeteringAction action) {
        return this.mFocusMeteringControl.startFocusAndMetering(action, this.mPreviewAspectRatio);
    }

    @Override // androidx.camera.core.CameraControl
    public ListenableFuture<Void> cancelFocusAndMetering() {
        return this.mFocusMeteringControl.cancelFocusAndMetering();
    }

    @Override // androidx.camera.core.CameraControl
    public ListenableFuture<Void> setZoomRatio(float ratio) {
        return this.mZoomControl.setZoomRatio(ratio);
    }

    @Override // androidx.camera.core.CameraControl
    public ListenableFuture<Void> setLinearZoom(float linearZoom) {
        return this.mZoomControl.setLinearZoom(linearZoom);
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public void setCropRegion(Rect crop) {
        this.mExecutor.execute(new Runnable(crop) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$n-55emHrx23eyFO4AOfALgxleaM
            private final /* synthetic */ Rect f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.lambda$setCropRegion$0$Camera2CameraControl(this.f$1);
            }
        });
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public int getFlashMode() {
        return this.mFlashMode;
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public void setFlashMode(int flashMode) {
        this.mFlashMode = flashMode;
        this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$o3ojAeQw5uAR3XMBxVrGNZPoNY0
            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.updateSessionConfig();
            }
        });
    }

    @Override // androidx.camera.core.CameraControl
    public ListenableFuture<Void> enableTorch(boolean torch) {
        return this.mTorchControl.enableTorch(torch);
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public ListenableFuture<CameraCaptureResult> triggerAf() {
        return Futures.nonCancellationPropagating(CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$LhXXr4YjcqqtgB80Gt7KYEF-Sqg
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return Camera2CameraControl.this.lambda$triggerAf$2$Camera2CameraControl(completer);
            }
        }));
    }

    public /* synthetic */ void lambda$triggerAf$1$Camera2CameraControl(CallbackToFutureAdapter.Completer completer) {
        this.mFocusMeteringControl.triggerAf(completer);
    }

    public /* synthetic */ Object lambda$triggerAf$2$Camera2CameraControl(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new Runnable(completer) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$pRTQClz14v6jtAS2kzg5k34jwRU
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.lambda$triggerAf$1$Camera2CameraControl(this.f$1);
            }
        });
        return "triggerAf";
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public ListenableFuture<CameraCaptureResult> triggerAePrecapture() {
        return Futures.nonCancellationPropagating(CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$qbEp_blAtgFNyq5CIYkSoors2BI
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return Camera2CameraControl.this.lambda$triggerAePrecapture$4$Camera2CameraControl(completer);
            }
        }));
    }

    public /* synthetic */ void lambda$triggerAePrecapture$3$Camera2CameraControl(CallbackToFutureAdapter.Completer completer) {
        this.mFocusMeteringControl.triggerAePrecapture(completer);
    }

    public /* synthetic */ Object lambda$triggerAePrecapture$4$Camera2CameraControl(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new Runnable(completer) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$m38ypg5zNYM-xs4jbaE3pba6GLE
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.lambda$triggerAePrecapture$3$Camera2CameraControl(this.f$1);
            }
        });
        return "triggerAePrecapture";
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public void cancelAfAeTrigger(boolean cancelAfTrigger, boolean cancelAePrecaptureTrigger) {
        this.mExecutor.execute(new Runnable(cancelAfTrigger, cancelAePrecaptureTrigger) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$SH_MDi-RNYlrIOaQQ35VpXd0Zlk
            private final /* synthetic */ boolean f$1;
            private final /* synthetic */ boolean f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.lambda$cancelAfAeTrigger$5$Camera2CameraControl(this.f$1, this.f$2);
            }
        });
    }

    public /* synthetic */ void lambda$cancelAfAeTrigger$5$Camera2CameraControl(boolean cancelAfTrigger, boolean cancelAePrecaptureTrigger) {
        this.mFocusMeteringControl.cancelAfAeTrigger(cancelAfTrigger, cancelAePrecaptureTrigger);
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public void submitCaptureRequests(List<CaptureConfig> captureConfigs) {
        this.mExecutor.execute(new Runnable(captureConfigs) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$EsEFplJTwKSf-WUUvRmZFH6Z3NE
            private final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.lambda$submitCaptureRequests$6$Camera2CameraControl(this.f$1);
            }
        });
    }

    public int getDefaultTemplate() {
        return 1;
    }

    public void updateSessionConfig() {
        this.mSessionConfigBuilder.setImplementationOptions(getSessionOptions());
        this.mControlUpdateCallback.onCameraControlUpdateSessionConfig(this.mSessionConfigBuilder.build());
    }

    /* renamed from: setCropRegionInternal */
    public void lambda$setCropRegion$0$Camera2CameraControl(Rect crop) {
        this.mCropRect = crop;
        updateSessionConfig();
    }

    public Rect getCropSensorRegion() {
        Rect cropRect = this.mCropRect;
        if (cropRect == null) {
            return getSensorRect();
        }
        return cropRect;
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public Rect getSensorRect() {
        return (Rect) Preconditions.checkNotNull((Rect) this.mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE));
    }

    public void removeCaptureResultListener(CaptureResultListener listener) {
        this.mSessionCallback.removeListener(listener);
    }

    public void addCaptureResultListener(CaptureResultListener listener) {
        this.mSessionCallback.addListener(listener);
    }

    public void addSessionCameraCaptureCallback(Executor executor, CameraCaptureCallback cameraCaptureCallback) {
        this.mExecutor.execute(new Runnable(executor, cameraCaptureCallback) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$nUACrUUeYrttbA02hHpwEbki32s
            private final /* synthetic */ Executor f$1;
            private final /* synthetic */ CameraCaptureCallback f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.lambda$addSessionCameraCaptureCallback$7$Camera2CameraControl(this.f$1, this.f$2);
            }
        });
    }

    public /* synthetic */ void lambda$addSessionCameraCaptureCallback$7$Camera2CameraControl(Executor executor, CameraCaptureCallback cameraCaptureCallback) {
        this.mCameraCaptureCallbackSet.addCaptureCallback(executor, cameraCaptureCallback);
    }

    public void removeSessionCameraCaptureCallback(CameraCaptureCallback cameraCaptureCallback) {
        this.mExecutor.execute(new Runnable(cameraCaptureCallback) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$ByuTObrXHLUA6qdbn8ZBNYGGu9M
            private final /* synthetic */ CameraCaptureCallback f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.lambda$removeSessionCameraCaptureCallback$8$Camera2CameraControl(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$removeSessionCameraCaptureCallback$8$Camera2CameraControl(CameraCaptureCallback cameraCaptureCallback) {
        this.mCameraCaptureCallbackSet.removeCaptureCallback(cameraCaptureCallback);
    }

    public void enableTorchInternal(boolean torch) {
        this.mExecutor.execute(new Runnable(torch) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$AcQVRm560m_YEuLhBqozdwsBVNM
            private final /* synthetic */ boolean f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Camera2CameraControl.this.lambda$enableTorchInternal$9$Camera2CameraControl(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$enableTorchInternal$9$Camera2CameraControl(boolean torch) {
        this.mIsTorchOn = torch;
        if (!torch) {
            CaptureConfig.Builder singleRequestBuilder = new CaptureConfig.Builder();
            singleRequestBuilder.setTemplateType(getDefaultTemplate());
            singleRequestBuilder.setUseRepeatingSurface(true);
            Camera2ImplConfig.Builder configBuilder = new Camera2ImplConfig.Builder();
            configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(getSupportedAeMode(1)));
            configBuilder.setCaptureRequestOption(CaptureRequest.FLASH_MODE, 0);
            singleRequestBuilder.addImplementationOptions(configBuilder.build());
            lambda$submitCaptureRequests$6$Camera2CameraControl(Collections.singletonList(singleRequestBuilder.build()));
        }
        updateSessionConfig();
    }

    /* renamed from: submitCaptureRequestsInternal */
    public void lambda$submitCaptureRequests$6$Camera2CameraControl(List<CaptureConfig> captureConfigs) {
        this.mControlUpdateCallback.onCameraControlCaptureRequests(captureConfigs);
    }

    Config getSessionOptions() {
        Camera2ImplConfig.Builder builder = new Camera2ImplConfig.Builder();
        builder.setCaptureRequestOption(CaptureRequest.CONTROL_MODE, 1);
        this.mFocusMeteringControl.addFocusMeteringOptions(builder);
        this.mAeFpsRange.addAeFpsRangeOptions(builder);
        int aeMode = 1;
        if (this.mIsTorchOn) {
            builder.setCaptureRequestOption(CaptureRequest.FLASH_MODE, 2);
        } else {
            int i = this.mFlashMode;
            if (i == 0) {
                aeMode = 2;
            } else if (i == 1) {
                aeMode = 3;
            } else if (i == 2) {
                aeMode = 1;
            }
        }
        builder.setCaptureRequestOption(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(getSupportedAeMode(aeMode)));
        builder.setCaptureRequestOption(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(getSupportedAwbMode(1)));
        if (this.mCropRect != null) {
            builder.setCaptureRequestOption(CaptureRequest.SCALER_CROP_REGION, this.mCropRect);
        }
        return builder.build();
    }

    public int getSupportedAfMode(int preferredMode) {
        int[] modes = (int[]) this.mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        if (modes == null) {
            return 0;
        }
        if (isModeInList(preferredMode, modes)) {
            return preferredMode;
        }
        if (isModeInList(4, modes)) {
            return 4;
        }
        if (isModeInList(1, modes)) {
            return 1;
        }
        return 0;
    }

    private int getSupportedAeMode(int preferredMode) {
        int[] modes = (int[]) this.mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
        if (modes == null) {
            return 0;
        }
        if (isModeInList(preferredMode, modes)) {
            return preferredMode;
        }
        if (isModeInList(1, modes)) {
            return 1;
        }
        return 0;
    }

    private int getSupportedAwbMode(int preferredMode) {
        int[] modes = (int[]) this.mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
        if (modes == null) {
            return 0;
        }
        if (isModeInList(preferredMode, modes)) {
            return preferredMode;
        }
        if (isModeInList(1, modes)) {
            return 1;
        }
        return 0;
    }

    private boolean isModeInList(int mode, int[] modeList) {
        for (int m : modeList) {
            if (mode == m) {
                return true;
            }
        }
        return false;
    }

    public int getMaxAfRegionCount() {
        Integer count = (Integer) this.mCameraCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF);
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    public int getMaxAeRegionCount() {
        Integer count = (Integer) this.mCameraCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AE);
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    public int getMaxAwbRegionCount() {
        Integer count = (Integer) this.mCameraCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB);
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    /* loaded from: classes.dex */
    public static final class CameraControlSessionCallback extends CameraCaptureSession.CaptureCallback {
        private final Executor mExecutor;
        final Set<CaptureResultListener> mResultListeners = new HashSet();

        CameraControlSessionCallback(Executor executor) {
            this.mExecutor = executor;
        }

        void addListener(CaptureResultListener listener) {
            this.mResultListeners.add(listener);
        }

        void removeListener(CaptureResultListener listener) {
            this.mResultListeners.remove(listener);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            this.mExecutor.execute(new Runnable(result) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$CameraControlSessionCallback$D22-IA_eQmzTWvkNwlgaZNWM8E8
                private final /* synthetic */ TotalCaptureResult f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Camera2CameraControl.CameraControlSessionCallback.this.lambda$onCaptureCompleted$0$Camera2CameraControl$CameraControlSessionCallback(this.f$1);
                }
            });
        }

        public /* synthetic */ void lambda$onCaptureCompleted$0$Camera2CameraControl$CameraControlSessionCallback(TotalCaptureResult result) {
            Set<CaptureResultListener> removeSet = new HashSet<>();
            for (CaptureResultListener listener : this.mResultListeners) {
                if (listener.onCaptureResult(result)) {
                    removeSet.add(listener);
                }
            }
            if (!removeSet.isEmpty()) {
                this.mResultListeners.removeAll(removeSet);
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class CameraCaptureCallbackSet extends CameraCaptureCallback {
        Set<CameraCaptureCallback> mCallbacks = new HashSet();
        Map<CameraCaptureCallback, Executor> mCallbackExecutors = new ArrayMap();

        CameraCaptureCallbackSet() {
        }

        void addCaptureCallback(Executor executor, CameraCaptureCallback callback) {
            this.mCallbacks.add(callback);
            this.mCallbackExecutors.put(callback, executor);
        }

        void removeCaptureCallback(CameraCaptureCallback callback) {
            this.mCallbacks.remove(callback);
            this.mCallbackExecutors.remove(callback);
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureCompleted(CameraCaptureResult cameraCaptureResult) {
            for (CameraCaptureCallback callback : this.mCallbacks) {
                try {
                    this.mCallbackExecutors.get(callback).execute(new Runnable(cameraCaptureResult) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$CameraCaptureCallbackSet$w0Rqa-UkCHSqHwweFl8JeVvrhr4
                        private final /* synthetic */ CameraCaptureResult f$1;

                        {
                            this.f$1 = r2;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            Camera2CameraControl.CameraCaptureCallbackSet.lambda$onCaptureCompleted$0(CameraCaptureCallback.this, this.f$1);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    Log.e(Camera2CameraControl.TAG, "Executor rejected to invoke onCaptureCompleted.", e);
                }
            }
        }

        public static /* synthetic */ void lambda$onCaptureCompleted$0(CameraCaptureCallback callback, CameraCaptureResult cameraCaptureResult) {
            callback.onCaptureCompleted(cameraCaptureResult);
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureFailed(CameraCaptureFailure failure) {
            for (CameraCaptureCallback callback : this.mCallbacks) {
                try {
                    this.mCallbackExecutors.get(callback).execute(new Runnable(failure) { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$CameraCaptureCallbackSet$PYNNtxWEYACZB4jFztc5CT7l5Us
                        private final /* synthetic */ CameraCaptureFailure f$1;

                        {
                            this.f$1 = r2;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            Camera2CameraControl.CameraCaptureCallbackSet.lambda$onCaptureFailed$1(CameraCaptureCallback.this, this.f$1);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    Log.e(Camera2CameraControl.TAG, "Executor rejected to invoke onCaptureFailed.", e);
                }
            }
        }

        public static /* synthetic */ void lambda$onCaptureFailed$1(CameraCaptureCallback callback, CameraCaptureFailure failure) {
            callback.onCaptureFailed(failure);
        }

        @Override // androidx.camera.core.impl.CameraCaptureCallback
        public void onCaptureCancelled() {
            for (CameraCaptureCallback callback : this.mCallbacks) {
                try {
                    this.mCallbackExecutors.get(callback).execute(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$Camera2CameraControl$CameraCaptureCallbackSet$rwnowJaR0rTqAvqzQggRKxKF0jc
                        @Override // java.lang.Runnable
                        public final void run() {
                            Camera2CameraControl.CameraCaptureCallbackSet.lambda$onCaptureCancelled$2(CameraCaptureCallback.this);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    Log.e(Camera2CameraControl.TAG, "Executor rejected to invoke onCaptureCancelled.", e);
                }
            }
        }

        public static /* synthetic */ void lambda$onCaptureCancelled$2(CameraCaptureCallback callback) {
            callback.onCaptureCancelled();
        }
    }
}
