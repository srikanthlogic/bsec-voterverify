package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
abstract class InterruptibleTask<T> extends AtomicReference<Runnable> implements Runnable {
    private static final int MAX_BUSY_WAIT_SPINS = 1000;
    private static final Runnable DONE = new DoNothingRunnable();
    private static final Runnable INTERRUPTING = new DoNothingRunnable();
    private static final Runnable PARKED = new DoNothingRunnable();

    abstract void afterRanInterruptibly(@NullableDecl T t, @NullableDecl Throwable th);

    abstract boolean isDone();

    abstract T runInterruptibly() throws Exception;

    abstract String toPendingString();

    /* loaded from: classes3.dex */
    private static final class DoNothingRunnable implements Runnable {
        private DoNothingRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
        }
    }

    @Override // java.lang.Runnable
    public final void run() {
        Thread currentThread = Thread.currentThread();
        if (compareAndSet(null, currentThread)) {
            boolean run = !isDone();
            T result = null;
            Throwable error = null;
            if (run) {
                try {
                    result = runInterruptibly();
                } catch (Throwable t) {
                    error = t;
                    if (!compareAndSet(currentThread, DONE)) {
                        boolean restoreInterruptedBit = false;
                        int spinCount = 0;
                        Runnable state = get();
                        while (true) {
                            Runnable state2 = state;
                            if (state2 != INTERRUPTING && state2 != PARKED) {
                                break;
                            }
                            spinCount++;
                            if (spinCount > 1000) {
                                Runnable runnable = PARKED;
                                if (state2 == runnable || compareAndSet(INTERRUPTING, runnable)) {
                                    restoreInterruptedBit = Thread.interrupted() || restoreInterruptedBit;
                                    LockSupport.park(this);
                                }
                            } else {
                                Thread.yield();
                            }
                            state = get();
                        }
                        if (restoreInterruptedBit) {
                            currentThread.interrupt();
                        }
                    }
                    if (!run) {
                        return;
                    }
                }
            }
            if (!compareAndSet(currentThread, DONE)) {
                boolean restoreInterruptedBit2 = false;
                int spinCount2 = 0;
                Runnable state3 = get();
                while (true) {
                    Runnable state4 = state3;
                    if (state4 != INTERRUPTING && state4 != PARKED) {
                        break;
                    }
                    spinCount2++;
                    if (spinCount2 > 1000) {
                        Runnable runnable2 = PARKED;
                        if (state4 == runnable2 || compareAndSet(INTERRUPTING, runnable2)) {
                            restoreInterruptedBit2 = Thread.interrupted() || restoreInterruptedBit2;
                            LockSupport.park(this);
                        }
                    } else {
                        Thread.yield();
                    }
                    state3 = get();
                }
                if (restoreInterruptedBit2) {
                    currentThread.interrupt();
                }
            }
            if (!run) {
                return;
            }
            afterRanInterruptibly(result, error);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void interruptTask() {
        Runnable currentRunner = get();
        if ((currentRunner instanceof Thread) && compareAndSet(currentRunner, INTERRUPTING)) {
            try {
                ((Thread) currentRunner).interrupt();
            } finally {
                if (getAndSet(DONE) == PARKED) {
                    LockSupport.unpark((Thread) currentRunner);
                }
            }
        }
    }

    @Override // java.util.concurrent.atomic.AtomicReference, java.lang.Object
    public final String toString() {
        String result;
        Runnable state = get();
        if (state == DONE) {
            result = "running=[DONE]";
        } else if (state == INTERRUPTING) {
            result = "running=[INTERRUPTED]";
        } else if (state instanceof Thread) {
            result = "running=[RUNNING ON " + ((Thread) state).getName() + "]";
        } else {
            result = "running=[NOT STARTED YET]";
        }
        return result + ", " + toPendingString();
    }
}
