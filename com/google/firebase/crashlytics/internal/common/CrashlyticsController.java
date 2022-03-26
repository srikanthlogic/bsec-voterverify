package com.google.firebase.crashlytics.internal.common;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.crashlytics.internal.CrashlyticsNativeComponent;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.NativeSessionFileProvider;
import com.google.firebase.crashlytics.internal.analytics.AnalyticsEventLogger;
import com.google.firebase.crashlytics.internal.common.CrashlyticsUncaughtExceptionHandler;
import com.google.firebase.crashlytics.internal.log.LogFileManager;
import com.google.firebase.crashlytics.internal.model.StaticSessionData;
import com.google.firebase.crashlytics.internal.persistence.FileStore;
import com.google.firebase.crashlytics.internal.settings.SettingsDataProvider;
import com.google.firebase.crashlytics.internal.settings.model.AppSettingsData;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes3.dex */
public class CrashlyticsController {
    static final FilenameFilter APP_EXCEPTION_MARKER_FILTER = $$Lambda$CrashlyticsController$S4ND4vdF1CBimHR5Yfiv04HIz8k.INSTANCE;
    static final String APP_EXCEPTION_MARKER_PREFIX;
    static final String FIREBASE_APPLICATION_EXCEPTION;
    static final String FIREBASE_CRASH_TYPE;
    static final int FIREBASE_CRASH_TYPE_FATAL;
    static final String FIREBASE_TIMESTAMP;
    private static final String GENERATOR_FORMAT;
    static final String NATIVE_SESSION_DIR;
    private final AnalyticsEventLogger analyticsEventLogger;
    private final AppData appData;
    private final CrashlyticsBackgroundWorker backgroundWorker;
    private final Context context;
    private CrashlyticsUncaughtExceptionHandler crashHandler;
    private final CrashlyticsFileMarker crashMarker;
    private final DataCollectionArbiter dataCollectionArbiter;
    private final FileStore fileStore;
    private final IdManager idManager;
    private final LogFileManager.DirectoryProvider logFileDirectoryProvider;
    private final LogFileManager logFileManager;
    private final CrashlyticsNativeComponent nativeComponent;
    private final SessionReportingCoordinator reportingCoordinator;
    private final String unityVersion;
    private final UserMetadata userMetadata;
    final TaskCompletionSource<Boolean> unsentReportsAvailable = new TaskCompletionSource<>();
    final TaskCompletionSource<Boolean> reportActionProvided = new TaskCompletionSource<>();
    final TaskCompletionSource<Void> unsentReportsHandled = new TaskCompletionSource<>();
    final AtomicBoolean checkForUnsentReportsCalled = new AtomicBoolean(false);

    public CrashlyticsController(Context context, CrashlyticsBackgroundWorker backgroundWorker, IdManager idManager, DataCollectionArbiter dataCollectionArbiter, FileStore fileStore, CrashlyticsFileMarker crashMarker, AppData appData, UserMetadata userMetadata, LogFileManager logFileManager, LogFileManager.DirectoryProvider logFileDirectoryProvider, SessionReportingCoordinator sessionReportingCoordinator, CrashlyticsNativeComponent nativeComponent, AnalyticsEventLogger analyticsEventLogger) {
        this.context = context;
        this.backgroundWorker = backgroundWorker;
        this.idManager = idManager;
        this.dataCollectionArbiter = dataCollectionArbiter;
        this.fileStore = fileStore;
        this.crashMarker = crashMarker;
        this.appData = appData;
        this.userMetadata = userMetadata;
        this.logFileManager = logFileManager;
        this.logFileDirectoryProvider = logFileDirectoryProvider;
        this.nativeComponent = nativeComponent;
        this.unityVersion = appData.unityVersionProvider.getUnityVersion();
        this.analyticsEventLogger = analyticsEventLogger;
        this.reportingCoordinator = sessionReportingCoordinator;
    }

    private Context getContext() {
        return this.context;
    }

