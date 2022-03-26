package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.SequenceScope;
/* compiled from: SlidingWindow.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/SequenceScope;", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 16})
@DebugMetadata(c = "kotlin.collections.SlidingWindowKt$windowedIterator$1", f = "SlidingWindow.kt", i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4}, l = {34, 40, 49, 55, 58}, m = "invokeSuspend", n = {"$this$iterator", "bufferInitialCapacity", "gap", "buffer", "skip", "e", "$this$iterator", "bufferInitialCapacity", "gap", "buffer", "skip", "$this$iterator", "bufferInitialCapacity", "gap", "buffer", "e", "$this$iterator", "bufferInitialCapacity", "gap", "buffer", "$this$iterator", "bufferInitialCapacity", "gap", "buffer"}, s = {"L$0", "I$0", "I$1", "L$1", "I$2", "L$2", "L$0", "I$0", "I$1", "L$1", "I$2", "L$0", "I$0", "I$1", "L$1", "L$2", "L$0", "I$0", "I$1", "L$1", "L$0", "I$0", "I$1", "L$1"})
/* loaded from: classes3.dex */
final class SlidingWindowKt$windowedIterator$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super List<? extends T>>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Iterator $iterator;
    final /* synthetic */ boolean $partialWindows;
    final /* synthetic */ boolean $reuseBuffer;
    final /* synthetic */ int $size;
    final /* synthetic */ int $step;
    int I$0;
    int I$1;
    int I$2;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private SequenceScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SlidingWindowKt$windowedIterator$1(int i, int i2, Iterator it, boolean z, boolean z2, Continuation continuation) {
        super(2, continuation);
        this.$size = i;
        this.$step = i2;
        this.$iterator = it;
        this.$reuseBuffer = z;
        this.$partialWindows = z2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        SlidingWindowKt$windowedIterator$1 slidingWindowKt$windowedIterator$1 = new SlidingWindowKt$windowedIterator$1(this.$size, this.$step, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
        SequenceScope sequenceScope = (SequenceScope) obj;
        slidingWindowKt$windowedIterator$1.p$ = (SequenceScope) obj;
        return slidingWindowKt$windowedIterator$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((SlidingWindowKt$windowedIterator$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x021d  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object $result) {
        Object $result2;
        Iterator it;
        int gap;
        RingBuffer buffer;
        int bufferInitialCapacity;
        Object e;
        SequenceScope $this$iterator;
        SlidingWindowKt$windowedIterator$1 slidingWindowKt$windowedIterator$1;
        SequenceScope $this$iterator2;
        RingBuffer buffer2;
        Iterator it2;
        int gap2;
        Object obj;
        int bufferInitialCapacity2;
        SequenceScope $this$iterator3;
        ArrayList buffer3;
        int skip;
        SlidingWindowKt$windowedIterator$1 slidingWindowKt$windowedIterator$12;
        SequenceScope $this$iterator4;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure($result);
            $this$iterator = this.p$;
            bufferInitialCapacity = RangesKt.coerceAtMost(this.$size, 1024);
            gap2 = this.$step - this.$size;
            if (gap2 >= 0) {
                buffer3 = new ArrayList(bufferInitialCapacity);
                it2 = this.$iterator;
                bufferInitialCapacity2 = bufferInitialCapacity;
                obj = coroutine_suspended;
                slidingWindowKt$windowedIterator$12 = this;
                $this$iterator3 = $this$iterator;
                skip = 0;
                while (it2.hasNext()) {
                }
                if (true ^ buffer3.isEmpty()) {
                }
                return Unit.INSTANCE;
            }
            RingBuffer buffer4 = new RingBuffer(bufferInitialCapacity);
            it = this.$iterator;
            $result2 = $result;
            gap = gap2;
            buffer = buffer4;
            e = coroutine_suspended;
            slidingWindowKt$windowedIterator$1 = this;
            while (it.hasNext()) {
            }
            if (slidingWindowKt$windowedIterator$1.$partialWindows) {
            }
        } else if (i == 1) {
            it2 = (Iterator) this.L$3;
            Object e2 = this.L$2;
            int skip2 = this.I$2;
            ArrayList buffer5 = (ArrayList) this.L$1;
            gap2 = this.I$1;
            int bufferInitialCapacity3 = this.I$0;
            ResultKt.throwOnFailure($result);
            buffer3 = buffer5;
            bufferInitialCapacity2 = bufferInitialCapacity3;
            obj = coroutine_suspended;
            slidingWindowKt$windowedIterator$12 = this;
            $this$iterator3 = (SequenceScope) this.L$0;
            if (!slidingWindowKt$windowedIterator$12.$reuseBuffer) {
                buffer3.clear();
            } else {
                buffer3 = new ArrayList(slidingWindowKt$windowedIterator$12.$size);
            }
            skip = gap2;
            while (it2.hasNext()) {
                Object e3 = it2.next();
                if (skip > 0) {
                    skip--;
                } else {
                    buffer3.add(e3);
                    if (buffer3.size() == slidingWindowKt$windowedIterator$12.$size) {
                        slidingWindowKt$windowedIterator$12.L$0 = $this$iterator3;
                        slidingWindowKt$windowedIterator$12.I$0 = bufferInitialCapacity2;
                        slidingWindowKt$windowedIterator$12.I$1 = gap2;
                        slidingWindowKt$windowedIterator$12.L$1 = buffer3;
                        slidingWindowKt$windowedIterator$12.I$2 = skip;
                        slidingWindowKt$windowedIterator$12.L$2 = e3;
                        slidingWindowKt$windowedIterator$12.L$3 = it2;
                        slidingWindowKt$windowedIterator$12.label = 1;
                        if ($this$iterator3.yield(buffer3, slidingWindowKt$windowedIterator$12) == obj) {
                            return obj;
                        }
                        if (!slidingWindowKt$windowedIterator$12.$reuseBuffer) {
                        }
                        skip = gap2;
                        while (it2.hasNext()) {
                        }
                    }
                }
            }
            if ((true ^ buffer3.isEmpty()) || (!slidingWindowKt$windowedIterator$12.$partialWindows && buffer3.size() != slidingWindowKt$windowedIterator$12.$size)) {
                return Unit.INSTANCE;
            }
            slidingWindowKt$windowedIterator$12.L$0 = $this$iterator3;
            slidingWindowKt$windowedIterator$12.I$0 = bufferInitialCapacity2;
            slidingWindowKt$windowedIterator$12.I$1 = gap2;
            slidingWindowKt$windowedIterator$12.L$1 = buffer3;
            slidingWindowKt$windowedIterator$12.I$2 = skip;
            slidingWindowKt$windowedIterator$12.label = 2;
            if ($this$iterator3.yield(buffer3, slidingWindowKt$windowedIterator$12) == obj) {
                return obj;
            }
            $this$iterator4 = $this$iterator3;
            return Unit.INSTANCE;
        } else if (i == 2) {
            int skip3 = this.I$2;
            ArrayList buffer6 = (ArrayList) this.L$1;
            int gap3 = this.I$1;
            int bufferInitialCapacity4 = this.I$0;
            ResultKt.throwOnFailure($result);
            $this$iterator4 = (SequenceScope) this.L$0;
            return Unit.INSTANCE;
        } else if (i == 3) {
            it = (Iterator) this.L$3;
            Object e4 = this.L$2;
            buffer = (RingBuffer) this.L$1;
            gap = this.I$1;
            bufferInitialCapacity = this.I$0;
            $this$iterator = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
            Object e5 = $result;
            Object $result3 = coroutine_suspended;
            slidingWindowKt$windowedIterator$1 = this;
            buffer.removeFirst(slidingWindowKt$windowedIterator$1.$step);
            e = $result3;
            $result2 = e5;
            while (it.hasNext()) {
                Object e6 = it.next();
                buffer.add((RingBuffer) e6);
                if (buffer.isFull()) {
                    int size = buffer.size();
                    int i2 = slidingWindowKt$windowedIterator$1.$size;
                    if (size >= i2) {
                        ArrayList arrayList = slidingWindowKt$windowedIterator$1.$reuseBuffer ? buffer : new ArrayList(buffer);
                        slidingWindowKt$windowedIterator$1.L$0 = $this$iterator;
                        slidingWindowKt$windowedIterator$1.I$0 = bufferInitialCapacity;
                        slidingWindowKt$windowedIterator$1.I$1 = gap;
                        slidingWindowKt$windowedIterator$1.L$1 = buffer;
                        slidingWindowKt$windowedIterator$1.L$2 = e6;
                        slidingWindowKt$windowedIterator$1.L$3 = it;
                        slidingWindowKt$windowedIterator$1.label = 3;
                        if ($this$iterator.yield(arrayList, slidingWindowKt$windowedIterator$1) == e) {
                            return e;
                        }
                        $result3 = e;
                        e5 = $result2;
                        buffer.removeFirst(slidingWindowKt$windowedIterator$1.$step);
                        e = $result3;
                        $result2 = e5;
                        while (it.hasNext()) {
                        }
                    } else {
                        buffer = buffer.expanded(i2);
                    }
                }
            }
            if (slidingWindowKt$windowedIterator$1.$partialWindows) {
                return Unit.INSTANCE;
            }
            $this$iterator2 = $this$iterator;
            buffer2 = buffer;
            if (buffer2.size() <= slidingWindowKt$windowedIterator$1.$step) {
            }
        } else if (i == 4) {
            RingBuffer buffer7 = (RingBuffer) this.L$1;
            int gap4 = this.I$1;
            bufferInitialCapacity = this.I$0;
            ResultKt.throwOnFailure($result);
            $result2 = $result;
            gap = gap4;
            e = coroutine_suspended;
            slidingWindowKt$windowedIterator$1 = this;
            $this$iterator2 = (SequenceScope) this.L$0;
            buffer2 = buffer7;
            buffer2.removeFirst(slidingWindowKt$windowedIterator$1.$step);
            if (buffer2.size() <= slidingWindowKt$windowedIterator$1.$step) {
                ArrayList arrayList2 = slidingWindowKt$windowedIterator$1.$reuseBuffer ? buffer2 : new ArrayList(buffer2);
                slidingWindowKt$windowedIterator$1.L$0 = $this$iterator2;
                slidingWindowKt$windowedIterator$1.I$0 = bufferInitialCapacity;
                slidingWindowKt$windowedIterator$1.I$1 = gap;
                slidingWindowKt$windowedIterator$1.L$1 = buffer2;
                slidingWindowKt$windowedIterator$1.label = 4;
                if ($this$iterator2.yield(arrayList2, slidingWindowKt$windowedIterator$1) == e) {
                    return e;
                }
                buffer2.removeFirst(slidingWindowKt$windowedIterator$1.$step);
                if (buffer2.size() <= slidingWindowKt$windowedIterator$1.$step) {
                    if (!buffer2.isEmpty()) {
                        slidingWindowKt$windowedIterator$1.L$0 = $this$iterator2;
                        slidingWindowKt$windowedIterator$1.I$0 = bufferInitialCapacity;
                        slidingWindowKt$windowedIterator$1.I$1 = gap;
                        slidingWindowKt$windowedIterator$1.L$1 = buffer2;
                        slidingWindowKt$windowedIterator$1.label = 5;
                        if ($this$iterator2.yield(buffer2, slidingWindowKt$windowedIterator$1) == e) {
                            return e;
                        }
                    }
                    return Unit.INSTANCE;
                }
            }
        } else if (i == 5) {
            RingBuffer buffer8 = (RingBuffer) this.L$1;
            int gap5 = this.I$1;
            int bufferInitialCapacity5 = this.I$0;
            SequenceScope $this$iterator5 = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure($result);
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
