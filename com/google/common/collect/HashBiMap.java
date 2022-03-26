package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class HashBiMap<K, V> extends AbstractMap<K, V> implements BiMap<K, V>, Serializable {
    private static final int ABSENT;
    private static final int ENDPOINT;
    private transient Set<Map.Entry<K, V>> entrySet;
    @NullableDecl
    private transient int firstInInsertionOrder;
    private transient int[] hashTableKToV;
    private transient int[] hashTableVToK;
    @NullableDecl
    @LazyInit
    private transient BiMap<V, K> inverse;
    private transient Set<K> keySet;
    transient K[] keys;
    @NullableDecl
    private transient int lastInInsertionOrder;
    transient int modCount;
    private transient int[] nextInBucketKToV;
    private transient int[] nextInBucketVToK;
    private transient int[] nextInInsertionOrder;
    private transient int[] prevInInsertionOrder;
    transient int size;
    private transient Set<V> valueSet;
    transient V[] values;

    public static <K, V> HashBiMap<K, V> create() {
        return create(16);
    }

    public static <K, V> HashBiMap<K, V> create(int expectedSize) {
        return new HashBiMap<>(expectedSize);
    }

    public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
        HashBiMap<K, V> bimap = create(map.size());
        bimap.putAll(map);
        return bimap;
    }

    private HashBiMap(int expectedSize) {
        init(expectedSize);
    }

    void init(int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        int tableSize = Hashing.closedTableSize(expectedSize, 1.0d);
        this.size = 0;
        this.keys = (K[]) new Object[expectedSize];
        this.values = (V[]) new Object[expectedSize];
        this.hashTableKToV = createFilledWithAbsent(tableSize);
        this.hashTableVToK = createFilledWithAbsent(tableSize);
        this.nextInBucketKToV = createFilledWithAbsent(expectedSize);
        this.nextInBucketVToK = createFilledWithAbsent(expectedSize);
        this.firstInInsertionOrder = -2;
        this.lastInInsertionOrder = -2;
        this.prevInInsertionOrder = createFilledWithAbsent(expectedSize);
        this.nextInInsertionOrder = createFilledWithAbsent(expectedSize);
    }

    private static int[] createFilledWithAbsent(int size) {
        int[] array = new int[size];
        Arrays.fill(array, -1);
        return array;
    }

    private static int[] expandAndFillWithAbsent(int[] array, int newSize) {
        int oldSize = array.length;
        int[] result = Arrays.copyOf(array, newSize);
        Arrays.fill(result, oldSize, newSize, -1);
        return result;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    private void ensureCapacity(int minCapacity) {
        int[] iArr = this.nextInBucketKToV;
        if (iArr.length < minCapacity) {
            int newCapacity = ImmutableCollection.Builder.expandedCapacity(iArr.length, minCapacity);
            this.keys = (K[]) Arrays.copyOf(this.keys, newCapacity);
            this.values = (V[]) Arrays.copyOf(this.values, newCapacity);
            this.nextInBucketKToV = expandAndFillWithAbsent(this.nextInBucketKToV, newCapacity);
            this.nextInBucketVToK = expandAndFillWithAbsent(this.nextInBucketVToK, newCapacity);
            this.prevInInsertionOrder = expandAndFillWithAbsent(this.prevInInsertionOrder, newCapacity);
            this.nextInInsertionOrder = expandAndFillWithAbsent(this.nextInInsertionOrder, newCapacity);
        }
        if (this.hashTableKToV.length < minCapacity) {
            int newTableSize = Hashing.closedTableSize(minCapacity, 1.0d);
            this.hashTableKToV = createFilledWithAbsent(newTableSize);
            this.hashTableVToK = createFilledWithAbsent(newTableSize);
            for (int entryToRehash = 0; entryToRehash < this.size; entryToRehash++) {
                int keyBucket = bucket(Hashing.smearedHash(this.keys[entryToRehash]));
                int[] iArr2 = this.nextInBucketKToV;
                int[] iArr3 = this.hashTableKToV;
                iArr2[entryToRehash] = iArr3[keyBucket];
                iArr3[keyBucket] = entryToRehash;
                int valueBucket = bucket(Hashing.smearedHash(this.values[entryToRehash]));
                int[] iArr4 = this.nextInBucketVToK;
                int[] iArr5 = this.hashTableVToK;
                iArr4[entryToRehash] = iArr5[valueBucket];
                iArr5[valueBucket] = entryToRehash;
            }
        }
    }

    private int bucket(int hash) {
        return (this.hashTableKToV.length - 1) & hash;
    }

    int findEntryByKey(@NullableDecl Object key) {
        return findEntryByKey(key, Hashing.smearedHash(key));
    }

    int findEntryByKey(@NullableDecl Object key, int keyHash) {
        return findEntry(key, keyHash, this.hashTableKToV, this.nextInBucketKToV, this.keys);
    }

    int findEntryByValue(@NullableDecl Object value) {
        return findEntryByValue(value, Hashing.smearedHash(value));
    }

    int findEntryByValue(@NullableDecl Object value, int valueHash) {
        return findEntry(value, valueHash, this.hashTableVToK, this.nextInBucketVToK, this.values);
    }

    int findEntry(@NullableDecl Object o, int oHash, int[] hashTable, int[] nextInBucket, Object[] array) {
        int entry = hashTable[bucket(oHash)];
        while (entry != -1) {
            if (Objects.equal(array[entry], o)) {
                return entry;
            }
            entry = nextInBucket[entry];
        }
        return -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@NullableDecl Object key) {
        return findEntryByKey(key) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@NullableDecl Object value) {
        return findEntryByValue(value) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @NullableDecl
    public V get(@NullableDecl Object key) {
        int entry = findEntryByKey(key);
        if (entry == -1) {
            return null;
        }
        return this.values[entry];
    }

    @NullableDecl
    K getInverse(@NullableDecl Object value) {
        int entry = findEntryByValue(value);
        if (entry == -1) {
            return null;
        }
        return this.keys[entry];
    }

    @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
    public V put(@NullableDecl K key, @NullableDecl V value) {
        return put(key, value, false);
    }

    @NullableDecl
    V put(@NullableDecl K key, @NullableDecl V value, boolean force) {
        int keyHash = Hashing.smearedHash(key);
        int entryForKey = findEntryByKey(key, keyHash);
        if (entryForKey != -1) {
            V oldValue = this.values[entryForKey];
            if (Objects.equal(oldValue, value)) {
                return value;
            }
            replaceValueInEntry(entryForKey, value, force);
            return oldValue;
        }
        int valueHash = Hashing.smearedHash(value);
        int valueEntry = findEntryByValue(value, valueHash);
        if (!force) {
            Preconditions.checkArgument(valueEntry == -1, "Value already present: %s", value);
        } else if (valueEntry != -1) {
            removeEntryValueHashKnown(valueEntry, valueHash);
        }
        ensureCapacity(this.size + 1);
        K[] kArr = this.keys;
        int i = this.size;
        kArr[i] = key;
        this.values[i] = value;
        insertIntoTableKToV(i, keyHash);
        insertIntoTableVToK(this.size, valueHash);
        setSucceeds(this.lastInInsertionOrder, this.size);
        setSucceeds(this.size, -2);
        this.size++;
        this.modCount++;
        return null;
    }

    @Override // com.google.common.collect.BiMap
    @NullableDecl
    public V forcePut(@NullableDecl K key, @NullableDecl V value) {
        return put(key, value, true);
    }

    @NullableDecl
    K putInverse(@NullableDecl V value, @NullableDecl K key, boolean force) {
        int valueHash = Hashing.smearedHash(value);
        int entryForValue = findEntryByValue(value, valueHash);
        if (entryForValue != -1) {
            K oldKey = this.keys[entryForValue];
            if (Objects.equal(oldKey, key)) {
                return key;
            }
            replaceKeyInEntry(entryForValue, key, force);
            return oldKey;
        }
        int predecessor = this.lastInInsertionOrder;
        int keyHash = Hashing.smearedHash(key);
        int keyEntry = findEntryByKey(key, keyHash);
        if (!force) {
            Preconditions.checkArgument(keyEntry == -1, "Key already present: %s", key);
        } else if (keyEntry != -1) {
            predecessor = this.prevInInsertionOrder[keyEntry];
            removeEntryKeyHashKnown(keyEntry, keyHash);
        }
        ensureCapacity(this.size + 1);
        K[] kArr = this.keys;
        int i = this.size;
        kArr[i] = key;
        this.values[i] = value;
        insertIntoTableKToV(i, keyHash);
        insertIntoTableVToK(this.size, valueHash);
        int successor = predecessor == -2 ? this.firstInInsertionOrder : this.nextInInsertionOrder[predecessor];
        setSucceeds(predecessor, this.size);
        setSucceeds(this.size, successor);
        this.size++;
        this.modCount++;
        return null;
    }

    private void setSucceeds(int prev, int next) {
        if (prev == -2) {
            this.firstInInsertionOrder = next;
        } else {
            this.nextInInsertionOrder[prev] = next;
        }
        if (next == -2) {
            this.lastInInsertionOrder = prev;
        } else {
            this.prevInInsertionOrder[next] = prev;
        }
    }

    private void insertIntoTableKToV(int entry, int keyHash) {
        Preconditions.checkArgument(entry != -1);
        int keyBucket = bucket(keyHash);
        int[] iArr = this.nextInBucketKToV;
        int[] iArr2 = this.hashTableKToV;
        iArr[entry] = iArr2[keyBucket];
        iArr2[keyBucket] = entry;
    }

    private void insertIntoTableVToK(int entry, int valueHash) {
        Preconditions.checkArgument(entry != -1);
        int valueBucket = bucket(valueHash);
        int[] iArr = this.nextInBucketVToK;
        int[] iArr2 = this.hashTableVToK;
        iArr[entry] = iArr2[valueBucket];
        iArr2[valueBucket] = entry;
    }

    private void deleteFromTableKToV(int entry, int keyHash) {
        Preconditions.checkArgument(entry != -1);
        int keyBucket = bucket(keyHash);
        int[] iArr = this.hashTableKToV;
        if (iArr[keyBucket] == entry) {
            int[] iArr2 = this.nextInBucketKToV;
            iArr[keyBucket] = iArr2[entry];
            iArr2[entry] = -1;
            return;
        }
        int prevInBucket = iArr[keyBucket];
        int entryInBucket = this.nextInBucketKToV[prevInBucket];
        while (entryInBucket != -1) {
            if (entryInBucket == entry) {
                int[] iArr3 = this.nextInBucketKToV;
                iArr3[prevInBucket] = iArr3[entry];
                iArr3[entry] = -1;
                return;
            }
            prevInBucket = entryInBucket;
            entryInBucket = this.nextInBucketKToV[entryInBucket];
        }
        throw new AssertionError("Expected to find entry with key " + this.keys[entry]);
    }

    private void deleteFromTableVToK(int entry, int valueHash) {
        Preconditions.checkArgument(entry != -1);
        int valueBucket = bucket(valueHash);
        int[] iArr = this.hashTableVToK;
        if (iArr[valueBucket] == entry) {
            int[] iArr2 = this.nextInBucketVToK;
            iArr[valueBucket] = iArr2[entry];
            iArr2[entry] = -1;
            return;
        }
        int prevInBucket = iArr[valueBucket];
        int entryInBucket = this.nextInBucketVToK[prevInBucket];
        while (entryInBucket != -1) {
            if (entryInBucket == entry) {
                int[] iArr3 = this.nextInBucketVToK;
                iArr3[prevInBucket] = iArr3[entry];
                iArr3[entry] = -1;
                return;
            }
            prevInBucket = entryInBucket;
            entryInBucket = this.nextInBucketVToK[entryInBucket];
        }
        throw new AssertionError("Expected to find entry with value " + this.values[entry]);
    }

    public void replaceValueInEntry(int entry, @NullableDecl V newValue, boolean force) {
        Preconditions.checkArgument(entry != -1);
        int newValueHash = Hashing.smearedHash(newValue);
        int newValueIndex = findEntryByValue(newValue, newValueHash);
        if (newValueIndex != -1) {
            if (force) {
                removeEntryValueHashKnown(newValueIndex, newValueHash);
                if (entry == this.size) {
                    entry = newValueIndex;
                }
            } else {
                throw new IllegalArgumentException("Value already present in map: " + newValue);
            }
        }
        deleteFromTableVToK(entry, Hashing.smearedHash(this.values[entry]));
        this.values[entry] = newValue;
        insertIntoTableVToK(entry, newValueHash);
    }

    public void replaceKeyInEntry(int entry, @NullableDecl K newKey, boolean force) {
        Preconditions.checkArgument(entry != -1);
        int newKeyHash = Hashing.smearedHash(newKey);
        int newKeyIndex = findEntryByKey(newKey, newKeyHash);
        int newPredecessor = this.lastInInsertionOrder;
        int newSuccessor = -2;
        if (newKeyIndex != -1) {
            if (force) {
                newPredecessor = this.prevInInsertionOrder[newKeyIndex];
                newSuccessor = this.nextInInsertionOrder[newKeyIndex];
                removeEntryKeyHashKnown(newKeyIndex, newKeyHash);
                if (entry == this.size) {
                    entry = newKeyIndex;
                }
            } else {
                throw new IllegalArgumentException("Key already present in map: " + newKey);
            }
        }
        if (newPredecessor == entry) {
            newPredecessor = this.prevInInsertionOrder[entry];
        } else if (newPredecessor == this.size) {
            newPredecessor = newKeyIndex;
        }
        if (newSuccessor == entry) {
            newSuccessor = this.nextInInsertionOrder[entry];
        } else if (newSuccessor == this.size) {
            newSuccessor = newKeyIndex;
        }
        setSucceeds(this.prevInInsertionOrder[entry], this.nextInInsertionOrder[entry]);
        deleteFromTableKToV(entry, Hashing.smearedHash(this.keys[entry]));
        this.keys[entry] = newKey;
        insertIntoTableKToV(entry, Hashing.smearedHash(newKey));
        setSucceeds(newPredecessor, entry);
        setSucceeds(entry, newSuccessor);
    }

    @Override // java.util.AbstractMap, java.util.Map
    @NullableDecl
    public V remove(@NullableDecl Object key) {
        int keyHash = Hashing.smearedHash(key);
        int entry = findEntryByKey(key, keyHash);
        if (entry == -1) {
            return null;
        }
        V value = this.values[entry];
        removeEntryKeyHashKnown(entry, keyHash);
        return value;
    }

    @NullableDecl
    K removeInverse(@NullableDecl Object value) {
        int valueHash = Hashing.smearedHash(value);
        int entry = findEntryByValue(value, valueHash);
        if (entry == -1) {
            return null;
        }
        K key = this.keys[entry];
        removeEntryValueHashKnown(entry, valueHash);
        return key;
    }

    void removeEntry(int entry) {
        removeEntryKeyHashKnown(entry, Hashing.smearedHash(this.keys[entry]));
    }

    private void removeEntry(int entry, int keyHash, int valueHash) {
        Preconditions.checkArgument(entry != -1);
        deleteFromTableKToV(entry, keyHash);
        deleteFromTableVToK(entry, valueHash);
        setSucceeds(this.prevInInsertionOrder[entry], this.nextInInsertionOrder[entry]);
        moveEntryToIndex(this.size - 1, entry);
        K[] kArr = this.keys;
        int i = this.size;
        kArr[i - 1] = null;
        this.values[i - 1] = null;
        this.size = i - 1;
        this.modCount++;
    }

    void removeEntryKeyHashKnown(int entry, int keyHash) {
        removeEntry(entry, keyHash, Hashing.smearedHash(this.values[entry]));
    }

    void removeEntryValueHashKnown(int entry, int valueHash) {
        removeEntry(entry, Hashing.smearedHash(this.keys[entry]), valueHash);
    }

    private void moveEntryToIndex(int src, int dest) {
        if (src != dest) {
            int predecessor = this.prevInInsertionOrder[src];
            int successor = this.nextInInsertionOrder[src];
            setSucceeds(predecessor, dest);
            setSucceeds(dest, successor);
            K[] kArr = this.keys;
            K key = kArr[src];
            V[] vArr = this.values;
            V value = vArr[src];
            kArr[dest] = key;
            vArr[dest] = value;
            int keyBucket = bucket(Hashing.smearedHash(key));
            int[] iArr = this.hashTableKToV;
            if (iArr[keyBucket] == src) {
                iArr[keyBucket] = dest;
            } else {
                int prevInBucket = iArr[keyBucket];
                int entryInBucket = this.nextInBucketKToV[prevInBucket];
                while (entryInBucket != src) {
                    prevInBucket = entryInBucket;
                    entryInBucket = this.nextInBucketKToV[entryInBucket];
                }
                this.nextInBucketKToV[prevInBucket] = dest;
            }
            int[] iArr2 = this.nextInBucketKToV;
            iArr2[dest] = iArr2[src];
            iArr2[src] = -1;
            int valueBucket = bucket(Hashing.smearedHash(value));
            int[] iArr3 = this.hashTableVToK;
            if (iArr3[valueBucket] == src) {
                iArr3[valueBucket] = dest;
            } else {
                int prevInBucket2 = iArr3[valueBucket];
                int entryInBucket2 = this.nextInBucketVToK[prevInBucket2];
                while (entryInBucket2 != src) {
                    prevInBucket2 = entryInBucket2;
                    entryInBucket2 = this.nextInBucketVToK[entryInBucket2];
                }
                this.nextInBucketVToK[prevInBucket2] = dest;
            }
            int[] iArr4 = this.nextInBucketVToK;
            iArr4[dest] = iArr4[src];
            iArr4[src] = -1;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Arrays.fill(this.keys, 0, this.size, (Object) null);
        Arrays.fill(this.values, 0, this.size, (Object) null);
        Arrays.fill(this.hashTableKToV, -1);
        Arrays.fill(this.hashTableVToK, -1);
        Arrays.fill(this.nextInBucketKToV, 0, this.size, -1);
        Arrays.fill(this.nextInBucketVToK, 0, this.size, -1);
        Arrays.fill(this.prevInInsertionOrder, 0, this.size, -1);
        Arrays.fill(this.nextInInsertionOrder, 0, this.size, -1);
        this.size = 0;
        this.firstInInsertionOrder = -2;
        this.lastInInsertionOrder = -2;
        this.modCount++;
    }

    /* loaded from: classes.dex */
    public static abstract class View<K, V, T> extends AbstractSet<T> {
        final HashBiMap<K, V> biMap;

        abstract T forEntry(int i);

        View(HashBiMap<K, V> biMap) {
            this.biMap = biMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
        public Iterator<T> iterator() {
            return new Iterator<T>() { // from class: com.google.common.collect.HashBiMap.View.1
                private int expectedModCount;
                private int index;
                private int indexToRemove = -1;
                private int remaining;

                {
                    this.index = ((HashBiMap) View.this.biMap).firstInInsertionOrder;
                    this.expectedModCount = View.this.biMap.modCount;
                    this.remaining = View.this.biMap.size;
                }

                private void checkForComodification() {
                    if (View.this.biMap.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    checkForComodification();
                    return this.index != -2 && this.remaining > 0;
                }

                @Override // java.util.Iterator
                public T next() {
                    if (hasNext()) {
                        T result = (T) View.this.forEntry(this.index);
                        this.indexToRemove = this.index;
                        this.index = ((HashBiMap) View.this.biMap).nextInInsertionOrder[this.index];
                        this.remaining--;
                        return result;
                    }
                    throw new NoSuchElementException();
                }

                @Override // java.util.Iterator
                public void remove() {
                    checkForComodification();
                    CollectPreconditions.checkRemove(this.indexToRemove != -1);
                    View.this.biMap.removeEntry(this.indexToRemove);
                    if (this.index == View.this.biMap.size) {
                        this.index = this.indexToRemove;
                    }
                    this.indexToRemove = -1;
                    this.expectedModCount = View.this.biMap.modCount;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.biMap.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.biMap.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> result = this.keySet;
        if (result != null) {
            return result;
        }
        KeySet keySet = new KeySet();
        this.keySet = keySet;
        return keySet;
    }

    /* loaded from: classes.dex */
    public final class KeySet extends View<K, V, K> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        KeySet() {
            super(this$0);
            HashBiMap.this = this$0;
        }

        @Override // com.google.common.collect.HashBiMap.View
        K forEntry(int entry) {
            return HashBiMap.this.keys[entry];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object o) {
            return HashBiMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@NullableDecl Object o) {
            int oHash = Hashing.smearedHash(o);
            int entry = HashBiMap.this.findEntryByKey(o, oHash);
            if (entry == -1) {
                return false;
            }
            HashBiMap.this.removeEntryKeyHashKnown(entry, oHash);
            return true;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
    public Set<V> values() {
        Set<V> result = this.valueSet;
        if (result != null) {
            return result;
        }
        ValueSet valueSet = new ValueSet();
        this.valueSet = valueSet;
        return valueSet;
    }

    /* loaded from: classes.dex */
    public final class ValueSet extends View<K, V, V> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ValueSet() {
            super(this$0);
            HashBiMap.this = this$0;
        }

        @Override // com.google.common.collect.HashBiMap.View
        V forEntry(int entry) {
            return HashBiMap.this.values[entry];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object o) {
            return HashBiMap.this.containsValue(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@NullableDecl Object o) {
            int oHash = Hashing.smearedHash(o);
            int entry = HashBiMap.this.findEntryByValue(o, oHash);
            if (entry == -1) {
                return false;
            }
            HashBiMap.this.removeEntryValueHashKnown(entry, oHash);
            return true;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> result = this.entrySet;
        if (result != null) {
            return result;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    /* loaded from: classes.dex */
    final class EntrySet extends View<K, V, Map.Entry<K, V>> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        EntrySet() {
            super(this$0);
            HashBiMap.this = this$0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            Object k = e.getKey();
            Object v = e.getValue();
            int eIndex = HashBiMap.this.findEntryByKey(k);
            if (eIndex == -1 || !Objects.equal(v, HashBiMap.this.values[eIndex])) {
                return false;
            }
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@NullableDecl Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            Object k = e.getKey();
            Object v = e.getValue();
            int kHash = Hashing.smearedHash(k);
            int eIndex = HashBiMap.this.findEntryByKey(k, kHash);
            if (eIndex == -1 || !Objects.equal(v, HashBiMap.this.values[eIndex])) {
                return false;
            }
            HashBiMap.this.removeEntryKeyHashKnown(eIndex, kHash);
            return true;
        }

        @Override // com.google.common.collect.HashBiMap.View
        public Map.Entry<K, V> forEntry(int entry) {
            return new EntryForKey(entry);
        }
    }

    /* loaded from: classes.dex */
    public final class EntryForKey extends AbstractMapEntry<K, V> {
        int index;
        @NullableDecl
        final K key;

        EntryForKey(int index) {
            HashBiMap.this = this$0;
            this.key = this$0.keys[index];
            this.index = index;
        }

        void updateIndex() {
            int i = this.index;
            if (i == -1 || i > HashBiMap.this.size || !Objects.equal(HashBiMap.this.keys[this.index], this.key)) {
                this.index = HashBiMap.this.findEntryByKey(this.key);
            }
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        @NullableDecl
        public V getValue() {
            updateIndex();
            if (this.index == -1) {
                return null;
            }
            return HashBiMap.this.values[this.index];
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V setValue(V value) {
            updateIndex();
            if (this.index == -1) {
                return (V) HashBiMap.this.put(this.key, value);
            }
            V oldValue = HashBiMap.this.values[this.index];
            if (Objects.equal(oldValue, value)) {
                return value;
            }
            HashBiMap.this.replaceValueInEntry(this.index, value, false);
            return oldValue;
        }
    }

    @Override // com.google.common.collect.BiMap
    public BiMap<V, K> inverse() {
        BiMap<V, K> result = this.inverse;
        if (result != null) {
            return result;
        }
        Inverse inverse = new Inverse(this);
        this.inverse = inverse;
        return inverse;
    }

    /* loaded from: classes.dex */
    static class Inverse<K, V> extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {
        private final HashBiMap<K, V> forward;
        private transient Set<Map.Entry<V, K>> inverseEntrySet;

        Inverse(HashBiMap<K, V> forward) {
            this.forward = forward;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.forward.size;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@NullableDecl Object key) {
            return this.forward.containsValue(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @NullableDecl
        public K get(@NullableDecl Object key) {
            return this.forward.getInverse(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(@NullableDecl Object value) {
            return this.forward.containsKey(value);
        }

        @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
        @NullableDecl
        public K put(@NullableDecl V value, @NullableDecl K key) {
            return this.forward.putInverse(value, key, false);
        }

        @Override // com.google.common.collect.BiMap
        @NullableDecl
        public K forcePut(@NullableDecl V value, @NullableDecl K key) {
            return this.forward.putInverse(value, key, true);
        }

        @Override // com.google.common.collect.BiMap
        public BiMap<K, V> inverse() {
            return this.forward;
        }

        @Override // java.util.AbstractMap, java.util.Map
        @NullableDecl
        public K remove(@NullableDecl Object value) {
            return this.forward.removeInverse(value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.forward.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<V> keySet() {
            return this.forward.values();
        }

        @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
        public Set<K> values() {
            return this.forward.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<V, K>> entrySet() {
            Set<Map.Entry<V, K>> result = this.inverseEntrySet;
            if (result != null) {
                return result;
            }
            InverseEntrySet inverseEntrySet = new InverseEntrySet(this.forward);
            this.inverseEntrySet = inverseEntrySet;
            return inverseEntrySet;
        }

        private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
            in.defaultReadObject();
            ((HashBiMap) this.forward).inverse = this;
        }
    }

    /* loaded from: classes.dex */
    static class InverseEntrySet<K, V> extends View<K, V, Map.Entry<V, K>> {
        InverseEntrySet(HashBiMap<K, V> biMap) {
            super(biMap);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            Object v = e.getKey();
            Object k = e.getValue();
            int eIndex = this.biMap.findEntryByValue(v);
            if (eIndex == -1 || !Objects.equal(this.biMap.keys[eIndex], k)) {
                return false;
            }
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            Object v = e.getKey();
            Object k = e.getValue();
            int vHash = Hashing.smearedHash(v);
            int eIndex = this.biMap.findEntryByValue(v, vHash);
            if (eIndex == -1 || !Objects.equal(this.biMap.keys[eIndex], k)) {
                return false;
            }
            this.biMap.removeEntryValueHashKnown(eIndex, vHash);
            return true;
        }

        @Override // com.google.common.collect.HashBiMap.View
        public Map.Entry<V, K> forEntry(int entry) {
            return new EntryForValue(this.biMap, entry);
        }
    }

    /* loaded from: classes.dex */
    public static final class EntryForValue<K, V> extends AbstractMapEntry<V, K> {
        final HashBiMap<K, V> biMap;
        int index;
        final V value;

        EntryForValue(HashBiMap<K, V> biMap, int index) {
            this.biMap = biMap;
            this.value = biMap.values[index];
            this.index = index;
        }

        private void updateIndex() {
            int i = this.index;
            if (i == -1 || i > this.biMap.size || !Objects.equal(this.value, this.biMap.values[this.index])) {
                this.index = this.biMap.findEntryByValue(this.value);
            }
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V getKey() {
            return this.value;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K getValue() {
            updateIndex();
            if (this.index == -1) {
                return null;
            }
            return this.biMap.keys[this.index];
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K setValue(K key) {
            updateIndex();
            if (this.index == -1) {
                return this.biMap.putInverse(this.value, key, false);
            }
            K oldKey = this.biMap.keys[this.index];
            if (Objects.equal(oldKey, key)) {
                return key;
            }
            this.biMap.replaceKeyInEntry(this.index, key, false);
            return oldKey;
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMap(this, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int size = Serialization.readCount(stream);
        init(16);
        Serialization.populateMap(this, stream, size);
    }
}
