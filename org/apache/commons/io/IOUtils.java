package org.apache.commons.io;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.StringBuilderWriter;
/* loaded from: classes3.dex */
public class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final char DIR_SEPARATOR = File.separatorChar;
    public static final char DIR_SEPARATOR_UNIX = '/';
    public static final char DIR_SEPARATOR_WINDOWS = '\\';
    public static final int EOF = -1;
    public static final String LINE_SEPARATOR;
    public static final String LINE_SEPARATOR_UNIX = "\n";
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    private static final int SKIP_BUFFER_SIZE = 2048;
    private static byte[] SKIP_BYTE_BUFFER;
    private static char[] SKIP_CHAR_BUFFER;

    static {
        StringBuilderWriter buf = new StringBuilderWriter(4);
        try {
            PrintWriter out = new PrintWriter(buf);
            out.println();
            LINE_SEPARATOR = buf.toString();
            out.close();
            buf.close();
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    buf.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    public static void close(URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    @Deprecated
    public static void closeQuietly(Reader input) {
        closeQuietly((Closeable) input);
    }

    @Deprecated
    public static void closeQuietly(Writer output) {
        closeQuietly((Closeable) output);
    }

    @Deprecated
    public static void closeQuietly(InputStream input) {
        closeQuietly((Closeable) input);
    }

    @Deprecated
    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable) output);
    }

    @Deprecated
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    @Deprecated
    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                closeQuietly(closeable);
            }
        }
    }

    @Deprecated
    public static void closeQuietly(Socket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException e) {
            }
        }
    }

    @Deprecated
    public static void closeQuietly(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
            }
        }
    }

    @Deprecated
    public static void closeQuietly(ServerSocket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException e) {
            }
        }
    }

    public static InputStream toBufferedInputStream(InputStream input) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(input);
    }

    public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(input, size);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader toBufferedReader(Reader reader, int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    public static BufferedReader buffer(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader buffer(Reader reader, int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    public static BufferedWriter buffer(Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public static BufferedWriter buffer(Writer writer, int size) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, size);
    }

    public static BufferedOutputStream buffer(OutputStream outputStream) {
        if (outputStream != null) {
            return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream);
        }
        throw new NullPointerException();
    }

    public static BufferedOutputStream buffer(OutputStream outputStream, int size) {
        if (outputStream != null) {
            return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream, size);
        }
        throw new NullPointerException();
    }

    public static BufferedInputStream buffer(InputStream inputStream) {
        if (inputStream != null) {
            return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream);
        }
        throw new NullPointerException();
    }

    public static BufferedInputStream buffer(InputStream inputStream, int size) {
        if (inputStream != null) {
            return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream, size);
        }
        throw new NullPointerException();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            copy(input, output);
            byte[] byteArray = output.toByteArray();
            output.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    output.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    public static byte[] toByteArray(InputStream input, long size) throws IOException {
        if (size <= 2147483647L) {
            return toByteArray(input, (int) size);
        }
        throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
    }

    public static byte[] toByteArray(InputStream input, int size) throws IOException {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        } else if (size == 0) {
            return new byte[0];
        } else {
            byte[] data = new byte[size];
            int offset = 0;
            while (offset < size) {
                int read = input.read(data, offset, size - offset);
                if (read == -1) {
                    break;
                }
                offset += read;
            }
            if (offset == size) {
                return data;
            }
            throw new IOException("Unexpected read size. current: " + offset + ", expected: " + size);
        }
    }

    @Deprecated
    public static byte[] toByteArray(Reader input) throws IOException {
        return toByteArray(input, Charset.defaultCharset());
    }

    public static byte[] toByteArray(Reader input, Charset encoding) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            copy(input, output, encoding);
            byte[] byteArray = output.toByteArray();
            output.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    output.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    public static byte[] toByteArray(Reader input, String encoding) throws IOException {
        return toByteArray(input, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static byte[] toByteArray(String input) throws IOException {
        return input.getBytes(Charset.defaultCharset());
    }

    public static byte[] toByteArray(URI uri) throws IOException {
        return toByteArray(uri.toURL());
    }

    public static byte[] toByteArray(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        try {
            return toByteArray(conn);
        } finally {
            close(conn);
        }
    }

    public static byte[] toByteArray(URLConnection urlConn) throws IOException {
        InputStream inputStream = urlConn.getInputStream();
        try {
            byte[] byteArray = toByteArray(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return byteArray;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }

    @Deprecated
    public static char[] toCharArray(InputStream is) throws IOException {
        return toCharArray(is, Charset.defaultCharset());
    }

    public static char[] toCharArray(InputStream is, Charset encoding) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output, encoding);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream is, String encoding) throws IOException {
        return toCharArray(is, Charsets.toCharset(encoding));
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter sw = new CharArrayWriter();
        copy(input, sw);
        return sw.toCharArray();
    }

    @Deprecated
    public static String toString(InputStream input) throws IOException {
        return toString(input, Charset.defaultCharset());
    }

    public static String toString(InputStream input, Charset encoding) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        try {
            copy(input, sw, encoding);
            String stringBuilderWriter = sw.toString();
            sw.close();
            return stringBuilderWriter;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    sw.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return toString(input, Charsets.toCharset(encoding));
    }

    public static String toString(Reader input) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        try {
            copy(input, sw);
            String stringBuilderWriter = sw.toString();
            sw.close();
            return stringBuilderWriter;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    sw.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    @Deprecated
    public static String toString(URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    public static String toString(URI uri, Charset encoding) throws IOException {
        return toString(uri.toURL(), Charsets.toCharset(encoding));
    }

    public static String toString(URI uri, String encoding) throws IOException {
        return toString(uri, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static String toString(URL url) throws IOException {
        return toString(url, Charset.defaultCharset());
    }

    public static String toString(URL url, Charset encoding) throws IOException {
        InputStream inputStream = url.openStream();
        try {
            String iOUtils = toString(inputStream, encoding);
            if (inputStream != null) {
                inputStream.close();
            }
            return iOUtils;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }

    public static String toString(URL url, String encoding) throws IOException {
        return toString(url, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static String toString(byte[] input) throws IOException {
        return new String(input, Charset.defaultCharset());
    }

    public static String toString(byte[] input, String encoding) throws IOException {
        return new String(input, Charsets.toCharset(encoding));
    }

    public static String resourceToString(String name, Charset encoding) throws IOException {
        return resourceToString(name, encoding, null);
    }

    public static String resourceToString(String name, Charset encoding, ClassLoader classLoader) throws IOException {
        return toString(resourceToURL(name, classLoader), encoding);
    }

    public static byte[] resourceToByteArray(String name) throws IOException {
        return resourceToByteArray(name, null);
    }

    public static byte[] resourceToByteArray(String name, ClassLoader classLoader) throws IOException {
        return toByteArray(resourceToURL(name, classLoader));
    }

    public static URL resourceToURL(String name) throws IOException {
        return resourceToURL(name, null);
    }

    public static URL resourceToURL(String name, ClassLoader classLoader) throws IOException {
        URL resource = classLoader == null ? IOUtils.class.getResource(name) : classLoader.getResource(name);
        if (resource != null) {
            return resource;
        }
        throw new IOException("Resource not found: " + name);
    }

    @Deprecated
    public static List<String> readLines(InputStream input) throws IOException {
        return readLines(input, Charset.defaultCharset());
    }

    public static List<String> readLines(InputStream input, Charset encoding) throws IOException {
        return readLines(new InputStreamReader(input, Charsets.toCharset(encoding)));
    }

    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        return readLines(input, Charsets.toCharset(encoding));
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = toBufferedReader(input);
        List<String> list = new ArrayList<>();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            list.add(line);
        }
        return list;
    }

    public static LineIterator lineIterator(Reader reader) {
        return new LineIterator(reader);
    }

    public static LineIterator lineIterator(InputStream input, Charset encoding) throws IOException {
        return new LineIterator(new InputStreamReader(input, Charsets.toCharset(encoding)));
    }

    public static LineIterator lineIterator(InputStream input, String encoding) throws IOException {
        return lineIterator(input, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static InputStream toInputStream(CharSequence input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    public static InputStream toInputStream(CharSequence input, Charset encoding) {
        return toInputStream(input.toString(), encoding);
    }

    public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
        return toInputStream(input, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static InputStream toInputStream(String input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    public static InputStream toInputStream(String input, Charset encoding) {
        return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(encoding)));
    }

    public static InputStream toInputStream(String input, String encoding) throws IOException {
        return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(encoding)));
    }

    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void writeChunked(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            int bytes = data.length;
            int offset = 0;
            while (bytes > 0) {
                int chunk = Math.min(bytes, 4096);
                output.write(data, offset, chunk);
                bytes -= chunk;
                offset += chunk;
            }
        }
    }

    @Deprecated
    public static void write(byte[] data, Writer output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(byte[] data, Writer output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(new String(data, Charsets.toCharset(encoding)));
        }
    }

    public static void write(byte[] data, Writer output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(char[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void writeChunked(char[] data, Writer output) throws IOException {
        if (data != null) {
            int bytes = data.length;
            int offset = 0;
            while (bytes > 0) {
                int chunk = Math.min(bytes, 4096);
                output.write(data, offset, chunk);
                bytes -= chunk;
                offset += chunk;
            }
        }
    }

    @Deprecated
    public static void write(char[] data, OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(char[] data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(new String(data).getBytes(Charsets.toCharset(encoding)));
        }
    }

    public static void write(char[] data, OutputStream output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(CharSequence data, Writer output) throws IOException {
        if (data != null) {
            write(data.toString(), output);
        }
    }

    @Deprecated
    public static void write(CharSequence data, OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(CharSequence data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            write(data.toString(), output, encoding);
        }
    }

    public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(String data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    @Deprecated
    public static void write(String data, OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(String data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(Charsets.toCharset(encoding)));
        }
    }

    public static void write(String data, OutputStream output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static void write(StringBuffer data, Writer output) throws IOException {
        if (data != null) {
            output.write(data.toString());
        }
    }

    @Deprecated
    public static void write(StringBuffer data, OutputStream output) throws IOException {
        write(data, output, (String) null);
    }

    @Deprecated
    public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes(Charsets.toCharset(encoding)));
        }
    }

    @Deprecated
    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
        writeLines(lines, lineEnding, output, Charset.defaultCharset());
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset encoding) throws IOException {
        if (lines != null) {
            if (lineEnding == null) {
                lineEnding = LINE_SEPARATOR;
            }
            Charset cs = Charsets.toCharset(encoding);
            for (Object line : lines) {
                if (line != null) {
                    output.write(line.toString().getBytes(cs));
                }
                output.write(lineEnding.getBytes(cs));
            }
        }
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding) throws IOException {
        writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
    }

    public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
        if (lines != null) {
            if (lineEnding == null) {
                lineEnding = LINE_SEPARATOR;
            }
            for (Object line : lines) {
                if (line != null) {
                    writer.write(line.toString());
                }
                writer.write(lineEnding);
            }
        }
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, 4096);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        while (true) {
            int n = input.read(buffer);
            if (-1 == n) {
                return count;
            }
            output.write(buffer, 0, n);
            count += (long) n;
        }
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new byte[4096]);
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
        long j = 0;
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < ((long) bufferLength)) {
            bytesToRead = (int) length;
        }
        long totalRead = 0;
        while (bytesToRead > 0) {
            int read = input.read(buffer, 0, bytesToRead);
            if (-1 == read) {
                break;
            }
            output.write(buffer, 0, read);
            totalRead += (long) read;
            if (length > j) {
                bytesToRead = (int) Math.min(length - totalRead, (long) bufferLength);
                j = 0;
            } else {
                j = 0;
            }
        }
        return totalRead;
    }

    @Deprecated
    public static void copy(InputStream input, Writer output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(InputStream input, Writer output, Charset inputEncoding) throws IOException {
        copy(new InputStreamReader(input, Charsets.toCharset(inputEncoding)), output);
    }

    public static void copy(InputStream input, Writer output, String inputEncoding) throws IOException {
        copy(input, output, Charsets.toCharset(inputEncoding));
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        return copyLarge(input, output, new char[4096]);
    }

    public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long count = 0;
        while (true) {
            int n = input.read(buffer);
            if (-1 == n) {
                return count;
            }
            output.write(buffer, 0, n);
            count += (long) n;
        }
    }

    public static long copyLarge(Reader input, Writer output, long inputOffset, long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new char[4096]);
    }

    public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer) throws IOException {
        long j = 0;
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        int bytesToRead = buffer.length;
        if (length > 0 && length < ((long) buffer.length)) {
            bytesToRead = (int) length;
        }
        long totalRead = 0;
        while (bytesToRead > 0) {
            int read = input.read(buffer, 0, bytesToRead);
            if (-1 == read) {
                break;
            }
            output.write(buffer, 0, read);
            totalRead += (long) read;
            if (length > j) {
                bytesToRead = (int) Math.min(length - totalRead, (long) buffer.length);
                j = 0;
            } else {
                j = 0;
            }
        }
        return totalRead;
    }

    @Deprecated
    public static void copy(Reader input, OutputStream output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(Reader input, OutputStream output, Charset outputEncoding) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output, Charsets.toCharset(outputEncoding));
        copy(input, out);
        out.flush();
    }

    public static void copy(Reader input, OutputStream output, String outputEncoding) throws IOException {
        copy(input, output, Charsets.toCharset(outputEncoding));
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        if (input1 == input2) {
            return true;
        }
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }
        for (int ch = input1.read(); -1 != ch; ch = input1.read()) {
            if (ch != input2.read()) {
                return false;
            }
        }
        if (input2.read() == -1) {
            return true;
        }
        return false;
    }

    public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
        if (input1 == input2) {
            return true;
        }
        Reader input12 = toBufferedReader(input1);
        Reader input22 = toBufferedReader(input2);
        for (int ch = input12.read(); -1 != ch; ch = input12.read()) {
            if (ch != input22.read()) {
                return false;
            }
        }
        if (input22.read() == -1) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0029, code lost:
        if (r4 != null) goto L_0x002c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002c, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:?, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
        if (input1 == input2) {
            return true;
        }
        BufferedReader br1 = toBufferedReader(input1);
        BufferedReader br2 = toBufferedReader(input2);
        String line1 = br1.readLine();
        String line2 = br2.readLine();
        while (line1 != null && line2 != null && line1.equals(line2)) {
            line1 = br1.readLine();
            line2 = br2.readLine();
        }
        return line1.equals(line2);
    }

    public static long skip(InputStream input, long toSkip) throws IOException {
        if (toSkip >= 0) {
            if (SKIP_BYTE_BUFFER == null) {
                SKIP_BYTE_BUFFER = new byte[2048];
            }
            long remain = toSkip;
            while (remain > 0) {
                long n = (long) input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, (long) PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
                if (n < 0) {
                    break;
                }
                remain -= n;
            }
            return toSkip - remain;
        }
        throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
    }

    public static long skip(ReadableByteChannel input, long toSkip) throws IOException {
        if (toSkip >= 0) {
            ByteBuffer skipByteBuffer = ByteBuffer.allocate((int) Math.min(toSkip, (long) PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
            long remain = toSkip;
            while (remain > 0) {
                skipByteBuffer.position(0);
                skipByteBuffer.limit((int) Math.min(remain, (long) PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
                int n = input.read(skipByteBuffer);
                if (n == -1) {
                    break;
                }
                remain -= (long) n;
            }
            return toSkip - remain;
        }
        throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
    }

    public static long skip(Reader input, long toSkip) throws IOException {
        if (toSkip >= 0) {
            if (SKIP_CHAR_BUFFER == null) {
                SKIP_CHAR_BUFFER = new char[2048];
            }
            long remain = toSkip;
            while (remain > 0) {
                long n = (long) input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, (long) PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
                if (n < 0) {
                    break;
                }
                remain -= n;
            }
            return toSkip - remain;
        }
        throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip >= 0) {
            long skipped = skip(input, toSkip);
            if (skipped != toSkip) {
                throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
            }
            return;
        }
        throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
    }

    public static void skipFully(ReadableByteChannel input, long toSkip) throws IOException {
        if (toSkip >= 0) {
            long skipped = skip(input, toSkip);
            if (skipped != toSkip) {
                throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
            }
            return;
        }
        throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
    }

    public static void skipFully(Reader input, long toSkip) throws IOException {
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static int read(Reader input, char[] buffer, int offset, int length) throws IOException {
        if (length >= 0) {
            int remaining = length;
            while (remaining > 0) {
                int count = input.read(buffer, offset + (length - remaining), remaining);
                if (-1 == count) {
                    break;
                }
                remaining -= count;
            }
            return length - remaining;
        }
        throw new IllegalArgumentException("Length must not be negative: " + length);
    }

    public static int read(Reader input, char[] buffer) throws IOException {
        return read(input, buffer, 0, buffer.length);
    }

    public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        if (length >= 0) {
            int remaining = length;
            while (remaining > 0) {
                int count = input.read(buffer, offset + (length - remaining), remaining);
                if (-1 == count) {
                    break;
                }
                remaining -= count;
            }
            return length - remaining;
        }
        throw new IllegalArgumentException("Length must not be negative: " + length);
    }

    public static int read(InputStream input, byte[] buffer) throws IOException {
        return read(input, buffer, 0, buffer.length);
    }

    public static int read(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
        int length = buffer.remaining();
        while (buffer.remaining() > 0 && -1 != input.read(buffer)) {
        }
        return length - buffer.remaining();
    }

    public static void readFully(Reader input, char[] buffer, int offset, int length) throws IOException {
        int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    public static void readFully(Reader input, char[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    public static void readFully(InputStream input, byte[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    public static byte[] readFully(InputStream input, int length) throws IOException {
        byte[] buffer = new byte[length];
        readFully(input, buffer, 0, buffer.length);
        return buffer;
    }

    public static void readFully(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
        int expected = buffer.remaining();
        int actual = read(input, buffer);
        if (actual != expected) {
            throw new EOFException("Length to read: " + expected + " actual: " + actual);
        }
    }
}
