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
@DebugMetadata(c = "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt$map$1", f = "Channels.common.kt", i = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3}, l = {1193, 1193, 1195, 1196, 1897}, m = "invokeSuspend", n = {"$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "it", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "it"}, s = {"L$1", "L$3", "L$4", "L$5", "L$1", "L$3", "L$4", "L$5", "L$1", "L$3", "L$4", "L$5", "L$7", "L$8", "L$1", "L$3", "L$4", "L$5", "L$7", "L$8"})
/* loaded from: classes3.dex */
public final class ChannelsKt__Channels_commonKt$map$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_map;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    Object L$9;
    int label;
    private ProducerScope p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelsKt__Channels_commonKt$map$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_map = receiveChannel;
        this.$transform = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$1 = new ChannelsKt__Channels_commonKt$map$1(this.$this_map, this.$transform, continuation);
        ProducerScope producerScope = (ProducerScope) obj;
        channelsKt__Channels_commonKt$map$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$map$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((ChannelsKt__Channels_commonKt$map$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX INFO: Multiple debug info for r0v33 'it'  java.lang.Object: [D('it' java.lang.Object), D('$receiver$iv' kotlinx.coroutines.channels.ReceiveChannel)] */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0184 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0185  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0195 A[Catch: all -> 0x0233, TRY_LEAVE, TryCatch #3 {all -> 0x0233, blocks: (B:48:0x018d, B:50:0x0195, B:68:0x022a), top: B:90:0x018d }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01e1 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0209 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x020a  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x022a A[Catch: all -> 0x0233, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x0233, blocks: (B:48:0x018d, B:50:0x0195, B:68:0x022a), top: B:90:0x018d }] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object invokeSuspend(Object result) {
        int $receiver$iv;
        int $i$f$consume;
        int $i$f$consume2;
        Object obj;
        ReceiveChannel $receiver$iv$iv;
        ChannelIterator channelIterator;
        ProducerScope producerScope;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$1;
        Object obj2;
        ProducerScope producerScope2;
        Object result2;
        ReceiveChannel $receiver$iv2;
        ReceiveChannel e$iv;
        Throwable cause$iv$iv;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$12;
        Object it;
        Throwable cause$iv$iv2;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$13;
        int $i$a$1$consumeEach;
        ProducerScope producerScope3;
        ChannelIterator channelIterator2;
        int $i$f$consume3;
        int $i$f$consumeEach;
        int $i$a$4$consume;
        Object result3;
        Object obj3;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$14;
        ReceiveChannel $receiver$iv3;
        Object hasNext;
        int $i$a$4$consume2;
        ChannelIterator channelIterator3;
        int $i$f$consumeEach2;
        int $i$a$4$consume3;
        int $i$f$consumeEach3;
        Object e$iv2 = result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        boolean z = true;
        int $i$f$consumeEach4 = 0;
        if (i != 0) {
            try {
                if (i == 1) {
                    $i$a$4$consume2 = 0;
                    $i$f$consume3 = 0;
                    channelIterator3 = (ChannelIterator) this.L$6;
                    $receiver$iv3 = (ReceiveChannel) this.L$5;
                    cause$iv$iv2 = (Throwable) this.L$4;
                    $receiver$iv$iv = (ReceiveChannel) this.L$3;
                    channelsKt__Channels_commonKt$map$13 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
                    $receiver$iv2 = (ReceiveChannel) this.L$1;
                    producerScope3 = (ProducerScope) this.L$0;
                    if (e$iv2 instanceof Result.Failure) {
                        throw ((Result.Failure) e$iv2).exception;
                    }
                    hasNext = e$iv2;
                    $i$a$1$consumeEach = 0;
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$map$14 = this;
                    $i$f$consumeEach2 = 0;
                    result2 = hasNext;
                    if (!((Boolean) hasNext).booleanValue()) {
                    }
                } else if (i == 2) {
                    $i$f$consume3 = 0;
                    z = false;
                    channelIterator3 = (ChannelIterator) this.L$6;
                    $receiver$iv3 = (ReceiveChannel) this.L$5;
                    Throwable cause$iv$iv3 = (Throwable) this.L$4;
                    $receiver$iv$iv = (ReceiveChannel) this.L$3;
                    channelsKt__Channels_commonKt$map$1 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
                    $receiver$iv2 = (ReceiveChannel) this.L$1;
                    producerScope = (ProducerScope) this.L$0;
                    if (!(e$iv2 instanceof Result.Failure)) {
                        $i$a$4$consume3 = 0;
                        cause$iv$iv = cause$iv$iv3;
                        obj2 = coroutine_suspended;
                        channelsKt__Channels_commonKt$map$12 = this;
                        $i$f$consumeEach3 = 0;
                        result2 = e$iv2;
                        Function2 function2 = channelsKt__Channels_commonKt$map$12.$transform;
                        channelsKt__Channels_commonKt$map$12.L$0 = producerScope;
                        channelsKt__Channels_commonKt$map$12.L$1 = $receiver$iv2;
                        channelsKt__Channels_commonKt$map$12.L$2 = channelsKt__Channels_commonKt$map$1;
                        channelsKt__Channels_commonKt$map$12.L$3 = $receiver$iv$iv;
                        channelsKt__Channels_commonKt$map$12.L$4 = cause$iv$iv;
                        channelsKt__Channels_commonKt$map$12.L$5 = $receiver$iv3;
                        channelsKt__Channels_commonKt$map$12.L$6 = channelIterator3;
                        channelsKt__Channels_commonKt$map$12.L$7 = e$iv2;
                        it = e$iv2;
                        channelsKt__Channels_commonKt$map$12.L$8 = it;
                        channelsKt__Channels_commonKt$map$12.L$9 = producerScope;
                        channelsKt__Channels_commonKt$map$12.label = 3;
                        obj = function2.invoke(it, channelsKt__Channels_commonKt$map$12);
                        if (obj != obj2) {
                        }
                    } else {
                        throw ((Result.Failure) e$iv2).exception;
                    }
                } else if (i == 3) {
                    producerScope = (ProducerScope) this.L$9;
                    Object it2 = this.L$8;
                    Object e$iv3 = this.L$7;
                    channelIterator = (ChannelIterator) this.L$6;
                    ReceiveChannel $receiver$iv4 = (ReceiveChannel) this.L$5;
                    Throwable cause$iv$iv4 = (Throwable) this.L$4;
                    $receiver$iv$iv = (ReceiveChannel) this.L$3;
                    channelsKt__Channels_commonKt$map$1 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
                    ReceiveChannel $receiver$iv5 = (ReceiveChannel) this.L$1;
                    producerScope2 = (ProducerScope) this.L$0;
                    try {
                        if (!(e$iv2 instanceof Result.Failure)) {
                            it = it2;
                            $i$f$consume = 0;
                            channelsKt__Channels_commonKt$map$12 = this;
                            $i$f$consume2 = 0;
                            result2 = e$iv2;
                            e$iv2 = e$iv3;
                            e$iv = $receiver$iv4;
                            $receiver$iv2 = $receiver$iv5;
                            obj = result2;
                            $i$f$consumeEach4 = 0;
                            cause$iv$iv = cause$iv$iv4;
                            obj2 = coroutine_suspended;
                            $receiver$iv = 0;
                            channelsKt__Channels_commonKt$map$12.L$0 = producerScope2;
                            channelsKt__Channels_commonKt$map$12.L$1 = $receiver$iv2;
                            channelsKt__Channels_commonKt$map$12.L$2 = channelsKt__Channels_commonKt$map$1;
                            channelsKt__Channels_commonKt$map$12.L$3 = $receiver$iv$iv;
                            channelsKt__Channels_commonKt$map$12.L$4 = cause$iv$iv;
                            channelsKt__Channels_commonKt$map$12.L$5 = e$iv;
                            channelsKt__Channels_commonKt$map$12.L$6 = channelIterator;
                            channelsKt__Channels_commonKt$map$12.L$7 = e$iv2;
                            channelsKt__Channels_commonKt$map$12.L$8 = it;
                            channelsKt__Channels_commonKt$map$12.label = 4;
                            if (producerScope.send(obj, channelsKt__Channels_commonKt$map$12) != obj2) {
                            }
                        } else {
                            throw ((Result.Failure) e$iv2).exception;
                        }
                    } catch (Throwable th) {
                        e$iv$iv = th;
                    }
                } else if (i == 4) {
                    Object it3 = this.L$8;
                    Object e$iv4 = this.L$7;
                    channelIterator2 = (ChannelIterator) this.L$6;
                    ReceiveChannel $receiver$iv6 = (ReceiveChannel) this.L$5;
                    cause$iv$iv2 = (Throwable) this.L$4;
                    $receiver$iv$iv = (ReceiveChannel) this.L$3;
                    channelsKt__Channels_commonKt$map$13 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
                    $receiver$iv2 = (ReceiveChannel) this.L$1;
                    producerScope3 = (ProducerScope) this.L$0;
                    try {
                        if (!(e$iv2 instanceof Result.Failure)) {
                            $receiver$iv = 0;
                            $i$f$consume = 0;
                            ReceiveChannel $receiver$iv7 = $receiver$iv6;
                            $i$f$consume2 = 0;
                            Object result4 = e$iv2;
                            $i$a$1$consumeEach = 0;
                            obj2 = coroutine_suspended;
                            channelsKt__Channels_commonKt$map$12 = this;
                            channelsKt__Channels_commonKt$map$14 = channelsKt__Channels_commonKt$map$12;
                            $receiver$iv3 = $receiver$iv7;
                            result3 = result4;
                            obj3 = obj2;
                            $i$a$4$consume = $i$f$consume2;
                            $i$f$consume3 = $i$f$consume;
                            $i$f$consumeEach = $receiver$iv;
                            try {
                                channelsKt__Channels_commonKt$map$14.L$0 = producerScope3;
                                channelsKt__Channels_commonKt$map$14.L$1 = $receiver$iv2;
                                channelsKt__Channels_commonKt$map$14.L$2 = channelsKt__Channels_commonKt$map$13;
                                channelsKt__Channels_commonKt$map$14.L$3 = $receiver$iv$iv;
                                channelsKt__Channels_commonKt$map$14.L$4 = cause$iv$iv2;
                                channelsKt__Channels_commonKt$map$14.L$5 = $receiver$iv3;
                                channelsKt__Channels_commonKt$map$14.L$6 = channelIterator2;
                                channelsKt__Channels_commonKt$map$14.label = 1;
                                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$map$13);
                                if (hasNext == obj3) {
                                    return obj3;
                                }
                                obj2 = obj3;
                                $i$a$4$consume2 = $i$a$4$consume;
                                channelIterator3 = channelIterator2;
                                result2 = result3;
                                $i$f$consumeEach2 = $i$f$consumeEach;
                                try {
                                    if (!((Boolean) hasNext).booleanValue()) {
                                        channelsKt__Channels_commonKt$map$14.L$0 = producerScope3;
                                        channelsKt__Channels_commonKt$map$14.L$1 = $receiver$iv2;
                                        channelsKt__Channels_commonKt$map$14.L$2 = channelsKt__Channels_commonKt$map$13;
                                        channelsKt__Channels_commonKt$map$14.L$3 = $receiver$iv$iv;
                                        channelsKt__Channels_commonKt$map$14.L$4 = cause$iv$iv2;
                                        channelsKt__Channels_commonKt$map$14.L$5 = $receiver$iv3;
                                        channelsKt__Channels_commonKt$map$14.L$6 = channelIterator3;
                                        channelsKt__Channels_commonKt$map$14.label = 2;
                                        Object next = channelIterator3.next(channelsKt__Channels_commonKt$map$13);
                                        if (next == obj2) {
                                            return obj2;
                                        }
                                        try {
                                            channelsKt__Channels_commonKt$map$12 = channelsKt__Channels_commonKt$map$14;
                                            e$iv2 = next;
                                            $i$a$4$consume3 = $i$a$4$consume2;
                                            channelsKt__Channels_commonKt$map$1 = channelsKt__Channels_commonKt$map$13;
                                            cause$iv$iv = cause$iv$iv2;
                                            producerScope = producerScope3;
                                            $i$f$consumeEach3 = $i$f$consumeEach2;
                                            $i$f$consumeEach4 = $i$a$1$consumeEach;
                                            Function2 function22 = channelsKt__Channels_commonKt$map$12.$transform;
                                            channelsKt__Channels_commonKt$map$12.L$0 = producerScope;
                                            channelsKt__Channels_commonKt$map$12.L$1 = $receiver$iv2;
                                            channelsKt__Channels_commonKt$map$12.L$2 = channelsKt__Channels_commonKt$map$1;
                                            channelsKt__Channels_commonKt$map$12.L$3 = $receiver$iv$iv;
                                            channelsKt__Channels_commonKt$map$12.L$4 = cause$iv$iv;
                                            channelsKt__Channels_commonKt$map$12.L$5 = $receiver$iv3;
                                            channelsKt__Channels_commonKt$map$12.L$6 = channelIterator3;
                                            channelsKt__Channels_commonKt$map$12.L$7 = e$iv2;
                                            it = e$iv2;
                                            channelsKt__Channels_commonKt$map$12.L$8 = it;
                                            channelsKt__Channels_commonKt$map$12.L$9 = producerScope;
                                            channelsKt__Channels_commonKt$map$12.label = 3;
                                            obj = function22.invoke(it, channelsKt__Channels_commonKt$map$12);
                                            if (obj != obj2) {
                                                return obj2;
                                            }
                                            $i$f$consume = $i$f$consume3;
                                            $i$f$consume2 = $i$a$4$consume3;
                                            channelIterator = channelIterator3;
                                            producerScope2 = producerScope;
                                            $receiver$iv = $i$f$consumeEach3;
                                            e$iv = $receiver$iv3;
                                            try {
                                                channelsKt__Channels_commonKt$map$12.L$0 = producerScope2;
                                                channelsKt__Channels_commonKt$map$12.L$1 = $receiver$iv2;
                                                channelsKt__Channels_commonKt$map$12.L$2 = channelsKt__Channels_commonKt$map$1;
                                                channelsKt__Channels_commonKt$map$12.L$3 = $receiver$iv$iv;
                                                channelsKt__Channels_commonKt$map$12.L$4 = cause$iv$iv;
                                                channelsKt__Channels_commonKt$map$12.L$5 = e$iv;
                                                channelsKt__Channels_commonKt$map$12.L$6 = channelIterator;
                                                channelsKt__Channels_commonKt$map$12.L$7 = e$iv2;
                                                channelsKt__Channels_commonKt$map$12.L$8 = it;
                                                channelsKt__Channels_commonKt$map$12.label = 4;
                                                if (producerScope.send(obj, channelsKt__Channels_commonKt$map$12) != obj2) {
                                                    return obj2;
                                                }
                                                cause$iv$iv2 = cause$iv$iv;
                                                channelsKt__Channels_commonKt$map$13 = channelsKt__Channels_commonKt$map$1;
                                                $i$a$1$consumeEach = $i$f$consumeEach4;
                                                $receiver$iv7 = e$iv;
                                                producerScope3 = producerScope2;
                                                result4 = result2;
                                                channelIterator2 = channelIterator;
                                                channelsKt__Channels_commonKt$map$14 = channelsKt__Channels_commonKt$map$12;
                                                $receiver$iv3 = $receiver$iv7;
                                                result3 = result4;
                                                obj3 = obj2;
                                                $i$a$4$consume = $i$f$consume2;
                                                $i$f$consume3 = $i$f$consume;
                                                $i$f$consumeEach = $receiver$iv;
                                                channelsKt__Channels_commonKt$map$14.L$0 = producerScope3;
                                                channelsKt__Channels_commonKt$map$14.L$1 = $receiver$iv2;
                                                channelsKt__Channels_commonKt$map$14.L$2 = channelsKt__Channels_commonKt$map$13;
                                                channelsKt__Channels_commonKt$map$14.L$3 = $receiver$iv$iv;
                                                channelsKt__Channels_commonKt$map$14.L$4 = cause$iv$iv2;
                                                channelsKt__Channels_commonKt$map$14.L$5 = $receiver$iv3;
                                                channelsKt__Channels_commonKt$map$14.L$6 = channelIterator2;
                                                channelsKt__Channels_commonKt$map$14.label = 1;
                                                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$map$13);
                                                if (hasNext == obj3) {
                                                }
                                            } catch (Throwable th2) {
                                                e$iv$iv = th2;
                                            }
                                        } catch (Throwable th3) {
                                            e$iv$iv = th3;
                                        }
                                    } else {
                                        Unit unit = Unit.INSTANCE;
                                        $receiver$iv$iv.cancel(cause$iv$iv2);
                                        return Unit.INSTANCE;
                                    }
                                } catch (Throwable th4) {
                                    e$iv$iv = th4;
                                }
                            } catch (Throwable th5) {
                                e$iv$iv = th5;
                            }
                        } else {
                            throw ((Result.Failure) e$iv2).exception;
                        }
                    } catch (Throwable th6) {
                        e$iv$iv = th6;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            } catch (Throwable th7) {
                e$iv$iv = th7;
            }
        } else if (!(e$iv2 instanceof Result.Failure)) {
            ProducerScope producerScope4 = this.p$;
            $receiver$iv2 = this.$this_map;
            $receiver$iv$iv = $receiver$iv2;
            $i$f$consume3 = 0;
            cause$iv$iv2 = null;
            try {
                channelIterator2 = $receiver$iv$iv.iterator();
                $i$f$consumeEach = 0;
                $i$a$4$consume = 0;
                $i$a$1$consumeEach = 0;
                obj3 = coroutine_suspended;
                producerScope3 = producerScope4;
                result3 = e$iv2;
                $receiver$iv3 = $receiver$iv$iv;
                channelsKt__Channels_commonKt$map$14 = this;
                channelsKt__Channels_commonKt$map$13 = channelsKt__Channels_commonKt$map$14;
                channelsKt__Channels_commonKt$map$14.L$0 = producerScope3;
                channelsKt__Channels_commonKt$map$14.L$1 = $receiver$iv2;
                channelsKt__Channels_commonKt$map$14.L$2 = channelsKt__Channels_commonKt$map$13;
                channelsKt__Channels_commonKt$map$14.L$3 = $receiver$iv$iv;
                channelsKt__Channels_commonKt$map$14.L$4 = cause$iv$iv2;
                channelsKt__Channels_commonKt$map$14.L$5 = $receiver$iv3;
                channelsKt__Channels_commonKt$map$14.L$6 = channelIterator2;
                channelsKt__Channels_commonKt$map$14.label = 1;
                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$map$13);
                if (hasNext == obj3) {
                }
            } catch (Throwable th8) {
                e$iv$iv = th8;
            }
        } else {
            throw ((Result.Failure) e$iv2).exception;
        }
        try {
            throw e$iv$iv;
        } catch (Throwable e$iv$iv) {
            $receiver$iv$iv.cancel(e$iv$iv);
            throw e$iv$iv;
        }
    }
}
