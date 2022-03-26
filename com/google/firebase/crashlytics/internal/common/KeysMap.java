package com.google.firebase.crashlytics.internal.common;

import com.google.firebase.crashlytics.internal.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes3.dex */
public class KeysMap {
    private final Map<String, String> keys = new HashMap();
    private final int maxEntries;
    private final int maxEntryLength;

    public KeysMap(int maxEntries, int maxEntryLength) {
        this.maxEntries = maxEntries;
        this.maxEntryLength = maxEntryLength;
    }

    public synchronized Map<String, String> getKeys() {
        return Collections.unmodifiableMap(new HashMap(this.keys));
    }

    public synchronized void setKey(String key, String value) {
        String sanitizedKey = sanitizeKey(key);
        if (this.keys.size() >= this.maxEntries && !this.keys.containsKey(sanitizedKey)) {
            Logger logger = Logger.getLogger();
            logger.w("Ignored entry \"" + key + "\" when adding custom keys. Maximum allowable: " + this.maxEntries);
        }
        this.keys.put(sanitizedKey, value == null ? "" : sanitizeAttribute(value));
    }

    public synchronized void setKeys(Map<String, String> keysAndValues) {
        int nOverLimit = 0;
        for (Map.Entry<String, String> entry : keysAndValues.entrySet()) {
            String sanitizedKey = sanitizeKey(entry.getKey());
            if (this.keys.size() >= this.maxEntries && !this.keys.containsKey(sanitizedKey)) {
                nOverLimit++;
            }
            String value = entry.getValue();
            this.keys.put(sanitizedKey, value == null ? "" : sanitizeAttribute(value));
        }
        if (nOverLimit > 0) {
            Logger logger = Logger.getLogger();
            logger.w("Ignored " + nOverLimit + " entries when adding custom keys. Maximum allowable: " + this.maxEntries);
        }
    }

    private String sanitizeKey(String key) {
        if (key != null) {
            return sanitizeAttribute(key);
        }
        throw new IllegalArgumentException("Custom attribute key must not be null.");
    }

    public String sanitizeAttribute(String input) {
        if (input == null) {
            return input;
        }
        String input2 = input.trim();
        int length = input2.length();
        int i = this.maxEntryLength;
        if (length > i) {
            return input2.substring(0, i);
        }
        return input2;
    }
}
