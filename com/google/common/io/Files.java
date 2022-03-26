package com.google.common.io;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.google.common.graph.SuccessorsFunction;
import com.google.common.graph.Traverser;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
/* loaded from: classes3.dex */
public final class Files {
    private static final int TEMP_DIR_ATTEMPTS = 10000;
    private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser<File>() { // from class: com.google.common.io.Files.2
        public Iterable<File> children(File file) {
            return Files.fileTreeChildren(file);
        }

        public String toString() {
            return "Files.fileTreeTraverser()";
        }
    };
    private static final SuccessorsFunction<File> FILE_TREE = new SuccessorsFunction<File>() { // from class: com.google.common.io.Files.3
        public Iterable<File> successors(File file) {
            return Files.fileTreeChildren(file);
        }
    };

    /* loaded from: classes3.dex */
    private enum FilePredicate implements Predicate<File> {
        IS_DIRECTORY {
            public boolean apply(File file) {
                return file.isDirectory();
            }

            @Override // java.lang.Enum, java.lang.Object
            public String toString() {
                return "Files.isDirectory()";
            }
        },
        IS_FILE {
            public boolean apply(File file) {
                return file.isFile();
            }

            @Override // java.lang.Enum, java.lang.Object
            public String toString() {
                return "Files.isFile()";
            }
        }
    }

    private Files() {
    }

    public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public static ByteSource asByteSource(File file) {
        return new FileByteSource(file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class FileByteSource extends ByteSource {
        private final File file;

        private FileByteSource(File file) {
            this.file = (File) Preconditions.checkNotNull(file);
        }

        @Override // com.google.common.io.ByteSource
        public FileInputStream openStream() throws IOException {
            return new FileInputStream(this.file);
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            if (this.file.isFile()) {
                return Optional.of(Long.valueOf(this.file.length()));
            }
            return Optional.absent();
        }

        @Override // com.google.common.io.ByteSource
        public long size() throws IOException {
            if (this.file.isFile()) {
                return this.file.length();
            }
            throw new FileNotFoundException(this.file.toString());
        }

        @Override // com.google.common.io.ByteSource
        public byte[] read() throws IOException {
            Closer closer = Closer.create();
            try {
                FileInputStream in = (FileInputStream) closer.register(openStream());
                return ByteStreams.toByteArray(in, in.getChannel().size());
            } finally {
                try {
                    throw closer.rethrow(e);
                } finally {
                }
            }
        }

        public String toString() {
            return "Files.asByteSource(" + this.file + ")";
        }
    }

    public static ByteSink asByteSink(File file, FileWriteMode... modes) {
        return new FileByteSink(file, modes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class FileByteSink extends ByteSink {
        private final File file;
        private final ImmutableSet<FileWriteMode> modes;

        private FileByteSink(File file, FileWriteMode... modes) {
            this.file = (File) Preconditions.checkNotNull(file);
            this.modes = ImmutableSet.copyOf(modes);
        }

        @Override // com.google.common.io.ByteSink
        public FileOutputStream openStream() throws IOException {
            return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
        }

        public String toString() {
            return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
        }
    }

    public static CharSource asCharSource(File file, Charset charset) {
        return asByteSource(file).asCharSource(charset);
    }

    public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
        return asByteSink(file, modes).asCharSink(charset);
    }

    public static byte[] toByteArray(File file) throws IOException {
        return asByteSource(file).read();
    }

    @Deprecated
    public static String toString(File file, Charset charset) throws IOException {
        return asCharSource(file, charset).read();
    }

    public static void write(byte[] from, File to) throws IOException {
        asByteSink(to, new FileWriteMode[0]).write(from);
    }

    @Deprecated
    public static void write(CharSequence from, File to, Charset charset) throws IOException {
        asCharSink(to, charset, new FileWriteMode[0]).write(from);
    }

    public static void copy(File from, OutputStream to) throws IOException {
        asByteSource(from).copyTo(to);
    }

    public static void copy(File from, File to) throws IOException {
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
    }

    @Deprecated
    public static void copy(File from, Charset charset, Appendable to) throws IOException {
        asCharSource(from, charset).copyTo(to);
    }

    @Deprecated
    public static void append(CharSequence from, File to, Charset charset) throws IOException {
        asCharSink(to, charset, FileWriteMode.APPEND).write(from);
    }

    public static boolean equal(File file1, File file2) throws IOException {
        Preconditions.checkNotNull(file1);
        Preconditions.checkNotNull(file2);
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }
        long len1 = file1.length();
        long len2 = file2.length();
        if (len1 == 0 || len2 == 0 || len1 == len2) {
            return asByteSource(file1).contentEquals(asByteSource(file2));
        }
        return false;
    }

    public static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";
        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + "9999)");
    }

