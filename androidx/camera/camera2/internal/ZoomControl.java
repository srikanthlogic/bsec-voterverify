package androidx.camera.camera2.internal;

import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Looper;
import androidx.camera.camera2.internal.Camera2CameraControl;
import androidx.camera.core.CameraControl;
import androidx.camera.core.ZoomState;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.core.internal.ImmutableZoomState;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.common.util.concurrent.ListenableFuture;
/* loaded from: classes.dex */
public final class ZoomControl {
    public static final float DEFAULT_ZOOM_RATIO;
    public static final float MIN_ZOOM;
    private static final String TAG;
    private final Camera2CameraControl mCamera2CameraControl;
    private final ZoomStateImpl mCurrentZoomState;
    CallbackToFutureAdapter.Completer<Void> mPendingZoomRatioCompleter;
    private final MutableLiveData<ZoomState> mZoomStateLiveData;
    final Object mCompleterLock = new Object();
    Rect mPendingZoomCropRegion = null;
    final Object mActiveLock = new Object();
    private boolean mIsActive = false;
    private Camera2CameraControl.CaptureResultListener mCaptureResultListener = new Camera2CameraControl.CaptureResultListener() { // from class: androidx.camera.camera2.internal.ZoomControl.1
        @Override // androidx.camera.camera2.internal.Camera2CameraControl.CaptureResultListener
        public boolean onCaptureResult(TotalCaptureResult captureResult) {
            Rect cropRect;
            CallbackToFutureAdapter.Completer<Void> completerToSet = null;
            synchronized (ZoomControl.this.mCompleterLock) {
                if (ZoomControl.this.mPendingZoomRatioCompleter != null) {
                    CaptureRequest request = captureResult.getRequest();
                    if (request == null) {
                        cropRect = null;
                    } else {
                        cropRect = (Rect) request.get(CaptureRequest.SCALER_CROP_REGION);
                    }
                    if (ZoomControl.this.mPendingZoomCropRegion != null && ZoomControl.this.mPendingZoomCropRegion.equals(cropRect)) {
                        completerToSet = ZoomControl.this.mPendingZoomRatioCompleter;
                        ZoomControl.this.mPendingZoomRatioCompleter = null;
                        ZoomControl.this.mPendingZoomCropRegion = null;
                    }
                }
            }
            if (completerToSet == null) {
                return false;
            }
            completerToSet.set(null);
            return false;
        }
    };

    public ZoomControl(Camera2CameraControl camera2CameraControl, CameraCharacteristics cameraCharacteristics) {
        this.mCamera2CameraControl = camera2CameraControl;
        this.mCurrentZoomState = new ZoomStateImpl(getMaxDigitalZoom(cameraCharacteristics), 1.0f);
        this.mCurrentZoomState.setZoomRatio(1.0f);
        this.mZoomStateLiveData = new MutableLiveData<>(ImmutableZoomState.create(this.mCurrentZoomState));
        camera2CameraControl.addCaptureResultListener(this.mCaptureResultListener);
    }

    public void setActive(boolean isActive) {
        CallbackToFutureAdapter.Completer<Void> completerToSetException = null;
        boolean shouldResetDefault = false;
        synchronized (this.mActiveLock) {
            if (this.mIsActive != isActive) {
                this.mIsActive = isActive;
                if (!this.mIsActive) {
                    synchronized (this.mCompleterLock) {
                        if (this.mPendingZoomRatioCompleter != null) {
                            completerToSetException = this.mPendingZoomRatioCompleter;
                            this.mPendingZoomRatioCompleter = null;
                            this.mPendingZoomCropRegion = null;
                        }
                    }
                    shouldResetDefault = true;
                    this.mCurrentZoomState.setZoomRatio(1.0f);
                    updateLiveData(ImmutableZoomState.create(this.mCurrentZoomState));
                }
                if (shouldResetDefault) {
                    this.mCamera2CameraControl.setCropRegion(null);
                }
                if (completerToSetException != null) {
                    completerToSetException.setException(new CameraControl.OperationCanceledException("Camera is not active."));
                }
            }
        }
    }

