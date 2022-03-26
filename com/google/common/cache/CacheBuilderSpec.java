package com.google.common.cache;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.cache.LocalCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class CacheBuilderSpec {
    private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
    private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
    private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder().put("initialCapacity", new InitialCapacityParser()).put("maximumSize", new MaximumSizeParser()).put("maximumWeight", new MaximumWeightParser()).put("concurrencyLevel", new ConcurrencyLevelParser()).put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK)).put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT)).put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK)).put("recordStats", new RecordStatsParser()).put("expireAfterAccess", new AccessDurationParser()).put("expireAfterWrite", new WriteDurationParser()).put("refreshAfterWrite", new RefreshDurationParser()).put("refreshInterval", new RefreshDurationParser()).build();
    long accessExpirationDuration;
    @NullableDecl
    TimeUnit accessExpirationTimeUnit;
    @NullableDecl
    Integer concurrencyLevel;
    @NullableDecl
    Integer initialCapacity;
    @NullableDecl
    LocalCache.Strength keyStrength;
    @NullableDecl
    Long maximumSize;
    @NullableDecl
    Long maximumWeight;
    @NullableDecl
    Boolean recordStats;
    long refreshDuration;
    @NullableDecl
    TimeUnit refreshTimeUnit;
    private final String specification;
    @NullableDecl
    LocalCache.Strength valueStrength;
    long writeExpirationDuration;
    @NullableDecl
    TimeUnit writeExpirationTimeUnit;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface ValueParser {
        void parse(CacheBuilderSpec cacheBuilderSpec, String str, @NullableDecl String str2);
    }

    private CacheBuilderSpec(String specification) {
        this.specification = specification;
    }

    public static CacheBuilderSpec parse(String cacheBuilderSpecification) {
        CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
        if (!cacheBuilderSpecification.isEmpty()) {
            for (String keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
                List<String> keyAndValue = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
                Preconditions.checkArgument(!keyAndValue.isEmpty(), "blank key-value pair");
                boolean z = false;
                Preconditions.checkArgument(keyAndValue.size() <= 2, "key-value pair %s with more than one equals sign", keyValuePair);
                String key = keyAndValue.get(0);
                ValueParser valueParser = VALUE_PARSERS.get(key);
                if (valueParser != null) {
                    z = true;
                }
                Preconditions.checkArgument(z, "unknown key %s", key);
                valueParser.parse(spec, key, keyAndValue.size() == 1 ? null : keyAndValue.get(1));
            }
        }
        return spec;
    }

    public static CacheBuilderSpec disableCaching() {
        return parse("maximumSize=0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CacheBuilder<Object, Object> toCacheBuilder() {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        Integer num = this.initialCapacity;
        if (num != null) {
            builder.initialCapacity(num.intValue());
        }
        Long l = this.maximumSize;
        if (l != null) {
            builder.maximumSize(l.longValue());
        }
        Long l2 = this.maximumWeight;
        if (l2 != null) {
            builder.maximumWeight(l2.longValue());
        }
        Integer num2 = this.concurrencyLevel;
        if (num2 != null) {
            builder.concurrencyLevel(num2.intValue());
        }
        if (this.keyStrength != null) {
            if (AnonymousClass1.$SwitchMap$com$google$common$cache$LocalCache$Strength[this.keyStrength.ordinal()] == 1) {
                builder.weakKeys();
            } else {
                throw new AssertionError();
            }
        }
        if (this.valueStrength != null) {
            int i = AnonymousClass1.$SwitchMap$com$google$common$cache$LocalCache$Strength[this.valueStrength.ordinal()];
            if (i == 1) {
                builder.weakValues();
            } else if (i == 2) {
                builder.softValues();
            } else {
                throw new AssertionError();
            }
        }
        Boolean bool = this.recordStats;
        if (bool != null && bool.booleanValue()) {
            builder.recordStats();
        }
        TimeUnit timeUnit = this.writeExpirationTimeUnit;
        if (timeUnit != null) {
            builder.expireAfterWrite(this.writeExpirationDuration, timeUnit);
        }
        TimeUnit timeUnit2 = this.accessExpirationTimeUnit;
        if (timeUnit2 != null) {
            builder.expireAfterAccess(this.accessExpirationDuration, timeUnit2);
        }
        TimeUnit timeUnit3 = this.refreshTimeUnit;
        if (timeUnit3 != null) {
            builder.refreshAfterWrite(this.refreshDuration, timeUnit3);
        }
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.cache.CacheBuilderSpec$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$cache$LocalCache$Strength = new int[LocalCache.Strength.values().length];

        static {
            try {
                $SwitchMap$com$google$common$cache$LocalCache$Strength[LocalCache.Strength.WEAK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$cache$LocalCache$Strength[LocalCache.Strength.SOFT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public String toParsableString() {
        return this.specification;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
    }

    public int hashCode() {
        return Objects.hashCode(this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit));
    }

    public boolean equals(@NullableDecl Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheBuilderSpec)) {
            return false;
        }
        CacheBuilderSpec that = (CacheBuilderSpec) obj;
        if (!Objects.equal(this.initialCapacity, that.initialCapacity) || !Objects.equal(this.maximumSize, that.maximumSize) || !Objects.equal(this.maximumWeight, that.maximumWeight) || !Objects.equal(this.concurrencyLevel, that.concurrencyLevel) || !Objects.equal(this.keyStrength, that.keyStrength) || !Objects.equal(this.valueStrength, that.valueStrength) || !Objects.equal(this.recordStats, that.recordStats) || !Objects.equal(durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) || !Objects.equal(durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) || !Objects.equal(durationInNanos(this.refreshDuration, this.refreshTimeUnit), durationInNanos(that.refreshDuration, that.refreshTimeUnit))) {
            return false;
        }
        return true;
    }

    @NullableDecl
    private static Long durationInNanos(long duration, @NullableDecl TimeUnit unit) {
        if (unit == null) {
            return null;
        }
        return Long.valueOf(unit.toNanos(duration));
    }

    /* loaded from: classes.dex */
    static abstract class IntegerParser implements ValueParser {
        protected abstract void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i);

        IntegerParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, String value) {
            Preconditions.checkArgument(value != null && !value.isEmpty(), "value of key %s omitted", key);
            try {
                parseInteger(spec, Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value), e);
            }
        }
    }

    /* loaded from: classes.dex */
    static abstract class LongParser implements ValueParser {
        protected abstract void parseLong(CacheBuilderSpec cacheBuilderSpec, long j);

        LongParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, String value) {
            Preconditions.checkArgument(value != null && !value.isEmpty(), "value of key %s omitted", key);
            try {
                parseLong(spec, Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value), e);
            }
        }
    }

    /* loaded from: classes.dex */
    static class InitialCapacityParser extends IntegerParser {
        InitialCapacityParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.IntegerParser
        protected void parseInteger(CacheBuilderSpec spec, int value) {
            Preconditions.checkArgument(spec.initialCapacity == null, "initial capacity was already set to ", spec.initialCapacity);
            spec.initialCapacity = Integer.valueOf(value);
        }
    }

    /* loaded from: classes.dex */
    static class MaximumSizeParser extends LongParser {
        MaximumSizeParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.LongParser
        protected void parseLong(CacheBuilderSpec spec, long value) {
            boolean z = true;
            Preconditions.checkArgument(spec.maximumSize == null, "maximum size was already set to ", spec.maximumSize);
            if (spec.maximumWeight != null) {
                z = false;
            }
            Preconditions.checkArgument(z, "maximum weight was already set to ", spec.maximumWeight);
            spec.maximumSize = Long.valueOf(value);
        }
    }

    /* loaded from: classes.dex */
    static class MaximumWeightParser extends LongParser {
        MaximumWeightParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.LongParser
        protected void parseLong(CacheBuilderSpec spec, long value) {
            boolean z = true;
            Preconditions.checkArgument(spec.maximumWeight == null, "maximum weight was already set to ", spec.maximumWeight);
            if (spec.maximumSize != null) {
                z = false;
            }
            Preconditions.checkArgument(z, "maximum size was already set to ", spec.maximumSize);
            spec.maximumWeight = Long.valueOf(value);
        }
    }

    /* loaded from: classes.dex */
    static class ConcurrencyLevelParser extends IntegerParser {
        ConcurrencyLevelParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.IntegerParser
        protected void parseInteger(CacheBuilderSpec spec, int value) {
            Preconditions.checkArgument(spec.concurrencyLevel == null, "concurrency level was already set to ", spec.concurrencyLevel);
            spec.concurrencyLevel = Integer.valueOf(value);
        }
    }

    /* loaded from: classes.dex */
    static class KeyStrengthParser implements ValueParser {
        private final LocalCache.Strength strength;

        public KeyStrengthParser(LocalCache.Strength strength) {
            this.strength = strength;
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, @NullableDecl String value) {
            boolean z = true;
            Preconditions.checkArgument(value == null, "key %s does not take values", key);
            if (spec.keyStrength != null) {
                z = false;
            }
            Preconditions.checkArgument(z, "%s was already set to %s", key, spec.keyStrength);
            spec.keyStrength = this.strength;
        }
    }

    /* loaded from: classes.dex */
    static class ValueStrengthParser implements ValueParser {
        private final LocalCache.Strength strength;

        public ValueStrengthParser(LocalCache.Strength strength) {
            this.strength = strength;
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, @NullableDecl String value) {
            boolean z = true;
            Preconditions.checkArgument(value == null, "key %s does not take values", key);
            if (spec.valueStrength != null) {
                z = false;
            }
            Preconditions.checkArgument(z, "%s was already set to %s", key, spec.valueStrength);
            spec.valueStrength = this.strength;
        }
    }

    /* loaded from: classes.dex */
    static class RecordStatsParser implements ValueParser {
        RecordStatsParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, @NullableDecl String value) {
            boolean z = false;
            Preconditions.checkArgument(value == null, "recordStats does not take values");
            if (spec.recordStats == null) {
                z = true;
            }
            Preconditions.checkArgument(z, "recordStats already set");
            spec.recordStats = true;
        }
    }

    /* loaded from: classes.dex */
    static abstract class DurationParser implements ValueParser {
        protected abstract void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit);

        DurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, String value) {
            TimeUnit timeUnit;
            Preconditions.checkArgument(value != null && !value.isEmpty(), "value of key %s omitted", key);
            try {
                char lastChar = value.charAt(value.length() - 1);
                if (lastChar == 'd') {
                    timeUnit = TimeUnit.DAYS;
                } else if (lastChar == 'h') {
                    timeUnit = TimeUnit.HOURS;
                } else if (lastChar == 'm') {
                    timeUnit = TimeUnit.MINUTES;
                } else if (lastChar == 's') {
                    timeUnit = TimeUnit.SECONDS;
                } else {
                    throw new IllegalArgumentException(CacheBuilderSpec.format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", key, value));
                }
                parseDuration(spec, Long.parseLong(value.substring(0, value.length() - 1)), timeUnit);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value));
            }
        }
    }

    /* loaded from: classes.dex */
    static class AccessDurationParser extends DurationParser {
        AccessDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.accessExpirationTimeUnit == null, "expireAfterAccess already set");
            spec.accessExpirationDuration = duration;
            spec.accessExpirationTimeUnit = unit;
        }
    }

    /* loaded from: classes.dex */
    static class WriteDurationParser extends DurationParser {
        WriteDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.writeExpirationTimeUnit == null, "expireAfterWrite already set");
            spec.writeExpirationDuration = duration;
            spec.writeExpirationTimeUnit = unit;
        }
    }

    /* loaded from: classes.dex */
    static class RefreshDurationParser extends DurationParser {
        RefreshDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.refreshTimeUnit == null, "refreshAfterWrite already set");
            spec.refreshDuration = duration;
            spec.refreshTimeUnit = unit;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String format(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
}