package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Channels.common.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$filterIndexed$1", f = "Channels.common.kt", i = {0, 1, 2, 2, 3, 3}, l = {654, 654, 657, 654, 656}, m = "invokeSuspend", n = {FirebaseAnalytics.Param.INDEX, FirebaseAnalytics.Param.INDEX, FirebaseAnalytics.Param.INDEX, "e", FirebaseAnalytics.Param.INDEX, "e"}, s = {"I$0", "I$0", "I$0", "L$1", "I$0", "L$1"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$filterIndexed$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function3 $predicate;
    final /* synthetic */ ReceiveChannel $this_filterIndexed;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$filterIndexed$1(ReceiveChannel receiveChannel, Function3 function3, Continuation continuation) {
        super(2, continuation);
        this.$this_filterIndexed = receiveChannel;
        this.$predicate = function3;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$filterIndexed$1 channelsKt__Channels_commonKt$filterIndexed$1 = new ChannelsKt__Channels_commonKt$filterIndexed$1(this.$this_filterIndexed, this.$predicate, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$filterIndexed$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$filterIndexed$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$filterIndexed$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00ab A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e3 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x010d  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object e) {
        ChannelsKt__Channels_commonKt$filterIndexed$1 channelsKt__Channels_commonKt$filterIndexed$1;
        int index;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        int index2;
        Object obj;
        Object e2;
        ChannelsKt__Channels_commonKt$filterIndexed$1 channelsKt__Channels_commonKt$filterIndexed$12;
        int index3;
        Object invoke;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator = (ChannelIterator) this.L$1;
                index3 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$filterIndexed$12 = this;
                    if (((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 2) {
                channelIterator = (ChannelIterator) this.L$1;
                index3 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$filterIndexed$12 = this;
                    Function3 function3 = channelsKt__Channels_commonKt$filterIndexed$12.$predicate;
                    Integer boxInt = Boxing.boxInt(index3);
                    int index4 = index3 + 1;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$filterIndexed$12.I$0 = index4;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$1 = e;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$filterIndexed$12.label = 3;
                    invoke = function3.invoke(boxInt, e, channelsKt__Channels_commonKt$filterIndexed$12);
                    if (invoke != obj) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                e2 = this.L$1;
                int index5 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    index = index5;
                    channelsKt__Channels_commonKt$filterIndexed$12 = this;
                    if (!((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 4) {
                channelIterator = (ChannelIterator) this.L$2;
                Object e3 = this.L$1;
                int index6 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    index = index6;
                    channelsKt__Channels_commonKt$filterIndexed$1 = this;
                    producerScope2 = producerScope;
                    channelIterator2 = channelIterator;
                    index2 = index;
                    channelsKt__Channels_commonKt$filterIndexed$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$filterIndexed$1.I$0 = index2;
                    channelsKt__Channels_commonKt$filterIndexed$1.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$filterIndexed$1.label = 1;
                    hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterIndexed$1);
                    if (hasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    channelsKt__Channels_commonKt$filterIndexed$12 = channelsKt__Channels_commonKt$filterIndexed$1;
                    e = hasNext;
                    obj = coroutine_suspended;
                    producerScope = producerScope2;
                    index3 = index2;
                    channelIterator = channelIterator2;
                    if (((Boolean) e).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$filterIndexed$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$filterIndexed$12.I$0 = index3;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$filterIndexed$12.label = 2;
                    e = channelIterator.next(channelsKt__Channels_commonKt$filterIndexed$12);
                    if (e == obj) {
                        return obj;
                    }
                    Function3 function32 = channelsKt__Channels_commonKt$filterIndexed$12.$predicate;
                    Integer boxInt2 = Boxing.boxInt(index3);
                    int index42 = index3 + 1;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$filterIndexed$12.I$0 = index42;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$1 = e;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$filterIndexed$12.label = 3;
                    invoke = function32.invoke(boxInt2, e, channelsKt__Channels_commonKt$filterIndexed$12);
                    if (invoke != obj) {
                        return obj;
                    }
                    e2 = e;
                    e = invoke;
                    index = index42;
                    if (!((Boolean) e).booleanValue()) {
                        channelsKt__Channels_commonKt$filterIndexed$12.L$0 = producerScope;
                        channelsKt__Channels_commonKt$filterIndexed$12.I$0 = index;
                        channelsKt__Channels_commonKt$filterIndexed$12.L$1 = e2;
                        channelsKt__Channels_commonKt$filterIndexed$12.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$filterIndexed$12.label = 4;
                        if (producerScope.send(e2, channelsKt__Channels_commonKt$filterIndexed$12) == obj) {
                            return obj;
                        }
                        channelsKt__Channels_commonKt$filterIndexed$1 = channelsKt__Channels_commonKt$filterIndexed$12;
                        coroutine_suspended = obj;
                        producerScope2 = producerScope;
                        channelIterator2 = channelIterator;
                        index2 = index;
                        channelsKt__Channels_commonKt$filterIndexed$1.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$filterIndexed$1.I$0 = index2;
                        channelsKt__Channels_commonKt$filterIndexed$1.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$filterIndexed$1.label = 1;
                        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterIndexed$1);
                        if (hasNext != coroutine_suspended) {
                        }
                    } else {
                        channelsKt__Channels_commonKt$filterIndexed$1 = channelsKt__Channels_commonKt$filterIndexed$12;
                        producerScope2 = producerScope;
                        coroutine_suspended = obj;
                        channelIterator2 = channelIterator;
                        index2 = index;
                        channelsKt__Channels_commonKt$filterIndexed$1.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$filterIndexed$1.I$0 = index2;
                        channelsKt__Channels_commonKt$filterIndexed$1.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$filterIndexed$1.label = 1;
                        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterIndexed$1);
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
            index2 = 0;
            channelIterator2 = this.$this_filterIndexed.iterator();
            channelsKt__Channels_commonKt$filterIndexed$1 = this;
            channelsKt__Channels_commonKt$filterIndexed$1.L$0 = producerScope2;
            channelsKt__Channels_commonKt$filterIndexed$1.I$0 = index2;
            channelsKt__Channels_commonKt$filterIndexed$1.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$filterIndexed$1.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterIndexed$1);
            if (hasNext != coroutine_suspended) {
            }
        } else {
            throw ((Result.Failure) e).exception;
        }
    }
}
