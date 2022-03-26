package okio;

import com.facebook.common.util.UriUtil;
import com.google.common.base.Ascii;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Charsets;
import kotlin.text.Typography;
import okhttp3.internal.connection.RealConnection;
import okio.internal.BufferKt;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
/* compiled from: Buffer.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000ª\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001a\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0017\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0002\u0090\u0001B\u0005¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0000H\u0016J\u0006\u0010\u0011\u001a\u00020\u0012J\b\u0010\u0013\u001a\u00020\u0000H\u0016J\b\u0010\u0014\u001a\u00020\u0012H\u0016J\u0006\u0010\u0015\u001a\u00020\fJ\u0006\u0010\u0016\u001a\u00020\u0000J$\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u001b\u001a\u00020\fH\u0007J\u0018\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u001a\u001a\u00020\fJ \u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u001a\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\fJ\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020\u0000H\u0016J\b\u0010!\u001a\u00020\u0000H\u0016J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010%H\u0096\u0002J\b\u0010&\u001a\u00020#H\u0016J\b\u0010'\u001a\u00020\u0012H\u0016J\u0016\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\fH\u0087\u0002¢\u0006\u0002\b+J\u0015\u0010+\u001a\u00020)2\u0006\u0010,\u001a\u00020\fH\u0007¢\u0006\u0002\b-J\b\u0010.\u001a\u00020/H\u0016J\u0018\u00100\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u00101\u001a\u00020\u001dH\u0002J\u000e\u00102\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u000e\u00103\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u000e\u00104\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u0010\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)H\u0016J\u0018\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)2\u0006\u00107\u001a\u00020\fH\u0016J \u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)2\u0006\u00107\u001a\u00020\f2\u0006\u00108\u001a\u00020\fH\u0016J\u0010\u00105\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001dH\u0016J\u0018\u00105\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001d2\u0006\u00107\u001a\u00020\fH\u0016J\u0010\u0010:\u001a\u00020\f2\u0006\u0010;\u001a\u00020\u001dH\u0016J\u0018\u0010:\u001a\u00020\f2\u0006\u0010;\u001a\u00020\u001d2\u0006\u00107\u001a\u00020\fH\u0016J\b\u0010<\u001a\u00020=H\u0016J\b\u0010>\u001a\u00020#H\u0016J\u0006\u0010?\u001a\u00020\u001dJ\b\u0010@\u001a\u00020\u0019H\u0016J\b\u0010A\u001a\u00020\u0001H\u0016J\u0018\u0010B\u001a\u00020#2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001dH\u0016J(\u0010B\u001a\u00020#2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001d2\u0006\u0010C\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0010\u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020FH\u0016J\u0010\u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020GH\u0016J \u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020G2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010D\u001a\u00020\f2\u0006\u0010E\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010H\u001a\u00020\f2\u0006\u0010E\u001a\u00020IH\u0016J\u0012\u0010J\u001a\u00020K2\b\b\u0002\u0010L\u001a\u00020KH\u0007J\b\u0010M\u001a\u00020)H\u0016J\b\u0010N\u001a\u00020GH\u0016J\u0010\u0010N\u001a\u00020G2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010O\u001a\u00020\u001dH\u0016J\u0010\u0010O\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010P\u001a\u00020\fH\u0016J\u000e\u0010Q\u001a\u00020\u00002\u0006\u0010R\u001a\u00020=J\u0016\u0010Q\u001a\u00020\u00002\u0006\u0010R\u001a\u00020=2\u0006\u0010\u001b\u001a\u00020\fJ \u0010Q\u001a\u00020\u00122\u0006\u0010R\u001a\u00020=2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010S\u001a\u00020#H\u0002J\u0010\u0010T\u001a\u00020\u00122\u0006\u0010E\u001a\u00020GH\u0016J\u0018\u0010T\u001a\u00020\u00122\u0006\u0010E\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010U\u001a\u00020\fH\u0016J\b\u0010V\u001a\u00020/H\u0016J\b\u0010W\u001a\u00020/H\u0016J\b\u0010X\u001a\u00020\fH\u0016J\b\u0010Y\u001a\u00020\fH\u0016J\b\u0010Z\u001a\u00020[H\u0016J\b\u0010\\\u001a\u00020[H\u0016J\u0010\u0010]\u001a\u00020\u001f2\u0006\u0010^\u001a\u00020_H\u0016J\u0018\u0010]\u001a\u00020\u001f2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010^\u001a\u00020_H\u0016J\u0012\u0010`\u001a\u00020K2\b\b\u0002\u0010L\u001a\u00020KH\u0007J\b\u0010a\u001a\u00020\u001fH\u0016J\u0010\u0010a\u001a\u00020\u001f2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010b\u001a\u00020/H\u0016J\n\u0010c\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010d\u001a\u00020\u001fH\u0016J\u0010\u0010d\u001a\u00020\u001f2\u0006\u0010e\u001a\u00020\fH\u0016J\u0010\u0010f\u001a\u00020#2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010g\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010h\u001a\u00020/2\u0006\u0010i\u001a\u00020jH\u0016J\u0006\u0010k\u001a\u00020\u001dJ\u0006\u0010l\u001a\u00020\u001dJ\u0006\u0010m\u001a\u00020\u001dJ\r\u0010\r\u001a\u00020\fH\u0007¢\u0006\u0002\bnJ\u0010\u0010o\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0006\u0010p\u001a\u00020\u001dJ\u000e\u0010p\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020/J\b\u0010q\u001a\u00020rH\u0016J\b\u0010s\u001a\u00020\u001fH\u0016J\u0015\u0010t\u001a\u00020\n2\u0006\u0010u\u001a\u00020/H\u0000¢\u0006\u0002\bvJ\u0010\u0010w\u001a\u00020/2\u0006\u0010x\u001a\u00020FH\u0016J\u0010\u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020GH\u0016J \u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020G2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010w\u001a\u00020\u00122\u0006\u0010x\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010w\u001a\u00020\u00002\u0006\u0010y\u001a\u00020\u001dH\u0016J \u0010w\u001a\u00020\u00002\u0006\u0010y\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020z2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010{\u001a\u00020\f2\u0006\u0010x\u001a\u00020zH\u0016J\u0010\u0010|\u001a\u00020\u00002\u0006\u00106\u001a\u00020/H\u0016J\u0010\u0010}\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0010\u0010\u007f\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0012\u0010\u0080\u0001\u001a\u00020\u00002\u0007\u0010\u0081\u0001\u001a\u00020/H\u0016J\u0012\u0010\u0082\u0001\u001a\u00020\u00002\u0007\u0010\u0081\u0001\u001a\u00020/H\u0016J\u0011\u0010\u0083\u0001\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0011\u0010\u0084\u0001\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0012\u0010\u0085\u0001\u001a\u00020\u00002\u0007\u0010\u0086\u0001\u001a\u00020/H\u0016J\u0012\u0010\u0087\u0001\u001a\u00020\u00002\u0007\u0010\u0086\u0001\u001a\u00020/H\u0016J\u001a\u0010\u0088\u0001\u001a\u00020\u00002\u0007\u0010\u0089\u0001\u001a\u00020\u001f2\u0006\u0010^\u001a\u00020_H\u0016J,\u0010\u0088\u0001\u001a\u00020\u00002\u0007\u0010\u0089\u0001\u001a\u00020\u001f2\u0007\u0010\u008a\u0001\u001a\u00020/2\u0007\u0010\u008b\u0001\u001a\u00020/2\u0006\u0010^\u001a\u00020_H\u0016J\u001b\u0010\u008c\u0001\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001b\u001a\u00020\fH\u0007J\u0012\u0010\u008d\u0001\u001a\u00020\u00002\u0007\u0010\u0089\u0001\u001a\u00020\u001fH\u0016J$\u0010\u008d\u0001\u001a\u00020\u00002\u0007\u0010\u0089\u0001\u001a\u00020\u001f2\u0007\u0010\u008a\u0001\u001a\u00020/2\u0007\u0010\u008b\u0001\u001a\u00020/H\u0016J\u0012\u0010\u008e\u0001\u001a\u00020\u00002\u0007\u0010\u008f\u0001\u001a\u00020/H\u0016R\u0014\u0010\u0006\u001a\u00020\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u0004\u0018\u00010\n8\u0000@\u0000X\u0081\u000e¢\u0006\u0002\n\u0000R&\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8G@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006\u0091\u0001"}, d2 = {"Lokio/Buffer;", "Lokio/BufferedSource;", "Lokio/BufferedSink;", "", "Ljava/nio/channels/ByteChannel;", "()V", "buffer", "getBuffer", "()Lokio/Buffer;", "head", "Lokio/Segment;", "<set-?>", "", "size", "()J", "setSize$okio", "(J)V", "clear", "", "clone", "close", "completeSegmentByteCount", "copy", "copyTo", "out", "Ljava/io/OutputStream;", "offset", "byteCount", "digest", "Lokio/ByteString;", "algorithm", "", "emit", "emitCompleteSegments", "equals", "", "other", "", "exhausted", "flush", "get", "", "pos", "getByte", FirebaseAnalytics.Param.INDEX, "-deprecated_getByte", "hashCode", "", "hmac", "key", "hmacSha1", "hmacSha256", "hmacSha512", "indexOf", "b", "fromIndex", "toIndex", "bytes", "indexOfElement", "targetBytes", "inputStream", "Ljava/io/InputStream;", "isOpen", "md5", "outputStream", "peek", "rangeEquals", "bytesOffset", "read", "sink", "Ljava/nio/ByteBuffer;", "", "readAll", "Lokio/Sink;", "readAndWriteUnsafe", "Lokio/Buffer$UnsafeCursor;", "unsafeCursor", "readByte", "readByteArray", "readByteString", "readDecimalLong", "readFrom", "input", "forever", "readFully", "readHexadecimalUnsignedLong", "readInt", "readIntLe", "readLong", "readLongLe", "readShort", "", "readShortLe", "readString", "charset", "Ljava/nio/charset/Charset;", "readUnsafe", "readUtf8", "readUtf8CodePoint", "readUtf8Line", "readUtf8LineStrict", "limit", "request", "require", "select", "options", "Lokio/Options;", "sha1", "sha256", "sha512", "-deprecated_size", "skip", "snapshot", "timeout", "Lokio/Timeout;", "toString", "writableSegment", "minimumCapacity", "writableSegment$okio", "write", FirebaseAnalytics.Param.SOURCE, "byteString", "Lokio/Source;", "writeAll", "writeByte", "writeDecimalLong", "v", "writeHexadecimalUnsignedLong", "writeInt", "i", "writeIntLe", "writeLong", "writeLongLe", "writeShort", "s", "writeShortLe", "writeString", "string", "beginIndex", "endIndex", "writeTo", "writeUtf8", "writeUtf8CodePoint", "codePoint", "UnsafeCursor", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
    public Segment head;
    private long size;

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        return buffer.copyTo(buffer2, j);
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long j, long j2, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        return buffer.copyTo(buffer2, j, j2);
    }

    public final Buffer copyTo(OutputStream outputStream) throws IOException {
        return copyTo$default(this, outputStream, 0, 0, 6, (Object) null);
    }

    public final Buffer copyTo(OutputStream outputStream, long j) throws IOException {
        return copyTo$default(this, outputStream, j, 0, 4, (Object) null);
    }

    public final UnsafeCursor readAndWriteUnsafe() {
        return readAndWriteUnsafe$default(this, null, 1, null);
    }

    public final UnsafeCursor readUnsafe() {
        return readUnsafe$default(this, null, 1, null);
    }

    public final Buffer writeTo(OutputStream outputStream) throws IOException {
        return writeTo$default(this, outputStream, 0, 2, null);
    }

    public final void setSize$okio(long j) {
        this.size = j;
    }

    public final long size() {
        return this.size;
    }

    @Override // okio.BufferedSource, okio.BufferedSink
    public Buffer buffer() {
        return this;
    }

    @Override // okio.BufferedSource, okio.BufferedSink
    public Buffer getBuffer() {
        return this;
    }

    @Override // okio.BufferedSink
    public OutputStream outputStream() {
        return new OutputStream() { // from class: okio.Buffer$outputStream$1
            @Override // java.io.OutputStream
            public void write(int b) {
                Buffer.this.writeByte(b);
            }

            @Override // java.io.OutputStream
            public void write(byte[] data, int offset, int byteCount) {
                Intrinsics.checkParameterIsNotNull(data, UriUtil.DATA_SCHEME);
                Buffer.this.write(data, offset, byteCount);
            }

            @Override // java.io.OutputStream, java.io.Flushable
            public void flush() {
            }

            @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }

            @Override // java.lang.Object
            public String toString() {
                return Buffer.this + ".outputStream()";
            }
        };
    }

    @Override // okio.BufferedSink
    public Buffer emitCompleteSegments() {
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer emit() {
        return this;
    }

    @Override // okio.BufferedSource
    public boolean exhausted() {
        return this.size == 0;
    }

    @Override // okio.BufferedSource
    public void require(long byteCount) throws EOFException {
        if (this.size < byteCount) {
            throw new EOFException();
        }
    }

    @Override // okio.BufferedSource
    public boolean request(long byteCount) {
        return this.size >= byteCount;
    }

    @Override // okio.BufferedSource
    public BufferedSource peek() {
        return Okio.buffer(new PeekSource(this));
    }

    @Override // okio.BufferedSource
    public InputStream inputStream() {
        return new InputStream() { // from class: okio.Buffer$inputStream$1
            @Override // java.io.InputStream
            public int read() {
                if (Buffer.this.size() > 0) {
                    return Buffer.this.readByte() & 255;
                }
                return -1;
            }

            @Override // java.io.InputStream
            public int read(byte[] sink, int offset, int byteCount) {
                Intrinsics.checkParameterIsNotNull(sink, "sink");
                return Buffer.this.read(sink, offset, byteCount);
            }

            @Override // java.io.InputStream
            public int available() {
                return (int) Math.min(Buffer.this.size(), (long) Integer.MAX_VALUE);
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }

            @Override // java.lang.Object
            public String toString() {
                return Buffer.this + ".inputStream()";
            }
        };
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, OutputStream outputStream, long j, long j2, int i, Object obj) throws IOException {
        long j3;
        long j4 = (i & 2) != 0 ? 0 : j;
        if ((i & 4) != 0) {
            j3 = buffer.size - j4;
        } else {
            j3 = j2;
        }
        return buffer.copyTo(outputStream, j4, j3);
    }

    /* JADX INFO: Multiple debug info for r10v6 int: [D('toCopy' int), D('a$iv' int)] */
    public final Buffer copyTo(OutputStream out, long offset, long byteCount) throws IOException {
        Intrinsics.checkParameterIsNotNull(out, "out");
        long offset2 = offset;
        long byteCount2 = byteCount;
        Util.checkOffsetAndCount(this.size, offset2, byteCount2);
        if (byteCount2 == 0) {
            return this;
        }
        Segment s = this.head;
        while (true) {
            if (s == null) {
                Intrinsics.throwNpe();
            }
            if (offset2 >= ((long) (s.limit - s.pos))) {
                offset2 -= (long) (s.limit - s.pos);
                s = s.next;
            }
        }
        while (byteCount2 > 0) {
            if (s == null) {
                Intrinsics.throwNpe();
            }
            int pos = (int) (((long) s.pos) + offset2);
            int a$iv = (int) Math.min((long) (s.limit - pos), byteCount2);
            out.write(s.data, pos, a$iv);
            byteCount2 -= (long) a$iv;
            offset2 = 0;
            s = s.next;
        }
        return this;
    }

    public final Buffer copyTo(Buffer out, long offset, long byteCount) {
        Intrinsics.checkParameterIsNotNull(out, "out");
        long offset$iv = offset;
        long byteCount$iv = byteCount;
        Util.checkOffsetAndCount(size(), offset$iv, byteCount$iv);
        if (byteCount$iv != 0) {
            out.setSize$okio(out.size() + byteCount$iv);
            Segment s$iv = this.head;
            while (true) {
                if (s$iv == null) {
                    Intrinsics.throwNpe();
                }
                if (offset$iv >= ((long) (s$iv.limit - s$iv.pos))) {
                    offset$iv -= (long) (s$iv.limit - s$iv.pos);
                    s$iv = s$iv.next;
                }
            }
            while (byteCount$iv > 0) {
                if (s$iv == null) {
                    Intrinsics.throwNpe();
                }
                Segment copy$iv = s$iv.sharedCopy();
                copy$iv.pos += (int) offset$iv;
                copy$iv.limit = Math.min(copy$iv.pos + ((int) byteCount$iv), copy$iv.limit);
                Segment segment = out.head;
                if (segment == null) {
                    copy$iv.prev = copy$iv;
                    copy$iv.next = copy$iv.prev;
                    out.head = copy$iv.next;
                } else {
                    if (segment == null) {
                        Intrinsics.throwNpe();
                    }
                    Segment segment2 = segment.prev;
                    if (segment2 == null) {
                        Intrinsics.throwNpe();
                    }
                    segment2.push(copy$iv);
                }
                byteCount$iv -= (long) (copy$iv.limit - copy$iv.pos);
                offset$iv = 0;
                s$iv = s$iv.next;
            }
        }
        return this;
    }

    public final Buffer copyTo(Buffer out, long offset) {
        Intrinsics.checkParameterIsNotNull(out, "out");
        return copyTo(out, offset, this.size - offset);
    }

    public static /* synthetic */ Buffer writeTo$default(Buffer buffer, OutputStream outputStream, long j, int i, Object obj) throws IOException {
        if ((i & 2) != 0) {
            j = buffer.size;
        }
        return buffer.writeTo(outputStream, j);
    }

    /* JADX INFO: Multiple debug info for r1v4 int: [D('toCopy' int), D('b$iv' int)] */
    public final Buffer writeTo(OutputStream out, long byteCount) throws IOException {
        Intrinsics.checkParameterIsNotNull(out, "out");
        long byteCount2 = byteCount;
        Util.checkOffsetAndCount(this.size, 0, byteCount2);
        Segment s = this.head;
        while (byteCount2 > 0) {
            if (s == null) {
                Intrinsics.throwNpe();
            }
            int b$iv = (int) Math.min(byteCount2, (long) (s.limit - s.pos));
            out.write(s.data, s.pos, b$iv);
            s.pos += b$iv;
            this.size -= (long) b$iv;
            byteCount2 -= (long) b$iv;
            if (s.pos == s.limit) {
                s = s.pop();
                this.head = s;
                SegmentPool.INSTANCE.recycle(s);
            }
        }
        return this;
    }

    public final Buffer readFrom(InputStream input) throws IOException {
        Intrinsics.checkParameterIsNotNull(input, "input");
        readFrom(input, Long.MAX_VALUE, true);
        return this;
    }

    public final Buffer readFrom(InputStream input, long byteCount) throws IOException {
        Intrinsics.checkParameterIsNotNull(input, "input");
        if (byteCount >= 0) {
            readFrom(input, byteCount, false);
            return this;
        }
        throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
    }

    /* JADX INFO: Multiple debug info for r3v2 int: [D('b$iv' int), D('maxToCopy' int)] */
    private final void readFrom(InputStream input, long byteCount, boolean forever) throws IOException {
        long byteCount2 = byteCount;
        while (true) {
            if (byteCount2 > 0 || forever) {
                Segment tail = writableSegment$okio(1);
                int bytesRead = input.read(tail.data, tail.limit, (int) Math.min(byteCount2, (long) (8192 - tail.limit)));
                if (bytesRead == -1) {
                    if (tail.pos == tail.limit) {
                        this.head = tail.pop();
                        SegmentPool.INSTANCE.recycle(tail);
                    }
                    if (!forever) {
                        throw new EOFException();
                    }
                    return;
                }
                tail.limit += bytesRead;
                this.size += (long) bytesRead;
                byteCount2 -= (long) bytesRead;
            } else {
                return;
            }
        }
    }

    public final long completeSegmentByteCount() {
        long result$iv = size();
        if (result$iv == 0) {
            return 0;
        }
        Segment segment = this.head;
        if (segment == null) {
            Intrinsics.throwNpe();
        }
        Segment tail$iv = segment.prev;
        if (tail$iv == null) {
            Intrinsics.throwNpe();
        }
        if (tail$iv.limit >= 8192 || !tail$iv.owner) {
            return result$iv;
        }
        return result$iv - ((long) (tail$iv.limit - tail$iv.pos));
    }

    /* JADX INFO: Multiple debug info for r3v1 byte: [D('pos$iv' int), D('b$iv' byte)] */
    @Override // okio.BufferedSource
    public byte readByte() throws EOFException {
        if (size() != 0) {
            Segment segment$iv = this.head;
            if (segment$iv == null) {
                Intrinsics.throwNpe();
            }
            int pos$iv = segment$iv.pos;
            int limit$iv = segment$iv.limit;
            int pos$iv2 = pos$iv + 1;
            byte b$iv = segment$iv.data[pos$iv];
            setSize$okio(size() - 1);
            if (pos$iv2 == limit$iv) {
                this.head = segment$iv.pop();
                SegmentPool.INSTANCE.recycle(segment$iv);
            } else {
                segment$iv.pos = pos$iv2;
            }
            return b$iv;
        }
        throw new EOFException();
    }

    public final byte getByte(long pos) {
        Util.checkOffsetAndCount(size(), pos, 1);
        Segment s$iv$iv = this.head;
        if (s$iv$iv == null) {
            Segment s$iv = null;
            Intrinsics.throwNpe();
            return s$iv.data[(int) ((((long) s$iv.pos) + pos) - -1)];
        } else if (size() - pos < pos) {
            long offset$iv$iv = size();
            while (offset$iv$iv > pos) {
                Segment segment = s$iv$iv.prev;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                s$iv$iv = segment;
                offset$iv$iv -= (long) (s$iv$iv.limit - s$iv$iv.pos);
            }
            if (s$iv$iv == null) {
                Intrinsics.throwNpe();
            }
            return s$iv$iv.data[(int) ((((long) s$iv$iv.pos) + pos) - offset$iv$iv)];
        } else {
            long offset$iv$iv2 = 0;
            while (true) {
                long nextOffset$iv$iv = ((long) (s$iv$iv.limit - s$iv$iv.pos)) + offset$iv$iv2;
                if (nextOffset$iv$iv > pos) {
                    break;
                }
                Segment s$iv2 = s$iv$iv.next;
                if (s$iv2 == null) {
                    Intrinsics.throwNpe();
                }
                s$iv$iv = s$iv2;
                offset$iv$iv2 = nextOffset$iv$iv;
            }
            if (s$iv$iv == null) {
                Intrinsics.throwNpe();
            }
            return s$iv$iv.data[(int) ((((long) s$iv$iv.pos) + pos) - offset$iv$iv2)];
        }
    }

    /* JADX INFO: Multiple debug info for r3v1 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r8v2 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    @Override // okio.BufferedSource
    public short readShort() throws EOFException {
        if (size() >= 2) {
            Segment segment$iv = this.head;
            if (segment$iv == null) {
                Intrinsics.throwNpe();
            }
            int pos$iv = segment$iv.pos;
            int limit$iv = segment$iv.limit;
            if (limit$iv - pos$iv < 2) {
                return (short) (((readByte() & 255) << 8) | (readByte() & 255));
            }
            byte[] data$iv = segment$iv.data;
            int pos$iv2 = pos$iv + 1;
            int pos$iv3 = pos$iv2 + 1;
            int s$iv = ((data$iv[pos$iv] & 255) << 8) | (data$iv[pos$iv2] & 255);
            setSize$okio(size() - 2);
            if (pos$iv3 == limit$iv) {
                this.head = segment$iv.pop();
                SegmentPool.INSTANCE.recycle(segment$iv);
            } else {
                segment$iv.pos = pos$iv3;
            }
            return (short) s$iv;
        }
        throw new EOFException();
    }

    /* JADX INFO: Multiple debug info for r3v1 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r8v1 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r8v5 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r9v2 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    @Override // okio.BufferedSource
    public int readInt() throws EOFException {
        if (size() >= 4) {
            Segment segment$iv = this.head;
            if (segment$iv == null) {
                Intrinsics.throwNpe();
            }
            int pos$iv = segment$iv.pos;
            int limit$iv = segment$iv.limit;
            if (((long) (limit$iv - pos$iv)) < 4) {
                return ((readByte() & 255) << 24) | ((readByte() & 255) << 16) | ((readByte() & 255) << 8) | (readByte() & 255);
            }
            byte[] data$iv = segment$iv.data;
            int pos$iv2 = pos$iv + 1;
            int pos$iv3 = pos$iv2 + 1;
            int i = ((data$iv[pos$iv] & 255) << 24) | ((data$iv[pos$iv2] & 255) << 16);
            int pos$iv4 = pos$iv3 + 1;
            int i2 = i | ((data$iv[pos$iv3] & 255) << 8);
            int pos$iv5 = pos$iv4 + 1;
            int i$iv = i2 | (data$iv[pos$iv4] & 255);
            setSize$okio(size() - 4);
            if (pos$iv5 == limit$iv) {
                this.head = segment$iv.pop();
                SegmentPool.INSTANCE.recycle(segment$iv);
                return i$iv;
            }
            segment$iv.pos = pos$iv5;
            return i$iv;
        }
        throw new EOFException();
    }

    /* JADX INFO: Multiple debug info for r3v1 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r3v4 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r5v1 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r5v4 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r8v3 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r8v7 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r9v1 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    /* JADX INFO: Multiple debug info for r9v4 byte: [D('$this$and$iv$iv' byte), D('pos$iv' int)] */
    @Override // okio.BufferedSource
    public long readLong() throws EOFException {
        if (size() >= 8) {
            Segment segment$iv = this.head;
            if (segment$iv == null) {
                Intrinsics.throwNpe();
            }
            int pos$iv = segment$iv.pos;
            int limit$iv = segment$iv.limit;
            if (((long) (limit$iv - pos$iv)) < 8) {
                return ((((long) readInt()) & 4294967295L) << 32) | (((long) readInt()) & 4294967295L);
            }
            byte[] data$iv = segment$iv.data;
            int pos$iv2 = pos$iv + 1;
            int pos$iv3 = pos$iv2 + 1;
            long j = (((long) data$iv[pos$iv2]) & 255) << 48;
            int pos$iv4 = pos$iv3 + 1;
            int pos$iv5 = pos$iv4 + 1;
            int pos$iv6 = pos$iv5 + 1;
            int pos$iv7 = pos$iv6 + 1;
            long j2 = j | ((255 & ((long) data$iv[pos$iv])) << 56) | ((255 & ((long) data$iv[pos$iv3])) << 40) | ((((long) data$iv[pos$iv4]) & 255) << 32) | ((255 & ((long) data$iv[pos$iv5])) << 24) | ((((long) data$iv[pos$iv6]) & 255) << 16);
            int pos$iv8 = pos$iv7 + 1;
            int pos$iv9 = pos$iv8 + 1;
            long v$iv = j2 | ((255 & ((long) data$iv[pos$iv7])) << 8) | (((long) data$iv[pos$iv8]) & 255);
            setSize$okio(size() - 8);
            if (pos$iv9 == limit$iv) {
                this.head = segment$iv.pop();
                SegmentPool.INSTANCE.recycle(segment$iv);
                return v$iv;
            }
            segment$iv.pos = pos$iv9;
            return v$iv;
        }
        throw new EOFException();
    }

    @Override // okio.BufferedSource
    public short readShortLe() throws EOFException {
        return Util.reverseBytes(readShort());
    }

    @Override // okio.BufferedSource
    public int readIntLe() throws EOFException {
        return Util.reverseBytes(readInt());
    }

    @Override // okio.BufferedSource
    public long readLongLe() throws EOFException {
        return Util.reverseBytes(readLong());
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x00f2, code lost:
        r1.setSize$okio(r1.size() - ((long) r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00fb, code lost:
        if (r5 == false) goto L_0x00fe;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0100, code lost:
        return -r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:?, code lost:
        return r2;
     */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00e2  */
    @Override // okio.BufferedSource
    /* Code decompiled incorrectly, please refer to instructions dump */
    public long readDecimalLong() throws EOFException {
        Buffer $this$commonReadDecimalLong$iv;
        int $i$f$commonReadDecimalLong;
        Buffer $this$commonReadDecimalLong$iv2;
        byte b$iv;
        byte[] data$iv;
        boolean done$iv;
        Buffer $this$commonReadDecimalLong$iv3 = this;
        int $i$f$commonReadDecimalLong2 = 0;
        if ($this$commonReadDecimalLong$iv3.size() != 0) {
            long value$iv = 0;
            int seen$iv = 0;
            boolean negative$iv = false;
            boolean done$iv2 = false;
            long overflowDigit$iv = -7;
            loop0: while (true) {
                Segment segment$iv = $this$commonReadDecimalLong$iv3.head;
                if (segment$iv == null) {
                    Intrinsics.throwNpe();
                }
                byte[] data$iv2 = segment$iv.data;
                int pos$iv = segment$iv.pos;
                int limit$iv = segment$iv.limit;
                while (pos$iv < limit$iv) {
                    b$iv = data$iv2[pos$iv];
                    byte b = (byte) 48;
                    if (b$iv < b || b$iv > ((byte) 57)) {
                        $this$commonReadDecimalLong$iv = $this$commonReadDecimalLong$iv3;
                        $i$f$commonReadDecimalLong = $i$f$commonReadDecimalLong2;
                        done$iv = done$iv2;
                        data$iv = data$iv2;
                        if (b$iv == ((byte) 45) && seen$iv == 0) {
                            negative$iv = true;
                            overflowDigit$iv--;
                        } else if (seen$iv != 0) {
                            done$iv2 = true;
                            if (pos$iv != limit$iv) {
                                $this$commonReadDecimalLong$iv2 = $this$commonReadDecimalLong$iv;
                                $this$commonReadDecimalLong$iv2.head = segment$iv.pop();
                                SegmentPool.INSTANCE.recycle(segment$iv);
                            } else {
                                $this$commonReadDecimalLong$iv2 = $this$commonReadDecimalLong$iv;
                                segment$iv.pos = pos$iv;
                            }
                            if (done$iv2 || $this$commonReadDecimalLong$iv2.head == null) {
                                break;
                            }
                            $this$commonReadDecimalLong$iv3 = $this$commonReadDecimalLong$iv2;
                            $i$f$commonReadDecimalLong2 = $i$f$commonReadDecimalLong;
                        } else {
                            throw new NumberFormatException("Expected leading [0-9] or '-' character but was 0x" + Util.toHexString(b$iv));
                        }
                    } else {
                        int digit$iv = b - b$iv;
                        if (value$iv < BufferKt.OVERFLOW_ZONE) {
                            break loop0;
                        }
                        if (value$iv == BufferKt.OVERFLOW_ZONE) {
                            $this$commonReadDecimalLong$iv = $this$commonReadDecimalLong$iv3;
                            $i$f$commonReadDecimalLong = $i$f$commonReadDecimalLong2;
                            if (((long) digit$iv) < overflowDigit$iv) {
                                break loop0;
                            }
                        } else {
                            $this$commonReadDecimalLong$iv = $this$commonReadDecimalLong$iv3;
                            $i$f$commonReadDecimalLong = $i$f$commonReadDecimalLong2;
                        }
                        value$iv = (value$iv * 10) + ((long) digit$iv);
                        done$iv = done$iv2;
                        data$iv = data$iv2;
                    }
                    pos$iv++;
                    seen$iv++;
                    $this$commonReadDecimalLong$iv3 = $this$commonReadDecimalLong$iv;
                    $i$f$commonReadDecimalLong2 = $i$f$commonReadDecimalLong;
                    done$iv2 = done$iv;
                    data$iv2 = data$iv;
                }
                $this$commonReadDecimalLong$iv = $this$commonReadDecimalLong$iv3;
                $i$f$commonReadDecimalLong = $i$f$commonReadDecimalLong2;
                if (pos$iv != limit$iv) {
                }
                if (done$iv2) {
                    break;
                }
                break;
            }
            Buffer buffer$iv = new Buffer().writeDecimalLong(value$iv).writeByte((int) b$iv);
            if (!negative$iv) {
                buffer$iv.readByte();
            }
            throw new NumberFormatException("Number too large: " + buffer$iv.readUtf8());
        }
        throw new EOFException();
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00c1 A[EDGE_INSN: B:46:0x00c1->B:40:0x00c1 ?: BREAK  , SYNTHETIC] */
    @Override // okio.BufferedSource
    /* Code decompiled incorrectly, please refer to instructions dump */
    public long readHexadecimalUnsignedLong() throws EOFException {
        int digit$iv;
        if (size() != 0) {
            long value$iv = 0;
            int seen$iv = 0;
            boolean done$iv = false;
            do {
                Segment segment$iv = this.head;
                if (segment$iv == null) {
                    Intrinsics.throwNpe();
                }
                byte[] data$iv = segment$iv.data;
                int pos$iv = segment$iv.pos;
                int limit$iv = segment$iv.limit;
                while (pos$iv < limit$iv) {
                    byte b$iv = data$iv[pos$iv];
                    byte b = (byte) 48;
                    if (b$iv < b || b$iv > ((byte) 57)) {
                        byte b2 = (byte) 97;
                        if (b$iv < b2 || b$iv > ((byte) 102)) {
                            byte b3 = (byte) 65;
                            if (b$iv >= b3 && b$iv <= ((byte) 70)) {
                                digit$iv = (b$iv - b3) + 10;
                            } else if (seen$iv != 0) {
                                done$iv = true;
                                if (pos$iv != limit$iv) {
                                    this.head = segment$iv.pop();
                                    SegmentPool.INSTANCE.recycle(segment$iv);
                                } else {
                                    segment$iv.pos = pos$iv;
                                }
                                if (!done$iv) {
                                    break;
                                }
                            } else {
                                throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + Util.toHexString(b$iv));
                            }
                        } else {
                            digit$iv = (b$iv - b2) + 10;
                        }
                    } else {
                        digit$iv = b$iv - b;
                    }
                    if ((-1152921504606846976L & value$iv) == 0) {
                        value$iv = (value$iv << 4) | ((long) digit$iv);
                        pos$iv++;
                        seen$iv++;
                    } else {
                        Buffer buffer$iv = new Buffer().writeHexadecimalUnsignedLong(value$iv).writeByte((int) b$iv);
                        throw new NumberFormatException("Number too large: " + buffer$iv.readUtf8());
                    }
                }
                if (pos$iv != limit$iv) {
                }
                if (!done$iv) {
                }
            } while (this.head != null);
            setSize$okio(size() - ((long) seen$iv));
            return value$iv;
        }
        throw new EOFException();
    }

    @Override // okio.BufferedSource
    public ByteString readByteString() {
        return readByteString(size());
    }

    @Override // okio.BufferedSource
    public ByteString readByteString(long byteCount) throws EOFException {
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if (size() < byteCount) {
            throw new EOFException();
        } else if (byteCount < ((long) 4096)) {
            return new ByteString(readByteArray(byteCount));
        } else {
            ByteString snapshot = snapshot((int) byteCount);
            skip(byteCount);
            return snapshot;
        }
    }

    @Override // okio.BufferedSource
    public int select(Options options) {
        Intrinsics.checkParameterIsNotNull(options, "options");
        int index$iv = BufferKt.selectPrefix$default(this, options, false, 2, null);
        if (index$iv == -1) {
            return -1;
        }
        skip((long) options.getByteStrings$okio()[index$iv].size());
        return index$iv;
    }

    @Override // okio.BufferedSource
    public void readFully(Buffer sink, long byteCount) throws EOFException {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        if (size() >= byteCount) {
            sink.write(this, byteCount);
        } else {
            sink.write(this, size());
            throw new EOFException();
        }
    }

    @Override // okio.BufferedSource
    public long readAll(Sink sink) throws IOException {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        long byteCount$iv = size();
        if (byteCount$iv > 0) {
            sink.write(this, byteCount$iv);
        }
        return byteCount$iv;
    }

    @Override // okio.BufferedSource
    public String readUtf8() {
        return readString(this.size, Charsets.UTF_8);
    }

    @Override // okio.BufferedSource
    public String readUtf8(long byteCount) throws EOFException {
        return readString(byteCount, Charsets.UTF_8);
    }

    @Override // okio.BufferedSource
    public String readString(Charset charset) {
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return readString(this.size, charset);
    }

    @Override // okio.BufferedSource
    public String readString(long byteCount, Charset charset) throws EOFException {
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if (this.size < byteCount) {
            throw new EOFException();
        } else if (byteCount == 0) {
            return "";
        } else {
            Segment s = this.head;
            if (s == null) {
                Intrinsics.throwNpe();
            }
            if (((long) s.pos) + byteCount > ((long) s.limit)) {
                return new String(readByteArray(byteCount), charset);
            }
            String result = new String(s.data, s.pos, (int) byteCount, charset);
            s.pos += (int) byteCount;
            this.size -= byteCount;
            if (s.pos == s.limit) {
                this.head = s.pop();
                SegmentPool.INSTANCE.recycle(s);
            }
            return result;
        }
    }

    @Override // okio.BufferedSource
    public String readUtf8Line() throws EOFException {
        long newline$iv = indexOf((byte) 10);
        if (newline$iv != -1) {
            return BufferKt.readUtf8Line(this, newline$iv);
        }
        if (size() != 0) {
            return readUtf8(size());
        }
        return null;
    }

    @Override // okio.BufferedSource
    public String readUtf8LineStrict() throws EOFException {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    @Override // okio.BufferedSource
    public String readUtf8LineStrict(long limit) throws EOFException {
        if (limit >= 0) {
            long scanLength$iv = Long.MAX_VALUE;
            if (limit != Long.MAX_VALUE) {
                scanLength$iv = limit + 1;
            }
            byte b = (byte) 10;
            long newline$iv = indexOf(b, 0, scanLength$iv);
            if (newline$iv != -1) {
                return BufferKt.readUtf8Line(this, newline$iv);
            }
            if (scanLength$iv < size() && getByte(scanLength$iv - 1) == ((byte) 13) && getByte(scanLength$iv) == b) {
                return BufferKt.readUtf8Line(this, scanLength$iv);
            }
            Buffer data$iv = new Buffer();
            copyTo(data$iv, 0, Math.min((long) 32, size()));
            throw new EOFException("\\n not found: limit=" + Math.min(size(), limit) + " content=" + data$iv.readByteString().hex() + Typography.ellipsis);
        }
        throw new IllegalArgumentException(("limit < 0: " + limit).toString());
    }

    @Override // okio.BufferedSource
    public int readUtf8CodePoint() throws EOFException {
        int min$iv;
        int byteCount$iv;
        int codePoint$iv;
        if (size() != 0) {
            byte b0$iv = getByte(0);
            if ((128 & b0$iv) == 0) {
                codePoint$iv = b0$iv & Byte.MAX_VALUE;
                byteCount$iv = 1;
                min$iv = 0;
            } else if ((224 & b0$iv) == 192) {
                codePoint$iv = b0$iv & Ascii.US;
                byteCount$iv = 2;
                min$iv = 128;
            } else if ((240 & b0$iv) == 224) {
                codePoint$iv = b0$iv & Ascii.SI;
                byteCount$iv = 3;
                min$iv = 2048;
            } else if ((248 & b0$iv) == 240) {
                codePoint$iv = b0$iv & 7;
                byteCount$iv = 4;
                min$iv = 65536;
            } else {
                skip(1);
                return Utf8.REPLACEMENT_CODE_POINT;
            }
            if (size() >= ((long) byteCount$iv)) {
                for (int i$iv = 1; i$iv < byteCount$iv; i$iv++) {
                    byte b$iv = getByte((long) i$iv);
                    if ((192 & b$iv) == 128) {
                        codePoint$iv = (codePoint$iv << 6) | (63 & b$iv);
                    } else {
                        skip((long) i$iv);
                        return Utf8.REPLACEMENT_CODE_POINT;
                    }
                }
                skip((long) byteCount$iv);
                if (codePoint$iv > 1114111) {
                    return Utf8.REPLACEMENT_CODE_POINT;
                }
                if ((55296 <= codePoint$iv && 57343 >= codePoint$iv) || codePoint$iv < min$iv) {
                    return Utf8.REPLACEMENT_CODE_POINT;
                }
                return codePoint$iv;
            }
            throw new EOFException("size < " + byteCount$iv + ": " + size() + " (to read code point prefixed 0x" + Util.toHexString(b0$iv) + ')');
        }
        throw new EOFException();
    }

    @Override // okio.BufferedSource
    public byte[] readByteArray() {
        return readByteArray(size());
    }

    @Override // okio.BufferedSource
    public byte[] readByteArray(long byteCount) throws EOFException {
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if (size() >= byteCount) {
            byte[] result$iv = new byte[(int) byteCount];
            readFully(result$iv);
            return result$iv;
        } else {
            throw new EOFException();
        }
    }

    @Override // okio.BufferedSource
    public int read(byte[] sink) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        return read(sink, 0, sink.length);
    }

    @Override // okio.BufferedSource
    public void readFully(byte[] sink) throws EOFException {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        int offset$iv = 0;
        while (offset$iv < sink.length) {
            int read$iv = read(sink, offset$iv, sink.length - offset$iv);
            if (read$iv != -1) {
                offset$iv += read$iv;
            } else {
                throw new EOFException();
            }
        }
    }

    @Override // okio.BufferedSource
    public int read(byte[] sink, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        Util.checkOffsetAndCount((long) sink.length, (long) offset, (long) byteCount);
        Segment s$iv = this.head;
        if (s$iv == null) {
            return -1;
        }
        int toCopy$iv = Math.min(byteCount, s$iv.limit - s$iv.pos);
        ArraysKt.copyInto(s$iv.data, sink, offset, s$iv.pos, s$iv.pos + toCopy$iv);
        s$iv.pos += toCopy$iv;
        setSize$okio(size() - ((long) toCopy$iv));
        if (s$iv.pos != s$iv.limit) {
            return toCopy$iv;
        }
        this.head = s$iv.pop();
        SegmentPool.INSTANCE.recycle(s$iv);
        return toCopy$iv;
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer sink) throws IOException {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        Segment s = this.head;
        if (s == null) {
            return -1;
        }
        int toCopy = Math.min(sink.remaining(), s.limit - s.pos);
        sink.put(s.data, s.pos, toCopy);
        s.pos += toCopy;
        this.size -= (long) toCopy;
        if (s.pos == s.limit) {
            this.head = s.pop();
            SegmentPool.INSTANCE.recycle(s);
        }
        return toCopy;
    }

    public final void clear() {
        skip(size());
    }

    /* JADX INFO: Multiple debug info for r5v2 int: [D('b$iv$iv' int), D('toSkip$iv' int)] */
    @Override // okio.BufferedSource
    public void skip(long byteCount) throws EOFException {
        long byteCount$iv = byteCount;
        while (byteCount$iv > 0) {
            Segment head$iv = this.head;
            if (head$iv != null) {
                int toSkip$iv = (int) Math.min(byteCount$iv, (long) (head$iv.limit - head$iv.pos));
                setSize$okio(size() - ((long) toSkip$iv));
                byteCount$iv -= (long) toSkip$iv;
                head$iv.pos += toSkip$iv;
                if (head$iv.pos == head$iv.limit) {
                    this.head = head$iv.pop();
                    SegmentPool.INSTANCE.recycle(head$iv);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    @Override // okio.BufferedSink
    public Buffer write(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "byteString");
        byteString.write$okio(this, 0, byteString.size());
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer write(ByteString byteString, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull(byteString, "byteString");
        byteString.write$okio(this, offset, byteCount);
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeUtf8(String string) {
        Intrinsics.checkParameterIsNotNull(string, "string");
        return writeUtf8(string, 0, string.length());
    }

    @Override // okio.BufferedSink
    public Buffer writeUtf8(String string, int beginIndex, int endIndex) {
        int runLimit$iv;
        Intrinsics.checkParameterIsNotNull(string, "string");
        int i = 1;
        if (beginIndex >= 0) {
            if (endIndex >= beginIndex) {
                if (endIndex <= string.length()) {
                    int runSize$iv = beginIndex;
                    while (runSize$iv < endIndex) {
                        int c$iv = string.charAt(runSize$iv);
                        if (c$iv < 128) {
                            Segment tail$iv = writableSegment$okio(i);
                            byte[] data$iv = tail$iv.data;
                            int segmentOffset$iv = tail$iv.limit - runSize$iv;
                            int runLimit$iv2 = Math.min(endIndex, 8192 - segmentOffset$iv);
                            int i$iv = runSize$iv + 1;
                            data$iv[runSize$iv + segmentOffset$iv] = (byte) c$iv;
                            while (i$iv < runLimit$iv2) {
                                int c$iv2 = string.charAt(i$iv);
                                if (c$iv2 >= 128) {
                                    break;
                                }
                                data$iv[i$iv + segmentOffset$iv] = (byte) c$iv2;
                                i$iv++;
                            }
                            int runSize$iv2 = (i$iv + segmentOffset$iv) - tail$iv.limit;
                            tail$iv.limit += runSize$iv2;
                            setSize$okio(((long) runSize$iv2) + size());
                            runSize$iv = i$iv;
                            runLimit$iv = 1;
                        } else if (c$iv < 2048) {
                            Segment tail$iv2 = writableSegment$okio(2);
                            tail$iv2.data[tail$iv2.limit] = (byte) ((c$iv >> 6) | 192);
                            tail$iv2.data[tail$iv2.limit + 1] = (byte) (128 | (c$iv & 63));
                            tail$iv2.limit += 2;
                            setSize$okio(size() + 2);
                            runSize$iv++;
                            runLimit$iv = 1;
                        } else if (c$iv < 55296 || c$iv > 57343) {
                            Segment tail$iv3 = writableSegment$okio(3);
                            tail$iv3.data[tail$iv3.limit] = (byte) ((c$iv >> 12) | 224);
                            runLimit$iv = 1;
                            tail$iv3.data[tail$iv3.limit + 1] = (byte) ((63 & (c$iv >> 6)) | 128);
                            tail$iv3.data[tail$iv3.limit + 2] = (byte) ((c$iv & 63) | 128);
                            tail$iv3.limit += 3;
                            setSize$okio(size() + 3);
                            runSize$iv++;
                        } else {
                            int low$iv = runSize$iv + 1 < endIndex ? string.charAt(runSize$iv + 1) : 0;
                            if (c$iv > 56319 || 56320 > low$iv || 57343 < low$iv) {
                                writeByte(63);
                                runSize$iv++;
                                runLimit$iv = 1;
                            } else {
                                int codePoint$iv = (((c$iv & 1023) << 10) | (low$iv & 1023)) + 65536;
                                Segment tail$iv4 = writableSegment$okio(4);
                                tail$iv4.data[tail$iv4.limit] = (byte) ((codePoint$iv >> 18) | 240);
                                tail$iv4.data[tail$iv4.limit + 1] = (byte) (((codePoint$iv >> 12) & 63) | 128);
                                tail$iv4.data[tail$iv4.limit + 2] = (byte) (((codePoint$iv >> 6) & 63) | 128);
                                tail$iv4.data[tail$iv4.limit + 3] = (byte) (128 | (codePoint$iv & 63));
                                tail$iv4.limit += 4;
                                setSize$okio(size() + 4);
                                runSize$iv += 2;
                                runLimit$iv = 1;
                            }
                        }
                        i = runLimit$iv;
                    }
                    return this;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + endIndex + " > " + string.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + endIndex + " < " + beginIndex).toString());
        }
        throw new IllegalArgumentException(("beginIndex < 0: " + beginIndex).toString());
    }

    @Override // okio.BufferedSink
    public Buffer writeUtf8CodePoint(int codePoint) {
        if (codePoint < 128) {
            writeByte(codePoint);
        } else if (codePoint < 2048) {
            Segment tail$iv = writableSegment$okio(2);
            tail$iv.data[tail$iv.limit] = (byte) ((codePoint >> 6) | 192);
            tail$iv.data[tail$iv.limit + 1] = (byte) (128 | (codePoint & 63));
            tail$iv.limit += 2;
            setSize$okio(size() + 2);
        } else if (55296 <= codePoint && 57343 >= codePoint) {
            writeByte(63);
        } else if (codePoint < 65536) {
            Segment tail$iv2 = writableSegment$okio(3);
            tail$iv2.data[tail$iv2.limit] = (byte) ((codePoint >> 12) | 224);
            tail$iv2.data[tail$iv2.limit + 1] = (byte) ((63 & (codePoint >> 6)) | 128);
            tail$iv2.data[tail$iv2.limit + 2] = (byte) (128 | (codePoint & 63));
            tail$iv2.limit += 3;
            setSize$okio(size() + 3);
        } else if (codePoint <= 1114111) {
            Segment tail$iv3 = writableSegment$okio(4);
            tail$iv3.data[tail$iv3.limit] = (byte) ((codePoint >> 18) | 240);
            tail$iv3.data[tail$iv3.limit + 1] = (byte) (((codePoint >> 12) & 63) | 128);
            tail$iv3.data[tail$iv3.limit + 2] = (byte) (((codePoint >> 6) & 63) | 128);
            tail$iv3.data[tail$iv3.limit + 3] = (byte) (128 | (codePoint & 63));
            tail$iv3.limit += 4;
            setSize$okio(size() + 4);
        } else {
            throw new IllegalArgumentException("Unexpected code point: 0x" + Util.toHexString(codePoint));
        }
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeString(String string, Charset charset) {
        Intrinsics.checkParameterIsNotNull(string, "string");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return writeString(string, 0, string.length(), charset);
    }

    @Override // okio.BufferedSink
    public Buffer writeString(String string, int beginIndex, int endIndex, Charset charset) {
        Intrinsics.checkParameterIsNotNull(string, "string");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex >= beginIndex) {
                if (endIndex > string.length()) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException(("endIndex > string.length: " + endIndex + " > " + string.length()).toString());
                } else if (Intrinsics.areEqual(charset, Charsets.UTF_8)) {
                    return writeUtf8(string, beginIndex, endIndex);
                } else {
                    String substring = string.substring(beginIndex, endIndex);
                    Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    if (substring != null) {
                        byte[] data = substring.getBytes(charset);
                        Intrinsics.checkExpressionValueIsNotNull(data, "(this as java.lang.String).getBytes(charset)");
                        return write(data, 0, data.length);
                    }
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
            } else {
                throw new IllegalArgumentException(("endIndex < beginIndex: " + endIndex + " < " + beginIndex).toString());
            }
        } else {
            throw new IllegalArgumentException(("beginIndex < 0: " + beginIndex).toString());
        }
    }

    @Override // okio.BufferedSink
    public Buffer write(byte[] source) {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        return write(source, 0, source.length);
    }

    @Override // okio.BufferedSink
    public Buffer write(byte[] source, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        int offset$iv = offset;
        Util.checkOffsetAndCount((long) source.length, (long) offset$iv, (long) byteCount);
        int limit$iv = offset$iv + byteCount;
        while (offset$iv < limit$iv) {
            Segment tail$iv = writableSegment$okio(1);
            int toCopy$iv = Math.min(limit$iv - offset$iv, 8192 - tail$iv.limit);
            ArraysKt.copyInto(source, tail$iv.data, tail$iv.limit, offset$iv, offset$iv + toCopy$iv);
            offset$iv += toCopy$iv;
            tail$iv.limit += toCopy$iv;
        }
        setSize$okio(size() + ((long) byteCount));
        return this;
    }

    @Override // java.nio.channels.WritableByteChannel
    public int write(ByteBuffer source) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        int byteCount = source.remaining();
        int remaining = byteCount;
        while (remaining > 0) {
            Segment tail = writableSegment$okio(1);
            int toCopy = Math.min(remaining, 8192 - tail.limit);
            source.get(tail.data, tail.limit, toCopy);
            remaining -= toCopy;
            tail.limit += toCopy;
        }
        this.size += (long) byteCount;
        return byteCount;
    }

    @Override // okio.BufferedSink
    public long writeAll(Source source) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        long totalBytesRead$iv = 0;
        while (true) {
            long readCount$iv = source.read(this, (long) 8192);
            if (readCount$iv == -1) {
                return totalBytesRead$iv;
            }
            totalBytesRead$iv += readCount$iv;
        }
    }

    @Override // okio.BufferedSink
    public Buffer write(Source source, long byteCount) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        long byteCount$iv = byteCount;
        while (byteCount$iv > 0) {
            long read$iv = source.read(this, byteCount$iv);
            if (read$iv != -1) {
                byteCount$iv -= read$iv;
            } else {
                throw new EOFException();
            }
        }
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeByte(int b) {
        Segment tail$iv = writableSegment$okio(1);
        byte[] bArr = tail$iv.data;
        int i = tail$iv.limit;
        tail$iv.limit = i + 1;
        bArr[i] = (byte) b;
        setSize$okio(size() + 1);
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeShort(int s) {
        Segment tail$iv = writableSegment$okio(2);
        byte[] data$iv = tail$iv.data;
        int limit$iv = tail$iv.limit;
        int limit$iv2 = limit$iv + 1;
        data$iv[limit$iv] = (byte) ((s >>> 8) & 255);
        data$iv[limit$iv2] = (byte) (s & 255);
        tail$iv.limit = limit$iv2 + 1;
        setSize$okio(size() + 2);
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeShortLe(int s) {
        return writeShort((int) Util.reverseBytes((short) s));
    }

    @Override // okio.BufferedSink
    public Buffer writeInt(int i) {
        Segment tail$iv = writableSegment$okio(4);
        byte[] data$iv = tail$iv.data;
        int limit$iv = tail$iv.limit;
        int limit$iv2 = limit$iv + 1;
        data$iv[limit$iv] = (byte) ((i >>> 24) & 255);
        int limit$iv3 = limit$iv2 + 1;
        data$iv[limit$iv2] = (byte) ((i >>> 16) & 255);
        int limit$iv4 = limit$iv3 + 1;
        data$iv[limit$iv3] = (byte) ((i >>> 8) & 255);
        data$iv[limit$iv4] = (byte) (i & 255);
        tail$iv.limit = limit$iv4 + 1;
        setSize$okio(size() + 4);
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeIntLe(int i) {
        return writeInt(Util.reverseBytes(i));
    }

    @Override // okio.BufferedSink
    public Buffer writeLong(long v) {
        Segment tail$iv = writableSegment$okio(8);
        byte[] data$iv = tail$iv.data;
        int limit$iv = tail$iv.limit;
        int limit$iv2 = limit$iv + 1;
        data$iv[limit$iv] = (byte) ((int) ((v >>> 56) & 255));
        int limit$iv3 = limit$iv2 + 1;
        data$iv[limit$iv2] = (byte) ((int) ((v >>> 48) & 255));
        int limit$iv4 = limit$iv3 + 1;
        data$iv[limit$iv3] = (byte) ((int) ((v >>> 40) & 255));
        int limit$iv5 = limit$iv4 + 1;
        data$iv[limit$iv4] = (byte) ((int) ((v >>> 32) & 255));
        int limit$iv6 = limit$iv5 + 1;
        data$iv[limit$iv5] = (byte) ((int) ((v >>> 24) & 255));
        int limit$iv7 = limit$iv6 + 1;
        data$iv[limit$iv6] = (byte) ((int) ((v >>> 16) & 255));
        int limit$iv8 = limit$iv7 + 1;
        data$iv[limit$iv7] = (byte) ((int) ((v >>> 8) & 255));
        data$iv[limit$iv8] = (byte) ((int) (v & 255));
        tail$iv.limit = limit$iv8 + 1;
        setSize$okio(size() + 8);
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeLongLe(long v) {
        return writeLong(Util.reverseBytes(v));
    }

    @Override // okio.BufferedSink
    public Buffer writeDecimalLong(long v) {
        int width$iv;
        long v$iv = v;
        if (v$iv == 0) {
            return writeByte(48);
        }
        boolean negative$iv = false;
        if (v$iv < 0) {
            v$iv = -v$iv;
            if (v$iv < 0) {
                return writeUtf8("-9223372036854775808");
            }
            negative$iv = true;
        }
        if (v$iv < 100000000) {
            width$iv = v$iv < 10000 ? v$iv < 100 ? v$iv < 10 ? 1 : 2 : v$iv < 1000 ? 3 : 4 : v$iv < 1000000 ? v$iv < 100000 ? 5 : 6 : v$iv < 10000000 ? 7 : 8;
        } else if (v$iv < 1000000000000L) {
            if (v$iv < RealConnection.IDLE_CONNECTION_HEALTHY_NS) {
                width$iv = v$iv < 1000000000 ? 9 : 10;
            } else if (v$iv < 100000000000L) {
                width$iv = 11;
            } else {
                width$iv = 12;
            }
        } else if (v$iv < 1000000000000000L) {
            if (v$iv < 10000000000000L) {
                width$iv = 13;
            } else if (v$iv < 100000000000000L) {
                width$iv = 14;
            } else {
                width$iv = 15;
            }
        } else if (v$iv < 100000000000000000L) {
            if (v$iv < 10000000000000000L) {
                width$iv = 16;
            } else {
                width$iv = 17;
            }
        } else if (v$iv < 1000000000000000000L) {
            width$iv = 18;
        } else {
            width$iv = 19;
        }
        if (negative$iv) {
            width$iv++;
        }
        Segment tail$iv = writableSegment$okio(width$iv);
        byte[] data$iv = tail$iv.data;
        int pos$iv = tail$iv.limit + width$iv;
        while (v$iv != 0) {
            long j = (long) 10;
            pos$iv--;
            data$iv[pos$iv] = BufferKt.getHEX_DIGIT_BYTES()[(int) (v$iv % j)];
            v$iv /= j;
        }
        if (negative$iv) {
            data$iv[pos$iv - 1] = (byte) 45;
        }
        tail$iv.limit += width$iv;
        setSize$okio(size() + ((long) width$iv));
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeHexadecimalUnsignedLong(long v) {
        long v$iv = v;
        if (v$iv == 0) {
            return writeByte(48);
        }
        long x$iv = v$iv | (v$iv >>> 1);
        long x$iv2 = x$iv | (x$iv >>> 2);
        long x$iv3 = x$iv2 | (x$iv2 >>> 4);
        long x$iv4 = x$iv3 | (x$iv3 >>> 8);
        long x$iv5 = x$iv4 | (x$iv4 >>> 16);
        long x$iv6 = x$iv5 | (x$iv5 >>> 32);
        long x$iv7 = x$iv6 - ((x$iv6 >>> 1) & 6148914691236517205L);
        long x$iv8 = ((x$iv7 >>> 2) & 3689348814741910323L) + (3689348814741910323L & x$iv7);
        long x$iv9 = ((x$iv8 >>> 4) + x$iv8) & 1085102592571150095L;
        long x$iv10 = x$iv9 + (x$iv9 >>> 8);
        long x$iv11 = x$iv10 + (x$iv10 >>> 16);
        int width$iv = (int) ((((long) 3) + ((x$iv11 & 63) + (63 & (x$iv11 >>> 32)))) / ((long) 4));
        Segment tail$iv = writableSegment$okio(width$iv);
        byte[] data$iv = tail$iv.data;
        int start$iv = tail$iv.limit;
        for (int pos$iv = (tail$iv.limit + width$iv) - 1; pos$iv >= start$iv; pos$iv--) {
            data$iv[pos$iv] = BufferKt.getHEX_DIGIT_BYTES()[(int) (15 & v$iv)];
            v$iv >>>= 4;
        }
        tail$iv.limit += width$iv;
        setSize$okio(size() + ((long) width$iv));
        return this;
    }

    /* JADX INFO: Multiple debug info for r2v10 okio.Segment: [D('result$iv' okio.Segment), D('tail$iv' okio.Segment)] */
    public final Segment writableSegment$okio(int minimumCapacity) {
        boolean z = true;
        if (minimumCapacity < 1 || minimumCapacity > 8192) {
            z = false;
        }
        if (z) {
            Segment segment = this.head;
            if (segment == null) {
                Segment tail$iv = SegmentPool.INSTANCE.take();
                this.head = tail$iv;
                tail$iv.prev = tail$iv;
                tail$iv.next = tail$iv;
                return tail$iv;
            }
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            Segment tail$iv2 = segment.prev;
            if (tail$iv2 == null) {
                Intrinsics.throwNpe();
            }
            if (tail$iv2.limit + minimumCapacity > 8192 || !tail$iv2.owner) {
                return tail$iv2.push(SegmentPool.INSTANCE.take());
            }
            return tail$iv2;
        }
        throw new IllegalArgumentException("unexpected capacity".toString());
    }

    @Override // okio.Sink
    public void write(Buffer source, long byteCount) {
        Segment tail$iv;
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        long byteCount$iv = byteCount;
        if (source != this) {
            Util.checkOffsetAndCount(source.size(), 0, byteCount$iv);
            while (byteCount$iv > 0) {
                Segment segment = source.head;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                int i = segment.limit;
                Segment segment2 = source.head;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                if (byteCount$iv < ((long) (i - segment2.pos))) {
                    Segment segment3 = this.head;
                    if (segment3 != null) {
                        if (segment3 == null) {
                            Intrinsics.throwNpe();
                        }
                        tail$iv = segment3.prev;
                    } else {
                        tail$iv = null;
                    }
                    if (tail$iv != null && tail$iv.owner) {
                        if ((((long) tail$iv.limit) + byteCount$iv) - ((long) (tail$iv.shared ? 0 : tail$iv.pos)) <= ((long) 8192)) {
                            Segment segment4 = source.head;
                            if (segment4 == null) {
                                Intrinsics.throwNpe();
                            }
                            segment4.writeTo(tail$iv, (int) byteCount$iv);
                            source.setSize$okio(source.size() - byteCount$iv);
                            setSize$okio(size() + byteCount$iv);
                            return;
                        }
                    }
                    Segment segment5 = source.head;
                    if (segment5 == null) {
                        Intrinsics.throwNpe();
                    }
                    source.head = segment5.split((int) byteCount$iv);
                }
                Segment segmentToMove$iv = source.head;
                if (segmentToMove$iv == null) {
                    Intrinsics.throwNpe();
                }
                long movedByteCount$iv = (long) (segmentToMove$iv.limit - segmentToMove$iv.pos);
                source.head = segmentToMove$iv.pop();
                Segment segment6 = this.head;
                if (segment6 == null) {
                    this.head = segmentToMove$iv;
                    segmentToMove$iv.prev = segmentToMove$iv;
                    segmentToMove$iv.next = segmentToMove$iv.prev;
                } else {
                    if (segment6 == null) {
                        Intrinsics.throwNpe();
                    }
                    Segment tail$iv2 = segment6.prev;
                    if (tail$iv2 == null) {
                        Intrinsics.throwNpe();
                    }
                    tail$iv2.push(segmentToMove$iv).compact();
                }
                source.setSize$okio(source.size() - movedByteCount$iv);
                setSize$okio(size() + movedByteCount$iv);
                byteCount$iv -= movedByteCount$iv;
            }
            return;
        }
        throw new IllegalArgumentException("source == this".toString());
    }

    @Override // okio.Source
    public long read(Buffer sink, long byteCount) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        long byteCount$iv = byteCount;
        if (!(byteCount$iv >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + byteCount$iv).toString());
        } else if (size() == 0) {
            return -1;
        } else {
            if (byteCount$iv > size()) {
                byteCount$iv = size();
            }
            sink.write(this, byteCount$iv);
            return byteCount$iv;
        }
    }

    @Override // okio.BufferedSource
    public long indexOf(byte b) {
        return indexOf(b, 0, Long.MAX_VALUE);
    }

    @Override // okio.BufferedSource
    public long indexOf(byte b, long fromIndex) {
        return indexOf(b, fromIndex, Long.MAX_VALUE);
    }

    /* JADX INFO: Multiple debug info for r2v11 byte[]: [D('s$iv' okio.Segment), D('data$iv' byte[])] */
    @Override // okio.BufferedSource
    public long indexOf(byte b, long fromIndex, long toIndex) {
        int limit$iv = 0;
        long fromIndex$iv = fromIndex;
        long toIndex$iv = toIndex;
        if (0 <= fromIndex$iv && toIndex$iv >= fromIndex$iv) {
            if (toIndex$iv > size()) {
                toIndex$iv = size();
            }
            if (fromIndex$iv == toIndex$iv) {
                return -1;
            }
            long fromIndex$iv$iv = fromIndex$iv;
            Buffer $this$seek$iv$iv = this;
            int $i$f$seek = 0;
            Segment s$iv$iv = $this$seek$iv$iv.head;
            if (s$iv$iv == null) {
                return -1;
            }
            if ($this$seek$iv$iv.size() - fromIndex$iv$iv < fromIndex$iv$iv) {
                long offset$iv$iv = $this$seek$iv$iv.size();
                while (offset$iv$iv > fromIndex$iv$iv) {
                    Segment segment = s$iv$iv.prev;
                    if (segment == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv$iv = segment;
                    offset$iv$iv -= (long) (s$iv$iv.limit - s$iv$iv.pos);
                }
                Segment s$iv = s$iv$iv;
                if (s$iv == null) {
                    return -1;
                }
                long offset$iv = offset$iv$iv;
                long fromIndex$iv2 = fromIndex$iv;
                Segment s$iv2 = s$iv;
                while (offset$iv < toIndex$iv) {
                    byte[] data$iv = s$iv2.data;
                    int limit$iv2 = (int) Math.min((long) s$iv2.limit, (((long) s$iv2.pos) + toIndex$iv) - offset$iv);
                    for (int pos$iv = (int) ((((long) s$iv2.pos) + fromIndex$iv2) - offset$iv); pos$iv < limit$iv2; pos$iv++) {
                        if (data$iv[pos$iv] == b) {
                            return ((long) (pos$iv - s$iv2.pos)) + offset$iv;
                        }
                    }
                    offset$iv += (long) (s$iv2.limit - s$iv2.pos);
                    fromIndex$iv2 = offset$iv;
                    Segment segment2 = s$iv2.next;
                    if (segment2 == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv2 = segment2;
                    limit$iv = limit$iv;
                    $this$seek$iv$iv = $this$seek$iv$iv;
                    s$iv = s$iv;
                    $i$f$seek = $i$f$seek;
                    s$iv$iv = s$iv$iv;
                }
                return -1;
            }
            long offset$iv$iv2 = 0;
            while (true) {
                long nextOffset$iv$iv = ((long) (s$iv$iv.limit - s$iv$iv.pos)) + offset$iv$iv2;
                if (nextOffset$iv$iv > fromIndex$iv$iv) {
                    break;
                }
                Segment segment3 = s$iv$iv.next;
                if (segment3 == null) {
                    Intrinsics.throwNpe();
                }
                s$iv$iv = segment3;
                offset$iv$iv2 = nextOffset$iv$iv;
                fromIndex$iv$iv = fromIndex$iv$iv;
            }
            Segment s$iv3 = s$iv$iv;
            int i = 0;
            if (s$iv3 == null) {
                return -1;
            }
            Segment s$iv4 = s$iv3;
            long offset$iv2 = offset$iv$iv2;
            while (offset$iv2 < toIndex$iv) {
                byte[] data$iv2 = s$iv4.data;
                int limit$iv3 = (int) Math.min((long) s$iv4.limit, (((long) s$iv4.pos) + toIndex$iv) - offset$iv2);
                for (int pos$iv2 = (int) ((((long) s$iv4.pos) + fromIndex$iv) - offset$iv2); pos$iv2 < limit$iv3; pos$iv2++) {
                    if (data$iv2[pos$iv2] == b) {
                        return ((long) (pos$iv2 - s$iv4.pos)) + offset$iv2;
                    }
                }
                offset$iv2 += (long) (s$iv4.limit - s$iv4.pos);
                fromIndex$iv = offset$iv2;
                Segment segment4 = s$iv4.next;
                if (segment4 == null) {
                    Intrinsics.throwNpe();
                }
                s$iv4 = segment4;
                s$iv3 = s$iv3;
                offset$iv$iv2 = offset$iv$iv2;
                i = i;
                fromIndex$iv$iv = fromIndex$iv$iv;
            }
            return -1;
        }
        throw new IllegalArgumentException(("size=" + size() + " fromIndex=" + fromIndex$iv + " toIndex=" + toIndex$iv).toString());
    }

    @Override // okio.BufferedSource
    public long indexOf(ByteString bytes) throws IOException {
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        return indexOf(bytes, 0);
    }

    /* JADX INFO: Multiple debug info for r5v11 int: [D('$this$seek$iv$iv' okio.Buffer), D('a$iv$iv' int)] */
    /* JADX INFO: Multiple debug info for r5v12 int: [D('a$iv$iv' int), D('segmentLimit$iv' int)] */
    @Override // okio.BufferedSource
    public long indexOf(ByteString bytes, long fromIndex) throws IOException {
        byte b0$iv;
        byte[] targetByteArray$iv;
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        Buffer $this$commonIndexOf$iv = this;
        long fromIndex$iv = fromIndex;
        if (bytes.size() > 0) {
            if (fromIndex$iv >= 0) {
                long fromIndex$iv$iv = fromIndex$iv;
                Buffer $this$seek$iv$iv = $this$commonIndexOf$iv;
                int $i$f$seek = 0;
                Segment s$iv$iv = $this$seek$iv$iv.head;
                if (s$iv$iv == null) {
                    return -1;
                }
                if ($this$seek$iv$iv.size() - fromIndex$iv$iv < fromIndex$iv$iv) {
                    long offset$iv$iv = $this$seek$iv$iv.size();
                    while (offset$iv$iv > fromIndex$iv$iv) {
                        Segment segment = s$iv$iv.prev;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        s$iv$iv = segment;
                        offset$iv$iv -= (long) (s$iv$iv.limit - s$iv$iv.pos);
                    }
                    Segment s$iv = s$iv$iv;
                    long offset$iv = offset$iv$iv;
                    if (s$iv == null) {
                        return -1;
                    }
                    long offset$iv2 = offset$iv;
                    byte[] targetByteArray$iv2 = bytes.internalArray$okio();
                    byte b0$iv2 = targetByteArray$iv2[0];
                    int bytesSize$iv = bytes.size();
                    long resultLimit$iv = ($this$commonIndexOf$iv.size() - ((long) bytesSize$iv)) + 1;
                    Segment s$iv2 = s$iv;
                    while (offset$iv2 < resultLimit$iv) {
                        byte[] data$iv = s$iv2.data;
                        int segmentLimit$iv = (int) Math.min((long) s$iv2.limit, (((long) s$iv2.pos) + resultLimit$iv) - offset$iv2);
                        for (int pos$iv = (int) ((((long) s$iv2.pos) + fromIndex$iv) - offset$iv2); pos$iv < segmentLimit$iv; pos$iv++) {
                            if (data$iv[pos$iv] == b0$iv2 && BufferKt.rangeEquals(s$iv2, pos$iv + 1, targetByteArray$iv2, 1, bytesSize$iv)) {
                                return ((long) (pos$iv - s$iv2.pos)) + offset$iv2;
                            }
                        }
                        offset$iv2 += (long) (s$iv2.limit - s$iv2.pos);
                        fromIndex$iv = offset$iv2;
                        Segment segment2 = s$iv2.next;
                        if (segment2 == null) {
                            Intrinsics.throwNpe();
                        }
                        s$iv2 = segment2;
                        $this$seek$iv$iv = $this$seek$iv$iv;
                        s$iv$iv = s$iv$iv;
                        $i$f$seek = $i$f$seek;
                        s$iv = s$iv;
                        offset$iv = offset$iv;
                    }
                    return -1;
                }
                long offset$iv$iv2 = 0;
                while (true) {
                    long nextOffset$iv$iv = ((long) (s$iv$iv.limit - s$iv$iv.pos)) + offset$iv$iv2;
                    if (nextOffset$iv$iv > fromIndex$iv$iv) {
                        break;
                    }
                    Segment segment3 = s$iv$iv.next;
                    if (segment3 == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv$iv = segment3;
                    offset$iv$iv2 = nextOffset$iv$iv;
                    $this$commonIndexOf$iv = $this$commonIndexOf$iv;
                    fromIndex$iv$iv = fromIndex$iv$iv;
                }
                long offset$iv3 = offset$iv$iv2;
                if (s$iv$iv == null) {
                    return -1;
                }
                Segment s$iv3 = s$iv$iv;
                long offset$iv4 = offset$iv3;
                byte[] targetByteArray$iv3 = bytes.internalArray$okio();
                byte b0$iv3 = targetByteArray$iv3[0];
                int bytesSize$iv2 = bytes.size();
                long resultLimit$iv2 = ($this$commonIndexOf$iv.size() - ((long) bytesSize$iv2)) + 1;
                while (offset$iv4 < resultLimit$iv2) {
                    byte[] data$iv2 = s$iv3.data;
                    byte[] targetByteArray$iv4 = targetByteArray$iv3;
                    int segmentLimit$iv2 = (int) Math.min((long) s$iv3.limit, (((long) s$iv3.pos) + resultLimit$iv2) - offset$iv4);
                    int pos$iv2 = (int) ((((long) s$iv3.pos) + fromIndex$iv) - offset$iv4);
                    while (pos$iv2 < segmentLimit$iv2) {
                        if (data$iv2[pos$iv2] == b0$iv3) {
                            targetByteArray$iv = targetByteArray$iv4;
                            if (BufferKt.rangeEquals(s$iv3, pos$iv2 + 1, targetByteArray$iv, 1, bytesSize$iv2)) {
                                return ((long) (pos$iv2 - s$iv3.pos)) + offset$iv4;
                            }
                            b0$iv = b0$iv3;
                        } else {
                            b0$iv = b0$iv3;
                            targetByteArray$iv = targetByteArray$iv4;
                        }
                        pos$iv2++;
                        b0$iv3 = b0$iv;
                        fromIndex$iv = fromIndex$iv;
                        targetByteArray$iv4 = targetByteArray$iv;
                    }
                    offset$iv4 += (long) (s$iv3.limit - s$iv3.pos);
                    fromIndex$iv = offset$iv4;
                    Segment segment4 = s$iv3.next;
                    if (segment4 == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv3 = segment4;
                    targetByteArray$iv3 = targetByteArray$iv4;
                    $this$commonIndexOf$iv = $this$commonIndexOf$iv;
                    b0$iv3 = b0$iv3;
                    offset$iv3 = offset$iv3;
                }
                return -1;
            }
            throw new IllegalArgumentException(("fromIndex < 0: " + fromIndex$iv).toString());
        }
        throw new IllegalArgumentException("bytes is empty".toString());
    }

    @Override // okio.BufferedSource
    public long indexOfElement(ByteString targetBytes) {
        Intrinsics.checkParameterIsNotNull(targetBytes, "targetBytes");
        return indexOfElement(targetBytes, 0);
    }

    /* JADX INFO: Multiple debug info for r2v10 byte: [D('t$iv' byte), D('data$iv' byte[])] */
    /* JADX INFO: Multiple debug info for r2v33 byte[]: [D('$i$f$commonIndexOfElement' int), D('data$iv' byte[])] */
    @Override // okio.BufferedSource
    public long indexOfElement(ByteString targetBytes, long fromIndex) {
        ByteString byteString = targetBytes;
        Intrinsics.checkParameterIsNotNull(byteString, "targetBytes");
        Buffer $this$commonIndexOfElement$iv = this;
        int $i$f$commonIndexOfElement = 0;
        long fromIndex$iv = fromIndex;
        if (fromIndex$iv >= 0) {
            Buffer $this$seek$iv$iv = $this$commonIndexOfElement$iv;
            int $i$f$seek = 0;
            Segment s$iv$iv = $this$seek$iv$iv.head;
            if (s$iv$iv == null) {
                return -1;
            }
            if ($this$seek$iv$iv.size() - fromIndex$iv < fromIndex$iv) {
                long offset$iv$iv = $this$seek$iv$iv.size();
                while (offset$iv$iv > fromIndex$iv) {
                    Segment segment = s$iv$iv.prev;
                    if (segment == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv$iv = segment;
                    offset$iv$iv -= (long) (s$iv$iv.limit - s$iv$iv.pos);
                }
                if (s$iv$iv == null) {
                    return -1;
                }
                long offset$iv = offset$iv$iv;
                if (targetBytes.size() == 2) {
                    byte b0$iv = byteString.getByte(0);
                    byte b1$iv = byteString.getByte(1);
                    Segment s$iv = s$iv$iv;
                    while (offset$iv < $this$commonIndexOfElement$iv.size()) {
                        byte[] data$iv = s$iv.data;
                        int limit$iv = s$iv.limit;
                        for (int pos$iv = (int) ((((long) s$iv.pos) + fromIndex$iv) - offset$iv); pos$iv < limit$iv; pos$iv++) {
                            byte b = data$iv[pos$iv];
                            if (b == b0$iv || b == b1$iv) {
                                return ((long) (pos$iv - s$iv.pos)) + offset$iv;
                            }
                        }
                        offset$iv += (long) (s$iv.limit - s$iv.pos);
                        fromIndex$iv = offset$iv;
                        Segment segment2 = s$iv.next;
                        if (segment2 == null) {
                            Intrinsics.throwNpe();
                        }
                        s$iv = segment2;
                        s$iv$iv = s$iv$iv;
                        $i$f$commonIndexOfElement = $i$f$commonIndexOfElement;
                        $this$seek$iv$iv = $this$seek$iv$iv;
                        $i$f$seek = $i$f$seek;
                    }
                } else {
                    byte[] targetByteArray$iv = targetBytes.internalArray$okio();
                    Segment s$iv2 = s$iv$iv;
                    while (offset$iv < $this$commonIndexOfElement$iv.size()) {
                        byte[] data$iv2 = s$iv2.data;
                        int pos$iv2 = (int) ((((long) s$iv2.pos) + fromIndex$iv) - offset$iv);
                        int limit$iv2 = s$iv2.limit;
                        while (pos$iv2 < limit$iv2) {
                            byte b2 = data$iv2[pos$iv2];
                            for (byte t$iv : targetByteArray$iv) {
                                if (b2 == t$iv) {
                                    return ((long) (pos$iv2 - s$iv2.pos)) + offset$iv;
                                }
                            }
                            pos$iv2++;
                            fromIndex$iv = fromIndex$iv;
                        }
                        offset$iv += (long) (s$iv2.limit - s$iv2.pos);
                        fromIndex$iv = offset$iv;
                        Segment segment3 = s$iv2.next;
                        if (segment3 == null) {
                            Intrinsics.throwNpe();
                        }
                        s$iv2 = segment3;
                        targetByteArray$iv = targetByteArray$iv;
                    }
                }
                return -1;
            }
            long offset$iv$iv2 = 0;
            while (true) {
                long nextOffset$iv$iv = ((long) (s$iv$iv.limit - s$iv$iv.pos)) + offset$iv$iv2;
                if (nextOffset$iv$iv > fromIndex$iv) {
                    break;
                }
                Segment segment4 = s$iv$iv.next;
                if (segment4 == null) {
                    Intrinsics.throwNpe();
                }
                s$iv$iv = segment4;
                offset$iv$iv2 = nextOffset$iv$iv;
                byteString = targetBytes;
                $this$commonIndexOfElement$iv = $this$commonIndexOfElement$iv;
            }
            Segment s$iv3 = s$iv$iv;
            if (s$iv3 == null) {
                return -1;
            }
            Segment s$iv4 = s$iv3;
            long offset$iv2 = offset$iv$iv2;
            if (targetBytes.size() == 2) {
                byte b0$iv2 = byteString.getByte(0);
                byte b1$iv2 = byteString.getByte(1);
                while (offset$iv2 < $this$commonIndexOfElement$iv.size()) {
                    byte[] data$iv3 = s$iv4.data;
                    int limit$iv3 = s$iv4.limit;
                    for (int pos$iv3 = (int) ((((long) s$iv4.pos) + fromIndex$iv) - offset$iv2); pos$iv3 < limit$iv3; pos$iv3++) {
                        byte b3 = data$iv3[pos$iv3];
                        if (b3 == b0$iv2 || b3 == b1$iv2) {
                            return ((long) (pos$iv3 - s$iv4.pos)) + offset$iv2;
                        }
                    }
                    offset$iv2 += (long) (s$iv4.limit - s$iv4.pos);
                    Segment segment5 = s$iv4.next;
                    if (segment5 == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv4 = segment5;
                    fromIndex$iv = offset$iv2;
                    s$iv3 = s$iv3;
                    offset$iv$iv2 = offset$iv$iv2;
                }
            } else {
                byte[] targetByteArray$iv2 = targetBytes.internalArray$okio();
                while (offset$iv2 < $this$commonIndexOfElement$iv.size()) {
                    byte[] data$iv4 = s$iv4.data;
                    int pos$iv4 = (int) ((((long) s$iv4.pos) + fromIndex$iv) - offset$iv2);
                    int limit$iv4 = s$iv4.limit;
                    while (pos$iv4 < limit$iv4) {
                        byte b4 = data$iv4[pos$iv4];
                        int length = targetByteArray$iv2.length;
                        int i = 0;
                        while (i < length) {
                            if (b4 == targetByteArray$iv2[i]) {
                                return ((long) (pos$iv4 - s$iv4.pos)) + offset$iv2;
                            }
                            i++;
                            data$iv4 = data$iv4;
                        }
                        pos$iv4++;
                        $this$commonIndexOfElement$iv = $this$commonIndexOfElement$iv;
                    }
                    offset$iv2 += (long) (s$iv4.limit - s$iv4.pos);
                    fromIndex$iv = offset$iv2;
                    Segment segment6 = s$iv4.next;
                    if (segment6 == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv4 = segment6;
                    $this$commonIndexOfElement$iv = $this$commonIndexOfElement$iv;
                    targetByteArray$iv2 = targetByteArray$iv2;
                }
            }
            return -1;
        }
        throw new IllegalArgumentException(("fromIndex < 0: " + fromIndex$iv).toString());
    }

    @Override // okio.BufferedSource
    public boolean rangeEquals(long offset, ByteString bytes) {
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        return rangeEquals(offset, bytes, 0, bytes.size());
    }

    @Override // okio.BufferedSource
    public boolean rangeEquals(long offset, ByteString bytes, int bytesOffset, int byteCount) {
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        if (offset < 0 || bytesOffset < 0 || byteCount < 0 || size() - offset < ((long) byteCount) || bytes.size() - bytesOffset < byteCount) {
            return false;
        }
        for (int i$iv = 0; i$iv < byteCount; i$iv++) {
            if (getByte(((long) i$iv) + offset) != bytes.getByte(bytesOffset + i$iv)) {
                return false;
            }
        }
        return true;
    }

    @Override // okio.BufferedSink, okio.Sink, java.io.Flushable
    public void flush() {
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return true;
    }

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // okio.Source
    public Timeout timeout() {
        return Timeout.NONE;
    }

    public final ByteString md5() {
        return digest(MessageDigestAlgorithms.MD5);
    }

    public final ByteString sha1() {
        return digest(MessageDigestAlgorithms.SHA_1);
    }

    public final ByteString sha256() {
        return digest(MessageDigestAlgorithms.SHA_256);
    }

    public final ByteString sha512() {
        return digest(MessageDigestAlgorithms.SHA_512);
    }

    private final ByteString digest(String algorithm) {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        Segment head = this.head;
        if (head != null) {
            messageDigest.update(head.data, head.pos, head.limit - head.pos);
            Segment s = head.next;
            if (s == null) {
                Intrinsics.throwNpe();
            }
            while (s != head) {
                messageDigest.update(s.data, s.pos, s.limit - s.pos);
                Segment segment = s.next;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                s = segment;
            }
        }
        byte[] digest = messageDigest.digest();
        Intrinsics.checkExpressionValueIsNotNull(digest, "messageDigest.digest()");
        return new ByteString(digest);
    }

    public final ByteString hmacSha1(ByteString key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return hmac("HmacSHA1", key);
    }

    public final ByteString hmacSha256(ByteString key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return hmac("HmacSHA256", key);
    }

    public final ByteString hmacSha512(ByteString key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return hmac("HmacSHA512", key);
    }

    private final ByteString hmac(String algorithm, ByteString key) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key.internalArray$okio(), algorithm));
            Segment head = this.head;
            if (head != null) {
                mac.update(head.data, head.pos, head.limit - head.pos);
                Segment s = head.next;
                if (s == null) {
                    Intrinsics.throwNpe();
                }
                while (s != head) {
                    mac.update(s.data, s.pos, s.limit - s.pos);
                    Segment segment = s.next;
                    if (segment == null) {
                        Intrinsics.throwNpe();
                    }
                    s = segment;
                }
            }
            byte[] doFinal = mac.doFinal();
            Intrinsics.checkExpressionValueIsNotNull(doFinal, "mac.doFinal()");
            return new ByteString(doFinal);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /* JADX INFO: Multiple debug info for r9v3 long: [D('i$iv' long), D('posA$iv' int)] */
    @Override // java.lang.Object
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Buffer) || size() != ((Buffer) other).size()) {
            return false;
        }
        if (size() == 0) {
            return true;
        }
        Segment sa$iv = this.head;
        if (sa$iv == null) {
            Intrinsics.throwNpe();
        }
        Segment sb$iv = ((Buffer) other).head;
        if (sb$iv == null) {
            Intrinsics.throwNpe();
        }
        int posA$iv = sa$iv.pos;
        int posB$iv = sb$iv.pos;
        long pos$iv = 0;
        while (pos$iv < size()) {
            long count$iv = (long) Math.min(sa$iv.limit - posA$iv, sb$iv.limit - posB$iv);
            int posA$iv2 = posA$iv;
            int posB$iv2 = posB$iv;
            long i$iv = 0;
            while (i$iv < count$iv) {
                int posA$iv3 = posA$iv2 + 1;
                int posB$iv3 = posB$iv2 + 1;
                if (sa$iv.data[posA$iv2] != sb$iv.data[posB$iv2]) {
                    return false;
                }
                i$iv = 1 + i$iv;
                posA$iv2 = posA$iv3;
                posB$iv2 = posB$iv3;
            }
            if (posA$iv2 == sa$iv.limit) {
                Segment sa$iv2 = sa$iv.next;
                if (sa$iv2 == null) {
                    Intrinsics.throwNpe();
                }
                posA$iv = sa$iv2.pos;
                sa$iv = sa$iv2;
            } else {
                posA$iv = posA$iv2;
            }
            if (posB$iv2 == sb$iv.limit) {
                Segment sb$iv2 = sb$iv.next;
                if (sb$iv2 == null) {
                    Intrinsics.throwNpe();
                }
                posB$iv = sb$iv2.pos;
                sb$iv = sb$iv2;
            } else {
                posB$iv = posB$iv2;
            }
            pos$iv += count$iv;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        Segment s$iv = this.head;
        if (s$iv == null) {
            return 0;
        }
        int result$iv = 1;
        do {
            int limit$iv = s$iv.limit;
            for (int pos$iv = s$iv.pos; pos$iv < limit$iv; pos$iv++) {
                result$iv = (result$iv * 31) + s$iv.data[pos$iv];
            }
            Segment segment = s$iv.next;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            s$iv = segment;
        } while (s$iv != this.head);
        return result$iv;
    }

    @Override // java.lang.Object
    public String toString() {
        return snapshot().toString();
    }

    public final Buffer copy() {
        Buffer result$iv = new Buffer();
        if (size() != 0) {
            Segment head$iv = this.head;
            if (head$iv == null) {
                Intrinsics.throwNpe();
            }
            Segment headCopy$iv = head$iv.sharedCopy();
            result$iv.head = headCopy$iv;
            headCopy$iv.prev = result$iv.head;
            headCopy$iv.next = headCopy$iv.prev;
            for (Segment s$iv = head$iv.next; s$iv != head$iv; s$iv = s$iv.next) {
                Segment segment = headCopy$iv.prev;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                if (s$iv == null) {
                    Intrinsics.throwNpe();
                }
                segment.push(s$iv.sharedCopy());
            }
            result$iv.setSize$okio(size());
        }
        return result$iv;
    }

    @Override // java.lang.Object
    public Buffer clone() {
        return copy();
    }

    public final ByteString snapshot() {
        if (size() <= ((long) Integer.MAX_VALUE)) {
            return snapshot((int) size());
        }
        throw new IllegalStateException(("size > Int.MAX_VALUE: " + size()).toString());
    }

    public final ByteString snapshot(int byteCount) {
        if (byteCount == 0) {
            return ByteString.EMPTY;
        }
        Util.checkOffsetAndCount(size(), 0, (long) byteCount);
        int offset$iv = 0;
        int segmentCount$iv = 0;
        Segment s$iv = this.head;
        while (offset$iv < byteCount) {
            if (s$iv == null) {
                Intrinsics.throwNpe();
            }
            if (s$iv.limit != s$iv.pos) {
                offset$iv += s$iv.limit - s$iv.pos;
                segmentCount$iv++;
                s$iv = s$iv.next;
            } else {
                throw new AssertionError("s.limit == s.pos");
            }
        }
        byte[][] segments$iv = new byte[segmentCount$iv];
        int[] directory$iv = new int[segmentCount$iv * 2];
        int offset$iv2 = 0;
        int segmentCount$iv2 = 0;
        Segment s$iv2 = this.head;
        while (offset$iv2 < byteCount) {
            if (s$iv2 == null) {
                Intrinsics.throwNpe();
            }
            segments$iv[segmentCount$iv2] = s$iv2.data;
            offset$iv2 += s$iv2.limit - s$iv2.pos;
            directory$iv[segmentCount$iv2] = Math.min(offset$iv2, byteCount);
            directory$iv[segments$iv.length + segmentCount$iv2] = s$iv2.pos;
            s$iv2.shared = true;
            segmentCount$iv2++;
            s$iv2 = s$iv2.next;
        }
        return new SegmentedByteString(segments$iv, directory$iv);
    }

    public static /* synthetic */ UnsafeCursor readUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int i, Object obj) {
        if ((i & 1) != 0) {
            unsafeCursor = new UnsafeCursor();
        }
        return buffer.readUnsafe(unsafeCursor);
    }

    public final UnsafeCursor readUnsafe(UnsafeCursor unsafeCursor) {
        Intrinsics.checkParameterIsNotNull(unsafeCursor, "unsafeCursor");
        if (unsafeCursor.buffer == null) {
            unsafeCursor.buffer = this;
            unsafeCursor.readWrite = false;
            return unsafeCursor;
        }
        throw new IllegalStateException("already attached to a buffer".toString());
    }

    public static /* synthetic */ UnsafeCursor readAndWriteUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int i, Object obj) {
        if ((i & 1) != 0) {
            unsafeCursor = new UnsafeCursor();
        }
        return buffer.readAndWriteUnsafe(unsafeCursor);
    }

    public final UnsafeCursor readAndWriteUnsafe(UnsafeCursor unsafeCursor) {
        Intrinsics.checkParameterIsNotNull(unsafeCursor, "unsafeCursor");
        if (unsafeCursor.buffer == null) {
            unsafeCursor.buffer = this;
            unsafeCursor.readWrite = true;
            return unsafeCursor;
        }
        throw new IllegalStateException("already attached to a buffer".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to operator function", replaceWith = @ReplaceWith(expression = "this[index]", imports = {}))
    /* renamed from: -deprecated_getByte */
    public final byte m1154deprecated_getByte(long index) {
        return getByte(index);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "size", imports = {}))
    /* renamed from: -deprecated_size */
    public final long m1155deprecated_size() {
        return this.size;
    }

    /* compiled from: Buffer.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\bJ\u0006\u0010\u0014\u001a\u00020\bJ\u000e\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nJ\u000e\u0010\u0017\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u00020\b8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lokio/Buffer$UnsafeCursor;", "Ljava/io/Closeable;", "()V", "buffer", "Lokio/Buffer;", UriUtil.DATA_SCHEME, "", "end", "", "offset", "", "readWrite", "", "segment", "Lokio/Segment;", "start", "close", "", "expandBuffer", "minByteCount", "next", "resizeBuffer", "newSize", "seek", "okio"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class UnsafeCursor implements Closeable {
        public Buffer buffer;
        public byte[] data;
        public boolean readWrite;
        private Segment segment;
        public long offset = -1;
        public int start = -1;
        public int end = -1;

        public final int next() {
            long j = this.offset;
            Buffer buffer = this.buffer;
            if (buffer == null) {
                Intrinsics.throwNpe();
            }
            if (j != buffer.size()) {
                long j2 = this.offset;
                return seek(j2 == -1 ? 0 : j2 + ((long) (this.end - this.start)));
            }
            throw new IllegalStateException("no more bytes".toString());
        }

        public final int seek(long offset) {
            long nextOffset;
            Segment next;
            Buffer buffer = this.buffer;
            if (buffer == null) {
                throw new IllegalStateException("not attached to a buffer".toString());
            } else if (offset < ((long) -1) || offset > buffer.size()) {
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                Object[] objArr = {Long.valueOf(offset), Long.valueOf(buffer.size())};
                String format = String.format("offset=%s > size=%s", Arrays.copyOf(objArr, objArr.length));
                Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
                throw new ArrayIndexOutOfBoundsException(format);
            } else if (offset == -1 || offset == buffer.size()) {
                this.segment = null;
                this.offset = offset;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return -1;
            } else {
                long min = 0;
                long max = buffer.size();
                Segment head = buffer.head;
                Segment tail = buffer.head;
                Segment segment = this.segment;
                if (segment != null) {
                    long j = this.offset;
                    int i = this.start;
                    if (segment == null) {
                        Intrinsics.throwNpe();
                    }
                    long segmentOffset = j - ((long) (i - segment.pos));
                    if (segmentOffset > offset) {
                        max = segmentOffset;
                        tail = this.segment;
                    } else {
                        min = segmentOffset;
                        head = this.segment;
                    }
                }
                if (max - offset > offset - min) {
                    next = head;
                    nextOffset = min;
                    while (true) {
                        if (next == null) {
                            Intrinsics.throwNpe();
                        }
                        if (offset < ((long) (next.limit - next.pos)) + nextOffset) {
                            break;
                        }
                        nextOffset += (long) (next.limit - next.pos);
                        next = next.next;
                    }
                } else {
                    next = tail;
                    nextOffset = max;
                    while (nextOffset > offset) {
                        if (next == null) {
                            Intrinsics.throwNpe();
                        }
                        next = next.prev;
                        if (next == null) {
                            Intrinsics.throwNpe();
                        }
                        nextOffset -= (long) (next.limit - next.pos);
                    }
                }
                if (this.readWrite) {
                    if (next == null) {
                        Intrinsics.throwNpe();
                    }
                    if (next.shared) {
                        Segment unsharedNext = next.unsharedCopy();
                        if (buffer.head == next) {
                            buffer.head = unsharedNext;
                        }
                        next = next.push(unsharedNext);
                        Segment segment2 = next.prev;
                        if (segment2 == null) {
                            Intrinsics.throwNpe();
                        }
                        segment2.pop();
                    }
                }
                this.segment = next;
                this.offset = offset;
                if (next == null) {
                    Intrinsics.throwNpe();
                }
                this.data = next.data;
                this.start = next.pos + ((int) (offset - nextOffset));
                this.end = next.limit;
                return this.end - this.start;
            }
        }

        public final long resizeBuffer(long newSize) {
            Buffer buffer = this.buffer;
            if (buffer == null) {
                throw new IllegalStateException("not attached to a buffer".toString());
            } else if (this.readWrite) {
                long oldSize = buffer.size();
                int segmentBytesToAdd = 1;
                if (newSize <= oldSize) {
                    if (newSize < 0) {
                        segmentBytesToAdd = 0;
                    }
                    if (segmentBytesToAdd != 0) {
                        long bytesToSubtract = oldSize - newSize;
                        while (true) {
                            if (bytesToSubtract <= 0) {
                                break;
                            }
                            Segment segment = buffer.head;
                            if (segment == null) {
                                Intrinsics.throwNpe();
                            }
                            Segment tail = segment.prev;
                            if (tail == null) {
                                Intrinsics.throwNpe();
                            }
                            int tailSize = tail.limit - tail.pos;
                            if (((long) tailSize) > bytesToSubtract) {
                                tail.limit -= (int) bytesToSubtract;
                                break;
                            }
                            buffer.head = tail.pop();
                            SegmentPool.INSTANCE.recycle(tail);
                            bytesToSubtract -= (long) tailSize;
                        }
                        this.segment = null;
                        this.offset = newSize;
                        this.data = null;
                        this.start = -1;
                        this.end = -1;
                    } else {
                        throw new IllegalArgumentException(("newSize < 0: " + newSize).toString());
                    }
                } else if (newSize > oldSize) {
                    boolean needsToSeek = true;
                    long bytesToAdd = newSize - oldSize;
                    for (long j = 0; bytesToAdd > j; j = 0) {
                        Segment tail2 = buffer.writableSegment$okio(segmentBytesToAdd);
                        int segmentBytesToAdd2 = (int) Math.min(bytesToAdd, (long) (8192 - tail2.limit));
                        tail2.limit += segmentBytesToAdd2;
                        bytesToAdd -= (long) segmentBytesToAdd2;
                        if (needsToSeek) {
                            this.segment = tail2;
                            this.offset = oldSize;
                            this.data = tail2.data;
                            this.start = tail2.limit - segmentBytesToAdd2;
                            this.end = tail2.limit;
                            needsToSeek = false;
                        }
                        segmentBytesToAdd = 1;
                    }
                }
                buffer.setSize$okio(newSize);
                return oldSize;
            } else {
                throw new IllegalStateException("resizeBuffer() only permitted for read/write buffers".toString());
            }
        }

        public final long expandBuffer(int minByteCount) {
            boolean z = true;
            if (minByteCount > 0) {
                if (minByteCount > 8192) {
                    z = false;
                }
                if (z) {
                    Buffer buffer = this.buffer;
                    if (buffer == null) {
                        throw new IllegalStateException("not attached to a buffer".toString());
                    } else if (this.readWrite) {
                        long oldSize = buffer.size();
                        Segment tail = buffer.writableSegment$okio(minByteCount);
                        int result = 8192 - tail.limit;
                        tail.limit = 8192;
                        buffer.setSize$okio(((long) result) + oldSize);
                        this.segment = tail;
                        this.offset = oldSize;
                        this.data = tail.data;
                        this.start = 8192 - result;
                        this.end = 8192;
                        return (long) result;
                    } else {
                        throw new IllegalStateException("expandBuffer() only permitted for read/write buffers".toString());
                    }
                } else {
                    throw new IllegalArgumentException(("minByteCount > Segment.SIZE: " + minByteCount).toString());
                }
            } else {
                throw new IllegalArgumentException(("minByteCount <= 0: " + minByteCount).toString());
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.buffer != null) {
                this.buffer = null;
                this.segment = null;
                this.offset = -1;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return;
            }
            throw new IllegalStateException("not attached to a buffer".toString());
        }
    }
}
