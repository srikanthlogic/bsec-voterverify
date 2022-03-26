package androidx.camera.core.internal;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Rational;
import android.util.Size;
import androidx.camera.core.UseCase;
import androidx.camera.core.internal.utils.ImageUtil;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class ViewPorts {
    private ViewPorts() {
    }

    public static Map<UseCase, Rect> calculateViewPortRects(Rect fullSensorRect, Rational viewPortAspectRatio, int outputRotationDegrees, int scaleType, int layoutDirection, Map<UseCase, Size> useCaseSizes) {
        Rational rotatedViewPortAspectRatio = ImageUtil.getRotatedAspectRatio(outputRotationDegrees, viewPortAspectRatio);
        RectF fullSensorRectF = new RectF(fullSensorRect);
        Map<UseCase, Matrix> useCaseToSensorTransformations = new HashMap<>();
        RectF sensorIntersectionRect = new RectF(fullSensorRect);
        for (Map.Entry<UseCase, Size> entry : useCaseSizes.entrySet()) {
            Matrix useCaseToSensorTransformation = new Matrix();
            RectF srcRect = new RectF(0.0f, 0.0f, (float) entry.getValue().getWidth(), (float) entry.getValue().getHeight());
            useCaseToSensorTransformation.setRectToRect(srcRect, fullSensorRectF, Matrix.ScaleToFit.CENTER);
            useCaseToSensorTransformations.put(entry.getKey(), useCaseToSensorTransformation);
            RectF useCaseSensorRect = new RectF();
            useCaseToSensorTransformation.mapRect(useCaseSensorRect, srcRect);
            sensorIntersectionRect.intersect(useCaseSensorRect);
        }
        RectF viewPortRect = getScaledRect(sensorIntersectionRect, rotatedViewPortAspectRatio, scaleType, layoutDirection, outputRotationDegrees);
        Map<UseCase, Rect> useCaseOutputRects = new HashMap<>();
        RectF useCaseOutputRect = new RectF();
        Matrix sensorToUseCaseTransformation = new Matrix();
        for (Map.Entry<UseCase, Matrix> entry2 : useCaseToSensorTransformations.entrySet()) {
            entry2.getValue().invert(sensorToUseCaseTransformation);
            sensorToUseCaseTransformation.mapRect(useCaseOutputRect, viewPortRect);
            Rect outputCropRect = new Rect();
            useCaseOutputRect.round(outputCropRect);
            useCaseOutputRects.put(entry2.getKey(), outputCropRect);
        }
        return useCaseOutputRects;
    }

    public static RectF getScaledRect(RectF fittingRect, Rational containerAspectRatio, int scaleType, int layoutDirection, int rotationDegrees) {
        if (scaleType == 3) {
            return fittingRect;
        }
        Matrix viewPortToSurfaceTransformation = new Matrix();
        RectF viewPortRect = new RectF(0.0f, 0.0f, (float) containerAspectRatio.getNumerator(), (float) containerAspectRatio.getDenominator());
        if (scaleType == 0) {
            viewPortToSurfaceTransformation.setRectToRect(viewPortRect, fittingRect, Matrix.ScaleToFit.START);
        } else if (scaleType == 1) {
            viewPortToSurfaceTransformation.setRectToRect(viewPortRect, fittingRect, Matrix.ScaleToFit.CENTER);
        } else if (scaleType == 2) {
            viewPortToSurfaceTransformation.setRectToRect(viewPortRect, fittingRect, Matrix.ScaleToFit.END);
        } else {
            throw new IllegalStateException("Unexpected scale type: " + scaleType);
        }
        RectF viewPortRectInSurfaceCoordinates = new RectF();
        viewPortToSurfaceTransformation.mapRect(viewPortRectInSurfaceCoordinates, viewPortRect);
        return correctStartOrEnd(layoutDirection, rotationDegrees, fittingRect, viewPortRectInSurfaceCoordinates);
    }

    private static RectF correctStartOrEnd(int layoutDirection, int rotationDegrees, RectF containerRect, RectF cropRect) {
        boolean rtlRotation270 = false;
        boolean ltrRotation0 = rotationDegrees == 0 && layoutDirection == 0;
        boolean rtlRotation90 = rotationDegrees == 90 && layoutDirection == 1;
        if (ltrRotation0 || rtlRotation90) {
            return cropRect;
        }
        boolean rtlRotation0 = rotationDegrees == 0 && layoutDirection == 1;
        boolean ltrRotation270 = rotationDegrees == 270 && layoutDirection == 0;
        if (rtlRotation0 || ltrRotation270) {
            return flipHorizontally(cropRect, containerRect.centerX());
        }
        boolean ltrRotation90 = rotationDegrees == 90 && layoutDirection == 0;
        boolean rtlRotation180 = rotationDegrees == 180 && layoutDirection == 1;
        if (ltrRotation90 || rtlRotation180) {
            return flipVertically(cropRect, containerRect.centerY());
        }
        boolean ltrRotation180 = rotationDegrees == 180 && layoutDirection == 0;
        if (rotationDegrees == 270 && layoutDirection == 1) {
            rtlRotation270 = true;
        }
        if (ltrRotation180 || rtlRotation270) {
            return flipHorizontally(flipVertically(cropRect, containerRect.centerY()), containerRect.centerX());
        }
        throw new IllegalArgumentException("Invalid argument: direction" + layoutDirection + " rotation " + rotationDegrees);
    }

    private static RectF flipHorizontally(RectF original, float flipLineX) {
        return new RectF(flipX(original.right, flipLineX), original.top, flipX(original.left, flipLineX), original.bottom);
    }

    private static RectF flipVertically(RectF original, float flipLineY) {
        return new RectF(original.left, flipY(original.bottom, flipLineY), original.right, flipY(original.top, flipLineY));
    }

    private static float flipX(float x, float flipLineX) {
        return (flipLineX + flipLineX) - x;
    }

    private static float flipY(float y, float flipLineY) {
        return (flipLineY + flipLineY) - y;
    }
}
