package androidx.camera.view.preview.transform;

import android.util.Pair;
import android.view.View;
import androidx.camera.view.preview.transform.transformation.TranslationTransformation;
/* loaded from: classes.dex */
final class TranslationTransform {
    private TranslationTransform() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TranslationTransformation start(View view, Pair<Float, Float> scaleXY, int deviceRotation) {
        int targetCenterY;
        int targetCenterX;
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            return new TranslationTransformation(0.0f, 0.0f);
        }
        int scaledWidth = (int) (((float) view.getWidth()) * ((Float) scaleXY.first).floatValue());
        int scaledHeight = (int) (((float) view.getHeight()) * ((Float) scaleXY.second).floatValue());
        int viewRotationDegrees = (int) RotationTransform.getRotationDegrees(view, deviceRotation);
        if (viewRotationDegrees == 0 || viewRotationDegrees == 180) {
            targetCenterX = scaledWidth / 2;
            targetCenterY = scaledHeight / 2;
        } else {
            targetCenterX = scaledHeight / 2;
            targetCenterY = scaledWidth / 2;
        }
        return new TranslationTransformation((float) reverseIfRTLLayout(view, targetCenterX - (view.getWidth() / 2)), (float) (targetCenterY - (view.getHeight() / 2)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TranslationTransformation center(View container, View view) {
        return (view.getWidth() == 0 || view.getHeight() == 0) ? new TranslationTransformation(0.0f, 0.0f) : new TranslationTransformation((float) reverseIfRTLLayout(view, (container.getWidth() / 2) - (view.getWidth() / 2)), (float) ((container.getHeight() / 2) - (view.getHeight() / 2)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TranslationTransformation end(View container, View view, Pair<Float, Float> scaleXY, int deviceRotation) {
        int targetCenterY;
        int targetCenterX;
        if (!(view.getWidth() == 0 || view.getHeight() == 0)) {
            int endX = container.getWidth();
            int endY = container.getHeight();
            int scaledWidth = (int) (((float) view.getWidth()) * ((Float) scaleXY.first).floatValue());
            int scaledHeight = (int) (((float) view.getHeight()) * ((Float) scaleXY.second).floatValue());
            int viewRotationDegrees = (int) RotationTransform.getRotationDegrees(view, deviceRotation);
            if (viewRotationDegrees == 0 || viewRotationDegrees == 180) {
                targetCenterX = endX - (scaledWidth / 2);
                targetCenterY = endY - (scaledHeight / 2);
            } else {
                targetCenterX = endX - (scaledHeight / 2);
                targetCenterY = endY - (scaledWidth / 2);
            }
            return new TranslationTransformation((float) reverseIfRTLLayout(view, targetCenterX - (view.getWidth() / 2)), (float) (targetCenterY - (view.getHeight() / 2)));
        }
        return new TranslationTransformation(0.0f, 0.0f);
    }

    private static int reverseIfRTLLayout(View view, int transX) {
        boolean isRTLDirection = true;
        if (view.getLayoutDirection() != 1) {
            isRTLDirection = false;
        }
        return isRTLDirection ? -transX : transX;
    }
}
