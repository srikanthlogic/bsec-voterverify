package androidx.camera.core;

import androidx.lifecycle.LiveData;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public interface CameraInfo {
    public static final String IMPLEMENTATION_TYPE_CAMERA2;
    public static final String IMPLEMENTATION_TYPE_CAMERA2_LEGACY;
    public static final String IMPLEMENTATION_TYPE_FAKE;
    public static final String IMPLEMENTATION_TYPE_UNKNOWN;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ImplementationType {
    }

    String getImplementationType();

    int getSensorRotationDegrees();

    int getSensorRotationDegrees(int i);

    LiveData<Integer> getTorchState();

    LiveData<ZoomState> getZoomState();

    boolean hasFlashUnit();
}
