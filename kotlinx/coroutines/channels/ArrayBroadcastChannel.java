package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.internal.ConcurrentKt;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;
/* compiled from: ArrayBroadcastChannel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u00016B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0012\u0010 \u001a\u00020\u00172\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010#\u001a\u00020$H\u0002J\u0012\u0010%\u001a\u00020\u00172\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010&\u001a\u00020\u0015H\u0002J\u0015\u0010'\u001a\u00028\u00002\u0006\u0010(\u001a\u00020\u0015H\u0002¢\u0006\u0002\u0010)J\u0015\u0010*\u001a\u00020\t2\u0006\u0010+\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010,J!\u0010-\u001a\u00020\t2\u0006\u0010+\u001a\u00028\u00002\n\u0010.\u001a\u0006\u0012\u0002\b\u00030/H\u0014¢\u0006\u0002\u00100J\u000e\u00101\u001a\b\u0012\u0004\u0012\u00028\u000002H\u0016J-\u00103\u001a\u00020$2\u0010\b\u0002\u00104\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u001d2\u0010\b\u0002\u00105\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u001dH\u0082\u0010R\u0018\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0012\u0010\u000f\u001a\u00060\u0010j\u0002`\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u00178TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\u00178TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0018R\u000e\u0010\u001a\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R*\u0010\u001b\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u001d0\u001cj\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u001d`\u001eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000¨\u00067"}, d2 = {"Lkotlinx/coroutines/channels/ArrayBroadcastChannel;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/AbstractSendChannel;", "Lkotlinx/coroutines/channels/BroadcastChannel;", "capacity", "", "(I)V", "buffer", "", "", "[Ljava/lang/Object;", "bufferDebugString", "", "getBufferDebugString", "()Ljava/lang/String;", "bufferLock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "getCapacity", "()I", "head", "", "isBufferAlwaysFull", "", "()Z", "isBufferFull", "size", "subscribers", "", "Lkotlinx/coroutines/channels/ArrayBroadcastChannel$Subscriber;", "Lkotlinx/coroutines/internal/SubscribersList;", "tail", "cancel", "cause", "", "checkSubOffers", "", "close", "computeMinHead", "elementAt", FirebaseAnalytics.Param.INDEX, "(J)Ljava/lang/Object;", "offerInternal", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "openSubscription", "Lkotlinx/coroutines/channels/ReceiveChannel;", "updateHead", "addSub", "removeSub", "Subscriber", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class ArrayBroadcastChannel<E> extends AbstractSendChannel<E> implements BroadcastChannel<E> {
    private final Object[] buffer;
    private final ReentrantLock bufferLock;
    private final int capacity;
    private volatile long head;
    private volatile int size;
    private final List<Subscriber<E>> subscribers;
    private volatile long tail;

    public ArrayBroadcastChannel(int capacity) {
        this.capacity = capacity;
        boolean z = true;
        z = false;
        if (this.capacity < 1) {
        }
        if (z) {
            this.bufferLock = new ReentrantLock();
            this.buffer = new Object[this.capacity];
            this.subscribers = ConcurrentKt.subscriberList();
            return;
        }
        throw new IllegalArgumentException(("ArrayBroadcastChannel capacity must be at least 1, but " + this.capacity + " was specified").toString());
    }

    public final int getCapacity() {
        return this.capacity;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected boolean isBufferAlwaysFull() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public boolean isBufferFull() {
        return this.size >= this.capacity;
    }

    @Override // kotlinx.coroutines.channels.BroadcastChannel
    public ReceiveChannel<E> openSubscription() {
        Subscriber it = new Subscriber(this);
        updateHead$default(this, it, null, 2, null);
        return it;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel, kotlinx.coroutines.channels.SendChannel
    public boolean close(Throwable cause) {
        if (!super.close(cause)) {
            return false;
        }
        checkSubOffers();
        return true;
    }

    @Override // kotlinx.coroutines.channels.BroadcastChannel
    public boolean cancel(Throwable cause) {
        boolean close = close(cause);
        Iterator<Subscriber<E>> it = this.subscribers.iterator();
        while (it.hasNext()) {
            it.next().cancel(cause);
        }
        return close;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected Object offerInternal(E e) {
        ReentrantLock reentrantLock = this.bufferLock;
        reentrantLock.lock();
        try {
            Closed it = getClosedForSend();
            if (it != null) {
                return it;
            }
            int size = this.size;
            if (size >= this.capacity) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            long tail = this.tail;
            this.buffer[(int) (tail % ((long) this.capacity))] = e;
            this.size = size + 1;
            this.tail = 1 + tail;
            Unit unit = Unit.INSTANCE;
            reentrantLock.unlock();
            checkSubOffers();
            return AbstractChannelKt.OFFER_SUCCESS;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected Object offerSelectInternal(E e, SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        ReentrantLock reentrantLock = this.bufferLock;
        reentrantLock.lock();
        try {
            Closed it = getClosedForSend();
            if (it != null) {
                return it;
            }
            int size = this.size;
            if (size >= this.capacity) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            if (!selectInstance.trySelect(null)) {
                return SelectKt.getALREADY_SELECTED();
            }
            long tail = this.tail;
            this.buffer[(int) (tail % ((long) this.capacity))] = e;
            this.size = size + 1;
            this.tail = 1 + tail;
            Unit unit = Unit.INSTANCE;
            reentrantLock.unlock();
            checkSubOffers();
            return AbstractChannelKt.OFFER_SUCCESS;
        } finally {
            reentrantLock.unlock();
        }
    }

    private final void checkSubOffers() {
        boolean updated = false;
        boolean hasSubs = false;
        Iterator<Subscriber<E>> it = this.subscribers.iterator();
        while (it.hasNext()) {
            hasSubs = true;
            if (it.next().checkOffer()) {
                updated = true;
            }
        }
        if (updated || !hasSubs) {
            updateHead$default(this, null, null, 3, null);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void updateHead$default(ArrayBroadcastChannel arrayBroadcastChannel, Subscriber subscriber, Subscriber subscriber2, int i, Object obj) {
        if ((i & 1) != 0) {
            subscriber = null;
        }
        if ((i & 2) != 0) {
            subscriber2 = null;
        }
        arrayBroadcastChannel.updateHead(subscriber, subscriber2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x00b5, code lost:
        r3 = r26.buffer;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00c2, code lost:
        r2 = (int) (r13 % ((long) r26.capacity));
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00c3, code lost:
        if (r22 == null) goto L_0x00f4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00c5, code lost:
        r3[r2] = r22.getPollResult();
        r26.size = r0 + 1;
        r26.tail = r13 + 1;
        r0 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00d8, code lost:
        r10.unlock();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00dc, code lost:
        if (r22 != null) goto L_0x00e1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00de, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00f1, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00fb, code lost:
        throw new kotlin.TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.Send");
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00fc, code lost:
        r0 = th;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final void updateHead(Subscriber<E> subscriber, Subscriber<E> subscriber2) {
        Send takeFirstSendOrPeekClosed;
        Object token;
        Subscriber addSub = subscriber;
        Subscriber removeSub = subscriber2;
        boolean wasFull = false;
        int $i$a$1$withLock = 0;
        while (true) {
            ReentrantLock reentrantLock = this.bufferLock;
            reentrantLock.lock();
            if (addSub != null) {
                try {
                    addSub.subHead = this.tail;
                    boolean wasEmpty = this.subscribers.isEmpty();
                    this.subscribers.add(addSub);
                    if (!wasEmpty) {
                        reentrantLock.unlock();
                        return;
                    }
                } catch (Throwable th) {
                    th = th;
                    reentrantLock.unlock();
                    throw th;
                }
            }
            if (removeSub != null) {
                this.subscribers.remove(removeSub);
                if (this.head != removeSub.subHead) {
                    reentrantLock.unlock();
                    return;
                }
            }
            try {
                long minHead = computeMinHead();
                long tail = this.tail;
                try {
                    long head = this.head;
                    long targetHead = RangesKt.coerceAtMost(minHead, tail);
                    if (targetHead <= head) {
                        reentrantLock.unlock();
                        return;
                    }
                    int size = this.size;
                    Object token2 = null;
                    while (head < targetHead) {
                        try {
                            try {
                                this.buffer[(int) (head % ((long) this.capacity))] = null;
                                boolean wasFull2 = size >= this.capacity;
                                long head2 = head + 1;
                                this.head = head2;
                                size--;
                                this.size = size;
                                if (wasFull2) {
                                    while (true) {
                                        takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
                                        if (takeFirstSendOrPeekClosed != null && !(takeFirstSendOrPeekClosed instanceof Closed)) {
                                            if (takeFirstSendOrPeekClosed == null) {
                                                try {
                                                    Intrinsics.throwNpe();
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    reentrantLock.unlock();
                                                    throw th;
                                                }
                                            }
                                            token = takeFirstSendOrPeekClosed.tryResumeSend(null);
                                            if (token != null) {
                                                try {
                                                    break;
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                }
                                            } else {
                                                token2 = token;
                                                removeSub = removeSub;
                                                head2 = head2;
                                            }
                                        }
                                    }
                                }
                                wasFull = wasFull;
                                $i$a$1$withLock = $i$a$1$withLock;
                                addSub = addSub;
                                removeSub = removeSub;
                                head = head2;
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                        }
                    }
                    reentrantLock.unlock();
                    return;
                    takeFirstSendOrPeekClosed.completeResumeSend(token);
                    checkSubOffers();
                    removeSub = null;
                    addSub = null;
                    wasFull = wasFull;
                    $i$a$1$withLock = $i$a$1$withLock;
                } catch (Throwable th6) {
                    th = th6;
                }
            } catch (Throwable th7) {
                th = th7;
            }
        }
    }

    private final long computeMinHead() {
        long minHead = Long.MAX_VALUE;
        Iterator<Subscriber<E>> it = this.subscribers.iterator();
        while (it.hasNext()) {
            minHead = RangesKt.coerceAtMost(minHead, it.next().subHead);
        }
        return minHead;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final E elementAt(long index) {
        return (E) this.buffer[(int) (index % ((long) this.capacity))];
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ArrayBroadcastChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0012\u0010\u0012\u001a\u00020\b2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u0006\u0010\u0015\u001a\u00020\bJ\b\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\bH\u0002J\n\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0002J\n\u0010\u001b\u001a\u0004\u0018\u00010\u001aH\u0014J\u0016\u0010\u001c\u001a\u0004\u0018\u00010\u001a2\n\u0010\u001d\u001a\u0006\u0012\u0002\b\u00030\u001eH\u0014R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\tR\u0014\u0010\n\u001a\u00020\b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\tR\u0014\u0010\u000b\u001a\u00020\b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\tR\u0014\u0010\f\u001a\u00020\b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\tR\u0012\u0010\r\u001a\u00020\u000e8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u00060\u0010j\u0002`\u0011X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/channels/ArrayBroadcastChannel$Subscriber;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/AbstractChannel;", "Lkotlinx/coroutines/channels/ReceiveChannel;", "broadcastChannel", "Lkotlinx/coroutines/channels/ArrayBroadcastChannel;", "(Lkotlinx/coroutines/channels/ArrayBroadcastChannel;)V", "isBufferAlwaysEmpty", "", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "subHead", "", "subLock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "cancel", "cause", "", "checkOffer", "clearBuffer", "", "needsToCheckOfferWithoutLock", "peekUnderLock", "", "pollInternal", "pollSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class Subscriber<E> extends AbstractChannel<E> implements ReceiveChannel<E> {
        private final ArrayBroadcastChannel<E> broadcastChannel;
        public volatile long subHead;
        private final ReentrantLock subLock = new ReentrantLock();

        public Subscriber(ArrayBroadcastChannel<E> arrayBroadcastChannel) {
            Intrinsics.checkParameterIsNotNull(arrayBroadcastChannel, "broadcastChannel");
            this.broadcastChannel = arrayBroadcastChannel;
        }

        @Override // kotlinx.coroutines.channels.AbstractChannel
        protected boolean isBufferAlwaysEmpty() {
            return false;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // kotlinx.coroutines.channels.AbstractChannel
        public boolean isBufferEmpty() {
            return this.subHead >= ((ArrayBroadcastChannel) this.broadcastChannel).tail;
        }

        @Override // kotlinx.coroutines.channels.AbstractSendChannel
        protected boolean isBufferAlwaysFull() {
            throw new IllegalStateException("Should not be used".toString());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // kotlinx.coroutines.channels.AbstractSendChannel
        public boolean isBufferFull() {
            throw new IllegalStateException("Should not be used".toString());
        }

        @Override // kotlinx.coroutines.channels.AbstractChannel, kotlinx.coroutines.channels.ReceiveChannel
        public boolean cancel(Throwable cause) {
            boolean closed = close(cause);
            if (closed) {
                ArrayBroadcastChannel.updateHead$default(this.broadcastChannel, null, this, 1, null);
            }
            clearBuffer();
            return closed;
        }

        private final void clearBuffer() {
            ReentrantLock reentrantLock = this.subLock;
            reentrantLock.lock();
            try {
                this.subHead = ((ArrayBroadcastChannel) this.broadcastChannel).tail;
                Unit unit = Unit.INSTANCE;
            } finally {
                reentrantLock.unlock();
            }
        }

        public final boolean checkOffer() {
            Closed closed = null;
            boolean updated = false;
            while (needsToCheckOfferWithoutLock() && this.subLock.tryLock()) {
                try {
                    E e = (E) peekUnderLock();
                    if (e != AbstractChannelKt.POLL_FAILED) {
                        if (e instanceof Closed) {
                            closed = (Closed) e;
                        } else {
                            ReceiveOrClosed receive = takeFirstReceiveOrPeekClosed();
                            if (receive != null && !(receive instanceof Closed)) {
                                Object token = receive.tryResumeReceive(e, null);
                                if (token != null) {
                                    this.subHead = 1 + this.subHead;
                                    updated = true;
                                    this.subLock.unlock();
                                    if (receive == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    receive.completeResumeReceive(token);
                                }
                            }
                        }
                        break;
                    }
                } finally {
                    this.subLock.unlock();
                }
            }
            if (closed != null) {
                close(closed.closeCause);
            }
            return updated;
        }

        /* JADX WARN: Finally extract failed */
        @Override // kotlinx.coroutines.channels.AbstractChannel
        protected Object pollInternal() {
            boolean updated = false;
            ReentrantLock reentrantLock = this.subLock;
            reentrantLock.lock();
            try {
                Object result = peekUnderLock();
                if (!(result instanceof Closed) && result != AbstractChannelKt.POLL_FAILED) {
                    this.subHead = 1 + this.subHead;
                    updated = true;
                }
                reentrantLock.unlock();
                Closed it = (Closed) (!(result instanceof Closed) ? null : result);
                if (it != null) {
                    close(it.closeCause);
                }
                if (checkOffer()) {
                    updated = true;
                }
                if (updated) {
                    ArrayBroadcastChannel.updateHead$default(this.broadcastChannel, null, null, 3, null);
                }
                return result;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        /* JADX WARN: Finally extract failed */
        @Override // kotlinx.coroutines.channels.AbstractChannel
        protected Object pollSelectInternal(SelectInstance<?> selectInstance) {
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            boolean updated = false;
            ReentrantLock reentrantLock = this.subLock;
            reentrantLock.lock();
            try {
                Object result = peekUnderLock();
                if (!(result instanceof Closed) && result != AbstractChannelKt.POLL_FAILED) {
                    if (!selectInstance.trySelect(null)) {
                        result = SelectKt.getALREADY_SELECTED();
                    } else {
                        this.subHead = 1 + this.subHead;
                        updated = true;
                    }
                }
                reentrantLock.unlock();
                Closed it = (Closed) (!(result instanceof Closed) ? null : result);
                if (it != null) {
                    close(it.closeCause);
                }
                if (checkOffer()) {
                    updated = true;
                }
                if (updated) {
                    ArrayBroadcastChannel.updateHead$default(this.broadcastChannel, null, null, 3, null);
                }
                return result;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        private final boolean needsToCheckOfferWithoutLock() {
            if (getClosedForReceive() != null) {
                return false;
            }
            if (!isBufferEmpty() || this.broadcastChannel.getClosedForReceive() != null) {
                return true;
            }
            return false;
        }

        private final Object peekUnderLock() {
            long subHead = this.subHead;
            Closed closedBroadcast = this.broadcastChannel.getClosedForReceive();
            if (subHead >= ((ArrayBroadcastChannel) this.broadcastChannel).tail) {
                Closed closedForReceive = closedBroadcast != null ? closedBroadcast : getClosedForReceive();
                return closedForReceive != null ? closedForReceive : AbstractChannelKt.POLL_FAILED;
            }
            Object result = this.broadcastChannel.elementAt(subHead);
            Closed closedSub = getClosedForReceive();
            if (closedSub != null) {
                return closedSub;
            }
            return result;
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected String getBufferDebugString() {
        return "(buffer:capacity=" + this.buffer.length + ",size=" + this.size + ')';
    }
}
