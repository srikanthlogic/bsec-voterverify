package okio;

import androidx.exifinterface.media.ExifInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AsyncTimeout.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u0000 \u001b2\u00020\u0001:\u0002\u001b\u001cB\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0001J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u0004J\u0012\u0010\u000e\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0014J\u0010\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0007H\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u0014J\b\u0010\u0015\u001a\u00020\fH\u0014J\"\u0010\u0016\u001a\u0002H\u0017\"\u0004\b\u0000\u0010\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00170\u0019H\u0086\b¢\u0006\u0002\u0010\u001aR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lokio/AsyncTimeout;", "Lokio/Timeout;", "()V", "inQueue", "", "next", "timeoutAt", "", "access$newTimeoutException", "Ljava/io/IOException;", "cause", "enter", "", "exit", "newTimeoutException", "remainingNanos", "now", "sink", "Lokio/Sink;", FirebaseAnalytics.Param.SOURCE, "Lokio/Source;", "timedOut", "withTimeout", ExifInterface.GPS_DIRECTION_TRUE, "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "Companion", "Watchdog", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public class AsyncTimeout extends Timeout {
    public static final Companion Companion = new Companion(null);
    private static final long IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60);
    private static final long IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
    private static final int TIMEOUT_WRITE_SIZE;
    private static AsyncTimeout head;
    private boolean inQueue;
    private AsyncTimeout next;
    private long timeoutAt;

    public final void enter() {
        if (!this.inQueue) {
            long timeoutNanos = timeoutNanos();
            boolean hasDeadline = hasDeadline();
            if (timeoutNanos != 0 || hasDeadline) {
                this.inQueue = true;
                Companion.scheduleTimeout(this, timeoutNanos, hasDeadline);
                return;
            }
            return;
        }
        throw new IllegalStateException("Unbalanced enter/exit".toString());
    }

    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return Companion.cancelScheduledTimeout(this);
    }

    public final long remainingNanos(long now) {
        return this.timeoutAt - now;
    }

    protected void timedOut() {
    }

    public final Sink sink(Sink sink) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        return new Sink(sink) { // from class: okio.AsyncTimeout$sink$1
            final /* synthetic */ Sink $sink;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$sink = $captured_local_variable$1;
            }

            @Override // okio.Sink
            public void write(Buffer source, long byteCount) {
                long toWrite;
                Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
                Util.checkOffsetAndCount(source.size(), 0, byteCount);
                for (long remaining = byteCount; remaining > 0; remaining -= toWrite) {
                    toWrite = 0;
                    Segment s = source.head;
                    if (s == null) {
                        Intrinsics.throwNpe();
                    }
                    while (true) {
                        if (toWrite >= ((long) 65536)) {
                            break;
                        }
                        toWrite += (long) (s.limit - s.pos);
                        if (toWrite >= remaining) {
                            toWrite = remaining;
                            break;
                        }
                        Segment segment = s.next;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        s = segment;
                    }
                    AsyncTimeout this_$iv = AsyncTimeout.this;
                    this_$iv.enter();
                    try {
                        try {
                            this.$sink.write(source, toWrite);
                            Unit unit = Unit.INSTANCE;
                            if (this_$iv.exit()) {
                                throw this_$iv.access$newTimeoutException(null);
                            }
                        } catch (IOException e$iv) {
                            if (this_$iv.exit()) {
                                throw this_$iv.access$newTimeoutException(e$iv);
                            }
                            throw e$iv;
                        }
                    } catch (Throwable th) {
                        if (!this_$iv.exit() || 0 == 0) {
                            throw th;
                        }
                        throw this_$iv.access$newTimeoutException(null);
                    }
                }
            }

            @Override // okio.Sink, java.io.Flushable
            public void flush() {
                AsyncTimeout this_$iv;
                try {
                    this_$iv = AsyncTimeout.this;
                    this_$iv.enter();
                    try {
                        this.$sink.flush();
                        Unit unit = Unit.INSTANCE;
                        if (this_$iv.exit()) {
                            throw this_$iv.access$newTimeoutException(null);
                        }
                    } catch (IOException e$iv) {
                        if (this_$iv.exit()) {
                            throw this_$iv.access$newTimeoutException(e$iv);
                        }
                    }
                } catch (Throwable th) {
                    if (!this_$iv.exit() || 0 == 0) {
                        throw th;
                    }
                    throw this_$iv.access$newTimeoutException(null);
                }
            }

            @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                AsyncTimeout this_$iv;
                try {
                    this_$iv = AsyncTimeout.this;
                    this_$iv.enter();
                    try {
                        this.$sink.close();
                        Unit unit = Unit.INSTANCE;
                        if (this_$iv.exit()) {
                            throw this_$iv.access$newTimeoutException(null);
                        }
                    } catch (IOException e$iv) {
                        if (this_$iv.exit()) {
                            throw this_$iv.access$newTimeoutException(e$iv);
                        }
                    }
                } catch (Throwable th) {
                    if (!this_$iv.exit() || 0 == 0) {
                        throw th;
                    }
                    throw this_$iv.access$newTimeoutException(null);
                }
            }

            @Override // okio.Sink
            public AsyncTimeout timeout() {
                return AsyncTimeout.this;
            }

            @Override // java.lang.Object
            public String toString() {
                return "AsyncTimeout.sink(" + this.$sink + ')';
            }
        };
    }

    public final Source source(Source source) {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        return new Source(source) { // from class: okio.AsyncTimeout$source$1
            final /* synthetic */ Source $source;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$source = $captured_local_variable$1;
            }

            @Override // okio.Source
            public long read(Buffer sink, long byteCount) {
                Intrinsics.checkParameterIsNotNull(sink, "sink");
                AsyncTimeout this_$iv = AsyncTimeout.this;
                this_$iv.enter();
                try {
                    try {
                        long result$iv = this.$source.read(sink, byteCount);
                        if (!this_$iv.exit()) {
                            return result$iv;
                        }
                        throw this_$iv.access$newTimeoutException(null);
                    } catch (IOException e$iv) {
                        if (!this_$iv.exit()) {
                            throw e$iv;
                        }
                        throw this_$iv.access$newTimeoutException(e$iv);
                    }
                } catch (Throwable th) {
                    if (!this_$iv.exit() || 0 == 0) {
                        throw th;
                    }
                    throw this_$iv.access$newTimeoutException(null);
                }
            }

            @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                AsyncTimeout this_$iv;
                try {
                    this_$iv = AsyncTimeout.this;
                    this_$iv.enter();
                    try {
                        this.$source.close();
                        Unit unit = Unit.INSTANCE;
                        if (this_$iv.exit()) {
                            throw this_$iv.access$newTimeoutException(null);
                        }
                    } catch (IOException e$iv) {
                        if (this_$iv.exit()) {
                            throw this_$iv.access$newTimeoutException(e$iv);
                        }
                    }
                } catch (Throwable th) {
                    if (!this_$iv.exit() || 0 == 0) {
                        throw th;
                    }
                    throw this_$iv.access$newTimeoutException(null);
                }
            }

            @Override // okio.Source
            public AsyncTimeout timeout() {
                return AsyncTimeout.this;
            }

            @Override // java.lang.Object
            public String toString() {
                return "AsyncTimeout.source(" + this.$source + ')';
            }
        };
    }

    public final <T> T withTimeout(Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        enter();
        try {
            try {
                T t = (T) function0.invoke();
                InlineMarker.finallyStart(1);
                if (!exit()) {
                    InlineMarker.finallyEnd(1);
                    return t;
                }
                throw access$newTimeoutException(null);
            } catch (IOException e) {
                if (!exit()) {
                    throw e;
                }
                throw access$newTimeoutException(e);
            }
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            if (!exit() || 0 == 0) {
                InlineMarker.finallyEnd(1);
                throw th;
            }
            throw access$newTimeoutException(null);
        }
    }

    public final IOException access$newTimeoutException(IOException cause) {
        return newTimeoutException(cause);
    }

    protected IOException newTimeoutException(IOException cause) {
        InterruptedIOException e = new InterruptedIOException("timeout");
        if (cause != null) {
            e.initCause(cause);
        }
        return e;
    }

    /* compiled from: AsyncTimeout.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0000¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"Lokio/AsyncTimeout$Watchdog;", "Ljava/lang/Thread;", "()V", "run", "", "okio"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Watchdog extends Thread {
        public Watchdog() {
            super("Okio Watchdog");
            setDaemon(true);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            AsyncTimeout awaitTimeout$okio;
            while (true) {
                try {
                    AsyncTimeout asyncTimeout = null;
                    synchronized (AsyncTimeout.class) {
                        awaitTimeout$okio = AsyncTimeout.Companion.awaitTimeout$okio();
                        if (awaitTimeout$okio == AsyncTimeout.head) {
                            AsyncTimeout.head = null;
                            return;
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                    if (awaitTimeout$okio != null) {
                        awaitTimeout$okio.timedOut();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /* compiled from: AsyncTimeout.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000f\u0010\n\u001a\u0004\u0018\u00010\tH\u0000¢\u0006\u0002\b\u000bJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH\u0002J \u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\rH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lokio/AsyncTimeout$Companion;", "", "()V", "IDLE_TIMEOUT_MILLIS", "", "IDLE_TIMEOUT_NANOS", "TIMEOUT_WRITE_SIZE", "", "head", "Lokio/AsyncTimeout;", "awaitTimeout", "awaitTimeout$okio", "cancelScheduledTimeout", "", "node", "scheduleTimeout", "", "timeoutNanos", "hasDeadline", "okio"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final void scheduleTimeout(AsyncTimeout node, long timeoutNanos, boolean hasDeadline) {
            synchronized (AsyncTimeout.class) {
                if (AsyncTimeout.head == null) {
                    AsyncTimeout.head = new AsyncTimeout();
                    new Watchdog().start();
                }
                long now = System.nanoTime();
                if (timeoutNanos != 0 && hasDeadline) {
                    node.timeoutAt = Math.min(timeoutNanos, node.deadlineNanoTime() - now) + now;
                } else if (timeoutNanos != 0) {
                    node.timeoutAt = now + timeoutNanos;
                } else if (hasDeadline) {
                    node.timeoutAt = node.deadlineNanoTime();
                } else {
                    throw new AssertionError();
                }
                long remainingNanos = node.remainingNanos(now);
                AsyncTimeout prev = AsyncTimeout.head;
                if (prev == null) {
                    Intrinsics.throwNpe();
                }
                while (prev.next != null) {
                    AsyncTimeout asyncTimeout = prev.next;
                    if (asyncTimeout == null) {
                        Intrinsics.throwNpe();
                    }
                    if (remainingNanos < asyncTimeout.remainingNanos(now)) {
                        break;
                    }
                    AsyncTimeout asyncTimeout2 = prev.next;
                    if (asyncTimeout2 == null) {
                        Intrinsics.throwNpe();
                    }
                    prev = asyncTimeout2;
                }
                node.next = prev.next;
                prev.next = node;
                if (prev == AsyncTimeout.head) {
                    AsyncTimeout.class.notify();
                }
                Unit unit = Unit.INSTANCE;
            }
        }

        public final boolean cancelScheduledTimeout(AsyncTimeout node) {
            synchronized (AsyncTimeout.class) {
                for (AsyncTimeout prev = AsyncTimeout.head; prev != null; prev = prev.next) {
                    if (prev.next == node) {
                        prev.next = node.next;
                        node.next = null;
                        return false;
                    }
                }
                return true;
            }
        }

        public final AsyncTimeout awaitTimeout$okio() throws InterruptedException {
            AsyncTimeout asyncTimeout = AsyncTimeout.head;
            if (asyncTimeout == null) {
                Intrinsics.throwNpe();
            }
            AsyncTimeout node = asyncTimeout.next;
            if (node == null) {
                long startNanos = System.nanoTime();
                AsyncTimeout.class.wait(AsyncTimeout.IDLE_TIMEOUT_MILLIS);
                AsyncTimeout asyncTimeout2 = AsyncTimeout.head;
                if (asyncTimeout2 == null) {
                    Intrinsics.throwNpe();
                }
                if (asyncTimeout2.next != null || System.nanoTime() - startNanos < AsyncTimeout.IDLE_TIMEOUT_NANOS) {
                    return null;
                }
                return AsyncTimeout.head;
            }
            long waitNanos = node.remainingNanos(System.nanoTime());
            if (waitNanos > 0) {
                long waitMillis = waitNanos / 1000000;
                AsyncTimeout.class.wait(waitMillis, (int) (waitNanos - (1000000 * waitMillis)));
                return null;
            }
            AsyncTimeout asyncTimeout3 = AsyncTimeout.head;
            if (asyncTimeout3 == null) {
                Intrinsics.throwNpe();
            }
            asyncTimeout3.next = node.next;
            node.next = null;
            return node;
        }
    }
}
