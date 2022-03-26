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
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$drop$1", f = "Channels.common.kt", i = {0, 1, 2, 3, 4, 4}, l = {584, 584, 589, 584, 594, 593}, m = "invokeSuspend", n = {"remaining", "remaining", "remaining", "remaining", "remaining", "e"}, s = {"I$0", "I$0", "I$0", "I$0", "I$0", "L$1"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$drop$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ int $n;
    final /* synthetic */ ReceiveChannel $this_drop;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$drop$1(ReceiveChannel receiveChannel, int i, Continuation continuation) {
        super(2, continuation);
        this.$this_drop = receiveChannel;
        this.$n = i;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$drop$1 channelsKt__Channels_commonKt$drop$1 = new ChannelsKt__Channels_commonKt$drop$1(this.$this_drop, this.$n, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$drop$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$drop$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$drop$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00d7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0125 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0126  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0157 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0160  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object result) {
        Object obj;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        int remaining;
        ChannelsKt__Channels_commonKt$drop$1 channelsKt__Channels_commonKt$drop$1;
        Object obj2;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        int remaining2;
        Object result2;
        Object result3;
        Object hasNext;
        ProducerScope producerScope3;
        Object result4;
        ChannelsKt__Channels_commonKt$drop$1 channelsKt__Channels_commonKt$drop$12;
        int remaining3;
        ProducerScope producerScope4;
        ChannelIterator channelIterator3;
        Object result5;
        Object result6;
        ChannelIterator channelIterator4;
        Object hasNext2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator4 = (ChannelIterator) this.L$1;
                remaining = this.I$0;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    result5 = result;
                    result6 = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = this;
                    if (!((Boolean) result).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else if (i == 2) {
                ChannelIterator channelIterator5 = (ChannelIterator) this.L$1;
                remaining = this.I$0;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    result4 = result;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = this;
                    remaining--;
                    if (remaining != 0) {
                        result = result4;
                        channelIterator = channelsKt__Channels_commonKt$drop$1.$this_drop.iterator();
                        producerScope = producerScope3;
                        channelsKt__Channels_commonKt$drop$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$drop$1.I$0 = remaining;
                        channelsKt__Channels_commonKt$drop$1.L$1 = channelIterator;
                        channelsKt__Channels_commonKt$drop$1.label = 3;
                        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$1);
                        if (hasNext == obj) {
                        }
                    } else {
                        channelsKt__Channels_commonKt$drop$12 = channelsKt__Channels_commonKt$drop$1;
                        coroutine_suspended = obj;
                        remaining3 = remaining;
                        producerScope4 = producerScope3;
                        channelIterator3 = channelIterator5;
                        channelsKt__Channels_commonKt$drop$12.L$0 = producerScope4;
                        channelsKt__Channels_commonKt$drop$12.I$0 = remaining3;
                        channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator3;
                        channelsKt__Channels_commonKt$drop$12.label = 1;
                        hasNext2 = channelIterator3.hasNext(channelsKt__Channels_commonKt$drop$12);
                        if (hasNext2 != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        channelsKt__Channels_commonKt$drop$1 = channelsKt__Channels_commonKt$drop$12;
                        result = hasNext2;
                        result5 = result4;
                        result6 = coroutine_suspended;
                        producerScope3 = producerScope4;
                        remaining = remaining3;
                        channelIterator4 = channelIterator3;
                        if (!((Boolean) result).booleanValue()) {
                            channelsKt__Channels_commonKt$drop$1.L$0 = producerScope3;
                            channelsKt__Channels_commonKt$drop$1.I$0 = remaining;
                            channelsKt__Channels_commonKt$drop$1.L$1 = channelIterator4;
                            channelsKt__Channels_commonKt$drop$1.label = 2;
                            if (channelIterator4.next(channelsKt__Channels_commonKt$drop$1) == result6) {
                                return result6;
                            }
                            channelIterator5 = channelIterator4;
                            obj = result6;
                            result4 = result5;
                            remaining--;
                            if (remaining != 0) {
                            }
                        } else {
                            obj = result6;
                            result = result5;
                            channelIterator = channelsKt__Channels_commonKt$drop$1.$this_drop.iterator();
                            producerScope = producerScope3;
                            channelsKt__Channels_commonKt$drop$1.L$0 = producerScope;
                            channelsKt__Channels_commonKt$drop$1.I$0 = remaining;
                            channelsKt__Channels_commonKt$drop$1.L$1 = channelIterator;
                            channelsKt__Channels_commonKt$drop$1.label = 3;
                            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$1);
                            if (hasNext == obj) {
                            }
                        }
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else if (i == 3) {
                channelIterator2 = (ChannelIterator) this.L$1;
                remaining = this.I$0;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = this;
                    result3 = result;
                    if (((Boolean) result).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else if (i == 4) {
                channelIterator2 = (ChannelIterator) this.L$1;
                int remaining4 = this.I$0;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = this;
                    remaining2 = remaining4;
                    result2 = result;
                    channelsKt__Channels_commonKt$drop$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$drop$1.I$0 = remaining2;
                    channelsKt__Channels_commonKt$drop$1.L$1 = result;
                    channelsKt__Channels_commonKt$drop$1.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$drop$1.label = 5;
                    if (producerScope2.send(result, channelsKt__Channels_commonKt$drop$1) != obj2) {
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else if (i == 5) {
                channelIterator = (ChannelIterator) this.L$2;
                Object e = this.L$1;
                int remaining5 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(result instanceof Result.Failure)) {
                    Object result7 = result;
                    remaining = remaining5;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = this;
                    result = result7;
                    channelsKt__Channels_commonKt$drop$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$drop$1.I$0 = remaining;
                    channelsKt__Channels_commonKt$drop$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$drop$1.label = 3;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$1);
                    if (hasNext == obj) {
                        return obj;
                    }
                    result3 = result;
                    result = hasNext;
                    obj2 = obj;
                    producerScope2 = producerScope;
                    channelIterator2 = channelIterator;
                    if (((Boolean) result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$drop$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$drop$1.I$0 = remaining;
                    channelsKt__Channels_commonKt$drop$1.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$drop$1.label = 4;
                    result = channelIterator2.next(channelsKt__Channels_commonKt$drop$1);
                    if (result == obj2) {
                        return obj2;
                    }
                    remaining2 = remaining;
                    result2 = result3;
                    channelsKt__Channels_commonKt$drop$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$drop$1.I$0 = remaining2;
                    channelsKt__Channels_commonKt$drop$1.L$1 = result;
                    channelsKt__Channels_commonKt$drop$1.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$drop$1.label = 5;
                    if (producerScope2.send(result, channelsKt__Channels_commonKt$drop$1) != obj2) {
                        return obj2;
                    }
                    result7 = result2;
                    remaining = remaining2;
                    channelIterator = channelIterator2;
                    producerScope = producerScope2;
                    obj = obj2;
                    result = result7;
                    channelsKt__Channels_commonKt$drop$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$drop$1.I$0 = remaining;
                    channelsKt__Channels_commonKt$drop$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$drop$1.label = 3;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$1);
                    if (hasNext == obj) {
                    }
                } else {
                    throw ((Result.Failure) result).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(result instanceof Result.Failure)) {
            producerScope4 = this.p$;
            if (this.$n >= 0) {
                remaining3 = this.$n;
                if (remaining3 > 0) {
                    channelIterator3 = this.$this_drop.iterator();
                    result4 = result;
                    channelsKt__Channels_commonKt$drop$12 = this;
                    channelsKt__Channels_commonKt$drop$12.L$0 = producerScope4;
                    channelsKt__Channels_commonKt$drop$12.I$0 = remaining3;
                    channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator3;
                    channelsKt__Channels_commonKt$drop$12.label = 1;
                    hasNext2 = channelIterator3.hasNext(channelsKt__Channels_commonKt$drop$12);
                    if (hasNext2 != coroutine_suspended) {
                    }
                } else {
                    producerScope3 = producerScope4;
                    remaining = remaining3;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = this;
                    channelIterator = channelsKt__Channels_commonKt$drop$1.$this_drop.iterator();
                    producerScope = producerScope3;
                    channelsKt__Channels_commonKt$drop$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$drop$1.I$0 = remaining;
                    channelsKt__Channels_commonKt$drop$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$drop$1.label = 3;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$1);
                    if (hasNext == obj) {
                    }
                }
            } else {
                throw new IllegalArgumentException(("Requested element count " + this.$n + " is less than zero.").toString());
            }
        } else {
            throw ((Result.Failure) result).exception;
        }
    }
}
