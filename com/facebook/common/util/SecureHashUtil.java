package com.facebook.common.util;

import android.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
/* loaded from: classes.dex */
public class SecureHashUtil {
    private static final int BUFFER_SIZE = 4096;
    static final byte[] HEX_CHAR_TABLE = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    public static String makeSHA1Hash(String text) {
        try {
            return makeSHA1Hash(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeSHA1Hash(byte[] bytes) {
        return makeHash(bytes, MessageDigestAlgorithms.SHA_1);
    }

    public static String makeSHA256Hash(byte[] bytes) {
        return makeHash(bytes, MessageDigestAlgorithms.SHA_256);
    }

    public static String makeSHA1HashBase64(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_1);
            md.update(bytes, 0, bytes.length);
            return Base64.encodeToString(md.digest(), 11);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeMD5Hash(String text) {
        try {
            return makeMD5Hash(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeMD5Hash(byte[] bytes) {
        return makeHash(bytes, MessageDigestAlgorithms.MD5);
    }

    public static String makeMD5Hash(InputStream stream) throws IOException {
        return makeHash(stream, MessageDigestAlgorithms.MD5);
    }

    public static String convertToHex(byte[] raw) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(raw.length);
        for (byte b : raw) {
            int v = b & 255;
            sb.append((char) HEX_CHAR_TABLE[v >>> 4]);
            sb.append((char) HEX_CHAR_TABLE[v & 15]);
        }
        return sb.toString();
    }

    private static String makeHash(byte[] bytes, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(bytes, 0, bytes.length);
            return convertToHex(md.digest());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        }
    }

    private static String makeHash(InputStream stream, String algorithm) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[4096];
            while (true) {
                int read = stream.read(buffer);
                if (read <= 0) {
                    return convertToHex(md.digest());
                }
                md.update(buffer, 0, read);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        }
    }
}
