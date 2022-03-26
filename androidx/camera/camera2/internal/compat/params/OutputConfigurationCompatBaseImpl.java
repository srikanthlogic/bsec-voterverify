package androidx.camera.camera2.internal.compat.params;

import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat;
import androidx.core.util.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class OutputConfigurationCompatBaseImpl implements OutputConfigurationCompat.OutputConfigurationCompatImpl {
    static final String TAG = "OutputConfigCompat";
    final Object mObject;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OutputConfigurationCompatBaseImpl(Surface surface) {
        this.mObject = new OutputConfigurationParamsApi21(surface);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OutputConfigurationCompatBaseImpl(Object outputConfiguration) {
        this.mObject = outputConfiguration;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void enableSurfaceSharing() {
        ((OutputConfigurationParamsApi21) this.mObject).mIsShared = true;
    }

    boolean isSurfaceSharingEnabled() {
        return ((OutputConfigurationParamsApi21) this.mObject).mIsShared;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void setPhysicalCameraId(String physicalCameraId) {
        ((OutputConfigurationParamsApi21) this.mObject).mPhysicalCameraId = physicalCameraId;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public String getPhysicalCameraId() {
        return ((OutputConfigurationParamsApi21) this.mObject).mPhysicalCameraId;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void addSurface(Surface surface) {
        Preconditions.checkNotNull(surface, "Surface must not be null");
        if (getSurface() == surface) {
            throw new IllegalStateException("Surface is already added!");
        } else if (!isSurfaceSharingEnabled()) {
            throw new IllegalStateException("Cannot have 2 surfaces for a non-sharing configuration");
        } else {
            throw new IllegalArgumentException("Exceeds maximum number of surfaces");
        }
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void removeSurface(Surface surface) {
        if (getSurface() == surface) {
            throw new IllegalArgumentException("Cannot remove surface associated with this output configuration");
        }
        throw new IllegalArgumentException("Surface is not part of this output configuration");
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public int getMaxSharedSurfaceCount() {
        return 1;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public Surface getSurface() {
        List<Surface> surfaces = ((OutputConfigurationParamsApi21) this.mObject).mSurfaces;
        if (surfaces.size() == 0) {
            return null;
        }
        return surfaces.get(0);
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public List<Surface> getSurfaces() {
        return ((OutputConfigurationParamsApi21) this.mObject).mSurfaces;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public int getSurfaceGroupId() {
        return -1;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public Object getOutputConfiguration() {
        return null;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof OutputConfigurationCompatBaseImpl)) {
            return false;
        }
        return Objects.equals(this.mObject, ((OutputConfigurationCompatBaseImpl) obj).mObject);
    }

    public int hashCode() {
        return this.mObject.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OutputConfigurationParamsApi21 {
        private static final String DETECT_SURFACE_TYPE_METHOD = "detectSurfaceType";
        private static final String GET_GENERATION_ID_METHOD = "getGenerationId";
        private static final String GET_SURFACE_SIZE_METHOD = "getSurfaceSize";
        private static final String LEGACY_CAMERA_DEVICE_CLASS = "android.hardware.camera2.legacy.LegacyCameraDevice";
        static final int MAX_SURFACES_COUNT = 1;
        final int mConfiguredFormat;
        final int mConfiguredGenerationId;
        final Size mConfiguredSize;
        boolean mIsShared = false;
        String mPhysicalCameraId;
        final List<Surface> mSurfaces;

        OutputConfigurationParamsApi21(Surface surface) {
            Preconditions.checkNotNull(surface, "Surface must not be null");
            this.mSurfaces = Collections.singletonList(surface);
            this.mConfiguredSize = getSurfaceSize(surface);
            this.mConfiguredFormat = getSurfaceFormat(surface);
            this.mConfiguredGenerationId = getSurfaceGenerationId(surface);
        }

        private static Size getSurfaceSize(Surface surface) {
            try {
                Method getSurfaceSize = Class.forName(LEGACY_CAMERA_DEVICE_CLASS).getDeclaredMethod(GET_SURFACE_SIZE_METHOD, Surface.class);
                getSurfaceSize.setAccessible(true);
                return (Size) getSurfaceSize.invoke(null, surface);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Log.e(OutputConfigurationCompatBaseImpl.TAG, "Unable to retrieve surface size.", e);
                return null;
            }
        }

        private static int getSurfaceFormat(Surface surface) {
            try {
                Method detectSurfaceType = Class.forName(LEGACY_CAMERA_DEVICE_CLASS).getDeclaredMethod(DETECT_SURFACE_TYPE_METHOD, Surface.class);
                if (Build.VERSION.SDK_INT < 22) {
                    detectSurfaceType.setAccessible(true);
                }
                return ((Integer) detectSurfaceType.invoke(null, surface)).intValue();
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Log.e(OutputConfigurationCompatBaseImpl.TAG, "Unable to retrieve surface format.", e);
                return 0;
            }
        }

        private static int getSurfaceGenerationId(Surface surface) {
            try {
                return ((Integer) Surface.class.getDeclaredMethod(GET_GENERATION_ID_METHOD, new Class[0]).invoke(surface, new Object[0])).intValue();
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Log.e(OutputConfigurationCompatBaseImpl.TAG, "Unable to retrieve surface generation id.", e);
                return -1;
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof OutputConfigurationParamsApi21)) {
                return false;
            }
            OutputConfigurationParamsApi21 otherOutputConfig = (OutputConfigurationParamsApi21) obj;
            if (!(this.mConfiguredSize.equals(otherOutputConfig.mConfiguredSize) && this.mConfiguredFormat == otherOutputConfig.mConfiguredFormat && this.mConfiguredGenerationId == otherOutputConfig.mConfiguredGenerationId && this.mIsShared == otherOutputConfig.mIsShared && Objects.equals(this.mPhysicalCameraId, otherOutputConfig.mPhysicalCameraId))) {
                return false;
            }
            int minLen = Math.min(this.mSurfaces.size(), otherOutputConfig.mSurfaces.size());
            for (int i = 0; i < minLen; i++) {
                if (this.mSurfaces.get(i) != otherOutputConfig.mSurfaces.get(i)) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            int h = ((1 << 5) - 1) ^ this.mSurfaces.hashCode();
            int h2 = ((h << 5) - h) ^ this.mConfiguredGenerationId;
            int h3 = ((h2 << 5) - h2) ^ this.mConfiguredSize.hashCode();
            int h4 = ((h3 << 5) - h3) ^ this.mConfiguredFormat;
            int h5 = ((h4 << 5) - h4) ^ (this.mIsShared ? 1 : 0);
            int i = (h5 << 5) - h5;
            String str = this.mPhysicalCameraId;
            return i ^ (str == null ? 0 : str.hashCode());
        }
    }
}
