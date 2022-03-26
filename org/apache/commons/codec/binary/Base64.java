package org.apache.commons.codec.binary;

import com.google.common.base.Ascii;
import java.math.BigInteger;
import okio.Utf8;
import org.apache.commons.codec.binary.BaseNCodec;
/* loaded from: classes3.dex */
public class Base64 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 6;
    private static final int BYTES_PER_ENCODED_BLOCK = 4;
    private static final int BYTES_PER_UNENCODED_BLOCK = 3;
    private static final int MASK_6BITS = 63;
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;
    static final byte[] CHUNK_SEPARATOR = {Ascii.CR, 10};
    private static final byte[] STANDARD_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, Utf8.REPLACEMENT_BYTE, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, Ascii.VT, Ascii.FF, Ascii.CR, Ascii.SO, Ascii.SI, Ascii.DLE, 17, Ascii.DC2, 19, Ascii.DC4, Ascii.NAK, Ascii.SYN, Ascii.ETB, Ascii.CAN, Ascii.EM, -1, -1, -1, -1, Utf8.REPLACEMENT_BYTE, -1, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

    public Base64() {
        this(0);
    }

    public Base64(int i) {
        this(i, CHUNK_SEPARATOR);
    }

    public Base64(int i, byte[] bArr) {
        this(i, bArr, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0059  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public Base64(int i, byte[] bArr, boolean z) {
        super(3, 4, i, bArr == null ? 0 : bArr.length);
        this.decodeTable = DECODE_TABLE;
        if (bArr != null) {
            if (containsAlphabetOrPad(bArr)) {
                String newStringUtf8 = StringUtils.newStringUtf8(bArr);
                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + newStringUtf8 + "]");
            } else if (i > 0) {
                this.encodeSize = bArr.length + 4;
                this.lineSeparator = new byte[bArr.length];
                System.arraycopy(bArr, 0, this.lineSeparator, 0, bArr.length);
                this.decodeSize = this.encodeSize - 1;
                this.encodeTable = !z ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
            }
        }
        this.encodeSize = 4;
        this.lineSeparator = null;
        this.decodeSize = this.encodeSize - 1;
        this.encodeTable = !z ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
    }

    public Base64(boolean z) {
        this(76, CHUNK_SEPARATOR, z);
    }

    public static byte[] decodeBase64(String str) {
        return new Base64().decode(str);
    }

    public static byte[] decodeBase64(byte[] bArr) {
        return new Base64().decode(bArr);
    }

    public static BigInteger decodeInteger(byte[] bArr) {
        return new BigInteger(1, decodeBase64(bArr));
    }

    public static byte[] encodeBase64(byte[] bArr) {
        return encodeBase64(bArr, false);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z) {
        return encodeBase64(bArr, z, false);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z, boolean z2) {
        return encodeBase64(bArr, z, z2, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z, boolean z2, int i) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        Base64 base64 = z ? new Base64(z2) : new Base64(0, CHUNK_SEPARATOR, z2);
        long encodedLength = base64.getEncodedLength(bArr);
        if (encodedLength <= ((long) i)) {
            return base64.encode(bArr);
        }
        throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + encodedLength + ") than the specified maximum size of " + i);
    }

    public static byte[] encodeBase64Chunked(byte[] bArr) {
        return encodeBase64(bArr, true);
    }

    public static String encodeBase64String(byte[] bArr) {
        return StringUtils.newStringUtf8(encodeBase64(bArr, false));
    }

    public static byte[] encodeBase64URLSafe(byte[] bArr) {
        return encodeBase64(bArr, false, true);
    }

    public static String encodeBase64URLSafeString(byte[] bArr) {
        return StringUtils.newStringUtf8(encodeBase64(bArr, false, true));
    }

    public static byte[] encodeInteger(BigInteger bigInteger) {
        if (bigInteger != null) {
            return encodeBase64(toIntegerBytes(bigInteger), false);
        }
        throw new NullPointerException("encodeInteger called with null parameter");
    }

    @Deprecated
    public static boolean isArrayByteBase64(byte[] bArr) {
        return isBase64(bArr);
    }

    public static boolean isBase64(byte b) {
        if (b != 61) {
            if (b >= 0) {
                byte[] bArr = DECODE_TABLE;
                if (b >= bArr.length || bArr[b] == -1) {
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isBase64(String str) {
        return isBase64(StringUtils.getBytesUtf8(str));
    }

    public static boolean isBase64(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            if (!(isBase64(bArr[i]) || isWhiteSpace(bArr[i]))) {
                return false;
            }
        }
        return true;
    }

    static byte[] toIntegerBytes(BigInteger bigInteger) {
        int bitLength = ((bigInteger.bitLength() + 7) >> 3) << 3;
        byte[] byteArray = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 != 0 && (bigInteger.bitLength() / 8) + 1 == bitLength / 8) {
            return byteArray;
        }
        int i = 0;
        int length = byteArray.length;
        if (bigInteger.bitLength() % 8 == 0) {
            length--;
            i = 1;
        }
        int i2 = bitLength / 8;
        int i3 = i2 - length;
        byte[] bArr = new byte[i2];
        System.arraycopy(byteArray, i, bArr, i3, length);
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.commons.codec.binary.BaseNCodec
    public void decode(byte[] bArr, int i, int i2, BaseNCodec.Context context) {
        byte b;
        if (!context.eof) {
            if (i2 < 0) {
                context.eof = true;
            }
            int i3 = 0;
            while (true) {
                if (i3 >= i2) {
                    break;
                }
                byte[] ensureBufferSize = ensureBufferSize(this.decodeSize, context);
                i++;
                byte b2 = bArr[i];
                if (b2 == 61) {
                    context.eof = true;
                    break;
                }
                if (b2 >= 0) {
                    byte[] bArr2 = DECODE_TABLE;
                    if (b2 < bArr2.length && (b = bArr2[b2]) >= 0) {
                        context.modulus = (context.modulus + 1) % 4;
                        context.ibitWorkArea = (context.ibitWorkArea << 6) + b;
                        if (context.modulus == 0) {
                            int i4 = context.pos;
                            context.pos = i4 + 1;
                            ensureBufferSize[i4] = (byte) ((context.ibitWorkArea >> 16) & 255);
                            int i5 = context.pos;
                            context.pos = i5 + 1;
                            ensureBufferSize[i5] = (byte) ((context.ibitWorkArea >> 8) & 255);
                            int i6 = context.pos;
                            context.pos = i6 + 1;
                            ensureBufferSize[i6] = (byte) (context.ibitWorkArea & 255);
                        }
                    }
                }
                i3++;
            }
            if (context.eof && context.modulus != 0) {
                byte[] ensureBufferSize2 = ensureBufferSize(this.decodeSize, context);
                int i7 = context.modulus;
                if (i7 == 1) {
                    return;
                }
                if (i7 == 2) {
                    context.ibitWorkArea >>= 4;
                    int i8 = context.pos;
                    context.pos = i8 + 1;
                    ensureBufferSize2[i8] = (byte) (context.ibitWorkArea & 255);
                } else if (i7 == 3) {
                    context.ibitWorkArea >>= 2;
                    int i9 = context.pos;
                    context.pos = i9 + 1;
                    ensureBufferSize2[i9] = (byte) ((context.ibitWorkArea >> 8) & 255);
                    int i10 = context.pos;
                    context.pos = i10 + 1;
                    ensureBufferSize2[i10] = (byte) (context.ibitWorkArea & 255);
                } else {
                    throw new IllegalStateException("Impossible modulus " + context.modulus);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.commons.codec.binary.BaseNCodec
    public void encode(byte[] bArr, int i, int i2, BaseNCodec.Context context) {
        if (!context.eof) {
            if (i2 < 0) {
                context.eof = true;
                if (!(context.modulus == 0 && this.lineLength == 0)) {
                    byte[] ensureBufferSize = ensureBufferSize(this.encodeSize, context);
                    int i3 = context.pos;
                    int i4 = context.modulus;
                    if (i4 != 0) {
                        if (i4 == 1) {
                            int i5 = context.pos;
                            context.pos = i5 + 1;
                            ensureBufferSize[i5] = this.encodeTable[(context.ibitWorkArea >> 2) & 63];
                            int i6 = context.pos;
                            context.pos = i6 + 1;
                            ensureBufferSize[i6] = this.encodeTable[(context.ibitWorkArea << 4) & 63];
                            if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                                int i7 = context.pos;
                                context.pos = i7 + 1;
                                ensureBufferSize[i7] = 61;
                                int i8 = context.pos;
                                context.pos = i8 + 1;
                                ensureBufferSize[i8] = 61;
                            }
                        } else if (i4 == 2) {
                            int i9 = context.pos;
                            context.pos = i9 + 1;
                            ensureBufferSize[i9] = this.encodeTable[(context.ibitWorkArea >> 10) & 63];
                            int i10 = context.pos;
                            context.pos = i10 + 1;
                            ensureBufferSize[i10] = this.encodeTable[(context.ibitWorkArea >> 4) & 63];
                            int i11 = context.pos;
                            context.pos = i11 + 1;
                            ensureBufferSize[i11] = this.encodeTable[(context.ibitWorkArea << 2) & 63];
                            if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                                int i12 = context.pos;
                                context.pos = i12 + 1;
                                ensureBufferSize[i12] = 61;
                            }
                        } else {
                            throw new IllegalStateException("Impossible modulus " + context.modulus);
                        }
                    }
                    context.currentLinePos += context.pos - i3;
                    if (this.lineLength > 0 && context.currentLinePos > 0) {
                        System.arraycopy(this.lineSeparator, 0, ensureBufferSize, context.pos, this.lineSeparator.length);
                        context.pos += this.lineSeparator.length;
                        return;
                    }
                    return;
                }
                return;
            }
            int i13 = i;
            for (int i14 = 0; i14 < i2; i14++) {
                byte[] ensureBufferSize2 = ensureBufferSize(this.encodeSize, context);
                context.modulus = (context.modulus + 1) % 3;
                i13++;
                byte b = bArr[i13];
                int i15 = b;
                if (b < 0) {
                    i15 = b + 256;
                }
                context.ibitWorkArea = (context.ibitWorkArea << 8) + (i15 == 1 ? 1 : 0);
                if (context.modulus == 0) {
                    int i16 = context.pos;
                    context.pos = i16 + 1;
                    ensureBufferSize2[i16] = this.encodeTable[(context.ibitWorkArea >> 18) & 63];
                    int i17 = context.pos;
                    context.pos = i17 + 1;
                    ensureBufferSize2[i17] = this.encodeTable[(context.ibitWorkArea >> 12) & 63];
                    int i18 = context.pos;
                    context.pos = i18 + 1;
                    ensureBufferSize2[i18] = this.encodeTable[(context.ibitWorkArea >> 6) & 63];
                    int i19 = context.pos;
                    context.pos = i19 + 1;
                    ensureBufferSize2[i19] = this.encodeTable[context.ibitWorkArea & 63];
                    context.currentLinePos += 4;
                    if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
                        System.arraycopy(this.lineSeparator, 0, ensureBufferSize2, context.pos, this.lineSeparator.length);
                        context.pos += this.lineSeparator.length;
                        context.currentLinePos = 0;
                    }
                }
            }
        }
    }

    @Override // org.apache.commons.codec.binary.BaseNCodec
    protected boolean isInAlphabet(byte b) {
        if (b >= 0) {
            byte[] bArr = this.decodeTable;
            if (b < bArr.length && bArr[b] != -1) {
                return true;
            }
        }
        return false;
    }

    public boolean isUrlSafe() {
        return this.encodeTable == URL_SAFE_ENCODE_TABLE;
    }
}
