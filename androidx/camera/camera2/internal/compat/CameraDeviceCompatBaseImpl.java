package androidx.camera.camera2.internal.compat;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import androidx.camera.camera2.internal.compat.CameraCaptureSessionCompat;
import androidx.camera.camera2.internal.compat.CameraDeviceCompat;
import androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat;
import androidx.camera.camera2.internal.compat.params.SessionConfigurationCompat;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class CameraDeviceCompatBaseImpl implements CameraDeviceCompat.CameraDeviceCompatImpl {
    final CameraDevice mCameraDevice;
    final Object mImplParams;

    public CameraDeviceCompatBaseImpl(CameraDevice cameraDevice, Object implParams) {
        this.mCameraDevice = (CameraDevice) Preconditions.checkNotNull(cameraDevice);
        this.mImplParams = implParams;
    }

    public static CameraDeviceCompatBaseImpl create(CameraDevice cameraDevice, Handler compatHandler) {
        return new CameraDeviceCompatBaseImpl(cameraDevice, new CameraDeviceCompatParamsApi21(compatHandler));
    }

    static List<Surface> unpackSurfaces(List<OutputConfigurationCompat> outputConfigs) {
        List<Surface> surfaces = new ArrayList<>(outputConfigs.size());
        for (OutputConfigurationCompat outputConfig : outputConfigs) {
            surfaces.add(outputConfig.getSurface());
        }
        return surfaces;
    }

    static void checkPreconditions(CameraDevice device, SessionConfigurationCompat config) {
        Preconditions.checkNotNull(device);
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(config.getStateCallback());
        List<OutputConfigurationCompat> outputConfigs = config.getOutputConfigurations();
        if (outputConfigs == null) {
            throw new IllegalArgumentException("Invalid output configurations");
        } else if (config.getExecutor() != null) {
            checkPhysicalCameraIdValid(device, outputConfigs);
        } else {
            throw new IllegalArgumentException("Invalid executor");
        }
    }

    private static void checkPhysicalCameraIdValid(CameraDevice device, List<OutputConfigurationCompat> outputConfigs) {
        String cameraId = device.getId();
        for (OutputConfigurationCompat outputConfigurationCompat : outputConfigs) {
            String outputConfigPhysicalId = outputConfigurationCompat.getPhysicalCameraId();
            if (outputConfigPhysicalId != null && !outputConfigPhysicalId.isEmpty()) {
                Log.w("CameraDeviceCompat", "Camera " + cameraId + ": Camera doesn't support physicalCameraId " + outputConfigPhysicalId + ". Ignoring.");
            }
        }
    }

    void createBaseCaptureSession(CameraDevice device, List<Surface> surfaces, CameraCaptureSession.StateCallback cb, Handler handler) throws CameraAccessException {
        device.createCaptureSession(surfaces, cb, handler);
    }

    @Override // androidx.camera.camera2.internal.compat.CameraDeviceCompat.CameraDeviceCompatImpl
    public void createCaptureSession(SessionConfigurationCompat config) throws CameraAccessException {
        checkPreconditions(this.mCameraDevice, config);
        if (config.getInputConfiguration() != null) {
            throw new IllegalArgumentException("Reprocessing sessions not supported until API 23");
        } else if (config.getSessionType() != 1) {
            CameraCaptureSession.StateCallback cb = new CameraCaptureSessionCompat.StateCallbackExecutorWrapper(config.getExecutor(), config.getStateCallback());
            createBaseCaptureSession(this.mCameraDevice, unpackSurfaces(config.getOutputConfigurations()), cb, ((CameraDeviceCompatParamsApi21) this.mImplParams).mCompatHandler);
        } else {
            throw new IllegalArgumentException("High speed capture sessions not supported until API 23");
        }
    }

    @Override // androidx.camera.camera2.internal.compat.CameraDeviceCompat.CameraDeviceCompatImpl
    public CameraDevice unwrap() {
        return this.mCameraDevice;
    }

    /* loaded from: classes.dex */
    public static class CameraDeviceCompatParamsApi21 {
        final Handler mCompatHandler;

        public CameraDeviceCompatParamsApi21(Handler compatHandler) {
            this.mCompatHandler = compatHandler;
        }
    }
}
