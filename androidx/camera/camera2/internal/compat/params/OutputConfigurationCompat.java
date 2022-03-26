package androidx.camera.camera2.internal.compat.params;

import android.hardware.camera2.params.OutputConfiguration;
import android.os.Build;
import android.util.Size;
import android.view.Surface;
import java.util.List;
/* loaded from: classes.dex */
public final class OutputConfigurationCompat {
    public static final int SURFACE_GROUP_ID_NONE;
    private final OutputConfigurationCompatImpl mImpl;

    /* loaded from: classes.dex */
    public interface OutputConfigurationCompatImpl {
        void addSurface(Surface surface);

        void enableSurfaceSharing();

        int getMaxSharedSurfaceCount();

        Object getOutputConfiguration();

        String getPhysicalCameraId();

        Surface getSurface();

        int getSurfaceGroupId();

        List<Surface> getSurfaces();

        void removeSurface(Surface surface);

        void setPhysicalCameraId(String str);
    }

    public OutputConfigurationCompat(Surface surface) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.mImpl = new OutputConfigurationCompatApi28Impl(surface);
        } else if (Build.VERSION.SDK_INT >= 26) {
            this.mImpl = new OutputConfigurationCompatApi26Impl(surface);
        } else if (Build.VERSION.SDK_INT >= 24) {
            this.mImpl = new OutputConfigurationCompatApi24Impl(surface);
        } else {
            this.mImpl = new OutputConfigurationCompatBaseImpl(surface);
        }
    }

    public <T> OutputConfigurationCompat(Size surfaceSize, Class<T> klass) {
        OutputConfiguration deferredConfig = new OutputConfiguration(surfaceSize, klass);
        if (Build.VERSION.SDK_INT >= 28) {
            this.mImpl = OutputConfigurationCompatApi28Impl.wrap(deferredConfig);
        } else {
            this.mImpl = OutputConfigurationCompatApi26Impl.wrap(deferredConfig);
        }
    }

    private OutputConfigurationCompat(OutputConfigurationCompatImpl impl) {
        this.mImpl = impl;
    }

    public static OutputConfigurationCompat wrap(Object outputConfiguration) {
        if (outputConfiguration == null) {
            return null;
        }
        OutputConfigurationCompatImpl outputConfigurationCompatImpl = null;
        if (Build.VERSION.SDK_INT >= 28) {
            outputConfigurationCompatImpl = OutputConfigurationCompatApi28Impl.wrap((OutputConfiguration) outputConfiguration);
        } else if (Build.VERSION.SDK_INT >= 26) {
            outputConfigurationCompatImpl = OutputConfigurationCompatApi26Impl.wrap((OutputConfiguration) outputConfiguration);
        } else if (Build.VERSION.SDK_INT >= 24) {
            outputConfigurationCompatImpl = OutputConfigurationCompatApi24Impl.wrap((OutputConfiguration) outputConfiguration);
        }
        if (outputConfigurationCompatImpl == null) {
            return null;
        }
        return new OutputConfigurationCompat(outputConfigurationCompatImpl);
    }

    public void enableSurfaceSharing() {
        this.mImpl.enableSurfaceSharing();
    }

    public String getPhysicalCameraId() {
        return this.mImpl.getPhysicalCameraId();
    }

    public void setPhysicalCameraId(String physicalCameraId) {
        this.mImpl.setPhysicalCameraId(physicalCameraId);
    }

    public void addSurface(Surface surface) {
        this.mImpl.addSurface(surface);
    }

    public void removeSurface(Surface surface) {
        this.mImpl.removeSurface(surface);
    }

    public int getMaxSharedSurfaceCount() {
        return this.mImpl.getMaxSharedSurfaceCount();
    }

    public Surface getSurface() {
        return this.mImpl.getSurface();
    }

    public List<Surface> getSurfaces() {
        return this.mImpl.getSurfaces();
    }

    public int getSurfaceGroupId() {
        return this.mImpl.getSurfaceGroupId();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof OutputConfigurationCompat)) {
            return false;
        }
        return this.mImpl.equals(((OutputConfigurationCompat) obj).mImpl);
    }

    public int hashCode() {
        return this.mImpl.hashCode();
    }

    public Object unwrap() {
        return this.mImpl.getOutputConfiguration();
    }
}
