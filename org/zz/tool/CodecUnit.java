package org.zz.tool;

import kotlin.text.Typography;
/* loaded from: classes3.dex */
public class CodecUnit {
    byte[] SignedIntToByte(int test) {
        return new byte[]{(byte) test, (byte) (test >> 8), (byte) (test >> 16), (byte) (test >> 24)};
    }

    public static int JUnsigned(int x) {
        if (x >= 0) {
            return x;
        }
        return x + 256;
    }

    public static void EncData(byte[] lpRawData, int nRawLen, byte[] lpEncData) {
        for (int i = 0; i < nRawLen; i++) {
            int aaa = JUnsigned(lpRawData[i]);
            lpEncData[i * 2] = (byte) ((aaa >> 4) + 48);
            lpEncData[(i * 2) + 1] = (byte) ((aaa & 15) + 48);
        }
    }

    public static void DecData(byte[] lpEncData, int nRawLen, byte[] lpRawData) {
        for (int i = 0; i < nRawLen; i++) {
            lpRawData[i] = (byte) (((lpEncData[i * 2] - 48) << 4) + (lpEncData[(i * 2) + 1] - 48));
        }
    }

    static byte GetAsc(byte b) {
        if (b < 10) {
            return (byte) (b + 48);
        }
        return (byte) ((b - 10) + 97);
    }

