package androidx.camera.lifecycle;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.UseCase;
import androidx.camera.core.internal.CameraUseCaseAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class LifecycleCamera implements LifecycleObserver, Camera {
    private final CameraUseCaseAdapter mCameraUseCaseAdapter;
    private final LifecycleOwner mLifecycleOwner;
    private final Object mLock = new Object();
    private volatile boolean mIsActive = false;
    private boolean mSuspended = false;
    private boolean mReleased = false;

    public LifecycleCamera(LifecycleOwner lifecycleOwner, CameraUseCaseAdapter cameraUseCaseAdaptor) {
        this.mLifecycleOwner = lifecycleOwner;
        this.mCameraUseCaseAdapter = cameraUseCaseAdaptor;
        if (this.mLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            this.mCameraUseCaseAdapter.attachUseCases();
        } else {
            this.mCameraUseCaseAdapter.detachUseCases();
        }
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(LifecycleOwner lifecycleOwner) {
        synchronized (this.mLock) {
            if (!this.mSuspended && !this.mReleased) {
                this.mCameraUseCaseAdapter.attachUseCases();
                this.mIsActive = true;
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(LifecycleOwner lifecycleOwner) {
        synchronized (this.mLock) {
            if (!this.mSuspended && !this.mReleased) {
                this.mCameraUseCaseAdapter.detachUseCases();
                this.mIsActive = false;
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        synchronized (this.mLock) {
            this.mCameraUseCaseAdapter.removeUseCases(this.mCameraUseCaseAdapter.getUseCases());
        }
    }

    public void suspend() {
        synchronized (this.mLock) {
            if (!this.mSuspended) {
                onStop(this.mLifecycleOwner);
                this.mSuspended = true;
            }
        }
    }

    public void unsuspend() {
        synchronized (this.mLock) {
            if (this.mSuspended) {
                this.mSuspended = false;
                if (this.mLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    onStart(this.mLifecycleOwner);
                }
            }
        }
    }

    public boolean isActive() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsActive;
        }
        return z;
    }

    public boolean isBound(UseCase useCase) {
        boolean contains;
        synchronized (this.mLock) {
            contains = this.mCameraUseCaseAdapter.getUseCases().contains(useCase);
        }
        return contains;
    }

    public List<UseCase> getUseCases() {
        List<UseCase> unmodifiableList;
        synchronized (this.mLock) {
            unmodifiableList = Collections.unmodifiableList(this.mCameraUseCaseAdapter.getUseCases());
        }
        return unmodifiableList;
    }

    public LifecycleOwner getLifecycleOwner() {
        LifecycleOwner lifecycleOwner;
        synchronized (this.mLock) {
            lifecycleOwner = this.mLifecycleOwner;
        }
        return lifecycleOwner;
    }

    public CameraUseCaseAdapter getCameraUseCaseAdapter() {
        return this.mCameraUseCaseAdapter;
    }

    public void bind(Collection<UseCase> useCases) throws CameraUseCaseAdapter.CameraException {
        synchronized (this.mLock) {
            this.mCameraUseCaseAdapter.addUseCases(useCases);
        }
    }

    public void unbind(Collection<UseCase> useCases) {
        synchronized (this.mLock) {
            List<UseCase> useCasesToRemove = new ArrayList<>(useCases);
            useCasesToRemove.retainAll(this.mCameraUseCaseAdapter.getUseCases());
            this.mCameraUseCaseAdapter.removeUseCases(useCasesToRemove);
        }
    }

    public void unbindAll() {
        synchronized (this.mLock) {
            this.mCameraUseCaseAdapter.removeUseCases(this.mCameraUseCaseAdapter.getUseCases());
        }
    }

    void release() {
        synchronized (this.mLock) {
            this.mReleased = true;
            this.mIsActive = false;
            this.mLifecycleOwner.getLifecycle().removeObserver(this);
        }
    }

    @Override // androidx.camera.core.Camera
    public CameraControl getCameraControl() {
        return this.mCameraUseCaseAdapter.getCameraControlInternal();
    }

    @Override // androidx.camera.core.Camera
    public CameraInfo getCameraInfo() {
        return this.mCameraUseCaseAdapter.getCameraInfoInternal();
    }
}
