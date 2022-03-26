package com.google.common.cache;

import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Ticker;
import com.google.common.cache.AbstractCache;
import com.google.common.cache.LocalCache;
import com.google.errorprone.annotations.CheckReturnValue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class CacheBuilder<K, V> {
    private static final int DEFAULT_CONCURRENCY_LEVEL;
    private static final int DEFAULT_EXPIRATION_NANOS;
    private static final int DEFAULT_INITIAL_CAPACITY;
    private static final int DEFAULT_REFRESH_NANOS;
    static final int UNSET_INT;
    @NullableDecl
    Equivalence<Object> keyEquivalence;
    @NullableDecl
    LocalCache.Strength keyStrength;
    @NullableDecl
    RemovalListener<? super K, ? super V> removalListener;
    @NullableDecl
    Ticker ticker;
    @NullableDecl
    Equivalence<Object> valueEquivalence;
    @NullableDecl
    LocalCache.Strength valueStrength;
    @NullableDecl
    Weigher<? super K, ? super V> weigher;
    static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter() { // from class: com.google.common.cache.CacheBuilder.1
        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordHits(int count) {
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordMisses(int count) {
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordLoadSuccess(long loadTime) {
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordLoadException(long loadTime) {
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordEviction() {
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public CacheStats snapshot() {
            return CacheBuilder.EMPTY_STATS;
        }
    });
    static final CacheStats EMPTY_STATS = new CacheStats(0, 0, 0, 0, 0, 0);
    static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>() { // from class: com.google.common.cache.CacheBuilder.2
        @Override // com.google.common.base.Supplier
        public AbstractCache.StatsCounter get() {
            return new AbstractCache.SimpleStatsCounter();
        }
    };
    static final Ticker NULL_TICKER = new Ticker() { // from class: com.google.common.cache.CacheBuilder.3
        @Override // com.google.common.base.Ticker
        public long read() {
            return 0;
        }
    };
    private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
    boolean strictParsing = true;
    int initialCapacity = -1;
    int concurrencyLevel = -1;
    long maximumSize = -1;
    long maximumWeight = -1;
    long expireAfterWriteNanos = -1;
    long expireAfterAccessNanos = -1;
    long refreshNanos = -1;
    Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum NullListener implements RemovalListener<Object, Object> {
        INSTANCE;

        @Override // com.google.common.cache.RemovalListener
        public void onRemoval(RemovalNotification<Object, Object> notification) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum OneWeigher implements Weigher<Object, Object> {
        INSTANCE;

        @Override // com.google.common.cache.Weigher
        public int weigh(Object key, Object value) {
            return 1;
        }
    }

    private CacheBuilder() {
    }

    public static CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder<>();
    }

    public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec) {
        return spec.toCacheBuilder().lenientParsing();
    }

    public static CacheBuilder<Object, Object> from(String spec) {
        return from(CacheBuilderSpec.parse(spec));
    }

    CacheBuilder<K, V> lenientParsing() {
        this.strictParsing = false;
        return this;
    }

    public CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
        return this;
    }

    public Equivalence<Object> getKeyEquivalence() {
        return (Equivalence) MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
    }

    public CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
        Preconditions.checkState(this.valueEquivalence == null, "value equivalence was already set to %s", this.valueEquivalence);
        this.valueEquivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
        return this;
    }

    public Equivalence<Object> getValueEquivalence() {
        return (Equivalence) MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
    }

    public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
        boolean z = true;
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
        if (initialCapacity < 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.initialCapacity = initialCapacity;
        return this;
    }

    public int getInitialCapacity() {
        int i = this.initialCapacity;
        if (i == -1) {
            return 16;
        }
        return i;
    }

    public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
        boolean z = true;
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
        if (concurrencyLevel <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }

    public int getConcurrencyLevel() {
        int i = this.concurrencyLevel;
        if (i == -1) {
            return 4;
        }
        return i;
    }

    public CacheBuilder<K, V> maximumSize(long maximumSize) {
        boolean z = true;
        Preconditions.checkState(this.maximumSize == -1, "maximum size was already set to %s", this.maximumSize);
        Preconditions.checkState(this.maximumWeight == -1, "maximum weight was already set to %s", this.maximumWeight);
        Preconditions.checkState(this.weigher == null, "maximum size can not be combined with weigher");
        if (maximumSize < 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "maximum size must not be negative");
        this.maximumSize = maximumSize;
        return this;
    }

    public CacheBuilder<K, V> maximumWeight(long maximumWeight) {
        boolean z = true;
        Preconditions.checkState(this.maximumWeight == -1, "maximum weight was already set to %s", this.maximumWeight);
        Preconditions.checkState(this.maximumSize == -1, "maximum size was already set to %s", this.maximumSize);
        this.maximumWeight = maximumWeight;
        if (maximumWeight < 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "maximum weight must not be negative");
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
        boolean z = true;
        Preconditions.checkState(this.weigher == null);
        if (this.strictParsing) {
            if (this.maximumSize != -1) {
                z = false;
            }
            Preconditions.checkState(z, "weigher can not be combined with maximum size", this.maximumSize);
        }
        this.weigher = (Weigher) Preconditions.checkNotNull(weigher);
        return this;
    }

    public long getMaximumWeight() {
        if (this.expireAfterWriteNanos == 0 || this.expireAfterAccessNanos == 0) {
            return 0;
        }
        return this.weigher == null ? this.maximumSize : this.maximumWeight;
    }

    public <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
        return (Weigher) MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
    }

    public CacheBuilder<K, V> weakKeys() {
        return setKeyStrength(LocalCache.Strength.WEAK);
    }

    public CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = (LocalCache.Strength) Preconditions.checkNotNull(strength);
        return this;
    }

    public LocalCache.Strength getKeyStrength() {
        return (LocalCache.Strength) MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
    }

    public CacheBuilder<K, V> weakValues() {
        return setValueStrength(LocalCache.Strength.WEAK);
    }

    public CacheBuilder<K, V> softValues() {
        return setValueStrength(LocalCache.Strength.SOFT);
    }

    public CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
        this.valueStrength = (LocalCache.Strength) Preconditions.checkNotNull(strength);
        return this;
    }

    public LocalCache.Strength getValueStrength() {
        return (LocalCache.Strength) MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
    }

    public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
        boolean z = true;
        Preconditions.checkState(this.expireAfterWriteNanos == -1, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
        if (duration < 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "duration cannot be negative: %s %s", duration, unit);
        this.expireAfterWriteNanos = unit.toNanos(duration);
        return this;
    }

    public long getExpireAfterWriteNanos() {
        long j = this.expireAfterWriteNanos;
        if (j == -1) {
            return 0;
        }
        return j;
    }

    public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
        boolean z = true;
        Preconditions.checkState(this.expireAfterAccessNanos == -1, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
        if (duration < 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "duration cannot be negative: %s %s", duration, unit);
        this.expireAfterAccessNanos = unit.toNanos(duration);
        return this;
    }

    public long getExpireAfterAccessNanos() {
        long j = this.expireAfterAccessNanos;
        if (j == -1) {
            return 0;
        }
        return j;
    }

    public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
        Preconditions.checkNotNull(unit);
        boolean z = true;
        Preconditions.checkState(this.refreshNanos == -1, "refresh was already set to %s ns", this.refreshNanos);
        if (duration <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "duration must be positive: %s %s", duration, unit);
        this.refreshNanos = unit.toNanos(duration);
        return this;
    }

    public long getRefreshNanos() {
        long j = this.refreshNanos;
        if (j == -1) {
            return 0;
        }
        return j;
    }

    public CacheBuilder<K, V> ticker(Ticker ticker) {
        Preconditions.checkState(this.ticker == null);
        this.ticker = (Ticker) Preconditions.checkNotNull(ticker);
        return this;
    }

    public Ticker getTicker(boolean recordsTime) {
        Ticker ticker = this.ticker;
        if (ticker != null) {
            return ticker;
        }
        return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CheckReturnValue
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
        Preconditions.checkState(this.removalListener == null);
        this.removalListener = (RemovalListener) Preconditions.checkNotNull(listener);
        return this;
    }

    public <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
        return (RemovalListener) MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
    }

    public CacheBuilder<K, V> recordStats() {
        this.statsCounterSupplier = CACHE_STATS_COUNTER;
        return this;
    }

    boolean isRecordingStats() {
        return this.statsCounterSupplier == CACHE_STATS_COUNTER;
    }

    public Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
        return this.statsCounterSupplier;
    }

    public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
        checkWeightWithWeigher();
        return new LocalCache.LocalLoadingCache(this, loader);
    }

    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        checkWeightWithWeigher();
        checkNonLoadingCache();
        return new LocalCache.LocalManualCache(this);
    }

    private void checkNonLoadingCache() {
        Preconditions.checkState(this.refreshNanos == -1, "refreshAfterWrite requires a LoadingCache");
    }

    private void checkWeightWithWeigher() {
        boolean z = true;
        if (this.weigher == null) {
            if (this.maximumWeight != -1) {
                z = false;
            }
            Preconditions.checkState(z, "maximumWeight requires weigher");
        } else if (this.strictParsing) {
            if (this.maximumWeight == -1) {
                z = false;
            }
            Preconditions.checkState(z, "weigher requires maximumWeight");
        } else if (this.maximumWeight == -1) {
            logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
        }
    }

    public String toString() {
        MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
        int i = this.initialCapacity;
        if (i != -1) {
            s.add("initialCapacity", i);
        }
        int i2 = this.concurrencyLevel;
        if (i2 != -1) {
            s.add("concurrencyLevel", i2);
        }
        long j = this.maximumSize;
        if (j != -1) {
            s.add("maximumSize", j);
        }
        long j2 = this.maximumWeight;
        if (j2 != -1) {
            s.add("maximumWeight", j2);
        }
        if (this.expireAfterWriteNanos != -1) {
            s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
        }
        if (this.expireAfterAccessNanos != -1) {
            s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
        }
        LocalCache.Strength strength = this.keyStrength;
        if (strength != null) {
            s.add("keyStrength", Ascii.toLowerCase(strength.toString()));
        }
        LocalCache.Strength strength2 = this.valueStrength;
        if (strength2 != null) {
            s.add("valueStrength", Ascii.toLowerCase(strength2.toString()));
        }
        if (this.keyEquivalence != null) {
            s.addValue("keyEquivalence");
        }
        if (this.valueEquivalence != null) {
            s.addValue("valueEquivalence");
        }
        if (this.removalListener != null) {
            s.addValue("removalListener");
        }
        return s.toString();
    }
}
