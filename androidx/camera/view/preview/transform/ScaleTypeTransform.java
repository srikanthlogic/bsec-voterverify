package androidx.camera.view.preview.transform;

import android.util.Pair;
import android.view.View;
import androidx.camera.view.PreviewView;
import androidx.camera.view.preview.transform.transformation.ScaleTransformation;
import androidx.camera.view.preview.transform.transformation.Transformation;
import androidx.camera.view.preview.transform.transformation.TranslationTransformation;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ScaleTypeTransform {
    private ScaleTypeTransform() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Transformation getTransformation(View container, View view, PreviewView.ScaleType scaleType, int deviceRotation) {
        Transformation scale = getScale(container, view, scaleType, deviceRotation);
        return scale.add(getScaledTranslation(container, view, scaleType, new Pair<>(Float.valueOf(view.getScaleX() * scale.getScaleX()), Float.valueOf(view.getScaleY() * scale.getScaleY())), deviceRotation));
    }

    private static ScaleTransformation getScale(View container, View view, PreviewView.ScaleType scaleType, int deviceRotation) {
        switch (scaleType) {
            case FILL_START:
            case FILL_CENTER:
            case FILL_END:
                return ScaleTransform.fill(container, view, deviceRotation);
            case FIT_START:
            case FIT_CENTER:
            case FIT_END:
                return ScaleTransform.fit(container, view, deviceRotation);
            default:
                throw new IllegalArgumentException("Unknown scale type " + scaleType);
        }
    }

    private static TranslationTransformation getScaledTranslation(View container, View view, PreviewView.ScaleType scaleType, Pair<Float, Float> scaleXY, int deviceRotation) {
        switch (scaleType) {
            case FILL_START:
            case FIT_START:
                return TranslationTransform.start(view, scaleXY, deviceRotation);
            case FILL_CENTER:
            case FIT_CENTER:
                return TranslationTransform.center(container, view);
            case FILL_END:
            case FIT_END:
                return TranslationTransform.end(container, view, scaleXY, deviceRotation);
            default:
                throw new IllegalArgumentException("Unknown scale type " + scaleType);
        }
    }
}
