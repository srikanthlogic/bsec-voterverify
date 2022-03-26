package com.google.firebase.crashlytics.internal.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.internal.Logger;
/* loaded from: classes3.dex */
public class DataCollectionArbiter {
    private static final String FIREBASE_CRASHLYTICS_COLLECTION_ENABLED;
    private Boolean crashlyticsDataCollectionEnabled;
    private final FirebaseApp firebaseApp;
    private final SharedPreferences sharedPreferences;
    boolean taskResolved;
    private final Object taskLock = new Object();
    TaskCompletionSource<Void> dataCollectionEnabledTask = new TaskCompletionSource<>();
    private boolean setInManifest = false;
    private final TaskCompletionSource<Void> dataCollectionExplicitlyApproved = new TaskCompletionSource<>();

    public DataCollectionArbiter(FirebaseApp app) {
        this.taskResolved = false;
        Context applicationContext = app.getApplicationContext();
        this.firebaseApp = app;
        this.sharedPreferences = CommonUtils.getSharedPrefs(applicationContext);
        Boolean dataCollectionEnabled = getDataCollectionValueFromSharedPreferences();
        this.crashlyticsDataCollectionEnabled = dataCollectionEnabled == null ? getDataCollectionValueFromManifest(applicationContext) : dataCollectionEnabled;
        synchronized (this.taskLock) {
            if (isAutomaticDataCollectionEnabled()) {
                this.dataCollectionEnabledTask.trySetResult(null);
                this.taskResolved = true;
            }
        }
    }

    public synchronized boolean isAutomaticDataCollectionEnabled() {
        boolean dataCollectionEnabled;
        if (this.crashlyticsDataCollectionEnabled != null) {
            dataCollectionEnabled = this.crashlyticsDataCollectionEnabled.booleanValue();
        } else {
            dataCollectionEnabled = this.firebaseApp.isDataCollectionDefaultEnabled();
        }
        logDataCollectionState(dataCollectionEnabled);
        return dataCollectionEnabled;
    }

    public synchronized void setCrashlyticsDataCollectionEnabled(Boolean enabled) {
        Boolean bool;
        if (enabled != null) {
            try {
                this.setInManifest = false;
            } catch (Throwable th) {
                throw th;
            }
        }
        if (enabled != null) {
            bool = enabled;
        } else {
            bool = getDataCollectionValueFromManifest(this.firebaseApp.getApplicationContext());
        }
        this.crashlyticsDataCollectionEnabled = bool;
        storeDataCollectionValueInSharedPreferences(this.sharedPreferences, enabled);
        synchronized (this.taskLock) {
            if (isAutomaticDataCollectionEnabled()) {
                if (!this.taskResolved) {
                    this.dataCollectionEnabledTask.trySetResult(null);
                    this.taskResolved = true;
                }
            } else if (this.taskResolved) {
                this.dataCollectionEnabledTask = new TaskCompletionSource<>();
                this.taskResolved = false;
            }
        }
    }

    public Task<Void> waitForAutomaticDataCollectionEnabled() {
        Task<Void> task;
        synchronized (this.taskLock) {
            task = this.dataCollectionEnabledTask.getTask();
        }
        return task;
    }

    public Task<Void> waitForDataCollectionPermission() {
        return Utils.race(this.dataCollectionExplicitlyApproved.getTask(), waitForAutomaticDataCollectionEnabled());
    }

    public void grantDataCollectionPermission(boolean dataCollectionToken) {
        if (dataCollectionToken) {
            this.dataCollectionExplicitlyApproved.trySetResult(null);
            return;
        }
        throw new IllegalStateException("An invalid data collection token was used.");
    }

    private void logDataCollectionState(boolean dataCollectionEnabled) {
        String fromString;
        String stateString = dataCollectionEnabled ? "ENABLED" : "DISABLED";
        if (this.crashlyticsDataCollectionEnabled == null) {
            fromString = "global Firebase setting";
        } else {
            fromString = this.setInManifest ? "firebase_crashlytics_collection_enabled manifest flag" : "API";
        }
        Logger.getLogger().d(String.format("Crashlytics automatic data collection %s by %s.", stateString, fromString));
    }

    private Boolean getDataCollectionValueFromSharedPreferences() {
        if (!this.sharedPreferences.contains(FIREBASE_CRASHLYTICS_COLLECTION_ENABLED)) {
            return null;
        }
        this.setInManifest = false;
        return Boolean.valueOf(this.sharedPreferences.getBoolean(FIREBASE_CRASHLYTICS_COLLECTION_ENABLED, true));
    }

    private Boolean getDataCollectionValueFromManifest(Context applicationContext) {
        Boolean manifestSetting = readCrashlyticsDataCollectionEnabledFromManifest(applicationContext);
        if (manifestSetting == null) {
            this.setInManifest = false;
            return null;
        }
        this.setInManifest = true;
        return Boolean.valueOf(Boolean.TRUE.equals(manifestSetting));
    }

    private static Boolean readCrashlyticsDataCollectionEnabledFromManifest(Context applicationContext) {
        ApplicationInfo applicationInfo;
        try {
            PackageManager packageManager = applicationContext.getPackageManager();
            if (packageManager == null || (applicationInfo = packageManager.getApplicationInfo(applicationContext.getPackageName(), 128)) == null || applicationInfo.metaData == null || !applicationInfo.metaData.containsKey(FIREBASE_CRASHLYTICS_COLLECTION_ENABLED)) {
                return null;
            }
            return Boolean.valueOf(applicationInfo.metaData.getBoolean(FIREBASE_CRASHLYTICS_COLLECTION_ENABLED));
        } catch (PackageManager.NameNotFoundException e) {
            Logger.getLogger().e("Could not read data collection permission from manifest", e);
            return null;
        }
    }

    private static void storeDataCollectionValueInSharedPreferences(SharedPreferences sharedPreferences, Boolean enabled) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        if (enabled != null) {
            prefsEditor.putBoolean(FIREBASE_CRASHLYTICS_COLLECTION_ENABLED, enabled.booleanValue());
        } else {
            prefsEditor.remove(FIREBASE_CRASHLYTICS_COLLECTION_ENABLED);
        }
        prefsEditor.commit();
    }
}
