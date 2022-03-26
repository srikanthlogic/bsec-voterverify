package org.apache.commons.codec.binary;

import java.util.Arrays;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
/* loaded from: classes3.dex */
public abstract class BaseNCodec implements BinaryDecoder, BinaryEncoder {
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR;
    private static final int DEFAULT_BUFFER_SIZE;
    static final int EOF;
    protected static final int MASK_8BITS;
    public static final int MIME_CHUNK_SIZE;
    protected static final byte PAD_DEFAULT;
    public static final int PEM_CHUNK_SIZE;
    protected final byte PAD = PAD_DEFAULT;
    private final int chunkSeparatorLength;
    private final int encodedBlockSize;
    protected final int lineLength;
    private final int unencodedBlockSize;

    /* loaded from: classes3.dex */
    public static class Context {
        byte[] buffer;
        int currentLinePos;
        boolean eof;
        int ibitWorkArea;
        long lbitWorkArea;
        int modulus;
        int pos;
        int readPos;

        public String toString() {
            return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", getClass().getSimpleName(), Arrays.toString(this.buffer), Integer.valueOf(this.currentLinePos), Boolean.valueOf(this.eof), Integer.valueOf(this.ibitWorkArea), Long.valueOf(this.lbitWorkArea), Integer.valueOf(this.modulus), Integer.valueOf(this.pos), Integer.valueOf(this.readPos));
        }
    }

    public BaseNCodec(int i, int i2, int i3, int i4) {
        this.unencodedBlockSize = i;
        this.encodedBlockSize = i2;
        this.lineLength = i3 > 0 && i4 > 0 ? (i3 / i2) * i2 : 0;
        this.chunkSeparatorLength = i4;
    }

    protected static boolean isWhiteSpace(byte b) {
        return b == 9 || b == 10 || b == 13 || b == 32;
    }

    private byte[] resizeBuffer(Context context) {
        if (context.buffer == null) {
            context.buffer = new byte[getDefaultBufferSize()];
            context.pos = 0;
            context.readPos = 0;
        } else {
            byte[] bArr = new byte[context.buffer.length * 2];
            System.arraycopy(context.buffer, 0, bArr, 0, context.buffer.length);
            context.buffer = bArr;
        }
        return context.buffer;
    }

    public int available(Context context) {
        if (context.buffer != null) {
            return context.pos - context.readPos;
        }
        return 0;
    }

    protected boolean containsAlphabetOrPad(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        for (byte b : bArr) {
            if (61 == b || isInAlphabet(b)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.codec.Decoder
    public Object decode(Object obj) throws DecoderException {
        if (obj instanceof byte[]) {
            return decode((byte[]) obj);
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void decode(byte[] bArr, int i, int i2, Context context);

    public byte[] decode(String str) {
        return decode(StringUtils.getBytesUtf8(str));
    }

    @Override // org.apache.commons.codec.BinaryDecoder
    public byte[] decode(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        Context context = new Context();
        decode(bArr, 0, bArr.length, context);
        decode(bArr, 0, -1, context);
        byte[] bArr2 = new byte[context.pos];
        readResults(bArr2, 0, bArr2.length, context);
        return bArr2;
    }

    @Override // org.apache.commons.codec.Encoder
    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof byte[]) {
            return encode((byte[]) obj);
        }
        throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void encode(byte[] bArr, int i, int i2, Context context);

    @Override // org.apache.commons.codec.BinaryEncoder
    public byte[] encode(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        Context context = new Context();
        encode(bArr, 0, bArr.length, context);
        encode(bArr, 0, -1, context);
        byte[] bArr2 = new byte[context.pos - context.readPos];
        readResults(bArr2, 0, bArr2.length, context);
        return bArr2;
    }

    public String encodeAsString(byte[] bArr) {
        return StringUtils.newStringUtf8(encode(bArr));
    }

    public String encodeToString(byte[] bArr) {
        return StringUtils.newStringUtf8(encode(bArr));
    }

    protected byte[] ensureBufferSize(int i, Context context) {
        return (context.buffer == null || context.buffer.length < context.pos + i) ? resizeBuffer(context) : context.buffer;
    }

    protected int getDefaultBufferSize() {
        return 8192;
    }

    public long getEncodedLength(byte[] bArr) {
        int length = bArr.length;
        int i = this.unencodedBlockSize;
        long j = ((long) (((length + i) - 1) / i)) * ((long) this.encodedBlockSize);
        int i2 = this.lineLength;
        return i2 > 0 ? j + ((((((long) i2) + j) - 1) / ((long) i2)) * ((long) this.chunkSeparatorLength)) : j;
    }

    public boolean hasData(Context context) {
        return context.buffer != null;
    }

    protected abstract boolean isInAlphabet(byte b);

    public boolean isInAlphabet(String str) {
        return isInAlphabet(StringUtils.getBytesUtf8(str), true);
    }

    public boolean isInAlphabet(byte[] bArr, boolean z) {
        for (int i = 0; i < bArr.length; i++) {
            if (!(isInAlphabet(bArr[i]) || (z && (bArr[i] == 61 || isWhiteSpace(bArr[i]))))) {
                return false;
            }
        }
        return true;
    }

    public int readResults(byte[] bArr, int i, int i2, Context context) {
        if (context.buffer == null) {
            return context.eof ? -1 : 0;
        }
        int min = Math.min(available(context), i2);
        System.arraycopy(context.buffer, context.readPos, bArr, i, min);
        context.readPos += min;
        if (context.readPos >= context.pos) {
            context.buffer = null;
        }
        return min;
    }
}
