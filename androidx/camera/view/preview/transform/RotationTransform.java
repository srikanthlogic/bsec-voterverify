package androidx.camera.view.preview.transform;

import android.view.Display;
import android.view.View;
/* loaded from: classes.dex */
final class RotationTransform {
    static final int ROTATION_AUTOMATIC = -1;

    private RotationTransform() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float getRotationDegrees(View view, int deviceRotation) {
        if (deviceRotation != -1) {
            return (float) SurfaceRotation.rotationDegreesFromSurfaceRotation(deviceRotation);
        }
        return getRotationDegrees(view);
    }

    static float getRotationDegrees(View view) {
        Display display = view.getDisplay();
        if (display == null) {
            return 0.0f;
        }
        return (float) SurfaceRotation.rotationDegreesFromSurfaceRotation(display.getRotation());
    }
}
