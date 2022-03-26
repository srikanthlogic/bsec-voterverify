package com.google.firebase.crashlytics;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.crashlytics.internal.CrashlyticsNativeComponent;
import com.google.firebase.crashlytics.internal.CrashlyticsNativeComponentDeferredProxy;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.common.AppData;
import com.google.firebase.crashlytics.internal.common.CommonUtils;
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;
import com.google.firebase.crashlytics.internal.common.DataCollectionArbiter;
import com.google.firebase.crashlytics.internal.common.ExecutorUtils;
import com.google.firebase.crashlytics.internal.common.IdManager;
import com.google.firebase.crashlytics.internal.network.HttpRequestFactory;
import com.google.firebase.crashlytics.internal.settings.SettingsController;
import com.google.firebase.crashlytics.internal.unity.ResourceUnityVersionProvider;
import com.google.firebase.inject.Deferred;
import com.google.firebase.installations.FirebaseInstallationsApi;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
/* loaded from: classes3.dex */
public class FirebaseCrashlytics {
    static final int APP_EXCEPTION_CALLBACK_TIMEOUT_MS = 500;
    static final String FIREBASE_CRASHLYTICS_ANALYTICS_ORIGIN = "clx";
    static final String LEGACY_CRASH_ANALYTICS_ORIGIN = "crash";
    final CrashlyticsCore core;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FirebaseCrashlytics init(FirebaseApp app, FirebaseInstallationsApi firebaseInstallationsApi, Deferred<CrashlyticsNativeComponent> nativeComponent, Deferred<AnalyticsConnector> analyticsConnector) {
        Context context = app.getApplicationContext();
        String appIdentifier = context.getPackageName();
        Logger logger = Logger.getLogger();
        logger.i("Initializing Firebase Crashlytics " + CrashlyticsCore.getVersion() + " for " + appIdentifier);
        DataCollectionArbiter arbiter = new DataCollectionArbiter(app);
        IdManager idManager = new IdManager(context, appIdentifier, firebaseInstallationsApi, arbiter);
        CrashlyticsNativeComponentDeferredProxy deferredNativeComponent = new CrashlyticsNativeComponentDeferredProxy(nativeComponent);
        AnalyticsDeferredProxy analyticsDeferredProxy = new AnalyticsDeferredProxy(analyticsConnector);
        final CrashlyticsCore core = new CrashlyticsCore(app, idManager, deferredNativeComponent, arbiter, analyticsDeferredProxy.getDeferredBreadcrumbSource(), analyticsDeferredProxy.getAnalyticsEventLogger(), ExecutorUtils.buildSingleThreadExecutorService("Crashlytics Exception Handler"));
        String googleAppId = app.getOptions().getApplicationId();
        String mappingFileId = CommonUtils.getMappingFileId(context);
        Logger logger2 = Logger.getLogger();
        logger2.d("Mapping file ID is: " + mappingFileId);
        try {
            AppData appData = AppData.create(context, idManager, googleAppId, mappingFileId, new ResourceUnityVersionProvider(context));
            Logger logger3 = Logger.getLogger();
            logger3.v("Installer package name is: " + appData.installerPackageName);
            ExecutorService threadPoolExecutor = ExecutorUtils.buildSingleThreadExecutorService("com.google.firebase.crashlytics.startup");
            final SettingsController settingsController = SettingsController.create(context, googleAppId, idManager, new HttpRequestFactory(), appData.versionCode, appData.versionName, arbiter);
            settingsController.loadSettingsData(threadPoolExecutor).continueWith(threadPoolExecutor, new Continuation<Void, Object>() { // from class: com.google.firebase.crashlytics.FirebaseCrashlytics.1
                @Override // com.google.android.gms.tasks.Continuation
                public Object then(Task<Void> task) throws Exception {
                    if (task.isSuccessful()) {
                        return null;
                    }
                    Logger.getLogger().e("Error fetching settings.", task.getException());
                    return null;
                }
            });
            final boolean finishCoreInBackground = core.onPreExecute(appData, settingsController);
            Tasks.call(threadPoolExecutor, new Callable<Void>() { // from class: com.google.firebase.crashlytics.FirebaseCrashlytics.2
                @Override // java.util.concurrent.Callable
                public Void call() throws Exception {
                    if (!finishCoreInBackground) {
                        return null;
                    }
                    core.doBackgroundInitializationAsync(settingsController);
                    return null;
                }
            });
            return new FirebaseCrashlytics(core);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.getLogger().e("Error retrieving app package info.", e);
            return null;
        }
    }

    private FirebaseCrashlytics(CrashlyticsCore core) {
        this.core = core;
    }

    public static FirebaseCrashlytics getInstance() {
        FirebaseCrashlytics instance = (FirebaseCrashlytics) FirebaseApp.getInstance().get(FirebaseCrashlytics.class);
        if (instance != null) {
            return instance;
        }
        throw new NullPointerException("FirebaseCrashlytics component is not present.");
    }

    public void recordException(Throwable throwable) {
        if (throwable == null) {
            Logger.getLogger().w("A null value was passed to recordException. Ignoring.");
        } else {
            this.core.logException(throwable);
        }
    }

    public void log(String message) {
        this.core.log(message);
    }

    public void setUserId(String identifier) {
        this.core.setUserId(identifier);
    }

    public void setCustomKey(String key, boolean value) {
        this.core.setCustomKey(key, Boolean.toString(value));
    }

    public void setCustomKey(String key, double value) {
        this.core.setCustomKey(key, Double.toString(value));
    }

    public void setCustomKey(String key, float value) {
        this.core.setCustomKey(key, Float.toString(value));
    }

    public void setCustomKey(String key, int value) {
        this.core.setCustomKey(key, Integer.toString(value));
    }

    public void setCustomKey(String key, long value) {
        this.core.setCustomKey(key, Long.toString(value));
    }

    public void setCustomKey(String key, String value) {
        this.core.setCustomKey(key, value);
    }

    public void setCustomKeys(CustomKeysAndValues keysAndValues) {
        this.core.setCustomKeys(keysAndValues.keysAndValues);
    }

    public Task<Boolean> checkForUnsentReports() {
        return this.core.checkForUnsentReports();
    }

    public void sendUnsentReports() {
        this.core.sendUnsentReports();
    }

    public void deleteUnsentReports() {
        this.core.deleteUnsentReports();
    }

    public boolean didCrashOnPreviousExecution() {
        return this.core.didCrashOnPreviousExecution();
    }

    public void setCrashlyticsCollectionEnabled(boolean enabled) {
        this.core.setCrashlyticsCollectionEnabled(Boolean.valueOf(enabled));
    }

    public void setCrashlyticsCollectionEnabled(Boolean enabled) {
        this.core.setCrashlyticsCollectionEnabled(enabled);
    }
}
