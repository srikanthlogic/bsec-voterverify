package androidx.camera.camera2.interop;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.core.ExtendableBuilder;
import androidx.camera.core.impl.Config;
/* loaded from: classes.dex */
public final class Camera2Interop {

    /* loaded from: classes.dex */
    public static final class Extender<T> {
        ExtendableBuilder<T> mBaseBuilder;

        public Extender(ExtendableBuilder<T> baseBuilder) {
            this.mBaseBuilder = baseBuilder;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <ValueT> Extender<T> setCaptureRequestOption(CaptureRequest.Key<ValueT> key, ValueT value) {
            this.mBaseBuilder.getMutableConfig().insertOption(Camera2ImplConfig.createCaptureRequestOption(key), Config.OptionPriority.ALWAYS_OVERRIDE, value);
            return this;
        }

        public Extender<T> setCaptureRequestTemplate(int templateType) {
            this.mBaseBuilder.getMutableConfig().insertOption(Camera2ImplConfig.TEMPLATE_TYPE_OPTION, Integer.valueOf(templateType));
            return this;
        }

        public Extender<T> setDeviceStateCallback(CameraDevice.StateCallback stateCallback) {
            this.mBaseBuilder.getMutableConfig().insertOption(Camera2ImplConfig.DEVICE_STATE_CALLBACK_OPTION, stateCallback);
            return this;
        }

        public Extender<T> setSessionStateCallback(CameraCaptureSession.StateCallback stateCallback) {
            this.mBaseBuilder.getMutableConfig().insertOption(Camera2ImplConfig.SESSION_STATE_CALLBACK_OPTION, stateCallback);
            return this;
        }

        public Extender<T> setSessionCaptureCallback(CameraCaptureSession.CaptureCallback captureCallback) {
            this.mBaseBuilder.getMutableConfig().insertOption(Camera2ImplConfig.SESSION_CAPTURE_CALLBACK_OPTION, captureCallback);
            return this;
        }
    }

    private Camera2Interop() {
    }
}
