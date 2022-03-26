package com.google.firebase.crashlytics.internal.common;

import com.google.firebase.crashlytics.internal.Logger;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: classes3.dex */
public final class ExecutorUtils {
    private static final long DEFAULT_TERMINATION_TIMEOUT = 2;

    private ExecutorUtils() {
    }

    public static ExecutorService buildSingleThreadExecutorService(String name) {
        ExecutorService executor = newSingleThreadExecutor(getNamedThreadFactory(name), new ThreadPoolExecutor.DiscardPolicy());
        addDelayedShutdownHook(name, executor);
        return executor;
    }

    public static ScheduledExecutorService buildSingleThreadScheduledExecutorService(String name) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(getNamedThreadFactory(name));
        addDelayedShutdownHook(name, executor);
        return executor;
    }

    public static ThreadFactory getNamedThreadFactory(final String threadNameTemplate) {
        final AtomicLong count = new AtomicLong(1);
        return new ThreadFactory() { // from class: com.google.firebase.crashlytics.internal.common.ExecutorUtils.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(final Runnable runnable) {
                Thread thread = Executors.defaultThreadFactory().newThread(new BackgroundPriorityRunnable() { // from class: com.google.firebase.crashlytics.internal.common.ExecutorUtils.1.1
                    @Override // com.google.firebase.crashlytics.internal.common.BackgroundPriorityRunnable
                    public void onRun() {
                        runnable.run();
                    }
                });
                thread.setName(threadNameTemplate + count.getAndIncrement());
                return thread;
            }
        };
    }

    private static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        return Executors.unconfigurableExecutorService(new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), threadFactory, rejectedExecutionHandler));
    }

    private static void addDelayedShutdownHook(String serviceName, ExecutorService service) {
        addDelayedShutdownHook(serviceName, service, 2, TimeUnit.SECONDS);
    }

    private static void addDelayedShutdownHook(final String serviceName, final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
        Runtime runtime = Runtime.getRuntime();
        AnonymousClass2 r8 = new BackgroundPriorityRunnable() { // from class: com.google.firebase.crashlytics.internal.common.ExecutorUtils.2
            @Override // com.google.firebase.crashlytics.internal.common.BackgroundPriorityRunnable
            public void onRun() {
                try {
                    Logger logger = Logger.getLogger();
                    logger.d("Executing shutdown hook for " + serviceName);
                    service.shutdown();
                    if (!service.awaitTermination(terminationTimeout, timeUnit)) {
                        Logger logger2 = Logger.getLogger();
                        logger2.d(serviceName + " did not shut down in the allocated time. Requesting immediate shutdown.");
                        service.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    Logger.getLogger().d(String.format(Locale.US, "Interrupted while waiting for %s to shut down. Requesting immediate shutdown.", serviceName));
                    service.shutdownNow();
                }
            }
        };
        runtime.addShutdownHook(new Thread(r8, "Crashlytics Shutdown Hook for " + serviceName));
    }
}
