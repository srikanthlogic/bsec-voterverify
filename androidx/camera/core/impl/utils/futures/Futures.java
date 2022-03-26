package androidx.camera.core.impl.utils.futures;

import androidx.arch.core.util.Function;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.ImmediateFuture;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
/* loaded from: classes.dex */
public final class Futures {
    private static final Function<?, ?> IDENTITY_FUNCTION = new Function<Object, Object>() { // from class: androidx.camera.core.impl.utils.futures.Futures.2
        @Override // androidx.arch.core.util.Function
        public Object apply(Object input) {
            return input;
        }
    };

    public static <V> ListenableFuture<V> immediateFuture(V value) {
        if (value == null) {
            return ImmediateFuture.nullFuture();
        }
        return new ImmediateFuture.ImmediateSuccessfulFuture(value);
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable cause) {
        return new ImmediateFuture.ImmediateFailedFuture(cause);
    }

    public static <V> ScheduledFuture<V> immediateFailedScheduledFuture(Throwable cause) {
        return new ImmediateFuture.ImmediateFailedScheduledFuture(cause);
    }

    public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        ChainingListenableFuture<I, O> output = new ChainingListenableFuture<>(function, input);
        input.addListener(output, executor);
        return output;
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, final Function<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(function);
        return transformAsync(input, new AsyncFunction<I, O>() { // from class: androidx.camera.core.impl.utils.futures.Futures.1
            @Override // androidx.camera.core.impl.utils.futures.AsyncFunction
            public ListenableFuture<O> apply(I input2) {
                return Futures.immediateFuture(function.apply(input2));
            }
        }, executor);
    }

    public static <V> void propagate(ListenableFuture<V> input, CallbackToFutureAdapter.Completer<V> completer) {
        propagateTransform(input, IDENTITY_FUNCTION, completer, CameraXExecutors.directExecutor());
    }

    public static <I, O> void propagateTransform(ListenableFuture<I> input, Function<? super I, ? extends O> function, CallbackToFutureAdapter.Completer<O> completer, Executor executor) {
        propagateTransform(true, input, function, completer, executor);
    }

    public static <I, O> void propagateTransform(boolean propagateCancellation, final ListenableFuture<I> input, final Function<? super I, ? extends O> function, final CallbackToFutureAdapter.Completer<O> completer, Executor executor) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(completer);
        Preconditions.checkNotNull(executor);
        addCallback(input, new FutureCallback<I>() { // from class: androidx.camera.core.impl.utils.futures.Futures.3
            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onSuccess(I result) {
                try {
                    completer.set(function.apply(result));
                } catch (Throwable t) {
                    completer.setException(t);
                }
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
                completer.setException(t);
            }
        }, executor);
        if (propagateCancellation) {
            completer.addCancellationListener(new Runnable() { // from class: androidx.camera.core.impl.utils.futures.Futures.4
                @Override // java.lang.Runnable
                public void run() {
                    input.cancel(true);
                }
            }, CameraXExecutors.directExecutor());
        }
    }

    public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
        Preconditions.checkNotNull(future);
        if (future.isDone()) {
            return future;
        }
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: androidx.camera.core.impl.utils.futures.-$$Lambda$Futures$BFJU90gKHywJ5fHtASrMxI3JslQ
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return Futures.propagateTransform(false, ListenableFuture.this, Futures.IDENTITY_FUNCTION, completer, CameraXExecutors.directExecutor());
            }
        });
    }

    public static <V> ListenableFuture<List<V>> successfulAsList(Collection<? extends ListenableFuture<? extends V>> futures) {
        return new ListFuture(new ArrayList(futures), false, CameraXExecutors.directExecutor());
    }

    public static <V> ListenableFuture<List<V>> allAsList(Collection<? extends ListenableFuture<? extends V>> futures) {
        return new ListFuture(new ArrayList(futures), true, CameraXExecutors.directExecutor());
    }

    public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor) {
        Preconditions.checkNotNull(callback);
        future.addListener(new CallbackListener(future, callback), executor);
    }

    /* loaded from: classes.dex */
    public static final class CallbackListener<V> implements Runnable {
        final FutureCallback<? super V> mCallback;
        final Future<V> mFuture;

        CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
            this.mFuture = future;
            this.mCallback = callback;
        }

        @Override // java.lang.Runnable
        public void run() {
            Throwable e;
            try {
                this.mCallback.onSuccess(Futures.getDone(this.mFuture));
            } catch (Error e2) {
                e = e2;
                this.mCallback.onFailure(e);
            } catch (RuntimeException e3) {
                e = e3;
                this.mCallback.onFailure(e);
            } catch (ExecutionException e4) {
                this.mCallback.onFailure(e4.getCause());
            }
        }

        @Override // java.lang.Object
        public String toString() {
            return getClass().getSimpleName() + "," + this.mCallback;
        }
    }

    public static <V> V getDone(Future<V> future) throws ExecutionException {
        boolean isDone = future.isDone();
        Preconditions.checkState(isDone, "Future was expected to be done, " + future);
        return (V) getUninterruptibly(future);
    }

    public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        V v;
        boolean interrupted = false;
        while (true) {
            try {
                v = future.get();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return v;
    }

    private Futures() {
    }
}
