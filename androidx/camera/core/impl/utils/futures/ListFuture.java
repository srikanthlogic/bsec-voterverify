package androidx.camera.core.impl.utils.futures;

import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ListFuture<V> implements ListenableFuture<List<V>> {
    private final boolean mAllMustSucceed;
    List<? extends ListenableFuture<? extends V>> mFutures;
    private final AtomicInteger mRemaining;
    private final ListenableFuture<List<V>> mResult = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<List<V>>() { // from class: androidx.camera.core.impl.utils.futures.ListFuture.1
        @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
        public Object attachCompleter(CallbackToFutureAdapter.Completer<List<V>> completer) {
            Preconditions.checkState(ListFuture.this.mResultNotifier == null, "The result can only set once!");
            ListFuture.this.mResultNotifier = completer;
            return "ListFuture[" + this + "]";
        }
    });
    CallbackToFutureAdapter.Completer<List<V>> mResultNotifier;
    List<V> mValues;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ListFuture(List<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor) {
        this.mFutures = (List) Preconditions.checkNotNull(futures);
        this.mValues = new ArrayList(futures.size());
        this.mAllMustSucceed = allMustSucceed;
        this.mRemaining = new AtomicInteger(futures.size());
        init(listenerExecutor);
    }

    /* JADX INFO: Multiple debug info for r0v5 java.util.List<? extends com.google.common.util.concurrent.ListenableFuture<? extends V>>: [D('i' int), D('localFutures' java.util.List<? extends com.google.common.util.concurrent.ListenableFuture<? extends V>>)] */
    private void init(Executor listenerExecutor) {
        addListener(new Runnable() { // from class: androidx.camera.core.impl.utils.futures.ListFuture.2
            @Override // java.lang.Runnable
            public void run() {
                ListFuture listFuture = ListFuture.this;
                listFuture.mValues = null;
                listFuture.mFutures = null;
            }
        }, CameraXExecutors.directExecutor());
        if (this.mFutures.isEmpty()) {
            this.mResultNotifier.set(new ArrayList(this.mValues));
            return;
        }
        for (int i = 0; i < this.mFutures.size(); i++) {
            this.mValues.add(null);
        }
        List<? extends ListenableFuture<? extends V>> localFutures = this.mFutures;
        for (final int i2 = 0; i2 < localFutures.size(); i2++) {
            final ListenableFuture<? extends V> listenable = (ListenableFuture) localFutures.get(i2);
            listenable.addListener(new Runnable() { // from class: androidx.camera.core.impl.utils.futures.ListFuture.3
                @Override // java.lang.Runnable
                public void run() {
                    ListFuture.this.setOneValue(i2, listenable);
                }
            }, listenerExecutor);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void setOneValue(int index, Future<? extends V> future) {
        ArrayList arrayList;
        CallbackToFutureAdapter.Completer<List<V>> completer;
        int newRemaining;
        List<V> localValues = this.mValues;
        boolean isDone = isDone();
        if (isDone || localValues == 0) {
            Preconditions.checkState(this.mAllMustSucceed, "Future was done before all dependencies completed");
            return;
        }
        try {
            isDone = true;
            try {
                try {
                    Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
                    localValues.set(index, Futures.getUninterruptibly(future));
                    newRemaining = this.mRemaining.decrementAndGet();
                    if (newRemaining < 0) {
                        isDone = false;
                    }
                    Preconditions.checkState(isDone, "Less than 0 remaining futures");
                } catch (CancellationException e) {
                    if (this.mAllMustSucceed) {
                        cancel(false);
                    }
                    int newRemaining2 = this.mRemaining.decrementAndGet();
                    if (newRemaining2 < 0) {
                        isDone = false;
                    }
                    Preconditions.checkState(isDone, "Less than 0 remaining futures");
                    if (newRemaining2 == 0) {
                        List<V> localValues2 = this.mValues;
                        if (localValues2 != null) {
                            completer = this.mResultNotifier;
                            arrayList = new ArrayList(localValues2);
                        }
                    } else {
                        return;
                    }
                } catch (ExecutionException e2) {
                    if (this.mAllMustSucceed) {
                        this.mResultNotifier.setException(e2.getCause());
                    }
                    int newRemaining3 = this.mRemaining.decrementAndGet();
                    if (newRemaining3 < 0) {
                        isDone = false;
                    }
                    Preconditions.checkState(isDone, "Less than 0 remaining futures");
                    if (newRemaining3 == 0) {
                        List<V> localValues3 = this.mValues;
                        if (localValues3 != null) {
                            completer = this.mResultNotifier;
                            arrayList = new ArrayList(localValues3);
                        }
                    } else {
                        return;
                    }
                }
            } catch (Error e3) {
                this.mResultNotifier.setException(e3);
                int newRemaining4 = this.mRemaining.decrementAndGet();
                if (newRemaining4 < 0) {
                    isDone = false;
                }
                Preconditions.checkState(isDone, "Less than 0 remaining futures");
                if (newRemaining4 == 0) {
                    List<V> localValues4 = this.mValues;
                    if (localValues4 != null) {
                        completer = this.mResultNotifier;
                        arrayList = new ArrayList(localValues4);
                    }
                } else {
                    return;
                }
            } catch (RuntimeException e4) {
                if (this.mAllMustSucceed) {
                    this.mResultNotifier.setException(e4);
                }
                int newRemaining5 = this.mRemaining.decrementAndGet();
                if (newRemaining5 < 0) {
                    isDone = false;
                }
                Preconditions.checkState(isDone, "Less than 0 remaining futures");
                if (newRemaining5 == 0) {
                    List<V> localValues5 = this.mValues;
                    if (localValues5 != null) {
                        completer = this.mResultNotifier;
                        arrayList = new ArrayList(localValues5);
                    }
                } else {
                    return;
                }
            }
            if (newRemaining == 0) {
                List<V> localValues6 = this.mValues;
                if (localValues6 != null) {
                    completer = this.mResultNotifier;
                    arrayList = new ArrayList(localValues6);
                    completer.set(arrayList);
                    return;
                }
                Preconditions.checkState(isDone());
            }
        } catch (Throwable th) {
            int newRemaining6 = this.mRemaining.decrementAndGet();
            if (newRemaining6 < 0) {
                isDone = false;
            }
            Preconditions.checkState(isDone, "Less than 0 remaining futures");
            if (newRemaining6 == 0) {
                List<V> localValues7 = this.mValues;
                if (localValues7 != null) {
                    this.mResultNotifier.set(new ArrayList(localValues7));
                } else {
                    Preconditions.checkState(isDone());
                }
            }
            throw th;
        }
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable listener, Executor executor) {
        this.mResult.addListener(listener, executor);
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        List<? extends ListenableFuture<? extends V>> list = this.mFutures;
        if (list != null) {
            for (ListenableFuture<? extends V> f : list) {
                f.cancel(mayInterruptIfRunning);
            }
        }
        return this.mResult.cancel(mayInterruptIfRunning);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.mResult.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.mResult.isDone();
    }

    @Override // java.util.concurrent.Future
    public List<V> get() throws InterruptedException, ExecutionException {
        callAllGets();
        return this.mResult.get();
    }

    @Override // java.util.concurrent.Future
    public List<V> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mResult.get(timeout, unit);
    }

    private void callAllGets() throws InterruptedException {
        List<? extends ListenableFuture<? extends V>> oldFutures = this.mFutures;
        if (!(oldFutures == null || isDone())) {
            for (ListenableFuture<? extends V> future : oldFutures) {
                while (!future.isDone()) {
                    try {
                        future.get();
                    } catch (Error e) {
                        throw e;
                    } catch (InterruptedException e2) {
                        throw e2;
                    } catch (Throwable th) {
                        if (this.mAllMustSucceed) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
