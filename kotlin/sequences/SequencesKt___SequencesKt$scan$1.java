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
@DebugMetadata(c = "kotlin.sequences.SequencesKt___SequencesKt$scan$1", f = "_Sequences.kt", i = {0, 1, 1, 1}, l = {1433, 1437}, m = "invokeSuspend", n = {"$this$sequence", "$this$sequence", "accumulator", "element"}, s = {"L$0", "L$0", "L$1", "L$2"})
/* loaded from: classes3.dex */
final class SequencesKt___SequencesKt$scan$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Object $initial;
    final /* synthetic */ Function2 $operation;
    final /* synthetic */ Sequence $this_scan;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private SequenceScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SequencesKt___SequencesKt$scan$1(Sequence sequence, Object obj, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_scan = sequence;
        this.$initial = obj;
        this.$operation = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        SequencesKt___SequencesKt$scan$1 sequencesKt___SequencesKt$scan$1 = new SequencesKt___SequencesKt$scan$1(this.$this_scan, this.$initial, this.$operation, continuation);
        SequenceScope sequenceScope = (SequenceScope) obj;
        sequencesKt___SequencesKt$scan$1.p$ = (SequenceScope) obj;
        return sequencesKt___SequencesKt$scan$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((SequencesKt___SequencesKt$scan$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x007d  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object $result) {
        SequencesKt___SequencesKt$scan$1 sequencesKt___SequencesKt$scan$1;
        Iterator it;
        SequenceScope $this$sequence;
        Object accumulator;
        Object accumulator2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure($result);
            $this$sequence = this.p$;
            Object obj = this.$initial;
            this.L$0 = $this$sequence;
            this.label = 1;
            if ($this$sequence.yield(obj, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
            $this$sequence = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
        } else if (i == 2) {
            it = (Iterator) this.L$3;
            Object element = this.L$2;
            Object accumulator3 = this.L$1;
            $this$sequence = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
            Object element2 = coroutine_suspended;
            sequencesKt___SequencesKt$scan$1 = this;
            accumulator = accumulator3;
            accumulator2 = element2;
            if (it.hasNext()) {
                Object element3 = it.next();
                Object accumulator4 = sequencesKt___SequencesKt$scan$1.$operation.invoke(accumulator, element3);
                sequencesKt___SequencesKt$scan$1.L$0 = $this$sequence;
                sequencesKt___SequencesKt$scan$1.L$1 = accumulator4;
                sequencesKt___SequencesKt$scan$1.L$2 = element3;
                sequencesKt___SequencesKt$scan$1.L$3 = it;
                sequencesKt___SequencesKt$scan$1.label = 2;
                if ($this$sequence.yield(accumulator4, sequencesKt___SequencesKt$scan$1) == accumulator2) {
                    return accumulator2;
                }
                accumulator3 = accumulator4;
                element2 = accumulator2;
                accumulator = accumulator3;
                accumulator2 = element2;
                if (it.hasNext()) {
                    return Unit.INSTANCE;
                }
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        accumulator = this.$initial;
        it = this.$this_scan.iterator();
        accumulator2 = coroutine_suspended;
        sequencesKt___SequencesKt$scan$1 = this;
        if (it.hasNext()) {
        }
    }
}
