package org.apache.commons.io.input;

import java.io.InputStream;
/* loaded from: classes3.dex */
public class CloseShieldInputStream extends ProxyInputStream {
    public CloseShieldInputStream(InputStream in) {
        super(in);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.Closeable, java.lang.AutoCloseable, java.io.InputStream
    public void close() {
        this.in = new ClosedInputStream();
    }
}
