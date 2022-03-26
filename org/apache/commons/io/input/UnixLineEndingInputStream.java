package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes3.dex */
public class UnixLineEndingInputStream extends InputStream {
    private final boolean ensureLineFeedAtEndOfFile;
    private final InputStream target;
    private boolean slashNSeen = false;
    private boolean slashRSeen = false;
    private boolean eofSeen = false;

    public UnixLineEndingInputStream(InputStream in, boolean ensureLineFeedAtEndOfFile) {
        this.target = in;
        this.ensureLineFeedAtEndOfFile = ensureLineFeedAtEndOfFile;
    }

    private int readWithUpdate() throws IOException {
        int target = this.target.read();
        boolean z = true;
        this.eofSeen = target == -1;
        if (this.eofSeen) {
            return target;
        }
        this.slashNSeen = target == 10;
        if (target != 13) {
            z = false;
        }
        this.slashRSeen = z;
        return target;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        boolean previousWasSlashR = this.slashRSeen;
        if (this.eofSeen) {
            return eofGame(previousWasSlashR);
        }
        int target = readWithUpdate();
        if (this.eofSeen) {
            return eofGame(previousWasSlashR);
        }
        if (this.slashRSeen) {
            return 10;
        }
        if (!previousWasSlashR || !this.slashNSeen) {
            return target;
        }
        return read();
    }

    private int eofGame(boolean previousWasSlashR) {
        if (previousWasSlashR || !this.ensureLineFeedAtEndOfFile || this.slashNSeen) {
            return -1;
        }
        this.slashNSeen = true;
        return 10;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.target.close();
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        throw new UnsupportedOperationException("Mark notsupported");
    }
}