    public ListenableFuture<Void> setZoomRatio(float ratio) {
        synchronized (this.mActiveLock) {
            if (!this.mIsActive) {
                return Futures.immediateFailedFuture(new CameraControl.OperationCanceledException("Camera is not active."));
            }
            try {
                this.mCurrentZoomState.setZoomRatio(ratio);
                updateLiveData(ImmutableZoomState.create(this.mCurrentZoomState));
                return submitCameraZoomRatio(ratio);
            } catch (IllegalArgumentException e) {
                return Futures.immediateFailedFuture(e);
            }
        }
    }

    static Rect getCropRectByRatio(Rect sensorRect, float ratio) {
        float cropWidth = ((float) sensorRect.width()) / ratio;
        float cropHeight = ((float) sensorRect.height()) / ratio;
        float left = (((float) sensorRect.width()) - cropWidth) / 2.0f;
        float top = (((float) sensorRect.height()) - cropHeight) / 2.0f;
        return new Rect((int) left, (int) top, (int) (left + cropWidth), (int) (top + cropHeight));
    }

    private ListenableFuture<Void> submitCameraZoomRatio(float ratio) {
        Rect targetRegion = getCropRectByRatio(this.mCamera2CameraControl.getSensorRect(), ratio);
        this.mCamera2CameraControl.setCropRegion(targetRegion);
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(targetRegion) { // from class: androidx.camera.camera2.internal.-$$Lambda$ZoomControl$Drb-7VorNRO23tcp0rNs8amFoh4
            private final /* synthetic */ Rect f$1;

            {
                this.f$1 = r2;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ZoomControl.this.lambda$submitCameraZoomRatio$0$ZoomControl(this.f$1, completer);
            }
        });
    }

    public /* synthetic */ Object lambda$submitCameraZoomRatio$0$ZoomControl(Rect targetRegion, CallbackToFutureAdapter.Completer completer) throws Exception {
        CallbackToFutureAdapter.Completer<Void> completerToCancel = null;
        synchronized (this.mCompleterLock) {
            if (this.mPendingZoomRatioCompleter != null) {
                completerToCancel = this.mPendingZoomRatioCompleter;
                this.mPendingZoomRatioCompleter = null;
            }
            this.mPendingZoomCropRegion = targetRegion;
            this.mPendingZoomRatioCompleter = completer;
        }
        if (completerToCancel == null) {
            return "setZoomRatio";
        }
        completerToCancel.setException(new CameraControl.OperationCanceledException("There is a new zoomRatio being set"));
        return "setZoomRatio";
    }

    public ListenableFuture<Void> setLinearZoom(float linearZoom) {
        synchronized (this.mActiveLock) {
            if (!this.mIsActive) {
                return Futures.immediateFailedFuture(new CameraControl.OperationCanceledException("Camera is not active."));
            }
            try {
                this.mCurrentZoomState.setLinearZoom(linearZoom);
                updateLiveData(ImmutableZoomState.create(this.mCurrentZoomState));
                return submitCameraZoomRatio(this.mCurrentZoomState.getZoomRatio());
            } catch (IllegalArgumentException e) {
                return Futures.immediateFailedFuture(e);
            }
        }
    }

    private void updateLiveData(ZoomState zoomState) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.mZoomStateLiveData.setValue(zoomState);
        } else {
            this.mZoomStateLiveData.postValue(zoomState);
        }
    }

    public LiveData<ZoomState> getZoomState() {
        return this.mZoomStateLiveData;
    }

    private static float getMaxDigitalZoom(CameraCharacteristics cameraCharacteristics) {
        Float maxZoom = (Float) cameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
        if (maxZoom == null) {
            return 1.0f;
        }
        return maxZoom.floatValue();
    }
}
