package okio;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: InflaterSource.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0006\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J\u0006\u0010\u0013\u001a\u00020\u000bJ\b\u0010\u0014\u001a\u00020\rH\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lokio/InflaterSource;", "Lokio/Source;", FirebaseAnalytics.Param.SOURCE, "inflater", "Ljava/util/zip/Inflater;", "(Lokio/Source;Ljava/util/zip/Inflater;)V", "Lokio/BufferedSource;", "(Lokio/BufferedSource;Ljava/util/zip/Inflater;)V", "bufferBytesHeldByInflater", "", "closed", "", "close", "", "read", "", "sink", "Lokio/Buffer;", "byteCount", "refill", "releaseInflatedBytes", "timeout", "Lokio/Timeout;", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class InflaterSource implements Source {
    private int bufferBytesHeldByInflater;
    private boolean closed;
    private final Inflater inflater;
    private final BufferedSource source;

    public InflaterSource(BufferedSource source, Inflater inflater) {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        Intrinsics.checkParameterIsNotNull(inflater, "inflater");
        this.source = source;
        this.inflater = inflater;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public InflaterSource(Source source, Inflater inflater) {
        this(Okio.buffer(source), inflater);
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        Intrinsics.checkParameterIsNotNull(inflater, "inflater");
    }

    /* JADX INFO: Multiple debug info for r2v10 int: [D('toRead' int), D('b$iv' int)] */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x006b, code lost:
        releaseInflatedBytes();
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0072, code lost:
        if (r1.pos != r1.limit) goto L_?;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0074, code lost:
        r10.head = r1.pop();
        okio.SegmentPool.INSTANCE.recycle(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x007f, code lost:
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:?, code lost:
        return -1;
     */
    @Override // okio.Source
    /* Code decompiled incorrectly, please refer to instructions dump */
    public long read(Buffer sink, long byteCount) throws IOException {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        if (!(byteCount >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
        } else if (!(!this.closed)) {
            throw new IllegalStateException("closed".toString());
        } else if (byteCount == 0) {
            return 0;
        } else {
            while (true) {
                boolean sourceExhausted = refill();
                try {
                    Segment tail = sink.writableSegment$okio(1);
                    int bytesInflated = this.inflater.inflate(tail.data, tail.limit, (int) Math.min(byteCount, (long) (8192 - tail.limit)));
                    if (bytesInflated <= 0) {
                        if (this.inflater.finished() || this.inflater.needsDictionary()) {
                            break;
                        } else if (sourceExhausted) {
                            throw new EOFException("source exhausted prematurely");
                        }
                    } else {
                        tail.limit += bytesInflated;
                        sink.setSize$okio(sink.size() + ((long) bytesInflated));
                        return (long) bytesInflated;
                    }
                } catch (DataFormatException e) {
                    throw new IOException(e);
                }
            }
        }
    }

    public final boolean refill() throws IOException {
        if (!this.inflater.needsInput()) {
            return false;
        }
        releaseInflatedBytes();
        if (!(this.inflater.getRemaining() == 0)) {
            throw new IllegalStateException("?".toString());
        } else if (this.source.exhausted()) {
            return true;
        } else {
            Segment head = this.source.getBuffer().head;
            if (head == null) {
                Intrinsics.throwNpe();
            }
            this.bufferBytesHeldByInflater = head.limit - head.pos;
            this.inflater.setInput(head.data, head.pos, this.bufferBytesHeldByInflater);
            return false;
        }
    }

    private final void releaseInflatedBytes() {
        int i = this.bufferBytesHeldByInflater;
        if (i != 0) {
            int toRelease = i - this.inflater.getRemaining();
            this.bufferBytesHeldByInflater -= toRelease;
            this.source.skip((long) toRelease);
        }
    }

    @Override // okio.Source
    public Timeout timeout() {
        return this.source.timeout();
    }

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.inflater.end();
            this.closed = true;
            this.source.close();
        }
    }
}
