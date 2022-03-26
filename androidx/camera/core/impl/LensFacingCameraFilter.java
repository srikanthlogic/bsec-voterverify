package androidx.camera.core.impl;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraFilter;
import androidx.core.util.Preconditions;
import java.util.Iterator;
import java.util.LinkedHashSet;
/* loaded from: classes.dex */
public class LensFacingCameraFilter implements CameraFilter {
    private int mLensFacing;

    public LensFacingCameraFilter(int lensFacing) {
        this.mLensFacing = lensFacing;
    }

    @Override // androidx.camera.core.CameraFilter
    public LinkedHashSet<Camera> filter(LinkedHashSet<Camera> cameras) {
        LinkedHashSet<Camera> resultCameras = new LinkedHashSet<>();
        Iterator<Camera> it = cameras.iterator();
        while (it.hasNext()) {
            Camera camera = it.next();
            Preconditions.checkState(camera instanceof CameraInternal, "The camera doesn't contain internal implementation.");
            Integer lensFacing = ((CameraInternal) camera).getCameraInfoInternal().getLensFacing();
            if (lensFacing != null && lensFacing.intValue() == this.mLensFacing) {
                resultCameras.add(camera);
            }
        }
        return resultCameras;
    }

    public int getLensFacing() {
        return this.mLensFacing;
    }
}
