package androidx.camera.camera2.internal;

import android.content.Context;
import android.util.Size;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.camera2.internal.compat.CameraManagerCompat;
import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.impl.CameraDeviceSurfaceManager;
import androidx.camera.core.impl.SurfaceConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class Camera2DeviceSurfaceManager implements CameraDeviceSurfaceManager {
    private static final Size MAXIMUM_PREVIEW_SIZE = new Size(1920, 1080);
    private static final String TAG;
    private final CamcorderProfileHelper mCamcorderProfileHelper;
    private final Map<String, SupportedSurfaceCombination> mCameraSupportedSurfaceCombinationMap;

    public Camera2DeviceSurfaceManager(Context context) throws CameraUnavailableException {
        this(context, $$Lambda$Camera2DeviceSurfaceManager$U7YGfX89lmJkjkmxjZTvW1ZUo0.INSTANCE);
    }

    Camera2DeviceSurfaceManager(Context context, CamcorderProfileHelper camcorderProfileHelper) throws CameraUnavailableException {
        this.mCameraSupportedSurfaceCombinationMap = new HashMap();
        Preconditions.checkNotNull(camcorderProfileHelper);
        this.mCamcorderProfileHelper = camcorderProfileHelper;
        init(context);
    }

    private void init(Context context) throws CameraUnavailableException {
        Preconditions.checkNotNull(context);
        try {
            String[] cameraIdList = CameraManagerCompat.from(context).getCameraIdList();
            for (String cameraId : cameraIdList) {
                this.mCameraSupportedSurfaceCombinationMap.put(cameraId, new SupportedSurfaceCombination(context, cameraId, this.mCamcorderProfileHelper));
            }
        } catch (CameraAccessExceptionCompat e) {
            throw CameraUnavailableExceptionHelper.createFrom(e);
        }
    }

    @Override // androidx.camera.core.impl.CameraDeviceSurfaceManager
    public boolean checkSupported(String cameraId, List<SurfaceConfig> surfaceConfigList) {
        if (surfaceConfigList == null || surfaceConfigList.isEmpty()) {
            return true;
        }
        SupportedSurfaceCombination supportedSurfaceCombination = this.mCameraSupportedSurfaceCombinationMap.get(cameraId);
        if (supportedSurfaceCombination != null) {
            return supportedSurfaceCombination.checkSupported(surfaceConfigList);
        }
        return false;
    }

    @Override // androidx.camera.core.impl.CameraDeviceSurfaceManager
    public SurfaceConfig transformSurfaceConfig(String cameraId, int imageFormat, Size size) {
        SupportedSurfaceCombination supportedSurfaceCombination = this.mCameraSupportedSurfaceCombinationMap.get(cameraId);
        if (supportedSurfaceCombination != null) {
            return supportedSurfaceCombination.transformSurfaceConfig(imageFormat, size);
        }
        return null;
    }

    @Override // androidx.camera.core.impl.CameraDeviceSurfaceManager
    public Map<UseCaseConfig<?>, Size> getSuggestedResolutions(String cameraId, List<SurfaceConfig> existingSurfaces, List<UseCaseConfig<?>> newUseCaseConfigs) {
        Preconditions.checkArgument(!newUseCaseConfigs.isEmpty(), "No new use cases to be bound.");
        List<SurfaceConfig> surfaceConfigs = new ArrayList<>(existingSurfaces);
        for (UseCaseConfig<?> useCaseConfig : newUseCaseConfigs) {
            surfaceConfigs.add(transformSurfaceConfig(cameraId, useCaseConfig.getInputFormat(), new Size(640, 480)));
        }
        SupportedSurfaceCombination supportedSurfaceCombination = this.mCameraSupportedSurfaceCombinationMap.get(cameraId);
        if (supportedSurfaceCombination == null) {
            throw new IllegalArgumentException("No such camera id in supported combination list: " + cameraId);
        } else if (supportedSurfaceCombination.checkSupported(surfaceConfigs)) {
            return supportedSurfaceCombination.getSuggestedResolutions(existingSurfaces, newUseCaseConfigs);
        } else {
            throw new IllegalArgumentException("No supported surface combination is found for camera device - Id : " + cameraId + ".  May be attempting to bind too many use cases. Existing surfaces: " + existingSurfaces + " New configs: " + newUseCaseConfigs);
        }
    }

    @Override // androidx.camera.core.impl.CameraDeviceSurfaceManager
    public Size getMaxOutputSize(String cameraId, int imageFormat) {
        SupportedSurfaceCombination supportedSurfaceCombination = this.mCameraSupportedSurfaceCombinationMap.get(cameraId);
        if (supportedSurfaceCombination != null) {
            return supportedSurfaceCombination.getMaxOutputSizeByFormat(imageFormat);
        }
        throw new IllegalArgumentException("Fail to find supported surface info - CameraId:" + cameraId);
    }

    @Override // androidx.camera.core.impl.CameraDeviceSurfaceManager
    public Size getPreviewSize() {
        Size previewSize = MAXIMUM_PREVIEW_SIZE;
        if (this.mCameraSupportedSurfaceCombinationMap.isEmpty()) {
            return previewSize;
        }
        return this.mCameraSupportedSurfaceCombinationMap.get((String) this.mCameraSupportedSurfaceCombinationMap.keySet().toArray()[0]).getSurfaceSizeDefinition().getPreviewSize();
    }
}
