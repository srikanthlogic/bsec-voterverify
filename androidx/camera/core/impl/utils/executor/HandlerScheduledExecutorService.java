package androidx.camera.core.impl.utils.executor;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public final class HandlerScheduledExecutorService extends AbstractExecutorService implements ScheduledExecutorService {
    private static ThreadLocal<ScheduledExecutorService> sThreadLocalInstance = new ThreadLocal<ScheduledExecutorService>() { // from class: androidx.camera.core.impl.utils.executor.HandlerScheduledExecutorService.1
        @Override // java.lang.ThreadLocal
        public ScheduledExecutorService initialValue() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                return CameraXExecutors.mainThreadExecutor();
            }
            if (Looper.myLooper() != null) {
                return new HandlerScheduledExecutorService(new Handler(Looper.myLooper()));
            }
            return null;
        }
    };
    private final Handler mHandler;

    public HandlerScheduledExecutorService(Handler handler) {
        this.mHandler = handler;
    }

    public static ScheduledExecutorService currentThreadExecutor() {
        ScheduledExecutorService executor = sThreadLocalInstance.get();
        if (executor != null) {
            return executor;
        }
        Looper looper = Looper.myLooper();
        if (looper != null) {
            ScheduledExecutorService executor2 = new HandlerScheduledExecutorService(new Handler(looper));
            sThreadLocalInstance.set(executor2);
            return executor2;
        }
        throw new IllegalStateException("Current thread has no looper!");
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(final Runnable command, long delay, TimeUnit unit) {
        return schedule(new Callable<Void>() { // from class: androidx.camera.core.impl.utils.executor.HandlerScheduledExecutorService.2
            @Override // java.util.concurrent.Callable
            public Void call() {
                command.run();
                return null;
            }
        }, delay, unit);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        long runAtMillis = SystemClock.uptimeMillis() + TimeUnit.MILLISECONDS.convert(delay, unit);
        HandlerScheduledFuture<V> future = new HandlerScheduledFuture<>(this.mHandler, runAtMillis, callable);
        if (this.mHandler.postAtTime(future, runAtMillis)) {
            return future;
        }
        return Futures.immediateFailedScheduledFuture(createPostFailedException());
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        throw new UnsupportedOperationException(HandlerScheduledExecutorService.class.getSimpleName() + " does not yet support fixed-rate scheduling.");
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException(HandlerScheduledExecutorService.class.getSimpleName() + " does not yet support fixed-delay scheduling.");
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        throw new UnsupportedOperationException(HandlerScheduledExecutorService.class.getSimpleName() + " cannot be shut down. Use Looper.quitSafely().");
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException(HandlerScheduledExecutorService.class.getSimpleName() + " cannot be shut down. Use Looper.quitSafely().");
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException(HandlerScheduledExecutorService.class.getSimpleName() + " cannot be shut down. Use Looper.quitSafely().");
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        if (!this.mHandler.post(command)) {
            throw createPostFailedException();
        }
    }

    private RejectedExecutionException createPostFailedException() {
        return new RejectedExecutionException(this.mHandler + " is shutting down");
    }

    /* loaded from: classes.dex */
    public static class HandlerScheduledFuture<V> implements RunnableScheduledFuture<V> {
        final AtomicReference<CallbackToFutureAdapter.Completer<V>> mCompleter = new AtomicReference<>(null);
        private final ListenableFuture<V> mDelegate;
        private final long mRunAtMillis;
        private final Callable<V> mTask;

        HandlerScheduledFuture(final Handler handler, long runAtMillis, final Callable<V> task) {
            this.mRunAtMillis = runAtMillis;
            this.mTask = task;
            this.mDelegate = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<V>() { // from class: androidx.camera.core.impl.utils.executor.HandlerScheduledExecutorService.HandlerScheduledFuture.1
                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public Object attachCompleter(CallbackToFutureAdapter.Completer<V> completer) throws RejectedExecutionException {
                    completer.addCancellationListener(new Runnable() { // from class: androidx.camera.core.impl.utils.executor.HandlerScheduledExecutorService.HandlerScheduledFuture.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (HandlerScheduledFuture.this.mCompleter.getAndSet(null) != null) {
                                handler.removeCallbacks(HandlerScheduledFuture.this);
                            }
                        }
                    }, CameraXExecutors.directExecutor());
                    HandlerScheduledFuture.this.mCompleter.set(completer);
                    return "HandlerScheduledFuture-" + task.toString();
                }
            });
        }

        @Override // java.util.concurrent.RunnableScheduledFuture
        public boolean isPeriodic() {
            return false;
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.mRunAtMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        public int compareTo(Delayed o) {
            return Long.compare(getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public void run() {
            CallbackToFutureAdapter.Completer<V> completer = this.mCompleter.getAndSet(null);
            if (completer != null) {
                try {
                    completer.set(this.mTask.call());
                } catch (Exception e) {
                    completer.setException(e);
                }
            }
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            return this.mDelegate.cancel(mayInterruptIfRunning);
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return this.mDelegate.isCancelled();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return this.mDelegate.isDone();
        }

        @Override // java.util.concurrent.Future
        public V get() throws ExecutionException, InterruptedException {
            return this.mDelegate.get();
        }

        @Override // java.util.concurrent.Future
        public V get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
            return this.mDelegate.get(timeout, unit);
        }
    }
}
