package androidx.camera.core.impl;

import android.util.Log;
import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.InitializationException;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public final class CameraRepository {
    private static final String TAG;
    private CallbackToFutureAdapter.Completer<Void> mDeinitCompleter;
    private ListenableFuture<Void> mDeinitFuture;
    private final Object mCamerasLock = new Object();
    private final Map<String, CameraInternal> mCameras = new LinkedHashMap();
    private final Set<CameraInternal> mReleasingCameras = new HashSet();

    public void init(CameraFactory cameraFactory) throws InitializationException {
        synchronized (this.mCamerasLock) {
            try {
                try {
                    for (String id : cameraFactory.getAvailableCameraIds()) {
                        Log.d(TAG, "Added camera: " + id);
                        this.mCameras.put(id, cameraFactory.getCamera(id));
                    }
                } catch (CameraUnavailableException e) {
                    throw new InitializationException(e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public ListenableFuture<Void> deinit() {
        synchronized (this.mCamerasLock) {
            if (this.mCameras.isEmpty()) {
                return this.mDeinitFuture == null ? Futures.immediateFuture(null) : this.mDeinitFuture;
            }
            ListenableFuture<Void> currentFuture = this.mDeinitFuture;
            if (currentFuture == null) {
                currentFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.core.impl.-$$Lambda$CameraRepository$GfCuwjwqCywAr4DgCn1JSOYgBEg
                    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                        return CameraRepository.this.lambda$deinit$0$CameraRepository(completer);
                    }
                });
                this.mDeinitFuture = currentFuture;
            }
            this.mReleasingCameras.addAll(this.mCameras.values());
            for (CameraInternal cameraInternal : this.mCameras.values()) {
                cameraInternal.release().addListener(new Runnable(cameraInternal) { // from class: androidx.camera.core.impl.-$$Lambda$CameraRepository$vZy2hmnvCLGH5kuV_-iaqXOZ0ng
                    private final /* synthetic */ CameraInternal f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraRepository.this.lambda$deinit$1$CameraRepository(this.f$1);
                    }
                }, CameraXExecutors.directExecutor());
            }
            this.mCameras.clear();
            return currentFuture;
        }
    }

    public /* synthetic */ Object lambda$deinit$0$CameraRepository(CallbackToFutureAdapter.Completer completer) throws Exception {
        synchronized (this.mCamerasLock) {
            this.mDeinitCompleter = completer;
        }
        return "CameraRepository-deinit";
    }

    public /* synthetic */ void lambda$deinit$1$CameraRepository(CameraInternal cameraInternal) {
        synchronized (this.mCamerasLock) {
            this.mReleasingCameras.remove(cameraInternal);
            if (this.mReleasingCameras.isEmpty()) {
                Preconditions.checkNotNull(this.mDeinitCompleter);
                this.mDeinitCompleter.set(null);
                this.mDeinitCompleter = null;
                this.mDeinitFuture = null;
            }
        }
    }

    public CameraInternal getCamera(String cameraId) {
        CameraInternal cameraInternal;
        synchronized (this.mCamerasLock) {
            cameraInternal = this.mCameras.get(cameraId);
            if (cameraInternal == null) {
                throw new IllegalArgumentException("Invalid camera: " + cameraId);
            }
        }
        return cameraInternal;
    }

    public LinkedHashSet<CameraInternal> getCameras() {
        LinkedHashSet<CameraInternal> linkedHashSet;
        synchronized (this.mCamerasLock) {
            linkedHashSet = new LinkedHashSet<>(this.mCameras.values());
        }
        return linkedHashSet;
    }

    Set<String> getCameraIds() {
        LinkedHashSet linkedHashSet;
        synchronized (this.mCamerasLock) {
            linkedHashSet = new LinkedHashSet(this.mCameras.keySet());
        }
        return linkedHashSet;
    }
}
