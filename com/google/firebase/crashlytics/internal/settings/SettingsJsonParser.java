package com.google.firebase.crashlytics.internal.settings;

import com.google.firebase.crashlytics.internal.common.CurrentTimeProvider;
import com.google.firebase.crashlytics.internal.settings.model.SettingsData;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class SettingsJsonParser {
    private final CurrentTimeProvider currentTimeProvider;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettingsJsonParser(CurrentTimeProvider currentTimeProvider) {
        this.currentTimeProvider = currentTimeProvider;
    }

    public SettingsData parseSettingsJson(JSONObject settingsJson) throws JSONException {
        return getJsonTransformForVersion(settingsJson.getInt("settings_version")).buildFromJson(this.currentTimeProvider, settingsJson);
    }

    private static SettingsJsonTransform getJsonTransformForVersion(int settingsVersion) {
        if (settingsVersion != 3) {
            return new DefaultSettingsJsonTransform();
        }
        return new SettingsV3JsonTransform();
    }
}
