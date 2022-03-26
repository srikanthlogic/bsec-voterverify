package kotlinx.coroutines.scheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.DefaultExecutor;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
/* compiled from: Dispatcher.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0017\u0018\u00002\u00020\u0001B\u001b\b\u0016\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0003J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\nH\u0002J\u001c\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00172\n\u0010\u0018\u001a\u00060\u0019j\u0002`\u001aH\u0016J)\u0010\u001b\u001a\u00020\u00132\n\u0010\u0018\u001a\u00060\u0019j\u0002`\u001a2\u0006\u0010\u0016\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0000¢\u0006\u0002\b\u001fJ\u001c\u0010 \u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00172\n\u0010\u0018\u001a\u00060\u0019j\u0002`\u001aH\u0016J\u000e\u0010!\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003J\r\u0010\"\u001a\u00020\u0013H\u0000¢\u0006\u0002\b#J\u0015\u0010$\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\u0007H\u0000¢\u0006\u0002\b&J\b\u0010'\u001a\u00020(H\u0016J\r\u0010)\u001a\u00020\u0013H\u0000¢\u0006\u0002\b*R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006+"}, d2 = {"Lkotlinx/coroutines/scheduling/ExperimentalCoroutineDispatcher;", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "corePoolSize", "", "maxPoolSize", "(II)V", "idleWorkerKeepAliveNs", "", "(IIJ)V", "coroutineScheduler", "Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "executor", "Ljava/util/concurrent/Executor;", "getExecutor", "()Ljava/util/concurrent/Executor;", "blocking", "Lkotlinx/coroutines/CoroutineDispatcher;", "parallelism", "close", "", "createScheduler", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dispatchWithContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "fair", "", "dispatchWithContext$kotlinx_coroutines_core", "dispatchYield", "limited", "restore", "restore$kotlinx_coroutines_core", "shutdown", "timeout", "shutdown$kotlinx_coroutines_core", "toString", "", "usePrivateScheduler", "usePrivateScheduler$kotlinx_coroutines_core", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public class ExperimentalCoroutineDispatcher extends ExecutorCoroutineDispatcher {
    private final int corePoolSize;
    private CoroutineScheduler coroutineScheduler;
    private final long idleWorkerKeepAliveNs;
    private final int maxPoolSize;

    public ExperimentalCoroutineDispatcher(int corePoolSize, int maxPoolSize, long idleWorkerKeepAliveNs) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.idleWorkerKeepAliveNs = idleWorkerKeepAliveNs;
        this.coroutineScheduler = createScheduler();
    }

    public ExperimentalCoroutineDispatcher(int corePoolSize, int maxPoolSize) {
        this(corePoolSize, maxPoolSize, TasksKt.IDLE_WORKER_KEEP_ALIVE_NS);
    }

    public /* synthetic */ ExperimentalCoroutineDispatcher(int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? TasksKt.CORE_POOL_SIZE : i, (i3 & 2) != 0 ? TasksKt.MAX_POOL_SIZE : i2);
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher
    public Executor getExecutor() {
        return this.coroutineScheduler;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext context, Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        try {
            CoroutineScheduler.dispatch$default(this.coroutineScheduler, block, null, false, 6, null);
        } catch (RejectedExecutionException e) {
            DefaultExecutor.INSTANCE.dispatch(context, block);
        }
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatchYield(CoroutineContext context, Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        try {
            CoroutineScheduler.dispatch$default(this.coroutineScheduler, block, null, true, 2, null);
        } catch (RejectedExecutionException e) {
            DefaultExecutor.INSTANCE.dispatchYield(context, block);
        }
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.coroutineScheduler.close();
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
    public String toString() {
        return super.toString() + "[scheduler = " + this.coroutineScheduler + ']';
    }

    public static /* synthetic */ CoroutineDispatcher blocking$default(ExperimentalCoroutineDispatcher experimentalCoroutineDispatcher, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 1) != 0) {
                i = TasksKt.BLOCKING_DEFAULT_PARALLELISM;
            }
            return experimentalCoroutineDispatcher.blocking(i);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: blocking");
    }

    public final CoroutineDispatcher blocking(int parallelism) {
        boolean z = false;
        if (parallelism > 0) {
            z = true;
        }
        if (z) {
            return new LimitingDispatcher(this, parallelism, TaskMode.PROBABLY_BLOCKING);
        }
        throw new IllegalArgumentException(("Expected positive parallelism level, but have " + parallelism).toString());
    }

    public final CoroutineDispatcher limited(int parallelism) {
        boolean z = true;
        if (parallelism > 0) {
            if (parallelism > this.corePoolSize) {
                z = false;
            }
            if (z) {
                return new LimitingDispatcher(this, parallelism, TaskMode.NON_BLOCKING);
            }
            throw new IllegalArgumentException(("Expected parallelism level lesser than core pool size (" + this.corePoolSize + "), but have " + parallelism).toString());
        }
        throw new IllegalArgumentException(("Expected positive parallelism level, but have " + parallelism).toString());
    }

    public final void dispatchWithContext$kotlinx_coroutines_core(Runnable block, TaskContext context, boolean fair) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(context, "context");
        try {
            this.coroutineScheduler.dispatch(block, context, fair);
        } catch (RejectedExecutionException e) {
            DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(this.coroutineScheduler.createTask$kotlinx_coroutines_core(block, context));
        }
    }

    private final CoroutineScheduler createScheduler() {
        return new CoroutineScheduler(this.corePoolSize, this.maxPoolSize, this.idleWorkerKeepAliveNs, null, 8, null);
    }

    public final synchronized void usePrivateScheduler$kotlinx_coroutines_core() {
        this.coroutineScheduler.shutdown(10000);
        this.coroutineScheduler = createScheduler();
    }

    public final synchronized void shutdown$kotlinx_coroutines_core(long timeout) {
        this.coroutineScheduler.shutdown(timeout);
    }

    public final void restore$kotlinx_coroutines_core() {
        usePrivateScheduler$kotlinx_coroutines_core();
    }
}
