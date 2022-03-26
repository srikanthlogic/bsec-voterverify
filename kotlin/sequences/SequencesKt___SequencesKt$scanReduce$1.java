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
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\b\b\u0001\u0010\u0003*\u0002H\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", ExifInterface.LATITUDE_SOUTH, ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/SequenceScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 16})
@DebugMetadata(c = "kotlin.sequences.SequencesKt___SequencesKt$scanReduce$1", f = "_Sequences.kt", i = {0, 0, 0, 1, 1, 1}, l = {1492, 1495}, m = "invokeSuspend", n = {"$this$sequence", "iterator", "accumulator", "$this$sequence", "iterator", "accumulator"}, s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2"})
/* loaded from: classes3.dex */
final class SequencesKt___SequencesKt$scanReduce$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super S>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $operation;
    final /* synthetic */ Sequence $this_scanReduce;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private SequenceScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SequencesKt___SequencesKt$scanReduce$1(Sequence sequence, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_scanReduce = sequence;
        this.$operation = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        SequencesKt___SequencesKt$scanReduce$1 sequencesKt___SequencesKt$scanReduce$1 = new SequencesKt___SequencesKt$scanReduce$1(this.$this_scanReduce, this.$operation, continuation);
        SequenceScope sequenceScope = (SequenceScope) obj;
        sequencesKt___SequencesKt$scanReduce$1.p$ = (SequenceScope) obj;
        return sequencesKt___SequencesKt$scanReduce$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((SequencesKt___SequencesKt$scanReduce$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x006c  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object $result) {
        SequencesKt___SequencesKt$scanReduce$1 sequencesKt___SequencesKt$scanReduce$1;
        Object accumulator;
        Iterator iterator;
        Object accumulator2;
        SequenceScope $this$sequence;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure($result);
            $this$sequence = this.p$;
            iterator = this.$this_scanReduce.iterator();
            if (!iterator.hasNext()) {
                return Unit.INSTANCE;
            }
            Object accumulator3 = iterator.next();
            this.L$0 = $this$sequence;
            this.L$1 = iterator;
            this.L$2 = accumulator3;
            this.label = 1;
            if ($this$sequence.yield(accumulator3, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
            accumulator2 = accumulator3;
        } else if (i == 1) {
            accumulator2 = this.L$2;
            iterator = (Iterator) this.L$1;
            $this$sequence = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
        } else if (i == 2) {
            accumulator2 = this.L$2;
            iterator = (Iterator) this.L$1;
            $this$sequence = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
            accumulator = coroutine_suspended;
            sequencesKt___SequencesKt$scanReduce$1 = this;
            while (iterator.hasNext()) {
                accumulator2 = sequencesKt___SequencesKt$scanReduce$1.$operation.invoke(accumulator2, iterator.next());
                sequencesKt___SequencesKt$scanReduce$1.L$0 = $this$sequence;
                sequencesKt___SequencesKt$scanReduce$1.L$1 = iterator;
                sequencesKt___SequencesKt$scanReduce$1.L$2 = accumulator2;
                sequencesKt___SequencesKt$scanReduce$1.label = 2;
                if ($this$sequence.yield(accumulator2, sequencesKt___SequencesKt$scanReduce$1) == accumulator) {
                    return accumulator;
                }
            }
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        accumulator = coroutine_suspended;
        sequencesKt___SequencesKt$scanReduce$1 = this;
        while (iterator.hasNext()) {
        }
        return Unit.INSTANCE;
    }
}
