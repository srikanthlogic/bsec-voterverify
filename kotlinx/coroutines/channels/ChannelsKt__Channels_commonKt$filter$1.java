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
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$filter$1", f = "Channels.common.kt", i = {2, 3}, l = {634, 634, 636, 634, 635}, m = "invokeSuspend", n = {"e", "e"}, s = {"L$1", "L$1"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$filter$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $predicate;
    final /* synthetic */ ReceiveChannel $this_filter;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$filter$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_filter = receiveChannel;
        this.$predicate = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$filter$1 channelsKt__Channels_commonKt$filter$1 = new ChannelsKt__Channels_commonKt$filter$1(this.$this_filter, this.$predicate, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$filter$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$filter$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$filter$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x009a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00c8 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00ef  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object e) {
        ChannelsKt__Channels_commonKt$filter$1 channelsKt__Channels_commonKt$filter$1;
        ChannelIterator channelIterator;
        ProducerScope producerScope;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        Object obj;
        Object e2;
        ChannelsKt__Channels_commonKt$filter$1 channelsKt__Channels_commonKt$filter$12;
        Object obj2;
        ChannelIterator channelIterator3;
        Object invoke;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator3 = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$filter$12 = this;
                    if (((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 2) {
                channelIterator3 = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$filter$12 = this;
                    Function2 function2 = channelsKt__Channels_commonKt$filter$12.$predicate;
                    channelsKt__Channels_commonKt$filter$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$filter$12.L$1 = e;
                    channelsKt__Channels_commonKt$filter$12.L$2 = channelIterator3;
                    channelsKt__Channels_commonKt$filter$12.label = 3;
                    invoke = function2.invoke(e, channelsKt__Channels_commonKt$filter$12);
                    if (invoke != obj2) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                e2 = this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$filter$12 = this;
                    if (!((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 4) {
                channelIterator = (ChannelIterator) this.L$2;
                Object e3 = this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    channelsKt__Channels_commonKt$filter$1 = this;
                    producerScope2 = producerScope;
                    channelIterator2 = channelIterator;
                    channelsKt__Channels_commonKt$filter$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$filter$1.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$filter$1.label = 1;
                    hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filter$1);
                    if (hasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    channelsKt__Channels_commonKt$filter$12 = channelsKt__Channels_commonKt$filter$1;
                    e = hasNext;
                    obj2 = coroutine_suspended;
                    producerScope = producerScope2;
                    channelIterator3 = channelIterator2;
                    if (((Boolean) e).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$filter$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$filter$12.L$1 = channelIterator3;
                    channelsKt__Channels_commonKt$filter$12.label = 2;
                    e = channelIterator3.next(channelsKt__Channels_commonKt$filter$12);
                    if (e == obj2) {
                        return obj2;
                    }
                    Function2 function22 = channelsKt__Channels_commonKt$filter$12.$predicate;
                    channelsKt__Channels_commonKt$filter$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$filter$12.L$1 = e;
                    channelsKt__Channels_commonKt$filter$12.L$2 = channelIterator3;
                    channelsKt__Channels_commonKt$filter$12.label = 3;
                    invoke = function22.invoke(e, channelsKt__Channels_commonKt$filter$12);
                    if (invoke != obj2) {
                        return obj2;
                    }
                    e2 = e;
                    e = invoke;
                    obj = obj2;
                    channelIterator = channelIterator3;
                    if (!((Boolean) e).booleanValue()) {
                        channelsKt__Channels_commonKt$filter$12.L$0 = producerScope;
                        channelsKt__Channels_commonKt$filter$12.L$1 = e2;
                        channelsKt__Channels_commonKt$filter$12.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$filter$12.label = 4;
                        if (producerScope.send(e2, channelsKt__Channels_commonKt$filter$12) == obj) {
                            return obj;
                        }
                        channelsKt__Channels_commonKt$filter$1 = channelsKt__Channels_commonKt$filter$12;
                        coroutine_suspended = obj;
                        producerScope2 = producerScope;
                        channelIterator2 = channelIterator;
                        channelsKt__Channels_commonKt$filter$1.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$filter$1.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$filter$1.label = 1;
                        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filter$1);
                        if (hasNext != coroutine_suspended) {
                        }
                    } else {
                        channelsKt__Channels_commonKt$filter$1 = channelsKt__Channels_commonKt$filter$12;
                        producerScope2 = producerScope;
                        channelIterator2 = channelIterator;
                        coroutine_suspended = obj;
                        channelsKt__Channels_commonKt$filter$1.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$filter$1.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$filter$1.label = 1;
                        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filter$1);
                        if (hasNext != coroutine_suspended) {
                        }
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(e instanceof Result.Failure)) {
            producerScope2 = this.p$;
            channelIterator2 = this.$this_filter.iterator();
            channelsKt__Channels_commonKt$filter$1 = this;
            channelsKt__Channels_commonKt$filter$1.L$0 = producerScope2;
            channelsKt__Channels_commonKt$filter$1.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$filter$1.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filter$1);
            if (hasNext != coroutine_suspended) {
            }
        } else {
            throw ((Result.Failure) e).exception;
        }
    }
}
