package kotlinx.coroutines;

import java.util.concurrent.Future;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
@Metadata(bv = {1, 0, 3}, d1 = {"kotlinx/coroutines/JobKt__FutureKt", "kotlinx/coroutines/JobKt__JobKt"}, k = 4, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class JobKt {
    public static final DisposableHandle DisposableHandle(Function0<Unit> function0) {
        return JobKt__JobKt.DisposableHandle(function0);
    }

    public static final Job Job(Job parent) {
        return JobKt__JobKt.Job(parent);
    }

    public static final void cancel(CoroutineContext $receiver) {
        JobKt__JobKt.cancel($receiver);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancel() without cause", replaceWith = @ReplaceWith(expression = "cancel()", imports = {}))
    public static final boolean cancel(CoroutineContext $receiver, Throwable cause) {
        return JobKt__JobKt.cancel($receiver, cause);
    }

    public static final Object cancelAndJoin(Job $receiver, Continuation<? super Unit> continuation) {
        return JobKt__JobKt.cancelAndJoin($receiver, continuation);
    }

    public static final void cancelChildren(CoroutineContext $receiver) {
        JobKt__JobKt.cancelChildren($receiver);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancelChildren() without cause", replaceWith = @ReplaceWith(expression = "cancelChildren()", imports = {}))
    public static final void cancelChildren(CoroutineContext $receiver, Throwable cause) {
        JobKt__JobKt.cancelChildren($receiver, cause);
    }

    public static final void cancelChildren(Job $receiver) {
        JobKt__JobKt.cancelChildren($receiver);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancelChildren() without cause", replaceWith = @ReplaceWith(expression = "cancelChildren()", imports = {}))
    public static final void cancelChildren(Job $receiver, Throwable cause) {
        JobKt__JobKt.cancelChildren($receiver, cause);
    }

    public static final void cancelFutureOnCancellation(CancellableContinuation<?> cancellableContinuation, Future<?> future) {
        JobKt__FutureKt.cancelFutureOnCancellation(cancellableContinuation, future);
    }

    public static final DisposableHandle cancelFutureOnCompletion(Job $receiver, Future<?> future) {
        return JobKt__FutureKt.cancelFutureOnCompletion($receiver, future);
    }

    public static final DisposableHandle disposeOnCompletion(Job $receiver, DisposableHandle handle) {
        return JobKt__JobKt.disposeOnCompletion($receiver, handle);
    }

    public static final boolean isActive(CoroutineContext $receiver) {
        return JobKt__JobKt.isActive($receiver);
    }
}
