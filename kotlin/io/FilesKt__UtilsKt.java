package kotlin.io;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
/* compiled from: Utils.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a(\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a(\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002¢\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002¢\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002¢\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0004¨\u0006+"}, d2 = {"extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/io/FilesKt")
/* loaded from: classes3.dex */
class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt {
    public static /* synthetic */ File createTempDir$default(String str, String str2, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempDir(str, str2, file);
    }

    public static final File createTempDir(String prefix, String suffix, File directory) {
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        File dir = File.createTempFile(prefix, suffix, directory);
        dir.delete();
        if (dir.mkdir()) {
            Intrinsics.checkExpressionValueIsNotNull(dir, "dir");
            return dir;
        }
        throw new IOException("Unable to create temporary directory " + dir + FilenameUtils.EXTENSION_SEPARATOR);
    }

    public static /* synthetic */ File createTempFile$default(String str, String str2, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempFile(str, str2, file);
    }

    public static final File createTempFile(String prefix, String suffix, File directory) {
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        File createTempFile = File.createTempFile(prefix, suffix, directory);
        Intrinsics.checkExpressionValueIsNotNull(createTempFile, "File.createTempFile(prefix, suffix, directory)");
        return createTempFile;
    }

    public static final String getExtension(File $this$extension) {
        Intrinsics.checkParameterIsNotNull($this$extension, "$this$extension");
        String name = $this$extension.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        return StringsKt.substringAfterLast(name, (char) FilenameUtils.EXTENSION_SEPARATOR, "");
    }

