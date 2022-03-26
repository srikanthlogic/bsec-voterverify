package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ForwardingListenableFuture;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes3.dex */
public final class MoreExecutors {
    private MoreExecutors() {
    }

    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
        return new Application().getExitingExecutorService(executor, terminationTimeout, timeUnit);
    }

    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
        return new Application().getExitingExecutorService(executor);
    }

    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
        return new Application().getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
    }

    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
        return new Application().getExitingScheduledExecutorService(executor);
    }

    public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
        new Application().addDelayedShutdownHook(service, terminationTimeout, timeUnit);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class Application {
        Application() {
        }

        final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
            MoreExecutors.useDaemonThreadFactory(executor);
            ExecutorService service = Executors.unconfigurableExecutorService(executor);
            addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
            return service;
        }

        final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
            return getExitingExecutorService(executor, 120, TimeUnit.SECONDS);
        }

        final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
            MoreExecutors.useDaemonThreadFactory(executor);
            ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
            addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
            return service;
        }

        final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
            return getExitingScheduledExecutorService(executor, 120, TimeUnit.SECONDS);
        }

        final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
            Preconditions.checkNotNull(service);
            Preconditions.checkNotNull(timeUnit);
            addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable() { // from class: com.google.common.util.concurrent.MoreExecutors.Application.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        service.shutdown();
                        service.awaitTermination(terminationTimeout, timeUnit);
                    } catch (InterruptedException e) {
                    }
                }
            }));
        }

        void addShutdownHook(Thread hook) {
            Runtime.getRuntime().addShutdownHook(hook);
        }
    }

    public static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
        executor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
    }

    /* loaded from: classes3.dex */
    private static final class DirectExecutorService extends AbstractListeningExecutorService {
        private final Object lock;
        private int runningTasks;
        private boolean shutdown;

        private DirectExecutorService() {
            this.lock = new Object();
            this.runningTasks = 0;
            this.shutdown = false;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            startTask();
            try {
                command.run();
            } finally {
                endTask();
            }
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isShutdown() {
            boolean z;
            synchronized (this.lock) {
                z = this.shutdown;
            }
            return z;
        }

        @Override // java.util.concurrent.ExecutorService
        public void shutdown() {
            synchronized (this.lock) {
                this.shutdown = true;
                if (this.runningTasks == 0) {
                    this.lock.notifyAll();
                }
            }
        }

        @Override // java.util.concurrent.ExecutorService
        public List<Runnable> shutdownNow() {
            shutdown();
            return Collections.emptyList();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isTerminated() {
            boolean z;
            synchronized (this.lock) {
                z = this.shutdown && this.runningTasks == 0;
            }
            return z;
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            long nanos = unit.toNanos(timeout);
            synchronized (this.lock) {
                while (true) {
                    if (this.shutdown && this.runningTasks == 0) {
                        return true;
                    }
                    if (nanos <= 0) {
                        return false;
                    }
                    long now = System.nanoTime();
                    TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
                    nanos -= System.nanoTime() - now;
                }
            }
        }

        private void startTask() {
            synchronized (this.lock) {
                if (!this.shutdown) {
                    this.runningTasks++;
                } else {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
            }
        }

        private void endTask() {
            synchronized (this.lock) {
                int numRunning = this.runningTasks - 1;
                this.runningTasks = numRunning;
                if (numRunning == 0) {
                    this.lock.notifyAll();
                }
            }
        }
    }

    public static ListeningExecutorService newDirectExecutorService() {
        return new DirectExecutorService();
    }

    public static Executor directExecutor() {
        return DirectExecutor.INSTANCE;
    }

    public static Executor newSequentialExecutor(Executor delegate) {
        return new SequentialExecutor(delegate);
    }

    public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
        if (delegate instanceof ListeningExecutorService) {
            return (ListeningExecutorService) delegate;
        }
        return delegate instanceof ScheduledExecutorService ? new ScheduledListeningDecorator((ScheduledExecutorService) delegate) : new ListeningDecorator(delegate);
    }

    public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
        return delegate instanceof ListeningScheduledExecutorService ? (ListeningScheduledExecutorService) delegate : new ScheduledListeningDecorator(delegate);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class ListeningDecorator extends AbstractListeningExecutorService {
        private final ExecutorService delegate;

        ListeningDecorator(ExecutorService delegate) {
            this.delegate = (ExecutorService) Preconditions.checkNotNull(delegate);
        }

        @Override // java.util.concurrent.ExecutorService
        public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return this.delegate.awaitTermination(timeout, unit);
        }

        @Override // java.util.concurrent.ExecutorService
        public final boolean isShutdown() {
            return this.delegate.isShutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public final boolean isTerminated() {
            return this.delegate.isTerminated();
        }

        @Override // java.util.concurrent.ExecutorService
        public final void shutdown() {
            this.delegate.shutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public final List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }

        @Override // java.util.concurrent.Executor
        public final void execute(Runnable command) {
            this.delegate.execute(command);
        }
    }

    /* loaded from: classes3.dex */
    private static final class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService {
        final ScheduledExecutorService delegate;

        ScheduledListeningDecorator(ScheduledExecutorService delegate) {
            super(delegate);
            this.delegate = (ScheduledExecutorService) Preconditions.checkNotNull(delegate);
        }

        @Override // com.google.common.util.concurrent.ListeningScheduledExecutorService, java.util.concurrent.ScheduledExecutorService
        public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(command, null);
            return new ListenableScheduledTask(task, this.delegate.schedule(task, delay, unit));
        }

        @Override // com.google.common.util.concurrent.ListeningScheduledExecutorService, java.util.concurrent.ScheduledExecutorService
        public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(callable);
            return new ListenableScheduledTask(task, this.delegate.schedule(task, delay, unit));
        }

        @Override // com.google.common.util.concurrent.ListeningScheduledExecutorService, java.util.concurrent.ScheduledExecutorService
        public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            return new ListenableScheduledTask(task, this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit));
        }

        @Override // com.google.common.util.concurrent.ListeningScheduledExecutorService, java.util.concurrent.ScheduledExecutorService
        public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
            NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            return new ListenableScheduledTask(task, this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit));
        }

        /* loaded from: classes3.dex */
        public static final class ListenableScheduledTask<V> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V> {
            private final ScheduledFuture<?> scheduledDelegate;

            public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
                super(listenableDelegate);
                this.scheduledDelegate = scheduledDelegate;
            }

            @Override // com.google.common.util.concurrent.ForwardingFuture, java.util.concurrent.Future
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean cancelled = super.cancel(mayInterruptIfRunning);
                if (cancelled) {
                    this.scheduledDelegate.cancel(mayInterruptIfRunning);
                }
                return cancelled;
            }

            @Override // java.util.concurrent.Delayed
            public long getDelay(TimeUnit unit) {
                return this.scheduledDelegate.getDelay(unit);
            }

            public int compareTo(Delayed other) {
                return this.scheduledDelegate.compareTo(other);
            }
        }

        /* loaded from: classes3.dex */
        public static final class NeverSuccessfulListenableFutureTask extends AbstractFuture.TrustedFuture<Void> implements Runnable {
            private final Runnable delegate;

            public NeverSuccessfulListenableFutureTask(Runnable delegate) {
                this.delegate = (Runnable) Preconditions.checkNotNull(delegate);
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    this.delegate.run();
                } catch (Throwable t) {
                    setException(t);
                    throw Throwables.propagate(t);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00e2 A[LOOP:2: B:48:0x00dc->B:50:0x00e2, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long lastTime;
        int ntasks;
        Future<T> f;
        ListeningExecutorService listeningExecutorService = executorService;
        Preconditions.checkNotNull(executorService);
        Preconditions.checkNotNull(unit);
        int ntasks2 = tasks.size();
        Preconditions.checkArgument(ntasks2 > 0);
        List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks2);
        BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
        long timeoutNanos = unit.toNanos(timeout);
        if (timed) {
            try {
                lastTime = System.nanoTime();
            } catch (Throwable th) {
                ee = th;
                while (r1.hasNext()) {
                }
                throw ee;
            }
        } else {
            lastTime = 0;
        }
        Iterator<? extends Callable<T>> it = tasks.iterator();
        futures.add(submitAndAddQueueListener(listeningExecutorService, (Callable) it.next(), futureQueue));
        int ntasks3 = ntasks2 - 1;
        int active = 1;
        long lastTime2 = lastTime;
        ExecutionException ee = null;
        while (true) {
            Future<T> f2 = futureQueue.poll();
            if (f2 != null) {
                ntasks = ntasks3;
                f = f2;
            } else if (ntasks3 > 0) {
                futures.add(submitAndAddQueueListener(listeningExecutorService, (Callable) it.next(), futureQueue));
                active++;
                ntasks = ntasks3 - 1;
                f = f2;
            } else if (active == 0) {
                if (ee != null) {
                    throw ee;
                }
                throw new ExecutionException((Throwable) null);
            } else if (timed) {
                Future<T> f3 = futureQueue.poll(timeoutNanos, TimeUnit.NANOSECONDS);
                if (f3 != null) {
                    long now = System.nanoTime();
                    timeoutNanos -= now - lastTime2;
                    lastTime2 = now;
                    ntasks = ntasks3;
                    f = f3;
                } else {
                    throw new TimeoutException();
                }
            } else {
                ntasks = ntasks3;
                f = futureQueue.take();
            }
            if (f != null) {
                active--;
                try {
                    T t = f.get();
                    for (Future<T> f4 : futures) {
                        f4.cancel(true);
                    }
                    return t;
                } catch (RuntimeException rex) {
                    try {
                        ee = new ExecutionException(rex);
                    } catch (Throwable th2) {
                        ee = th2;
                        for (Future<T> f5 : futures) {
                            f5.cancel(true);
                        }
                        throw ee;
                    }
                } catch (ExecutionException ee2) {
                    ee = ee2;
                }
            }
            listeningExecutorService = executorService;
            ntasks3 = ntasks;
        }
    }

    private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
        final ListenableFuture<T> future = executorService.submit((Callable) task);
        future.addListener(new Runnable() { // from class: com.google.common.util.concurrent.MoreExecutors.1
            @Override // java.lang.Runnable
            public void run() {
                queue.add(future);
            }
        }, directExecutor());
        return future;
    }

    public static ThreadFactory platformThreadFactory() {
        if (!isAppEngineWithApiClasses()) {
            return Executors.defaultThreadFactory();
        }
        try {
            return (ThreadFactory) Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e3);
        } catch (InvocationTargetException e4) {
            throw Throwables.propagate(e4.getCause());
        }
    }

    private static boolean isAppEngineWithApiClasses() {
        if (System.getProperty("com.google.appengine.runtime.environment") == null) {
            return false;
        }
        try {
            Class.forName("com.google.appengine.api.utils.SystemProperty");
            try {
                if (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null) {
                    return true;
                }
                return false;
            } catch (ClassNotFoundException e) {
                return false;
            } catch (IllegalAccessException e2) {
                return false;
            } catch (NoSuchMethodException e3) {
                return false;
            } catch (InvocationTargetException e4) {
                return false;
            }
        } catch (ClassNotFoundException e5) {
            return false;
        }
    }

    public static Thread newThread(String name, Runnable runnable) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(runnable);
        Thread result = platformThreadFactory().newThread(runnable);
        try {
            result.setName(name);
        } catch (SecurityException e) {
        }
        return result;
    }

    public static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(nameSupplier);
        return new Executor() { // from class: com.google.common.util.concurrent.MoreExecutors.2
            @Override // java.util.concurrent.Executor
            public void execute(Runnable command) {
                executor.execute(Callables.threadRenaming(command, nameSupplier));
            }
        };
    }

    static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        return new WrappingExecutorService(service) { // from class: com.google.common.util.concurrent.MoreExecutors.3
            @Override // com.google.common.util.concurrent.WrappingExecutorService
            protected <T> Callable<T> wrapTask(Callable<T> callable) {
                return Callables.threadRenaming(callable, nameSupplier);
            }

            @Override // com.google.common.util.concurrent.WrappingExecutorService
            protected Runnable wrapTask(Runnable command) {
                return Callables.threadRenaming(command, nameSupplier);
            }
        };
    }

    public static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        return new WrappingScheduledExecutorService(service) { // from class: com.google.common.util.concurrent.MoreExecutors.4
            @Override // com.google.common.util.concurrent.WrappingExecutorService
            protected <T> Callable<T> wrapTask(Callable<T> callable) {
                return Callables.threadRenaming(callable, nameSupplier);
            }

            @Override // com.google.common.util.concurrent.WrappingExecutorService
            protected Runnable wrapTask(Runnable command) {
                return Callables.threadRenaming(command, nameSupplier);
            }
        };
    }

    public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
        long halfTimeoutNanos = unit.toNanos(timeout) / 2;
        service.shutdown();
        try {
            if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
                service.shutdownNow();
                service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            service.shutdownNow();
        }
        return service.isTerminated();
    }

    public static Executor rejectionPropagatingExecutor(final Executor delegate, final AbstractFuture<?> future) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(future);
        if (delegate == directExecutor()) {
            return delegate;
        }
        return new Executor() { // from class: com.google.common.util.concurrent.MoreExecutors.5
            boolean thrownFromDelegate = true;

            @Override // java.util.concurrent.Executor
            public void execute(final Runnable command) {
                try {
                    delegate.execute(new Runnable() { // from class: com.google.common.util.concurrent.MoreExecutors.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass5.this.thrownFromDelegate = false;
                            command.run();
                        }

                        @Override // java.lang.Object
                        public String toString() {
                            return command.toString();
                        }
                    });
                } catch (RejectedExecutionException e) {
                    if (this.thrownFromDelegate) {
                        future.setException(e);
                    }
                }
            }
        };
    }
}
