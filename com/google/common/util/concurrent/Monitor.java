package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class Monitor {
    private Guard activeGuards;
    private final boolean fair;
    private final ReentrantLock lock;

    /* loaded from: classes3.dex */
    public static abstract class Guard {
        final Condition condition;
        final Monitor monitor;
        @NullableDecl
        Guard next;
        int waiterCount = 0;

        public abstract boolean isSatisfied();

        /* JADX INFO: Access modifiers changed from: protected */
        public Guard(Monitor monitor) {
            this.monitor = (Monitor) Preconditions.checkNotNull(monitor, "monitor");
            this.condition = monitor.lock.newCondition();
        }
    }

    public Monitor() {
        this(false);
    }

    public Monitor(boolean fair) {
        this.activeGuards = null;
        this.fair = fair;
        this.lock = new ReentrantLock(fair);
    }

    public void enter() {
        this.lock.lock();
    }

    public boolean enter(long time, TimeUnit unit) {
        long timeoutNanos = toSafeNanos(time, unit);
        ReentrantLock lock = this.lock;
        if (!this.fair && lock.tryLock()) {
            return true;
        }
        boolean interrupted = Thread.interrupted();
        try {
            long remainingNanos = timeoutNanos;
            while (true) {
                try {
                    break;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = remainingNanos(System.nanoTime(), timeoutNanos);
                }
            }
            return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    public void enterWhen(Guard guard) throws InterruptedException {
        if (guard.monitor == this) {
            ReentrantLock lock = this.lock;
            boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
            lock.lockInterruptibly();
            boolean satisfied = false;
            try {
                if (!guard.isSatisfied()) {
                    await(guard, signalBeforeWaiting);
                }
                satisfied = true;
            } finally {
                if (!satisfied) {
                    leave();
                }
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x004c, code lost:
        if (awaitNanos(r12, r9, r3) != false) goto L_0x004e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001f, code lost:
        if (r2.tryLock() != false) goto L_0x0033;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        long j;
        long timeoutNanos = toSafeNanos(time, unit);
        if (guard.monitor == this) {
            ReentrantLock lock = this.lock;
            boolean reentrant = lock.isHeldByCurrentThread();
            long startTime = 0;
            boolean satisfied = false;
            if (!this.fair) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
            startTime = initNanoTime(timeoutNanos);
            if (!lock.tryLock(time, unit)) {
                return false;
            }
            try {
                if (!guard.isSatisfied()) {
                    if (startTime == 0) {
                        j = timeoutNanos;
                    } else {
                        j = remainingNanos(startTime, timeoutNanos);
                    }
                }
                satisfied = true;
                if (!satisfied) {
                    if (0 != 0 && !reentrant) {
                        try {
                            signalNextWaiter();
                        } catch (Throwable th) {
                            lock.unlock();
                            throw th;
                        }
                    }
                    lock.unlock();
                }
                return satisfied;
            } catch (Throwable th2) {
                if (0 == 0) {
                    if (1 != 0 && !reentrant) {
                        try {
                            signalNextWaiter();
                        } catch (Throwable th3) {
                            lock.unlock();
                            throw th3;
                        }
                    }
                    lock.unlock();
                }
                throw th2;
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public void enterWhenUninterruptibly(Guard guard) {
        if (guard.monitor == this) {
            ReentrantLock lock = this.lock;
            boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
            lock.lock();
            boolean satisfied = false;
            try {
                if (!guard.isSatisfied()) {
                    awaitUninterruptibly(guard, signalBeforeWaiting);
                }
                satisfied = true;
            } finally {
                if (!satisfied) {
                    leave();
                }
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
        boolean satisfied;
        long remainingNanos;
        long timeoutNanos = toSafeNanos(time, unit);
        if (guard.monitor == this) {
            ReentrantLock lock = this.lock;
            long startTime = 0;
            boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
            boolean interrupted = Thread.interrupted();
            try {
                if (this.fair || !lock.tryLock()) {
                    startTime = initNanoTime(timeoutNanos);
                    long remainingNanos2 = timeoutNanos;
                    while (true) {
                        try {
                            break;
                        } catch (InterruptedException e) {
                            interrupted = true;
                            remainingNanos2 = remainingNanos(startTime, timeoutNanos);
                        }
                    }
                    if (!lock.tryLock(remainingNanos2, TimeUnit.NANOSECONDS)) {
                        if (interrupted) {
                            Thread.currentThread().interrupt();
                        }
                        return false;
                    }
                }
                while (true) {
                    try {
                        if (!guard.isSatisfied()) {
                            if (startTime == 0) {
                                startTime = initNanoTime(timeoutNanos);
                                remainingNanos = timeoutNanos;
                            } else {
                                remainingNanos = remainingNanos(startTime, timeoutNanos);
                            }
                            satisfied = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
                            break;
                        }
                        satisfied = true;
                        break;
                    } catch (InterruptedException e2) {
                        interrupted = true;
                        signalBeforeWaiting = false;
                    } catch (Throwable th) {
                        if (0 == 0) {
                            lock.unlock();
                        }
                        throw th;
                    }
                }
                if (!satisfied) {
                    lock.unlock();
                }
                return satisfied;
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean enterIf(Guard guard) {
        if (guard.monitor == this) {
            ReentrantLock lock = this.lock;
            lock.lock();
            boolean satisfied = false;
            try {
                satisfied = guard.isSatisfied();
                return satisfied;
            } finally {
                if (!satisfied) {
                    lock.unlock();
                }
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean enterIf(Guard guard, long time, TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (!enter(time, unit)) {
            return false;
        } else {
            boolean satisfied = false;
            try {
                satisfied = guard.isSatisfied();
                return satisfied;
            } finally {
                if (!satisfied) {
                    this.lock.unlock();
                }
            }
        }
    }

    public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
        if (guard.monitor == this) {
            ReentrantLock lock = this.lock;
            lock.lockInterruptibly();
            boolean satisfied = false;
            try {
                satisfied = guard.isSatisfied();
                return satisfied;
            } finally {
                if (!satisfied) {
                    lock.unlock();
                }
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        if (guard.monitor == this) {
            ReentrantLock lock = this.lock;
            if (!lock.tryLock(time, unit)) {
                return false;
            }
            boolean satisfied = false;
            try {
                satisfied = guard.isSatisfied();
                return satisfied;
            } finally {
                if (!satisfied) {
                    lock.unlock();
                }
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean tryEnterIf(Guard guard) {
        if (guard.monitor == this) {
            ReentrantLock lock = this.lock;
            if (!lock.tryLock()) {
                return false;
            }
            boolean satisfied = false;
            try {
                satisfied = guard.isSatisfied();
                return satisfied;
            } finally {
                if (!satisfied) {
                    lock.unlock();
                }
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public void waitFor(Guard guard) throws InterruptedException {
        if (!(guard.monitor == this) || !this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        } else if (!guard.isSatisfied()) {
            await(guard, true);
        }
    }

    public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        long timeoutNanos = toSafeNanos(time, unit);
        if (!(guard.monitor == this) || !this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        } else if (guard.isSatisfied()) {
            return true;
        } else {
            if (!Thread.interrupted()) {
                return awaitNanos(guard, timeoutNanos, true);
            }
            throw new InterruptedException();
        }
    }

    public void waitForUninterruptibly(Guard guard) {
        if (!(guard.monitor == this) || !this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        } else if (!guard.isSatisfied()) {
            awaitUninterruptibly(guard, true);
        }
    }

    public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
        long timeoutNanos = toSafeNanos(time, unit);
        if (!(guard.monitor == this) || !this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        } else if (guard.isSatisfied()) {
            return true;
        } else {
            long startTime = initNanoTime(timeoutNanos);
            long remainingNanos = timeoutNanos;
            boolean interrupted = Thread.interrupted();
            boolean signalBeforeWaiting = true;
            while (true) {
                try {
                    try {
                        boolean signalBeforeWaiting2 = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
                        if (interrupted) {
                            Thread.currentThread().interrupt();
                        }
                        return signalBeforeWaiting2;
                    } catch (InterruptedException e) {
                        interrupted = true;
                        if (guard.isSatisfied()) {
                            if (1 != 0) {
                                Thread.currentThread().interrupt();
                            }
                            return true;
                        }
                        signalBeforeWaiting = false;
                        remainingNanos = remainingNanos(startTime, timeoutNanos);
                    }
                } catch (Throwable th) {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                    throw th;
                }
            }
        }
    }

    public void leave() {
        ReentrantLock lock = this.lock;
        try {
            if (lock.getHoldCount() == 1) {
                signalNextWaiter();
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isFair() {
        return this.fair;
    }

    public boolean isOccupied() {
        return this.lock.isLocked();
    }

    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }

    public int getQueueLength() {
        return this.lock.getQueueLength();
    }

    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }

    public boolean hasQueuedThread(Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }

    public boolean hasWaiters(Guard guard) {
        return getWaitQueueLength(guard) > 0;
    }

    public int getWaitQueueLength(Guard guard) {
        if (guard.monitor == this) {
            this.lock.lock();
            try {
                return guard.waiterCount;
            } finally {
                this.lock.unlock();
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    private static long toSafeNanos(long time, TimeUnit unit) {
        return Longs.constrainToRange(unit.toNanos(time), 0, 6917529027641081853L);
    }

    private static long initNanoTime(long timeoutNanos) {
        if (timeoutNanos <= 0) {
            return 0;
        }
        long startTime = System.nanoTime();
        if (startTime == 0) {
            return 1;
        }
        return startTime;
    }

    private static long remainingNanos(long startTime, long timeoutNanos) {
        if (timeoutNanos <= 0) {
            return 0;
        }
        return timeoutNanos - (System.nanoTime() - startTime);
    }

    private void signalNextWaiter() {
        for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
            if (isSatisfied(guard)) {
                guard.condition.signal();
                return;
            }
        }
    }

    private boolean isSatisfied(Guard guard) {
        try {
            return guard.isSatisfied();
        } catch (Throwable throwable) {
            signalAllWaiters();
            throw throwable;
        }
    }

    private void signalAllWaiters() {
        for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
            guard.condition.signalAll();
        }
    }

    private void beginWaitingFor(Guard guard) {
        int waiters = guard.waiterCount;
        guard.waiterCount = waiters + 1;
        if (waiters == 0) {
            guard.next = this.activeGuards;
            this.activeGuards = guard;
        }
    }

    private void endWaitingFor(Guard guard) {
        int waiters = guard.waiterCount - 1;
        guard.waiterCount = waiters;
        if (waiters == 0) {
            Guard p = this.activeGuards;
            Guard pred = null;
            while (p != guard) {
                pred = p;
                p = p.next;
            }
            if (pred == null) {
                this.activeGuards = p.next;
            } else {
                pred.next = p.next;
            }
            p.next = null;
        }
    }

    private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        do {
            try {
                guard.condition.await();
            } finally {
                endWaitingFor(guard);
            }
        } while (!guard.isSatisfied());
    }

    private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        do {
            try {
                guard.condition.awaitUninterruptibly();
            } finally {
                endWaitingFor(guard);
            }
        } while (!guard.isSatisfied());
    }

    private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
        boolean firstTime = true;
        while (nanos > 0) {
            if (firstTime) {
                if (signalBeforeWaiting) {
                    try {
                        signalNextWaiter();
                    } finally {
                        if (!firstTime) {
                            endWaitingFor(guard);
                        }
                    }
                }
                beginWaitingFor(guard);
                firstTime = false;
            }
            nanos = guard.condition.awaitNanos(nanos);
            if (guard.isSatisfied()) {
                if (!firstTime) {
                    endWaitingFor(guard);
                }
                return true;
            }
        }
        return false;
    }
}
