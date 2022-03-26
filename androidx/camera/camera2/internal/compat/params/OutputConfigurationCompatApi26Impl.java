package androidx.camera.camera2.internal.compat.params;

import android.hardware.camera2.params.OutputConfiguration;
import android.util.Log;
import android.view.Surface;
import androidx.core.util.Preconditions;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class OutputConfigurationCompatApi26Impl extends OutputConfigurationCompatApi24Impl {
    private static final String MAX_SHARED_SURFACES_COUNT_FIELD = "MAX_SURFACES_COUNT";
    private static final String SURFACES_FIELD = "mSurfaces";

    /* JADX INFO: Access modifiers changed from: package-private */
    public OutputConfigurationCompatApi26Impl(Surface surface) {
        this(new OutputConfigurationParamsApi26(new OutputConfiguration(surface)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OutputConfigurationCompatApi26Impl(Object outputConfiguration) {
        super(outputConfiguration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OutputConfigurationCompatApi26Impl wrap(OutputConfiguration outputConfiguration) {
        return new OutputConfigurationCompatApi26Impl(new OutputConfigurationParamsApi26(outputConfiguration));
    }

    private static int getMaxSharedSurfaceCountApi26() throws NoSuchFieldException, IllegalAccessException {
        Field maxSurfacesCountField = OutputConfiguration.class.getDeclaredField(MAX_SHARED_SURFACES_COUNT_FIELD);
        maxSurfacesCountField.setAccessible(true);
        return maxSurfacesCountField.getInt(null);
    }

    private static List<Surface> getMutableSurfaceListApi26(OutputConfiguration outputConfiguration) throws NoSuchFieldException, IllegalAccessException {
        Field surfacesField = OutputConfiguration.class.getDeclaredField(SURFACES_FIELD);
        surfacesField.setAccessible(true);
        return (List) surfacesField.get(outputConfiguration);
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatApi24Impl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void enableSurfaceSharing() {
        ((OutputConfiguration) getOutputConfiguration()).enableSurfaceSharing();
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatApi24Impl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl
    final boolean isSurfaceSharingEnabled() {
        throw new AssertionError("isSurfaceSharingEnabled() should not be called on API >= 26");
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void addSurface(Surface surface) {
        ((OutputConfiguration) getOutputConfiguration()).addSurface(surface);
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatApi24Impl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void setPhysicalCameraId(String physicalCameraId) {
        ((OutputConfigurationParamsApi26) this.mObject).mPhysicalCameraId = physicalCameraId;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatApi24Impl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public String getPhysicalCameraId() {
        return ((OutputConfigurationParamsApi26) this.mObject).mPhysicalCameraId;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void removeSurface(Surface surface) {
        if (getSurface() != surface) {
            try {
                if (!getMutableSurfaceListApi26((OutputConfiguration) getOutputConfiguration()).remove(surface)) {
                    throw new IllegalArgumentException("Surface is not part of this output configuration");
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                Log.e("OutputConfigCompat", "Unable to remove surface from this output configuration.", e);
            }
        } else {
            throw new IllegalArgumentException("Cannot remove surface associated with this output configuration");
        }
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public int getMaxSharedSurfaceCount() {
        try {
            return getMaxSharedSurfaceCountApi26();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Log.e("OutputConfigCompat", "Unable to retrieve max shared surface count.", e);
            return super.getMaxSharedSurfaceCount();
        }
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatApi24Impl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public List<Surface> getSurfaces() {
        return ((OutputConfiguration) getOutputConfiguration()).getSurfaces();
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatApi24Impl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public Object getOutputConfiguration() {
        Preconditions.checkArgument(this.mObject instanceof OutputConfigurationParamsApi26);
        return ((OutputConfigurationParamsApi26) this.mObject).mOutputConfiguration;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OutputConfigurationParamsApi26 {
        final OutputConfiguration mOutputConfiguration;
        String mPhysicalCameraId;

        OutputConfigurationParamsApi26(OutputConfiguration configuration) {
            this.mOutputConfiguration = configuration;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof OutputConfigurationParamsApi26)) {
                return false;
            }
            OutputConfigurationParamsApi26 otherOutputConfig = (OutputConfigurationParamsApi26) obj;
            if (!Objects.equals(this.mOutputConfiguration, otherOutputConfig.mOutputConfiguration) || !Objects.equals(this.mPhysicalCameraId, otherOutputConfig.mPhysicalCameraId)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int h = ((1 << 5) - 1) ^ this.mOutputConfiguration.hashCode();
            int i = (h << 5) - h;
            String str = this.mPhysicalCameraId;
            return i ^ (str == null ? 0 : str.hashCode());
        }
    }
}
