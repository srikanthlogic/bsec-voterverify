package com.google.firebase.crashlytics.internal.analytics;

import android.os.Bundle;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbHandler;
import com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbSource;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class BreadcrumbAnalyticsEventReceiver implements AnalyticsEventReceiver, BreadcrumbSource {
    private static final String BREADCRUMB_NAME_KEY = "name";
    private static final String BREADCRUMB_PARAMS_KEY = "parameters";
    private static final String BREADCRUMB_PREFIX = "$A$:";
    private BreadcrumbHandler breadcrumbHandler;

    @Override // com.google.firebase.crashlytics.internal.analytics.AnalyticsEventReceiver
    public void onEvent(String name, Bundle params) {
        BreadcrumbHandler receiver = this.breadcrumbHandler;
        if (receiver != null) {
            try {
                receiver.handleBreadcrumb(BREADCRUMB_PREFIX + serializeEvent(name, params));
            } catch (JSONException e) {
                Logger.getLogger().w("Unable to serialize Firebase Analytics event to breadcrumb.");
            }
        }
    }

    @Override // com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbSource
    public void registerBreadcrumbHandler(BreadcrumbHandler breadcrumbHandler) {
        this.breadcrumbHandler = breadcrumbHandler;
        Logger.getLogger().d("Registered Firebase Analytics event receiver for breadcrumbs");
    }

    private static String serializeEvent(String name, Bundle params) throws JSONException {
        JSONObject enclosingObject = new JSONObject();
        JSONObject paramsObject = new JSONObject();
        for (String key : params.keySet()) {
            paramsObject.put(key, params.get(key));
        }
        enclosingObject.put("name", name);
        enclosingObject.put(BREADCRUMB_PARAMS_KEY, paramsObject);
        return enclosingObject.toString();
    }
}
