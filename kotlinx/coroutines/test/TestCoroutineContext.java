package kotlinx.coroutines.test;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineExceptionHandler;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.Delay;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.EventLoop;
import kotlinx.coroutines.internal.ThreadSafeHeap;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
/* compiled from: TestCoroutineContext.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001:\u0001<B\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u0018\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\u0019J\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\u0019J$\u0010\u001d\u001a\u00020\u001b2\b\b\u0002\u0010\u001e\u001a\u00020\u00032\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020!0 J$\u0010\"\u001a\u00020\u001b2\b\b\u0002\u0010\u001e\u001a\u00020\u00032\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020!0 J*\u0010#\u001a\u00020\u001b2\b\b\u0002\u0010\u001e\u001a\u00020\u00032\u0018\u0010\u001f\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f\u0012\u0004\u0012\u00020!0 J$\u0010$\u001a\u00020\u001b2\b\b\u0002\u0010\u001e\u001a\u00020\u00032\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020!0 J\u0006\u0010%\u001a\u00020\u001bJ5\u0010&\u001a\u0002H'\"\u0004\b\u0000\u0010'2\u0006\u0010(\u001a\u0002H'2\u0018\u0010)\u001a\u0014\u0012\u0004\u0012\u0002H'\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u0002H'0*H\u0016¢\u0006\u0002\u0010,J(\u0010-\u001a\u0004\u0018\u0001H.\"\b\b\u0000\u0010.*\u00020+2\f\u0010/\u001a\b\u0012\u0004\u0012\u0002H.00H\u0096\u0002¢\u0006\u0002\u00101J\u0014\u00102\u001a\u00020\u00012\n\u0010/\u001a\u0006\u0012\u0002\b\u000300H\u0016J\u0010\u00103\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\u0019J\u0014\u00104\u001a\u00020\u001b2\n\u00105\u001a\u000606j\u0002`7H\u0002J\u001c\u00108\u001a\u00020\u00122\n\u00105\u001a\u000606j\u0002`72\u0006\u0010\u0017\u001a\u00020\u0006H\u0002J\b\u00109\u001a\u00020\u0006H\u0002J\b\u0010:\u001a\u00020\u0003H\u0016J\u0006\u0010;\u001a\u00020\u001bJ\u0010\u0010;\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0006H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00060\bR\u00020\u0000X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\r0\u0015X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006="}, d2 = {"Lkotlinx/coroutines/test/TestCoroutineContext;", "Lkotlin/coroutines/CoroutineContext;", AppMeasurementSdk.ConditionalUserProperty.NAME, "", "(Ljava/lang/String;)V", "counter", "", "ctxDispatcher", "Lkotlinx/coroutines/test/TestCoroutineContext$Dispatcher;", "ctxHandler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "exceptions", "", "", "getExceptions", "()Ljava/util/List;", "queue", "Lkotlinx/coroutines/internal/ThreadSafeHeap;", "Lkotlinx/coroutines/test/TimedRunnable;", "time", "uncaughtExceptions", "", "advanceTimeBy", "delayTime", "unit", "Ljava/util/concurrent/TimeUnit;", "advanceTimeTo", "", "targetTime", "assertAllUnhandledExceptions", "message", "predicate", "Lkotlin/Function1;", "", "assertAnyUnhandledException", "assertExceptions", "assertUnhandledException", "cancelAllActions", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "Lkotlin/coroutines/CoroutineContext$Element;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", ExifInterface.LONGITUDE_EAST, "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusKey", "now", "post", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "postDelayed", "processNextEvent", "toString", "triggerActions", "Dispatcher", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class TestCoroutineContext implements CoroutineContext {
    private long counter;
    private final Dispatcher ctxDispatcher;
    private final CoroutineExceptionHandler ctxHandler;
    private final String name;
    private final ThreadSafeHeap<TimedRunnable> queue;
    private long time;
    private final List<Throwable> uncaughtExceptions;

    public TestCoroutineContext() {
        this(null, 1, null);
    }

    public TestCoroutineContext(String name) {
        this.name = name;
        this.uncaughtExceptions = new ArrayList();
        this.ctxDispatcher = new Dispatcher();
        this.ctxHandler = new CoroutineExceptionHandler(CoroutineExceptionHandler.Key, this) { // from class: kotlinx.coroutines.test.TestCoroutineContext$$special$$inlined$CoroutineExceptionHandler$1
            final /* synthetic */ TestCoroutineContext this$0;

            {
                this.this$0 = r2;
            }

            @Override // kotlinx.coroutines.CoroutineExceptionHandler
            public void handleException(CoroutineContext context, Throwable exception) {
                Intrinsics.checkParameterIsNotNull(context, "context");
                Intrinsics.checkParameterIsNotNull(exception, "exception");
                this.this$0.uncaughtExceptions.add(exception);
            }
        };
        this.queue = new ThreadSafeHeap<>();
    }

    public /* synthetic */ TestCoroutineContext(String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public CoroutineContext plus(CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return CoroutineContext.DefaultImpls.plus(this, context);
    }

    public final List<Throwable> getExceptions() {
        return this.uncaughtExceptions;
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return (R) function2.invoke((Object) function2.invoke(r, this.ctxDispatcher), this.ctxHandler);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        if (key == ContinuationInterceptor.Key) {
            Dispatcher dispatcher = this.ctxDispatcher;
            if (dispatcher != null) {
                return dispatcher;
            }
            throw new TypeCastException("null cannot be cast to non-null type E");
        } else if (key != CoroutineExceptionHandler.Key) {
            return null;
        } else {
            CoroutineExceptionHandler coroutineExceptionHandler = this.ctxHandler;
            if (coroutineExceptionHandler != null) {
                return coroutineExceptionHandler;
            }
            throw new TypeCastException("null cannot be cast to non-null type E");
        }
    }

    @Override // kotlin.coroutines.CoroutineContext
    public CoroutineContext minusKey(CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        if (key == ContinuationInterceptor.Key) {
            return this.ctxHandler;
        }
        if (key == CoroutineExceptionHandler.Key) {
            return this.ctxDispatcher;
        }
        return this;
    }

    public static /* synthetic */ long now$default(TestCoroutineContext testCoroutineContext, TimeUnit timeUnit, int i, Object obj) {
        if ((i & 1) != 0) {
            timeUnit = TimeUnit.MILLISECONDS;
        }
        return testCoroutineContext.now(timeUnit);
    }

    public final long now(TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "unit");
        return unit.convert(this.time, TimeUnit.NANOSECONDS);
    }

    public static /* synthetic */ long advanceTimeBy$default(TestCoroutineContext testCoroutineContext, long j, TimeUnit timeUnit, int i, Object obj) {
        if ((i & 2) != 0) {
            timeUnit = TimeUnit.MILLISECONDS;
        }
        return testCoroutineContext.advanceTimeBy(j, timeUnit);
    }

    public final long advanceTimeBy(long delayTime, TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "unit");
        long oldTime = this.time;
        advanceTimeTo(unit.toNanos(delayTime) + oldTime, TimeUnit.NANOSECONDS);
        return unit.convert(this.time - oldTime, TimeUnit.NANOSECONDS);
    }

    public static /* synthetic */ void advanceTimeTo$default(TestCoroutineContext testCoroutineContext, long j, TimeUnit timeUnit, int i, Object obj) {
        if ((i & 2) != 0) {
            timeUnit = TimeUnit.MILLISECONDS;
        }
        testCoroutineContext.advanceTimeTo(j, timeUnit);
    }

    public final void advanceTimeTo(long targetTime, TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "unit");
        long nanoTime = unit.toNanos(targetTime);
        triggerActions(nanoTime);
        if (nanoTime > this.time) {
            this.time = nanoTime;
        }
    }

    public final void triggerActions() {
        triggerActions(this.time);
    }

    public final void cancelAllActions() {
        if (!this.queue.isEmpty()) {
            this.queue.clear();
        }
    }

    public static /* synthetic */ void assertUnhandledException$default(TestCoroutineContext testCoroutineContext, String str, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "";
        }
        testCoroutineContext.assertUnhandledException(str, function1);
    }

    public final void assertUnhandledException(String message, Function1<? super Throwable, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        if (this.uncaughtExceptions.size() != 1 || !function1.invoke(this.uncaughtExceptions.get(0)).booleanValue()) {
            throw new AssertionError(message);
        }
        this.uncaughtExceptions.clear();
    }

    public static /* synthetic */ void assertAllUnhandledExceptions$default(TestCoroutineContext testCoroutineContext, String str, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "";
        }
        testCoroutineContext.assertAllUnhandledExceptions(str, function1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void assertAllUnhandledExceptions(String message, Function1<? super Throwable, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Iterable $receiver$iv = this.uncaughtExceptions;
        boolean z = true;
        if (!($receiver$iv instanceof Collection) || !((Collection) $receiver$iv).isEmpty()) {
            Iterator<T> it = $receiver$iv.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (!function1.invoke(it.next()).booleanValue()) {
                        z = false;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (z) {
            this.uncaughtExceptions.clear();
            return;
        }
        throw new AssertionError(message);
    }

    public static /* synthetic */ void assertAnyUnhandledException$default(TestCoroutineContext testCoroutineContext, String str, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "";
        }
        testCoroutineContext.assertAnyUnhandledException(str, function1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void assertAnyUnhandledException(String message, Function1<? super Throwable, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Iterable $receiver$iv = this.uncaughtExceptions;
        boolean z = false;
        if (!($receiver$iv instanceof Collection) || !((Collection) $receiver$iv).isEmpty()) {
            Iterator<T> it = $receiver$iv.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (function1.invoke(it.next()).booleanValue()) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (z) {
            this.uncaughtExceptions.clear();
            return;
        }
        throw new AssertionError(message);
    }

    public static /* synthetic */ void assertExceptions$default(TestCoroutineContext testCoroutineContext, String str, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "";
        }
        testCoroutineContext.assertExceptions(str, function1);
    }

    public final void assertExceptions(String message, Function1<? super List<? extends Throwable>, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        if (function1.invoke(this.uncaughtExceptions).booleanValue()) {
            this.uncaughtExceptions.clear();
            return;
        }
        throw new AssertionError(message);
    }

    public final void post(Runnable block) {
        ThreadSafeHeap<TimedRunnable> threadSafeHeap = this.queue;
        long j = this.counter;
        this.counter = 1 + j;
        threadSafeHeap.addLast(new TimedRunnable(block, j, 0, 4, null));
    }

    public final TimedRunnable postDelayed(Runnable block, long delayTime) {
        long j = this.counter;
        this.counter = 1 + j;
        TimedRunnable it = new TimedRunnable(block, j, TimeUnit.MILLISECONDS.toNanos(delayTime) + this.time);
        this.queue.addLast(it);
        return it;
    }

    public final long processNextEvent() {
        TimedRunnable current = this.queue.peek();
        if (current != null) {
            triggerActions(current.time);
        }
        return this.queue.isEmpty() ? Long.MAX_VALUE : 0;
    }

    private final void triggerActions(long targetTime) {
        TimedRunnable timedRunnable;
        while (true) {
            ThreadSafeHeap this_$iv = this.queue;
            synchronized (this_$iv) {
                ThreadSafeHeapNode first$iv = this_$iv.firstImpl();
                timedRunnable = null;
                if (first$iv != null) {
                    if (((TimedRunnable) first$iv).time <= targetTime) {
                        timedRunnable = this_$iv.removeAtImpl(0);
                    }
                }
            }
            TimedRunnable current = timedRunnable;
            if (current != null) {
                if (current.time != 0) {
                    this.time = current.time;
                }
                current.run();
            } else {
                return;
            }
        }
    }

    public String toString() {
        String str = this.name;
        if (str != null) {
            return str;
        }
        return "TestCoroutineContext@" + DebugKt.getHexAddress(this);
    }

    /* compiled from: TestCoroutineContext.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u00060\nj\u0002`\u000bH\u0016J\u001c\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\n\u0010\t\u001a\u00060\nj\u0002`\u000bH\u0016J\b\u0010\u0010\u001a\u00020\u000fH\u0016J\u001e\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016¨\u0006\u0016"}, d2 = {"Lkotlinx/coroutines/test/TestCoroutineContext$Dispatcher;", "Lkotlinx/coroutines/CoroutineDispatcher;", "Lkotlinx/coroutines/Delay;", "Lkotlinx/coroutines/EventLoop;", "(Lkotlinx/coroutines/test/TestCoroutineContext;)V", "dispatch", "", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "", "processNextEvent", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public final class Dispatcher extends CoroutineDispatcher implements Delay, EventLoop {
        public Dispatcher() {
            TestCoroutineContext.this = $outer;
        }

        @Override // kotlinx.coroutines.Delay
        public Object delay(long time, Continuation<? super Unit> continuation) {
            return Delay.DefaultImpls.delay(this, time, continuation);
        }

        @Override // kotlinx.coroutines.CoroutineDispatcher
        public void dispatch(CoroutineContext context, Runnable block) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(block, "block");
            TestCoroutineContext.this.post(block);
        }

        @Override // kotlinx.coroutines.Delay
        public void scheduleResumeAfterDelay(long timeMillis, CancellableContinuation<? super Unit> cancellableContinuation) {
            Intrinsics.checkParameterIsNotNull(cancellableContinuation, "continuation");
            TestCoroutineContext.this.postDelayed(new TestCoroutineContext$Dispatcher$scheduleResumeAfterDelay$$inlined$Runnable$1(this, cancellableContinuation), timeMillis);
        }

        @Override // kotlinx.coroutines.Delay
        public DisposableHandle invokeOnTimeout(long timeMillis, Runnable block) {
            Intrinsics.checkParameterIsNotNull(block, "block");
            return new TestCoroutineContext$Dispatcher$invokeOnTimeout$1(this, TestCoroutineContext.this.postDelayed(block, timeMillis));
        }

        @Override // kotlinx.coroutines.EventLoop
        public long processNextEvent() {
            return TestCoroutineContext.this.processNextEvent();
        }

        @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
        public String toString() {
            return "Dispatcher(" + TestCoroutineContext.this + ')';
        }
    }
}
