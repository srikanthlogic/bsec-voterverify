package com.facebook.soloader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import dalvik.system.BaseDexClassLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
/* loaded from: classes.dex */
public class SoLoader {
    static final boolean DEBUG;
    public static final int SOLOADER_ALLOW_ASYNC_INIT;
    public static final int SOLOADER_DISABLE_BACKUP_SOSOURCE;
    public static final int SOLOADER_ENABLE_EXOPACKAGE;
    public static final int SOLOADER_LOOK_IN_ZIP;
    private static final String SO_STORE_NAME_MAIN;
    private static final String SO_STORE_NAME_SPLIT;
    static final boolean SYSTRACE_LIBRARY_LOADING;
    static final String TAG;
    @Nullable
    private static ApplicationSoSource sApplicationSoSource;
    @Nullable
    private static UnpackingSoSource[] sBackupSoSources;
    private static int sFlags;
    @Nullable
    static SoFileLoader sSoFileLoader;
    private static final ReentrantReadWriteLock sSoSourcesLock = new ReentrantReadWriteLock();
    @Nullable
    private static SoSource[] sSoSources = null;
    private static int sSoSourcesVersion = 0;
    private static final HashSet<String> sLoadedLibraries = new HashSet<>();
    private static final Map<String, Object> sLoadingLibraries = new HashMap();
    private static final Set<String> sLoadedAndMergedLibraries = Collections.newSetFromMap(new ConcurrentHashMap());
    @Nullable
    private static SystemLoadLibraryWrapper sSystemLoadLibraryWrapper = null;

    static {
        boolean z = false;
        boolean shouldSystrace = false;
        try {
            if (Build.VERSION.SDK_INT >= 18) {
                z = true;
            }
            shouldSystrace = z;
        } catch (NoClassDefFoundError e) {
        } catch (UnsatisfiedLinkError e2) {
        }
        SYSTRACE_LIBRARY_LOADING = shouldSystrace;
    }

    public static void init(Context context, int flags) throws IOException {
        init(context, flags, null);
    }

