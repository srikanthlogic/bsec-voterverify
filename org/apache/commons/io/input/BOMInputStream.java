package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.ByteOrderMark;
/* loaded from: classes3.dex */
public class BOMInputStream extends ProxyInputStream {
    private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = new Comparator<ByteOrderMark>() { // from class: org.apache.commons.io.input.BOMInputStream.1
        public int compare(ByteOrderMark bom1, ByteOrderMark bom2) {
            int len1 = bom1.length();
            int len2 = bom2.length();
            if (len1 > len2) {
                return -1;
            }
            if (len2 > len1) {
                return 1;
            }
            return 0;
        }
    };
    private final List<ByteOrderMark> boms;
    private ByteOrderMark byteOrderMark;
    private int fbIndex;
    private int fbLength;
    private int[] firstBytes;
    private final boolean include;
    private int markFbIndex;
    private boolean markedAtStart;

    public BOMInputStream(InputStream delegate) {
        this(delegate, false, ByteOrderMark.UTF_8);
    }

    public BOMInputStream(InputStream delegate, boolean include) {
        this(delegate, include, ByteOrderMark.UTF_8);
    }

    public BOMInputStream(InputStream delegate, ByteOrderMark... boms) {
        this(delegate, false, boms);
    }

    public BOMInputStream(InputStream delegate, boolean include, ByteOrderMark... boms) {
        super(delegate);
        if (boms == null || boms.length == 0) {
            throw new IllegalArgumentException("No BOMs specified");
        }
        this.include = include;
        List<ByteOrderMark> list = Arrays.asList(boms);
        Collections.sort(list, ByteOrderMarkLengthComparator);
        this.boms = list;
    }

    public boolean hasBOM() throws IOException {
        return getBOM() != null;
    }

    public boolean hasBOM(ByteOrderMark bom) throws IOException {
        if (this.boms.contains(bom)) {
            getBOM();
            ByteOrderMark byteOrderMark = this.byteOrderMark;
            return byteOrderMark != null && byteOrderMark.equals(bom);
        }
        throw new IllegalArgumentException("Stream not configure to detect " + bom);
    }

    public ByteOrderMark getBOM() throws IOException {
        if (this.firstBytes == null) {
            this.fbLength = 0;
            this.firstBytes = new int[this.boms.get(0).length()];
            int i = 0;
            while (true) {
                int[] iArr = this.firstBytes;
                if (i >= iArr.length) {
                    break;
                }
                iArr[i] = this.in.read();
                this.fbLength++;
                if (this.firstBytes[i] < 0) {
                    break;
                }
                i++;
            }
            this.byteOrderMark = find();
            ByteOrderMark byteOrderMark = this.byteOrderMark;
            if (byteOrderMark != null && !this.include) {
                if (byteOrderMark.length() < this.firstBytes.length) {
                    this.fbIndex = this.byteOrderMark.length();
                } else {
                    this.fbLength = 0;
                }
            }
        }
        return this.byteOrderMark;
    }

    public String getBOMCharsetName() throws IOException {
        getBOM();
        ByteOrderMark byteOrderMark = this.byteOrderMark;
        if (byteOrderMark == null) {
            return null;
        }
        return byteOrderMark.getCharsetName();
    }

    private int readFirstBytes() throws IOException {
        getBOM();
        int i = this.fbIndex;
        if (i >= this.fbLength) {
            return -1;
        }
        int[] iArr = this.firstBytes;
        this.fbIndex = i + 1;
        return iArr[i];
    }

    private ByteOrderMark find() {
        for (ByteOrderMark bom : this.boms) {
            if (matches(bom)) {
                return bom;
            }
        }
        return null;
    }

    private boolean matches(ByteOrderMark bom) {
        for (int i = 0; i < bom.length(); i++) {
            if (bom.get(i) != this.firstBytes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int b = readFirstBytes();
        return b >= 0 ? b : this.in.read();
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buf, int off, int len) throws IOException {
        int firstCount = 0;
        int b = 0;
        while (len > 0 && b >= 0) {
            b = readFirstBytes();
            if (b >= 0) {
                buf[off] = (byte) (b & 255);
                len--;
                firstCount++;
                off++;
            }
        }
        int secondCount = this.in.read(buf, off, len);
        if (secondCount >= 0) {
            return firstCount + secondCount;
        }
        if (firstCount > 0) {
            return firstCount;
        }
        return -1;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buf) throws IOException {
        return read(buf, 0, buf.length);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.markFbIndex = this.fbIndex;
        this.markedAtStart = this.firstBytes == null;
        this.in.mark(readlimit);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        this.fbIndex = this.markFbIndex;
        if (this.markedAtStart) {
            this.firstBytes = null;
        }
        this.in.reset();
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public long skip(long n) throws IOException {
        int skipped = 0;
        while (n > ((long) skipped) && readFirstBytes() >= 0) {
            skipped++;
        }
        return this.in.skip(n - ((long) skipped)) + ((long) skipped);
    }
}
