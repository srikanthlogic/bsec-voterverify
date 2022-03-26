package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.Metadata;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.SystemPropsKt;
import kotlinx.coroutines.internal.ThreadContextKt;
import kotlinx.coroutines.scheduling.DefaultScheduler;
/* compiled from: CoroutineContext.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\b\u0010\r\u001a\u00020\u000eH\u0000\u001a\b\u0010\u000f\u001a\u00020\u0010H\u0000\u001a4\u0010\u0011\u001a\u0002H\u0012\"\u0004\b\u0000\u0010\u00122\u0006\u0010\u0013\u001a\u00020\n2\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0017H\u0080\b¢\u0006\u0002\u0010\u0018\u001a\u0014\u0010\u0019\u001a\u00020\n*\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\nH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u0014\u0010\u0005\u001a\u00020\u0006X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u001a\u0010\t\u001a\u0004\u0018\u00010\u0001*\u00020\n8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u001b"}, d2 = {"COROUTINES_SCHEDULER_PROPERTY_NAME", "", "COROUTINE_ID", "Ljava/util/concurrent/atomic/AtomicLong;", "DEBUG_THREAD_NAME_SEPARATOR", "useCoroutinesScheduler", "", "getUseCoroutinesScheduler", "()Z", "coroutineName", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineName", "(Lkotlin/coroutines/CoroutineContext;)Ljava/lang/String;", "createDefaultDispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "resetCoroutineId", "", "withCoroutineContext", ExifInterface.GPS_DIRECTION_TRUE, "context", "countOrElement", "", "block", "Lkotlin/Function0;", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "newCoroutineContext", "Lkotlinx/coroutines/CoroutineScope;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class CoroutineContextKt {
    public static final String COROUTINES_SCHEDULER_PROPERTY_NAME;
    private static final AtomicLong COROUTINE_ID = new AtomicLong();
    private static final String DEBUG_THREAD_NAME_SEPARATOR;
    private static final boolean useCoroutinesScheduler;

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0027, code lost:
        if (r0.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF) != false) goto L_0x003d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0030, code lost:
        if (r0.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON) != false) goto L_0x003c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0039, code lost:
        if (r0.equals("") != false) goto L_0x003c;
     */
    static {
        String value = SystemPropsKt.systemProp(COROUTINES_SCHEDULER_PROPERTY_NAME);
        boolean z = false;
        if (value != null) {
            int hashCode = value.hashCode();
            if (hashCode != 0) {
                if (hashCode != 3551) {
                    if (hashCode == 109935) {
                    }
                }
                throw new IllegalStateException(("System property 'kotlinx.coroutines.scheduler' has unrecognized value '" + value + '\'').toString());
            }
            useCoroutinesScheduler = z;
        }
        z = true;
        useCoroutinesScheduler = z;
    }

    public static final void resetCoroutineId() {
        COROUTINE_ID.set(0);
    }

    public static final boolean getUseCoroutinesScheduler() {
        return useCoroutinesScheduler;
    }

    public static final CoroutineDispatcher createDefaultDispatcher() {
        return useCoroutinesScheduler ? DefaultScheduler.INSTANCE : CommonPool.INSTANCE;
    }

    public static final CoroutineContext newCoroutineContext(CoroutineScope $receiver, CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(context, "context");
        CoroutineContext combined = $receiver.getCoroutineContext().plus(context);
        CoroutineContext debug = DebugKt.getDEBUG() ? combined.plus(new CoroutineId(COROUTINE_ID.incrementAndGet())) : combined;
        return (combined == Dispatchers.getDefault() || combined.get(ContinuationInterceptor.Key) != null) ? debug : debug.plus(Dispatchers.getDefault());
    }

    public static final <T> T withCoroutineContext(CoroutineContext context, Object countOrElement, Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(function0, "block");
        Object oldValue = ThreadContextKt.updateThreadContext(context, countOrElement);
        try {
            return (T) function0.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            ThreadContextKt.restoreThreadContext(context, oldValue);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final String getCoroutineName(CoroutineContext $receiver) {
        CoroutineId coroutineId;
        String coroutineName;
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (!DebugKt.getDEBUG() || (coroutineId = (CoroutineId) $receiver.get(CoroutineId.Key)) == null) {
            return null;
        }
        CoroutineName coroutineName2 = (CoroutineName) $receiver.get(CoroutineName.Key);
        if (coroutineName2 == null || (coroutineName = coroutineName2.getName()) == null) {
            coroutineName = "coroutine";
        }
        return coroutineName + '#' + coroutineId.getId();
    }
}
