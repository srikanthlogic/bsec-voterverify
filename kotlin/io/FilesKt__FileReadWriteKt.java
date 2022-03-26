package kotlin.io;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.text.Charsets;
/* compiled from: FileReadWrite.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\u0087\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a?\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010,\u001a\u0012\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010.\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010/\u001a\u000200*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u0082\u0002\b\n\u0006\b\u0011(+0\u0001¨\u00061"}, d2 = {"appendBytes", "", "Ljava/io/File;", "array", "", "appendText", "text", "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", "line", "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", ExifInterface.GPS_DIRECTION_TRUE, "block", "Lkotlin/sequences/Sequence;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/io/FilesKt")
/* loaded from: classes3.dex */
class FilesKt__FileReadWriteKt extends FilesKt__FilePathComponentsKt {
    static /* synthetic */ InputStreamReader reader$default(File $this$reader, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new InputStreamReader(new FileInputStream($this$reader), charset);
    }

    private static final InputStreamReader reader(File $this$reader, Charset charset) {
        return new InputStreamReader(new FileInputStream($this$reader), charset);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(File $this$bufferedReader, Charset charset, int bufferSize, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((i & 2) != 0) {
            bufferSize = 8192;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream($this$bufferedReader), charset);
        return inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, bufferSize);
    }

