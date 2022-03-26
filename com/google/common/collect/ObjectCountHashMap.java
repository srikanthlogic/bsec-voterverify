package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.Arrays;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public class ObjectCountHashMap<K> {
    static final float DEFAULT_LOAD_FACTOR;
    static final int DEFAULT_SIZE;
    private static final long HASH_MASK;
    private static final int MAXIMUM_CAPACITY;
    private static final long NEXT_MASK;
    static final int UNSET;
    transient long[] entries;
    transient Object[] keys;
    private transient float loadFactor;
    transient int modCount;
    transient int size;
    private transient int[] table;
    private transient int threshold;
    transient int[] values;

    public static <K> ObjectCountHashMap<K> create() {
        return new ObjectCountHashMap<>();
    }

    public static <K> ObjectCountHashMap<K> createWithExpectedSize(int expectedSize) {
        return new ObjectCountHashMap<>(expectedSize);
    }

    public ObjectCountHashMap() {
        init(3, 1.0f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ObjectCountHashMap(ObjectCountHashMap<? extends K> map) {
        init(map.size(), 1.0f);
        int i = map.firstIndex();
        while (i != -1) {
            put(map.getKey(i), map.getValue(i));
            i = map.nextIndex(i);
        }
    }

    public ObjectCountHashMap(int capacity) {
        this(capacity, 1.0f);
    }

    public ObjectCountHashMap(int expectedSize, float loadFactor) {
        init(expectedSize, loadFactor);
    }

    public void init(int expectedSize, float loadFactor) {
        boolean z = false;
        Preconditions.checkArgument(expectedSize >= 0, "Initial capacity must be non-negative");
        if (loadFactor > 0.0f) {
            z = true;
        }
        Preconditions.checkArgument(z, "Illegal load factor");
        int buckets = Hashing.closedTableSize(expectedSize, (double) loadFactor);
        this.table = newTable(buckets);
        this.loadFactor = loadFactor;
        this.keys = new Object[expectedSize];
        this.values = new int[expectedSize];
        this.entries = newEntries(expectedSize);
        this.threshold = Math.max(1, (int) (((float) buckets) * loadFactor));
    }

    private static int[] newTable(int size) {
        int[] array = new int[size];
        Arrays.fill(array, -1);
        return array;
    }

    private static long[] newEntries(int size) {
        long[] array = new long[size];
        Arrays.fill(array, -1L);
        return array;
    }

    private int hashTableMask() {
        return this.table.length - 1;
    }

    public int firstIndex() {
        return this.size == 0 ? -1 : 0;
    }

    public int nextIndex(int index) {
        if (index + 1 < this.size) {
            return index + 1;
        }
        return -1;
    }

    public int nextIndexAfterRemove(int oldNextIndex, int removedIndex) {
        return oldNextIndex - 1;
    }

    public int size() {
        return this.size;
    }

    public K getKey(int index) {
        Preconditions.checkElementIndex(index, this.size);
        return (K) this.keys[index];
    }

    public int getValue(int index) {
        Preconditions.checkElementIndex(index, this.size);
        return this.values[index];
    }

    public void setValue(int index, int newValue) {
        Preconditions.checkElementIndex(index, this.size);
        this.values[index] = newValue;
    }

    public Multiset.Entry<K> getEntry(int index) {
        Preconditions.checkElementIndex(index, this.size);
        return new MapEntry(index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class MapEntry extends Multisets.AbstractEntry<K> {
        @NullableDecl
        final K key;
        int lastKnownIndex;

        MapEntry(int index) {
            ObjectCountHashMap.this = this$0;
            this.key = (K) this$0.keys[index];
            this.lastKnownIndex = index;
        }

        @Override // com.google.common.collect.Multiset.Entry
        public K getElement() {
            return this.key;
        }

        void updateLastKnownIndex() {
            int i = this.lastKnownIndex;
            if (i == -1 || i >= ObjectCountHashMap.this.size() || !Objects.equal(this.key, ObjectCountHashMap.this.keys[this.lastKnownIndex])) {
                this.lastKnownIndex = ObjectCountHashMap.this.indexOf(this.key);
            }
        }

        @Override // com.google.common.collect.Multiset.Entry
        public int getCount() {
            updateLastKnownIndex();
            if (this.lastKnownIndex == -1) {
                return 0;
            }
            return ObjectCountHashMap.this.values[this.lastKnownIndex];
        }

        public int setCount(int count) {
            updateLastKnownIndex();
            if (this.lastKnownIndex == -1) {
                ObjectCountHashMap.this.put(this.key, count);
                return 0;
            }
            int old = ObjectCountHashMap.this.values[this.lastKnownIndex];
            ObjectCountHashMap.this.values[this.lastKnownIndex] = count;
            return old;
        }
    }

    private static int getHash(long entry) {
        return (int) (entry >>> 32);
    }

    private static int getNext(long entry) {
        return (int) entry;
    }

    private static long swapNext(long entry, int newNext) {
        return (HASH_MASK & entry) | (((long) newNext) & NEXT_MASK);
    }

    public void ensureCapacity(int minCapacity) {
        if (minCapacity > this.entries.length) {
            resizeEntries(minCapacity);
        }
        if (minCapacity >= this.threshold) {
            resizeTable(Math.max(2, Integer.highestOneBit(minCapacity - 1) << 1));
        }
    }

    public int put(@NullableDecl K key, int value) {
        long entry;
        CollectPreconditions.checkPositive(value, "count");
        long[] entries = this.entries;
        Object[] keys = this.keys;
        int[] values = this.values;
        int hash = Hashing.smearedHash(key);
        int tableIndex = hashTableMask() & hash;
        int newEntryIndex = this.size;
        int[] iArr = this.table;
        int next = iArr[tableIndex];
        if (next == -1) {
            iArr[tableIndex] = newEntryIndex;
        } else {
            do {
                entry = entries[next];
                if (getHash(entry) != hash || !Objects.equal(key, keys[next])) {
                    next = getNext(entry);
                } else {
                    int oldValue = values[next];
                    values[next] = value;
                    return oldValue;
                }
            } while (next != -1);
            entries[next] = swapNext(entry, newEntryIndex);
        }
        if (newEntryIndex != Integer.MAX_VALUE) {
            int newSize = newEntryIndex + 1;
            resizeMeMaybe(newSize);
            insertEntry(newEntryIndex, key, value, hash);
            this.size = newSize;
            if (newEntryIndex >= this.threshold) {
                resizeTable(this.table.length * 2);
            }
            this.modCount++;
            return 0;
        }
        throw new IllegalStateException("Cannot contain more than Integer.MAX_VALUE elements!");
    }

    public void insertEntry(int entryIndex, @NullableDecl K key, int value, int hash) {
        this.entries[entryIndex] = (((long) hash) << 32) | NEXT_MASK;
        this.keys[entryIndex] = key;
        this.values[entryIndex] = value;
    }

    private void resizeMeMaybe(int newSize) {
        int entriesSize = this.entries.length;
        if (newSize > entriesSize) {
            int newCapacity = Math.max(1, entriesSize >>> 1) + entriesSize;
            if (newCapacity < 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            if (newCapacity != entriesSize) {
                resizeEntries(newCapacity);
            }
        }
    }

    public void resizeEntries(int newCapacity) {
        this.keys = Arrays.copyOf(this.keys, newCapacity);
        this.values = Arrays.copyOf(this.values, newCapacity);
        long[] entries = this.entries;
        int oldCapacity = entries.length;
        long[] entries2 = Arrays.copyOf(entries, newCapacity);
        if (newCapacity > oldCapacity) {
            Arrays.fill(entries2, oldCapacity, newCapacity, -1L);
        }
        this.entries = entries2;
    }

    private void resizeTable(int newCapacity) {
        int[] oldTable = this.table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= 1073741824) {
            this.threshold = Integer.MAX_VALUE;
            return;
        }
        int newThreshold = ((int) (((float) newCapacity) * this.loadFactor)) + 1;
        int[] newTable = newTable(newCapacity);
        long[] entries = this.entries;
        int mask = newTable.length - 1;
        int i = 0;
        while (i < this.size) {
            int hash = getHash(entries[i]);
            int tableIndex = hash & mask;
            int next = newTable[tableIndex];
            newTable[tableIndex] = i;
            entries[i] = (((long) next) & NEXT_MASK) | (((long) hash) << 32);
            i++;
            oldTable = oldTable;
            oldCapacity = oldCapacity;
        }
        this.threshold = newThreshold;
        this.table = newTable;
    }

    public int indexOf(@NullableDecl Object key) {
        int hash = Hashing.smearedHash(key);
        int next = this.table[hashTableMask() & hash];
        while (next != -1) {
            long entry = this.entries[next];
            if (getHash(entry) == hash && Objects.equal(key, this.keys[next])) {
                return next;
            }
            next = getNext(entry);
        }
        return -1;
    }

    public boolean containsKey(@NullableDecl Object key) {
        return indexOf(key) != -1;
    }

    public int get(@NullableDecl Object key) {
        int index = indexOf(key);
        if (index == -1) {
            return 0;
        }
        return this.values[index];
    }

    public int remove(@NullableDecl Object key) {
        return remove(key, Hashing.smearedHash(key));
    }

    private int remove(@NullableDecl Object key, int hash) {
        int tableIndex = hashTableMask() & hash;
        int next = this.table[tableIndex];
        if (next == -1) {
            return 0;
        }
        int last = -1;
        do {
            if (getHash(this.entries[next]) != hash || !Objects.equal(key, this.keys[next])) {
                last = next;
                next = getNext(this.entries[next]);
            } else {
                int oldValue = this.values[next];
                if (last == -1) {
                    this.table[tableIndex] = getNext(this.entries[next]);
                } else {
                    long[] jArr = this.entries;
                    jArr[last] = swapNext(jArr[last], getNext(jArr[next]));
                }
                moveLastEntry(next);
                this.size--;
                this.modCount++;
                return oldValue;
            }
        } while (next != -1);
        return 0;
    }

    public int removeEntry(int entryIndex) {
        return remove(this.keys[entryIndex], getHash(this.entries[entryIndex]));
    }

    public void moveLastEntry(int dstIndex) {
        long entry;
        int srcIndex = size() - 1;
        if (dstIndex < srcIndex) {
            Object[] objArr = this.keys;
            objArr[dstIndex] = objArr[srcIndex];
            int[] iArr = this.values;
            iArr[dstIndex] = iArr[srcIndex];
            objArr[srcIndex] = null;
            iArr[srcIndex] = 0;
            long[] jArr = this.entries;
            long lastEntry = jArr[srcIndex];
            jArr[dstIndex] = lastEntry;
            jArr[srcIndex] = -1;
            int tableIndex = getHash(lastEntry) & hashTableMask();
            int[] iArr2 = this.table;
            int lastNext = iArr2[tableIndex];
            if (lastNext == srcIndex) {
                iArr2[tableIndex] = dstIndex;
                return;
            }
            do {
                entry = this.entries[lastNext];
                lastNext = getNext(entry);
            } while (lastNext != srcIndex);
            this.entries[lastNext] = swapNext(entry, dstIndex);
            return;
        }
        this.keys[dstIndex] = null;
        this.values[dstIndex] = 0;
        this.entries[dstIndex] = -1;
    }

    public void clear() {
        this.modCount++;
        Arrays.fill(this.keys, 0, this.size, (Object) null);
        Arrays.fill(this.values, 0, this.size, 0);
        Arrays.fill(this.table, -1);
        Arrays.fill(this.entries, -1L);
        this.size = 0;
    }
}
