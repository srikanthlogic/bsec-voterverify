package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
/* loaded from: classes3.dex */
public class MagicNumberFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = -547733176983104172L;
    private final long byteOffset;
    private final byte[] magicNumbers;

    public MagicNumberFileFilter(byte[] magicNumber) {
        this(magicNumber, 0);
    }

    public MagicNumberFileFilter(String magicNumber) {
        this(magicNumber, 0);
    }

    public MagicNumberFileFilter(String magicNumber, long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        } else if (magicNumber.isEmpty()) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        } else if (offset >= 0) {
            this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset());
            this.byteOffset = offset;
        } else {
            throw new IllegalArgumentException("The offset cannot be negative");
        }
    }

    public MagicNumberFileFilter(byte[] magicNumber, long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        } else if (magicNumber.length == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        } else if (offset >= 0) {
            this.magicNumbers = new byte[magicNumber.length];
            System.arraycopy(magicNumber, 0, this.magicNumbers, 0, magicNumber.length);
            this.byteOffset = offset;
        } else {
            throw new IllegalArgumentException("The offset cannot be negative");
        }
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        if (file != null && file.isFile() && file.canRead()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                byte[] fileBytes = new byte[this.magicNumbers.length];
                randomAccessFile.seek(this.byteOffset);
                if (randomAccessFile.read(fileBytes) != this.magicNumbers.length) {
                    randomAccessFile.close();
                    return false;
                }
                boolean equals = Arrays.equals(this.magicNumbers, fileBytes);
                randomAccessFile.close();
                return equals;
            } catch (IOException e) {
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, java.lang.Object
    public String toString() {
        return super.toString() + "(" + new String(this.magicNumbers, Charset.defaultCharset()) + "," + this.byteOffset + ")";
    }
}
