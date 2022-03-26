package com.google.firebase.crashlytics.internal.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Debug;
import android.os.StatFs;
import android.provider.Settings;
import android.text.TextUtils;
import com.google.firebase.crashlytics.internal.Logger;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
/* loaded from: classes3.dex */
public class CommonUtils {
    static final int BYTES_IN_A_GIGABYTE = 1073741824;
    static final int BYTES_IN_A_KILOBYTE = 1024;
    static final int BYTES_IN_A_MEGABYTE = 1048576;
    public static final int DEVICE_STATE_BETAOS = 8;
    public static final int DEVICE_STATE_COMPROMISEDLIBRARIES = 32;
    public static final int DEVICE_STATE_DEBUGGERATTACHED = 4;
    public static final int DEVICE_STATE_ISSIMULATOR = 1;
    public static final int DEVICE_STATE_JAILBROKEN = 2;
    public static final int DEVICE_STATE_VENDORINTERNAL = 16;
    private static final String GOLDFISH = "goldfish";
    static final String LEGACY_MAPPING_FILE_ID_RESOURCE_NAME = "com.crashlytics.android.build_id";
    public static final String LEGACY_SHARED_PREFS_NAME = "com.crashlytics.prefs";
    static final String MAPPING_FILE_ID_RESOURCE_NAME = "com.google.firebase.crashlytics.mapping_file_id";
    private static final String RANCHU = "ranchu";
    private static final String SDK = "sdk";
    private static final String SHA1_INSTANCE = "SHA-1";
    public static final String SHARED_PREFS_NAME = "com.google.firebase.crashlytics";
    private static final long UNCALCULATED_TOTAL_RAM = -1;
    private static final String UNITY_EDITOR_VERSION = "com.google.firebase.crashlytics.unity_version";
    private static final char[] HEX_VALUES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static long totalRamInBytes = -1;

