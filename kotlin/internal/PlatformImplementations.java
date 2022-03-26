package kotlin.internal;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.lang.reflect.Method;
import java.util.regex.MatchResult;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;
import kotlin.text.MatchGroup;
/* compiled from: PlatformImplementations.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u0010B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016¨\u0006\u0011"}, d2 = {"Lkotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", AppMeasurementSdk.ConditionalUserProperty.NAME, "", "ReflectAddSuppressedMethod", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public class PlatformImplementations {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PlatformImplementations.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlin/internal/PlatformImplementations$ReflectAddSuppressedMethod;", "", "()V", FirebaseAnalytics.Param.METHOD, "Ljava/lang/reflect/Method;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class ReflectAddSuppressedMethod {
        public static final ReflectAddSuppressedMethod INSTANCE = new ReflectAddSuppressedMethod();
        public static final Method method;

        /* JADX WARN: Removed duplicated region for block: B:11:0x0048 A[LOOP:0: B:3:0x0016->B:11:0x0048, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:15:0x004c A[EDGE_INSN: B:15:0x004c->B:13:0x004c ?: BREAK  , SYNTHETIC] */
        static {
            Method it;
            boolean z;
            Method[] methods = Throwable.class.getMethods();
            Intrinsics.checkExpressionValueIsNotNull(methods, "throwableClass.methods");
            int length = methods.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    it = null;
                    break;
                }
                it = methods[i];
                Intrinsics.checkExpressionValueIsNotNull(it, "it");
                if (Intrinsics.areEqual(it.getName(), "addSuppressed")) {
                    Class<?>[] parameterTypes = it.getParameterTypes();
                    Intrinsics.checkExpressionValueIsNotNull(parameterTypes, "it.parameterTypes");
                    if (Intrinsics.areEqual((Class) ArraysKt.singleOrNull(parameterTypes), Throwable.class)) {
                        z = true;
                        if (!z) {
                            break;
                        }
                        i++;
                    }
                }
                z = false;
                if (!z) {
                }
            }
            method = it;
        }

        private ReflectAddSuppressedMethod() {
        }
    }

    public void addSuppressed(Throwable cause, Throwable exception) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Method method = ReflectAddSuppressedMethod.method;
        if (method != null) {
            method.invoke(cause, exception);
        }
    }

    public MatchGroup getMatchResultNamedGroup(MatchResult matchResult, String name) {
        Intrinsics.checkParameterIsNotNull(matchResult, "matchResult");
        Intrinsics.checkParameterIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        throw new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }

    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }
}
