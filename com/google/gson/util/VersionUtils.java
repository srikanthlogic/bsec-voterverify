package com.google.gson.util;
/* loaded from: classes3.dex */
public class VersionUtils {
    private static final int majorJavaVersion = determineMajorJavaVersion();

    private static int determineMajorJavaVersion() {
        String[] parts = System.getProperty("java.version").split("[._]");
        int firstVer = Integer.parseInt(parts[0]);
        if (firstVer != 1 || parts.length <= 1) {
            return firstVer;
        }
        return Integer.parseInt(parts[1]);
    }

    public static int getMajorJavaVersion() {
        return majorJavaVersion;
    }

    public static boolean isJava9OrLater() {
        return majorJavaVersion >= 9;
    }
}
