package com.facebook.imagepipeline.cache;

import android.graphics.Bitmap;
import android.os.SystemClock;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Predicate;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class CountingMemoryCache<K, V> implements MemoryCache<K, V>, MemoryTrimmable {
    static final long PARAMS_INTERCHECK_INTERVAL_MS = TimeUnit.MINUTES.toMillis(5);
    private final CacheTrimStrategy mCacheTrimStrategy;
    final CountingLruMap<K, Entry<K, V>> mCachedEntries;
    final CountingLruMap<K, Entry<K, V>> mExclusiveEntries;
    protected MemoryCacheParams mMemoryCacheParams;
    private final Supplier<MemoryCacheParams> mMemoryCacheParamsSupplier;
    private final ValueDescriptor<V> mValueDescriptor;
    final Map<Bitmap, Object> mOtherEntries = new WeakHashMap();
    private long mLastCacheParamsCheck = SystemClock.uptimeMillis();

    /* loaded from: classes.dex */
    public interface CacheTrimStrategy {
        double getTrimRatio(MemoryTrimType memoryTrimType);
    }

    /* loaded from: classes.dex */
    public interface EntryStateObserver<K> {
        void onExclusivityChanged(K k, boolean z);
    }

    /* loaded from: classes.dex */
    public static class Entry<K, V> {
        public int clientCount = 0;
        public boolean isOrphan = false;
        public final K key;
        @Nullable
        public final EntryStateObserver<K> observer;
        public final CloseableReference<V> valueRef;

        private Entry(K key, CloseableReference<V> valueRef, @Nullable EntryStateObserver<K> observer) {
            this.key = (K) Preconditions.checkNotNull(key);
            this.valueRef = (CloseableReference) Preconditions.checkNotNull(CloseableReference.cloneOrNull(valueRef));
            this.observer = observer;
        }

        static <K, V> Entry<K, V> of(K key, CloseableReference<V> valueRef, @Nullable EntryStateObserver<K> observer) {
            return new Entry<>(key, valueRef, observer);
        }
    }

    public CountingMemoryCache(ValueDescriptor<V> valueDescriptor, CacheTrimStrategy cacheTrimStrategy, Supplier<MemoryCacheParams> memoryCacheParamsSupplier) {
        this.mValueDescriptor = valueDescriptor;
        this.mExclusiveEntries = new CountingLruMap<>(wrapValueDescriptor(valueDescriptor));
        this.mCachedEntries = new CountingLruMap<>(wrapValueDescriptor(valueDescriptor));
        this.mCacheTrimStrategy = cacheTrimStrategy;
        this.mMemoryCacheParamsSupplier = memoryCacheParamsSupplier;
        this.mMemoryCacheParams = this.mMemoryCacheParamsSupplier.get();
    }

    private ValueDescriptor<Entry<K, V>> wrapValueDescriptor(final ValueDescriptor<V> evictableValueDescriptor) {
        return new ValueDescriptor<Entry<K, V>>() { // from class: com.facebook.imagepipeline.cache.CountingMemoryCache.1
            @Override // com.facebook.imagepipeline.cache.ValueDescriptor
            public /* bridge */ /* synthetic */ int getSizeInBytes(Object obj) {
                return getSizeInBytes((Entry) ((Entry) obj));
            }

            public int getSizeInBytes(Entry<K, V> entry) {
                return evictableValueDescriptor.getSizeInBytes(entry.valueRef.get());
            }
        };
    }

    @Override // com.facebook.imagepipeline.cache.MemoryCache
    public CloseableReference<V> cache(K key, CloseableReference<V> valueRef) {
        return cache(key, valueRef, null);
    }

    @Nullable
    public CloseableReference<V> cache(K key, CloseableReference<V> valueRef, EntryStateObserver<K> observer) {
        Entry<K, V> oldExclusive;
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(valueRef);
        maybeUpdateCacheParams();
        CloseableReference<V> oldRefToClose = null;
        CloseableReference<V> clientRef = null;
        synchronized (this) {
            oldExclusive = this.mExclusiveEntries.remove(key);
            Entry<K, V> oldEntry = this.mCachedEntries.remove(key);
            if (oldEntry != null) {
                makeOrphan(oldEntry);
                oldRefToClose = referenceToClose(oldEntry);
            }
            if (canCacheNewValue(valueRef.get())) {
                Entry<K, V> newEntry = Entry.of(key, valueRef, observer);
                this.mCachedEntries.put(key, newEntry);
                clientRef = newClientReference(newEntry);
            }
        }
        CloseableReference.closeSafely((CloseableReference<?>) oldRefToClose);
        maybeNotifyExclusiveEntryRemoval(oldExclusive);
        maybeEvictEntries();
        return clientRef;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0022, code lost:
        if (getInUseSizeInBytes() <= (r4.mMemoryCacheParams.maxCacheSize - r0)) goto L_0x0026;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private synchronized boolean canCacheNewValue(V value) {
        boolean z;
        int newValueSize = this.mValueDescriptor.getSizeInBytes(value);
        z = true;
        if (newValueSize <= this.mMemoryCacheParams.maxCacheEntrySize && getInUseCount() <= this.mMemoryCacheParams.maxCacheEntries - 1) {
        }
        z = false;
        return z;
    }

    @Override // com.facebook.imagepipeline.cache.MemoryCache
    @Nullable
    public CloseableReference<V> get(K key) {
        Entry<K, V> oldExclusive;
        Preconditions.checkNotNull(key);
        CloseableReference<V> clientRef = null;
        synchronized (this) {
            oldExclusive = this.mExclusiveEntries.remove(key);
            Entry<K, V> entry = this.mCachedEntries.get(key);
            if (entry != null) {
                clientRef = newClientReference(entry);
            }
        }
        maybeNotifyExclusiveEntryRemoval(oldExclusive);
        maybeUpdateCacheParams();
        maybeEvictEntries();
        return clientRef;
    }

    private synchronized CloseableReference<V> newClientReference(final Entry<K, V> entry) {
        increaseClientCount(entry);
        return CloseableReference.of(entry.valueRef.get(), new ResourceReleaser<V>() { // from class: com.facebook.imagepipeline.cache.CountingMemoryCache.2
            @Override // com.facebook.common.references.ResourceReleaser
            public void release(V unused) {
                CountingMemoryCache.this.releaseClientReference(entry);
            }
        });
    }

    public void releaseClientReference(Entry<K, V> entry) {
        boolean isExclusiveAdded;
        CloseableReference<V> oldRefToClose;
        Preconditions.checkNotNull(entry);
        synchronized (this) {
            decreaseClientCount(entry);
            isExclusiveAdded = maybeAddToExclusives(entry);
            oldRefToClose = referenceToClose(entry);
        }
        CloseableReference.closeSafely((CloseableReference<?>) oldRefToClose);
        maybeNotifyExclusiveEntryInsertion(isExclusiveAdded ? entry : null);
        maybeUpdateCacheParams();
        maybeEvictEntries();
    }

    private synchronized boolean maybeAddToExclusives(Entry<K, V> entry) {
        if (entry.isOrphan || entry.clientCount != 0) {
            return false;
        }
        this.mExclusiveEntries.put(entry.key, entry);
        return true;
    }

    @Nullable
    public CloseableReference<V> reuse(K key) {
        Entry<K, V> oldExclusive;
        Preconditions.checkNotNull(key);
        CloseableReference<V> clientRef = null;
        boolean removed = false;
        synchronized (this) {
            oldExclusive = this.mExclusiveEntries.remove(key);
            if (oldExclusive != null) {
                Entry<K, V> entry = this.mCachedEntries.remove(key);
                Preconditions.checkNotNull(entry);
                Preconditions.checkState(entry.clientCount == 0);
                clientRef = entry.valueRef;
                removed = true;
            }
        }
        if (removed) {
            maybeNotifyExclusiveEntryRemoval(oldExclusive);
        }
        return clientRef;
    }

    @Override // com.facebook.imagepipeline.cache.MemoryCache
    public int removeAll(Predicate<K> predicate) {
        ArrayList<Entry<K, V>> oldExclusives;
        ArrayList<Entry<K, V>> oldEntries;
        synchronized (this) {
            oldExclusives = this.mExclusiveEntries.removeAll(predicate);
            oldEntries = this.mCachedEntries.removeAll(predicate);
            makeOrphans(oldEntries);
        }
        maybeClose(oldEntries);
        maybeNotifyExclusiveEntryRemoval(oldExclusives);
        maybeUpdateCacheParams();
        maybeEvictEntries();
        return oldEntries.size();
    }

    public void clear() {
        ArrayList<Entry<K, V>> oldExclusives;
        ArrayList<Entry<K, V>> oldEntries;
        synchronized (this) {
            oldExclusives = this.mExclusiveEntries.clear();
            oldEntries = this.mCachedEntries.clear();
            makeOrphans(oldEntries);
        }
        maybeClose(oldEntries);
        maybeNotifyExclusiveEntryRemoval(oldExclusives);
        maybeUpdateCacheParams();
    }

    @Override // com.facebook.imagepipeline.cache.MemoryCache
    public synchronized boolean contains(Predicate<K> predicate) {
        return !this.mCachedEntries.getMatchingEntries(predicate).isEmpty();
    }

    @Override // com.facebook.imagepipeline.cache.MemoryCache
    public synchronized boolean contains(K key) {
        return this.mCachedEntries.contains(key);
    }

    @Override // com.facebook.common.memory.MemoryTrimmable
    public void trim(MemoryTrimType trimType) {
        ArrayList<Entry<K, V>> oldEntries;
        double trimRatio = this.mCacheTrimStrategy.getTrimRatio(trimType);
        synchronized (this) {
            oldEntries = trimExclusivelyOwnedEntries(Integer.MAX_VALUE, Math.max(0, ((int) (((double) this.mCachedEntries.getSizeInBytes()) * (1.0d - trimRatio))) - getInUseSizeInBytes()));
            makeOrphans(oldEntries);
        }
        maybeClose(oldEntries);
        maybeNotifyExclusiveEntryRemoval(oldEntries);
        maybeUpdateCacheParams();
        maybeEvictEntries();
    }

    private synchronized void maybeUpdateCacheParams() {
        if (this.mLastCacheParamsCheck + PARAMS_INTERCHECK_INTERVAL_MS <= SystemClock.uptimeMillis()) {
            this.mLastCacheParamsCheck = SystemClock.uptimeMillis();
            this.mMemoryCacheParams = this.mMemoryCacheParamsSupplier.get();
        }
    }

    private void maybeEvictEntries() {
        ArrayList<Entry<K, V>> oldEntries;
        synchronized (this) {
            oldEntries = trimExclusivelyOwnedEntries(Math.min(this.mMemoryCacheParams.maxEvictionQueueEntries, this.mMemoryCacheParams.maxCacheEntries - getInUseCount()), Math.min(this.mMemoryCacheParams.maxEvictionQueueSize, this.mMemoryCacheParams.maxCacheSize - getInUseSizeInBytes()));
            makeOrphans(oldEntries);
        }
        maybeClose(oldEntries);
        maybeNotifyExclusiveEntryRemoval(oldEntries);
    }

    @Nullable
    private synchronized ArrayList<Entry<K, V>> trimExclusivelyOwnedEntries(int count, int size) {
        int count2 = Math.max(count, 0);
        int size2 = Math.max(size, 0);
        if (this.mExclusiveEntries.getCount() <= count2 && this.mExclusiveEntries.getSizeInBytes() <= size2) {
            return null;
        }
        ArrayList<Entry<K, V>> oldEntries = new ArrayList<>();
        while (true) {
            if (this.mExclusiveEntries.getCount() <= count2 && this.mExclusiveEntries.getSizeInBytes() <= size2) {
                return oldEntries;
            }
            K key = this.mExclusiveEntries.getFirstKey();
            this.mExclusiveEntries.remove(key);
            oldEntries.add(this.mCachedEntries.remove(key));
        }
    }

    private void maybeClose(@Nullable ArrayList<Entry<K, V>> oldEntries) {
        if (oldEntries != null) {
            Iterator<Entry<K, V>> it = oldEntries.iterator();
            while (it.hasNext()) {
                CloseableReference.closeSafely((CloseableReference<?>) referenceToClose(it.next()));
            }
        }
    }

    private void maybeNotifyExclusiveEntryRemoval(@Nullable ArrayList<Entry<K, V>> entries) {
        if (entries != null) {
            Iterator<Entry<K, V>> it = entries.iterator();
            while (it.hasNext()) {
                maybeNotifyExclusiveEntryRemoval(it.next());
            }
        }
    }

    private static <K, V> void maybeNotifyExclusiveEntryRemoval(@Nullable Entry<K, V> entry) {
        if (entry != null && entry.observer != null) {
            entry.observer.onExclusivityChanged(entry.key, false);
        }
    }

    private static <K, V> void maybeNotifyExclusiveEntryInsertion(@Nullable Entry<K, V> entry) {
        if (entry != null && entry.observer != null) {
            entry.observer.onExclusivityChanged(entry.key, true);
        }
    }

    private synchronized void makeOrphans(@Nullable ArrayList<Entry<K, V>> oldEntries) {
        if (oldEntries != null) {
            Iterator<Entry<K, V>> it = oldEntries.iterator();
            while (it.hasNext()) {
                makeOrphan(it.next());
            }
        }
    }

    private synchronized void makeOrphan(Entry<K, V> entry) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkState(!entry.isOrphan);
        entry.isOrphan = true;
    }

    private synchronized void increaseClientCount(Entry<K, V> entry) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkState(!entry.isOrphan);
        entry.clientCount++;
    }

    private synchronized void decreaseClientCount(Entry<K, V> entry) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkState(entry.clientCount > 0);
        entry.clientCount--;
    }

    @Nullable
    private synchronized CloseableReference<V> referenceToClose(Entry<K, V> entry) {
        Preconditions.checkNotNull(entry);
        return (!entry.isOrphan || entry.clientCount != 0) ? null : entry.valueRef;
    }

    public synchronized int getCount() {
        return this.mCachedEntries.getCount();
    }

    public synchronized int getSizeInBytes() {
        return this.mCachedEntries.getSizeInBytes();
    }

    public synchronized int getInUseCount() {
        return this.mCachedEntries.getCount() - this.mExclusiveEntries.getCount();
    }

    public synchronized int getInUseSizeInBytes() {
        return this.mCachedEntries.getSizeInBytes() - this.mExclusiveEntries.getSizeInBytes();
    }

    public synchronized int getEvictionQueueCount() {
        return this.mExclusiveEntries.getCount();
    }

    public synchronized int getEvictionQueueSizeInBytes() {
        return this.mExclusiveEntries.getSizeInBytes();
    }
}
