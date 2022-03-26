package androidx.camera.camera2.internal;

import android.content.Context;
import android.util.Rational;
import android.view.WindowManager;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.ConfigProvider;
import androidx.camera.core.impl.ImageCaptureConfig;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.SessionConfig;
/* loaded from: classes.dex */
public final class ImageCaptureConfigProvider implements ConfigProvider<ImageCaptureConfig> {
    private static final String TAG = "ImageCaptureProvider";
    private final WindowManager mWindowManager;

    public ImageCaptureConfigProvider(Context context) {
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    @Override // androidx.camera.core.impl.ConfigProvider
    public ImageCaptureConfig getConfig(CameraInfo cameraInfo) {
        Rational rational;
        ImageCapture.Builder builder = ImageCapture.Builder.fromConfig(ImageCapture.DEFAULT_CONFIG.getConfig(cameraInfo));
        SessionConfig.Builder sessionBuilder = new SessionConfig.Builder();
        boolean isRotateNeeded = true;
        sessionBuilder.setTemplateType(1);
        builder.setDefaultSessionConfig(sessionBuilder.build());
        builder.setSessionOptionUnpacker((SessionConfig.OptionUnpacker) Camera2SessionOptionUnpacker.INSTANCE);
        CaptureConfig.Builder captureConfig = new CaptureConfig.Builder();
        captureConfig.setTemplateType(2);
        builder.setDefaultCaptureConfig(captureConfig.build());
        builder.setCaptureOptionUnpacker((CaptureConfig.OptionUnpacker) ImageCaptureOptionUnpacker.INSTANCE);
        int targetRotation = this.mWindowManager.getDefaultDisplay().getRotation();
        builder.setTargetRotation(targetRotation);
        if (cameraInfo != null) {
            int rotationDegrees = cameraInfo.getSensorRotationDegrees(targetRotation);
            if (!(rotationDegrees == 90 || rotationDegrees == 270)) {
                isRotateNeeded = false;
            }
            if (isRotateNeeded) {
                rational = ImageOutputConfig.DEFAULT_ASPECT_RATIO_PORTRAIT;
            } else {
                rational = ImageOutputConfig.DEFAULT_ASPECT_RATIO_LANDSCAPE;
            }
            builder.setTargetAspectRatioCustom(rational);
        }
        return builder.getUseCaseConfig();
    }
}
