package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DispatchedTask;
import kotlinx.coroutines.UndispatchedEventLoop;
import kotlinx.coroutines.internal.ThreadContextKt;
/* compiled from: Dispatched.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u001b\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\u0002\u0010\u0007J\u0017\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00028\u0000H\u0000¢\u0006\u0004\b\u001d\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00028\u0000H\u0086\b¢\u0006\u0002\u0010\u001eJ\u0011\u0010 \u001a\u00020\u001b2\u0006\u0010!\u001a\u00020\"H\u0086\bJ\t\u0010#\u001a\u00020$H\u0086\bJ\u0016\u0010%\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00028\u0000H\u0086\b¢\u0006\u0002\u0010\u001eJ\u0011\u0010&\u001a\u00020\u001b2\u0006\u0010!\u001a\u00020\"H\u0086\bJ\u001e\u0010'\u001a\u00020\u001b2\f\u0010(\u001a\b\u0012\u0004\u0012\u00028\u00000)H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\n\u0010*\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010+\u001a\u00020,H\u0016R\u001a\u0010\b\u001a\u0004\u0018\u00010\t8\u0000@\u0000X\u0081\u000e¢\u0006\b\n\u0000\u0012\u0004\b\n\u0010\u000bR\u0012\u0010\f\u001a\u00020\rX\u0096\u0005¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\t8\u0000X\u0081\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0014\u001a\u00020\u0015X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006-"}, d2 = {"Lkotlinx/coroutines/DispatchedContinuation;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/coroutines/Continuation;", "Lkotlinx/coroutines/DispatchedTask;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "continuation", "(Lkotlinx/coroutines/CoroutineDispatcher;Lkotlin/coroutines/Continuation;)V", "_state", "", "_state$annotations", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "countOrElement", "delegate", "getDelegate", "()Lkotlin/coroutines/Continuation;", "resumeMode", "", "getResumeMode", "()I", "setResumeMode", "(I)V", "dispatchYield", "", "value", "dispatchYield$kotlinx_coroutines_core", "(Ljava/lang/Object;)V", "resumeCancellable", "resumeCancellableWithException", "exception", "", "resumeCancelled", "", "resumeUndispatched", "resumeUndispatchedWithException", "resumeWith", "result", "Lkotlin/Result;", "takeState", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class DispatchedContinuation<T> implements Continuation<T>, DispatchedTask<T> {
    public final Continuation<T> continuation;
    public final CoroutineDispatcher dispatcher;
    private int resumeMode;
    public Object _state = DispatchedKt.UNDEFINED;
    public final Object countOrElement = ThreadContextKt.threadContextElements(getContext());

    public static /* synthetic */ void _state$annotations() {
    }

    @Override // kotlin.coroutines.Continuation
    public CoroutineContext getContext() {
        return this.continuation.getContext();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DispatchedContinuation(CoroutineDispatcher dispatcher, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(dispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        this.dispatcher = dispatcher;
        this.continuation = continuation;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public Throwable getExceptionalResult(Object state) {
        return DispatchedTask.DefaultImpls.getExceptionalResult(this, state);
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public <T> T getSuccessfulResult(Object state) {
        return (T) DispatchedTask.DefaultImpls.getSuccessfulResult(this, state);
    }

    @Override // kotlinx.coroutines.DispatchedTask, java.lang.Runnable
    public void run() {
        DispatchedTask.DefaultImpls.run(this);
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public int getResumeMode() {
        return this.resumeMode;
    }

    public void setResumeMode(int i) {
        this.resumeMode = i;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public Object takeState() {
        Object state = this._state;
        if (state != DispatchedKt.UNDEFINED) {
            this._state = DispatchedKt.UNDEFINED;
            return state;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public Continuation<T> getDelegate() {
        return this;
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(Object result) {
        CoroutineContext context = this.continuation.getContext();
        Object state = CompletedExceptionallyKt.toState(result);
        if (this.dispatcher.isDispatchNeeded(context)) {
            this._state = state;
            setResumeMode(0);
            this.dispatcher.dispatch(context, this);
            return;
        }
        UndispatchedEventLoop undispatchedEventLoop = UndispatchedEventLoop.INSTANCE;
        UndispatchedEventLoop.EventLoop eventLoop$iv = UndispatchedEventLoop.threadLocalEventLoop.get();
        if (eventLoop$iv.isActive) {
            this._state = state;
            setResumeMode(0);
            eventLoop$iv.queue.addLast(this);
            return;
        }
        Intrinsics.checkExpressionValueIsNotNull(eventLoop$iv, "eventLoop");
        try {
            eventLoop$iv.isActive = true;
            CoroutineContext context$iv = getContext();
            Object oldValue$iv = ThreadContextKt.updateThreadContext(context$iv, this.countOrElement);
            this.continuation.resumeWith(result);
            Unit unit = Unit.INSTANCE;
            ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            while (true) {
                Runnable nextEvent$iv$iv = eventLoop$iv.queue.removeFirstOrNull();
                if (nextEvent$iv$iv != null) {
                    nextEvent$iv$iv.run();
                } else {
                    return;
                }
            }
        } catch (Throwable e$iv$iv) {
            try {
                eventLoop$iv.queue.clear();
                throw new DispatchException("Unexpected exception in undispatched event loop, clearing pending tasks", e$iv$iv);
            } finally {
                eventLoop$iv.isActive = false;
            }
        }
    }

    /* JADX INFO: Multiple debug info for r21v0 'this'  kotlinx.coroutines.DispatchedContinuation<T>: [D('continuation$iv' kotlinx.coroutines.DispatchedContinuation), D('this_$iv' kotlinx.coroutines.DispatchedContinuation)] */
    public final void resumeCancellable(T t) {
        boolean z;
        Object countOrElement$iv$iv;
        Throwable th;
        if (this.dispatcher.isDispatchNeeded(getContext())) {
            this._state = t;
            setResumeMode(1);
            this.dispatcher.dispatch(getContext(), this);
            return;
        }
        UndispatchedEventLoop undispatchedEventLoop = UndispatchedEventLoop.INSTANCE;
        UndispatchedEventLoop.EventLoop eventLoop$iv = UndispatchedEventLoop.threadLocalEventLoop.get();
        if (eventLoop$iv.isActive) {
            this._state = t;
            setResumeMode(1);
            eventLoop$iv.queue.addLast(this);
            return;
        }
        Intrinsics.checkExpressionValueIsNotNull(eventLoop$iv, "eventLoop");
        try {
            eventLoop$iv.isActive = true;
            Job job$iv = (Job) getContext().get(Job.Key);
            if (job$iv == null || job$iv.isActive()) {
                z = false;
            } else {
                Result.Companion companion = Result.Companion;
                resumeWith(Result.m13constructorimpl(ResultKt.createFailure(job$iv.getCancellationException())));
                z = true;
            }
            if (!z) {
                CoroutineContext context$iv$iv = getContext();
                Object oldValue$iv$iv = ThreadContextKt.updateThreadContext(context$iv$iv, this.countOrElement);
                try {
                    Continuation<T> continuation = this.continuation;
                    Result.Companion companion2 = Result.Companion;
                    try {
                        continuation.resumeWith(Result.m13constructorimpl(t));
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ThreadContextKt.restoreThreadContext(context$iv$iv, oldValue$iv$iv);
                        InlineMarker.finallyEnd(1);
                    } catch (Throwable th2) {
                        th = th2;
                        countOrElement$iv$iv = oldValue$iv$iv;
                        InlineMarker.finallyStart(1);
                        ThreadContextKt.restoreThreadContext(context$iv$iv, countOrElement$iv$iv);
                        InlineMarker.finallyEnd(1);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    countOrElement$iv$iv = oldValue$iv$iv;
                }
            }
            while (true) {
                Runnable nextEvent$iv$iv = eventLoop$iv.queue.removeFirstOrNull();
                if (nextEvent$iv$iv != null) {
                    nextEvent$iv$iv.run();
                } else {
                    return;
                }
            }
        } catch (Throwable e$iv$iv) {
            try {
                eventLoop$iv.queue.clear();
                throw new DispatchException("Unexpected exception in undispatched event loop, clearing pending tasks", e$iv$iv);
            } finally {
                int i = 1;
                InlineMarker.finallyStart(i);
                eventLoop$iv.isActive = false;
                InlineMarker.finallyEnd(i);
            }
        }
    }

    /* JADX INFO: Multiple debug info for r24v0 'this'  kotlinx.coroutines.DispatchedContinuation<T>: [D('continuation$iv' kotlinx.coroutines.DispatchedContinuation), D('this_$iv' kotlinx.coroutines.DispatchedContinuation)] */
    public final void resumeCancellableWithException(Throwable exception) {
        boolean z;
        Object countOrElement$iv$iv;
        Throwable th;
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        CoroutineContext context = this.continuation.getContext();
        CompletedExceptionally state = new CompletedExceptionally(exception);
        if (this.dispatcher.isDispatchNeeded(context)) {
            this._state = new CompletedExceptionally(exception);
            setResumeMode(1);
            this.dispatcher.dispatch(context, this);
            return;
        }
        UndispatchedEventLoop undispatchedEventLoop = UndispatchedEventLoop.INSTANCE;
        UndispatchedEventLoop.EventLoop eventLoop$iv = UndispatchedEventLoop.threadLocalEventLoop.get();
        if (eventLoop$iv.isActive) {
            this._state = state;
            setResumeMode(1);
            eventLoop$iv.queue.addLast(this);
            return;
        }
        Intrinsics.checkExpressionValueIsNotNull(eventLoop$iv, "eventLoop");
        try {
            eventLoop$iv.isActive = true;
            Job job$iv = (Job) getContext().get(Job.Key);
            if (job$iv == null || job$iv.isActive()) {
                z = false;
            } else {
                Result.Companion companion = Result.Companion;
                resumeWith(Result.m13constructorimpl(ResultKt.createFailure(job$iv.getCancellationException())));
                z = true;
            }
            if (!z) {
                CoroutineContext context$iv$iv = getContext();
                Object oldValue$iv$iv = ThreadContextKt.updateThreadContext(context$iv$iv, this.countOrElement);
                try {
                    Continuation<T> continuation = this.continuation;
                    Result.Companion companion2 = Result.Companion;
                    try {
                        continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ThreadContextKt.restoreThreadContext(context$iv$iv, oldValue$iv$iv);
                        InlineMarker.finallyEnd(1);
                    } catch (Throwable th2) {
                        th = th2;
                        countOrElement$iv$iv = oldValue$iv$iv;
                        InlineMarker.finallyStart(1);
                        ThreadContextKt.restoreThreadContext(context$iv$iv, countOrElement$iv$iv);
                        InlineMarker.finallyEnd(1);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    countOrElement$iv$iv = oldValue$iv$iv;
                }
            }
            while (true) {
                Runnable nextEvent$iv$iv = eventLoop$iv.queue.removeFirstOrNull();
                if (nextEvent$iv$iv != null) {
                    nextEvent$iv$iv.run();
                } else {
                    return;
                }
            }
        } catch (Throwable e$iv$iv) {
            try {
                eventLoop$iv.queue.clear();
                throw new DispatchException("Unexpected exception in undispatched event loop, clearing pending tasks", e$iv$iv);
            } finally {
                int i = 1;
                InlineMarker.finallyStart(i);
                eventLoop$iv.isActive = false;
                InlineMarker.finallyEnd(i);
            }
        }
    }

    public final boolean resumeCancelled() {
        Job job = (Job) getContext().get(Job.Key);
        if (job == null || job.isActive()) {
            return false;
        }
        Result.Companion companion = Result.Companion;
        resumeWith(Result.m13constructorimpl(ResultKt.createFailure(job.getCancellationException())));
        return true;
    }

    public final void resumeUndispatched(T t) {
        CoroutineContext context$iv = getContext();
        Object oldValue$iv = ThreadContextKt.updateThreadContext(context$iv, this.countOrElement);
        try {
            Continuation<T> continuation = this.continuation;
            Result.Companion companion = Result.Companion;
            continuation.resumeWith(Result.m13constructorimpl(t));
            Unit unit = Unit.INSTANCE;
        } finally {
            InlineMarker.finallyStart(1);
            ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            InlineMarker.finallyEnd(1);
        }
    }

    public final void resumeUndispatchedWithException(Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        CoroutineContext context$iv = getContext();
        Object oldValue$iv = ThreadContextKt.updateThreadContext(context$iv, this.countOrElement);
        try {
            Continuation<T> continuation = this.continuation;
            Result.Companion companion = Result.Companion;
            continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
            Unit unit = Unit.INSTANCE;
        } finally {
            InlineMarker.finallyStart(1);
            ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            InlineMarker.finallyEnd(1);
        }
    }

    public final void dispatchYield$kotlinx_coroutines_core(T t) {
        CoroutineContext context = this.continuation.getContext();
        this._state = t;
        setResumeMode(1);
        this.dispatcher.dispatchYield(context, this);
    }

    @Override // java.lang.Object
    public String toString() {
        return "DispatchedContinuation[" + this.dispatcher + ", " + DebugKt.toDebugString(this.continuation) + ']';
    }
}
