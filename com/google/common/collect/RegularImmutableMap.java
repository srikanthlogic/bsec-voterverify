package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import kotlin.UShort;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final byte ABSENT = -1;
    private static final int BYTE_MASK = 255;
    private static final int BYTE_MAX_SIZE = 128;
    static final ImmutableMap<Object, Object> EMPTY = new RegularImmutableMap(null, new Object[0], 0);
    private static final int SHORT_MASK = 65535;
    private static final int SHORT_MAX_SIZE = 32768;
    private static final long serialVersionUID = 0;
    final transient Object[] alternatingKeysAndValues;
    private final transient Object hashTable;
    private final transient int size;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> RegularImmutableMap<K, V> create(int n, Object[] alternatingKeysAndValues) {
        if (n == 0) {
            return (RegularImmutableMap) EMPTY;
        }
        if (n == 1) {
            CollectPreconditions.checkEntryNotNull(alternatingKeysAndValues[0], alternatingKeysAndValues[1]);
            return new RegularImmutableMap<>(null, alternatingKeysAndValues, 1);
        }
        Preconditions.checkPositionIndex(n, alternatingKeysAndValues.length >> 1);
        return new RegularImmutableMap<>(createHashTable(alternatingKeysAndValues, n, ImmutableSet.chooseTableSize(n), 0), alternatingKeysAndValues, n);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Multiple debug info for r1v2 int[]: [D('hashTable' int[]), D('hashTable' short[])] */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0039, code lost:
        r1[r6] = (byte) r3;
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x007b, code lost:
        r1[r6] = (short) r3;
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00b4, code lost:
        r1[r7] = r4;
        r3 = r3 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static Object createHashTable(Object[] alternatingKeysAndValues, int n, int tableSize, int keyOffset) {
        if (n == 1) {
            CollectPreconditions.checkEntryNotNull(alternatingKeysAndValues[keyOffset], alternatingKeysAndValues[keyOffset ^ 1]);
            return null;
        }
        int mask = tableSize - 1;
        if (tableSize <= 128) {
            byte[] hashTable = new byte[tableSize];
            Arrays.fill(hashTable, (byte) -1);
            int i = 0;
            while (i < n) {
                int keyIndex = (i * 2) + keyOffset;
                Object key = alternatingKeysAndValues[keyIndex];
                Object value = alternatingKeysAndValues[keyIndex ^ 1];
                CollectPreconditions.checkEntryNotNull(key, value);
                int h = Hashing.smear(key.hashCode());
                while (true) {
                    int h2 = h & mask;
                    int previousKeyIndex = hashTable[h2] & 255;
                    if (previousKeyIndex == 255) {
                        break;
                    } else if (!alternatingKeysAndValues[previousKeyIndex].equals(key)) {
                        h = h2 + 1;
                    } else {
                        throw duplicateKeyException(key, value, alternatingKeysAndValues, previousKeyIndex);
                    }
                }
            }
            return hashTable;
        } else if (tableSize <= 32768) {
            short[] hashTable2 = new short[tableSize];
            Arrays.fill(hashTable2, (short) -1);
            int i2 = 0;
            while (i2 < n) {
                int keyIndex2 = (i2 * 2) + keyOffset;
                Object key2 = alternatingKeysAndValues[keyIndex2];
                Object value2 = alternatingKeysAndValues[keyIndex2 ^ 1];
                CollectPreconditions.checkEntryNotNull(key2, value2);
                int h3 = Hashing.smear(key2.hashCode());
                while (true) {
                    int h4 = h3 & mask;
                    int previousKeyIndex2 = hashTable2[h4] & UShort.MAX_VALUE;
                    if (previousKeyIndex2 == 65535) {
                        break;
                    } else if (!alternatingKeysAndValues[previousKeyIndex2].equals(key2)) {
                        h3 = h4 + 1;
                    } else {
                        throw duplicateKeyException(key2, value2, alternatingKeysAndValues, previousKeyIndex2);
                    }
                }
            }
            return hashTable2;
        } else {
            int[] hashTable3 = new int[tableSize];
            Arrays.fill(hashTable3, -1);
            int i3 = 0;
            while (i3 < n) {
                int keyIndex3 = (i3 * 2) + keyOffset;
                Object key3 = alternatingKeysAndValues[keyIndex3];
                Object value3 = alternatingKeysAndValues[keyIndex3 ^ 1];
                CollectPreconditions.checkEntryNotNull(key3, value3);
                int h5 = Hashing.smear(key3.hashCode());
                while (true) {
                    int h6 = h5 & mask;
                    int previousKeyIndex3 = hashTable3[h6];
                    if (previousKeyIndex3 == -1) {
                        break;
                    } else if (!alternatingKeysAndValues[previousKeyIndex3].equals(key3)) {
                        h5 = h6 + 1;
                    } else {
                        throw duplicateKeyException(key3, value3, alternatingKeysAndValues, previousKeyIndex3);
                    }
                }
            }
            return hashTable3;
        }
    }

    private static IllegalArgumentException duplicateKeyException(Object key, Object value, Object[] alternatingKeysAndValues, int previousKeyIndex) {
        return new IllegalArgumentException("Multiple entries with same key: " + key + "=" + value + " and " + alternatingKeysAndValues[previousKeyIndex] + "=" + alternatingKeysAndValues[previousKeyIndex ^ 1]);
    }

    private RegularImmutableMap(Object hashTable, Object[] alternatingKeysAndValues, int size) {
        this.hashTable = hashTable;
        this.alternatingKeysAndValues = alternatingKeysAndValues;
        this.size = size;
    }

    @Override // java.util.Map
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    @NullableDecl
    public V get(@NullableDecl Object key) {
        return (V) get(this.hashTable, this.alternatingKeysAndValues, this.size, 0, key);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object get(@NullableDecl Object hashTableObject, @NullableDecl Object[] alternatingKeysAndValues, int size, int keyOffset, @NullableDecl Object key) {
        if (key == null) {
            return null;
        }
        if (size == 1) {
            if (alternatingKeysAndValues[keyOffset].equals(key)) {
                return alternatingKeysAndValues[keyOffset ^ 1];
            }
            return null;
        } else if (hashTableObject == null) {
            return null;
        } else {
            if (hashTableObject instanceof byte[]) {
                byte[] hashTable = (byte[]) hashTableObject;
                int mask = hashTable.length - 1;
                int h = Hashing.smear(key.hashCode());
                while (true) {
                    int h2 = h & mask;
                    int keyIndex = hashTable[h2] & 255;
                    if (keyIndex == 255) {
                        return null;
                    }
                    if (alternatingKeysAndValues[keyIndex].equals(key)) {
                        return alternatingKeysAndValues[keyIndex ^ 1];
                    }
                    h = h2 + 1;
                }
            } else if (hashTableObject instanceof short[]) {
                short[] hashTable2 = (short[]) hashTableObject;
                int mask2 = hashTable2.length - 1;
                int h3 = Hashing.smear(key.hashCode());
                while (true) {
                    int h4 = h3 & mask2;
                    int keyIndex2 = hashTable2[h4] & UShort.MAX_VALUE;
                    if (keyIndex2 == 65535) {
                        return null;
                    }
                    if (alternatingKeysAndValues[keyIndex2].equals(key)) {
                        return alternatingKeysAndValues[keyIndex2 ^ 1];
                    }
                    h3 = h4 + 1;
                }
            } else {
                int[] hashTable3 = (int[]) hashTableObject;
                int mask3 = hashTable3.length - 1;
                int h5 = Hashing.smear(key.hashCode());
                while (true) {
                    int h6 = h5 & mask3;
                    int keyIndex3 = hashTable3[h6];
                    if (keyIndex3 == -1) {
                        return null;
                    }
                    if (alternatingKeysAndValues[keyIndex3].equals(key)) {
                        return alternatingKeysAndValues[keyIndex3 ^ 1];
                    }
                    h5 = h6 + 1;
                }
            }
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new EntrySet(this, this.alternatingKeysAndValues, 0, this.size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
        private final transient Object[] alternatingKeysAndValues;
        private final transient int keyOffset;
        private final transient ImmutableMap<K, V> map;
        private final transient int size;

        /* JADX INFO: Access modifiers changed from: package-private */
        public EntrySet(ImmutableMap<K, V> map, Object[] alternatingKeysAndValues, int keyOffset, int size) {
            this.map = map;
            this.alternatingKeysAndValues = alternatingKeysAndValues;
            this.keyOffset = keyOffset;
            this.size = size;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return asList().iterator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int copyIntoArray(Object[] dst, int offset) {
            return asList().copyIntoArray(dst, offset);
        }

        @Override // com.google.common.collect.ImmutableSet
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new ImmutableList<Map.Entry<K, V>>() { // from class: com.google.common.collect.RegularImmutableMap.EntrySet.1
                @Override // java.util.List
                public Map.Entry<K, V> get(int index) {
                    Preconditions.checkElementIndex(index, EntrySet.this.size);
                    return new AbstractMap.SimpleImmutableEntry(EntrySet.this.alternatingKeysAndValues[(index * 2) + EntrySet.this.keyOffset], EntrySet.this.alternatingKeysAndValues[(index * 2) + (EntrySet.this.keyOffset ^ 1)]);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return EntrySet.this.size;
                }

                @Override // com.google.common.collect.ImmutableCollection
                public boolean isPartialView() {
                    return true;
                }
            };
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) object;
            Object k = entry.getKey();
            Object v = entry.getValue();
            if (v == null || !v.equals(this.map.get(k))) {
                return false;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.size;
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> createKeySet() {
        return new KeySet(this, new KeysOrValuesAsList(this.alternatingKeysAndValues, 0, this.size));
    }

    /* loaded from: classes.dex */
    static final class KeysOrValuesAsList extends ImmutableList<Object> {
        private final transient Object[] alternatingKeysAndValues;
        private final transient int offset;
        private final transient int size;

        /* JADX INFO: Access modifiers changed from: package-private */
        public KeysOrValuesAsList(Object[] alternatingKeysAndValues, int offset, int size) {
            this.alternatingKeysAndValues = alternatingKeysAndValues;
            this.offset = offset;
            this.size = size;
        }

        @Override // java.util.List
        public Object get(int index) {
            Preconditions.checkElementIndex(index, this.size);
            return this.alternatingKeysAndValues[(index * 2) + this.offset];
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.size;
        }
    }

    /* loaded from: classes.dex */
    static final class KeySet<K> extends ImmutableSet<K> {
        private final transient ImmutableList<K> list;
        private final transient ImmutableMap<K, ?> map;

        /* JADX INFO: Access modifiers changed from: package-private */
        public KeySet(ImmutableMap<K, ?> map, ImmutableList<K> list) {
            this.map = map;
            this.list = list;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<K> iterator() {
            return asList().iterator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int copyIntoArray(Object[] dst, int offset) {
            return asList().copyIntoArray(dst, offset);
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        public ImmutableList<K> asList() {
            return this.list;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object object) {
            return this.map.get(object) != null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection<V> createValues() {
        return new KeysOrValuesAsList(this.alternatingKeysAndValues, 1, this.size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public boolean isPartialView() {
        return false;
    }
}
