package com.facebook.common.util;
/* loaded from: classes.dex */
public class Hex {
    private static final byte[] DIGITS;
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] FIRST_CHAR = new char[256];
    private static final char[] SECOND_CHAR = new char[256];

    static {
        for (int i = 0; i < 256; i++) {
            char[] cArr = FIRST_CHAR;
            char[] cArr2 = HEX_DIGITS;
            cArr[i] = cArr2[(i >> 4) & 15];
            SECOND_CHAR[i] = cArr2[i & 15];
        }
        DIGITS = new byte[103];
        for (int i2 = 0; i2 <= 70; i2++) {
            DIGITS[i2] = -1;
        }
        for (byte i3 = 0; i3 < 10; i3 = (byte) (i3 + 1)) {
            DIGITS[i3 + 48] = i3;
        }
        for (byte i4 = 0; i4 < 6; i4 = (byte) (i4 + 1)) {
            byte[] bArr = DIGITS;
            bArr[i4 + 65] = (byte) (i4 + 10);
            bArr[i4 + 97] = (byte) (i4 + 10);
        }
    }

    public static String byte2Hex(int value) {
        if (value > 255 || value < 0) {
            throw new IllegalArgumentException("The int converting to hex should be in range 0~255");
        }
        return String.valueOf(FIRST_CHAR[value]) + String.valueOf(SECOND_CHAR[value]);
    }

    public static String encodeHex(byte[] array, boolean zeroTerminated) {
        int index;
        char[] cArray = new char[array.length * 2];
        int j = 0;
        int i = 0;
        while (i < array.length && ((index = array[i] & 255) != 0 || !zeroTerminated)) {
            int j2 = j + 1;
            cArray[j] = FIRST_CHAR[index];
            j = j2 + 1;
            cArray[j2] = SECOND_CHAR[index];
            i++;
        }
        return new String(cArray, 0, j);
    }

    public static byte[] decodeHex(String hexString) {
        int length = hexString.length();
        if ((length & 1) == 0) {
            boolean badHex = false;
            byte[] out = new byte[length >> 1];
            int i = 0;
            int j = 0;
            while (true) {
                if (j >= length) {
                    break;
                }
                int j2 = j + 1;
                int c1 = hexString.charAt(j);
                if (c1 > 102) {
                    badHex = true;
                    break;
                }
                byte d1 = DIGITS[c1];
                if (d1 == -1) {
                    badHex = true;
                    break;
                }
                int j3 = j2 + 1;
                int c2 = hexString.charAt(j2);
                if (c2 > 102) {
                    badHex = true;
                    break;
                }
                byte d2 = DIGITS[c2];
                if (d2 == -1) {
                    badHex = true;
                    break;
                }
                out[i] = (byte) ((d1 << 4) | d2);
                i++;
                j = j3;
            }
            if (!badHex) {
                return out;
            }
            throw new IllegalArgumentException("Invalid hexadecimal digit: " + hexString);
        }
        throw new IllegalArgumentException("Odd number of characters.");
    }

    public static byte[] hexStringToByteArray(String s) {
        return decodeHex(s.replaceAll(" ", ""));
    }
}
