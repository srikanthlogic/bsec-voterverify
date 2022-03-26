package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.Priority;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import com.google.android.datatransport.runtime.time.Clock;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_SchedulerConfig extends SchedulerConfig {
    private final Clock clock;
    private final Map<Priority, SchedulerConfig.ConfigValue> values;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_SchedulerConfig(Clock clock, Map<Priority, SchedulerConfig.ConfigValue> values) {
        if (clock != null) {
            this.clock = clock;
            if (values != null) {
                this.values = values;
                return;
            }
            throw new NullPointerException("Null values");
        }
        throw new NullPointerException("Null clock");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig
    public Clock getClock() {
        return this.clock;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig
    public Map<Priority, SchedulerConfig.ConfigValue> getValues() {
        return this.values;
    }

    public String toString() {
        return "SchedulerConfig{clock=" + this.clock + ", values=" + this.values + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SchedulerConfig)) {
            return false;
        }
        SchedulerConfig that = (SchedulerConfig) o;
        if (!this.clock.equals(that.getClock()) || !this.values.equals(that.getValues())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.clock.hashCode()) * 1000003) ^ this.values.hashCode();
    }
}
