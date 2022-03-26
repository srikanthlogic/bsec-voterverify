package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.ArrayQueue;
import kotlinx.coroutines.internal.ThreadLocalWithInitialValue;
/* compiled from: Dispatched.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bÁ\u0002\u0018\u00002\u00020\u0001:\u0001\u0017B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J?\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00012\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0086\bJ\u0012\u0010\u0012\u001a\u00020\b2\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0014J\u001f\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00052\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0086\bR \u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u00068\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lkotlinx/coroutines/UndispatchedEventLoop;", "", "()V", "threadLocalEventLoop", "Lkotlinx/coroutines/internal/ThreadLocalWithInitialValue;", "Lkotlinx/coroutines/UndispatchedEventLoop$EventLoop;", "Lkotlinx/coroutines/internal/CommonThreadLocal;", "execute", "", "continuation", "Lkotlinx/coroutines/DispatchedContinuation;", "contState", "mode", "", "doYield", "block", "Lkotlin/Function0;", "", "resumeUndispatched", "task", "Lkotlinx/coroutines/DispatchedTask;", "runEventLoop", "eventLoop", "EventLoop", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class UndispatchedEventLoop {
    public static final UndispatchedEventLoop INSTANCE = new UndispatchedEventLoop();
    public static final ThreadLocalWithInitialValue<EventLoop> threadLocalEventLoop = new ThreadLocalWithInitialValue<>(UndispatchedEventLoop$threadLocalEventLoop$1.INSTANCE);

    /* compiled from: Dispatched.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B#\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0012\b\u0002\u0010\u0004\u001a\f\u0012\b\u0012\u00060\u0006j\u0002`\u00070\u0005¢\u0006\u0002\u0010\bJ\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\n\u001a\f\u0012\b\u0012\u00060\u0006j\u0002`\u00070\u0005HÆ\u0003J'\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0012\b\u0002\u0010\u0004\u001a\f\u0012\b\u0012\u00060\u0006j\u0002`\u00070\u0005HÆ\u0001J\u0013\u0010\f\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0012\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\f\u0012\b\u0012\u00060\u0006j\u0002`\u00070\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/UndispatchedEventLoop$EventLoop;", "", "isActive", "", "queue", "Lkotlinx/coroutines/internal/ArrayQueue;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "(ZLkotlinx/coroutines/internal/ArrayQueue;)V", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class EventLoop {
        public boolean isActive;
        public final ArrayQueue<Runnable> queue;

        public EventLoop() {
            this(false, null, 3, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ EventLoop copy$default(EventLoop eventLoop, boolean z, ArrayQueue arrayQueue, int i, Object obj) {
            if ((i & 1) != 0) {
                z = eventLoop.isActive;
            }
            if ((i & 2) != 0) {
                arrayQueue = eventLoop.queue;
            }
            return eventLoop.copy(z, arrayQueue);
        }

        public final boolean component1() {
            return this.isActive;
        }

        public final ArrayQueue<Runnable> component2() {
            return this.queue;
        }

        public final EventLoop copy(boolean z, ArrayQueue<Runnable> arrayQueue) {
            Intrinsics.checkParameterIsNotNull(arrayQueue, "queue");
            return new EventLoop(z, arrayQueue);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof EventLoop) {
                    EventLoop eventLoop = (EventLoop) obj;
                    if (!(this.isActive == eventLoop.isActive) || !Intrinsics.areEqual(this.queue, eventLoop.queue)) {
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            boolean z = this.isActive;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            int i4 = i * 31;
            ArrayQueue<Runnable> arrayQueue = this.queue;
            return i4 + (arrayQueue != null ? arrayQueue.hashCode() : 0);
        }

        public String toString() {
            return "EventLoop(isActive=" + this.isActive + ", queue=" + this.queue + ")";
        }

        public EventLoop(boolean isActive, ArrayQueue<Runnable> arrayQueue) {
            Intrinsics.checkParameterIsNotNull(arrayQueue, "queue");
            this.isActive = isActive;
            this.queue = arrayQueue;
        }

        public /* synthetic */ EventLoop(boolean z, ArrayQueue arrayQueue, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? false : z, (i & 2) != 0 ? new ArrayQueue() : arrayQueue);
        }
    }

    private UndispatchedEventLoop() {
    }

    public static /* synthetic */ boolean execute$default(UndispatchedEventLoop undispatchedEventLoop, DispatchedContinuation continuation, Object contState, int mode, boolean doYield, Function0 block, int i, Object obj) {
        String str;
        if ((i & 8) != 0) {
            doYield = false;
        }
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        Intrinsics.checkParameterIsNotNull(block, "block");
        EventLoop eventLoop = threadLocalEventLoop.get();
        if (!eventLoop.isActive) {
            Intrinsics.checkExpressionValueIsNotNull(eventLoop, "eventLoop");
            try {
                eventLoop.isActive = true;
                block.invoke();
                while (true) {
                    Runnable nextEvent$iv = eventLoop.queue.removeFirstOrNull();
                    if (nextEvent$iv == null) {
                        return false;
                    }
                    nextEvent$iv.run();
                }
            } finally {
                try {
                    throw new DispatchException(str, e$iv);
                } finally {
                }
            }
        } else if (doYield && eventLoop.queue.isEmpty()) {
            return false;
        } else {
            continuation._state = contState;
            continuation.setResumeMode(mode);
            eventLoop.queue.addLast(continuation);
            return true;
        }
    }

    public final boolean execute(DispatchedContinuation<?> dispatchedContinuation, Object contState, int mode, boolean doYield, Function0<Unit> function0) {
        String str;
        Intrinsics.checkParameterIsNotNull(dispatchedContinuation, "continuation");
        Intrinsics.checkParameterIsNotNull(function0, "block");
        EventLoop eventLoop = threadLocalEventLoop.get();
        if (!eventLoop.isActive) {
            Intrinsics.checkExpressionValueIsNotNull(eventLoop, "eventLoop");
            try {
                eventLoop.isActive = true;
                function0.invoke();
                while (true) {
                    Runnable nextEvent$iv = eventLoop.queue.removeFirstOrNull();
                    if (nextEvent$iv == null) {
                        return false;
                    }
                    nextEvent$iv.run();
                }
            } finally {
                try {
                    throw new DispatchException(str, e$iv);
                } finally {
                }
            }
        } else if (doYield && eventLoop.queue.isEmpty()) {
            return false;
        } else {
            dispatchedContinuation._state = contState;
            dispatchedContinuation.setResumeMode(mode);
            eventLoop.queue.addLast(dispatchedContinuation);
            return true;
        }
    }

    public final boolean resumeUndispatched(DispatchedTask<?> dispatchedTask) {
        String str;
        Intrinsics.checkParameterIsNotNull(dispatchedTask, "task");
        EventLoop eventLoop = threadLocalEventLoop.get();
        if (eventLoop.isActive) {
            eventLoop.queue.addLast(dispatchedTask);
            return true;
        }
        Intrinsics.checkExpressionValueIsNotNull(eventLoop, "eventLoop");
        try {
            eventLoop.isActive = true;
            DispatchedKt.resume(dispatchedTask, dispatchedTask.getDelegate(), 3);
            while (true) {
                Runnable nextEvent$iv = eventLoop.queue.removeFirstOrNull();
                if (nextEvent$iv == null) {
                    return false;
                }
                nextEvent$iv.run();
            }
        } finally {
            try {
                throw new DispatchException(str, e$iv);
            } finally {
            }
        }
    }

    public final void runEventLoop(EventLoop eventLoop, Function0<Unit> function0) {
        String str;
        Intrinsics.checkParameterIsNotNull(eventLoop, "eventLoop");
        Intrinsics.checkParameterIsNotNull(function0, "block");
        try {
            eventLoop.isActive = true;
            function0.invoke();
            while (true) {
                Runnable nextEvent = eventLoop.queue.removeFirstOrNull();
                if (nextEvent != null) {
                    nextEvent.run();
                } else {
                    return;
                }
            }
        } finally {
            try {
                throw new DispatchException(str, e);
            } finally {
            }
        }
    }
}
