package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
/* compiled from: Timeout.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000\u001a_\u0010\u0006\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\b\"\b\b\u0001\u0010\t*\u0002H\b2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\t0\n2'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u00020\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\u000fH\u0002ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u001aH\u0010\u0011\u001a\u0002H\t\"\u0004\b\u0000\u0010\t2\u0006\u0010\u0012\u001a\u00020\u00032'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u00020\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\u000fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001aJ\u0010\u0014\u001a\u0004\u0018\u0001H\t\"\u0004\b\u0000\u0010\t2\u0006\u0010\u0012\u001a\u00020\u00032'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u00020\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\u000fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0013\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0015"}, d2 = {"TimeoutCancellationException", "Lkotlinx/coroutines/TimeoutCancellationException;", "time", "", "coroutine", "Lkotlinx/coroutines/Job;", "setupTimeout", "", "U", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlinx/coroutines/TimeoutCoroutine;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/TimeoutCoroutine;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "withTimeout", "timeMillis", "(JLkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "withTimeoutOrNull", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class TimeoutKt {
    public static final <T> Object withTimeout(long timeMillis, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, Continuation<? super T> continuation) {
        if (timeMillis > 0) {
            Object obj = setupTimeout(new TimeoutCoroutine(timeMillis, continuation), function2);
            if (obj == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return obj;
        }
        throw new CancellationException("Timed out immediately");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0026  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0094 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0095  */
    /* JADX WARN: Type inference failed for: r5v1, types: [kotlinx.coroutines.TimeoutCoroutine, T] */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final <T> Object withTimeoutOrNull(long timeMillis, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, Continuation<? super T> continuation) {
        TimeoutKt$withTimeoutOrNull$1 uCont;
        int i;
        TimeoutCancellationException e;
        Ref.ObjectRef coroutine;
        if (continuation instanceof TimeoutKt$withTimeoutOrNull$1) {
            uCont = (TimeoutKt$withTimeoutOrNull$1) continuation;
            if ((uCont.label & Integer.MIN_VALUE) != 0) {
                uCont.label -= Integer.MIN_VALUE;
                Object obj = uCont.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = uCont.label;
                if (i == 0) {
                    if (i == 1) {
                        Ref.ObjectRef coroutine2 = (Ref.ObjectRef) uCont.L$1;
                        Function2 block = (Function2) uCont.L$0;
                        long timeMillis2 = uCont.J$0;
                        try {
                            if (!(obj instanceof Result.Failure)) {
                                return obj;
                            }
                            throw ((Result.Failure) obj).exception;
                        } catch (TimeoutCancellationException e2) {
                            e = e2;
                            coroutine = coroutine2;
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (obj instanceof Result.Failure) {
                    throw ((Result.Failure) obj).exception;
                } else if (timeMillis <= 0) {
                    return null;
                } else {
                    coroutine = new Ref.ObjectRef();
                    coroutine.element = (T) null;
                    try {
                        uCont.J$0 = timeMillis;
                        uCont.L$0 = function2;
                        uCont.L$1 = coroutine;
                        uCont.label = 1;
                        ?? r5 = (T) new TimeoutCoroutine(timeMillis, uCont);
                        coroutine.element = r5;
                        Object obj2 = setupTimeout(r5, function2);
                        if (obj2 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            DebugProbesKt.probeCoroutineSuspended(uCont);
                        }
                        if (obj2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return obj2;
                    } catch (TimeoutCancellationException e3) {
                        e = e3;
                    }
                }
                if (e.coroutine != ((TimeoutCoroutine) coroutine.element)) {
                    return null;
                }
                throw e;
            }
        }
        uCont = new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.TimeoutKt$withTimeoutOrNull$1
            long J$0;
            Object L$0;
            Object L$1;
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj3) {
                this.result = obj3;
                this.label |= Integer.MIN_VALUE;
                return TimeoutKt.withTimeoutOrNull(0, null, this);
            }
        };
        Object obj3 = uCont.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = uCont.label;
        if (i == 0) {
        }
        if (e.coroutine != ((TimeoutCoroutine) coroutine.element)) {
        }
    }

    public static final <U, T extends U> Object setupTimeout(TimeoutCoroutine<U, ? super T> timeoutCoroutine, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) {
        JobKt.disposeOnCompletion(timeoutCoroutine, DelayKt.getDelay(timeoutCoroutine.uCont.getContext()).invokeOnTimeout(timeoutCoroutine.time, timeoutCoroutine));
        return UndispatchedKt.startUndispatchedOrReturn(timeoutCoroutine, timeoutCoroutine, function2);
    }

    public static final TimeoutCancellationException TimeoutCancellationException(long time, Job coroutine) {
        Intrinsics.checkParameterIsNotNull(coroutine, "coroutine");
        return new TimeoutCancellationException("Timed out waiting for " + time + " ms", coroutine);
    }
}
