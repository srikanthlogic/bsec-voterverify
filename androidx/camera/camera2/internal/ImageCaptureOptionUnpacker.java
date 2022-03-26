package androidx.camera.camera2.internal;

import android.hardware.camera2.CaptureRequest;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.DeviceProperties;
import androidx.camera.core.impl.ImageCaptureConfig;
import androidx.camera.core.impl.UseCaseConfig;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ImageCaptureOptionUnpacker extends Camera2CaptureOptionUnpacker {
    static final ImageCaptureOptionUnpacker INSTANCE = new ImageCaptureOptionUnpacker();
    private DeviceProperties mDeviceProperties = DeviceProperties.create();

    ImageCaptureOptionUnpacker() {
    }

    @Override // androidx.camera.camera2.internal.Camera2CaptureOptionUnpacker, androidx.camera.core.impl.CaptureConfig.OptionUnpacker
    public void unpack(UseCaseConfig<?> config, CaptureConfig.Builder builder) {
        super.unpack(config, builder);
        if (config instanceof ImageCaptureConfig) {
            ImageCaptureConfig imageCaptureConfig = (ImageCaptureConfig) config;
            Camera2ImplConfig.Builder camera2ConfigBuilder = new Camera2ImplConfig.Builder();
            if (imageCaptureConfig.hasCaptureMode()) {
                applyPixelHdrPlusChangeForCaptureMode(imageCaptureConfig.getCaptureMode(), camera2ConfigBuilder);
            }
            builder.addImplementationOptions(camera2ConfigBuilder.build());
            return;
        }
        throw new IllegalArgumentException("config is not ImageCaptureConfig");
    }

    void setDeviceProperty(DeviceProperties deviceProperties) {
        this.mDeviceProperties = deviceProperties;
    }

    private void applyPixelHdrPlusChangeForCaptureMode(int captureMode, Camera2ImplConfig.Builder builder) {
        if (!"Google".equals(this.mDeviceProperties.manufacturer())) {
            return;
        }
        if ((!"Pixel 2".equals(this.mDeviceProperties.model()) && !"Pixel 3".equals(this.mDeviceProperties.model())) || this.mDeviceProperties.sdkVersion() < 26) {
            return;
        }
        if (captureMode == 0) {
            builder.setCaptureRequestOption(CaptureRequest.CONTROL_ENABLE_ZSL, true);
        } else if (captureMode == 1) {
            builder.setCaptureRequestOption(CaptureRequest.CONTROL_ENABLE_ZSL, false);
        }
    }
}
