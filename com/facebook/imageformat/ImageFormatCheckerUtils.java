package com.facebook.imageformat;

import com.facebook.common.internal.Preconditions;
import java.io.UnsupportedEncodingException;
/* loaded from: classes.dex */
public class ImageFormatCheckerUtils {
    public static byte[] asciiBytes(String value) {
        Preconditions.checkNotNull(value);
        try {
            return value.getBytes("ASCII");
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("ASCII not found!", uee);
        }
    }

    public static boolean startsWithPattern(byte[] byteArray, byte[] pattern) {
        Preconditions.checkNotNull(byteArray);
        Preconditions.checkNotNull(pattern);
        if (pattern.length > byteArray.length) {
            return false;
        }
        for (int i = 0; i < pattern.length; i++) {
            if (byteArray[i] != pattern[i]) {
                return false;
            }
        }
        return true;
    }

    public static int indexOfPattern(byte[] byteArray, int byteArrayLen, byte[] pattern, int patternLen) {
        Preconditions.checkNotNull(byteArray);
        Preconditions.checkNotNull(pattern);
        if (patternLen > byteArrayLen) {
            return -1;
        }
        byte first = pattern[0];
        int max = byteArrayLen - patternLen;
        int i = 0;
        while (i <= max) {
            if (byteArray[i] != first) {
                do {
                    i++;
                    if (i > max) {
                        break;
                    }
                } while (byteArray[i] != first);
            }
            if (i <= max) {
                int j = i + 1;
                int end = (j + patternLen) - 1;
                int k = 1;
                while (j < end && byteArray[j] == pattern[k]) {
                    j++;
                    k++;
                }
                if (j == end) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }

    private ImageFormatCheckerUtils() {
    }
}
