package kotlinx.coroutines.scheduling;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Dispatcher.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u001d\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\b\u0010\u001a\u001a\u00020\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u001bH\u0016J\u001c\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u001f2\n\u0010 \u001a\u00060\u0016j\u0002`\u0017H\u0016J\u001c\u0010\u001d\u001a\u00020\u001b2\n\u0010 \u001a\u00060\u0016j\u0002`\u00172\u0006\u0010!\u001a\u00020\"H\u0002J\u0014\u0010#\u001a\u00020\u001b2\n\u0010$\u001a\u00060\u0016j\u0002`\u0017H\u0016J\b\u0010%\u001a\u00020&H\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0018\u0010\u0014\u001a\f\u0012\b\u0012\u00060\u0016j\u0002`\u00170\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019¨\u0006'"}, d2 = {"Lkotlinx/coroutines/scheduling/LimitingDispatcher;", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "Lkotlinx/coroutines/scheduling/TaskContext;", "Ljava/util/concurrent/Executor;", "dispatcher", "Lkotlinx/coroutines/scheduling/ExperimentalCoroutineDispatcher;", "parallelism", "", "taskMode", "Lkotlinx/coroutines/scheduling/TaskMode;", "(Lkotlinx/coroutines/scheduling/ExperimentalCoroutineDispatcher;ILkotlinx/coroutines/scheduling/TaskMode;)V", "getDispatcher", "()Lkotlinx/coroutines/scheduling/ExperimentalCoroutineDispatcher;", "executor", "getExecutor", "()Ljava/util/concurrent/Executor;", "inFlightTasks", "Lkotlinx/atomicfu/AtomicInt;", "getParallelism", "()I", "queue", "Ljava/util/concurrent/ConcurrentLinkedQueue;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "getTaskMode", "()Lkotlinx/coroutines/scheduling/TaskMode;", "afterTask", "", "close", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "fair", "", "execute", "command", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class LimitingDispatcher extends ExecutorCoroutineDispatcher implements TaskContext, Executor {
    private static final AtomicIntegerFieldUpdater inFlightTasks$FU = AtomicIntegerFieldUpdater.newUpdater(LimitingDispatcher.class, "inFlightTasks");
    private final ExperimentalCoroutineDispatcher dispatcher;
    private final int parallelism;
    private final TaskMode taskMode;
    private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
    private volatile int inFlightTasks = 0;

    public LimitingDispatcher(ExperimentalCoroutineDispatcher dispatcher, int parallelism, TaskMode taskMode) {
        Intrinsics.checkParameterIsNotNull(dispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(taskMode, "taskMode");
        this.dispatcher = dispatcher;
        this.parallelism = parallelism;
        this.taskMode = taskMode;
    }

    public final ExperimentalCoroutineDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public final int getParallelism() {
        return this.parallelism;
    }

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public TaskMode getTaskMode() {
        return this.taskMode;
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher
    public Executor getExecutor() {
        return this;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        dispatch(command, false);
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new IllegalStateException("Close cannot be invoked on LimitingBlockingDispatcher".toString());
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext context, Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        dispatch(block, false);
    }

    private final void dispatch(Runnable block, boolean fair) {
        Runnable poll;
        Runnable taskToSchedule = block;
        while (inFlightTasks$FU.incrementAndGet(this) > this.parallelism) {
            this.queue.add(taskToSchedule);
            if (inFlightTasks$FU.decrementAndGet(this) < this.parallelism && (poll = this.queue.poll()) != null) {
                taskToSchedule = poll;
            } else {
                return;
            }
        }
        this.dispatcher.dispatchWithContext$kotlinx_coroutines_core(taskToSchedule, this, fair);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
    public String toString() {
        return super.toString() + "[dispatcher = " + this.dispatcher + ']';
    }

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public void afterTask() {
        Runnable next = this.queue.poll();
        if (next != null) {
            this.dispatcher.dispatchWithContext$kotlinx_coroutines_core(next, this, true);
            return;
        }
        inFlightTasks$FU.decrementAndGet(this);
        Runnable next2 = this.queue.poll();
        if (next2 != null) {
            dispatch(next2, true);
        }
    }
}
