package androidx.camera.core.internal;

import androidx.camera.core.ImageInfo;
import androidx.camera.core.impl.CameraCaptureResult;
/* loaded from: classes.dex */
public final class CameraCaptureResultImageInfo implements ImageInfo {
    private final CameraCaptureResult mCameraCaptureResult;

    public CameraCaptureResultImageInfo(CameraCaptureResult cameraCaptureResult) {
        this.mCameraCaptureResult = cameraCaptureResult;
    }

    @Override // androidx.camera.core.ImageInfo
    public Object getTag() {
        return this.mCameraCaptureResult.getTag();
    }

    @Override // androidx.camera.core.ImageInfo
    public long getTimestamp() {
        return this.mCameraCaptureResult.getTimestamp();
    }

    @Override // androidx.camera.core.ImageInfo
    public int getRotationDegrees() {
        return 0;
    }

    public CameraCaptureResult getCameraCaptureResult() {
        return this.mCameraCaptureResult;
    }
}
