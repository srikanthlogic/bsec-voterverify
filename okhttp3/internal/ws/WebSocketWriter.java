package okhttp3.internal.ws;

import com.facebook.imagepipeline.producers.DecodeProducer;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.util.Random;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Timeout;
/* compiled from: WebSocketWriter.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0000\u0018\u00002\u00020\u0001:\u00012B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0016\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#J\u0018\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020!2\b\u0010'\u001a\u0004\u0018\u00010(J\u0018\u0010)\u001a\u00020%2\u0006\u0010*\u001a\u00020!2\u0006\u0010+\u001a\u00020(H\u0002J&\u0010,\u001a\u00020%2\u0006\u0010 \u001a\u00020!2\u0006\u0010-\u001a\u00020#2\u0006\u0010.\u001a\u00020\u00032\u0006\u0010/\u001a\u00020\u0003J\u000e\u00100\u001a\u00020%2\u0006\u0010+\u001a\u00020(J\u000e\u00101\u001a\u00020%2\u0006\u0010+\u001a\u00020(R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0012\u0010\u0012\u001a\u00060\u0013R\u00020\u0000X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u00063"}, d2 = {"Lokhttp3/internal/ws/WebSocketWriter;", "", "isClient", "", "sink", "Lokio/BufferedSink;", "random", "Ljava/util/Random;", "(ZLokio/BufferedSink;Ljava/util/Random;)V", "activeWriter", "getActiveWriter", "()Z", "setActiveWriter", "(Z)V", "buffer", "Lokio/Buffer;", "getBuffer", "()Lokio/Buffer;", "frameSink", "Lokhttp3/internal/ws/WebSocketWriter$FrameSink;", "maskCursor", "Lokio/Buffer$UnsafeCursor;", "maskKey", "", "getRandom", "()Ljava/util/Random;", "getSink", "()Lokio/BufferedSink;", "sinkBuffer", "writerClosed", "newMessageSink", "Lokio/Sink;", "formatOpcode", "", "contentLength", "", "writeClose", "", "code", "reason", "Lokio/ByteString;", "writeControlFrame", "opcode", "payload", "writeMessageFrame", "byteCount", "isFirstFrame", DecodeProducer.EXTRA_IS_FINAL, "writePing", "writePong", "FrameSink", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class WebSocketWriter {
    private boolean activeWriter;
    private final Buffer buffer = new Buffer();
    private final FrameSink frameSink = new FrameSink();
    private final boolean isClient;
    private final Buffer.UnsafeCursor maskCursor;
    private final byte[] maskKey;
    private final Random random;
    private final BufferedSink sink;
    private final Buffer sinkBuffer;
    private boolean writerClosed;

    public WebSocketWriter(boolean isClient, BufferedSink sink, Random random) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        Intrinsics.checkParameterIsNotNull(random, "random");
        this.isClient = isClient;
        this.sink = sink;
        this.random = random;
        this.sinkBuffer = this.sink.getBuffer();
        Buffer.UnsafeCursor unsafeCursor = null;
        this.maskKey = this.isClient ? new byte[4] : null;
        this.maskCursor = this.isClient ? new Buffer.UnsafeCursor() : unsafeCursor;
    }

    public final BufferedSink getSink() {
        return this.sink;
    }

    public final Random getRandom() {
        return this.random;
    }

    public final Buffer getBuffer() {
        return this.buffer;
    }

    public final boolean getActiveWriter() {
        return this.activeWriter;
    }

    public final void setActiveWriter(boolean z) {
        this.activeWriter = z;
    }

    public final void writePing(ByteString payload) throws IOException {
        Intrinsics.checkParameterIsNotNull(payload, "payload");
        writeControlFrame(9, payload);
    }

    public final void writePong(ByteString payload) throws IOException {
        Intrinsics.checkParameterIsNotNull(payload, "payload");
        writeControlFrame(10, payload);
    }

    public final void writeClose(int code, ByteString reason) throws IOException {
        ByteString payload = ByteString.EMPTY;
        if (!(code == 0 && reason == null)) {
            if (code != 0) {
                WebSocketProtocol.INSTANCE.validateCloseCode(code);
            }
            Buffer $this$run = new Buffer();
            $this$run.writeShort(code);
            if (reason != null) {
                $this$run.write(reason);
            }
            payload = $this$run.readByteString();
        }
        try {
            writeControlFrame(8, payload);
        } finally {
            this.writerClosed = true;
        }
    }

    private final void writeControlFrame(int opcode, ByteString payload) throws IOException {
        if (!this.writerClosed) {
            int length = payload.size();
            if (((long) length) <= 125) {
                this.sinkBuffer.writeByte(opcode | 128);
                if (this.isClient) {
                    this.sinkBuffer.writeByte(length | 128);
                    Random random = this.random;
                    byte[] bArr = this.maskKey;
                    if (bArr == null) {
                        Intrinsics.throwNpe();
                    }
                    random.nextBytes(bArr);
                    this.sinkBuffer.write(this.maskKey);
                    if (length > 0) {
                        long payloadStart = this.sinkBuffer.size();
                        this.sinkBuffer.write(payload);
                        Buffer buffer = this.sinkBuffer;
                        Buffer.UnsafeCursor unsafeCursor = this.maskCursor;
                        if (unsafeCursor == null) {
                            Intrinsics.throwNpe();
                        }
                        buffer.readAndWriteUnsafe(unsafeCursor);
                        this.maskCursor.seek(payloadStart);
                        WebSocketProtocol.INSTANCE.toggleMask(this.maskCursor, this.maskKey);
                        this.maskCursor.close();
                    }
                } else {
                    this.sinkBuffer.writeByte(length);
                    this.sinkBuffer.write(payload);
                }
                this.sink.flush();
                return;
            }
            throw new IllegalArgumentException("Payload size must be less than or equal to 125".toString());
        }
        throw new IOException("closed");
    }

    public final Sink newMessageSink(int formatOpcode, long contentLength) {
        if (!this.activeWriter) {
            this.activeWriter = true;
            this.frameSink.setFormatOpcode(formatOpcode);
            this.frameSink.setContentLength(contentLength);
            this.frameSink.setFirstFrame(true);
            this.frameSink.setClosed(false);
            return this.frameSink;
        }
        throw new IllegalStateException("Another message writer is active. Did you call close()?".toString());
    }

    public final void writeMessageFrame(int formatOpcode, long byteCount, boolean isFirstFrame, boolean isFinal) throws IOException {
        if (!this.writerClosed) {
            int b0 = isFirstFrame ? formatOpcode : 0;
            if (isFinal) {
                b0 |= 128;
            }
            this.sinkBuffer.writeByte(b0);
            int b1 = 0;
            if (this.isClient) {
                b1 = 0 | 128;
            }
            if (byteCount <= 125) {
                this.sinkBuffer.writeByte(b1 | ((int) byteCount));
            } else if (byteCount <= WebSocketProtocol.PAYLOAD_SHORT_MAX) {
                this.sinkBuffer.writeByte(b1 | 126);
                this.sinkBuffer.writeShort((int) byteCount);
            } else {
                this.sinkBuffer.writeByte(b1 | 127);
                this.sinkBuffer.writeLong(byteCount);
            }
            if (this.isClient) {
                Random random = this.random;
                byte[] bArr = this.maskKey;
                if (bArr == null) {
                    Intrinsics.throwNpe();
                }
                random.nextBytes(bArr);
                this.sinkBuffer.write(this.maskKey);
                if (byteCount > 0) {
                    long bufferStart = this.sinkBuffer.size();
                    this.sinkBuffer.write(this.buffer, byteCount);
                    Buffer buffer = this.sinkBuffer;
                    Buffer.UnsafeCursor unsafeCursor = this.maskCursor;
                    if (unsafeCursor == null) {
                        Intrinsics.throwNpe();
                    }
                    buffer.readAndWriteUnsafe(unsafeCursor);
                    this.maskCursor.seek(bufferStart);
                    WebSocketProtocol.INSTANCE.toggleMask(this.maskCursor, this.maskKey);
                    this.maskCursor.close();
                }
            } else {
                this.sinkBuffer.write(this.buffer, byteCount);
            }
            this.sink.emit();
            return;
        }
        throw new IOException("closed");
    }

    /* compiled from: WebSocketWriter.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0018H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0018\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\nH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0006\"\u0004\b\u0016\u0010\b¨\u0006 "}, d2 = {"Lokhttp3/internal/ws/WebSocketWriter$FrameSink;", "Lokio/Sink;", "(Lokhttp3/internal/ws/WebSocketWriter;)V", "closed", "", "getClosed", "()Z", "setClosed", "(Z)V", "contentLength", "", "getContentLength", "()J", "setContentLength", "(J)V", "formatOpcode", "", "getFormatOpcode", "()I", "setFormatOpcode", "(I)V", "isFirstFrame", "setFirstFrame", "close", "", "flush", "timeout", "Lokio/Timeout;", "write", FirebaseAnalytics.Param.SOURCE, "Lokio/Buffer;", "byteCount", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public final class FrameSink implements Sink {
        private boolean closed;
        private long contentLength;
        private int formatOpcode;
        private boolean isFirstFrame;

        /* JADX WARN: Incorrect args count in method signature: ()V */
        public FrameSink() {
        }

        public final int getFormatOpcode() {
            return this.formatOpcode;
        }

        public final void setFormatOpcode(int i) {
            this.formatOpcode = i;
        }

        public final long getContentLength() {
            return this.contentLength;
        }

        public final void setContentLength(long j) {
            this.contentLength = j;
        }

        public final boolean isFirstFrame() {
            return this.isFirstFrame;
        }

        public final void setFirstFrame(boolean z) {
            this.isFirstFrame = z;
        }

        public final boolean getClosed() {
            return this.closed;
        }

        public final void setClosed(boolean z) {
            this.closed = z;
        }

        @Override // okio.Sink
        public void write(Buffer source, long byteCount) throws IOException {
            Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
            if (!this.closed) {
                WebSocketWriter.this.getBuffer().write(source, byteCount);
                boolean deferWrite = this.isFirstFrame && this.contentLength != -1 && WebSocketWriter.this.getBuffer().size() > this.contentLength - ((long) 8192);
                long emitCount = WebSocketWriter.this.getBuffer().completeSegmentByteCount();
                if (emitCount > 0 && !deferWrite) {
                    WebSocketWriter.this.writeMessageFrame(this.formatOpcode, emitCount, this.isFirstFrame, false);
                    this.isFirstFrame = false;
                    return;
                }
                return;
            }
            throw new IOException("closed");
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            if (!this.closed) {
                WebSocketWriter webSocketWriter = WebSocketWriter.this;
                webSocketWriter.writeMessageFrame(this.formatOpcode, webSocketWriter.getBuffer().size(), this.isFirstFrame, false);
                this.isFirstFrame = false;
                return;
            }
            throw new IOException("closed");
        }

        @Override // okio.Sink
        public Timeout timeout() {
            return WebSocketWriter.this.getSink().timeout();
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                WebSocketWriter webSocketWriter = WebSocketWriter.this;
                webSocketWriter.writeMessageFrame(this.formatOpcode, webSocketWriter.getBuffer().size(), this.isFirstFrame, true);
                this.closed = true;
                WebSocketWriter.this.setActiveWriter(false);
                return;
            }
            throw new IOException("closed");
        }
    }
}
