package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
/* loaded from: classes3.dex */
class ReverseComparator extends AbstractFileComparator implements Serializable {
    private static final long serialVersionUID = -4808255005272229056L;
    private final Comparator<File> delegate;

    public ReverseComparator(Comparator<File> delegate) {
        if (delegate != null) {
            this.delegate = delegate;
            return;
        }
        throw new IllegalArgumentException("Delegate comparator is missing");
    }

    public int compare(File file1, File file2) {
        return this.delegate.compare(file2, file1);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator, java.lang.Object
    public String toString() {
        return super.toString() + "[" + this.delegate.toString() + "]";
    }
}
