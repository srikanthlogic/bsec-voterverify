package kotlinx.coroutines.sync;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.internal.AtomicDesc;
import kotlinx.coroutines.internal.AtomicOp;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause2;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;
import kotlinx.coroutines.sync.MutexImpl;
/* compiled from: Mutex.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\b\u0000\u0018\u00002\u00020\u00012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00010\u0002:\u0007\"#$%&'(B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0003H\u0016J\u001b\u0010\u0012\u001a\u00020\u00132\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0014J\u001b\u0010\u0015\u001a\u00020\u00132\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0014JR\u0010\u0016\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00170\u00192\b\u0010\u0011\u001a\u0004\u0018\u00010\u00032\"\u0010\u001a\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00170\u001c\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u001bH\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u001dJ\b\u0010\u001e\u001a\u00020\u001fH\u0016J\u0012\u0010 \u001a\u00020\u00052\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u0016J\u0012\u0010!\u001a\u00020\u00132\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u0016R\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\u00058@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\nR\"\u0010\r\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00010\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006)"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl;", "Lkotlinx/coroutines/sync/Mutex;", "Lkotlinx/coroutines/selects/SelectClause2;", "", "locked", "", "(Z)V", "_state", "Lkotlinx/atomicfu/AtomicRef;", "isLocked", "()Z", "isLockedEmptyQueueState", "isLockedEmptyQueueState$kotlinx_coroutines_core", "onLock", "getOnLock", "()Lkotlinx/coroutines/selects/SelectClause2;", "holdsLock", "owner", "lock", "", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "lockSuspend", "registerSelectClause2", "R", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "toString", "", "tryLock", "unlock", "LockCont", "LockSelect", "LockWaiter", "LockedQueue", "TryEnqueueLockDesc", "TryLockDesc", "UnlockOp", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class MutexImpl implements Mutex, SelectClause2<Object, Mutex> {
    static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(MutexImpl.class, Object.class, "_state");
    volatile Object _state;

    public MutexImpl(boolean locked) {
        this._state = locked ? MutexKt.EmptyLocked : MutexKt.EmptyUnlocked;
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public boolean isLocked() {
        while (true) {
            Object state = this._state;
            if (state instanceof Empty) {
                if (((Empty) state).locked != MutexKt.UNLOCKED) {
                    return true;
                }
                return false;
            } else if (state instanceof LockedQueue) {
                return true;
            } else {
                if (state instanceof OpDescriptor) {
                    ((OpDescriptor) state).perform(this);
                } else {
                    throw new IllegalStateException(("Illegal state " + state).toString());
                }
            }
        }
    }

    public final boolean isLockedEmptyQueueState$kotlinx_coroutines_core() {
        Object state = this._state;
        return (state instanceof LockedQueue) && ((LockedQueue) state).isEmpty();
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public boolean tryLock(Object owner) {
        while (true) {
            Object state = this._state;
            boolean z = true;
            if (state instanceof Empty) {
                if (((Empty) state).locked != MutexKt.UNLOCKED) {
                    return false;
                }
                if (_state$FU.compareAndSet(this, state, owner == null ? MutexKt.EmptyLocked : new Empty(owner))) {
                    return true;
                }
            } else if (state instanceof LockedQueue) {
                if (((LockedQueue) state).owner == owner) {
                    z = false;
                }
                if (z) {
                    return false;
                }
                throw new IllegalStateException(("Already locked by " + owner).toString());
            } else if (state instanceof OpDescriptor) {
                ((OpDescriptor) state).perform(this);
            } else {
                throw new IllegalStateException(("Illegal state " + state).toString());
            }
        }
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public Object lock(Object owner, Continuation<? super Unit> continuation) {
        if (tryLock(owner)) {
            return Unit.INSTANCE;
        }
        return lockSuspend(owner, continuation);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00e0, code lost:
        if (r11 == false) goto L_0x00fe;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00e2, code lost:
        r7.initCancellability();
        kotlinx.coroutines.CancellableContinuationKt.removeOnCancellation(r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00ef, code lost:
        r0 = r0.getResult();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00f7, code lost:
        if (r0 != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L_0x00fd;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00f9, code lost:
        kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r32);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00fd, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00fe, code lost:
        r4 = r7;
        r1 = r1;
        r2 = r2;
        r0 = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    final /* synthetic */ Object lockSuspend(Object owner, Continuation<? super Unit> continuation) {
        MutexImpl $receiver$iv;
        CancellableContinuation cont;
        boolean z;
        boolean z2 = false;
        CancellableContinuationImpl cancellable$iv = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        CancellableContinuationImpl cont2 = cancellable$iv;
        LockCont waiter = new LockCont(owner, cont2);
        MutexImpl $receiver$iv2 = this;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        while (true) {
            Object state = $receiver$iv2._state;
            if (state instanceof Empty) {
                if (((Empty) state).locked != MutexKt.UNLOCKED) {
                    _state$FU.compareAndSet(this, state, new LockedQueue(((Empty) state).locked));
                    $receiver$iv = $receiver$iv2;
                    cont = cont2;
                    z = z2;
                } else {
                    if (_state$FU.compareAndSet(this, state, owner == null ? MutexKt.EmptyLocked : new Empty(owner))) {
                        Unit unit = Unit.INSTANCE;
                        Result.Companion companion = Result.Companion;
                        cont2.resumeWith(Result.m13constructorimpl(unit));
                        break;
                    }
                    $receiver$iv = $receiver$iv2;
                    cont = cont2;
                    z = z2;
                }
                z2 = z;
                cont2 = cont;
                z3 = z3;
                $receiver$iv2 = $receiver$iv;
            } else {
                if (state instanceof LockedQueue) {
                    if (((LockedQueue) state).owner != owner ? true : z2) {
                        LockFreeLinkedListNode node$iv = waiter;
                        LockFreeLinkedListNode this_$iv = (LockedQueue) state;
                        boolean z6 = true;
                        $receiver$iv = $receiver$iv2;
                        LockFreeLinkedListNode.CondAddOp condAdd$iv = new LockFreeLinkedListNode.CondAddOp(node$iv, state, cont2, waiter, this, owner) { // from class: kotlinx.coroutines.sync.MutexImpl$lockSuspend$$inlined$suspendAtomicCancellableCoroutine$lambda$1
                            final /* synthetic */ CancellableContinuation $cont$inlined;
                            final /* synthetic */ Object $owner$inlined;
                            final /* synthetic */ Object $state$inlined;
                            final /* synthetic */ MutexImpl.LockCont $waiter$inlined;
                            final /* synthetic */ MutexImpl this$0;

                            {
                                this.$state$inlined = r3;
                                this.$cont$inlined = r4;
                                this.$waiter$inlined = r5;
                                this.this$0 = r6;
                                this.$owner$inlined = r7;
                            }

                            public Object prepare(LockFreeLinkedListNode affected) {
                                Intrinsics.checkParameterIsNotNull(affected, "affected");
                                boolean z7 = false;
                                if (this.this$0._state == this.$state$inlined) {
                                    z7 = true;
                                }
                                if (z7) {
                                    return null;
                                }
                                return LockFreeLinkedListKt.getCONDITION_FALSE();
                            }
                        };
                        while (true) {
                            Object prev = this_$iv.getPrev();
                            if (prev != null) {
                                int tryCondAddNext = ((LockFreeLinkedListNode) prev).tryCondAddNext(node$iv, this_$iv, condAdd$iv);
                                if (tryCondAddNext != 1) {
                                    if (tryCondAddNext == 2) {
                                        z6 = false;
                                        break;
                                    }
                                    this_$iv = this_$iv;
                                    node$iv = node$iv;
                                } else {
                                    break;
                                }
                            } else {
                                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
                            }
                        }
                    } else {
                        throw new IllegalStateException(("Already locked by " + owner).toString());
                    }
                } else {
                    $receiver$iv = $receiver$iv2;
                    cont = cont2;
                    z = z2;
                    if (state instanceof OpDescriptor) {
                        ((OpDescriptor) state).perform(this);
                    } else {
                        throw new IllegalStateException(("Illegal state " + state).toString());
                    }
                }
                z2 = z;
                cont2 = cont;
                z3 = z3;
                $receiver$iv2 = $receiver$iv;
            }
        }
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public SelectClause2<Object, Mutex> getOnLock() {
        return this;
    }

    @Override // kotlinx.coroutines.selects.SelectClause2
    public <R> void registerSelectClause2(SelectInstance<? super R> selectInstance, Object owner, Function2<? super Mutex, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        while (!selectInstance.isSelected()) {
            Object state = this._state;
            if (state instanceof Empty) {
                if (((Empty) state).locked != MutexKt.UNLOCKED) {
                    _state$FU.compareAndSet(this, state, new LockedQueue(((Empty) state).locked));
                } else {
                    Object failure = selectInstance.performAtomicTrySelect(new TryLockDesc(this, owner));
                    if (failure == null) {
                        UndispatchedKt.startCoroutineUnintercepted(function2, this, selectInstance.getCompletion());
                        return;
                    } else if (failure != SelectKt.getALREADY_SELECTED()) {
                        if (failure != MutexKt.LOCK_FAIL) {
                            throw new IllegalStateException(("performAtomicTrySelect(TryLockDesc) returned " + failure).toString());
                        }
                    } else {
                        return;
                    }
                }
            } else if (state instanceof LockedQueue) {
                boolean z = false;
                if (((LockedQueue) state).owner != owner) {
                    z = true;
                }
                if (z) {
                    TryEnqueueLockDesc enqueueOp = new TryEnqueueLockDesc(this, owner, (LockedQueue) state, selectInstance, function2);
                    Object failure2 = selectInstance.performAtomicIfNotSelected(enqueueOp);
                    if (failure2 == null) {
                        selectInstance.disposeOnSelect((DisposableHandle) enqueueOp.node);
                        return;
                    } else if (failure2 != SelectKt.getALREADY_SELECTED()) {
                        if (failure2 != MutexKt.ENQUEUE_FAIL) {
                            throw new IllegalStateException(("performAtomicIfNotSelected(TryEnqueueLockDesc) returned " + failure2).toString());
                        }
                    } else {
                        return;
                    }
                } else {
                    throw new IllegalStateException(("Already locked by " + owner).toString());
                }
            } else if (state instanceof OpDescriptor) {
                ((OpDescriptor) state).perform(this);
            } else {
                throw new IllegalStateException(("Illegal state " + state).toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Mutex.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001:\u0001\rB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u001e\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0005H\u0016J\u0016\u0010\f\u001a\u0004\u0018\u00010\u00052\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\nH\u0016R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$TryLockDesc;", "Lkotlinx/coroutines/internal/AtomicDesc;", "mutex", "Lkotlinx/coroutines/sync/MutexImpl;", "owner", "", "(Lkotlinx/coroutines/sync/MutexImpl;Ljava/lang/Object;)V", "complete", "", "op", "Lkotlinx/coroutines/internal/AtomicOp;", "failure", "prepare", "PrepareOp", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class TryLockDesc extends AtomicDesc {
        public final MutexImpl mutex;
        public final Object owner;

        public TryLockDesc(MutexImpl mutex, Object owner) {
            Intrinsics.checkParameterIsNotNull(mutex, "mutex");
            this.mutex = mutex;
            this.owner = owner;
        }

        /* compiled from: Mutex.kt */
        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0016R\u0012\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$TryLockDesc$PrepareOp;", "Lkotlinx/coroutines/internal/OpDescriptor;", "op", "Lkotlinx/coroutines/internal/AtomicOp;", "(Lkotlinx/coroutines/sync/MutexImpl$TryLockDesc;Lkotlinx/coroutines/internal/AtomicOp;)V", "perform", "", "affected", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
        /* loaded from: classes3.dex */
        private final class PrepareOp extends OpDescriptor {
            private final AtomicOp<?> op;
            final /* synthetic */ TryLockDesc this$0;

            public PrepareOp(TryLockDesc $outer, AtomicOp<?> atomicOp) {
                Intrinsics.checkParameterIsNotNull(atomicOp, "op");
                this.this$0 = $outer;
                this.op = atomicOp;
            }

            @Override // kotlinx.coroutines.internal.OpDescriptor
            public Object perform(Object affected) {
                Object update = this.op.isDecided() ? MutexKt.EmptyUnlocked : this.op;
                if (affected != null) {
                    MutexImpl._state$FU.compareAndSet((MutexImpl) affected, this, update);
                    return null;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.sync.MutexImpl");
            }
        }

        @Override // kotlinx.coroutines.internal.AtomicDesc
        public Object prepare(AtomicOp<?> atomicOp) {
            Intrinsics.checkParameterIsNotNull(atomicOp, "op");
            PrepareOp prepare = new PrepareOp(this, atomicOp);
            if (!MutexImpl._state$FU.compareAndSet(this.mutex, MutexKt.EmptyUnlocked, prepare)) {
                return MutexKt.LOCK_FAIL;
            }
            return prepare.perform(this.mutex);
        }

        @Override // kotlinx.coroutines.internal.AtomicDesc
        public void complete(AtomicOp<?> atomicOp, Object failure) {
            Empty update;
            Intrinsics.checkParameterIsNotNull(atomicOp, "op");
            if (failure != null) {
                update = MutexKt.EmptyUnlocked;
            } else {
                Object obj = this.owner;
                update = obj == null ? MutexKt.EmptyLocked : new Empty(obj);
            }
            MutexImpl._state$FU.compareAndSet(this.mutex, atomicOp, update);
        }
    }

    /* compiled from: Mutex.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004BT\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\f\u0012\"\u0010\r\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0010\u0012\u0006\u0012\u0004\u0018\u00010\b0\u000eø\u0001\u0000¢\u0006\u0002\u0010\u0011J\u001a\u0010\u0012\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0014R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0016"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$TryEnqueueLockDesc;", "R", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/sync/MutexImpl$LockSelect;", "Lkotlinx/coroutines/internal/AddLastDesc;", "mutex", "Lkotlinx/coroutines/sync/MutexImpl;", "owner", "", "queue", "Lkotlinx/coroutines/sync/MutexImpl$LockedQueue;", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/sync/Mutex;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/sync/MutexImpl;Ljava/lang/Object;Lkotlinx/coroutines/sync/MutexImpl$LockedQueue;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "onPrepare", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    private static final class TryEnqueueLockDesc<R> extends LockFreeLinkedListNode.AddLastDesc<LockSelect<R>> {
        public final MutexImpl mutex;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TryEnqueueLockDesc(MutexImpl mutex, Object owner, LockedQueue queue, SelectInstance<? super R> selectInstance, Function2<? super Mutex, ? super Continuation<? super R>, ? extends Object> function2) {
            super(queue, new LockSelect(owner, mutex, selectInstance, function2));
            Intrinsics.checkParameterIsNotNull(mutex, "mutex");
            Intrinsics.checkParameterIsNotNull(queue, "queue");
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function2, "block");
            this.mutex = mutex;
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.AddLastDesc, kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        public Object onPrepare(LockFreeLinkedListNode affected, LockFreeLinkedListNode next) {
            Intrinsics.checkParameterIsNotNull(affected, "affected");
            Intrinsics.checkParameterIsNotNull(next, "next");
            if (this.mutex._state != this.queue) {
                return MutexKt.ENQUEUE_FAIL;
            }
            return super.onPrepare(affected, next);
        }
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public boolean holdsLock(Object owner) {
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Object state = this._state;
        if (state instanceof Empty) {
            if (((Empty) state).locked == owner) {
                return true;
            }
            return false;
        } else if (!(state instanceof LockedQueue) || ((LockedQueue) state).owner != owner) {
            return false;
        } else {
            return true;
        }
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public void unlock(Object owner) {
        while (true) {
            Object state = this._state;
            boolean z = true;
            if (state instanceof Empty) {
                if (owner == null) {
                    if (((Empty) state).locked == MutexKt.UNLOCKED) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException("Mutex is not locked".toString());
                    }
                } else {
                    if (((Empty) state).locked != owner) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException(("Mutex is locked by " + ((Empty) state).locked + " but expected " + owner).toString());
                    }
                }
                if (_state$FU.compareAndSet(this, state, MutexKt.EmptyUnlocked)) {
                    return;
                }
            } else if (state instanceof OpDescriptor) {
                ((OpDescriptor) state).perform(this);
            } else if (state instanceof LockedQueue) {
                if (owner != null) {
                    if (((LockedQueue) state).owner != owner) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException(("Mutex is locked by " + ((LockedQueue) state).owner + " but expected " + owner).toString());
                    }
                }
                LockFreeLinkedListNode waiter = ((LockedQueue) state).removeFirstOrNull();
                if (waiter == null) {
                    UnlockOp op = new UnlockOp((LockedQueue) state);
                    if (_state$FU.compareAndSet(this, state, op) && op.perform(this) == null) {
                        return;
                    }
                } else {
                    Object token = ((LockWaiter) waiter).tryResumeLockWaiter();
                    if (token != null) {
                        LockedQueue lockedQueue = (LockedQueue) state;
                        Object obj = ((LockWaiter) waiter).owner;
                        if (obj == null) {
                            obj = MutexKt.LOCKED;
                        }
                        lockedQueue.owner = obj;
                        ((LockWaiter) waiter).completeResumeLockWaiter(token);
                        return;
                    }
                }
            } else {
                throw new IllegalStateException(("Illegal state " + state).toString());
            }
        }
    }

    public String toString() {
        while (true) {
            Object state = this._state;
            if (state instanceof Empty) {
                return "Mutex[" + ((Empty) state).locked + ']';
            } else if (state instanceof OpDescriptor) {
                ((OpDescriptor) state).perform(this);
            } else if (state instanceof LockedQueue) {
                return "Mutex[" + ((LockedQueue) state).owner + ']';
            } else {
                throw new IllegalStateException(("Illegal state " + state).toString());
            }
        }
    }

    /* compiled from: Mutex.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016R\u0012\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$LockedQueue;", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "owner", "", "(Ljava/lang/Object;)V", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class LockedQueue extends LockFreeLinkedListHead {
        public Object owner;

        public LockedQueue(Object owner) {
            Intrinsics.checkParameterIsNotNull(owner, "owner");
            this.owner = owner;
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "LockedQueue[" + this.owner + ']';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Mutex.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\"\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0004H&J\u0006\u0010\t\u001a\u00020\u0007J\n\u0010\n\u001a\u0004\u0018\u00010\u0004H&R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$LockWaiter;", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/DisposableHandle;", "owner", "", "(Ljava/lang/Object;)V", "completeResumeLockWaiter", "", "token", "dispose", "tryResumeLockWaiter", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static abstract class LockWaiter extends LockFreeLinkedListNode implements DisposableHandle {
        public final Object owner;

        public abstract void completeResumeLockWaiter(Object obj);

        public abstract Object tryResumeLockWaiter();

        public LockWaiter(Object owner) {
            this.owner = owner;
        }

        @Override // kotlinx.coroutines.DisposableHandle
        public final void dispose() {
            remove();
        }
    }

    /* compiled from: Mutex.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0003H\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\n\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0016R\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$LockCont;", "Lkotlinx/coroutines/sync/MutexImpl$LockWaiter;", "owner", "", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Ljava/lang/Object;Lkotlinx/coroutines/CancellableContinuation;)V", "completeResumeLockWaiter", "token", "toString", "", "tryResumeLockWaiter", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class LockCont extends LockWaiter {
        public final CancellableContinuation<Unit> cont;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public LockCont(Object owner, CancellableContinuation<? super Unit> cancellableContinuation) {
            super(owner);
            Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
            this.cont = cancellableContinuation;
        }

        @Override // kotlinx.coroutines.sync.MutexImpl.LockWaiter
        public Object tryResumeLockWaiter() {
            return CancellableContinuation.DefaultImpls.tryResume$default(this.cont, Unit.INSTANCE, null, 2, null);
        }

        @Override // kotlinx.coroutines.sync.MutexImpl.LockWaiter
        public void completeResumeLockWaiter(Object token) {
            Intrinsics.checkParameterIsNotNull(token, "token");
            this.cont.completeResume(token);
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "LockCont[" + this.owner + ", " + this.cont + ']';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Mutex.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002BL\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b\u0012\"\u0010\t\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00040\nø\u0001\u0000¢\u0006\u0002\u0010\fJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\n\u0010\u0013\u001a\u0004\u0018\u00010\u0004H\u0016R1\u0010\t\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00040\n8\u0006X\u0087\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\rR\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$LockSelect;", "R", "Lkotlinx/coroutines/sync/MutexImpl$LockWaiter;", "owner", "", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/Object;Lkotlinx/coroutines/sync/Mutex;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "completeResumeLockWaiter", "", "token", "toString", "", "tryResumeLockWaiter", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class LockSelect<R> extends LockWaiter {
        public final Function2<Mutex, Continuation<? super R>, Object> block;
        public final Mutex mutex;
        public final SelectInstance<R> select;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public LockSelect(Object owner, Mutex mutex, SelectInstance<? super R> selectInstance, Function2<? super Mutex, ? super Continuation<? super R>, ? extends Object> function2) {
            super(owner);
            Intrinsics.checkParameterIsNotNull(mutex, "mutex");
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function2, "block");
            this.mutex = mutex;
            this.select = selectInstance;
            this.block = function2;
        }

        @Override // kotlinx.coroutines.sync.MutexImpl.LockWaiter
        public Object tryResumeLockWaiter() {
            if (this.select.trySelect(null)) {
                return MutexKt.SELECT_SUCCESS;
            }
            return null;
        }

        @Override // kotlinx.coroutines.sync.MutexImpl.LockWaiter
        public void completeResumeLockWaiter(Object token) {
            Intrinsics.checkParameterIsNotNull(token, "token");
            if (token == MutexKt.SELECT_SUCCESS) {
                ContinuationKt.startCoroutine(this.block, this.mutex, this.select.getCompletion());
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "LockSelect[" + this.owner + ", " + this.mutex + ", " + this.select + ']';
        }
    }

    /* compiled from: Mutex.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0016R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$UnlockOp;", "Lkotlinx/coroutines/internal/OpDescriptor;", "queue", "Lkotlinx/coroutines/sync/MutexImpl$LockedQueue;", "(Lkotlinx/coroutines/sync/MutexImpl$LockedQueue;)V", "perform", "", "affected", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    private static final class UnlockOp extends OpDescriptor {
        public final LockedQueue queue;

        public UnlockOp(LockedQueue queue) {
            Intrinsics.checkParameterIsNotNull(queue, "queue");
            this.queue = queue;
        }

        @Override // kotlinx.coroutines.internal.OpDescriptor
        public Object perform(Object affected) {
            Object update = this.queue.isEmpty() ? MutexKt.EmptyUnlocked : this.queue;
            if (affected != null) {
                MutexImpl._state$FU.compareAndSet((MutexImpl) affected, this, update);
                if (((MutexImpl) affected)._state == this.queue) {
                    return MutexKt.UNLOCK_FAIL;
                }
                return null;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.sync.MutexImpl");
        }
    }
}
