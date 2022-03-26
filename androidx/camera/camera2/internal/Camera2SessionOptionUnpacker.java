package androidx.camera.camera2.internal;

import android.hardware.camera2.CaptureRequest;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.camera2.impl.CameraEventCallbacks;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfig;
/* loaded from: classes.dex */
public final class Camera2SessionOptionUnpacker implements SessionConfig.OptionUnpacker {
    static final Camera2SessionOptionUnpacker INSTANCE = new Camera2SessionOptionUnpacker();

    @Override // androidx.camera.core.impl.SessionConfig.OptionUnpacker
    public void unpack(UseCaseConfig<?> config, SessionConfig.Builder builder) {
        SessionConfig defaultSessionConfig = config.getDefaultSessionConfig(null);
        Config implOptions = OptionsBundle.emptyBundle();
        int templateType = SessionConfig.defaultEmptySessionConfig().getTemplateType();
        if (defaultSessionConfig != null) {
            templateType = defaultSessionConfig.getTemplateType();
            builder.addAllDeviceStateCallbacks(defaultSessionConfig.getDeviceStateCallbacks());
            builder.addAllSessionStateCallbacks(defaultSessionConfig.getSessionStateCallbacks());
            builder.addAllRepeatingCameraCaptureCallbacks(defaultSessionConfig.getRepeatingCameraCaptureCallbacks());
            implOptions = defaultSessionConfig.getImplementationOptions();
        }
        builder.setImplementationOptions(implOptions);
        Camera2ImplConfig camera2Config = new Camera2ImplConfig(config);
        builder.setTemplateType(camera2Config.getCaptureRequestTemplate(templateType));
        builder.addDeviceStateCallback(camera2Config.getDeviceStateCallback(CameraDeviceStateCallbacks.createNoOpCallback()));
        builder.addSessionStateCallback(camera2Config.getSessionStateCallback(CameraCaptureSessionStateCallbacks.createNoOpCallback()));
        builder.addCameraCaptureCallback(CaptureCallbackContainer.create(camera2Config.getSessionCaptureCallback(Camera2CaptureCallbacks.createNoOpCallback())));
        MutableOptionsBundle cameraEventConfig = MutableOptionsBundle.create();
        cameraEventConfig.insertOption(Camera2ImplConfig.CAMERA_EVENT_CALLBACK_OPTION, camera2Config.getCameraEventCallback(CameraEventCallbacks.createEmptyCallback()));
        builder.addImplementationOptions(cameraEventConfig);
        Camera2ImplConfig.Builder configBuilder = new Camera2ImplConfig.Builder();
        for (Config.Option<?> option : camera2Config.getCaptureRequestOptions()) {
            configBuilder.setCaptureRequestOptionWithPriority((CaptureRequest.Key) option.getToken(), camera2Config.retrieveOption(option), camera2Config.getOptionPriority(option));
        }
        builder.addImplementationOptions(configBuilder.build());
    }
}
