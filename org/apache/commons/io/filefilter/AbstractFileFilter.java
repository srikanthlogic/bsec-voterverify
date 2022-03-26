package org.apache.commons.io.filefilter;

import java.io.File;
/* loaded from: classes3.dex */
public abstract class AbstractFileFilter implements IOFileFilter {
    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return accept(file.getParentFile(), file.getName());
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }

    @Override // java.lang.Object
    public String toString() {
        return getClass().getSimpleName();
    }
}
