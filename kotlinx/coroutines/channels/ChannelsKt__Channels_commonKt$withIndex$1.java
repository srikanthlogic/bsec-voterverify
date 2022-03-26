package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Unit;
import kotlin.collections.IndexedValue;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Channels.common.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/ProducerScope;", "Lkotlin/collections/IndexedValue;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$withIndex$1", f = "Channels.common.kt", i = {0, 1, 2, 2}, l = {1418, 1418, 1421, 1420}, m = "invokeSuspend", n = {FirebaseAnalytics.Param.INDEX, FirebaseAnalytics.Param.INDEX, FirebaseAnalytics.Param.INDEX, "e"}, s = {"I$0", "I$0", "I$0", "L$1"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$withIndex$1 extends SuspendLambda implements Function2<ProducerScope<? super IndexedValue<? extends E>>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_withIndex;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$withIndex$1(ReceiveChannel receiveChannel, Continuation continuation) {
        super(2, continuation);
        this.$this_withIndex = receiveChannel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$withIndex$1 channelsKt__Channels_commonKt$withIndex$1 = new ChannelsKt__Channels_commonKt$withIndex$1(this.$this_withIndex, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$withIndex$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$withIndex$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$withIndex$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0091 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00ca A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00d3  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object e) {
        int index;
        Object result;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        Object e2;
        ChannelsKt__Channels_commonKt$withIndex$1 channelsKt__Channels_commonKt$withIndex$1;
        ChannelsKt__Channels_commonKt$withIndex$1 channelsKt__Channels_commonKt$withIndex$12;
        int index2;
        Object obj;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        Object result2;
        int index3;
        IndexedValue indexedValue;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator = (ChannelIterator) this.L$1;
                index3 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    result = e;
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$withIndex$1 = this;
                    if (((Boolean) e).booleanValue()) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 2) {
                channelIterator2 = (ChannelIterator) this.L$1;
                index3 = this.I$0;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    obj = coroutine_suspended;
                    channelsKt__Channels_commonKt$withIndex$1 = this;
                    result2 = e;
                    index = index3 + 1;
                    indexedValue = new IndexedValue(index3, e);
                    channelsKt__Channels_commonKt$withIndex$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$withIndex$1.I$0 = index;
                    channelsKt__Channels_commonKt$withIndex$1.L$1 = e;
                    channelsKt__Channels_commonKt$withIndex$1.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$withIndex$1.label = 3;
                    if (producerScope2.send(indexedValue, channelsKt__Channels_commonKt$withIndex$1) != obj) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else if (i == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                Object e3 = this.L$1;
                int index4 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(e instanceof Result.Failure)) {
                    index = index4;
                    result = e;
                    e2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$withIndex$1 = this;
                    channelsKt__Channels_commonKt$withIndex$12 = channelsKt__Channels_commonKt$withIndex$1;
                    index2 = index;
                    channelsKt__Channels_commonKt$withIndex$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$withIndex$12.I$0 = index2;
                    channelsKt__Channels_commonKt$withIndex$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$withIndex$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$withIndex$12);
                    if (hasNext == e2) {
                        return e2;
                    }
                    channelsKt__Channels_commonKt$withIndex$1 = channelsKt__Channels_commonKt$withIndex$12;
                    e = hasNext;
                    obj = e2;
                    index3 = index2;
                    if (((Boolean) e).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    channelsKt__Channels_commonKt$withIndex$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$withIndex$1.I$0 = index3;
                    channelsKt__Channels_commonKt$withIndex$1.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$withIndex$1.label = 2;
                    e = channelIterator.next(channelsKt__Channels_commonKt$withIndex$1);
                    if (e == obj) {
                        return obj;
                    }
                    channelIterator2 = channelIterator;
                    result2 = result;
                    producerScope2 = producerScope;
                    index = index3 + 1;
                    indexedValue = new IndexedValue(index3, e);
                    channelsKt__Channels_commonKt$withIndex$1.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$withIndex$1.I$0 = index;
                    channelsKt__Channels_commonKt$withIndex$1.L$1 = e;
                    channelsKt__Channels_commonKt$withIndex$1.L$2 = channelIterator2;
                    channelsKt__Channels_commonKt$withIndex$1.label = 3;
                    if (producerScope2.send(indexedValue, channelsKt__Channels_commonKt$withIndex$1) != obj) {
                        return obj;
                    }
                    e2 = obj;
                    result = result2;
                    channelIterator = channelIterator2;
                    producerScope = producerScope2;
                    channelsKt__Channels_commonKt$withIndex$12 = channelsKt__Channels_commonKt$withIndex$1;
                    index2 = index;
                    channelsKt__Channels_commonKt$withIndex$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$withIndex$12.I$0 = index2;
                    channelsKt__Channels_commonKt$withIndex$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$withIndex$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$withIndex$12);
                    if (hasNext == e2) {
                    }
                } else {
                    throw ((Result.Failure) e).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(e instanceof Result.Failure)) {
            ProducerScope producerScope3 = this.p$;
            channelIterator = this.$this_withIndex.iterator();
            result = e;
            channelsKt__Channels_commonKt$withIndex$12 = this;
            e2 = coroutine_suspended;
            index2 = 0;
            producerScope = producerScope3;
            channelsKt__Channels_commonKt$withIndex$12.L$0 = producerScope;
            channelsKt__Channels_commonKt$withIndex$12.I$0 = index2;
            channelsKt__Channels_commonKt$withIndex$12.L$1 = channelIterator;
            channelsKt__Channels_commonKt$withIndex$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$withIndex$12);
            if (hasNext == e2) {
            }
        } else {
            throw ((Result.Failure) e).exception;
        }
    }
}
