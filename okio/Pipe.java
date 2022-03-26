package okio;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Pipe.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0011\u001a\u00020\nJ\r\u0010\u0011\u001a\u00020\nH\u0007¢\u0006\u0002\b J\r\u0010\u0018\u001a\u00020\u0019H\u0007¢\u0006\u0002\b!J&\u0010\"\u001a\u00020\u001f*\u00020\n2\u0017\u0010#\u001a\u0013\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u001f0$¢\u0006\u0002\b%H\u0082\bR\u0014\u0010\u0005\u001a\u00020\u0006X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\u0011\u001a\u00020\n8G¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\fR\u001a\u0010\u0012\u001a\u00020\u0013X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0013\u0010\u0018\u001a\u00020\u00198G¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u001aR\u001a\u0010\u001b\u001a\u00020\u0013X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0015\"\u0004\b\u001d\u0010\u0017¨\u0006&"}, d2 = {"Lokio/Pipe;", "", "maxBufferSize", "", "(J)V", "buffer", "Lokio/Buffer;", "getBuffer$okio", "()Lokio/Buffer;", "foldedSink", "Lokio/Sink;", "getFoldedSink$okio", "()Lokio/Sink;", "setFoldedSink$okio", "(Lokio/Sink;)V", "getMaxBufferSize$okio", "()J", "sink", "sinkClosed", "", "getSinkClosed$okio", "()Z", "setSinkClosed$okio", "(Z)V", FirebaseAnalytics.Param.SOURCE, "Lokio/Source;", "()Lokio/Source;", "sourceClosed", "getSourceClosed$okio", "setSourceClosed$okio", "fold", "", "-deprecated_sink", "-deprecated_source", "forward", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Pipe {
    private final Buffer buffer = new Buffer();
    private Sink foldedSink;
    private final long maxBufferSize;
    private final Sink sink;
    private boolean sinkClosed;
    private final Source source;
    private boolean sourceClosed;

    public Pipe(long maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
        if (this.maxBufferSize >= 1) {
            this.sink = new Sink() { // from class: okio.Pipe$sink$1
                private final Timeout timeout = new Timeout();

                @Override // okio.Sink
                public void write(Buffer source, long byteCount) {
                    Throwable th;
                    Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
                    long byteCount2 = byteCount;
                    Sink sink = null;
                    synchronized (Pipe.this.getBuffer$okio()) {
                        try {
                            if (!Pipe.this.getSinkClosed$okio()) {
                                while (byteCount2 > 0) {
                                    Sink it = Pipe.this.getFoldedSink$okio();
                                    if (it != null) {
                                        sink = it;
                                        break;
                                    } else if (!Pipe.this.getSourceClosed$okio()) {
                                        long bufferSpaceAvailable = Pipe.this.getMaxBufferSize$okio() - Pipe.this.getBuffer$okio().size();
                                        if (bufferSpaceAvailable == 0) {
                                            this.timeout.waitUntilNotified(Pipe.this.getBuffer$okio());
                                        } else {
                                            long bytesToWrite = Math.min(bufferSpaceAvailable, byteCount2);
                                            Pipe.this.getBuffer$okio().write(source, bytesToWrite);
                                            byteCount2 -= bytesToWrite;
                                            Buffer buffer$okio = Pipe.this.getBuffer$okio();
                                            if (buffer$okio != null) {
                                                buffer$okio.notifyAll();
                                            } else {
                                                throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                                            }
                                        }
                                    } else {
                                        throw new IOException("source is closed");
                                    }
                                }
                                try {
                                    Unit unit = Unit.INSTANCE;
                                    if (sink != null) {
                                        Pipe this_$iv = Pipe.this;
                                        Timeout this_$iv$iv = sink.timeout();
                                        Timeout other$iv$iv = this_$iv.sink().timeout();
                                        long originalTimeout$iv$iv = this_$iv$iv.timeoutNanos();
                                        this_$iv$iv.timeout(Timeout.Companion.minTimeout(other$iv$iv.timeoutNanos(), this_$iv$iv.timeoutNanos()), TimeUnit.NANOSECONDS);
                                        if (this_$iv$iv.hasDeadline()) {
                                            long originalDeadline$iv$iv = this_$iv$iv.deadlineNanoTime();
                                            if (other$iv$iv.hasDeadline()) {
                                                this_$iv$iv.deadlineNanoTime(Math.min(this_$iv$iv.deadlineNanoTime(), other$iv$iv.deadlineNanoTime()));
                                            }
                                            try {
                                                sink.write(source, byteCount2);
                                            } finally {
                                                this_$iv$iv.timeout(originalTimeout$iv$iv, TimeUnit.NANOSECONDS);
                                                if (other$iv$iv.hasDeadline()) {
                                                    this_$iv$iv.deadlineNanoTime(originalDeadline$iv$iv);
                                                }
                                            }
                                        } else {
                                            if (other$iv$iv.hasDeadline()) {
                                                this_$iv$iv.deadlineNanoTime(other$iv$iv.deadlineNanoTime());
                                            }
                                            try {
                                                sink.write(source, byteCount2);
                                            } finally {
                                                this_$iv$iv.timeout(originalTimeout$iv$iv, TimeUnit.NANOSECONDS);
                                                if (other$iv$iv.hasDeadline()) {
                                                    this_$iv$iv.clearDeadline();
                                                }
                                            }
                                        }
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            } else {
                                throw new IllegalStateException("closed".toString());
                            }
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    }
                }

                @Override // okio.Sink, java.io.Flushable
                public void flush() {
                    Sink sink = null;
                    synchronized (Pipe.this.getBuffer$okio()) {
                        if (!Pipe.this.getSinkClosed$okio()) {
                            Sink it = Pipe.this.getFoldedSink$okio();
                            if (it != null) {
                                sink = it;
                            } else if (Pipe.this.getSourceClosed$okio() && Pipe.this.getBuffer$okio().size() > 0) {
                                throw new IOException("source is closed");
                            }
                            Unit unit = Unit.INSTANCE;
                        } else {
                            throw new IllegalStateException("closed".toString());
                        }
                    }
                    if (sink != null) {
                        Pipe this_$iv = Pipe.this;
                        Timeout this_$iv$iv = sink.timeout();
                        Timeout other$iv$iv = this_$iv.sink().timeout();
                        long originalTimeout$iv$iv = this_$iv$iv.timeoutNanos();
                        this_$iv$iv.timeout(Timeout.Companion.minTimeout(other$iv$iv.timeoutNanos(), this_$iv$iv.timeoutNanos()), TimeUnit.NANOSECONDS);
                        if (this_$iv$iv.hasDeadline()) {
                            long originalDeadline$iv$iv = this_$iv$iv.deadlineNanoTime();
                            if (other$iv$iv.hasDeadline()) {
                                this_$iv$iv.deadlineNanoTime(Math.min(this_$iv$iv.deadlineNanoTime(), other$iv$iv.deadlineNanoTime()));
                            }
                            try {
                                sink.flush();
                            } finally {
                                this_$iv$iv.timeout(originalTimeout$iv$iv, TimeUnit.NANOSECONDS);
                                if (other$iv$iv.hasDeadline()) {
                                    this_$iv$iv.deadlineNanoTime(originalDeadline$iv$iv);
                                }
                            }
                        } else {
                            if (other$iv$iv.hasDeadline()) {
                                this_$iv$iv.deadlineNanoTime(other$iv$iv.deadlineNanoTime());
                            }
                            try {
                                sink.flush();
                            } finally {
                                this_$iv$iv.timeout(originalTimeout$iv$iv, TimeUnit.NANOSECONDS);
                                if (other$iv$iv.hasDeadline()) {
                                    this_$iv$iv.clearDeadline();
                                }
                            }
                        }
                    }
                }

                @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                    Sink sink = null;
                    synchronized (Pipe.this.getBuffer$okio()) {
                        if (!Pipe.this.getSinkClosed$okio()) {
                            Sink it = Pipe.this.getFoldedSink$okio();
                            if (it != null) {
                                sink = it;
                            } else {
                                if (Pipe.this.getSourceClosed$okio() && Pipe.this.getBuffer$okio().size() > 0) {
                                    throw new IOException("source is closed");
                                }
                                Pipe.this.setSinkClosed$okio(true);
                                Buffer buffer$okio = Pipe.this.getBuffer$okio();
                                if (buffer$okio != null) {
                                    buffer$okio.notifyAll();
                                } else {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                                }
                            }
                            Unit unit = Unit.INSTANCE;
                            if (sink != null) {
                                Pipe this_$iv = Pipe.this;
                                Timeout this_$iv$iv = sink.timeout();
                                Timeout other$iv$iv = this_$iv.sink().timeout();
                                long originalTimeout$iv$iv = this_$iv$iv.timeoutNanos();
                                this_$iv$iv.timeout(Timeout.Companion.minTimeout(other$iv$iv.timeoutNanos(), this_$iv$iv.timeoutNanos()), TimeUnit.NANOSECONDS);
                                if (this_$iv$iv.hasDeadline()) {
                                    long originalDeadline$iv$iv = this_$iv$iv.deadlineNanoTime();
                                    if (other$iv$iv.hasDeadline()) {
                                        this_$iv$iv.deadlineNanoTime(Math.min(this_$iv$iv.deadlineNanoTime(), other$iv$iv.deadlineNanoTime()));
                                    }
                                    try {
                                        sink.close();
                                    } finally {
                                        this_$iv$iv.timeout(originalTimeout$iv$iv, TimeUnit.NANOSECONDS);
                                        if (other$iv$iv.hasDeadline()) {
                                            this_$iv$iv.deadlineNanoTime(originalDeadline$iv$iv);
                                        }
                                    }
                                } else {
                                    if (other$iv$iv.hasDeadline()) {
                                        this_$iv$iv.deadlineNanoTime(other$iv$iv.deadlineNanoTime());
                                    }
                                    try {
                                        sink.close();
                                    } finally {
                                        this_$iv$iv.timeout(originalTimeout$iv$iv, TimeUnit.NANOSECONDS);
                                        if (other$iv$iv.hasDeadline()) {
                                            this_$iv$iv.clearDeadline();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                @Override // okio.Sink
                public Timeout timeout() {
                    return this.timeout;
                }
            };
            this.source = new Source() { // from class: okio.Pipe$source$1
                private final Timeout timeout = new Timeout();

                @Override // okio.Source
                public long read(Buffer sink, long byteCount) {
                    Intrinsics.checkParameterIsNotNull(sink, "sink");
                    synchronized (Pipe.this.getBuffer$okio()) {
                        if (!Pipe.this.getSourceClosed$okio()) {
                            while (Pipe.this.getBuffer$okio().size() == 0) {
                                if (Pipe.this.getSinkClosed$okio()) {
                                    return -1;
                                }
                                this.timeout.waitUntilNotified(Pipe.this.getBuffer$okio());
                            }
                            long result = Pipe.this.getBuffer$okio().read(sink, byteCount);
                            Buffer buffer$okio = Pipe.this.getBuffer$okio();
                            if (buffer$okio != null) {
                                buffer$okio.notifyAll();
                                return result;
                            }
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                        }
                        throw new IllegalStateException("closed".toString());
                    }
                }

                @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                    synchronized (Pipe.this.getBuffer$okio()) {
                        Pipe.this.setSourceClosed$okio(true);
                        Buffer buffer$okio = Pipe.this.getBuffer$okio();
                        if (buffer$okio != null) {
                            buffer$okio.notifyAll();
                            Unit unit = Unit.INSTANCE;
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                        }
                    }
                }

                @Override // okio.Source
                public Timeout timeout() {
                    return this.timeout;
                }
            };
            return;
        }
        throw new IllegalArgumentException(("maxBufferSize < 1: " + this.maxBufferSize).toString());
    }

    public final long getMaxBufferSize$okio() {
        return this.maxBufferSize;
    }

    public final Buffer getBuffer$okio() {
        return this.buffer;
    }

    public final boolean getSinkClosed$okio() {
        return this.sinkClosed;
    }

    public final void setSinkClosed$okio(boolean z) {
        this.sinkClosed = z;
    }

    public final boolean getSourceClosed$okio() {
        return this.sourceClosed;
    }

    public final void setSourceClosed$okio(boolean z) {
        this.sourceClosed = z;
    }

    public final Sink getFoldedSink$okio() {
        return this.foldedSink;
    }

    public final void setFoldedSink$okio(Sink sink) {
        this.foldedSink = sink;
    }

    public final Sink sink() {
        return this.sink;
    }

    public final Source source() {
        return this.source;
    }

    public final void fold(Sink sink) throws IOException {
        boolean closed;
        Buffer buffer;
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        while (true) {
            synchronized (this.buffer) {
                try {
                    if (!(this.foldedSink == null)) {
                        throw new IllegalStateException("sink already folded".toString());
                    } else if (this.buffer.exhausted()) {
                        this.sourceClosed = true;
                        this.foldedSink = sink;
                        return;
                    } else {
                        closed = this.sinkClosed;
                        buffer = new Buffer();
                        buffer.write(this.buffer, this.buffer.size());
                        Buffer buffer2 = this.buffer;
                        if (buffer2 != null) {
                            buffer2.notifyAll();
                            Unit unit = Unit.INSTANCE;
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            try {
                sink.write(buffer, buffer.size());
                if (closed) {
                    sink.close();
                } else {
                    sink.flush();
                }
            } catch (Throwable th2) {
                synchronized (this.buffer) {
                    try {
                        this.sourceClosed = true;
                        Buffer buffer3 = this.buffer;
                        if (buffer3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                        }
                        buffer3.notifyAll();
                        Unit unit2 = Unit.INSTANCE;
                        throw th2;
                    } catch (Throwable th3) {
                        throw th3;
                    }
                }
            }
        }
    }

    public final void forward(Sink $this$forward, Function1<? super Sink, Unit> function1) {
        Timeout this_$iv = $this$forward.timeout();
        Timeout other$iv = sink().timeout();
        long originalTimeout$iv = this_$iv.timeoutNanos();
        this_$iv.timeout(Timeout.Companion.minTimeout(other$iv.timeoutNanos(), this_$iv.timeoutNanos()), TimeUnit.NANOSECONDS);
        if (this_$iv.hasDeadline()) {
            long originalDeadline$iv = this_$iv.deadlineNanoTime();
            if (other$iv.hasDeadline()) {
                this_$iv.deadlineNanoTime(Math.min(this_$iv.deadlineNanoTime(), other$iv.deadlineNanoTime()));
            }
            try {
                function1.invoke($this$forward);
            } finally {
                InlineMarker.finallyStart(1);
                this_$iv.timeout(originalTimeout$iv, TimeUnit.NANOSECONDS);
                if (other$iv.hasDeadline()) {
                    this_$iv.deadlineNanoTime(originalDeadline$iv);
                }
                InlineMarker.finallyEnd(1);
            }
        } else {
            if (other$iv.hasDeadline()) {
                this_$iv.deadlineNanoTime(other$iv.deadlineNanoTime());
            }
            try {
                function1.invoke($this$forward);
            } finally {
                InlineMarker.finallyStart(1);
                this_$iv.timeout(originalTimeout$iv, TimeUnit.NANOSECONDS);
                if (other$iv.hasDeadline()) {
                    this_$iv.clearDeadline();
                }
                InlineMarker.finallyEnd(1);
            }
        }
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "sink", imports = {}))
    /* renamed from: -deprecated_sink */
    public final Sink m1170deprecated_sink() {
        return this.sink;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "source", imports = {}))
    /* renamed from: -deprecated_source */
    public final Source m1171deprecated_source() {
        return this.source;
    }
}
