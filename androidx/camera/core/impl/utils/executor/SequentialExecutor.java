package androidx.camera.core.impl.utils.executor;

import androidx.core.util.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SequentialExecutor implements Executor {
    private static final String TAG = "SequentialExecutor";
    private final Executor mExecutor;
    final Deque<Runnable> mQueue = new ArrayDeque();
    private final QueueWorker mWorker = new QueueWorker();
    WorkerRunningState mWorkerRunningState = WorkerRunningState.IDLE;
    long mWorkerRunCount = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum WorkerRunningState {
        IDLE,
        QUEUING,
        QUEUED,
        RUNNING
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SequentialExecutor(Executor executor) {
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
    }

    @Override // java.util.concurrent.Executor
    public void execute(final Runnable task) {
        Preconditions.checkNotNull(task);
        synchronized (this.mQueue) {
            if (!(this.mWorkerRunningState == WorkerRunningState.RUNNING || this.mWorkerRunningState == WorkerRunningState.QUEUED)) {
                long oldRunCount = this.mWorkerRunCount;
                Runnable submittedTask = new Runnable() { // from class: androidx.camera.core.impl.utils.executor.SequentialExecutor.1
                    @Override // java.lang.Runnable
                    public void run() {
                        task.run();
                    }
                };
                this.mQueue.add(submittedTask);
                this.mWorkerRunningState = WorkerRunningState.QUEUING;
                boolean alreadyMarkedQueued = true;
                try {
                    this.mExecutor.execute(this.mWorker);
                    if (this.mWorkerRunningState == WorkerRunningState.QUEUING) {
                        alreadyMarkedQueued = false;
                    }
                    if (!alreadyMarkedQueued) {
                        synchronized (this.mQueue) {
                            if (this.mWorkerRunCount == oldRunCount && this.mWorkerRunningState == WorkerRunningState.QUEUING) {
                                this.mWorkerRunningState = WorkerRunningState.QUEUED;
                            }
                        }
                        return;
                    }
                    return;
                } catch (Error | RuntimeException t) {
                    synchronized (this.mQueue) {
                        if ((this.mWorkerRunningState != WorkerRunningState.IDLE && this.mWorkerRunningState != WorkerRunningState.QUEUING) || !this.mQueue.removeLastOccurrence(submittedTask)) {
                            alreadyMarkedQueued = false;
                        }
                        if (!(t instanceof RejectedExecutionException) || alreadyMarkedQueued) {
                            throw t;
                        }
                    }
                    return;
                }
            }
            this.mQueue.add(task);
        }
    }

    /* loaded from: classes.dex */
    final class QueueWorker implements Runnable {
        QueueWorker() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                workOnQueue();
            } catch (Error e) {
                synchronized (SequentialExecutor.this.mQueue) {
                    SequentialExecutor.this.mWorkerRunningState = WorkerRunningState.IDLE;
                    throw e;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x003f, code lost:
            if (r0 == false) goto L_?;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0041, code lost:
            java.lang.Thread.currentThread().interrupt();
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0048, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x004e, code lost:
            r0 = r0 | java.lang.Thread.interrupted();
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x004f, code lost:
            r3.run();
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0053, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0054, code lost:
            android.util.Log.e(androidx.camera.core.impl.utils.executor.SequentialExecutor.TAG, "Exception while executing runnable " + r3, r2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump */
        private void workOnQueue() {
            boolean interruptedDuringTask = false;
            boolean hasSetRunning = false;
            while (true) {
                try {
                    synchronized (SequentialExecutor.this.mQueue) {
                        if (!hasSetRunning) {
                            if (SequentialExecutor.this.mWorkerRunningState != WorkerRunningState.RUNNING) {
                                SequentialExecutor.this.mWorkerRunCount++;
                                SequentialExecutor.this.mWorkerRunningState = WorkerRunningState.RUNNING;
                                hasSetRunning = true;
                            }
                        }
                        Runnable task = SequentialExecutor.this.mQueue.poll();
                        if (task == null) {
                            SequentialExecutor.this.mWorkerRunningState = WorkerRunningState.IDLE;
                        }
                    }
                    if (!interruptedDuringTask) {
                        return;
                    }
                    return;
                } finally {
                    if (interruptedDuringTask) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
