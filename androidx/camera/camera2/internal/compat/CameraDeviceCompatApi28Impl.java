package androidx.camera.camera2.internal.compat;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.SessionConfiguration;
import androidx.camera.camera2.internal.compat.params.SessionConfigurationCompat;
import androidx.core.util.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class CameraDeviceCompatApi28Impl extends CameraDeviceCompatApi24Impl {
    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraDeviceCompatApi28Impl(CameraDevice cameraDevice) {
        super((CameraDevice) Preconditions.checkNotNull(cameraDevice), null);
    }

    @Override // androidx.camera.camera2.internal.compat.CameraDeviceCompatApi24Impl, androidx.camera.camera2.internal.compat.CameraDeviceCompatApi23Impl, androidx.camera.camera2.internal.compat.CameraDeviceCompatBaseImpl, androidx.camera.camera2.internal.compat.CameraDeviceCompat.CameraDeviceCompatImpl
    public void createCaptureSession(SessionConfigurationCompat config) throws CameraAccessException {
        SessionConfiguration sessionConfig = (SessionConfiguration) config.unwrap();
        Preconditions.checkNotNull(sessionConfig);
        this.mCameraDevice.createCaptureSession(sessionConfig);
    }
}
