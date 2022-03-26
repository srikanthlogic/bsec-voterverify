package com.google.firebase.crashlytics;

import java.util.HashMap;
import java.util.Map;
/* loaded from: classes3.dex */
public class CustomKeysAndValues {
    final Map<String, String> keysAndValues;

    /* loaded from: classes3.dex */
    public static class Builder {
        private Map<String, String> keysAndValues = new HashMap();

        public Builder putString(String key, String value) {
            this.keysAndValues.put(key, value);
            return this;
        }

        public Builder putBoolean(String key, boolean value) {
            this.keysAndValues.put(key, Boolean.toString(value));
            return this;
        }

        public Builder putDouble(String key, double value) {
            this.keysAndValues.put(key, Double.toString(value));
            return this;
        }

        public Builder putFloat(String key, float value) {
            this.keysAndValues.put(key, Float.toString(value));
            return this;
        }

        public Builder putLong(String key, long value) {
            this.keysAndValues.put(key, Long.toString(value));
            return this;
        }

        public Builder putInt(String key, int value) {
            this.keysAndValues.put(key, Integer.toString(value));
            return this;
        }

        public CustomKeysAndValues build() {
            return new CustomKeysAndValues(this);
        }
    }

    CustomKeysAndValues(Builder builder) {
        this.keysAndValues = builder.keysAndValues;
    }
}
