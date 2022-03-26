package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequenceScope;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
/* compiled from: JobSupport.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Lkotlinx/coroutines/ChildJob;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/JobSupport$children$1", f = "JobSupport.kt", i = {0, 1, 1, 1, 1, 1}, l = {838, 842, 1323}, m = "invokeSuspend", n = {"state", "state", "list", "this_$iv", "cur$iv", "it"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5"})
/* loaded from: classes3.dex */
final class JobSupport$children$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super ChildJob>, Continuation<? super Unit>, Object> {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    int label;
    private SequenceScope p$;
    final /* synthetic */ JobSupport this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JobSupport$children$1(JobSupport jobSupport, Continuation continuation) {
        super(2, continuation);
        this.this$0 = jobSupport;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        JobSupport$children$1 jobSupport$children$1 = new JobSupport$children$1(this.this$0, continuation);
        SequenceScope sequenceScope = (SequenceScope) obj;
        jobSupport$children$1.p$ = (SequenceScope) obj;
        return jobSupport$children$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(SequenceScope<? super ChildJob> sequenceScope, Continuation<? super Unit> continuation) {
        return ((JobSupport$children$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00a8  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object result) {
        SequenceScope sequenceScope;
        NodeList list;
        NodeList this_$iv;
        LockFreeLinkedListNode cur$iv;
        Object obj;
        Object state;
        JobSupport$children$1 jobSupport$children$1;
        NodeList list2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                Object state2 = this.L$0;
                if (result instanceof Result.Failure) {
                    throw ((Result.Failure) result).exception;
                }
            } else if (i == 2) {
                ChildHandleNode it = (ChildHandleNode) this.L$5;
                cur$iv = (LockFreeLinkedListNode) this.L$4;
                this_$iv = (NodeList) this.L$3;
                list = (NodeList) this.L$2;
                state = this.L$1;
                sequenceScope = (SequenceScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    jobSupport$children$1 = this;
                    cur$iv = cur$iv.getNextNode();
                    if (!Intrinsics.areEqual(cur$iv, this_$iv)) {
                        if (cur$iv instanceof ChildHandleNode) {
                            ChildHandleNode it2 = (ChildHandleNode) cur$iv;
                            ChildJob childJob = it2.childJob;
                            jobSupport$children$1.L$0 = sequenceScope;
                            jobSupport$children$1.L$1 = state;
                            jobSupport$children$1.L$2 = list;
                            jobSupport$children$1.L$3 = this_$iv;
                            jobSupport$children$1.L$4 = cur$iv;
                            jobSupport$children$1.L$5 = it2;
                            jobSupport$children$1.label = 2;
                            if (sequenceScope.yield(childJob, jobSupport$children$1) == obj) {
                                return obj;
                            }
                        }
                        cur$iv = cur$iv.getNextNode();
                        if (!Intrinsics.areEqual(cur$iv, this_$iv)) {
                        }
                    }
                    return Unit.INSTANCE;
                }
                throw ((Result.Failure) result).exception;
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(result instanceof Result.Failure)) {
            SequenceScope sequenceScope2 = this.p$;
            state = this.this$0.getState$kotlinx_coroutines_core();
            if (state instanceof ChildHandleNode) {
                ChildJob childJob2 = ((ChildHandleNode) state).childJob;
                this.L$0 = state;
                this.label = 1;
                if (sequenceScope2.yield(childJob2, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else if ((state instanceof Incomplete) && (list2 = ((Incomplete) state).getList()) != null) {
                this_$iv = list2;
                Object next = this_$iv.getNext();
                if (next != null) {
                    cur$iv = (LockFreeLinkedListNode) next;
                    sequenceScope = sequenceScope2;
                    list = list2;
                    obj = coroutine_suspended;
                    jobSupport$children$1 = this;
                    if (!Intrinsics.areEqual(cur$iv, this_$iv)) {
                    }
                    return Unit.INSTANCE;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } else {
            throw ((Result.Failure) result).exception;
        }
        return Unit.INSTANCE;
    }
}
