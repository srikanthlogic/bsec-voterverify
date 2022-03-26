package androidx.camera.camera2.impl;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import androidx.camera.core.ExtendableBuilder;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.MutableConfig;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.ReadableConfig;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public final class Camera2ImplConfig implements ReadableConfig {
    public static final String CAPTURE_REQUEST_ID_STEM;
    private final Config mConfig;
    public static final Config.Option<Integer> TEMPLATE_TYPE_OPTION = Config.Option.create("camera2.captureRequest.templateType", Integer.TYPE);
    public static final Config.Option<CameraDevice.StateCallback> DEVICE_STATE_CALLBACK_OPTION = Config.Option.create("camera2.cameraDevice.stateCallback", CameraDevice.StateCallback.class);
    public static final Config.Option<CameraCaptureSession.StateCallback> SESSION_STATE_CALLBACK_OPTION = Config.Option.create("camera2.cameraCaptureSession.stateCallback", CameraCaptureSession.StateCallback.class);
    public static final Config.Option<CameraCaptureSession.CaptureCallback> SESSION_CAPTURE_CALLBACK_OPTION = Config.Option.create("camera2.cameraCaptureSession.captureCallback", CameraCaptureSession.CaptureCallback.class);
    public static final Config.Option<CameraEventCallbacks> CAMERA_EVENT_CALLBACK_OPTION = Config.Option.create("camera2.cameraEvent.callback", CameraEventCallbacks.class);

    public Camera2ImplConfig(Config config) {
        this.mConfig = config;
    }

    public static Config.Option<Object> createCaptureRequestOption(CaptureRequest.Key<?> key) {
        return Config.Option.create(CAPTURE_REQUEST_ID_STEM + key.getName(), Object.class, key);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <ValueT> ValueT getCaptureRequestOption(CaptureRequest.Key<ValueT> key, ValueT valueIfMissing) {
        return (ValueT) this.mConfig.retrieveOption(createCaptureRequestOption(key), valueIfMissing);
    }

    public Set<Config.Option<?>> getCaptureRequestOptions() {
        final Set<Config.Option<?>> optionSet = new HashSet<>();
        findOptions(CAPTURE_REQUEST_ID_STEM, new Config.OptionMatcher() { // from class: androidx.camera.camera2.impl.Camera2ImplConfig.1
            @Override // androidx.camera.core.impl.Config.OptionMatcher
            public boolean onOptionMatched(Config.Option<?> option) {
                optionSet.add(option);
                return true;
            }
        });
        return optionSet;
    }

    public int getCaptureRequestTemplate(int valueIfMissing) {
        return ((Integer) this.mConfig.retrieveOption(TEMPLATE_TYPE_OPTION, Integer.valueOf(valueIfMissing))).intValue();
    }

    public CameraDevice.StateCallback getDeviceStateCallback(CameraDevice.StateCallback valueIfMissing) {
        return (CameraDevice.StateCallback) this.mConfig.retrieveOption(DEVICE_STATE_CALLBACK_OPTION, valueIfMissing);
    }

    public CameraCaptureSession.StateCallback getSessionStateCallback(CameraCaptureSession.StateCallback valueIfMissing) {
        return (CameraCaptureSession.StateCallback) this.mConfig.retrieveOption(SESSION_STATE_CALLBACK_OPTION, valueIfMissing);
    }

    public CameraCaptureSession.CaptureCallback getSessionCaptureCallback(CameraCaptureSession.CaptureCallback valueIfMissing) {
        return (CameraCaptureSession.CaptureCallback) this.mConfig.retrieveOption(SESSION_CAPTURE_CALLBACK_OPTION, valueIfMissing);
    }

    public CameraEventCallbacks getCameraEventCallback(CameraEventCallbacks valueIfMissing) {
        return (CameraEventCallbacks) this.mConfig.retrieveOption(CAMERA_EVENT_CALLBACK_OPTION, valueIfMissing);
    }

    @Override // androidx.camera.core.impl.ReadableConfig
    public Config getConfig() {
        return this.mConfig;
    }

    /* loaded from: classes.dex */
    public static final class Builder implements ExtendableBuilder<Camera2ImplConfig> {
        private final MutableOptionsBundle mMutableOptionsBundle = MutableOptionsBundle.create();

        @Override // androidx.camera.core.ExtendableBuilder
        public MutableConfig getMutableConfig() {
            return this.mMutableOptionsBundle;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <ValueT> Builder setCaptureRequestOption(CaptureRequest.Key<ValueT> key, ValueT value) {
            this.mMutableOptionsBundle.insertOption(Camera2ImplConfig.createCaptureRequestOption(key), value);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <ValueT> Builder setCaptureRequestOptionWithPriority(CaptureRequest.Key<ValueT> key, ValueT value, Config.OptionPriority priority) {
            this.mMutableOptionsBundle.insertOption(Camera2ImplConfig.createCaptureRequestOption(key), priority, value);
            return this;
        }

        public Builder insertAllOptions(Config config) {
            for (Config.Option<?> option : config.listOptions()) {
                this.mMutableOptionsBundle.insertOption(option, config.retrieveOption(option));
            }
            return this;
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public Camera2ImplConfig build() {
            return new Camera2ImplConfig(OptionsBundle.from(this.mMutableOptionsBundle));
        }
    }

    /* loaded from: classes.dex */
    public static final class Extender<T> {
        ExtendableBuilder<T> mBaseBuilder;

        public Extender(ExtendableBuilder<T> baseBuilder) {
            this.mBaseBuilder = baseBuilder;
        }

        public Extender<T> setCameraEventCallback(CameraEventCallbacks cameraEventCallbacks) {
            this.mBaseBuilder.getMutableConfig().insertOption(Camera2ImplConfig.CAMERA_EVENT_CALLBACK_OPTION, cameraEventCallbacks);
            return this;
        }
    }
}
