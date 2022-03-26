package com.google.firebase.crashlytics.internal.settings;

import androidx.core.app.NotificationCompat;
import com.google.firebase.crashlytics.internal.common.CurrentTimeProvider;
import com.google.firebase.crashlytics.internal.settings.model.AppSettingsData;
import com.google.firebase.crashlytics.internal.settings.model.FeaturesSettingsData;
import com.google.firebase.crashlytics.internal.settings.model.SessionSettingsData;
import com.google.firebase.crashlytics.internal.settings.model.SettingsData;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes3.dex */
class SettingsV3JsonTransform implements SettingsJsonTransform {
    private static final String CRASHLYTICS_APP_URL = "https://update.crashlytics.com/spi/v1/platforms/android/apps";
    private static final String CRASHLYTICS_APP_URL_FORMAT = "https://update.crashlytics.com/spi/v1/platforms/android/apps/%s";
    private static final String NDK_REPORTS_URL_FORMAT = "https://reports.crashlytics.com/sdk-api/v1/platforms/android/apps/%s/minidumps";
    private static final String REPORTS_URL_FORMAT = "https://reports.crashlytics.com/spi/v1/platforms/android/apps/%s/reports";

    @Override // com.google.firebase.crashlytics.internal.settings.SettingsJsonTransform
    public SettingsData buildFromJson(CurrentTimeProvider currentTimeProvider, JSONObject json) throws JSONException {
        int settingsVersion = json.optInt("settings_version", 0);
        int cacheDuration = json.optInt("cache_duration", 3600);
        return new SettingsData(getExpiresAtFrom(currentTimeProvider, (long) cacheDuration, json), buildAppDataFrom(json.getJSONObject("fabric"), json.getJSONObject("app")), defaultSessionData(), buildFeaturesSessionDataFrom(json.getJSONObject("features")), settingsVersion, cacheDuration);
    }

    @Override // com.google.firebase.crashlytics.internal.settings.SettingsJsonTransform
    public JSONObject toJson(SettingsData settingsData) throws JSONException {
        return new JSONObject().put("expires_at", settingsData.expiresAtMillis).put("cache_duration", settingsData.cacheDuration).put("settings_version", settingsData.settingsVersion).put("features", toFeaturesJson(settingsData.featuresData)).put("app", toAppJson(settingsData.appData)).put("fabric", toFabricJson(settingsData.appData));
    }

    private static AppSettingsData buildAppDataFrom(JSONObject fabricJson, JSONObject appJson) throws JSONException {
        String url;
        String status = appJson.getString(NotificationCompat.CATEGORY_STATUS);
        boolean isNewApp = AppSettingsData.STATUS_NEW.equals(status);
        String bundleId = fabricJson.getString("bundle_id");
        String organizationId = fabricJson.getString("org_id");
        if (isNewApp) {
            url = CRASHLYTICS_APP_URL;
        } else {
            url = String.format(Locale.US, CRASHLYTICS_APP_URL_FORMAT, bundleId);
        }
        return new AppSettingsData(status, url, String.format(Locale.US, REPORTS_URL_FORMAT, bundleId), String.format(Locale.US, NDK_REPORTS_URL_FORMAT, bundleId), bundleId, organizationId, appJson.optBoolean("update_required", false), appJson.optInt("report_upload_variant", 0), appJson.optInt("native_report_upload_variant", 0));
    }

    private static FeaturesSettingsData buildFeaturesSessionDataFrom(JSONObject json) {
        return new FeaturesSettingsData(json.optBoolean("collect_reports", true), json.optBoolean("collect_anrs", false));
    }

    private static SessionSettingsData defaultSessionData() {
        return new SessionSettingsData(8, 4);
    }

    private JSONObject toFabricJson(AppSettingsData appData) throws JSONException {
        return new JSONObject().put("bundle_id", appData.bundleId).put("org_id", appData.organizationId);
    }

    private JSONObject toAppJson(AppSettingsData appData) throws JSONException {
        return new JSONObject().put(NotificationCompat.CATEGORY_STATUS, appData.status).put("update_required", appData.updateRequired).put("report_upload_variant", appData.reportUploadVariant).put("native_report_upload_variant", appData.nativeReportUploadVariant);
    }

    private JSONObject toFeaturesJson(FeaturesSettingsData features) throws JSONException {
        return new JSONObject().put("collect_reports", features.collectReports);
    }

    private static long getExpiresAtFrom(CurrentTimeProvider currentTimeProvider, long cacheDurationSeconds, JSONObject json) {
        if (json.has("expires_at")) {
            return json.optLong("expires_at");
        }
        return currentTimeProvider.getCurrentTimeMillis() + (1000 * cacheDurationSeconds);
    }
}
