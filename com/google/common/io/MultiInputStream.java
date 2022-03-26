package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
final class MultiInputStream extends InputStream {
    @NullableDecl
    private InputStream in;
    private Iterator<? extends ByteSource> it;

    public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
        this.it = (Iterator) Preconditions.checkNotNull(it);
        advance();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        InputStream inputStream = this.in;
        if (inputStream != null) {
            try {
                inputStream.close();
            } finally {
                this.in = null;
            }
        }
    }

    private void advance() throws IOException {
        close();
        if (this.it.hasNext()) {
            this.in = ((ByteSource) this.it.next()).openStream();
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        InputStream inputStream = this.in;
        if (inputStream == null) {
            return 0;
        }
        return inputStream.available();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        while (true) {
            InputStream inputStream = this.in;
            if (inputStream == null) {
                return -1;
            }
            int result = inputStream.read();
            if (result != -1) {
                return result;
            }
            advance();
        }
    }

    @Override // java.io.InputStream
    public int read(@NullableDecl byte[] b, int off, int len) throws IOException {
        while (true) {
            InputStream inputStream = this.in;
            if (inputStream == null) {
                return -1;
            }
            int result = inputStream.read(b, off, len);
            if (result != -1) {
                return result;
            }
            advance();
        }
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        InputStream inputStream = this.in;
        if (inputStream == null || n <= 0) {
            return 0;
        }
        long result = inputStream.skip(n);
        if (result != 0) {
            return result;
        }
        if (read() == -1) {
            return 0;
        }
        return this.in.skip(n - 1) + 1;
    }
}
