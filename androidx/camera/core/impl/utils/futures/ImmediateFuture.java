package androidx.camera.core.impl.utils.futures;

import android.util.Log;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ImmediateFuture<V> implements ListenableFuture<V> {
    private static final String TAG = "ImmediateFuture";

    @Override // java.util.concurrent.Future
    public abstract V get() throws ExecutionException;

    ImmediateFuture() {
    }

    public static <V> ListenableFuture<V> nullFuture() {
        return ImmediateSuccessfulFuture.NULL_FUTURE;
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable listener, Executor executor) {
        Preconditions.checkNotNull(listener);
        Preconditions.checkNotNull(executor);
        try {
            executor.execute(listener);
        } catch (RuntimeException e) {
            Log.e(TAG, "Experienced RuntimeException while attempting to notify " + listener + " on Executor " + executor, e);
        }
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return true;
    }

    @Override // java.util.concurrent.Future
    public V get(long timeout, TimeUnit unit) throws ExecutionException {
        Preconditions.checkNotNull(unit);
        return get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {
        static final ImmediateFuture<Object> NULL_FUTURE = new ImmediateSuccessfulFuture(null);
        private final V mValue;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ImmediateSuccessfulFuture(V value) {
            this.mValue = value;
        }

        @Override // androidx.camera.core.impl.utils.futures.ImmediateFuture, java.util.concurrent.Future
        public V get() {
            return this.mValue;
        }

        @Override // java.lang.Object
        public String toString() {
            return super.toString() + "[status=SUCCESS, result=[" + this.mValue + "]]";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ImmediateFailedFuture<V> extends ImmediateFuture<V> {
        private final Throwable mCause;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ImmediateFailedFuture(Throwable cause) {
            this.mCause = cause;
        }

        @Override // androidx.camera.core.impl.utils.futures.ImmediateFuture, java.util.concurrent.Future
        public V get() throws ExecutionException {
            throw new ExecutionException(this.mCause);
        }

        @Override // java.lang.Object
        public String toString() {
            return super.toString() + "[status=FAILURE, cause=[" + this.mCause + "]]";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ImmediateFailedScheduledFuture<V> extends ImmediateFailedFuture<V> implements ScheduledFuture<V> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public ImmediateFailedScheduledFuture(Throwable cause) {
            super(cause);
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit timeUnit) {
            return 0;
        }

        public int compareTo(Delayed delayed) {
            return -1;
        }
    }
}
