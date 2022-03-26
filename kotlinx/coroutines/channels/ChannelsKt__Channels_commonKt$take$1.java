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
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$take$1", f = "Channels.common.kt", i = {0, 1, 2, 2}, l = {840, 840, 845, 846}, m = "invokeSuspend", n = {"remaining", "remaining", "remaining", "e"}, s = {"I$0", "I$0", "I$0", "L$1"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$take$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ int $n;
    final /* synthetic */ ReceiveChannel $this_take;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$take$1(ReceiveChannel receiveChannel, int i, Continuation continuation) {
        super(2, continuation);
        this.$this_take = receiveChannel;
        this.$n = i;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$take$1 channelsKt__Channels_commonKt$take$1 = new ChannelsKt__Channels_commonKt$take$1(this.$this_take, this.$n, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$take$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$take$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$take$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00a0 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00d2 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00e4  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object e) {
        Object obj;
        Object result;
        ProducerScope producerScope;
        int remaining;
        ChannelsKt__Channels_commonKt$take$1 channelsKt__Channels_commonKt$take$1;
        ChannelsKt__Channels_commonKt$take$1 channelsKt__Channels_commonKt$take$12;
        ChannelIterator channelIterator;
        int remaining2;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        Object result2;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator = (ChannelIterator) this.L$1;
                remaining2 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = e;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$take$1 = this;
                    if (((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 2) {
                channelIterator2 = (ChannelIterator) this.L$1;
                int remaining3 = this.I$0;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$take$1 = this;
                    remaining = remaining3;
                    result2 = e;
                    channelsKt__Channels_commonKt$take$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$take$1.I$0 = remaining;
                    channelsKt__Channels_commonKt$take$1.L$1 = e;
                    channelsKt__Channels_commonKt$take$1.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$take$1.label = 3;
                    if (producerScope2.send(e, channelsKt__Channels_commonKt$take$1) != obj) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 3) {
                ChannelIterator channelIterator3 = (ChannelIterator) this.L$2;
                Object e2 = this.L$1;
                remaining = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$take$1 = this;
                    result = e;
                    int remaining4 = remaining - 1;
                    if (remaining4 != 0) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$take$12 = channelsKt__Channels_commonKt$take$1;
                    coroutine_suspended = obj;
                    channelIterator = channelIterator3;
                    remaining2 = remaining4;
                    channelsKt__Channels_commonKt$take$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$take$12.I$0 = remaining2;
                    channelsKt__Channels_commonKt$take$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$take$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$take$12);
                    if (hasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    channelsKt__Channels_commonKt$take$1 = channelsKt__Channels_commonKt$take$12;
                    e = hasNext;
                    obj = coroutine_suspended;
                    if (((Boolean) e).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$take$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$take$1.I$0 = remaining2;
                    channelsKt__Channels_commonKt$take$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$take$1.label = 2;
                    e = channelIterator.next(channelsKt__Channels_commonKt$take$1);
                    if (e == obj) {
                        return obj;
                    }
                    remaining = remaining2;
                    result2 = result;
                    producerScope2 = producerScope;
                    channelIterator2 = channelIterator;
                    channelsKt__Channels_commonKt$take$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$take$1.I$0 = remaining;
                    channelsKt__Channels_commonKt$take$1.L$1 = e;
                    channelsKt__Channels_commonKt$take$1.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$take$1.label = 3;
                    if (producerScope2.send(e, channelsKt__Channels_commonKt$take$1) != obj) {
                        return obj;
                    }
                    result = result2;
                    channelIterator3 = channelIterator2;
                    producerScope = producerScope2;
                    int remaining42 = remaining - 1;
                    if (remaining42 != 0) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(e instanceof Result.Failure)) {
            ProducerScope producerScope3 = this.p$;
            int i2 = this.$n;
            if (i2 == 0) {
                return Unit.INSTANCE;
            }
            if (i2 >= 0) {
                int remaining5 = this.$n;
                channelIterator = this.$this_take.iterator();
                result = e;
                channelsKt__Channels_commonKt$take$12 = this;
                producerScope = producerScope3;
                remaining2 = remaining5;
                channelsKt__Channels_commonKt$take$12.L$0 = producerScope;
                channelsKt__Channels_commonKt$take$12.I$0 = remaining2;
                channelsKt__Channels_commonKt$take$12.L$1 = channelIterator;
                channelsKt__Channels_commonKt$take$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$take$12);
                if (hasNext != coroutine_suspended) {
                }
            } else {
                throw new IllegalArgumentException(("Requested element count " + this.$n + " is less than zero.").toString());
            }
        } else {
            throw ((Result.Failure) e).exception;
        }
    }
}
