package com.google.firebase.crashlytics.internal.common;

import java.util.Map;
/* loaded from: classes3.dex */
public class UserMetadata {
    static final int MAX_ATTRIBUTES = 64;
    static final int MAX_ATTRIBUTE_SIZE = 1024;
    static final int MAX_INTERNAL_KEY_SIZE = 8192;
    private String userId = null;
    private final KeysMap customKeys = new KeysMap(64, 1024);
    private final KeysMap internalKeys = new KeysMap(64, 8192);

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String identifier) {
        this.userId = this.customKeys.sanitizeAttribute(identifier);
    }

    public Map<String, String> getCustomKeys() {
        return this.customKeys.getKeys();
    }

    public void setCustomKey(String key, String value) {
        this.customKeys.setKey(key, value);
    }

    public void setCustomKeys(Map<String, String> keysAndValues) {
        this.customKeys.setKeys(keysAndValues);
    }

    public Map<String, String> getInternalKeys() {
        return this.internalKeys.getKeys();
    }

    public void setInternalKey(String key, String value) {
        this.internalKeys.setKey(key, value);
    }
}
