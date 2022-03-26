package com.shockwave.pdfium.util;
/* loaded from: classes3.dex */
public class Size {
    private final int height;
    private final int width;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Size)) {
            return false;
        }
        Size other = (Size) obj;
        if (this.width == other.width && this.height == other.height) {
            return true;
        }
        return false;
    }

    public String toString() {
        return this.width + "x" + this.height;
    }

    public int hashCode() {
        int i = this.height;
        int i2 = this.width;
        return i ^ ((i2 >>> 16) | (i2 << 16));
    }
}
