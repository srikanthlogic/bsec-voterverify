package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;
/* loaded from: classes3.dex */
public class PrefixFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 8533897440809599867L;
    private final IOCase caseSensitivity;
    private final String[] prefixes;

    public PrefixFileFilter(String prefix) {
        this(prefix, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(String prefix, IOCase caseSensitivity) {
        if (prefix != null) {
            this.prefixes = new String[]{prefix};
            this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
            return;
        }
        throw new IllegalArgumentException("The prefix must not be null");
    }

    public PrefixFileFilter(String[] prefixes) {
        this(prefixes, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(String[] prefixes, IOCase caseSensitivity) {
        if (prefixes != null) {
            this.prefixes = new String[prefixes.length];
            System.arraycopy(prefixes, 0, this.prefixes, 0, prefixes.length);
            this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
            return;
        }
        throw new IllegalArgumentException("The array of prefixes must not be null");
    }

    public PrefixFileFilter(List<String> prefixes) {
        this(prefixes, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(List<String> prefixes, IOCase caseSensitivity) {
        if (prefixes != null) {
            this.prefixes = (String[]) prefixes.toArray(new String[prefixes.size()]);
            this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
            return;
        }
        throw new IllegalArgumentException("The list of prefixes must not be null");
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        String name = file.getName();
        for (String prefix : this.prefixes) {
            if (this.caseSensitivity.checkStartsWith(name, prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File file, String name) {
        for (String prefix : this.prefixes) {
            if (this.caseSensitivity.checkStartsWith(name, prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, java.lang.Object
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        if (this.prefixes != null) {
            for (int i = 0; i < this.prefixes.length; i++) {
                if (i > 0) {
                    buffer.append(",");
                }
                buffer.append(this.prefixes[i]);
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
