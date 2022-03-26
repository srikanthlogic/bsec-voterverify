package androidx.camera.core.impl;

import android.util.Log;
import androidx.camera.core.Camera;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.CameraStateRegistry;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
/* loaded from: classes.dex */
public final class CameraStateRegistry {
    private int mAvailableCameras;
    private final Map<Camera, CameraRegistration> mCameraStates;
    private StringBuilder mDebugString;
    private final Object mLock;
    private final int mMaxAllowedOpenedCameras;
    private static final String TAG;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    /* loaded from: classes.dex */
    public interface OnOpenAvailableListener {
        void onOpenAvailable();
    }

    public CameraStateRegistry(int maxAllowedOpenedCameras) {
        this.mDebugString = DEBUG ? new StringBuilder() : null;
        this.mLock = new Object();
        this.mCameraStates = new HashMap();
        this.mMaxAllowedOpenedCameras = maxAllowedOpenedCameras;
        synchronized ("mLock") {
            this.mAvailableCameras = this.mMaxAllowedOpenedCameras;
        }
    }

    public void registerCamera(Camera camera, Executor notifyExecutor, OnOpenAvailableListener cameraAvailableListener) {
        synchronized (this.mLock) {
            boolean z = !this.mCameraStates.containsKey(camera);
            Preconditions.checkState(z, "Camera is already registered: " + camera);
            this.mCameraStates.put(camera, new CameraRegistration(null, notifyExecutor, cameraAvailableListener));
        }
    }

    public boolean tryOpenCamera(Camera camera) {
        boolean success;
        synchronized (this.mLock) {
            CameraRegistration registration = (CameraRegistration) Preconditions.checkNotNull(this.mCameraStates.get(camera), "Camera must first be registered with registerCamera()");
            success = false;
            if (DEBUG) {
                this.mDebugString.setLength(0);
                this.mDebugString.append(String.format(Locale.US, "tryOpenCamera(%s) [Available Cameras: %d, Already Open: %b (Previous state: %s)]", camera, Integer.valueOf(this.mAvailableCameras), Boolean.valueOf(isOpen(registration.getState())), registration.getState()));
            }
            if (this.mAvailableCameras > 0 || isOpen(registration.getState())) {
                registration.setState(CameraInternal.State.OPENING);
                success = true;
            }
            if (DEBUG) {
                StringBuilder sb = this.mDebugString;
                Locale locale = Locale.US;
                Object[] objArr = new Object[1];
                objArr[0] = success ? "SUCCESS" : "FAIL";
                sb.append(String.format(locale, " --> %s", objArr));
                Log.d(TAG, this.mDebugString.toString());
            }
            if (success) {
                recalculateAvailableCameras();
            }
        }
        return success;
    }

    public void markCameraState(Camera camera, CameraInternal.State state) {
        CameraInternal.State previousState;
        List<CameraRegistration> camerasToNotify = null;
        synchronized (this.mLock) {
            int previousAvailableCameras = this.mAvailableCameras;
            if (state == CameraInternal.State.RELEASED) {
                previousState = unregisterCamera(camera);
            } else {
                previousState = updateAndVerifyState(camera, state);
            }
            if (previousState != state) {
                if (previousAvailableCameras < 1 && this.mAvailableCameras > 0) {
                    camerasToNotify = new ArrayList<>();
                    for (Map.Entry<Camera, CameraRegistration> entry : this.mCameraStates.entrySet()) {
                        if (entry.getValue().getState() == CameraInternal.State.PENDING_OPEN) {
                            camerasToNotify.add(entry.getValue());
                        }
                    }
                } else if (state == CameraInternal.State.PENDING_OPEN && this.mAvailableCameras > 0) {
                    camerasToNotify = Collections.singletonList(this.mCameraStates.get(camera));
                }
                if (camerasToNotify != null) {
                    for (CameraRegistration registration : camerasToNotify) {
                        registration.notifyListener();
                    }
                }
            }
        }
    }

