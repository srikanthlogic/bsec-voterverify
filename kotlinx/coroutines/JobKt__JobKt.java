package kotlinx.coroutines;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
/* compiled from: Job.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0007\u001a\u0019\u0010\u0004\u001a\u00020\u00052\u000e\b\u0004\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0087\b\u001a\u0012\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\n\u001a\n\u0010\f\u001a\u00020\b*\u00020\u0002\u001a\u0018\u0010\f\u001a\u00020\u0001*\u00020\u00022\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0007\u001a\u0011\u0010\u000f\u001a\u00020\u0001*\u00020\u0002H\u0007¢\u0006\u0002\b\f\u001a\u0015\u0010\u0010\u001a\u00020\b*\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a\n\u0010\u0012\u001a\u00020\b*\u00020\u0002\u001a\u0018\u0010\u0012\u001a\u00020\b*\u00020\u00022\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0007\u001a\n\u0010\u0012\u001a\u00020\b*\u00020\n\u001a\u0018\u0010\u0012\u001a\u00020\b*\u00020\n2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0007\u001a\u0014\u0010\u0013\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0014\u001a\u00020\u0005H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0015"}, d2 = {"isActive", "", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)Z", "DisposableHandle", "Lkotlinx/coroutines/DisposableHandle;", "block", "Lkotlin/Function0;", "", "Job", "Lkotlinx/coroutines/Job;", "parent", "cancel", "cause", "", "cancel0", "cancelAndJoin", "(Lkotlinx/coroutines/Job;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cancelChildren", "disposeOnCompletion", "handle", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 13}, xs = "kotlinx/coroutines/JobKt")
/* loaded from: classes3.dex */
public final /* synthetic */ class JobKt__JobKt {
    public static final Job Job(Job parent) {
        return new JobImpl(parent);
    }

    public static /* synthetic */ Job Job$default(Job job, int i, Object obj) {
        if ((i & 1) != 0) {
            job = null;
        }
        return JobKt.Job(job);
    }

    public static final DisposableHandle DisposableHandle(Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        return new DisposableHandle() { // from class: kotlinx.coroutines.JobKt__JobKt$DisposableHandle$1
            @Override // kotlinx.coroutines.DisposableHandle
            public void dispose() {
                Function0.this.invoke();
            }
        };
    }

    public static final DisposableHandle disposeOnCompletion(Job $receiver, DisposableHandle handle) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(handle, "handle");
        return $receiver.invokeOnCompletion(new DisposeOnCompletion($receiver, handle));
    }

    public static final Object cancelAndJoin(Job $receiver, Continuation<? super Unit> continuation) {
        $receiver.cancel();
        return $receiver.join(continuation);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancelChildren() without cause", replaceWith = @ReplaceWith(expression = "cancelChildren()", imports = {}))
    public static /* synthetic */ void cancelChildren$default(Job job, Throwable th, int i, Object obj) {
        if ((i & 1) != 0) {
            th = null;
        }
        JobKt.cancelChildren(job, th);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancelChildren() without cause", replaceWith = @ReplaceWith(expression = "cancelChildren()", imports = {}))
    public static final void cancelChildren(Job $receiver, Throwable cause) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        for (Job it : $receiver.getChildren()) {
            it.cancel(cause);
        }
    }

    public static final void cancelChildren(Job $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        for (Job it : $receiver.getChildren()) {
            it.cancel();
        }
    }

    public static final boolean isActive(CoroutineContext $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Job job = (Job) $receiver.get(Job.Key);
        return job != null && job.isActive();
    }

    public static final void cancel(CoroutineContext $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Job job = (Job) $receiver.get(Job.Key);
        if (job != null) {
            job.cancel();
        }
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancel() without cause", replaceWith = @ReplaceWith(expression = "cancel()", imports = {}))
    public static /* synthetic */ boolean cancel$default(CoroutineContext coroutineContext, Throwable th, int i, Object obj) {
        if ((i & 1) != 0) {
            th = null;
        }
        return JobKt.cancel(coroutineContext, th);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancel() without cause", replaceWith = @ReplaceWith(expression = "cancel()", imports = {}))
    public static final boolean cancel(CoroutineContext $receiver, Throwable cause) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Job job = (Job) $receiver.get(Job.Key);
        if (job != null) {
            return job.cancel(cause);
        }
        return false;
    }

    public static final void cancelChildren(CoroutineContext $receiver) {
        Sequence<Job> $receiver$iv;
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Job job = (Job) $receiver.get(Job.Key);
        if (!(job == null || ($receiver$iv = job.getChildren()) == null)) {
            for (Job it : $receiver$iv) {
                it.cancel();
            }
        }
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancelChildren() without cause", replaceWith = @ReplaceWith(expression = "cancelChildren()", imports = {}))
    public static /* synthetic */ void cancelChildren$default(CoroutineContext coroutineContext, Throwable th, int i, Object obj) {
        if ((i & 1) != 0) {
            th = null;
        }
        JobKt.cancelChildren(coroutineContext, th);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use cancelChildren() without cause", replaceWith = @ReplaceWith(expression = "cancelChildren()", imports = {}))
    public static final void cancelChildren(CoroutineContext $receiver, Throwable cause) {
        Sequence<Job> $receiver$iv;
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Job job = (Job) $receiver.get(Job.Key);
        if (!(job == null || ($receiver$iv = job.getChildren()) == null)) {
            for (Job it : $receiver$iv) {
                it.cancel(cause);
            }
        }
    }
}
