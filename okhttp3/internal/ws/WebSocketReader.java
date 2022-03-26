package okhttp3.internal.ws;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
/* compiled from: WebSocketReader.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0000\u0018\u00002\u00020\u0001:\u0001$B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\u001d\u001a\u00020\u001eJ\b\u0010\u001f\u001a\u00020\u001eH\u0002J\b\u0010 \u001a\u00020\u001eH\u0002J\b\u0010!\u001a\u00020\u001eH\u0002J\b\u0010\"\u001a\u00020\u001eH\u0002J\b\u0010#\u001a\u00020\u001eH\u0002R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c¨\u0006%"}, d2 = {"Lokhttp3/internal/ws/WebSocketReader;", "", "isClient", "", FirebaseAnalytics.Param.SOURCE, "Lokio/BufferedSource;", "frameCallback", "Lokhttp3/internal/ws/WebSocketReader$FrameCallback;", "(ZLokio/BufferedSource;Lokhttp3/internal/ws/WebSocketReader$FrameCallback;)V", "closed", "getClosed", "()Z", "setClosed", "(Z)V", "controlFrameBuffer", "Lokio/Buffer;", "frameLength", "", "isControlFrame", "isFinalFrame", "maskCursor", "Lokio/Buffer$UnsafeCursor;", "maskKey", "", "messageFrameBuffer", "opcode", "", "getSource", "()Lokio/BufferedSource;", "processNextFrame", "", "readControlFrame", "readHeader", "readMessage", "readMessageFrame", "readUntilNonControlFrame", "FrameCallback", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class WebSocketReader {
    private boolean closed;
    private final FrameCallback frameCallback;
    private long frameLength;
    private final boolean isClient;
    private boolean isControlFrame;
    private boolean isFinalFrame;
    private final Buffer.UnsafeCursor maskCursor;
    private final byte[] maskKey;
    private int opcode;
    private final BufferedSource source;
    private final Buffer controlFrameBuffer = new Buffer();
    private final Buffer messageFrameBuffer = new Buffer();

    /* compiled from: WebSocketReader.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH&J\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000bH&J\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000bH&¨\u0006\u000f"}, d2 = {"Lokhttp3/internal/ws/WebSocketReader$FrameCallback;", "", "onReadClose", "", "code", "", "reason", "", "onReadMessage", "text", "bytes", "Lokio/ByteString;", "onReadPing", "payload", "onReadPong", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public interface FrameCallback {
        void onReadClose(int i, String str);

        void onReadMessage(String str) throws IOException;

        void onReadMessage(ByteString byteString) throws IOException;

        void onReadPing(ByteString byteString);

        void onReadPong(ByteString byteString);
    }

    public WebSocketReader(boolean isClient, BufferedSource source, FrameCallback frameCallback) {
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        Intrinsics.checkParameterIsNotNull(frameCallback, "frameCallback");
        this.isClient = isClient;
        this.source = source;
        this.frameCallback = frameCallback;
        Buffer.UnsafeCursor unsafeCursor = null;
        this.maskKey = this.isClient ? null : new byte[4];
        this.maskCursor = !this.isClient ? new Buffer.UnsafeCursor() : unsafeCursor;
    }

    public final BufferedSource getSource() {
        return this.source;
    }

    public final boolean getClosed() {
        return this.closed;
    }

    public final void setClosed(boolean z) {
        this.closed = z;
    }

    public final void processNextFrame() throws IOException {
        readHeader();
        if (this.isControlFrame) {
            readControlFrame();
        } else {
            readMessageFrame();
        }
    }

    /* JADX WARN: Finally extract failed */
    private final void readHeader() throws IOException, ProtocolException {
        String str;
        if (!this.closed) {
            long timeoutBefore = this.source.timeout().timeoutNanos();
            this.source.timeout().clearTimeout();
            try {
                int b0 = Util.and(this.source.readByte(), 255);
                this.source.timeout().timeout(timeoutBefore, TimeUnit.NANOSECONDS);
                this.opcode = b0 & 15;
                boolean isMasked = true;
                this.isFinalFrame = (b0 & 128) != 0;
                this.isControlFrame = (b0 & 8) != 0;
                if (!this.isControlFrame || this.isFinalFrame) {
                    boolean reservedFlag1 = (b0 & 64) != 0;
                    boolean reservedFlag2 = (b0 & 32) != 0;
                    boolean reservedFlag3 = (b0 & 16) != 0;
                    if (reservedFlag1 || reservedFlag2 || reservedFlag3) {
                        throw new ProtocolException("Reserved flags are unsupported.");
                    }
                    int b1 = Util.and(this.source.readByte(), 255);
                    if ((b1 & 128) == 0) {
                        isMasked = false;
                    }
                    boolean z = this.isClient;
                    if (isMasked == z) {
                        if (z) {
                            str = "Server-sent frames must not be masked.";
                        } else {
                            str = "Client-sent frames must be masked.";
                        }
                        throw new ProtocolException(str);
                    }
                    this.frameLength = (long) (b1 & 127);
                    long j = this.frameLength;
                    if (j == ((long) 126)) {
                        this.frameLength = (long) Util.and(this.source.readShort(), 65535);
                    } else if (j == ((long) 127)) {
                        this.frameLength = this.source.readLong();
                        if (this.frameLength < 0) {
                            throw new ProtocolException("Frame length 0x" + Util.toHexString(this.frameLength) + " > 0x7FFFFFFFFFFFFFFF");
                        }
                    }
                    if (this.isControlFrame && this.frameLength > 125) {
                        throw new ProtocolException("Control frame must be less than 125B.");
                    } else if (isMasked) {
                        BufferedSource bufferedSource = this.source;
                        byte[] bArr = this.maskKey;
                        if (bArr == null) {
                            Intrinsics.throwNpe();
                        }
                        bufferedSource.readFully(bArr);
                    }
                } else {
                    throw new ProtocolException("Control frames must be final.");
                }
            } catch (Throwable th) {
                this.source.timeout().timeout(timeoutBefore, TimeUnit.NANOSECONDS);
                throw th;
            }
        } else {
            throw new IOException("closed");
        }
    }

    private final void readControlFrame() throws IOException {
        long j = this.frameLength;
        if (j > 0) {
            this.source.readFully(this.controlFrameBuffer, j);
            if (!this.isClient) {
                Buffer buffer = this.controlFrameBuffer;
                Buffer.UnsafeCursor unsafeCursor = this.maskCursor;
                if (unsafeCursor == null) {
                    Intrinsics.throwNpe();
                }
                buffer.readAndWriteUnsafe(unsafeCursor);
                this.maskCursor.seek(0);
                WebSocketProtocol webSocketProtocol = WebSocketProtocol.INSTANCE;
                Buffer.UnsafeCursor unsafeCursor2 = this.maskCursor;
                byte[] bArr = this.maskKey;
                if (bArr == null) {
                    Intrinsics.throwNpe();
                }
                webSocketProtocol.toggleMask(unsafeCursor2, bArr);
                this.maskCursor.close();
            }
        }
        switch (this.opcode) {
            case 8:
                int code = 1005;
                String reason = "";
                long bufferSize = this.controlFrameBuffer.size();
                if (bufferSize != 1) {
                    if (bufferSize != 0) {
                        code = this.controlFrameBuffer.readShort();
                        reason = this.controlFrameBuffer.readUtf8();
                        String codeExceptionMessage = WebSocketProtocol.INSTANCE.closeCodeExceptionMessage(code);
                        if (codeExceptionMessage != null) {
                            throw new ProtocolException(codeExceptionMessage);
                        }
                    }
                    this.frameCallback.onReadClose(code, reason);
                    this.closed = true;
                    return;
                }
                throw new ProtocolException("Malformed close payload length of 1.");
            case 9:
                this.frameCallback.onReadPing(this.controlFrameBuffer.readByteString());
                return;
            case 10:
                this.frameCallback.onReadPong(this.controlFrameBuffer.readByteString());
                return;
            default:
                throw new ProtocolException("Unknown control opcode: " + Util.toHexString(this.opcode));
        }
    }

    private final void readMessageFrame() throws IOException {
        int opcode = this.opcode;
        if (opcode == 1 || opcode == 2) {
            readMessage();
            if (opcode == 1) {
                this.frameCallback.onReadMessage(this.messageFrameBuffer.readUtf8());
            } else {
                this.frameCallback.onReadMessage(this.messageFrameBuffer.readByteString());
            }
        } else {
            throw new ProtocolException("Unknown opcode: " + Util.toHexString(opcode));
        }
    }

    private final void readUntilNonControlFrame() throws IOException {
        while (!this.closed) {
            readHeader();
            if (this.isControlFrame) {
                readControlFrame();
            } else {
                return;
            }
        }
    }

    private final void readMessage() throws IOException {
        while (!this.closed) {
            long j = this.frameLength;
            if (j > 0) {
                this.source.readFully(this.messageFrameBuffer, j);
                if (!this.isClient) {
                    Buffer buffer = this.messageFrameBuffer;
                    Buffer.UnsafeCursor unsafeCursor = this.maskCursor;
                    if (unsafeCursor == null) {
                        Intrinsics.throwNpe();
                    }
                    buffer.readAndWriteUnsafe(unsafeCursor);
                    this.maskCursor.seek(this.messageFrameBuffer.size() - this.frameLength);
                    WebSocketProtocol webSocketProtocol = WebSocketProtocol.INSTANCE;
                    Buffer.UnsafeCursor unsafeCursor2 = this.maskCursor;
                    byte[] bArr = this.maskKey;
                    if (bArr == null) {
                        Intrinsics.throwNpe();
                    }
                    webSocketProtocol.toggleMask(unsafeCursor2, bArr);
                    this.maskCursor.close();
                }
            }
            if (!this.isFinalFrame) {
                readUntilNonControlFrame();
                if (this.opcode != 0) {
                    throw new ProtocolException("Expected continuation opcode. Got: " + Util.toHexString(this.opcode));
                }
            } else {
                return;
            }
        }
        throw new IOException("closed");
    }
}
