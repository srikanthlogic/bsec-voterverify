package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
/* loaded from: classes3.dex */
public class SizeFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 7388077430788600069L;
    private final boolean acceptLarger;
    private final long size;

    public SizeFileFilter(long size) {
        this(size, true);
    }

    public SizeFileFilter(long size, boolean acceptLarger) {
        if (size >= 0) {
            this.size = size;
            this.acceptLarger = acceptLarger;
            return;
        }
        throw new IllegalArgumentException("The size must be non-negative");
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        boolean smaller = file.length() < this.size;
        if (this.acceptLarger) {
            return !smaller;
        }
        return smaller;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, java.lang.Object
    public String toString() {
        String condition = this.acceptLarger ? ">=" : "<";
        return super.toString() + "(" + condition + this.size + ")";
    }
}
