package androidx.camera.camera2.interop;

import android.hardware.camera2.CameraCharacteristics;
import androidx.camera.camera2.internal.Camera2CameraInfoImpl;
import androidx.camera.camera2.interop.Camera2CameraFilter;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraFilter;
import androidx.camera.core.CameraInfo;
import androidx.core.util.Preconditions;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
/* loaded from: classes.dex */
public final class Camera2CameraFilter {

    /* loaded from: classes.dex */
    public interface Camera2Filter {
        LinkedHashMap<String, CameraCharacteristics> filter(LinkedHashMap<String, CameraCharacteristics> linkedHashMap);
    }

    public static CameraFilter createCameraFilter(Camera2Filter filter) {
        return new CameraFilter() { // from class: androidx.camera.camera2.interop.-$$Lambda$Camera2CameraFilter$mh7N2Lu6R4FWWbPCYNntr5DkJdE
            @Override // androidx.camera.core.CameraFilter
            public final LinkedHashSet filter(LinkedHashSet linkedHashSet) {
                return Camera2CameraFilter.lambda$createCameraFilter$0(Camera2CameraFilter.Camera2Filter.this, linkedHashSet);
            }
        };
    }

    public static /* synthetic */ LinkedHashSet lambda$createCameraFilter$0(Camera2Filter filter, LinkedHashSet cameras) {
        LinkedHashMap<String, Camera> cameraMap = new LinkedHashMap<>();
        LinkedHashMap<String, CameraCharacteristics> characteristicsMap = new LinkedHashMap<>();
        Iterator it = cameras.iterator();
        while (it.hasNext()) {
            Camera camera = (Camera) it.next();
            CameraInfo cameraInfo = camera.getCameraInfo();
            Preconditions.checkState(cameraInfo instanceof Camera2CameraInfoImpl, "CameraInfo does not contain any Camera2 information.");
            Camera2CameraInfoImpl camera2CameraInfoImpl = (Camera2CameraInfoImpl) cameraInfo;
            cameraMap.put(camera2CameraInfoImpl.getCameraId(), camera);
            characteristicsMap.put(camera2CameraInfoImpl.getCameraId(), camera2CameraInfoImpl.getCameraCharacteristics());
        }
        LinkedHashMap<String, CameraCharacteristics> resultMap = filter.filter(characteristicsMap);
        LinkedHashSet<Camera> resultCameras = new LinkedHashSet<>();
        for (Map.Entry<String, CameraCharacteristics> entry : resultMap.entrySet()) {
            String cameraId = entry.getKey();
            if (cameraMap.containsKey(cameraId)) {
                resultCameras.add(cameraMap.get(cameraId));
            } else {
                throw new IllegalArgumentException("There are camera IDs not contained in the original camera map.");
            }
        }
        return resultCameras;
    }

    private Camera2CameraFilter() {
    }
}
