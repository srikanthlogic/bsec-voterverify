package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
class CameraBurstCaptureCallback extends CameraCaptureSession.CaptureCallback {
    final Map<CaptureRequest, List<CameraCaptureSession.CaptureCallback>> mCallbackMap = new HashMap();

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureBufferLost(CameraCaptureSession session, CaptureRequest request, Surface surface, long frame) {
        for (CameraCaptureSession.CaptureCallback callback : getCallbacks(request)) {
            callback.onCaptureBufferLost(session, request, surface, frame);
        }
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
        for (CameraCaptureSession.CaptureCallback callback : getCallbacks(request)) {
            callback.onCaptureCompleted(session, request, result);
        }
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
        for (CameraCaptureSession.CaptureCallback callback : getCallbacks(request)) {
            callback.onCaptureFailed(session, request, failure);
        }
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
        for (CameraCaptureSession.CaptureCallback callback : getCallbacks(request)) {
            callback.onCaptureProgressed(session, request, partialResult);
        }
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
        for (CameraCaptureSession.CaptureCallback callback : getCallbacks(request)) {
            callback.onCaptureStarted(session, request, timestamp, frameNumber);
        }
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId) {
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureSequenceCompleted(CameraCaptureSession session, int sequenceId, long frameNumber) {
    }

    private List<CameraCaptureSession.CaptureCallback> getCallbacks(CaptureRequest request) {
        List<CameraCaptureSession.CaptureCallback> callbacks = this.mCallbackMap.get(request);
        return callbacks != null ? callbacks : Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addCamera2Callbacks(CaptureRequest captureRequest, List<CameraCaptureSession.CaptureCallback> captureCallbacks) {
        List<CameraCaptureSession.CaptureCallback> existingCallbacks = this.mCallbackMap.get(captureRequest);
        if (existingCallbacks != null) {
            List<CameraCaptureSession.CaptureCallback> totalCallbacks = new ArrayList<>(captureCallbacks.size() + existingCallbacks.size());
            totalCallbacks.addAll(captureCallbacks);
            totalCallbacks.addAll(existingCallbacks);
            this.mCallbackMap.put(captureRequest, totalCallbacks);
            return;
        }
        this.mCallbackMap.put(captureRequest, captureCallbacks);
    }
}
