package org.apache.commons.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
/* loaded from: classes3.dex */
public abstract class DirectoryWalker<T> {
    private final int depthLimit;
    private final FileFilter filter;

    protected DirectoryWalker() {
        this(null, -1);
    }

    protected DirectoryWalker(FileFilter filter, int depthLimit) {
        this.filter = filter;
        this.depthLimit = depthLimit;
    }

    protected DirectoryWalker(IOFileFilter directoryFilter, IOFileFilter fileFilter, int depthLimit) {
        if (directoryFilter == null && fileFilter == null) {
            this.filter = null;
        } else {
            this.filter = FileFilterUtils.or(FileFilterUtils.makeDirectoryOnly(directoryFilter != null ? directoryFilter : TrueFileFilter.TRUE), FileFilterUtils.makeFileOnly(fileFilter != null ? fileFilter : TrueFileFilter.TRUE));
        }
        this.depthLimit = depthLimit;
    }

    protected final void walk(File startDirectory, Collection<T> results) throws IOException {
        if (startDirectory != null) {
            try {
                handleStart(startDirectory, results);
                walk(startDirectory, 0, results);
                handleEnd(results);
            } catch (CancelException cancel) {
                handleCancelled(startDirectory, results, cancel);
            }
        } else {
            throw new NullPointerException("Start Directory is null");
        }
    }

    private void walk(File directory, int depth, Collection<T> results) throws IOException {
        checkIfCancelled(directory, depth, results);
        if (handleDirectory(directory, depth, results)) {
            handleDirectoryStart(directory, depth, results);
            int childDepth = depth + 1;
            int i = this.depthLimit;
            if (i < 0 || childDepth <= i) {
                checkIfCancelled(directory, depth, results);
                FileFilter fileFilter = this.filter;
                File[] childFiles = filterDirectoryContents(directory, depth, fileFilter == null ? directory.listFiles() : directory.listFiles(fileFilter));
                if (childFiles == null) {
                    handleRestricted(directory, childDepth, results);
                } else {
                    for (File childFile : childFiles) {
                        if (childFile.isDirectory()) {
                            walk(childFile, childDepth, results);
                        } else {
                            checkIfCancelled(childFile, childDepth, results);
                            handleFile(childFile, childDepth, results);
                            checkIfCancelled(childFile, childDepth, results);
                        }
                    }
                }
            }
            handleDirectoryEnd(directory, depth, results);
        }
        checkIfCancelled(directory, depth, results);
    }

    protected final void checkIfCancelled(File file, int depth, Collection<T> results) throws IOException {
        if (handleIsCancelled(file, depth, results)) {
            throw new CancelException(file, depth);
        }
    }

    protected boolean handleIsCancelled(File file, int depth, Collection<T> results) throws IOException {
        return false;
    }

    protected void handleCancelled(File startDirectory, Collection<T> results, CancelException cancel) throws IOException {
        throw cancel;
    }

    protected void handleStart(File startDirectory, Collection<T> results) throws IOException {
    }

    protected boolean handleDirectory(File directory, int depth, Collection<T> results) throws IOException {
        return true;
    }

    protected void handleDirectoryStart(File directory, int depth, Collection<T> results) throws IOException {
    }

    protected File[] filterDirectoryContents(File directory, int depth, File[] files) throws IOException {
        return files;
    }

    protected void handleFile(File file, int depth, Collection<T> results) throws IOException {
    }

    protected void handleRestricted(File directory, int depth, Collection<T> results) throws IOException {
    }

    protected void handleDirectoryEnd(File directory, int depth, Collection<T> results) throws IOException {
    }

    protected void handleEnd(Collection<T> results) throws IOException {
    }

    /* loaded from: classes3.dex */
    public static class CancelException extends IOException {
        private static final long serialVersionUID = 1347339620135041008L;
        private final int depth;
        private final File file;

        public CancelException(File file, int depth) {
            this("Operation Cancelled", file, depth);
        }

        public CancelException(String message, File file, int depth) {
            super(message);
            this.file = file;
            this.depth = depth;
        }

        public File getFile() {
            return this.file;
        }

        public int getDepth() {
            return this.depth;
        }
    }
}
