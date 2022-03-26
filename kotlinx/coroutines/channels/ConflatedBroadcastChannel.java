package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.internal.ArrayCopyKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause2;
import kotlinx.coroutines.selects.SelectInstance;
/* compiled from: ConflatedBroadcastChannel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u0000 @*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0004?@ABB\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00028\u0000¢\u0006\u0002\u0010\u0004B\u0005¢\u0006\u0002\u0010\u0005J=\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u001d0\u001c2\u0014\u0010\u001e\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u001d\u0018\u00010\u001c2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00028\u00000\u001dH\u0002¢\u0006\u0002\u0010 J\u0012\u0010!\u001a\u00020\f2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u0012\u0010$\u001a\u00020\f2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u0016\u0010%\u001a\u00020&2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00028\u00000\u001dH\u0002J\"\u0010'\u001a\u00020&2\u0018\u0010(\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010#\u0012\u0004\u0012\u00020&0)j\u0002`*H\u0016J\u0012\u0010+\u001a\u00020&2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0002J\u0015\u0010,\u001a\u00020\f2\u0006\u0010-\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010.J\u0017\u0010/\u001a\u0004\u0018\u0001002\u0006\u0010-\u001a\u00028\u0000H\u0002¢\u0006\u0002\u00101J\u000e\u00102\u001a\b\u0012\u0004\u0012\u00028\u000003H\u0016JV\u00104\u001a\u00020&\"\u0004\b\u0001\u001052\f\u00106\u001a\b\u0012\u0004\u0012\u0002H5072\u0006\u0010-\u001a\u00028\u00002(\u00108\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0012\u0012\n\u0012\b\u0012\u0004\u0012\u0002H50:\u0012\u0006\u0012\u0004\u0018\u00010\b09H\u0002ø\u0001\u0000¢\u0006\u0002\u0010;J=\u0010<\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u001d\u0018\u00010\u001c2\u0012\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u001d0\u001c2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00028\u00000\u001dH\u0002¢\u0006\u0002\u0010 J\u0019\u0010=\u001a\u00020&2\u0006\u0010-\u001a\u00028\u0000H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010>R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\rR\u0014\u0010\u000e\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\rR\u0016\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010\u0010\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00120\u00118VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0017\u0010\u0003\u001a\u00028\u00008F¢\u0006\f\u0012\u0004\b\u0015\u0010\u0005\u001a\u0004\b\u0016\u0010\u0017R\u0019\u0010\u0018\u001a\u0004\u0018\u00018\u00008F¢\u0006\f\u0012\u0004\b\u0019\u0010\u0005\u001a\u0004\b\u001a\u0010\u0017\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006C"}, d2 = {"Lkotlinx/coroutines/channels/ConflatedBroadcastChannel;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/BroadcastChannel;", "value", "(Ljava/lang/Object;)V", "()V", "_state", "Lkotlinx/atomicfu/AtomicRef;", "", "_updating", "Lkotlinx/atomicfu/AtomicInt;", "isClosedForSend", "", "()Z", "isFull", "onCloseHandler", "onSend", "Lkotlinx/coroutines/selects/SelectClause2;", "Lkotlinx/coroutines/channels/SendChannel;", "getOnSend", "()Lkotlinx/coroutines/selects/SelectClause2;", "value$annotations", "getValue", "()Ljava/lang/Object;", "valueOrNull", "valueOrNull$annotations", "getValueOrNull", "addSubscriber", "", "Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Subscriber;", "list", "subscriber", "([Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Subscriber;Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Subscriber;)[Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Subscriber;", "cancel", "cause", "", "close", "closeSubscriber", "", "invokeOnClose", "handler", "Lkotlin/Function1;", "Lkotlinx/coroutines/channels/Handler;", "invokeOnCloseHandler", "offer", "element", "(Ljava/lang/Object;)Z", "offerInternal", "Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Closed;", "(Ljava/lang/Object;)Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Closed;", "openSubscription", "Lkotlinx/coroutines/channels/ReceiveChannel;", "registerSelectSend", "R", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "removeSubscriber", "send", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Closed", "Companion", "State", "Subscriber", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class ConflatedBroadcastChannel<E> implements BroadcastChannel<E> {
    private volatile Object _state;
    private volatile int _updating;
    private volatile Object onCloseHandler;
    @Deprecated
    public static final Companion Companion = new Companion(null);
    private static final Closed CLOSED = new Closed(null);
    private static final Symbol UNDEFINED = new Symbol("UNDEFINED");
    private static final State<Object> INITIAL_STATE = new State<>(UNDEFINED, null);
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(ConflatedBroadcastChannel.class, Object.class, "_state");
    private static final AtomicIntegerFieldUpdater _updating$FU = AtomicIntegerFieldUpdater.newUpdater(ConflatedBroadcastChannel.class, "_updating");
    private static final AtomicReferenceFieldUpdater onCloseHandler$FU = AtomicReferenceFieldUpdater.newUpdater(ConflatedBroadcastChannel.class, Object.class, "onCloseHandler");

    public static /* synthetic */ void value$annotations() {
    }

    public static /* synthetic */ void valueOrNull$annotations() {
    }

    public ConflatedBroadcastChannel() {
        this._state = INITIAL_STATE;
        this._updating = 0;
        this.onCloseHandler = null;
    }

    public ConflatedBroadcastChannel(E e) {
        this();
        _state$FU.lazySet(this, new State(e, null));
    }

    /* compiled from: ConflatedBroadcastChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Companion;", "", "()V", "CLOSED", "Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Closed;", "INITIAL_STATE", "Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$State;", "UNDEFINED", "Lkotlinx/coroutines/internal/Symbol;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    private static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    /* compiled from: ConflatedBroadcastChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\u00020\u0002B%\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002\u0012\u0014\u0010\u0004\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0006\u0018\u00010\u0005¢\u0006\u0002\u0010\u0007R \u0010\u0004\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0006\u0018\u00010\u00058\u0006X\u0087\u0004¢\u0006\u0004\n\u0002\u0010\bR\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00028\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$State;", ExifInterface.LONGITUDE_EAST, "", "value", "subscribers", "", "Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Subscriber;", "(Ljava/lang/Object;[Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Subscriber;)V", "[Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Subscriber;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class State<E> {
        public final Subscriber<E>[] subscribers;
        public final Object value;

        public State(Object value, Subscriber<E>[] subscriberArr) {
            this.value = value;
            this.subscribers = subscriberArr;
        }
    }

    /* compiled from: ConflatedBroadcastChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\t\u0010\u0007¨\u0006\n"}, d2 = {"Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Closed;", "", "closeCause", "", "(Ljava/lang/Throwable;)V", "sendException", "getSendException", "()Ljava/lang/Throwable;", "valueException", "getValueException", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class Closed {
        public final Throwable closeCause;

        public Closed(Throwable closeCause) {
            this.closeCause = closeCause;
        }

        public final Throwable getSendException() {
            Throwable th = this.closeCause;
            return th != null ? th : new ClosedSendChannelException(ChannelsKt.DEFAULT_CLOSE_MESSAGE);
        }

        public final Throwable getValueException() {
            Throwable th = this.closeCause;
            return th != null ? th : new IllegalStateException(ChannelsKt.DEFAULT_CLOSE_MESSAGE);
        }
    }

    public final E getValue() {
        Object state = this._state;
        if (state instanceof Closed) {
            throw ((Closed) state).getValueException();
        } else if (!(state instanceof State)) {
            throw new IllegalStateException(("Invalid state " + state).toString());
        } else if (((State) state).value != UNDEFINED) {
            return (E) ((State) state).value;
        } else {
            throw new IllegalStateException("No value");
        }
    }

    public final E getValueOrNull() {
        Object state = this._state;
        if (state instanceof Closed) {
            return null;
        }
        if (!(state instanceof State)) {
            throw new IllegalStateException(("Invalid state " + state).toString());
        } else if (((State) state).value == UNDEFINED) {
            return null;
        } else {
            return (E) ((State) state).value;
        }
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public boolean isClosedForSend() {
        return this._state instanceof Closed;
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public boolean isFull() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.channels.BroadcastChannel
    public ReceiveChannel<E> openSubscription() {
        Object state;
        Object obj;
        Subscriber subscriber = new Subscriber(this);
        do {
            state = this._state;
            if (state instanceof Closed) {
                subscriber.close(((Closed) state).closeCause);
                return subscriber;
            } else if (state instanceof State) {
                if (((State) state).value != UNDEFINED) {
                    subscriber.offerInternal(((State) state).value);
                }
                obj = ((State) state).value;
                if (state != null) {
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.ConflatedBroadcastChannel.State<E>");
                }
            } else {
                throw new IllegalStateException(("Invalid state " + state).toString());
            }
        } while (!_state$FU.compareAndSet(this, state, new State(obj, addSubscriber(((State) state).subscribers, subscriber))));
        return subscriber;
    }

    public final void closeSubscriber(Subscriber<E> subscriber) {
        Object state;
        Object obj;
        Subscriber<E>[] subscriberArr;
        do {
            state = this._state;
            if (!(state instanceof Closed)) {
                if (state instanceof State) {
                    obj = ((State) state).value;
                    if (state != null) {
                        subscriberArr = ((State) state).subscribers;
                        if (subscriberArr == null) {
                            Intrinsics.throwNpe();
                        }
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.ConflatedBroadcastChannel.State<E>");
                    }
                } else {
                    throw new IllegalStateException(("Invalid state " + state).toString());
                }
            } else {
                return;
            }
        } while (!_state$FU.compareAndSet(this, state, new State(obj, removeSubscriber(subscriberArr, subscriber))));
    }

    private final Subscriber<E>[] addSubscriber(Subscriber<E>[] subscriberArr, Subscriber<E> subscriber) {
        if (subscriberArr != null) {
            return (Subscriber[]) ArraysKt.plus(subscriberArr, subscriber);
        }
        Subscriber<E>[] subscriberArr2 = new Subscriber[1];
        int length = subscriberArr2.length;
        for (int i$iv = 0; i$iv < length; i$iv++) {
            subscriberArr2[i$iv] = subscriber;
        }
        return subscriberArr2;
    }

    private final Subscriber<E>[] removeSubscriber(Subscriber<E>[] subscriberArr, Subscriber<E> subscriber) {
        int n = subscriberArr.length;
        int i = ArraysKt.indexOf(subscriberArr, subscriber);
        if (!(i >= 0)) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (n == 1) {
            return null;
        } else {
            Subscriber[] update = new Subscriber[n - 1];
            ArrayCopyKt.arraycopy(subscriberArr, 0, update, 0, i);
            ArrayCopyKt.arraycopy(subscriberArr, i + 1, update, i, (n - i) - 1);
            return update;
        }
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public boolean close(Throwable cause) {
        Object state;
        do {
            state = this._state;
            if (state instanceof Closed) {
                return false;
            }
            if (!(state instanceof State)) {
                throw new IllegalStateException(("Invalid state " + state).toString());
            }
        } while (!_state$FU.compareAndSet(this, state, cause == null ? CLOSED : new Closed(cause)));
        if (state != null) {
            Subscriber<E>[] subscriberArr = ((State) state).subscribers;
            if (subscriberArr != null) {
                for (Subscriber<E> subscriber : subscriberArr) {
                    subscriber.close(cause);
                }
            }
            invokeOnCloseHandler(cause);
            return true;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.ConflatedBroadcastChannel.State<E>");
    }

    private final void invokeOnCloseHandler(Throwable cause) {
        Object handler = this.onCloseHandler;
        if (handler != null && handler != AbstractChannelKt.HANDLER_INVOKED && onCloseHandler$FU.compareAndSet(this, handler, AbstractChannelKt.HANDLER_INVOKED)) {
            ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(handler, 1)).invoke(cause);
        }
    }

    /* JADX INFO: Multiple debug info for r0v3 java.lang.Object: [D('value' java.lang.Object), D('state' java.lang.Object)] */
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
        Object state = this._state;
        if ((state instanceof Closed) && onCloseHandler$FU.compareAndSet(this, function1, AbstractChannelKt.HANDLER_INVOKED)) {
            function1.invoke(((Closed) state).closeCause);
        }
    }

    @Override // kotlinx.coroutines.channels.BroadcastChannel
    public boolean cancel(Throwable cause) {
        return close(cause);
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public Object send(E e, Continuation<? super Unit> continuation) {
        Closed it = offerInternal(e);
        if (it == null) {
            return Unit.INSTANCE;
        }
        throw it.getSendException();
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public boolean offer(E e) {
        Closed it = offerInternal(e);
        if (it == null) {
            return true;
        }
        throw it.getSendException();
    }

    private final Closed offerInternal(E e) {
        Object state;
        if (!_updating$FU.compareAndSet(this, 0, 1)) {
            return null;
        }
        do {
            try {
                state = this._state;
                if (state instanceof Closed) {
                    return (Closed) state;
                }
                if (!(state instanceof State)) {
                    throw new IllegalStateException(("Invalid state " + state).toString());
                } else if (state == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.ConflatedBroadcastChannel.State<E>");
                }
            } finally {
                this._updating = 0;
            }
        } while (!_state$FU.compareAndSet(this, state, new State(e, ((State) state).subscribers)));
        Subscriber<E>[] subscriberArr = ((State) state).subscribers;
        if (subscriberArr != null) {
            for (Subscriber<E> subscriber : subscriberArr) {
                subscriber.offerInternal(e);
            }
        }
        return null;
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public SelectClause2<E, SendChannel<E>> getOnSend() {
        return new SelectClause2<E, SendChannel<? super E>>() { // from class: kotlinx.coroutines.channels.ConflatedBroadcastChannel$onSend$1
            @Override // kotlinx.coroutines.selects.SelectClause2
            public <R> void registerSelectClause2(SelectInstance<? super R> selectInstance, E e, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
                Intrinsics.checkParameterIsNotNull(selectInstance, "select");
                Intrinsics.checkParameterIsNotNull(function2, "block");
                ConflatedBroadcastChannel.this.registerSelectSend(selectInstance, e, function2);
            }
        };
    }

    public final <R> void registerSelectSend(SelectInstance<? super R> selectInstance, E e, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
        if (selectInstance.trySelect(null)) {
            Closed it = offerInternal(e);
            if (it != null) {
                selectInstance.resumeSelectCancellableWithException(it.getSendException());
            } else {
                UndispatchedKt.startCoroutineUnintercepted(function2, this, selectInstance.getCompletion());
            }
        }
    }

    /* compiled from: ConflatedBroadcastChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u0015\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\u000eR\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/channels/ConflatedBroadcastChannel$Subscriber;", ExifInterface.LONGITUDE_EAST, "Lkotlinx/coroutines/channels/ConflatedChannel;", "Lkotlinx/coroutines/channels/ReceiveChannel;", "broadcastChannel", "Lkotlinx/coroutines/channels/ConflatedBroadcastChannel;", "(Lkotlinx/coroutines/channels/ConflatedBroadcastChannel;)V", "cancel", "", "cause", "", "offerInternal", "", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class Subscriber<E> extends ConflatedChannel<E> implements ReceiveChannel<E> {
        private final ConflatedBroadcastChannel<E> broadcastChannel;

        public Subscriber(ConflatedBroadcastChannel<E> conflatedBroadcastChannel) {
            Intrinsics.checkParameterIsNotNull(conflatedBroadcastChannel, "broadcastChannel");
            this.broadcastChannel = conflatedBroadcastChannel;
        }

        @Override // kotlinx.coroutines.channels.AbstractChannel, kotlinx.coroutines.channels.ReceiveChannel
        public boolean cancel(Throwable cause) {
            boolean closed = close(cause);
            if (closed) {
                this.broadcastChannel.closeSubscriber(this);
            }
            return closed;
        }

        @Override // kotlinx.coroutines.channels.ConflatedChannel, kotlinx.coroutines.channels.AbstractSendChannel
        public Object offerInternal(E e) {
            return super.offerInternal(e);
        }
    }
}
