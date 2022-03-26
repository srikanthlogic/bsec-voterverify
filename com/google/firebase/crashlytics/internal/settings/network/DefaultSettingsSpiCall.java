package com.google.firebase.crashlytics.internal.settings.network;

import android.text.TextUtils;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;
import com.google.firebase.crashlytics.internal.network.HttpGetRequest;
import com.google.firebase.crashlytics.internal.network.HttpRequestFactory;
import com.google.firebase.crashlytics.internal.network.HttpResponse;
import com.google.firebase.crashlytics.internal.settings.model.SettingsRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class DefaultSettingsSpiCall implements SettingsSpiCall {
    static final String ACCEPT_JSON_VALUE = "application/json";
    static final String ANDROID_CLIENT_TYPE = "android";
    static final String BUILD_VERSION_PARAM = "build_version";
    static final String CRASHLYTICS_USER_AGENT = "Crashlytics Android SDK/";
    static final String DISPLAY_VERSION_PARAM = "display_version";
    static final String HEADER_ACCEPT = "Accept";
    static final String HEADER_CLIENT_TYPE = "X-CRASHLYTICS-API-CLIENT-TYPE";
    static final String HEADER_CLIENT_VERSION = "X-CRASHLYTICS-API-CLIENT-VERSION";
    static final String HEADER_DEVICE_MODEL = "X-CRASHLYTICS-DEVICE-MODEL";
    static final String HEADER_GOOGLE_APP_ID = "X-CRASHLYTICS-GOOGLE-APP-ID";
    static final String HEADER_INSTALLATION_ID = "X-CRASHLYTICS-INSTALLATION-ID";
    static final String HEADER_OS_BUILD_VERSION = "X-CRASHLYTICS-OS-BUILD-VERSION";
    static final String HEADER_OS_DISPLAY_VERSION = "X-CRASHLYTICS-OS-DISPLAY-VERSION";
    static final String HEADER_USER_AGENT = "User-Agent";
    static final String INSTANCE_PARAM = "instance";
    static final String SOURCE_PARAM = "source";
    private final Logger logger;
    private final HttpRequestFactory requestFactory;
    private final String url;

    public DefaultSettingsSpiCall(String url, HttpRequestFactory requestFactory) {
        this(url, requestFactory, Logger.getLogger());
    }

    DefaultSettingsSpiCall(String url, HttpRequestFactory requestFactory, Logger logger) {
        if (url != null) {
            this.logger = logger;
            this.requestFactory = requestFactory;
            this.url = url;
            return;
        }
        throw new IllegalArgumentException("url must not be null.");
    }

    protected HttpGetRequest createHttpGetRequest(Map<String, String> queryParams) {
        HttpGetRequest httpRequest = this.requestFactory.buildHttpGetRequest(this.url, queryParams);
        return httpRequest.header("User-Agent", CRASHLYTICS_USER_AGENT + CrashlyticsCore.getVersion()).header("X-CRASHLYTICS-DEVELOPER-TOKEN", "470fa2b4ae81cd56ecbcda9735803434cec591fa");
    }

    @Override // com.google.firebase.crashlytics.internal.settings.network.SettingsSpiCall
    public JSONObject invoke(SettingsRequest requestData, boolean dataCollectionToken) {
        if (dataCollectionToken) {
            try {
                Map<String, String> queryParams = getQueryParamsFor(requestData);
                HttpGetRequest httpRequest = applyHeadersTo(createHttpGetRequest(queryParams), requestData);
                Logger logger = this.logger;
                logger.d("Requesting settings from " + this.url);
                Logger logger2 = this.logger;
                logger2.v("Settings query params were: " + queryParams);
                return handleResponse(httpRequest.execute());
            } catch (IOException e) {
                this.logger.e("Settings request failed.", e);
                return null;
            }
        } else {
            throw new RuntimeException("An invalid data collection token was used.");
        }
    }

    JSONObject handleResponse(HttpResponse httpResponse) {
        int statusCode = httpResponse.code();
        Logger logger = this.logger;
        logger.v("Settings response code was: " + statusCode);
        if (requestWasSuccessful(statusCode)) {
            return getJsonObjectFrom(httpResponse.body());
        }
        Logger logger2 = this.logger;
        logger2.e("Settings request failed; (status: " + statusCode + ") from " + this.url);
        return null;
    }

    boolean requestWasSuccessful(int httpStatusCode) {
        return httpStatusCode == 200 || httpStatusCode == 201 || httpStatusCode == 202 || httpStatusCode == 203;
    }

    private JSONObject getJsonObjectFrom(String httpRequestBody) {
        try {
            return new JSONObject(httpRequestBody);
        } catch (Exception e) {
            Logger logger = this.logger;
            logger.w("Failed to parse settings JSON from " + this.url, e);
            Logger logger2 = this.logger;
            logger2.w("Settings response " + httpRequestBody);
            return null;
        }
    }

    private Map<String, String> getQueryParamsFor(SettingsRequest requestData) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(BUILD_VERSION_PARAM, requestData.buildVersion);
        queryParams.put(DISPLAY_VERSION_PARAM, requestData.displayVersion);
        queryParams.put("source", Integer.toString(requestData.source));
        String instanceId = requestData.instanceId;
        if (!TextUtils.isEmpty(instanceId)) {
            queryParams.put(INSTANCE_PARAM, instanceId);
        }
        return queryParams;
    }

    private HttpGetRequest applyHeadersTo(HttpGetRequest request, SettingsRequest requestData) {
        applyNonNullHeader(request, HEADER_GOOGLE_APP_ID, requestData.googleAppId);
        applyNonNullHeader(request, HEADER_CLIENT_TYPE, ANDROID_CLIENT_TYPE);
        applyNonNullHeader(request, HEADER_CLIENT_VERSION, CrashlyticsCore.getVersion());
        applyNonNullHeader(request, "Accept", ACCEPT_JSON_VALUE);
        applyNonNullHeader(request, HEADER_DEVICE_MODEL, requestData.deviceModel);
        applyNonNullHeader(request, HEADER_OS_BUILD_VERSION, requestData.osBuildVersion);
        applyNonNullHeader(request, HEADER_OS_DISPLAY_VERSION, requestData.osDisplayVersion);
        applyNonNullHeader(request, HEADER_INSTALLATION_ID, requestData.installIdProvider.getCrashlyticsInstallId());
        return request;
    }

    private void applyNonNullHeader(HttpGetRequest request, String key, String value) {
        if (value != null) {
            request.header(key, value);
        }
    }
}
