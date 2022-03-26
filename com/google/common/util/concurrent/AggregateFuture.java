package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public abstract class AggregateFuture<InputT, OutputT> extends AggregateFutureState<OutputT> {
    private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
    private final boolean allMustSucceed;
    private final boolean collectsValues;
    @NullableDecl
    private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;

    /* loaded from: classes3.dex */
    public enum ReleaseResourcesReason {
        OUTPUT_FUTURE_DONE,
        ALL_INPUT_FUTURES_PROCESSED
    }

    abstract void collectOneValue(int i, @NullableDecl InputT inputt);

    abstract void handleAllCompleted();

    public AggregateFuture(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
        super(futures.size());
        this.futures = (ImmutableCollection) Preconditions.checkNotNull(futures);
        this.allMustSucceed = allMustSucceed;
        this.collectsValues = collectsValues;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected final void afterDone() {
        super.afterDone();
        ImmutableCollection<? extends Future<?>> localFutures = this.futures;
        releaseResources(ReleaseResourcesReason.OUTPUT_FUTURE_DONE);
        if (isCancelled() && (localFutures != null)) {
            boolean wasInterrupted = wasInterrupted();
            UnmodifiableIterator<? extends Future<?>> it = localFutures.iterator();
            while (it.hasNext()) {
                ((Future) it.next()).cancel(wasInterrupted);
            }
        }
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected final String pendingToString() {
        ImmutableCollection<? extends Future<?>> localFutures = this.futures;
        if (localFutures == null) {
            return super.pendingToString();
        }
        return "futures=" + localFutures;
    }

    /* JADX INFO: Multiple debug info for r0v8 'i'  int: [D('index' int), D('i' int)] */
    final void init() {
        if (this.futures.isEmpty()) {
            handleAllCompleted();
        } else if (this.allMustSucceed) {
            final int index = 0;
            UnmodifiableIterator<? extends ListenableFuture<? extends InputT>> it = this.futures.iterator();
            while (it.hasNext()) {
                final ListenableFuture<? extends InputT> future = (ListenableFuture) it.next();
                future.addListener(new Runnable() { // from class: com.google.common.util.concurrent.AggregateFuture.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (future.isCancelled()) {
                                AggregateFuture.this.futures = null;
                                AggregateFuture.this.cancel(false);
                            } else {
                                AggregateFuture.this.collectValueFromNonCancelledFuture(index, future);
                            }
                        } finally {
                            AggregateFuture.this.decrementCountAndMaybeComplete(null);
                        }
                    }
                }, MoreExecutors.directExecutor());
                index++;
            }
        } else {
            final ImmutableCollection<? extends Future<? extends InputT>> localFutures = this.collectsValues ? this.futures : null;
            Runnable listener = new Runnable() { // from class: com.google.common.util.concurrent.AggregateFuture.2
                @Override // java.lang.Runnable
                public void run() {
                    AggregateFuture.this.decrementCountAndMaybeComplete(localFutures);
                }
            };
            UnmodifiableIterator<? extends ListenableFuture<? extends InputT>> it2 = this.futures.iterator();
            while (it2.hasNext()) {
                ((ListenableFuture) it2.next()).addListener(listener, MoreExecutors.directExecutor());
            }
        }
    }

    private void handleException(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        if (this.allMustSucceed && !setException(throwable) && addCausalChain(getOrInitSeenExceptions(), throwable)) {
            log(throwable);
        } else if (throwable instanceof Error) {
            log(throwable);
        }
    }

    private static void log(Throwable throwable) {
        logger.log(Level.SEVERE, throwable instanceof Error ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first", throwable);
    }

    @Override // com.google.common.util.concurrent.AggregateFutureState
    final void addInitialException(Set<Throwable> seen) {
        Preconditions.checkNotNull(seen);
        if (!isCancelled()) {
            addCausalChain(seen, tryInternalFastPathGetFailure());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void collectValueFromNonCancelledFuture(int index, Future<? extends InputT> future) {
        try {
            collectOneValue(index, Futures.getDone(future));
        } catch (ExecutionException e) {
            handleException(e.getCause());
        } catch (Throwable t) {
            handleException(t);
        }
    }

    public void decrementCountAndMaybeComplete(@NullableDecl ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
        int newRemaining = decrementRemainingAndGet();
        Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
        if (newRemaining == 0) {
            processCompleted(futuresIfNeedToCollectAtCompletion);
        }
    }

    private void processCompleted(@NullableDecl ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
        if (futuresIfNeedToCollectAtCompletion != null) {
            int i = 0;
            UnmodifiableIterator<? extends Future<? extends InputT>> it = futuresIfNeedToCollectAtCompletion.iterator();
            while (it.hasNext()) {
                Future<? extends InputT> future = (Future) it.next();
                if (!future.isCancelled()) {
                    collectValueFromNonCancelledFuture(i, future);
                }
                i++;
            }
        }
        clearSeenExceptions();
        handleAllCompleted();
        releaseResources(ReleaseResourcesReason.ALL_INPUT_FUTURES_PROCESSED);
    }

    public void releaseResources(ReleaseResourcesReason reason) {
        Preconditions.checkNotNull(reason);
        this.futures = null;
    }

    private static boolean addCausalChain(Set<Throwable> seen, Throwable t) {
        while (t != null) {
            if (!seen.add(t)) {
                return false;
            }
            t = t.getCause();
        }
        return true;
    }
}
