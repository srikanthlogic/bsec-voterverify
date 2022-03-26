package com.google.firebase.crashlytics.internal.common;

import android.os.Looper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes3.dex */
public final class Utils {
    private static final ExecutorService TASK_CONTINUATION_EXECUTOR_SERVICE = ExecutorUtils.buildSingleThreadExecutorService("awaitEvenIfOnMainThread task continuation executor");

    private Utils() {
    }

    public static <T> Task<T> race(Task<T> t1, Task<T> t2) {
        final TaskCompletionSource<T> result = new TaskCompletionSource<>();
        AnonymousClass1 r1 = new Continuation<T, Void>() { // from class: com.google.firebase.crashlytics.internal.common.Utils.1
            @Override // com.google.android.gms.tasks.Continuation
            public Void then(Task<T> task) throws Exception {
                if (task.isSuccessful()) {
                    result.trySetResult(task.getResult());
                    return null;
                }
                result.trySetException(task.getException());
                return null;
            }
        };
        t1.continueWith(r1);
        t2.continueWith(r1);
        return result.getTask();
    }

    public static <T> Task<T> callTask(Executor executor, final Callable<Task<T>> callable) {
        final TaskCompletionSource<T> tcs = new TaskCompletionSource<>();
        executor.execute(new Runnable() { // from class: com.google.firebase.crashlytics.internal.common.Utils.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ((Task) callable.call()).continueWith(new Continuation<T, Void>() { // from class: com.google.firebase.crashlytics.internal.common.Utils.2.1
                        @Override // com.google.android.gms.tasks.Continuation
                        public Void then(Task<T> task) throws Exception {
                            if (task.isSuccessful()) {
                                tcs.setResult(task.getResult());
                                return null;
                            }
                            tcs.setException(task.getException());
                            return null;
                        }
                    });
                } catch (Exception e) {
                    tcs.setException(e);
                }
            }
        });
        return tcs.getTask();
    }

    public static <T> T awaitEvenIfOnMainThread(Task<T> task) throws InterruptedException, TimeoutException {
        CountDownLatch latch = new CountDownLatch(1);
        task.continueWith(TASK_CONTINUATION_EXECUTOR_SERVICE, new Continuation(latch) { // from class: com.google.firebase.crashlytics.internal.common.-$$Lambda$Utils$ReojOYyqQI6_HWJdWAeNhkimk4Y
            private final /* synthetic */ CountDownLatch f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.android.gms.tasks.Continuation
            public final Object then(Task task2) {
                return this.f$0.countDown();
            }
        });
        if (Looper.getMainLooper() == Looper.myLooper()) {
            latch.await(4, TimeUnit.SECONDS);
        } else {
            latch.await();
        }
        if (task.isSuccessful()) {
            return task.getResult();
        }
        if (task.isCanceled()) {
            throw new CancellationException("Task is already canceled");
        } else if (task.isComplete()) {
            throw new IllegalStateException(task.getException());
        } else {
            throw new TimeoutException();
        }
    }
}
