package okio;

import android.support.v4.media.session.PlaybackStateCompat;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.io.InterruptedIOException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Throttler.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u001d\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\fJ$\u0010\u0006\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\b\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u0004H\u0007J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u0011J\u0015\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u0013J\u0010\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0004H\u0002J\f\u0010\u0016\u001a\u00020\u0004*\u00020\u0004H\u0002J\f\u0010\u0017\u001a\u00020\u0004*\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lokio/Throttler;", "", "()V", "allocatedUntil", "", "(J)V", "bytesPerSecond", "maxByteCount", "waitByteCount", "byteCountOrWaitNanos", "now", "byteCount", "byteCountOrWaitNanos$okio", "", "sink", "Lokio/Sink;", FirebaseAnalytics.Param.SOURCE, "Lokio/Source;", "take", "take$okio", "waitNanos", "nanosToWait", "bytesToNanos", "nanosToBytes", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Throttler {
    private long allocatedUntil;
    private long bytesPerSecond;
    private long maxByteCount;
    private long waitByteCount;

    public final void bytesPerSecond(long j) {
        bytesPerSecond$default(this, j, 0, 0, 6, null);
    }

    public final void bytesPerSecond(long j, long j2) {
        bytesPerSecond$default(this, j, j2, 0, 4, null);
    }

    public Throttler(long allocatedUntil) {
        this.allocatedUntil = allocatedUntil;
        this.waitByteCount = PlaybackStateCompat.ACTION_PLAY_FROM_URI;
        this.maxByteCount = PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
    }

    public Throttler() {
        this(System.nanoTime());
    }

    public static /* synthetic */ void bytesPerSecond$default(Throttler throttler, long j, long j2, long j3, int i, Object obj) {
        long j4;
        long j5 = (i & 2) != 0 ? throttler.waitByteCount : j2;
        if ((i & 4) != 0) {
            j4 = throttler.maxByteCount;
        } else {
            j4 = j3;
        }
        throttler.bytesPerSecond(j, j5, j4);
    }

    public final void bytesPerSecond(long bytesPerSecond, long waitByteCount, long maxByteCount) {
        synchronized (this) {
            boolean z = true;
            if (bytesPerSecond >= 0) {
                if (waitByteCount > 0) {
                    if (maxByteCount < waitByteCount) {
                        z = false;
                    }
                    if (z) {
                        this.bytesPerSecond = bytesPerSecond;
                        this.waitByteCount = waitByteCount;
                        this.maxByteCount = maxByteCount;
                        notifyAll();
                        Unit unit = Unit.INSTANCE;
                    } else {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                } else {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
            } else {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        }
    }

    public final long take$okio(long byteCount) {
        long byteCountOrWaitNanos;
        boolean z = false;
        if (byteCount > 0) {
            z = true;
        }
        if (z) {
            synchronized (this) {
                while (true) {
                    byteCountOrWaitNanos = byteCountOrWaitNanos$okio(System.nanoTime(), byteCount);
                    if (byteCountOrWaitNanos < 0) {
                        waitNanos(-byteCountOrWaitNanos);
                    }
                }
            }
            return byteCountOrWaitNanos;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    public final long byteCountOrWaitNanos$okio(long now, long byteCount) {
        if (this.bytesPerSecond == 0) {
            return byteCount;
        }
        long idleInNanos = Math.max(this.allocatedUntil - now, 0L);
        long immediateBytes = this.maxByteCount - nanosToBytes(idleInNanos);
        if (immediateBytes >= byteCount) {
            this.allocatedUntil = now + idleInNanos + bytesToNanos(byteCount);
            return byteCount;
        }
        long j = this.waitByteCount;
        if (immediateBytes >= j) {
            this.allocatedUntil = bytesToNanos(this.maxByteCount) + now;
            return immediateBytes;
        }
        long minByteCount = Math.min(j, byteCount);
        long minWaitNanos = bytesToNanos(minByteCount - this.maxByteCount) + idleInNanos;
        if (minWaitNanos != 0) {
            return -minWaitNanos;
        }
        this.allocatedUntil = bytesToNanos(this.maxByteCount) + now;
        return minByteCount;
    }

    private final long nanosToBytes(long $this$nanosToBytes) {
        return (this.bytesPerSecond * $this$nanosToBytes) / 1000000000;
    }

    private final long bytesToNanos(long $this$bytesToNanos) {
        return (1000000000 * $this$bytesToNanos) / this.bytesPerSecond;
    }

    private final void waitNanos(long nanosToWait) {
        long millisToWait = nanosToWait / 1000000;
        wait(millisToWait, (int) (nanosToWait - (1000000 * millisToWait)));
    }

    public final Source source(Source source) {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        return new ForwardingSource(source, source) { // from class: okio.Throttler$source$1
            final /* synthetic */ Source $source;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$source = $captured_local_variable$1;
            }

            @Override // okio.ForwardingSource, okio.Source
            public long read(Buffer sink, long byteCount) {
                Intrinsics.checkParameterIsNotNull(sink, "sink");
                try {
                    return super.read(sink, Throttler.this.take$okio(byteCount));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedIOException("interrupted");
                }
            }
        };
    }

    public final Sink sink(Sink sink) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        return new ForwardingSink(sink, sink) { // from class: okio.Throttler$sink$1
            final /* synthetic */ Sink $sink;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$sink = $captured_local_variable$1;
            }

            @Override // okio.ForwardingSink, okio.Sink
            public void write(Buffer source, long byteCount) throws IOException {
                Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
                long remaining = byteCount;
                while (remaining > 0) {
                    try {
                        long toWrite = Throttler.this.take$okio(remaining);
                        super.write(source, toWrite);
                        remaining -= toWrite;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedIOException("interrupted");
                    }
                }
            }
        };
    }
}
