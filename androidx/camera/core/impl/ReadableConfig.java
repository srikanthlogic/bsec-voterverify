package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import java.util.Set;
/* loaded from: classes.dex */
public interface ReadableConfig extends Config {
    Config getConfig();

    @Override // androidx.camera.core.impl.Config
    default boolean containsOption(Config.Option<?> id) {
        return getConfig().containsOption(id);
    }

    @Override // androidx.camera.core.impl.Config
    default <ValueT> ValueT retrieveOption(Config.Option<ValueT> id) {
        return (ValueT) getConfig().retrieveOption(id);
    }

    @Override // androidx.camera.core.impl.Config
    default <ValueT> ValueT retrieveOption(Config.Option<ValueT> id, ValueT valueIfMissing) {
        return (ValueT) getConfig().retrieveOption(id, valueIfMissing);
    }

    @Override // androidx.camera.core.impl.Config
    default void findOptions(String idSearchString, Config.OptionMatcher matcher) {
        getConfig().findOptions(idSearchString, matcher);
    }

    @Override // androidx.camera.core.impl.Config
    default Set<Config.Option<?>> listOptions() {
        return getConfig().listOptions();
    }

    @Override // androidx.camera.core.impl.Config
    default <ValueT> ValueT retrieveOptionWithPriority(Config.Option<ValueT> id, Config.OptionPriority priority) {
        return (ValueT) getConfig().retrieveOptionWithPriority(id, priority);
    }

    @Override // androidx.camera.core.impl.Config
    default Config.OptionPriority getOptionPriority(Config.Option<?> opt) {
        return getConfig().getOptionPriority(opt);
    }

    @Override // androidx.camera.core.impl.Config
    default Set<Config.OptionPriority> getPriorities(Config.Option<?> option) {
        return getConfig().getPriorities(option);
    }
}
