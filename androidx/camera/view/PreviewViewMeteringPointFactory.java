package androidx.camera.view;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Size;
import android.view.Display;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.DisplayOrientedMeteringPointFactory;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.view.PreviewView;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class PreviewViewMeteringPointFactory extends MeteringPointFactory {
    private DisplayOrientedMeteringPointFactory mDisplayOrientedMeteringPointFactory;
    private float mFactoryHeight;
    private float mFactoryWidth;
    private final boolean mIsValid;
    private final PreviewView.ScaleType mScaleType;
    private final float mViewHeight;
    private final float mViewWidth;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PreviewViewMeteringPointFactory(Display display, CameraSelector cameraSelector, Size resolution, PreviewView.ScaleType scaleType, int viewWidth, int viewHeight) {
        int bufferRotatedHeight;
        int bufferRotatedWidth;
        float scale;
        this.mViewWidth = (float) viewWidth;
        this.mViewHeight = (float) viewHeight;
        this.mScaleType = scaleType;
        if (resolution == null || this.mViewWidth <= 0.0f || this.mViewHeight <= 0.0f) {
            this.mIsValid = false;
            return;
        }
        this.mIsValid = true;
        boolean needReverse = false;
        if (isNaturalPortrait(display)) {
            if (display.getRotation() == 0 || display.getRotation() == 2) {
                needReverse = true;
            }
        } else if (display.getRotation() == 1 || display.getRotation() == 3) {
            needReverse = true;
        }
        if (needReverse) {
            bufferRotatedWidth = resolution.getHeight();
            bufferRotatedHeight = resolution.getWidth();
        } else {
            bufferRotatedWidth = resolution.getWidth();
            bufferRotatedHeight = resolution.getHeight();
        }
        if (this.mScaleType == PreviewView.ScaleType.FILL_CENTER || this.mScaleType == PreviewView.ScaleType.FILL_START || this.mScaleType == PreviewView.ScaleType.FILL_END) {
            scale = Math.max(((float) viewWidth) / ((float) bufferRotatedWidth), ((float) viewHeight) / ((float) bufferRotatedHeight));
        } else if (this.mScaleType == PreviewView.ScaleType.FIT_START || this.mScaleType == PreviewView.ScaleType.FIT_CENTER || this.mScaleType == PreviewView.ScaleType.FIT_END) {
            scale = Math.min(((float) viewWidth) / ((float) bufferRotatedWidth), ((float) viewHeight) / ((float) bufferRotatedHeight));
        } else {
            throw new IllegalArgumentException("Unknown scale type " + scaleType);
        }
        this.mFactoryWidth = ((float) bufferRotatedWidth) * scale;
        this.mFactoryHeight = ((float) bufferRotatedHeight) * scale;
        this.mDisplayOrientedMeteringPointFactory = new DisplayOrientedMeteringPointFactory(display, cameraSelector, this.mFactoryWidth, this.mFactoryHeight);
    }

    @Override // androidx.camera.core.MeteringPointFactory
    protected PointF convertPoint(float x, float y) {
        if (!this.mIsValid) {
            return new PointF(2.0f, 2.0f);
        }
        float offsetX = 0.0f;
        float offsetY = 0.0f;
        if (this.mScaleType == PreviewView.ScaleType.FILL_START || this.mScaleType == PreviewView.ScaleType.FIT_START) {
            offsetX = 0.0f;
            offsetY = 0.0f;
        } else if (this.mScaleType == PreviewView.ScaleType.FILL_CENTER || this.mScaleType == PreviewView.ScaleType.FIT_CENTER) {
            offsetX = (this.mFactoryWidth - this.mViewWidth) / 2.0f;
            offsetY = (this.mFactoryHeight - this.mViewHeight) / 2.0f;
        } else if (this.mScaleType == PreviewView.ScaleType.FILL_END || this.mScaleType == PreviewView.ScaleType.FIT_END) {
            offsetX = this.mFactoryWidth - this.mViewWidth;
            offsetY = this.mFactoryHeight - this.mViewHeight;
        }
        MeteringPoint pt = this.mDisplayOrientedMeteringPointFactory.createPoint(x + offsetX, y + offsetY);
        return new PointF(pt.getX(), pt.getY());
    }

    private boolean isNaturalPortrait(Display display) {
        Point deviceSize = new Point();
        display.getRealSize(deviceSize);
        int rotation = display.getRotation();
        int width = deviceSize.x;
        int height = deviceSize.y;
        if ((rotation == 0 || rotation == 2) && width < height) {
            return true;
        }
        return (rotation == 1 || rotation == 3) && width >= height;
    }
}
