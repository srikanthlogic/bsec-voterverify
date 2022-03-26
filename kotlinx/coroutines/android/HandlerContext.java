package kotlinx.coroutines.android;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.Delay;
import kotlinx.coroutines.DisposableHandle;
/* compiled from: HandlerDispatcher.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u001b\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007B!\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u001c\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015H\u0016J\u0013\u0010\u0016\u001a\u00020\t2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0096\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u001c\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015H\u0016J\u0010\u0010\u001f\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u001e\u0010 \u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u001e2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00100\"H\u0016J\b\u0010#\u001a\u00020\u0006H\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u0000X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lkotlinx/coroutines/android/HandlerContext;", "Lkotlinx/coroutines/android/HandlerDispatcher;", "Lkotlinx/coroutines/Delay;", "handler", "Landroid/os/Handler;", AppMeasurementSdk.ConditionalUserProperty.NAME, "", "(Landroid/os/Handler;Ljava/lang/String;)V", "invokeImmediately", "", "(Landroid/os/Handler;Ljava/lang/String;Z)V", "_immediate", "immediate", "getImmediate", "()Lkotlinx/coroutines/android/HandlerContext;", "dispatch", "", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "equals", "other", "", "hashCode", "", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "", "isDispatchNeeded", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "toString", "kotlinx-coroutines-android"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class HandlerContext extends HandlerDispatcher implements Delay {
    private volatile HandlerContext _immediate;
    private final Handler handler;
    private final HandlerContext immediate;
    private final boolean invokeImmediately;
    private final String name;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private HandlerContext(Handler handler, String name, boolean invokeImmediately) {
        super(null);
        HandlerContext handlerContext = null;
        this.handler = handler;
        this.name = name;
        this.invokeImmediately = invokeImmediately;
        this._immediate = this.invokeImmediately ? this : handlerContext;
        HandlerContext it = this._immediate;
        if (it == null) {
            it = new HandlerContext(this.handler, this.name, true);
            this._immediate = it;
        }
        this.immediate = it;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public HandlerContext(Handler handler, String name) {
        this(handler, name, false);
        Intrinsics.checkParameterIsNotNull(handler, "handler");
    }

    public /* synthetic */ HandlerContext(Handler handler, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(handler, (i & 2) != 0 ? null : str);
    }

    @Override // kotlinx.coroutines.android.HandlerDispatcher, kotlinx.coroutines.MainCoroutineDispatcher
    public HandlerContext getImmediate() {
        return this.immediate;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public boolean isDispatchNeeded(CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return !this.invokeImmediately || (Intrinsics.areEqual(Looper.myLooper(), this.handler.getLooper()) ^ true);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext context, Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        this.handler.post(block);
    }

    @Override // kotlinx.coroutines.Delay
    public void scheduleResumeAfterDelay(long timeMillis, CancellableContinuation<? super Unit> cancellableContinuation) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "continuation");
        Runnable block = new Runnable(cancellableContinuation) { // from class: kotlinx.coroutines.android.HandlerContext$scheduleResumeAfterDelay$$inlined$Runnable$1
            final /* synthetic */ CancellableContinuation $continuation$inlined;

            {
                this.$continuation$inlined = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.$continuation$inlined.resumeUndispatched(HandlerContext.this, Unit.INSTANCE);
            }
        };
        this.handler.postDelayed(block, RangesKt.coerceAtMost(timeMillis, 4611686018427387903L));
        cancellableContinuation.invokeOnCancellation(new Function1<Throwable, Unit>(block) { // from class: kotlinx.coroutines.android.HandlerContext$scheduleResumeAfterDelay$1
            final /* synthetic */ Runnable $block;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$block = r2;
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke2(th);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke  reason: avoid collision after fix types in other method */
            public final void invoke2(Throwable it) {
                HandlerContext.this.handler.removeCallbacks(this.$block);
            }
        });
    }

    @Override // kotlinx.coroutines.android.HandlerDispatcher, kotlinx.coroutines.Delay
    public DisposableHandle invokeOnTimeout(long timeMillis, Runnable block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        this.handler.postDelayed(block, RangesKt.coerceAtMost(timeMillis, 4611686018427387903L));
        return new DisposableHandle(block) { // from class: kotlinx.coroutines.android.HandlerContext$invokeOnTimeout$1
            final /* synthetic */ Runnable $block;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$block = $captured_local_variable$1;
            }

            @Override // kotlinx.coroutines.DisposableHandle
            public void dispose() {
                HandlerContext.this.handler.removeCallbacks(this.$block);
            }
        };
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
    public String toString() {
        String str = this.name;
        if (str == null) {
            String handler = this.handler.toString();
            Intrinsics.checkExpressionValueIsNotNull(handler, "handler.toString()");
            return handler;
        } else if (!this.invokeImmediately) {
            return str;
        } else {
            return this.name + " [immediate]";
        }
    }

    public boolean equals(Object other) {
        return (other instanceof HandlerContext) && ((HandlerContext) other).handler == this.handler;
    }

    public int hashCode() {
        return System.identityHashCode(this.handler);
    }
}
