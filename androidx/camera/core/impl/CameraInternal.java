package androidx.camera.core.impl;

import androidx.camera.core.Camera;
import androidx.camera.core.UseCase;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collection;
/* loaded from: classes.dex */
public interface CameraInternal extends Camera, UseCase.StateChangeCallback {
    void attachUseCases(Collection<UseCase> collection);

    void close();

    void detachUseCases(Collection<UseCase> collection);

    CameraControlInternal getCameraControlInternal();

    CameraInfoInternal getCameraInfoInternal();

    Observable<State> getCameraState();

    void open();

    ListenableFuture<Void> release();

    /* loaded from: classes.dex */
    public enum State {
        PENDING_OPEN(false),
        OPENING(true),
        OPEN(true),
        CLOSING(true),
        CLOSED(false),
        RELEASING(true),
        RELEASED(false);
        
        private final boolean mHoldsCameraSlot;

        State(boolean holdsCameraSlot) {
            this.mHoldsCameraSlot = holdsCameraSlot;
        }

        public boolean holdsCameraSlot() {
            return this.mHoldsCameraSlot;
        }
    }
}
