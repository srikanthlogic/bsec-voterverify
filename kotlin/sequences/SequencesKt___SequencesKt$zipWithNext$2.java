package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: _Sequences.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "R", "Lkotlin/sequences/SequenceScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 16})
@DebugMetadata(c = "kotlin.sequences.SequencesKt___SequencesKt$zipWithNext$2", f = "_Sequences.kt", i = {0, 0, 0, 0}, l = {1871}, m = "invokeSuspend", n = {"$this$result", "iterator", "current", "next"}, s = {"L$0", "L$1", "L$2", "L$3"})
/* loaded from: classes3.dex */
final class SequencesKt___SequencesKt$zipWithNext$2 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Sequence $this_zipWithNext;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private SequenceScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SequencesKt___SequencesKt$zipWithNext$2(Sequence sequence, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_zipWithNext = sequence;
        this.$transform = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        SequencesKt___SequencesKt$zipWithNext$2 sequencesKt___SequencesKt$zipWithNext$2 = new SequencesKt___SequencesKt$zipWithNext$2(this.$this_zipWithNext, this.$transform, continuation);
        SequenceScope sequenceScope = (SequenceScope) obj;
        sequencesKt___SequencesKt$zipWithNext$2.p$ = (SequenceScope) obj;
        return sequencesKt___SequencesKt$zipWithNext$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((SequencesKt___SequencesKt$zipWithNext$2) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0072  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object $result) {
        SequencesKt___SequencesKt$zipWithNext$2 sequencesKt___SequencesKt$zipWithNext$2;
        SequenceScope $this$result;
        Iterator iterator;
        Object current;
        Object next;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure($result);
            SequenceScope $this$result2 = this.p$;
            Iterator iterator2 = this.$this_zipWithNext.iterator();
            if (!iterator2.hasNext()) {
                return Unit.INSTANCE;
            }
            current = iterator2.next();
            next = coroutine_suspended;
            sequencesKt___SequencesKt$zipWithNext$2 = this;
            $this$result = $this$result2;
            iterator = iterator2;
            if (iterator.hasNext()) {
            }
        } else if (i == 1) {
            Object next2 = this.L$3;
            Object current2 = this.L$2;
            iterator = (Iterator) this.L$1;
            $this$result = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
            Object next3 = coroutine_suspended;
            sequencesKt___SequencesKt$zipWithNext$2 = this;
            current = next2;
            next = next3;
            if (iterator.hasNext()) {
                Object next4 = iterator.next();
                Object invoke = sequencesKt___SequencesKt$zipWithNext$2.$transform.invoke(current, next4);
                sequencesKt___SequencesKt$zipWithNext$2.L$0 = $this$result;
                sequencesKt___SequencesKt$zipWithNext$2.L$1 = iterator;
                sequencesKt___SequencesKt$zipWithNext$2.L$2 = current;
                sequencesKt___SequencesKt$zipWithNext$2.L$3 = next4;
                sequencesKt___SequencesKt$zipWithNext$2.label = 1;
                if ($this$result.yield(invoke, sequencesKt___SequencesKt$zipWithNext$2) == next) {
                    return next;
                }
                next3 = next;
                next2 = next4;
                current = next2;
                next = next3;
                if (iterator.hasNext()) {
                    return Unit.INSTANCE;
                }
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
