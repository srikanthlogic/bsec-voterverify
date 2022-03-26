package org.zz.tool;

import com.google.common.base.Ascii;
import java.util.Vector;
/* loaded from: classes3.dex */
public class StringUnit {
    public static Vector<Byte> str2vectorbcd(String str) {
        byte[] a2 = str.getBytes();
        Vector<Byte> v = new Vector<>();
        v.clear();
        for (int i = 0; i < str.length() / 2; i++) {
            v.add(Byte.valueOf((byte) (((a2[i * 2] - 48) << 4) | (a2[(i * 2) + 1] - 48))));
        }
        return v;
    }

    public static byte[] str2bcd(String str) {
        byte[] a2 = str.getBytes();
        Vector<Byte> v = new Vector<>();
        v.clear();
        for (int i = 0; i < str.length() / 2; i++) {
            v.add(Byte.valueOf((byte) (((a2[i * 2] - 48) << 4) | (a2[(i * 2) + 1] - 48))));
        }
        byte[] tmpbt = new byte[v.size()];
        for (int j = 0; j < v.size(); j++) {
            tmpbt[j] = v.get(j).byteValue();
        }
        return tmpbt;
    }

    public static Vector<Byte> datetime2vectorbcd(String time) {
        String[] a2 = time.split(" ");
        String[] a3 = (String.valueOf(a2[0]) + a2[1]).split("[:-]");
        StringBuilder b = new StringBuilder();
        for (String s : a3) {
            b.append(s);
        }
        return str2vectorbcd(b.toString().substring(2));
    }

    public static byte[] datetime2bcd(String time) {
        String[] a2 = time.split(" ");
        String[] a3 = (String.valueOf(a2[0]) + a2[1]).split("[:-]");
        StringBuilder b = new StringBuilder();
        for (String s : a3) {
            b.append(s);
        }
        return str2bcd(b.toString().substring(2));
    }

    public static byte[] time2bcd(String time) {
        return str2bcd(time);
    }

    public static String bcd2time(byte[] time) {
        return String.format("%02d:%02d:%02d", Integer.valueOf(((time[0] >> 4) * 10) + (time[0] & Ascii.SI)), Integer.valueOf(((time[1] >> 4) * 10) + (time[1] & Ascii.SI)), Integer.valueOf(((time[2] >> 4) * 10) + (time[2] & Ascii.SI)));
    }

    public static String bcd2date(byte[] date) {
        return String.format("%02d-%02d-%02d", Integer.valueOf(((date[0] >> 4) * 10) + 2000 + (date[0] & Ascii.SI)), Integer.valueOf(((date[1] >> 4) * 10) + (date[1] & Ascii.SI)), Integer.valueOf(((date[2] >> 4) * 10) + (date[2] & Ascii.SI)));
    }

    public static String bcd2datetime(byte[] datetime) {
        return String.format("%02d-%02d-%02d %02d:%02d:%02d", Integer.valueOf(((datetime[0] >> 4) * 10) + 2000 + (datetime[0] & Ascii.SI)), Integer.valueOf(((datetime[1] >> 4) * 10) + (datetime[1] & Ascii.SI)), Integer.valueOf(((datetime[2] >> 4) * 10) + (datetime[2] & Ascii.SI)), Integer.valueOf(((datetime[3] >> 4) * 10) + (datetime[3] & Ascii.SI)), Integer.valueOf(((datetime[4] >> 4) * 10) + (datetime[4] & Ascii.SI)), Integer.valueOf(((datetime[5] >> 4) * 10) + (datetime[5] & Ascii.SI)));
    }

    public static String bcd2str(byte[] bcd) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bcd) {
            sb.append(String.format("%d%d", Integer.valueOf(b >> 4), Integer.valueOf(b & Ascii.SI)));
        }
        return sb.toString();
    }

    public static String hex2str(byte[] hex) {
        StringBuilder sb = new StringBuilder();
        int length = hex.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x ", Byte.valueOf(hex[i])));
        }
        return sb.toString();
    }

    public static byte[] str2hex(String str) {
        byte b1;
        int i;
        byte[] a2 = str.getBytes();
        Vector<Byte> v = new Vector<>();
        v.clear();
        for (int i2 = 0; i2 < str.length() / 2; i2++) {
            if (48 <= a2[i2 * 2] && a2[i2 * 2] <= 57) {
                b1 = (byte) (a2[i2 * 2] - 48);
            } else if (65 > a2[i2 * 2] || a2[i2 * 2] > 70) {
                b1 = (byte) ((a2[i2 * 2] - 97) + 10);
            } else {
                b1 = (byte) ((a2[i2 * 2] - 65) + 10);
            }
            if (48 <= a2[(i2 * 2) + 1] && a2[(i2 * 2) + 1] <= 57) {
                i = a2[(i2 * 2) + 1] - 48;
            } else if (65 > a2[(i2 * 2) + 1] || a2[(i2 * 2) + 1] > 70) {
                i = (a2[(i2 * 2) + 1] - 97) + 10;
            } else {
                i = (a2[(i2 * 2) + 1] - 65) + 10;
            }
            v.add(Byte.valueOf((byte) ((b1 << 4) | ((byte) i))));
        }
        byte[] tmpbt = new byte[v.size()];
        for (int j = 0; j < v.size(); j++) {
            tmpbt[j] = v.get(j).byteValue();
        }
        return tmpbt;
    }
}
