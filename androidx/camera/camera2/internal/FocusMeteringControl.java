package androidx.camera.camera2.internal;

import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Build;
import android.util.Rational;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.camera2.internal.Camera2CameraControl;
import androidx.camera.core.CameraControl;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraCaptureFailure;
import androidx.camera.core.impl.CameraCaptureResult;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.CaptureConfig;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class FocusMeteringControl {
    private static final String TAG;
    private ScheduledFuture<?> mAutoCancelHandle;
    private final Camera2CameraControl mCameraControl;
    final Executor mExecutor;
    private final ScheduledExecutorService mScheduler;
    private volatile boolean mIsActive = false;
    private boolean mIsInAfAutoMode = false;
    Integer mCurrentAfState = 0;
    long mFocusTimeoutCounter = 0;
    boolean mIsAutoFocusCompleted = false;
    boolean mIsFocusSuccessful = false;
    private Camera2CameraControl.CaptureResultListener mSessionListenerForFocus = null;
    private Camera2CameraControl.CaptureResultListener mSessionListenerForCancel = null;
    private MeteringRectangle[] mAfRects = new MeteringRectangle[0];
    private MeteringRectangle[] mAeRects = new MeteringRectangle[0];
    private MeteringRectangle[] mAwbRects = new MeteringRectangle[0];
    MeteringRectangle[] mDefaultAfRects = new MeteringRectangle[0];
    MeteringRectangle[] mDefaultAeRects = new MeteringRectangle[0];
    MeteringRectangle[] mDefaultAwbRects = new MeteringRectangle[0];
    CallbackToFutureAdapter.Completer<FocusMeteringResult> mRunningActionCompleter = null;
    CallbackToFutureAdapter.Completer<Void> mRunningCancelCompleter = null;

    public FocusMeteringControl(Camera2CameraControl cameraControl, ScheduledExecutorService scheduler, Executor executor) {
        this.mCameraControl = cameraControl;
        this.mExecutor = executor;
        this.mScheduler = scheduler;
    }

    public void setDefaultRequestBuilder(CaptureRequest.Builder builder) {
        this.mDefaultAfRects = (MeteringRectangle[]) builder.get(CaptureRequest.CONTROL_AF_REGIONS);
        this.mDefaultAeRects = (MeteringRectangle[]) builder.get(CaptureRequest.CONTROL_AE_REGIONS);
        this.mDefaultAwbRects = (MeteringRectangle[]) builder.get(CaptureRequest.CONTROL_AWB_REGIONS);
    }

    public void setActive(boolean isActive) {
        if (isActive != this.mIsActive) {
            this.mIsActive = isActive;
            if (!this.mIsActive) {
                this.mExecutor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$Fo24HuX4QgnBob7n82jQvBqN3XU
                    @Override // java.lang.Runnable
                    public final void run() {
                        FocusMeteringControl.this.lambda$setActive$0$FocusMeteringControl();
                    }
                });
            }
        }
    }

    public void addFocusMeteringOptions(Camera2ImplConfig.Builder configBuilder) {
        int afMode;
        if (this.mIsInAfAutoMode) {
            afMode = 1;
        } else {
            afMode = 4;
        }
        configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(this.mCameraControl.getSupportedAfMode(afMode)));
        if (this.mAfRects.length != 0) {
            configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AF_REGIONS, this.mAfRects);
        }
        if (this.mAeRects.length != 0) {
            configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AE_REGIONS, this.mAeRects);
        }
        if (this.mAwbRects.length != 0) {
            configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AWB_REGIONS, this.mAwbRects);
        }
    }

    private boolean isValid(MeteringPoint pt) {
        return pt.getX() >= 0.0f && pt.getX() <= 1.0f && pt.getY() >= 0.0f && pt.getY() <= 1.0f;
    }

    private PointF getFovAdjustedPoint(MeteringPoint meteringPoint, Rational cropRegionAspectRatio, Rational defaultAspectRatio) {
        Rational fovAspectRatio = defaultAspectRatio;
        if (meteringPoint.getSurfaceAspectRatio() != null) {
            fovAspectRatio = meteringPoint.getSurfaceAspectRatio();
        }
        PointF adjustedPoint = new PointF(meteringPoint.getX(), meteringPoint.getY());
        if (!fovAspectRatio.equals(cropRegionAspectRatio)) {
            if (fovAspectRatio.compareTo(cropRegionAspectRatio) > 0) {
                float heightOfCropRegion = (float) (fovAspectRatio.doubleValue() / cropRegionAspectRatio.doubleValue());
                adjustedPoint.y = (adjustedPoint.y + ((float) ((((double) heightOfCropRegion) - 1.0d) / 2.0d))) * (1.0f / heightOfCropRegion);
            } else {
                float widthOfCropRegion = (float) (cropRegionAspectRatio.doubleValue() / fovAspectRatio.doubleValue());
                adjustedPoint.x = (adjustedPoint.x + ((float) ((((double) widthOfCropRegion) - 1.0d) / 2.0d))) * (1.0f / widthOfCropRegion);
            }
        }
        return adjustedPoint;
    }

    private MeteringRectangle getMeteringRect(MeteringPoint meteringPoint, PointF adjustedPoint, Rect cropRegion) {
        int centerX = (int) (((float) cropRegion.left) + (adjustedPoint.x * ((float) cropRegion.width())));
        int centerY = (int) (((float) cropRegion.top) + (adjustedPoint.y * ((float) cropRegion.height())));
        int width = (int) (meteringPoint.getSize() * ((float) cropRegion.width()));
        int height = (int) (meteringPoint.getSize() * ((float) cropRegion.height()));
        Rect focusRect = new Rect(centerX - (width / 2), centerY - (height / 2), (width / 2) + centerX, (height / 2) + centerY);
        focusRect.left = rangeLimit(focusRect.left, cropRegion.right, cropRegion.left);
        focusRect.right = rangeLimit(focusRect.right, cropRegion.right, cropRegion.left);
        focusRect.top = rangeLimit(focusRect.top, cropRegion.bottom, cropRegion.top);
        focusRect.bottom = rangeLimit(focusRect.bottom, cropRegion.bottom, cropRegion.top);
        return new MeteringRectangle(focusRect, 1000);
    }

    private int rangeLimit(int val, int max, int min) {
        return Math.min(Math.max(val, min), max);
    }

    public ListenableFuture<FocusMeteringResult> startFocusAndMetering(FocusMeteringAction action, Rational defaultAspectRatio) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(action, defaultAspectRatio) { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$Gz_st8wNW3AE79rbLhzPR3lH1sM
            private final /* synthetic */ FocusMeteringAction f$1;
            private final /* synthetic */ Rational f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return FocusMeteringControl.this.lambda$startFocusAndMetering$2$FocusMeteringControl(this.f$1, this.f$2, completer);
            }
        });
    }

    public /* synthetic */ Object lambda$startFocusAndMetering$2$FocusMeteringControl(FocusMeteringAction action, Rational defaultAspectRatio, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new Runnable(completer, action, defaultAspectRatio) { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$op3EKNHLPc_PTEcZjXwaEiMKsWc
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;
            private final /* synthetic */ FocusMeteringAction f$2;
            private final /* synthetic */ Rational f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                FocusMeteringControl.this.lambda$startFocusAndMetering$1$FocusMeteringControl(this.f$1, this.f$2, this.f$3);
            }
        });
        return "startFocusAndMetering";
    }

    /* renamed from: startFocusAndMeteringInternal */
    public void lambda$startFocusAndMetering$1$FocusMeteringControl(CallbackToFutureAdapter.Completer<FocusMeteringResult> completer, FocusMeteringAction action, Rational defaultAspectRatio) {
        Rational defaultAspectRatio2;
        if (!this.mIsActive) {
            completer.setException(new CameraControl.OperationCanceledException("Camera is not active."));
        } else if (!action.getMeteringPointsAf().isEmpty() || !action.getMeteringPointsAe().isEmpty() || !action.getMeteringPointsAwb().isEmpty()) {
            int supportedAfCount = Math.min(action.getMeteringPointsAf().size(), this.mCameraControl.getMaxAfRegionCount());
            int supportedAeCount = Math.min(action.getMeteringPointsAe().size(), this.mCameraControl.getMaxAeRegionCount());
            int supportedAwbCount = Math.min(action.getMeteringPointsAwb().size(), this.mCameraControl.getMaxAwbRegionCount());
            if (supportedAfCount + supportedAeCount + supportedAwbCount <= 0) {
                completer.setException(new IllegalArgumentException("None of the specified AF/AE/AWB MeteringPoints is supported on this camera."));
                return;
            }
            List<MeteringPoint> meteringPointListAF = new ArrayList<>();
            List<MeteringPoint> meteringPointListAE = new ArrayList<>();
            List<MeteringPoint> meteringPointListAWB = new ArrayList<>();
            if (supportedAfCount > 0) {
                meteringPointListAF.addAll(action.getMeteringPointsAf().subList(0, supportedAfCount));
            }
            if (supportedAeCount > 0) {
                meteringPointListAE.addAll(action.getMeteringPointsAe().subList(0, supportedAeCount));
            }
            if (supportedAwbCount > 0) {
                meteringPointListAWB.addAll(action.getMeteringPointsAwb().subList(0, supportedAwbCount));
            }
            Rect cropSensorRegion = this.mCameraControl.getCropSensorRegion();
            Rational cropRegionAspectRatio = new Rational(cropSensorRegion.width(), cropSensorRegion.height());
            if (defaultAspectRatio == null) {
                defaultAspectRatio2 = cropRegionAspectRatio;
            } else {
                defaultAspectRatio2 = defaultAspectRatio;
            }
            List<MeteringRectangle> meteringRectanglesListAF = new ArrayList<>();
            List<MeteringRectangle> meteringRectanglesListAE = new ArrayList<>();
            List<MeteringRectangle> meteringRectanglesListAWB = new ArrayList<>();
            for (MeteringPoint meteringPoint : meteringPointListAF) {
                if (isValid(meteringPoint)) {
                    MeteringRectangle meteringRectangle = getMeteringRect(meteringPoint, getFovAdjustedPoint(meteringPoint, cropRegionAspectRatio, defaultAspectRatio2), cropSensorRegion);
                    if (meteringRectangle.getWidth() == 0) {
                        supportedAfCount = supportedAfCount;
                        supportedAeCount = supportedAeCount;
                    } else if (meteringRectangle.getHeight() == 0) {
                        supportedAfCount = supportedAfCount;
                        supportedAeCount = supportedAeCount;
                    } else {
                        meteringRectanglesListAF.add(meteringRectangle);
                        supportedAfCount = supportedAfCount;
                        supportedAeCount = supportedAeCount;
                    }
                }
            }
            Iterator<MeteringPoint> it = meteringPointListAE.iterator();
            while (it.hasNext()) {
                MeteringPoint meteringPoint2 = it.next();
                if (isValid(meteringPoint2)) {
                    MeteringRectangle meteringRectangle2 = getMeteringRect(meteringPoint2, getFovAdjustedPoint(meteringPoint2, cropRegionAspectRatio, defaultAspectRatio2), cropSensorRegion);
                    if (meteringRectangle2.getWidth() == 0) {
                        it = it;
                    } else if (meteringRectangle2.getHeight() == 0) {
                        it = it;
                    } else {
                        meteringRectanglesListAE.add(meteringRectangle2);
                        it = it;
                    }
                }
            }
            Iterator<MeteringPoint> it2 = meteringPointListAWB.iterator();
            while (it2.hasNext()) {
                MeteringPoint meteringPoint3 = it2.next();
                if (isValid(meteringPoint3)) {
                    MeteringRectangle meteringRectangle3 = getMeteringRect(meteringPoint3, getFovAdjustedPoint(meteringPoint3, cropRegionAspectRatio, defaultAspectRatio2), cropSensorRegion);
                    if (meteringRectangle3.getWidth() == 0) {
                        it2 = it2;
                    } else if (meteringRectangle3.getHeight() == 0) {
                        it2 = it2;
                    } else {
                        meteringRectanglesListAWB.add(meteringRectangle3);
                        it2 = it2;
                    }
                }
            }
            if (!meteringRectanglesListAF.isEmpty() || !meteringRectanglesListAE.isEmpty() || !meteringRectanglesListAWB.isEmpty()) {
                failActionFuture("Cancelled by another startFocusAndMetering()");
                failCancelFuture("Cancelled by another startFocusAndMetering()");
                disableAutoCancel();
                this.mRunningActionCompleter = completer;
                executeMeteringAction((MeteringRectangle[]) meteringRectanglesListAF.toArray(new MeteringRectangle[0]), (MeteringRectangle[]) meteringRectanglesListAE.toArray(new MeteringRectangle[0]), (MeteringRectangle[]) meteringRectanglesListAWB.toArray(new MeteringRectangle[0]), action);
                return;
            }
            completer.setException(new IllegalArgumentException("None of the specified AF/AE/AWB MeteringPoints are valid."));
        } else {
            completer.setException(new IllegalArgumentException("No AF/AE/AWB MeteringPoints are added."));
        }
    }

    private int getDefaultTemplate() {
        return 1;
    }

    public void triggerAf(final CallbackToFutureAdapter.Completer<CameraCaptureResult> completer) {
        if (this.mIsActive) {
            CaptureConfig.Builder builder = new CaptureConfig.Builder();
            builder.setTemplateType(getDefaultTemplate());
            builder.setUseRepeatingSurface(true);
            Camera2ImplConfig.Builder configBuilder = new Camera2ImplConfig.Builder();
            configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AF_TRIGGER, 1);
            builder.addImplementationOptions(configBuilder.build());
            builder.addCameraCaptureCallback(new CameraCaptureCallback() { // from class: androidx.camera.camera2.internal.FocusMeteringControl.1
                @Override // androidx.camera.core.impl.CameraCaptureCallback
                public void onCaptureCompleted(CameraCaptureResult cameraCaptureResult) {
                    CallbackToFutureAdapter.Completer completer2 = completer;
                    if (completer2 != null) {
                        completer2.set(cameraCaptureResult);
                    }
                }

                @Override // androidx.camera.core.impl.CameraCaptureCallback
                public void onCaptureFailed(CameraCaptureFailure failure) {
                    CallbackToFutureAdapter.Completer completer2 = completer;
                    if (completer2 != null) {
                        completer2.setException(new CameraControlInternal.CameraControlException(failure));
                    }
                }

                @Override // androidx.camera.core.impl.CameraCaptureCallback
                public void onCaptureCancelled() {
                    CallbackToFutureAdapter.Completer completer2 = completer;
                    if (completer2 != null) {
                        completer2.setException(new CameraControl.OperationCanceledException("Camera is closed"));
                    }
                }
            });
            this.mCameraControl.lambda$submitCaptureRequests$6$Camera2CameraControl(Collections.singletonList(builder.build()));
        } else if (completer != null) {
            completer.setException(new CameraControl.OperationCanceledException("Camera is not active."));
        }
    }

    public void triggerAePrecapture(final CallbackToFutureAdapter.Completer<CameraCaptureResult> completer) {
        if (this.mIsActive) {
            CaptureConfig.Builder builder = new CaptureConfig.Builder();
            builder.setTemplateType(getDefaultTemplate());
            builder.setUseRepeatingSurface(true);
            Camera2ImplConfig.Builder configBuilder = new Camera2ImplConfig.Builder();
            configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 1);
            builder.addImplementationOptions(configBuilder.build());
            builder.addCameraCaptureCallback(new CameraCaptureCallback() { // from class: androidx.camera.camera2.internal.FocusMeteringControl.2
                @Override // androidx.camera.core.impl.CameraCaptureCallback
                public void onCaptureCompleted(CameraCaptureResult cameraCaptureResult) {
                    CallbackToFutureAdapter.Completer completer2 = completer;
                    if (completer2 != null) {
                        completer2.set(cameraCaptureResult);
                    }
                }

                @Override // androidx.camera.core.impl.CameraCaptureCallback
                public void onCaptureFailed(CameraCaptureFailure failure) {
                    CallbackToFutureAdapter.Completer completer2 = completer;
                    if (completer2 != null) {
                        completer2.setException(new CameraControlInternal.CameraControlException(failure));
                    }
                }

                @Override // androidx.camera.core.impl.CameraCaptureCallback
                public void onCaptureCancelled() {
                    CallbackToFutureAdapter.Completer completer2 = completer;
                    if (completer2 != null) {
                        completer2.setException(new CameraControl.OperationCanceledException("Camera is closed"));
                    }
                }
            });
            this.mCameraControl.lambda$submitCaptureRequests$6$Camera2CameraControl(Collections.singletonList(builder.build()));
        } else if (completer != null) {
            completer.setException(new CameraControl.OperationCanceledException("Camera is not active."));
        }
    }

    public void cancelAfAeTrigger(boolean cancelAfTrigger, boolean cancelAePrecaptureTrigger) {
        if (this.mIsActive) {
            CaptureConfig.Builder builder = new CaptureConfig.Builder();
            builder.setUseRepeatingSurface(true);
            builder.setTemplateType(getDefaultTemplate());
            Camera2ImplConfig.Builder configBuilder = new Camera2ImplConfig.Builder();
            if (cancelAfTrigger) {
                configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AF_TRIGGER, 2);
            }
            if (Build.VERSION.SDK_INT >= 23 && cancelAePrecaptureTrigger) {
                configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 2);
            }
            builder.addImplementationOptions(configBuilder.build());
            this.mCameraControl.lambda$submitCaptureRequests$6$Camera2CameraControl(Collections.singletonList(builder.build()));
        }
    }

    private void disableAutoCancel() {
        ScheduledFuture<?> scheduledFuture = this.mAutoCancelHandle;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.mAutoCancelHandle = null;
        }
    }

    private static int getRegionCount(MeteringRectangle[] regions) {
        if (regions == null) {
            return 0;
        }
        return regions.length;
    }

    private static boolean hasEqualRegions(MeteringRectangle[] regions1, MeteringRectangle[] regions2) {
        if (getRegionCount(regions1) == 0 && getRegionCount(regions2) == 0) {
            return true;
        }
        if (getRegionCount(regions1) != getRegionCount(regions2)) {
            return false;
        }
        if (!(regions1 == null || regions2 == null)) {
            for (int i = 0; i < regions1.length; i++) {
                if (!regions1[i].equals(regions2[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isAfModeSupported() {
        return this.mCameraControl.getSupportedAfMode(1) == 1;
    }

    private void completeActionFuture(boolean isFocusSuccessful) {
        CallbackToFutureAdapter.Completer<FocusMeteringResult> completer = this.mRunningActionCompleter;
        if (completer != null) {
            completer.set(FocusMeteringResult.create(isFocusSuccessful));
            this.mRunningActionCompleter = null;
        }
    }

    private void failActionFuture(String message) {
        this.mCameraControl.removeCaptureResultListener(this.mSessionListenerForFocus);
        CallbackToFutureAdapter.Completer<FocusMeteringResult> completer = this.mRunningActionCompleter;
        if (completer != null) {
            completer.setException(new CameraControl.OperationCanceledException(message));
            this.mRunningActionCompleter = null;
        }
    }

    private void failCancelFuture(String message) {
        this.mCameraControl.removeCaptureResultListener(this.mSessionListenerForCancel);
        CallbackToFutureAdapter.Completer<Void> completer = this.mRunningCancelCompleter;
        if (completer != null) {
            completer.setException(new CameraControl.OperationCanceledException(message));
            this.mRunningCancelCompleter = null;
        }
    }

    private void completeCancelFuture() {
        CallbackToFutureAdapter.Completer<Void> completer = this.mRunningCancelCompleter;
        if (completer != null) {
            completer.set(null);
            this.mRunningCancelCompleter = null;
        }
    }

    private void executeMeteringAction(MeteringRectangle[] afRects, MeteringRectangle[] aeRects, MeteringRectangle[] awbRects, FocusMeteringAction focusMeteringAction) {
        this.mCameraControl.removeCaptureResultListener(this.mSessionListenerForFocus);
        disableAutoCancel();
        this.mAfRects = afRects;
        this.mAeRects = aeRects;
        this.mAwbRects = awbRects;
        if (shouldTriggerAF()) {
            this.mIsInAfAutoMode = true;
            this.mIsAutoFocusCompleted = false;
            this.mIsFocusSuccessful = false;
            this.mCameraControl.updateSessionConfig();
            triggerAf(null);
        } else {
            this.mIsInAfAutoMode = false;
            this.mIsAutoFocusCompleted = true;
            this.mIsFocusSuccessful = false;
            this.mCameraControl.updateSessionConfig();
        }
        this.mCurrentAfState = 0;
        this.mSessionListenerForFocus = new Camera2CameraControl.CaptureResultListener(isAfModeSupported(), afRects, aeRects, awbRects) { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$myFOvxwuOccCZDq-wHBp6yswlIg
            private final /* synthetic */ boolean f$1;
            private final /* synthetic */ MeteringRectangle[] f$2;
            private final /* synthetic */ MeteringRectangle[] f$3;
            private final /* synthetic */ MeteringRectangle[] f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // androidx.camera.camera2.internal.Camera2CameraControl.CaptureResultListener
            public final boolean onCaptureResult(TotalCaptureResult totalCaptureResult) {
                return FocusMeteringControl.this.lambda$executeMeteringAction$3$FocusMeteringControl(this.f$1, this.f$2, this.f$3, this.f$4, totalCaptureResult);
            }
        };
        this.mCameraControl.addCaptureResultListener(this.mSessionListenerForFocus);
        if (focusMeteringAction.isAutoCancelEnabled()) {
            long timeoutId = this.mFocusTimeoutCounter + 1;
            this.mFocusTimeoutCounter = timeoutId;
            this.mAutoCancelHandle = this.mScheduler.schedule(new Runnable(timeoutId) { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$d6WwVXBeWXIsWhvPS-3v3isXXpE
                private final /* synthetic */ long f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    FocusMeteringControl.this.lambda$executeMeteringAction$5$FocusMeteringControl(this.f$1);
                }
            }, focusMeteringAction.getAutoCancelDurationInMillis(), TimeUnit.MILLISECONDS);
        }
    }

    public /* synthetic */ boolean lambda$executeMeteringAction$3$FocusMeteringControl(boolean isAfModeSupported, MeteringRectangle[] afRects, MeteringRectangle[] aeRects, MeteringRectangle[] awbRects, TotalCaptureResult result) {
        Integer afState = (Integer) result.get(CaptureResult.CONTROL_AF_STATE);
        if (shouldTriggerAF()) {
            if (!isAfModeSupported || afState == null) {
                this.mIsFocusSuccessful = true;
                this.mIsAutoFocusCompleted = true;
            } else if (this.mCurrentAfState.intValue() == 3) {
                if (afState.intValue() == 4) {
                    this.mIsFocusSuccessful = true;
                    this.mIsAutoFocusCompleted = true;
                } else if (afState.intValue() == 5) {
                    this.mIsFocusSuccessful = false;
                    this.mIsAutoFocusCompleted = true;
                }
            }
        }
        if (this.mIsAutoFocusCompleted && result.getRequest() != null) {
            MeteringRectangle[] toMatchAfRegions = afRects.length != 0 ? afRects : this.mDefaultAfRects;
            MeteringRectangle[] toMatchAeRegions = aeRects.length != 0 ? aeRects : this.mDefaultAeRects;
            MeteringRectangle[] toMatchAwbRegions = awbRects.length != 0 ? awbRects : this.mDefaultAwbRects;
            CaptureRequest request = result.getRequest();
            if (hasEqualRegions((MeteringRectangle[]) request.get(CaptureRequest.CONTROL_AF_REGIONS), toMatchAfRegions) && hasEqualRegions((MeteringRectangle[]) request.get(CaptureRequest.CONTROL_AE_REGIONS), toMatchAeRegions) && hasEqualRegions((MeteringRectangle[]) request.get(CaptureRequest.CONTROL_AWB_REGIONS), toMatchAwbRegions)) {
                completeActionFuture(this.mIsFocusSuccessful);
                return true;
            }
        }
        if (!this.mCurrentAfState.equals(afState) && afState != null) {
            this.mCurrentAfState = afState;
        }
        return false;
    }

    public /* synthetic */ void lambda$executeMeteringAction$5$FocusMeteringControl(long timeoutId) {
        this.mExecutor.execute(new Runnable(timeoutId) { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$QDQ_s-ak03R5p4E1-ZO3ShzW4qo
            private final /* synthetic */ long f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                FocusMeteringControl.this.lambda$executeMeteringAction$4$FocusMeteringControl(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$executeMeteringAction$4$FocusMeteringControl(long timeoutId) {
        if (timeoutId == this.mFocusTimeoutCounter) {
            lambda$setActive$0$FocusMeteringControl();
        }
    }

    private boolean shouldTriggerAF() {
        return this.mAfRects.length > 0;
    }

    public ListenableFuture<Void> cancelFocusAndMetering() {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$FJquylG-HqkA7eofMN5jopP8hJg
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return FocusMeteringControl.this.lambda$cancelFocusAndMetering$7$FocusMeteringControl(completer);
            }
        });
    }

    public /* synthetic */ Object lambda$cancelFocusAndMetering$7$FocusMeteringControl(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new Runnable(completer) { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$Bui6vmx_a8rzc50QrH6J8MZ0kh0
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                FocusMeteringControl.this.lambda$cancelFocusAndMetering$6$FocusMeteringControl(this.f$1);
            }
        });
        return "cancelFocusAndMetering";
    }

    /* renamed from: cancelFocusAndMeteringWithoutAsyncResult */
    public void lambda$setActive$0$FocusMeteringControl() {
        lambda$cancelFocusAndMetering$6$FocusMeteringControl(null);
    }

    /* renamed from: cancelFocusAndMeteringInternal */
    public void lambda$cancelFocusAndMetering$6$FocusMeteringControl(CallbackToFutureAdapter.Completer<Void> completer) {
        failCancelFuture("Cancelled by another cancelFocusAndMetering()");
        failActionFuture("Cancelled by cancelFocusAndMetering()");
        this.mRunningCancelCompleter = completer;
        disableAutoCancel();
        if (this.mRunningCancelCompleter != null) {
            this.mSessionListenerForCancel = new Camera2CameraControl.CaptureResultListener(this.mCameraControl.getSupportedAfMode(4)) { // from class: androidx.camera.camera2.internal.-$$Lambda$FocusMeteringControl$vgaNwg87Tv3HiROj1CRAp20PA-Q
                private final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                @Override // androidx.camera.camera2.internal.Camera2CameraControl.CaptureResultListener
                public final boolean onCaptureResult(TotalCaptureResult totalCaptureResult) {
                    return FocusMeteringControl.this.lambda$cancelFocusAndMeteringInternal$8$FocusMeteringControl(this.f$1, totalCaptureResult);
                }
            };
            this.mCameraControl.addCaptureResultListener(this.mSessionListenerForCancel);
        }
        if (shouldTriggerAF()) {
            cancelAfAeTrigger(true, false);
        }
        this.mAfRects = new MeteringRectangle[0];
        this.mAeRects = new MeteringRectangle[0];
        this.mAwbRects = new MeteringRectangle[0];
        this.mIsInAfAutoMode = false;
        this.mCameraControl.updateSessionConfig();
    }

    public /* synthetic */ boolean lambda$cancelFocusAndMeteringInternal$8$FocusMeteringControl(int targetAfMode, TotalCaptureResult captureResult) {
        CaptureRequest request = captureResult.getRequest();
        MeteringRectangle[] afRegions = (MeteringRectangle[]) request.get(CaptureRequest.CONTROL_AF_REGIONS);
        MeteringRectangle[] aeRegions = (MeteringRectangle[]) request.get(CaptureRequest.CONTROL_AE_REGIONS);
        MeteringRectangle[] awbRegions = (MeteringRectangle[]) request.get(CaptureRequest.CONTROL_AWB_REGIONS);
        if (((Integer) captureResult.get(CaptureResult.CONTROL_AF_MODE)).intValue() != targetAfMode || !hasEqualRegions(afRegions, this.mDefaultAfRects) || !hasEqualRegions(aeRegions, this.mDefaultAeRects) || !hasEqualRegions(awbRegions, this.mDefaultAwbRects)) {
            return false;
        }
        completeCancelFuture();
        return true;
    }
}
