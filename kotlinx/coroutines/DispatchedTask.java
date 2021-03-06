package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.internal.ThreadContextKt;
/* compiled from: Dispatched.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b`\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00060\u0002j\u0002`\u0003J\u0014\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u001d\u0010\u0010\u001a\u0002H\u0001\"\u0004\b\u0001\u0010\u00012\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016¢\u0006\u0002\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\n\u0010\u0014\u001a\u0004\u0018\u00010\u000fH&R\u0018\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/DispatchedTask;", ExifInterface.GPS_DIRECTION_TRUE, "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "delegate", "Lkotlin/coroutines/Continuation;", "getDelegate", "()Lkotlin/coroutines/Continuation;", "resumeMode", "", "getResumeMode", "()I", "getExceptionalResult", "", "state", "", "getSuccessfulResult", "(Ljava/lang/Object;)Ljava/lang/Object;", "run", "", "takeState", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public interface DispatchedTask<T> extends Runnable {
    Continuation<T> getDelegate();

    Throwable getExceptionalResult(Object obj);

    int getResumeMode();

    <T> T getSuccessfulResult(Object obj);

    @Override // java.lang.Runnable
    void run();

    Object takeState();

    /* compiled from: Dispatched.kt */
    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class DefaultImpls {
        public static <T> int getResumeMode(DispatchedTask<? super T> dispatchedTask) {
            return 1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static <T_I1, T> T getSuccessfulResult(DispatchedTask<? super T_I1> dispatchedTask, Object state) {
            return state;
        }

        public static <T> Throwable getExceptionalResult(DispatchedTask<? super T> dispatchedTask, Object state) {
            CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(state instanceof CompletedExceptionally) ? null : state);
            if (completedExceptionally != null) {
                return completedExceptionally.cause;
            }
            return null;
        }

        public static <T> void run(DispatchedTask<? super T> dispatchedTask) {
            try {
                Continuation<? super T> delegate = dispatchedTask.getDelegate();
                if (delegate != null) {
                    DispatchedContinuation delegate2 = (DispatchedContinuation) delegate;
                    Continuation continuation = delegate2.continuation;
                    CoroutineContext context = continuation.getContext();
                    Job job = ResumeModeKt.isCancellableMode(dispatchedTask.getResumeMode()) ? (Job) context.get(Job.Key) : null;
                    Object state = dispatchedTask.takeState();
                    Object oldValue$iv = ThreadContextKt.updateThreadContext(context, delegate2.countOrElement);
                    if (job == null || job.isActive()) {
                        Throwable exception = dispatchedTask.getExceptionalResult(state);
                        if (exception != null) {
                            Result.Companion companion = Result.Companion;
                            continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
                        } else {
                            Object successfulResult = dispatchedTask.getSuccessfulResult(state);
                            Result.Companion companion2 = Result.Companion;
                            continuation.resumeWith(Result.m13constructorimpl(successfulResult));
                        }
                    } else {
                        Result.Companion companion3 = Result.Companion;
                        continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(job.getCancellationException())));
                    }
                    Unit unit = Unit.INSTANCE;
                    ThreadContextKt.restoreThreadContext(context, oldValue$iv);
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.DispatchedContinuation<T>");
            } catch (Throwable e) {
                throw new DispatchException("Unexpected exception running " + dispatchedTask, e);
            }
        }
    }
}
