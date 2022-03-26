package androidx.camera.view;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.TextureView;
import androidx.camera.core.MeteringPointFactory;
/* loaded from: classes.dex */
public class TextureViewMeteringPointFactory extends MeteringPointFactory {
    private final TextureView mTextureView;

    public TextureViewMeteringPointFactory(TextureView textureView) {
        this.mTextureView = textureView;
    }

    @Override // androidx.camera.core.MeteringPointFactory
    protected PointF convertPoint(float x, float y) {
        Matrix transform = new Matrix();
        this.mTextureView.getTransform(transform);
        Matrix inverse = new Matrix();
        transform.invert(inverse);
        float[] pt = {x, y};
        inverse.mapPoints(pt);
        float[] surfaceTextureMat = new float[16];
        this.mTextureView.getSurfaceTexture().getTransformMatrix(surfaceTextureMat);
        Matrix surfaceTextureTransform = glMatrixToGraphicsMatrix(surfaceTextureMat);
        float[] pt2 = {pt[0] / ((float) this.mTextureView.getWidth()), (((float) this.mTextureView.getHeight()) - pt[1]) / ((float) this.mTextureView.getHeight())};
        surfaceTextureTransform.mapPoints(pt2);
        return new PointF(pt2[0], pt2[1]);
    }

    private Matrix glMatrixToGraphicsMatrix(float[] glMatrix) {
        float[] convert = {glMatrix[0], glMatrix[4], glMatrix[12], glMatrix[1], glMatrix[5], glMatrix[13], glMatrix[3], glMatrix[7], glMatrix[15]};
        Matrix graphicsMatrix = new Matrix();
        graphicsMatrix.setValues(convert);
        return graphicsMatrix;
    }
}
