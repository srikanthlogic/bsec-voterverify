package androidx.camera.core.impl;

import java.util.Set;
/* loaded from: classes.dex */
public interface Config {

    /* loaded from: classes.dex */
    public interface OptionMatcher {
        boolean onOptionMatched(Option<?> option);
    }

    /* loaded from: classes.dex */
    public enum OptionPriority {
        ALWAYS_OVERRIDE,
        REQUIRED,
        OPTIONAL
    }

    boolean containsOption(Option<?> option);

    void findOptions(String str, OptionMatcher optionMatcher);

    OptionPriority getOptionPriority(Option<?> option);

    Set<OptionPriority> getPriorities(Option<?> option);

    Set<Option<?>> listOptions();

    <ValueT> ValueT retrieveOption(Option<ValueT> option);

    <ValueT> ValueT retrieveOption(Option<ValueT> option, ValueT valuet);

    <ValueT> ValueT retrieveOptionWithPriority(Option<ValueT> option, OptionPriority optionPriority);

    /* loaded from: classes.dex */
    public static abstract class Option<T> {
        public abstract String getId();

        public abstract Object getToken();

        public abstract Class<T> getValueClass();

        public static <T> Option<T> create(String id, Class<?> valueClass) {
            return create(id, valueClass, null);
        }

        public static <T> Option<T> create(String id, Class<?> valueClass, Object token) {
            return new AutoValue_Config_Option(id, valueClass, token);
        }
    }

    static boolean hasConflict(OptionPriority priority1, OptionPriority priority2) {
        if (priority1 == OptionPriority.ALWAYS_OVERRIDE && priority2 == OptionPriority.ALWAYS_OVERRIDE) {
            return true;
        }
        if (priority1 == OptionPriority.REQUIRED && priority2 == OptionPriority.REQUIRED) {
            return true;
        }
        return false;
    }
}
