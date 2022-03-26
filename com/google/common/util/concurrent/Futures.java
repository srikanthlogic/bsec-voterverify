package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.CollectionFuture;
import com.google.common.util.concurrent.ImmediateFuture;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class Futures extends GwtFuturesCatchingSpecialization {
    private Futures() {
    }

    public static <V> ListenableFuture<V> immediateFuture(@NullableDecl V value) {
        if (value == null) {
            return (ListenableFuture<V>) ImmediateFuture.NULL;
        }
        return new ImmediateFuture(value);
    }

    public static ListenableFuture<Void> immediateVoidFuture() {
        return ImmediateFuture.NULL;
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFuture.ImmediateFailedFuture(throwable);
    }

    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new ImmediateFuture.ImmediateCancelledFuture();
    }

    public static <O> ListenableFuture<O> submit(Callable<O> callable, Executor executor) {
        TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
        executor.execute(task);
        return task;
    }

    public static ListenableFuture<Void> submit(Runnable runnable, Executor executor) {
        TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(runnable, null);
        executor.execute(task);
        return task;
    }

    public static <O> ListenableFuture<O> submitAsync(AsyncCallable<O> callable, Executor executor) {
        TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
        executor.execute(task);
        return task;
    }

    public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, long delay, TimeUnit timeUnit, ScheduledExecutorService executorService) {
        TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
        final Future<?> scheduled = executorService.schedule(task, delay, timeUnit);
        task.addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.1
            @Override // java.lang.Runnable
            public void run() {
                scheduled.cancel(false);
            }
        }, MoreExecutors.directExecutor());
        return task;
    }

    public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
    }

    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
    }

    public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
        if (delegate.isDone()) {
            return delegate;
        }
        return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
    }

    public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        return AbstractTransformFuture.create(input, function, executor);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
        return AbstractTransformFuture.create(input, function, executor);
    }

    public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(function);
        return new Future<O>() { // from class: com.google.common.util.concurrent.Futures.2
            @Override // java.util.concurrent.Future
            public boolean cancel(boolean mayInterruptIfRunning) {
                return input.cancel(mayInterruptIfRunning);
            }

            @Override // java.util.concurrent.Future
            public boolean isCancelled() {
                return input.isCancelled();
            }

            @Override // java.util.concurrent.Future
            public boolean isDone() {
                return input.isDone();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object, O] */
            @Override // java.util.concurrent.Future
            public O get() throws InterruptedException, ExecutionException {
                return applyTransformation(input.get());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object, O] */
            @Override // java.util.concurrent.Future
            public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return applyTransformation(input.get(timeout, unit));
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object, O] */
            private O applyTransformation(I input2) throws ExecutionException {
                try {
                    return function.apply(input2);
                } catch (Throwable t) {
                    throw new ExecutionException(t);
                }
            }
        };
    }

    @SafeVarargs
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
    }

    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
    }

    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures) {
        return new FutureCombiner<>(false, ImmutableList.copyOf(futures));
    }

    public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner<>(false, ImmutableList.copyOf(futures));
    }

    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures) {
        return new FutureCombiner<>(true, ImmutableList.copyOf(futures));
    }

    public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner<>(true, ImmutableList.copyOf(futures));
    }

    /* loaded from: classes3.dex */
    public static final class FutureCombiner<V> {
        private final boolean allMustSucceed;
        private final ImmutableList<ListenableFuture<? extends V>> futures;

        private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
            this.allMustSucceed = allMustSucceed;
            this.futures = futures;
        }

        public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor) {
            return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
        }

        public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor) {
            return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
        }

        public ListenableFuture<?> run(final Runnable combiner, Executor executor) {
            return call(new Callable<Void>() { // from class: com.google.common.util.concurrent.Futures.FutureCombiner.1
                @Override // java.util.concurrent.Callable
                public Void call() throws Exception {
                    combiner.run();
                    return null;
                }
            }, executor);
        }
    }

    public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
        if (future.isDone()) {
            return future;
        }
        NonCancellationPropagatingFuture<V> output = new NonCancellationPropagatingFuture<>(future);
        future.addListener(output, MoreExecutors.directExecutor());
        return output;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class NonCancellationPropagatingFuture<V> extends AbstractFuture.TrustedFuture<V> implements Runnable {
        private ListenableFuture<V> delegate;

        NonCancellationPropagatingFuture(ListenableFuture<V> delegate) {
            this.delegate = delegate;
        }

        @Override // java.lang.Runnable
        public void run() {
            ListenableFuture<V> localDelegate = this.delegate;
            if (localDelegate != null) {
                setFuture(localDelegate);
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture
        protected String pendingToString() {
            ListenableFuture<V> localDelegate = this.delegate;
            if (localDelegate == null) {
                return null;
            }
            return "delegate=[" + localDelegate + "]";
        }

        @Override // com.google.common.util.concurrent.AbstractFuture
        protected void afterDone() {
            this.delegate = null;
        }
    }

    @SafeVarargs
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
    }

    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
    }

    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
        Collection<ListenableFuture<? extends T>> collection;
        if (futures instanceof Collection) {
            collection = (Collection) futures;
        } else {
            collection = ImmutableList.copyOf(futures);
        }
        ListenableFuture<? extends T>[] copy = (ListenableFuture[]) collection.toArray(new ListenableFuture[collection.size()]);
        final InCompletionOrderState<T> state = new InCompletionOrderState<>(copy);
        ImmutableList.Builder<AbstractFuture<T>> delegatesBuilder = ImmutableList.builder();
        for (int i = 0; i < copy.length; i++) {
            delegatesBuilder.add((ImmutableList.Builder<AbstractFuture<T>>) new InCompletionOrderFuture<>(state));
        }
        final ImmutableList<AbstractFuture<T>> delegates = delegatesBuilder.build();
        for (final int i2 = 0; i2 < copy.length; i2++) {
            copy[i2].addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.3
                @Override // java.lang.Runnable
                public void run() {
                    state.recordInputCompletion(delegates, i2);
                }
            }, MoreExecutors.directExecutor());
        }
        return delegates;
    }

    /* loaded from: classes3.dex */
    private static final class InCompletionOrderFuture<T> extends AbstractFuture<T> {
        private InCompletionOrderState<T> state;

        private InCompletionOrderFuture(InCompletionOrderState<T> state) {
            this.state = state;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public boolean cancel(boolean interruptIfRunning) {
            InCompletionOrderState<T> localState = this.state;
            if (!super.cancel(interruptIfRunning)) {
                return false;
            }
            localState.recordOutputCancellation(interruptIfRunning);
            return true;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture
        protected void afterDone() {
            this.state = null;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture
        protected String pendingToString() {
            InCompletionOrderState<T> localState = this.state;
            if (localState == null) {
                return null;
            }
            return "inputCount=[" + ((InCompletionOrderState) localState).inputFutures.length + "], remaining=[" + ((InCompletionOrderState) localState).incompleteOutputCount.get() + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class InCompletionOrderState<T> {
        private volatile int delegateIndex;
        private final AtomicInteger incompleteOutputCount;
        private final ListenableFuture<? extends T>[] inputFutures;
        private boolean shouldInterrupt;
        private boolean wasCancelled;

        private InCompletionOrderState(ListenableFuture<? extends T>[] inputFutures) {
            this.wasCancelled = false;
            this.shouldInterrupt = true;
            this.delegateIndex = 0;
            this.inputFutures = inputFutures;
            this.incompleteOutputCount = new AtomicInteger(inputFutures.length);
        }

        public void recordOutputCancellation(boolean interruptIfRunning) {
            this.wasCancelled = true;
            if (!interruptIfRunning) {
                this.shouldInterrupt = false;
            }
            recordCompletion();
        }

        public void recordInputCompletion(ImmutableList<AbstractFuture<T>> delegates, int inputFutureIndex) {
            ListenableFuture<? extends T>[] listenableFutureArr = this.inputFutures;
            ListenableFuture<? extends T> inputFuture = listenableFutureArr[inputFutureIndex];
            listenableFutureArr[inputFutureIndex] = null;
            for (int i = this.delegateIndex; i < delegates.size(); i++) {
                if (delegates.get(i).setFuture(inputFuture)) {
                    recordCompletion();
                    this.delegateIndex = i + 1;
                    return;
                }
            }
            this.delegateIndex = delegates.size();
        }

        private void recordCompletion() {
            if (this.incompleteOutputCount.decrementAndGet() == 0 && this.wasCancelled) {
                ListenableFuture<?>[] listenableFutureArr = this.inputFutures;
                for (ListenableFuture<?> toCancel : listenableFutureArr) {
                    if (toCancel != null) {
                        toCancel.cancel(this.shouldInterrupt);
                    }
                }
            }
        }
    }

    public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor) {
        Preconditions.checkNotNull(callback);
        future.addListener(new CallbackListener(future, callback), executor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class CallbackListener<V> implements Runnable {
        final FutureCallback<? super V> callback;
        final Future<V> future;

        CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
            this.future = future;
            this.callback = callback;
        }

        @Override // java.lang.Runnable
        public void run() {
            Throwable e;
            Throwable failure;
            Future<V> future = this.future;
            if (!(future instanceof InternalFutureFailureAccess) || (failure = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) future)) == null) {
                try {
                    this.callback.onSuccess(Futures.getDone(this.future));
                } catch (Error e2) {
                    e = e2;
                    this.callback.onFailure(e);
                } catch (RuntimeException e3) {
                    e = e3;
                    this.callback.onFailure(e);
                } catch (ExecutionException e4) {
                    this.callback.onFailure(e4.getCause());
                }
            } else {
                this.callback.onFailure(failure);
            }
        }

        @Override // java.lang.Object
        public String toString() {
            return MoreObjects.toStringHelper(this).addValue(this.callback).toString();
        }
    }

    public static <V> V getDone(Future<V> future) throws ExecutionException {
        Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
        return (V) Uninterruptibles.getUninterruptibly(future);
    }

    public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws Exception {
        return (V) FuturesGetChecked.getChecked(future, exceptionClass);
    }

    public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws Exception {
        return (V) FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
    }

    public static <V> V getUnchecked(Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return (V) Uninterruptibles.getUninterruptibly(future);
        } catch (ExecutionException e) {
            wrapAndThrowUnchecked(e.getCause());
            throw new AssertionError();
        }
    }

    private static void wrapAndThrowUnchecked(Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error) cause);
        }
        throw new UncheckedExecutionException(cause);
    }
}
