package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
@Deprecated
/* loaded from: classes3.dex */
public class FileSystemUtils {
    private static final String DF;
    private static final int INIT_PROBLEM = -1;
    private static final FileSystemUtils INSTANCE = new FileSystemUtils();
    private static final int OS;
    private static final int OTHER = 0;
    private static final int POSIX_UNIX = 3;
    private static final int UNIX = 2;
    private static final int WINDOWS = 1;

    static {
        int os;
        String osName;
        String dfPath = "df";
        try {
            osName = System.getProperty("os.name");
        } catch (Exception e) {
            os = -1;
        }
        if (osName != null) {
            String osName2 = osName.toLowerCase(Locale.ENGLISH);
            if (osName2.contains("windows")) {
                os = 1;
            } else {
                if (!osName2.contains("linux") && !osName2.contains("mpe/ix") && !osName2.contains("freebsd") && !osName2.contains("openbsd") && !osName2.contains("irix") && !osName2.contains("digital unix") && !osName2.contains("unix") && !osName2.contains("mac os x")) {
                    if (!osName2.contains("sun os") && !osName2.contains("sunos") && !osName2.contains("solaris")) {
                        if (!osName2.contains("hp-ux") && !osName2.contains("aix")) {
                            os = 0;
                        }
                        os = 3;
                    }
                    os = 3;
                    dfPath = "/usr/xpg4/bin/df";
                }
                os = 2;
            }
            OS = os;
            DF = dfPath;
            return;
        }
        throw new IOException("os.name not found");
    }

    @Deprecated
    public static long freeSpace(String path) throws IOException {
        return INSTANCE.freeSpaceOS(path, OS, false, -1);
    }

    @Deprecated
    public static long freeSpaceKb(String path) throws IOException {
        return freeSpaceKb(path, -1);
    }

    @Deprecated
    public static long freeSpaceKb(String path, long timeout) throws IOException {
        return INSTANCE.freeSpaceOS(path, OS, true, timeout);
    }

    @Deprecated
    public static long freeSpaceKb() throws IOException {
        return freeSpaceKb(-1);
    }

    @Deprecated
    public static long freeSpaceKb(long timeout) throws IOException {
        return freeSpaceKb(new File(".").getAbsolutePath(), timeout);
    }

