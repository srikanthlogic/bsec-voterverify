package androidx.camera.core.internal;

import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ReadableConfig;
/* loaded from: classes.dex */
public interface TargetConfig<T> extends ReadableConfig {
    public static final Config.Option<String> OPTION_TARGET_NAME = Config.Option.create("camerax.core.target.name", String.class);
    public static final Config.Option<Class<?>> OPTION_TARGET_CLASS = Config.Option.create("camerax.core.target.class", Class.class);

    /* loaded from: classes.dex */
    public interface Builder<T, B> {
        B setTargetClass(Class<T> cls);

        B setTargetName(String str);
    }

    default Class<T> getTargetClass(Class<T> valueIfMissing) {
        return (Class) retrieveOption(OPTION_TARGET_CLASS, valueIfMissing);
    }

    default Class<T> getTargetClass() {
        return (Class) retrieveOption(OPTION_TARGET_CLASS);
    }

    default String getTargetName(String valueIfMissing) {
        return (String) retrieveOption(OPTION_TARGET_NAME, valueIfMissing);
    }

    default String getTargetName() {
        return (String) retrieveOption(OPTION_TARGET_NAME);
    }
}
