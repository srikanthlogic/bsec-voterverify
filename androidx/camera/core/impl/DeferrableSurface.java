package androidx.camera.core.impl;

import android.util.Log;
import android.view.Surface;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public abstract class DeferrableSurface {
    private CallbackToFutureAdapter.Completer<Void> mTerminationCompleter;
    private static final String TAG;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static AtomicInteger sUsedCount = new AtomicInteger(0);
    private static AtomicInteger sTotalCount = new AtomicInteger(0);
    private final Object mLock = new Object();
    private int mUseCount = 0;
    private boolean mClosed = false;
    private final ListenableFuture<Void> mTerminationFuture = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.core.impl.-$$Lambda$DeferrableSurface$4AwivYkWbX9ifTwpoNEQg994K4I
        @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
        public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
            return DeferrableSurface.this.lambda$new$0$DeferrableSurface(completer);
        }
    });

    protected abstract ListenableFuture<Surface> provideSurface();

    /* loaded from: classes.dex */
    public static final class SurfaceUnavailableException extends Exception {
        public SurfaceUnavailableException(String message) {
            super(message);
        }
    }

    /* loaded from: classes.dex */
    public static final class SurfaceClosedException extends Exception {
        DeferrableSurface mDeferrableSurface;

        public SurfaceClosedException(String s, DeferrableSurface surface) {
            super(s);
            this.mDeferrableSurface = surface;
        }

        public DeferrableSurface getDeferrableSurface() {
            return this.mDeferrableSurface;
        }
    }

    public DeferrableSurface() {
        if (DEBUG) {
            printGlobalDebugCounts("Surface created", sTotalCount.incrementAndGet(), sUsedCount.get());
            this.mTerminationFuture.addListener(new Runnable(Log.getStackTraceString(new Exception())) { // from class: androidx.camera.core.impl.-$$Lambda$DeferrableSurface$XxjDv83UzOW4COPv7lY3gXa8cFA
                private final /* synthetic */ String f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    DeferrableSurface.this.lambda$new$1$DeferrableSurface(this.f$1);
                }
            }, CameraXExecutors.directExecutor());
        }
    }

    public /* synthetic */ Object lambda$new$0$DeferrableSurface(CallbackToFutureAdapter.Completer completer) throws Exception {
        synchronized (this.mLock) {
            this.mTerminationCompleter = completer;
        }
        return "DeferrableSurface-termination(" + this + ")";
    }

    public /* synthetic */ void lambda$new$1$DeferrableSurface(String creationStackTrace) {
        try {
            this.mTerminationFuture.get();
            printGlobalDebugCounts("Surface terminated", sTotalCount.decrementAndGet(), sUsedCount.get());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected surface termination for " + this + "\nStack Trace:\n" + creationStackTrace);
            throw new IllegalArgumentException("DeferrableSurface terminated with unexpected exception.", e);
        }
    }

    private void printGlobalDebugCounts(String prefix, int totalCount, int useCount) {
        Log.d(TAG, prefix + "[total_surfaces=" + totalCount + ", used_surfaces=" + useCount + "](" + this + "}");
    }

    public final ListenableFuture<Surface> getSurface() {
        synchronized (this.mLock) {
            if (this.mClosed) {
                return Futures.immediateFailedFuture(new SurfaceClosedException("DeferrableSurface already closed.", this));
            }
            return provideSurface();
        }
    }

    public ListenableFuture<Void> getTerminationFuture() {
        return Futures.nonCancellationPropagating(this.mTerminationFuture);
    }

    public void incrementUseCount() throws SurfaceClosedException {
        synchronized (this.mLock) {
            if (this.mUseCount == 0 && this.mClosed) {
                throw new SurfaceClosedException("Cannot begin use on a closed surface.", this);
            }
            this.mUseCount++;
            if (DEBUG) {
                if (this.mUseCount == 1) {
                    printGlobalDebugCounts("New surface in use", sTotalCount.get(), sUsedCount.incrementAndGet());
                }
                Log.d(TAG, "use count+1, useCount=" + this.mUseCount + " " + this);
            }
        }
    }

    public final void close() {
        CallbackToFutureAdapter.Completer<Void> terminationCompleter = null;
        synchronized (this.mLock) {
            if (!this.mClosed) {
                this.mClosed = true;
                if (this.mUseCount == 0) {
                    terminationCompleter = this.mTerminationCompleter;
                    this.mTerminationCompleter = null;
                }
                if (DEBUG) {
                    Log.d(TAG, "surface closed,  useCount=" + this.mUseCount + " closed=true " + this);
                }
            }
        }
        if (terminationCompleter != null) {
            terminationCompleter.set(null);
        }
    }

    public void decrementUseCount() {
        CallbackToFutureAdapter.Completer<Void> terminationCompleter = null;
        synchronized (this.mLock) {
            if (this.mUseCount != 0) {
                this.mUseCount--;
                if (this.mUseCount == 0 && this.mClosed) {
                    terminationCompleter = this.mTerminationCompleter;
                    this.mTerminationCompleter = null;
                }
                if (DEBUG) {
                    Log.d(TAG, "use count-1,  useCount=" + this.mUseCount + " closed=" + this.mClosed + " " + this);
                    if (this.mUseCount == 0 && DEBUG) {
                        printGlobalDebugCounts("Surface no longer in use", sTotalCount.get(), sUsedCount.decrementAndGet());
                    }
                }
            } else {
                throw new IllegalStateException("Decrementing use count occurs more times than incrementing");
            }
        }
        if (terminationCompleter != null) {
            terminationCompleter.set(null);
        }
    }

    public int getUseCount() {
        int i;
        synchronized (this.mLock) {
            i = this.mUseCount;
        }
        return i;
    }
}
