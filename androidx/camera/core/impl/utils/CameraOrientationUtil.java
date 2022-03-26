package androidx.camera.core.impl.utils;

import android.util.Log;
import com.facebook.imagepipeline.common.RotationOptions;
/* loaded from: classes.dex */
public final class CameraOrientationUtil {
    private static final String TAG = "CameraOrientationUtil";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    private CameraOrientationUtil() {
    }

    public static int getRelativeImageRotation(int destRotationDegrees, int sourceRotationDegrees, boolean isOppositeFacing) {
        int result;
        if (isOppositeFacing) {
            result = ((sourceRotationDegrees - destRotationDegrees) + 360) % 360;
        } else {
            result = (sourceRotationDegrees + destRotationDegrees) % 360;
        }
        if (DEBUG) {
            Log.d(TAG, String.format("getRelativeImageRotation: destRotationDegrees=%s, sourceRotationDegrees=%s, isOppositeFacing=%s, result=%s", Integer.valueOf(destRotationDegrees), Integer.valueOf(sourceRotationDegrees), Boolean.valueOf(isOppositeFacing), Integer.valueOf(result)));
        }
        return result;
    }

    public static int surfaceRotationToDegrees(int rotationEnum) {
        if (rotationEnum == 0) {
            return 0;
        }
        if (rotationEnum == 1) {
            return 90;
        }
        if (rotationEnum == 2) {
            return RotationOptions.ROTATE_180;
        }
        if (rotationEnum == 3) {
            return 270;
        }
        throw new IllegalArgumentException("Unsupported surface rotation: " + rotationEnum);
    }

    public static int degreesToSurfaceRotation(int degrees) {
        if (degrees == 0) {
            return 0;
        }
        if (degrees == 90) {
            return 1;
        }
        if (degrees == 180) {
            return 2;
        }
        if (degrees == 270) {
            return 3;
        }
        throw new IllegalStateException("Invalid sensor rotation: " + degrees);
    }
}
