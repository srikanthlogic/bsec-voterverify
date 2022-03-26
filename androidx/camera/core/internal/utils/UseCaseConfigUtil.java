package androidx.camera.core.internal.utils;

import android.util.Rational;
import android.util.Size;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.utils.CameraOrientationUtil;
import com.facebook.imagepipeline.common.RotationOptions;
/* loaded from: classes.dex */
public final class UseCaseConfigUtil {
    private UseCaseConfigUtil() {
    }

    public static void updateTargetRotationAndRelatedConfigs(UseCaseConfig.Builder<?, ?, ?> builder, int newRotation) {
        ImageOutputConfig config = (ImageOutputConfig) builder.getUseCaseConfig();
        int oldRotation = config.getTargetRotation(-1);
        if (oldRotation == -1 || oldRotation != newRotation) {
            ((ImageOutputConfig.Builder) builder).setTargetRotation(newRotation);
        }
        if (oldRotation != -1 && newRotation != -1 && oldRotation != newRotation) {
            if (Math.abs(CameraOrientationUtil.surfaceRotationToDegrees(newRotation) - CameraOrientationUtil.surfaceRotationToDegrees(oldRotation)) % RotationOptions.ROTATE_180 == 90) {
                Size targetResolution = config.getTargetResolution(null);
                Rational targetAspectRatioCustom = config.getTargetAspectRatioCustom(null);
                if (targetResolution != null) {
                    ((ImageOutputConfig.Builder) builder).setTargetResolution(new Size(targetResolution.getHeight(), targetResolution.getWidth()));
                }
                if (targetAspectRatioCustom != null) {
                    ((ImageOutputConfig.Builder) builder).setTargetAspectRatioCustom(new Rational(targetAspectRatioCustom.getDenominator(), targetAspectRatioCustom.getNumerator()));
                }
            }
        }
    }
}
