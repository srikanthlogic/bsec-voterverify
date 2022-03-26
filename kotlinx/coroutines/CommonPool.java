package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
/* compiled from: CommonPool.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0004\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\u0011\u001a\u0004\u0018\u0001H\u0012\"\u0004\b\u0000\u0010\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0014H\u0082\b¢\u0006\u0002\u0010\u0015J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0019H\u0002J\u001c\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u001d2\n\u0010\u0013\u001a\u00060\u001ej\u0002`\u001fH\u0016J\b\u0010 \u001a\u00020\u0006H\u0002J!\u0010!\u001a\u00020\u00102\n\u0010\"\u001a\u0006\u0012\u0002\b\u00030#2\u0006\u0010\u0005\u001a\u00020\u0019H\u0000¢\u0006\u0002\b$J\r\u0010%\u001a\u00020\u0017H\u0000¢\u0006\u0002\b&J\u0015\u0010'\u001a\u00020\u00172\u0006\u0010(\u001a\u00020)H\u0000¢\u0006\u0002\b*J\b\u0010+\u001a\u00020\u0004H\u0016J\r\u0010\u000f\u001a\u00020\u0017H\u0000¢\u0006\u0002\b,R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0010\u0010\r\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006-"}, d2 = {"Lkotlinx/coroutines/CommonPool;", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "()V", "DEFAULT_PARALLELISM_PROPERTY_NAME", "", "executor", "Ljava/util/concurrent/Executor;", "getExecutor", "()Ljava/util/concurrent/Executor;", "parallelism", "", "getParallelism", "()I", "pool", "requestedParallelism", "usePrivatePool", "", "Try", ExifInterface.GPS_DIRECTION_TRUE, "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "close", "", "createPlainPool", "Ljava/util/concurrent/ExecutorService;", "createPool", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "getOrCreatePoolSync", "isGoodCommonPool", "fjpClass", "Ljava/lang/Class;", "isGoodCommonPool$kotlinx_coroutines_core", "restore", "restore$kotlinx_coroutines_core", "shutdown", "timeout", "", "shutdown$kotlinx_coroutines_core", "toString", "usePrivatePool$kotlinx_coroutines_core", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class CommonPool extends ExecutorCoroutineDispatcher {
    public static final String DEFAULT_PARALLELISM_PROPERTY_NAME = "kotlinx.coroutines.default.parallelism";
    public static final CommonPool INSTANCE = new CommonPool();
    private static volatile Executor pool;
    private static final int requestedParallelism;
    private static boolean usePrivatePool;

    static {
        String property;
        int i;
        CommonPool commonPool = INSTANCE;
        try {
            property = System.getProperty(DEFAULT_PARALLELISM_PROPERTY_NAME);
        } catch (Throwable th) {
            property = null;
        }
        if (property != null) {
            Integer parallelism = StringsKt.toIntOrNull(property);
            if (parallelism == null || parallelism.intValue() < 1) {
                throw new IllegalStateException(("Expected positive number in kotlinx.coroutines.default.parallelism, but has " + property).toString());
            }
            i = parallelism.intValue();
        } else {
            i = -1;
        }
        requestedParallelism = i;
    }

    private CommonPool() {
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher
    public Executor getExecutor() {
        Executor executor = pool;
        return executor != null ? executor : getOrCreatePoolSync();
    }

    private final int getParallelism() {
        Integer valueOf = Integer.valueOf(requestedParallelism);
        boolean z = false;
        if (valueOf.intValue() > 0) {
            z = true;
        }
        if (!z) {
            valueOf = null;
        }
        if (valueOf != null) {
            return valueOf.intValue();
        }
        return RangesKt.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 1, 1);
    }

    private final <T> T Try(Function0<? extends T> function0) {
        try {
            return (T) function0.invoke();
        } catch (Throwable th) {
            return null;
        }
    }

    private final ExecutorService createPool() {
        Class fjpClass;
        boolean z;
        ExecutorService it;
        ExecutorService it2;
        if (System.getSecurityManager() != null) {
            return createPlainPool();
        }
        try {
            fjpClass = Class.forName("java.util.concurrent.ForkJoinPool");
        } catch (Throwable th) {
            fjpClass = null;
        }
        if (fjpClass == null) {
            return createPlainPool();
        }
        if (usePrivatePool || requestedParallelism >= 0) {
            z = false;
        } else {
            try {
                Method method = fjpClass.getMethod("commonPool", new Class[0]);
                Object invoke = method != null ? method.invoke(null, new Object[0]) : null;
                if (!(invoke instanceof ExecutorService)) {
                    invoke = null;
                }
                it2 = (ExecutorService) invoke;
            } catch (Throwable th2) {
                it2 = null;
            }
            if (it2 != null) {
                z = false;
                if (!INSTANCE.isGoodCommonPool$kotlinx_coroutines_core(fjpClass, it2)) {
                    it2 = null;
                }
                if (it2 != null) {
                    return it2;
                }
            } else {
                z = false;
            }
        }
        try {
            Object newInstance = fjpClass.getConstructor(Integer.TYPE).newInstance(Integer.valueOf(INSTANCE.getParallelism()));
            if (!(newInstance instanceof ExecutorService)) {
                newInstance = null;
            }
            it = (ExecutorService) newInstance;
        } catch (Throwable th3) {
            it = null;
        }
        if (it != null) {
            return it;
        }
        return createPlainPool();
    }

    public final boolean isGoodCommonPool$kotlinx_coroutines_core(Class<?> cls, ExecutorService executor) {
        Intrinsics.checkParameterIsNotNull(cls, "fjpClass");
        Intrinsics.checkParameterIsNotNull(executor, "executor");
        executor.submit(CommonPool$isGoodCommonPool$1.INSTANCE);
        Integer num = null;
        try {
            Object invoke = cls.getMethod("getPoolSize", new Class[0]).invoke(executor, new Object[0]);
            if (!(invoke instanceof Integer)) {
                invoke = null;
            }
            num = (Integer) invoke;
        } catch (Throwable th) {
        }
        if (num == null || num.intValue() < 1) {
            return false;
        }
        return true;
    }

    private final ExecutorService createPlainPool() {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(getParallelism(), new ThreadFactory(new AtomicInteger()) { // from class: kotlinx.coroutines.CommonPool$createPlainPool$1
            final /* synthetic */ AtomicInteger $threadId;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$threadId = r1;
            }

            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable it) {
                Thread $receiver = new Thread(it, "CommonPool-worker-" + this.$threadId.incrementAndGet());
                $receiver.setDaemon(true);
                return $receiver;
            }
        });
        Intrinsics.checkExpressionValueIsNotNull(newFixedThreadPool, "Executors.newFixedThread…Daemon = true }\n        }");
        return newFixedThreadPool;
    }

    private final synchronized Executor getOrCreatePoolSync() {
        ExecutorService executorService;
        executorService = pool;
        if (executorService == null) {
            ExecutorService it = createPool();
            pool = it;
            executorService = it;
        }
        return executorService;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext context, Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        try {
            Executor executor = pool;
            if (executor == null) {
                executor = getOrCreatePoolSync();
            }
            executor.execute(TimeSourceKt.getTimeSource().wrapTask(block));
        } catch (RejectedExecutionException e) {
            TimeSourceKt.getTimeSource().unTrackTask();
            DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(block);
        }
    }

    public final synchronized void usePrivatePool$kotlinx_coroutines_core() {
        shutdown$kotlinx_coroutines_core(0);
        usePrivatePool = true;
        pool = null;
    }

    public final synchronized void shutdown$kotlinx_coroutines_core(long timeout) {
        Executor executor = pool;
        if (!(executor instanceof ExecutorService)) {
            executor = null;
        }
        ExecutorService $receiver = (ExecutorService) executor;
        if ($receiver != null) {
            $receiver.shutdown();
            if (timeout > 0) {
                $receiver.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            }
            Iterable<Runnable> $receiver$iv = $receiver.shutdownNow();
            Intrinsics.checkExpressionValueIsNotNull($receiver$iv, "shutdownNow()");
            for (Runnable it : $receiver$iv) {
                DefaultExecutor defaultExecutor = DefaultExecutor.INSTANCE;
                Intrinsics.checkExpressionValueIsNotNull(it, "it");
                defaultExecutor.execute$kotlinx_coroutines_core(it);
            }
        }
        pool = CommonPool$shutdown$2.INSTANCE;
    }

    public final synchronized void restore$kotlinx_coroutines_core() {
        shutdown$kotlinx_coroutines_core(0);
        usePrivatePool = false;
        pool = null;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
    public String toString() {
        return "CommonPool";
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new IllegalStateException("Close cannot be invoked on CommonPool".toString());
    }
}
