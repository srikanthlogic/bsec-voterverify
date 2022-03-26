package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
class MultiReader extends Reader {
    @NullableDecl
    private Reader current;
    private final Iterator<? extends CharSource> it;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiReader(Iterator<? extends CharSource> readers) throws IOException {
        this.it = readers;
        advance();
    }

    private void advance() throws IOException {
        close();
        if (this.it.hasNext()) {
            this.current = ((CharSource) this.it.next()).openStream();
        }
    }

    @Override // java.io.Reader
    public int read(@NullableDecl char[] cbuf, int off, int len) throws IOException {
        Reader reader = this.current;
        if (reader == null) {
            return -1;
        }
        int result = reader.read(cbuf, off, len);
        if (result != -1) {
            return result;
        }
        advance();
        return read(cbuf, off, len);
    }

    @Override // java.io.Reader
    public long skip(long n) throws IOException {
        Preconditions.checkArgument(n >= 0, "n is negative");
        if (n > 0) {
            while (true) {
                Reader reader = this.current;
                if (reader == null) {
                    break;
                }
                long result = reader.skip(n);
                if (result > 0) {
                    return result;
                }
                advance();
            }
        }
        return 0;
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        Reader reader = this.current;
        return reader != null && reader.ready();
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Reader reader = this.current;
        if (reader != null) {
            try {
                reader.close();
            } finally {
                this.current = null;
            }
        }
    }
}
