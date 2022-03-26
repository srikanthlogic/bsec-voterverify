package kotlinx.coroutines.internal;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeMPMCQueueNode;
/* compiled from: LockFreeMPMCQueue.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0010\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u0016J9\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00028\u00002!\u0010\u0019\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u001b\u0012\b\b\u001c\u0012\u0004\b\b(\u001d\u0012\u0004\u0012\u00020\u00180\u001aH\u0086\b¢\u0006\u0002\u0010\u001eJE\u0010\u001f\u001a\u0002H \"\u0004\b\u0001\u0010 2\u0006\u0010!\u001a\u0002H 2'\u0010\"\u001a#\u0012\u0013\u0012\u0011H ¢\u0006\f\b\u001b\u0012\b\b\u001c\u0012\u0004\b\b($\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H 0#H\u0086\b¢\u0006\u0002\u0010%J\u001d\u0010&\u001a\u00020\u00182\u0006\u0010'\u001a\u00028\u00002\u0006\u0010(\u001a\u00028\u0000H\u0001¢\u0006\u0002\u0010)J\u0006\u0010*\u001a\u00020\u0018J\r\u0010+\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010\nJ3\u0010,\u001a\u0004\u0018\u00018\u00002!\u0010\u0019\u001a\u001d\u0012\u0013\u0012\u00118\u0000¢\u0006\f\b\u001b\u0012\b\b\u001c\u0012\u0004\b\b(-\u0012\u0004\u0012\u00020\u00180\u001aH\u0086\b¢\u0006\u0002\u0010.J\u001d\u0010/\u001a\u00020\u00182\u0006\u00100\u001a\u00028\u00002\u0006\u0010(\u001a\u00028\u0000H\u0001¢\u0006\u0002\u0010)R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00028\u00008@X\u0081\u0004¢\u0006\f\u0012\u0004\b\b\u0010\u0004\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u00028\u00008@X\u0081\u0004¢\u0006\f\u0012\u0004\b\u0011\u0010\u0004\u001a\u0004\b\u0012\u0010\n¨\u00061"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeMPMCQueue;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlinx/coroutines/internal/LockFreeMPMCQueueNode;", "", "()V", "head", "Lkotlinx/atomicfu/AtomicRef;", "headValue", "headValue$annotations", "getHeadValue", "()Lkotlinx/coroutines/internal/LockFreeMPMCQueueNode;", "size", "", "getSize", "()I", "tail", "tailValue", "tailValue$annotations", "getTailValue", "addLast", "", "node", "(Lkotlinx/coroutines/internal/LockFreeMPMCQueueNode;)V", "addLastIfPrev", "", "predicate", "Lkotlin/Function1;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "prev", "(Lkotlinx/coroutines/internal/LockFreeMPMCQueueNode;Lkotlin/jvm/functions/Function1;)Z", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "acc", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "headCas", "curHead", "update", "(Lkotlinx/coroutines/internal/LockFreeMPMCQueueNode;Lkotlinx/coroutines/internal/LockFreeMPMCQueueNode;)Z", "isEmpty", "removeFirstOrNull", "removeFirstOrNullIf", "first", "(Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/internal/LockFreeMPMCQueueNode;", "tailCas", "curTail", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public class LockFreeMPMCQueue<T extends LockFreeMPMCQueueNode<T>> {
    private static final AtomicReferenceFieldUpdater head$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeMPMCQueue.class, Object.class, "head");
    private static final AtomicReferenceFieldUpdater tail$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeMPMCQueue.class, Object.class, "tail");
    private volatile Object head = new LockFreeMPMCQueueNode();
    private volatile Object tail = this.head;

    public static /* synthetic */ void headValue$annotations() {
    }

    public static /* synthetic */ void tailValue$annotations() {
    }

    public final T getHeadValue() {
        return (T) ((LockFreeMPMCQueueNode) this.head);
    }

    public final T getTailValue() {
        return (T) ((LockFreeMPMCQueueNode) this.tail);
    }

    public final boolean headCas(T t, T t2) {
        Intrinsics.checkParameterIsNotNull(t, "curHead");
        Intrinsics.checkParameterIsNotNull(t2, "update");
        return head$FU.compareAndSet(this, t, t2);
    }

    public final boolean tailCas(T t, T t2) {
        Intrinsics.checkParameterIsNotNull(t, "curTail");
        Intrinsics.checkParameterIsNotNull(t2, "update");
        return tail$FU.compareAndSet(this, t, t2);
    }

    public final void addLast(T t) {
        Intrinsics.checkParameterIsNotNull(t, "node");
        while (true) {
            LockFreeMPMCQueueNode curTail = (LockFreeMPMCQueueNode) this.tail;
            LockFreeMPMCQueueNode curNext = (LockFreeMPMCQueueNode) curTail.next;
            if (curNext != null) {
                tail$FU.compareAndSet(this, curTail, curNext);
            } else if (LockFreeMPMCQueueNode.next$FU.compareAndSet(curTail, null, t)) {
                tail$FU.compareAndSet(this, curTail, t);
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final boolean addLastIfPrev(T t, Function1<Object, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(t, "node");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        while (true) {
            LockFreeMPMCQueueNode curTail = getTailValue();
            LockFreeMPMCQueueNode curNext = (LockFreeMPMCQueueNode) curTail.getNextValue();
            if (curNext != null) {
                tailCas(curTail, curNext);
            } else if (!function1.invoke(curTail).booleanValue()) {
                return false;
            } else {
                if (curTail.nextCas(null, t)) {
                    tailCas(curTail, t);
                    return true;
                }
            }
        }
    }

    public final T removeFirstOrNull() {
        LockFreeMPMCQueueNode curHead;
        T t;
        do {
            curHead = (LockFreeMPMCQueueNode) this.head;
            t = (T) ((LockFreeMPMCQueueNode) curHead.next);
            if (t == null) {
                return null;
            }
        } while (!head$FU.compareAndSet(this, curHead, t));
        return t;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [T extends kotlinx.coroutines.internal.LockFreeMPMCQueueNode<T>, java.lang.Object, kotlinx.coroutines.internal.LockFreeMPMCQueueNode] */
    public final T removeFirstOrNullIf(Function1<? super T, Boolean> function1) {
        LockFreeMPMCQueueNode curHead;
        LockFreeMPMCQueueNode lockFreeMPMCQueueNode;
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        do {
            curHead = getHeadValue();
            lockFreeMPMCQueueNode = (LockFreeMPMCQueueNode) curHead.getNextValue();
            if (lockFreeMPMCQueueNode == 0 || !function1.invoke(lockFreeMPMCQueueNode).booleanValue()) {
                return null;
            }
        } while (!headCas(curHead, lockFreeMPMCQueueNode));
        return lockFreeMPMCQueueNode;
    }

    public final boolean isEmpty() {
        return getSize() == 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final int getSize() {
        int acc$iv = 0;
        LockFreeMPMCQueueNode cur$iv = getHeadValue();
        while (true) {
            LockFreeMPMCQueueNode next$iv = (LockFreeMPMCQueueNode) cur$iv.getNextValue();
            if (next$iv == null) {
                return acc$iv;
            }
            acc$iv++;
            cur$iv = next$iv;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <R> R fold(R r, Function2<? super R, ? super T, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        Object acc = r;
        LockFreeMPMCQueueNode cur = getHeadValue();
        while (true) {
            LockFreeMPMCQueueNode next = (LockFreeMPMCQueueNode) cur.getNextValue();
            if (next == null) {
                return (R) acc;
            }
            acc = (R) function2.invoke(acc, next);
            cur = next;
        }
    }
}
