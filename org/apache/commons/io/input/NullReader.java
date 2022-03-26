package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
/* loaded from: classes3.dex */
public class NullReader extends Reader {
    private boolean eof;
    private long mark;
    private final boolean markSupported;
    private long position;
    private long readlimit;
    private final long size;
    private final boolean throwEofException;

    public NullReader(long size) {
        this(size, true, false);
    }

    public NullReader(long size, boolean markSupported, boolean throwEofException) {
        this.mark = -1;
        this.size = size;
        this.markSupported = markSupported;
        this.throwEofException = throwEofException;
    }

    public long getPosition() {
        return this.position;
    }

    public long getSize() {
        return this.size;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.eof = false;
        this.position = 0;
        this.mark = -1;
    }

    @Override // java.io.Reader
    public synchronized void mark(int readlimit) {
        if (this.markSupported) {
            this.mark = this.position;
            this.readlimit = (long) readlimit;
        } else {
            throw new UnsupportedOperationException("Mark not supported");
        }
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return this.markSupported;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        if (!this.eof) {
            long j = this.position;
            if (j == this.size) {
                return doEndOfFile();
            }
            this.position = j + 1;
            return processChar();
        }
        throw new IOException("Read after end of file");
    }

    @Override // java.io.Reader
    public int read(char[] chars) throws IOException {
        return read(chars, 0, chars.length);
    }

    @Override // java.io.Reader
    public int read(char[] chars, int offset, int length) throws IOException {
        if (!this.eof) {
            long j = this.position;
            long j2 = this.size;
            if (j == j2) {
                return doEndOfFile();
            }
            this.position = j + ((long) length);
            int returnLength = length;
            long j3 = this.position;
            if (j3 > j2) {
                returnLength = length - ((int) (j3 - j2));
                this.position = j2;
            }
            processChars(chars, offset, returnLength);
            return returnLength;
        }
        throw new IOException("Read after end of file");
    }

    @Override // java.io.Reader
    public synchronized void reset() throws IOException {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        } else if (this.mark < 0) {
            throw new IOException("No position has been marked");
        } else if (this.position <= this.mark + this.readlimit) {
            this.position = this.mark;
            this.eof = false;
        } else {
            throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
        }
    }

    @Override // java.io.Reader
    public long skip(long numberOfChars) throws IOException {
        if (!this.eof) {
            long j = this.position;
            long j2 = this.size;
            if (j == j2) {
                return (long) doEndOfFile();
            }
            this.position = j + numberOfChars;
            long j3 = this.position;
            if (j3 <= j2) {
                return numberOfChars;
            }
            long returnLength = numberOfChars - (j3 - j2);
            this.position = j2;
            return returnLength;
        }
        throw new IOException("Skip after end of file");
    }

    protected int processChar() {
        return 0;
    }

    protected void processChars(char[] chars, int offset, int length) {
    }

    private int doEndOfFile() throws EOFException {
        this.eof = true;
        if (!this.throwEofException) {
            return -1;
        }
        throw new EOFException();
    }
}