    public static final String getInvariantSeparatorsPath(File $this$invariantSeparatorsPath) {
        Intrinsics.checkParameterIsNotNull($this$invariantSeparatorsPath, "$this$invariantSeparatorsPath");
        if (File.separatorChar != '/') {
            String path = $this$invariantSeparatorsPath.getPath();
            Intrinsics.checkExpressionValueIsNotNull(path, "path");
            return StringsKt.replace$default(path, File.separatorChar, (char) IOUtils.DIR_SEPARATOR_UNIX, false, 4, (Object) null);
        }
        String path2 = $this$invariantSeparatorsPath.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path2, "path");
        return path2;
    }

    public static final String getNameWithoutExtension(File $this$nameWithoutExtension) {
        Intrinsics.checkParameterIsNotNull($this$nameWithoutExtension, "$this$nameWithoutExtension");
        String name = $this$nameWithoutExtension.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
        return StringsKt.substringBeforeLast$default(name, ".", (String) null, 2, (Object) null);
    }

    public static final String toRelativeString(File $this$toRelativeString, File base) {
        Intrinsics.checkParameterIsNotNull($this$toRelativeString, "$this$toRelativeString");
        Intrinsics.checkParameterIsNotNull(base, "base");
        String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($this$toRelativeString, base);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return relativeStringOrNull$FilesKt__UtilsKt;
        }
        throw new IllegalArgumentException("this and base files have different roots: " + $this$toRelativeString + " and " + base + FilenameUtils.EXTENSION_SEPARATOR);
    }

    public static final File relativeTo(File $this$relativeTo, File base) {
        Intrinsics.checkParameterIsNotNull($this$relativeTo, "$this$relativeTo");
        Intrinsics.checkParameterIsNotNull(base, "base");
        return new File(FilesKt.toRelativeString($this$relativeTo, base));
    }

    public static final File relativeToOrSelf(File $this$relativeToOrSelf, File base) {
        Intrinsics.checkParameterIsNotNull($this$relativeToOrSelf, "$this$relativeToOrSelf");
        Intrinsics.checkParameterIsNotNull(base, "base");
        String p1 = toRelativeStringOrNull$FilesKt__UtilsKt($this$relativeToOrSelf, base);
        return p1 != null ? new File(p1) : $this$relativeToOrSelf;
    }

    public static final File relativeToOrNull(File $this$relativeToOrNull, File base) {
        Intrinsics.checkParameterIsNotNull($this$relativeToOrNull, "$this$relativeToOrNull");
        Intrinsics.checkParameterIsNotNull(base, "base");
        String p1 = toRelativeStringOrNull$FilesKt__UtilsKt($this$relativeToOrNull, base);
        if (p1 != null) {
            return new File(p1);
        }
        return null;
    }

    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(File $this$toRelativeStringOrNull, File base) {
        FilePathComponents thisComponents = normalize$FilesKt__UtilsKt(FilesKt.toComponents($this$toRelativeStringOrNull));
        FilePathComponents baseComponents = normalize$FilesKt__UtilsKt(FilesKt.toComponents(base));
        if (!Intrinsics.areEqual(thisComponents.getRoot(), baseComponents.getRoot())) {
            return null;
        }
        int baseCount = baseComponents.getSize();
        int thisCount = thisComponents.getSize();
        int i = 0;
        int maxSameCount = Math.min(thisCount, baseCount);
        while (i < maxSameCount && Intrinsics.areEqual(thisComponents.getSegments().get(i), baseComponents.getSegments().get(i))) {
            i++;
        }
        StringBuilder res = new StringBuilder();
        int i2 = baseCount - 1;
        if (i2 >= i) {
            while (!Intrinsics.areEqual(baseComponents.getSegments().get(i2).getName(), "..")) {
                res.append("..");
                if (i2 != i) {
                    res.append(File.separatorChar);
                }
                if (i2 != i) {
                    i2--;
                }
            }
            return null;
        }
        if (i < thisCount) {
            if (i < baseCount) {
                res.append(File.separatorChar);
            }
            String str = File.separator;
            Intrinsics.checkExpressionValueIsNotNull(str, "File.separator");
            CollectionsKt.joinTo$default(CollectionsKt.drop(thisComponents.getSegments(), i), res, str, null, null, 0, null, null, 124, null);
        }
        return res.toString();
    }

    public static /* synthetic */ File copyTo$default(File file, File file2, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 8192;
        }
        return FilesKt.copyTo(file, file2, z, i);
    }

    public static final File copyTo(File $this$copyTo, File target, boolean overwrite, int bufferSize) {
        Intrinsics.checkParameterIsNotNull($this$copyTo, "$this$copyTo");
        Intrinsics.checkParameterIsNotNull(target, "target");
        if ($this$copyTo.exists()) {
            if (target.exists()) {
                if (!overwrite) {
                    throw new FileAlreadyExistsException($this$copyTo, target, "The destination file already exists.");
                } else if (!target.delete()) {
                    throw new FileAlreadyExistsException($this$copyTo, target, "Tried to overwrite the destination, but failed to delete it.");
                }
            }
            if (!$this$copyTo.isDirectory()) {
                File parentFile = target.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                FileOutputStream output = new FileInputStream($this$copyTo);
                th = null;
                try {
                    FileInputStream input = output;
                    output = new FileOutputStream(target);
                    th = null;
                    ByteStreamsKt.copyTo(input, output, bufferSize);
                } finally {
                    try {
                        throw th;
                    } finally {
                    }
                }
            } else if (!target.mkdirs()) {
                throw new FileSystemException($this$copyTo, target, "Failed to create target directory.");
            }
            return target;
        }
        throw new NoSuchFileException($this$copyTo, null, "The source file doesn't exist.", 2, null);
    }

    public static /* synthetic */ boolean copyRecursively$default(File file, File file2, boolean z, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        if ((i & 4) != 0) {
            function2 = FilesKt__UtilsKt$copyRecursively$1.INSTANCE;
        }
        return FilesKt.copyRecursively(file, file2, z, function2);
    }

    public static final boolean copyRecursively(File $this$copyRecursively, File target, boolean overwrite, Function2<? super File, ? super IOException, ? extends OnErrorAction> function2) {
        boolean stillExists;
        Intrinsics.checkParameterIsNotNull($this$copyRecursively, "$this$copyRecursively");
        Intrinsics.checkParameterIsNotNull(target, "target");
        Intrinsics.checkParameterIsNotNull(function2, "onError");
        if (!$this$copyRecursively.exists()) {
            return ((OnErrorAction) function2.invoke($this$copyRecursively, new NoSuchFileException($this$copyRecursively, null, "The source file doesn't exist.", 2, null))) != OnErrorAction.TERMINATE;
        }
        try {
            Iterator<File> it = FilesKt.walkTopDown($this$copyRecursively).onFail(new Function2<File, IOException, Unit>() { // from class: kotlin.io.FilesKt__UtilsKt$copyRecursively$2
                @Override // kotlin.jvm.functions.Function2
                public /* bridge */ /* synthetic */ Unit invoke(File file, IOException iOException) {
                    invoke2(file, iOException);
                    return Unit.INSTANCE;
                }

                /* renamed from: invoke  reason: avoid collision after fix types in other method */
                public final void invoke2(File f, IOException e) {
                    Intrinsics.checkParameterIsNotNull(f, "f");
                    Intrinsics.checkParameterIsNotNull(e, "e");
                    if (((OnErrorAction) Function2.this.invoke(f, e)) == OnErrorAction.TERMINATE) {
                        throw new TerminateException(f);
                    }
                }
            }).iterator();
            while (it.hasNext()) {
                File src = it.next();
                if (src.exists()) {
                    File dstFile = new File(target, FilesKt.toRelativeString(src, $this$copyRecursively));
                    if (dstFile.exists() && (!src.isDirectory() || !dstFile.isDirectory())) {
                        if (!overwrite) {
                            stillExists = true;
                        } else if (dstFile.isDirectory()) {
                            stillExists = !FilesKt.deleteRecursively(dstFile);
                        } else {
                            stillExists = !dstFile.delete();
                        }
                        if (stillExists) {
                            if (((OnErrorAction) function2.invoke(dstFile, new FileAlreadyExistsException(src, dstFile, "The destination file already exists."))) == OnErrorAction.TERMINATE) {
                                return false;
                            }
                        }
                    }
                    if (src.isDirectory()) {
                        dstFile.mkdirs();
                    } else if (FilesKt.copyTo$default(src, dstFile, overwrite, 0, 4, null).length() != src.length() && ((OnErrorAction) function2.invoke(src, new IOException("Source file wasn't copied completely, length of destination file differs."))) == OnErrorAction.TERMINATE) {
                        return false;
                    }
                } else if (((OnErrorAction) function2.invoke(src, new NoSuchFileException(src, null, "The source file doesn't exist.", 2, null))) == OnErrorAction.TERMINATE) {
                    return false;
                }
            }
            return true;
        } catch (TerminateException e) {
            return false;
        }
    }

    public static final boolean deleteRecursively(File $this$deleteRecursively) {
        Intrinsics.checkParameterIsNotNull($this$deleteRecursively, "$this$deleteRecursively");
        boolean accumulator$iv = true;
        for (File it : FilesKt.walkBottomUp($this$deleteRecursively)) {
            accumulator$iv = (it.delete() || !it.exists()) && accumulator$iv;
        }
        return accumulator$iv;
    }

    public static final boolean startsWith(File $this$startsWith, File other) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        FilePathComponents components = FilesKt.toComponents($this$startsWith);
        FilePathComponents otherComponents = FilesKt.toComponents(other);
        if (!(!Intrinsics.areEqual(components.getRoot(), otherComponents.getRoot())) && components.getSize() >= otherComponents.getSize()) {
            return components.getSegments().subList(0, otherComponents.getSize()).equals(otherComponents.getSegments());
        }
        return false;
    }

    public static final boolean startsWith(File $this$startsWith, String other) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return FilesKt.startsWith($this$startsWith, new File(other));
    }

    public static final boolean endsWith(File $this$endsWith, File other) {
        Intrinsics.checkParameterIsNotNull($this$endsWith, "$this$endsWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        FilePathComponents components = FilesKt.toComponents($this$endsWith);
        FilePathComponents otherComponents = FilesKt.toComponents(other);
        if (otherComponents.isRooted()) {
            return Intrinsics.areEqual($this$endsWith, other);
        }
        int shift = components.getSize() - otherComponents.getSize();
        if (shift < 0) {
            return false;
        }
        return components.getSegments().subList(shift, components.getSize()).equals(otherComponents.getSegments());
    }

    public static final boolean endsWith(File $this$endsWith, String other) {
        Intrinsics.checkParameterIsNotNull($this$endsWith, "$this$endsWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return FilesKt.endsWith($this$endsWith, new File(other));
    }

    public static final File normalize(File $this$normalize) {
        Intrinsics.checkParameterIsNotNull($this$normalize, "$this$normalize");
        FilePathComponents $this$with = FilesKt.toComponents($this$normalize);
        File root = $this$with.getRoot();
        List<File> normalize$FilesKt__UtilsKt = normalize$FilesKt__UtilsKt($this$with.getSegments());
        String str = File.separator;
        Intrinsics.checkExpressionValueIsNotNull(str, "File.separator");
        return FilesKt.resolve(root, CollectionsKt.joinToString$default(normalize$FilesKt__UtilsKt, str, null, null, 0, null, null, 62, null));
    }

    private static final FilePathComponents normalize$FilesKt__UtilsKt(FilePathComponents $this$normalize) {
        return new FilePathComponents($this$normalize.getRoot(), normalize$FilesKt__UtilsKt($this$normalize.getSegments()));
    }

    private static final List<File> normalize$FilesKt__UtilsKt(List<? extends File> list) {
        List list2 = new ArrayList(list.size());
        for (File file : list) {
            String name = file.getName();
            if (name != null) {
                int hashCode = name.hashCode();
                if (hashCode != 46) {
                    if (hashCode == 1472 && name.equals("..")) {
                        if (list2.isEmpty() || !(!Intrinsics.areEqual(((File) CollectionsKt.last((List<? extends Object>) list2)).getName(), ".."))) {
                            list2.add(file);
                        } else {
                            list2.remove(list2.size() - 1);
                        }
                    }
                } else if (name.equals(".")) {
                }
            }
            list2.add(file);
        }
        return list2;
    }

    public static final File resolve(File $this$resolve, File relative) {
        Intrinsics.checkParameterIsNotNull($this$resolve, "$this$resolve");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        if (FilesKt.isRooted(relative)) {
            return relative;
        }
        String baseName = $this$resolve.toString();
        Intrinsics.checkExpressionValueIsNotNull(baseName, "this.toString()");
        if ((baseName.length() == 0) || StringsKt.endsWith$default((CharSequence) baseName, File.separatorChar, false, 2, (Object) null)) {
            return new File(baseName + relative);
        }
        return new File(baseName + File.separatorChar + relative);
    }

    public static final File resolve(File $this$resolve, String relative) {
        Intrinsics.checkParameterIsNotNull($this$resolve, "$this$resolve");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        return FilesKt.resolve($this$resolve, new File(relative));
    }

    public static final File resolveSibling(File $this$resolveSibling, File relative) {
        Intrinsics.checkParameterIsNotNull($this$resolveSibling, "$this$resolveSibling");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        FilePathComponents components = FilesKt.toComponents($this$resolveSibling);
        return FilesKt.resolve(FilesKt.resolve(components.getRoot(), components.getSize() == 0 ? new File("..") : components.subPath(0, components.getSize() - 1)), relative);
    }

    public static final File resolveSibling(File $this$resolveSibling, String relative) {
        Intrinsics.checkParameterIsNotNull($this$resolveSibling, "$this$resolveSibling");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        return FilesKt.resolveSibling($this$resolveSibling, new File(relative));
    }
}
