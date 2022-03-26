package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
/* loaded from: classes3.dex */
final class ReaderInputStream extends InputStream {
    private ByteBuffer byteBuffer;
    private CharBuffer charBuffer;
    private boolean doneFlushing;
    private boolean draining;
    private final CharsetEncoder encoder;
    private boolean endOfInput;
    private final Reader reader;
    private final byte[] singleByte;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReaderInputStream(Reader reader, Charset charset, int bufferSize) {
        this(reader, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
    }

    ReaderInputStream(Reader reader, CharsetEncoder encoder, int bufferSize) {
        boolean z = true;
        this.singleByte = new byte[1];
        this.reader = (Reader) Preconditions.checkNotNull(reader);
        this.encoder = (CharsetEncoder) Preconditions.checkNotNull(encoder);
        Preconditions.checkArgument(bufferSize <= 0 ? false : z, "bufferSize must be positive: %s", bufferSize);
        encoder.reset();
        this.charBuffer = CharBuffer.allocate(bufferSize);
        this.charBuffer.flip();
        this.byteBuffer = ByteBuffer.allocate(bufferSize);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.reader.close();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (read(this.singleByte) == 1) {
            return UnsignedBytes.toInt(this.singleByte[0]);
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0029, code lost:
        if (r1 <= 0) goto L_0x002d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002d, code lost:
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:?, code lost:
        return r1;
     */
    @Override // java.io.InputStream
    /* Code decompiled incorrectly, please refer to instructions dump */
    public int read(byte[] b, int off, int len) throws IOException {
        CoderResult result;
        Preconditions.checkPositionIndexes(off, off + len, b.length);
        if (len == 0) {
            return 0;
        }
        int totalBytesRead = 0;
        boolean doneEncoding = this.endOfInput;
        while (true) {
            if (this.draining) {
                totalBytesRead += drain(b, off + totalBytesRead, len - totalBytesRead);
                if (totalBytesRead == len || this.doneFlushing) {
                    break;
                }
                this.draining = false;
                this.byteBuffer.clear();
            }
            while (true) {
                if (this.doneFlushing) {
                    result = CoderResult.UNDERFLOW;
                } else if (doneEncoding) {
                    result = this.encoder.flush(this.byteBuffer);
                } else {
                    result = this.encoder.encode(this.charBuffer, this.byteBuffer, this.endOfInput);
                }
                if (result.isOverflow()) {
                    startDraining(true);
                    break;
                } else if (result.isUnderflow()) {
                    if (doneEncoding) {
                        this.doneFlushing = true;
                        startDraining(false);
                        break;
                    } else if (this.endOfInput) {
                        doneEncoding = true;
                    } else {
                        readMoreChars();
                    }
                } else if (result.isError()) {
                    result.throwException();
                    return 0;
                }
            }
        }
    }

    private static CharBuffer grow(CharBuffer buf) {
        CharBuffer bigger = CharBuffer.wrap(Arrays.copyOf(buf.array(), buf.capacity() * 2));
        bigger.position(buf.position());
        bigger.limit(buf.limit());
        return bigger;
    }

    private void readMoreChars() throws IOException {
        if (availableCapacity(this.charBuffer) == 0) {
            if (this.charBuffer.position() > 0) {
                this.charBuffer.compact().flip();
            } else {
                this.charBuffer = grow(this.charBuffer);
            }
        }
        int limit = this.charBuffer.limit();
        int numChars = this.reader.read(this.charBuffer.array(), limit, availableCapacity(this.charBuffer));
        if (numChars == -1) {
            this.endOfInput = true;
        } else {
            this.charBuffer.limit(limit + numChars);
        }
    }

    private static int availableCapacity(Buffer buffer) {
        return buffer.capacity() - buffer.limit();
    }

    private void startDraining(boolean overflow) {
        this.byteBuffer.flip();
        if (!overflow || this.byteBuffer.remaining() != 0) {
            this.draining = true;
        } else {
            this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() * 2);
        }
    }

    private int drain(byte[] b, int off, int len) {
        int remaining = Math.min(len, this.byteBuffer.remaining());
        this.byteBuffer.get(b, off, remaining);
        return remaining;
    }
}
