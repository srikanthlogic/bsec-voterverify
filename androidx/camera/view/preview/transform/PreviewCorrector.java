package androidx.camera.view.preview.transform;

import android.util.Pair;
import android.util.Size;
import android.view.View;
import androidx.camera.view.preview.transform.transformation.PreviewCorrectionTransformation;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class PreviewCorrector {
    private PreviewCorrector() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PreviewCorrectionTransformation getCorrectionTransformation(View container, View preview, Size bufferSize, boolean sensorDimensionFlipNeeded, int deviceRotation) {
        Pair<Float, Float> scaleXY = getCorrectionScale(container, preview, bufferSize, sensorDimensionFlipNeeded);
        return new PreviewCorrectionTransformation(((Float) scaleXY.first).floatValue(), ((Float) scaleXY.second).floatValue(), (float) (-((int) RotationTransform.getRotationDegrees(preview, deviceRotation))));
    }

    private static Pair<Float, Float> getCorrectionScale(View container, View preview, Size bufferSize, boolean sensorDimensionFlipNeeded) {
        int bufferHeight;
        int bufferWidth;
        if (container.getWidth() == 0 || container.getHeight() == 0 || preview.getWidth() == 0 || preview.getHeight() == 0 || bufferSize.getWidth() == 0 || bufferSize.getHeight() == 0) {
            return new Pair<>(Float.valueOf(1.0f), Float.valueOf(1.0f));
        }
        if (sensorDimensionFlipNeeded) {
            bufferWidth = bufferSize.getHeight();
            bufferHeight = bufferSize.getWidth();
        } else {
            bufferWidth = bufferSize.getWidth();
            bufferHeight = bufferSize.getHeight();
        }
        return new Pair<>(Float.valueOf(((float) bufferWidth) / ((float) preview.getWidth())), Float.valueOf(((float) bufferHeight) / ((float) preview.getHeight())));
    }
}