    private static final BufferedReader bufferedReader(File $this$bufferedReader, Charset charset, int bufferSize) {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream($this$bufferedReader), charset);
        return inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, bufferSize);
    }

    static /* synthetic */ OutputStreamWriter writer$default(File $this$writer, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new OutputStreamWriter(new FileOutputStream($this$writer), charset);
    }

    private static final OutputStreamWriter writer(File $this$writer, Charset charset) {
        return new OutputStreamWriter(new FileOutputStream($this$writer), charset);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(File $this$bufferedWriter, Charset charset, int bufferSize, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((i & 2) != 0) {
            bufferSize = 8192;
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream($this$bufferedWriter), charset);
        return outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, bufferSize);
    }

    private static final BufferedWriter bufferedWriter(File $this$bufferedWriter, Charset charset, int bufferSize) {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream($this$bufferedWriter), charset);
        return outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, bufferSize);
    }

    static /* synthetic */ PrintWriter printWriter$default(File $this$printWriter, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream($this$printWriter), charset);
        return new PrintWriter(outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, 8192));
    }

    private static final PrintWriter printWriter(File $this$printWriter, Charset charset) {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream($this$printWriter), charset);
        return new PrintWriter(outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, 8192));
    }

    public static final byte[] readBytes(File $this$readBytes) {
        byte[] bArr;
        Intrinsics.checkParameterIsNotNull($this$readBytes, "$this$readBytes");
        FileInputStream fileInputStream = new FileInputStream($this$readBytes);
        th = null;
        try {
            FileInputStream input = fileInputStream;
            int offset = 0;
            long length = $this$readBytes.length();
            if (length <= ((long) Integer.MAX_VALUE)) {
                int remaining = (int) length;
                byte[] result = new byte[remaining];
                while (remaining > 0) {
                    int read = input.read(result, offset, remaining);
                    if (read < 0) {
                        break;
                    }
                    remaining -= read;
                    offset += read;
                }
                if (remaining > 0) {
                    bArr = Arrays.copyOf(result, offset);
                    Intrinsics.checkExpressionValueIsNotNull(bArr, "java.util.Arrays.copyOf(this, newSize)");
                } else {
                    int extraByte = input.read();
                    if (extraByte == -1) {
                        bArr = result;
                    } else {
                        ExposingBufferByteArrayOutputStream extra = new ExposingBufferByteArrayOutputStream(8193);
                        extra.write(extraByte);
                        ByteStreamsKt.copyTo$default(input, extra, 0, 2, null);
                        int resultingSize = result.length + extra.size();
                        if (resultingSize >= 0) {
                            byte[] buffer = extra.getBuffer();
                            byte[] copyOf = Arrays.copyOf(result, resultingSize);
                            Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
                            bArr = ArraysKt.copyInto(buffer, copyOf, result.length, 0, extra.size());
                        } else {
                            throw new OutOfMemoryError("File " + $this$readBytes + " is too big to fit in memory.");
                        }
                    }
                }
                return bArr;
            }
            throw new OutOfMemoryError("File " + $this$readBytes + " is too big (" + length + " bytes) to fit in memory.");
        } finally {
            try {
                throw th;
            } finally {
            }
        }
    }

    public static final void writeBytes(File $this$writeBytes, byte[] array) {
        Intrinsics.checkParameterIsNotNull($this$writeBytes, "$this$writeBytes");
        Intrinsics.checkParameterIsNotNull(array, "array");
        FileOutputStream it = new FileOutputStream($this$writeBytes);
        th = null;
        try {
            it.write(array);
            Unit unit = Unit.INSTANCE;
        } finally {
            try {
                throw th;
            } finally {
            }
        }
    }

    public static final void appendBytes(File $this$appendBytes, byte[] array) {
        Intrinsics.checkParameterIsNotNull($this$appendBytes, "$this$appendBytes");
        Intrinsics.checkParameterIsNotNull(array, "array");
        FileOutputStream it = new FileOutputStream($this$appendBytes, true);
        th = null;
        try {
            it.write(array);
            Unit unit = Unit.INSTANCE;
        } finally {
            try {
                throw th;
            } finally {
            }
        }
    }

    public static final String readText(File $this$readText, Charset charset) {
        Intrinsics.checkParameterIsNotNull($this$readText, "$this$readText");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        InputStreamReader it = new InputStreamReader(new FileInputStream($this$readText), charset);
        th = null;
        try {
            return TextStreamsKt.readText(it);
        } finally {
            try {
                throw th;
            } finally {
            }
        }
    }

    public static /* synthetic */ String readText$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readText(file, charset);
    }

    public static final void writeText(File $this$writeText, String text, Charset charset) {
        Intrinsics.checkParameterIsNotNull($this$writeText, "$this$writeText");
        Intrinsics.checkParameterIsNotNull(text, "text");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        byte[] bytes = text.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        FilesKt.writeBytes($this$writeText, bytes);
    }

    public static /* synthetic */ void writeText$default(File file, String str, Charset charset, int i, Object obj) {
        if ((i & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.writeText(file, str, charset);
    }

    public static final void appendText(File $this$appendText, String text, Charset charset) {
        Intrinsics.checkParameterIsNotNull($this$appendText, "$this$appendText");
        Intrinsics.checkParameterIsNotNull(text, "text");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        byte[] bytes = text.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        FilesKt.appendBytes($this$appendText, bytes);
    }

    public static /* synthetic */ void appendText$default(File file, String str, Charset charset, int i, Object obj) {
        if ((i & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.appendText(file, str, charset);
    }

    public static final void forEachBlock(File $this$forEachBlock, Function2<? super byte[], ? super Integer, Unit> function2) {
        Intrinsics.checkParameterIsNotNull($this$forEachBlock, "$this$forEachBlock");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        FilesKt.forEachBlock($this$forEachBlock, 4096, function2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [byte[], java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final void forEachBlock(File $this$forEachBlock, int blockSize, Function2<? super byte[], ? super Integer, Unit> function2) {
        Intrinsics.checkParameterIsNotNull($this$forEachBlock, "$this$forEachBlock");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        ?? r0 = new byte[RangesKt.coerceAtLeast(blockSize, 512)];
        FileInputStream fileInputStream = new FileInputStream($this$forEachBlock);
        th = null;
        try {
            FileInputStream input = fileInputStream;
            while (true) {
                int size = input.read(r0);
                if (size <= 0) {
                    Unit unit = Unit.INSTANCE;
                    return;
                }
                function2.invoke(r0, Integer.valueOf(size));
            }
        } finally {
            try {
                throw th;
            } finally {
            }
        }
    }

    public static /* synthetic */ void forEachLine$default(File file, Charset charset, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.forEachLine(file, charset, function1);
    }

    public static final void forEachLine(File $this$forEachLine, Charset charset, Function1<? super String, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$forEachLine, "$this$forEachLine");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TextStreamsKt.forEachLine(new BufferedReader(new InputStreamReader(new FileInputStream($this$forEachLine), charset)), function1);
    }

    private static final FileInputStream inputStream(File $this$inputStream) {
        return new FileInputStream($this$inputStream);
    }

    private static final FileOutputStream outputStream(File $this$outputStream) {
        return new FileOutputStream($this$outputStream);
    }

    public static /* synthetic */ List readLines$default(File file, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readLines(file, charset);
    }

    public static final List<String> readLines(File $this$readLines, Charset charset) {
        Intrinsics.checkParameterIsNotNull($this$readLines, "$this$readLines");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        ArrayList result = new ArrayList();
        FilesKt.forEachLine($this$readLines, charset, new Function1<String, Unit>(result) { // from class: kotlin.io.FilesKt__FileReadWriteKt$readLines$1
            final /* synthetic */ ArrayList $result;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$result = r1;
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(String str) {
                invoke2(str);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke  reason: avoid collision after fix types in other method */
            public final void invoke2(String it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                this.$result.add(it);
            }
        });
        return result;
    }

    public static /* synthetic */ Object useLines$default(File $this$useLines, Charset charset, Function1 block, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkParameterIsNotNull($this$useLines, "$this$useLines");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        Intrinsics.checkParameterIsNotNull(block, "block");
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream($this$useLines), charset);
        BufferedReader it = inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, 8192);
        Throwable th = null;
        try {
            Object invoke = block.invoke(TextStreamsKt.lineSequence(it));
            InlineMarker.finallyStart(1);
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                CloseableKt.closeFinally(it, th);
            } else {
                it.close();
            }
            InlineMarker.finallyEnd(1);
            return invoke;
        } finally {
            try {
                throw th;
            } catch (Throwable th2) {
            }
        }
    }

    public static final <T> T useLines(File $this$useLines, Charset charset, Function1<? super Sequence<String>, ? extends T> function1) {
        Intrinsics.checkParameterIsNotNull($this$useLines, "$this$useLines");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream($this$useLines), charset);
        BufferedReader it = inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, 8192);
        Throwable th = null;
        try {
            T t = (T) function1.invoke(TextStreamsKt.lineSequence(it));
            InlineMarker.finallyStart(1);
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                CloseableKt.closeFinally(it, th);
            } else {
                it.close();
            }
            InlineMarker.finallyEnd(1);
            return t;
        } catch (Throwable th2) {
            try {
                throw th2;
            } catch (Throwable th3) {
                InlineMarker.finallyStart(1);
                if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    try {
                        it.close();
                    } catch (Throwable th4) {
                    }
                } else {
                    CloseableKt.closeFinally(it, th2);
                }
                InlineMarker.finallyEnd(1);
                throw th3;
            }
        }
    }
}
