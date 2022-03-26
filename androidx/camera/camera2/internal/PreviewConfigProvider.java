package androidx.camera.camera2.internal;

import android.content.Context;
import android.util.Rational;
import android.view.WindowManager;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.ConfigProvider;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.core.impl.SessionConfig;
/* loaded from: classes.dex */
public final class PreviewConfigProvider implements ConfigProvider<PreviewConfig> {
    private static final String TAG = "PreviewConfigProvider";
    private final WindowManager mWindowManager;

    public PreviewConfigProvider(Context context) {
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    @Override // androidx.camera.core.impl.ConfigProvider
    public PreviewConfig getConfig(CameraInfo cameraInfo) {
        Rational rational;
        Preview.Builder builder = Preview.Builder.fromConfig(Preview.DEFAULT_CONFIG.getConfig(cameraInfo));
        SessionConfig.Builder sessionBuilder = new SessionConfig.Builder();
        boolean isRotateNeeded = true;
        sessionBuilder.setTemplateType(1);
        builder.setDefaultSessionConfig(sessionBuilder.build());
        builder.setSessionOptionUnpacker((SessionConfig.OptionUnpacker) Camera2SessionOptionUnpacker.INSTANCE);
        CaptureConfig.Builder captureBuilder = new CaptureConfig.Builder();
        captureBuilder.setTemplateType(1);
        builder.setDefaultCaptureConfig(captureBuilder.build());
        builder.setCaptureOptionUnpacker((CaptureConfig.OptionUnpacker) Camera2CaptureOptionUnpacker.INSTANCE);
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
