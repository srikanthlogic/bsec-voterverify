package androidx.camera.view;

import android.util.Log;
import androidx.arch.core.util.Function;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.impl.CameraCaptureCallback;
import androidx.camera.core.impl.CameraCaptureResult;
import androidx.camera.core.impl.CameraInfoInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.Observable;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.AsyncFunction;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.FutureChain;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.view.PreviewView;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.lifecycle.MutableLiveData;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class PreviewStreamStateObserver implements Observable.Observer<CameraInternal.State> {
    private static final String TAG;
    private final CameraInfoInternal mCameraInfoInternal;
    ListenableFuture<Void> mFlowFuture;
    private boolean mHasStartedPreviewStreamFlow = false;
    private PreviewView.StreamState mPreviewStreamState;
    private final MutableLiveData<PreviewView.StreamState> mPreviewStreamStateLiveData;
    private final PreviewViewImplementation mPreviewViewImplementation;

    public PreviewStreamStateObserver(CameraInfoInternal cameraInfoInternal, MutableLiveData<PreviewView.StreamState> previewStreamLiveData, PreviewViewImplementation implementation) {
        this.mCameraInfoInternal = cameraInfoInternal;
        this.mPreviewStreamStateLiveData = previewStreamLiveData;
        this.mPreviewViewImplementation = implementation;
        synchronized (this) {
            this.mPreviewStreamState = previewStreamLiveData.getValue();
        }
    }

    public void onNewData(CameraInternal.State value) {
        if (value == CameraInternal.State.CLOSING || value == CameraInternal.State.CLOSED || value == CameraInternal.State.RELEASING || value == CameraInternal.State.RELEASED) {
            updatePreviewStreamState(PreviewView.StreamState.IDLE);
            if (this.mHasStartedPreviewStreamFlow) {
                this.mHasStartedPreviewStreamFlow = false;
                cancelFlow();
            }
        } else if ((value == CameraInternal.State.OPENING || value == CameraInternal.State.OPEN || value == CameraInternal.State.PENDING_OPEN) && !this.mHasStartedPreviewStreamFlow) {
            startPreviewStreamStateFlow(this.mCameraInfoInternal);
            this.mHasStartedPreviewStreamFlow = true;
        }
    }

    @Override // androidx.camera.core.impl.Observable.Observer
    public void onError(Throwable t) {
        clear();
        updatePreviewStreamState(PreviewView.StreamState.IDLE);
    }

    public void clear() {
        cancelFlow();
    }

    private void cancelFlow() {
        ListenableFuture<Void> listenableFuture = this.mFlowFuture;
        if (listenableFuture != null) {
            listenableFuture.cancel(false);
            this.mFlowFuture = null;
        }
    }

    private void startPreviewStreamStateFlow(final CameraInfo cameraInfo) {
        updatePreviewStreamState(PreviewView.StreamState.IDLE);
        final List<CameraCaptureCallback> callbacksToClear = new ArrayList<>();
        this.mFlowFuture = FutureChain.from(waitForCaptureResult(cameraInfo, callbacksToClear)).transformAsync(new AsyncFunction() { // from class: androidx.camera.view.-$$Lambda$PreviewStreamStateObserver$W-AFYrLTx1dowMs6tBeQNGMvooA
            @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
            public final ListenableFuture apply(Object obj) {
                return PreviewStreamStateObserver.this.lambda$startPreviewStreamStateFlow$0$PreviewStreamStateObserver((Void) obj);
            }
        }, CameraXExecutors.directExecutor()).transform(new Function() { // from class: androidx.camera.view.-$$Lambda$PreviewStreamStateObserver$e0zfOi9jG0fhpTQVSvkChPeUtgY
            @Override // androidx.arch.core.util.Function
            public final Object apply(Object obj) {
                return PreviewStreamStateObserver.this.lambda$startPreviewStreamStateFlow$1$PreviewStreamStateObserver((Void) obj);
            }
        }, CameraXExecutors.directExecutor());
        Futures.addCallback(this.mFlowFuture, new FutureCallback<Void>() { // from class: androidx.camera.view.PreviewStreamStateObserver.1
            public void onSuccess(Void result) {
                PreviewStreamStateObserver.this.mFlowFuture = null;
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
                PreviewStreamStateObserver.this.mFlowFuture = null;
                if (!callbacksToClear.isEmpty()) {
                    for (CameraCaptureCallback callback : callbacksToClear) {
                        ((CameraInfoInternal) cameraInfo).removeSessionCaptureCallback(callback);
                    }
                    callbacksToClear.clear();
                }
            }
        }, CameraXExecutors.directExecutor());
    }

    public /* synthetic */ ListenableFuture lambda$startPreviewStreamStateFlow$0$PreviewStreamStateObserver(Void v) throws Exception {
        return this.mPreviewViewImplementation.waitForNextFrame();
    }

    public /* synthetic */ Void lambda$startPreviewStreamStateFlow$1$PreviewStreamStateObserver(Void v) {
        updatePreviewStreamState(PreviewView.StreamState.STREAMING);
        return null;
    }

    public void updatePreviewStreamState(PreviewView.StreamState streamState) {
        synchronized (this) {
            if (!this.mPreviewStreamState.equals(streamState)) {
                this.mPreviewStreamState = streamState;
                Log.d(TAG, "Update Preview stream state to " + streamState);
                this.mPreviewStreamStateLiveData.postValue(streamState);
            }
        }
    }

    private ListenableFuture<Void> waitForCaptureResult(CameraInfo cameraInfo, List<CameraCaptureCallback> callbacksToClear) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(cameraInfo, callbacksToClear) { // from class: androidx.camera.view.-$$Lambda$PreviewStreamStateObserver$Xs-T-8VJ4seWgFRfmLz37L9vi5k
            private final /* synthetic */ CameraInfo f$1;
            private final /* synthetic */ List f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return PreviewStreamStateObserver.this.lambda$waitForCaptureResult$2$PreviewStreamStateObserver(this.f$1, this.f$2, completer);
            }
        });
    }

    public /* synthetic */ Object lambda$waitForCaptureResult$2$PreviewStreamStateObserver(final CameraInfo cameraInfo, List callbacksToClear, final CallbackToFutureAdapter.Completer completer) throws Exception {
        AnonymousClass2 r0 = new CameraCaptureCallback() { // from class: androidx.camera.view.PreviewStreamStateObserver.2
            @Override // androidx.camera.core.impl.CameraCaptureCallback
            public void onCaptureCompleted(CameraCaptureResult result) {
                completer.set(null);
                ((CameraInfoInternal) cameraInfo).removeSessionCaptureCallback(this);
            }
        };
        callbacksToClear.add(r0);
        ((CameraInfoInternal) cameraInfo).addSessionCaptureCallback(CameraXExecutors.directExecutor(), r0);
        return "waitForCaptureResult";
    }
}
