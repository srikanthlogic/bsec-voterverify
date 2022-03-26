package androidx.camera.lifecycle;

import androidx.camera.core.internal.CameraUseCaseAdapter;
import androidx.camera.lifecycle.LifecycleCameraRepository;
import androidx.lifecycle.LifecycleOwner;
/* loaded from: classes.dex */
final class AutoValue_LifecycleCameraRepository_Key extends LifecycleCameraRepository.Key {
    private final CameraUseCaseAdapter.CameraId cameraId;
    private final LifecycleOwner lifecycleOwner;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_LifecycleCameraRepository_Key(LifecycleOwner lifecycleOwner, CameraUseCaseAdapter.CameraId cameraId) {
        if (lifecycleOwner != null) {
            this.lifecycleOwner = lifecycleOwner;
            if (cameraId != null) {
                this.cameraId = cameraId;
                return;
            }
            throw new NullPointerException("Null cameraId");
        }
        throw new NullPointerException("Null lifecycleOwner");
    }

    @Override // androidx.camera.lifecycle.LifecycleCameraRepository.Key
    public LifecycleOwner getLifecycleOwner() {
        return this.lifecycleOwner;
    }

    @Override // androidx.camera.lifecycle.LifecycleCameraRepository.Key
    public CameraUseCaseAdapter.CameraId getCameraId() {
        return this.cameraId;
    }

    public String toString() {
        return "Key{lifecycleOwner=" + this.lifecycleOwner + ", cameraId=" + this.cameraId + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LifecycleCameraRepository.Key)) {
            return false;
        }
        LifecycleCameraRepository.Key that = (LifecycleCameraRepository.Key) o;
        if (!this.lifecycleOwner.equals(that.getLifecycleOwner()) || !this.cameraId.equals(that.getCameraId())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.lifecycleOwner.hashCode()) * 1000003) ^ this.cameraId.hashCode();
    }
}
