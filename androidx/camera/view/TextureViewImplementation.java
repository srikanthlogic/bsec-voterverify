package androidx.camera.view;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.view.PreviewViewImplementation;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public final class TextureViewImplementation extends PreviewViewImplementation {
    private static final String TAG;
    SurfaceTexture mDetachedSurfaceTexture;
    boolean mIsSurfaceTextureDetachedFromView = false;
    AtomicReference<CallbackToFutureAdapter.Completer<Void>> mNextFrameCompleter = new AtomicReference<>();
    PreviewViewImplementation.OnSurfaceNotInUseListener mOnSurfaceNotInUseListener;
    ListenableFuture<SurfaceRequest.Result> mSurfaceReleaseFuture;
    SurfaceRequest mSurfaceRequest;
    SurfaceTexture mSurfaceTexture;
    TextureView mTextureView;

    @Override // androidx.camera.view.PreviewViewImplementation
    View getPreview() {
        return this.mTextureView;
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    public void onAttachedToWindow() {
        reattachSurfaceTexture();
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    public void onDetachedFromWindow() {
        this.mIsSurfaceTextureDetachedFromView = true;
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    public void onSurfaceRequested(SurfaceRequest surfaceRequest, PreviewViewImplementation.OnSurfaceNotInUseListener onSurfaceNotInUseListener) {
        this.mResolution = surfaceRequest.getResolution();
        this.mOnSurfaceNotInUseListener = onSurfaceNotInUseListener;
        initializePreview();
        SurfaceRequest surfaceRequest2 = this.mSurfaceRequest;
        if (surfaceRequest2 != null) {
            surfaceRequest2.willNotProvideSurface();
        }
        this.mSurfaceRequest = surfaceRequest;
        surfaceRequest.addRequestCancellationListener(ContextCompat.getMainExecutor(this.mTextureView.getContext()), new Runnable(surfaceRequest) { // from class: androidx.camera.view.-$$Lambda$TextureViewImplementation$ibNE93jRGx1y4qAjVgRiccRc54U
            private final /* synthetic */ SurfaceRequest f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                TextureViewImplementation.this.lambda$onSurfaceRequested$0$TextureViewImplementation(this.f$1);
            }
        });
        tryToProvidePreviewSurface();
    }

    public /* synthetic */ void lambda$onSurfaceRequested$0$TextureViewImplementation(SurfaceRequest surfaceRequest) {
        SurfaceRequest surfaceRequest2 = this.mSurfaceRequest;
        if (surfaceRequest2 != null && surfaceRequest2 == surfaceRequest) {
            this.mSurfaceRequest = null;
            this.mSurfaceReleaseFuture = null;
        }
        notifySurfaceNotInUse();
    }

    private void notifySurfaceNotInUse() {
        PreviewViewImplementation.OnSurfaceNotInUseListener onSurfaceNotInUseListener = this.mOnSurfaceNotInUseListener;
        if (onSurfaceNotInUseListener != null) {
            onSurfaceNotInUseListener.onSurfaceNotInUse();
            this.mOnSurfaceNotInUseListener = null;
        }
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    public void initializePreview() {
        Preconditions.checkNotNull(this.mParent);
        Preconditions.checkNotNull(this.mResolution);
        this.mTextureView = new TextureView(this.mParent.getContext());
        this.mTextureView.setLayoutParams(new FrameLayout.LayoutParams(this.mResolution.getWidth(), this.mResolution.getHeight()));
        this.mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() { // from class: androidx.camera.view.TextureViewImplementation.1
            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                Log.d(TextureViewImplementation.TAG, "SurfaceTexture available. Size: " + width + "x" + height);
                TextureViewImplementation textureViewImplementation = TextureViewImplementation.this;
                textureViewImplementation.mSurfaceTexture = surfaceTexture;
                textureViewImplementation.tryToProvidePreviewSurface();
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
                Log.d(TextureViewImplementation.TAG, "SurfaceTexture size changed: " + width + "x" + height);
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
                Log.d(TextureViewImplementation.TAG, "SurfaceTexture destroyed");
                TextureViewImplementation textureViewImplementation = TextureViewImplementation.this;
                textureViewImplementation.mSurfaceTexture = null;
                if (textureViewImplementation.mSurfaceRequest != null || TextureViewImplementation.this.mSurfaceReleaseFuture == null) {
                    return true;
                }
                Futures.addCallback(TextureViewImplementation.this.mSurfaceReleaseFuture, new FutureCallback<SurfaceRequest.Result>() { // from class: androidx.camera.view.TextureViewImplementation.1.1
                    public void onSuccess(SurfaceRequest.Result result) {
                        Preconditions.checkState(result.getResultCode() != 3, "Unexpected result from SurfaceRequest. Surface was provided twice.");
                        surfaceTexture.release();
                        if (TextureViewImplementation.this.mDetachedSurfaceTexture != null) {
                            TextureViewImplementation.this.mDetachedSurfaceTexture = null;
                        }
                    }

                    @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                    public void onFailure(Throwable t) {
                        throw new IllegalStateException("SurfaceReleaseFuture did not complete nicely.", t);
                    }
                }, ContextCompat.getMainExecutor(TextureViewImplementation.this.mTextureView.getContext()));
                TextureViewImplementation.this.mDetachedSurfaceTexture = surfaceTexture;
                return false;
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                CallbackToFutureAdapter.Completer<Void> completer = TextureViewImplementation.this.mNextFrameCompleter.getAndSet(null);
                if (completer != null) {
                    completer.set(null);
                }
            }
        });
        this.mParent.removeAllViews();
        this.mParent.addView(this.mTextureView);
    }

    void tryToProvidePreviewSurface() {
        SurfaceTexture surfaceTexture;
        if (this.mResolution != null && (surfaceTexture = this.mSurfaceTexture) != null && this.mSurfaceRequest != null) {
            surfaceTexture.setDefaultBufferSize(this.mResolution.getWidth(), this.mResolution.getHeight());
            Surface surface = new Surface(this.mSurfaceTexture);
            ListenableFuture<SurfaceRequest.Result> surfaceReleaseFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(surface) { // from class: androidx.camera.view.-$$Lambda$TextureViewImplementation$Hl0YGRNVRzSkOIexKiob_cdd3zk
                private final /* synthetic */ Surface f$1;

                {
                    this.f$1 = r2;
                }

                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                    return TextureViewImplementation.this.lambda$tryToProvidePreviewSurface$1$TextureViewImplementation(this.f$1, completer);
                }
            });
            this.mSurfaceReleaseFuture = surfaceReleaseFuture;
            this.mSurfaceReleaseFuture.addListener(new Runnable(surface, surfaceReleaseFuture) { // from class: androidx.camera.view.-$$Lambda$TextureViewImplementation$sdgHTiuiPGYGR680GznXYPoksgQ
                private final /* synthetic */ Surface f$1;
                private final /* synthetic */ ListenableFuture f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    TextureViewImplementation.this.lambda$tryToProvidePreviewSurface$2$TextureViewImplementation(this.f$1, this.f$2);
                }
            }, ContextCompat.getMainExecutor(this.mTextureView.getContext()));
            this.mSurfaceRequest = null;
            onSurfaceProvided();
        }
    }

    public /* synthetic */ Object lambda$tryToProvidePreviewSurface$1$TextureViewImplementation(Surface surface, CallbackToFutureAdapter.Completer completer) throws Exception {
        Log.d(TAG, "Surface set on Preview.");
        SurfaceRequest surfaceRequest = this.mSurfaceRequest;
        Executor directExecutor = CameraXExecutors.directExecutor();
        Objects.requireNonNull(completer);
        surfaceRequest.provideSurface(surface, directExecutor, new Consumer() { // from class: androidx.camera.view.-$$Lambda$TextureViewImplementation$K8R1nxorC_-fhJS80SsYrGI5qJg
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                boolean unused = CallbackToFutureAdapter.Completer.this.set((SurfaceRequest.Result) obj);
            }
        });
        return "provideSurface[request=" + this.mSurfaceRequest + " surface=" + surface + "]";
    }

    public /* synthetic */ void lambda$tryToProvidePreviewSurface$2$TextureViewImplementation(Surface surface, ListenableFuture surfaceReleaseFuture) {
        Log.d(TAG, "Safe to release surface.");
        notifySurfaceNotInUse();
        surface.release();
        if (this.mSurfaceReleaseFuture == surfaceReleaseFuture) {
            this.mSurfaceReleaseFuture = null;
        }
    }

    private void reattachSurfaceTexture() {
        SurfaceTexture surfaceTexture;
        if (this.mIsSurfaceTextureDetachedFromView && this.mDetachedSurfaceTexture != null && this.mTextureView.getSurfaceTexture() != (surfaceTexture = this.mDetachedSurfaceTexture)) {
            this.mTextureView.setSurfaceTexture(surfaceTexture);
            this.mDetachedSurfaceTexture = null;
            this.mIsSurfaceTextureDetachedFromView = false;
        }
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    public ListenableFuture<Void> waitForNextFrame() {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.view.-$$Lambda$TextureViewImplementation$wCD6ilTjRIgpgUn9gSmCeAFq7P4
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return TextureViewImplementation.this.lambda$waitForNextFrame$3$TextureViewImplementation(completer);
            }
        });
    }

    public /* synthetic */ Object lambda$waitForNextFrame$3$TextureViewImplementation(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mNextFrameCompleter.set(completer);
        return "textureViewImpl_waitForNextFrame";
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    Bitmap getPreviewBitmap() {
        TextureView textureView = this.mTextureView;
        if (textureView == null || !textureView.isAvailable()) {
            return null;
        }
        return this.mTextureView.getBitmap();
    }
}
