package androidx.camera.core.internal;

import android.graphics.Rect;
import android.util.Log;
import android.util.Size;
import androidx.camera.core.UseCase;
import androidx.camera.core.ViewPort;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.CameraDeviceSurfaceManager;
import androidx.camera.core.impl.CameraInfoInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.SurfaceConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class CameraUseCaseAdapter {
    private static final String TAG;
    private final CameraDeviceSurfaceManager mCameraDeviceSurfaceManager;
    private final CameraInternal mCameraInternal;
    private final LinkedHashSet<CameraInternal> mCameraInternals;
    private final CameraId mId;
    private ViewPort mViewPort;
    private final List<UseCase> mUseCases = new ArrayList();
    private final Object mLock = new Object();
    private boolean mAttached = true;

    public CameraUseCaseAdapter(CameraInternal cameraInternal, LinkedHashSet<CameraInternal> cameras, CameraDeviceSurfaceManager cameraDeviceSurfaceManager) {
        this.mCameraInternal = cameraInternal;
        this.mCameraInternals = new LinkedHashSet<>(cameras);
        this.mId = new CameraId(this.mCameraInternals);
        this.mCameraDeviceSurfaceManager = cameraDeviceSurfaceManager;
    }

    public static CameraId generateCameraId(LinkedHashSet<CameraInternal> cameras) {
        return new CameraId(cameras);
    }

    public CameraId getCameraId() {
        return this.mId;
    }

    public boolean isEquivalent(CameraUseCaseAdapter cameraUseCaseAdapter) {
        return this.mId.equals(cameraUseCaseAdapter.getCameraId());
    }

    public void setViewPort(ViewPort viewPort) {
        synchronized (this.mLock) {
            this.mViewPort = viewPort;
        }
    }

    public void checkAttachUseCases(List<UseCase> useCases) throws CameraException {
        if (UseCaseOccupancy.checkUseCaseLimitNotExceeded(useCases)) {
            try {
                calculateSuggestedResolutions(useCases, Collections.emptyList());
            } catch (IllegalArgumentException e) {
                throw new CameraException(e.getMessage());
            }
        } else {
            throw new CameraException("Attempting to bind too many ImageCapture or VideoCapture instances");
        }
    }

    public void addUseCases(Collection<UseCase> useCases) throws CameraException {
        synchronized (this.mLock) {
            List<UseCase> useCaseListAfterUpdate = new ArrayList<>(this.mUseCases);
            List<UseCase> newUseCases = new ArrayList<>();
            for (UseCase useCase : useCases) {
                if (this.mUseCases.contains(useCase)) {
                    Log.e(TAG, "Attempting to attach already attached UseCase");
                } else {
                    useCaseListAfterUpdate.add(useCase);
                    newUseCases.add(useCase);
                }
            }
            if (UseCaseOccupancy.checkUseCaseLimitNotExceeded(useCaseListAfterUpdate)) {
                try {
                    Map<UseCase, Size> suggestedResolutionsMap = calculateSuggestedResolutions(newUseCases, this.mUseCases);
                    if (this.mViewPort != null) {
                        Map<UseCase, Rect> cropRectMap = ViewPorts.calculateViewPortRects(this.mCameraInternal.getCameraControlInternal().getSensorRect(), this.mViewPort.getAspectRatio(), this.mCameraInternal.getCameraInfoInternal().getSensorRotationDegrees(this.mViewPort.getRotation()), this.mViewPort.getScaleType(), this.mViewPort.getLayoutDirection(), suggestedResolutionsMap);
                        for (UseCase useCase2 : useCases) {
                            useCase2.setViewPortCropRect(cropRectMap.get(useCase2));
                        }
                    }
                    for (UseCase useCase3 : newUseCases) {
                        useCase3.onAttach(this.mCameraInternal);
                        useCase3.updateSuggestedResolution((Size) Preconditions.checkNotNull(suggestedResolutionsMap.get(useCase3)));
                    }
                    this.mUseCases.addAll(newUseCases);
                    if (this.mAttached) {
                        this.mCameraInternal.attachUseCases(newUseCases);
                    }
                    for (UseCase useCase4 : newUseCases) {
                        useCase4.notifyState();
                    }
                } catch (IllegalArgumentException e) {
                    throw new CameraException(e.getMessage());
                }
            } else {
                throw new CameraException("Attempting to bind too many ImageCapture or VideoCapture instances");
            }
        }
    }

    public void removeUseCases(Collection<UseCase> useCases) {
        synchronized (this.mLock) {
            this.mCameraInternal.detachUseCases(useCases);
            for (UseCase useCase : useCases) {
                if (this.mUseCases.contains(useCase)) {
                    useCase.onDetach(this.mCameraInternal);
                    useCase.onDestroy();
                } else {
                    Log.e(TAG, "Attempting to detach non-attached UseCase: " + useCase);
                }
            }
            this.mUseCases.removeAll(useCases);
        }
    }

    public List<UseCase> getUseCases() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mUseCases);
        }
        return arrayList;
    }

    public void attachUseCases() {
        synchronized (this.mLock) {
            if (!this.mAttached) {
                this.mCameraInternal.attachUseCases(this.mUseCases);
                this.mAttached = true;
            }
        }
    }

    public void detachUseCases() {
        synchronized (this.mLock) {
            if (this.mAttached) {
                this.mCameraInternal.detachUseCases(this.mUseCases);
                this.mAttached = false;
            }
        }
    }

    private Map<UseCase, Size> calculateSuggestedResolutions(List<UseCase> newUseCases, List<UseCase> currentUseCases) {
        List<SurfaceConfig> existingSurfaces = new ArrayList<>();
        String cameraId = this.mCameraInternal.getCameraInfoInternal().getCameraId();
        Map<UseCaseConfig<?>, UseCase> configToUseCaseMap = new HashMap<>();
        for (UseCase useCase : currentUseCases) {
            existingSurfaces.add(this.mCameraDeviceSurfaceManager.transformSurfaceConfig(cameraId, useCase.getImageFormat(), useCase.getAttachedSurfaceResolution()));
        }
        for (UseCase useCase2 : newUseCases) {
            configToUseCaseMap.put(useCase2.applyDefaults(useCase2.getUseCaseConfig(), useCase2.getDefaultBuilder(this.mCameraInternal.getCameraInfoInternal())), useCase2);
        }
        Map<UseCaseConfig<?>, Size> useCaseConfigSizeMap = this.mCameraDeviceSurfaceManager.getSuggestedResolutions(cameraId, existingSurfaces, new ArrayList(configToUseCaseMap.keySet()));
        Map<UseCase, Size> suggestedResolutions = new HashMap<>();
        for (Map.Entry<UseCaseConfig<?>, UseCase> entry : configToUseCaseMap.entrySet()) {
            suggestedResolutions.put(entry.getValue(), useCaseConfigSizeMap.get(entry.getKey()));
        }
        return suggestedResolutions;
    }

    public CameraInfoInternal getCameraInfoInternal() {
        return this.mCameraInternal.getCameraInfoInternal();
    }

    public CameraControlInternal getCameraControlInternal() {
        return this.mCameraInternal.getCameraControlInternal();
    }

    /* loaded from: classes.dex */
    public static final class CameraId {
        private final List<String> mIds = new ArrayList();

        CameraId(LinkedHashSet<CameraInternal> cameraInternals) {
            Iterator<CameraInternal> it = cameraInternals.iterator();
            while (it.hasNext()) {
                this.mIds.add(it.next().getCameraInfoInternal().getCameraId());
            }
        }

        public boolean equals(Object cameraId) {
            if (cameraId instanceof CameraId) {
                return this.mIds.equals(((CameraId) cameraId).mIds);
            }
            return false;
        }

        public int hashCode() {
            return this.mIds.hashCode() * 53;
        }
    }

    /* loaded from: classes.dex */
    public static final class CameraException extends Exception {
        public CameraException() {
        }

        public CameraException(String message) {
            super(message);
        }

        public CameraException(Throwable cause) {
            super(cause);
        }
    }
}
