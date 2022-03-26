package com.google.common.util.concurrent;

import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class ListenerCallQueue<L> {
    private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
    private final List<PerListenerQueue<L>> listeners = Collections.synchronizedList(new ArrayList());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public interface Event<L> {
        void call(L l);
    }

    public void addListener(L listener, Executor executor) {
        Preconditions.checkNotNull(listener, ServiceSpecificExtraArgs.CastExtraArgs.LISTENER);
        Preconditions.checkNotNull(executor, "executor");
        this.listeners.add(new PerListenerQueue<>(listener, executor));
    }

    public void enqueue(Event<L> event) {
        enqueueHelper(event, event);
    }

    public void enqueue(Event<L> event, String label) {
        enqueueHelper(event, label);
    }

    private void enqueueHelper(Event<L> event, Object label) {
        Preconditions.checkNotNull(event, NotificationCompat.CATEGORY_EVENT);
        Preconditions.checkNotNull(label, "label");
        synchronized (this.listeners) {
            for (PerListenerQueue<L> queue : this.listeners) {
                queue.add(event, label);
            }
        }
    }

    public void dispatch() {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).dispatch();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class PerListenerQueue<L> implements Runnable {
        final Executor executor;
        boolean isThreadScheduled;
        final L listener;
        final Queue<Event<L>> waitQueue = Queues.newArrayDeque();
        final Queue<Object> labelQueue = Queues.newArrayDeque();

        PerListenerQueue(L listener, Executor executor) {
            this.listener = (L) Preconditions.checkNotNull(listener);
            this.executor = (Executor) Preconditions.checkNotNull(executor);
        }

        synchronized void add(Event<L> event, Object label) {
            this.waitQueue.add(event);
            this.labelQueue.add(label);
        }

        void dispatch() {
            boolean scheduleEventRunner = false;
            synchronized (this) {
                if (!this.isThreadScheduled) {
                    this.isThreadScheduled = true;
                    scheduleEventRunner = true;
                }
            }
            if (scheduleEventRunner) {
                try {
                    this.executor.execute(this);
                } catch (RuntimeException e) {
                    synchronized (this) {
                        this.isThreadScheduled = false;
                        Logger logger = ListenerCallQueue.logger;
                        Level level = Level.SEVERE;
                        logger.log(level, "Exception while running callbacks for " + this.listener + " on " + this.executor, (Throwable) e);
                        throw e;
                    }
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0028, code lost:
            r2.call(r9.listener);
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x002e, code lost:
            r4 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x002f, code lost:
            r5 = com.google.common.util.concurrent.ListenerCallQueue.logger;
            r6 = java.util.logging.Level.SEVERE;
            r5.log(r6, "Exception while executing callback: " + r9.listener + " " + r3, (java.lang.Throwable) r4);
         */
        /* JADX WARN: Removed duplicated region for block: B:10:0x001e  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0026 A[ORIG_RETURN, RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:24:0x005a  */
        @Override // java.lang.Runnable
        /* Code decompiled incorrectly, please refer to instructions dump */
        public void run() {
            boolean stillRunning = true;
            while (true) {
                try {
                } catch (Throwable th) {
                    if (stillRunning) {
                    }
                    throw th;
                }
                synchronized (this) {
                    Preconditions.checkState(this.isThreadScheduled);
                    Event<L> nextToRun = this.waitQueue.poll();
                    Object nextLabel = this.labelQueue.poll();
                    if (nextToRun == null) {
                        break;
                    }
                    if (stillRunning) {
                        synchronized (this) {
                            try {
                                this.isThreadScheduled = false;
                            } catch (Throwable th2) {
                                throw th2;
                            }
                        }
                    }
                    throw th;
                }
                if (0 == 0) {
                    synchronized (this) {
                        try {
                            this.isThreadScheduled = false;
                        } catch (Throwable th3) {
                            throw th3;
                        }
                    }
                    return;
                }
                return;
            }
            this.isThreadScheduled = false;
            stillRunning = false;
            if (0 == 0) {
            }
        }
    }
}
