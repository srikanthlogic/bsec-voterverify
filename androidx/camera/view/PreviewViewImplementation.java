package androidx.camera.view;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Size;
import android.view.View;
import android.widget.FrameLayout;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.view.PreviewView;
import androidx.camera.view.preview.transform.PreviewTransform;
import androidx.camera.view.preview.transform.transformation.Transformation;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class PreviewViewImplementation {
    FrameLayout mParent;
    private PreviewTransform mPreviewTransform;
    Size mResolution;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnSurfaceNotInUseListener {
        void onSurfaceNotInUse();
    }

    abstract View getPreview();

    abstract Bitmap getPreviewBitmap();

    abstract void initializePreview();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void onAttachedToWindow();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void onDetachedFromWindow();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void onSurfaceRequested(SurfaceRequest surfaceRequest, OnSurfaceNotInUseListener onSurfaceNotInUseListener);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract ListenableFuture<Void> waitForNextFrame();

    public void init(FrameLayout parent, PreviewTransform previewTransform) {
        this.mParent = parent;
        this.mPreviewTransform = previewTransform;
    }

    public Size getResolution() {
        return this.mResolution;
    }

    public void redrawPreview() {
        applyCurrentScaleType();
    }

    void onSurfaceProvided() {
        applyCurrentScaleType();
    }

    private void applyCurrentScaleType() {
        FrameLayout frameLayout;
        Size size;
        View preview = getPreview();
        PreviewTransform previewTransform = this.mPreviewTransform;
        if (previewTransform != null && (frameLayout = this.mParent) != null && preview != null && (size = this.mResolution) != null) {
            previewTransform.applyCurrentScaleType(frameLayout, preview, size);
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = getPreviewBitmap();
        if (bitmap == null) {
            return bitmap;
        }
        Preconditions.checkNotNull(this.mPreviewTransform);
        Transformation transformation = this.mPreviewTransform.getCurrentTransformation();
        if (transformation == null) {
            return bitmap;
        }
        Matrix scale = new Matrix();
        scale.setScale(transformation.getScaleX(), transformation.getScaleY());
        scale.postRotate(transformation.getRotation());
        Bitmap scaled = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), scale, true);
        PreviewView.ScaleType scaleType = this.mPreviewTransform.getScaleType();
        Preconditions.checkNotNull(this.mParent);
        int x = 0;
        int y = 0;
        switch (scaleType) {
            case FIT_START:
            case FIT_CENTER:
            case FIT_END:
                return scaled;
            case FILL_START:
                x = 0;
                y = 0;
                break;
            case FILL_CENTER:
                x = (scaled.getWidth() - this.mParent.getWidth()) / 2;
                y = (scaled.getHeight() - this.mParent.getHeight()) / 2;
                break;
            case FILL_END:
                x = scaled.getWidth() - this.mParent.getWidth();
                y = scaled.getHeight() - this.mParent.getHeight();
                break;
        }
        return Bitmap.createBitmap(scaled, x, y, this.mParent.getWidth(), this.mParent.getHeight());
    }
}
