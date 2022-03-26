package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.SystemPropsKt;
/* compiled from: Debug.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u000f\u001a\u00020\u0005*\u0006\u0012\u0002\b\u00030\u0010H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000\"\u0018\u0010\t\u001a\u00020\u0005*\u00020\n8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f\"\u0018\u0010\r\u001a\u00020\u0005*\u00020\n8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f¨\u0006\u0011"}, d2 = {"DEBUG", "", "getDEBUG", "()Z", "DEBUG_PROPERTY_NAME", "", "DEBUG_PROPERTY_VALUE_AUTO", "DEBUG_PROPERTY_VALUE_OFF", "DEBUG_PROPERTY_VALUE_ON", "classSimpleName", "", "getClassSimpleName", "(Ljava/lang/Object;)Ljava/lang/String;", "hexAddress", "getHexAddress", "toDebugString", "Lkotlin/coroutines/Continuation;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class DebugKt {
    private static final boolean DEBUG;
    public static final String DEBUG_PROPERTY_NAME;
    public static final String DEBUG_PROPERTY_VALUE_AUTO;
    public static final String DEBUG_PROPERTY_VALUE_OFF;
    public static final String DEBUG_PROPERTY_VALUE_ON;

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:
        if (r1.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_AUTO) != false) goto L_0x0027;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x003e, code lost:
        if (r1.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON) != false) goto L_0x0049;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0047, code lost:
        if (r1.equals("") != false) goto L_0x0049;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0049, code lost:
        r2 = true;
     */
    static {
        boolean z;
        String value = SystemPropsKt.systemProp(DEBUG_PROPERTY_NAME);
        if (value != null) {
            int hashCode = value.hashCode();
            if (hashCode != 0) {
                if (hashCode != 3551) {
                    if (hashCode != 109935) {
                        if (hashCode == 3005871) {
                        }
                    } else if (value.equals(DEBUG_PROPERTY_VALUE_OFF)) {
                        z = false;
                    }
                }
                throw new IllegalStateException(("System property 'kotlinx.coroutines.debug' has unrecognized value '" + value + '\'').toString());
            }
            DEBUG = z;
        }
        z = CoroutineId.class.desiredAssertionStatus();
        DEBUG = z;
    }

    public static final boolean getDEBUG() {
        return DEBUG;
    }

    public static final String getHexAddress(Object $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        String hexString = Integer.toHexString(System.identityHashCode($receiver));
        Intrinsics.checkExpressionValueIsNotNull(hexString, "Integer.toHexString(System.identityHashCode(this))");
        return hexString;
    }

    public static final String toDebugString(Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        if (continuation instanceof DispatchedContinuation) {
            return continuation.toString();
        }
        return continuation + '@' + getHexAddress(continuation);
    }

    public static final String getClassSimpleName(Object $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        String simpleName = $receiver.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName");
        return simpleName;
    }
}
