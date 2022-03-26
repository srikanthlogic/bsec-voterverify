package androidx.camera.view;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.Size;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.view.PreviewViewImplementation;
import androidx.camera.view.SurfaceViewImplementation;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
/* loaded from: classes.dex */
public final class SurfaceViewImplementation extends PreviewViewImplementation {
    private static final String TAG;
    private PreviewViewImplementation.OnSurfaceNotInUseListener mOnSurfaceNotInUseListener;
    final SurfaceRequestCallback mSurfaceRequestCallback = new SurfaceRequestCallback();
    SurfaceView mSurfaceView;

    @Override // androidx.camera.view.PreviewViewImplementation
    public void onSurfaceRequested(SurfaceRequest surfaceRequest, PreviewViewImplementation.OnSurfaceNotInUseListener onSurfaceNotInUseListener) {
        this.mResolution = surfaceRequest.getResolution();
        this.mOnSurfaceNotInUseListener = onSurfaceNotInUseListener;
        initializePreview();
        surfaceRequest.addRequestCancellationListener(ContextCompat.getMainExecutor(this.mSurfaceView.getContext()), new Runnable() { // from class: androidx.camera.view.-$$Lambda$z-SNMRjphPUVSN0L2ch7MX_D73Q
            @Override // java.lang.Runnable
            public final void run() {
                SurfaceViewImplementation.this.notifySurfaceNotInUse();
            }
        });
        this.mSurfaceView.post(new Runnable(surfaceRequest) { // from class: androidx.camera.view.-$$Lambda$SurfaceViewImplementation$AfczykelZ3G5A9yZzYzRueR5eGc
            private final /* synthetic */ SurfaceRequest f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SurfaceViewImplementation.this.lambda$onSurfaceRequested$0$SurfaceViewImplementation(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$onSurfaceRequested$0$SurfaceViewImplementation(SurfaceRequest surfaceRequest) {
        this.mSurfaceRequestCallback.setSurfaceRequest(surfaceRequest);
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    void initializePreview() {
        Preconditions.checkNotNull(this.mParent);
        Preconditions.checkNotNull(this.mResolution);
        this.mSurfaceView = new SurfaceView(this.mParent.getContext());
        this.mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(this.mResolution.getWidth(), this.mResolution.getHeight()));
        this.mParent.removeAllViews();
        this.mParent.addView(this.mSurfaceView);
        this.mSurfaceView.getHolder().addCallback(this.mSurfaceRequestCallback);
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    View getPreview() {
        return this.mSurfaceView;
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    public void onAttachedToWindow() {
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    public void onDetachedFromWindow() {
    }

    public void notifySurfaceNotInUse() {
        PreviewViewImplementation.OnSurfaceNotInUseListener onSurfaceNotInUseListener = this.mOnSurfaceNotInUseListener;
        if (onSurfaceNotInUseListener != null) {
            onSurfaceNotInUseListener.onSurfaceNotInUse();
            this.mOnSurfaceNotInUseListener = null;
        }
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    Bitmap getPreviewBitmap() {
        SurfaceView surfaceView = this.mSurfaceView;
        if (surfaceView == null || surfaceView.getHolder().getSurface() == null || !this.mSurfaceView.getHolder().getSurface().isValid()) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(this.mSurfaceView.getWidth(), this.mSurfaceView.getHeight(), Bitmap.Config.ARGB_8888);
        SurfaceView surfaceView2 = this.mSurfaceView;
        PixelCopy.request(surfaceView2, bitmap, $$Lambda$SurfaceViewImplementation$5eZhJM3kno_gCDPwBLaHikLYv30.INSTANCE, surfaceView2.getHandler());
        return bitmap;
    }

    public static /* synthetic */ void lambda$getPreviewBitmap$1(int copyResult) {
        if (copyResult == 0) {
            Log.d(TAG, "PreviewView.SurfaceViewImplementation.getBitmap() succeeded");
            return;
        }
        Log.e(TAG, "PreviewView.SurfaceViewImplementation.getBitmap() failed with error " + copyResult);
    }

    /* loaded from: classes.dex */
    public class SurfaceRequestCallback implements SurfaceHolder.Callback {
        private Size mCurrentSurfaceSize;
        private SurfaceRequest mSurfaceRequest;
        private Size mTargetSize;
        private boolean mWasSurfaceProvided = false;

        SurfaceRequestCallback() {
            SurfaceViewImplementation.this = this$0;
        }

        void setSurfaceRequest(SurfaceRequest surfaceRequest) {
            cancelPreviousRequest();
            this.mSurfaceRequest = surfaceRequest;
            Size targetSize = surfaceRequest.getResolution();
            this.mTargetSize = targetSize;
            this.mWasSurfaceProvided = false;
            if (!tryToComplete()) {
                Log.d(SurfaceViewImplementation.TAG, "Wait for new Surface creation.");
                SurfaceViewImplementation.this.mSurfaceView.getHolder().setFixedSize(targetSize.getWidth(), targetSize.getHeight());
            }
        }

        private boolean tryToComplete() {
            Surface surface = SurfaceViewImplementation.this.mSurfaceView.getHolder().getSurface();
            if (!canProvideSurface()) {
                return false;
            }
            Log.d(SurfaceViewImplementation.TAG, "Surface set on Preview.");
            this.mSurfaceRequest.provideSurface(surface, ContextCompat.getMainExecutor(SurfaceViewImplementation.this.mSurfaceView.getContext()), new Consumer() { // from class: androidx.camera.view.-$$Lambda$SurfaceViewImplementation$SurfaceRequestCallback$81WMgLrawbdolK1M9hchPAqvUhI
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    SurfaceViewImplementation.SurfaceRequestCallback.this.lambda$tryToComplete$0$SurfaceViewImplementation$SurfaceRequestCallback((SurfaceRequest.Result) obj);
                }
            });
            this.mWasSurfaceProvided = true;
            SurfaceViewImplementation.this.onSurfaceProvided();
            return true;
        }

        public /* synthetic */ void lambda$tryToComplete$0$SurfaceViewImplementation$SurfaceRequestCallback(SurfaceRequest.Result result) {
            Log.d(SurfaceViewImplementation.TAG, "Safe to release surface.");
            SurfaceViewImplementation.this.notifySurfaceNotInUse();
        }

        private boolean canProvideSurface() {
            Size size;
            return !this.mWasSurfaceProvided && this.mSurfaceRequest != null && (size = this.mTargetSize) != null && size.equals(this.mCurrentSurfaceSize);
        }

        private void cancelPreviousRequest() {
            if (this.mSurfaceRequest != null) {
                Log.d(SurfaceViewImplementation.TAG, "Request canceled: " + this.mSurfaceRequest);
                this.mSurfaceRequest.willNotProvideSurface();
            }
        }

        private void invalidateSurface() {
            if (this.mSurfaceRequest != null) {
                Log.d(SurfaceViewImplementation.TAG, "Surface invalidated " + this.mSurfaceRequest);
                this.mSurfaceRequest.getDeferrableSurface().close();
            }
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d(SurfaceViewImplementation.TAG, "Surface created.");
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            Log.d(SurfaceViewImplementation.TAG, "Surface changed. Size: " + width + "x" + height);
            this.mCurrentSurfaceSize = new Size(width, height);
            tryToComplete();
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.d(SurfaceViewImplementation.TAG, "Surface destroyed.");
            if (this.mWasSurfaceProvided) {
                invalidateSurface();
            } else {
                cancelPreviousRequest();
            }
            this.mWasSurfaceProvided = false;
            this.mSurfaceRequest = null;
            this.mCurrentSurfaceSize = null;
            this.mTargetSize = null;
        }
    }

    @Override // androidx.camera.view.PreviewViewImplementation
    public ListenableFuture<Void> waitForNextFrame() {
        return Futures.immediateFuture(null);
    }
}
