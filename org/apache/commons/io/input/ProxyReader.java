package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
/* loaded from: classes3.dex */
public abstract class ProxyReader extends FilterReader {
    public ProxyReader(Reader proxy) {
        super(proxy);
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int i = 1;
        try {
            beforeRead(1);
            int c = this.in.read();
            if (c == -1) {
                i = -1;
            }
            afterRead(i);
            return c;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    @Override // java.io.Reader
    public int read(char[] chr) throws IOException {
        int length;
        if (chr != null) {
            try {
                length = chr.length;
            } catch (IOException e) {
                handleIOException(e);
                return -1;
            }
        } else {
            length = 0;
        }
        beforeRead(length);
        int n = this.in.read(chr);
        afterRead(n);
        return n;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read(char[] chr, int st, int len) throws IOException {
        try {
            beforeRead(len);
            int n = this.in.read(chr, st, len);
            afterRead(n);
            return n;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    @Override // java.lang.Readable, java.io.Reader
    public int read(CharBuffer target) throws IOException {
        int length;
        if (target != null) {
            try {
                length = target.length();
            } catch (IOException e) {
                handleIOException(e);
                return -1;
            }
        } else {
            length = 0;
        }
        beforeRead(length);
        int n = this.in.read(target);
        afterRead(n);
        return n;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public long skip(long ln) throws IOException {
        try {
            return this.in.skip(ln);
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    @Override // java.io.FilterReader, java.io.Reader
    public boolean ready() throws IOException {
        try {
            return this.in.ready();
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override // java.io.FilterReader, java.io.Closeable, java.io.Reader, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            this.in.close();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    @Override // java.io.FilterReader, java.io.Reader
    public synchronized void mark(int idx) throws IOException {
        try {
            this.in.mark(idx);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    @Override // java.io.FilterReader, java.io.Reader
    public synchronized void reset() throws IOException {
        try {
            this.in.reset();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    @Override // java.io.FilterReader, java.io.Reader
    public boolean markSupported() {
        return this.in.markSupported();
    }

    protected void beforeRead(int n) throws IOException {
    }

    protected void afterRead(int n) throws IOException {
    }

    protected void handleIOException(IOException e) throws IOException {
        throw e;
    }
}
