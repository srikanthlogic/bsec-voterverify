package kotlinx.coroutines.internal;

import androidx.exifinterface.media.ExifInterface;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
/* compiled from: LockFreeMPMCQueue.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J!\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00018\u00002\b\u0010\u000f\u001a\u0004\u0018\u00018\u0000H\u0001¢\u0006\u0002\u0010\u0010R\u0019\u0010\u0004\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001c\u0010\b\u001a\u0004\u0018\u00018\u00008@X\u0081\u0004¢\u0006\f\u0012\u0004\b\t\u0010\u0003\u001a\u0004\b\n\u0010\u000b¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeMPMCQueueNode;", ExifInterface.GPS_DIRECTION_TRUE, "", "()V", "next", "Lkotlinx/atomicfu/AtomicRef;", "getNext", "()Lkotlinx/atomicfu/AtomicRef;", "nextValue", "nextValue$annotations", "getNextValue", "()Ljava/lang/Object;", "nextCas", "", "expect", "update", "(Ljava/lang/Object;Ljava/lang/Object;)Z", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public class LockFreeMPMCQueueNode<T> {
    static final AtomicReferenceFieldUpdater next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeMPMCQueueNode.class, Object.class, "next");
    volatile Object next = null;

    public static /* synthetic */ void nextValue$annotations() {
    }

    public final T getNextValue() {
        return (T) this.next;
    }

    public final boolean nextCas(T t, T t2) {
        return next$FU.compareAndSet(this, t, t2);
    }
}
