package okio;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GzipSource.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J \u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0002J\b\u0010\u0014\u001a\u00020\u000eH\u0016J\b\u0010\u0015\u001a\u00020\u000eH\u0002J\b\u0010\u0016\u001a\u00020\u000eH\u0002J\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0018H\u0016J\b\u0010\u001c\u001a\u00020\u001dH\u0016J \u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u0018H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006!"}, d2 = {"Lokio/GzipSource;", "Lokio/Source;", FirebaseAnalytics.Param.SOURCE, "(Lokio/Source;)V", "crc", "Ljava/util/zip/CRC32;", "inflater", "Ljava/util/zip/Inflater;", "inflaterSource", "Lokio/InflaterSource;", "section", "", "Lokio/RealBufferedSource;", "checkEqual", "", AppMeasurementSdk.ConditionalUserProperty.NAME, "", "expected", "", "actual", "close", "consumeHeader", "consumeTrailer", "read", "", "sink", "Lokio/Buffer;", "byteCount", "timeout", "Lokio/Timeout;", "updateCrc", "buffer", "offset", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class GzipSource implements Source {
    private final InflaterSource inflaterSource;
    private byte section;
    private final RealBufferedSource source;
    private final Inflater inflater = new Inflater(true);
    private final CRC32 crc = new CRC32();

    public GzipSource(Source source) {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        this.source = new RealBufferedSource(source);
        this.inflaterSource = new InflaterSource((BufferedSource) this.source, this.inflater);
    }

    @Override // okio.Source
    public long read(Buffer sink, long byteCount) throws IOException {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        if (!(byteCount >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
        } else if (byteCount == 0) {
            return 0;
        } else {
            if (this.section == 0) {
                consumeHeader();
                this.section = 1;
            }
            if (this.section == 1) {
                long offset = sink.size();
                long result = this.inflaterSource.read(sink, byteCount);
                if (result != -1) {
                    updateCrc(sink, offset, result);
                    return result;
                }
                this.section = 2;
            }
            if (this.section == 2) {
                consumeTrailer();
                this.section = 3;
                if (!this.source.exhausted()) {
                    throw new IOException("gzip finished without exhausting source");
                }
            }
            return -1;
        }
    }

    private final void consumeHeader() throws IOException {
        this.source.require(10);
        int flags = this.source.bufferField.getByte(3);
        boolean z = true;
        int bit$iv = ((flags >> 1) & 1) == 1 ? 1 : 0;
        if (bit$iv != 0) {
            updateCrc(this.source.bufferField, 0, 10);
        }
        checkEqual("ID1ID2", 8075, this.source.readShort());
        this.source.skip(8);
        if ((((flags >> 2) & 1) == 1 ? 1 : 0) != 0) {
            this.source.require(2);
            if (bit$iv != 0) {
                updateCrc(this.source.bufferField, 0, 2);
            }
            long xlen = (long) this.source.bufferField.readShortLe();
            this.source.require(xlen);
            if (bit$iv != 0) {
                updateCrc(this.source.bufferField, 0, xlen);
            }
            this.source.skip(xlen);
        }
        if ((((flags >> 3) & 1) == 1 ? 1 : 0) != 0) {
            long index = this.source.indexOf((byte) 0);
            if (index != -1) {
                if (bit$iv != 0) {
                    updateCrc(this.source.bufferField, 0, index + 1);
                }
                this.source.skip(index + 1);
            } else {
                throw new EOFException();
            }
        }
        if (((flags >> 4) & 1) != 1) {
            z = false;
        }
        if (z) {
            long index2 = this.source.indexOf((byte) 0);
            if (index2 != -1) {
                if (bit$iv != 0) {
                    updateCrc(this.source.bufferField, 0, index2 + 1);
                }
                this.source.skip(1 + index2);
            } else {
                throw new EOFException();
            }
        }
        if (bit$iv != 0) {
            checkEqual("FHCRC", this.source.readShortLe(), (short) ((int) this.crc.getValue()));
            this.crc.reset();
        }
    }

    private final void consumeTrailer() throws IOException {
        checkEqual("CRC", this.source.readIntLe(), (int) this.crc.getValue());
        checkEqual("ISIZE", this.source.readIntLe(), (int) this.inflater.getBytesWritten());
    }

    @Override // okio.Source
    public Timeout timeout() {
        return this.source.timeout();
    }

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.inflaterSource.close();
    }

    /* JADX INFO: Multiple debug info for r6v3 int: [D('toUpdate' int), D('a$iv' int)] */
    private final void updateCrc(Buffer buffer, long offset, long byteCount) {
        long offset2 = offset;
        long byteCount2 = byteCount;
        Segment s = buffer.head;
        if (s == null) {
            Intrinsics.throwNpe();
        }
        while (offset2 >= ((long) (s.limit - s.pos))) {
            offset2 -= (long) (s.limit - s.pos);
            Segment segment = s.next;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            s = segment;
        }
        while (byteCount2 > 0) {
            int pos = (int) (((long) s.pos) + offset2);
            int a$iv = (int) Math.min((long) (s.limit - pos), byteCount2);
            this.crc.update(s.data, pos, a$iv);
            byteCount2 -= (long) a$iv;
            offset2 = 0;
            Segment segment2 = s.next;
            if (segment2 == null) {
                Intrinsics.throwNpe();
            }
            s = segment2;
        }
    }

    private final void checkEqual(String name, int expected, int actual) {
        if (actual != expected) {
            Object[] objArr = {name, Integer.valueOf(actual), Integer.valueOf(expected)};
            String format = String.format("%s: actual 0x%08x != expected 0x%08x", Arrays.copyOf(objArr, objArr.length));
            Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(this, *args)");
            throw new IOException(format);
        }
    }
}
