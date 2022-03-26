package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes3.dex */
public class AutoCloseInputStream extends ProxyInputStream {
    public AutoCloseInputStream(InputStream in) {
        super(in);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.Closeable, java.lang.AutoCloseable, java.io.InputStream
    public void close() throws IOException {
        this.in.close();
        this.in = new ClosedInputStream();
    }

    @Override // org.apache.commons.io.input.ProxyInputStream
    protected void afterRead(int n) throws IOException {
        if (n == -1) {
            close();
        }
    }

    @Override // java.lang.Object
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
