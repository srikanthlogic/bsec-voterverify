package okhttp3.internal.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
/* compiled from: TaskRunner.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u0000 #2\u00020\u0001:\u0003\"#$B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\t0\u0014J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\rH\u0002J\b\u0010\u001a\u001a\u0004\u0018\u00010\u0018J\u0010\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\b\u0010\u001c\u001a\u00020\u0016H\u0002J\u0015\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\tH\u0000¢\u0006\u0002\b\u001fJ\u0006\u0010 \u001a\u00020\tJ\u0010\u0010!\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lokhttp3/internal/concurrent/TaskRunner;", "", "backend", "Lokhttp3/internal/concurrent/TaskRunner$Backend;", "(Lokhttp3/internal/concurrent/TaskRunner$Backend;)V", "getBackend", "()Lokhttp3/internal/concurrent/TaskRunner$Backend;", "busyQueues", "", "Lokhttp3/internal/concurrent/TaskQueue;", "coordinatorWaiting", "", "coordinatorWakeUpAt", "", "nextQueueName", "", "readyQueues", "runnable", "Ljava/lang/Runnable;", "activeQueues", "", "afterRun", "", "task", "Lokhttp3/internal/concurrent/Task;", "delayNanos", "awaitTaskToRun", "beforeRun", "cancelAll", "kickCoordinator", "taskQueue", "kickCoordinator$okhttp", "newQueue", "runTask", "Backend", "Companion", "RealBackend", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class TaskRunner {
    public static final Companion Companion = new Companion(null);
    public static final TaskRunner INSTANCE = new TaskRunner(new RealBackend(Util.threadFactory(Util.okHttpName + " TaskRunner", true)));
    private static final Logger logger;
    private final Backend backend;
    private boolean coordinatorWaiting;
    private long coordinatorWakeUpAt;
    private int nextQueueName = 10000;
    private final List<TaskQueue> busyQueues = new ArrayList();
    private final List<TaskQueue> readyQueues = new ArrayList();
    private final Runnable runnable = new Runnable() { // from class: okhttp3.internal.concurrent.TaskRunner$runnable$1
        @Override // java.lang.Runnable
        public void run() {
            Task task;
            while (true) {
                synchronized (TaskRunner.this) {
                    task = TaskRunner.this.awaitTaskToRun();
                }
                if (task != null) {
                    TaskQueue queue$iv = task.getQueue$okhttp();
                    if (queue$iv == null) {
                        Intrinsics.throwNpe();
                    }
                    long startNs$iv = -1;
                    boolean loggingEnabled$iv = TaskRunner.Companion.getLogger().isLoggable(Level.FINE);
                    if (loggingEnabled$iv) {
                        startNs$iv = queue$iv.getTaskRunner$okhttp().getBackend().nanoTime();
                        TaskLoggerKt.log(task, queue$iv, "starting");
                    }
                    TaskRunner.this.runTask(task);
                    try {
                        Unit unit = Unit.INSTANCE;
                        if (loggingEnabled$iv) {
                            TaskLoggerKt.log(task, queue$iv, "finished run in " + TaskLoggerKt.formatDuration(queue$iv.getTaskRunner$okhttp().getBackend().nanoTime() - startNs$iv));
                        }
                    } catch (Throwable th) {
                        if (loggingEnabled$iv) {
                            long elapsedNs$iv = queue$iv.getTaskRunner$okhttp().getBackend().nanoTime() - startNs$iv;
                            if (0 != 0) {
                                TaskLoggerKt.log(task, queue$iv, "finished run in " + TaskLoggerKt.formatDuration(elapsedNs$iv));
                            } else {
                                TaskLoggerKt.log(task, queue$iv, "failed a run in " + TaskLoggerKt.formatDuration(elapsedNs$iv));
                            }
                        }
                        throw th;
                    }
                } else {
                    return;
                }
            }
        }
    };

    /* compiled from: TaskRunner.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\tH&¨\u0006\u000e"}, d2 = {"Lokhttp3/internal/concurrent/TaskRunner$Backend;", "", "beforeTask", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "coordinatorNotify", "coordinatorWait", "nanos", "", "execute", "runnable", "Ljava/lang/Runnable;", "nanoTime", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public interface Backend {
        void beforeTask(TaskRunner taskRunner);

        void coordinatorNotify(TaskRunner taskRunner);

        void coordinatorWait(TaskRunner taskRunner, long j);

        void execute(Runnable runnable);

        long nanoTime();
    }

    public TaskRunner(Backend backend) {
        Intrinsics.checkParameterIsNotNull(backend, "backend");
        this.backend = backend;
    }

    public final Backend getBackend() {
        return this.backend;
    }

    public final void kickCoordinator$okhttp(TaskQueue taskQueue) {
        Intrinsics.checkParameterIsNotNull(taskQueue, "taskQueue");
        if (!Util.assertionsEnabled || Thread.holdsLock(this)) {
            if (taskQueue.getActiveTask$okhttp() == null) {
                if (!taskQueue.getFutureTasks$okhttp().isEmpty()) {
                    Util.addIfAbsent(this.readyQueues, taskQueue);
                } else {
                    this.readyQueues.remove(taskQueue);
                }
            }
            if (this.coordinatorWaiting) {
                this.backend.coordinatorNotify(this);
            } else {
                this.backend.execute(this.runnable);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
            sb.append(currentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
    }

    private final void beforeRun(Task task) {
        if (!Util.assertionsEnabled || Thread.holdsLock(this)) {
            task.setNextExecuteNanoTime$okhttp(-1);
            TaskQueue queue = task.getQueue$okhttp();
            if (queue == null) {
                Intrinsics.throwNpe();
            }
            queue.getFutureTasks$okhttp().remove(task);
            this.readyQueues.remove(queue);
            queue.setActiveTask$okhttp(task);
            this.busyQueues.add(queue);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    public final void runTask(Task task) {
        if (!Util.assertionsEnabled || !Thread.holdsLock(this)) {
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
            String oldName = currentThread.getName();
            currentThread.setName(task.getName());
            try {
                long delayNanos = task.runOnce();
                synchronized (this) {
                    try {
                        afterRun(task, delayNanos);
                        Unit unit = Unit.INSTANCE;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                currentThread.setName(oldName);
            } catch (Throwable th2) {
                synchronized (this) {
                    try {
                        afterRun(task, -1);
                        Unit unit2 = Unit.INSTANCE;
                        currentThread.setName(oldName);
                        throw th2;
                    } catch (Throwable th3) {
                        throw th3;
                    }
                }
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread currentThread2 = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread2, "Thread.currentThread()");
            sb.append(currentThread2.getName());
            sb.append(" MUST NOT hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
    }

    private final void afterRun(Task task, long delayNanos) {
        if (!Util.assertionsEnabled || Thread.holdsLock(this)) {
            TaskQueue queue = task.getQueue$okhttp();
            if (queue == null) {
                Intrinsics.throwNpe();
            }
            if (queue.getActiveTask$okhttp() == task) {
                boolean cancelActiveTask = queue.getCancelActiveTask$okhttp();
                queue.setCancelActiveTask$okhttp(false);
                queue.setActiveTask$okhttp(null);
                this.busyQueues.remove(queue);
                if (delayNanos != -1 && !cancelActiveTask && !queue.getShutdown$okhttp()) {
                    queue.scheduleAndDecide$okhttp(task, delayNanos, true);
                }
                if (!queue.getFutureTasks$okhttp().isEmpty()) {
                    this.readyQueues.add(queue);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    public final Task awaitTaskToRun() {
        if (!Util.assertionsEnabled || Thread.holdsLock(this)) {
            while (!this.readyQueues.isEmpty()) {
                long now = this.backend.nanoTime();
                long minDelayNanos = Long.MAX_VALUE;
                Task readyTask = null;
                boolean multipleReadyTasks = false;
                Iterator<TaskQueue> it = this.readyQueues.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Task candidate = it.next().getFutureTasks$okhttp().get(0);
                    long candidateDelay = Math.max(0L, candidate.getNextExecuteNanoTime$okhttp() - now);
                    if (candidateDelay > 0) {
                        minDelayNanos = Math.min(candidateDelay, minDelayNanos);
                    } else if (readyTask != null) {
                        multipleReadyTasks = true;
                        break;
                    } else {
                        readyTask = candidate;
                    }
                }
                if (readyTask != null) {
                    beforeRun(readyTask);
                    if (multipleReadyTasks || (!this.coordinatorWaiting && (!this.readyQueues.isEmpty()))) {
                        this.backend.execute(this.runnable);
                    }
                    return readyTask;
                } else if (this.coordinatorWaiting) {
                    if (minDelayNanos < this.coordinatorWakeUpAt - now) {
                        this.backend.coordinatorNotify(this);
                    }
                    return null;
                } else {
                    this.coordinatorWaiting = true;
                    this.coordinatorWakeUpAt = now + minDelayNanos;
                    try {
                        try {
                            this.backend.coordinatorWait(this, minDelayNanos);
                        } catch (InterruptedException e) {
                            cancelAll();
                        }
                    } finally {
                        this.coordinatorWaiting = false;
                    }
                }
            }
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    public final TaskQueue newQueue() {
        int name;
        synchronized (this) {
            name = this.nextQueueName;
            this.nextQueueName = name + 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('Q');
        sb.append(name);
        return new TaskQueue(this, sb.toString());
    }

    public final List<TaskQueue> activeQueues() {
        List<TaskQueue> plus;
        synchronized (this) {
            plus = CollectionsKt.plus((Collection) this.busyQueues, (Iterable) this.readyQueues);
        }
        return plus;
    }

    private final void cancelAll() {
        for (int i = this.busyQueues.size() - 1; i >= 0; i--) {
            this.readyQueues.get(i).cancelAllAndDecide$okhttp();
        }
        for (int i2 = this.readyQueues.size() - 1; i2 >= 0; i2--) {
            TaskQueue queue = this.readyQueues.get(i2);
            queue.cancelAllAndDecide$okhttp();
            if (queue.getFutureTasks$okhttp().isEmpty()) {
                this.readyQueues.remove(i2);
            }
        }
    }

    /* compiled from: TaskRunner.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u000eH\u0016J\u0006\u0010\u0013\u001a\u00020\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lokhttp3/internal/concurrent/TaskRunner$RealBackend;", "Lokhttp3/internal/concurrent/TaskRunner$Backend;", "threadFactory", "Ljava/util/concurrent/ThreadFactory;", "(Ljava/util/concurrent/ThreadFactory;)V", "executor", "Ljava/util/concurrent/ThreadPoolExecutor;", "beforeTask", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "coordinatorNotify", "coordinatorWait", "nanos", "", "execute", "runnable", "Ljava/lang/Runnable;", "nanoTime", "shutdown", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class RealBackend implements Backend {
        private final ThreadPoolExecutor executor;

        public RealBackend(ThreadFactory threadFactory) {
            Intrinsics.checkParameterIsNotNull(threadFactory, "threadFactory");
            this.executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), threadFactory);
        }

        @Override // okhttp3.internal.concurrent.TaskRunner.Backend
        public void beforeTask(TaskRunner taskRunner) {
            Intrinsics.checkParameterIsNotNull(taskRunner, "taskRunner");
        }

        @Override // okhttp3.internal.concurrent.TaskRunner.Backend
        public long nanoTime() {
            return System.nanoTime();
        }

        @Override // okhttp3.internal.concurrent.TaskRunner.Backend
        public void coordinatorNotify(TaskRunner taskRunner) {
            Intrinsics.checkParameterIsNotNull(taskRunner, "taskRunner");
            taskRunner.notify();
        }

        @Override // okhttp3.internal.concurrent.TaskRunner.Backend
        public void coordinatorWait(TaskRunner taskRunner, long nanos) throws InterruptedException {
            Intrinsics.checkParameterIsNotNull(taskRunner, "taskRunner");
            long ms = nanos / 1000000;
            long ns = nanos - (1000000 * ms);
            if (ms > 0 || nanos > 0) {
                taskRunner.wait(ms, (int) ns);
            }
        }

        @Override // okhttp3.internal.concurrent.TaskRunner.Backend
        public void execute(Runnable runnable) {
            Intrinsics.checkParameterIsNotNull(runnable, "runnable");
            this.executor.execute(runnable);
        }

        public final void shutdown() {
            this.executor.shutdown();
        }
    }

    /* compiled from: TaskRunner.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lokhttp3/internal/concurrent/TaskRunner$Companion;", "", "()V", "INSTANCE", "Lokhttp3/internal/concurrent/TaskRunner;", "logger", "Ljava/util/logging/Logger;", "getLogger", "()Ljava/util/logging/Logger;", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final Logger getLogger() {
            return TaskRunner.logger;
        }
    }

    static {
        Logger logger2 = Logger.getLogger(TaskRunner.class.getName());
        Intrinsics.checkExpressionValueIsNotNull(logger2, "Logger.getLogger(TaskRunner::class.java.name)");
        logger = logger2;
    }
}
