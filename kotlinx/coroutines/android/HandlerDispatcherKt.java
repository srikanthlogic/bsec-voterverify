package kotlinx.coroutines.android;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.lang.reflect.Constructor;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
/* compiled from: HandlerDispatcher.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\u0001H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\f\u001a\u001e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0010H\u0002\u001a\u0016\u0010\u0011\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0010H\u0002\u001a\u001d\u0010\u0012\u001a\u00020\u0003*\u00020\b2\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0007¢\u0006\u0002\b\u0015\u001a\u0014\u0010\u0016\u001a\u00020\b*\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0001\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u0010\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000\"\u0014\u0010\u0007\u001a\u00020\bX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001a"}, d2 = {"MAX_DELAY", "", "Main", "Lkotlinx/coroutines/android/HandlerDispatcher;", "MainDispatcher", "choreographer", "Landroid/view/Choreographer;", "mainHandler", "Landroid/os/Handler;", "getMainHandler", "()Landroid/os/Handler;", "awaitFrame", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postFrameCallback", "", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "updateChoreographerAndPostFrameCallback", "asCoroutineDispatcher", AppMeasurementSdk.ConditionalUserProperty.NAME, "", "from", "asHandler", "Landroid/os/Looper;", "async", "", "kotlinx-coroutines-android"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class HandlerDispatcherKt {
    private static final long MAX_DELAY;
    public static final HandlerDispatcher Main;
    private static final HandlerDispatcher MainDispatcher;
    private static volatile Choreographer choreographer;
    private static final Handler mainHandler;

    public static final HandlerDispatcher from(Handler handler) {
        return from$default(handler, null, 1, null);
    }

    public static /* synthetic */ HandlerDispatcher from$default(Handler handler, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        return from(handler, str);
    }

    public static final HandlerDispatcher from(Handler $receiver, String name) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return new HandlerContext($receiver, name);
    }

    static {
        Looper mainLooper = Looper.getMainLooper();
        Intrinsics.checkExpressionValueIsNotNull(mainLooper, "Looper.getMainLooper()");
        mainHandler = asHandler(mainLooper, true);
        Main = new HandlerContext(mainHandler, "Main");
        MainDispatcher = Main;
    }

    public static final Handler getMainHandler() {
        return mainHandler;
    }

    public static final Handler asHandler(Looper $receiver, boolean async) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (!async || Build.VERSION.SDK_INT < 16) {
            return new Handler($receiver);
        }
        if (Build.VERSION.SDK_INT >= 28) {
            Object invoke = Handler.class.getDeclaredMethod("createAsync", Looper.class).invoke(null, $receiver);
            if (invoke != null) {
                return (Handler) invoke;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.os.Handler");
        }
        try {
            Constructor constructor = Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, Boolean.TYPE);
            Intrinsics.checkExpressionValueIsNotNull(constructor, "Handler::class.java.getD…:class.javaPrimitiveType)");
            Object newInstance = constructor.newInstance($receiver, null, true);
            Intrinsics.checkExpressionValueIsNotNull(newInstance, "constructor.newInstance(this, null, true)");
            return (Handler) newInstance;
        } catch (NoSuchMethodException e) {
            return new Handler($receiver);
        }
    }

    public static final Object awaitFrame(Continuation<? super Long> continuation) {
        Choreographer choreographer2 = choreographer;
        if (choreographer2 != null) {
            CancellableContinuationImpl cancellable$iv = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
            cancellable$iv.initCancellability();
            postFrameCallback(choreographer2, cancellable$iv);
            Object result = cancellable$iv.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return result;
        }
        CancellableContinuationImpl cancellable$iv2 = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv2.initCancellability();
        getMainHandler().post(new Runnable() { // from class: kotlinx.coroutines.android.HandlerDispatcherKt$awaitFrame$3$1
            @Override // java.lang.Runnable
            public final void run() {
                HandlerDispatcherKt.updateChoreographerAndPostFrameCallback(CancellableContinuation.this);
            }
        });
        Object result2 = cancellable$iv2.getResult();
        if (result2 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result2;
    }

    public static final void updateChoreographerAndPostFrameCallback(CancellableContinuation<? super Long> cancellableContinuation) {
        Choreographer it = choreographer;
        if (it == null) {
            it = Choreographer.getInstance();
            if (it == null) {
                Intrinsics.throwNpe();
            }
            choreographer = it;
        }
        postFrameCallback(it, cancellableContinuation);
    }

    public static final void postFrameCallback(Choreographer choreographer2, CancellableContinuation<? super Long> cancellableContinuation) {
        choreographer2.postFrameCallback(new Choreographer.FrameCallback() { // from class: kotlinx.coroutines.android.HandlerDispatcherKt$postFrameCallback$1
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long nanos) {
                CancellableContinuation.this.resumeUndispatched(HandlerDispatcherKt.Main, Long.valueOf(nanos));
            }
        });
    }
}
