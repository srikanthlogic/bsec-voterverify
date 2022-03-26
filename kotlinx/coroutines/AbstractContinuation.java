package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DispatchedTask;
import kotlinx.coroutines.Job;
/* compiled from: AbstractContinuation.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0001\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\b \u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u001b\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\u001b\u001a\u00020\u00102\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dJ\u0010\u0010\u001e\u001a\u00020\u00102\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dJ\"\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\f2\u0006\u0010$\u001a\u00020\u0006H\u0004J\u0010\u0010%\u001a\u00020 2\u0006\u0010$\u001a\u00020\u0006H\u0002J\u0010\u0010&\u001a\u00020\u001d2\u0006\u0010'\u001a\u00020(H\u0016J\n\u0010)\u001a\u0004\u0018\u00010\fH\u0001J\u0010\u0010*\u001a\u00020 2\u0006\u0010+\u001a\u00020\u001dH\u0002J\u0017\u0010,\u001a\u00020 2\b\u0010'\u001a\u0004\u0018\u00010(H\u0000¢\u0006\u0002\b-J/\u0010.\u001a\u00020 2'\u0010/\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u001d¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(\u001c\u0012\u0004\u0012\u00020 00j\u0002`3J\u001f\u00104\u001a\u0002052\u0014\u00106\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\f\u0012\u0004\u0012\u00020 00H\u0084\bJ1\u00107\u001a\u0002082'\u0010/\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u001d¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(\u001c\u0012\u0004\u0012\u00020 00j\u0002`3H\u0002J\b\u00109\u001a\u00020:H\u0014J\u001a\u0010;\u001a\u00020 2\b\u0010<\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0004J\u001e\u0010=\u001a\u00020 2\f\u0010>\u001a\b\u0012\u0004\u0012\u00028\u00000?H\u0016ø\u0001\u0000¢\u0006\u0002\u0010@J\u001d\u0010A\u001a\u00020 2\u0006\u0010+\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u0006H\u0000¢\u0006\u0002\bBJ\b\u0010C\u001a\u00020:H\u0002J\n\u0010D\u001a\u0004\u0018\u00010\fH\u0016J\b\u0010E\u001a\u00020:H\u0016J\b\u0010F\u001a\u00020\u0010H\u0002J\b\u0010G\u001a\u00020\u0010H\u0002J\u001a\u0010H\u001a\u00020\u00102\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\fH\u0004J\"\u0010I\u001a\u00020\u00102\u0006\u0010!\u001a\u00020\"2\b\u0010<\u001a\u0004\u0018\u00010\f2\u0006\u0010$\u001a\u00020\u0006H\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0013\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0011R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0016\u0010\u0018\u001a\u0004\u0018\u00010\f8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006J"}, d2 = {"Lkotlinx/coroutines/AbstractContinuation;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/coroutines/Continuation;", "Lkotlinx/coroutines/DispatchedTask;", "delegate", "resumeMode", "", "(Lkotlin/coroutines/Continuation;I)V", "_decision", "Lkotlinx/atomicfu/AtomicInt;", "_state", "Lkotlinx/atomicfu/AtomicRef;", "", "getDelegate", "()Lkotlin/coroutines/Continuation;", "isActive", "", "()Z", "isCancelled", "isCompleted", "parentHandle", "Lkotlinx/coroutines/DisposableHandle;", "getResumeMode", "()I", "state", "getState$kotlinx_coroutines_core", "()Ljava/lang/Object;", "cancel", "cause", "", "cancelImpl", "completeStateUpdate", "", "expect", "Lkotlinx/coroutines/NotCompleted;", "update", "mode", "dispatchResume", "getContinuationCancellationCause", "parent", "Lkotlinx/coroutines/Job;", "getResult", "handleException", "exception", "initParentJobInternal", "initParentJobInternal$kotlinx_coroutines_core", "invokeOnCancellation", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "Lkotlinx/coroutines/CompletionHandler;", "loopOnState", "", "block", "makeHandler", "Lkotlinx/coroutines/CancelHandler;", "nameString", "", "resumeImpl", "proposedUpdate", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "resumeWithExceptionMode", "resumeWithExceptionMode$kotlinx_coroutines_core", "stateString", "takeState", "toString", "tryResume", "trySuspend", "tryUpdateStateToFinal", "updateStateToFinal", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public abstract class AbstractContinuation<T> implements Continuation<T>, DispatchedTask<T> {
    private static final AtomicIntegerFieldUpdater _decision$FU = AtomicIntegerFieldUpdater.newUpdater(AbstractContinuation.class, "_decision");
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(AbstractContinuation.class, Object.class, "_state");
    private volatile int _decision = 0;
    private volatile Object _state = AbstractContinuationKt.ACTIVE;
    private final Continuation<T> delegate;
    private volatile DisposableHandle parentHandle;
    private final int resumeMode;

    /* JADX WARN: Multi-variable type inference failed */
    public AbstractContinuation(Continuation<? super T> continuation, int resumeMode) {
        Intrinsics.checkParameterIsNotNull(continuation, "delegate");
        this.delegate = continuation;
        this.resumeMode = resumeMode;
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
    public final Continuation<T> getDelegate() {
        return this.delegate;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final int getResumeMode() {
        return this.resumeMode;
    }

    public final Object getState$kotlinx_coroutines_core() {
        return this._state;
    }

    public final boolean isActive() {
        return getState$kotlinx_coroutines_core() instanceof NotCompleted;
    }

    public final boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof NotCompleted);
    }

    public final boolean isCancelled() {
        return getState$kotlinx_coroutines_core() instanceof CancelledContinuation;
    }

    public final void initParentJobInternal$kotlinx_coroutines_core(Job parent) {
        boolean z = false;
        if (this.parentHandle == null) {
            z = true;
        }
        if (!z) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (parent == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
        } else {
            parent.start();
            DisposableHandle handle = Job.DefaultImpls.invokeOnCompletion$default(parent, true, false, new ChildContinuation(parent, this), 2, null);
            this.parentHandle = handle;
            if (isCompleted()) {
                handle.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
        }
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public Object takeState() {
        return getState$kotlinx_coroutines_core();
    }

    public final boolean cancel(Throwable cause) {
        return cancelImpl(cause);
    }

    public final boolean cancelImpl(Throwable cause) {
        Object state;
        do {
            state = getState$kotlinx_coroutines_core();
            if (!(state instanceof NotCompleted)) {
                return false;
            }
        } while (!updateStateToFinal((NotCompleted) state, new CancelledContinuation(this, cause), 0));
        return true;
    }

    public Throwable getContinuationCancellationCause(Job parent) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        return parent.getCancellationException();
    }

    private final boolean trySuspend() {
        do {
            int decision = this._decision;
            if (decision != 0) {
                if (decision == 2) {
                    return false;
                }
                throw new IllegalStateException("Already suspended".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 1));
        return true;
    }

    private final boolean tryResume() {
        do {
            int decision = this._decision;
            if (decision != 0) {
                if (decision == 1) {
                    return false;
                }
                throw new IllegalStateException("Already resumed".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 2));
        return true;
    }

    public final Object getResult() {
        if (trySuspend()) {
            return IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        Object state = getState$kotlinx_coroutines_core();
        if (!(state instanceof CompletedExceptionally)) {
            return getSuccessfulResult(state);
        }
        throw ((CompletedExceptionally) state).cause;
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(Object result) {
        resumeImpl(CompletedExceptionallyKt.toState(result), this.resumeMode);
    }

    public final void resumeWithExceptionMode$kotlinx_coroutines_core(Throwable exception, int mode) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        resumeImpl(new CompletedExceptionally(exception), mode);
    }

    public final void invokeOnCancellation(Function1<? super Throwable, Unit> function1) {
        Object obj;
        CancelHandler cancelHandler;
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        Throwable cause$iv = null;
        CancelHandler node = null;
        Object handleCache = null;
        while (true) {
            Object state = getState$kotlinx_coroutines_core();
            if (state instanceof Active) {
                if (node != null) {
                    obj = handleCache;
                    cancelHandler = node;
                } else {
                    CancelHandler it = makeHandler(function1);
                    cancelHandler = it;
                    node = it;
                    obj = handleCache;
                }
                if (!_state$FU.compareAndSet(this, state, node)) {
                    node = cancelHandler;
                    handleCache = obj;
                } else {
                    return;
                }
            } else if (state instanceof CancelHandler) {
                throw new IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + function1 + ", already has " + state).toString());
            } else if (state instanceof CancelledContinuation) {
                CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(state instanceof CompletedExceptionally) ? null : state);
                if (completedExceptionally != null) {
                    cause$iv = completedExceptionally.cause;
                }
                function1.invoke(cause$iv);
                return;
            } else {
                return;
            }
        }
    }

    private final CancelHandler makeHandler(Function1<? super Throwable, Unit> function1) {
        return function1 instanceof CancelHandler ? (CancelHandler) function1 : new InvokeOnCancel(function1);
    }

    private final void dispatchResume(int mode) {
        if (!tryResume()) {
            DispatchedKt.dispatch(this, mode);
        }
    }

    protected final Void loopOnState(Function1<Object, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        while (true) {
            function1.invoke(getState$kotlinx_coroutines_core());
        }
    }

    protected final void resumeImpl(Object proposedUpdate, int resumeMode) {
        Object state;
        do {
            state = getState$kotlinx_coroutines_core();
            if (!(state instanceof NotCompleted)) {
                if (!(state instanceof CancelledContinuation)) {
                    throw new IllegalStateException(("Already resumed, but proposed with update " + proposedUpdate).toString());
                } else if (proposedUpdate instanceof CompletedExceptionally) {
                    handleException(((CompletedExceptionally) proposedUpdate).cause);
                    return;
                } else {
                    return;
                }
            }
        } while (!updateStateToFinal((NotCompleted) state, proposedUpdate, resumeMode));
    }

    private final boolean updateStateToFinal(NotCompleted expect, Object proposedUpdate, int mode) {
        if (!tryUpdateStateToFinal(expect, proposedUpdate)) {
            return false;
        }
        completeStateUpdate(expect, proposedUpdate, mode);
        return true;
    }

    protected final boolean tryUpdateStateToFinal(NotCompleted expect, Object update) {
        Intrinsics.checkParameterIsNotNull(expect, "expect");
        if (!(!(update instanceof NotCompleted))) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        } else if (!_state$FU.compareAndSet(this, expect, update)) {
            return false;
        } else {
            DisposableHandle it = this.parentHandle;
            if (it != null) {
                it.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
            return true;
        }
    }

    protected final void completeStateUpdate(NotCompleted expect, Object update, int mode) {
        Intrinsics.checkParameterIsNotNull(expect, "expect");
        Throwable th = null;
        CompletedExceptionally exceptionally = (CompletedExceptionally) (!(update instanceof CompletedExceptionally) ? null : update);
        if ((update instanceof CancelledContinuation) && (expect instanceof CancelHandler)) {
            try {
                CancelHandler cancelHandler = (CancelHandler) expect;
                if (exceptionally != null) {
                    th = exceptionally.cause;
                }
                cancelHandler.invoke(th);
            } catch (Throwable ex) {
                handleException(new CompletionHandlerException("Exception in completion handler " + expect + " for " + this, ex));
            }
        }
        dispatchResume(mode);
    }

    private final void handleException(Throwable exception) {
        CoroutineExceptionHandlerKt.handleCoroutineException$default(getContext(), exception, null, 4, null);
    }

    @Override // java.lang.Object
    public String toString() {
        return nameString() + '{' + stateString() + "}@" + DebugKt.getHexAddress(this);
    }

    protected String nameString() {
        return DebugKt.getClassSimpleName(this);
    }

    private final String stateString() {
        Object state = getState$kotlinx_coroutines_core();
        if (state instanceof NotCompleted) {
            return "Active";
        }
        if (state instanceof CancelledContinuation) {
            return "Cancelled";
        }
        if (state instanceof CompletedExceptionally) {
            return "CompletedExceptionally";
        }
        return "Completed";
    }
}
