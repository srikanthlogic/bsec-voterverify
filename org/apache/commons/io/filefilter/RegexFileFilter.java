package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.regex.Pattern;
import org.apache.commons.io.IOCase;
/* loaded from: classes3.dex */
public class RegexFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 4269646126155225062L;
    private final Pattern pattern;

    public RegexFileFilter(String pattern) {
        if (pattern != null) {
            this.pattern = Pattern.compile(pattern);
            return;
        }
        throw new IllegalArgumentException("Pattern is missing");
    }

    public RegexFileFilter(String pattern, IOCase caseSensitivity) {
        if (pattern != null) {
            int flags = 0;
            if (caseSensitivity != null && !caseSensitivity.isCaseSensitive()) {
                flags = 2;
            }
            this.pattern = Pattern.compile(pattern, flags);
            return;
        }
        throw new IllegalArgumentException("Pattern is missing");
    }

    public RegexFileFilter(String pattern, int flags) {
        if (pattern != null) {
            this.pattern = Pattern.compile(pattern, flags);
            return;
        }
        throw new IllegalArgumentException("Pattern is missing");
    }

    public RegexFileFilter(Pattern pattern) {
        if (pattern != null) {
            this.pattern = pattern;
            return;
        }
        throw new IllegalArgumentException("Pattern is missing");
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return this.pattern.matcher(name).matches();
    }
}
