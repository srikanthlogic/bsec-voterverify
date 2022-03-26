package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import sun.misc.Unsafe;
/* loaded from: classes3.dex */
public abstract class AbstractFuture<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final boolean GENERATE_CANCELLATION_CAUSES;
    private static final Object NULL;
    private static final long SPIN_THRESHOLD_NANOS;
    private static final Logger log;
    @NullableDecl
    private volatile Listener listeners;
    @NullableDecl
    private volatile Object value;
    @NullableDecl
    private volatile Waiter waiters;

    /* loaded from: classes3.dex */
    public interface Trusted<V> extends ListenableFuture<V> {
    }

    static {
        boolean generateCancellationCauses;
        AtomicHelper helper;
        try {
            generateCancellationCauses = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        } catch (SecurityException e) {
            generateCancellationCauses = false;
        }
        GENERATE_CANCELLATION_CAUSES = generateCancellationCauses;
        log = Logger.getLogger(AbstractFuture.class.getName());
        Throwable thrownUnsafeFailure = null;
        Throwable thrownAtomicReferenceFieldUpdaterFailure = null;
        try {
            helper = new UnsafeAtomicHelper();
        } catch (Throwable unsafeFailure) {
            thrownUnsafeFailure = unsafeFailure;
            try {
                helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
            } catch (Throwable atomicReferenceFieldUpdaterFailure) {
                thrownAtomicReferenceFieldUpdaterFailure = atomicReferenceFieldUpdaterFailure;
                helper = new SynchronizedHelper();
            }
        }
        ATOMIC_HELPER = helper;
        if (thrownAtomicReferenceFieldUpdaterFailure != null) {
            log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
        }
        NULL = new Object();
    }

    /* loaded from: classes3.dex */
    public static abstract class TrustedFuture<V> extends AbstractFuture<V> implements Trusted<V> {
        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final V get() throws InterruptedException, ExecutionException {
            return (V) AbstractFuture.super.get();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return (V) AbstractFuture.super.get(timeout, unit);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isDone() {
            return AbstractFuture.super.isDone();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isCancelled() {
            return AbstractFuture.super.isCancelled();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable listener, Executor executor) {
            AbstractFuture.super.addListener(listener, executor);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean cancel(boolean mayInterruptIfRunning) {
            return AbstractFuture.super.cancel(mayInterruptIfRunning);
        }
    }

    /* loaded from: classes3.dex */
    public static final class Waiter {
        static final Waiter TOMBSTONE = new Waiter(false);
        @NullableDecl
        volatile Waiter next;
        @NullableDecl
        volatile Thread thread;

        Waiter(boolean unused) {
        }

        Waiter() {
            AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }

        void setNext(Waiter next) {
            AbstractFuture.ATOMIC_HELPER.putNext(this, next);
        }

        void unpark() {
            Thread w = this.thread;
            if (w != null) {
                this.thread = null;
                LockSupport.unpark(w);
            }
        }
    }

    private void removeWaiter(Waiter node) {
        node.thread = null;
        while (true) {
            Waiter pred = null;
            Waiter curr = this.waiters;
            if (curr != Waiter.TOMBSTONE) {
                while (curr != null) {
                    Waiter succ = curr.next;
                    if (curr.thread != null) {
                        pred = curr;
                    } else if (pred != null) {
                        pred.next = succ;
                        if (pred.thread == null) {
                            break;
                        }
                    } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) {
                        break;
                    }
                    curr = succ;
                }
                return;
            }
            return;
        }
    }

    /* loaded from: classes3.dex */
    public static final class Listener {
        static final Listener TOMBSTONE = new Listener(null, null);
        final Executor executor;
        @NullableDecl
        Listener next;
        final Runnable task;

        Listener(Runnable task, Executor executor) {
            this.task = task;
            this.executor = executor;
        }
    }

    /* loaded from: classes3.dex */
    public static final class Failure {
        static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") { // from class: com.google.common.util.concurrent.AbstractFuture.Failure.1
            @Override // java.lang.Throwable
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        });
        final Throwable exception;

        Failure(Throwable exception) {
            this.exception = (Throwable) Preconditions.checkNotNull(exception);
        }
    }

    /* loaded from: classes3.dex */
    public static final class Cancellation {
        static final Cancellation CAUSELESS_CANCELLED;
        static final Cancellation CAUSELESS_INTERRUPTED;
        @NullableDecl
        final Throwable cause;
        final boolean wasInterrupted;

        static {
            if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
                CAUSELESS_CANCELLED = null;
                CAUSELESS_INTERRUPTED = null;
                return;
            }
            CAUSELESS_CANCELLED = new Cancellation(false, null);
            CAUSELESS_INTERRUPTED = new Cancellation(true, null);
        }

        Cancellation(boolean wasInterrupted, @NullableDecl Throwable cause) {
            this.wasInterrupted = wasInterrupted;
            this.cause = cause;
        }
    }

    /* loaded from: classes3.dex */
    public static final class SetFuture<V> implements Runnable {
        final ListenableFuture<? extends V> future;
        final AbstractFuture<V> owner;

        SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
            this.owner = owner;
            this.future = future;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (((AbstractFuture) this.owner).value == this) {
                if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, AbstractFuture.getFutureValue(this.future))) {
                    AbstractFuture.complete(this.owner);
                }
            }
        }
    }

    /* JADX INFO: Multiple debug info for r6v3 long: [D('remainingNanos' long), D('overWaitLeftoverNanos' long)] */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x004d, code lost:
        java.util.concurrent.locks.LockSupport.parkNanos(r28, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0054, code lost:
        if (java.lang.Thread.interrupted() != false) goto L_0x007b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0056, code lost:
        r8 = r28.value;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0058, code lost:
        if (r8 == null) goto L_0x005c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x005a, code lost:
        r12 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x005c, code lost:
        r12 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0061, code lost:
        if ((r12 & (!(r8 instanceof com.google.common.util.concurrent.AbstractFuture.SetFuture))) == false) goto L_0x0068;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0067, code lost:
        return getDoneValue(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0068, code lost:
        r6 = r13 - java.lang.System.nanoTime();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0072, code lost:
        if (r6 >= com.google.common.util.concurrent.AbstractFuture.SPIN_THRESHOLD_NANOS) goto L_0x0078;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0074, code lost:
        removeWaiter(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x007b, code lost:
        removeWaiter(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0083, code lost:
        throw new java.lang.InterruptedException();
     */
    @Override // java.util.concurrent.Future
    /* Code decompiled incorrectly, please refer to instructions dump */
    public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        long remainingNanos = unit.toNanos(timeout);
        if (!Thread.interrupted()) {
            Object localValue = this.value;
            if ((localValue != null) && (!(localValue instanceof SetFuture))) {
                return getDoneValue(localValue);
            }
            long endNanos = remainingNanos > 0 ? System.nanoTime() + remainingNanos : 0;
            if (remainingNanos >= SPIN_THRESHOLD_NANOS) {
                Waiter oldHead = this.waiters;
                if (oldHead != Waiter.TOMBSTONE) {
                    Waiter node = new Waiter();
                    while (true) {
                        node.setNext(oldHead);
                        if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                            break;
                        }
                        oldHead = this.waiters;
                        if (oldHead == Waiter.TOMBSTONE) {
                            break;
                        }
                    }
                }
                return getDoneValue(this.value);
            }
            while (remainingNanos > 0) {
                Object localValue2 = this.value;
                if ((localValue2 != null) && (!(localValue2 instanceof SetFuture))) {
                    return getDoneValue(localValue2);
                }
                if (!Thread.interrupted()) {
                    remainingNanos = endNanos - System.nanoTime();
                } else {
                    throw new InterruptedException();
                }
            }
            String futureToString = toString();
            String unitString = unit.toString().toLowerCase(Locale.ROOT);
            String message = "Waited " + timeout + " " + unit.toString().toLowerCase(Locale.ROOT);
            if (remainingNanos + SPIN_THRESHOLD_NANOS < 0) {
                String message2 = message + " (plus ";
                long overWaitNanos = -remainingNanos;
                long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
                long overWaitLeftoverNanos = overWaitNanos - unit.toNanos(overWaitUnits);
                boolean shouldShowExtraNanos = overWaitUnits == 0 || overWaitLeftoverNanos > SPIN_THRESHOLD_NANOS;
                if (overWaitUnits > 0) {
                    String message3 = message2 + overWaitUnits + " " + unitString;
                    if (shouldShowExtraNanos) {
                        message3 = message3 + ",";
                    }
                    message2 = message3 + " ";
                }
                if (shouldShowExtraNanos) {
                    message2 = message2 + overWaitLeftoverNanos + " nanoseconds ";
                }
                message = message2 + "delay)";
            }
            if (isDone()) {
                throw new TimeoutException(message + " but future completed as timeout expired");
            }
            throw new TimeoutException(message + " for " + futureToString);
        }
        throw new InterruptedException();
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0030, code lost:
        java.util.concurrent.locks.LockSupport.park(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0037, code lost:
        if (java.lang.Thread.interrupted() != false) goto L_0x004b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
        r0 = r7.value;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x003b, code lost:
        if (r0 == null) goto L_0x003f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003d, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x003f, code lost:
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0044, code lost:
        if ((r5 & (!(r0 instanceof com.google.common.util.concurrent.AbstractFuture.SetFuture))) == false) goto L_0x0030;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x004a, code lost:
        return getDoneValue(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x004b, code lost:
        removeWaiter(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0053, code lost:
        throw new java.lang.InterruptedException();
     */
    @Override // java.util.concurrent.Future
    /* Code decompiled incorrectly, please refer to instructions dump */
    public V get() throws InterruptedException, ExecutionException {
        if (!Thread.interrupted()) {
            Object localValue = this.value;
            if ((localValue != null) && (!(localValue instanceof SetFuture))) {
                return getDoneValue(localValue);
            }
            Waiter oldHead = this.waiters;
            if (oldHead == Waiter.TOMBSTONE) {
                return getDoneValue(this.value);
            }
            Waiter node = new Waiter();
            do {
                node.setNext(oldHead);
                if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                    break;
                }
                oldHead = this.waiters;
            } while (oldHead != Waiter.TOMBSTONE);
            return getDoneValue(this.value);
        }
        throw new InterruptedException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private V getDoneValue(Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) obj).cause);
        } else if (obj instanceof Failure) {
            throw new ExecutionException(((Failure) obj).exception);
        } else if (obj == NULL) {
            return null;
        } else {
            return obj;
        }
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        Object localValue = this.value;
        return (true ^ (localValue instanceof SetFuture)) & (localValue != null);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.value instanceof Cancellation;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        Object localValue = this.value;
        boolean rValue = false;
        if ((localValue == null) || (localValue instanceof SetFuture)) {
            Object valueToSet = GENERATE_CANCELLATION_CAUSES ? new Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called.")) : mayInterruptIfRunning ? Cancellation.CAUSELESS_INTERRUPTED : Cancellation.CAUSELESS_CANCELLED;
            AbstractFuture<V> abstractFuture = this;
            while (true) {
                if (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
                    rValue = true;
                    if (mayInterruptIfRunning) {
                        abstractFuture.interruptTask();
                    }
                    complete(abstractFuture);
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                    ListenableFuture<?> futureToPropagateTo = ((SetFuture) localValue).future;
                    if (!(futureToPropagateTo instanceof Trusted)) {
                        futureToPropagateTo.cancel(mayInterruptIfRunning);
                        break;
                    }
                    AbstractFuture<V> abstractFuture2 = (AbstractFuture) futureToPropagateTo;
                    localValue = abstractFuture2.value;
                    if (!(localValue == null) && !(localValue instanceof SetFuture)) {
                        break;
                    }
                    abstractFuture = abstractFuture2;
                } else {
                    localValue = abstractFuture.value;
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                }
            }
        }
        return rValue;
    }

    protected void interruptTask() {
    }

    protected final boolean wasInterrupted() {
        Object localValue = this.value;
        return (localValue instanceof Cancellation) && ((Cancellation) localValue).wasInterrupted;
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable listener, Executor executor) {
        Listener oldHead;
        Preconditions.checkNotNull(listener, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        if (isDone() || (oldHead = this.listeners) == Listener.TOMBSTONE) {
            executeListener(listener, executor);
        }
        Listener newNode = new Listener(listener, executor);
        do {
            newNode.next = oldHead;
            if (!ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
                oldHead = this.listeners;
            } else {
                return;
            }
        } while (oldHead != Listener.TOMBSTONE);
        executeListener(listener, executor);
    }

    protected boolean set(@NullableDecl V value) {
        if (!ATOMIC_HELPER.casValue(this, null, value == null ? NULL : value)) {
            return false;
        }
        complete(this);
        return true;
    }

    public boolean setException(Throwable throwable) {
        if (!ATOMIC_HELPER.casValue(this, null, new Failure((Throwable) Preconditions.checkNotNull(throwable)))) {
            return false;
        }
        complete(this);
        return true;
    }

    public boolean setFuture(ListenableFuture<? extends V> future) {
        Failure failure;
        Preconditions.checkNotNull(future);
        Object localValue = this.value;
        if (localValue == null) {
            if (future.isDone()) {
                if (!ATOMIC_HELPER.casValue(this, null, getFutureValue(future))) {
                    return false;
                }
                complete(this);
                return true;
            }
            SetFuture valueToSet = new SetFuture(this, future);
            if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                try {
                    future.addListener(valueToSet, DirectExecutor.INSTANCE);
                } catch (Throwable t) {
                    try {
                        failure = new Failure(t);
                    } catch (Throwable th) {
                        failure = Failure.FALLBACK_INSTANCE;
                    }
                    ATOMIC_HELPER.casValue(this, valueToSet, failure);
                }
                return true;
            }
            localValue = this.value;
        }
        if (localValue instanceof Cancellation) {
            future.cancel(((Cancellation) localValue).wasInterrupted);
        }
        return false;
    }

    public static Object getFutureValue(ListenableFuture<?> future) {
        Throwable throwable;
        if (future instanceof Trusted) {
            Object v = ((AbstractFuture) future).value;
            if (!(v instanceof Cancellation)) {
                return v;
            }
            Cancellation c = (Cancellation) v;
            if (!c.wasInterrupted) {
                return v;
            }
            return c.cause != null ? new Cancellation(false, c.cause) : Cancellation.CAUSELESS_CANCELLED;
        } else if ((future instanceof InternalFutureFailureAccess) && (throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) future)) != null) {
            return new Failure(throwable);
        } else {
            boolean wasCancelled = future.isCancelled();
            if ((!GENERATE_CANCELLATION_CAUSES) && wasCancelled) {
                return Cancellation.CAUSELESS_CANCELLED;
            }
            try {
                Object v2 = getUninterruptibly(future);
                if (!wasCancelled) {
                    return v2 == null ? NULL : v2;
                }
                return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future));
            } catch (CancellationException cancellation) {
                if (wasCancelled) {
                    return new Cancellation(false, cancellation);
                }
                return new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + future, cancellation));
            } catch (ExecutionException exception) {
                if (!wasCancelled) {
                    return new Failure(exception.getCause());
                }
                return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future, exception));
            } catch (Throwable t) {
                return new Failure(t);
            }
        }
    }

    private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        V v;
        boolean interrupted = false;
        while (true) {
            try {
                v = future.get();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return v;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0, types: [com.google.common.util.concurrent.AbstractFuture$AtomicHelper] */
    /* JADX WARN: Type inference failed for: r6v0, types: [com.google.common.util.concurrent.AbstractFuture<?>] */
    /* JADX WARN: Type inference failed for: r6v1, types: [com.google.common.util.concurrent.AbstractFuture] */
    /* JADX WARN: Type inference failed for: r6v3, types: [com.google.common.util.concurrent.AbstractFuture<V>, com.google.common.util.concurrent.AbstractFuture] */
    public static void complete(AbstractFuture<?> abstractFuture) {
        Listener next = null;
        while (true) {
            abstractFuture.releaseWaiters();
            abstractFuture.afterDone();
            next = abstractFuture.clearListeners(next);
            while (next != null) {
                next = next.next;
                Runnable task = next.task;
                if (task instanceof SetFuture) {
                    SetFuture<?> setFuture = (SetFuture) task;
                    abstractFuture = (AbstractFuture<V>) setFuture.owner;
                    if (((AbstractFuture) abstractFuture).value == setFuture) {
                        if (ATOMIC_HELPER.casValue(abstractFuture, setFuture, getFutureValue(setFuture.future))) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    executeListener(task, next.executor);
                }
            }
            return;
        }
    }

    protected void afterDone() {
    }

    @Override // com.google.common.util.concurrent.internal.InternalFutureFailureAccess
    @NullableDecl
    public final Throwable tryInternalFastPathGetFailure() {
        if (!(this instanceof Trusted)) {
            return null;
        }
        Object obj = this.value;
        if (obj instanceof Failure) {
            return ((Failure) obj).exception;
        }
        return null;
    }

    final void maybePropagateCancellationTo(@NullableDecl Future<?> related) {
        if ((related != null) && isCancelled()) {
            related.cancel(wasInterrupted());
        }
    }

    private void releaseWaiters() {
        Waiter head;
        do {
            head = this.waiters;
        } while (!ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE));
        for (Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
            currentWaiter.unpark();
        }
    }

    private Listener clearListeners(Listener onto) {
        Listener head;
        do {
            head = this.listeners;
        } while (!ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE));
        Listener reversedList = onto;
        while (head != null) {
            head = head.next;
            head.next = reversedList;
            reversedList = head;
        }
        return reversedList;
    }

    @Override // java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        StringBuilder builder = sb.append("[status=");
        if (isCancelled()) {
            builder.append("CANCELLED");
        } else if (isDone()) {
            addDoneString(builder);
        } else {
            addPendingString(builder);
        }
        builder.append("]");
        return builder.toString();
    }

    @NullableDecl
    protected String pendingToString() {
        if (!(this instanceof ScheduledFuture)) {
            return null;
        }
        return "remaining delay=[" + ((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS) + " ms]";
    }

    private void addPendingString(StringBuilder builder) {
        String pendingDescription;
        int truncateLength = builder.length();
        builder.append("PENDING");
        Object localValue = this.value;
        if (localValue instanceof SetFuture) {
            builder.append(", setFuture=[");
            appendUserObject(builder, ((SetFuture) localValue).future);
            builder.append("]");
        } else {
            try {
                pendingDescription = Strings.emptyToNull(pendingToString());
            } catch (RuntimeException | StackOverflowError e) {
                pendingDescription = "Exception thrown from implementation: " + e.getClass();
            }
            if (pendingDescription != null) {
                builder.append(", info=[");
                builder.append(pendingDescription);
                builder.append("]");
            }
        }
        if (isDone()) {
            builder.delete(truncateLength, builder.length());
            addDoneString(builder);
        }
    }

    private void addDoneString(StringBuilder builder) {
        try {
            Object uninterruptibly = getUninterruptibly(this);
            builder.append("SUCCESS, result=[");
            appendUserObject(builder, uninterruptibly);
            builder.append("]");
        } catch (CancellationException e) {
            builder.append("CANCELLED");
        } catch (RuntimeException e2) {
            builder.append("UNKNOWN, cause=[");
            builder.append(e2.getClass());
            builder.append(" thrown from get()]");
        } catch (ExecutionException e3) {
            builder.append("FAILURE, cause=[");
            builder.append(e3.getCause());
            builder.append("]");
        }
    }

    private void appendUserObject(StringBuilder builder, Object o) {
        try {
            if (o == this) {
                builder.append("this future");
            } else {
                builder.append(o);
            }
        } catch (RuntimeException | StackOverflowError e) {
            while (true) {
                builder.append("Exception thrown from implementation: ");
                builder.append(e.getClass());
                return;
            }
        }
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            Logger logger = log;
            Level level = Level.SEVERE;
            logger.log(level, "RuntimeException while executing runnable " + runnable + " with executor " + executor, (Throwable) e);
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class AtomicHelper {
        abstract boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2);

        abstract boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2);

        abstract boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2);

        abstract void putNext(Waiter waiter, Waiter waiter2);

        abstract void putThread(Waiter waiter, Thread thread);

        private AtomicHelper() {
        }
    }

    /* loaded from: classes3.dex */
    private static final class UnsafeAtomicHelper extends AtomicHelper {
        static final long LISTENERS_OFFSET;
        static final Unsafe UNSAFE;
        static final long VALUE_OFFSET;
        static final long WAITERS_OFFSET;
        static final long WAITER_NEXT_OFFSET;
        static final long WAITER_THREAD_OFFSET;

        private UnsafeAtomicHelper() {
            super();
        }

        static {
            Unsafe unsafe;
            try {
                unsafe = Unsafe.getUnsafe();
            } catch (SecurityException e) {
                try {
                    unsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() { // from class: com.google.common.util.concurrent.AbstractFuture.UnsafeAtomicHelper.1
                        @Override // java.security.PrivilegedExceptionAction
                        public Unsafe run() throws Exception {
                            Field[] declaredFields = Unsafe.class.getDeclaredFields();
                            for (Field f : declaredFields) {
                                f.setAccessible(true);
                                Object x = f.get(null);
                                if (Unsafe.class.isInstance(x)) {
                                    return (Unsafe) Unsafe.class.cast(x);
                                }
                            }
                            throw new NoSuchFieldError("the Unsafe");
                        }
                    });
                } catch (PrivilegedActionException e2) {
                    throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
                }
            }
            try {
                WAITERS_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("waiters"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("listeners"));
                VALUE_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("value"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            } catch (Exception e3) {
                Throwables.throwIfUnchecked(e3);
                throw new RuntimeException(e3);
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
        }
    }

    /* loaded from: classes3.dex */
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
            super();
            this.waiterThreadUpdater = waiterThreadUpdater;
            this.waiterNextUpdater = waiterNextUpdater;
            this.waitersUpdater = waitersUpdater;
            this.listenersUpdater = listenersUpdater;
            this.valueUpdater = valueUpdater;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            this.waiterThreadUpdater.lazySet(waiter, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            this.waiterNextUpdater.lazySet(waiter, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            return this.waitersUpdater.compareAndSet(future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            return this.listenersUpdater.compareAndSet(future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            return this.valueUpdater.compareAndSet(future, expect, update);
        }
    }

    /* loaded from: classes3.dex */
    private static final class SynchronizedHelper extends AtomicHelper {
        private SynchronizedHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            waiter.thread = newValue;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            waiter.next = newValue;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> future, Waiter expect, Waiter update) {
            synchronized (future) {
                if (((AbstractFuture) future).waiters != expect) {
                    return false;
                }
                ((AbstractFuture) future).waiters = update;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> future, Listener expect, Listener update) {
            synchronized (future) {
                if (((AbstractFuture) future).listeners != expect) {
                    return false;
                }
                ((AbstractFuture) future).listeners = update;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
            synchronized (future) {
                if (((AbstractFuture) future).value != expect) {
                    return false;
                }
                ((AbstractFuture) future).value = update;
                return true;
            }
        }
    }

    private static CancellationException cancellationExceptionWithCause(@NullableDecl String message, @NullableDecl Throwable cause) {
        CancellationException exception = new CancellationException(message);
        exception.initCause(cause);
        return exception;
    }
}