    long freeSpaceOS(String path, int os, boolean kb, long timeout) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        } else if (os == 0) {
            throw new IllegalStateException("Unsupported operating system");
        } else if (os == 1) {
            long freeSpaceWindows = freeSpaceWindows(path, timeout);
            return kb ? freeSpaceWindows / 1024 : freeSpaceWindows;
        } else if (os == 2) {
            return freeSpaceUnix(path, kb, false, timeout);
        } else {
            if (os == 3) {
                return freeSpaceUnix(path, kb, true, timeout);
            }
            throw new IllegalStateException("Exception caught when determining operating system");
        }
    }

    long freeSpaceWindows(String path, long timeout) throws IOException {
        String normPath = FilenameUtils.normalize(path, false);
        if (normPath != null) {
            if (normPath.length() > 0 && normPath.charAt(0) != '\"') {
                normPath = "\"" + normPath + "\"";
            }
            List<String> lines = performCommand(new String[]{"cmd.exe", "/C", "dir /a /-c " + normPath}, Integer.MAX_VALUE, timeout);
            for (int i = lines.size() - 1; i >= 0; i--) {
                String line = lines.get(i);
                if (line.length() > 0) {
                    return parseDir(line, normPath);
                }
            }
            throw new IOException("Command line 'dir /-c' did not return any info for path '" + normPath + "'");
        }
        throw new IllegalArgumentException(path);
    }

    long parseDir(String line, String path) throws IOException {
        int bytesStart = 0;
        int bytesEnd = 0;
        int j = line.length() - 1;
        while (true) {
            if (j < 0) {
                break;
            } else if (Character.isDigit(line.charAt(j))) {
                bytesEnd = j + 1;
                break;
            } else {
                j--;
            }
        }
        while (true) {
            if (j < 0) {
                break;
            }
            char c = line.charAt(j);
            if (!Character.isDigit(c) && c != ',' && c != '.') {
                bytesStart = j + 1;
                break;
            }
            j--;
        }
        if (j >= 0) {
            StringBuilder buf = new StringBuilder(line.substring(bytesStart, bytesEnd));
            int k = 0;
            while (k < buf.length()) {
                if (buf.charAt(k) == ',' || buf.charAt(k) == '.') {
                    buf.deleteCharAt(k);
                    k--;
                }
                k++;
            }
            return parseBytes(buf.toString(), path);
        }
        throw new IOException("Command line 'dir /-c' did not return valid info for path '" + path + "'");
    }

    long freeSpaceUnix(String path, boolean kb, boolean posix, long timeout) throws IOException {
        if (!path.isEmpty()) {
            String flags = "-";
            if (kb) {
                flags = flags + "k";
            }
            if (posix) {
                flags = flags + "P";
            }
            List<String> lines = performCommand(flags.length() > 1 ? new String[]{DF, flags, path} : new String[]{DF, path}, 3, timeout);
            if (lines.size() >= 2) {
                StringTokenizer tok = new StringTokenizer(lines.get(1), " ");
                if (tok.countTokens() >= 4) {
                    tok.nextToken();
                } else if (tok.countTokens() != 1 || lines.size() < 3) {
                    throw new IOException("Command line '" + DF + "' did not return data as expected for path '" + path + "'- check path is valid");
                } else {
                    tok = new StringTokenizer(lines.get(2), " ");
                }
                tok.nextToken();
                tok.nextToken();
                return parseBytes(tok.nextToken(), path);
            }
            throw new IOException("Command line '" + DF + "' did not return info as expected for path '" + path + "'- response was " + lines);
        }
        throw new IllegalArgumentException("Path must not be empty");
    }

    long parseBytes(String freeSpace, String path) throws IOException {
        try {
            long bytes = Long.parseLong(freeSpace);
            if (bytes >= 0) {
                return bytes;
            }
            throw new IOException("Command line '" + DF + "' did not find free space in response for path '" + path + "'- check path is valid");
        } catch (NumberFormatException ex) {
            throw new IOException("Command line '" + DF + "' did not return numeric data as expected for path '" + path + "'- check path is valid", ex);
        }
    }

    List<String> performCommand(String[] cmdAttribs, int max, long timeout) throws IOException {
        Process proc;
        InputStream in;
        OutputStream out;
        InputStream err;
        BufferedReader inr;
        try {
            List<String> lines = new ArrayList<>(20);
            proc = null;
            in = null;
            out = null;
            err = null;
            inr = null;
            try {
                Thread monitor = ThreadMonitor.start(timeout);
                proc = openProcess(cmdAttribs);
                InputStream in2 = proc.getInputStream();
                OutputStream out2 = proc.getOutputStream();
                InputStream err2 = proc.getErrorStream();
                BufferedReader inr2 = new BufferedReader(new InputStreamReader(in2, Charset.defaultCharset()));
                for (String line = inr2.readLine(); line != null && lines.size() < max; line = inr2.readLine()) {
                    lines.add(line.toLowerCase(Locale.ENGLISH).trim());
                }
                proc.waitFor();
                ThreadMonitor.stop(monitor);
                if (proc.exitValue() != 0) {
                    throw new IOException("Command line returned OS error code '" + proc.exitValue() + "' for command " + Arrays.asList(cmdAttribs));
                } else if (!lines.isEmpty()) {
                    inr2.close();
                    inr = null;
                    in2.close();
                    in = null;
                    if (out2 != null) {
                        out2.close();
                        out2 = null;
                    }
                    if (err2 != null) {
                        err2.close();
                        err2 = null;
                    }
                    return lines;
                } else {
                    throw new IOException("Command line did not return any info for command " + Arrays.asList(cmdAttribs));
                }
            } catch (InterruptedException ex) {
                throw new IOException("Command line threw an InterruptedException for command " + Arrays.asList(cmdAttribs) + " timeout=" + timeout, ex);
            }
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(err);
            IOUtils.closeQuietly((Reader) inr);
            if (proc != null) {
                proc.destroy();
            }
        }
    }

    Process openProcess(String[] cmdAttribs) throws IOException {
        return Runtime.getRuntime().exec(cmdAttribs);
    }
}
