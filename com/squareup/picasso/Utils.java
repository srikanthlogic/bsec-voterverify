package com.squareup.picasso;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import okio.BufferedSource;
import okio.ByteString;
/* loaded from: classes3.dex */
public final class Utils {
    private static final int KEY_PADDING;
    static final char KEY_SEPARATOR;
    private static final int MAX_DISK_CACHE_SIZE;
    private static final int MIN_DISK_CACHE_SIZE;
    static final String OWNER_DISPATCHER;
    static final String OWNER_HUNTER;
    static final String OWNER_MAIN;
    private static final String PICASSO_CACHE;
    static final String THREAD_IDLE_NAME;
    static final int THREAD_LEAK_CLEANING_MS;
    static final String THREAD_PREFIX;
    static final String VERB_BATCHED;
    static final String VERB_CANCELED;
    static final String VERB_CHANGED;
    static final String VERB_COMPLETED;
    static final String VERB_CREATED;
    static final String VERB_DECODED;
    static final String VERB_DELIVERED;
    static final String VERB_ENQUEUED;
    static final String VERB_ERRORED;
    static final String VERB_EXECUTING;
    static final String VERB_IGNORED;
    static final String VERB_JOINED;
    static final String VERB_PAUSED;
    static final String VERB_REMOVED;
    static final String VERB_REPLAYING;
    static final String VERB_RESUMED;
    static final String VERB_RETRYING;
    static final String VERB_TRANSFORMED;
    static final StringBuilder MAIN_THREAD_KEY_BUILDER = new StringBuilder();
    private static final ByteString WEBP_FILE_HEADER_RIFF = ByteString.encodeUtf8("RIFF");
    private static final ByteString WEBP_FILE_HEADER_WEBP = ByteString.encodeUtf8("WEBP");

    private Utils() {
    }

