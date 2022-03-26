package androidx.camera.view.preview.transform;

import com.facebook.imagepipeline.common.RotationOptions;
/* loaded from: classes.dex */
final class SurfaceRotation {
    private SurfaceRotation() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int rotationDegreesFromSurfaceRotation(int rotationConstant) {
        if (rotationConstant == 0) {
            return 0;
        }
        if (rotationConstant == 1) {
            return 90;
        }
        if (rotationConstant == 2) {
            return RotationOptions.ROTATE_180;
        }
        if (rotationConstant == 3) {
            return 270;
        }
        throw new UnsupportedOperationException("Unsupported surface rotation constant: " + rotationConstant);
    }
}
