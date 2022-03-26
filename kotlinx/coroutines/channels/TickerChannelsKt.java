package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.EventLoopKt;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.TimeSourceKt;
/* compiled from: TickerChannels.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a/\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a/\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a4\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, d2 = {"fixedDelayTicker", "", "delayMillis", "", "initialDelayMillis", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "(JJLkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fixedPeriodTicker", "ticker", "Lkotlinx/coroutines/channels/ReceiveChannel;", "context", "Lkotlin/coroutines/CoroutineContext;", "mode", "Lkotlinx/coroutines/channels/TickerMode;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class TickerChannelsKt {

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = new int[TickerMode.values().length];

        static {
            $EnumSwitchMapping$0[TickerMode.FIXED_PERIOD.ordinal()] = 1;
            $EnumSwitchMapping$0[TickerMode.FIXED_DELAY.ordinal()] = 2;
        }
    }

    public static /* synthetic */ ReceiveChannel ticker$default(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode, int i, Object obj) {
        if ((i & 2) != 0) {
            j2 = j;
        }
        if ((i & 4) != 0) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 8) != 0) {
            tickerMode = TickerMode.FIXED_PERIOD;
        }
        return ticker(j, j2, coroutineContext, tickerMode);
    }

    public static final ReceiveChannel<Unit> ticker(long delayMillis, long initialDelayMillis, CoroutineContext context, TickerMode mode) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(mode, "mode");
        boolean z = true;
        if (delayMillis >= 0) {
            if (initialDelayMillis < 0) {
                z = false;
            }
            if (z) {
                return ProduceKt.produce(GlobalScope.INSTANCE, Dispatchers.getUnconfined().plus(context), 0, new TickerChannelsKt$ticker$3(mode, delayMillis, initialDelayMillis, null));
            }
            throw new IllegalArgumentException(("Expected non-negative initial delay, but has " + initialDelayMillis + " ms").toString());
        }
        throw new IllegalArgumentException(("Expected non-negative delay, but has " + delayMillis + " ms").toString());
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x011a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01a2 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final /* synthetic */ Object fixedPeriodTicker(long delayMillis, long initialDelayMillis, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) {
        TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$1;
        Object coroutine_suspended;
        int i;
        long initialDelayMillis2;
        Object obj;
        long delayMillis2;
        long deadline;
        long delayNs;
        SendChannel channel;
        long deadline2;
        long delayNs2;
        long delayNs3;
        long deadline3;
        Object obj2;
        long nextDelay;
        SendChannel channel2;
        long initialDelayMillis3;
        SendChannel channel3;
        long now;
        long nextDelay2;
        long delayNanosToMillis;
        SendChannel channel4;
        Unit unit;
        SendChannel channel5;
        long initialDelayMillis4 = initialDelayMillis;
        if (continuation instanceof TickerChannelsKt$fixedPeriodTicker$1) {
            tickerChannelsKt$fixedPeriodTicker$1 = (TickerChannelsKt$fixedPeriodTicker$1) continuation;
            if ((tickerChannelsKt$fixedPeriodTicker$1.label & Integer.MIN_VALUE) != 0) {
                tickerChannelsKt$fixedPeriodTicker$1.label -= Integer.MIN_VALUE;
                Object obj3 = tickerChannelsKt$fixedPeriodTicker$1.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = tickerChannelsKt$fixedPeriodTicker$1.label;
                if (i == 0) {
                    if (i == 1) {
                        deadline2 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                        channel5 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                        initialDelayMillis4 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                        long delayMillis3 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                        if (!(obj3 instanceof Result.Failure)) {
                            delayMillis2 = delayMillis3;
                        } else {
                            throw ((Result.Failure) obj3).exception;
                        }
                    } else if (i == 2) {
                        delayNs = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                        deadline = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                        SendChannel channel6 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                        initialDelayMillis4 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                        delayMillis2 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                        if (!(obj3 instanceof Result.Failure)) {
                            channel3 = channel6;
                            now = TimeSourceKt.getTimeSource().nanoTime();
                            nextDelay2 = RangesKt.coerceAtLeast(deadline - now, 0L);
                            if (nextDelay2 == 0) {
                            }
                            obj = coroutine_suspended;
                            delayNanosToMillis = EventLoopKt.delayNanosToMillis(nextDelay2);
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel3;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs;
                            initialDelayMillis2 = initialDelayMillis4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$4 = now;
                            tickerChannelsKt$fixedPeriodTicker$1.J$5 = nextDelay2;
                            tickerChannelsKt$fixedPeriodTicker$1.label = 4;
                            channel = channel3;
                            if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$1) == obj) {
                            }
                            coroutine_suspended = obj;
                            initialDelayMillis4 = initialDelayMillis2;
                            deadline2 = deadline;
                            delayNs2 = delayNs;
                            channel4 = channel;
                            long deadline4 = deadline2 + delayNs2;
                            unit = Unit.INSTANCE;
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs2;
                            tickerChannelsKt$fixedPeriodTicker$1.label = 2;
                            if (channel4.send(unit, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
                            }
                        } else {
                            throw ((Result.Failure) obj3).exception;
                        }
                    } else if (i == 3) {
                        long j = tickerChannelsKt$fixedPeriodTicker$1.J$6;
                        long j2 = tickerChannelsKt$fixedPeriodTicker$1.J$5;
                        long j3 = tickerChannelsKt$fixedPeriodTicker$1.J$4;
                        long delayNs4 = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                        long deadline5 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                        SendChannel sendChannel2 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                        long initialDelayMillis5 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                        long delayMillis4 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                        if (!(obj3 instanceof Result.Failure)) {
                            initialDelayMillis3 = delayMillis4;
                            delayNs3 = delayNs4;
                            deadline3 = deadline5;
                            obj2 = coroutine_suspended;
                            nextDelay = initialDelayMillis5;
                            channel2 = sendChannel2;
                            delayMillis2 = initialDelayMillis3;
                            initialDelayMillis4 = nextDelay;
                            coroutine_suspended = obj2;
                            deadline2 = deadline3;
                            delayNs2 = delayNs3;
                            channel4 = channel2;
                            long deadline42 = deadline2 + delayNs2;
                            unit = Unit.INSTANCE;
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline42;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs2;
                            tickerChannelsKt$fixedPeriodTicker$1.label = 2;
                            if (channel4.send(unit, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
                            }
                        } else {
                            throw ((Result.Failure) obj3).exception;
                        }
                    } else if (i == 4) {
                        long nextDelay3 = tickerChannelsKt$fixedPeriodTicker$1.J$5;
                        long now2 = tickerChannelsKt$fixedPeriodTicker$1.J$4;
                        delayNs = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                        deadline = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                        SendChannel channel7 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                        long initialDelayMillis6 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                        delayMillis2 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                        if (!(obj3 instanceof Result.Failure)) {
                            initialDelayMillis2 = initialDelayMillis6;
                            obj = coroutine_suspended;
                            channel = channel7;
                            coroutine_suspended = obj;
                            initialDelayMillis4 = initialDelayMillis2;
                            deadline2 = deadline;
                            delayNs2 = delayNs;
                            channel4 = channel;
                            long deadline422 = deadline2 + delayNs2;
                            unit = Unit.INSTANCE;
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline422;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs2;
                            tickerChannelsKt$fixedPeriodTicker$1.label = 2;
                            if (channel4.send(unit, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            delayNs = delayNs2;
                            deadline = deadline422;
                            channel3 = channel4;
                            now = TimeSourceKt.getTimeSource().nanoTime();
                            nextDelay2 = RangesKt.coerceAtLeast(deadline - now, 0L);
                            if (nextDelay2 == 0 || delayNs == 0) {
                                obj = coroutine_suspended;
                                delayNanosToMillis = EventLoopKt.delayNanosToMillis(nextDelay2);
                                tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                                tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                                tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel3;
                                tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline;
                                tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs;
                                initialDelayMillis2 = initialDelayMillis4;
                                tickerChannelsKt$fixedPeriodTicker$1.J$4 = now;
                                tickerChannelsKt$fixedPeriodTicker$1.J$5 = nextDelay2;
                                tickerChannelsKt$fixedPeriodTicker$1.label = 4;
                                channel = channel3;
                                if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$1) == obj) {
                                    return obj;
                                }
                                coroutine_suspended = obj;
                                initialDelayMillis4 = initialDelayMillis2;
                                deadline2 = deadline;
                                delayNs2 = delayNs;
                                channel4 = channel;
                                long deadline4222 = deadline2 + delayNs2;
                                unit = Unit.INSTANCE;
                                tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                                tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                                tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel4;
                                tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline4222;
                                tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs2;
                                tickerChannelsKt$fixedPeriodTicker$1.label = 2;
                                if (channel4.send(unit, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
                                }
                            } else {
                                long adjustedDelay = delayNs - ((now - deadline) % delayNs);
                                long deadline6 = now + adjustedDelay;
                                long nextDelay4 = EventLoopKt.delayNanosToMillis(adjustedDelay);
                                tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                                tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                                tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel3;
                                tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline6;
                                tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs;
                                tickerChannelsKt$fixedPeriodTicker$1.J$4 = now;
                                tickerChannelsKt$fixedPeriodTicker$1.J$5 = nextDelay2;
                                tickerChannelsKt$fixedPeriodTicker$1.J$6 = adjustedDelay;
                                tickerChannelsKt$fixedPeriodTicker$1.label = 3;
                                Object delay = DelayKt.delay(nextDelay4, tickerChannelsKt$fixedPeriodTicker$1);
                                obj2 = coroutine_suspended;
                                if (delay == obj2) {
                                    return obj2;
                                }
                                delayNs3 = delayNs;
                                deadline3 = deadline6;
                                nextDelay = initialDelayMillis4;
                                initialDelayMillis3 = delayMillis2;
                                channel2 = channel3;
                                delayMillis2 = initialDelayMillis3;
                                initialDelayMillis4 = nextDelay;
                                coroutine_suspended = obj2;
                                deadline2 = deadline3;
                                delayNs2 = delayNs3;
                                channel4 = channel2;
                                long deadline42222 = deadline2 + delayNs2;
                                unit = Unit.INSTANCE;
                                tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                                tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                                tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel4;
                                tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline42222;
                                tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs2;
                                tickerChannelsKt$fixedPeriodTicker$1.label = 2;
                                if (channel4.send(unit, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
                                }
                            }
                        } else {
                            throw ((Result.Failure) obj3).exception;
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (!(obj3 instanceof Result.Failure)) {
                    channel5 = sendChannel;
                    deadline2 = TimeSourceKt.getTimeSource().nanoTime() + EventLoopKt.delayToNanos(initialDelayMillis);
                    delayMillis2 = delayMillis;
                    tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                    tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                    tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel5;
                    tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline2;
                    tickerChannelsKt$fixedPeriodTicker$1.label = 1;
                    if (DelayKt.delay(initialDelayMillis4, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    throw ((Result.Failure) obj3).exception;
                }
                delayNs2 = EventLoopKt.delayToNanos(delayMillis2);
                channel4 = channel5;
                long deadline422222 = deadline2 + delayNs2;
                unit = Unit.INSTANCE;
                tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
                tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
                tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel4;
                tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline422222;
                tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs2;
                tickerChannelsKt$fixedPeriodTicker$1.label = 2;
                if (channel4.send(unit, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
                }
            }
        }
        tickerChannelsKt$fixedPeriodTicker$1 = new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.channels.TickerChannelsKt$fixedPeriodTicker$1
            long J$0;
            long J$1;
            long J$2;
            long J$3;
            long J$4;
            long J$5;
            long J$6;
            Object L$0;
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj4) {
                this.result = obj4;
                this.label |= Integer.MIN_VALUE;
                return TickerChannelsKt.fixedPeriodTicker(0, 0, null, this);
            }
        };
        Object obj32 = tickerChannelsKt$fixedPeriodTicker$1.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = tickerChannelsKt$fixedPeriodTicker$1.label;
        if (i == 0) {
        }
        delayNs2 = EventLoopKt.delayToNanos(delayMillis2);
        channel4 = channel5;
        long deadline4222222 = deadline2 + delayNs2;
        unit = Unit.INSTANCE;
        tickerChannelsKt$fixedPeriodTicker$1.J$0 = delayMillis2;
        tickerChannelsKt$fixedPeriodTicker$1.J$1 = initialDelayMillis4;
        tickerChannelsKt$fixedPeriodTicker$1.L$0 = channel4;
        tickerChannelsKt$fixedPeriodTicker$1.J$2 = deadline4222222;
        tickerChannelsKt$fixedPeriodTicker$1.J$3 = delayNs2;
        tickerChannelsKt$fixedPeriodTicker$1.label = 2;
        if (channel4.send(unit, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
        }
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:56)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:30)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:18)
        */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0092 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a1 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    static final /* synthetic */ java.lang.Object fixedDelayTicker(long r6, long r8, kotlinx.coroutines.channels.SendChannel<? super kotlin.Unit> r10, kotlin.coroutines.Continuation<? super kotlin.Unit> r11) {
        /*
            boolean r0 = r11 instanceof kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1
            if (r0 == 0) goto L_0x0014
            r0 = r11
            kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1 r0 = (kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0014
            int r11 = r0.label
            int r11 = r11 - r2
            r0.label = r11
            goto L_0x0019
        L_0x0014:
            kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1 r0 = new kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1
            r0.<init>(r11)
        L_0x0019:
            r11 = r0
            java.lang.Object r0 = r11.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r11.label
            r3 = 3
            r4 = 2
            r5 = 1
            if (r2 == 0) goto L_0x006e
            if (r2 == r5) goto L_0x005b
            if (r2 == r4) goto L_0x0048
            if (r2 != r3) goto L_0x0040
            java.lang.Object r2 = r11.L$0
            r10 = r2
            kotlinx.coroutines.channels.SendChannel r10 = (kotlinx.coroutines.channels.SendChannel) r10
            long r8 = r11.J$1
            long r6 = r11.J$0
            boolean r2 = r0 instanceof kotlin.Result.Failure
            if (r2 != 0) goto L_0x003b
            goto L_0x0081
        L_0x003b:
            kotlin.Result$Failure r0 = (kotlin.Result.Failure) r0
            java.lang.Throwable r0 = r0.exception
            throw r0
        L_0x0040:
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.String r11 = "call to 'resume' before 'invoke' with coroutine"
            r10.<init>(r11)
            throw r10
        L_0x0048:
            java.lang.Object r2 = r11.L$0
            r10 = r2
            kotlinx.coroutines.channels.SendChannel r10 = (kotlinx.coroutines.channels.SendChannel) r10
            long r8 = r11.J$1
            long r6 = r11.J$0
            boolean r2 = r0 instanceof kotlin.Result.Failure
            if (r2 != 0) goto L_0x0056
            goto L_0x0093
        L_0x0056:
            kotlin.Result$Failure r0 = (kotlin.Result.Failure) r0
            java.lang.Throwable r0 = r0.exception
            throw r0
        L_0x005b:
            java.lang.Object r2 = r11.L$0
            r10 = r2
            kotlinx.coroutines.channels.SendChannel r10 = (kotlinx.coroutines.channels.SendChannel) r10
            long r8 = r11.J$1
            long r6 = r11.J$0
            boolean r2 = r0 instanceof kotlin.Result.Failure
            if (r2 != 0) goto L_0x0069
            goto L_0x0081
        L_0x0069:
            kotlin.Result$Failure r0 = (kotlin.Result.Failure) r0
            java.lang.Throwable r0 = r0.exception
            throw r0
        L_0x006e:
            boolean r2 = r0 instanceof kotlin.Result.Failure
            if (r2 != 0) goto L_0x00a2
            r11.J$0 = r6
            r11.J$1 = r8
            r11.L$0 = r10
            r11.label = r5
            java.lang.Object r0 = kotlinx.coroutines.DelayKt.delay(r8, r11)
            if (r0 != r1) goto L_0x0081
            return r1
        L_0x0081:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            r11.J$0 = r6
            r11.J$1 = r8
            r11.L$0 = r10
            r11.label = r4
            java.lang.Object r0 = r10.send(r0, r11)
            if (r0 != r1) goto L_0x0093
            return r1
        L_0x0093:
            r11.J$0 = r6
            r11.J$1 = r8
            r11.L$0 = r10
            r11.label = r3
            java.lang.Object r0 = kotlinx.coroutines.DelayKt.delay(r6, r11)
            if (r0 != r1) goto L_0x0081
            return r1
        L_0x00a2:
            kotlin.Result$Failure r0 = (kotlin.Result.Failure) r0
            java.lang.Throwable r10 = r0.exception
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.TickerChannelsKt.fixedDelayTicker(long, long, kotlinx.coroutines.channels.SendChannel, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
