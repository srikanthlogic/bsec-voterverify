package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class Callables {
    private Callables() {
    }

    public static <T> Callable<T> returning(@NullableDecl final T value) {
        return new Callable<T>() { // from class: com.google.common.util.concurrent.Callables.1
            /* JADX WARN: Type inference failed for: r0v0, types: [T, java.lang.Object] */
            @Override // java.util.concurrent.Callable
            public T call() {
                return value;
            }
        };
    }

    public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> callable, final ListeningExecutorService listeningExecutorService) {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(listeningExecutorService);
        return new AsyncCallable<T>() { // from class: com.google.common.util.concurrent.Callables.2
            @Override // com.google.common.util.concurrent.AsyncCallable
            public ListenableFuture<T> call() throws Exception {
                return ListeningExecutorService.this.submit(callable);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(callable);
        return new Callable<T>() { // from class: com.google.common.util.concurrent.Callables.3
            /* JADX WARN: Type inference failed for: r3v2, types: [T, java.lang.Object] */
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                Thread currentThread = Thread.currentThread();
                String oldName = currentThread.getName();
                boolean restoreName = Callables.trySetName((String) Supplier.this.get(), currentThread);
                try {
                    return callable.call();
                } finally {
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(task);
        return new Runnable() { // from class: com.google.common.util.concurrent.Callables.4
            @Override // java.lang.Runnable
            public void run() {
                Thread currentThread = Thread.currentThread();
                String oldName = currentThread.getName();
                boolean restoreName = Callables.trySetName((String) Supplier.this.get(), currentThread);
                try {
                    task.run();
                } finally {
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean trySetName(String threadName, Thread currentThread) {
        try {
            currentThread.setName(threadName);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }
}
