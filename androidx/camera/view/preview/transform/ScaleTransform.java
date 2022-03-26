package androidx.camera.view.preview.transform;

import android.view.View;
import androidx.camera.view.preview.transform.transformation.ScaleTransformation;
/* loaded from: classes.dex */
final class ScaleTransform {

    /* loaded from: classes.dex */
    public interface FloatBiFunction {
        float apply(float f, float f2);
    }

    private ScaleTransform() {
    }

    public static ScaleTransformation fill(View container, View view, int deviceRotation) {
        return computeScale(container, view, $$Lambda$ScaleTransform$d3P8984_eeiWSAQFGMGoCy9GDSw.INSTANCE, deviceRotation);
    }

    public static ScaleTransformation fit(View container, View view, int deviceRotation) {
        return computeScale(container, view, $$Lambda$ScaleTransform$lwPIhAW7iIqiik5EZ9KZtbZQIA.INSTANCE, deviceRotation);
    }

    private static ScaleTransformation computeScale(View container, View view, FloatBiFunction function, int deviceRotation) {
        float bufferRotatedHeight;
        float bufferRotatedWidth;
        if (container.getWidth() == 0 || container.getHeight() == 0 || view.getWidth() == 0 || view.getHeight() == 0) {
            return new ScaleTransformation(1.0f);
        }
        int rotationDegrees = (int) RotationTransform.getRotationDegrees(view, deviceRotation);
        if (rotationDegrees == 0 || rotationDegrees == 180) {
            bufferRotatedWidth = ((float) view.getWidth()) * view.getScaleX();
            bufferRotatedHeight = ((float) view.getHeight()) * view.getScaleY();
        } else {
            bufferRotatedWidth = ((float) view.getHeight()) * view.getScaleY();
            bufferRotatedHeight = ((float) view.getWidth()) * view.getScaleX();
        }
        return new ScaleTransformation(function.apply(((float) container.getWidth()) / bufferRotatedWidth, ((float) container.getHeight()) / bufferRotatedHeight));
    }
}
