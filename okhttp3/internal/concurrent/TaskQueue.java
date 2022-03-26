package okhttp3.internal.concurrent;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
/* compiled from: TaskQueue.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u00013B\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010!\u001a\u00020\"J\r\u0010#\u001a\u00020\u000eH\u0000¢\u0006\u0002\b$J5\u0010%\u001a\u00020\"2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010&\u001a\u00020'2\b\b\u0002\u0010(\u001a\u00020\u000e2\u000e\b\u0004\u0010)\u001a\b\u0012\u0004\u0012\u00020\"0*H\u0086\bJ\u0006\u0010+\u001a\u00020,J+\u0010-\u001a\u00020\"2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010&\u001a\u00020'2\u000e\b\u0004\u0010)\u001a\b\u0012\u0004\u0012\u00020'0*H\u0086\bJ\u0018\u0010-\u001a\u00020\"2\u0006\u0010.\u001a\u00020\b2\b\b\u0002\u0010&\u001a\u00020'J%\u0010/\u001a\u00020\u000e2\u0006\u0010.\u001a\u00020\b2\u0006\u0010&\u001a\u00020'2\u0006\u00100\u001a\u00020\u000eH\u0000¢\u0006\u0002\b1J\u0006\u0010\u001c\u001a\u00020\"J\b\u00102\u001a\u00020\u0005H\u0016R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u0014X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001a8F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0016R\u001a\u0010\u001c\u001a\u00020\u000eX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0010\"\u0004\b\u001e\u0010\u0012R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 ¨\u00064"}, d2 = {"Lokhttp3/internal/concurrent/TaskQueue;", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", AppMeasurementSdk.ConditionalUserProperty.NAME, "", "(Lokhttp3/internal/concurrent/TaskRunner;Ljava/lang/String;)V", "activeTask", "Lokhttp3/internal/concurrent/Task;", "getActiveTask$okhttp", "()Lokhttp3/internal/concurrent/Task;", "setActiveTask$okhttp", "(Lokhttp3/internal/concurrent/Task;)V", "cancelActiveTask", "", "getCancelActiveTask$okhttp", "()Z", "setCancelActiveTask$okhttp", "(Z)V", "futureTasks", "", "getFutureTasks$okhttp", "()Ljava/util/List;", "getName$okhttp", "()Ljava/lang/String;", "scheduledTasks", "", "getScheduledTasks", "shutdown", "getShutdown$okhttp", "setShutdown$okhttp", "getTaskRunner$okhttp", "()Lokhttp3/internal/concurrent/TaskRunner;", "cancelAll", "", "cancelAllAndDecide", "cancelAllAndDecide$okhttp", "execute", "delayNanos", "", "cancelable", "block", "Lkotlin/Function0;", "idleLatch", "Ljava/util/concurrent/CountDownLatch;", "schedule", "task", "scheduleAndDecide", "recurrence", "scheduleAndDecide$okhttp", "toString", "AwaitIdleTask", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class TaskQueue {
    private Task activeTask;
    private boolean cancelActiveTask;
    private final List<Task> futureTasks = new ArrayList();
    private final String name;
    private boolean shutdown;
    private final TaskRunner taskRunner;

    public TaskQueue(TaskRunner taskRunner, String name) {
        Intrinsics.checkParameterIsNotNull(taskRunner, "taskRunner");
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        this.taskRunner = taskRunner;
        this.name = name;
    }

    public final TaskRunner getTaskRunner$okhttp() {
        return this.taskRunner;
    }

    public final String getName$okhttp() {
        return this.name;
    }

    public final boolean getShutdown$okhttp() {
        return this.shutdown;
    }

    public final void setShutdown$okhttp(boolean z) {
        this.shutdown = z;
    }

    public final Task getActiveTask$okhttp() {
        return this.activeTask;
    }

    public final void setActiveTask$okhttp(Task task) {
        this.activeTask = task;
    }

    public final List<Task> getFutureTasks$okhttp() {
        return this.futureTasks;
    }

    public final boolean getCancelActiveTask$okhttp() {
        return this.cancelActiveTask;
    }

    public final void setCancelActiveTask$okhttp(boolean z) {
        this.cancelActiveTask = z;
    }

    public final List<Task> getScheduledTasks() {
        List<Task> list;
        synchronized (this.taskRunner) {
            list = CollectionsKt.toList(this.futureTasks);
        }
        return list;
    }

    public static /* synthetic */ void schedule$default(TaskQueue taskQueue, Task task, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        taskQueue.schedule(task, j);
    }

    public final void schedule(Task task, long delayNanos) {
        Intrinsics.checkParameterIsNotNull(task, "task");
        synchronized (this.taskRunner) {
            if (!this.shutdown) {
                if (scheduleAndDecide$okhttp(task, delayNanos, false)) {
                    this.taskRunner.kickCoordinator$okhttp(this);
                }
                Unit unit = Unit.INSTANCE;
            } else if (task.getCancelable()) {
                if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                    TaskLoggerKt.log(task, this, "schedule canceled (queue is shutdown)");
                }
            } else {
                if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                    TaskLoggerKt.log(task, this, "schedule failed (queue is shutdown)");
                }
                throw new RejectedExecutionException();
            }
        }
    }

    public static /* synthetic */ void schedule$default(TaskQueue taskQueue, String name, long delayNanos, Function0 block, int i, Object obj) {
        if ((i & 2) != 0) {
            delayNanos = 0;
        }
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        Intrinsics.checkParameterIsNotNull(block, "block");
        taskQueue.schedule(new TaskQueue$schedule$2(block, name, name), delayNanos);
    }

    public final void schedule(String name, long delayNanos, Function0<Long> function0) {
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        Intrinsics.checkParameterIsNotNull(function0, "block");
        schedule(new TaskQueue$schedule$2(function0, name, name), delayNanos);
    }

    public static /* synthetic */ void execute$default(TaskQueue taskQueue, String name, long delayNanos, boolean cancelable, Function0 block, int i, Object obj) {
        if ((i & 2) != 0) {
            delayNanos = 0;
        }
        if ((i & 4) != 0) {
            cancelable = true;
        }
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        Intrinsics.checkParameterIsNotNull(block, "block");
        taskQueue.schedule(new TaskQueue$execute$1(block, name, cancelable, name, cancelable), delayNanos);
    }

    public final void execute(String name, long delayNanos, boolean cancelable, Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        Intrinsics.checkParameterIsNotNull(function0, "block");
        schedule(new TaskQueue$execute$1(function0, name, cancelable, name, cancelable), delayNanos);
    }

    public final CountDownLatch idleLatch() {
        synchronized (this.taskRunner) {
            if (this.activeTask != null || !this.futureTasks.isEmpty()) {
                Task existingTask = this.activeTask;
                if (existingTask instanceof AwaitIdleTask) {
                    return ((AwaitIdleTask) existingTask).getLatch();
                }
                for (Task futureTask : this.futureTasks) {
                    if (futureTask instanceof AwaitIdleTask) {
                        return ((AwaitIdleTask) futureTask).getLatch();
                    }
                }
                AwaitIdleTask newTask = new AwaitIdleTask();
                if (scheduleAndDecide$okhttp(newTask, 0, false)) {
                    this.taskRunner.kickCoordinator$okhttp(this);
                }
                return newTask.getLatch();
            }
            return new CountDownLatch(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\t"}, d2 = {"Lokhttp3/internal/concurrent/TaskQueue$AwaitIdleTask;", "Lokhttp3/internal/concurrent/Task;", "()V", "latch", "Ljava/util/concurrent/CountDownLatch;", "getLatch", "()Ljava/util/concurrent/CountDownLatch;", "runOnce", "", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class AwaitIdleTask extends Task {
        private final CountDownLatch latch = new CountDownLatch(1);

        public AwaitIdleTask() {
            super(Util.okHttpName + " awaitIdle", false);
        }

        public final CountDownLatch getLatch() {
            return this.latch;
        }

        @Override // okhttp3.internal.concurrent.Task
        public long runOnce() {
            this.latch.countDown();
            return -1;
        }
    }

    /* JADX INFO: Multiple debug info for r9v1 java.util.List<okhttp3.internal.concurrent.Task>: [D('queue$iv' okhttp3.internal.concurrent.TaskQueue), D('$this$indexOfFirst$iv' java.util.List)] */
    /* JADX INFO: Multiple debug info for r9v2 int: [D('$this$indexOfFirst$iv' java.util.List), D('insertAt' int)] */
    public final boolean scheduleAndDecide$okhttp(Task task, long delayNanos, boolean recurrence) {
        String str;
        Intrinsics.checkParameterIsNotNull(task, "task");
        task.initQueue$okhttp(this);
        long now = this.taskRunner.getBackend().nanoTime();
        long executeNanoTime = now + delayNanos;
        int existingIndex = this.futureTasks.indexOf(task);
        if (existingIndex != -1) {
            if (task.getNextExecuteNanoTime$okhttp() <= executeNanoTime) {
                if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                    TaskLoggerKt.log(task, this, "already scheduled");
                }
                return false;
            }
            this.futureTasks.remove(existingIndex);
        }
        task.setNextExecuteNanoTime$okhttp(executeNanoTime);
        if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
            if (recurrence) {
                str = "run again after " + TaskLoggerKt.formatDuration(executeNanoTime - now);
            } else {
                str = "scheduled after " + TaskLoggerKt.formatDuration(executeNanoTime - now);
            }
            TaskLoggerKt.log(task, this, str);
        }
        int index$iv = 0;
        Iterator<Task> it = this.futureTasks.iterator();
        while (true) {
            if (!it.hasNext()) {
                index$iv = -1;
                break;
            }
            if ((((Task) it.next()).getNextExecuteNanoTime$okhttp() - now > delayNanos ? 1 : null) != null) {
                break;
            }
            index$iv++;
        }
        int insertAt = index$iv;
        if (insertAt == -1) {
            insertAt = this.futureTasks.size();
        }
        this.futureTasks.add(insertAt, task);
        if (insertAt == 0) {
            return true;
        }
        return false;
    }

    public final void cancelAll() {
        if (!Util.assertionsEnabled || !Thread.holdsLock(this)) {
            synchronized (this.taskRunner) {
                if (cancelAllAndDecide$okhttp()) {
                    this.taskRunner.kickCoordinator$okhttp(this);
                }
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST NOT hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    public final void shutdown() {
        if (!Util.assertionsEnabled || !Thread.holdsLock(this)) {
            synchronized (this.taskRunner) {
                this.shutdown = true;
                if (cancelAllAndDecide$okhttp()) {
                    this.taskRunner.kickCoordinator$okhttp(this);
                }
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        sb.append(currentThread.getName());
        sb.append(" MUST NOT hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    public final boolean cancelAllAndDecide$okhttp() {
        Task task = this.activeTask;
        if (task != null) {
            if (task == null) {
                Intrinsics.throwNpe();
            }
            if (task.getCancelable()) {
                this.cancelActiveTask = true;
            }
        }
        boolean tasksCanceled = false;
        for (int i = this.futureTasks.size() - 1; i >= 0; i--) {
            if (this.futureTasks.get(i).getCancelable()) {
                Task task$iv = this.futureTasks.get(i);
                if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                    TaskLoggerKt.log(task$iv, this, "canceled");
                }
                tasksCanceled = true;
                this.futureTasks.remove(i);
            }
        }
        return tasksCanceled;
    }

    public String toString() {
        return this.name;
    }
}
