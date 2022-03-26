package androidx.camera.core;

import android.graphics.Rect;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public final class SurfaceRequest {
    private final Camera mCamera;
    private DeferrableSurface mInternalDeferrableSurface;
    private final CallbackToFutureAdapter.Completer<Void> mRequestCancellationCompleter;
    private final Size mResolution;
    private final ListenableFuture<Void> mSessionStatusFuture;
    private final CallbackToFutureAdapter.Completer<Surface> mSurfaceCompleter;
    final ListenableFuture<Surface> mSurfaceFuture;
    private final Rect mViewPortRect;

    public SurfaceRequest(Size resolution, Camera camera, Rect viewPortRect) {
        Rect rect;
        this.mResolution = resolution;
        this.mCamera = camera;
        if (viewPortRect != null) {
            rect = viewPortRect;
        } else {
            rect = new Rect(0, 0, resolution.getWidth(), resolution.getHeight());
        }
        this.mViewPortRect = rect;
        final String surfaceRequestString = "SurfaceRequest[size: " + resolution + ", id: " + hashCode() + "]";
        AtomicReference<CallbackToFutureAdapter.Completer<Void>> cancellationCompleterRef = new AtomicReference<>(null);
        final ListenableFuture<Void> requestCancellationFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(cancellationCompleterRef, surfaceRequestString) { // from class: androidx.camera.core.-$$Lambda$SurfaceRequest$odG17THPHlbCF8n40ySxsMVBMjU
            private final /* synthetic */ AtomicReference f$0;
            private final /* synthetic */ String f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                AtomicReference atomicReference = this.f$0;
                String str = this.f$1;
                return atomicReference.set(completer);
            }
        });
        final CallbackToFutureAdapter.Completer<Void> requestCancellationCompleter = (CallbackToFutureAdapter.Completer) Preconditions.checkNotNull(cancellationCompleterRef.get());
        this.mRequestCancellationCompleter = requestCancellationCompleter;
        AtomicReference<CallbackToFutureAdapter.Completer<Void>> sessionStatusCompleterRef = new AtomicReference<>(null);
        this.mSessionStatusFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(sessionStatusCompleterRef, surfaceRequestString) { // from class: androidx.camera.core.-$$Lambda$SurfaceRequest$sEXLWXS66apbUecgY06U3wMjup4
            private final /* synthetic */ AtomicReference f$0;
            private final /* synthetic */ String f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                AtomicReference atomicReference = this.f$0;
                String str = this.f$1;
                return atomicReference.set(completer);
            }
        });
        Futures.addCallback(this.mSessionStatusFuture, new FutureCallback<Void>() { // from class: androidx.camera.core.SurfaceRequest.1
            public void onSuccess(Void result) {
                Preconditions.checkState(requestCancellationCompleter.set(null));
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
                if (t instanceof RequestCancelledException) {
                    Preconditions.checkState(requestCancellationFuture.cancel(false));
                } else {
                    Preconditions.checkState(requestCancellationCompleter.set(null));
                }
            }
        }, CameraXExecutors.directExecutor());
        final CallbackToFutureAdapter.Completer<Void> sessionStatusCompleter = (CallbackToFutureAdapter.Completer) Preconditions.checkNotNull(sessionStatusCompleterRef.get());
        AtomicReference<CallbackToFutureAdapter.Completer<Surface>> surfaceCompleterRef = new AtomicReference<>(null);
        this.mSurfaceFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(surfaceCompleterRef, surfaceRequestString) { // from class: androidx.camera.core.-$$Lambda$SurfaceRequest$izJhW7Kwab2vgiWRDUyBSJPuRwo
            private final /* synthetic */ AtomicReference f$0;
            private final /* synthetic */ String f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                AtomicReference atomicReference = this.f$0;
                String str = this.f$1;
                return atomicReference.set(completer);
            }
        });
        this.mSurfaceCompleter = (CallbackToFutureAdapter.Completer) Preconditions.checkNotNull(surfaceCompleterRef.get());
        this.mInternalDeferrableSurface = new DeferrableSurface() { // from class: androidx.camera.core.SurfaceRequest.2
            @Override // androidx.camera.core.impl.DeferrableSurface
            protected ListenableFuture<Surface> provideSurface() {
                return SurfaceRequest.this.mSurfaceFuture;
            }
        };
        final ListenableFuture<Void> terminationFuture = this.mInternalDeferrableSurface.getTerminationFuture();
        Futures.addCallback(this.mSurfaceFuture, new FutureCallback<Surface>() { // from class: androidx.camera.core.SurfaceRequest.3
            public void onSuccess(Surface result) {
                Futures.propagate(terminationFuture, sessionStatusCompleter);
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
                if (t instanceof CancellationException) {
                    CallbackToFutureAdapter.Completer completer = sessionStatusCompleter;
                    Preconditions.checkState(completer.setException(new RequestCancelledException(surfaceRequestString + " cancelled.", t)));
                    return;
                }
                sessionStatusCompleter.set(null);
            }
        }, CameraXExecutors.directExecutor());
        terminationFuture.addListener(new Runnable() { // from class: androidx.camera.core.-$$Lambda$SurfaceRequest$ngtA0g5dJH3w7teLIgUUOnvVWD4
            @Override // java.lang.Runnable
            public final void run() {
                SurfaceRequest.this.lambda$new$3$SurfaceRequest();
            }
        }, CameraXExecutors.directExecutor());
    }

    public /* synthetic */ void lambda$new$3$SurfaceRequest() {
        this.mSurfaceFuture.cancel(true);
    }

    public DeferrableSurface getDeferrableSurface() {
        return this.mInternalDeferrableSurface;
    }

    public Size getResolution() {
        return this.mResolution;
    }

    public Camera getCamera() {
        return this.mCamera;
    }

    public Rect getCropRect() {
        return this.mViewPortRect;
    }

    public void provideSurface(final Surface surface, Executor executor, final Consumer<Result> resultListener) {
        if (this.mSurfaceCompleter.set(surface) || this.mSurfaceFuture.isCancelled()) {
            Futures.addCallback(this.mSessionStatusFuture, new FutureCallback<Void>() { // from class: androidx.camera.core.SurfaceRequest.4
                public void onSuccess(Void result) {
                    resultListener.accept(Result.of(0, surface));
                }

                @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                public void onFailure(Throwable t) {
                    Preconditions.checkState(t instanceof RequestCancelledException, "Camera surface session should only fail with request cancellation. Instead failed due to:\n" + t);
                    resultListener.accept(Result.of(1, surface));
                }
            }, executor);
            return;
        }
        Preconditions.checkState(this.mSurfaceFuture.isDone());
        try {
            this.mSurfaceFuture.get();
            executor.execute(new Runnable(surface) { // from class: androidx.camera.core.-$$Lambda$SurfaceRequest$aC9bT1pfUxuJxiP2CHR2nby0fcw
                private final /* synthetic */ Surface f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Consumer.this.accept(SurfaceRequest.Result.of(3, this.f$1));
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            executor.execute(new Runnable(surface) { // from class: androidx.camera.core.-$$Lambda$SurfaceRequest$1B-FEIX2iizhnKAclUlsKNJ3zuM
                private final /* synthetic */ Surface f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Consumer.this.accept(SurfaceRequest.Result.of(4, this.f$1));
                }
            });
        }
    }

    public boolean willNotProvideSurface() {
        return this.mSurfaceCompleter.setException(new DeferrableSurface.SurfaceUnavailableException("Surface request will not complete."));
    }

    public void addRequestCancellationListener(Executor executor, Runnable listener) {
        this.mRequestCancellationCompleter.addCancellationListener(listener, executor);
    }

    /* loaded from: classes.dex */
    private static final class RequestCancelledException extends RuntimeException {
        RequestCancelledException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Result {
        public static final int RESULT_INVALID_SURFACE;
        public static final int RESULT_REQUEST_CANCELLED;
        public static final int RESULT_SURFACE_ALREADY_PROVIDED;
        public static final int RESULT_SURFACE_USED_SUCCESSFULLY;
        public static final int RESULT_WILL_NOT_PROVIDE_SURFACE;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface ResultCode {
        }

        public abstract int getResultCode();

        public abstract Surface getSurface();

        /* JADX INFO: Access modifiers changed from: package-private */
        public static Result of(int code, Surface surface) {
            return new AutoValue_SurfaceRequest_Result(code, surface);
        }
    }
}
