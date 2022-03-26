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
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$dropWhile$1", f = "Channels.common.kt", i = {2, 3, 6}, l = {610, 610, 612, 613, 614, 610, 618, 617}, m = "invokeSuspend", n = {"e", "e", "e"}, s = {"L$1", "L$1", "L$1"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$dropWhile$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $predicate;
    final /* synthetic */ ReceiveChannel $this_dropWhile;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$dropWhile$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_dropWhile = receiveChannel;
        this.$predicate = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$1 = new ChannelsKt__Channels_commonKt$dropWhile$1(this.$this_dropWhile, this.$predicate, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$dropWhile$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$dropWhile$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$dropWhile$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x00dd A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x010c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0145 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x014e  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0170 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0176  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object e) {
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$1;
        Object result;
        ChannelIterator channelIterator2;
        ProducerScope producerScope2;
        Object obj2;
        ChannelIterator channelIterator3;
        Object result2;
        Object e2;
        ChannelIterator channelIterator4;
        Object result3;
        Object invoke;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                if (!(e instanceof Result.Failure)) {
                    ProducerScope producerScope3 = this.p$;
                    channelIterator4 = this.$this_dropWhile.iterator();
                    result2 = e;
                    ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$12 = this;
                    Object obj3 = coroutine_suspended;
                    ProducerScope producerScope4 = producerScope3;
                    channelsKt__Channels_commonKt$dropWhile$12.L$0 = producerScope4;
                    channelsKt__Channels_commonKt$dropWhile$12.L$1 = channelIterator4;
                    channelsKt__Channels_commonKt$dropWhile$12.label = 1;
                    Object hasNext = channelIterator4.hasNext(channelsKt__Channels_commonKt$dropWhile$12);
                    if (hasNext != obj3) {
                        return obj3;
                    }
                    channelsKt__Channels_commonKt$dropWhile$1 = channelsKt__Channels_commonKt$dropWhile$12;
                    e = hasNext;
                    result3 = result2;
                    result = obj3;
                    producerScope2 = producerScope4;
                    if (((Boolean) e).booleanValue()) {
                        channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator4;
                        channelsKt__Channels_commonKt$dropWhile$1.label = 2;
                        e = channelIterator4.next(channelsKt__Channels_commonKt$dropWhile$1);
                        if (e == result) {
                            return result;
                        }
                        Function2 function2 = channelsKt__Channels_commonKt$dropWhile$1.$predicate;
                        channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$dropWhile$1.L$1 = e;
                        channelsKt__Channels_commonKt$dropWhile$1.L$2 = channelIterator4;
                        channelsKt__Channels_commonKt$dropWhile$1.label = 3;
                        invoke = function2.invoke(e, channelsKt__Channels_commonKt$dropWhile$1);
                        if (invoke != result) {
                            return result;
                        }
                        e2 = e;
                        e = invoke;
                        obj2 = result;
                        result2 = result3;
                        channelIterator3 = channelIterator4;
                        if (((Boolean) e).booleanValue()) {
                            channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                            channelsKt__Channels_commonKt$dropWhile$1.L$1 = e2;
                            channelsKt__Channels_commonKt$dropWhile$1.label = 4;
                            if (producerScope2.send(e2, channelsKt__Channels_commonKt$dropWhile$1) == obj2) {
                                return obj2;
                            }
                            result = obj2;
                        } else {
                            channelsKt__Channels_commonKt$dropWhile$12 = channelsKt__Channels_commonKt$dropWhile$1;
                            producerScope4 = producerScope2;
                            channelIterator4 = channelIterator3;
                            obj3 = obj2;
                            channelsKt__Channels_commonKt$dropWhile$12.L$0 = producerScope4;
                            channelsKt__Channels_commonKt$dropWhile$12.L$1 = channelIterator4;
                            channelsKt__Channels_commonKt$dropWhile$12.label = 1;
                            Object hasNext2 = channelIterator4.hasNext(channelsKt__Channels_commonKt$dropWhile$12);
                            if (hasNext2 != obj3) {
                            }
                        }
                    }
                    channelIterator2 = channelsKt__Channels_commonKt$dropWhile$1.$this_dropWhile.iterator();
                    channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$dropWhile$1.label = 5;
                    e = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
                    if (e == result) {
                        return result;
                    }
                    if (!((Boolean) e).booleanValue()) {
                        channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$dropWhile$1.label = 6;
                        e = channelIterator2.next(channelsKt__Channels_commonKt$dropWhile$1);
                        if (e == result) {
                            return result;
                        }
                        channelIterator = channelIterator2;
                        obj = result;
                        producerScope = producerScope2;
                        channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$dropWhile$1.L$1 = e;
                        channelsKt__Channels_commonKt$dropWhile$1.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$dropWhile$1.label = 7;
                        if (producerScope.send(e, channelsKt__Channels_commonKt$dropWhile$1) != obj) {
                            return obj;
                        }
                        result = obj;
                        channelIterator2 = channelIterator;
                        producerScope2 = producerScope;
                        channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$dropWhile$1.label = 5;
                        e = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
                        if (e == result) {
                        }
                        if (!((Boolean) e).booleanValue()) {
                            return Unit.INSTANCE;
                        }
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            case 1:
                channelIterator4 = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result3 = e;
                    result = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    if (((Boolean) e).booleanValue()) {
                    }
                    channelIterator2 = channelsKt__Channels_commonKt$dropWhile$1.$this_dropWhile.iterator();
                    channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$dropWhile$1.label = 5;
                    e = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
                    if (e == result) {
                    }
                    if (!((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
                break;
            case 2:
                channelIterator4 = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result3 = e;
                    result = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    Function2 function22 = channelsKt__Channels_commonKt$dropWhile$1.$predicate;
                    channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$dropWhile$1.L$1 = e;
                    channelsKt__Channels_commonKt$dropWhile$1.L$2 = channelIterator4;
                    channelsKt__Channels_commonKt$dropWhile$1.label = 3;
                    invoke = function22.invoke(e, channelsKt__Channels_commonKt$dropWhile$1);
                    if (invoke != result) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
                break;
            case 3:
                channelIterator3 = (ChannelIterator) this.L$2;
                e2 = this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    result2 = e;
                    if (((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
                break;
            case 4:
                Object e3 = this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    channelIterator2 = channelsKt__Channels_commonKt$dropWhile$1.$this_dropWhile.iterator();
                    channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$dropWhile$1.label = 5;
                    e = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
                    if (e == result) {
                    }
                    if (!((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
                break;
            case 5:
                channelIterator2 = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    if (!((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
                break;
            case 6:
                channelIterator = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$dropWhile$1.L$1 = e;
                    channelsKt__Channels_commonKt$dropWhile$1.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$dropWhile$1.label = 7;
                    if (producerScope.send(e, channelsKt__Channels_commonKt$dropWhile$1) != obj) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
                break;
            case 7:
                channelIterator2 = (ChannelIterator) this.L$2;
                Object e4 = this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$dropWhile$1.label = 5;
                    e = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
                    if (e == result) {
                    }
                    if (!((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
