package androidx.camera.lifecycle;

import android.content.Context;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraFilter;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.UseCase;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.utils.Threads;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.core.internal.CameraUseCaseAdapter;
import androidx.core.util.Preconditions;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
/* loaded from: classes.dex */
public final class ProcessCameraProvider implements LifecycleCameraProvider {
    private static final ProcessCameraProvider sAppInstance = new ProcessCameraProvider();
    private CameraX mCameraX;
    private final LifecycleCameraRepository mLifecycleCameraRepository = new LifecycleCameraRepository();

    public static ListenableFuture<ProcessCameraProvider> getInstance(Context context) {
        Preconditions.checkNotNull(context);
        return Futures.transform(CameraX.getOrCreateInstance(context), $$Lambda$ProcessCameraProvider$TYjfluwv4_m1lcHTHHt4JLTu5vc.INSTANCE, CameraXExecutors.directExecutor());
    }

    public static void configureInstance(CameraXConfig cameraXConfig) {
        CameraX.configureInstance(cameraXConfig);
    }

    public ListenableFuture<Void> shutdown() {
        this.mLifecycleCameraRepository.clear();
        return CameraX.shutdown();
    }

    public void setCameraX(CameraX cameraX) {
        this.mCameraX = cameraX;
    }

    public Camera bindToLifecycle(LifecycleOwner lifecycleOwner, CameraSelector cameraSelector, UseCase... useCases) {
        return bindToLifecycle(lifecycleOwner, cameraSelector, null, useCases);
    }

    public Camera bindToLifecycle(LifecycleOwner lifecycleOwner, CameraSelector cameraSelector, UseCaseGroup useCaseGroup) {
        return bindToLifecycle(lifecycleOwner, cameraSelector, useCaseGroup.getViewPort(), (UseCase[]) useCaseGroup.getUseCases().toArray(new UseCase[0]));
    }

    public Camera bindToLifecycle(LifecycleOwner lifecycleOwner, CameraSelector cameraSelector, ViewPort viewPort, UseCase... useCases) {
        Threads.checkMainThread();
        CameraSelector.Builder selectorBuilder = CameraSelector.Builder.fromSelector(cameraSelector);
        for (UseCase useCase : useCases) {
            CameraSelector selector = useCase.getUseCaseConfig().getCameraSelector(null);
            if (selector != null) {
                Iterator<CameraFilter> it = selector.getCameraFilterSet().iterator();
                while (it.hasNext()) {
                    selectorBuilder.addCameraFilter(it.next());
                }
            }
        }
        LinkedHashSet<CameraInternal> cameraInternals = selectorBuilder.build().filter(this.mCameraX.getCameraRepository().getCameras());
        LifecycleCamera lifecycleCameraToBind = this.mLifecycleCameraRepository.getLifecycleCamera(lifecycleOwner, CameraUseCaseAdapter.generateCameraId(cameraInternals));
        Collection<LifecycleCamera> lifecycleCameras = this.mLifecycleCameraRepository.getLifecycleCameras();
        for (UseCase useCase2 : useCases) {
            for (LifecycleCamera lifecycleCamera : lifecycleCameras) {
                if (lifecycleCamera.isBound(useCase2) && lifecycleCamera != lifecycleCameraToBind) {
                    throw new IllegalStateException(String.format("Use case %s already bound to a different lifecycle.", useCase2));
                }
            }
        }
        if (lifecycleCameraToBind == null) {
            lifecycleCameraToBind = this.mLifecycleCameraRepository.createLifecycleCamera(lifecycleOwner, new CameraUseCaseAdapter(cameraInternals.iterator().next(), cameraInternals, this.mCameraX.getCameraDeviceSurfaceManager()));
        }
        if (useCases.length == 0) {
            return lifecycleCameraToBind;
        }
        this.mLifecycleCameraRepository.bindToLifecycleCamera(lifecycleCameraToBind, viewPort, Arrays.asList(useCases));
        return lifecycleCameraToBind;
    }

    @Override // androidx.camera.lifecycle.LifecycleCameraProvider
    public boolean isBound(UseCase useCase) {
        for (LifecycleCamera lifecycleCamera : this.mLifecycleCameraRepository.getLifecycleCameras()) {
            if (lifecycleCamera.isBound(useCase)) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.camera.lifecycle.LifecycleCameraProvider
    public void unbind(UseCase... useCases) {
        Threads.checkMainThread();
        this.mLifecycleCameraRepository.unbind(Arrays.asList(useCases));
    }

    @Override // androidx.camera.lifecycle.LifecycleCameraProvider
    public void unbindAll() {
        Threads.checkMainThread();
        this.mLifecycleCameraRepository.unbindAll();
    }

    @Override // androidx.camera.lifecycle.LifecycleCameraProvider
    public boolean hasCamera(CameraSelector cameraSelector) throws CameraInfoUnavailableException {
        return CameraX.hasCamera(cameraSelector);
    }

    private ProcessCameraProvider() {
    }
}