    public static void touch(File file) throws IOException {
        Preconditions.checkNotNull(file);
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            throw new IOException("Unable to update modification time of " + file);
        }
    }

    public static void createParentDirs(File file) throws IOException {
        Preconditions.checkNotNull(file);
        File parent = file.getCanonicalFile().getParentFile();
        if (parent != null) {
            parent.mkdirs();
            if (!parent.isDirectory()) {
                throw new IOException("Unable to create parent directories of " + file);
            }
        }
    }

    public static void move(File from, File to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        if (!from.renameTo(to)) {
            copy(from, to);
            if (from.delete()) {
                return;
            }
            if (!to.delete()) {
                throw new IOException("Unable to delete " + to);
            }
            throw new IOException("Unable to delete " + from);
        }
    }

    @Deprecated
    public static String readFirstLine(File file, Charset charset) throws IOException {
        return asCharSource(file, charset).readFirstLine();
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return (List) asCharSource(file, charset).readLines(new LineProcessor<List<String>>() { // from class: com.google.common.io.Files.1
            final List<String> result = Lists.newArrayList();

            @Override // com.google.common.io.LineProcessor
            public boolean processLine(String line) {
                this.result.add(line);
                return true;
            }

            @Override // com.google.common.io.LineProcessor
            public List<String> getResult() {
                return this.result;
            }
        });
    }

    @Deprecated
    public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
        return (T) asCharSource(file, charset).readLines(callback);
    }

    @Deprecated
    public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
        return (T) asByteSource(file).read(processor);
    }

    @Deprecated
    public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
        return asByteSource(file).hash(hashFunction);
    }

    public static MappedByteBuffer map(File file) throws IOException {
        Preconditions.checkNotNull(file);
        return map(file, FileChannel.MapMode.READ_ONLY);
    }

    public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
        return mapInternal(file, mode, -1);
    }

    public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws IOException {
        Preconditions.checkArgument(size >= 0, "size (%s) may not be negative", size);
        return mapInternal(file, mode, size);
    }

    private static MappedByteBuffer mapInternal(File file, FileChannel.MapMode mode, long size) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mode);
        Closer closer = Closer.create();
        try {
            FileChannel channel = (FileChannel) closer.register(((RandomAccessFile) closer.register(new RandomAccessFile(file, mode == FileChannel.MapMode.READ_ONLY ? "r" : "rw"))).getChannel());
            return channel.map(mode, 0, size == -1 ? channel.size() : size);
        } finally {
            try {
                throw closer.rethrow(e);
            } finally {
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x004d, code lost:
        if (r5.equals(".") != false) goto L_0x0051;
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0053 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0023 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static String simplifyPath(String pathname) {
        Preconditions.checkNotNull(pathname);
        if (pathname.length() == 0) {
            return ".";
        }
        Iterable<String> components = Splitter.on((char) IOUtils.DIR_SEPARATOR_UNIX).omitEmptyStrings().split(pathname);
        List<String> path = new ArrayList<>();
        Iterator<String> it = components.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            String component = it.next();
            int hashCode = component.hashCode();
            if (hashCode != 46) {
                if (hashCode == 1472 && component.equals("..")) {
                    z = true;
                    if (z) {
                        if (!z) {
                            path.add(component);
                        } else if (path.size() <= 0 || path.get(path.size() - 1).equals("..")) {
                            path.add("..");
                        } else {
                            path.remove(path.size() - 1);
                        }
                    }
                }
                z = true;
                if (z) {
                }
            }
        }
        String result = Joiner.on((char) IOUtils.DIR_SEPARATOR_UNIX).join(path);
        if (pathname.charAt(0) == '/') {
            result = "/" + result;
        }
        while (result.startsWith("/../")) {
            result = result.substring(3);
        }
        if (result.equals("/..")) {
            return "/";
        }
        if ("".equals(result)) {
            return ".";
        }
        return result;
    }

    public static String getFileExtension(String fullName) {
        Preconditions.checkNotNull(fullName);
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getNameWithoutExtension(String file) {
        Preconditions.checkNotNull(file);
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }

    @Deprecated
    static TreeTraverser<File> fileTreeTraverser() {
        return FILE_TREE_TRAVERSER;
    }

    public static Traverser<File> fileTraverser() {
        return Traverser.forTree(FILE_TREE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Iterable<File> fileTreeChildren(File file) {
        File[] files;
        if (!file.isDirectory() || (files = file.listFiles()) == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(files));
    }

    public static Predicate<File> isDirectory() {
        return FilePredicate.IS_DIRECTORY;
    }

    public static Predicate<File> isFile() {
        return FilePredicate.IS_FILE;
    }
}
