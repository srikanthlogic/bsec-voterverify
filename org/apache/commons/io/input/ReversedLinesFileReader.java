package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
/* loaded from: classes3.dex */
public class ReversedLinesFileReader implements Closeable {
    private final int avoidNewlineSplitBufferSize;
    private final int blockSize;
    private final int byteDecrement;
    private FilePart currentFilePart;
    private final Charset encoding;
    private final byte[][] newLineSequences;
    private final RandomAccessFile randomAccessFile;
    private final long totalBlockCount;
    private final long totalByteLength;
    private boolean trailingNewlineOfFileSkipped;

    @Deprecated
    public ReversedLinesFileReader(File file) throws IOException {
        this(file, 4096, Charset.defaultCharset());
    }

    public ReversedLinesFileReader(File file, Charset charset) throws IOException {
        this(file, 4096, charset);
    }

    public ReversedLinesFileReader(File file, int blockSize, Charset encoding) throws IOException {
        this.trailingNewlineOfFileSkipped = false;
        this.blockSize = blockSize;
        this.encoding = encoding;
        Charset charset = Charsets.toCharset(encoding);
        if (charset.newEncoder().maxBytesPerChar() == 1.0f) {
            this.byteDecrement = 1;
        } else if (charset == StandardCharsets.UTF_8) {
            this.byteDecrement = 1;
        } else if (charset == Charset.forName("Shift_JIS") || charset == Charset.forName("windows-31j") || charset == Charset.forName("x-windows-949") || charset == Charset.forName("gbk") || charset == Charset.forName("x-windows-950")) {
            this.byteDecrement = 1;
        } else if (charset == StandardCharsets.UTF_16BE || charset == StandardCharsets.UTF_16LE) {
            this.byteDecrement = 2;
        } else if (charset == StandardCharsets.UTF_16) {
            throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
        } else {
            throw new UnsupportedEncodingException("Encoding " + encoding + " is not supported yet (feel free to submit a patch)");
        }
        this.newLineSequences = new byte[][]{"\r\n".getBytes(encoding), IOUtils.LINE_SEPARATOR_UNIX.getBytes(encoding), "\r".getBytes(encoding)};
        this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.totalByteLength = this.randomAccessFile.length();
        long j = this.totalByteLength;
        int lastBlockLength = (int) (j % ((long) blockSize));
        if (lastBlockLength > 0) {
            this.totalBlockCount = (j / ((long) blockSize)) + 1;
        } else {
            this.totalBlockCount = j / ((long) blockSize);
            if (j > 0) {
                lastBlockLength = blockSize;
            }
        }
        this.currentFilePart = new FilePart(this.totalBlockCount, lastBlockLength, null);
    }

    public ReversedLinesFileReader(File file, int blockSize, String encoding) throws IOException {
        this(file, blockSize, Charsets.toCharset(encoding));
    }

    public String readLine() throws IOException {
        String line = this.currentFilePart.readLine();
        while (line == null) {
            this.currentFilePart = this.currentFilePart.rollOver();
            FilePart filePart = this.currentFilePart;
            if (filePart == null) {
                break;
            }
            line = filePart.readLine();
        }
        if (!"".equals(line) || this.trailingNewlineOfFileSkipped) {
            return line;
        }
        this.trailingNewlineOfFileSkipped = true;
        return readLine();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.randomAccessFile.close();
    }

    /* loaded from: classes3.dex */
    private class FilePart {
        private int currentLastBytePos;
        private final byte[] data;
        private byte[] leftOver;
        private final long no;

        private FilePart(long no, int length, byte[] leftOverOfLastFilePart) throws IOException {
            this.no = no;
            this.data = new byte[(leftOverOfLastFilePart != null ? leftOverOfLastFilePart.length : 0) + length];
            long off = (no - 1) * ((long) ReversedLinesFileReader.this.blockSize);
            if (no > 0) {
                ReversedLinesFileReader.this.randomAccessFile.seek(off);
                if (ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, length) != length) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            if (leftOverOfLastFilePart != null) {
                System.arraycopy(leftOverOfLastFilePart, 0, this.data, length, leftOverOfLastFilePart.length);
            }
            this.currentLastBytePos = this.data.length - 1;
            this.leftOver = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FilePart rollOver() throws IOException {
            if (this.currentLastBytePos <= -1) {
                long j = this.no;
                if (j > 1) {
                    ReversedLinesFileReader reversedLinesFileReader = ReversedLinesFileReader.this;
                    return new FilePart(j - 1, reversedLinesFileReader.blockSize, this.leftOver);
                } else if (this.leftOver == null) {
                    return null;
                } else {
                    throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
                }
            } else {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String readLine() throws IOException {
            byte[] bArr;
            String line = null;
            boolean isLastFilePart = this.no == 1;
            int i = this.currentLastBytePos;
            while (true) {
                if (i > -1) {
                    if (!isLastFilePart && i < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
                        createLeftOver();
                        break;
                    }
                    int newLineMatchByteCount = getNewLineMatchByteCount(this.data, i);
                    if (newLineMatchByteCount <= 0) {
                        i -= ReversedLinesFileReader.this.byteDecrement;
                        if (i < 0) {
                            createLeftOver();
                            break;
                        }
                    } else {
                        int lineStart = i + 1;
                        int lineLengthBytes = (this.currentLastBytePos - lineStart) + 1;
                        if (lineLengthBytes >= 0) {
                            byte[] lineData = new byte[lineLengthBytes];
                            System.arraycopy(this.data, lineStart, lineData, 0, lineLengthBytes);
                            line = new String(lineData, ReversedLinesFileReader.this.encoding);
                            this.currentLastBytePos = i - newLineMatchByteCount;
                        } else {
                            throw new IllegalStateException("Unexpected negative line length=" + lineLengthBytes);
                        }
                    }
                } else {
                    break;
                }
            }
            if (!isLastFilePart || (bArr = this.leftOver) == null) {
                return line;
            }
            String line2 = new String(bArr, ReversedLinesFileReader.this.encoding);
            this.leftOver = null;
            return line2;
        }

        private void createLeftOver() {
            int lineLengthBytes = this.currentLastBytePos + 1;
            if (lineLengthBytes > 0) {
                this.leftOver = new byte[lineLengthBytes];
                System.arraycopy(this.data, 0, this.leftOver, 0, lineLengthBytes);
            } else {
                this.leftOver = null;
            }
            this.currentLastBytePos = -1;
        }

        private int getNewLineMatchByteCount(byte[] data, int i) {
            byte[][] bArr = ReversedLinesFileReader.this.newLineSequences;
            for (byte[] newLineSequence : bArr) {
                boolean match = true;
                for (int j = newLineSequence.length - 1; j >= 0; j--) {
                    int k = (i + j) - (newLineSequence.length - 1);
                    match &= k >= 0 && data[k] == newLineSequence[j];
                }
                if (match) {
                    return newLineSequence.length;
                }
            }
            return 0;
        }
    }
}
