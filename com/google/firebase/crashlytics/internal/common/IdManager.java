package com.google.firebase.crashlytics.internal.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.installations.FirebaseInstallationsApi;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;
/* loaded from: classes3.dex */
public class IdManager implements InstallIdProvider {
    public static final String DEFAULT_VERSION_NAME;
    static final String PREFKEY_ADVERTISING_ID;
    static final String PREFKEY_FIREBASE_IID;
    static final String PREFKEY_INSTALLATION_UUID;
    static final String PREFKEY_LEGACY_INSTALLATION_UUID;
    private static final String SYNTHETIC_FID_PREFIX;
    private final Context appContext;
    private final String appIdentifier;
    private String crashlyticsInstallId;
    private final DataCollectionArbiter dataCollectionArbiter;
    private final FirebaseInstallationsApi firebaseInstallationsApi;
    private final InstallerPackageNameProvider installerPackageNameProvider;
    private static final Pattern ID_PATTERN = Pattern.compile("[^\\p{Alnum}]");
    private static final String FORWARD_SLASH_REGEX = Pattern.quote("/");

    public IdManager(Context appContext, String appIdentifier, FirebaseInstallationsApi firebaseInstallationsApi, DataCollectionArbiter dataCollectionArbiter) {
        if (appContext == null) {
            throw new IllegalArgumentException("appContext must not be null");
        } else if (appIdentifier != null) {
            this.appContext = appContext;
            this.appIdentifier = appIdentifier;
            this.firebaseInstallationsApi = firebaseInstallationsApi;
            this.dataCollectionArbiter = dataCollectionArbiter;
            this.installerPackageNameProvider = new InstallerPackageNameProvider();
        } else {
            throw new IllegalArgumentException("appIdentifier must not be null");
        }
    }

    private static String formatId(String id) {
        if (id == null) {
            return null;
        }
        return ID_PATTERN.matcher(id).replaceAll("").toLowerCase(Locale.US);
    }

    @Override // com.google.firebase.crashlytics.internal.common.InstallIdProvider
    public synchronized String getCrashlyticsInstallId() {
        if (this.crashlyticsInstallId != null) {
            return this.crashlyticsInstallId;
        }
        Logger.getLogger().v("Determining Crashlytics installation ID...");
        SharedPreferences prefs = CommonUtils.getSharedPrefs(this.appContext);
        String cachedFid = prefs.getString(PREFKEY_FIREBASE_IID, null);
        Logger logger = Logger.getLogger();
        logger.v("Cached Firebase Installation ID: " + cachedFid);
        if (this.dataCollectionArbiter.isAutomaticDataCollectionEnabled()) {
            String trueFid = fetchTrueFid();
            Logger logger2 = Logger.getLogger();
            logger2.v("Fetched Firebase Installation ID: " + trueFid);
            if (trueFid == null) {
                trueFid = cachedFid == null ? createSyntheticFid() : cachedFid;
            }
            if (trueFid.equals(cachedFid)) {
                this.crashlyticsInstallId = readCachedCrashlyticsInstallId(prefs);
            } else {
                this.crashlyticsInstallId = createAndCacheCrashlyticsInstallId(trueFid, prefs);
            }
        } else if (isSyntheticFid(cachedFid)) {
            this.crashlyticsInstallId = readCachedCrashlyticsInstallId(prefs);
        } else {
            this.crashlyticsInstallId = createAndCacheCrashlyticsInstallId(createSyntheticFid(), prefs);
        }
        if (this.crashlyticsInstallId == null) {
            Logger.getLogger().w("Unable to determine Crashlytics Install Id, creating a new one.");
            this.crashlyticsInstallId = createAndCacheCrashlyticsInstallId(createSyntheticFid(), prefs);
        }
        Logger logger3 = Logger.getLogger();
        logger3.v("Crashlytics installation ID: " + this.crashlyticsInstallId);
        return this.crashlyticsInstallId;
    }

    static String createSyntheticFid() {
        return SYNTHETIC_FID_PREFIX + UUID.randomUUID().toString();
    }

    static boolean isSyntheticFid(String fid) {
        return fid != null && fid.startsWith(SYNTHETIC_FID_PREFIX);
    }

    private String readCachedCrashlyticsInstallId(SharedPreferences prefs) {
        return prefs.getString("crashlytics.installation.id", null);
    }

    private String fetchTrueFid() {
        try {
            return (String) Utils.awaitEvenIfOnMainThread(this.firebaseInstallationsApi.getId());
        } catch (Exception e) {
            Logger.getLogger().w("Failed to retrieve Firebase Installations ID.", e);
            return null;
        }
    }

    private synchronized String createAndCacheCrashlyticsInstallId(String fidToCache, SharedPreferences prefs) {
        String iid;
        iid = formatId(UUID.randomUUID().toString());
        Logger logger = Logger.getLogger();
        logger.v("Created new Crashlytics installation ID: " + iid + " for FID: " + fidToCache);
        prefs.edit().putString("crashlytics.installation.id", iid).putString(PREFKEY_FIREBASE_IID, fidToCache).apply();
        return iid;
    }

    public String getAppIdentifier() {
        return this.appIdentifier;
    }

    public String getOsDisplayVersionString() {
        return removeForwardSlashesIn(Build.VERSION.RELEASE);
    }

    public String getOsBuildVersionString() {
        return removeForwardSlashesIn(Build.VERSION.INCREMENTAL);
    }

    public String getModelName() {
        return String.format(Locale.US, "%s/%s", removeForwardSlashesIn(Build.MANUFACTURER), removeForwardSlashesIn(Build.MODEL));
    }

    private String removeForwardSlashesIn(String s) {
        return s.replaceAll(FORWARD_SLASH_REGEX, "");
    }

    public String getInstallerPackageName() {
        return this.installerPackageNameProvider.getInstallerPackageName(this.appContext);
    }
}
