package androidx.camera.core.impl;

import android.util.ArrayMap;
import androidx.camera.core.impl.Config;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class OptionsBundle implements Config {
    protected final TreeMap<Config.Option<?>, Map<Config.OptionPriority, Object>> mOptions;
    protected static final Comparator<Config.Option<?>> ID_COMPARE = $$Lambda$OptionsBundle$eYBKifCst1YbBGv195jrqZXdLA.INSTANCE;
    private static final OptionsBundle EMPTY_BUNDLE = new OptionsBundle(new TreeMap(ID_COMPARE));

    public OptionsBundle(TreeMap<Config.Option<?>, Map<Config.OptionPriority, Object>> options) {
        this.mOptions = options;
    }

    public static OptionsBundle from(Config otherConfig) {
        if (OptionsBundle.class.equals(otherConfig.getClass())) {
            return (OptionsBundle) otherConfig;
        }
        TreeMap<Config.Option<?>, Map<Config.OptionPriority, Object>> persistentOptions = new TreeMap<>(ID_COMPARE);
        for (Config.Option<?> opt : otherConfig.listOptions()) {
            Set<Config.OptionPriority> priorities = otherConfig.getPriorities(opt);
            Map<Config.OptionPriority, Object> valuesMap = new ArrayMap<>();
            for (Config.OptionPriority priority : priorities) {
                valuesMap.put(priority, otherConfig.retrieveOptionWithPriority(opt, priority));
            }
            persistentOptions.put(opt, valuesMap);
        }
        return new OptionsBundle(persistentOptions);
    }

    public static OptionsBundle emptyBundle() {
        return EMPTY_BUNDLE;
    }

    @Override // androidx.camera.core.impl.Config
    public Set<Config.Option<?>> listOptions() {
        return Collections.unmodifiableSet(this.mOptions.keySet());
    }

    @Override // androidx.camera.core.impl.Config
    public boolean containsOption(Config.Option<?> id) {
        return this.mOptions.containsKey(id);
    }

    @Override // androidx.camera.core.impl.Config
    public <ValueT> ValueT retrieveOption(Config.Option<ValueT> id) {
        Map<Config.OptionPriority, Object> values = this.mOptions.get(id);
        if (values != null) {
            return (ValueT) values.get((Config.OptionPriority) Collections.min(values.keySet()));
        }
        throw new IllegalArgumentException("Option does not exist: " + id);
    }

    @Override // androidx.camera.core.impl.Config
    public <ValueT> ValueT retrieveOption(Config.Option<ValueT> id, ValueT valueIfMissing) {
        try {
            return (ValueT) retrieveOption(id);
        } catch (IllegalArgumentException e) {
            return valueIfMissing;
        }
    }

    @Override // androidx.camera.core.impl.Config
    public <ValueT> ValueT retrieveOptionWithPriority(Config.Option<ValueT> id, Config.OptionPriority priority) {
        Map<Config.OptionPriority, Object> values = this.mOptions.get(id);
        if (values == null) {
            throw new IllegalArgumentException("Option does not exist: " + id);
        } else if (values.containsKey(priority)) {
            return (ValueT) values.get(priority);
        } else {
            throw new IllegalArgumentException("Option does not exist: " + id + " with priority=" + priority);
        }
    }

    @Override // androidx.camera.core.impl.Config
    public Config.OptionPriority getOptionPriority(Config.Option<?> opt) {
        Map<Config.OptionPriority, Object> values = this.mOptions.get(opt);
        if (values != null) {
            return (Config.OptionPriority) Collections.min(values.keySet());
        }
        throw new IllegalArgumentException("Option does not exist: " + opt);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x001a  */
    @Override // androidx.camera.core.impl.Config
    /* Code decompiled incorrectly, please refer to instructions dump */
    public void findOptions(String idStem, Config.OptionMatcher matcher) {
        for (Map.Entry<Config.Option<?>, Map<Config.OptionPriority, Object>> entry : this.mOptions.tailMap(Config.Option.create(idStem, Void.class)).entrySet()) {
            if (!entry.getKey().getId().startsWith(idStem) || !matcher.onOptionMatched(entry.getKey())) {
                return;
            }
            while (r1.hasNext()) {
            }
        }
    }

    @Override // androidx.camera.core.impl.Config
    public Set<Config.OptionPriority> getPriorities(Config.Option<?> opt) {
        Map<Config.OptionPriority, Object> values = this.mOptions.get(opt);
        if (values == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(values.keySet());
    }
}