    public static void HexToAsc(byte[] lpHexData, int nHexLength, byte[] lpAscData) {
        for (short i = 0; i < nHexLength; i = (short) (i + 1)) {
            int aaa = JUnsigned(lpHexData[i]);
            lpAscData[i * 2] = GetAsc((byte) (aaa >> 4));
            lpAscData[(i * 2) + 1] = GetAsc((byte) (aaa & 15));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00c9  */
    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference failed for: r11v2 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static int JavaBase64Encode(byte[] pInput, int inputLen, byte[] pOutput, int outputbufsize) {
        int i;
        int currentin;
        boolean z;
        String codebuffer = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] bArr = new byte[65];
        byte[] encodingTable = codebuffer.getBytes();
        int outlen = ((inputLen + 2) / 3) * 4;
        int modulus = inputLen % 3;
        int datalen = inputLen - modulus;
        int encodedatalen = (datalen * 4) / 3;
        if (outputbufsize < outlen) {
            return 0;
        }
        if (modulus != 0) {
            if (modulus == 1) {
                int j = outlen - 4;
                byte b = pInput[inputLen - 1];
                int currentin2 = b;
                if (b < 0) {
                    currentin2 = b + 256;
                }
                long ltmp = (currentin2 == 1 ? 1 : 0) << 16;
                pOutput[j] = encodingTable[(int) ((ltmp >> 18) & 63)];
                pOutput[j + 1] = encodingTable[(int) ((ltmp >> 12) & 63)];
                pOutput[j + 2] = 61;
                pOutput[j + 3] = 61;
            } else if (modulus != 2) {
                z = false;
            } else {
                int i2 = inputLen - 2;
                int j2 = outlen - 4;
                byte b2 = pInput[i2];
                byte currentin22 = pInput[i2 + 1];
                int currentin3 = b2;
                if (b2 < 0) {
                    currentin3 = b2 + 256;
                }
                if (currentin22 < 0) {
                    currentin22 += 256;
                }
                long ltmp2 = (((long) pInput[i2]) << 16) | ((currentin22 == true ? 1 : 0) << 8);
                pOutput[j2] = encodingTable[(int) ((ltmp2 >> 18) & 63)];
                pOutput[j2 + 1] = encodingTable[(int) ((ltmp2 >> 12) & 63)];
                pOutput[j2 + 2] = encodingTable[(int) ((ltmp2 >> 6) & 63)];
                pOutput[j2 + 3] = 61;
            }
            i = datalen - 3;
            int j3 = encodedatalen - 4;
            while (i >= 0) {
                byte b3 = pInput[i];
                byte currentin23 = pInput[i + 1];
                byte currentin32 = pInput[i + 2];
                if (b3 < 0) {
                    currentin = b3 + 256;
                } else {
                    currentin = b3;
                }
                if (currentin23 < 0) {
                    currentin23 += 256;
                }
                if (currentin32 < 0) {
                    currentin32 += 256;
                }
                long ltmp3 = ((currentin23 == true ? 1 : 0) << 8) | (((long) currentin) << 16) | (currentin32 == true ? 1 : 0);
                pOutput[j3] = encodingTable[(int) ((ltmp3 >> 18) & 63)];
                pOutput[j3 + 1] = encodingTable[(int) ((ltmp3 >> 12) & 63)];
                pOutput[j3 + 2] = encodingTable[(int) ((ltmp3 >> 6) & 63)];
                pOutput[j3 + 3] = encodingTable[(int) (ltmp3 & 63)];
                i -= 3;
                j3 -= 4;
                codebuffer = codebuffer;
            }
            return outlen;
        }
        z = false;
        i = datalen - 3;
        int j32 = encodedatalen - 4;
        while (i >= 0) {
        }
        return outlen;
    }

    public static int JavaBase64Decode(byte[] pInput, int inputLen, byte[] pOutput) {
        int padnum;
        char[] decodingTable = new char[256];
        for (int i = 0; i < 256; i++) {
            decodingTable[i] = 255;
        }
        for (int i2 = 65; i2 <= 90; i2++) {
            decodingTable[i2] = (char) (i2 - 65);
        }
        for (int i3 = 97; i3 <= 122; i3++) {
            decodingTable[i3] = (char) ((i3 - 97) + 26);
        }
        for (int i4 = 48; i4 <= 57; i4++) {
            decodingTable[i4] = (char) ((i4 - 48) + 52);
        }
        decodingTable[43] = Typography.greater;
        decodingTable[47] = '?';
        int i5 = 0;
        if (inputLen % 4 != 0) {
            return 0;
        }
        if (pInput[inputLen - 2] == 61) {
            padnum = 2;
        } else if (pInput[inputLen - 1] == 61) {
            padnum = 1;
        } else {
            padnum = 0;
        }
        int outlen = ((inputLen / 4) * 3) - padnum;
        int datalen = ((inputLen - padnum) / 4) * 3;
        int j = 0;
        int i6 = 0;
        while (true) {
            char c = 6;
            long j2 = 255;
            if (i6 >= datalen) {
                if (padnum != 0) {
                    if (padnum == 1) {
                        long ltmp = 0;
                        int m = inputLen - 4;
                        while (m < inputLen - 1) {
                            char ctmp = decodingTable[pInput[m]];
                            if (ctmp == 255) {
                                return i5;
                            }
                            ltmp = (ltmp << c) | ((long) ctmp);
                            m++;
                            c = 6;
                        }
                        long ltmp2 = ltmp << c;
                        pOutput[outlen - 2] = (byte) ((int) ((ltmp2 >> 16) & 255));
                        pOutput[outlen - 1] = (byte) ((int) ((ltmp2 >> 8) & 255));
                    } else if (padnum == 2) {
                        long ltmp3 = 0;
                        int m2 = inputLen - 4;
                        while (m2 < inputLen - 2) {
                            char ctmp2 = decodingTable[pInput[m2]];
                            if (ctmp2 == 255) {
                                return i5;
                            }
                            ltmp3 = (ltmp3 << 6) | ((long) ctmp2);
                            m2++;
                            j2 = 255;
                        }
                        pOutput[outlen - 1] = (byte) ((int) (((ltmp3 << 12) >> 16) & j2));
                    }
                }
                return outlen;
            }
            long ltmp4 = 0;
            int m3 = j;
            while (m3 < j + 4) {
                char ctmp3 = decodingTable[pInput[m3]];
                if (ctmp3 == 255) {
                    return i5;
                }
                ltmp4 = (ltmp4 << 6) | ((long) ctmp3);
                m3++;
                outlen = outlen;
                i5 = 0;
            }
            pOutput[i6] = (byte) ((int) ((ltmp4 >> 16) & 255));
            pOutput[i6 + 1] = (byte) ((int) ((ltmp4 >> 8) & 255));
            pOutput[i6 + 2] = (byte) ((int) (ltmp4 & 255));
            i6 += 3;
            j += 4;
        }
    }
}
