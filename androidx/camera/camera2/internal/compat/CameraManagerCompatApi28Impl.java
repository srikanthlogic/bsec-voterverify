package androidx.camera.camera2.internal.compat;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class CameraManagerCompatApi28Impl extends CameraManagerCompatBaseImpl {
    public CameraManagerCompatApi28Impl(Context context, Object cameraManagerParams) {
        super(context, cameraManagerParams);
    }

    public static CameraManagerCompatApi28Impl create(Context context) {
        return new CameraManagerCompatApi28Impl(context, new CameraManagerCompatParamsApi28());
    }

    @Override // androidx.camera.camera2.internal.compat.CameraManagerCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraManagerCompat.CameraManagerCompatImpl
    public void registerAvailabilityCallback(Executor executor, CameraManager.AvailabilityCallback callback) {
        this.mCameraManager.registerAvailabilityCallback(executor, callback);
    }

    @Override // androidx.camera.camera2.internal.compat.CameraManagerCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraManagerCompat.CameraManagerCompatImpl
    public void unregisterAvailabilityCallback(CameraManager.AvailabilityCallback callback) {
        this.mCameraManager.unregisterAvailabilityCallback(callback);
    }

    @Override // androidx.camera.camera2.internal.compat.CameraManagerCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraManagerCompat.CameraManagerCompatImpl
    public void openCamera(String cameraId, Executor executor, CameraDevice.StateCallback callback) throws CameraAccessExceptionCompat {
        try {
            this.mCameraManager.openCamera(cameraId, executor, callback);
        } catch (CameraAccessException e) {
            throw CameraAccessExceptionCompat.toCameraAccessExceptionCompat(e);
        } catch (IllegalArgumentException e2) {
            throw e2;
        } catch (SecurityException e3) {
        } catch (RuntimeException e4) {
            if (isDndFailCase(e4)) {
                throwDndException(e4);
            }
            throw e4;
        }
    }

    @Override // androidx.camera.camera2.internal.compat.CameraManagerCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraManagerCompat.CameraManagerCompatImpl
    public CameraCharacteristics getCameraCharacteristics(String cameraId) throws CameraAccessExceptionCompat {
        try {
            CameraCharacteristics cameraCharacteristics = super.getCameraCharacteristics(cameraId);
            if (Build.VERSION.SDK_INT == 28) {
                synchronized (CameraManagerCompatParamsApi28.sCameraCharacteristicsMap) {
                    if (!CameraManagerCompatParamsApi28.sCameraCharacteristicsMap.containsKey(cameraId)) {
                        CameraManagerCompatParamsApi28.sCameraCharacteristicsMap.put(cameraId, cameraCharacteristics);
                    }
                }
            }
            return cameraCharacteristics;
        } catch (RuntimeException e) {
            if (isDndFailCase(e)) {
                synchronized (CameraManagerCompatParamsApi28.sCameraCharacteristicsMap) {
                    if (CameraManagerCompatParamsApi28.sCameraCharacteristicsMap.containsKey(cameraId)) {
                        return CameraManagerCompatParamsApi28.sCameraCharacteristicsMap.get(cameraId);
                    }
                    throwDndException(e);
                }
            }
            throw e;
        }
    }

    private void throwDndException(Throwable cause) throws CameraAccessExceptionCompat {
        throw new CameraAccessExceptionCompat((int) CameraAccessExceptionCompat.CAMERA_UNAVAILABLE_DO_NOT_DISTURB, cause);
    }

    private boolean isDndFailCase(Throwable throwable) {
        return Build.VERSION.SDK_INT == 28 && isDndRuntimeException(throwable);
    }

    private static boolean isDndRuntimeException(Throwable throwable) {
        StackTraceElement[] stackTraceElement;
        if (!throwable.getClass().equals(RuntimeException.class) || (stackTraceElement = throwable.getStackTrace()) == null || stackTraceElement.length < 0) {
            return false;
        }
        return "_enableShutterSound".equals(stackTraceElement[0].getMethodName());
    }

    /* loaded from: classes.dex */
    public static final class CameraManagerCompatParamsApi28 {
        static final Map<String, CameraCharacteristics> sCameraCharacteristicsMap = new HashMap();

        CameraManagerCompatParamsApi28() {
        }
    }
}
