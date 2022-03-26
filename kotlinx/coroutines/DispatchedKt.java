package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.UndispatchedEventLoop;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.ThreadContextKt;
/* compiled from: Dispatched.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\u001a\"\u0010\u0004\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0000\u001a.\u0010\n\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00060\f2\u0006\u0010\r\u001a\u00020\tH\u0000\u001a%\u0010\u000e\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\f2\u0006\u0010\u000f\u001a\u0002H\u0006H\u0000¢\u0006\u0002\u0010\u0010\u001a \u0010\u0011\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0000\u001a%\u0010\u0014\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\f2\u0006\u0010\u000f\u001a\u0002H\u0006H\u0000¢\u0006\u0002\u0010\u0010\u001a \u0010\u0015\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0000\u001a\u0012\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\u00050\u0018H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003¨\u0006\u0019"}, d2 = {"UNDEFINED", "Lkotlinx/coroutines/internal/Symbol;", "UNDEFINED$annotations", "()V", "dispatch", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlinx/coroutines/DispatchedTask;", "mode", "", "resume", "delegate", "Lkotlin/coroutines/Continuation;", "useMode", "resumeCancellable", "value", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;)V", "resumeCancellableWithException", "exception", "", "resumeDirect", "resumeDirectWithException", "yieldUndispatched", "", "Lkotlinx/coroutines/DispatchedContinuation;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class DispatchedKt {
    private static final Symbol UNDEFINED = new Symbol("UNDEFINED");

    private static /* synthetic */ void UNDEFINED$annotations() {
    }

    public static final /* synthetic */ Symbol access$getUNDEFINED$p() {
        return UNDEFINED;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00ee A[Catch: all -> 0x00f9, LOOP:0: B:38:0x00e3->B:40:0x00ee, LOOP_END, TRY_LEAVE, TryCatch #3 {all -> 0x00f9, blocks: (B:24:0x00b3, B:30:0x00ce, B:35:0x00db, B:36:0x00de, B:38:0x00e3, B:40:0x00ee), top: B:58:0x00b3 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00f2 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final <T> void resumeCancellable(Continuation<? super T> continuation, T t) {
        Throwable e$iv$iv$iv;
        boolean z;
        Runnable nextEvent$iv$iv$iv;
        Object countOrElement$iv$iv$iv;
        Throwable th;
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        if (continuation instanceof DispatchedContinuation) {
            DispatchedContinuation this_$iv = (DispatchedContinuation) continuation;
            if (this_$iv.dispatcher.isDispatchNeeded(this_$iv.getContext())) {
                this_$iv._state = t;
                this_$iv.setResumeMode(1);
                this_$iv.dispatcher.dispatch(this_$iv.getContext(), this_$iv);
                return;
            }
            UndispatchedEventLoop undispatchedEventLoop = UndispatchedEventLoop.INSTANCE;
            UndispatchedEventLoop.EventLoop eventLoop$iv$iv = UndispatchedEventLoop.threadLocalEventLoop.get();
            if (eventLoop$iv$iv.isActive) {
                this_$iv._state = t;
                this_$iv.setResumeMode(1);
                eventLoop$iv$iv.queue.addLast(this_$iv);
                return;
            }
            Intrinsics.checkExpressionValueIsNotNull(eventLoop$iv$iv, "eventLoop");
            try {
                eventLoop$iv$iv.isActive = true;
                Job job$iv$iv = (Job) this_$iv.getContext().get(Job.Key);
                if (job$iv$iv != null) {
                    try {
                        if (!job$iv$iv.isActive()) {
                            Result.Companion companion = Result.Companion;
                            this_$iv.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(job$iv$iv.getCancellationException())));
                            z = true;
                            if (z) {
                                CoroutineContext context$iv$iv$iv = this_$iv.getContext();
                                try {
                                    Object oldValue$iv$iv$iv = ThreadContextKt.updateThreadContext(context$iv$iv$iv, this_$iv.countOrElement);
                                    try {
                                        Continuation<T> continuation2 = this_$iv.continuation;
                                        Result.Companion companion2 = Result.Companion;
                                        try {
                                            continuation2.resumeWith(Result.m13constructorimpl(t));
                                            Unit unit = Unit.INSTANCE;
                                            ThreadContextKt.restoreThreadContext(context$iv$iv$iv, oldValue$iv$iv$iv);
                                        } catch (Throwable th2) {
                                            th = th2;
                                            countOrElement$iv$iv$iv = oldValue$iv$iv$iv;
                                            ThreadContextKt.restoreThreadContext(context$iv$iv$iv, countOrElement$iv$iv$iv);
                                            throw th;
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                        countOrElement$iv$iv$iv = oldValue$iv$iv$iv;
                                    }
                                } catch (Throwable th4) {
                                    e$iv$iv$iv = th4;
                                    try {
                                        eventLoop$iv$iv.queue.clear();
                                        throw new DispatchException("Unexpected exception in undispatched event loop, clearing pending tasks", e$iv$iv$iv);
                                    } finally {
                                        eventLoop$iv$iv.isActive = false;
                                    }
                                }
                            }
                            while (true) {
                                nextEvent$iv$iv$iv = eventLoop$iv$iv.queue.removeFirstOrNull();
                                if (nextEvent$iv$iv$iv == null) {
                                    nextEvent$iv$iv$iv.run();
                                } else {
                                    return;
                                }
                            }
                        }
                    } catch (Throwable th5) {
                        e$iv$iv$iv = th5;
                        eventLoop$iv$iv.queue.clear();
                        throw new DispatchException("Unexpected exception in undispatched event loop, clearing pending tasks", e$iv$iv$iv);
                    }
                }
                z = false;
                if (z) {
                }
                while (true) {
                    nextEvent$iv$iv$iv = eventLoop$iv$iv.queue.removeFirstOrNull();
                    if (nextEvent$iv$iv$iv == null) {
                    }
                    nextEvent$iv$iv$iv.run();
                }
            } catch (Throwable th6) {
                e$iv$iv$iv = th6;
            }
        } else {
            Result.Companion companion3 = Result.Companion;
            continuation.resumeWith(Result.m13constructorimpl(t));
        }
    }

    /* JADX INFO: Multiple debug info for r3v3 java.lang.Object: [D('countOrElement$iv$iv$iv' java.lang.Object), D('this_$iv' kotlinx.coroutines.DispatchedContinuation)] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0112 A[Catch: all -> 0x011d, LOOP:0: B:42:0x0107->B:44:0x0112, LOOP_END, TRY_LEAVE, TryCatch #4 {all -> 0x011d, blocks: (B:26:0x00cd, B:32:0x00ec, B:37:0x00f9, B:38:0x00fc, B:42:0x0107, B:44:0x0112), top: B:64:0x00cd }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0116 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final <T> void resumeCancellableWithException(Continuation<? super T> continuation, Throwable exception) {
        Throwable e$iv$iv$iv;
        boolean z;
        Runnable nextEvent$iv$iv$iv;
        Object oldValue$iv$iv$iv;
        Throwable th;
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (continuation instanceof DispatchedContinuation) {
            DispatchedContinuation this_$iv = (DispatchedContinuation) continuation;
            CoroutineContext context$iv = this_$iv.continuation.getContext();
            CompletedExceptionally state$iv = new CompletedExceptionally(exception);
            if (this_$iv.dispatcher.isDispatchNeeded(context$iv)) {
                this_$iv._state = new CompletedExceptionally(exception);
                this_$iv.setResumeMode(1);
                this_$iv.dispatcher.dispatch(context$iv, this_$iv);
                return;
            }
            UndispatchedEventLoop undispatchedEventLoop = UndispatchedEventLoop.INSTANCE;
            UndispatchedEventLoop.EventLoop eventLoop$iv$iv = UndispatchedEventLoop.threadLocalEventLoop.get();
            if (eventLoop$iv$iv.isActive) {
                this_$iv._state = state$iv;
                this_$iv.setResumeMode(1);
                eventLoop$iv$iv.queue.addLast(this_$iv);
                return;
            }
            Intrinsics.checkExpressionValueIsNotNull(eventLoop$iv$iv, "eventLoop");
            try {
                eventLoop$iv$iv.isActive = true;
                Job job$iv$iv = (Job) this_$iv.getContext().get(Job.Key);
                if (job$iv$iv != null) {
                    try {
                        if (!job$iv$iv.isActive()) {
                            Result.Companion companion = Result.Companion;
                            this_$iv.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(job$iv$iv.getCancellationException())));
                            z = true;
                            if (z) {
                                CoroutineContext context$iv$iv$iv = this_$iv.getContext();
                                try {
                                    try {
                                        Object oldValue$iv$iv$iv2 = ThreadContextKt.updateThreadContext(context$iv$iv$iv, this_$iv.countOrElement);
                                        try {
                                            Continuation<T> continuation2 = this_$iv.continuation;
                                            Result.Companion companion2 = Result.Companion;
                                            try {
                                                continuation2.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
                                                Unit unit = Unit.INSTANCE;
                                                ThreadContextKt.restoreThreadContext(context$iv$iv$iv, oldValue$iv$iv$iv2);
                                            } catch (Throwable th2) {
                                                th = th2;
                                                oldValue$iv$iv$iv = oldValue$iv$iv$iv2;
                                                ThreadContextKt.restoreThreadContext(context$iv$iv$iv, oldValue$iv$iv$iv);
                                                throw th;
                                            }
                                        } catch (Throwable th3) {
                                            th = th3;
                                            oldValue$iv$iv$iv = oldValue$iv$iv$iv2;
                                        }
                                    } catch (Throwable th4) {
                                        e$iv$iv$iv = th4;
                                        try {
                                            eventLoop$iv$iv.queue.clear();
                                            throw new DispatchException("Unexpected exception in undispatched event loop, clearing pending tasks", e$iv$iv$iv);
                                        } finally {
                                            eventLoop$iv$iv.isActive = false;
                                        }
                                    }
                                } catch (Throwable th5) {
                                    e$iv$iv$iv = th5;
                                }
                            }
                            while (true) {
                                nextEvent$iv$iv$iv = eventLoop$iv$iv.queue.removeFirstOrNull();
                                if (nextEvent$iv$iv$iv == null) {
                                    nextEvent$iv$iv$iv.run();
                                } else {
                                    return;
                                }
                            }
                        }
                    } catch (Throwable th6) {
                        e$iv$iv$iv = th6;
                        eventLoop$iv$iv.queue.clear();
                        throw new DispatchException("Unexpected exception in undispatched event loop, clearing pending tasks", e$iv$iv$iv);
                    }
                }
                z = false;
                if (z) {
                }
                while (true) {
                    nextEvent$iv$iv$iv = eventLoop$iv$iv.queue.removeFirstOrNull();
                    if (nextEvent$iv$iv$iv == null) {
                    }
                    nextEvent$iv$iv$iv.run();
                }
            } catch (Throwable th7) {
                e$iv$iv$iv = th7;
            }
        } else {
            Result.Companion companion3 = Result.Companion;
            continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
        }
    }

    public static final <T> void resumeDirect(Continuation<? super T> continuation, T t) {
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        if (continuation instanceof DispatchedContinuation) {
            Continuation<T> continuation2 = ((DispatchedContinuation) continuation).continuation;
            Result.Companion companion = Result.Companion;
            continuation2.resumeWith(Result.m13constructorimpl(t));
            return;
        }
        Result.Companion companion2 = Result.Companion;
        continuation.resumeWith(Result.m13constructorimpl(t));
    }

    public static final <T> void resumeDirectWithException(Continuation<? super T> continuation, Throwable exception) {
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (continuation instanceof DispatchedContinuation) {
            Continuation<T> continuation2 = ((DispatchedContinuation) continuation).continuation;
            Result.Companion companion = Result.Companion;
            continuation2.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
            return;
        }
        Result.Companion companion2 = Result.Companion;
        continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
    }

    public static final boolean yieldUndispatched(DispatchedContinuation<? super Unit> dispatchedContinuation) {
        Intrinsics.checkParameterIsNotNull(dispatchedContinuation, "receiver$0");
        UndispatchedEventLoop undispatchedEventLoop = UndispatchedEventLoop.INSTANCE;
        Object contState$iv = Unit.INSTANCE;
        UndispatchedEventLoop.EventLoop eventLoop$iv = UndispatchedEventLoop.threadLocalEventLoop.get();
        if (!eventLoop$iv.isActive) {
            Intrinsics.checkExpressionValueIsNotNull(eventLoop$iv, "eventLoop");
            try {
                eventLoop$iv.isActive = true;
                dispatchedContinuation.run();
                while (true) {
                    Runnable nextEvent$iv$iv = eventLoop$iv.queue.removeFirstOrNull();
                    if (nextEvent$iv$iv == null) {
                        return false;
                    }
                    nextEvent$iv$iv.run();
                }
            } catch (Throwable e$iv$iv) {
                try {
                    eventLoop$iv.queue.clear();
                    throw new DispatchException("Unexpected exception in undispatched event loop, clearing pending tasks", e$iv$iv);
                } finally {
                    eventLoop$iv.isActive = false;
                }
            }
        } else if (eventLoop$iv.queue.isEmpty()) {
            return false;
        } else {
            dispatchedContinuation._state = contState$iv;
            dispatchedContinuation.setResumeMode(1);
            eventLoop$iv.queue.addLast(dispatchedContinuation);
            return true;
        }
    }

    public static /* synthetic */ void dispatch$default(DispatchedTask dispatchedTask, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 1;
        }
        dispatch(dispatchedTask, i);
    }

    public static final <T> void dispatch(DispatchedTask<? super T> dispatchedTask, int mode) {
        Intrinsics.checkParameterIsNotNull(dispatchedTask, "receiver$0");
        Continuation delegate = dispatchedTask.getDelegate();
        if (!ResumeModeKt.isDispatchedMode(mode) || !(delegate instanceof DispatchedContinuation) || ResumeModeKt.isCancellableMode(mode) != ResumeModeKt.isCancellableMode(dispatchedTask.getResumeMode())) {
            resume(dispatchedTask, delegate, mode);
            return;
        }
        CoroutineDispatcher dispatcher = ((DispatchedContinuation) delegate).dispatcher;
        CoroutineContext context = delegate.getContext();
        if (dispatcher.isDispatchNeeded(context)) {
            dispatcher.dispatch(context, dispatchedTask);
        } else {
            UndispatchedEventLoop.INSTANCE.resumeUndispatched(dispatchedTask);
        }
    }

    public static final <T> void resume(DispatchedTask<? super T> dispatchedTask, Continuation<? super T> continuation, int useMode) {
        Intrinsics.checkParameterIsNotNull(dispatchedTask, "receiver$0");
        Intrinsics.checkParameterIsNotNull(continuation, "delegate");
        Object state = dispatchedTask.takeState();
        Throwable exception = dispatchedTask.getExceptionalResult(state);
        if (exception != null) {
            ResumeModeKt.resumeWithExceptionMode(continuation, exception, useMode);
        } else {
            ResumeModeKt.resumeMode(continuation, dispatchedTask.getSuccessfulResult(state), useMode);
        }
    }
}
