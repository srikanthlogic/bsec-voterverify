package okio;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.OutputStream;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Okio.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\bH\u0016J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lokio/OutputStreamSink;", "Lokio/Sink;", "out", "Ljava/io/OutputStream;", "timeout", "Lokio/Timeout;", "(Ljava/io/OutputStream;Lokio/Timeout;)V", "close", "", "flush", "toString", "", "write", FirebaseAnalytics.Param.SOURCE, "Lokio/Buffer;", "byteCount", "", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class OutputStreamSink implements Sink {
    private final OutputStream out;
    private final Timeout timeout;

    public OutputStreamSink(OutputStream out, Timeout timeout) {
        Intrinsics.checkParameterIsNotNull(out, "out");
        Intrinsics.checkParameterIsNotNull(timeout, "timeout");
        this.out = out;
        this.timeout = timeout;
    }

    /* JADX INFO: Multiple debug info for r3v3 int: [D('toCopy' int), D('b$iv' int)] */
    @Override // okio.Sink
    public void write(Buffer source, long byteCount) {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        Util.checkOffsetAndCount(source.size(), 0, byteCount);
        long remaining = byteCount;
        while (remaining > 0) {
            this.timeout.throwIfReached();
            Segment head = source.head;
            if (head == null) {
                Intrinsics.throwNpe();
            }
            int b$iv = (int) Math.min(remaining, (long) (head.limit - head.pos));
            this.out.write(head.data, head.pos, b$iv);
            head.pos += b$iv;
            remaining -= (long) b$iv;
            source.setSize$okio(source.size() - ((long) b$iv));
            if (head.pos == head.limit) {
                source.head = head.pop();
                SegmentPool.INSTANCE.recycle(head);
            }
        }
    }

    @Override // okio.Sink, java.io.Flushable
    public void flush() {
        this.out.flush();
    }

    @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.out.close();
    }

    @Override // okio.Sink
    public Timeout timeout() {
        return this.timeout;
    }

    @Override // java.lang.Object
    public String toString() {
        return "sink(" + this.out + ')';
    }
}
