package androidx.camera.core;

import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.LensFacingCameraFilter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashSet;
/* loaded from: classes.dex */
public final class CameraSelector {
    public static final int LENS_FACING_BACK;
    public static final int LENS_FACING_FRONT;
    private LinkedHashSet<CameraFilter> mCameraFilterSet;
    public static final CameraSelector DEFAULT_FRONT_CAMERA = new Builder().requireLensFacing(0).build();
    public static final CameraSelector DEFAULT_BACK_CAMERA = new Builder().requireLensFacing(1).build();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface LensFacing {
    }

    CameraSelector(LinkedHashSet<CameraFilter> cameraFilterSet) {
        this.mCameraFilterSet = cameraFilterSet;
    }

    public CameraInternal select(LinkedHashSet<CameraInternal> cameras) {
        return filter(cameras).iterator().next();
    }

    public LinkedHashSet<CameraInternal> filter(LinkedHashSet<CameraInternal> cameras) {
        LinkedHashSet<Camera> camerasCopy = new LinkedHashSet<>(cameras);
        LinkedHashSet<Camera> resultCameras = new LinkedHashSet<>(cameras);
        Iterator<CameraFilter> it = this.mCameraFilterSet.iterator();
        while (it.hasNext()) {
            resultCameras = it.next().filter(resultCameras);
            if (resultCameras.isEmpty()) {
                throw new IllegalArgumentException("No available camera can be found.");
            } else if (camerasCopy.containsAll(resultCameras)) {
                camerasCopy.retainAll(resultCameras);
            } else {
                throw new IllegalArgumentException("The output isn't contained in the input.");
            }
        }
        LinkedHashSet<CameraInternal> returnCameras = new LinkedHashSet<>();
        Iterator<Camera> it2 = resultCameras.iterator();
        while (it2.hasNext()) {
            returnCameras.add((CameraInternal) it2.next());
        }
        return returnCameras;
    }

    public LinkedHashSet<CameraFilter> getCameraFilterSet() {
        return this.mCameraFilterSet;
    }

    public Integer getLensFacing() {
        Integer currentLensFacing = null;
        Iterator<CameraFilter> it = this.mCameraFilterSet.iterator();
        while (it.hasNext()) {
            CameraFilter filter = it.next();
            if (filter instanceof LensFacingCameraFilter) {
                Integer newLensFacing = Integer.valueOf(((LensFacingCameraFilter) filter).getLensFacing());
                if (currentLensFacing == null) {
                    currentLensFacing = newLensFacing;
                } else if (!currentLensFacing.equals(newLensFacing)) {
                    throw new IllegalStateException("Multiple conflicting lens facing requirements exist.");
                }
            }
        }
        return currentLensFacing;
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private final LinkedHashSet<CameraFilter> mCameraFilterSet;

        public Builder() {
            this.mCameraFilterSet = new LinkedHashSet<>();
        }

        private Builder(LinkedHashSet<CameraFilter> cameraFilterSet) {
            this.mCameraFilterSet = new LinkedHashSet<>(cameraFilterSet);
        }

        public Builder requireLensFacing(int lensFacing) {
            this.mCameraFilterSet.add(new LensFacingCameraFilter(lensFacing));
            return this;
        }

        public Builder addCameraFilter(CameraFilter cameraFilter) {
            this.mCameraFilterSet.add(cameraFilter);
            return this;
        }

        public static Builder fromSelector(CameraSelector cameraSelector) {
            return new Builder(cameraSelector.getCameraFilterSet());
        }

        public CameraSelector build() {
            return new CameraSelector(this.mCameraFilterSet);
        }
    }
}
