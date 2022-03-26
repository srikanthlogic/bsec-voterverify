package com.facebook.common.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class Files {
    private Files() {
    }

    static byte[] readFile(InputStream in, long expectedSize) throws IOException {
        if (expectedSize > 2147483647L) {
            throw new OutOfMemoryError("file is too large to fit in a byte array: " + expectedSize + " bytes");
        } else if (expectedSize == 0) {
            return ByteStreams.toByteArray(in);
        } else {
            return ByteStreams.toByteArray(in, (int) expectedSize);
        }
    }

    public static byte[] toByteArray(File file) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] readFile = readFile(in, in.getChannel().size());
            in.close();
            return readFile;
        } catch (Throwable th) {
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }
}
