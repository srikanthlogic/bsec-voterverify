package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
/* compiled from: CancellableContinuation.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a=\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0006\u0012\u0004\u0012\u00020\u00070\u0005H\u0087Hø\u0001\u0000¢\u0006\u0002\u0010\b\u001a3\u0010\t\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0006\u0012\u0004\u0012\u00020\u00070\u0005H\u0086Hø\u0001\u0000¢\u0006\u0002\u0010\n\u001a\u0018\u0010\u000b\u001a\u00020\u0007*\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\f\u001a\u00020\rH\u0007\u001a\u0018\u0010\u000e\u001a\u00020\u0007*\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u000f\u001a\u00020\u0010H\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011"}, d2 = {"suspendAtomicCancellableCoroutine", ExifInterface.GPS_DIRECTION_TRUE, "holdCancellability", "", "block", "Lkotlin/Function1;", "Lkotlinx/coroutines/CancellableContinuation;", "", "(ZLkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "suspendCancellableCoroutine", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disposeOnCancellation", "handle", "Lkotlinx/coroutines/DisposableHandle;", "removeOnCancellation", "node", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class CancellableContinuationKt {
    private static final <T> Object suspendCancellableCoroutine(Function1<? super CancellableContinuation<? super T>, Unit> function1, Continuation<? super T> continuation) {
        InlineMarker.mark(0);
        CancellableContinuationImpl cancellable = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        cancellable.initCancellability();
        function1.invoke(cancellable);
        Object result = cancellable.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        InlineMarker.mark(1);
        return result;
    }

    static /* synthetic */ Object suspendAtomicCancellableCoroutine$default(boolean holdCancellability, Function1 block, Continuation uCont, int i, Object obj) {
        if ((i & 1) != 0) {
            holdCancellability = false;
        }
        InlineMarker.mark(0);
        CancellableContinuationImpl cancellable = new CancellableContinuationImpl(IntrinsicsKt.intercepted(uCont), 0);
        if (!holdCancellability) {
            cancellable.initCancellability();
        }
        block.invoke(cancellable);
        Object result = cancellable.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(uCont);
        }
        InlineMarker.mark(1);
        return result;
    }

    private static final <T> Object suspendAtomicCancellableCoroutine(boolean holdCancellability, Function1<? super CancellableContinuation<? super T>, Unit> function1, Continuation<? super T> continuation) {
        InlineMarker.mark(0);
        CancellableContinuationImpl cancellable = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        if (!holdCancellability) {
            cancellable.initCancellability();
        }
        function1.invoke(cancellable);
        Object result = cancellable.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        InlineMarker.mark(1);
        return result;
    }

    public static final void removeOnCancellation(CancellableContinuation<?> cancellableContinuation, LockFreeLinkedListNode node) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "receiver$0");
        Intrinsics.checkParameterIsNotNull(node, "node");
        cancellableContinuation.invokeOnCancellation(new RemoveOnCancel(node));
    }

    public static final void disposeOnCancellation(CancellableContinuation<?> cancellableContinuation, DisposableHandle handle) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "receiver$0");
        Intrinsics.checkParameterIsNotNull(handle, "handle");
        cancellableContinuation.invokeOnCancellation(new DisposeOnCancel(handle));
    }
}
