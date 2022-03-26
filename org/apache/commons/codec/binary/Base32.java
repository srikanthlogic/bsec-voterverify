package org.apache.commons.codec.binary;

import com.google.common.base.Ascii;
import org.apache.commons.codec.binary.BaseNCodec;
/* loaded from: classes3.dex */
public class Base32 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 5;
    private static final int BYTES_PER_ENCODED_BLOCK = 8;
    private static final int BYTES_PER_UNENCODED_BLOCK = 5;
    private static final byte[] CHUNK_SEPARATOR = {Ascii.CR, 10};
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, Ascii.VT, Ascii.FF, Ascii.CR, Ascii.SO, Ascii.SI, Ascii.DLE, 17, Ascii.DC2, 19, Ascii.DC4, Ascii.NAK, Ascii.SYN, Ascii.ETB, Ascii.CAN, Ascii.EM};
    private static final byte[] ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55};
    private static final byte[] HEX_DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, Ascii.VT, Ascii.FF, Ascii.CR, Ascii.SO, Ascii.SI, Ascii.DLE, 17, Ascii.DC2, 19, Ascii.DC4, Ascii.NAK, Ascii.SYN, Ascii.ETB, Ascii.CAN, Ascii.EM, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, 32};
    private static final byte[] HEX_ENCODE_TABLE = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86};
    private static final int MASK_5BITS = 31;
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;

    public Base32() {
        this(false);
    }

    public Base32(int i) {
        this(i, CHUNK_SEPARATOR);
    }

    public Base32(int i, byte[] bArr) {
        this(i, bArr, false);
    }

    public Base32(int i, byte[] bArr, boolean z) {
        super(5, 8, i, bArr == null ? 0 : bArr.length);
        if (z) {
            this.encodeTable = HEX_ENCODE_TABLE;
            this.decodeTable = HEX_DECODE_TABLE;
        } else {
            this.encodeTable = ENCODE_TABLE;
            this.decodeTable = DECODE_TABLE;
        }
        if (i <= 0) {
            this.encodeSize = 8;
            this.lineSeparator = null;
        } else if (bArr == null) {
            throw new IllegalArgumentException("lineLength " + i + " > 0, but lineSeparator is null");
        } else if (!containsAlphabetOrPad(bArr)) {
            this.encodeSize = bArr.length + 8;
            this.lineSeparator = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.lineSeparator, 0, bArr.length);
        } else {
            String newStringUtf8 = StringUtils.newStringUtf8(bArr);
            throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + newStringUtf8 + "]");
        }
        this.decodeSize = this.encodeSize - 1;
    }

    public Base32(boolean z) {
        this(0, null, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.commons.codec.binary.BaseNCodec
    public void decode(byte[] bArr, int i, int i2, BaseNCodec.Context context) {
        int i3;
        byte b;
        if (!context.eof) {
            boolean z = true;
            if (i2 < 0) {
                context.eof = true;
            }
            int i4 = 0;
            int i5 = i;
            while (true) {
                if (i4 >= i2) {
                    break;
                }
                i5++;
                byte b2 = bArr[i5];
                if (b2 == 61) {
                    context.eof = z;
                    break;
                }
                byte[] ensureBufferSize = ensureBufferSize(this.decodeSize, context);
                if (b2 >= 0) {
                    byte[] bArr2 = this.decodeTable;
                    if (b2 < bArr2.length && (b = bArr2[b2]) >= 0) {
                        int i6 = context.modulus;
                        int i7 = z ? 1 : 0;
                        int i8 = z ? 1 : 0;
                        int i9 = z ? 1 : 0;
                        context.modulus = (i6 + i7) % 8;
                        i3 = i4;
                        context.lbitWorkArea = (context.lbitWorkArea << 5) + ((long) b);
                        if (context.modulus == 0) {
                            int i10 = context.pos;
                            context.pos = i10 + 1;
                            ensureBufferSize[i10] = (byte) ((int) ((context.lbitWorkArea >> 32) & 255));
                            int i11 = context.pos;
                            context.pos = i11 + 1;
                            ensureBufferSize[i11] = (byte) ((int) ((context.lbitWorkArea >> 24) & 255));
                            int i12 = context.pos;
                            context.pos = i12 + 1;
                            ensureBufferSize[i12] = (byte) ((int) ((context.lbitWorkArea >> 16) & 255));
                            int i13 = context.pos;
                            context.pos = i13 + 1;
                            ensureBufferSize[i13] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                            int i14 = context.pos;
                            context.pos = i14 + 1;
                            ensureBufferSize[i14] = (byte) ((int) (context.lbitWorkArea & 255));
                        }
                        i4 = i3 + 1;
                        z = true;
                    }
                }
                i3 = i4;
                i4 = i3 + 1;
                z = true;
            }
            if (context.eof && context.modulus >= 2) {
                byte[] ensureBufferSize2 = ensureBufferSize(this.decodeSize, context);
                switch (context.modulus) {
                    case 2:
                        int i15 = context.pos;
                        context.pos = i15 + 1;
                        ensureBufferSize2[i15] = (byte) ((int) ((context.lbitWorkArea >> 2) & 255));
                        return;
                    case 3:
                        int i16 = context.pos;
                        context.pos = i16 + 1;
                        ensureBufferSize2[i16] = (byte) ((int) ((context.lbitWorkArea >> 7) & 255));
                        return;
                    case 4:
                        context.lbitWorkArea >>= 4;
                        int i17 = context.pos;
                        context.pos = i17 + 1;
                        ensureBufferSize2[i17] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i18 = context.pos;
                        context.pos = i18 + 1;
                        ensureBufferSize2[i18] = (byte) ((int) (context.lbitWorkArea & 255));
                        return;
                    case 5:
                        context.lbitWorkArea >>= 1;
                        int i19 = context.pos;
                        context.pos = i19 + 1;
                        ensureBufferSize2[i19] = (byte) ((int) ((context.lbitWorkArea >> 16) & 255));
                        int i20 = context.pos;
                        context.pos = i20 + 1;
                        ensureBufferSize2[i20] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i21 = context.pos;
                        context.pos = i21 + 1;
                        ensureBufferSize2[i21] = (byte) ((int) (context.lbitWorkArea & 255));
                        return;
                    case 6:
                        context.lbitWorkArea >>= 6;
                        int i22 = context.pos;
                        context.pos = i22 + 1;
                        ensureBufferSize2[i22] = (byte) ((int) ((context.lbitWorkArea >> 16) & 255));
                        int i23 = context.pos;
                        context.pos = i23 + 1;
                        ensureBufferSize2[i23] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i24 = context.pos;
                        context.pos = i24 + 1;
                        ensureBufferSize2[i24] = (byte) ((int) (context.lbitWorkArea & 255));
                        return;
                    case 7:
                        context.lbitWorkArea >>= 3;
                        int i25 = context.pos;
                        context.pos = i25 + 1;
                        ensureBufferSize2[i25] = (byte) ((int) ((context.lbitWorkArea >> 24) & 255));
                        int i26 = context.pos;
                        context.pos = i26 + 1;
                        ensureBufferSize2[i26] = (byte) ((int) ((context.lbitWorkArea >> 16) & 255));
                        int i27 = context.pos;
                        context.pos = i27 + 1;
                        ensureBufferSize2[i27] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i28 = context.pos;
                        context.pos = i28 + 1;
                        ensureBufferSize2[i28] = (byte) ((int) (context.lbitWorkArea & 255));
                        return;
                    default:
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
                            ensureBufferSize[i5] = this.encodeTable[((int) (context.lbitWorkArea >> 3)) & 31];
                            int i6 = context.pos;
                            context.pos = i6 + 1;
                            ensureBufferSize[i6] = this.encodeTable[((int) (context.lbitWorkArea << 2)) & 31];
                            int i7 = context.pos;
                            context.pos = i7 + 1;
                            ensureBufferSize[i7] = 61;
                            int i8 = context.pos;
                            context.pos = i8 + 1;
                            ensureBufferSize[i8] = 61;
                            int i9 = context.pos;
                            context.pos = i9 + 1;
                            ensureBufferSize[i9] = 61;
                            int i10 = context.pos;
                            context.pos = i10 + 1;
                            ensureBufferSize[i10] = 61;
                            int i11 = context.pos;
                            context.pos = i11 + 1;
                            ensureBufferSize[i11] = 61;
                            int i12 = context.pos;
                            context.pos = i12 + 1;
                            ensureBufferSize[i12] = 61;
                        } else if (i4 == 2) {
                            int i13 = context.pos;
                            context.pos = i13 + 1;
                            ensureBufferSize[i13] = this.encodeTable[((int) (context.lbitWorkArea >> 11)) & 31];
                            int i14 = context.pos;
                            context.pos = i14 + 1;
                            ensureBufferSize[i14] = this.encodeTable[((int) (context.lbitWorkArea >> 6)) & 31];
                            int i15 = context.pos;
                            context.pos = i15 + 1;
                            ensureBufferSize[i15] = this.encodeTable[((int) (context.lbitWorkArea >> 1)) & 31];
                            int i16 = context.pos;
                            context.pos = i16 + 1;
                            ensureBufferSize[i16] = this.encodeTable[((int) (context.lbitWorkArea << 4)) & 31];
                            int i17 = context.pos;
                            context.pos = i17 + 1;
                            ensureBufferSize[i17] = 61;
                            int i18 = context.pos;
                            context.pos = i18 + 1;
                            ensureBufferSize[i18] = 61;
                            int i19 = context.pos;
                            context.pos = i19 + 1;
                            ensureBufferSize[i19] = 61;
                            int i20 = context.pos;
                            context.pos = i20 + 1;
                            ensureBufferSize[i20] = 61;
                        } else if (i4 == 3) {
                            int i21 = context.pos;
                            context.pos = i21 + 1;
                            ensureBufferSize[i21] = this.encodeTable[((int) (context.lbitWorkArea >> 19)) & 31];
                            int i22 = context.pos;
                            context.pos = i22 + 1;
                            ensureBufferSize[i22] = this.encodeTable[((int) (context.lbitWorkArea >> 14)) & 31];
                            int i23 = context.pos;
                            context.pos = i23 + 1;
                            ensureBufferSize[i23] = this.encodeTable[((int) (context.lbitWorkArea >> 9)) & 31];
                            int i24 = context.pos;
                            context.pos = i24 + 1;
                            ensureBufferSize[i24] = this.encodeTable[((int) (context.lbitWorkArea >> 4)) & 31];
                            int i25 = context.pos;
                            context.pos = i25 + 1;
                            ensureBufferSize[i25] = this.encodeTable[((int) (context.lbitWorkArea << 1)) & 31];
                            int i26 = context.pos;
                            context.pos = i26 + 1;
                            ensureBufferSize[i26] = 61;
                            int i27 = context.pos;
                            context.pos = i27 + 1;
                            ensureBufferSize[i27] = 61;
                            int i28 = context.pos;
                            context.pos = i28 + 1;
                            ensureBufferSize[i28] = 61;
                        } else if (i4 == 4) {
                            int i29 = context.pos;
                            context.pos = i29 + 1;
                            ensureBufferSize[i29] = this.encodeTable[((int) (context.lbitWorkArea >> 27)) & 31];
                            int i30 = context.pos;
                            context.pos = i30 + 1;
                            ensureBufferSize[i30] = this.encodeTable[((int) (context.lbitWorkArea >> 22)) & 31];
                            int i31 = context.pos;
                            context.pos = i31 + 1;
                            ensureBufferSize[i31] = this.encodeTable[((int) (context.lbitWorkArea >> 17)) & 31];
                            int i32 = context.pos;
                            context.pos = i32 + 1;
                            ensureBufferSize[i32] = this.encodeTable[((int) (context.lbitWorkArea >> 12)) & 31];
                            int i33 = context.pos;
                            context.pos = i33 + 1;
                            ensureBufferSize[i33] = this.encodeTable[((int) (context.lbitWorkArea >> 7)) & 31];
                            int i34 = context.pos;
                            context.pos = i34 + 1;
                            ensureBufferSize[i34] = this.encodeTable[((int) (context.lbitWorkArea >> 2)) & 31];
                            int i35 = context.pos;
                            context.pos = i35 + 1;
                            ensureBufferSize[i35] = this.encodeTable[((int) (context.lbitWorkArea << 3)) & 31];
                            int i36 = context.pos;
                            context.pos = i36 + 1;
                            ensureBufferSize[i36] = 61;
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
            int i37 = i;
            for (int i38 = 0; i38 < i2; i38++) {
                byte[] ensureBufferSize2 = ensureBufferSize(this.encodeSize, context);
                context.modulus = (context.modulus + 1) % 5;
                i37++;
                byte b = bArr[i37];
                int i39 = b;
                if (b < 0) {
                    i39 = b + 256;
                }
                context.lbitWorkArea = (context.lbitWorkArea << 8) + (i39 == 1 ? 1 : 0);
                if (context.modulus == 0) {
                    int i40 = context.pos;
                    context.pos = i40 + 1;
                    ensureBufferSize2[i40] = this.encodeTable[((int) (context.lbitWorkArea >> 35)) & 31];
                    int i41 = context.pos;
                    context.pos = i41 + 1;
                    ensureBufferSize2[i41] = this.encodeTable[((int) (context.lbitWorkArea >> 30)) & 31];
                    int i42 = context.pos;
                    context.pos = i42 + 1;
                    ensureBufferSize2[i42] = this.encodeTable[((int) (context.lbitWorkArea >> 25)) & 31];
                    int i43 = context.pos;
                    context.pos = i43 + 1;
                    ensureBufferSize2[i43] = this.encodeTable[((int) (context.lbitWorkArea >> 20)) & 31];
                    int i44 = context.pos;
                    context.pos = i44 + 1;
                    ensureBufferSize2[i44] = this.encodeTable[((int) (context.lbitWorkArea >> 15)) & 31];
                    int i45 = context.pos;
                    context.pos = i45 + 1;
                    ensureBufferSize2[i45] = this.encodeTable[((int) (context.lbitWorkArea >> 10)) & 31];
                    int i46 = context.pos;
                    context.pos = i46 + 1;
                    ensureBufferSize2[i46] = this.encodeTable[((int) (context.lbitWorkArea >> 5)) & 31];
                    int i47 = context.pos;
                    context.pos = i47 + 1;
                    ensureBufferSize2[i47] = this.encodeTable[((int) context.lbitWorkArea) & 31];
                    context.currentLinePos += 8;
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
    public boolean isInAlphabet(byte b) {
        if (b >= 0) {
            byte[] bArr = this.decodeTable;
            if (b < bArr.length && bArr[b] != -1) {
                return true;
            }
        }
        return false;
    }
}
