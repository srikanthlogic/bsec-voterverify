package com.google.firebase.crashlytics.internal.settings;

import android.content.Context;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.common.CommonUtils;
import com.google.firebase.crashlytics.internal.persistence.FileStoreImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class CachedSettingsIo {
    private static final String SETTINGS_CACHE_FILENAME = "com.crashlytics.settings.json";
    private final Context context;

    public CachedSettingsIo(Context context) {
        this.context = context;
    }

    private File getSettingsFile() {
        return new File(new FileStoreImpl(this.context).getFilesDir(), SETTINGS_CACHE_FILENAME);
    }

    public JSONObject readCachedSettings() {
        FileInputStream fis;
        try {
            Logger.getLogger().d("Checking for cached settings...");
            fis = null;
            JSONObject toReturn = null;
            try {
                File settingsFile = getSettingsFile();
                if (settingsFile.exists()) {
                    fis = new FileInputStream(settingsFile);
                    toReturn = new JSONObject(CommonUtils.streamToString(fis));
                } else {
                    Logger.getLogger().v("Settings file does not exist.");
                }
            } catch (Exception e) {
                Logger.getLogger().e("Failed to fetch cached settings", e);
            }
            return toReturn;
        } finally {
            CommonUtils.closeOrLog(fis, "Error while closing settings cache file.");
        }
    }

    public void writeCachedSettings(long expiresAtMillis, JSONObject settingsJson) {
        Logger.getLogger().v("Writing settings to cache file...");
        if (settingsJson != null) {
            FileWriter writer = null;
            try {
                try {
                    settingsJson.put("expires_at", expiresAtMillis);
                    writer = new FileWriter(getSettingsFile());
                    writer.write(settingsJson.toString());
                    writer.flush();
                } catch (Exception e) {
                    Logger.getLogger().e("Failed to cache settings", e);
                }
            } finally {
                CommonUtils.closeOrLog(writer, "Failed to close settings writer.");
            }
        }
    }
}
