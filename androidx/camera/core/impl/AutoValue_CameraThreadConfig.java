package androidx.camera.core.impl;

import android.os.Handler;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_CameraThreadConfig extends CameraThreadConfig {
    private final Executor cameraExecutor;
    private final Handler schedulerHandler;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_CameraThreadConfig(Executor cameraExecutor, Handler schedulerHandler) {
        if (cameraExecutor != null) {
            this.cameraExecutor = cameraExecutor;
            if (schedulerHandler != null) {
                this.schedulerHandler = schedulerHandler;
                return;
            }
            throw new NullPointerException("Null schedulerHandler");
        }
        throw new NullPointerException("Null cameraExecutor");
    }

    @Override // androidx.camera.core.impl.CameraThreadConfig
    public Executor getCameraExecutor() {
        return this.cameraExecutor;
    }

    @Override // androidx.camera.core.impl.CameraThreadConfig
    public Handler getSchedulerHandler() {
        return this.schedulerHandler;
    }

    public String toString() {
        return "CameraThreadConfig{cameraExecutor=" + this.cameraExecutor + ", schedulerHandler=" + this.schedulerHandler + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CameraThreadConfig)) {
            return false;
        }
        CameraThreadConfig that = (CameraThreadConfig) o;
        if (!this.cameraExecutor.equals(that.getCameraExecutor()) || !this.schedulerHandler.equals(that.getSchedulerHandler())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.cameraExecutor.hashCode()) * 1000003) ^ this.schedulerHandler.hashCode();
    }
}
