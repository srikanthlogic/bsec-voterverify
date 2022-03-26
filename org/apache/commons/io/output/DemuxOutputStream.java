package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: classes3.dex */
public class DemuxOutputStream extends OutputStream {
    private final InheritableThreadLocal<OutputStream> outputStreamThreadLocal = new InheritableThreadLocal<>();

    public OutputStream bindStream(OutputStream output) {
        OutputStream stream = this.outputStreamThreadLocal.get();
        this.outputStreamThreadLocal.set(output);
        return stream;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        OutputStream output = this.outputStreamThreadLocal.get();
        if (output != null) {
            output.close();
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        OutputStream output = this.outputStreamThreadLocal.get();
        if (output != null) {
            output.flush();
        }
    }

    @Override // java.io.OutputStream
    public void write(int ch) throws IOException {
        OutputStream output = this.outputStreamThreadLocal.get();
        if (output != null) {
            output.write(ch);
        }
    }
}
