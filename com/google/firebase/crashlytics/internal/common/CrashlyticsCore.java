package com.google.firebase.crashlytics.internal.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.BuildConfig;
import com.google.firebase.crashlytics.internal.CrashlyticsNativeComponent;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.analytics.AnalyticsEventLogger;
import com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbHandler;
import com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbSource;
import com.google.firebase.crashlytics.internal.log.LogFileManager;
import com.google.firebase.crashlytics.internal.persistence.FileStore;
import com.google.firebase.crashlytics.internal.persistence.FileStoreImpl;
import com.google.firebase.crashlytics.internal.settings.SettingsDataProvider;
import com.google.firebase.crashlytics.internal.stacktrace.MiddleOutFallbackStrategy;
import com.google.firebase.crashlytics.internal.stacktrace.RemoveRepeatsStrategy;
import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes3.dex */
public class CrashlyticsCore {
    static final String CRASHLYTICS_REQUIRE_BUILD_ID;
    static final boolean CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT;
    static final String CRASH_MARKER_FILE_NAME;
    static final int DEFAULT_MAIN_HANDLER_TIMEOUT_SEC;
    private static final String INITIALIZATION_MARKER_FILE_NAME;
    static final int MAX_STACK_SIZE;
    private static final String MISSING_BUILD_ID_MSG;
    static final int NUM_STACK_REPETITIONS_ALLOWED;
    private final AnalyticsEventLogger analyticsEventLogger;
    private final FirebaseApp app;
    private final CrashlyticsBackgroundWorker backgroundWorker;
    public final BreadcrumbSource breadcrumbSource;
    private final Context context;
    private CrashlyticsController controller;
    private final ExecutorService crashHandlerExecutor;
    private CrashlyticsFileMarker crashMarker;
    private final DataCollectionArbiter dataCollectionArbiter;
    private boolean didCrashOnPreviousExecution;
    private final IdManager idManager;
    private CrashlyticsFileMarker initializationMarker;
    private final CrashlyticsNativeComponent nativeComponent;
    private final long startTime = System.currentTimeMillis();

    public CrashlyticsCore(FirebaseApp app, IdManager idManager, CrashlyticsNativeComponent nativeComponent, DataCollectionArbiter dataCollectionArbiter, BreadcrumbSource breadcrumbSource, AnalyticsEventLogger analyticsEventLogger, ExecutorService crashHandlerExecutor) {
        this.app = app;
        this.dataCollectionArbiter = dataCollectionArbiter;
        this.context = app.getApplicationContext();
        this.idManager = idManager;
        this.nativeComponent = nativeComponent;
        this.breadcrumbSource = breadcrumbSource;
        this.analyticsEventLogger = analyticsEventLogger;
        this.crashHandlerExecutor = crashHandlerExecutor;
        this.backgroundWorker = new CrashlyticsBackgroundWorker(crashHandlerExecutor);
    }

    public boolean onPreExecute(AppData appData, SettingsDataProvider settingsProvider) {
        if (isBuildIdValid(appData.buildId, CommonUtils.getBooleanResourceValue(this.context, CRASHLYTICS_REQUIRE_BUILD_ID, true))) {
            try {
                FileStore fileStore = new FileStoreImpl(this.context);
                this.crashMarker = new CrashlyticsFileMarker(CRASH_MARKER_FILE_NAME, fileStore);
                this.initializationMarker = new CrashlyticsFileMarker(INITIALIZATION_MARKER_FILE_NAME, fileStore);
                UserMetadata userMetadata = new UserMetadata();
                LogFileDirectoryProvider logFileDirectoryProvider = new LogFileDirectoryProvider(fileStore);
                LogFileManager logFileManager = new LogFileManager(this.context, logFileDirectoryProvider);
                this.controller = new CrashlyticsController(this.context, this.backgroundWorker, this.idManager, this.dataCollectionArbiter, fileStore, this.crashMarker, appData, userMetadata, logFileManager, logFileDirectoryProvider, SessionReportingCoordinator.create(this.context, this.idManager, fileStore, appData, logFileManager, userMetadata, new MiddleOutFallbackStrategy(1024, new RemoveRepeatsStrategy(10)), settingsProvider), this.nativeComponent, this.analyticsEventLogger);
                boolean initializeSynchronously = didPreviousInitializationFail();
                checkForPreviousCrash();
                this.controller.enableExceptionHandling(Thread.getDefaultUncaughtExceptionHandler(), settingsProvider);
                if (!initializeSynchronously || !CommonUtils.canTryConnection(this.context)) {
                    Logger.getLogger().d("Successfully configured exception handler.");
                    return true;
                }
                Logger.getLogger().d("Crashlytics did not finish previous background initialization. Initializing synchronously.");
                finishInitSynchronously(settingsProvider);
                return false;
            } catch (Exception e) {
                Logger.getLogger().e("Crashlytics was not started due to an exception during initialization", e);
                this.controller = null;
                return false;
            }
        } else {
            throw new IllegalStateException(MISSING_BUILD_ID_MSG);
        }
    }

