package kotlinx.coroutines.internal;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LockFreeMPSCQueue.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u000e\b\u0000\u0018\u0000 \"*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002:\u0002\"#B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00028\u0000¢\u0006\u0002\u0010\u0013J \u0010\u0014\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0000j\b\u0012\u0004\u0012\u00028\u0000`\b2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J \u0010\u0017\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0000j\b\u0012\u0004\u0012\u00028\u0000`\b2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0006\u0010\u0018\u001a\u00020\u000eJ1\u0010\u0019\u001a\u0016\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\b2\u0006\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u0016H\u0002J\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00028\u00000\u0000J\b\u0010\u001e\u001a\u0004\u0018\u00010\u0002J,\u0010\u001f\u001a\u0016\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\b2\u0006\u0010 \u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u0004H\u0002R(\u0010\u0006\u001a\u001c\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\r\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeMPSCQueueCore;", ExifInterface.LONGITUDE_EAST, "", "capacity", "", "(I)V", "_next", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/internal/Core;", "_state", "Lkotlinx/atomicfu/AtomicLong;", "array", "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "isEmpty", "", "()Z", "mask", "addLast", "element", "(Ljava/lang/Object;)I", "allocateNextCopy", "state", "", "allocateOrGetNextCopy", "close", "fillPlaceholder", FirebaseAnalytics.Param.INDEX, "(ILjava/lang/Object;)Lkotlinx/coroutines/internal/LockFreeMPSCQueueCore;", "markFrozen", "next", "removeFirstOrNull", "removeSlowPath", "oldHead", "newHead", "Companion", "Placeholder", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class LockFreeMPSCQueueCore<E> {
    public static final int ADD_CLOSED = 2;
    public static final int ADD_FROZEN = 1;
    public static final int ADD_SUCCESS = 0;
    private static final int CAPACITY_BITS = 30;
    private static final long CLOSED_MASK = 2305843009213693952L;
    private static final int CLOSED_SHIFT = 61;
    private static final long FROZEN_MASK = 1152921504606846976L;
    private static final int FROZEN_SHIFT = 60;
    private static final long HEAD_MASK = 1073741823;
    private static final int HEAD_SHIFT = 0;
    public static final int INITIAL_CAPACITY = 8;
    private static final int MAX_CAPACITY_MASK = 1073741823;
    private static final long TAIL_MASK = 1152921503533105152L;
    private static final int TAIL_SHIFT = 30;
    private volatile Object _next = null;
    private volatile long _state = 0;
    private final AtomicReferenceArray<Object> array;
    private final int capacity;
    private final int mask;
    public static final Companion Companion = new Companion(null);
    public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
    private static final AtomicReferenceFieldUpdater _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeMPSCQueueCore.class, Object.class, "_next");
    private static final AtomicLongFieldUpdater _state$FU = AtomicLongFieldUpdater.newUpdater(LockFreeMPSCQueueCore.class, "_state");

    public LockFreeMPSCQueueCore(int capacity) {
        this.capacity = capacity;
        int i = this.capacity;
        this.mask = i - 1;
        this.array = new AtomicReferenceArray<>(i);
        boolean z = false;
        if (this.mask <= MAX_CAPACITY_MASK) {
            if (!((this.capacity & this.mask) == 0 ? true : z)) {
                throw new IllegalStateException("Check failed.".toString());
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public final boolean isEmpty() {
        Companion companion = Companion;
        long $receiver$iv = this._state;
        return ((int) ((HEAD_MASK & $receiver$iv) >> 0)) == ((int) ((TAIL_MASK & $receiver$iv) >> 30));
    }

    public final boolean close() {
        boolean z = false;
        while (true) {
            long cur$iv = this._state;
            if ((cur$iv & CLOSED_MASK) != 0) {
                return true;
            }
            if ((cur$iv & 1152921504606846976L) != 0) {
                return false;
            }
            if (_state$FU.compareAndSet(this, cur$iv, cur$iv | CLOSED_MASK)) {
                return true;
            }
            z = z;
        }
    }

    public final int addLast(E e) {
        LockFreeMPSCQueueCore fillPlaceholder;
        Intrinsics.checkParameterIsNotNull(e, "element");
        char c = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            long state = this._state;
            if ((3458764513820540928L & state) != 0) {
                return Companion.addFailReason(state);
            }
            Companion companion = Companion;
            int head$iv = (int) ((state & HEAD_MASK) >> c);
            int tail$iv = (int) ((state & TAIL_MASK) >> 30);
            int i = this.mask;
            if (((tail$iv + 2) & i) == (head$iv & i)) {
                return 1;
            }
            if (_state$FU.compareAndSet(this, state, Companion.updateTail(state, (tail$iv + 1) & MAX_CAPACITY_MASK))) {
                this.array.set(this.mask & tail$iv, e);
                LockFreeMPSCQueueCore<E> cur = this;
                while ((cur._state & 1152921504606846976L) != 0 && (fillPlaceholder = cur.next().fillPlaceholder(tail$iv, e)) != null) {
                    cur = fillPlaceholder;
                }
                return 0;
            }
            c = 0;
            z = z;
            z2 = z2;
            z3 = z3;
        }
    }

    private final LockFreeMPSCQueueCore<E> fillPlaceholder(int index, E e) {
        Object old = this.array.get(this.mask & index);
        if (!(old instanceof Placeholder) || ((Placeholder) old).index != index) {
            return null;
        }
        this.array.set(this.mask & index, e);
        return this;
    }

    public final Object removeFirstOrNull() {
        Object element;
        long state = this._state;
        if ((1152921504606846976L & state) != 0) {
            return REMOVE_FROZEN;
        }
        Companion companion = Companion;
        int head$iv = (int) ((HEAD_MASK & state) >> 0);
        int i = this.mask;
        if ((((int) ((TAIL_MASK & state) >> 30)) & i) == (head$iv & i) || (element = this.array.get(i & head$iv)) == null || (element instanceof Placeholder)) {
            return null;
        }
        int newHead = MAX_CAPACITY_MASK & (head$iv + 1);
        if (_state$FU.compareAndSet(this, state, Companion.updateHead(state, newHead))) {
            this.array.set(this.mask & head$iv, null);
            return element;
        }
        LockFreeMPSCQueueCore<E> cur = this;
        while (true) {
            LockFreeMPSCQueueCore removeSlowPath = cur.removeSlowPath(head$iv, newHead);
            if (removeSlowPath == null) {
                return element;
            }
            cur = removeSlowPath;
        }
    }

    private final LockFreeMPSCQueueCore<E> removeSlowPath(int oldHead, int newHead) {
        char c = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            long state = this._state;
            Companion companion = Companion;
            int head$iv = (int) ((HEAD_MASK & state) >> c);
            int i = (int) ((TAIL_MASK & state) >> 30);
            if ((head$iv == oldHead ? 1 : c) == 0) {
                throw new IllegalStateException("This queue can have only one consumer".toString());
            } else if ((1152921504606846976L & state) != 0) {
                return next();
            } else {
                if (_state$FU.compareAndSet(this, state, Companion.updateHead(state, newHead))) {
                    this.array.set(head$iv & this.mask, null);
                    return null;
                }
                z = z;
                z2 = z2;
                z3 = z3;
                c = 0;
            }
        }
    }

    public final LockFreeMPSCQueueCore<E> next() {
        return allocateOrGetNextCopy(markFrozen());
    }

    private final long markFrozen() {
        boolean z = false;
        while (true) {
            long cur$iv = this._state;
            if ((cur$iv & 1152921504606846976L) != 0) {
                return cur$iv;
            }
            long upd$iv = cur$iv | 1152921504606846976L;
            if (_state$FU.compareAndSet(this, cur$iv, upd$iv)) {
                return upd$iv;
            }
            z = z;
        }
    }

    private final LockFreeMPSCQueueCore<E> allocateOrGetNextCopy(long state) {
        while (true) {
            LockFreeMPSCQueueCore next = (LockFreeMPSCQueueCore) this._next;
            if (next != null) {
                return next;
            }
            _next$FU.compareAndSet(this, null, allocateNextCopy(state));
        }
    }

    private final LockFreeMPSCQueueCore<E> allocateNextCopy(long state) {
        LockFreeMPSCQueueCore next = new LockFreeMPSCQueueCore(this.capacity * 2);
        Companion companion = Companion;
        int tail$iv = (int) ((TAIL_MASK & state) >> 30);
        int index = (int) ((HEAD_MASK & state) >> 0);
        while (true) {
            int i = this.mask;
            if ((index & i) != (tail$iv & i)) {
                AtomicReferenceArray<Object> atomicReferenceArray = next.array;
                int i2 = next.mask & index;
                Object obj = this.array.get(i & index);
                if (obj == null) {
                    obj = new Placeholder(index);
                }
                atomicReferenceArray.set(i2, obj);
                index++;
            } else {
                next._state = Companion.wo(state, 1152921504606846976L);
                return next;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LockFreeMPSCQueue.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeMPSCQueueCore$Placeholder;", "", FirebaseAnalytics.Param.INDEX, "", "(I)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class Placeholder {
        public final int index;

        public Placeholder(int index) {
            this.index = index;
        }
    }

    /* compiled from: LockFreeMPSCQueue.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\f\u0010\u0015\u001a\u00020\u0004*\u00020\tH\u0002J\u0014\u0010\u0016\u001a\u00020\t*\u00020\t2\u0006\u0010\u0017\u001a\u00020\u0004H\u0002J\u0014\u0010\u0018\u001a\u00020\t*\u00020\t2\u0006\u0010\u0019\u001a\u00020\u0004H\u0002JP\u0010\u001a\u001a\u0002H\u001b\"\u0004\b\u0001\u0010\u001b*\u00020\t26\u0010\u001c\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(!\u0012\u0004\u0012\u0002H\u001b0\u001dH\u0082\b¢\u0006\u0002\u0010\"J\u0015\u0010#\u001a\u00020\t*\u00020\t2\u0006\u0010$\u001a\u00020\tH\u0082\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u00128\u0000X\u0081\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeMPSCQueueCore$Companion;", "", "()V", "ADD_CLOSED", "", "ADD_FROZEN", "ADD_SUCCESS", "CAPACITY_BITS", "CLOSED_MASK", "", "CLOSED_SHIFT", "FROZEN_MASK", "FROZEN_SHIFT", "HEAD_MASK", "HEAD_SHIFT", "INITIAL_CAPACITY", "MAX_CAPACITY_MASK", "REMOVE_FROZEN", "Lkotlinx/coroutines/internal/Symbol;", "TAIL_MASK", "TAIL_SHIFT", "addFailReason", "updateHead", "newHead", "updateTail", "newTail", "withState", ExifInterface.GPS_DIRECTION_TRUE, "block", "Lkotlin/Function2;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "head", "tail", "(JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "wo", "other", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final long wo(long $receiver, long other) {
            return (~other) & $receiver;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final long updateHead(long $receiver, int newHead) {
            return wo($receiver, LockFreeMPSCQueueCore.HEAD_MASK) | (((long) newHead) << 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final long updateTail(long $receiver, int newTail) {
            return wo($receiver, LockFreeMPSCQueueCore.TAIL_MASK) | (((long) newTail) << 30);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final <T> T withState(long $receiver, Function2<? super Integer, ? super Integer, ? extends T> function2) {
            return (T) function2.invoke(Integer.valueOf((int) ((LockFreeMPSCQueueCore.HEAD_MASK & $receiver) >> 0)), Integer.valueOf((int) ((LockFreeMPSCQueueCore.TAIL_MASK & $receiver) >> 30)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int addFailReason(long $receiver) {
            return (LockFreeMPSCQueueCore.CLOSED_MASK & $receiver) != 0 ? 2 : 1;
        }
    }
}
