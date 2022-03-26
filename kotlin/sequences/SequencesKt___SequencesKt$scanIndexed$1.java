package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: _Sequences.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "R", "Lkotlin/sequences/SequenceScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 16})
@DebugMetadata(c = "kotlin.sequences.SequencesKt___SequencesKt$scanIndexed$1", f = "_Sequences.kt", i = {0, 1, 1, 1, 1}, l = {1462, 1467}, m = "invokeSuspend", n = {"$this$sequence", "$this$sequence", FirebaseAnalytics.Param.INDEX, "accumulator", "element"}, s = {"L$0", "L$0", "I$0", "L$1", "L$2"})
/* loaded from: classes3.dex */
final class SequencesKt___SequencesKt$scanIndexed$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Object $initial;
    final /* synthetic */ Function3 $operation;
    final /* synthetic */ Sequence $this_scanIndexed;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private SequenceScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SequencesKt___SequencesKt$scanIndexed$1(Sequence sequence, Object obj, Function3 function3, Continuation continuation) {
        super(2, continuation);
        this.$this_scanIndexed = sequence;
        this.$initial = obj;
        this.$operation = function3;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        SequencesKt___SequencesKt$scanIndexed$1 sequencesKt___SequencesKt$scanIndexed$1 = new SequencesKt___SequencesKt$scanIndexed$1(this.$this_scanIndexed, this.$initial, this.$operation, continuation);
        SequenceScope sequenceScope = (SequenceScope) obj;
        sequencesKt___SequencesKt$scanIndexed$1.p$ = (SequenceScope) obj;
        return sequencesKt___SequencesKt$scanIndexed$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((SequencesKt___SequencesKt$scanIndexed$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x008d  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object $result) {
        SequencesKt___SequencesKt$scanIndexed$1 sequencesKt___SequencesKt$scanIndexed$1;
        Iterator it;
        Object accumulator;
        SequenceScope $this$sequence;
        int index;
        Object obj;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure($result);
            $this$sequence = this.p$;
            Object obj2 = this.$initial;
            this.L$0 = $this$sequence;
            this.label = 1;
            if ($this$sequence.yield(obj2, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
            $this$sequence = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
        } else if (i == 2) {
            it = (Iterator) this.L$3;
            Object element = this.L$2;
            accumulator = this.L$1;
            int index2 = this.I$0;
            $this$sequence = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
            Object element2 = coroutine_suspended;
            sequencesKt___SequencesKt$scanIndexed$1 = this;
            index = index2;
            obj = element2;
            if (it.hasNext()) {
                Object element3 = it.next();
                Function3 function3 = sequencesKt___SequencesKt$scanIndexed$1.$operation;
                int index3 = index + 1;
                if (index < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                accumulator = function3.invoke(Boxing.boxInt(index), accumulator, element3);
                sequencesKt___SequencesKt$scanIndexed$1.L$0 = $this$sequence;
                sequencesKt___SequencesKt$scanIndexed$1.I$0 = index3;
                sequencesKt___SequencesKt$scanIndexed$1.L$1 = accumulator;
                sequencesKt___SequencesKt$scanIndexed$1.L$2 = element3;
                sequencesKt___SequencesKt$scanIndexed$1.L$3 = it;
                sequencesKt___SequencesKt$scanIndexed$1.label = 2;
                if ($this$sequence.yield(accumulator, sequencesKt___SequencesKt$scanIndexed$1) == obj) {
                    return obj;
                }
                element2 = obj;
                index2 = index3;
                index = index2;
                obj = element2;
                if (it.hasNext()) {
                    return Unit.INSTANCE;
                }
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        index = 0;
        accumulator = this.$initial;
        it = this.$this_scanIndexed.iterator();
        obj = coroutine_suspended;
        sequencesKt___SequencesKt$scanIndexed$1 = this;
        if (it.hasNext()) {
        }
    }
}