    private static void init(Context context, int flags, @Nullable SoFileLoader soFileLoader) throws IOException {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            initSoLoader(soFileLoader);
            initSoSources(context, flags, soFileLoader);
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public static void init(Context context, boolean nativeExopackage) {
        try {
            init(context, nativeExopackage ? 1 : 0);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void initSoSources(Context context, int flags, @Nullable SoFileLoader soFileLoader) throws IOException {
        int apkSoSourceFlags;
        sSoSourcesLock.writeLock().lock();
        try {
            if (sSoSources == null) {
                Log.d(TAG, "init start");
                sFlags = flags;
                ArrayList<SoSource> soSources = new ArrayList<>();
                String LD_LIBRARY_PATH = System.getenv("LD_LIBRARY_PATH");
                if (LD_LIBRARY_PATH == null) {
                    LD_LIBRARY_PATH = "/vendor/lib:/system/lib";
                }
                String[] systemLibraryDirectories = LD_LIBRARY_PATH.split(":");
                for (int i = 0; i < systemLibraryDirectories.length; i++) {
                    Log.d(TAG, "adding system library source: " + systemLibraryDirectories[i]);
                    soSources.add(new DirectorySoSource(new File(systemLibraryDirectories[i]), 2));
                }
                if (context != null) {
                    if ((flags & 1) != 0) {
                        sBackupSoSources = null;
                        Log.d(TAG, "adding exo package source: lib-main");
                        soSources.add(0, new ExoSoSource(context, SO_STORE_NAME_MAIN));
                    } else {
                        ApplicationInfo applicationInfo = context.getApplicationInfo();
                        if ((applicationInfo.flags & 1) != 0 && (applicationInfo.flags & 128) == 0) {
                            apkSoSourceFlags = 0;
                        } else {
                            apkSoSourceFlags = 1;
                            int ourSoSourceFlags = 0;
                            if (Build.VERSION.SDK_INT <= 17) {
                                ourSoSourceFlags = 0 | 1;
                            }
                            sApplicationSoSource = new ApplicationSoSource(context, ourSoSourceFlags);
                            Log.d(TAG, "adding application source: " + sApplicationSoSource.toString());
                            soSources.add(0, sApplicationSoSource);
                        }
                        if ((sFlags & 8) != 0) {
                            sBackupSoSources = null;
                        } else {
                            File mainApkDir = new File(context.getApplicationInfo().sourceDir);
                            ArrayList<UnpackingSoSource> backupSources = new ArrayList<>();
                            ApkSoSource mainApkSource = new ApkSoSource(context, mainApkDir, SO_STORE_NAME_MAIN, apkSoSourceFlags);
                            backupSources.add(mainApkSource);
                            Log.d(TAG, "adding backup source from : " + mainApkSource.toString());
                            if (Build.VERSION.SDK_INT >= 21 && context.getApplicationInfo().splitSourceDirs != null) {
                                Log.d(TAG, "adding backup sources from split apks");
                                String[] strArr = context.getApplicationInfo().splitSourceDirs;
                                int length = strArr.length;
                                int splitIndex = 0;
                                int splitIndex2 = 0;
                                while (splitIndex < length) {
                                    ApkSoSource splitApkSource = new ApkSoSource(context, new File(strArr[splitIndex]), SO_STORE_NAME_SPLIT + splitIndex2, apkSoSourceFlags);
                                    Log.d(TAG, "adding backup source: " + splitApkSource.toString());
                                    backupSources.add(splitApkSource);
                                    splitIndex++;
                                    splitIndex2++;
                                    LD_LIBRARY_PATH = LD_LIBRARY_PATH;
                                    systemLibraryDirectories = systemLibraryDirectories;
                                    length = length;
                                    applicationInfo = applicationInfo;
                                }
                            }
                            sBackupSoSources = (UnpackingSoSource[]) backupSources.toArray(new UnpackingSoSource[backupSources.size()]);
                            soSources.addAll(0, backupSources);
                        }
                    }
                }
                SoSource[] finalSoSources = (SoSource[]) soSources.toArray(new SoSource[soSources.size()]);
                int prepareFlags = makePrepareFlags();
                int i2 = finalSoSources.length;
                while (true) {
                    int i3 = i2 - 1;
                    if (i2 <= 0) {
                        break;
                    }
                    Log.d(TAG, "Preparing SO source: " + finalSoSources[i3]);
                    finalSoSources[i3].prepare(prepareFlags);
                    i2 = i3;
                }
                sSoSources = finalSoSources;
                sSoSourcesVersion++;
                Log.d(TAG, "init finish: " + sSoSources.length + " SO sources prepared");
            }
        } finally {
            Log.d(TAG, "init exiting");
            sSoSourcesLock.writeLock().unlock();
        }
    }

    private static int makePrepareFlags() {
        int prepareFlags = 0;
        sSoSourcesLock.writeLock().lock();
        try {
            if ((sFlags & 2) != 0) {
                prepareFlags = 0 | 1;
            }
            return prepareFlags;
        } finally {
            sSoSourcesLock.writeLock().unlock();
        }
    }

    private static synchronized void initSoLoader(@Nullable SoFileLoader soFileLoader) {
        synchronized (SoLoader.class) {
            if (soFileLoader != null) {
                sSoFileLoader = soFileLoader;
                return;
            }
            final Runtime runtime = Runtime.getRuntime();
            final Method nativeLoadRuntimeMethod = getNativeLoadRuntimeMethod();
            final boolean hasNativeLoadMethod = nativeLoadRuntimeMethod != null;
            final String localLdLibraryPath = hasNativeLoadMethod ? Api14Utils.getClassLoaderLdLoadLibrary() : null;
            final String localLdLibraryPathNoZips = makeNonZipPath(localLdLibraryPath);
            sSoFileLoader = new SoFileLoader() { // from class: com.facebook.soloader.SoLoader.1
                @Override // com.facebook.soloader.SoFileLoader
                public void load(String pathToSoFile, int loadFlags) {
                    String error = null;
                    if (hasNativeLoadMethod) {
                        String path = (loadFlags & 4) == 4 ? localLdLibraryPath : localLdLibraryPathNoZips;
                        try {
                            try {
                                synchronized (runtime) {
                                    error = (String) nativeLoadRuntimeMethod.invoke(runtime, pathToSoFile, SoLoader.class.getClassLoader(), path);
                                    if (error != null) {
                                        throw new UnsatisfiedLinkError(error);
                                    }
                                }
                            } finally {
                                if (error != null) {
                                    Log.e(SoLoader.TAG, "Error when loading lib: " + error + " lib hash: " + getLibHash(pathToSoFile) + " search path is " + path);
                                }
                            }
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            throw new RuntimeException("Error: Cannot load " + pathToSoFile, e);
                        }
                    } else {
                        System.load(pathToSoFile);
                    }
                }

                private String getLibHash(String libPath) {
                    try {
                        File libFile = new File(libPath);
                        MessageDigest digest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
                        InputStream libInStream = new FileInputStream(libFile);
                        try {
                            byte[] buffer = new byte[4096];
                            while (true) {
                                int bytesRead = libInStream.read(buffer);
                                if (bytesRead > 0) {
                                    digest.update(buffer, 0, bytesRead);
                                } else {
                                    String digestStr = String.format("%32x", new BigInteger(1, digest.digest()));
                                    libInStream.close();
                                    return digestStr;
                                }
                            }
                        } catch (Throwable th) {
                            try {
                                throw th;
                            } catch (Throwable th2) {
                                try {
                                    libInStream.close();
                                } catch (Throwable th3) {
                                    th.addSuppressed(th3);
                                }
                                throw th2;
                            }
                        }
                    } catch (IOException e) {
                        return e.toString();
                    } catch (SecurityException e2) {
                        return e2.toString();
                    } catch (NoSuchAlgorithmException e3) {
                        return e3.toString();
                    }
                }
            };
        }
    }

    @Nullable
    private static Method getNativeLoadRuntimeMethod() {
        if (Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT > 27) {
            return null;
        }
        try {
            Method method = Runtime.class.getDeclaredMethod("nativeLoad", String.class, ClassLoader.class, String.class);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException | SecurityException e) {
            Log.w(TAG, "Cannot get nativeLoad method", e);
            return null;
        }
    }

    public static void setInTestMode() {
        setSoSources(new SoSource[]{new NoopSoSource()});
    }

    public static void deinitForTest() {
        setSoSources(null);
    }

    static void setSoSources(SoSource[] sources) {
        sSoSourcesLock.writeLock().lock();
        try {
            sSoSources = sources;
            sSoSourcesVersion++;
        } finally {
            sSoSourcesLock.writeLock().unlock();
        }
    }

    static void setSoFileLoader(SoFileLoader loader) {
        sSoFileLoader = loader;
    }

    static void resetStatus() {
        synchronized (SoLoader.class) {
            sLoadedLibraries.clear();
            sLoadingLibraries.clear();
            sSoFileLoader = null;
        }
        setSoSources(null);
    }

    public static void setSystemLoadLibraryWrapper(SystemLoadLibraryWrapper wrapper) {
        sSystemLoadLibraryWrapper = wrapper;
    }

    /* loaded from: classes.dex */
    public static final class WrongAbiError extends UnsatisfiedLinkError {
        WrongAbiError(Throwable cause) {
            super("APK was built for a different platform");
            initCause(cause);
        }
    }

    public static boolean loadLibrary(String shortName) {
        return loadLibrary(shortName, 0);
    }

    public static boolean loadLibrary(String shortName, int loadFlags) throws UnsatisfiedLinkError {
        boolean needsLoad;
        sSoSourcesLock.readLock().lock();
        try {
            if (sSoSources == null) {
                if ("http://www.android.com/".equals(System.getProperty("java.vendor.url"))) {
                    assertInitialized();
                } else {
                    synchronized (SoLoader.class) {
                        needsLoad = !sLoadedLibraries.contains(shortName);
                        if (needsLoad) {
                            if (sSystemLoadLibraryWrapper != null) {
                                sSystemLoadLibraryWrapper.loadLibrary(shortName);
                            } else {
                                System.loadLibrary(shortName);
                            }
                        }
                    }
                    return needsLoad;
                }
            }
            sSoSourcesLock.readLock().unlock();
            String mergedLibName = MergedSoMapping.mapLibName(shortName);
            return loadLibraryBySoName(System.mapLibraryName(mergedLibName != null ? mergedLibName : shortName), shortName, mergedLibName, loadFlags | 2, null);
        } finally {
            sSoSourcesLock.readLock().unlock();
        }
    }

    public static void loadLibraryBySoName(String soName, int loadFlags, StrictMode.ThreadPolicy oldPolicy) {
        loadLibraryBySoName(soName, null, null, loadFlags, oldPolicy);
    }

    private static boolean loadLibraryBySoName(String soName, @Nullable String shortName, @Nullable String mergedLibName, int loadFlags, @Nullable StrictMode.ThreadPolicy oldPolicy) {
        Object loadingLibLock;
        boolean isAlreadyMerged = false;
        if (!TextUtils.isEmpty(shortName) && sLoadedAndMergedLibraries.contains(shortName)) {
            return false;
        }
        boolean loaded = false;
        synchronized (SoLoader.class) {
            if (sLoadedLibraries.contains(soName)) {
                if (mergedLibName == null) {
                    return false;
                }
                loaded = true;
            }
            if (sLoadingLibraries.containsKey(soName)) {
                loadingLibLock = sLoadingLibraries.get(soName);
            } else {
                loadingLibLock = new Object();
                sLoadingLibraries.put(soName, loadingLibLock);
            }
            synchronized (loadingLibLock) {
                if (!loaded) {
                    synchronized (SoLoader.class) {
                        if (sLoadedLibraries.contains(soName)) {
                            if (mergedLibName == null) {
                                return false;
                            }
                            loaded = true;
                        }
                        if (!loaded) {
                            try {
                                Log.d(TAG, "About to load: " + soName);
                                doLoadLibraryBySoName(soName, loadFlags, oldPolicy);
                                synchronized (SoLoader.class) {
                                    Log.d(TAG, "Loaded: " + soName);
                                    sLoadedLibraries.add(soName);
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (UnsatisfiedLinkError ex2) {
                                String message = ex2.getMessage();
                                if (message == null || !message.contains("unexpected e_machine:")) {
                                    throw ex2;
                                }
                                throw new WrongAbiError(ex2);
                            }
                        }
                    }
                }
                if (!TextUtils.isEmpty(shortName) && sLoadedAndMergedLibraries.contains(shortName)) {
                    isAlreadyMerged = true;
                }
                if (mergedLibName != null && !isAlreadyMerged) {
                    if (SYSTRACE_LIBRARY_LOADING) {
                        Api18TraceUtils.beginTraceSection("MergedSoMapping.invokeJniOnload[" + shortName + "]");
                    }
                    Log.d(TAG, "About to merge: " + shortName + " / " + soName);
                    MergedSoMapping.invokeJniOnload(shortName);
                    sLoadedAndMergedLibraries.add(shortName);
                    if (SYSTRACE_LIBRARY_LOADING) {
                        Api18TraceUtils.endSection();
                    }
                }
                return !loaded;
            }
        }
    }

    public static File unpackLibraryAndDependencies(String shortName) throws UnsatisfiedLinkError {
        assertInitialized();
        try {
            return unpackLibraryBySoName(System.mapLibraryName(shortName));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /* JADX INFO: Multiple debug info for r18v6 'retry'  boolean: [D('retry' boolean), D('resultFromBackup' int)] */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0116  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static void doLoadLibraryBySoName(String soName, int loadFlags, StrictMode.ThreadPolicy oldPolicy) throws IOException {
        boolean restoreOldPolicy;
        StrictMode.ThreadPolicy oldPolicy2;
        UnsatisfiedLinkError unsatisfiedLinkError;
        int i;
        Throwable th;
        int result;
        boolean retry;
        boolean retry2;
        int result2 = 0;
        sSoSourcesLock.readLock().lock();
        try {
            if (sSoSources != null) {
                sSoSourcesLock.readLock().unlock();
                if (oldPolicy == null) {
                    oldPolicy2 = StrictMode.allowThreadDiskReads();
                    restoreOldPolicy = true;
                } else {
                    oldPolicy2 = oldPolicy;
                    restoreOldPolicy = false;
                }
                if (SYSTRACE_LIBRARY_LOADING) {
                    Api18TraceUtils.beginTraceSection("SoLoader.loadLibrary[" + soName + "]");
                }
                Throwable error = null;
                do {
                    boolean retry3 = false;
                    int i2 = 3;
                    try {
                        sSoSourcesLock.readLock().lock();
                        int currentSoSourcesVersion = sSoSourcesVersion;
                        int i3 = 0;
                        while (result2 == 0) {
                            try {
                                if (i3 >= sSoSources.length) {
                                    break;
                                }
                                result2 = sSoSources[i3].loadLibrary(soName, loadFlags, oldPolicy2);
                                if (result2 == i2) {
                                    try {
                                        if (sBackupSoSources != null) {
                                            Log.d(TAG, "Trying backup SoSource for " + soName);
                                            UnpackingSoSource[] unpackingSoSourceArr = sBackupSoSources;
                                            int length = unpackingSoSourceArr.length;
                                            int i4 = 0;
                                            while (true) {
                                                if (i4 >= length) {
                                                    result = result2;
                                                    retry = retry3;
                                                    break;
                                                }
                                                UnpackingSoSource backupSoSource = unpackingSoSourceArr[i4];
                                                try {
                                                    backupSoSource.prepare(soName);
                                                    int resultFromBackup = backupSoSource.loadLibrary(soName, loadFlags, oldPolicy2);
                                                    retry = retry3;
                                                    if (resultFromBackup == 1) {
                                                        result = resultFromBackup;
                                                        break;
                                                    }
                                                    i4++;
                                                    result2 = result2;
                                                    retry3 = retry;
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    result2 = result2;
                                                    sSoSourcesLock.readLock().unlock();
                                                    throw th;
                                                }
                                            }
                                            result2 = result;
                                            sSoSourcesLock.readLock().unlock();
                                            if ((loadFlags & 2) == 2 && result2 == 0) {
                                                sSoSourcesLock.writeLock().lock();
                                                if (sApplicationSoSource != null && sApplicationSoSource.checkAndMaybeUpdate()) {
                                                    sSoSourcesVersion++;
                                                }
                                                if (sSoSourcesVersion == currentSoSourcesVersion) {
                                                    retry2 = true;
                                                } else {
                                                    retry2 = retry;
                                                }
                                                sSoSourcesLock.writeLock().unlock();
                                                retry = retry2;
                                                continue;
                                            }
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                    }
                                }
                                i3++;
                                result2 = result2;
                                retry3 = retry3;
                                i2 = 3;
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        }
                        retry = retry3;
                        sSoSourcesLock.readLock().unlock();
                        if ((loadFlags & 2) == 2) {
                            sSoSourcesLock.writeLock().lock();
                            if (sApplicationSoSource != null) {
                                sSoSourcesVersion++;
                            }
                            if (sSoSourcesVersion == currentSoSourcesVersion) {
                            }
                            sSoSourcesLock.writeLock().unlock();
                            retry = retry2;
                            continue;
                        }
                    } finally {
                        if (result2 == 0 || result2 == i) {
                        }
                    }
                } while (retry);
                if (SYSTRACE_LIBRARY_LOADING) {
                    Api18TraceUtils.endSection();
                }
                if (restoreOldPolicy) {
                    StrictMode.setThreadPolicy(oldPolicy2);
                }
                if (result2 == 0 || result2 == 3) {
                    String message = "couldn't find DSO to load: " + soName;
                    if (0 != 0) {
                        String cause = error.getMessage();
                        if (cause == null) {
                            cause = error.toString();
                        }
                        message = message + " caused by: " + cause;
                    }
                    Log.e(TAG, message);
                    throw new UnsatisfiedLinkError(message);
                }
                return;
            }
            Log.e(TAG, "Could not load: " + soName + " because no SO source exists");
            throw new UnsatisfiedLinkError("couldn't find DSO to load: " + soName);
        } catch (Throwable th5) {
            sSoSourcesLock.readLock().unlock();
            throw th5;
        }
    }

    @Nullable
    public static String makeNonZipPath(String localLdLibraryPath) {
        if (localLdLibraryPath == null) {
            return null;
        }
        String[] paths = localLdLibraryPath.split(":");
        ArrayList<String> pathsWithoutZip = new ArrayList<>(paths.length);
        for (String path : paths) {
            if (!path.contains("!")) {
                pathsWithoutZip.add(path);
            }
        }
        return TextUtils.join(":", pathsWithoutZip);
    }

    static File unpackLibraryBySoName(String soName) throws IOException {
        sSoSourcesLock.readLock().lock();
        for (int i = 0; i < sSoSources.length; i++) {
            try {
                File unpacked = sSoSources[i].unpackLibrary(soName);
                if (unpacked != null) {
                    return unpacked;
                }
            } finally {
                sSoSourcesLock.readLock().unlock();
            }
        }
        sSoSourcesLock.readLock().unlock();
        throw new FileNotFoundException(soName);
    }

    private static void assertInitialized() {
        sSoSourcesLock.readLock().lock();
        try {
            if (sSoSources == null) {
                throw new RuntimeException("SoLoader.init() not yet called");
            }
        } finally {
            sSoSourcesLock.readLock().unlock();
        }
    }

    public static void prependSoSource(SoSource extraSoSource) throws IOException {
        sSoSourcesLock.writeLock().lock();
        try {
            Log.d(TAG, "Prepending to SO sources: " + extraSoSource);
            assertInitialized();
            extraSoSource.prepare(makePrepareFlags());
            SoSource[] newSoSources = new SoSource[sSoSources.length + 1];
            newSoSources[0] = extraSoSource;
            System.arraycopy(sSoSources, 0, newSoSources, 1, sSoSources.length);
            sSoSources = newSoSources;
            sSoSourcesVersion++;
            Log.d(TAG, "Prepended to SO sources: " + extraSoSource);
        } finally {
            sSoSourcesLock.writeLock().unlock();
        }
    }

    public static String makeLdLibraryPath() {
        sSoSourcesLock.readLock().lock();
        try {
            assertInitialized();
            Log.d(TAG, "makeLdLibraryPath");
            ArrayList<String> pathElements = new ArrayList<>();
            for (SoSource soSource : sSoSources) {
                soSource.addToLdLibraryPath(pathElements);
            }
            String joinedPaths = TextUtils.join(":", pathElements);
            Log.d(TAG, "makeLdLibraryPath final path: " + joinedPaths);
            return joinedPaths;
        } finally {
            sSoSourcesLock.readLock().unlock();
        }
    }

    public static boolean areSoSourcesAbisSupported() {
        sSoSourcesLock.readLock().lock();
        try {
            if (sSoSources == null) {
                return false;
            }
            String[] supportedAbis = SysUtil.getSupportedAbis();
            for (int i = 0; i < sSoSources.length; i++) {
                String[] soSourceAbis = sSoSources[i].getSoSourceAbis();
                for (int j = 0; j < soSourceAbis.length; j++) {
                    boolean soSourceSupported = false;
                    for (int k = 0; k < supportedAbis.length && !soSourceSupported; k++) {
                        soSourceSupported = soSourceAbis[j].equals(supportedAbis[k]);
                    }
                    if (!soSourceSupported) {
                        Log.e(TAG, "abi not supported: " + soSourceAbis[j]);
                        return false;
                    }
                }
            }
            sSoSourcesLock.readLock().unlock();
            return true;
        } finally {
            sSoSourcesLock.readLock().unlock();
        }
    }

    /* loaded from: classes.dex */
    public static class Api14Utils {
        private Api14Utils() {
        }

        public static String getClassLoaderLdLoadLibrary() {
            ClassLoader classLoader = SoLoader.class.getClassLoader();
            if (classLoader instanceof BaseDexClassLoader) {
                try {
                    return (String) BaseDexClassLoader.class.getMethod("getLdLibraryPath", new Class[0]).invoke((BaseDexClassLoader) classLoader, new Object[0]);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot call getLdLibraryPath", e);
                }
            } else {
                throw new IllegalStateException("ClassLoader " + classLoader.getClass().getName() + " should be of type BaseDexClassLoader");
            }
        }
    }
}
