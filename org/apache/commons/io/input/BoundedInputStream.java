package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes3.dex */
public class BoundedInputStream extends InputStream {
    private final InputStream in;
    private long mark;
    private final long max;
    private long pos;
    private boolean propagateClose;

    public BoundedInputStream(InputStream in, long size) {
        this.pos = 0;
        this.mark = -1;
        this.propagateClose = true;
        this.max = size;
        this.in = in;
    }

    public BoundedInputStream(InputStream in) {
        this(in, -1);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        long j = this.max;
        if (j >= 0 && this.pos >= j) {
            return -1;
        }
        int result = this.in.read();
        this.pos++;
        return result;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        long j = this.max;
        if (j >= 0 && this.pos >= j) {
            return -1;
        }
        long j2 = this.max;
        int bytesRead = this.in.read(b, off, (int) (j2 >= 0 ? Math.min((long) len, j2 - this.pos) : (long) len));
        if (bytesRead == -1) {
            return -1;
        }
        this.pos += (long) bytesRead;
        return bytesRead;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        long j = this.max;
        long skippedBytes = this.in.skip(j >= 0 ? Math.min(n, j - this.pos) : n);
        this.pos += skippedBytes;
        return skippedBytes;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        long j = this.max;
        if (j < 0 || this.pos < j) {
            return this.in.available();
        }
        return 0;
    }

    @Override // java.lang.Object
    public String toString() {
        return this.in.toString();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.propagateClose) {
            this.in.close();
        }
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        this.in.reset();
        this.pos = this.mark;
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.in.mark(readlimit);
        this.mark = this.pos;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.in.markSupported();
    }

    public boolean isPropagateClose() {
        return this.propagateClose;
    }

    public void setPropagateClose(boolean propagateClose) {
        this.propagateClose = propagateClose;
    }
}
