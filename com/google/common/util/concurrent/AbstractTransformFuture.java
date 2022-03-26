package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FluentFuture;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public abstract class AbstractTransformFuture<I, O, F, T> extends FluentFuture.TrustedFuture<O> implements Runnable {
    @NullableDecl
    F function;
    @NullableDecl
    ListenableFuture<? extends I> inputFuture;

    @NullableDecl
    abstract T doTransform(F f, @NullableDecl I i) throws Exception;

    abstract void setResult(@NullableDecl T t);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(executor);
        AsyncTransformFuture<I, O> output = new AsyncTransformFuture<>(input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return output;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(function);
        TransformFuture<I, O> output = new TransformFuture<>(input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return output;
    }

    AbstractTransformFuture(ListenableFuture<? extends I> inputFuture, F function) {
        this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(inputFuture);
        this.function = (F) Preconditions.checkNotNull(function);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public final void run() {
        ListenableFuture<? extends I> localInputFuture = this.inputFuture;
        F localFunction = this.function;
        boolean z = true;
        boolean isCancelled = isCancelled() | (localInputFuture == null);
        if (localFunction != null) {
            z = false;
        }
        if (!isCancelled && !z) {
            this.inputFuture = null;
            if (localInputFuture.isCancelled()) {
                setFuture(localInputFuture);
                return;
            }
            try {
                try {
                    T transformResult = doTransform(localFunction, Futures.getDone(localInputFuture));
                    this.function = null;
                    setResult(transformResult);
                } catch (Throwable t) {
                    try {
                        setException(t);
                        this.function = null;
                    } catch (Throwable t2) {
                        this.function = null;
                        throw t2;
                    }
                }
            } catch (Error e) {
                setException(e);
            } catch (CancellationException e2) {
                cancel(false);
            } catch (RuntimeException e3) {
                setException(e3);
            } catch (ExecutionException e4) {
                setException(e4.getCause());
            }
        }
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected final void afterDone() {
        maybePropagateCancellationTo(this.inputFuture);
        this.inputFuture = null;
        this.function = null;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected String pendingToString() {
        ListenableFuture<? extends I> localInputFuture = this.inputFuture;
        F localFunction = this.function;
        String superString = super.pendingToString();
        String resultString = "";
        if (localInputFuture != null) {
            resultString = "inputFuture=[" + localInputFuture + "], ";
        }
        if (localFunction != null) {
            return resultString + "function=[" + localFunction + "]";
        } else if (superString == null) {
            return null;
        } else {
            return resultString + superString;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class AsyncTransformFuture<I, O> extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        /* bridge */ /* synthetic */ Object doTransform(Object obj, @NullableDecl Object obj2) throws Exception {
            return doTransform((AsyncFunction<? super AsyncFunction<? super I, ? extends O>, ? extends O>) ((AsyncFunction) obj), (AsyncFunction<? super I, ? extends O>) obj2);
        }

        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        /* bridge */ /* synthetic */ void setResult(Object obj) {
            setResult((ListenableFuture) ((ListenableFuture) obj));
        }

        AsyncTransformFuture(ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function) {
            super(inputFuture, function);
        }

        /* JADX WARN: Multi-variable type inference failed */
        ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> function, @NullableDecl I input) throws Exception {
            ListenableFuture<? extends O> outputFuture = function.apply(input);
            Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", function);
            return outputFuture;
        }

        /* JADX WARN: Multi-variable type inference failed */
        void setResult(ListenableFuture<? extends O> result) {
            setFuture(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        @NullableDecl
        /* bridge */ /* synthetic */ Object doTransform(Object obj, @NullableDecl Object obj2) throws Exception {
            return doTransform((Function<? super Function<? super I, ? extends O>, ? extends O>) ((Function) obj), (Function<? super I, ? extends O>) obj2);
        }

        TransformFuture(ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function) {
            super(inputFuture, function);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @NullableDecl
        O doTransform(Function<? super I, ? extends O> function, @NullableDecl I input) {
            return (O) function.apply(input);
        }

        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        void setResult(@NullableDecl O result) {
            set(result);
        }
    }
}
