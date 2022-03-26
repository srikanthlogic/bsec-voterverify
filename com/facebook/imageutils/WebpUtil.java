package com.facebook.imageutils;

import android.util.Pair;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.google.common.base.Ascii;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class WebpUtil {
    private static final String VP8L_HEADER = "VP8L";
    private static final String VP8X_HEADER = "VP8X";
    private static final String VP8_HEADER = "VP8 ";

    private WebpUtil() {
    }

    @Nullable
    public static Pair<Integer, Integer> getSize(InputStream is) {
        byte[] headerBuffer;
        try {
            try {
                headerBuffer = new byte[4];
                try {
                    is.read(headerBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (is != null) {
                        is.close();
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (!compare(headerBuffer, "RIFF")) {
                return null;
            }
            getInt(is);
            is.read(headerBuffer);
            if (!compare(headerBuffer, "WEBP")) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                return null;
            }
            is.read(headerBuffer);
            String headerAsString = getHeader(headerBuffer);
            if (VP8_HEADER.equals(headerAsString)) {
                Pair<Integer, Integer> vP8Dimension = getVP8Dimension(is);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                return vP8Dimension;
            } else if (VP8L_HEADER.equals(headerAsString)) {
                Pair<Integer, Integer> vP8LDimension = getVP8LDimension(is);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                return vP8LDimension;
            } else if (VP8X_HEADER.equals(headerAsString)) {
                Pair<Integer, Integer> vP8XDimension = getVP8XDimension(is);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
                return vP8XDimension;
            } else {
                if (is != null) {
                    is.close();
                }
                return null;
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
        }
    }

    @Nullable
    private static Pair<Integer, Integer> getVP8Dimension(InputStream is) throws IOException {
        is.skip(7);
        short sign1 = getShort(is);
        short sign2 = getShort(is);
        short sign3 = getShort(is);
        if (sign1 == 157 && sign2 == 1 && sign3 == 42) {
            return new Pair<>(Integer.valueOf(get2BytesAsInt(is)), Integer.valueOf(get2BytesAsInt(is)));
        }
        return null;
    }

    @Nullable
    private static Pair<Integer, Integer> getVP8LDimension(InputStream is) throws IOException {
        getInt(is);
        if (getByte(is) != 47) {
            return null;
        }
        int data2 = ((byte) is.read()) & 255;
        return new Pair<>(Integer.valueOf((((data2 & 63) << 8) | (((byte) is.read()) & 255)) + 1), Integer.valueOf(((((((byte) is.read()) & 255) & 15) << 10) | ((((byte) is.read()) & 255) << 2) | ((data2 & 192) >> 6)) + 1));
    }

    private static Pair<Integer, Integer> getVP8XDimension(InputStream is) throws IOException {
        is.skip(8);
        return new Pair<>(Integer.valueOf(read3Bytes(is) + 1), Integer.valueOf(read3Bytes(is) + 1));
    }

    private static boolean compare(byte[] what, String with) {
        if (what.length != with.length()) {
            return false;
        }
        for (int i = 0; i < what.length; i++) {
            if (with.charAt(i) != what[i]) {
                return false;
            }
        }
        return true;
    }

    private static String getHeader(byte[] header) {
        StringBuilder str = new StringBuilder();
        for (byte b : header) {
            str.append((char) b);
        }
        return str.toString();
    }

    private static int getInt(InputStream is) throws IOException {
        return ((((byte) is.read()) << Ascii.CAN) & ViewCompat.MEASURED_STATE_MASK) | ((((byte) is.read()) << Ascii.DLE) & 16711680) | ((((byte) is.read()) << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (((byte) is.read()) & 255);
    }

    public static int get2BytesAsInt(InputStream is) throws IOException {
        return ((((byte) is.read()) << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (((byte) is.read()) & 255);
    }

    private static int read3Bytes(InputStream is) throws IOException {
        byte byte1 = getByte(is);
        return ((getByte(is) << Ascii.DLE) & 16711680) | ((getByte(is) << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (byte1 & 255);
    }

    private static short getShort(InputStream is) throws IOException {
        return (short) (is.read() & 255);
    }

    private static byte getByte(InputStream is) throws IOException {
        return (byte) (is.read() & 255);
    }

    private static boolean isBitOne(byte input, int bitIndex) {
        return ((input >> (bitIndex % 8)) & 1) == 1;
    }
}