    private CameraInternal.State unregisterCamera(Camera camera) {
        CameraRegistration registration = this.mCameraStates.remove(camera);
        if (registration == null) {
            return null;
        }
        recalculateAvailableCameras();
        return registration.getState();
    }

    private CameraInternal.State updateAndVerifyState(Camera camera, CameraInternal.State state) {
        CameraInternal.State previousState = ((CameraRegistration) Preconditions.checkNotNull(this.mCameraStates.get(camera), "Cannot update state of camera which has not yet been registered. Register with CameraAvailabilityRegistry.registerCamera()")).setState(state);
        if (state == CameraInternal.State.OPENING) {
            Preconditions.checkState(isOpen(state) || previousState == CameraInternal.State.OPENING, "Cannot mark camera as opening until camera was successful at calling CameraAvailabilityRegistry.tryOpen()");
        }
        if (previousState != state) {
            recalculateAvailableCameras();
        }
        return previousState;
    }

    private static boolean isOpen(CameraInternal.State state) {
        return state != null && state.holdsCameraSlot();
    }

    private void recalculateAvailableCameras() {
        String stateString;
        if (DEBUG) {
            this.mDebugString.setLength(0);
            this.mDebugString.append("Recalculating open cameras:\n");
            this.mDebugString.append(String.format(Locale.US, "%-45s%-22s\n", "Camera", "State"));
            this.mDebugString.append("-------------------------------------------------------------------\n");
        }
        int openCount = 0;
        for (Map.Entry<Camera, CameraRegistration> entry : this.mCameraStates.entrySet()) {
            if (DEBUG) {
                if (entry.getValue().getState() != null) {
                    stateString = entry.getValue().getState().toString();
                } else {
                    stateString = "UNKNOWN";
                }
                this.mDebugString.append(String.format(Locale.US, "%-45s%-22s\n", entry.getKey().toString(), stateString));
            }
            if (isOpen(entry.getValue().getState())) {
                openCount++;
            }
        }
        if (DEBUG) {
            this.mDebugString.append("-------------------------------------------------------------------\n");
            this.mDebugString.append(String.format(Locale.US, "Open count: %d (Max allowed: %d)", Integer.valueOf(openCount), Integer.valueOf(this.mMaxAllowedOpenedCameras)));
            Log.d(TAG, this.mDebugString.toString());
        }
        this.mAvailableCameras = Math.max(this.mMaxAllowedOpenedCameras - openCount, 0);
    }

    /* loaded from: classes.dex */
    public static class CameraRegistration {
        private final OnOpenAvailableListener mCameraAvailableListener;
        private final Executor mNotifyExecutor;
        private CameraInternal.State mState;

        CameraRegistration(CameraInternal.State initialState, Executor notifyExecutor, OnOpenAvailableListener cameraAvailableListener) {
            this.mState = initialState;
            this.mNotifyExecutor = notifyExecutor;
            this.mCameraAvailableListener = cameraAvailableListener;
        }

        CameraInternal.State setState(CameraInternal.State state) {
            CameraInternal.State previousState = this.mState;
            this.mState = state;
            return previousState;
        }

        CameraInternal.State getState() {
            return this.mState;
        }

        void notifyListener() {
            try {
                Executor executor = this.mNotifyExecutor;
                OnOpenAvailableListener onOpenAvailableListener = this.mCameraAvailableListener;
                Objects.requireNonNull(onOpenAvailableListener);
                executor.execute(new Runnable() { // from class: androidx.camera.core.impl.-$$Lambda$q0-737uiXXnVhEez-6fh5OPQDB0
                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraStateRegistry.OnOpenAvailableListener.this.onOpenAvailable();
                    }
                });
            } catch (RejectedExecutionException e) {
                Log.e(CameraStateRegistry.TAG, "Unable to notify camera.", e);
            }
        }
    }
}
