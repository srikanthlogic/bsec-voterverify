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
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", ExifInterface.LONGITUDE_EAST, "R", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$mapIndexed$1", f = "Channels.common.kt", i = {0, 1, 2, 2, 3, 3}, l = {1214, 1214, 1217, 1216, 1219}, m = "invokeSuspend", n = {FirebaseAnalytics.Param.INDEX, FirebaseAnalytics.Param.INDEX, FirebaseAnalytics.Param.INDEX, "e", FirebaseAnalytics.Param.INDEX, "e"}, s = {"I$0", "I$0", "I$0", "L$1", "I$0", "L$1"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$mapIndexed$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_mapIndexed;
    final /* synthetic */ Function3 $transform;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$mapIndexed$1(ReceiveChannel receiveChannel, Function3 function3, Continuation continuation) {
        super(2, continuation);
        this.$this_mapIndexed = receiveChannel;
        this.$transform = function3;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$1 = new ChannelsKt__Channels_commonKt$mapIndexed$1(this.$this_mapIndexed, this.$transform, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$mapIndexed$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$mapIndexed$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$mapIndexed$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00ac A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e6 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ff A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0106  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object e) {
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$1;
        Object result;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        int index;
        ProducerScope producerScope2;
        Object result2;
        Object obj;
        ChannelIterator channelIterator2;
        ProducerScope producerScope3;
        Object e2;
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$12;
        Object obj2;
        ChannelIterator channelIterator3;
        int index2;
        Object invoke;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator3 = (ChannelIterator) this.L$1;
                index2 = this.I$0;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result2 = e;
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$mapIndexed$12 = this;
                    if (((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 2) {
                channelIterator3 = (ChannelIterator) this.L$1;
                index2 = this.I$0;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result2 = e;
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$mapIndexed$12 = this;
                    Function3 function3 = channelsKt__Channels_commonKt$mapIndexed$12.$transform;
                    Integer boxInt = Boxing.boxInt(index2);
                    int index3 = index2 + 1;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope3;
                    channelsKt__Channels_commonKt$mapIndexed$12.I$0 = index3;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$1 = e;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$2 = channelIterator3;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$3 = producerScope3;
                    channelsKt__Channels_commonKt$mapIndexed$12.label = 3;
                    invoke = function3.invoke(boxInt, e, channelsKt__Channels_commonKt$mapIndexed$12);
                    if (invoke != obj2) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 3) {
                producerScope3 = (ProducerScope) this.L$3;
                channelIterator2 = (ChannelIterator) this.L$2;
                e2 = this.L$1;
                index = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = e;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$mapIndexed$12 = this;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$mapIndexed$12.I$0 = index;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$1 = e2;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$mapIndexed$12.label = 4;
                    if (producerScope3.send(e, channelsKt__Channels_commonKt$mapIndexed$12) != obj) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 4) {
                channelIterator = (ChannelIterator) this.L$2;
                Object e3 = this.L$1;
                index = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = e;
                    channelsKt__Channels_commonKt$mapIndexed$1 = this;
                    producerScope2 = producerScope;
                    result2 = result;
                    channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$mapIndexed$1.I$0 = index;
                    channelsKt__Channels_commonKt$mapIndexed$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$mapIndexed$1.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexed$1);
                    if (hasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    channelsKt__Channels_commonKt$mapIndexed$12 = channelsKt__Channels_commonKt$mapIndexed$1;
                    e = hasNext;
                    obj2 = coroutine_suspended;
                    producerScope3 = producerScope2;
                    index2 = index;
                    channelIterator3 = channelIterator;
                    if (((Boolean) e).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope3;
                    channelsKt__Channels_commonKt$mapIndexed$12.I$0 = index2;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$1 = channelIterator3;
                    channelsKt__Channels_commonKt$mapIndexed$12.label = 2;
                    e = channelIterator3.next(channelsKt__Channels_commonKt$mapIndexed$12);
                    if (e == obj2) {
                        return obj2;
                    }
                    Function3 function32 = channelsKt__Channels_commonKt$mapIndexed$12.$transform;
                    Integer boxInt2 = Boxing.boxInt(index2);
                    int index32 = index2 + 1;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope3;
                    channelsKt__Channels_commonKt$mapIndexed$12.I$0 = index32;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$1 = e;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$2 = channelIterator3;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$3 = producerScope3;
                    channelsKt__Channels_commonKt$mapIndexed$12.label = 3;
                    invoke = function32.invoke(boxInt2, e, channelsKt__Channels_commonKt$mapIndexed$12);
                    if (invoke != obj2) {
                        return obj2;
                    }
                    result = result2;
                    producerScope = producerScope3;
                    e2 = e;
                    e = invoke;
                    obj = obj2;
                    channelIterator2 = channelIterator3;
                    index = index32;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$mapIndexed$12.I$0 = index;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$1 = e2;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$mapIndexed$12.label = 4;
                    if (producerScope3.send(e, channelsKt__Channels_commonKt$mapIndexed$12) != obj) {
                        return obj;
                    }
                    channelsKt__Channels_commonKt$mapIndexed$1 = channelsKt__Channels_commonKt$mapIndexed$12;
                    channelIterator = channelIterator2;
                    coroutine_suspended = obj;
                    producerScope2 = producerScope;
                    result2 = result;
                    channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$mapIndexed$1.I$0 = index;
                    channelsKt__Channels_commonKt$mapIndexed$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$mapIndexed$1.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexed$1);
                    if (hasNext == coroutine_suspended) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(e instanceof Result.Failure)) {
            producerScope2 = this.p$;
            index = 0;
            channelIterator = this.$this_mapIndexed.iterator();
            result2 = e;
            channelsKt__Channels_commonKt$mapIndexed$1 = this;
            channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope2;
            channelsKt__Channels_commonKt$mapIndexed$1.I$0 = index;
            channelsKt__Channels_commonKt$mapIndexed$1.L$1 = channelIterator;
            channelsKt__Channels_commonKt$mapIndexed$1.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexed$1);
            if (hasNext == coroutine_suspended) {
            }
        } else {
            throw ((Result.Failure) e).exception;
        }
    }
}
