package com.facebook.common.memory;

import com.facebook.common.internal.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes.dex */
public class PooledByteStreams {
    private static final int DEFAULT_TEMP_BUF_SIZE = 16384;
    private final ByteArrayPool mByteArrayPool;
    private final int mTempBufSize;

    public PooledByteStreams(ByteArrayPool byteArrayPool) {
        this(byteArrayPool, 16384);
    }

    public PooledByteStreams(ByteArrayPool byteArrayPool, int tempBufSize) {
        Preconditions.checkArgument(tempBufSize > 0);
        this.mTempBufSize = tempBufSize;
        this.mByteArrayPool = byteArrayPool;
    }

    public long copy(InputStream from, OutputStream to) throws IOException {
        long count = 0;
        byte[] tmp = this.mByteArrayPool.get(this.mTempBufSize);
        while (true) {
            try {
                int read = from.read(tmp, 0, this.mTempBufSize);
                if (read == -1) {
                    return count;
                }
                to.write(tmp, 0, read);
                count += (long) read;
            } finally {
                this.mByteArrayPool.release(tmp);
            }
        }
    }

    public long copy(InputStream from, OutputStream to, long bytesToCopy) throws IOException {
        Preconditions.checkState(bytesToCopy > 0);
        long copied = 0;
        byte[] tmp = this.mByteArrayPool.get(this.mTempBufSize);
        while (copied < bytesToCopy) {
            try {
                int read = from.read(tmp, 0, (int) Math.min((long) this.mTempBufSize, bytesToCopy - copied));
                if (read == -1) {
                    return copied;
                }
                to.write(tmp, 0, read);
                copied += (long) read;
            } finally {
                this.mByteArrayPool.release(tmp);
            }
        }
        return copied;
    }
}
