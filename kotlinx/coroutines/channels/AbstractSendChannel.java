package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CancellableContinuationKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause2;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;
/* compiled from: AbstractChannel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000¦\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b \u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0006WXYZ[\\B\u0005¢\u0006\u0002\u0010\u0003J\u0012\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$H\u0014J\u0012\u0010%\u001a\u00020\u000f2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\u0010\u0010&\u001a\u00020\"2\u0006\u0010'\u001a\u00020(H\u0004J\b\u0010)\u001a\u00020*H\u0002J!\u0010+\u001a\u000e\u0012\u0002\b\u00030,j\u0006\u0012\u0002\b\u0003`-2\u0006\u0010.\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010/J!\u00100\u001a\u000e\u0012\u0002\b\u00030,j\u0006\u0012\u0002\b\u0003`-2\u0006\u0010.\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010/J\u001b\u00101\u001a\b\u0012\u0004\u0012\u00028\u0000022\u0006\u0010.\u001a\u00028\u0000H\u0004¢\u0006\u0002\u00103J\u0012\u00104\u001a\u0004\u0018\u00010\u00162\u0006\u00105\u001a\u000206H\u0002J\u0014\u00107\u001a\u00020\"2\n\u00108\u001a\u0006\u0012\u0002\b\u00030\tH\u0002J\"\u00109\u001a\u00020\"2\u0018\u0010:\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010$\u0012\u0004\u0012\u00020\"0;j\u0002`<H\u0016J\u0012\u0010=\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$H\u0002J\u0013\u0010>\u001a\u00020\u000f2\u0006\u0010.\u001a\u00028\u0000¢\u0006\u0002\u0010?J\u0015\u0010@\u001a\u00020\u00162\u0006\u0010.\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010AJ!\u0010B\u001a\u00020\u00162\u0006\u0010.\u001a\u00028\u00002\n\u0010C\u001a\u0006\u0012\u0002\b\u00030DH\u0014¢\u0006\u0002\u0010EJ\u0016\u0010F\u001a\u00020\"2\f\u00108\u001a\b\u0012\u0004\u0012\u00028\u00000\tH\u0014JV\u0010G\u001a\u00020\"\"\u0004\b\u0001\u0010H2\f\u0010C\u001a\b\u0012\u0004\u0012\u0002HH0D2\u0006\u0010.\u001a\u00028\u00002(\u0010I\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002HH0K\u0012\u0006\u0012\u0004\u0018\u00010\u00160JH\u0002ø\u0001\u0000¢\u0006\u0002\u0010LJ\u0019\u00105\u001a\u00020\"2\u0006\u0010.\u001a\u00028\u0000H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010MJ\u001b\u0010N\u001a\b\u0012\u0002\b\u0003\u0018\u00010O2\u0006\u0010.\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010PJ\u001b\u0010Q\u001a\b\u0012\u0002\b\u0003\u0018\u00010O2\u0006\u0010.\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010PJ\u0019\u0010R\u001a\u00020\"2\u0006\u0010.\u001a\u00028\u0000H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010MJ\u0010\u0010S\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010OH\u0014J\n\u0010T\u001a\u0004\u0018\u00010UH\u0004J\b\u0010V\u001a\u00020\u0005H\u0016R\u0014\u0010\u0004\u001a\u00020\u00058TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\t8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\b\u0012\u0002\b\u0003\u0018\u00010\t8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0012\u0010\u000e\u001a\u00020\u000fX¤\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u0010R\u0012\u0010\u0011\u001a\u00020\u000fX¤\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0012\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0013\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0010R\u0016\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R#\u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00020\u00188F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001b\u001a\u00020\u001cX\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0014\u0010\u001f\u001a\u00020\u00058BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b \u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006]"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/SendChannel;", "()V", "bufferDebugString", "", "getBufferDebugString", "()Ljava/lang/String;", "closedForReceive", "Lkotlinx/coroutines/channels/Closed;", "getClosedForReceive", "()Lkotlinx/coroutines/channels/Closed;", "closedForSend", "getClosedForSend", "isBufferAlwaysFull", "", "()Z", "isBufferFull", "isClosedForSend", "isFull", "onCloseHandler", "Lkotlinx/atomicfu/AtomicRef;", "", "onSend", "Lkotlinx/coroutines/selects/SelectClause2;", "getOnSend", "()Lkotlinx/coroutines/selects/SelectClause2;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "getQueue", "()Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "queueDebugStateString", "getQueueDebugStateString", "afterClose", "", "cause", "", "close", "conflatePreviousSendBuffered", "node", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "countQueueSize", "", "describeSendBuffered", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/internal/AddLastDesc;", "element", "(Ljava/lang/Object;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "describeSendConflated", "describeTryOffer", "Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", "(Ljava/lang/Object;)Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", "enqueueSend", "send", "Lkotlinx/coroutines/channels/SendElement;", "helpClose", "closed", "invokeOnClose", "handler", "Lkotlin/Function1;", "Lkotlinx/coroutines/channels/Handler;", "invokeOnCloseHandler", "offer", "(Ljava/lang/Object;)Z", "offerInternal", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "onClosed", "registerSelectSend", "R", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendBuffered", "Lkotlinx/coroutines/channels/ReceiveOrClosed;", "(Ljava/lang/Object;)Lkotlinx/coroutines/channels/ReceiveOrClosed;", "sendConflated", "sendSuspend", "takeFirstReceiveOrPeekClosed", "takeFirstSendOrPeekClosed", "Lkotlinx/coroutines/channels/Send;", "toString", "SendBuffered", "SendBufferedDesc", "SendConflatedDesc", "SendSelect", "TryEnqueueSendDesc", "TryOfferDesc", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public abstract class AbstractSendChannel<E> implements SendChannel<E> {
    private static final AtomicReferenceFieldUpdater onCloseHandler$FU = AtomicReferenceFieldUpdater.newUpdater(AbstractSendChannel.class, Object.class, "onCloseHandler");
    private final LockFreeLinkedListHead queue = new LockFreeLinkedListHead();
    private volatile Object onCloseHandler = null;

    protected abstract boolean isBufferAlwaysFull();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean isBufferFull();

    protected final LockFreeLinkedListHead getQueue() {
        return this.queue;
    }

    protected Object offerInternal(E e) {
        ReceiveOrClosed receive;
        Object token;
        do {
            receive = takeFirstReceiveOrPeekClosed();
            if (receive == null) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            token = receive.tryResumeReceive(e, null);
        } while (token == null);
        receive.completeResumeReceive(token);
        return receive.getOfferResult();
    }

    protected Object offerSelectInternal(E e, SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        TryOfferDesc offerOp = describeTryOffer(e);
        Object failure = selectInstance.performAtomicTrySelect(offerOp);
        if (failure != null) {
            return failure;
        }
        ReceiveOrClosed receive = offerOp.getResult();
        Object obj = offerOp.resumeToken;
        if (obj == null) {
            Intrinsics.throwNpe();
        }
        receive.completeResumeReceive(obj);
        return receive.getOfferResult();
    }

    protected final Closed<?> getClosedForSend() {
        LockFreeLinkedListNode prevNode = this.queue.getPrevNode();
        if (!(prevNode instanceof Closed)) {
            prevNode = null;
        }
        Closed it = (Closed) prevNode;
        if (it == null) {
            return null;
        }
        helpClose(it);
        return it;
    }

    protected final Closed<?> getClosedForReceive() {
        LockFreeLinkedListNode nextNode = this.queue.getNextNode();
        if (!(nextNode instanceof Closed)) {
            nextNode = null;
        }
        Closed it = (Closed) nextNode;
        if (it == null) {
            return null;
        }
        helpClose(it);
        return it;
    }

    protected final Send takeFirstSendOrPeekClosed() {
        LockFreeLinkedListNode first$iv;
        Send send;
        LockFreeLinkedListNode this_$iv = this.queue;
        while (true) {
            Object next = this_$iv.getNext();
            if (next != null) {
                first$iv = (LockFreeLinkedListNode) next;
                send = null;
                if (first$iv == this_$iv || !(first$iv instanceof Send)) {
                    break;
                } else if (!(((Send) first$iv) instanceof Closed) && !first$iv.remove()) {
                    first$iv.helpDelete();
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
        send = first$iv;
        return send;
    }

    protected final ReceiveOrClosed<?> sendBuffered(E e) {
        LockFreeLinkedListNode prev$iv;
        LockFreeLinkedListNode this_$iv = this.queue;
        LockFreeLinkedListNode node$iv = new SendBuffered(e);
        do {
            Object prev = this_$iv.getPrev();
            if (prev != null) {
                prev$iv = (LockFreeLinkedListNode) prev;
                if (prev$iv instanceof ReceiveOrClosed) {
                    return (ReceiveOrClosed) prev$iv;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } while (!prev$iv.addNext(node$iv, this_$iv));
        return null;
    }

    protected final ReceiveOrClosed<?> sendConflated(E e) {
        LockFreeLinkedListNode prev$iv;
        SendBuffered node = new SendBuffered(e);
        LockFreeLinkedListNode this_$iv = this.queue;
        do {
            Object prev = this_$iv.getPrev();
            if (prev != null) {
                prev$iv = (LockFreeLinkedListNode) prev;
                if (prev$iv instanceof ReceiveOrClosed) {
                    return (ReceiveOrClosed) prev$iv;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } while (!prev$iv.addNext(node, this_$iv));
        conflatePreviousSendBuffered(node);
        return null;
    }

    protected final void conflatePreviousSendBuffered(LockFreeLinkedListNode node) {
        Intrinsics.checkParameterIsNotNull(node, "node");
        LockFreeLinkedListNode prev = node.getPrevNode();
        SendBuffered sendBuffered = (SendBuffered) (!(prev instanceof SendBuffered) ? null : prev);
        if (sendBuffered != null) {
            sendBuffered.remove();
        }
    }

    protected final LockFreeLinkedListNode.AddLastDesc<?> describeSendBuffered(E e) {
        return new SendBufferedDesc(this.queue, e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0012\u0018\u0000*\u0004\b\u0001\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00028\u0001¢\u0006\u0002\u0010\bJ\u001a\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\nH\u0014¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendBufferedDesc;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/channels/AbstractSendChannel$SendBuffered;", "Lkotlinx/coroutines/internal/AddLastDesc;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "element", "(Lkotlinx/coroutines/internal/LockFreeLinkedListHead;Ljava/lang/Object;)V", "failure", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static class SendBufferedDesc<E> extends LockFreeLinkedListNode.AddLastDesc<SendBuffered<? extends E>> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SendBufferedDesc(LockFreeLinkedListHead queue, E e) {
            super(queue, new SendBuffered(e));
            Intrinsics.checkParameterIsNotNull(queue, "queue");
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        protected Object failure(LockFreeLinkedListNode affected, Object next) {
            Intrinsics.checkParameterIsNotNull(affected, "affected");
            Intrinsics.checkParameterIsNotNull(next, "next");
            if (affected instanceof ReceiveOrClosed) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            return null;
        }
    }

    protected final LockFreeLinkedListNode.AddLastDesc<?> describeSendConflated(E e) {
        return new SendConflatedDesc(this.queue, e);
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0001¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0014¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendConflatedDesc;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/AbstractSendChannel$SendBufferedDesc;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "element", "(Lkotlinx/coroutines/internal/LockFreeLinkedListHead;Ljava/lang/Object;)V", "finishOnSuccess", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    private static final class SendConflatedDesc<E> extends SendBufferedDesc<E> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SendConflatedDesc(LockFreeLinkedListHead queue, E e) {
            super(queue, e);
            Intrinsics.checkParameterIsNotNull(queue, "queue");
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.AddLastDesc, kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        public void finishOnSuccess(LockFreeLinkedListNode affected, LockFreeLinkedListNode next) {
            Intrinsics.checkParameterIsNotNull(affected, "affected");
            Intrinsics.checkParameterIsNotNull(next, "next");
            super.finishOnSuccess(affected, next);
            SendBuffered sendBuffered = (SendBuffered) (!(affected instanceof SendBuffered) ? null : affected);
            if (sendBuffered != null) {
                sendBuffered.remove();
            }
        }
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public final boolean isClosedForSend() {
        return getClosedForSend() != null;
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public final boolean isFull() {
        return !(this.queue.getNextNode() instanceof ReceiveOrClosed) && isBufferFull();
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public final Object send(E e, Continuation<? super Unit> continuation) {
        if (offer(e)) {
            return Unit.INSTANCE;
        }
        return sendSuspend(e, continuation);
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public final boolean offer(E e) {
        Throwable sendException;
        Object result = offerInternal(e);
        if (result == AbstractChannelKt.OFFER_SUCCESS) {
            return true;
        }
        if (result == AbstractChannelKt.OFFER_FAILED) {
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend == null || (sendException = closedForSend.getSendException()) == null) {
                return false;
            }
            throw sendException;
        } else if (result instanceof Closed) {
            throw ((Closed) result).getSendException();
        } else {
            throw new IllegalStateException(("offerInternal returned " + result).toString());
        }
    }

    final /* synthetic */ Object sendSuspend(E e, Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellable$iv = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        CancellableContinuationImpl cont = cancellable$iv;
        SendElement send = new SendElement(e, cont);
        while (true) {
            Object enqueueResult = enqueueSend(send);
            if (enqueueResult == null) {
                cont.initCancellability();
                CancellableContinuationKt.removeOnCancellation(cont, send);
                break;
            } else if (enqueueResult instanceof Closed) {
                helpClose((Closed) enqueueResult);
                Throwable sendException = ((Closed) enqueueResult).getSendException();
                Result.Companion companion = Result.Companion;
                cont.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(sendException)));
                break;
            } else {
                Object offerResult = offerInternal(e);
                if (offerResult == AbstractChannelKt.OFFER_SUCCESS) {
                    Unit unit = Unit.INSTANCE;
                    Result.Companion companion2 = Result.Companion;
                    cont.resumeWith(Result.m13constructorimpl(unit));
                    break;
                } else if (offerResult != AbstractChannelKt.OFFER_FAILED) {
                    if (offerResult instanceof Closed) {
                        helpClose((Closed) offerResult);
                        Throwable sendException2 = ((Closed) offerResult).getSendException();
                        Result.Companion companion3 = Result.Companion;
                        cont.resumeWith(Result.m13constructorimpl(ResultKt.createFailure(sendException2)));
                    } else {
                        throw new IllegalStateException(("offerInternal returned " + offerResult).toString());
                    }
                }
            }
        }
        Object result = cancellable$iv.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0061, code lost:
        if (r2 != false) goto L_0x0066;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0065, code lost:
        return kotlinx.coroutines.channels.AbstractChannelKt.ENQUEUE_FAILED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0066, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Object enqueueSend(SendElement send) {
        LockFreeLinkedListNode prev$iv;
        boolean z = false;
        if (isBufferAlwaysFull()) {
            LockFreeLinkedListNode this_$iv = this.queue;
            do {
                Object prev = this_$iv.getPrev();
                if (prev != null) {
                    prev$iv = (LockFreeLinkedListNode) prev;
                    if (prev$iv instanceof ReceiveOrClosed) {
                        return prev$iv;
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
                }
            } while (!prev$iv.addNext(send, this_$iv));
            return null;
        }
        LockFreeLinkedListNode this_$iv2 = this.queue;
        LockFreeLinkedListNode.CondAddOp condAdd$iv = new LockFreeLinkedListNode.CondAddOp(send, this) { // from class: kotlinx.coroutines.channels.AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1
            final /* synthetic */ AbstractSendChannel this$0;

            {
                this.this$0 = r3;
            }

            public Object prepare(LockFreeLinkedListNode affected) {
                Intrinsics.checkParameterIsNotNull(affected, "affected");
                if (this.this$0.isBufferFull()) {
                    return null;
                }
                return LockFreeLinkedListKt.getCONDITION_FALSE();
            }
        };
        while (true) {
            Object prev2 = this_$iv2.getPrev();
            if (prev2 != null) {
                LockFreeLinkedListNode prev$iv2 = (LockFreeLinkedListNode) prev2;
                if (!(prev$iv2 instanceof ReceiveOrClosed)) {
                    int tryCondAddNext = prev$iv2.tryCondAddNext(send, this_$iv2, condAdd$iv);
                    if (tryCondAddNext != 1) {
                        if (tryCondAddNext == 2) {
                            break;
                        }
                    } else {
                        z = true;
                        break;
                    }
                } else {
                    return prev$iv2;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0027, code lost:
        if (r1 != null) goto L_0x003f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0029, code lost:
        r3 = r8.queue.getPrevNode();
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002f, code lost:
        if (r3 == null) goto L_0x0037;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0031, code lost:
        helpClose((kotlinx.coroutines.channels.Closed) r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0036, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x003e, code lost:
        throw new kotlin.TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.Closed<*>");
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x003f, code lost:
        helpClose(r0);
        invokeOnCloseHandler(r9);
        onClosed(r0);
        afterClose(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x004b, code lost:
        return true;
     */
    @Override // kotlinx.coroutines.channels.SendChannel
    /* Code decompiled incorrectly, please refer to instructions dump */
    public boolean close(Throwable cause) {
        LockFreeLinkedListNode this_$iv;
        Closed closed = new Closed(cause);
        LockFreeLinkedListNode this_$iv2 = this.queue;
        while (true) {
            Object prev = this_$iv2.getPrev();
            if (prev != null) {
                LockFreeLinkedListNode prev$iv = (LockFreeLinkedListNode) prev;
                if (!(prev$iv instanceof Closed)) {
                    if (prev$iv.addNext(closed, this_$iv2)) {
                        this_$iv = 1;
                        break;
                    }
                } else {
                    this_$iv = null;
                    break;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
    }

    private final void invokeOnCloseHandler(Throwable cause) {
        Object handler = this.onCloseHandler;
        if (handler != null && handler != AbstractChannelKt.HANDLER_INVOKED && onCloseHandler$FU.compareAndSet(this, handler, AbstractChannelKt.HANDLER_INVOKED)) {
            ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(handler, 1)).invoke(cause);
        }
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public void invokeOnClose(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        if (!onCloseHandler$FU.compareAndSet(this, null, function1)) {
            Object value = this.onCloseHandler;
            if (value == AbstractChannelKt.HANDLER_INVOKED) {
                throw new IllegalStateException("Another handler was already registered and successfully invoked");
            }
            throw new IllegalStateException("Another handler was already registered: " + value);
        }
        Closed closedToken = getClosedForSend();
        if (closedToken != null && onCloseHandler$FU.compareAndSet(this, function1, AbstractChannelKt.HANDLER_INVOKED)) {
            function1.invoke(closedToken.closeCause);
        }
    }

    public final void helpClose(Closed<?> closed) {
        while (true) {
            LockFreeLinkedListNode previous = closed.getPrevNode();
            if (!(previous instanceof LockFreeLinkedListHead) && (previous instanceof Receive)) {
                if (!previous.remove()) {
                    previous.helpRemove();
                } else {
                    Receive receive = (Receive) previous;
                    ((Receive) previous).resumeReceiveClosed(closed);
                }
            } else {
                return;
            }
        }
    }

    protected void onClosed(Closed<? super E> closed) {
        Intrinsics.checkParameterIsNotNull(closed, "closed");
    }

    protected void afterClose(Throwable cause) {
    }

    public ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed() {
        LockFreeLinkedListNode first$iv;
        ReceiveOrClosed<E> receiveOrClosed;
        LockFreeLinkedListNode this_$iv = this.queue;
        while (true) {
            Object next = this_$iv.getNext();
            if (next != null) {
                first$iv = (LockFreeLinkedListNode) next;
                receiveOrClosed = null;
                if (first$iv == this_$iv || !(first$iv instanceof ReceiveOrClosed)) {
                    break;
                } else if (!(((ReceiveOrClosed) first$iv) instanceof Closed) && !first$iv.remove()) {
                    first$iv.helpDelete();
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
        receiveOrClosed = first$iv;
        return receiveOrClosed;
    }

    protected final TryOfferDesc<E> describeTryOffer(E e) {
        return new TryOfferDesc<>(e, this.queue);
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00028\u0001\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u001a\u0010\f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0014J\u0016\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00010\u0003H\u0014R\u0012\u0010\u0005\u001a\u00028\u00018\u0006X\u0087\u0004¢\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\u0004\u0018\u00010\u000b8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "Lkotlinx/coroutines/channels/ReceiveOrClosed;", "Lkotlinx/coroutines/internal/RemoveFirstDesc;", "element", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "(Ljava/lang/Object;Lkotlinx/coroutines/internal/LockFreeLinkedListHead;)V", "Ljava/lang/Object;", "resumeToken", "", "failure", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "validatePrepared", "", "node", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class TryOfferDesc<E> extends LockFreeLinkedListNode.RemoveFirstDesc<ReceiveOrClosed<? super E>> {
        public final E element;
        public Object resumeToken;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TryOfferDesc(E e, LockFreeLinkedListHead queue) {
            super(queue);
            Intrinsics.checkParameterIsNotNull(queue, "queue");
            this.element = e;
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.RemoveFirstDesc
        public /* bridge */ /* synthetic */ boolean validatePrepared(Object obj) {
            return validatePrepared((ReceiveOrClosed) ((ReceiveOrClosed) obj));
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.RemoveFirstDesc, kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        protected Object failure(LockFreeLinkedListNode affected, Object next) {
            Intrinsics.checkParameterIsNotNull(affected, "affected");
            Intrinsics.checkParameterIsNotNull(next, "next");
            if (!(affected instanceof ReceiveOrClosed)) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            if (affected instanceof Closed) {
                return affected;
            }
            return null;
        }

        protected boolean validatePrepared(ReceiveOrClosed<? super E> receiveOrClosed) {
            Intrinsics.checkParameterIsNotNull(receiveOrClosed, "node");
            Object token = receiveOrClosed.tryResumeReceive((E) this.element, this);
            if (token == null) {
                return false;
            }
            this.resumeToken = token;
            return true;
        }
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0082\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004BH\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0007\u0012(\u0010\b\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\n\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\tø\u0001\u0000¢\u0006\u0002\u0010\rJ\u001a\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\fH\u0014J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0014J\u001a\u0010\u0014\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0014\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$TryEnqueueSendDesc;", "R", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/channels/AbstractSendChannel$SendSelect;", "Lkotlinx/coroutines/internal/AddLastDesc;", "element", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/SendChannel;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/channels/AbstractSendChannel;Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "failure", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "finishOnSuccess", "", "onPrepare", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public final class TryEnqueueSendDesc<R> extends LockFreeLinkedListNode.AddLastDesc<SendSelect<E, R>> {
        final /* synthetic */ AbstractSendChannel this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TryEnqueueSendDesc(AbstractSendChannel $outer, E e, SelectInstance<? super R> selectInstance, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
            super($outer.getQueue(), new SendSelect(e, $outer, selectInstance, function2));
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function2, "block");
            this.this$0 = $outer;
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        protected Object failure(LockFreeLinkedListNode affected, Object next) {
            Intrinsics.checkParameterIsNotNull(affected, "affected");
            Intrinsics.checkParameterIsNotNull(next, "next");
            Closed closed = null;
            if (!(affected instanceof ReceiveOrClosed)) {
                return null;
            }
            if (affected instanceof Closed) {
                closed = affected;
            }
            Closed closed2 = closed;
            return closed2 != null ? closed2 : AbstractChannelKt.ENQUEUE_FAILED;
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.AddLastDesc, kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        public Object onPrepare(LockFreeLinkedListNode affected, LockFreeLinkedListNode next) {
            Intrinsics.checkParameterIsNotNull(affected, "affected");
            Intrinsics.checkParameterIsNotNull(next, "next");
            if (!this.this$0.isBufferFull()) {
                return AbstractChannelKt.ENQUEUE_FAILED;
            }
            return super.onPrepare(affected, next);
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.AddLastDesc, kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        public void finishOnSuccess(LockFreeLinkedListNode affected, LockFreeLinkedListNode next) {
            Intrinsics.checkParameterIsNotNull(affected, "affected");
            Intrinsics.checkParameterIsNotNull(next, "next");
            super.finishOnSuccess(affected, next);
            ((SendSelect) this.node).disposeOnSelect();
        }
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public final SelectClause2<E, SendChannel<E>> getOnSend() {
        return new SelectClause2<E, SendChannel<? super E>>() { // from class: kotlinx.coroutines.channels.AbstractSendChannel$onSend$1
            @Override // kotlinx.coroutines.selects.SelectClause2
            public <R> void registerSelectClause2(SelectInstance<? super R> selectInstance, E e, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
                Intrinsics.checkParameterIsNotNull(selectInstance, "select");
                Intrinsics.checkParameterIsNotNull(function2, "block");
                AbstractSendChannel.this.registerSelectSend(selectInstance, e, function2);
            }
        };
    }

    public final <R> void registerSelectSend(SelectInstance<? super R> selectInstance, E e, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
        while (!selectInstance.isSelected()) {
            if (isFull()) {
                Object enqueueResult = selectInstance.performAtomicIfNotSelected(new TryEnqueueSendDesc(this, e, selectInstance, function2));
                if (enqueueResult != null && enqueueResult != SelectKt.getALREADY_SELECTED()) {
                    if (enqueueResult != AbstractChannelKt.ENQUEUE_FAILED) {
                        if (enqueueResult instanceof Closed) {
                            throw ((Closed) enqueueResult).getSendException();
                        }
                        throw new IllegalStateException(("performAtomicIfNotSelected(TryEnqueueSendDesc) returned " + enqueueResult).toString());
                    }
                } else {
                    return;
                }
            } else {
                Object offerResult = offerSelectInternal(e, selectInstance);
                if (offerResult != SelectKt.getALREADY_SELECTED()) {
                    if (offerResult != AbstractChannelKt.OFFER_FAILED) {
                        if (offerResult == AbstractChannelKt.OFFER_SUCCESS) {
                            UndispatchedKt.startCoroutineUnintercepted(function2, this, selectInstance.getCompletion());
                            return;
                        } else if (offerResult instanceof Closed) {
                            throw ((Closed) offerResult).getSendException();
                        } else {
                            throw new IllegalStateException(("offerSelectInternal returned " + offerResult).toString());
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    public String toString() {
        return DebugKt.getClassSimpleName(this) + '@' + DebugKt.getHexAddress(this) + '{' + getQueueDebugStateString() + '}' + getBufferDebugString();
    }

    private final String getQueueDebugStateString() {
        String result;
        LockFreeLinkedListNode head = this.queue.getNextNode();
        if (head == this.queue) {
            return "EmptyQueue";
        }
        if (head instanceof Closed) {
            result = head.toString();
        } else if (head instanceof Receive) {
            result = "ReceiveQueued";
        } else if (head instanceof Send) {
            result = "SendQueued";
        } else {
            result = "UNEXPECTED:" + head;
        }
        LockFreeLinkedListNode tail = this.queue.getPrevNode();
        if (tail == head) {
            return result;
        }
        String result2 = result + ",queueSize=" + countQueueSize();
        if (!(tail instanceof Closed)) {
            return result2;
        }
        return result2 + ",closedForSend=" + tail;
    }

    private final int countQueueSize() {
        int size = 0;
        LockFreeLinkedListHead this_$iv = this.queue;
        Object next = this_$iv.getNext();
        if (next != null) {
            for (LockFreeLinkedListNode cur$iv = (LockFreeLinkedListNode) next; !Intrinsics.areEqual(cur$iv, this_$iv); cur$iv = cur$iv.getNextNode()) {
                if (cur$iv instanceof LockFreeLinkedListNode) {
                    size++;
                }
            }
            return size;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    protected String getBufferDebugString() {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u0001*\u0004\b\u0002\u0010\u00022\u00020\u00032\u00020\u00042\u00020\u0005BX\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00010\t\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00020\u000b\u0012(\u0010\f\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\t\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\rø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0007H\u0016J\b\u0010\u0016\u001a\u00020\u0014H\u0016J\u0006\u0010\u0017\u001a\u00020\u0014J\u0014\u0010\u0018\u001a\u00020\u00142\n\u0010\u0019\u001a\u0006\u0012\u0002\b\u00030\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0014\u0010\u001d\u001a\u0004\u0018\u00010\u00072\b\u0010\u001e\u001a\u0004\u0018\u00010\u0007H\u0016R7\u0010\f\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\t\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\r8\u0006X\u0087\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0010R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00010\t8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00020\u000b8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendSelect;", ExifInterface.LONGITUDE_EAST, "R", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/channels/Send;", "Lkotlinx/coroutines/DisposableHandle;", "pollResult", "", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/Object;Lkotlinx/coroutines/channels/SendChannel;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "getPollResult", "()Ljava/lang/Object;", "completeResumeSend", "", "token", "dispose", "disposeOnSelect", "resumeSendClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "toString", "", "tryResumeSend", "idempotent", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class SendSelect<E, R> extends LockFreeLinkedListNode implements Send, DisposableHandle {
        public final Function2<SendChannel<? super E>, Continuation<? super R>, Object> block;
        public final SendChannel<E> channel;
        private final Object pollResult;
        public final SelectInstance<R> select;

        /* JADX WARN: Multi-variable type inference failed */
        public SendSelect(Object pollResult, SendChannel<? super E> sendChannel, SelectInstance<? super R> selectInstance, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
            Intrinsics.checkParameterIsNotNull(sendChannel, "channel");
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function2, "block");
            this.pollResult = pollResult;
            this.channel = sendChannel;
            this.select = selectInstance;
            this.block = function2;
        }

        @Override // kotlinx.coroutines.channels.Send
        public Object getPollResult() {
            return this.pollResult;
        }

        @Override // kotlinx.coroutines.channels.Send
        public Object tryResumeSend(Object idempotent) {
            if (this.select.trySelect(idempotent)) {
                return AbstractChannelKt.SELECT_STARTED;
            }
            return null;
        }

        @Override // kotlinx.coroutines.channels.Send
        public void completeResumeSend(Object token) {
            Intrinsics.checkParameterIsNotNull(token, "token");
            if (token == AbstractChannelKt.SELECT_STARTED) {
                ContinuationKt.startCoroutine(this.block, this.channel, this.select.getCompletion());
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }

        public final void disposeOnSelect() {
            this.select.disposeOnSelect(this);
        }

        @Override // kotlinx.coroutines.DisposableHandle
        public void dispose() {
            remove();
        }

        @Override // kotlinx.coroutines.channels.Send
        public void resumeSendClosed(Closed<?> closed) {
            Intrinsics.checkParameterIsNotNull(closed, "closed");
            if (this.select.trySelect(null)) {
                this.select.resumeSelectCancellableWithException(closed.getSendException());
            }
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "SendSelect(" + getPollResult() + ")[" + this.channel + ", " + this.select + ']';
        }
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0001¢\u0006\u0002\u0010\u0005J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0016J\u0014\u0010\u000e\u001a\u00020\f2\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u0010H\u0016J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\b2\b\u0010\u0012\u001a\u0004\u0018\u00010\bH\u0016R\u0012\u0010\u0004\u001a\u00028\u00018\u0006X\u0087\u0004¢\u0006\u0004\n\u0002\u0010\u0006R\u0016\u0010\u0007\u001a\u0004\u0018\u00010\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendBuffered;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/channels/Send;", "element", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "pollResult", "", "getPollResult", "()Ljava/lang/Object;", "completeResumeSend", "", "token", "resumeSendClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "tryResumeSend", "idempotent", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class SendBuffered<E> extends LockFreeLinkedListNode implements Send {
        public final E element;

        public SendBuffered(E e) {
            this.element = e;
        }

        @Override // kotlinx.coroutines.channels.Send
        public Object getPollResult() {
            return this.element;
        }

        @Override // kotlinx.coroutines.channels.Send
        public Object tryResumeSend(Object idempotent) {
            return AbstractChannelKt.SEND_RESUMED;
        }

        @Override // kotlinx.coroutines.channels.Send
        public void completeResumeSend(Object token) {
            Intrinsics.checkParameterIsNotNull(token, "token");
            if (!(token == AbstractChannelKt.SEND_RESUMED)) {
                throw new IllegalStateException("Check failed.".toString());
            }
        }

        @Override // kotlinx.coroutines.channels.Send
        public void resumeSendClosed(Closed<?> closed) {
            Intrinsics.checkParameterIsNotNull(closed, "closed");
        }
    }
}
