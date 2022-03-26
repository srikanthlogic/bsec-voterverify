package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.ThreadContextKt;
/* compiled from: ResumeMode.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0003\n\u0002\b\u0002\u001a-\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132\u0006\u0010\u0014\u001a\u0002H\u00122\u0006\u0010\u0015\u001a\u00020\u0001H\u0000¢\u0006\u0002\u0010\u0016\u001a-\u0010\u0017\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132\u0006\u0010\u0014\u001a\u0002H\u00122\u0006\u0010\u0015\u001a\u00020\u0001H\u0000¢\u0006\u0002\u0010\u0016\u001a(\u0010\u0018\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u0001H\u0000\u001a(\u0010\u001b\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0012*\b\u0012\u0004\u0012\u0002H\u00120\u00132\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u0001H\u0000\"\u0016\u0010\u0000\u001a\u00020\u00018\u0000X\u0081T¢\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003\"\u0016\u0010\u0004\u001a\u00020\u00018\u0000X\u0081T¢\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0003\"\u0016\u0010\u0006\u001a\u00020\u00018\u0000X\u0081T¢\u0006\b\n\u0000\u0012\u0004\b\u0007\u0010\u0003\"\u0016\u0010\b\u001a\u00020\u00018\u0000X\u0081T¢\u0006\b\n\u0000\u0012\u0004\b\t\u0010\u0003\"\u0016\u0010\n\u001a\u00020\u00018\u0000X\u0081T¢\u0006\b\n\u0000\u0012\u0004\b\u000b\u0010\u0003\"\u0018\u0010\f\u001a\u00020\r*\u00020\u00018@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\u000e\"\u0018\u0010\u000f\u001a\u00020\r*\u00020\u00018@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u000e¨\u0006\u001c"}, d2 = {"MODE_ATOMIC_DEFAULT", "", "MODE_ATOMIC_DEFAULT$annotations", "()V", "MODE_CANCELLABLE", "MODE_CANCELLABLE$annotations", "MODE_DIRECT", "MODE_DIRECT$annotations", "MODE_IGNORE", "MODE_IGNORE$annotations", "MODE_UNDISPATCHED", "MODE_UNDISPATCHED$annotations", "isCancellableMode", "", "(I)Z", "isDispatchedMode", "resumeMode", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/coroutines/Continuation;", "value", "mode", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;I)V", "resumeUninterceptedMode", "resumeUninterceptedWithExceptionMode", "exception", "", "resumeWithExceptionMode", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class ResumeModeKt {
    public static final int MODE_ATOMIC_DEFAULT;
    public static final int MODE_CANCELLABLE;
    public static final int MODE_DIRECT;
    public static final int MODE_IGNORE;
    public static final int MODE_UNDISPATCHED;

    public static /* synthetic */ void MODE_ATOMIC_DEFAULT$annotations() {
    }

    public static /* synthetic */ void MODE_CANCELLABLE$annotations() {
    }

    public static /* synthetic */ void MODE_DIRECT$annotations() {
    }

    public static /* synthetic */ void MODE_IGNORE$annotations() {
    }

    public static /* synthetic */ void MODE_UNDISPATCHED$annotations() {
    }

    public static final boolean isCancellableMode(int $receiver) {
        return $receiver == 1;
    }

    public static final boolean isDispatchedMode(int $receiver) {
        return $receiver == 0 || $receiver == 1;
    }

    public static final <T> void resumeMode(Continuation<? super T> continuation, T t, int mode) {
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        if (mode == 0) {
            Result.Companion companion = Result.Companion;
            continuation.resumeWith(Result.m13constructorimpl(t));
        } else if (mode == 1) {
            DispatchedKt.resumeCancellable(continuation, t);
        } else if (mode == 2) {
            DispatchedKt.resumeDirect(continuation, t);
        } else if (mode == 3) {
            DispatchedContinuation this_$iv = (DispatchedContinuation) continuation;
            CoroutineContext context$iv$iv = this_$iv.getContext();
            Object oldValue$iv$iv = ThreadContextKt.updateThreadContext(context$iv$iv, this_$iv.countOrElement);
            try {
                Continuation<T> continuation2 = this_$iv.continuation;
                Result.Companion companion2 = Result.Companion;
                continuation2.resumeWith(Result.m13constructorimpl(t));
                Unit unit = Unit.INSTANCE;
            } finally {
                ThreadContextKt.restoreThreadContext(context$iv$iv, oldValue$iv$iv);
            }
        } else if (mode != 4) {
            throw new IllegalStateException(("Invalid mode " + mode).toString());
        }
    }

    public static final <T> void resumeWithExceptionMode(Continuation<? super T> continuation, Throwable exception, int mode) {
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (mode == 0) {
            Result.Companion companion = Result.Companion;
            continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
        } else if (mode == 1) {
            DispatchedKt.resumeCancellableWithException(continuation, exception);
        } else if (mode == 2) {
            DispatchedKt.resumeDirectWithException(continuation, exception);
        } else if (mode == 3) {
            DispatchedContinuation this_$iv = (DispatchedContinuation) continuation;
            CoroutineContext context$iv$iv = this_$iv.getContext();
            Object oldValue$iv$iv = ThreadContextKt.updateThreadContext(context$iv$iv, this_$iv.countOrElement);
            try {
                Continuation<T> continuation2 = this_$iv.continuation;
                Result.Companion companion2 = Result.Companion;
                continuation2.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
                Unit unit = Unit.INSTANCE;
            } finally {
                ThreadContextKt.restoreThreadContext(context$iv$iv, oldValue$iv$iv);
            }
        } else if (mode != 4) {
            throw new IllegalStateException(("Invalid mode " + mode).toString());
        }
    }

    public static final <T> void resumeUninterceptedMode(Continuation<? super T> continuation, T t, int mode) {
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        if (mode == 0) {
            Continuation intercepted = IntrinsicsKt.intercepted(continuation);
            Result.Companion companion = Result.Companion;
            intercepted.resumeWith(Result.m13constructorimpl(t));
        } else if (mode == 1) {
            DispatchedKt.resumeCancellable(IntrinsicsKt.intercepted(continuation), t);
        } else if (mode == 2) {
            Result.Companion companion2 = Result.Companion;
            continuation.resumeWith(Result.m13constructorimpl(t));
        } else if (mode == 3) {
            CoroutineContext context$iv = continuation.getContext();
            Object oldValue$iv = ThreadContextKt.updateThreadContext(context$iv, null);
            try {
                Result.Companion companion3 = Result.Companion;
                continuation.resumeWith(Result.m13constructorimpl(t));
                Unit unit = Unit.INSTANCE;
            } finally {
                ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            }
        } else if (mode != 4) {
            throw new IllegalStateException(("Invalid mode " + mode).toString());
        }
    }

    public static final <T> void resumeUninterceptedWithExceptionMode(Continuation<? super T> continuation, Throwable exception, int mode) {
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (mode == 0) {
            Continuation intercepted = IntrinsicsKt.intercepted(continuation);
            Result.Companion companion = Result.Companion;
            intercepted.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
        } else if (mode == 1) {
            DispatchedKt.resumeCancellableWithException(IntrinsicsKt.intercepted(continuation), exception);
        } else if (mode == 2) {
            Result.Companion companion2 = Result.Companion;
            continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
        } else if (mode == 3) {
            CoroutineContext context$iv = continuation.getContext();
            Object oldValue$iv = ThreadContextKt.updateThreadContext(context$iv, null);
            try {
                Result.Companion companion3 = Result.Companion;
                continuation.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(exception)));
                Unit unit = Unit.INSTANCE;
            } finally {
                ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            }
        } else if (mode != 4) {
            throw new IllegalStateException(("Invalid mode " + mode).toString());
        }
    }
}
