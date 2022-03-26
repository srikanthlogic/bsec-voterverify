package kotlinx.coroutines.scheduling;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.TimeSourceKt;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: CoroutineScheduler.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001c\b\u0000\u0018\u0000 E2\u00020\u00012\u00020\u0002:\u0003EFGB)\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0011\u0010\r\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u0007H\u0082\bJ\b\u0010#\u001a\u00020$H\u0016J\b\u0010%\u001a\u00020\u0004H\u0002J!\u0010&\u001a\u00020'2\n\u0010(\u001a\u00060)j\u0002`*2\u0006\u0010+\u001a\u00020,H\u0000¢\u0006\u0002\b-J\u0011\u0010\u0014\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u0007H\u0082\bJ\t\u0010.\u001a\u00020$H\u0082\bJ\t\u0010/\u001a\u00020\u0004H\u0082\bJ&\u00100\u001a\u00020$2\n\u0010(\u001a\u00060)j\u0002`*2\b\b\u0002\u0010+\u001a\u00020,2\b\b\u0002\u00101\u001a\u00020\u0019J\u0014\u00102\u001a\u00020$2\n\u00103\u001a\u00060)j\u0002`*H\u0016J\t\u00104\u001a\u00020$H\u0082\bJ\t\u00105\u001a\u00020\u0004H\u0082\bJ\u0014\u00106\u001a\u00020\u00042\n\u00107\u001a\u00060 R\u00020\u0000H\u0002J\u000e\u00108\u001a\b\u0018\u00010 R\u00020\u0000H\u0002J\u0014\u00109\u001a\u00020$2\n\u00107\u001a\u00060 R\u00020\u0000H\u0002J$\u0010:\u001a\u00020$2\n\u00107\u001a\u00060 R\u00020\u00002\u0006\u0010;\u001a\u00020\u00042\u0006\u0010<\u001a\u00020\u0004H\u0002J\b\u0010=\u001a\u00020$H\u0002J\u0010\u0010>\u001a\u00020$2\u0006\u0010?\u001a\u00020'H\u0002J\u000e\u0010@\u001a\u00020$2\u0006\u0010A\u001a\u00020\u0007J\u0018\u0010B\u001a\u00020\u00042\u0006\u0010?\u001a\u00020'2\u0006\u00101\u001a\u00020\u0019H\u0002J\b\u0010C\u001a\u00020\tH\u0016J\b\u0010D\u001a\u00020\u0019H\u0002R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0015\u0010\r\u001a\u00020\u00048Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u0015\u0010\u0014\u001a\u00020\u00048Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u000fR\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\u00198BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u001aR\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0018\u00010 R\u00020\u00000\u001fX\u0082\u0004¢\u0006\u0004\n\u0002\u0010!¨\u0006H"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "Ljava/util/concurrent/Executor;", "Ljava/io/Closeable;", "corePoolSize", "", "maxPoolSize", "idleWorkerKeepAliveNs", "", "schedulerName", "", "(IIJLjava/lang/String;)V", "_isTerminated", "Lkotlinx/atomicfu/AtomicInt;", "blockingWorkers", "getBlockingWorkers", "()I", "controlState", "Lkotlinx/atomicfu/AtomicLong;", "cpuPermits", "Ljava/util/concurrent/Semaphore;", "createdWorkers", "getCreatedWorkers", "globalQueue", "Lkotlinx/coroutines/scheduling/GlobalQueue;", "isTerminated", "", "()Z", "parkedWorkersStack", "random", "Ljava/util/Random;", "workers", "", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "[Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "state", "close", "", "createNewWorker", "createTask", "Lkotlinx/coroutines/scheduling/Task;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "taskContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "createTask$kotlinx_coroutines_core", "decrementBlockingWorkers", "decrementCreatedWorkers", "dispatch", "fair", "execute", "command", "incrementBlockingWorkers", "incrementCreatedWorkers", "parkedWorkersStackNextIndex", "worker", "parkedWorkersStackPop", "parkedWorkersStackPush", "parkedWorkersStackTopUpdate", "oldIndex", "newIndex", "requestCpuWorker", "runSafely", "task", "shutdown", "timeout", "submitToLocalQueue", "toString", "tryUnpark", "Companion", "Worker", "WorkerState", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class CoroutineScheduler implements Executor, Closeable {
    private static final int ADDED = -1;
    private static final int ADDED_REQUIRES_HELP = 0;
    private static final int ALLOWED = 0;
    private static final long BLOCKING_MASK = 4398044413952L;
    private static final int BLOCKING_SHIFT = 21;
    private static final long CREATED_MASK = 2097151;
    private static final int FORBIDDEN = -1;
    private static final int MAX_SPINS = 1000;
    public static final int MAX_SUPPORTED_POOL_SIZE = 2097150;
    private static final int MAX_YIELDS = 1500;
    public static final int MIN_SUPPORTED_POOL_SIZE = 1;
    private static final int NOT_ADDED = 1;
    private static final long PARKED_INDEX_MASK = 2097151;
    private static final long PARKED_VERSION_INC = 2097152;
    private static final long PARKED_VERSION_MASK = -2097152;
    private static final int TERMINATED = 1;
    private volatile int _isTerminated;
    volatile long controlState;
    private final int corePoolSize;
    private final Semaphore cpuPermits;
    private final GlobalQueue globalQueue;
    private final long idleWorkerKeepAliveNs;
    private final int maxPoolSize;
    private volatile long parkedWorkersStack;
    private final Random random;
    private final String schedulerName;
    private final Worker[] workers;
    public static final Companion Companion = new Companion(null);
    private static final int MAX_PARK_TIME_NS = (int) TimeUnit.SECONDS.toNanos(1);
    private static final int MIN_PARK_TIME_NS = (int) RangesKt.coerceAtMost(RangesKt.coerceAtLeast(TasksKt.WORK_STEALING_TIME_RESOLUTION_NS / ((long) 4), 10L), (long) MAX_PARK_TIME_NS);
    private static final Symbol NOT_IN_STACK = new Symbol("NOT_IN_STACK");
    private static final AtomicLongFieldUpdater parkedWorkersStack$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "parkedWorkersStack");
    static final AtomicLongFieldUpdater controlState$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "controlState");
    private static final AtomicIntegerFieldUpdater _isTerminated$FU = AtomicIntegerFieldUpdater.newUpdater(CoroutineScheduler.class, "_isTerminated");

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = new int[WorkerState.values().length];

        static {
            $EnumSwitchMapping$0[WorkerState.PARKING.ordinal()] = 1;
            $EnumSwitchMapping$0[WorkerState.BLOCKING.ordinal()] = 2;
            $EnumSwitchMapping$0[WorkerState.CPU_ACQUIRED.ordinal()] = 3;
            $EnumSwitchMapping$0[WorkerState.RETIRING.ordinal()] = 4;
            $EnumSwitchMapping$0[WorkerState.TERMINATED.ordinal()] = 5;
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "", "(Ljava/lang/String;I)V", "CPU_ACQUIRED", "BLOCKING", "PARKING", "RETIRING", "TERMINATED", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public enum WorkerState {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        RETIRING,
        TERMINATED
    }

    public CoroutineScheduler(int corePoolSize, int maxPoolSize, long idleWorkerKeepAliveNs, String schedulerName) {
        Intrinsics.checkParameterIsNotNull(schedulerName, "schedulerName");
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.idleWorkerKeepAliveNs = idleWorkerKeepAliveNs;
        this.schedulerName = schedulerName;
        if (this.corePoolSize >= 1) {
            if (this.maxPoolSize >= this.corePoolSize) {
                if (this.maxPoolSize <= 2097150) {
                    if (this.idleWorkerKeepAliveNs > 0) {
                        this.globalQueue = new GlobalQueue();
                        this.cpuPermits = new Semaphore(this.corePoolSize, false);
                        this.parkedWorkersStack = 0;
                        this.workers = new Worker[this.maxPoolSize + 1];
                        this.controlState = 0;
                        this.random = new Random();
                        this._isTerminated = 0;
                        return;
                    }
                    throw new IllegalArgumentException(("Idle worker keep alive time " + this.idleWorkerKeepAliveNs + " must be positive").toString());
                }
                throw new IllegalArgumentException(("Max pool size " + this.maxPoolSize + " should not exceed maximal supported number of threads 2097150").toString());
            }
            throw new IllegalArgumentException(("Max pool size " + this.maxPoolSize + " should be greater than or equals to core pool size " + this.corePoolSize).toString());
        }
        throw new IllegalArgumentException(("Core pool size " + this.corePoolSize + " should be at least 1").toString());
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public /* synthetic */ CoroutineScheduler(int i, int i2, long j, String str, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, r3, r5);
        String str2;
        long j2 = (i3 & 4) != 0 ? TasksKt.IDLE_WORKER_KEEP_ALIVE_NS : j;
        if ((i3 & 8) != 0) {
            str2 = TasksKt.DEFAULT_SCHEDULER_NAME;
        } else {
            str2 = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void parkedWorkersStackTopUpdate(Worker worker, int oldIndex, int newIndex) {
        int updIndex;
        while (true) {
            long top = this.parkedWorkersStack;
            int index = (int) (2097151 & top);
            long updVersion = (2097152 + top) & PARKED_VERSION_MASK;
            if (index != oldIndex) {
                updIndex = index;
            } else if (newIndex == 0) {
                updIndex = parkedWorkersStackNextIndex(worker);
            } else {
                updIndex = newIndex;
            }
            if (updIndex >= 0 && parkedWorkersStack$FU.compareAndSet(this, top, updVersion | ((long) updIndex))) {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void parkedWorkersStackPush(Worker worker) {
        long top;
        long updVersion;
        int updIndex;
        if (worker.getNextParkedWorker() == NOT_IN_STACK) {
            do {
                top = this.parkedWorkersStack;
                int index = (int) (2097151 & top);
                updVersion = (2097152 + top) & PARKED_VERSION_MASK;
                updIndex = worker.getIndexInArray();
                boolean z = updIndex != 0;
                if (!_Assertions.ENABLED || z) {
                    worker.setNextParkedWorker(this.workers[index]);
                } else {
                    throw new AssertionError("Assertion failed");
                }
            } while (!parkedWorkersStack$FU.compareAndSet(this, top, updVersion | ((long) updIndex)));
        }
    }

    private final Worker parkedWorkersStackPop() {
        int $i$a$1$loop = 0;
        while (true) {
            long top = this.parkedWorkersStack;
            Worker worker = this.workers[(int) (2097151 & top)];
            if (worker == null) {
                return null;
            }
            long updVersion = (2097152 + top) & PARKED_VERSION_MASK;
            int updIndex = parkedWorkersStackNextIndex(worker);
            if (updIndex >= 0 && parkedWorkersStack$FU.compareAndSet(this, top, updVersion | ((long) updIndex))) {
                worker.setNextParkedWorker(NOT_IN_STACK);
                return worker;
            }
            $i$a$1$loop = $i$a$1$loop;
        }
    }

    private final int parkedWorkersStackNextIndex(Worker worker) {
        Object next = worker.getNextParkedWorker();
        while (next != NOT_IN_STACK) {
            if (next == null) {
                return 0;
            }
            Worker nextWorker = (Worker) next;
            int updIndex = nextWorker.getIndexInArray();
            if (updIndex != 0) {
                return updIndex;
            }
            next = nextWorker.getNextParkedWorker();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getCreatedWorkers() {
        return (int) (this.controlState & 2097151);
    }

    private final int getBlockingWorkers() {
        return (int) ((this.controlState & BLOCKING_MASK) >> 21);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int createdWorkers(long state) {
        return (int) (2097151 & state);
    }

    private final int blockingWorkers(long state) {
        return (int) ((BLOCKING_MASK & state) >> 21);
    }

    private final int incrementCreatedWorkers() {
        return (int) (2097151 & controlState$FU.incrementAndGet(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int decrementCreatedWorkers() {
        return (int) (2097151 & controlState$FU.getAndDecrement(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void incrementBlockingWorkers() {
        controlState$FU.addAndGet(this, 2097152);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void decrementBlockingWorkers() {
        controlState$FU.addAndGet(this, PARKED_VERSION_MASK);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isTerminated() {
        return this._isTerminated != 0;
    }

    /* compiled from: CoroutineScheduler.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\u00020\u00048\u0002X\u0083\u0004¢\u0006\b\n\u0000\u0012\u0004\b\r\u0010\u0002R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\u00020\u00048\u0002X\u0083\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0012\u0010\u0002R\u000e\u0010\u0013\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$Companion;", "", "()V", "ADDED", "", "ADDED_REQUIRES_HELP", "ALLOWED", "BLOCKING_MASK", "", "BLOCKING_SHIFT", "CREATED_MASK", "FORBIDDEN", "MAX_PARK_TIME_NS", "MAX_PARK_TIME_NS$annotations", "MAX_SPINS", "MAX_SUPPORTED_POOL_SIZE", "MAX_YIELDS", "MIN_PARK_TIME_NS", "MIN_PARK_TIME_NS$annotations", "MIN_SUPPORTED_POOL_SIZE", "NOT_ADDED", "NOT_IN_STACK", "Lkotlinx/coroutines/internal/Symbol;", "PARKED_INDEX_MASK", "PARKED_VERSION_INC", "PARKED_VERSION_MASK", "TERMINATED", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class Companion {
        @JvmStatic
        private static /* synthetic */ void MAX_PARK_TIME_NS$annotations() {
        }

        @JvmStatic
        private static /* synthetic */ void MIN_PARK_TIME_NS$annotations() {
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        dispatch$default(this, command, null, false, 6, null);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        shutdown(10000);
    }

    /* JADX INFO: Multiple debug info for r6v1 int: [D('this_$iv' kotlinx.coroutines.scheduling.CoroutineScheduler), D('created' int)] */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0092, code lost:
        if (r4 != null) goto L_0x009b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void shutdown(long timeout) {
        int created;
        Task task;
        boolean z = true;
        if (_isTerminated$FU.compareAndSet(this, 0, 1)) {
            Thread currentThread = Thread.currentThread();
            if (!(currentThread instanceof Worker)) {
                currentThread = null;
            }
            Worker currentWorker = (Worker) currentThread;
            synchronized (this.workers) {
                created = (int) (this.controlState & 2097151);
            }
            if (1 <= created) {
                int i = 1;
                while (true) {
                    Worker worker = this.workers[i];
                    if (worker == null) {
                        Intrinsics.throwNpe();
                    }
                    if (worker != currentWorker) {
                        while (worker.isAlive()) {
                            LockSupport.unpark(worker);
                            worker.join(timeout);
                        }
                        WorkerState state = worker.getState();
                        if (state == WorkerState.TERMINATED) {
                            worker.getLocalQueue().offloadAllWork$kotlinx_coroutines_core(this.globalQueue);
                        } else {
                            throw new IllegalStateException(("Expected TERMINATED state, but found " + state).toString());
                        }
                    }
                    if (i == created) {
                        break;
                    }
                    i++;
                }
            }
            if (this.globalQueue.add(TasksKt.getCLOSED_TASK())) {
                while (true) {
                    if (currentWorker != null) {
                        task = currentWorker.findTask$kotlinx_coroutines_core();
                    }
                    task = this.globalQueue.removeFirstIfNotClosed();
                    if (task == null) {
                        break;
                    }
                    runSafely(task);
                }
                if (currentWorker != null) {
                    currentWorker.tryReleaseCpu$kotlinx_coroutines_core(WorkerState.TERMINATED);
                }
                if (this.cpuPermits.availablePermits() != this.corePoolSize) {
                    z = false;
                }
                if (!_Assertions.ENABLED || z) {
                    this.parkedWorkersStack = 0;
                    this.controlState = 0;
                    return;
                }
                throw new AssertionError("Assertion failed");
            }
            throw new IllegalStateException("GlobalQueue could not be closed yet".toString());
        }
    }

    public static /* synthetic */ void dispatch$default(CoroutineScheduler coroutineScheduler, Runnable runnable, TaskContext taskContext, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            taskContext = NonBlockingContext.INSTANCE;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        coroutineScheduler.dispatch(runnable, taskContext, z);
    }

    public final void dispatch(Runnable block, TaskContext taskContext, boolean fair) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        TimeSourceKt.getTimeSource().trackTask();
        Task task = createTask$kotlinx_coroutines_core(block, taskContext);
        int submitToLocalQueue = submitToLocalQueue(task, fair);
        if (submitToLocalQueue == -1) {
            return;
        }
        if (submitToLocalQueue != 1) {
            requestCpuWorker();
        } else if (this.globalQueue.add(task)) {
            requestCpuWorker();
        } else {
            throw new RejectedExecutionException(this.schedulerName + " was terminated");
        }
    }

    public final Task createTask$kotlinx_coroutines_core(Runnable block, TaskContext taskContext) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        return new Task(block, TasksKt.schedulerTimeSource.nanoTime(), taskContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Multiple debug info for r2v1 int: [D('this_$iv' kotlinx.coroutines.scheduling.CoroutineScheduler), D('created' int)] */
    /* JADX INFO: Multiple debug info for r3v1 int: [D('$i$f$blockingWorkers' int), D('blocking' int)] */
    public final void requestCpuWorker() {
        if (this.cpuPermits.availablePermits() == 0) {
            tryUnpark();
        } else if (!tryUnpark()) {
            long state = this.controlState;
            if (((int) (2097151 & state)) - ((int) ((BLOCKING_MASK & state) >> 21)) < this.corePoolSize) {
                int newCpuWorkers = createNewWorker();
                if (newCpuWorkers == 1 && this.corePoolSize > 1) {
                    createNewWorker();
                }
                if (newCpuWorkers > 0) {
                    return;
                }
            }
            tryUnpark();
        }
    }

    private final boolean tryUnpark() {
        while (true) {
            Worker worker = parkedWorkersStackPop();
            if (worker == null) {
                return false;
            }
            worker.idleResetBeforeUnpark();
            boolean wasParking = worker.isParking();
            LockSupport.unpark(worker);
            if (wasParking && worker.tryForbidTermination()) {
                return true;
            }
        }
    }

    /* JADX INFO: Multiple debug info for r12v9 kotlinx.coroutines.scheduling.CoroutineScheduler$Worker: [D('worker' kotlinx.coroutines.scheduling.CoroutineScheduler$Worker), D('$receiver' kotlinx.coroutines.scheduling.CoroutineScheduler$Worker)] */
    /* JADX INFO: Multiple debug info for r7v1 int: [D('this_$iv' kotlinx.coroutines.scheduling.CoroutineScheduler), D('created' int)] */
    /* JADX INFO: Multiple debug info for r8v2 int: [D('this_$iv' kotlinx.coroutines.scheduling.CoroutineScheduler), D('blocking' int)] */
    private final int createNewWorker() {
        synchronized (this.workers) {
            if (isTerminated()) {
                return -1;
            }
            long state = this.controlState;
            int created = (int) (state & 2097151);
            int cpuWorkers = created - ((int) ((BLOCKING_MASK & state) >> 21));
            if (cpuWorkers >= this.corePoolSize) {
                return 0;
            }
            if (created < this.maxPoolSize && this.cpuPermits.availablePermits() != 0) {
                int newIndex = (int) (2097151 & controlState$FU.incrementAndGet(this));
                if (newIndex > 0 && this.workers[newIndex] == null) {
                    Worker $receiver = new Worker(this, newIndex);
                    $receiver.start();
                    this.workers[newIndex] = $receiver;
                    return cpuWorkers + 1;
                }
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            return 0;
        }
    }

    private final int submitToLocalQueue(Task task, boolean fair) {
        boolean noOffloadingHappened;
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof Worker)) {
            currentThread = null;
        }
        Worker worker = (Worker) currentThread;
        if (worker == null || worker.getScheduler() != this || worker.getState() == WorkerState.TERMINATED) {
            return 1;
        }
        int result = -1;
        if (task.getMode() == TaskMode.NON_BLOCKING) {
            if (worker.isBlocking()) {
                result = 0;
            } else if (!worker.tryAcquireCpuPermit()) {
                return 1;
            }
        }
        if (fair) {
            noOffloadingHappened = worker.getLocalQueue().addLast(task, this.globalQueue);
        } else {
            noOffloadingHappened = worker.getLocalQueue().add(task, this.globalQueue);
        }
        if (!noOffloadingHappened || worker.getLocalQueue().getBufferSize$kotlinx_coroutines_core() > TasksKt.QUEUE_SIZE_OFFLOAD_THRESHOLD) {
            return 0;
        }
        return result;
    }

    @Override // java.lang.Object
    public String toString() {
        int parkedWorkers = 0;
        int blockingWorkers = 0;
        int cpuWorkers = 0;
        int retired = 0;
        int terminated = 0;
        ArrayList queueSizes = new ArrayList();
        Worker[] workerArr = this.workers;
        for (Worker worker : workerArr) {
            if (worker != null) {
                int queueSize = worker.getLocalQueue().size$kotlinx_coroutines_core();
                int i = WhenMappings.$EnumSwitchMapping$0[worker.getState().ordinal()];
                if (i == 1) {
                    parkedWorkers++;
                } else if (i == 2) {
                    blockingWorkers++;
                    queueSizes.add(String.valueOf(queueSize) + "b");
                } else if (i == 3) {
                    cpuWorkers++;
                    queueSizes.add(String.valueOf(queueSize) + "c");
                } else if (i == 4) {
                    retired++;
                    if (queueSize > 0) {
                        queueSizes.add(String.valueOf(queueSize) + "r");
                    }
                } else if (i == 5) {
                    terminated++;
                }
            }
        }
        long state = this.controlState;
        return this.schedulerName + '@' + DebugKt.getHexAddress(this) + "[Pool Size {core = " + this.corePoolSize + ", max = " + this.maxPoolSize + "}, Worker States {CPU = " + cpuWorkers + ", blocking = " + blockingWorkers + ", parked = " + parkedWorkers + ", retired = " + retired + ", terminated = " + terminated + "}, running workers queues = " + queueSizes + ", global queue size = " + this.globalQueue.getSize() + ", Control State Workers {created = " + ((int) (2097151 & state)) + ", blocking = " + ((int) ((BLOCKING_MASK & state) >> 21)) + "}]";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void runSafely(Task task) {
        try {
            task.run();
        } finally {
            try {
            } finally {
            }
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\r\b\u0080\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0007\b\u0002¢\u0006\u0002\u0010\u0005J\u0010\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0002J\u0010\u00100\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0002J\b\u00101\u001a\u00020\fH\u0002J\b\u00102\u001a\u00020-H\u0002J\b\u00103\u001a\u00020-H\u0002J\u0010\u00104\u001a\u00020-2\u0006\u00105\u001a\u00020\u0010H\u0002J\u000f\u00106\u001a\u0004\u0018\u00010/H\u0000¢\u0006\u0002\b7J\n\u00108\u001a\u0004\u0018\u00010/H\u0002J\u0010\u00109\u001a\u00020-2\u0006\u0010:\u001a\u00020;H\u0002J\u0006\u0010<\u001a\u00020-J\u0015\u0010=\u001a\u00020\u00032\u0006\u0010>\u001a\u00020\u0003H\u0000¢\u0006\u0002\b?J\b\u0010@\u001a\u00020-H\u0016J\u0006\u0010A\u001a\u00020\fJ\u0006\u0010B\u001a\u00020\fJ\u0015\u0010C\u001a\u00020\f2\u0006\u0010D\u001a\u00020$H\u0000¢\u0006\u0002\bEJ\n\u0010F\u001a\u0004\u0018\u00010/H\u0002J\b\u0010G\u001a\u00020-H\u0002R$\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\rR\u0011\u0010\u000e\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\rR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0012\u001a\u00020\u0013¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001e\u001a\u00020\u001f8F¢\u0006\u0006\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010#\u001a\u00020$X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u000e\u0010)\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006H"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "Ljava/lang/Thread;", FirebaseAnalytics.Param.INDEX, "", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler;I)V", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler;)V", "indexInArray", "getIndexInArray", "()I", "setIndexInArray", "(I)V", "isBlocking", "", "()Z", "isParking", "lastExhaustionTime", "", "lastStealIndex", "localQueue", "Lkotlinx/coroutines/scheduling/WorkQueue;", "getLocalQueue", "()Lkotlinx/coroutines/scheduling/WorkQueue;", "nextParkedWorker", "", "getNextParkedWorker", "()Ljava/lang/Object;", "setNextParkedWorker", "(Ljava/lang/Object;)V", "parkTimeNs", "rngState", "scheduler", "Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "getScheduler", "()Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "spins", "state", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "getState", "()Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "setState", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;)V", "terminationDeadline", "terminationState", "Lkotlinx/atomicfu/AtomicInt;", "afterTask", "", "task", "Lkotlinx/coroutines/scheduling/Task;", "beforeTask", "blockingQuiescence", "blockingWorkerIdle", "cpuWorkerIdle", "doPark", "nanos", "findTask", "findTask$kotlinx_coroutines_core", "findTaskWithCpuPermit", "idleReset", "mode", "Lkotlinx/coroutines/scheduling/TaskMode;", "idleResetBeforeUnpark", "nextInt", "upperBound", "nextInt$kotlinx_coroutines_core", "run", "tryAcquireCpuPermit", "tryForbidTermination", "tryReleaseCpu", "newState", "tryReleaseCpu$kotlinx_coroutines_core", "trySteal", "tryTerminateWorker", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public final class Worker extends Thread {
        private static final AtomicIntegerFieldUpdater terminationState$FU = AtomicIntegerFieldUpdater.newUpdater(Worker.class, "terminationState");
        private volatile int indexInArray;
        private long lastExhaustionTime;
        private int lastStealIndex;
        private final WorkQueue localQueue;
        private volatile Object nextParkedWorker;
        private int parkTimeNs;
        private int rngState;
        private volatile int spins;
        private volatile WorkerState state;
        private long terminationDeadline;
        private volatile int terminationState;

        /* JADX WARN: Incorrect args count in method signature: ()V */
        private Worker() {
            setDaemon(true);
            this.localQueue = new WorkQueue();
            this.state = WorkerState.RETIRING;
            this.terminationState = 0;
            this.nextParkedWorker = CoroutineScheduler.NOT_IN_STACK;
            this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
            this.rngState = CoroutineScheduler.this.random.nextInt();
        }

        public final int getIndexInArray() {
            return this.indexInArray;
        }

        public final void setIndexInArray(int index) {
            StringBuilder sb = new StringBuilder();
            sb.append(CoroutineScheduler.this.schedulerName);
            sb.append("-worker-");
            sb.append(index == 0 ? "TERMINATED" : String.valueOf(index));
            setName(sb.toString());
            this.indexInArray = index;
        }

        public Worker(CoroutineScheduler $outer, int index) {
            this();
            setIndexInArray(index);
        }

        public final CoroutineScheduler getScheduler() {
            return CoroutineScheduler.this;
        }

        public final WorkQueue getLocalQueue() {
            return this.localQueue;
        }

        @Override // java.lang.Thread
        public final WorkerState getState() {
            return this.state;
        }

        public final void setState(WorkerState workerState) {
            Intrinsics.checkParameterIsNotNull(workerState, "<set-?>");
            this.state = workerState;
        }

        public final boolean isParking() {
            return this.state == WorkerState.PARKING;
        }

        public final boolean isBlocking() {
            return this.state == WorkerState.BLOCKING;
        }

        public final Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }

        public final void setNextParkedWorker(Object obj) {
            this.nextParkedWorker = obj;
        }

        public final boolean tryForbidTermination() {
            int state = this.terminationState;
            if (state == -1) {
                return false;
            }
            if (state == 0) {
                return terminationState$FU.compareAndSet(this, 0, -1);
            }
            if (state == 1) {
                return false;
            }
            throw new IllegalStateException(("Invalid terminationState = " + state).toString());
        }

        public final boolean tryAcquireCpuPermit() {
            if (this.state == WorkerState.CPU_ACQUIRED) {
                return true;
            }
            if (!CoroutineScheduler.this.cpuPermits.tryAcquire()) {
                return false;
            }
            this.state = WorkerState.CPU_ACQUIRED;
            return true;
        }

        public final boolean tryReleaseCpu$kotlinx_coroutines_core(WorkerState newState) {
            Intrinsics.checkParameterIsNotNull(newState, "newState");
            WorkerState previousState = this.state;
            boolean hadCpu = previousState == WorkerState.CPU_ACQUIRED;
            if (hadCpu) {
                CoroutineScheduler.this.cpuPermits.release();
            }
            if (previousState != newState) {
                this.state = newState;
            }
            return hadCpu;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            boolean wasIdle = false;
            while (!CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                Task task = findTask$kotlinx_coroutines_core();
                if (task == null) {
                    if (this.state == WorkerState.CPU_ACQUIRED) {
                        cpuWorkerIdle();
                    } else {
                        blockingWorkerIdle();
                    }
                    wasIdle = true;
                } else {
                    if (wasIdle) {
                        idleReset(task.getMode());
                        wasIdle = false;
                    }
                    beforeTask(task);
                    CoroutineScheduler.this.runSafely(task);
                    afterTask(task);
                }
            }
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.TERMINATED);
        }

        private final void beforeTask(Task task) {
            if (task.getMode() != TaskMode.NON_BLOCKING) {
                CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, 2097152);
                if (tryReleaseCpu$kotlinx_coroutines_core(WorkerState.BLOCKING)) {
                    CoroutineScheduler.this.requestCpuWorker();
                }
            } else if (CoroutineScheduler.this.cpuPermits.availablePermits() != 0) {
                long now = TasksKt.schedulerTimeSource.nanoTime();
                if (now - task.submissionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS && now - this.lastExhaustionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS * ((long) 5)) {
                    this.lastExhaustionTime = now;
                    CoroutineScheduler.this.requestCpuWorker();
                }
            }
        }

        /* JADX INFO: Multiple debug info for r0v2 kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState: [D('this_$iv' kotlinx.coroutines.scheduling.CoroutineScheduler), D('currentState' kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState)] */
        private final void afterTask(Task task) {
            if (task.getMode() != TaskMode.NON_BLOCKING) {
                CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, CoroutineScheduler.PARKED_VERSION_MASK);
                WorkerState currentState = this.state;
                if (currentState != WorkerState.TERMINATED) {
                    boolean z = currentState == WorkerState.BLOCKING;
                    if (!_Assertions.ENABLED || z) {
                        this.state = WorkerState.RETIRING;
                        return;
                    }
                    throw new AssertionError("Expected BLOCKING state, but has " + currentState);
                }
            }
        }

        public final int nextInt$kotlinx_coroutines_core(int upperBound) {
            int i = this.rngState;
            this.rngState = i ^ (i << 13);
            int i2 = this.rngState;
            this.rngState = i2 ^ (i2 >> 17);
            int i3 = this.rngState;
            this.rngState = i3 ^ (i3 << 5);
            int mask = upperBound - 1;
            if ((mask & upperBound) == 0) {
                return this.rngState & mask;
            }
            return (this.rngState & Integer.MAX_VALUE) % upperBound;
        }

        private final void cpuWorkerIdle() {
            int spins = this.spins;
            if (spins <= 1500) {
                this.spins = spins + 1;
                if (spins >= 1000) {
                    Thread.yield();
                    return;
                }
                return;
            }
            if (this.parkTimeNs < CoroutineScheduler.MAX_PARK_TIME_NS) {
                this.parkTimeNs = RangesKt.coerceAtMost((this.parkTimeNs * 3) >>> 1, CoroutineScheduler.MAX_PARK_TIME_NS);
            }
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
            doPark((long) this.parkTimeNs);
        }

        private final void blockingWorkerIdle() {
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
            if (blockingQuiescence()) {
                this.terminationState = 0;
                if (this.terminationDeadline == 0) {
                    this.terminationDeadline = System.nanoTime() + CoroutineScheduler.this.idleWorkerKeepAliveNs;
                }
                doPark(CoroutineScheduler.this.idleWorkerKeepAliveNs);
                if (System.nanoTime() - this.terminationDeadline >= 0) {
                    this.terminationDeadline = 0;
                    tryTerminateWorker();
                }
            }
        }

        private final void doPark(long nanos) {
            CoroutineScheduler.this.parkedWorkersStackPush(this);
            LockSupport.parkNanos(nanos);
        }

        /* JADX INFO: Multiple debug info for r1v2 int: [D('$i$f$createdWorkers' int), D('lastIndex' int)] */
        private final void tryTerminateWorker() {
            synchronized (CoroutineScheduler.this.workers) {
                if (!CoroutineScheduler.this.isTerminated()) {
                    if (CoroutineScheduler.this.getCreatedWorkers() > CoroutineScheduler.this.corePoolSize) {
                        if (blockingQuiescence()) {
                            if (terminationState$FU.compareAndSet(this, 0, 1)) {
                                int oldIndex = this.indexInArray;
                                setIndexInArray(0);
                                CoroutineScheduler.this.parkedWorkersStackTopUpdate(this, oldIndex, 0);
                                int lastIndex = (int) (2097151 & CoroutineScheduler.controlState$FU.getAndDecrement(CoroutineScheduler.this));
                                if (lastIndex != oldIndex) {
                                    Worker lastWorker = CoroutineScheduler.this.workers[lastIndex];
                                    if (lastWorker == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    CoroutineScheduler.this.workers[oldIndex] = lastWorker;
                                    lastWorker.setIndexInArray(oldIndex);
                                    CoroutineScheduler.this.parkedWorkersStackTopUpdate(lastWorker, lastIndex, oldIndex);
                                }
                                CoroutineScheduler.this.workers[lastIndex] = null;
                                Unit unit = Unit.INSTANCE;
                                this.state = WorkerState.TERMINATED;
                            }
                        }
                    }
                }
            }
        }

        private final boolean blockingQuiescence() {
            Task it = CoroutineScheduler.this.globalQueue.removeFirstBlockingModeOrNull();
            if (it == null) {
                return true;
            }
            this.localQueue.add(it, CoroutineScheduler.this.globalQueue);
            return false;
        }

        private final void idleReset(TaskMode mode) {
            this.terminationDeadline = 0;
            this.lastStealIndex = 0;
            if (this.state == WorkerState.PARKING) {
                boolean z = mode == TaskMode.PROBABLY_BLOCKING;
                if (!_Assertions.ENABLED || z) {
                    this.state = WorkerState.BLOCKING;
                    this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
                } else {
                    throw new AssertionError("Assertion failed");
                }
            }
            this.spins = 0;
        }

        public final void idleResetBeforeUnpark() {
            this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
            this.spins = 0;
        }

        public final Task findTask$kotlinx_coroutines_core() {
            if (tryAcquireCpuPermit()) {
                return findTaskWithCpuPermit();
            }
            Task poll = this.localQueue.poll();
            return poll != null ? poll : CoroutineScheduler.this.globalQueue.removeFirstBlockingModeOrNull();
        }

        private final Task findTaskWithCpuPermit() {
            Task it;
            Task it2;
            boolean globalFirst = false;
            if (nextInt$kotlinx_coroutines_core(CoroutineScheduler.this.corePoolSize * 2) == 0) {
                globalFirst = true;
            }
            if (globalFirst && (it2 = CoroutineScheduler.this.globalQueue.removeFirstIfNotClosed()) != null) {
                return it2;
            }
            Task it3 = this.localQueue.poll();
            if (it3 != null) {
                return it3;
            }
            if (globalFirst || (it = CoroutineScheduler.this.globalQueue.removeFirstIfNotClosed()) == null) {
                return trySteal();
            }
            return it;
        }

        private final Task trySteal() {
            int created = CoroutineScheduler.this.getCreatedWorkers();
            if (created < 2) {
                return null;
            }
            int stealIndex = this.lastStealIndex;
            if (stealIndex == 0) {
                stealIndex = nextInt$kotlinx_coroutines_core(created);
            }
            int stealIndex2 = stealIndex + 1;
            if (stealIndex2 > created) {
                stealIndex2 = 1;
            }
            this.lastStealIndex = stealIndex2;
            Worker worker = CoroutineScheduler.this.workers[stealIndex2];
            if (worker == null || worker == this || !this.localQueue.trySteal(worker.localQueue, CoroutineScheduler.this.globalQueue)) {
                return null;
            }
            return this.localQueue.poll();
        }
    }
}
