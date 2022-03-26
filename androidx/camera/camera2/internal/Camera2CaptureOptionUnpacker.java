package androidx.camera.camera2.internal;

import android.hardware.camera2.CaptureRequest;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.UseCaseConfig;
/* loaded from: classes.dex */
class Camera2CaptureOptionUnpacker implements CaptureConfig.OptionUnpacker {
    static final Camera2CaptureOptionUnpacker INSTANCE = new Camera2CaptureOptionUnpacker();

    @Override // androidx.camera.core.impl.CaptureConfig.OptionUnpacker
    public void unpack(UseCaseConfig<?> config, CaptureConfig.Builder builder) {
        CaptureConfig defaultCaptureConfig = config.getDefaultCaptureConfig(null);
        Config implOptions = OptionsBundle.emptyBundle();
        int templateType = CaptureConfig.defaultEmptyCaptureConfig().getTemplateType();
        if (defaultCaptureConfig != null) {
            templateType = defaultCaptureConfig.getTemplateType();
            builder.addAllCameraCaptureCallbacks(defaultCaptureConfig.getCameraCaptureCallbacks());
            implOptions = defaultCaptureConfig.getImplementationOptions();
        }
        builder.setImplementationOptions(implOptions);
        Camera2ImplConfig camera2Config = new Camera2ImplConfig(config);
        builder.setTemplateType(camera2Config.getCaptureRequestTemplate(templateType));
        builder.addCameraCaptureCallback(CaptureCallbackContainer.create(camera2Config.getSessionCaptureCallback(Camera2CaptureCallbacks.createNoOpCallback())));
        Camera2ImplConfig.Builder configBuilder = new Camera2ImplConfig.Builder();
        for (Config.Option<?> option : camera2Config.getCaptureRequestOptions()) {
            configBuilder.setCaptureRequestOptionWithPriority((CaptureRequest.Key) option.getToken(), camera2Config.retrieveOption(option), camera2Config.getOptionPriority(option));
        }
        builder.addImplementationOptions(configBuilder.build());
    }
}
