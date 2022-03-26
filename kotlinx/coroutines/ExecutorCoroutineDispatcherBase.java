package kotlinx.coroutines;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Delay;
import kotlinx.coroutines.internal.ConcurrentKt;
/* compiled from: Executors.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b \u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u001c\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\n\u0010\u000b\u001a\u00060\fj\u0002`\rH\u0016J\u0013\u0010\u000e\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\r\u0010\u0013\u001a\u00020\u0007H\u0000¢\u0006\u0002\b\u0014J\u001c\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\n\u0010\u000b\u001a\u00060\fj\u0002`\rH\u0016J*\u0010\u0019\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u001a2\n\u0010\u000b\u001a\u00060\fj\u0002`\r2\u0006\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u001e\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00182\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00070 H\u0016J\b\u0010!\u001a\u00020\"H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lkotlinx/coroutines/ExecutorCoroutineDispatcherBase;", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "Lkotlinx/coroutines/Delay;", "()V", "removesFutureOnCancellation", "", "close", "", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "equals", "other", "", "hashCode", "", "initFutureCancellation", "initFutureCancellation$kotlinx_coroutines_core", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "", "scheduleBlock", "Ljava/util/concurrent/ScheduledFuture;", "time", "unit", "Ljava/util/concurrent/TimeUnit;", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public abstract class ExecutorCoroutineDispatcherBase extends ExecutorCoroutineDispatcher implements Delay {
    private boolean removesFutureOnCancellation;

    @Override // kotlinx.coroutines.Delay
    public Object delay(long time, Continuation<? super Unit> continuation) {
        return Delay.DefaultImpls.delay(this, time, continuation);
    }

    public final void initFutureCancellation$kotlinx_coroutines_core() {
        this.removesFutureOnCancellation = ConcurrentKt.removeFutureOnCancel(getExecutor());
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext context, Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        try {
            getExecutor().execute(TimeSourceKt.getTimeSource().wrapTask(block));
        } catch (RejectedExecutionException e) {
            TimeSourceKt.getTimeSource().unTrackTask();
            DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(block);
        }
    }

    @Override // kotlinx.coroutines.Delay
    public void scheduleResumeAfterDelay(long timeMillis, CancellableContinuation<? super Unit> cancellableContinuation) {
        ScheduledFuture future;
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "continuation");
        if (this.removesFutureOnCancellation) {
            future = scheduleBlock(new ResumeUndispatchedRunnable(this, cancellableContinuation), timeMillis, TimeUnit.MILLISECONDS);
        } else {
            future = null;
        }
        if (future != null) {
            JobKt.cancelFutureOnCancellation(cancellableContinuation, future);
        } else {
            DefaultExecutor.INSTANCE.scheduleResumeAfterDelay(timeMillis, cancellableContinuation);
        }
    }

    @Override // kotlinx.coroutines.Delay
    public DisposableHandle invokeOnTimeout(long timeMillis, Runnable block) {
        ScheduledFuture future;
        Intrinsics.checkParameterIsNotNull(block, "block");
        if (this.removesFutureOnCancellation) {
            future = scheduleBlock(block, timeMillis, TimeUnit.MILLISECONDS);
        } else {
            future = null;
        }
        return future != null ? new DisposableFutureHandle(future) : DefaultExecutor.INSTANCE.invokeOnTimeout(timeMillis, block);
    }

    private final ScheduledFuture<?> scheduleBlock(Runnable block, long time, TimeUnit unit) {
        try {
            Executor executor = getExecutor();
            if (!(executor instanceof ScheduledExecutorService)) {
                executor = null;
            }
            ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) executor;
            if (scheduledExecutorService != null) {
                return scheduledExecutorService.schedule(block, time, unit);
            }
            return null;
        } catch (RejectedExecutionException e) {
            return null;
        }
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Executor executor = getExecutor();
        if (!(executor instanceof ExecutorService)) {
            executor = null;
        }
        ExecutorService executorService = (ExecutorService) executor;
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
    public String toString() {
        return getExecutor().toString();
    }

    @Override // java.lang.Object
    public boolean equals(Object other) {
        return (other instanceof ExecutorCoroutineDispatcherBase) && ((ExecutorCoroutineDispatcherBase) other).getExecutor() == getExecutor();
    }

    @Override // java.lang.Object
    public int hashCode() {
        return System.identityHashCode(getExecutor());
    }
}
