package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.AbstractChannel;
import kotlinx.coroutines.channels.AbstractSendChannel;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;
/* compiled from: ArrayChannel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u001b\u001a\u00020\u001cH\u0014J\u0015\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u001fJ!\u0010 \u001a\u00020\b2\u0006\u0010\u001e\u001a\u00028\u00002\n\u0010!\u001a\u0006\u0012\u0002\b\u00030\"H\u0014¢\u0006\u0002\u0010#J\n\u0010$\u001a\u0004\u0018\u00010\bH\u0014J\u0016\u0010%\u001a\u0004\u0018\u00010\b2\n\u0010!\u001a\u0006\u0012\u0002\b\u00030\"H\u0014R\u0018\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0013R\u0014\u0010\u0015\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0013R\u0014\u0010\u0016\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0013R\u0012\u0010\u0017\u001a\u00060\u0018j\u0002`\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lkotlinx/coroutines/channels/ArrayChannel;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/AbstractChannel;", "capacity", "", "(I)V", "buffer", "", "", "[Ljava/lang/Object;", "bufferDebugString", "", "getBufferDebugString", "()Ljava/lang/String;", "getCapacity", "()I", "head", "isBufferAlwaysEmpty", "", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "size", "cleanupSendQueueOnCancel", "", "offerInternal", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "pollInternal", "pollSelectInternal", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public class ArrayChannel<E> extends AbstractChannel<E> {
    private final Object[] buffer;
    private final int capacity;
    private int head;
    private final ReentrantLock lock;
    private volatile int size;

    public ArrayChannel(int capacity) {
        this.capacity = capacity;
        boolean z = true;
        z = false;
        if (this.capacity < 1) {
        }
        if (z) {
            this.lock = new ReentrantLock();
            this.buffer = new Object[this.capacity];
            return;
        }
        throw new IllegalArgumentException(("ArrayChannel capacity must be at least 1, but " + this.capacity + " was specified").toString());
    }

    public final int getCapacity() {
        return this.capacity;
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected final boolean isBufferAlwaysEmpty() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferEmpty() {
        return this.size == 0;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected final boolean isBufferAlwaysFull() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferFull() {
        return this.size == this.capacity;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected Object offerInternal(E e) {
        ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed;
        Object token;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int size = this.size;
            Closed it = getClosedForSend();
            if (it != null) {
                return it;
            }
            if (size >= this.capacity) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            this.size = size + 1;
            if (size == 0) {
                do {
                    takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
                    if (takeFirstReceiveOrPeekClosed != null) {
                        if (takeFirstReceiveOrPeekClosed instanceof Closed) {
                            this.size = size;
                            if (takeFirstReceiveOrPeekClosed == null) {
                                Intrinsics.throwNpe();
                            }
                            return takeFirstReceiveOrPeekClosed;
                        }
                        if (takeFirstReceiveOrPeekClosed == null) {
                            Intrinsics.throwNpe();
                        }
                        token = takeFirstReceiveOrPeekClosed.tryResumeReceive(e, null);
                    }
                } while (token == null);
                this.size = size;
                Unit unit = Unit.INSTANCE;
                if (takeFirstReceiveOrPeekClosed == null) {
                    Intrinsics.throwNpe();
                }
                takeFirstReceiveOrPeekClosed.completeResumeReceive(token);
                if (takeFirstReceiveOrPeekClosed == null) {
                    Intrinsics.throwNpe();
                }
                return takeFirstReceiveOrPeekClosed.getOfferResult();
            }
            this.buffer[(this.head + size) % this.capacity] = e;
            return AbstractChannelKt.OFFER_SUCCESS;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected Object offerSelectInternal(E e, SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        boolean z = false;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int size = this.size;
            Closed it = getClosedForSend();
            if (it != null) {
                return it;
            }
            if (size >= this.capacity) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            this.size = size + 1;
            if (size == 0) {
                AbstractSendChannel.TryOfferDesc offerOp = describeTryOffer(e);
                Object failure = selectInstance.performAtomicTrySelect(offerOp);
                if (failure == null) {
                    this.size = size;
                    ReceiveOrClosed<? super E> result = offerOp.getResult();
                    Object token = offerOp.resumeToken;
                    if (token != null) {
                        z = true;
                    }
                    if (z) {
                        Unit unit = Unit.INSTANCE;
                        if (result == null) {
                            Intrinsics.throwNpe();
                        }
                        if (token == null) {
                            Intrinsics.throwNpe();
                        }
                        result.completeResumeReceive(token);
                        if (result == null) {
                            Intrinsics.throwNpe();
                        }
                        return result.getOfferResult();
                    }
                    throw new IllegalStateException("Check failed.".toString());
                } else if (failure != AbstractChannelKt.OFFER_FAILED) {
                    if (failure != SelectKt.getALREADY_SELECTED() && !(failure instanceof Closed)) {
                        throw new IllegalStateException(("performAtomicTrySelect(describeTryOffer) returned " + failure).toString());
                    }
                    this.size = size;
                    return failure;
                }
            }
            if (!selectInstance.trySelect(null)) {
                this.size = size;
                return SelectKt.getALREADY_SELECTED();
            }
            this.buffer[(this.head + size) % this.capacity] = e;
            return AbstractChannelKt.OFFER_SUCCESS;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected Object pollInternal() {
        Send send = null;
        Object token = null;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int size = this.size;
            if (size == 0) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            Object result = this.buffer[this.head];
            this.buffer[this.head] = null;
            this.size = size - 1;
            Object replacement = AbstractChannelKt.POLL_FAILED;
            if (size == this.capacity) {
                while (true) {
                    Send takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
                    if (takeFirstSendOrPeekClosed == null) {
                        break;
                    }
                    send = takeFirstSendOrPeekClosed;
                    if (send == null) {
                        Intrinsics.throwNpe();
                    }
                    token = send.tryResumeSend(null);
                    if (token != null) {
                        if (send == null) {
                            Intrinsics.throwNpe();
                        }
                        replacement = send.getPollResult();
                    }
                }
            }
            if (replacement != AbstractChannelKt.POLL_FAILED && !(replacement instanceof Closed)) {
                this.size = size;
                this.buffer[(this.head + size) % this.capacity] = replacement;
            }
            this.head = (this.head + 1) % this.capacity;
            Unit unit = Unit.INSTANCE;
            if (token != null) {
                if (send == null) {
                    Intrinsics.throwNpe();
                }
                send.completeResumeSend(token);
            }
            return result;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected Object pollSelectInternal(SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Send send = null;
        Object token = null;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int size = this.size;
            if (size == 0) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            Object result = this.buffer[this.head];
            this.buffer[this.head] = null;
            this.size = size - 1;
            Object replacement = AbstractChannelKt.POLL_FAILED;
            if (size == this.capacity) {
                AbstractChannel.TryPollDesc pollOp = describeTryPoll();
                Object failure = selectInstance.performAtomicTrySelect(pollOp);
                if (failure == null) {
                    send = pollOp.getResult();
                    token = pollOp.resumeToken;
                    if (token != null) {
                        if (send == null) {
                            Intrinsics.throwNpe();
                        }
                        replacement = send.getPollResult();
                    } else {
                        throw new IllegalStateException("Check failed.".toString());
                    }
                } else if (failure != AbstractChannelKt.POLL_FAILED) {
                    if (failure == SelectKt.getALREADY_SELECTED()) {
                        this.size = size;
                        this.buffer[this.head] = result;
                        return failure;
                    } else if (failure instanceof Closed) {
                        send = (Send) failure;
                        token = ((Closed) failure).tryResumeSend(null);
                        replacement = failure;
                    } else {
                        throw new IllegalStateException(("performAtomicTrySelect(describeTryOffer) returned " + failure).toString());
                    }
                }
            }
            if (replacement != AbstractChannelKt.POLL_FAILED && !(replacement instanceof Closed)) {
                this.size = size;
                this.buffer[(this.head + size) % this.capacity] = replacement;
            } else if (!selectInstance.trySelect(null)) {
                this.size = size;
                this.buffer[this.head] = result;
                return SelectKt.getALREADY_SELECTED();
            }
            this.head = (this.head + 1) % this.capacity;
            Unit unit = Unit.INSTANCE;
            if (token != null) {
                if (send == null) {
                    Intrinsics.throwNpe();
                }
                send.completeResumeSend(token);
            }
            return result;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Finally extract failed */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public void cleanupSendQueueOnCancel() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i = this.size;
            for (int i2 = 0; i2 < i; i2++) {
                this.buffer[this.head] = 0;
                this.head = (this.head + 1) % this.capacity;
            }
            this.size = 0;
            Unit unit = Unit.INSTANCE;
            reentrantLock.unlock();
            super.cleanupSendQueueOnCancel();
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected String getBufferDebugString() {
        return "(buffer:capacity=" + this.buffer.length + ",size=" + this.size + ')';
    }
}
