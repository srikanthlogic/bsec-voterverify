package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class SequentialExecutor implements Executor {
    private static final Logger log = Logger.getLogger(SequentialExecutor.class.getName());
    private final Executor executor;
    private final Deque<Runnable> queue = new ArrayDeque();
    private WorkerRunningState workerRunningState = WorkerRunningState.IDLE;
    private long workerRunCount = 0;
    private final QueueWorker worker = new QueueWorker();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public enum WorkerRunningState {
        IDLE,
        QUEUING,
        QUEUED,
        RUNNING
    }

    static /* synthetic */ long access$308(SequentialExecutor x0) {
        long j = x0.workerRunCount;
        x0.workerRunCount = 1 + j;
        return j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SequentialExecutor(Executor executor) {
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    @Override // java.util.concurrent.Executor
    public void execute(final Runnable task) {
        Preconditions.checkNotNull(task);
        synchronized (this.queue) {
            if (!(this.workerRunningState == WorkerRunningState.RUNNING || this.workerRunningState == WorkerRunningState.QUEUED)) {
                long oldRunCount = this.workerRunCount;
                Runnable submittedTask = new Runnable() { // from class: com.google.common.util.concurrent.SequentialExecutor.1
                    @Override // java.lang.Runnable
                    public void run() {
                        task.run();
                    }
                };
                this.queue.add(submittedTask);
                this.workerRunningState = WorkerRunningState.QUEUING;
                boolean alreadyMarkedQueued = true;
                try {
                    this.executor.execute(this.worker);
                    if (this.workerRunningState == WorkerRunningState.QUEUING) {
                        alreadyMarkedQueued = false;
                    }
                    if (!alreadyMarkedQueued) {
                        synchronized (this.queue) {
                            if (this.workerRunCount == oldRunCount && this.workerRunningState == WorkerRunningState.QUEUING) {
                                this.workerRunningState = WorkerRunningState.QUEUED;
                            }
                        }
                        return;
                    }
                    return;
                } catch (Error | RuntimeException t) {
                    synchronized (this.queue) {
                        if ((this.workerRunningState != WorkerRunningState.IDLE && this.workerRunningState != WorkerRunningState.QUEUING) || !this.queue.removeLastOccurrence(submittedTask)) {
                            alreadyMarkedQueued = false;
                        }
                        if (!(t instanceof RejectedExecutionException) || alreadyMarkedQueued) {
                            throw t;
                        }
                    }
                    return;
                }
            }
            this.queue.add(task);
        }
    }

    /* loaded from: classes3.dex */
    private final class QueueWorker implements Runnable {
        private QueueWorker() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                workOnQueue();
            } catch (Error e) {
                synchronized (SequentialExecutor.this.queue) {
                    SequentialExecutor.this.workerRunningState = WorkerRunningState.IDLE;
                    throw e;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x0043, code lost:
            if (r0 == false) goto L_?;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0045, code lost:
            java.lang.Thread.currentThread().interrupt();
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x004c, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x0052, code lost:
            r0 = r0 | java.lang.Thread.interrupted();
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0053, code lost:
            r3.run();
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0057, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0058, code lost:
            r4 = com.google.common.util.concurrent.SequentialExecutor.log;
            r5 = java.util.logging.Level.SEVERE;
            r4.log(r5, "Exception while executing runnable " + r3, (java.lang.Throwable) r2);
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
                    synchronized (SequentialExecutor.this.queue) {
                        if (!hasSetRunning) {
                            if (SequentialExecutor.this.workerRunningState != WorkerRunningState.RUNNING) {
                                SequentialExecutor.access$308(SequentialExecutor.this);
                                SequentialExecutor.this.workerRunningState = WorkerRunningState.RUNNING;
                                hasSetRunning = true;
                            }
                        }
                        Runnable task = (Runnable) SequentialExecutor.this.queue.poll();
                        if (task == null) {
                            SequentialExecutor.this.workerRunningState = WorkerRunningState.IDLE;
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

    @Override // java.lang.Object
    public String toString() {
        return "SequentialExecutor@" + System.identityHashCode(this) + "{" + this.executor + "}";
    }
}
