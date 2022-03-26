package androidx.camera.core;

import android.graphics.PointF;
import android.view.Display;
import androidx.camera.core.impl.CameraInfoInternal;
/* loaded from: classes.dex */
public final class DisplayOrientedMeteringPointFactory extends MeteringPointFactory {
    private final CameraInfoInternal mCameraInfo;
    private final CameraSelector mCameraSelector;
    private final Display mDisplay;
    private final float mHeight;
    private final float mWidth;

    public DisplayOrientedMeteringPointFactory(Display display, CameraSelector cameraSelector, float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mCameraSelector = cameraSelector;
        this.mDisplay = display;
        try {
            this.mCameraInfo = CameraX.getCameraWithCameraSelector(this.mCameraSelector).getCameraInfoInternal();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unable to get camera id for the CameraSelector.", e);
        }
    }

    private Integer getLensFacing() {
        return this.mCameraInfo.getLensFacing();
    }

    @Override // androidx.camera.core.MeteringPointFactory
    protected PointF convertPoint(float x, float y) {
        float width = this.mWidth;
        float height = this.mHeight;
        Integer lensFacing = getLensFacing();
        boolean compensateForMirroring = lensFacing != null && lensFacing.intValue() == 0;
        int relativeCameraOrientation = getRelativeCameraOrientation(compensateForMirroring);
        float outputX = x;
        float outputY = y;
        float outputWidth = width;
        float outputHeight = height;
        if (relativeCameraOrientation == 90 || relativeCameraOrientation == 270) {
            outputX = y;
            outputY = x;
            outputWidth = height;
            outputHeight = width;
        }
        if (relativeCameraOrientation == 90) {
            outputY = outputHeight - outputY;
        } else if (relativeCameraOrientation == 180) {
            outputX = outputWidth - outputX;
            outputY = outputHeight - outputY;
        } else if (relativeCameraOrientation == 270) {
            outputX = outputWidth - outputX;
        }
        if (compensateForMirroring) {
            outputX = outputWidth - outputX;
        }
        return new PointF(outputX / outputWidth, outputY / outputHeight);
    }

    private int getRelativeCameraOrientation(boolean compensateForMirroring) {
        try {
            int rotationDegrees = this.mCameraInfo.getSensorRotationDegrees(this.mDisplay.getRotation());
            if (compensateForMirroring) {
                return (360 - rotationDegrees) % 360;
            }
            return rotationDegrees;
        } catch (Exception e) {
            return 0;
        }
    }
}
