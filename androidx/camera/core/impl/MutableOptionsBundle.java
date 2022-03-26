package androidx.camera.core.impl;

import android.util.ArrayMap;
import androidx.camera.core.impl.Config;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
/* loaded from: classes.dex */
public final class MutableOptionsBundle extends OptionsBundle implements MutableConfig {
    private static final Config.OptionPriority DEFAULT_PRIORITY = Config.OptionPriority.OPTIONAL;

    private MutableOptionsBundle(TreeMap<Config.Option<?>, Map<Config.OptionPriority, Object>> persistentOptions) {
        super(persistentOptions);
    }

    public static MutableOptionsBundle create() {
        return new MutableOptionsBundle(new TreeMap(ID_COMPARE));
    }

    public static MutableOptionsBundle from(Config otherConfig) {
        TreeMap<Config.Option<?>, Map<Config.OptionPriority, Object>> persistentOptions = new TreeMap<>(ID_COMPARE);
        for (Config.Option<?> opt : otherConfig.listOptions()) {
            Set<Config.OptionPriority> priorities = otherConfig.getPriorities(opt);
            Map<Config.OptionPriority, Object> valuesMap = new ArrayMap<>();
            for (Config.OptionPriority priority : priorities) {
                valuesMap.put(priority, otherConfig.retrieveOptionWithPriority(opt, priority));
            }
            persistentOptions.put(opt, valuesMap);
        }
        return new MutableOptionsBundle(persistentOptions);
    }

    @Override // androidx.camera.core.impl.MutableConfig
    public <ValueT> ValueT removeOption(Config.Option<ValueT> opt) {
        return (ValueT) this.mOptions.remove(opt);
    }

    @Override // androidx.camera.core.impl.MutableConfig
    public <ValueT> void insertOption(Config.Option<ValueT> opt, ValueT value) {
        insertOption(opt, DEFAULT_PRIORITY, value);
    }

    @Override // androidx.camera.core.impl.MutableConfig
    public <ValueT> void insertOption(Config.Option<ValueT> opt, Config.OptionPriority priority, ValueT value) {
        Map<Config.OptionPriority, Object> values = (Map) this.mOptions.get(opt);
        if (values == null) {
            ArrayMap arrayMap = new ArrayMap();
            this.mOptions.put(opt, arrayMap);
            arrayMap.put(priority, value);
            return;
        }
        Config.OptionPriority priority1 = (Config.OptionPriority) Collections.min(values.keySet());
        if (values.get(priority1).equals(value) || !Config.hasConflict(priority1, priority)) {
            values.put(priority, value);
            return;
        }
        throw new IllegalArgumentException("Option values conflicts: " + opt.getId() + ", existing value (" + priority1 + ")=" + values.get(priority1) + ", conflicting (" + priority + ")=" + value);
    }
}
