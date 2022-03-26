package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Channels.common.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0005H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007"}, d2 = {"<anonymous>", "", ExifInterface.LONGITUDE_EAST, "R", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$zip$2", f = "Channels.common.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4}, l = {1881, 1881, 1884, 1885, 1886, 1887}, m = "invokeSuspend", n = {"otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "element1", "otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "element1", "otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "element1", "element2"}, s = {"L$1", "L$2", "L$4", "L$5", "L$6", "L$1", "L$2", "L$4", "L$5", "L$6", "L$1", "L$2", "L$4", "L$5", "L$6", "L$8", "L$9", "L$1", "L$2", "L$4", "L$5", "L$6", "L$8", "L$9", "L$1", "L$2", "L$4", "L$5", "L$6", "L$8", "L$9", "L$10"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$zip$2 extends SuspendLambda implements Function2<ProducerScope<? super V>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $other;
    final /* synthetic */ ReceiveChannel $this_zip;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$10;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    Object L$9;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$zip$2(ReceiveChannel receiveChannel, ReceiveChannel receiveChannel2, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_zip = receiveChannel;
        this.$other = receiveChannel2;
        this.$transform = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$2 = new ChannelsKt__Channels_commonKt$zip$2(this.$this_zip, this.$other, this.$transform, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$zip$2.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$zip$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$zip$2) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX INFO: Multiple debug info for r0v0 java.lang.Object: [D('element1' java.lang.Object), D('$receiver$iv' kotlinx.coroutines.channels.ReceiveChannel)] */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01fe A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x020f A[Catch: all -> 0x0319, TRY_LEAVE, TryCatch #4 {all -> 0x0319, blocks: (B:54:0x0207, B:56:0x020f, B:84:0x0310), top: B:108:0x0207 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x025d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0285 A[Catch: all -> 0x0306, TRY_LEAVE, TryCatch #3 {all -> 0x0306, blocks: (B:65:0x026f, B:68:0x0285), top: B:106:0x026f }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x02e1 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x02e2  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0310 A[Catch: all -> 0x0319, TRY_ENTER, TRY_LEAVE, TryCatch #4 {all -> 0x0319, blocks: (B:54:0x0207, B:56:0x020f, B:84:0x0310), top: B:108:0x0207 }] */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v22, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r4v7 */
    /* JADX WARN: Type inference failed for: r6v0 */
    /* JADX WARN: Type inference failed for: r6v19, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r6v4 */
    /* JADX WARN: Type inference failed for: r7v10 */
    /* JADX WARN: Type inference failed for: r7v18, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r7v36 */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v31, types: [kotlinx.coroutines.channels.ChannelIterator] */
    /* JADX WARN: Type inference failed for: r8v7 */
    /* JADX WARN: Unknown variable types count: 4 */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object result) {
        int $i$f$consume;
        Throwable cause$iv$iv;
        boolean z;
        boolean z2;
        ProducerScope producerScope;
        ChannelIterator otherIterator;
        int $i$a$4$consume;
        ReceiveChannel $receiver$iv;
        Object result2;
        ReceiveChannel $receiver$iv$iv;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$2;
        Object obj;
        ChannelIterator otherIterator2;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$22;
        ReceiveChannel $receiver$iv2;
        Object hasNext;
        ChannelIterator otherIterator3;
        int otherIterator4;
        boolean z3;
        Object obj2;
        ReceiveChannel $receiver$iv$iv2;
        int $i$f$consume2;
        Object e$iv;
        int $receiver$iv3;
        Object obj3;
        ChannelIterator channelIterator;
        ChannelIterator otherIterator5;
        boolean z4;
        ProducerScope producerScope2;
        boolean z5;
        int $receiver$iv4;
        int $i$f$consume3;
        Object result3;
        Object result4;
        ReceiveChannel $receiver$iv5;
        Object element1;
        Object element12;
        Object element2;
        Object element13;
        Object result5;
        ReceiveChannel $receiver$iv6;
        ChannelIterator channelIterator2;
        Object obj4;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$23;
        boolean z6;
        int $i$a$4$consume2;
        int $i$f$consume4;
        Object result6;
        int $i$a$4$consume3;
        int $i$f$consume5;
        int $i$f$consume6;
        Object invoke;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$24 = this;
        Object result7 = result;
        Object element14 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = channelsKt__Channels_commonKt$zip$24.label;
        ?? r4 = 5;
        ?? r6 = 3;
        int $i$f$consumeEach = 2;
        ?? r7 = 2;
        ?? r8 = 1;
        boolean z7 = false;
        if (i != 0) {
            try {
                if (i == 1) {
                    otherIterator4 = 0;
                    $i$f$consume2 = 0;
                    otherIterator = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$7;
                    $receiver$iv2 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$6;
                    cause$iv$iv = (Throwable) channelsKt__Channels_commonKt$zip$24.L$5;
                    ReceiveChannel $receiver$iv$iv3 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$4;
                    channelsKt__Channels_commonKt$zip$2 = (ChannelsKt__Channels_commonKt$zip$2) channelsKt__Channels_commonKt$zip$24.L$3;
                    ReceiveChannel $receiver$iv7 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$2;
                    otherIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$1;
                    producerScope = (ProducerScope) channelsKt__Channels_commonKt$zip$24.L$0;
                    if (result7 instanceof Result.Failure) {
                        throw ((Result.Failure) result7).exception;
                    }
                    hasNext = result7;
                    z = false;
                    obj2 = element14;
                    channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$24;
                    z3 = false;
                    result2 = hasNext;
                    $receiver$iv$iv = $receiver$iv$iv3;
                    $receiver$iv$iv2 = $receiver$iv7;
                    if (!((Boolean) hasNext).booleanValue()) {
                    }
                } else if (i != 2) {
                    try {
                        if (i == 3) {
                            Object element15 = channelsKt__Channels_commonKt$zip$24.L$9;
                            Object e$iv2 = channelsKt__Channels_commonKt$zip$24.L$8;
                            channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$7;
                            ReceiveChannel $receiver$iv8 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$6;
                            cause$iv$iv = (Throwable) channelsKt__Channels_commonKt$zip$24.L$5;
                            ReceiveChannel $receiver$iv$iv4 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$4;
                            channelsKt__Channels_commonKt$zip$2 = (ChannelsKt__Channels_commonKt$zip$2) channelsKt__Channels_commonKt$zip$24.L$3;
                            ReceiveChannel $receiver$iv9 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$2;
                            otherIterator5 = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$1;
                            producerScope2 = (ProducerScope) channelsKt__Channels_commonKt$zip$24.L$0;
                            if (result7 instanceof Result.Failure) {
                                throw ((Result.Failure) result7).exception;
                            }
                            e$iv = e$iv2;
                            $receiver$iv5 = $receiver$iv8;
                            z5 = false;
                            $receiver$iv = $receiver$iv9;
                            result3 = result7;
                            z7 = false;
                            element1 = element15;
                            element12 = result3;
                            channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$24;
                            $i$f$consume3 = 0;
                            $receiver$iv$iv = $receiver$iv$iv4;
                            result4 = element14;
                            $receiver$iv4 = 0;
                            if (((Boolean) element12).booleanValue()) {
                            }
                        } else if (i == 4) {
                            $i$f$consume4 = 0;
                            Object element16 = channelsKt__Channels_commonKt$zip$24.L$9;
                            Object e$iv3 = channelsKt__Channels_commonKt$zip$24.L$8;
                            channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$7;
                            $receiver$iv6 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$6;
                            cause$iv$iv = (Throwable) channelsKt__Channels_commonKt$zip$24.L$5;
                            r7 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$4;
                            channelsKt__Channels_commonKt$zip$23 = (ChannelsKt__Channels_commonKt$zip$2) channelsKt__Channels_commonKt$zip$24.L$3;
                            ReceiveChannel $receiver$iv10 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$2;
                            otherIterator5 = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$1;
                            producerScope2 = (ProducerScope) channelsKt__Channels_commonKt$zip$24.L$0;
                            if (!(result7 instanceof Result.Failure)) {
                                e$iv = e$iv3;
                                $i$a$4$consume2 = 0;
                                $receiver$iv$iv = r7;
                                $receiver$iv = $receiver$iv10;
                                obj4 = element14;
                                element13 = element16;
                                element2 = result7;
                                z6 = false;
                                result5 = element2;
                                channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$24;
                                invoke = channelsKt__Channels_commonKt$zip$22.$transform.invoke(element13, element2);
                                channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                                channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator5;
                                channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                                channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$23;
                                channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                                channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                                channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv6;
                                channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator2;
                                channelsKt__Channels_commonKt$zip$22.L$8 = e$iv;
                                channelsKt__Channels_commonKt$zip$22.L$9 = element13;
                                channelsKt__Channels_commonKt$zip$22.L$10 = element2;
                                channelsKt__Channels_commonKt$zip$22.label = 5;
                                if (producerScope2.send(invoke, channelsKt__Channels_commonKt$zip$22) != obj4) {
                                }
                            } else {
                                throw ((Result.Failure) result7).exception;
                            }
                        } else if (i == 5) {
                            int $i$f$consume7 = 0;
                            Object element22 = channelsKt__Channels_commonKt$zip$24.L$10;
                            Object element17 = channelsKt__Channels_commonKt$zip$24.L$9;
                            Object e$iv4 = channelsKt__Channels_commonKt$zip$24.L$8;
                            channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$7;
                            $receiver$iv6 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$6;
                            cause$iv$iv = (Throwable) channelsKt__Channels_commonKt$zip$24.L$5;
                            $receiver$iv$iv = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$4;
                            channelsKt__Channels_commonKt$zip$23 = (ChannelsKt__Channels_commonKt$zip$2) channelsKt__Channels_commonKt$zip$24.L$3;
                            $receiver$iv = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$2;
                            otherIterator5 = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$1;
                            ProducerScope producerScope3 = (ProducerScope) channelsKt__Channels_commonKt$zip$24.L$0;
                            try {
                                if (!(result7 instanceof Result.Failure)) {
                                    boolean z8 = false;
                                    int $i$f$consumeEach2 = 0;
                                    z6 = false;
                                    obj4 = element14;
                                    try {
                                        otherIterator2 = otherIterator5;
                                        producerScope = producerScope3;
                                        otherIterator = channelIterator2;
                                        $receiver$iv5 = $receiver$iv6;
                                        channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$23;
                                        result4 = obj4;
                                        z2 = z8;
                                        z = z6;
                                        channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$24;
                                        result6 = result7;
                                        $i$a$4$consume3 = $i$f$consumeEach2;
                                        $i$f$consume5 = $i$f$consume7;
                                        channelsKt__Channels_commonKt$zip$22.L$0 = producerScope;
                                        channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator2;
                                        channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                                        channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                                        channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                                        channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                                        channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv2;
                                        channelsKt__Channels_commonKt$zip$22.L$7 = otherIterator;
                                        channelsKt__Channels_commonKt$zip$22.label = 1;
                                        hasNext = otherIterator.hasNext(channelsKt__Channels_commonKt$zip$2);
                                        if (hasNext != obj) {
                                            return obj;
                                        }
                                        otherIterator3 = otherIterator2;
                                        int $i$a$4$consume4 = $i$a$4$consume == 1 ? 1 : 0;
                                        int $i$a$4$consume5 = $i$a$4$consume == 1 ? 1 : 0;
                                        int $i$a$4$consume6 = $i$a$4$consume == 1 ? 1 : 0;
                                        int $i$a$4$consume7 = $i$a$4$consume == 1 ? 1 : 0;
                                        otherIterator4 = $i$a$4$consume4;
                                        z3 = z2;
                                        obj2 = obj;
                                        $receiver$iv$iv2 = $receiver$iv;
                                        $i$f$consume2 = $i$f$consume;
                                        try {
                                            if (!((Boolean) hasNext).booleanValue()) {
                                                channelsKt__Channels_commonKt$zip$22.L$0 = producerScope;
                                                channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator3;
                                                channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv$iv2;
                                                channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                                                channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                                                channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                                                channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv2;
                                                channelsKt__Channels_commonKt$zip$22.L$7 = otherIterator;
                                                channelsKt__Channels_commonKt$zip$22.label = 2;
                                                e$iv = otherIterator.next(channelsKt__Channels_commonKt$zip$2);
                                                if (e$iv == obj2) {
                                                    return obj2;
                                                }
                                                try {
                                                    int $i$a$4$consume8 = otherIterator4 == 1 ? 1 : 0;
                                                    int $i$a$4$consume9 = otherIterator4 == 1 ? 1 : 0;
                                                    int $i$a$4$consume10 = otherIterator4 == 1 ? 1 : 0;
                                                    $receiver$iv3 = $i$a$4$consume8;
                                                    obj3 = obj2;
                                                    channelIterator = otherIterator;
                                                    otherIterator5 = otherIterator3;
                                                    $receiver$iv = $receiver$iv$iv2;
                                                    z4 = z3;
                                                    z7 = z;
                                                    producerScope2 = producerScope;
                                                    $i$f$consume6 = $i$f$consume2;
                                                    channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                                                    channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator5;
                                                    channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                                                    channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                                                    channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                                                    channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                                                    channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv2;
                                                    channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator;
                                                    channelsKt__Channels_commonKt$zip$22.L$8 = e$iv;
                                                    channelsKt__Channels_commonKt$zip$22.L$9 = e$iv;
                                                    channelsKt__Channels_commonKt$zip$22.label = 3;
                                                    element12 = otherIterator5.hasNext(channelsKt__Channels_commonKt$zip$22);
                                                    if (element12 != obj3) {
                                                        return obj3;
                                                    }
                                                    result4 = obj3;
                                                    element1 = e$iv;
                                                    z5 = z4;
                                                    result3 = result2;
                                                    int $i$a$4$consume11 = $receiver$iv3 == 1 ? 1 : 0;
                                                    int $i$a$4$consume12 = $receiver$iv3 == 1 ? 1 : 0;
                                                    int $i$a$4$consume13 = $receiver$iv3 == 1 ? 1 : 0;
                                                    $i$f$consume3 = $i$a$4$consume11;
                                                    $receiver$iv5 = $receiver$iv2;
                                                    int $i$f$consume8 = $i$f$consume6 == 1 ? 1 : 0;
                                                    int $i$f$consume9 = $i$f$consume6 == 1 ? 1 : 0;
                                                    int $i$f$consume10 = $i$f$consume6 == 1 ? 1 : 0;
                                                    int $i$f$consume11 = $i$f$consume6 == 1 ? 1 : 0;
                                                    $receiver$iv4 = $i$f$consume8;
                                                    try {
                                                        if (((Boolean) element12).booleanValue()) {
                                                            otherIterator2 = otherIterator5;
                                                            result6 = result3;
                                                            otherIterator = channelIterator;
                                                            producerScope = producerScope2;
                                                            z2 = z5;
                                                            z = z7;
                                                            $i$a$4$consume3 = $i$f$consume3;
                                                            int $i$f$consume12 = $receiver$iv4 == 1 ? 1 : 0;
                                                            int $i$f$consume13 = $receiver$iv4 == 1 ? 1 : 0;
                                                            int $i$f$consume14 = $receiver$iv4 == 1 ? 1 : 0;
                                                            $i$f$consume5 = $i$f$consume12;
                                                            $receiver$iv2 = $receiver$iv5;
                                                            obj = result4;
                                                            result2 = result6;
                                                            $i$a$4$consume = $i$a$4$consume3;
                                                            $i$f$consume = $i$f$consume5;
                                                            channelsKt__Channels_commonKt$zip$22.L$0 = producerScope;
                                                            channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator2;
                                                            channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                                                            channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                                                            channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                                                            channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                                                            channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv2;
                                                            channelsKt__Channels_commonKt$zip$22.L$7 = otherIterator;
                                                            channelsKt__Channels_commonKt$zip$22.label = 1;
                                                            hasNext = otherIterator.hasNext(channelsKt__Channels_commonKt$zip$2);
                                                            if (hasNext != obj) {
                                                            }
                                                        } else {
                                                            channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                                                            channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator5;
                                                            channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                                                            channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                                                            channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                                                            channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                                                            channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv5;
                                                            channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator;
                                                            channelsKt__Channels_commonKt$zip$22.L$8 = e$iv;
                                                            channelsKt__Channels_commonKt$zip$22.L$9 = element1;
                                                            channelsKt__Channels_commonKt$zip$22.label = 4;
                                                            element2 = otherIterator5.next(channelsKt__Channels_commonKt$zip$22);
                                                            if (element2 == result4) {
                                                                return result4;
                                                            }
                                                            element13 = element1;
                                                            result5 = result3;
                                                            $receiver$iv6 = $receiver$iv5;
                                                            channelIterator2 = channelIterator;
                                                            obj4 = result4;
                                                            channelsKt__Channels_commonKt$zip$23 = channelsKt__Channels_commonKt$zip$2;
                                                            z6 = z7;
                                                            z7 = z5;
                                                            int $i$a$4$consume14 = $i$f$consume3 == 1 ? 1 : 0;
                                                            int $i$a$4$consume15 = $i$f$consume3 == 1 ? 1 : 0;
                                                            int $i$a$4$consume16 = $i$f$consume3 == 1 ? 1 : 0;
                                                            $i$a$4$consume2 = $i$a$4$consume14;
                                                            $i$f$consume4 = $receiver$iv4;
                                                            try {
                                                                invoke = channelsKt__Channels_commonKt$zip$22.$transform.invoke(element13, element2);
                                                                channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                                                                channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator5;
                                                                channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                                                                channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$23;
                                                                channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                                                                channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                                                                channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv6;
                                                                channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator2;
                                                                channelsKt__Channels_commonKt$zip$22.L$8 = e$iv;
                                                                channelsKt__Channels_commonKt$zip$22.L$9 = element13;
                                                                channelsKt__Channels_commonKt$zip$22.L$10 = element2;
                                                                channelsKt__Channels_commonKt$zip$22.label = 5;
                                                                if (producerScope2.send(invoke, channelsKt__Channels_commonKt$zip$22) != obj4) {
                                                                    return obj4;
                                                                }
                                                                channelsKt__Channels_commonKt$zip$24 = channelsKt__Channels_commonKt$zip$22;
                                                                result7 = result5;
                                                                z8 = z7;
                                                                int $i$a$4$consume17 = $i$a$4$consume2 == 1 ? 1 : 0;
                                                                int $i$a$4$consume18 = $i$a$4$consume2 == 1 ? 1 : 0;
                                                                int $i$a$4$consume19 = $i$a$4$consume2 == 1 ? 1 : 0;
                                                                $i$f$consumeEach2 = $i$a$4$consume17;
                                                                producerScope3 = producerScope2;
                                                                $i$f$consume7 = $i$f$consume4;
                                                                otherIterator2 = otherIterator5;
                                                                producerScope = producerScope3;
                                                                otherIterator = channelIterator2;
                                                                $receiver$iv5 = $receiver$iv6;
                                                                channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$23;
                                                                result4 = obj4;
                                                                z2 = z8;
                                                                z = z6;
                                                                channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$24;
                                                                result6 = result7;
                                                                $i$a$4$consume3 = $i$f$consumeEach2;
                                                                $i$f$consume5 = $i$f$consume7;
                                                                $receiver$iv2 = $receiver$iv5;
                                                                obj = result4;
                                                                result2 = result6;
                                                                $i$a$4$consume = $i$a$4$consume3;
                                                                $i$f$consume = $i$f$consume5;
                                                                channelsKt__Channels_commonKt$zip$22.L$0 = producerScope;
                                                                channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator2;
                                                                channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                                                                channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                                                                channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                                                                channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                                                                channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv2;
                                                                channelsKt__Channels_commonKt$zip$22.L$7 = otherIterator;
                                                                channelsKt__Channels_commonKt$zip$22.label = 1;
                                                                hasNext = otherIterator.hasNext(channelsKt__Channels_commonKt$zip$2);
                                                                if (hasNext != obj) {
                                                                }
                                                            } catch (Throwable th) {
                                                                e$iv$iv = th;
                                                            }
                                                        }
                                                    } catch (Throwable th2) {
                                                        e$iv$iv = th2;
                                                    }
                                                } catch (Throwable th3) {
                                                    e$iv$iv = th3;
                                                }
                                            } else {
                                                Unit unit = Unit.INSTANCE;
                                                $receiver$iv$iv.cancel(cause$iv$iv);
                                                return Unit.INSTANCE;
                                            }
                                        } catch (Throwable th4) {
                                            e$iv$iv = th4;
                                        }
                                    } catch (Throwable th5) {
                                        e$iv$iv = th5;
                                    }
                                    $receiver$iv2 = $receiver$iv5;
                                    obj = result4;
                                    result2 = result6;
                                    $i$a$4$consume = $i$a$4$consume3;
                                    $i$f$consume = $i$f$consume5;
                                } else {
                                    throw ((Result.Failure) result7).exception;
                                }
                            } catch (Throwable th6) {
                                e$iv$iv = th6;
                            }
                        } else {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    } catch (Throwable th7) {
                        e$iv$iv = th7;
                        $receiver$iv$iv = r7;
                    }
                } else {
                    $i$f$consume6 = 0;
                    $i$f$consumeEach = 0;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$7;
                    $receiver$iv2 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$6;
                    cause$iv$iv = (Throwable) channelsKt__Channels_commonKt$zip$24.L$5;
                    r4 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$4;
                    channelsKt__Channels_commonKt$zip$2 = (ChannelsKt__Channels_commonKt$zip$2) channelsKt__Channels_commonKt$zip$24.L$3;
                    r6 = (ReceiveChannel) channelsKt__Channels_commonKt$zip$24.L$2;
                    r8 = (ChannelIterator) channelsKt__Channels_commonKt$zip$24.L$1;
                    producerScope2 = (ProducerScope) channelsKt__Channels_commonKt$zip$24.L$0;
                    if (!(result7 instanceof Result.Failure)) {
                        e$iv = result7;
                        channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$24;
                        z4 = false;
                        otherIterator5 = r8;
                        result2 = e$iv;
                        $receiver$iv = r6;
                        $receiver$iv$iv = r4;
                        $receiver$iv3 = 0;
                        obj3 = element14;
                        channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator5;
                        channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                        channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                        channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                        channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                        channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv2;
                        channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator;
                        channelsKt__Channels_commonKt$zip$22.L$8 = e$iv;
                        channelsKt__Channels_commonKt$zip$22.L$9 = e$iv;
                        channelsKt__Channels_commonKt$zip$22.label = 3;
                        element12 = otherIterator5.hasNext(channelsKt__Channels_commonKt$zip$22);
                        if (element12 != obj3) {
                        }
                    } else {
                        throw ((Result.Failure) result7).exception;
                    }
                }
            } catch (Throwable th8) {
                e$iv$iv = th8;
                $receiver$iv$iv = r4;
            }
        } else if (!(result7 instanceof Result.Failure)) {
            ProducerScope producerScope4 = channelsKt__Channels_commonKt$zip$24.p$;
            otherIterator2 = channelsKt__Channels_commonKt$zip$24.$other.iterator();
            $receiver$iv = channelsKt__Channels_commonKt$zip$24.$this_zip;
            $receiver$iv$iv = $receiver$iv;
            $i$f$consume = 0;
            cause$iv$iv = null;
            try {
                otherIterator = $receiver$iv$iv.iterator();
                producerScope = producerScope4;
                z2 = false;
                $receiver$iv2 = $receiver$iv$iv;
                z = false;
                obj = element14;
                channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$24;
                $i$a$4$consume = 0;
                result2 = result7;
                channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$2;
                channelsKt__Channels_commonKt$zip$22.L$0 = producerScope;
                channelsKt__Channels_commonKt$zip$22.L$1 = otherIterator2;
                channelsKt__Channels_commonKt$zip$22.L$2 = $receiver$iv;
                channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                channelsKt__Channels_commonKt$zip$22.L$4 = $receiver$iv$iv;
                channelsKt__Channels_commonKt$zip$22.L$5 = cause$iv$iv;
                channelsKt__Channels_commonKt$zip$22.L$6 = $receiver$iv2;
                channelsKt__Channels_commonKt$zip$22.L$7 = otherIterator;
                channelsKt__Channels_commonKt$zip$22.label = 1;
                hasNext = otherIterator.hasNext(channelsKt__Channels_commonKt$zip$2);
                if (hasNext != obj) {
                }
            } catch (Throwable th9) {
                e$iv$iv = th9;
            }
        } else {
            throw ((Result.Failure) result7).exception;
        }
        try {
            throw e$iv$iv;
        } catch (Throwable e$iv$iv) {
            $receiver$iv$iv.cancel(e$iv$iv);
            throw e$iv$iv;
        }
    }
}
