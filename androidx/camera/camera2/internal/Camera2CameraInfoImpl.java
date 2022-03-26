package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
import android.util.Log;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.ZoomState;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraInfoInternal;
import androidx.camera.core.impl.utils.CameraOrientationUtil;
import androidx.core.util.Preconditions;
import androidx.lifecycle.LiveData;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class Camera2CameraInfoImpl implements CameraInfoInternal {
    private static final String TAG = "Camera2CameraInfo";
    private final Camera2CameraControl mCamera2CameraControl;
    private final CameraCharacteristics mCameraCharacteristics;
    private final String mCameraId;
    private final TorchControl mTorchControl;
    private final ZoomControl mZoomControl;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Camera2CameraInfoImpl(String cameraId, CameraCharacteristics cameraCharacteristics, Camera2CameraControl camera2CameraControl) {
        Preconditions.checkNotNull(cameraCharacteristics, "Camera characteristics map is missing");
        this.mCameraId = (String) Preconditions.checkNotNull(cameraId);
        this.mCameraCharacteristics = cameraCharacteristics;
        this.mCamera2CameraControl = camera2CameraControl;
        this.mZoomControl = camera2CameraControl.getZoomControl();
        this.mTorchControl = camera2CameraControl.getTorchControl();
        logDeviceInfo();
    }

    @Override // androidx.camera.core.impl.CameraInfoInternal
    public String getCameraId() {
        return this.mCameraId;
    }

    public CameraCharacteristics getCameraCharacteristics() {
        return this.mCameraCharacteristics;
    }

    @Override // androidx.camera.core.impl.CameraInfoInternal
    public Integer getLensFacing() {
        Integer lensFacing = (Integer) this.mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
        Preconditions.checkNotNull(lensFacing);
        int intValue = lensFacing.intValue();
        if (intValue == 0) {
            return 0;
        }
        if (intValue != 1) {
            return null;
        }
        return 1;
    }

    @Override // androidx.camera.core.CameraInfo
    public int getSensorRotationDegrees(int relativeRotation) {
        Integer sensorOrientation = Integer.valueOf(getSensorOrientation());
        int relativeRotationDegrees = CameraOrientationUtil.surfaceRotationToDegrees(relativeRotation);
        Integer lensFacing = getLensFacing();
        boolean isOppositeFacingScreen = true;
        if (lensFacing == null || 1 != lensFacing.intValue()) {
            isOppositeFacingScreen = false;
        }
        return CameraOrientationUtil.getRelativeImageRotation(relativeRotationDegrees, sensorOrientation.intValue(), isOppositeFacingScreen);
    }

    int getSensorOrientation() {
        Integer sensorOrientation = (Integer) this.mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        Preconditions.checkNotNull(sensorOrientation);
        return sensorOrientation.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSupportedHardwareLevel() {
        Integer deviceLevel = (Integer) this.mCameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        Preconditions.checkNotNull(deviceLevel);
        return deviceLevel.intValue();
    }

    @Override // androidx.camera.core.CameraInfo
    public int getSensorRotationDegrees() {
        return getSensorRotationDegrees(0);
    }

    private void logDeviceInfo() {
        logDeviceLevel();
    }

    private void logDeviceLevel() {
        String levelString;
        int deviceLevel = getSupportedHardwareLevel();
        if (deviceLevel == 0) {
            levelString = "INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED";
        } else if (deviceLevel == 1) {
            levelString = "INFO_SUPPORTED_HARDWARE_LEVEL_FULL";
        } else if (deviceLevel == 2) {
            levelString = "INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY";
        } else if (deviceLevel == 3) {
            levelString = "INFO_SUPPORTED_HARDWARE_LEVEL_3";
        } else if (deviceLevel != 4) {
            levelString = "Unknown value: " + deviceLevel;
        } else {
            levelString = "INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL";
        }
        Log.i(TAG, "Device Level: " + levelString);
    }

    @Override // androidx.camera.core.CameraInfo
    public boolean hasFlashUnit() {
        Boolean hasFlashUnit = (Boolean) this.mCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        Preconditions.checkNotNull(hasFlashUnit);
        return hasFlashUnit.booleanValue();
    }

    @Override // androidx.camera.core.CameraInfo
    public LiveData<Integer> getTorchState() {
        return this.mTorchControl.getTorchState();
    }

    @Override // androidx.camera.core.CameraInfo
    public LiveData<ZoomState> getZoomState() {
        return this.mZoomControl.getZoomState();
    }

    @Override // androidx.camera.core.CameraInfo
    public String getImplementationType() {
        return getSupportedHardwareLevel() == 2 ? CameraInfo.IMPLEMENTATION_TYPE_CAMERA2_LEGACY : CameraInfo.IMPLEMENTATION_TYPE_CAMERA2;
    }

    @Override // androidx.camera.core.impl.CameraInfoInternal
    public void addSessionCaptureCallback(Executor executor, CameraCaptureCallback callback) {
        this.mCamera2CameraControl.addSessionCameraCaptureCallback(executor, callback);
    }

    @Override // androidx.camera.core.impl.CameraInfoInternal
    public void removeSessionCaptureCallback(CameraCaptureCallback callback) {
        this.mCamera2CameraControl.removeSessionCameraCaptureCallback(callback);
    }
}
