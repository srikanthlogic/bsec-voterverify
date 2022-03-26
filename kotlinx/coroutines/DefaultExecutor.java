package kotlinx.coroutines;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin._Assertions;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.EventLoopBase;
/* compiled from: DefaultExecutor.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\bÀ\u0002\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0004J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u000eH\u0002J\r\u0010\u0018\u001a\u00020\u0016H\u0000¢\u0006\u0002\b\u0019J\u001c\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\b2\n\u0010\u001d\u001a\u00060\u0002j\u0002`\u0003H\u0016J\b\u0010\u001e\u001a\u00020\u0012H\u0014J\b\u0010\u001f\u001a\u00020\u0012H\u0002J\b\u0010 \u001a\u00020\u0016H\u0016J\u000e\u0010!\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020\bJ\b\u0010#\u001a\u00020\u000eH\u0002J\b\u0010$\u001a\u00020\u0016H\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\b\n\u0000\u0012\u0004\b\u000f\u0010\u0004R\u000e\u0010\u0010\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00128BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0013¨\u0006%"}, d2 = {"Lkotlinx/coroutines/DefaultExecutor;", "Lkotlinx/coroutines/EventLoopBase;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "()V", "ACTIVE", "", "DEFAULT_KEEP_ALIVE", "", "FRESH", "KEEP_ALIVE_NANOS", "SHUTDOWN_ACK", "SHUTDOWN_REQ", "_thread", "Ljava/lang/Thread;", "_thread$annotations", "debugStatus", "isCompleted", "", "()Z", "isShutdownRequested", "acknowledgeShutdownIfNeeded", "", "createThreadSync", "ensureStarted", "ensureStarted$kotlinx_coroutines_core", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "block", "isCorrectThread", "notifyStartup", "run", "shutdown", "timeout", "thread", "unpark", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class DefaultExecutor extends EventLoopBase implements Runnable {
    private static final int ACTIVE;
    private static final long DEFAULT_KEEP_ALIVE;
    private static final int FRESH;
    public static final DefaultExecutor INSTANCE = new DefaultExecutor();
    private static final long KEEP_ALIVE_NANOS;
    private static final int SHUTDOWN_ACK;
    private static final int SHUTDOWN_REQ;
    private static volatile Thread _thread;
    private static volatile int debugStatus;

    private static /* synthetic */ void _thread$annotations() {
    }

    static {
        Long l;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        try {
            l = Long.getLong("kotlinx.coroutines.DefaultExecutor.keepAlive", (long) DEFAULT_KEEP_ALIVE);
        } catch (SecurityException e) {
            l = Long.valueOf((long) DEFAULT_KEEP_ALIVE);
        }
        Intrinsics.checkExpressionValueIsNotNull(l, "try {\n            java.l…AULT_KEEP_ALIVE\n        }");
        KEEP_ALIVE_NANOS = timeUnit.toNanos(l.longValue());
    }

    private DefaultExecutor() {
    }

    @Override // kotlinx.coroutines.EventLoopBase
    protected boolean isCompleted() {
        return false;
    }

    private final boolean isShutdownRequested() {
        int debugStatus2 = debugStatus;
        return debugStatus2 == 2 || debugStatus2 == 3;
    }

    @Override // kotlinx.coroutines.EventLoopBase, kotlinx.coroutines.Delay
    public DisposableHandle invokeOnTimeout(long timeMillis, Runnable block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        EventLoopBase.DelayedRunnableTask it = new EventLoopBase.DelayedRunnableTask(timeMillis, block);
        INSTANCE.schedule$kotlinx_coroutines_core(it);
        return it;
    }

    @Override // java.lang.Runnable
    public void run() {
        TimeSourceKt.getTimeSource().registerTimeLoopThread();
        long shutdownNanos = Long.MAX_VALUE;
        try {
            if (notifyStartup()) {
                while (true) {
                    Thread.interrupted();
                    long parkNanos = processNextEvent();
                    if (parkNanos == Long.MAX_VALUE) {
                        if (shutdownNanos == Long.MAX_VALUE) {
                            long now = TimeSourceKt.getTimeSource().nanoTime();
                            if (shutdownNanos == Long.MAX_VALUE) {
                                shutdownNanos = now + KEEP_ALIVE_NANOS;
                            }
                            long tillShutdown = shutdownNanos - now;
                            if (tillShutdown <= 0) {
                                _thread = null;
                                acknowledgeShutdownIfNeeded();
                                TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
                                if (!isEmpty()) {
                                    thread();
                                    return;
                                }
                                return;
                            }
                            parkNanos = RangesKt.coerceAtMost(parkNanos, tillShutdown);
                        } else {
                            parkNanos = RangesKt.coerceAtMost(parkNanos, KEEP_ALIVE_NANOS);
                        }
                    }
                    if (parkNanos > 0) {
                        if (isShutdownRequested()) {
                            _thread = null;
                            acknowledgeShutdownIfNeeded();
                            TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
                            if (!isEmpty()) {
                                thread();
                                return;
                            }
                            return;
                        }
                        TimeSourceKt.getTimeSource().parkNanos(this, parkNanos);
                    }
                }
            }
        } finally {
            _thread = null;
            acknowledgeShutdownIfNeeded();
            TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
            if (!isEmpty()) {
                thread();
            }
        }
    }

    private final Thread thread() {
        Thread thread = _thread;
        return thread != null ? thread : createThreadSync();
    }

    private final synchronized Thread createThreadSync() {
        Thread $receiver;
        $receiver = _thread;
        if ($receiver == null) {
            $receiver = new Thread(this, "kotlinx.coroutines.DefaultExecutor");
            _thread = $receiver;
            $receiver.setDaemon(true);
            $receiver.start();
        }
        return $receiver;
    }

    @Override // kotlinx.coroutines.EventLoopBase
    protected void unpark() {
        TimeSourceKt.getTimeSource().unpark(thread());
    }

    @Override // kotlinx.coroutines.EventLoopBase
    protected boolean isCorrectThread() {
        return true;
    }

    public final synchronized void ensureStarted$kotlinx_coroutines_core() {
        boolean z = true;
        boolean z2 = _thread == null;
        if (_Assertions.ENABLED && !z2) {
            throw new AssertionError("Assertion failed");
        }
        if (!(debugStatus == 0 || debugStatus == 3)) {
            z = false;
        }
        if (_Assertions.ENABLED && !z) {
            throw new AssertionError("Assertion failed");
        }
        debugStatus = 0;
        createThreadSync();
        while (debugStatus == 0) {
            wait();
        }
    }

    private final synchronized boolean notifyStartup() {
        if (isShutdownRequested()) {
            return false;
        }
        debugStatus = 1;
        notifyAll();
        return true;
    }

    public final synchronized void shutdown(long timeout) {
        long deadline = System.currentTimeMillis() + timeout;
        if (!isShutdownRequested()) {
            debugStatus = 2;
        }
        while (debugStatus != 3 && _thread != null) {
            Thread it = _thread;
            if (it != null) {
                TimeSourceKt.getTimeSource().unpark(it);
            }
            if (deadline - System.currentTimeMillis() <= 0) {
                break;
            }
            wait(timeout);
        }
        debugStatus = 0;
    }

    private final synchronized void acknowledgeShutdownIfNeeded() {
        if (isShutdownRequested()) {
            debugStatus = 3;
            resetAll();
            notifyAll();
        }
    }
}