    public static int getBitmapBytes(Bitmap bitmap) {
        int result = Build.VERSION.SDK_INT >= 19 ? bitmap.getAllocationByteCount() : bitmap.getByteCount();
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("Negative size: " + bitmap);
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value != null) {
            return value;
        }
        throw new NullPointerException(message);
    }

    public static void checkNotMain() {
        if (isMain()) {
            throw new IllegalStateException("Method call should not happen from the main thread.");
        }
    }

    public static void checkMain() {
        if (!isMain()) {
            throw new IllegalStateException("Method call should happen from the main thread.");
        }
    }

    static boolean isMain() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static String getLogIdsForHunter(BitmapHunter hunter) {
        return getLogIdsForHunter(hunter, "");
    }

    public static String getLogIdsForHunter(BitmapHunter hunter, String prefix) {
        StringBuilder builder = new StringBuilder(prefix);
        Action action = hunter.getAction();
        if (action != null) {
            builder.append(action.request.logId());
        }
        List<Action> actions = hunter.getActions();
        if (actions != null) {
            int count = actions.size();
            for (int i = 0; i < count; i++) {
                if (i > 0 || action != null) {
                    builder.append(", ");
                }
                builder.append(actions.get(i).request.logId());
            }
        }
        return builder.toString();
    }

    public static void log(String owner, String verb, String logId) {
        log(owner, verb, logId, "");
    }

    public static void log(String owner, String verb, String logId, String extras) {
        Log.d("Picasso", String.format("%1$-11s %2$-12s %3$s %4$s", owner, verb, logId, extras));
    }

    public static String createKey(Request data) {
        String result = createKey(data, MAIN_THREAD_KEY_BUILDER);
        MAIN_THREAD_KEY_BUILDER.setLength(0);
        return result;
    }

    public static String createKey(Request data, StringBuilder builder) {
        if (data.stableKey != null) {
            builder.ensureCapacity(data.stableKey.length() + 50);
            builder.append(data.stableKey);
        } else if (data.uri != null) {
            String path = data.uri.toString();
            builder.ensureCapacity(path.length() + 50);
            builder.append(path);
        } else {
            builder.ensureCapacity(50);
            builder.append(data.resourceId);
        }
        builder.append(KEY_SEPARATOR);
        if (data.rotationDegrees != 0.0f) {
            builder.append("rotation:");
            builder.append(data.rotationDegrees);
            if (data.hasRotationPivot) {
                builder.append('@');
                builder.append(data.rotationPivotX);
                builder.append('x');
                builder.append(data.rotationPivotY);
            }
            builder.append(KEY_SEPARATOR);
        }
        if (data.hasSize()) {
            builder.append("resize:");
            builder.append(data.targetWidth);
            builder.append('x');
            builder.append(data.targetHeight);
            builder.append(KEY_SEPARATOR);
        }
        if (data.centerCrop) {
            builder.append("centerCrop:");
            builder.append(data.centerCropGravity);
            builder.append(KEY_SEPARATOR);
        } else if (data.centerInside) {
            builder.append("centerInside");
            builder.append(KEY_SEPARATOR);
        }
        if (data.transformations != null) {
            int count = data.transformations.size();
            for (int i = 0; i < count; i++) {
                builder.append(data.transformations.get(i).key());
                builder.append(KEY_SEPARATOR);
            }
        }
        return builder.toString();
    }

    public static File createDefaultCacheDir(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), PICASSO_CACHE);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    public static long calculateDiskCacheSize(File dir) {
        long size = 5242880;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            size = ((Build.VERSION.SDK_INT < 18 ? (long) statFs.getBlockCount() : statFs.getBlockCountLong()) * (Build.VERSION.SDK_INT < 18 ? (long) statFs.getBlockSize() : statFs.getBlockSizeLong())) / 50;
        } catch (IllegalArgumentException e) {
        }
        return Math.max(Math.min(size, 52428800L), 5242880L);
    }

    public static int calculateMemoryCacheSize(Context context) {
        ActivityManager am = (ActivityManager) getService(context, "activity");
        return (int) ((((long) ((context.getApplicationInfo().flags & 1048576) != 0 ? am.getLargeMemoryClass() : am.getMemoryClass())) * 1048576) / 7);
    }

    public static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            return Build.VERSION.SDK_INT < 17 ? Settings.System.getInt(contentResolver, "airplane_mode_on", 0) != 0 : Settings.Global.getInt(contentResolver, "airplane_mode_on", 0) != 0;
        } catch (NullPointerException e) {
            return false;
        } catch (SecurityException e2) {
            return false;
        }
    }

    public static <T> T getService(Context context, String service) {
        return (T) context.getSystemService(service);
    }

    public static boolean hasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    public static boolean isWebPFile(BufferedSource source) throws IOException {
        return source.rangeEquals(0, WEBP_FILE_HEADER_RIFF) && source.rangeEquals(8, WEBP_FILE_HEADER_WEBP);
    }

    /* JADX INFO: Multiple debug info for r2v13 int: [D('id' int), D('type' java.lang.String)] */
    public static int getResourceId(Resources resources, Request data) throws FileNotFoundException {
        if (data.resourceId != 0 || data.uri == null) {
            return data.resourceId;
        }
        String pkg = data.uri.getAuthority();
        if (pkg != null) {
            List<String> segments = data.uri.getPathSegments();
            if (segments == null || segments.isEmpty()) {
                throw new FileNotFoundException("No path segments: " + data.uri);
            } else if (segments.size() == 1) {
                try {
                    return Integer.parseInt(segments.get(0));
                } catch (NumberFormatException e) {
                    throw new FileNotFoundException("Last path segment is not a resource ID: " + data.uri);
                }
            } else if (segments.size() == 2) {
                return resources.getIdentifier(segments.get(1), segments.get(0), pkg);
            } else {
                throw new FileNotFoundException("More than two path segments: " + data.uri);
            }
        } else {
            throw new FileNotFoundException("No package provided: " + data.uri);
        }
    }

    public static Resources getResources(Context context, Request data) throws FileNotFoundException {
        if (data.resourceId != 0 || data.uri == null) {
            return context.getResources();
        }
        String pkg = data.uri.getAuthority();
        if (pkg != null) {
            try {
                return context.getPackageManager().getResourcesForApplication(pkg);
            } catch (PackageManager.NameNotFoundException e) {
                throw new FileNotFoundException("Unable to obtain resources for package: " + data.uri);
            }
        } else {
            throw new FileNotFoundException("No package provided: " + data.uri);
        }
    }

    public static void flushStackLocalLeaks(Looper looper) {
        Handler handler = new Handler(looper) { // from class: com.squareup.picasso.Utils.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                sendMessageDelayed(obtainMessage(), 1000);
            }
        };
        handler.sendMessageDelayed(handler.obtainMessage(), 1000);
    }

    /* loaded from: classes3.dex */
    public static class PicassoThreadFactory implements ThreadFactory {
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            return new PicassoThread(r);
        }
    }

    /* loaded from: classes3.dex */
    private static class PicassoThread extends Thread {
        PicassoThread(Runnable r) {
            super(r);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Process.setThreadPriority(10);
            super.run();
        }
    }
}
