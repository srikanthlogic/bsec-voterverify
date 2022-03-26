package kotlinx.coroutines.sync;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: Mutex.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0010\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r\u001a5\u0010\u000e\u001a\u0002H\u000f\"\u0004\b\u0000\u0010\u000f*\u00020\u000b2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0013H\u0086Hø\u0001\u0000¢\u0006\u0002\u0010\u0014\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0015"}, d2 = {"ENQUEUE_FAIL", "Lkotlinx/coroutines/internal/Symbol;", "EmptyLocked", "Lkotlinx/coroutines/sync/Empty;", "EmptyUnlocked", "LOCKED", "LOCK_FAIL", "SELECT_SUCCESS", "UNLOCKED", "UNLOCK_FAIL", "Mutex", "Lkotlinx/coroutines/sync/Mutex;", "locked", "", "withLock", ExifInterface.GPS_DIRECTION_TRUE, "owner", "", "action", "Lkotlin/Function0;", "(Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class MutexKt {
    private static final Symbol LOCK_FAIL = new Symbol("LOCK_FAIL");
    private static final Symbol ENQUEUE_FAIL = new Symbol("ENQUEUE_FAIL");
    private static final Symbol UNLOCK_FAIL = new Symbol("UNLOCK_FAIL");
    private static final Symbol SELECT_SUCCESS = new Symbol("SELECT_SUCCESS");
    private static final Symbol LOCKED = new Symbol("LOCKED");
    private static final Symbol UNLOCKED = new Symbol("UNLOCKED");
    private static final Empty EmptyLocked = new Empty(LOCKED);
    private static final Empty EmptyUnlocked = new Empty(UNLOCKED);

    public static final /* synthetic */ Symbol access$getENQUEUE_FAIL$p() {
        return ENQUEUE_FAIL;
    }

    public static final /* synthetic */ Empty access$getEmptyLocked$p() {
        return EmptyLocked;
    }

    public static final /* synthetic */ Empty access$getEmptyUnlocked$p() {
        return EmptyUnlocked;
    }

    public static final /* synthetic */ Symbol access$getLOCKED$p() {
        return LOCKED;
    }

    public static final /* synthetic */ Symbol access$getLOCK_FAIL$p() {
        return LOCK_FAIL;
    }

    public static final /* synthetic */ Symbol access$getSELECT_SUCCESS$p() {
        return SELECT_SUCCESS;
    }

    public static final /* synthetic */ Symbol access$getUNLOCKED$p() {
        return UNLOCKED;
    }

    public static final /* synthetic */ Symbol access$getUNLOCK_FAIL$p() {
        return UNLOCK_FAIL;
    }

    public static /* synthetic */ Mutex Mutex$default(boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return Mutex(z);
    }

    public static final Mutex Mutex(boolean locked) {
        return new MutexImpl(locked);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0026  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0048  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final <T> Object withLock(Mutex $receiver, Object owner, Function0<? extends T> function0, Continuation<? super T> continuation) {
        MutexKt$withLock$1 mutexKt$withLock$1;
        int i;
        try {
            if (continuation instanceof MutexKt$withLock$1) {
                mutexKt$withLock$1 = (MutexKt$withLock$1) continuation;
                if ((mutexKt$withLock$1.label & Integer.MIN_VALUE) != 0) {
                    mutexKt$withLock$1.label -= Integer.MIN_VALUE;
                    Object obj = mutexKt$withLock$1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    i = mutexKt$withLock$1.label;
                    if (i == 0) {
                        if (i == 1) {
                            function0 = (Function0) mutexKt$withLock$1.L$2;
                            owner = mutexKt$withLock$1.L$1;
                            $receiver = (Mutex) mutexKt$withLock$1.L$0;
                            if (obj instanceof Result.Failure) {
                                throw ((Result.Failure) obj).exception;
                            }
                        } else {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    } else if (!(obj instanceof Result.Failure)) {
                        mutexKt$withLock$1.L$0 = $receiver;
                        mutexKt$withLock$1.L$1 = owner;
                        mutexKt$withLock$1.L$2 = function0;
                        mutexKt$withLock$1.label = 1;
                        if ($receiver.lock(owner, mutexKt$withLock$1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        throw ((Result.Failure) obj).exception;
                    }
                    return function0.invoke();
                }
            }
            return function0.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            $receiver.unlock(owner);
            InlineMarker.finallyEnd(1);
        }
        mutexKt$withLock$1 = new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.sync.MutexKt$withLock$1
            Object L$0;
            Object L$1;
            Object L$2;
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj2) {
                this.result = obj2;
                this.label |= Integer.MIN_VALUE;
                return MutexKt.withLock(null, null, null, this);
            }
        };
        Object obj2 = mutexKt$withLock$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = mutexKt$withLock$1.label;
        if (i == 0) {
        }
    }

    public static /* synthetic */ Object withLock$default(Mutex $receiver, Object owner, Function0 action, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            owner = null;
        }
        InlineMarker.mark(0);
        $receiver.lock(owner, continuation);
        InlineMarker.mark(2);
        InlineMarker.mark(1);
        try {
            return action.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            $receiver.unlock(owner);
            InlineMarker.finallyEnd(1);
        }
    }

    private static final Object withLock$$forInline(Mutex $receiver, Object owner, Function0 action, Continuation continuation) {
        InlineMarker.mark(0);
        $receiver.lock(owner, continuation);
        InlineMarker.mark(2);
        InlineMarker.mark(1);
        try {
            return action.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            $receiver.unlock(owner);
            InlineMarker.finallyEnd(1);
        }
    }
}
