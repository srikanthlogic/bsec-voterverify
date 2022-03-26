package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import java.util.HashSet;
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
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", ExifInterface.LONGITUDE_EAST, "K", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$distinctBy$1", f = "Channels.common.kt", i = {0, 1, 2, 2, 3, 3, 3}, l = {1455, 1455, 1458, 1459, 1461}, m = "invokeSuspend", n = {"keys", "keys", "keys", "e", "keys", "e", "k"}, s = {"L$1", "L$1", "L$1", "L$2", "L$1", "L$2", "L$4"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$distinctBy$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $selector;
    final /* synthetic */ ReceiveChannel $this_distinctBy;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$distinctBy$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_distinctBy = receiveChannel;
        this.$selector = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$distinctBy$1 channelsKt__Channels_commonKt$distinctBy$1 = new ChannelsKt__Channels_commonKt$distinctBy$1(this.$this_distinctBy, this.$selector, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$distinctBy$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$distinctBy$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$distinctBy$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00bf A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00f2 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0125  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object result) {
        Object obj;
        ProducerScope producerScope;
        HashSet keys;
        ChannelIterator channelIterator;
        ChannelsKt__Channels_commonKt$distinctBy$1 channelsKt__Channels_commonKt$distinctBy$1;
        ChannelsKt__Channels_commonKt$distinctBy$1 channelsKt__Channels_commonKt$distinctBy$12;
        ChannelIterator channelIterator2;
        ProducerScope producerScope2;
        Object e;
        Object obj2;
        ProducerScope producerScope3;
        ChannelIterator channelIterator3;
        HashSet keys2;
        Object invoke;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator3 = (ChannelIterator) this.L$2;
                keys2 = (HashSet) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$distinctBy$1 = this;
                    if (((Boolean) result).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else if (i == 2) {
                channelIterator3 = (ChannelIterator) this.L$2;
                keys2 = (HashSet) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$distinctBy$1 = this;
                    Function2 function2 = channelsKt__Channels_commonKt$distinctBy$1.$selector;
                    channelsKt__Channels_commonKt$distinctBy$1.L$0 = producerScope3;
                    channelsKt__Channels_commonKt$distinctBy$1.L$1 = keys2;
                    channelsKt__Channels_commonKt$distinctBy$1.L$2 = result;
                    channelsKt__Channels_commonKt$distinctBy$1.L$3 = channelIterator3;
                    channelsKt__Channels_commonKt$distinctBy$1.label = 3;
                    invoke = function2.invoke(result, channelsKt__Channels_commonKt$distinctBy$1);
                    if (invoke != obj2) {
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else if (i == 3) {
                channelIterator = (ChannelIterator) this.L$3;
                Object e2 = this.L$2;
                keys = (HashSet) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$distinctBy$1 = this;
                    e = e2;
                    if (keys.contains(result)) {
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else if (i == 4) {
                Object k = this.L$4;
                channelIterator = (ChannelIterator) this.L$3;
                Object e3 = this.L$2;
                keys = (HashSet) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$distinctBy$1 = this;
                    result = k;
                    keys.add(result);
                    channelsKt__Channels_commonKt$distinctBy$12 = channelsKt__Channels_commonKt$distinctBy$1;
                    channelIterator2 = channelIterator;
                    producerScope2 = producerScope;
                    coroutine_suspended = obj;
                    channelsKt__Channels_commonKt$distinctBy$12.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$distinctBy$12.L$1 = keys;
                    channelsKt__Channels_commonKt$distinctBy$12.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$distinctBy$12.label = 1;
                    hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$distinctBy$12);
                    if (hasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    channelsKt__Channels_commonKt$distinctBy$1 = channelsKt__Channels_commonKt$distinctBy$12;
                    result = hasNext;
                    obj2 = coroutine_suspended;
                    producerScope3 = producerScope2;
                    keys2 = keys;
                    channelIterator3 = channelIterator2;
                    if (((Boolean) result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$distinctBy$1.L$0 = producerScope3;
                    channelsKt__Channels_commonKt$distinctBy$1.L$1 = keys2;
                    channelsKt__Channels_commonKt$distinctBy$1.L$2 = channelIterator3;
                    channelsKt__Channels_commonKt$distinctBy$1.label = 2;
                    result = channelIterator3.next(channelsKt__Channels_commonKt$distinctBy$1);
                    if (result == obj2) {
                        return obj2;
                    }
                    Function2 function22 = channelsKt__Channels_commonKt$distinctBy$1.$selector;
                    channelsKt__Channels_commonKt$distinctBy$1.L$0 = producerScope3;
                    channelsKt__Channels_commonKt$distinctBy$1.L$1 = keys2;
                    channelsKt__Channels_commonKt$distinctBy$1.L$2 = result;
                    channelsKt__Channels_commonKt$distinctBy$1.L$3 = channelIterator3;
                    channelsKt__Channels_commonKt$distinctBy$1.label = 3;
                    invoke = function22.invoke(result, channelsKt__Channels_commonKt$distinctBy$1);
                    if (invoke != obj2) {
                        return obj2;
                    }
                    e = result;
                    result = invoke;
                    obj = obj2;
                    producerScope = producerScope3;
                    keys = keys2;
                    channelIterator = channelIterator3;
                    if (keys.contains(result)) {
                        channelsKt__Channels_commonKt$distinctBy$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$distinctBy$1.L$1 = keys;
                        channelsKt__Channels_commonKt$distinctBy$1.L$2 = e;
                        channelsKt__Channels_commonKt$distinctBy$1.L$3 = channelIterator;
                        channelsKt__Channels_commonKt$distinctBy$1.L$4 = result;
                        channelsKt__Channels_commonKt$distinctBy$1.label = 4;
                        if (producerScope.send(e, channelsKt__Channels_commonKt$distinctBy$1) == obj) {
                            return obj;
                        }
                        keys.add(result);
                        channelsKt__Channels_commonKt$distinctBy$12 = channelsKt__Channels_commonKt$distinctBy$1;
                        channelIterator2 = channelIterator;
                        producerScope2 = producerScope;
                        coroutine_suspended = obj;
                        channelsKt__Channels_commonKt$distinctBy$12.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$distinctBy$12.L$1 = keys;
                        channelsKt__Channels_commonKt$distinctBy$12.L$2 = channelIterator2;
                        channelsKt__Channels_commonKt$distinctBy$12.label = 1;
                        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$distinctBy$12);
                        if (hasNext != coroutine_suspended) {
                        }
                    } else {
                        channelsKt__Channels_commonKt$distinctBy$12 = channelsKt__Channels_commonKt$distinctBy$1;
                        channelIterator2 = channelIterator;
                        producerScope2 = producerScope;
                        coroutine_suspended = obj;
                        channelsKt__Channels_commonKt$distinctBy$12.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$distinctBy$12.L$1 = keys;
                        channelsKt__Channels_commonKt$distinctBy$12.L$2 = channelIterator2;
                        channelsKt__Channels_commonKt$distinctBy$12.label = 1;
                        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$distinctBy$12);
                        if (hasNext != coroutine_suspended) {
                        }
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(result instanceof Result.Failure)) {
            producerScope2 = this.p$;
            keys = new HashSet();
            channelIterator2 = this.$this_distinctBy.iterator();
            channelsKt__Channels_commonKt$distinctBy$12 = this;
            channelsKt__Channels_commonKt$distinctBy$12.L$0 = producerScope2;
            channelsKt__Channels_commonKt$distinctBy$12.L$1 = keys;
            channelsKt__Channels_commonKt$distinctBy$12.L$2 = channelIterator2;
            channelsKt__Channels_commonKt$distinctBy$12.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$distinctBy$12);
            if (hasNext != coroutine_suspended) {
            }
        } else {
            throw ((Result.Failure) result).exception;
        }
    }
}
