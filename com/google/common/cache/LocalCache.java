package com.google.common.cache;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.google.common.cache.AbstractCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public class LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    static final int CONTAINS_VALUE_RETRIES;
    static final int DRAIN_MAX;
    static final int DRAIN_THRESHOLD;
    static final int MAXIMUM_CAPACITY;
    static final int MAX_SEGMENTS;
    final int concurrencyLevel;
    @NullableDecl
    final CacheLoader<? super K, V> defaultLoader;
    final EntryFactory entryFactory;
    @NullableDecl
    Set<Map.Entry<K, V>> entrySet;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final AbstractCache.StatsCounter globalStatsCounter;
    final Equivalence<Object> keyEquivalence;
    @NullableDecl
    Set<K> keySet;
    final Strength keyStrength;
    final long maxWeight;
    final long refreshNanos;
    final RemovalListener<K, V> removalListener;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final int segmentMask;
    final int segmentShift;
    final Segment<K, V>[] segments;
    final Ticker ticker;
    final Equivalence<Object> valueEquivalence;
    final Strength valueStrength;
    @NullableDecl
    Collection<V> values;
    final Weigher<K, V> weigher;
    static final Logger logger = Logger.getLogger(LocalCache.class.getName());
    static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>() { // from class: com.google.common.cache.LocalCache.1
        @Override // com.google.common.cache.LocalCache.ValueReference
        public Object get() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 0;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<Object, Object> getEntry() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<Object, Object> copyFor(ReferenceQueue<Object> queue, @NullableDecl Object value, ReferenceEntry<Object, Object> entry) {
            return this;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public Object waitForValue() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(Object newValue) {
        }
    };
    static final Queue<?> DISCARDING_QUEUE = new AbstractQueue<Object>() { // from class: com.google.common.cache.LocalCache.2
        @Override // java.util.Queue
        public boolean offer(Object o) {
            return true;
        }

        @Override // java.util.Queue
        public Object peek() {
            return null;
        }

        @Override // java.util.Queue
        public Object poll() {
            return null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<Object> iterator() {
            return ImmutableSet.of().iterator();
        }
    };

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* loaded from: classes.dex */
    public static abstract class Strength extends Enum<Strength> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Equivalence<Object> defaultEquivalence();

        abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i);
    }

    /* loaded from: classes.dex */
    public interface ValueReference<K, V> {
        ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, @NullableDecl V v, ReferenceEntry<K, V> referenceEntry);

        @NullableDecl
        V get();

        @NullableDecl
        ReferenceEntry<K, V> getEntry();

        int getWeight();

        boolean isActive();

        boolean isLoading();

        void notifyNewValue(@NullableDecl V v);

        V waitForValue() throws ExecutionException;
    }

    LocalCache(CacheBuilder<? super K, ? super V> builder, @NullableDecl CacheLoader<? super K, V> loader) {
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
        this.keyStrength = builder.getKeyStrength();
        this.valueStrength = builder.getValueStrength();
        this.keyEquivalence = builder.getKeyEquivalence();
        this.valueEquivalence = builder.getValueEquivalence();
        this.maxWeight = builder.getMaximumWeight();
        this.weigher = (Weigher<K, V>) builder.getWeigher();
        this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
        this.refreshNanos = builder.getRefreshNanos();
        this.removalListener = (RemovalListener<K, V>) builder.getRemovalListener();
        this.removalNotificationQueue = this.removalListener == CacheBuilder.NullListener.INSTANCE ? discardingQueue() : new ConcurrentLinkedQueue<>();
        this.ticker = builder.getTicker(recordsTime());
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, usesAccessEntries(), usesWriteEntries());
        this.globalStatsCounter = (AbstractCache.StatsCounter) builder.getStatsCounterSupplier().get();
        this.defaultLoader = loader;
        int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
        if (evictsBySize() && !customWeigher()) {
            initialCapacity = (int) Math.min((long) initialCapacity, this.maxWeight);
        }
        int segmentShift = 0;
        int segmentCount = 1;
        while (segmentCount < this.concurrencyLevel && (!evictsBySize() || ((long) (segmentCount * 20)) <= this.maxWeight)) {
            segmentShift++;
            segmentCount <<= 1;
        }
        this.segmentShift = 32 - segmentShift;
        this.segmentMask = segmentCount - 1;
        this.segments = newSegmentArray(segmentCount);
        int segmentCapacity = initialCapacity / segmentCount;
        int segmentSize = 1;
        while (segmentSize < (segmentCapacity * segmentCount < initialCapacity ? segmentCapacity + 1 : segmentCapacity)) {
            segmentSize <<= 1;
        }
        if (evictsBySize()) {
            long j = this.maxWeight;
            long maxSegmentWeight = (j / ((long) segmentCount)) + 1;
            long remainder = j % ((long) segmentCount);
            for (int i = 0; i < this.segments.length; i++) {
                if (((long) i) == remainder) {
                    maxSegmentWeight--;
                }
                this.segments[i] = createSegment(segmentSize, maxSegmentWeight, (AbstractCache.StatsCounter) builder.getStatsCounterSupplier().get());
            }
            return;
        }
        int i2 = 0;
        while (true) {
            Segment<K, V>[] segmentArr = this.segments;
            if (i2 < segmentArr.length) {
                segmentArr[i2] = createSegment(segmentSize, -1, (AbstractCache.StatsCounter) builder.getStatsCounterSupplier().get());
                i2++;
            } else {
                return;
            }
        }
    }

    boolean evictsBySize() {
        return this.maxWeight >= 0;
    }

    boolean customWeigher() {
        return this.weigher != CacheBuilder.OneWeigher.INSTANCE;
    }

    boolean expires() {
        return expiresAfterWrite() || expiresAfterAccess();
    }

    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0;
    }

    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0;
    }

    boolean refreshes() {
        return this.refreshNanos > 0;
    }

    boolean usesAccessQueue() {
        return expiresAfterAccess() || evictsBySize();
    }

    boolean usesWriteQueue() {
        return expiresAfterWrite();
    }

    boolean recordsWrite() {
        return expiresAfterWrite() || refreshes();
    }

    boolean recordsAccess() {
        return expiresAfterAccess();
    }

    boolean recordsTime() {
        return recordsWrite() || recordsAccess();
    }

    boolean usesWriteEntries() {
        return usesWriteQueue() || recordsWrite();
    }

    boolean usesAccessEntries() {
        return usesAccessQueue() || recordsAccess();
    }

    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }

    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* loaded from: classes.dex */
    public static abstract class EntryFactory extends Enum<EntryFactory> {
        static final int ACCESS_MASK;
        static final int WEAK_MASK;
        static final int WRITE_MASK;
        static final EntryFactory[] factories = {STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};

        abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @NullableDecl ReferenceEntry<K, V> referenceEntry);

        /* JADX WARN: Multi-variable type inference failed */
        static EntryFactory getFactory(Strength keyStrength, boolean usesAccessQueue, boolean usesWriteQueue) {
            int i = 0;
            int i2 = (keyStrength == Strength.WEAK ? 4 : 0) | (usesAccessQueue ? 1 : 0);
            if (usesWriteQueue) {
                i = 2;
            }
            return factories[i2 | i];
        }

        <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
            return newEntry(segment, original.getKey(), original.getHash(), newNext);
        }

        <K, V> void copyAccessEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
            newEntry.setAccessTime(original.getAccessTime());
            LocalCache.connectAccessOrder(original.getPreviousInAccessQueue(), newEntry);
            LocalCache.connectAccessOrder(newEntry, original.getNextInAccessQueue());
            LocalCache.nullifyAccessOrder(original);
        }

        <K, V> void copyWriteEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
            newEntry.setWriteTime(original.getWriteTime());
            LocalCache.connectWriteOrder(original.getPreviousInWriteQueue(), newEntry);
            LocalCache.connectWriteOrder(newEntry, original.getNextInWriteQueue());
            LocalCache.nullifyWriteOrder(original);
        }
    }

    static <K, V> ValueReference<K, V> unset() {
        return (ValueReference<K, V>) UNSET;
    }

    /* loaded from: classes.dex */
    public enum NullEntry implements ReferenceEntry<Object, Object> {
        INSTANCE;

        @Override // com.google.common.cache.ReferenceEntry
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<Object, Object> valueReference) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            return 0;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public Object getKey() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return 0;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setAccessTime(long time) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getNextInAccessQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<Object, Object> next) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> previous) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return 0;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setWriteTime(long time) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getNextInWriteQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<Object, Object> next) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> previous) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V> {
        AbstractReferenceEntry() {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public K getKey() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setAccessTime(long time) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setWriteTime(long time) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
    }

    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return NullEntry.INSTANCE;
    }

    static <E> Queue<E> discardingQueue() {
        return (Queue<E>) DISCARDING_QUEUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class StrongEntry<K, V> extends AbstractReferenceEntry<K, V> {
        final int hash;
        final K key;
        @NullableDecl
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

        StrongEntry(K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public int getHash() {
            return this.hash;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    /* loaded from: classes.dex */
    static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();

        StrongAccessEntry(K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long time) {
            this.accessTime = time;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }
    }

    /* loaded from: classes.dex */
    static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> {
        volatile long writeTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();

        StrongWriteEntry(K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long time) {
            this.writeTime = time;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }

    /* loaded from: classes.dex */
    static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();

        StrongAccessWriteEntry(K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long time) {
            this.accessTime = time;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long time) {
            this.writeTime = time;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        @NullableDecl
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

        WeakEntry(ReferenceQueue<K> queue, K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            super(key, queue);
            this.hash = hash;
            this.next = next;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public K getKey() {
            return (K) get();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setAccessTime(long time) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setWriteTime(long time) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            return this.hash;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    /* loaded from: classes.dex */
    static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();

        WeakAccessEntry(ReferenceQueue<K> queue, K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long time) {
            this.accessTime = time;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }
    }

    /* loaded from: classes.dex */
    static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> {
        volatile long writeTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();

        WeakWriteEntry(ReferenceQueue<K> queue, K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long time) {
            this.writeTime = time;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }

    /* loaded from: classes.dex */
    static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();

        WeakAccessWriteEntry(ReferenceQueue<K> queue, K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long time) {
            this.accessTime = time;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long time) {
            this.writeTime = time;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        WeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
            super(referent, queue);
            this.entry = entry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V newValue) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new WeakValueReference(queue, value, entry);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        SoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
            super(referent, queue);
            this.entry = entry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V newValue) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new SoftValueReference(queue, value, entry);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;

        StrongValueReference(V referent) {
            this.referent = referent;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V get() {
            return this.referent;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return this;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V newValue) {
        }
    }

    /* loaded from: classes.dex */
    static final class WeightedWeakValueReference<K, V> extends WeakValueReference<K, V> {
        final int weight;

        WeightedWeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
            super(queue, referent, entry);
            this.weight = weight;
        }

        @Override // com.google.common.cache.LocalCache.WeakValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }

        @Override // com.google.common.cache.LocalCache.WeakValueReference, com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new WeightedWeakValueReference(queue, value, entry, this.weight);
        }
    }

    /* loaded from: classes.dex */
    static final class WeightedSoftValueReference<K, V> extends SoftValueReference<K, V> {
        final int weight;

        WeightedSoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
            super(queue, referent, entry);
            this.weight = weight;
        }

        @Override // com.google.common.cache.LocalCache.SoftValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }

        @Override // com.google.common.cache.LocalCache.SoftValueReference, com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new WeightedSoftValueReference(queue, value, entry, this.weight);
        }
    }

    /* loaded from: classes.dex */
    static final class WeightedStrongValueReference<K, V> extends StrongValueReference<K, V> {
        final int weight;

        WeightedStrongValueReference(V referent, int weight) {
            super(referent);
            this.weight = weight;
        }

        @Override // com.google.common.cache.LocalCache.StrongValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }
    }

    static int rehash(int h) {
        int h2 = h + ((h << 15) ^ -12931);
        int h3 = h2 ^ (h2 >>> 10);
        int h4 = h3 + (h3 << 3);
        int h5 = h4 ^ (h4 >>> 6);
        int h6 = h5 + (h5 << 2) + (h5 << 14);
        return (h6 >>> 16) ^ h6;
    }

    ReferenceEntry<K, V> newEntry(K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
        Segment<K, V> segment = segmentFor(hash);
        segment.lock();
        try {
            return segment.newEntry(key, hash, next);
        } finally {
            segment.unlock();
        }
    }

    ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
        return segmentFor(original.getHash()).copyEntry(original, newNext);
    }

    /* JADX WARN: Multi-variable type inference failed */
    ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value, int weight) {
        return this.valueStrength.referenceValue(segmentFor(entry.getHash()), entry, Preconditions.checkNotNull(value), weight);
    }

    int hash(@NullableDecl Object key) {
        return rehash(this.keyEquivalence.hash(key));
    }

    void reclaimValue(ValueReference<K, V> valueReference) {
        ReferenceEntry<K, V> entry = valueReference.getEntry();
        int hash = entry.getHash();
        segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }

    void reclaimKey(ReferenceEntry<K, V> entry) {
        int hash = entry.getHash();
        segmentFor(hash).reclaimKey(entry, hash);
    }

    boolean isLive(ReferenceEntry<K, V> entry, long now) {
        return segmentFor(entry.getHash()).getLiveValue(entry, now) != null;
    }

    Segment<K, V> segmentFor(int hash) {
        return this.segments[(hash >>> this.segmentShift) & this.segmentMask];
    }

    Segment<K, V> createSegment(int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter) {
        return new Segment<>(this, initialCapacity, maxSegmentWeight, statsCounter);
    }

    @NullableDecl
    V getLiveValue(ReferenceEntry<K, V> entry, long now) {
        V value;
        if (entry.getKey() == null || (value = entry.getValueReference().get()) == null || isExpired(entry, now)) {
            return null;
        }
        return value;
    }

    boolean isExpired(ReferenceEntry<K, V> entry, long now) {
        Preconditions.checkNotNull(entry);
        if (expiresAfterAccess() && now - entry.getAccessTime() >= this.expireAfterAccessNanos) {
            return true;
        }
        if (!expiresAfterWrite() || now - entry.getWriteTime() < this.expireAfterWriteNanos) {
            return false;
        }
        return true;
    }

    static <K, V> void connectAccessOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
        previous.setNextInAccessQueue(next);
        next.setPreviousInAccessQueue(previous);
    }

    static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> nulled) {
        ReferenceEntry<K, V> nullEntry = nullEntry();
        nulled.setNextInAccessQueue(nullEntry);
        nulled.setPreviousInAccessQueue(nullEntry);
    }

    static <K, V> void connectWriteOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
        previous.setNextInWriteQueue(next);
        next.setPreviousInWriteQueue(previous);
    }

    static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> nulled) {
        ReferenceEntry<K, V> nullEntry = nullEntry();
        nulled.setNextInWriteQueue(nullEntry);
        nulled.setPreviousInWriteQueue(nullEntry);
    }

    void processPendingNotifications() {
        while (true) {
            RemovalNotification<K, V> notification = this.removalNotificationQueue.poll();
            if (notification != null) {
                try {
                    this.removalListener.onRemoval(notification);
                } catch (Throwable e) {
                    logger.log(Level.WARNING, "Exception thrown by removal listener", e);
                }
            } else {
                return;
            }
        }
    }

    final Segment<K, V>[] newSegmentArray(int ssize) {
        return new Segment[ssize];
    }

    /* loaded from: classes.dex */
    public static class Segment<K, V> extends ReentrantLock {
        final Queue<ReferenceEntry<K, V>> accessQueue;
        volatile int count;
        @NullableDecl
        final ReferenceQueue<K> keyReferenceQueue;
        final LocalCache<K, V> map;
        final long maxSegmentWeight;
        int modCount;
        final AtomicInteger readCount = new AtomicInteger();
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AbstractCache.StatsCounter statsCounter;
        @NullableDecl
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        int threshold;
        long totalWeight;
        @NullableDecl
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> writeQueue;

        Segment(LocalCache<K, V> map, int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter) {
            Queue<ReferenceEntry<K, V>> queue;
            Queue<ReferenceEntry<K, V>> queue2;
            Queue<ReferenceEntry<K, V>> queue3;
            this.map = map;
            this.maxSegmentWeight = maxSegmentWeight;
            this.statsCounter = (AbstractCache.StatsCounter) Preconditions.checkNotNull(statsCounter);
            initTable(newEntryArray(initialCapacity));
            ReferenceQueue<V> referenceQueue = null;
            this.keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue<>() : null;
            this.valueReferenceQueue = map.usesValueReferences() ? new ReferenceQueue<>() : referenceQueue;
            if (map.usesAccessQueue()) {
                queue = new ConcurrentLinkedQueue<>();
            } else {
                queue = LocalCache.discardingQueue();
            }
            this.recencyQueue = queue;
            if (map.usesWriteQueue()) {
                queue2 = new WriteQueue<>();
            } else {
                queue2 = LocalCache.discardingQueue();
            }
            this.writeQueue = queue2;
            if (map.usesAccessQueue()) {
                queue3 = new AccessQueue<>();
            } else {
                queue3 = LocalCache.discardingQueue();
            }
            this.accessQueue = queue3;
        }

        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int size) {
            return new AtomicReferenceArray<>(size);
        }

        void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
            this.threshold = (newTable.length() * 3) / 4;
            if (!this.map.customWeigher()) {
                int i = this.threshold;
                if (((long) i) == this.maxSegmentWeight) {
                    this.threshold = i + 1;
                }
            }
            this.table = newTable;
        }

        /* JADX WARN: Multi-variable type inference failed */
        ReferenceEntry<K, V> newEntry(K key, int hash, @NullableDecl ReferenceEntry<K, V> next) {
            return this.map.entryFactory.newEntry(this, Preconditions.checkNotNull(key), hash, next);
        }

        ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
            if (original.getKey() == null) {
                return null;
            }
            ValueReference<K, V> valueReference = original.getValueReference();
            V value = valueReference.get();
            if (value == null && valueReference.isActive()) {
                return null;
            }
            ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
            newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
            return newEntry;
        }

        void setValue(ReferenceEntry<K, V> entry, K key, V value, long now) {
            ValueReference<K, V> previous = entry.getValueReference();
            int weight = this.map.weigher.weigh(key, value);
            Preconditions.checkState(weight >= 0, "Weights must be non-negative");
            entry.setValueReference(this.map.valueStrength.referenceValue(this, entry, value, weight));
            recordWrite(entry, weight, now);
            previous.notifyNewValue(value);
        }

        V get(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
            ReferenceEntry<K, V> e;
            try {
                Preconditions.checkNotNull(key);
                Preconditions.checkNotNull(loader);
                try {
                    if (!(this.count == 0 || (e = getEntry(key, hash)) == null)) {
                        long now = this.map.ticker.read();
                        V value = getLiveValue(e, now);
                        if (value != null) {
                            recordRead(e, now);
                            this.statsCounter.recordHits(1);
                            return scheduleRefresh(e, key, hash, value, now, loader);
                        }
                        ValueReference<K, V> valueReference = e.getValueReference();
                        if (valueReference.isLoading()) {
                            return waitForLoadingValue(e, key, valueReference);
                        }
                    }
                    return lockedGetOrLoad(key, hash, loader);
                } catch (ExecutionException ee) {
                    Throwable cause = ee.getCause();
                    if (cause instanceof Error) {
                        throw new ExecutionError((Error) cause);
                    } else if (cause instanceof RuntimeException) {
                        throw new UncheckedExecutionException(cause);
                    } else {
                        throw ee;
                    }
                }
            } finally {
                postReadCleanup();
            }
        }

        @NullableDecl
        V get(Object key, int hash) {
            try {
                if (this.count != 0) {
                    long now = this.map.ticker.read();
                    ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
                    if (e == null) {
                        return null;
                    }
                    V value = e.getValueReference().get();
                    if (value != null) {
                        recordRead(e, now);
                        return scheduleRefresh(e, e.getKey(), hash, value, now, this.map.defaultLoader);
                    }
                    tryDrainReferenceQueues();
                }
                return null;
            } finally {
                postReadCleanup();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x0047, code lost:
            r2 = r4.getValueReference();
         */
        /* JADX WARN: Code restructure failed: missing block: B:14:0x0051, code lost:
            if (r2.isLoading() == false) goto L_0x005f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x0053, code lost:
            r18 = false;
            r14 = r4;
            r11 = r6;
            r21 = r10;
            r1 = r2;
            r10 = r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x005f, code lost:
            r1 = r2.get();
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x0065, code lost:
            if (r1 != null) goto L_0x0086;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x006e, code lost:
            r14 = r4;
            r21 = r10;
            r10 = r5;
            r18 = r11;
            r11 = r6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0082, code lost:
            enqueueNotification(r2, r24, r1, r2.getWeight(), com.google.common.cache.RemovalCause.COLLECTED);
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x0086, code lost:
            r14 = r4;
            r21 = r10;
            r18 = r11;
            r10 = r5;
            r11 = r6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x0095, code lost:
            if (r22.map.isExpired(r14, r2) == false) goto L_0x00b9;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0098, code lost:
            enqueueNotification(r2, r24, r1, r2.getWeight(), com.google.common.cache.RemovalCause.EXPIRED);
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x00a9, code lost:
            r22.writeQueue.remove(r14);
            r22.accessQueue.remove(r14);
            r22.count = r0;
            r1 = r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x00b9, code lost:
            recordLockedRead(r14, r2);
            r22.statsCounter.recordHits(1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x00c3, code lost:
            unlock();
            postWriteCleanup();
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x00c9, code lost:
            return r1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x00ca, code lost:
            r0 = th;
         */
        /* Code decompiled incorrectly, please refer to instructions dump */
        V lockedGetOrLoad(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
            Throwable th;
            ReferenceEntry<K, V> e;
            LoadingValueReference<K, V> loadingValueReference;
            boolean createNewEntry;
            ReferenceEntry<K, V> first;
            int index;
            Throwable th2;
            LoadingValueReference<K, V> loadingValueReference2;
            V loadSync;
            ValueReference<K, V> valueReference = null;
            LoadingValueReference<K, V> loadingValueReference3 = null;
            boolean index2 = true;
            lock();
            try {
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index3 = hash & (table.length() - 1);
                ReferenceEntry<K, V> referenceEntry = table.get(index3);
                ReferenceEntry<K, V> first2 = referenceEntry;
                ReferenceEntry<K, V> e2 = referenceEntry;
                while (true) {
                    if (e2 == null) {
                        e = e2;
                        loadingValueReference = loadingValueReference3;
                        createNewEntry = index2;
                        first = first2;
                        index = index3;
                        break;
                    }
                    try {
                        K entryKey = e2.getKey();
                        if (e2.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                            break;
                        }
                        try {
                            e2 = e2.getNext();
                            first2 = first2;
                            index3 = index3;
                            index2 = index2;
                            loadingValueReference3 = loadingValueReference3;
                        } catch (Throwable th3) {
                            th = th3;
                            unlock();
                            postWriteCleanup();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                    }
                }
                if (createNewEntry) {
                    loadingValueReference2 = new LoadingValueReference<>();
                    try {
                        if (e == null) {
                            ReferenceEntry<K, V> e3 = newEntry(key, hash, first);
                            e3.setValueReference(loadingValueReference2);
                            table.set(index, e3);
                            e = e3;
                        } else {
                            e.setValueReference(loadingValueReference2);
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        unlock();
                        postWriteCleanup();
                        throw th;
                    }
                } else {
                    loadingValueReference2 = loadingValueReference;
                }
                unlock();
                postWriteCleanup();
                if (!createNewEntry) {
                    return waitForLoadingValue(e, key, valueReference);
                }
                try {
                    synchronized (e) {
                        try {
                            loadSync = loadSync(key, hash, loadingValueReference2, loader);
                        } finally {
                            try {
                            } catch (Throwable th6) {
                                th2 = th6;
                                this.statsCounter.recordMisses(1);
                                throw th2;
                            }
                        }
                    }
                    this.statsCounter.recordMisses(1);
                    return loadSync;
                } catch (Throwable th7) {
                    th2 = th7;
                }
            } catch (Throwable th8) {
                th = th8;
            }
        }

        V waitForLoadingValue(ReferenceEntry<K, V> e, K key, ValueReference<K, V> valueReference) throws ExecutionException {
            if (valueReference.isLoading()) {
                Preconditions.checkState(!Thread.holdsLock(e), "Recursive load of: %s", key);
                try {
                    V value = valueReference.waitForValue();
                    if (value != null) {
                        recordRead(e, this.map.ticker.read());
                        return value;
                    }
                    throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
                } finally {
                    this.statsCounter.recordMisses(1);
                }
            } else {
                throw new AssertionError();
            }
        }

        V loadSync(K key, int hash, LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) throws ExecutionException {
            return getAndRecordStats(key, hash, loadingValueReference, loadingValueReference.loadFuture(key, loader));
        }

        ListenableFuture<V> loadAsync(final K key, final int hash, final LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) {
            final ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
            loadingFuture.addListener(new Runnable() { // from class: com.google.common.cache.LocalCache.Segment.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Segment.this.getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
                    } catch (Throwable t) {
                        LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", t);
                        loadingValueReference.setException(t);
                    }
                }
            }, MoreExecutors.directExecutor());
            return loadingFuture;
        }

        V getAndRecordStats(K key, int hash, LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> newValue) throws ExecutionException {
            V value = null;
            try {
                V value2 = (V) Uninterruptibles.getUninterruptibly(newValue);
                if (value2 != null) {
                    this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
                    storeLoadedValue(key, hash, loadingValueReference, value2);
                    return value2;
                }
                throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
            } finally {
                if (value == null) {
                    this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                    removeLoadingValue(key, hash, loadingValueReference);
                }
            }
        }

        V scheduleRefresh(ReferenceEntry<K, V> entry, K key, int hash, V oldValue, long now, CacheLoader<? super K, V> loader) {
            V newValue;
            if (!this.map.refreshes() || now - entry.getWriteTime() <= this.map.refreshNanos || entry.getValueReference().isLoading() || (newValue = refresh(key, hash, loader, true)) == null) {
                return oldValue;
            }
            return newValue;
        }

        @NullableDecl
        V refresh(K key, int hash, CacheLoader<? super K, V> loader, boolean checkTime) {
            LoadingValueReference<K, V> loadingValueReference = insertLoadingValueReference(key, hash, checkTime);
            if (loadingValueReference == null) {
                return null;
            }
            ListenableFuture<V> result = loadAsync(key, hash, loadingValueReference, loader);
            if (result.isDone()) {
                try {
                    return (V) Uninterruptibles.getUninterruptibly(result);
                } catch (Throwable th) {
                }
            }
            return null;
        }

        @NullableDecl
        LoadingValueReference<K, V> insertLoadingValueReference(K key, int hash, boolean checkTime) {
            lock();
            try {
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = (table.length() - 1) & hash;
                ReferenceEntry<K, V> first = table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e.getValueReference();
                        if (!valueReference.isLoading() && (!checkTime || now - e.getWriteTime() >= this.map.refreshNanos)) {
                            this.modCount++;
                            LoadingValueReference<K, V> loadingValueReference = new LoadingValueReference<>(valueReference);
                            e.setValueReference(loadingValueReference);
                            return loadingValueReference;
                        }
                        return null;
                    }
                }
                this.modCount++;
                LoadingValueReference<K, V> loadingValueReference2 = new LoadingValueReference<>();
                ReferenceEntry<K, V> e2 = newEntry(key, hash, first);
                e2.setValueReference(loadingValueReference2);
                table.set(index, e2);
                return loadingValueReference2;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        void tryDrainReferenceQueues() {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                } finally {
                    unlock();
                }
            }
        }

        void drainReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                drainValueReferenceQueue();
            }
        }

        void drainKeyReferenceQueue() {
            int i = 0;
            do {
                Reference<? extends K> ref = this.keyReferenceQueue.poll();
                if (ref != null) {
                    this.map.reclaimKey((ReferenceEntry) ref);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        void drainValueReferenceQueue() {
            int i = 0;
            do {
                Reference<? extends V> ref = this.valueReferenceQueue.poll();
                if (ref != null) {
                    this.map.reclaimValue((ValueReference) ref);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                clearValueReferenceQueue();
            }
        }

        void clearKeyReferenceQueue() {
            do {
            } while (this.keyReferenceQueue.poll() != null);
        }

        void clearValueReferenceQueue() {
            do {
            } while (this.valueReferenceQueue.poll() != null);
        }

        void recordRead(ReferenceEntry<K, V> entry, long now) {
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            this.recencyQueue.add(entry);
        }

        void recordLockedRead(ReferenceEntry<K, V> entry, long now) {
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            this.accessQueue.add(entry);
        }

        void recordWrite(ReferenceEntry<K, V> entry, int weight, long now) {
            drainRecencyQueue();
            this.totalWeight += (long) weight;
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            if (this.map.recordsWrite()) {
                entry.setWriteTime(now);
            }
            this.accessQueue.add(entry);
            this.writeQueue.add(entry);
        }

        void drainRecencyQueue() {
            while (true) {
                ReferenceEntry<K, V> e = this.recencyQueue.poll();
                if (e == null) {
                    return;
                }
                if (this.accessQueue.contains(e)) {
                    this.accessQueue.add(e);
                }
            }
        }

        void tryExpireEntries(long now) {
            if (tryLock()) {
                try {
                    expireEntries(now);
                } finally {
                    unlock();
                }
            }
        }

        void expireEntries(long now) {
            ReferenceEntry<K, V> e;
            ReferenceEntry<K, V> e2;
            drainRecencyQueue();
            do {
                e = this.writeQueue.peek();
                if (e == null || !this.map.isExpired(e, now)) {
                    do {
                        e2 = this.accessQueue.peek();
                        if (e2 == null || !this.map.isExpired(e2, now)) {
                            return;
                        }
                    } while (removeEntry(e2, e2.getHash(), RemovalCause.EXPIRED));
                    throw new AssertionError();
                }
            } while (removeEntry(e, e.getHash(), RemovalCause.EXPIRED));
            throw new AssertionError();
        }

        void enqueueNotification(@NullableDecl K key, int hash, @NullableDecl V value, int weight, RemovalCause cause) {
            this.totalWeight -= (long) weight;
            if (cause.wasEvicted()) {
                this.statsCounter.recordEviction();
            }
            if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
                this.map.removalNotificationQueue.offer(RemovalNotification.create(key, value, cause));
            }
        }

        void evictEntries(ReferenceEntry<K, V> newest) {
            if (this.map.evictsBySize()) {
                drainRecencyQueue();
                if (((long) newest.getValueReference().getWeight()) <= this.maxSegmentWeight || removeEntry(newest, newest.getHash(), RemovalCause.SIZE)) {
                    while (this.totalWeight > this.maxSegmentWeight) {
                        ReferenceEntry<K, V> e = getNextEvictable();
                        if (!removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
                            throw new AssertionError();
                        }
                    }
                    return;
                }
                throw new AssertionError();
            }
        }

        ReferenceEntry<K, V> getNextEvictable() {
            for (ReferenceEntry<K, V> e : this.accessQueue) {
                if (e.getValueReference().getWeight() > 0) {
                    return e;
                }
            }
            throw new AssertionError();
        }

        ReferenceEntry<K, V> getFirst(int hash) {
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            return table.get((table.length() - 1) & hash);
        }

        @NullableDecl
        ReferenceEntry<K, V> getEntry(Object key, int hash) {
            for (ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
                if (e.getHash() == hash) {
                    K entryKey = e.getKey();
                    if (entryKey == null) {
                        tryDrainReferenceQueues();
                    } else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                        return e;
                    }
                }
            }
            return null;
        }

        @NullableDecl
        ReferenceEntry<K, V> getLiveEntry(Object key, int hash, long now) {
            ReferenceEntry<K, V> e = getEntry(key, hash);
            if (e == null) {
                return null;
            }
            if (!this.map.isExpired(e, now)) {
                return e;
            }
            tryExpireEntries(now);
            return null;
        }

        V getLiveValue(ReferenceEntry<K, V> entry, long now) {
            if (entry.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V value = entry.getValueReference().get();
            if (value == null) {
                tryDrainReferenceQueues();
                return null;
            } else if (!this.map.isExpired(entry, now)) {
                return value;
            } else {
                tryExpireEntries(now);
                return null;
            }
        }

        boolean containsKey(Object key, int hash) {
            try {
                boolean z = false;
                if (this.count == 0) {
                    return false;
                }
                ReferenceEntry<K, V> e = getLiveEntry(key, hash, this.map.ticker.read());
                if (e == null) {
                    return false;
                }
                if (e.getValueReference().get() != null) {
                    z = true;
                }
                return z;
            } finally {
                postReadCleanup();
            }
        }

        /* JADX WARN: Finally extract failed */
        boolean containsValue(Object value) {
            try {
                if (this.count != 0) {
                    long now = this.map.ticker.read();
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int length = table.length();
                    for (int i = 0; i < length; i++) {
                        ReferenceEntry<K, V> e = table.get(i);
                        while (e != null) {
                            V entryValue = getLiveValue(e, now);
                            if (entryValue != null && this.map.valueEquivalence.equivalent(value, entryValue)) {
                                postReadCleanup();
                                return true;
                            }
                            e = e.getNext();
                        }
                    }
                }
                postReadCleanup();
                return false;
            } catch (Throwable th) {
                postReadCleanup();
                throw th;
            }
        }

        @NullableDecl
        V put(K key, int hash, V value, boolean onlyIfAbsent) {
            int newCount;
            lock();
            try {
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                if (this.count + 1 > this.threshold) {
                    expand();
                    int newCount2 = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e.getValueReference();
                        V entryValue = valueReference.get();
                        if (entryValue == null) {
                            this.modCount++;
                            if (valueReference.isActive()) {
                                enqueueNotification(key, hash, entryValue, valueReference.getWeight(), RemovalCause.COLLECTED);
                                setValue(e, key, value, now);
                                newCount = this.count;
                            } else {
                                setValue(e, key, value, now);
                                newCount = this.count + 1;
                            }
                            this.count = newCount;
                            evictEntries(e);
                            return null;
                        } else if (onlyIfAbsent) {
                            recordLockedRead(e, now);
                            return entryValue;
                        } else {
                            this.modCount++;
                            enqueueNotification(key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                            setValue(e, key, value, now);
                            evictEntries(e);
                            return entryValue;
                        }
                    }
                }
                this.modCount++;
                ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
                setValue(newEntry, key, value, now);
                table.set(index, newEntry);
                this.count++;
                evictEntries(newEntry);
                return null;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        void expand() {
            AtomicReferenceArray<ReferenceEntry<K, V>> oldTable = this.table;
            int oldCapacity = oldTable.length();
            if (oldCapacity < 1073741824) {
                int newCount = this.count;
                AtomicReferenceArray<ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
                this.threshold = (newTable.length() * 3) / 4;
                int newMask = newTable.length() - 1;
                for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
                    ReferenceEntry<K, V> head = oldTable.get(oldIndex);
                    if (head != null) {
                        ReferenceEntry<K, V> next = head.getNext();
                        int headIndex = head.getHash() & newMask;
                        if (next == null) {
                            newTable.set(headIndex, head);
                        } else {
                            ReferenceEntry<K, V> tail = head;
                            int tailIndex = headIndex;
                            for (ReferenceEntry<K, V> e = next; e != null; e = e.getNext()) {
                                int newIndex = e.getHash() & newMask;
                                if (newIndex != tailIndex) {
                                    tailIndex = newIndex;
                                    tail = e;
                                }
                            }
                            newTable.set(tailIndex, tail);
                            for (ReferenceEntry<K, V> e2 = head; e2 != tail; e2 = e2.getNext()) {
                                int newIndex2 = e2.getHash() & newMask;
                                ReferenceEntry<K, V> newFirst = copyEntry(e2, newTable.get(newIndex2));
                                if (newFirst != null) {
                                    newTable.set(newIndex2, newFirst);
                                } else {
                                    removeCollectedEntry(e2);
                                    newCount--;
                                }
                            }
                        }
                    }
                }
                this.table = newTable;
                this.count = newCount;
            }
        }

        boolean replace(K key, int hash, V oldValue, V newValue) {
            Throwable th;
            int index;
            long now;
            ReferenceEntry<K, V> e;
            lock();
            try {
                long now2 = this.map.ticker.read();
                preWriteCleanup(now2);
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index2 = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = table.get(index2);
                ReferenceEntry<K, V> e2 = first;
                while (e2 != null) {
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null) {
                        e = e2;
                        index = index2;
                        now = now2;
                    } else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e2.getValueReference();
                        V entryValue = valueReference.get();
                        if (entryValue == null) {
                            if (valueReference.isActive()) {
                                int i = this.count - 1;
                                this.modCount++;
                                table.set(index2, removeValueFromChain(first, e2, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED));
                                this.count--;
                            }
                            unlock();
                            postWriteCleanup();
                            return false;
                        } else if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
                            this.modCount++;
                            enqueueNotification(key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                            setValue(e2, key, newValue, now2);
                            evictEntries(e2);
                            unlock();
                            postWriteCleanup();
                            return true;
                        } else {
                            recordLockedRead(e2, now2);
                            unlock();
                            postWriteCleanup();
                            return false;
                        }
                    } else {
                        e = e2;
                        index = index2;
                        now = now2;
                    }
                    try {
                        e2 = e.getNext();
                        now2 = now;
                        index2 = index;
                    } catch (Throwable th2) {
                        th = th2;
                        unlock();
                        postWriteCleanup();
                        throw th;
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } catch (Throwable th3) {
                th = th3;
            }
        }

        @NullableDecl
        V replace(K key, int hash, V newValue) {
            long now;
            int index;
            ReferenceEntry<K, V> e;
            lock();
            try {
                long now2 = this.map.ticker.read();
                preWriteCleanup(now2);
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index2 = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = table.get(index2);
                ReferenceEntry<K, V> e2 = first;
                while (e2 != null) {
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null) {
                        e = e2;
                        index = index2;
                        now = now2;
                    } else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e2.getValueReference();
                        V entryValue = valueReference.get();
                        if (entryValue == null) {
                            if (valueReference.isActive()) {
                                int i = this.count - 1;
                                this.modCount++;
                                table.set(index2, removeValueFromChain(first, e2, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED));
                                this.count--;
                            }
                            return null;
                        }
                        this.modCount++;
                        enqueueNotification(key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                        setValue(e2, key, newValue, now2);
                        evictEntries(e2);
                        return entryValue;
                    } else {
                        e = e2;
                        index = index2;
                        now = now2;
                    }
                    e2 = e.getNext();
                    index2 = index;
                    now2 = now;
                }
                return null;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        @NullableDecl
        V remove(Object key, int hash) {
            RemovalCause cause;
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                int i = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = table.get(index);
                ReferenceEntry<K, V> e = first;
                while (e != null) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null) {
                        if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                            ValueReference<K, V> valueReference = e.getValueReference();
                            V entryValue = valueReference.get();
                            if (entryValue != null) {
                                cause = RemovalCause.EXPLICIT;
                            } else if (!valueReference.isActive()) {
                                return null;
                            } else {
                                cause = RemovalCause.COLLECTED;
                            }
                            this.modCount++;
                            table.set(index, removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause));
                            this.count--;
                            return entryValue;
                        }
                    }
                    e = e.getNext();
                }
                return null;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean remove(Object key, int hash, Object value) {
            RemovalCause cause;
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                boolean z = true;
                int i = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = table.get(index);
                ReferenceEntry<K, V> e = first;
                while (e != null) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null) {
                        if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                            ValueReference<K, V> valueReference = e.getValueReference();
                            V entryValue = valueReference.get();
                            if (this.map.valueEquivalence.equivalent(value, entryValue)) {
                                cause = RemovalCause.EXPLICIT;
                            } else if (entryValue != null || !valueReference.isActive()) {
                                return false;
                            } else {
                                cause = RemovalCause.COLLECTED;
                            }
                            this.modCount++;
                            table.set(index, removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause));
                            this.count--;
                            if (cause != RemovalCause.EXPLICIT) {
                                z = false;
                            }
                            return z;
                        }
                    }
                    e = e.getNext();
                }
                return false;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean storeLoadedValue(K key, int hash, LoadingValueReference<K, V> oldValueReference, V newValue) {
            ReferenceEntry<K, V> e;
            ReferenceEntry<K, V> e2;
            lock();
            try {
                long now = this.map.ticker.read();
                preWriteCleanup(now);
                int newCount = this.count + 1;
                if (newCount > this.threshold) {
                    expand();
                    newCount = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = table.get(index);
                for (ReferenceEntry<K, V> e3 = first; e3 != null; e3 = e3.getNext()) {
                    K entryKey = e3.getKey();
                    if (e3.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        ValueReference<K, V> valueReference = e3.getValueReference();
                        V entryValue = valueReference.get();
                        if (oldValueReference == valueReference) {
                            e = e3;
                        } else if (entryValue != null || valueReference == LocalCache.UNSET) {
                            enqueueNotification(key, hash, newValue, 0, RemovalCause.REPLACED);
                            return false;
                        } else {
                            e = e3;
                        }
                        this.modCount++;
                        if (oldValueReference.isActive()) {
                            e2 = e;
                            enqueueNotification(key, hash, entryValue, oldValueReference.getWeight(), entryValue == null ? RemovalCause.COLLECTED : RemovalCause.REPLACED);
                            newCount--;
                        } else {
                            e2 = e;
                        }
                        setValue(e2, key, newValue, now);
                        this.count = newCount;
                        evictEntries(e2);
                        return true;
                    }
                }
                this.modCount++;
                ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
                setValue(newEntry, key, newValue, now);
                table.set(index, newEntry);
                this.count = newCount;
                evictEntries(newEntry);
                unlock();
                postWriteCleanup();
                return true;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        void clear() {
            RemovalCause cause;
            if (this.count != 0) {
                lock();
                try {
                    preWriteCleanup(this.map.ticker.read());
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    for (int i = 0; i < table.length(); i++) {
                        for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
                            if (e.getValueReference().isActive()) {
                                K key = e.getKey();
                                V value = e.getValueReference().get();
                                if (!(key == null || value == null)) {
                                    cause = RemovalCause.EXPLICIT;
                                    enqueueNotification(key, e.getHash(), value, e.getValueReference().getWeight(), cause);
                                }
                                cause = RemovalCause.COLLECTED;
                                enqueueNotification(key, e.getHash(), value, e.getValueReference().getWeight(), cause);
                            }
                        }
                    }
                    for (int i2 = 0; i2 < table.length(); i2++) {
                        table.set(i2, null);
                    }
                    clearReferenceQueues();
                    this.writeQueue.clear();
                    this.accessQueue.clear();
                    this.readCount.set(0);
                    this.modCount++;
                    this.count = 0;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }
        }

        @NullableDecl
        ReferenceEntry<K, V> removeValueFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry, @NullableDecl K key, int hash, V value, ValueReference<K, V> valueReference, RemovalCause cause) {
            enqueueNotification(key, hash, value, valueReference.getWeight(), cause);
            this.writeQueue.remove(entry);
            this.accessQueue.remove(entry);
            if (!valueReference.isLoading()) {
                return removeEntryFromChain(first, entry);
            }
            valueReference.notifyNewValue(null);
            return first;
        }

        @NullableDecl
        ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry) {
            int newCount = this.count;
            ReferenceEntry<K, V> newFirst = entry.getNext();
            for (ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
                ReferenceEntry<K, V> next = copyEntry(e, newFirst);
                if (next != null) {
                    newFirst = next;
                } else {
                    removeCollectedEntry(e);
                    newCount--;
                }
            }
            this.count = newCount;
            return newFirst;
        }

        void removeCollectedEntry(ReferenceEntry<K, V> entry) {
            enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference().get(), entry.getValueReference().getWeight(), RemovalCause.COLLECTED);
            this.writeQueue.remove(entry);
            this.accessQueue.remove(entry);
        }

        boolean reclaimKey(ReferenceEntry<K, V> entry, int hash) {
            lock();
            try {
                int i = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = (table.length() - 1) & hash;
                ReferenceEntry<K, V> first = table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    if (e == entry) {
                        this.modCount++;
                        table.set(index, removeValueFromChain(first, e, e.getKey(), hash, e.getValueReference().get(), e.getValueReference(), RemovalCause.COLLECTED));
                        this.count--;
                        return true;
                    }
                }
                return false;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean reclaimValue(K key, int hash, ValueReference<K, V> valueReference) {
            lock();
            try {
                int i = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = table.get(index);
                ReferenceEntry<K, V> e = first;
                while (e != null) {
                    K entryKey = e.getKey();
                    if (e.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) {
                        e = e.getNext();
                    } else if (e.getValueReference() == valueReference) {
                        this.modCount++;
                        table.set(index, removeValueFromChain(first, e, entryKey, hash, valueReference.get(), valueReference, RemovalCause.COLLECTED));
                        this.count--;
                        return true;
                    } else {
                        unlock();
                        if (!isHeldByCurrentThread()) {
                            postWriteCleanup();
                        }
                        return false;
                    }
                }
                unlock();
                if (!isHeldByCurrentThread()) {
                    postWriteCleanup();
                }
                return false;
            } finally {
                unlock();
                if (!isHeldByCurrentThread()) {
                    postWriteCleanup();
                }
            }
        }

        boolean removeLoadingValue(K key, int hash, LoadingValueReference<K, V> valueReference) {
            lock();
            try {
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = (table.length() - 1) & hash;
                ReferenceEntry<K, V> first = table.get(index);
                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        if (e.getValueReference() != valueReference) {
                            return false;
                        } else {
                            if (valueReference.isActive()) {
                                e.setValueReference(valueReference.getOldValue());
                            } else {
                                table.set(index, removeEntryFromChain(first, e));
                            }
                            return true;
                        }
                    }
                }
                return false;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean removeEntry(ReferenceEntry<K, V> entry, int hash, RemovalCause cause) {
            int i = this.count - 1;
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & (table.length() - 1);
            ReferenceEntry<K, V> first = table.get(index);
            for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                if (e == entry) {
                    this.modCount++;
                    table.set(index, removeValueFromChain(first, e, e.getKey(), hash, e.getValueReference().get(), e.getValueReference(), cause));
                    this.count--;
                    return true;
                }
            }
            return false;
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                cleanUp();
            }
        }

        void preWriteCleanup(long now) {
            runLockedCleanup(now);
        }

        void postWriteCleanup() {
            runUnlockedCleanup();
        }

        void cleanUp() {
            runLockedCleanup(this.map.ticker.read());
            runUnlockedCleanup();
        }

        void runLockedCleanup(long now) {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                    expireEntries(now);
                    this.readCount.set(0);
                } finally {
                    unlock();
                }
            }
        }

        void runUnlockedCleanup() {
            if (!isHeldByCurrentThread()) {
                this.map.processPendingNotifications();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class LoadingValueReference<K, V> implements ValueReference<K, V> {
        final SettableFuture<V> futureValue;
        volatile ValueReference<K, V> oldValue;
        final Stopwatch stopwatch;

        public LoadingValueReference() {
            this(LocalCache.unset());
        }

        public LoadingValueReference(ValueReference<K, V> oldValue) {
            this.futureValue = SettableFuture.create();
            this.stopwatch = Stopwatch.createUnstarted();
            this.oldValue = oldValue;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return this.oldValue.isActive();
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.oldValue.getWeight();
        }

        public boolean set(@NullableDecl V newValue) {
            return this.futureValue.set(newValue);
        }

        public boolean setException(Throwable t) {
            return this.futureValue.setException(t);
        }

        private ListenableFuture<V> fullyFailedFuture(Throwable t) {
            return Futures.immediateFailedFuture(t);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(@NullableDecl V newValue) {
            if (newValue != null) {
                set(newValue);
            } else {
                this.oldValue = LocalCache.unset();
            }
        }

        public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader) {
            try {
                this.stopwatch.start();
                V previousValue = this.oldValue.get();
                if (previousValue == null) {
                    V newValue = loader.load(key);
                    return set(newValue) ? this.futureValue : Futures.immediateFuture(newValue);
                }
                ListenableFuture<V> newValue2 = loader.reload(key, previousValue);
                if (newValue2 == null) {
                    return Futures.immediateFuture(null);
                }
                return Futures.transform(newValue2, new Function<V, V>() { // from class: com.google.common.cache.LocalCache.LoadingValueReference.1
                    @Override // com.google.common.base.Function
                    public V apply(V newValue3) {
                        LoadingValueReference.this.set(newValue3);
                        return newValue3;
                    }
                }, MoreExecutors.directExecutor());
            } catch (Throwable t) {
                ListenableFuture<V> result = setException(t) ? this.futureValue : fullyFailedFuture(t);
                if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                return result;
            }
        }

        public long elapsedNanos() {
            return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() throws ExecutionException {
            return (V) Uninterruptibles.getUninterruptibly(this.futureValue);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V get() {
            return this.oldValue.get();
        }

        public ValueReference<K, V> getOldValue() {
            return this.oldValue;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, @NullableDecl V value, ReferenceEntry<K, V> entry) {
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static final class WriteQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new AbstractReferenceEntry<K, V>() { // from class: com.google.common.cache.LocalCache.WriteQueue.1
            ReferenceEntry<K, V> nextWrite = this;
            ReferenceEntry<K, V> previousWrite = this;

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public long getWriteTime() {
                return Long.MAX_VALUE;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setWriteTime(long time) {
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public ReferenceEntry<K, V> getNextInWriteQueue() {
                return this.nextWrite;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                this.nextWrite = next;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                return this.previousWrite;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                this.previousWrite = previous;
            }
        };

        WriteQueue() {
        }

        @Override // java.util.Queue
        public /* bridge */ /* synthetic */ boolean offer(Object obj) {
            return offer((ReferenceEntry) ((ReferenceEntry) obj));
        }

        public boolean offer(ReferenceEntry<K, V> entry) {
            LocalCache.connectWriteOrder(entry.getPreviousInWriteQueue(), entry.getNextInWriteQueue());
            LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), entry);
            LocalCache.connectWriteOrder(entry, this.head);
            return true;
        }

        @Override // java.util.Queue
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
            if (next == this.head) {
                return null;
            }
            return next;
        }

        @Override // java.util.Queue
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
            if (next == this.head) {
                return null;
            }
            remove(next);
            return next;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object o) {
            ReferenceEntry<K, V> e = (ReferenceEntry) o;
            ReferenceEntry<K, V> previous = e.getPreviousInWriteQueue();
            ReferenceEntry<K, V> next = e.getNextInWriteQueue();
            LocalCache.connectWriteOrder(previous, next);
            LocalCache.nullifyWriteOrder(e);
            return next != NullEntry.INSTANCE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object o) {
            return ((ReferenceEntry) o).getNextInWriteQueue() != NullEntry.INSTANCE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.head.getNextInWriteQueue() == this.head;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e = this.head.getNextInWriteQueue(); e != this.head; e = e.getNextInWriteQueue()) {
                size++;
            }
            return size;
        }

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
            while (true) {
                ReferenceEntry<K, V> referenceEntry = this.head;
                if (e != referenceEntry) {
                    ReferenceEntry<K, V> next = e.getNextInWriteQueue();
                    LocalCache.nullifyWriteOrder(e);
                    e = next;
                } else {
                    referenceEntry.setNextInWriteQueue(referenceEntry);
                    ReferenceEntry<K, V> referenceEntry2 = this.head;
                    referenceEntry2.setPreviousInWriteQueue(referenceEntry2);
                    return;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) { // from class: com.google.common.cache.LocalCache.WriteQueue.2
                @Override // com.google.common.collect.AbstractSequentialIterator
                protected /* bridge */ /* synthetic */ Object computeNext(Object obj) {
                    return computeNext((ReferenceEntry) ((ReferenceEntry) obj));
                }

                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
                    ReferenceEntry<K, V> next = previous.getNextInWriteQueue();
                    if (next == WriteQueue.this.head) {
                        return null;
                    }
                    return next;
                }
            };
        }
    }

    /* loaded from: classes.dex */
    public static final class AccessQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new AbstractReferenceEntry<K, V>() { // from class: com.google.common.cache.LocalCache.AccessQueue.1
            ReferenceEntry<K, V> nextAccess = this;
            ReferenceEntry<K, V> previousAccess = this;

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public long getAccessTime() {
                return Long.MAX_VALUE;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setAccessTime(long time) {
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public ReferenceEntry<K, V> getNextInAccessQueue() {
                return this.nextAccess;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                this.nextAccess = next;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                return this.previousAccess;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                this.previousAccess = previous;
            }
        };

        AccessQueue() {
        }

        @Override // java.util.Queue
        public /* bridge */ /* synthetic */ boolean offer(Object obj) {
            return offer((ReferenceEntry) ((ReferenceEntry) obj));
        }

        public boolean offer(ReferenceEntry<K, V> entry) {
            LocalCache.connectAccessOrder(entry.getPreviousInAccessQueue(), entry.getNextInAccessQueue());
            LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), entry);
            LocalCache.connectAccessOrder(entry, this.head);
            return true;
        }

        @Override // java.util.Queue
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
            if (next == this.head) {
                return null;
            }
            return next;
        }

        @Override // java.util.Queue
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
            if (next == this.head) {
                return null;
            }
            remove(next);
            return next;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object o) {
            ReferenceEntry<K, V> e = (ReferenceEntry) o;
            ReferenceEntry<K, V> previous = e.getPreviousInAccessQueue();
            ReferenceEntry<K, V> next = e.getNextInAccessQueue();
            LocalCache.connectAccessOrder(previous, next);
            LocalCache.nullifyAccessOrder(e);
            return next != NullEntry.INSTANCE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object o) {
            return ((ReferenceEntry) o).getNextInAccessQueue() != NullEntry.INSTANCE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.head.getNextInAccessQueue() == this.head;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e = this.head.getNextInAccessQueue(); e != this.head; e = e.getNextInAccessQueue()) {
                size++;
            }
            return size;
        }

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
            while (true) {
                ReferenceEntry<K, V> referenceEntry = this.head;
                if (e != referenceEntry) {
                    ReferenceEntry<K, V> next = e.getNextInAccessQueue();
                    LocalCache.nullifyAccessOrder(e);
                    e = next;
                } else {
                    referenceEntry.setNextInAccessQueue(referenceEntry);
                    ReferenceEntry<K, V> referenceEntry2 = this.head;
                    referenceEntry2.setPreviousInAccessQueue(referenceEntry2);
                    return;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) { // from class: com.google.common.cache.LocalCache.AccessQueue.2
                @Override // com.google.common.collect.AbstractSequentialIterator
                protected /* bridge */ /* synthetic */ Object computeNext(Object obj) {
                    return computeNext((ReferenceEntry) ((ReferenceEntry) obj));
                }

                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
                    ReferenceEntry<K, V> next = previous.getNextInAccessQueue();
                    if (next == AccessQueue.this.head) {
                        return null;
                    }
                    return next;
                }
            };
        }
    }

    public void cleanUp() {
        for (Segment<K, V> segment : this.segments) {
            segment.cleanUp();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        long sum = 0;
        Segment<K, V>[] segments = this.segments;
        for (int i = 0; i < segments.length; i++) {
            if (segments[i].count != 0) {
                return false;
            }
            sum += (long) segments[i].modCount;
        }
        if (sum == 0) {
            return true;
        }
        for (int i2 = 0; i2 < segments.length; i2++) {
            if (segments[i2].count != 0) {
                return false;
            }
            sum -= (long) segments[i2].modCount;
        }
        if (sum == 0) {
            return true;
        }
        return false;
    }

    long longSize() {
        long sum = 0;
        for (Segment<K, V> segment : this.segments) {
            sum += (long) Math.max(0, segment.count);
        }
        return sum;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return Ints.saturatedCast(longSize());
    }

    @Override // java.util.AbstractMap, java.util.Map
    @NullableDecl
    public V get(@NullableDecl Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).get(key, hash);
    }

    V get(K key, CacheLoader<? super K, V> loader) throws ExecutionException {
        int hash = hash(Preconditions.checkNotNull(key));
        return segmentFor(hash).get(key, hash, loader);
    }

    @NullableDecl
    public V getIfPresent(Object key) {
        int hash = hash(Preconditions.checkNotNull(key));
        V value = segmentFor(hash).get(key, hash);
        if (value == null) {
            this.globalStatsCounter.recordMisses(1);
        } else {
            this.globalStatsCounter.recordHits(1);
        }
        return value;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @NullableDecl
    public V getOrDefault(@NullableDecl Object key, @NullableDecl V defaultValue) {
        V result = get(key);
        return result != null ? result : defaultValue;
    }

    V getOrLoad(K key) throws ExecutionException {
        return get(key, this.defaultLoader);
    }

    /* JADX WARN: Multi-variable type inference failed */
    ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
        int hits = 0;
        int misses = 0;
        LinkedHashMap newLinkedHashMap = Maps.newLinkedHashMap();
        for (Object key : keys) {
            V value = get(key);
            if (value == null) {
                misses++;
            } else {
                newLinkedHashMap.put(key, value);
                hits++;
            }
        }
        this.globalStatsCounter.recordHits(hits);
        this.globalStatsCounter.recordMisses(misses);
        return ImmutableMap.copyOf(newLinkedHashMap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
        int hits = 0;
        int misses = 0;
        LinkedHashMap newLinkedHashMap = Maps.newLinkedHashMap();
        LinkedHashSet newLinkedHashSet = Sets.newLinkedHashSet();
        for (Object obj : keys) {
            V value = get(obj);
            if (!newLinkedHashMap.containsKey(obj)) {
                newLinkedHashMap.put(obj, value);
                if (value == null) {
                    misses++;
                    newLinkedHashSet.add(obj);
                } else {
                    hits++;
                }
            }
        }
        try {
            if (!newLinkedHashSet.isEmpty()) {
                try {
                    Map<K, V> newEntries = loadAll(newLinkedHashSet, this.defaultLoader);
                    for (Object obj2 : newLinkedHashSet) {
                        V value2 = newEntries.get(obj2);
                        if (value2 != null) {
                            newLinkedHashMap.put(obj2, value2);
                        } else {
                            throw new CacheLoader.InvalidCacheLoadException("loadAll failed to return a value for " + obj2);
                        }
                    }
                } catch (CacheLoader.UnsupportedLoadingOperationException e) {
                    for (Object obj3 : newLinkedHashSet) {
                        misses--;
                        newLinkedHashMap.put(obj3, get(obj3, this.defaultLoader));
                    }
                }
            }
            return ImmutableMap.copyOf(newLinkedHashMap);
        } finally {
            this.globalStatsCounter.recordHits(hits);
            this.globalStatsCounter.recordMisses(misses);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NullableDecl
    Map<K, V> loadAll(Set<? extends K> keys, CacheLoader<? super K, V> loader) throws ExecutionException {
        Stopwatch stopwatch;
        boolean success;
        try {
            Preconditions.checkNotNull(loader);
            Preconditions.checkNotNull(keys);
            stopwatch = Stopwatch.createStarted();
            success = false;
            try {
                try {
                    Map map = (Map<? super K, V>) loader.loadAll(keys);
                    boolean success2 = true;
                    if (map != null) {
                        stopwatch.stop();
                        boolean nullsPresent = false;
                        for (Map.Entry<K, V> entry : map.entrySet()) {
                            K key = entry.getKey();
                            V value = entry.getValue();
                            if (key == null || value == null) {
                                nullsPresent = true;
                            } else {
                                put(key, value);
                            }
                        }
                        if (!nullsPresent) {
                            this.globalStatsCounter.recordLoadSuccess(stopwatch.elapsed(TimeUnit.NANOSECONDS));
                            return map;
                        }
                        this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
                        throw new CacheLoader.InvalidCacheLoadException(loader + " returned null keys or values from loadAll");
                    }
                    this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
                    throw new CacheLoader.InvalidCacheLoadException(loader + " returned null map from loadAll");
                } catch (RuntimeException e) {
                    throw new UncheckedExecutionException(e);
                } catch (Exception e2) {
                    throw new ExecutionException(e2);
                }
            } catch (CacheLoader.UnsupportedLoadingOperationException e3) {
                throw e3;
            } catch (Error e4) {
                throw new ExecutionError(e4);
            } catch (InterruptedException e5) {
                Thread.currentThread().interrupt();
                throw new ExecutionException(e5);
            }
        } finally {
            if (!success) {
                this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
            }
        }
    }

    ReferenceEntry<K, V> getEntry(@NullableDecl Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).getEntry(key, hash);
    }

    void refresh(K key) {
        int hash = hash(Preconditions.checkNotNull(key));
        segmentFor(hash).refresh(key, hash, this.defaultLoader, false);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@NullableDecl Object key) {
        if (key == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).containsKey(key, hash);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@NullableDecl Object value) {
        long now;
        int i = 0;
        if (value == null) {
            return false;
        }
        long now2 = this.ticker.read();
        Segment<K, V>[] segments = this.segments;
        long last = -1;
        int i2 = 0;
        while (i2 < 3) {
            int length = segments.length;
            long sum = 0;
            int i3 = i;
            while (i3 < length) {
                Segment<K, V> segment = segments[i3];
                int i4 = segment.count;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
                int j = 0;
                while (j < table.length()) {
                    ReferenceEntry<K, V> e = table.get(j);
                    while (e != null) {
                        V v = segment.getLiveValue(e, now2);
                        if (v != null) {
                            now = now2;
                            if (this.valueEquivalence.equivalent(value, v)) {
                                return true;
                            }
                        } else {
                            now = now2;
                        }
                        e = e.getNext();
                        length = length;
                        now2 = now;
                    }
                    j++;
                    segments = segments;
                }
                sum += (long) segment.modCount;
                i3++;
                segments = segments;
                now2 = now2;
            }
            if (sum == last) {
                return false;
            }
            last = sum;
            i2++;
            segments = segments;
            now2 = now2;
            i = 0;
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, false);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V putIfAbsent(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(@NullableDecl Object key) {
        if (key == null) {
            return null;
        }
        int hash = hash(key);
        return segmentFor(hash).remove(key, hash);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(@NullableDecl Object key, @NullableDecl Object value) {
        if (key == null || value == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).remove(key, hash, value);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K key, @NullableDecl V oldValue, V newValue) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(newValue);
        if (oldValue == null) {
            return false;
        }
        int hash = hash(key);
        return segmentFor(hash).replace(key, hash, oldValue, newValue);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V replace(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = hash(key);
        return segmentFor(hash).replace(key, hash, value);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        for (Segment<K, V> segment : this.segments) {
            segment.clear();
        }
    }

    void invalidateAll(Iterable<?> keys) {
        Iterator<?> it = keys.iterator();
        while (it.hasNext()) {
            remove(it.next());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        KeySet keySet = new KeySet(this);
        this.keySet = keySet;
        return keySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> vs = this.values;
        if (vs != null) {
            return vs;
        }
        Values values = new Values(this);
        this.values = values;
        return values;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        EntrySet entrySet = new EntrySet(this);
        this.entrySet = entrySet;
        return entrySet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public abstract class HashIterator<T> implements Iterator<T> {
        @NullableDecl
        Segment<K, V> currentSegment;
        @NullableDecl
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        @NullableDecl
        LocalCache<K, V>.WriteThroughEntry lastReturned;
        @NullableDecl
        ReferenceEntry<K, V> nextEntry;
        @NullableDecl
        LocalCache<K, V>.WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex = -1;

        @Override // java.util.Iterator
        public abstract T next();

        HashIterator() {
            LocalCache.this = this$0;
            this.nextSegmentIndex = this$0.segments.length - 1;
            advance();
        }

        final void advance() {
            this.nextExternal = null;
            if (!nextInChain() && !nextInTable()) {
                while (this.nextSegmentIndex >= 0) {
                    Segment<K, V>[] segmentArr = LocalCache.this.segments;
                    int i = this.nextSegmentIndex;
                    this.nextSegmentIndex = i - 1;
                    this.currentSegment = segmentArr[i];
                    if (this.currentSegment.count != 0) {
                        this.currentTable = this.currentSegment.table;
                        this.nextTableIndex = this.currentTable.length() - 1;
                        if (nextInTable()) {
                            return;
                        }
                    }
                }
            }
        }

        boolean nextInChain() {
            ReferenceEntry<K, V> referenceEntry = this.nextEntry;
            if (referenceEntry == null) {
                return false;
            }
            while (true) {
                this.nextEntry = referenceEntry.getNext();
                ReferenceEntry<K, V> referenceEntry2 = this.nextEntry;
                if (referenceEntry2 == null) {
                    return false;
                }
                if (advanceTo(referenceEntry2)) {
                    return true;
                }
                referenceEntry = this.nextEntry;
            }
        }

        boolean nextInTable() {
            while (true) {
                int i = this.nextTableIndex;
                if (i < 0) {
                    return false;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.currentTable;
                this.nextTableIndex = i - 1;
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(i);
                this.nextEntry = referenceEntry;
                if (referenceEntry != null && (advanceTo(this.nextEntry) || nextInChain())) {
                    return true;
                }
            }
        }

        boolean advanceTo(ReferenceEntry<K, V> entry) {
            Segment<K, V> segment;
            try {
                long now = LocalCache.this.ticker.read();
                K key = entry.getKey();
                Object liveValue = LocalCache.this.getLiveValue(entry, now);
                if (liveValue != null) {
                    this.nextExternal = new WriteThroughEntry(key, liveValue);
                    return true;
                }
                return false;
            } finally {
                this.currentSegment.postReadCleanup();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextExternal != null;
        }

        LocalCache<K, V>.WriteThroughEntry nextEntry() {
            LocalCache<K, V>.WriteThroughEntry writeThroughEntry = this.nextExternal;
            if (writeThroughEntry != null) {
                this.lastReturned = writeThroughEntry;
                advance();
                return this.lastReturned;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            Preconditions.checkState(this.lastReturned != null);
            LocalCache.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    /* loaded from: classes.dex */
    final class KeyIterator extends LocalCache<K, V>.HashIterator {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        KeyIterator() {
            super();
            LocalCache.this = this$0;
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    /* loaded from: classes.dex */
    final class ValueIterator extends LocalCache<K, V>.HashIterator {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ValueIterator() {
            super();
            LocalCache.this = this$0;
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public V next() {
            return nextEntry().getValue();
        }
    }

    /* loaded from: classes.dex */
    public final class WriteThroughEntry implements Map.Entry<K, V> {
        final K key;
        V value;

        WriteThroughEntry(K key, V value) {
            LocalCache.this = this$0;
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> that = (Map.Entry) object;
            if (!this.key.equals(that.getKey()) || !this.value.equals(that.getValue())) {
                return false;
            }
            return true;
        }

        @Override // java.util.Map.Entry, java.lang.Object
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        @Override // java.util.Map.Entry
        public V setValue(V newValue) {
            V oldValue = (V) LocalCache.this.put(this.key, newValue);
            this.value = newValue;
            return oldValue;
        }

        @Override // java.lang.Object
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    /* loaded from: classes.dex */
    final class EntryIterator extends LocalCache<K, V>.HashIterator {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        EntryIterator() {
            super();
            LocalCache.this = this$0;
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public abstract class AbstractCacheSet<T> extends AbstractSet<T> {
        final ConcurrentMap<?, ?> map;

        AbstractCacheSet(ConcurrentMap<?, ?> map) {
            LocalCache.this = this$0;
            this.map = map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.map.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return LocalCache.toArrayList(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <E> E[] toArray(E[] a2) {
            return (E[]) LocalCache.toArrayList(this).toArray(a2);
        }
    }

    public static <E> ArrayList<E> toArrayList(Collection<E> c) {
        ArrayList<E> result = new ArrayList<>(c.size());
        Iterators.addAll(result, c.iterator());
        return result;
    }

    /* loaded from: classes.dex */
    final class KeySet extends LocalCache<K, V>.AbstractCacheSet {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        KeySet(ConcurrentMap<?, ?> map) {
            super(map);
            LocalCache.this = this$0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return this.map.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return this.map.remove(o) != null;
        }
    }

    /* loaded from: classes.dex */
    final class Values extends AbstractCollection<V> {
        private final ConcurrentMap<?, ?> map;

        Values(ConcurrentMap<?, ?> map) {
            LocalCache.this = this$0;
            this.map = map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.map.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.map.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object o) {
            return this.map.containsValue(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return LocalCache.toArrayList(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <E> E[] toArray(E[] a2) {
            return (E[]) LocalCache.toArrayList(this).toArray(a2);
        }
    }

    /* loaded from: classes.dex */
    final class EntrySet extends LocalCache<K, V>.AbstractCacheSet {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        EntrySet(ConcurrentMap<?, ?> map) {
            super(map);
            LocalCache.this = this$0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            Map.Entry<?, ?> e;
            Object key;
            Object obj;
            if ((o instanceof Map.Entry) && (key = (e = (Map.Entry) o).getKey()) != null && (obj = LocalCache.this.get(key)) != null && LocalCache.this.valueEquivalence.equivalent(e.getValue(), obj)) {
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            Map.Entry<?, ?> e;
            Object key;
            if ((o instanceof Map.Entry) && (key = (e = (Map.Entry) o).getKey()) != null && LocalCache.this.remove(key, e.getValue())) {
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ManualSerializationProxy<K, V> extends ForwardingCache<K, V> implements Serializable {
        private static final long serialVersionUID;
        final int concurrencyLevel;
        @NullableDecl
        transient Cache<K, V> delegate;
        final long expireAfterAccessNanos;
        final long expireAfterWriteNanos;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final CacheLoader<? super K, V> loader;
        final long maxWeight;
        final RemovalListener<? super K, ? super V> removalListener;
        @NullableDecl
        final Ticker ticker;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;
        final Weigher<K, V> weigher;

        ManualSerializationProxy(LocalCache<K, V> cache) {
            this(cache.keyStrength, cache.valueStrength, cache.keyEquivalence, cache.valueEquivalence, cache.expireAfterWriteNanos, cache.expireAfterAccessNanos, cache.maxWeight, cache.weigher, cache.concurrencyLevel, cache.removalListener, cache.ticker, cache.defaultLoader);
        }

        private ManualSerializationProxy(Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, long maxWeight, Weigher<K, V> weigher, int concurrencyLevel, RemovalListener<? super K, ? super V> removalListener, Ticker ticker, CacheLoader<? super K, V> loader) {
            this.keyStrength = keyStrength;
            this.valueStrength = valueStrength;
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
            this.expireAfterWriteNanos = expireAfterWriteNanos;
            this.expireAfterAccessNanos = expireAfterAccessNanos;
            this.maxWeight = maxWeight;
            this.weigher = weigher;
            this.concurrencyLevel = concurrencyLevel;
            this.removalListener = removalListener;
            this.ticker = (ticker == Ticker.systemTicker() || ticker == CacheBuilder.NULL_TICKER) ? null : ticker;
            this.loader = loader;
        }

        CacheBuilder<K, V> recreateCacheBuilder() {
            CacheBuilder<K, V> builder = (CacheBuilder<K, V>) CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener((RemovalListener<? super K, ? super V>) this.removalListener);
            builder.strictParsing = false;
            long j = this.expireAfterWriteNanos;
            if (j > 0) {
                builder.expireAfterWrite(j, TimeUnit.NANOSECONDS);
            }
            long j2 = this.expireAfterAccessNanos;
            if (j2 > 0) {
                builder.expireAfterAccess(j2, TimeUnit.NANOSECONDS);
            }
            if (this.weigher != CacheBuilder.OneWeigher.INSTANCE) {
                builder.weigher(this.weigher);
                long j3 = this.maxWeight;
                if (j3 != -1) {
                    builder.maximumWeight(j3);
                }
            } else {
                long j4 = this.maxWeight;
                if (j4 != -1) {
                    builder.maximumSize(j4);
                }
            }
            Ticker ticker = this.ticker;
            if (ticker != null) {
                builder.ticker(ticker);
            }
            return builder;
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.delegate = (Cache<K, V>) recreateCacheBuilder().build();
        }

        private Object readResolve() {
            return this.delegate;
        }

        @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        public Cache<K, V> delegate() {
            return this.delegate;
        }
    }

    /* loaded from: classes.dex */
    static final class LoadingSerializationProxy<K, V> extends ManualSerializationProxy<K, V> implements LoadingCache<K, V>, Serializable {
        private static final long serialVersionUID;
        @NullableDecl
        transient LoadingCache<K, V> autoDelegate;

        LoadingSerializationProxy(LocalCache<K, V> cache) {
            super(cache);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.autoDelegate = (LoadingCache<K, V>) recreateCacheBuilder().build(this.loader);
        }

        @Override // com.google.common.cache.LoadingCache
        public V get(K key) throws ExecutionException {
            return this.autoDelegate.get(key);
        }

        @Override // com.google.common.cache.LoadingCache
        public V getUnchecked(K key) {
            return this.autoDelegate.getUnchecked(key);
        }

        @Override // com.google.common.cache.LoadingCache
        public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
            return this.autoDelegate.getAll(keys);
        }

        @Override // com.google.common.cache.LoadingCache, com.google.common.base.Function
        public final V apply(K key) {
            return this.autoDelegate.apply(key);
        }

        @Override // com.google.common.cache.LoadingCache
        public void refresh(K key) {
            this.autoDelegate.refresh(key);
        }

        private Object readResolve() {
            return this.autoDelegate;
        }
    }

    /* loaded from: classes.dex */
    public static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
        private static final long serialVersionUID;
        final LocalCache<K, V> localCache;

        public LocalManualCache(CacheBuilder<? super K, ? super V> builder) {
            this(new LocalCache(builder, null));
        }

        private LocalManualCache(LocalCache<K, V> localCache) {
            this.localCache = localCache;
        }

        @Override // com.google.common.cache.Cache
        @NullableDecl
        public V getIfPresent(Object key) {
            return this.localCache.getIfPresent(key);
        }

        @Override // com.google.common.cache.Cache
        public V get(K key, final Callable<? extends V> valueLoader) throws ExecutionException {
            Preconditions.checkNotNull(valueLoader);
            return this.localCache.get(key, new CacheLoader<Object, V>() { // from class: com.google.common.cache.LocalCache.LocalManualCache.1
                @Override // com.google.common.cache.CacheLoader
                public V load(Object key2) throws Exception {
                    return (V) valueLoader.call();
                }
            });
        }

        @Override // com.google.common.cache.Cache
        public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
            return this.localCache.getAllPresent(keys);
        }

        @Override // com.google.common.cache.Cache
        public void put(K key, V value) {
            this.localCache.put(key, value);
        }

        @Override // com.google.common.cache.Cache
        public void putAll(Map<? extends K, ? extends V> m) {
            this.localCache.putAll(m);
        }

        @Override // com.google.common.cache.Cache
        public void invalidate(Object key) {
            Preconditions.checkNotNull(key);
            this.localCache.remove(key);
        }

        @Override // com.google.common.cache.Cache
        public void invalidateAll(Iterable<?> keys) {
            this.localCache.invalidateAll(keys);
        }

        @Override // com.google.common.cache.Cache
        public void invalidateAll() {
            this.localCache.clear();
        }

        @Override // com.google.common.cache.Cache
        public long size() {
            return this.localCache.longSize();
        }

        @Override // com.google.common.cache.Cache
        public ConcurrentMap<K, V> asMap() {
            return this.localCache;
        }

        @Override // com.google.common.cache.Cache
        public CacheStats stats() {
            AbstractCache.SimpleStatsCounter aggregator = new AbstractCache.SimpleStatsCounter();
            aggregator.incrementBy(this.localCache.globalStatsCounter);
            for (Segment<K, V> segment : this.localCache.segments) {
                aggregator.incrementBy(segment.statsCounter);
            }
            return aggregator.snapshot();
        }

        @Override // com.google.common.cache.Cache
        public void cleanUp() {
            this.localCache.cleanUp();
        }

        Object writeReplace() {
            return new ManualSerializationProxy(this.localCache);
        }
    }

    /* loaded from: classes.dex */
    public static class LocalLoadingCache<K, V> extends LocalManualCache<K, V> implements LoadingCache<K, V> {
        private static final long serialVersionUID;

        public LocalLoadingCache(CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader) {
            super();
        }

        @Override // com.google.common.cache.LoadingCache
        public V get(K key) throws ExecutionException {
            return (V) this.localCache.getOrLoad(key);
        }

        @Override // com.google.common.cache.LoadingCache
        public V getUnchecked(K key) {
            try {
                return get(key);
            } catch (ExecutionException e) {
                throw new UncheckedExecutionException(e.getCause());
            }
        }

        @Override // com.google.common.cache.LoadingCache
        public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
            return this.localCache.getAll(keys);
        }

        @Override // com.google.common.cache.LoadingCache
        public void refresh(K key) {
            this.localCache.refresh(key);
        }

        @Override // com.google.common.cache.LoadingCache, com.google.common.base.Function
        public final V apply(K key) {
            return getUnchecked(key);
        }

        @Override // com.google.common.cache.LocalCache.LocalManualCache
        Object writeReplace() {
            return new LoadingSerializationProxy(this.localCache);
        }
    }
}