    public void enableExceptionHandling(Thread.UncaughtExceptionHandler defaultHandler, SettingsDataProvider settingsProvider) {
        openSession();
        this.crashHandler = new CrashlyticsUncaughtExceptionHandler(new CrashlyticsUncaughtExceptionHandler.CrashListener() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.1
            @Override // com.google.firebase.crashlytics.internal.common.CrashlyticsUncaughtExceptionHandler.CrashListener
            public void onUncaughtException(SettingsDataProvider settingsDataProvider, Thread thread, Throwable ex) {
                CrashlyticsController.this.handleUncaughtException(settingsDataProvider, thread, ex);
            }
        }, settingsProvider, defaultHandler);
        Thread.setDefaultUncaughtExceptionHandler(this.crashHandler);
    }

    synchronized void handleUncaughtException(final SettingsDataProvider settingsDataProvider, final Thread thread, final Throwable ex) {
        Logger logger = Logger.getLogger();
        logger.d("Handling uncaught exception \"" + ex + "\" from thread " + thread.getName());
        final long timestampMillis = System.currentTimeMillis();
        try {
            Utils.awaitEvenIfOnMainThread(this.backgroundWorker.submitTask(new Callable<Task<Void>>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.2
                @Override // java.util.concurrent.Callable
                public Task<Void> call() throws Exception {
                    long timestampSeconds = CrashlyticsController.getTimestampSeconds(timestampMillis);
                    String currentSessionId = CrashlyticsController.this.getCurrentSessionId();
                    if (currentSessionId == null) {
                        Logger.getLogger().e("Tried to write a fatal exception while no session was open.");
                        return Tasks.forResult(null);
                    }
                    CrashlyticsController.this.crashMarker.create();
                    CrashlyticsController.this.reportingCoordinator.persistFatalEvent(ex, thread, currentSessionId, timestampSeconds);
                    CrashlyticsController.this.doWriteAppExceptionMarker(timestampMillis);
                    CrashlyticsController.this.doCloseSessions(settingsDataProvider);
                    CrashlyticsController.this.doOpenSession();
                    if (!CrashlyticsController.this.dataCollectionArbiter.isAutomaticDataCollectionEnabled()) {
                        return Tasks.forResult(null);
                    }
                    final Executor executor = CrashlyticsController.this.backgroundWorker.getExecutor();
                    return settingsDataProvider.getAppSettings().onSuccessTask(executor, new SuccessContinuation<AppSettingsData, Void>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.2.1
                        public Task<Void> then(AppSettingsData appSettingsData) throws Exception {
                            if (appSettingsData != null) {
                                return Tasks.whenAll(CrashlyticsController.this.logAnalyticsAppExceptionEvents(), CrashlyticsController.this.reportingCoordinator.sendReports(executor));
                            }
                            Logger.getLogger().w("Received null app settings, cannot send reports at crash time.");
                            return Tasks.forResult(null);
                        }
                    });
                }
            }));
        } catch (Exception e) {
            Logger.getLogger().e("Error handling uncaught exception", e);
        }
    }

    private Task<Boolean> waitForReportAction() {
        if (this.dataCollectionArbiter.isAutomaticDataCollectionEnabled()) {
            Logger.getLogger().d("Automatic data collection is enabled. Allowing upload.");
            this.unsentReportsAvailable.trySetResult(false);
            return Tasks.forResult(true);
        }
        Logger.getLogger().d("Automatic data collection is disabled.");
        Logger.getLogger().v("Notifying that unsent reports are available.");
        this.unsentReportsAvailable.trySetResult(true);
        Task<TContinuationResult> onSuccessTask = this.dataCollectionArbiter.waitForAutomaticDataCollectionEnabled().onSuccessTask(new SuccessContinuation<Void, Boolean>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.3
            public Task<Boolean> then(Void aVoid) throws Exception {
                return Tasks.forResult(true);
            }
        });
        Logger.getLogger().d("Waiting for send/deleteUnsentReports to be called.");
        return Utils.race(onSuccessTask, this.reportActionProvided.getTask());
    }

    public boolean didCrashOnPreviousExecution() {
        if (!this.crashMarker.isPresent()) {
            String sessionId = getCurrentSessionId();
            return sessionId != null && this.nativeComponent.hasCrashDataForSession(sessionId);
        }
        Logger.getLogger().v("Found previous crash marker.");
        this.crashMarker.remove();
        return Boolean.TRUE.booleanValue();
    }

    public Task<Boolean> checkForUnsentReports() {
        if (this.checkForUnsentReportsCalled.compareAndSet(false, true)) {
            return this.unsentReportsAvailable.getTask();
        }
        Logger.getLogger().w("checkForUnsentReports should only be called once per execution.");
        return Tasks.forResult(false);
    }

    public Task<Void> sendUnsentReports() {
        this.reportActionProvided.trySetResult(true);
        return this.unsentReportsHandled.getTask();
    }

    public Task<Void> deleteUnsentReports() {
        this.reportActionProvided.trySetResult(false);
        return this.unsentReportsHandled.getTask();
    }

    public Task<Void> submitAllReports(final Task<AppSettingsData> appSettingsDataTask) {
        if (!this.reportingCoordinator.hasReportsToSend()) {
            Logger.getLogger().v("No crash reports are available to be sent.");
            this.unsentReportsAvailable.trySetResult(false);
            return Tasks.forResult(null);
        }
        Logger.getLogger().v("Crash reports are available to be sent.");
        return waitForReportAction().onSuccessTask(new SuccessContinuation<Boolean, Void>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.4
            public Task<Void> then(final Boolean send) throws Exception {
                return CrashlyticsController.this.backgroundWorker.submitTask(new Callable<Task<Void>>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.4.1
                    @Override // java.util.concurrent.Callable
                    public Task<Void> call() throws Exception {
                        if (!send.booleanValue()) {
                            Logger.getLogger().v("Deleting cached crash reports...");
                            CrashlyticsController.deleteFiles(CrashlyticsController.this.listAppExceptionMarkerFiles());
                            CrashlyticsController.this.reportingCoordinator.removeAllReports();
                            CrashlyticsController.this.unsentReportsHandled.trySetResult(null);
                            return Tasks.forResult(null);
                        }
                        Logger.getLogger().d("Sending cached crash reports...");
                        CrashlyticsController.this.dataCollectionArbiter.grantDataCollectionPermission(send.booleanValue());
                        final Executor executor = CrashlyticsController.this.backgroundWorker.getExecutor();
                        return appSettingsDataTask.onSuccessTask(executor, new SuccessContinuation<AppSettingsData, Void>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.4.1.1
                            public Task<Void> then(AppSettingsData appSettingsData) throws Exception {
                                if (appSettingsData == null) {
                                    Logger.getLogger().w("Received null app settings at app startup. Cannot send cached reports");
                                    return Tasks.forResult(null);
                                }
                                CrashlyticsController.this.logAnalyticsAppExceptionEvents();
                                CrashlyticsController.this.reportingCoordinator.sendReports(executor);
                                CrashlyticsController.this.unsentReportsHandled.trySetResult(null);
                                return Tasks.forResult(null);
                            }
                        });
                    }
                });
            }
        });
    }

    public void writeToLog(final long timestamp, final String msg) {
        this.backgroundWorker.submit(new Callable<Void>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.5
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                if (CrashlyticsController.this.isHandlingException()) {
                    return null;
                }
                CrashlyticsController.this.logFileManager.writeToLog(timestamp, msg);
                return null;
            }
        });
    }

    public void writeNonFatalException(final Thread thread, final Throwable ex) {
        final long timestampMillis = System.currentTimeMillis();
        this.backgroundWorker.submit(new Runnable() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.6
            @Override // java.lang.Runnable
            public void run() {
                if (!CrashlyticsController.this.isHandlingException()) {
                    long timestampSeconds = CrashlyticsController.getTimestampSeconds(timestampMillis);
                    String currentSessionId = CrashlyticsController.this.getCurrentSessionId();
                    if (currentSessionId == null) {
                        Logger.getLogger().w("Tried to write a non-fatal exception while no session was open.");
                    } else {
                        CrashlyticsController.this.reportingCoordinator.persistNonFatalEvent(ex, thread, currentSessionId, timestampSeconds);
                    }
                }
            }
        });
    }

    public void setUserId(String identifier) {
        this.userMetadata.setUserId(identifier);
        cacheUserData(this.userMetadata);
    }

    public void setCustomKey(String key, String value) {
        try {
            this.userMetadata.setCustomKey(key, value);
            cacheKeyData(this.userMetadata.getCustomKeys(), false);
        } catch (IllegalArgumentException ex) {
            Context context = this.context;
            if (context == null || !CommonUtils.isAppDebuggable(context)) {
                Logger.getLogger().e("Attempting to set custom attribute with null key, ignoring.");
                return;
            }
            throw ex;
        }
    }

    public void setCustomKeys(Map<String, String> keysAndValues) {
        this.userMetadata.setCustomKeys(keysAndValues);
        cacheKeyData(this.userMetadata.getCustomKeys(), false);
    }

    public void setInternalKey(String key, String value) {
        try {
            this.userMetadata.setInternalKey(key, value);
            cacheKeyData(this.userMetadata.getInternalKeys(), true);
        } catch (IllegalArgumentException ex) {
            Context context = this.context;
            if (context == null || !CommonUtils.isAppDebuggable(context)) {
                Logger.getLogger().e("Attempting to set custom attribute with null key, ignoring.");
                return;
            }
            throw ex;
        }
    }

    private void cacheUserData(final UserMetadata userMetaData) {
        this.backgroundWorker.submit(new Callable<Void>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.7
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                String currentSessionId = CrashlyticsController.this.getCurrentSessionId();
                if (currentSessionId == null) {
                    Logger.getLogger().d("Tried to cache user data while no session was open.");
                    return null;
                }
                CrashlyticsController.this.reportingCoordinator.persistUserId(currentSessionId);
                new MetaDataStore(CrashlyticsController.this.getFilesDir()).writeUserData(currentSessionId, userMetaData);
                return null;
            }
        });
    }

    private void cacheKeyData(final Map<String, String> keyData, final boolean isInternal) {
        this.backgroundWorker.submit(new Callable<Void>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.8
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                new MetaDataStore(CrashlyticsController.this.getFilesDir()).writeKeyData(CrashlyticsController.this.getCurrentSessionId(), keyData, isInternal);
                return null;
            }
        });
    }

    void openSession() {
        this.backgroundWorker.submit(new Callable<Void>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.9
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                CrashlyticsController.this.doOpenSession();
                return null;
            }
        });
    }

    public String getCurrentSessionId() {
        List<String> sortedOpenSessions = this.reportingCoordinator.listSortedOpenSessionIds();
        if (!sortedOpenSessions.isEmpty()) {
            return sortedOpenSessions.get(0);
        }
        return null;
    }

    public boolean finalizeSessions(SettingsDataProvider settingsDataProvider) {
        this.backgroundWorker.checkRunningOnThread();
        if (isHandlingException()) {
            Logger.getLogger().w("Skipping session finalization because a crash has already occurred.");
            return Boolean.FALSE.booleanValue();
        }
        Logger.getLogger().v("Finalizing previously open sessions.");
        try {
            doCloseSessions(true, settingsDataProvider);
            Logger.getLogger().v("Closed all previously open sessions.");
            return true;
        } catch (Exception e) {
            Logger.getLogger().e("Unable to finalize previously open sessions.", e);
            return false;
        }
    }

    public void doOpenSession() {
        long startedAtSeconds = getCurrentTimestampSeconds();
        String sessionIdentifier = new CLSUUID(this.idManager).toString();
        Logger logger = Logger.getLogger();
        logger.d("Opening a new session with ID " + sessionIdentifier);
        this.nativeComponent.openSession(sessionIdentifier, String.format(Locale.US, GENERATOR_FORMAT, CrashlyticsCore.getVersion()), startedAtSeconds, StaticSessionData.create(createAppData(this.idManager, this.appData, this.unityVersion), createOsData(getContext()), createDeviceData(getContext())));
        this.logFileManager.setCurrentSession(sessionIdentifier);
        this.reportingCoordinator.onBeginSession(sessionIdentifier, startedAtSeconds);
    }

    void doCloseSessions(SettingsDataProvider settingsDataProvider) {
        doCloseSessions(false, settingsDataProvider);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void doCloseSessions(boolean skipCurrentSession, SettingsDataProvider settingsDataProvider) {
        List<String> sortedOpenSessions = this.reportingCoordinator.listSortedOpenSessionIds();
        if (sortedOpenSessions.size() <= skipCurrentSession) {
            Logger.getLogger().v("No open sessions to be closed.");
            return;
        }
        String mostRecentSessionIdToClose = sortedOpenSessions.get(skipCurrentSession ? 1 : 0);
        if (settingsDataProvider.getSettings().getFeaturesData().collectAnrs) {
            writeApplicationExitInfoEventIfRelevant(mostRecentSessionIdToClose);
        }
        if (this.nativeComponent.hasCrashDataForSession(mostRecentSessionIdToClose)) {
            finalizePreviousNativeSession(mostRecentSessionIdToClose);
            this.nativeComponent.finalizeSession(mostRecentSessionIdToClose);
        }
        String currentSessionId = null;
        if (skipCurrentSession != 0) {
            currentSessionId = sortedOpenSessions.get(0);
        }
        this.reportingCoordinator.finalizeSessions(getCurrentTimestampSeconds(), currentSessionId);
    }

    File[] listNativeSessionFileDirectories() {
        return ensureFileArrayNotNull(getNativeSessionFilesDir().listFiles());
    }

    File[] listAppExceptionMarkerFiles() {
        return listFilesMatching(APP_EXCEPTION_MARKER_FILTER);
    }

    private File[] listFilesMatching(FilenameFilter filter) {
        return listFilesMatching(getFilesDir(), filter);
    }

    private static File[] listFilesMatching(File directory, FilenameFilter filter) {
        return ensureFileArrayNotNull(directory.listFiles(filter));
    }

    private static File[] ensureFileArrayNotNull(File[] files) {
        return files == null ? new File[0] : files;
    }

    private void finalizePreviousNativeSession(String previousSessionId) {
        Logger logger = Logger.getLogger();
        logger.v("Finalizing native report for session " + previousSessionId);
        NativeSessionFileProvider nativeSessionFileProvider = this.nativeComponent.getSessionFileProvider(previousSessionId);
        File minidumpFile = nativeSessionFileProvider.getMinidumpFile();
        if (minidumpFile == null || !minidumpFile.exists()) {
            Logger logger2 = Logger.getLogger();
            logger2.w("No minidump data found for session " + previousSessionId);
            return;
        }
        long eventTime = minidumpFile.lastModified();
        LogFileManager previousSessionLogManager = new LogFileManager(this.context, this.logFileDirectoryProvider, previousSessionId);
        File nativeSessionDirectory = new File(getNativeSessionFilesDir(), previousSessionId);
        if (!nativeSessionDirectory.mkdirs()) {
            Logger.getLogger().w("Couldn't create directory to store native session files, aborting.");
            return;
        }
        doWriteAppExceptionMarker(eventTime);
        List<NativeSessionFile> nativeSessionFiles = getNativeSessionFiles(nativeSessionFileProvider, previousSessionId, getFilesDir(), previousSessionLogManager.getBytesForLog());
        NativeSessionFileGzipper.processNativeSessions(nativeSessionDirectory, nativeSessionFiles);
        this.reportingCoordinator.finalizeSessionWithNativeEvent(previousSessionId, nativeSessionFiles);
        previousSessionLogManager.clearLog();
    }

    private static long getCurrentTimestampSeconds() {
        return getTimestampSeconds(System.currentTimeMillis());
    }

    public static long getTimestampSeconds(long timestampMillis) {
        return timestampMillis / 1000;
    }

    public void doWriteAppExceptionMarker(long eventTime) {
        try {
            File filesDir = getFilesDir();
            new File(filesDir, APP_EXCEPTION_MARKER_PREFIX + eventTime).createNewFile();
        } catch (IOException e) {
            Logger.getLogger().w("Could not create app exception marker file.", e);
        }
    }

    private static StaticSessionData.AppData createAppData(IdManager idManager, AppData appData, String unityVersion) {
        return StaticSessionData.AppData.create(idManager.getAppIdentifier(), appData.versionCode, appData.versionName, idManager.getCrashlyticsInstallId(), DeliveryMechanism.determineFrom(appData.installerPackageName).getId(), unityVersion);
    }

    private static StaticSessionData.OsData createOsData(Context context) {
        return StaticSessionData.OsData.create(Build.VERSION.RELEASE, Build.VERSION.CODENAME, CommonUtils.isRooted(context));
    }

    private static StaticSessionData.DeviceData createDeviceData(Context context) {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return StaticSessionData.DeviceData.create(CommonUtils.getCpuArchitectureInt(), Build.MODEL, Runtime.getRuntime().availableProcessors(), CommonUtils.getTotalRamInBytes(), ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize()), CommonUtils.isEmulator(context), CommonUtils.getDeviceState(context), Build.MANUFACTURER, Build.PRODUCT);
    }

    UserMetadata getUserMetadata() {
        return this.userMetadata;
    }

    boolean isHandlingException() {
        CrashlyticsUncaughtExceptionHandler crashlyticsUncaughtExceptionHandler = this.crashHandler;
        return crashlyticsUncaughtExceptionHandler != null && crashlyticsUncaughtExceptionHandler.isHandlingException();
    }

    File getFilesDir() {
        return this.fileStore.getFilesDir();
    }

    File getNativeSessionFilesDir() {
        return new File(getFilesDir(), NATIVE_SESSION_DIR);
    }

    public Task<Void> logAnalyticsAppExceptionEvents() {
        List<Task<Void>> events = new ArrayList<>();
        File[] appExceptionMarkers = listAppExceptionMarkerFiles();
        for (File markerFile : appExceptionMarkers) {
            try {
                events.add(logAnalyticsAppExceptionEvent(Long.parseLong(markerFile.getName().substring(APP_EXCEPTION_MARKER_PREFIX.length()))));
            } catch (NumberFormatException e) {
                Logger.getLogger().w("Could not parse app exception timestamp from file " + markerFile.getName());
            }
            markerFile.delete();
        }
        return Tasks.whenAll(events);
    }

    private Task<Void> logAnalyticsAppExceptionEvent(final long timestamp) {
        if (firebaseCrashExists()) {
            Logger.getLogger().w("Skipping logging Crashlytics event to Firebase, FirebaseCrash exists");
            return Tasks.forResult(null);
        }
        Logger.getLogger().d("Logging app exception event to Firebase Analytics");
        return Tasks.call(new ScheduledThreadPoolExecutor(1), new Callable<Void>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsController.10
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                Bundle params = new Bundle();
                params.putInt(CrashlyticsController.FIREBASE_CRASH_TYPE, 1);
                params.putLong(CrashlyticsController.FIREBASE_TIMESTAMP, timestamp);
                CrashlyticsController.this.analyticsEventLogger.logEvent(CrashlyticsController.FIREBASE_APPLICATION_EXCEPTION, params);
                return null;
            }
        });
    }

    public static void deleteFiles(File[] files) {
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    private static boolean firebaseCrashExists() {
        try {
            Class.forName("com.google.firebase.crash.FirebaseCrash");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    static List<NativeSessionFile> getNativeSessionFiles(NativeSessionFileProvider fileProvider, String previousSessionId, File filesDir, byte[] logBytes) {
        MetaDataStore metaDataStore = new MetaDataStore(filesDir);
        File userFile = metaDataStore.getUserDataFileForSession(previousSessionId);
        File keysFile = metaDataStore.getKeysFileForSession(previousSessionId);
        List<NativeSessionFile> nativeSessionFiles = new ArrayList<>();
        nativeSessionFiles.add(new BytesBackedNativeSessionFile("logs_file", "logs", logBytes));
        nativeSessionFiles.add(new FileBackedNativeSessionFile("crash_meta_file", "metadata", fileProvider.getMetadataFile()));
        nativeSessionFiles.add(new FileBackedNativeSessionFile("session_meta_file", "session", fileProvider.getSessionFile()));
        nativeSessionFiles.add(new FileBackedNativeSessionFile("app_meta_file", "app", fileProvider.getAppFile()));
        nativeSessionFiles.add(new FileBackedNativeSessionFile("device_meta_file", "device", fileProvider.getDeviceFile()));
        nativeSessionFiles.add(new FileBackedNativeSessionFile("os_meta_file", "os", fileProvider.getOsFile()));
        nativeSessionFiles.add(new FileBackedNativeSessionFile("minidump_file", "minidump", fileProvider.getMinidumpFile()));
        nativeSessionFiles.add(new FileBackedNativeSessionFile("user_meta_file", "user", userFile));
        nativeSessionFiles.add(new FileBackedNativeSessionFile("keys_file", "keys", keysFile));
        return nativeSessionFiles;
    }

    private void writeApplicationExitInfoEventIfRelevant(String sessionId) {
        if (Build.VERSION.SDK_INT >= 30) {
            List<ApplicationExitInfo> applicationExitInfoList = ((ActivityManager) this.context.getSystemService("activity")).getHistoricalProcessExitReasons(null, 0, 1);
            if (applicationExitInfoList.size() != 0) {
                LogFileManager relevantSessionLogManager = new LogFileManager(this.context, this.logFileDirectoryProvider, sessionId);
                UserMetadata relevantUserMetadata = new UserMetadata();
                relevantUserMetadata.setCustomKeys(new MetaDataStore(getFilesDir()).readKeyData(sessionId));
                this.reportingCoordinator.persistAppExitInfoEvent(sessionId, applicationExitInfoList.get(0), relevantSessionLogManager, relevantUserMetadata);
                return;
            }
            return;
        }
        Logger logger = Logger.getLogger();
        logger.v("ANR feature enabled, but device is API " + Build.VERSION.SDK_INT);
    }
}
