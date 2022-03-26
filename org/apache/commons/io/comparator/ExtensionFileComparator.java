package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
/* loaded from: classes3.dex */
public class ExtensionFileComparator extends AbstractFileComparator implements Serializable {
    private static final long serialVersionUID = 1928235200184222815L;
    private final IOCase caseSensitivity;
    public static final Comparator<File> EXTENSION_COMPARATOR = new ExtensionFileComparator();
    public static final Comparator<File> EXTENSION_REVERSE = new ReverseComparator(EXTENSION_COMPARATOR);
    public static final Comparator<File> EXTENSION_INSENSITIVE_COMPARATOR = new ExtensionFileComparator(IOCase.INSENSITIVE);
    public static final Comparator<File> EXTENSION_INSENSITIVE_REVERSE = new ReverseComparator(EXTENSION_INSENSITIVE_COMPARATOR);
    public static final Comparator<File> EXTENSION_SYSTEM_COMPARATOR = new ExtensionFileComparator(IOCase.SYSTEM);
    public static final Comparator<File> EXTENSION_SYSTEM_REVERSE = new ReverseComparator(EXTENSION_SYSTEM_COMPARATOR);

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ List sort(List list) {
        return super.sort(list);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ File[] sort(File[] fileArr) {
        return super.sort(fileArr);
    }

    public ExtensionFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    public ExtensionFileComparator(IOCase caseSensitivity) {
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    public int compare(File file1, File file2) {
        return this.caseSensitivity.checkCompareTo(FilenameUtils.getExtension(file1.getName()), FilenameUtils.getExtension(file2.getName()));
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator, java.lang.Object
    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
}
