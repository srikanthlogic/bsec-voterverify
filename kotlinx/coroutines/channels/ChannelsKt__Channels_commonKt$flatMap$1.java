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
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", ExifInterface.LONGITUDE_EAST, "R", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$flatMap$1", f = "Channels.common.kt", i = {2, 3}, l = {1100, 1100, 1102, 1101, 1104}, m = "invokeSuspend", n = {"e", "e"}, s = {"L$1", "L$1"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$flatMap$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_flatMap;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$flatMap$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_flatMap = receiveChannel;
        this.$transform = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$1 = new ChannelsKt__Channels_commonKt$flatMap$1(this.$this_flatMap, this.$transform, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$flatMap$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$flatMap$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$flatMap$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0096 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00c4 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00de A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e4  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object e) {
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$1;
        Object result;
        Object result2;
        ChannelIterator channelIterator;
        Object e2;
        Object result3;
        Object obj;
        Object e3;
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$12;
        Object obj2;
        ChannelIterator channelIterator2;
        Object invoke;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator2 = (ChannelIterator) this.L$1;
                obj2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result3 = e;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$flatMap$12 = this;
                    if (((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 2) {
                channelIterator2 = (ChannelIterator) this.L$1;
                obj2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result3 = e;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$flatMap$12 = this;
                    Function2 function2 = channelsKt__Channels_commonKt$flatMap$12.$transform;
                    channelsKt__Channels_commonKt$flatMap$12.L$0 = obj2;
                    channelsKt__Channels_commonKt$flatMap$12.L$1 = e;
                    channelsKt__Channels_commonKt$flatMap$12.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$flatMap$12.label = 3;
                    invoke = function2.invoke(e, channelsKt__Channels_commonKt$flatMap$12);
                    if (invoke != obj) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                e3 = this.L$1;
                result2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = e;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$flatMap$12 = this;
                    channelsKt__Channels_commonKt$flatMap$12.L$0 = result2;
                    channelsKt__Channels_commonKt$flatMap$12.L$1 = e3;
                    channelsKt__Channels_commonKt$flatMap$12.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$flatMap$12.label = 4;
                    if (ChannelsKt.toChannel((ReceiveChannel) e, (SendChannel) result2, channelsKt__Channels_commonKt$flatMap$12) != obj) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 4) {
                channelIterator = (ChannelIterator) this.L$2;
                Object e4 = this.L$1;
                result2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = e;
                    channelsKt__Channels_commonKt$flatMap$1 = this;
                    e2 = result2;
                    result3 = result;
                    channelsKt__Channels_commonKt$flatMap$1.L$0 = e2;
                    channelsKt__Channels_commonKt$flatMap$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$flatMap$1.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$flatMap$1);
                    if (hasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    channelsKt__Channels_commonKt$flatMap$12 = channelsKt__Channels_commonKt$flatMap$1;
                    e = hasNext;
                    obj = coroutine_suspended;
                    obj2 = e2;
                    channelIterator2 = channelIterator;
                    if (((Boolean) e).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$flatMap$12.L$0 = obj2;
                    channelsKt__Channels_commonKt$flatMap$12.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$flatMap$12.label = 2;
                    e = channelIterator2.next(channelsKt__Channels_commonKt$flatMap$12);
                    if (e == obj) {
                        return obj;
                    }
                    Function2 function22 = channelsKt__Channels_commonKt$flatMap$12.$transform;
                    channelsKt__Channels_commonKt$flatMap$12.L$0 = obj2;
                    channelsKt__Channels_commonKt$flatMap$12.L$1 = e;
                    channelsKt__Channels_commonKt$flatMap$12.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$flatMap$12.label = 3;
                    invoke = function22.invoke(e, channelsKt__Channels_commonKt$flatMap$12);
                    if (invoke != obj) {
                        return obj;
                    }
                    e3 = e;
                    e = invoke;
                    result = result3;
                    result2 = obj2;
                    channelIterator = channelIterator2;
                    channelsKt__Channels_commonKt$flatMap$12.L$0 = result2;
                    channelsKt__Channels_commonKt$flatMap$12.L$1 = e3;
                    channelsKt__Channels_commonKt$flatMap$12.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$flatMap$12.label = 4;
                    if (ChannelsKt.toChannel((ReceiveChannel) e, (SendChannel) result2, channelsKt__Channels_commonKt$flatMap$12) != obj) {
                        return obj;
                    }
                    channelsKt__Channels_commonKt$flatMap$1 = channelsKt__Channels_commonKt$flatMap$12;
                    coroutine_suspended = obj;
                    e2 = result2;
                    result3 = result;
                    channelsKt__Channels_commonKt$flatMap$1.L$0 = e2;
                    channelsKt__Channels_commonKt$flatMap$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$flatMap$1.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$flatMap$1);
                    if (hasNext == coroutine_suspended) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(e instanceof Result.Failure)) {
            e2 = this.p$;
            channelIterator = this.$this_flatMap.iterator();
            result3 = e;
            channelsKt__Channels_commonKt$flatMap$1 = this;
            channelsKt__Channels_commonKt$flatMap$1.L$0 = e2;
            channelsKt__Channels_commonKt$flatMap$1.L$1 = channelIterator;
            channelsKt__Channels_commonKt$flatMap$1.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$flatMap$1);
            if (hasNext == coroutine_suspended) {
            }
        } else {
            throw ((Result.Failure) e).exception;
        }
    }
}
