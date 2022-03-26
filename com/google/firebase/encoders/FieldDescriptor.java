package com.google.firebase.encoders;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes3.dex */
public final class FieldDescriptor {
    private final String name;
    private final Map<Class<?>, Object> properties;

    private FieldDescriptor(String name, Map<Class<?>, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return this.name;
    }

    public <T extends Annotation> T getProperty(Class<T> type) {
        return (T) ((Annotation) this.properties.get(type));
    }

    public static FieldDescriptor of(String name) {
        return new FieldDescriptor(name, Collections.emptyMap());
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldDescriptor)) {
            return false;
        }
        FieldDescriptor that = (FieldDescriptor) o;
        if (!this.name.equals(that.name) || !this.properties.equals(that.properties)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.name.hashCode() * 31) + this.properties.hashCode();
    }

    public String toString() {
        return "FieldDescriptor{name=" + this.name + ", properties=" + this.properties.values() + "}";
    }

    /* loaded from: classes3.dex */
    public static final class Builder {
        private final String name;
        private Map<Class<?>, Object> properties = null;

        Builder(String name) {
            this.name = name;
        }

        public <T extends Annotation> Builder withProperty(T value) {
            if (this.properties == null) {
                this.properties = new HashMap();
            }
            this.properties.put(value.annotationType(), value);
            return this;
        }

        public FieldDescriptor build() {
            Map map;
            String str = this.name;
            Map<Class<?>, Object> map2 = this.properties;
            if (map2 == null) {
                map = Collections.emptyMap();
            } else {
                map = Collections.unmodifiableMap(new HashMap(map2));
            }
            return new FieldDescriptor(str, map);
        }
    }
}
