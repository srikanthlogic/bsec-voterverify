package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import java.lang.Throwable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public abstract class AbstractCatchingFuture<V, X extends Throwable, F, T> extends FluentFuture.TrustedFuture<V> implements Runnable {
    @NullableDecl
    Class<X> exceptionType;
    @NullableDecl
    F fallback;
    @NullableDecl
    ListenableFuture<? extends V> inputFuture;

    @NullableDecl
    abstract T doFallback(F f, X x) throws Exception;

    abstract void setResult(@NullableDecl T t);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
        CatchingFuture<V, X> future = new CatchingFuture<>(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
        return future;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
        AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture<>(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
        return future;
    }

    AbstractCatchingFuture(ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback) {
        this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(inputFuture);
        this.exceptionType = (Class) Preconditions.checkNotNull(exceptionType);
        this.fallback = (F) Preconditions.checkNotNull(fallback);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public final void run() {
        ListenableFuture<? extends V> localInputFuture = this.inputFuture;
        Class<X> localExceptionType = this.exceptionType;
        F localFallback = this.fallback;
        boolean z = true;
        boolean z2 = (localInputFuture == null) | (localExceptionType == null);
        if (localFallback != null) {
            z = false;
        }
        if ((!z && !z2) && !isCancelled()) {
            this.inputFuture = null;
            Object obj = null;
            Throwable throwable = null;
            try {
                if (localInputFuture instanceof InternalFutureFailureAccess) {
                    throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) localInputFuture);
                }
                if (throwable == null) {
                    obj = Futures.getDone(localInputFuture);
                }
            } catch (ExecutionException e) {
                throwable = e.getCause();
                if (throwable == null) {
                    throwable = new NullPointerException("Future type " + localInputFuture.getClass() + " threw " + e.getClass() + " without a cause");
                }
            } catch (Throwable e2) {
                throwable = e2;
            }
            if (throwable == null) {
                set(obj);
            } else if (!Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
                setFuture(localInputFuture);
            } else {
                try {
                    T fallbackResult = doFallback(localFallback, throwable);
                    this.exceptionType = null;
                    this.fallback = null;
                    setResult(fallbackResult);
                } catch (Throwable t) {
                    try {
                        setException(t);
                        this.exceptionType = null;
                        this.fallback = null;
                    } catch (Throwable t2) {
                        this.exceptionType = null;
                        this.fallback = null;
                        throw t2;
                    }
                }
            }
        }
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected String pendingToString() {
        ListenableFuture<? extends V> localInputFuture = this.inputFuture;
        Class<X> localExceptionType = this.exceptionType;
        F localFallback = this.fallback;
        String superString = super.pendingToString();
        String resultString = "";
        if (localInputFuture != null) {
            resultString = "inputFuture=[" + localInputFuture + "], ";
        }
        if (localExceptionType != null && localFallback != null) {
            return resultString + "exceptionType=[" + localExceptionType + "], fallback=[" + localFallback + "]";
        } else if (superString == null) {
            return null;
        } else {
            return resultString + superString;
        }
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected final void afterDone() {
        maybePropagateCancellationTo(this.inputFuture);
        this.inputFuture = null;
        this.exceptionType = null;
        this.fallback = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class AsyncCatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractCatchingFuture
        /* bridge */ /* synthetic */ Object doFallback(Object obj, Throwable th) throws Exception {
            return doFallback((AsyncFunction<? super AsyncFunction<? super X, ? extends V>, ? extends V>) ((AsyncFunction) obj), (AsyncFunction<? super X, ? extends V>) th);
        }

        @Override // com.google.common.util.concurrent.AbstractCatchingFuture
        /* bridge */ /* synthetic */ void setResult(Object obj) {
            setResult((ListenableFuture) ((ListenableFuture) obj));
        }

        AsyncCatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
            super(input, exceptionType, fallback);
        }

        ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> fallback, X cause) throws Exception {
            ListenableFuture<? extends V> replacement = fallback.apply(cause);
            Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", fallback);
            return replacement;
        }

        void setResult(ListenableFuture<? extends V> result) {
            setFuture(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class CatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractCatchingFuture
        @NullableDecl
        /* bridge */ /* synthetic */ Object doFallback(Object obj, Throwable th) throws Exception {
            return doFallback((Function<? super Function<? super X, ? extends V>, ? extends V>) ((Function) obj), (Function<? super X, ? extends V>) th);
        }

        CatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
            super(input, exceptionType, fallback);
        }

        @NullableDecl
        V doFallback(Function<? super X, ? extends V> fallback, X cause) throws Exception {
            return (V) fallback.apply(cause);
        }

        @Override // com.google.common.util.concurrent.AbstractCatchingFuture
        void setResult(@NullableDecl V result) {
            set(result);
        }
    }
}
