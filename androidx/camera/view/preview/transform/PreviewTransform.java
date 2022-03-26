package androidx.camera.view.preview.transform;

import android.util.Size;
import android.view.View;
import androidx.camera.view.PreviewView;
import androidx.camera.view.preview.transform.transformation.Transformation;
/* loaded from: classes.dex */
public final class PreviewTransform {
    private static final PreviewView.ScaleType DEFAULT_SCALE_TYPE = PreviewView.ScaleType.FILL_CENTER;
    private Transformation mCurrentTransformation;
    private PreviewView.ScaleType mScaleType = DEFAULT_SCALE_TYPE;
    private boolean mSensorDimensionFlipNeeded = true;
    private int mDeviceRotation = -1;

    public PreviewView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setScaleType(PreviewView.ScaleType scaleType) {
        this.mScaleType = scaleType;
    }

    public Transformation getCurrentTransformation() {
        return this.mCurrentTransformation;
    }

    public boolean isSensorDimensionFlipNeeded() {
        return this.mSensorDimensionFlipNeeded;
    }

    public void setSensorDimensionFlipNeeded(boolean sensorDimensionFlipNeeded) {
        this.mSensorDimensionFlipNeeded = sensorDimensionFlipNeeded;
    }

    public int getDeviceRotation() {
        return this.mDeviceRotation;
    }

    public void setDeviceRotation(int deviceRotation) {
        this.mDeviceRotation = deviceRotation;
    }

    public void applyCurrentScaleType(View container, View view, Size bufferSize) {
        resetPreview(view);
        correctPreview(container, view, bufferSize);
        applyScaleTypeInternal(container, view, this.mScaleType, this.mDeviceRotation);
    }

    private void resetPreview(View view) {
        applyTransformation(view, new Transformation());
    }

    private void correctPreview(View container, View view, Size bufferSize) {
        applyTransformation(view, PreviewCorrector.getCorrectionTransformation(container, view, bufferSize, this.mSensorDimensionFlipNeeded, this.mDeviceRotation));
    }

    private void applyScaleTypeInternal(View container, View view, PreviewView.ScaleType scaleType, int deviceRotation) {
        applyTransformation(view, Transformation.getTransformation(view).add(ScaleTypeTransform.getTransformation(container, view, scaleType, deviceRotation)));
    }

    private void applyTransformation(View view, Transformation transformation) {
        view.setX(0.0f);
        view.setY(0.0f);
        view.setScaleX(transformation.getScaleX());
        view.setScaleY(transformation.getScaleY());
        view.setTranslationX(transformation.getTransX());
        view.setTranslationY(transformation.getTransY());
        view.setRotation(transformation.getRotation());
        this.mCurrentTransformation = transformation;
    }
}
