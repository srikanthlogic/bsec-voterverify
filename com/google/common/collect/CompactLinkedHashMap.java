package com.google.common.collect;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
class CompactLinkedHashMap<K, V> extends CompactHashMap<K, V> {
    private static final int ENDPOINT = -2;
    private final boolean accessOrder;
    private transient int firstEntry;
    private transient int lastEntry;
    @NullableDecl
    transient long[] links;

    public static <K, V> CompactLinkedHashMap<K, V> create() {
        return new CompactLinkedHashMap<>();
    }

    public static <K, V> CompactLinkedHashMap<K, V> createWithExpectedSize(int expectedSize) {
        return new CompactLinkedHashMap<>(expectedSize);
    }

    CompactLinkedHashMap() {
        this(3);
    }

    CompactLinkedHashMap(int expectedSize) {
        this(expectedSize, false);
    }

    CompactLinkedHashMap(int expectedSize, boolean accessOrder) {
        super(expectedSize);
        this.accessOrder = accessOrder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.CompactHashMap
    public void init(int expectedSize) {
        super.init(expectedSize);
        this.firstEntry = -2;
        this.lastEntry = -2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.CompactHashMap
    public int allocArrays() {
        int expectedSize = super.allocArrays();
        this.links = new long[expectedSize];
        return expectedSize;
    }

    @Override // com.google.common.collect.CompactHashMap
    Map<K, V> createHashFloodingResistantDelegate(int tableSize) {
        return new LinkedHashMap(tableSize, 1.0f, this.accessOrder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.CompactHashMap
    public Map<K, V> convertToHashFloodingResistantImplementation() {
        Map<K, V> result = super.convertToHashFloodingResistantImplementation();
        this.links = null;
        return result;
    }

    private int getPredecessor(int entry) {
        return ((int) (this.links[entry] >>> 32)) - 1;
    }

    @Override // com.google.common.collect.CompactHashMap
    int getSuccessor(int entry) {
        return ((int) this.links[entry]) - 1;
    }

    private void setSuccessor(int entry, int succ) {
        long[] jArr = this.links;
        jArr[entry] = (jArr[entry] & (~4294967295L)) | (((long) (succ + 1)) & 4294967295L);
    }

    private void setPredecessor(int entry, int pred) {
        long[] jArr = this.links;
        jArr[entry] = (jArr[entry] & (~-4294967296L)) | (((long) (pred + 1)) << 32);
    }

    private void setSucceeds(int pred, int succ) {
        if (pred == -2) {
            this.firstEntry = succ;
        } else {
            setSuccessor(pred, succ);
        }
        if (succ == -2) {
            this.lastEntry = pred;
        } else {
            setPredecessor(succ, pred);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.CompactHashMap
    public void insertEntry(int entryIndex, @NullableDecl K key, @NullableDecl V value, int hash, int mask) {
        super.insertEntry(entryIndex, key, value, hash, mask);
        setSucceeds(this.lastEntry, entryIndex);
        setSucceeds(entryIndex, -2);
    }

    @Override // com.google.common.collect.CompactHashMap
    void accessEntry(int index) {
        if (this.accessOrder) {
            setSucceeds(getPredecessor(index), getSuccessor(index));
            setSucceeds(this.lastEntry, index);
            setSucceeds(index, -2);
            incrementModCount();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.CompactHashMap
    public void moveLastEntry(int dstIndex, int mask) {
        int srcIndex = size() - 1;
        super.moveLastEntry(dstIndex, mask);
        setSucceeds(getPredecessor(dstIndex), getSuccessor(dstIndex));
        if (dstIndex < srcIndex) {
            setSucceeds(getPredecessor(srcIndex), dstIndex);
            setSucceeds(dstIndex, getSuccessor(srcIndex));
        }
        this.links[srcIndex] = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.CompactHashMap
    public void resizeEntries(int newCapacity) {
        super.resizeEntries(newCapacity);
        this.links = Arrays.copyOf(this.links, newCapacity);
    }

    @Override // com.google.common.collect.CompactHashMap
    int firstEntryIndex() {
        return this.firstEntry;
    }

    @Override // com.google.common.collect.CompactHashMap
    int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
        return indexBeforeRemove >= size() ? indexRemoved : indexBeforeRemove;
    }

    @Override // com.google.common.collect.CompactHashMap, java.util.AbstractMap, java.util.Map
    public void clear() {
        if (!needsAllocArrays()) {
            this.firstEntry = -2;
            this.lastEntry = -2;
            long[] jArr = this.links;
            if (jArr != null) {
                Arrays.fill(jArr, 0, size(), 0L);
            }
            super.clear();
        }
    }
}
