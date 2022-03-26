package androidx.camera.core;

import com.google.common.util.concurrent.ListenableFuture;
/* loaded from: classes.dex */
public interface CameraControl {
    ListenableFuture<Void> cancelFocusAndMetering();

    ListenableFuture<Void> enableTorch(boolean z);

    ListenableFuture<Void> setLinearZoom(float f);

    ListenableFuture<Void> setZoomRatio(float f);

    ListenableFuture<FocusMeteringResult> startFocusAndMetering(FocusMeteringAction focusMeteringAction);

    /* loaded from: classes.dex */
    public static final class OperationCanceledException extends Exception {
        public OperationCanceledException(String message) {
            super(message);
        }

        public OperationCanceledException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
