package kotlin.coroutines.experimental.intrinsics;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
/* compiled from: IntrinsicsJvm.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a:\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u00072\u0010\b\u0004\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\fH\u0082\b¢\u0006\u0002\b\r\u001aD\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\"\u0004\b\u0000\u0010\t*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u000f2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u0007H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u001a]\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\"\u0004\b\u0000\u0010\u0011\"\u0004\b\u0001\u0010\t*#\b\u0001\u0012\u0004\u0012\u0002H\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0012¢\u0006\u0002\b\u00132\u0006\u0010\u0014\u001a\u0002H\u00112\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u0007H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001aA\u0010\u0016\u001a\u0004\u0018\u00010\u0001\"\u0004\b\u0000\u0010\t*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u000f2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u0007H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001aZ\u0010\u0016\u001a\u0004\u0018\u00010\u0001\"\u0004\b\u0000\u0010\u0011\"\u0004\b\u0001\u0010\t*#\b\u0001\u0012\u0004\u0012\u0002H\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0012¢\u0006\u0002\b\u00132\u0006\u0010\u0014\u001a\u0002H\u00112\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u0007H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0018\"\u001a\u0010\u0000\u001a\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\u0082\u0002\u0004\n\u0002\b\t¨\u0006\u0019"}, d2 = {"COROUTINE_SUSPENDED", "", "COROUTINE_SUSPENDED$annotations", "()V", "getCOROUTINE_SUSPENDED", "()Ljava/lang/Object;", "buildContinuationByInvokeCall", "Lkotlin/coroutines/experimental/Continuation;", "", ExifInterface.GPS_DIRECTION_TRUE, "completion", "block", "Lkotlin/Function0;", "buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnchecked", "Lkotlin/Function1;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Lkotlin/coroutines/experimental/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Lkotlin/coroutines/experimental/Continuation;", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlin-stdlib-coroutines"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/coroutines/experimental/intrinsics/IntrinsicsKt")
/* loaded from: classes3.dex */
class IntrinsicsKt__IntrinsicsJvmKt {
    public static /* synthetic */ void COROUTINE_SUSPENDED$annotations() {
    }

    private static final <T> Object startCoroutineUninterceptedOrReturn(Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        if (function1 != null) {
            return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(continuation);
        }
        throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
    }

    private static final <R, T> Object startCoroutineUninterceptedOrReturn(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        if (function2 != null) {
            return ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, continuation);
        }
        throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
    }

    public static final <T> Continuation<Unit> createCoroutineUnchecked(Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function1, "$this$createCoroutineUnchecked");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        if (!(function1 instanceof CoroutineImpl)) {
            return CoroutineIntrinsics.interceptContinuationIfNeeded(continuation.getContext(), new Continuation<Unit>(function1, continuation) { // from class: kotlin.coroutines.experimental.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnchecked$$inlined$buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt$1
                final /* synthetic */ Continuation $completion$inlined;
                final /* synthetic */ Function1 $this_createCoroutineUnchecked$inlined;

                {
                    this.$this_createCoroutineUnchecked$inlined = r2;
                    this.$completion$inlined = r3;
                }

                @Override // kotlin.coroutines.experimental.Continuation
                public CoroutineContext getContext() {
                    return Continuation.this.getContext();
                }

                public void resume(Unit value) {
                    Intrinsics.checkParameterIsNotNull(value, "value");
                    Continuation continuation2 = Continuation.this;
                    try {
                        Function1 function12 = this.$this_createCoroutineUnchecked$inlined;
                        if (function12 != null) {
                            Object invoke = ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function12, 1)).invoke(this.$completion$inlined);
                            if (invoke == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                                return;
                            }
                            if (continuation2 != null) {
                                continuation2.resume(invoke);
                                return;
                            }
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
                        }
                        throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
                    } catch (Throwable th) {
                        continuation2.resumeWithException(th);
                    }
                }

                @Override // kotlin.coroutines.experimental.Continuation
                public void resumeWithException(Throwable exception) {
                    Intrinsics.checkParameterIsNotNull(exception, "exception");
                    Continuation.this.resumeWithException(exception);
                }
            });
        }
        Continuation<Unit> create = ((CoroutineImpl) function1).create(continuation);
        if (create != null) {
            return ((CoroutineImpl) create).getFacade();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
    }

    public static final <R, T> Continuation<Unit> createCoroutineUnchecked(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$this$createCoroutineUnchecked");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        if (!(function2 instanceof CoroutineImpl)) {
            return CoroutineIntrinsics.interceptContinuationIfNeeded(continuation.getContext(), new Continuation<Unit>(function2, r, continuation) { // from class: kotlin.coroutines.experimental.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnchecked$$inlined$buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt$2
                final /* synthetic */ Continuation $completion$inlined;
                final /* synthetic */ Object $receiver$inlined;
                final /* synthetic */ Function2 $this_createCoroutineUnchecked$inlined;

                {
                    this.$this_createCoroutineUnchecked$inlined = r2;
                    this.$receiver$inlined = r3;
                    this.$completion$inlined = r4;
                }

                @Override // kotlin.coroutines.experimental.Continuation
                public CoroutineContext getContext() {
                    return Continuation.this.getContext();
                }

                public void resume(Unit value) {
                    Intrinsics.checkParameterIsNotNull(value, "value");
                    Continuation continuation2 = Continuation.this;
                    try {
                        Function2 function22 = this.$this_createCoroutineUnchecked$inlined;
                        if (function22 != null) {
                            Object invoke = ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function22, 2)).invoke(this.$receiver$inlined, this.$completion$inlined);
                            if (invoke == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                                return;
                            }
                            if (continuation2 != null) {
                                continuation2.resume(invoke);
                                return;
                            }
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
                        }
                        throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
                    } catch (Throwable th) {
                        continuation2.resumeWithException(th);
                    }
                }

                @Override // kotlin.coroutines.experimental.Continuation
                public void resumeWithException(Throwable exception) {
                    Intrinsics.checkParameterIsNotNull(exception, "exception");
                    Continuation.this.resumeWithException(exception);
                }
            });
        }
        Continuation<Unit> create = ((CoroutineImpl) function2).create(r, continuation);
        if (create != null) {
            return ((CoroutineImpl) create).getFacade();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
    }

    private static final <T> Continuation<Unit> buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt(Continuation<? super T> continuation, Function0<? extends Object> function0) {
        return CoroutineIntrinsics.interceptContinuationIfNeeded(continuation.getContext(), new Continuation<Unit>(function0) { // from class: kotlin.coroutines.experimental.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$buildContinuationByInvokeCall$continuation$1
            final /* synthetic */ Function0 $block;

            {
                this.$block = $captured_local_variable$1;
            }

            @Override // kotlin.coroutines.experimental.Continuation
            public CoroutineContext getContext() {
                return Continuation.this.getContext();
            }

            public void resume(Unit value) {
                Intrinsics.checkParameterIsNotNull(value, "value");
                Continuation continuation2 = Continuation.this;
                try {
                    Object invoke = this.$block.invoke();
                    if (invoke == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                        return;
                    }
                    if (continuation2 != null) {
                        continuation2.resume(invoke);
                        return;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
                } catch (Throwable th) {
                    continuation2.resumeWithException(th);
                }
            }

            @Override // kotlin.coroutines.experimental.Continuation
            public void resumeWithException(Throwable exception) {
                Intrinsics.checkParameterIsNotNull(exception, "exception");
                Continuation.this.resumeWithException(exception);
            }
        });
    }

    public static final Object getCOROUTINE_SUSPENDED() {
        return IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }
}
