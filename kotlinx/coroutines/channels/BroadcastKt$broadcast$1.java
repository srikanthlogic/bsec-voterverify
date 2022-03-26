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
/* compiled from: Broadcast.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/channels/BroadcastKt$broadcast$1", f = "Broadcast.kt", i = {2}, l = {23, 23, 25, 24}, m = "invokeSuspend", n = {"e"}, s = {"L$1"})
/* loaded from: classes3.dex */
final class BroadcastKt$broadcast$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_broadcast;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BroadcastKt$broadcast$1(ReceiveChannel receiveChannel, Continuation continuation) {
        super(2, continuation);
        this.$this_broadcast = receiveChannel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        BroadcastKt$broadcast$1 broadcastKt$broadcast$1 = new BroadcastKt$broadcast$1(this.$this_broadcast, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        broadcastKt$broadcast$1.p$ = (ProducerScope) obj;
        return broadcastKt$broadcast$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((BroadcastKt$broadcast$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0083 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00b0 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b8  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object e) {
        Object obj;
        Object result;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        BroadcastKt$broadcast$1 broadcastKt$broadcast$1;
        BroadcastKt$broadcast$1 broadcastKt$broadcast$12;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        Object result2;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = e;
                    obj = coroutine_suspended;
                    broadcastKt$broadcast$1 = this;
                    if (((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 2) {
                channelIterator2 = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    broadcastKt$broadcast$1 = this;
                    result2 = e;
                    broadcastKt$broadcast$1.L$0 = producerScope2;
                    broadcastKt$broadcast$1.L$1 = e;
                    broadcastKt$broadcast$1.L$2 = channelIterator2;
                    broadcastKt$broadcast$1.label = 3;
                    if (producerScope2.send(e, broadcastKt$broadcast$1) != obj) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                Object e2 = this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    broadcastKt$broadcast$1 = this;
                    result = e;
                    broadcastKt$broadcast$12 = broadcastKt$broadcast$1;
                    coroutine_suspended = obj;
                    broadcastKt$broadcast$12.L$0 = producerScope;
                    broadcastKt$broadcast$12.L$1 = channelIterator;
                    broadcastKt$broadcast$12.label = 1;
                    hasNext = channelIterator.hasNext(broadcastKt$broadcast$12);
                    if (hasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    broadcastKt$broadcast$1 = broadcastKt$broadcast$12;
                    e = hasNext;
                    obj = coroutine_suspended;
                    if (((Boolean) e).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    broadcastKt$broadcast$1.L$0 = producerScope;
                    broadcastKt$broadcast$1.L$1 = channelIterator;
                    broadcastKt$broadcast$1.label = 2;
                    e = channelIterator.next(broadcastKt$broadcast$1);
                    if (e == obj) {
                        return obj;
                    }
                    channelIterator2 = channelIterator;
                    result2 = result;
                    producerScope2 = producerScope;
                    broadcastKt$broadcast$1.L$0 = producerScope2;
                    broadcastKt$broadcast$1.L$1 = e;
                    broadcastKt$broadcast$1.L$2 = channelIterator2;
                    broadcastKt$broadcast$1.label = 3;
                    if (producerScope2.send(e, broadcastKt$broadcast$1) != obj) {
                        return obj;
                    }
                    result = result2;
                    channelIterator = channelIterator2;
                    producerScope = producerScope2;
                    broadcastKt$broadcast$12 = broadcastKt$broadcast$1;
                    coroutine_suspended = obj;
                    broadcastKt$broadcast$12.L$0 = producerScope;
                    broadcastKt$broadcast$12.L$1 = channelIterator;
                    broadcastKt$broadcast$12.label = 1;
                    hasNext = channelIterator.hasNext(broadcastKt$broadcast$12);
                    if (hasNext == coroutine_suspended) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(e instanceof Result.Failure)) {
            ProducerScope producerScope3 = this.p$;
            channelIterator = this.$this_broadcast.iterator();
            result = e;
            broadcastKt$broadcast$12 = this;
            producerScope = producerScope3;
            broadcastKt$broadcast$12.L$0 = producerScope;
            broadcastKt$broadcast$12.L$1 = channelIterator;
            broadcastKt$broadcast$12.label = 1;
            hasNext = channelIterator.hasNext(broadcastKt$broadcast$12);
            if (hasNext == coroutine_suspended) {
            }
        } else {
            throw ((Result.Failure) e).exception;
        }
    }
}
