package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.selects.SelectClause1;
import kotlinx.coroutines.selects.SelectInstance;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Builders.common.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0012\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u0002H\u00010\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0011\u0010\u0010\u001a\u00028\u0000H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0011J\r\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013JH\u0010\u0014\u001a\u00020\u0015\"\u0004\b\u0001\u0010\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u00182\"\u0010\u0019\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00160\u001b\u0012\u0006\u0012\u0004\u0018\u00010\u001c0\u001aH\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u001dR\u0014\u0010\n\u001a\u00020\b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/DeferredCoroutine;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlinx/coroutines/AbstractCoroutine;", "Lkotlinx/coroutines/Deferred;", "Lkotlinx/coroutines/selects/SelectClause1;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", AppMeasurementSdk.ConditionalUserProperty.ACTIVE, "", "(Lkotlin/coroutines/CoroutineContext;Z)V", "cancelsParent", "getCancelsParent", "()Z", "onAwait", "getOnAwait", "()Lkotlinx/coroutines/selects/SelectClause1;", "await", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCompleted", "()Ljava/lang/Object;", "registerSelectClause1", "", "R", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public class DeferredCoroutine<T> extends AbstractCoroutine<T> implements Deferred<T>, SelectClause1<T> {
    @Override // kotlinx.coroutines.Deferred
    public Object await(Continuation<? super T> continuation) {
        return await$suspendImpl(this, continuation);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeferredCoroutine(CoroutineContext parentContext, boolean active) {
        super(parentContext, active);
        Intrinsics.checkParameterIsNotNull(parentContext, "parentContext");
    }

    @Override // kotlinx.coroutines.JobSupport
    protected boolean getCancelsParent() {
        return true;
    }

    @Override // kotlinx.coroutines.Deferred
    public T getCompleted() {
        return (T) getCompletedInternal$kotlinx_coroutines_core();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static /* synthetic */ Object await$suspendImpl(DeferredCoroutine deferredCoroutine, Continuation continuation) {
        DeferredCoroutine$await$1 deferredCoroutine$await$1;
        int i;
        if (continuation instanceof DeferredCoroutine$await$1) {
            deferredCoroutine$await$1 = (DeferredCoroutine$await$1) continuation;
            if ((deferredCoroutine$await$1.label & Integer.MIN_VALUE) != 0) {
                deferredCoroutine$await$1.label -= Integer.MIN_VALUE;
                Object obj = deferredCoroutine$await$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = deferredCoroutine$await$1.label;
                if (i == 0) {
                    if (i == 1) {
                        DeferredCoroutine deferredCoroutine2 = (DeferredCoroutine) deferredCoroutine$await$1.L$0;
                        if (obj instanceof Result.Failure) {
                            throw ((Result.Failure) obj).exception;
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (!(obj instanceof Result.Failure)) {
                    deferredCoroutine$await$1.L$0 = deferredCoroutine;
                    deferredCoroutine$await$1.label = 1;
                    obj = deferredCoroutine.awaitInternal$kotlinx_coroutines_core(deferredCoroutine$await$1);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    throw ((Result.Failure) obj).exception;
                }
                return obj;
            }
        }
        deferredCoroutine$await$1 = new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.DeferredCoroutine$await$1
            Object L$0;
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj2) {
                this.result = obj2;
                this.label |= Integer.MIN_VALUE;
                return DeferredCoroutine.await$suspendImpl(DeferredCoroutine.this, this);
            }
        };
        Object obj2 = deferredCoroutine$await$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = deferredCoroutine$await$1.label;
        if (i == 0) {
        }
        return obj2;
    }

    @Override // kotlinx.coroutines.Deferred
    public SelectClause1<T> getOnAwait() {
        return this;
    }

    @Override // kotlinx.coroutines.selects.SelectClause1
    public <R> void registerSelectClause1(SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        registerSelectClause1Internal$kotlinx_coroutines_core(selectInstance, function2);
    }
}
