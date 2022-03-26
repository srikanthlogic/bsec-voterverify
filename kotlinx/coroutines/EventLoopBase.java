package kotlinx.coroutines;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.Delay;
import kotlinx.coroutines.internal.LockFreeMPSCQueueCore;
import kotlinx.coroutines.internal.ThreadSafeHeap;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
/* compiled from: EventLoop.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\b \u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0003345B\u0005¢\u0006\u0002\u0010\u0004J\b\u0010\u0015\u001a\u00020\u0016H\u0004J\u0010\u0010\u0017\u001a\n\u0018\u00010\u0018j\u0004\u0018\u0001`\u0019H\u0002J\u001c\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001c2\n\u0010\u001d\u001a\u00060\u0018j\u0002`\u0019H\u0016J\u0014\u0010\u001e\u001a\u00020\f2\n\u0010\u001f\u001a\u00060\u0018j\u0002`\u0019H\u0002J\u0019\u0010 \u001a\u00020\u00162\n\u0010\u001f\u001a\u00060\u0018j\u0002`\u0019H\u0000¢\u0006\u0002\b!J\b\u0010\"\u001a\u00020\fH$J\b\u0010#\u001a\u00020\u0012H\u0016J\u0015\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u00020\bH\u0000¢\u0006\u0002\b&J\b\u0010'\u001a\u00020\u0016H\u0004J\b\u0010(\u001a\u00020\u0016H\u0004J\u0015\u0010)\u001a\u00020\u00162\u0006\u0010%\u001a\u00020\bH\u0000¢\u0006\u0002\b*J\u0010\u0010+\u001a\u00020,2\u0006\u0010%\u001a\u00020\bH\u0002J\u001e\u0010-\u001a\u00020\u00162\u0006\u0010.\u001a\u00020\u00122\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u001600H\u0016J\u0010\u00101\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\bH\u0002J\b\u00102\u001a\u00020\u0016H$R\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\fX¤\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\rR\u0014\u0010\u000e\u001a\u00020\f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\rR\u0014\u0010\u000f\u001a\u00020\f8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\rR\u0014\u0010\u0010\u001a\u00020\f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\rR\u0014\u0010\u0011\u001a\u00020\u00128BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014¨\u00066"}, d2 = {"Lkotlinx/coroutines/EventLoopBase;", "Lkotlinx/coroutines/CoroutineDispatcher;", "Lkotlinx/coroutines/Delay;", "Lkotlinx/coroutines/EventLoop;", "()V", "_delayed", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/internal/ThreadSafeHeap;", "Lkotlinx/coroutines/EventLoopBase$DelayedTask;", "_queue", "", "isCompleted", "", "()Z", "isDelayedEmpty", "isEmpty", "isQueueEmpty", "nextTime", "", "getNextTime", "()J", "closeQueue", "", "dequeue", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "enqueueImpl", "task", "execute", "execute$kotlinx_coroutines_core", "isCorrectThread", "processNextEvent", "removeDelayedImpl", "delayedTask", "removeDelayedImpl$kotlinx_coroutines_core", "rescheduleAllDelayed", "resetAll", "schedule", "schedule$kotlinx_coroutines_core", "scheduleImpl", "", "scheduleResumeAfterDelay", "timeMillis", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "shouldUnpark", "unpark", "DelayedResumeTask", "DelayedRunnableTask", "DelayedTask", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public abstract class EventLoopBase extends CoroutineDispatcher implements Delay, EventLoop {
    private static final AtomicReferenceFieldUpdater _queue$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopBase.class, Object.class, "_queue");
    private static final AtomicReferenceFieldUpdater _delayed$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopBase.class, Object.class, "_delayed");
    private volatile Object _queue = null;
    private volatile Object _delayed = null;

    protected abstract boolean isCompleted();

    protected abstract boolean isCorrectThread();

    protected abstract void unpark();

    @Override // kotlinx.coroutines.Delay
    public Object delay(long time, Continuation<? super Unit> continuation) {
        return Delay.DefaultImpls.delay(this, time, continuation);
    }

    @Override // kotlinx.coroutines.Delay
    public DisposableHandle invokeOnTimeout(long timeMillis, Runnable block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        return Delay.DefaultImpls.invokeOnTimeout(this, timeMillis, block);
    }

    protected final boolean isEmpty() {
        return isQueueEmpty() && isDelayedEmpty();
    }

    private final boolean isQueueEmpty() {
        Object queue = this._queue;
        if (queue == null) {
            return true;
        }
        if (queue instanceof LockFreeMPSCQueueCore) {
            return ((LockFreeMPSCQueueCore) queue).isEmpty();
        }
        if (queue == EventLoopKt.CLOSED_EMPTY) {
            return true;
        }
        return false;
    }

    private final boolean isDelayedEmpty() {
        ThreadSafeHeap delayed = (ThreadSafeHeap) this._delayed;
        return delayed == null || delayed.isEmpty();
    }

    private final long getNextTime() {
        DelayedTask nextDelayedTask;
        Object queue = this._queue;
        if (queue != null) {
            if (!(queue instanceof LockFreeMPSCQueueCore)) {
                return queue == EventLoopKt.CLOSED_EMPTY ? Long.MAX_VALUE : 0;
            }
            if (!((LockFreeMPSCQueueCore) queue).isEmpty()) {
                return 0;
            }
        }
        ThreadSafeHeap delayed = (ThreadSafeHeap) this._delayed;
        if (delayed == null || (nextDelayedTask = (DelayedTask) delayed.peek()) == null) {
            return Long.MAX_VALUE;
        }
        return RangesKt.coerceAtLeast(nextDelayedTask.nanoTime - TimeSourceKt.getTimeSource().nanoTime(), 0L);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext context, Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        execute$kotlinx_coroutines_core(block);
    }

    @Override // kotlinx.coroutines.Delay
    public void scheduleResumeAfterDelay(long timeMillis, CancellableContinuation<? super Unit> cancellableContinuation) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "continuation");
        schedule$kotlinx_coroutines_core(new DelayedResumeTask(this, timeMillis, cancellableContinuation));
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x005f  */
    @Override // kotlinx.coroutines.EventLoop
    /* Code decompiled incorrectly, please refer to instructions dump */
    public long processNextEvent() {
        Runnable dequeue;
        ThreadSafeHeapNode threadSafeHeapNode;
        boolean z;
        if (!isCorrectThread()) {
            return Long.MAX_VALUE;
        }
        ThreadSafeHeap delayed = (ThreadSafeHeap) this._delayed;
        if (delayed == null || delayed.isEmpty()) {
            dequeue = dequeue();
            if (dequeue != null) {
                dequeue.run();
            }
            return getNextTime();
        }
        long now = TimeSourceKt.getTimeSource().nanoTime();
        do {
            synchronized (delayed) {
                ThreadSafeHeapNode first$iv = delayed.firstImpl();
                threadSafeHeapNode = null;
                if (first$iv != null) {
                    DelayedTask it = (DelayedTask) first$iv;
                    if (it.timeToExecute(now)) {
                        z = enqueueImpl(it);
                    } else {
                        z = false;
                    }
                    if (z) {
                        threadSafeHeapNode = delayed.removeAtImpl(0);
                    }
                }
            }
        } while (((DelayedTask) threadSafeHeapNode) != null);
        dequeue = dequeue();
        if (dequeue != null) {
        }
        return getNextTime();
    }

    public final void execute$kotlinx_coroutines_core(Runnable task) {
        Intrinsics.checkParameterIsNotNull(task, "task");
        if (enqueueImpl(task)) {
            unpark();
        } else {
            DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(task);
        }
    }

    private final boolean enqueueImpl(Runnable task) {
        while (true) {
            Object queue = this._queue;
            if (isCompleted()) {
                return false;
            }
            if (queue == null) {
                if (_queue$FU.compareAndSet(this, null, task)) {
                    return true;
                }
            } else if (queue instanceof LockFreeMPSCQueueCore) {
                if (queue != null) {
                    int addLast = ((LockFreeMPSCQueueCore) queue).addLast(task);
                    if (addLast == 0) {
                        return true;
                    }
                    if (addLast == 1) {
                        _queue$FU.compareAndSet(this, queue, ((LockFreeMPSCQueueCore) queue).next());
                    } else if (addLast == 2) {
                        return false;
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.Queue<kotlinx.coroutines.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.internal.LockFreeMPSCQueueCore<kotlinx.coroutines.Runnable /* = java.lang.Runnable */> */");
                }
            } else if (queue == EventLoopKt.CLOSED_EMPTY) {
                return false;
            } else {
                LockFreeMPSCQueueCore newQueue = new LockFreeMPSCQueueCore(8);
                if (queue != null) {
                    newQueue.addLast((Runnable) queue);
                    newQueue.addLast(task);
                    if (_queue$FU.compareAndSet(this, queue, newQueue)) {
                        return true;
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.Runnable /* = java.lang.Runnable */");
                }
            }
        }
    }

    private final Runnable dequeue() {
        while (true) {
            Object queue = this._queue;
            if (queue == null) {
                return null;
            }
            if (queue instanceof LockFreeMPSCQueueCore) {
                if (queue != null) {
                    Object result = ((LockFreeMPSCQueueCore) queue).removeFirstOrNull();
                    if (result != LockFreeMPSCQueueCore.REMOVE_FROZEN) {
                        return (Runnable) result;
                    }
                    _queue$FU.compareAndSet(this, queue, ((LockFreeMPSCQueueCore) queue).next());
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.Queue<kotlinx.coroutines.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.internal.LockFreeMPSCQueueCore<kotlinx.coroutines.Runnable /* = java.lang.Runnable */> */");
                }
            } else if (queue == EventLoopKt.CLOSED_EMPTY) {
                return null;
            } else {
                if (_queue$FU.compareAndSet(this, queue, null)) {
                    if (queue != null) {
                        return (Runnable) queue;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.Runnable /* = java.lang.Runnable */");
                }
            }
        }
    }

    protected final void closeQueue() {
        boolean isCompleted = isCompleted();
        if (!_Assertions.ENABLED || isCompleted) {
            while (true) {
                Object queue = this._queue;
                if (queue == null) {
                    if (_queue$FU.compareAndSet(this, null, EventLoopKt.CLOSED_EMPTY)) {
                        return;
                    }
                } else if (queue instanceof LockFreeMPSCQueueCore) {
                    ((LockFreeMPSCQueueCore) queue).close();
                    return;
                } else if (queue != EventLoopKt.CLOSED_EMPTY) {
                    LockFreeMPSCQueueCore newQueue = new LockFreeMPSCQueueCore(8);
                    if (queue != null) {
                        newQueue.addLast((Runnable) queue);
                        if (_queue$FU.compareAndSet(this, queue, newQueue)) {
                            return;
                        }
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.Runnable /* = java.lang.Runnable */");
                    }
                } else {
                    return;
                }
            }
        } else {
            throw new AssertionError("Assertion failed");
        }
    }

    public final void schedule$kotlinx_coroutines_core(DelayedTask delayedTask) {
        Intrinsics.checkParameterIsNotNull(delayedTask, "delayedTask");
        int scheduleImpl = scheduleImpl(delayedTask);
        if (scheduleImpl != 0) {
            if (scheduleImpl == 1) {
                DefaultExecutor.INSTANCE.schedule$kotlinx_coroutines_core(delayedTask);
            } else if (scheduleImpl != 2) {
                throw new IllegalStateException("unexpected result".toString());
            }
        } else if (shouldUnpark(delayedTask)) {
            unpark();
        }
    }

    private final boolean shouldUnpark(DelayedTask task) {
        ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) this._delayed;
        return (threadSafeHeap != null ? (DelayedTask) threadSafeHeap.peek() : null) == task;
    }

    private final int scheduleImpl(DelayedTask delayedTask) {
        if (isCompleted()) {
            return 1;
        }
        ThreadSafeHeap delayed = (ThreadSafeHeap) this._delayed;
        if (delayed == null) {
            EventLoopBase $receiver = this;
            _delayed$FU.compareAndSet($receiver, null, new ThreadSafeHeap());
            Object obj = $receiver._delayed;
            if (obj == null) {
                Intrinsics.throwNpe();
            }
            delayed = (ThreadSafeHeap) obj;
        }
        return delayedTask.schedule(delayed, this);
    }

    public final void removeDelayedImpl$kotlinx_coroutines_core(DelayedTask delayedTask) {
        Intrinsics.checkParameterIsNotNull(delayedTask, "delayedTask");
        ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) this._delayed;
        if (threadSafeHeap != null) {
            threadSafeHeap.remove(delayedTask);
        }
    }

    protected final void resetAll() {
        this._queue = null;
        this._delayed = null;
    }

    protected final void rescheduleAllDelayed() {
        DelayedTask delayedTask;
        while (true) {
            ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) this._delayed;
            if (threadSafeHeap != null && (delayedTask = (DelayedTask) threadSafeHeap.removeFirstOrNull()) != null) {
                delayedTask.rescheduleOnShutdown();
            } else {
                return;
            }
        }
    }

    /* compiled from: EventLoop.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b \u0018\u00002\u00060\u0001j\u0002`\u00022\b\u0012\u0004\u0012\u00020\u00000\u00032\u00020\u00042\u00020\u0005B\r\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0011\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u0000H\u0096\u0002J\u0006\u0010\u001b\u001a\u00020\u001cJ\u0006\u0010\u001d\u001a\u00020\u001cJ\u001c\u0010\u001e\u001a\u00020\u00132\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00000\f2\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0007J\b\u0010%\u001a\u00020&H\u0016R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R0\u0010\r\u001a\b\u0012\u0002\b\u0003\u0018\u00010\f2\f\u0010\u000b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\f8V@VX\u0096\u000e¢\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0010\u0010\u0018\u001a\u00020\u00078\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lkotlinx/coroutines/EventLoopBase$DelayedTask;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "", "Lkotlinx/coroutines/DisposableHandle;", "Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "timeMillis", "", "(J)V", "_heap", "", "value", "Lkotlinx/coroutines/internal/ThreadSafeHeap;", "heap", "getHeap", "()Lkotlinx/coroutines/internal/ThreadSafeHeap;", "setHeap", "(Lkotlinx/coroutines/internal/ThreadSafeHeap;)V", FirebaseAnalytics.Param.INDEX, "", "getIndex", "()I", "setIndex", "(I)V", "nanoTime", "compareTo", "other", "dispose", "", "rescheduleOnShutdown", "schedule", "delayed", "eventLoop", "Lkotlinx/coroutines/EventLoopBase;", "timeToExecute", "", "now", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static abstract class DelayedTask implements Runnable, Comparable<DelayedTask>, DisposableHandle, ThreadSafeHeapNode {
        private Object _heap;
        private int index = -1;
        public final long nanoTime;

        public DelayedTask(long timeMillis) {
            this.nanoTime = TimeSourceKt.getTimeSource().nanoTime() + EventLoopKt.delayToNanos(timeMillis);
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public ThreadSafeHeap<?> getHeap() {
            Object obj = this._heap;
            if (!(obj instanceof ThreadSafeHeap)) {
                obj = null;
            }
            return (ThreadSafeHeap) obj;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public void setHeap(ThreadSafeHeap<?> threadSafeHeap) {
            if (this._heap != EventLoopKt.DISPOSED_TASK) {
                this._heap = threadSafeHeap;
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public int getIndex() {
            return this.index;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public void setIndex(int i) {
            this.index = i;
        }

        public int compareTo(DelayedTask other) {
            Intrinsics.checkParameterIsNotNull(other, "other");
            long dTime = this.nanoTime - other.nanoTime;
            if (dTime > 0) {
                return 1;
            }
            if (dTime < 0) {
                return -1;
            }
            return 0;
        }

        public final boolean timeToExecute(long now) {
            return now - this.nanoTime >= 0;
        }

        public final synchronized int schedule(ThreadSafeHeap<DelayedTask> threadSafeHeap, EventLoopBase eventLoop) {
            Throwable th;
            int $i$a$1$synchronized;
            Intrinsics.checkParameterIsNotNull(threadSafeHeap, "delayed");
            Intrinsics.checkParameterIsNotNull(eventLoop, "eventLoop");
            if (this._heap == EventLoopKt.DISPOSED_TASK) {
                return 2;
            }
            DelayedTask delayedTask = this;
            int i = 0;
            synchronized (threadSafeHeap) {
                try {
                    if (!eventLoop.isCompleted()) {
                        try {
                            threadSafeHeap.addImpl(delayedTask);
                            $i$a$1$synchronized = 1;
                        } catch (Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    } else {
                        $i$a$1$synchronized = 0;
                    }
                    if ($i$a$1$synchronized == 0) {
                        i = 1;
                    }
                    return i;
                } catch (Throwable th3) {
                    th = th3;
                }
            }
        }

        public final void rescheduleOnShutdown() {
            DefaultExecutor.INSTANCE.schedule$kotlinx_coroutines_core(this);
        }

        @Override // kotlinx.coroutines.DisposableHandle
        public final synchronized void dispose() {
            Object heap = this._heap;
            if (heap != EventLoopKt.DISPOSED_TASK) {
                ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) (!(heap instanceof ThreadSafeHeap) ? null : heap);
                if (threadSafeHeap != null) {
                    threadSafeHeap.remove(this);
                }
                this._heap = EventLoopKt.DISPOSED_TASK;
            }
        }

        @Override // java.lang.Object
        public String toString() {
            return "Delayed[nanos=" + this.nanoTime + ']';
        }
    }

    /* compiled from: EventLoop.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0082\u0004\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\u0006H\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/EventLoopBase$DelayedResumeTask;", "Lkotlinx/coroutines/EventLoopBase$DelayedTask;", "timeMillis", "", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlinx/coroutines/EventLoopBase;JLkotlinx/coroutines/CancellableContinuation;)V", "run", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    private final class DelayedResumeTask extends DelayedTask {
        private final CancellableContinuation<Unit> cont;
        final /* synthetic */ EventLoopBase this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public DelayedResumeTask(EventLoopBase $outer, long timeMillis, CancellableContinuation<? super Unit> cancellableContinuation) {
            super(timeMillis);
            Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
            this.this$0 = $outer;
            this.cont = cancellableContinuation;
            CancellableContinuationKt.disposeOnCancellation(this.cont, this);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.cont.resumeUndispatched(this.this$0, Unit.INSTANCE);
        }
    }

    /* compiled from: EventLoop.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016R\u0012\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/EventLoopBase$DelayedRunnableTask;", "Lkotlinx/coroutines/EventLoopBase$DelayedTask;", "time", "", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "(JLjava/lang/Runnable;)V", "run", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class DelayedRunnableTask extends DelayedTask {
        private final Runnable block;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DelayedRunnableTask(long time, Runnable block) {
            super(time);
            Intrinsics.checkParameterIsNotNull(block, "block");
            this.block = block;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.block.run();
        }

        @Override // kotlinx.coroutines.EventLoopBase.DelayedTask, java.lang.Object
        public String toString() {
            return super.toString() + this.block.toString();
        }
    }
}
