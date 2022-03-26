package androidx.camera.camera2.internal;

import android.hardware.camera2.CaptureResult;
import android.util.Log;
import androidx.camera.core.impl.CameraCaptureMetaData;
import androidx.camera.core.impl.CameraCaptureResult;
/* loaded from: classes.dex */
public class Camera2CameraCaptureResult implements CameraCaptureResult {
    private static final String TAG = "C2CameraCaptureResult";
    private final CaptureResult mCaptureResult;
    private final Object mTag;

    public Camera2CameraCaptureResult(Object tag, CaptureResult captureResult) {
        this.mTag = tag;
        this.mCaptureResult = captureResult;
    }

    @Override // androidx.camera.core.impl.CameraCaptureResult
    public CameraCaptureMetaData.AfMode getAfMode() {
        Integer mode = (Integer) this.mCaptureResult.get(CaptureResult.CONTROL_AF_MODE);
        if (mode == null) {
            return CameraCaptureMetaData.AfMode.UNKNOWN;
        }
        int intValue = mode.intValue();
        if (intValue != 0) {
            if (intValue == 1 || intValue == 2) {
                return CameraCaptureMetaData.AfMode.ON_MANUAL_AUTO;
            }
            if (intValue == 3 || intValue == 4) {
                return CameraCaptureMetaData.AfMode.ON_CONTINUOUS_AUTO;
            }
            if (intValue != 5) {
                Log.e(TAG, "Undefined af mode: " + mode);
                return CameraCaptureMetaData.AfMode.UNKNOWN;
            }
        }
        return CameraCaptureMetaData.AfMode.OFF;
    }

    @Override // androidx.camera.core.impl.CameraCaptureResult
    public CameraCaptureMetaData.AfState getAfState() {
        Integer state = (Integer) this.mCaptureResult.get(CaptureResult.CONTROL_AF_STATE);
        if (state == null) {
            return CameraCaptureMetaData.AfState.UNKNOWN;
        }
        switch (state.intValue()) {
            case 0:
                return CameraCaptureMetaData.AfState.INACTIVE;
            case 1:
            case 3:
            case 6:
                return CameraCaptureMetaData.AfState.SCANNING;
            case 2:
                return CameraCaptureMetaData.AfState.FOCUSED;
            case 4:
                return CameraCaptureMetaData.AfState.LOCKED_FOCUSED;
            case 5:
                return CameraCaptureMetaData.AfState.LOCKED_NOT_FOCUSED;
            default:
                Log.e(TAG, "Undefined af state: " + state);
                return CameraCaptureMetaData.AfState.UNKNOWN;
        }
    }

    @Override // androidx.camera.core.impl.CameraCaptureResult
    public CameraCaptureMetaData.AeState getAeState() {
        Integer state = (Integer) this.mCaptureResult.get(CaptureResult.CONTROL_AE_STATE);
        if (state == null) {
            return CameraCaptureMetaData.AeState.UNKNOWN;
        }
        int intValue = state.intValue();
        if (intValue == 0) {
            return CameraCaptureMetaData.AeState.INACTIVE;
        }
        if (intValue != 1) {
            if (intValue == 2) {
                return CameraCaptureMetaData.AeState.CONVERGED;
            }
            if (intValue == 3) {
                return CameraCaptureMetaData.AeState.LOCKED;
            }
            if (intValue == 4) {
                return CameraCaptureMetaData.AeState.FLASH_REQUIRED;
            }
            if (intValue != 5) {
                Log.e(TAG, "Undefined ae state: " + state);
                return CameraCaptureMetaData.AeState.UNKNOWN;
            }
        }
        return CameraCaptureMetaData.AeState.SEARCHING;
    }

    @Override // androidx.camera.core.impl.CameraCaptureResult
    public CameraCaptureMetaData.AwbState getAwbState() {
        Integer state = (Integer) this.mCaptureResult.get(CaptureResult.CONTROL_AWB_STATE);
        if (state == null) {
            return CameraCaptureMetaData.AwbState.UNKNOWN;
        }
        int intValue = state.intValue();
        if (intValue == 0) {
            return CameraCaptureMetaData.AwbState.INACTIVE;
        }
        if (intValue == 1) {
            return CameraCaptureMetaData.AwbState.METERING;
        }
        if (intValue == 2) {
            return CameraCaptureMetaData.AwbState.CONVERGED;
        }
        if (intValue == 3) {
            return CameraCaptureMetaData.AwbState.LOCKED;
        }
        Log.e(TAG, "Undefined awb state: " + state);
        return CameraCaptureMetaData.AwbState.UNKNOWN;
    }

    @Override // androidx.camera.core.impl.CameraCaptureResult
    public CameraCaptureMetaData.FlashState getFlashState() {
        Integer state = (Integer) this.mCaptureResult.get(CaptureResult.FLASH_STATE);
        if (state == null) {
            return CameraCaptureMetaData.FlashState.UNKNOWN;
        }
        int intValue = state.intValue();
        if (intValue == 0 || intValue == 1) {
            return CameraCaptureMetaData.FlashState.NONE;
        }
        if (intValue == 2) {
            return CameraCaptureMetaData.FlashState.READY;
        }
        if (intValue == 3 || intValue == 4) {
            return CameraCaptureMetaData.FlashState.FIRED;
        }
        Log.e(TAG, "Undefined flash state: " + state);
        return CameraCaptureMetaData.FlashState.UNKNOWN;
    }

    @Override // androidx.camera.core.impl.CameraCaptureResult
    public long getTimestamp() {
        Long timestamp = (Long) this.mCaptureResult.get(CaptureResult.SENSOR_TIMESTAMP);
        if (timestamp == null) {
            return -1;
        }
        return timestamp.longValue();
    }

    @Override // androidx.camera.core.impl.CameraCaptureResult
    public Object getTag() {
        return this.mTag;
    }

    public CaptureResult getCaptureResult() {
        return this.mCaptureResult;
    }
}