    public static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences("com.google.firebase.crashlytics", 0);
    }

    public static SharedPreferences getLegacySharedPrefs(Context context) {
        return context.getSharedPreferences(LEGACY_SHARED_PREFS_NAME, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0036, code lost:
        r1 = r5[1];
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static String extractFieldFromSystemFile(File file, String fieldname) {
        String toReturn = null;
        ?? exists = file.exists();
        if (exists != 0) {
            try {
                exists = 0;
                exists = 0;
                try {
                    exists = new BufferedReader(new FileReader(file), 1024);
                    while (true) {
                        String line = exists.readLine();
                        if (line == null) {
                            break;
                        }
                        String[] pieces = Pattern.compile("\\s*:\\s*").split(line, 2);
                        if (pieces.length > 1 && pieces[0].equals(fieldname)) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    Logger logger = Logger.getLogger();
                    logger.e("Error parsing " + file, e);
                }
            } finally {
                closeOrLog(exists, "Failed to close system file reader.");
            }
        }
        return toReturn;
    }

    public static int getCpuArchitectureInt() {
        return Architecture.getValue().ordinal();
    }

    /* loaded from: classes3.dex */
    enum Architecture {
        X86_32,
        X86_64,
        ARM_UNKNOWN,
        PPC,
        PPC64,
        ARMV6,
        ARMV7,
        UNKNOWN,
        ARMV7S,
        ARM64;
        
        private static final Map<String, Architecture> matcher = new HashMap(4);

        static {
            matcher.put("armeabi-v7a", ARMV7);
            matcher.put("armeabi", ARMV6);
            matcher.put("arm64-v8a", ARM64);
            matcher.put("x86", X86_32);
        }

        static Architecture getValue() {
            String arch = Build.CPU_ABI;
            if (TextUtils.isEmpty(arch)) {
                Logger.getLogger().v("Architecture#getValue()::Build.CPU_ABI returned null or empty");
                return UNKNOWN;
            }
            Architecture value = matcher.get(arch.toLowerCase(Locale.US));
            if (value == null) {
                return UNKNOWN;
            }
            return value;
        }
    }

    public static synchronized long getTotalRamInBytes() {
        long bytes;
        synchronized (CommonUtils.class) {
            if (totalRamInBytes == -1) {
                long bytes2 = 0;
                String result = extractFieldFromSystemFile(new File("/proc/meminfo"), "MemTotal");
                if (!TextUtils.isEmpty(result)) {
                    result = result.toUpperCase(Locale.US);
                    try {
                        if (result.endsWith("KB")) {
                            bytes2 = convertMemInfoToBytes(result, "KB", 1024);
                        } else if (result.endsWith("MB")) {
                            bytes2 = convertMemInfoToBytes(result, "MB", 1048576);
                        } else if (result.endsWith("GB")) {
                            bytes2 = convertMemInfoToBytes(result, "GB", 1073741824);
                        } else {
                            Logger logger = Logger.getLogger();
                            logger.w("Unexpected meminfo format while computing RAM: " + result);
                        }
                    } catch (NumberFormatException e) {
                        Logger logger2 = Logger.getLogger();
                        logger2.e("Unexpected meminfo format while computing RAM: " + result, e);
                    }
                }
                totalRamInBytes = bytes2;
            }
            bytes = totalRamInBytes;
        }
        return bytes;
    }

    static long convertMemInfoToBytes(String memInfo, String notation, int notationMultiplier) {
        return Long.parseLong(memInfo.split(notation)[0].trim()) * ((long) notationMultiplier);
    }

    public static ActivityManager.RunningAppProcessInfo getAppProcessInfo(String packageName, Context context) {
        List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (processes == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info.processName.equals(packageName)) {
                return info;
            }
        }
        return null;
    }

    public static String streamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String sha1(String source) {
        return hash(source, "SHA-1");
    }

    private static String hash(String s, String algorithm) {
        return hash(s.getBytes(), algorithm);
    }

    private static String hash(byte[] bytes, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            return hexify(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            Logger logger = Logger.getLogger();
            logger.e("Could not create hashing algorithm: " + algorithm + ", returning empty string.", e);
            return "";
        }
    }

    public static String createInstanceIdFrom(String... sliceIds) {
        if (sliceIds == null || sliceIds.length == 0) {
            return null;
        }
        List<String> sliceIdList = new ArrayList<>();
        for (String id : sliceIds) {
            if (id != null) {
                sliceIdList.add(id.replace("-", "").toLowerCase(Locale.US));
            }
        }
        Collections.sort(sliceIdList);
        StringBuilder sb = new StringBuilder();
        for (String id2 : sliceIdList) {
            sb.append(id2);
        }
        String concatValue = sb.toString();
        if (concatValue.length() > 0) {
            return sha1(concatValue);
        }
        return null;
    }

    public static long calculateFreeRamInBytes(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(mi);
        return mi.availMem;
    }

    public static long calculateUsedDiskSpaceInBytes(String path) {
        StatFs statFs = new StatFs(path);
        long blockSizeBytes = (long) statFs.getBlockSize();
        return (((long) statFs.getBlockCount()) * blockSizeBytes) - (((long) statFs.getAvailableBlocks()) * blockSizeBytes);
    }

    public static boolean getProximitySensorEnabled(Context context) {
        if (!isEmulator(context) && ((SensorManager) context.getSystemService("sensor")).getDefaultSensor(8) != null) {
            return true;
        }
        return false;
    }

    @Deprecated
    public static boolean isLoggingEnabled(Context context) {
        return false;
    }

    public static boolean getBooleanResourceValue(Context context, String key, boolean defaultValue) {
        Resources resources;
        if (!(context == null || (resources = context.getResources()) == null)) {
            int id = getResourcesIdentifier(context, key, "bool");
            if (id > 0) {
                return resources.getBoolean(id);
            }
            int id2 = getResourcesIdentifier(context, key, "string");
            if (id2 > 0) {
                return Boolean.parseBoolean(context.getString(id2));
            }
        }
        return defaultValue;
    }

    public static int getResourcesIdentifier(Context context, String key, String resourceType) {
        return context.getResources().getIdentifier(key, resourceType, getResourcePackageName(context));
    }

    public static boolean isEmulator(Context context) {
        return Build.PRODUCT.contains(SDK) || Build.HARDWARE.contains(GOLDFISH) || Build.HARDWARE.contains(RANCHU) || Settings.Secure.getString(context.getContentResolver(), "android_id") == null;
    }

    public static boolean isRooted(Context context) {
        boolean isEmulator = isEmulator(context);
        String buildTags = Build.TAGS;
        if ((!isEmulator && buildTags != null && buildTags.contains("test-keys")) || new File("/system/app/Superuser.apk").exists()) {
            return true;
        }
        File file = new File("/system/xbin/su");
        if (isEmulator || !file.exists()) {
            return false;
        }
        return true;
    }

    public static boolean isDebuggerAttached() {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger();
    }

    public static int getDeviceState(Context context) {
        int deviceState = 0;
        if (isEmulator(context)) {
            deviceState = 0 | 1;
        }
        if (isRooted(context)) {
            deviceState |= 2;
        }
        if (isDebuggerAttached()) {
            return deviceState | 4;
        }
        return deviceState;
    }

    public static String hexify(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 255;
            char[] cArr = HEX_VALUES;
            hexChars[i * 2] = cArr[v >>> 4];
            hexChars[(i * 2) + 1] = cArr[v & 15];
        }
        return new String(hexChars);
    }

    public static boolean isAppDebuggable(Context context) {
        return (context.getApplicationInfo().flags & 2) != 0;
    }

    public static String getStringsFileValue(Context context, String key) {
        int id = getResourcesIdentifier(context, key, "string");
        if (id > 0) {
            return context.getString(id);
        }
        return "";
    }

    public static void closeOrLog(Closeable c, String message) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                Logger.getLogger().e(message, e);
            }
        }
    }

    public static String padWithZerosToMaxIntWidth(int value) {
        if (value >= 0) {
            return String.format(Locale.US, "%1$10s", Integer.valueOf(value)).replace(' ', '0');
        }
        throw new IllegalArgumentException("value must be zero or greater");
    }

    /* JADX INFO: Multiple debug info for r1v1 java.lang.String: [D('e' android.content.res.Resources$NotFoundException), D('resourcePackageName' java.lang.String)] */
    public static String getResourcePackageName(Context context) {
        int iconId = context.getApplicationContext().getApplicationInfo().icon;
        if (iconId <= 0) {
            return context.getPackageName();
        }
        try {
            String resourcePackageName = context.getResources().getResourcePackageName(iconId);
            if ("android".equals(resourcePackageName)) {
                return context.getPackageName();
            }
            return resourcePackageName;
        } catch (Resources.NotFoundException e) {
            return context.getPackageName();
        }
    }

    public static String getMappingFileId(Context context) {
        int id = getResourcesIdentifier(context, MAPPING_FILE_ID_RESOURCE_NAME, "string");
        if (id == 0) {
            id = getResourcesIdentifier(context, LEGACY_MAPPING_FILE_ID_RESOURCE_NAME, "string");
        }
        if (id != 0) {
            return context.getResources().getString(id);
        }
        return null;
    }

    public static String resolveUnityEditorVersion(Context context) {
        int id = getResourcesIdentifier(context, UNITY_EDITOR_VERSION, "string");
        if (id == 0) {
            return null;
        }
        String version = context.getResources().getString(id);
        Logger logger = Logger.getLogger();
        logger.v("Unity Editor version is: " + version);
        return version;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    public static boolean canTryConnection(Context context) {
        if (!checkPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
            return true;
        }
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }
}
