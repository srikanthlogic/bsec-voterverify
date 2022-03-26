package androidx.camera.core;

import android.view.Surface;
import androidx.camera.core.SurfaceRequest;
/* loaded from: classes.dex */
final class AutoValue_SurfaceRequest_Result extends SurfaceRequest.Result {
    private final int resultCode;
    private final Surface surface;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_SurfaceRequest_Result(int resultCode, Surface surface) {
        this.resultCode = resultCode;
        if (surface != null) {
            this.surface = surface;
            return;
        }
        throw new NullPointerException("Null surface");
    }

    @Override // androidx.camera.core.SurfaceRequest.Result
    public int getResultCode() {
        return this.resultCode;
    }

    @Override // androidx.camera.core.SurfaceRequest.Result
    public Surface getSurface() {
        return this.surface;
    }

    public String toString() {
        return "Result{resultCode=" + this.resultCode + ", surface=" + this.surface + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SurfaceRequest.Result)) {
            return false;
        }
        SurfaceRequest.Result that = (SurfaceRequest.Result) o;
        if (this.resultCode != that.getResultCode() || !this.surface.equals(that.getSurface())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.resultCode) * 1000003) ^ this.surface.hashCode();
    }
}