    public Task<Void> doBackgroundInitializationAsync(final SettingsDataProvider settingsProvider) {
        return Utils.callTask(this.crashHandlerExecutor, new Callable<Task<Void>>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsCore.1
            @Override // java.util.concurrent.Callable
            public Task<Void> call() throws Exception {
                return CrashlyticsCore.this.doBackgroundInitialization(settingsProvider);
            }
        });
    }

    public Task<Void> doBackgroundInitialization(SettingsDataProvider settingsProvider) {
        try {
            markInitializationStarted();
            this.breadcrumbSource.registerBreadcrumbHandler(new BreadcrumbHandler() { // from class: com.google.firebase.crashlytics.internal.common.-$$Lambda$Cu0wOoGXbvGzEhedNS4Zq8UJwmQ
                @Override // com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbHandler
                public final void handleBreadcrumb(String str) {
                    CrashlyticsCore.this.log(str);
                }
            });
            if (!settingsProvider.getSettings().getFeaturesData().collectReports) {
                Logger.getLogger().d("Collection of crash reports disabled in Crashlytics settings.");
                return Tasks.forException(new RuntimeException("Collection of crash reports disabled in Crashlytics settings."));
            }
            if (!this.controller.finalizeSessions(settingsProvider)) {
                Logger.getLogger().w("Previous sessions could not be finalized.");
            }
            return this.controller.submitAllReports(settingsProvider.getAppSettings());
        } catch (Exception e) {
            Logger.getLogger().e("Crashlytics encountered a problem during asynchronous initialization.", e);
            return Tasks.forException(e);
        } finally {
            markInitializationComplete();
        }
    }

    public void setCrashlyticsCollectionEnabled(Boolean enabled) {
        this.dataCollectionArbiter.setCrashlyticsDataCollectionEnabled(enabled);
    }

    public Task<Boolean> checkForUnsentReports() {
        return this.controller.checkForUnsentReports();
    }

    public Task<Void> sendUnsentReports() {
        return this.controller.sendUnsentReports();
    }

    public Task<Void> deleteUnsentReports() {
        return this.controller.deleteUnsentReports();
    }

    public static String getVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public void logException(Throwable throwable) {
        this.controller.writeNonFatalException(Thread.currentThread(), throwable);
    }

    public void log(String msg) {
        this.controller.writeToLog(System.currentTimeMillis() - this.startTime, msg);
    }

    public void setUserId(String identifier) {
        this.controller.setUserId(identifier);
    }

    public void setCustomKey(String key, String value) {
        this.controller.setCustomKey(key, value);
    }

    public void setCustomKeys(Map<String, String> keysAndValues) {
        this.controller.setCustomKeys(keysAndValues);
    }

    public void setInternalKey(String key, String value) {
        this.controller.setInternalKey(key, value);
    }

    CrashlyticsController getController() {
        return this.controller;
    }

    private void finishInitSynchronously(final SettingsDataProvider settingsDataProvider) {
        Future<?> future = this.crashHandlerExecutor.submit(new Runnable() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsCore.2
            @Override // java.lang.Runnable
            public void run() {
                CrashlyticsCore.this.doBackgroundInitialization(settingsDataProvider);
            }
        });
        Logger.getLogger().d("Crashlytics detected incomplete initialization on previous app launch. Will initialize synchronously.");
        try {
            future.get(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Logger.getLogger().e("Crashlytics was interrupted during initialization.", e);
        } catch (ExecutionException e2) {
            Logger.getLogger().e("Crashlytics encountered a problem during initialization.", e2);
        } catch (TimeoutException e3) {
            Logger.getLogger().e("Crashlytics timed out during initialization.", e3);
        }
    }

    void markInitializationStarted() {
        this.backgroundWorker.checkRunningOnThread();
        this.initializationMarker.create();
        Logger.getLogger().v("Initialization marker file was created.");
    }

    void markInitializationComplete() {
        this.backgroundWorker.submit(new Callable<Boolean>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsCore.3
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                try {
                    boolean removed = CrashlyticsCore.this.initializationMarker.remove();
                    if (!removed) {
                        Logger.getLogger().w("Initialization marker file was not properly removed.");
                    }
                    return Boolean.valueOf(removed);
                } catch (Exception e) {
                    Logger.getLogger().e("Problem encountered deleting Crashlytics initialization marker.", e);
                    return false;
                }
            }
        });
    }

    boolean didPreviousInitializationFail() {
        return this.initializationMarker.isPresent();
    }

    private void checkForPreviousCrash() {
        try {
            this.didCrashOnPreviousExecution = Boolean.TRUE.equals((Boolean) Utils.awaitEvenIfOnMainThread(this.backgroundWorker.submit(new Callable<Boolean>() { // from class: com.google.firebase.crashlytics.internal.common.CrashlyticsCore.4
                @Override // java.util.concurrent.Callable
                public Boolean call() throws Exception {
                    return Boolean.valueOf(CrashlyticsCore.this.controller.didCrashOnPreviousExecution());
                }
            })));
        } catch (Exception e) {
            this.didCrashOnPreviousExecution = false;
        }
    }

    public boolean didCrashOnPreviousExecution() {
        return this.didCrashOnPreviousExecution;
    }

    static boolean isBuildIdValid(String buildId, boolean requiresBuildId) {
        if (!requiresBuildId) {
            Logger.getLogger().v("Configured not to require a build ID.");
            return true;
        } else if (!TextUtils.isEmpty(buildId)) {
            return true;
        } else {
            Log.e(Logger.TAG, ".");
            Log.e(Logger.TAG, ".     |  | ");
            Log.e(Logger.TAG, ".     |  |");
            Log.e(Logger.TAG, ".     |  |");
            Log.e(Logger.TAG, ".   \\ |  | /");
            Log.e(Logger.TAG, ".    \\    /");
            Log.e(Logger.TAG, ".     \\  /");
            Log.e(Logger.TAG, ".      \\/");
            Log.e(Logger.TAG, ".");
            Log.e(Logger.TAG, MISSING_BUILD_ID_MSG);
            Log.e(Logger.TAG, ".");
            Log.e(Logger.TAG, ".      /\\");
            Log.e(Logger.TAG, ".     /  \\");
            Log.e(Logger.TAG, ".    /    \\");
            Log.e(Logger.TAG, ".   / |  | \\");
            Log.e(Logger.TAG, ".     |  |");
            Log.e(Logger.TAG, ".     |  |");
            Log.e(Logger.TAG, ".     |  |");
            Log.e(Logger.TAG, ".");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class LogFileDirectoryProvider implements LogFileManager.DirectoryProvider {
        private static final String LOG_FILES_DIR;
        private final FileStore rootFileStore;

        public LogFileDirectoryProvider(FileStore rootFileStore) {
            this.rootFileStore = rootFileStore;
        }

        @Override // com.google.firebase.crashlytics.internal.log.LogFileManager.DirectoryProvider
        public File getLogFileDir() {
            File logFileDir = new File(this.rootFileStore.getFilesDir(), LOG_FILES_DIR);
            if (!logFileDir.exists()) {
                logFileDir.mkdirs();
            }
            return logFileDir;
        }
    }
}
