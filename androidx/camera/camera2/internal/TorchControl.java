package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;
import androidx.camera.camera2.internal.Camera2CameraControl;
import androidx.camera.core.CameraControl;
import androidx.camera.core.impl.utils.Threads;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.common.util.concurrent.ListenableFuture;
/* loaded from: classes.dex */
public final class TorchControl {
    private static final String TAG;
    private final Camera2CameraControl mCamera2CameraControl;
    CallbackToFutureAdapter.Completer<Void> mEnableTorchCompleter;
    private final boolean mHasFlashUnit;
    private boolean mIsActive;
    boolean mTargetTorchEnabled;
    private final MutableLiveData<Integer> mTorchState;
    final Object mEnableTorchLock = new Object();
    private final Object mActiveLock = new Object();
    private final Camera2CameraControl.CaptureResultListener mCaptureResultListener = new Camera2CameraControl.CaptureResultListener() { // from class: androidx.camera.camera2.internal.TorchControl.1
        @Override // androidx.camera.camera2.internal.Camera2CameraControl.CaptureResultListener
        public boolean onCaptureResult(TotalCaptureResult captureResult) {
            CallbackToFutureAdapter.Completer<Void> completerToSet = null;
            synchronized (TorchControl.this.mEnableTorchLock) {
                if (TorchControl.this.mEnableTorchCompleter != null) {
                    Integer flashMode = (Integer) captureResult.getRequest().get(CaptureRequest.FLASH_MODE);
                    if ((flashMode != null && flashMode.intValue() == 2) == TorchControl.this.mTargetTorchEnabled) {
                        completerToSet = TorchControl.this.mEnableTorchCompleter;
                        TorchControl.this.mEnableTorchCompleter = null;
                    }
                }
            }
            if (completerToSet != null) {
                completerToSet.set(null);
            }
            return false;
        }
    };

    public TorchControl(Camera2CameraControl camera2CameraControl, CameraCharacteristics cameraCharacteristics) {
        this.mCamera2CameraControl = camera2CameraControl;
        Boolean hasFlashUnit = (Boolean) cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        this.mHasFlashUnit = hasFlashUnit != null && hasFlashUnit.booleanValue();
        this.mTorchState = new MutableLiveData<>(0);
        this.mCamera2CameraControl.addCaptureResultListener(this.mCaptureResultListener);
    }

    public void setActive(boolean isActive) {
        synchronized (this.mActiveLock) {
            if (this.mIsActive != isActive) {
                this.mIsActive = isActive;
                boolean shouldResetDefault = false;
                CallbackToFutureAdapter.Completer<Void> completerToCancel = null;
                synchronized (this.mEnableTorchLock) {
                    if (!isActive) {
                        if (this.mEnableTorchCompleter != null) {
                            completerToCancel = this.mEnableTorchCompleter;
                            this.mEnableTorchCompleter = null;
                        }
                        if (this.mTargetTorchEnabled) {
                            shouldResetDefault = true;
                            this.mTargetTorchEnabled = false;
                            this.mCamera2CameraControl.enableTorchInternal(false);
                        }
                    }
                }
                if (shouldResetDefault) {
                    setLiveDataValue(this.mTorchState, 0);
                }
                if (completerToCancel != null) {
                    completerToCancel.setException(new CameraControl.OperationCanceledException("Camera is not active."));
                }
            }
        }
    }

    public ListenableFuture<Void> enableTorch(boolean enabled) {
        if (!this.mHasFlashUnit) {
            Log.d(TAG, "Unable to enableTorch due to there is no flash unit.");
            return Futures.immediateFailedFuture(new IllegalStateException("No flash unit"));
        }
        synchronized (this.mActiveLock) {
            if (!this.mIsActive) {
                return Futures.immediateFailedFuture(new CameraControl.OperationCanceledException("Camera is not active."));
            }
            return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(enabled) { // from class: androidx.camera.camera2.internal.-$$Lambda$TorchControl$yfLA642SB9aJ7gO5KSM5aWHWKK4
                private final /* synthetic */ boolean f$1;

                {
                    this.f$1 = r2;
                }

                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                    return TorchControl.this.lambda$enableTorch$0$TorchControl(this.f$1, completer);
                }
            });
        }
    }

    public /* synthetic */ Object lambda$enableTorch$0$TorchControl(boolean enabled, CallbackToFutureAdapter.Completer completer) throws Exception {
        CallbackToFutureAdapter.Completer<Void> completerToCancel = null;
        synchronized (this.mEnableTorchLock) {
            if (this.mEnableTorchCompleter != null) {
                completerToCancel = this.mEnableTorchCompleter;
            }
            this.mEnableTorchCompleter = completer;
            this.mTargetTorchEnabled = enabled;
            this.mCamera2CameraControl.enableTorchInternal(enabled);
        }
        setLiveDataValue(this.mTorchState, Integer.valueOf(enabled ? 1 : 0));
        if (completerToCancel != null) {
            completerToCancel.setException(new CameraControl.OperationCanceledException("There is a new enableTorch being set"));
        }
        return "enableTorch: " + enabled;
    }

    public LiveData<Integer> getTorchState() {
        return this.mTorchState;
    }

    private <T> void setLiveDataValue(MutableLiveData<T> liveData, T value) {
        if (Threads.isMainThread()) {
            liveData.setValue(value);
        } else {
            liveData.postValue(value);
        }
    }
}
