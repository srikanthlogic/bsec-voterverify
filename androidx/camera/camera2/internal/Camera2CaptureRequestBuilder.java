package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.util.Log;
import android.view.Surface;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Camera2CaptureRequestBuilder {
    private static final String TAG = "CaptureRequestBuilder";

    private Camera2CaptureRequestBuilder() {
    }

    private static List<Surface> getConfiguredSurfaces(List<DeferrableSurface> deferrableSurfaces, Map<DeferrableSurface, Surface> configuredSurfaceMap) {
        List<Surface> surfaceList = new ArrayList<>();
        for (DeferrableSurface deferrableSurface : deferrableSurfaces) {
            Surface surface = configuredSurfaceMap.get(deferrableSurface);
            if (surface != null) {
                surfaceList.add(surface);
            } else {
                throw new IllegalArgumentException("DeferrableSurface not in configuredSurfaceMap");
            }
        }
        return surfaceList;
    }

    private static void applyImplementationOptionToCaptureBuilder(CaptureRequest.Builder builder, Config config) {
        Camera2ImplConfig camera2Config = new Camera2ImplConfig(config);
        for (Config.Option<?> option : camera2Config.getCaptureRequestOptions()) {
            CaptureRequest.Key<Object> key = (CaptureRequest.Key) option.getToken();
            try {
                builder.set(key, camera2Config.retrieveOption(option));
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "CaptureRequest.Key is not supported: " + key);
            }
        }
    }

    public static CaptureRequest build(CaptureConfig captureConfig, CameraDevice device, Map<DeferrableSurface, Surface> configuredSurfaceMap) throws CameraAccessException {
        if (device == null) {
            return null;
        }
        List<Surface> surfaceList = getConfiguredSurfaces(captureConfig.getSurfaces(), configuredSurfaceMap);
        if (surfaceList.isEmpty()) {
            return null;
        }
        CaptureRequest.Builder builder = device.createCaptureRequest(captureConfig.getTemplateType());
        applyImplementationOptionToCaptureBuilder(builder, captureConfig.getImplementationOptions());
        if (captureConfig.getImplementationOptions().containsOption(CaptureConfig.OPTION_ROTATION)) {
            builder.set(CaptureRequest.JPEG_ORIENTATION, (Integer) captureConfig.getImplementationOptions().retrieveOption(CaptureConfig.OPTION_ROTATION));
        }
        if (captureConfig.getImplementationOptions().containsOption(CaptureConfig.OPTION_JPEG_QUALITY)) {
            builder.set(CaptureRequest.JPEG_QUALITY, Byte.valueOf(((Integer) captureConfig.getImplementationOptions().retrieveOption(CaptureConfig.OPTION_JPEG_QUALITY)).byteValue()));
        }
        for (Surface surface : surfaceList) {
            builder.addTarget(surface);
        }
        builder.setTag(captureConfig.getTag());
        return builder.build();
    }

    public static CaptureRequest buildWithoutTarget(CaptureConfig captureConfig, CameraDevice device) throws CameraAccessException {
        if (device == null) {
            return null;
        }
        CaptureRequest.Builder builder = device.createCaptureRequest(captureConfig.getTemplateType());
        applyImplementationOptionToCaptureBuilder(builder, captureConfig.getImplementationOptions());
        return builder.build();
    }
}
