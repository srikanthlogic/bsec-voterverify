package androidx.camera.camera2.internal.compat.params;

import android.hardware.camera2.params.OutputConfiguration;
import android.view.Surface;
import androidx.core.util.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class OutputConfigurationCompatApi24Impl extends OutputConfigurationCompatBaseImpl {
    public OutputConfigurationCompatApi24Impl(Surface surface) {
        this(new OutputConfigurationParamsApi24(new OutputConfiguration(surface)));
    }

    public OutputConfigurationCompatApi24Impl(Object outputConfiguration) {
        super(outputConfiguration);
    }

    public static OutputConfigurationCompatApi24Impl wrap(OutputConfiguration outputConfiguration) {
        return new OutputConfigurationCompatApi24Impl(new OutputConfigurationParamsApi24(outputConfiguration));
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void enableSurfaceSharing() {
        ((OutputConfigurationParamsApi24) this.mObject).mIsShared = true;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl
    boolean isSurfaceSharingEnabled() {
        return ((OutputConfigurationParamsApi24) this.mObject).mIsShared;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public void setPhysicalCameraId(String physicalCameraId) {
        ((OutputConfigurationParamsApi24) this.mObject).mPhysicalCameraId = physicalCameraId;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public String getPhysicalCameraId() {
        return ((OutputConfigurationParamsApi24) this.mObject).mPhysicalCameraId;
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public Surface getSurface() {
        return ((OutputConfiguration) getOutputConfiguration()).getSurface();
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public List<Surface> getSurfaces() {
        return Collections.singletonList(getSurface());
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public int getSurfaceGroupId() {
        return ((OutputConfiguration) getOutputConfiguration()).getSurfaceGroupId();
    }

    @Override // androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatBaseImpl, androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.OutputConfigurationCompatImpl
    public Object getOutputConfiguration() {
        Preconditions.checkArgument(this.mObject instanceof OutputConfigurationParamsApi24);
        return ((OutputConfigurationParamsApi24) this.mObject).mOutputConfiguration;
    }

    /* loaded from: classes.dex */
    public static final class OutputConfigurationParamsApi24 {
        boolean mIsShared;
        final OutputConfiguration mOutputConfiguration;
        String mPhysicalCameraId;

        OutputConfigurationParamsApi24(OutputConfiguration configuration) {
            this.mOutputConfiguration = configuration;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof OutputConfigurationParamsApi24)) {
                return false;
            }
            OutputConfigurationParamsApi24 otherOutputConfig = (OutputConfigurationParamsApi24) obj;
            if (!Objects.equals(this.mOutputConfiguration, otherOutputConfig.mOutputConfiguration) || this.mIsShared != otherOutputConfig.mIsShared || !Objects.equals(this.mPhysicalCameraId, otherOutputConfig.mPhysicalCameraId)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int h = ((1 << 5) - 1) ^ this.mOutputConfiguration.hashCode();
            int h2 = ((h << 5) - h) ^ (this.mIsShared ? 1 : 0);
            int i = (h2 << 5) - h2;
            String str = this.mPhysicalCameraId;
            return i ^ (str == null ? 0 : str.hashCode());
        }
    }
}
