package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public class CompactHashSet<E> extends AbstractSet<E> implements Serializable {
    static final double HASH_FLOODING_FPP;
    private static final int MAX_HASH_BUCKET_LENGTH;
    @NullableDecl
    transient Object[] elements;
    @NullableDecl
    private transient int[] entries;
    private transient int metadata;
    private transient int size;
    @NullableDecl
    private transient Object table;

    public static <E> CompactHashSet<E> create() {
        return new CompactHashSet<>();
    }

    public static <E> CompactHashSet<E> create(Collection<? extends E> collection) {
        CompactHashSet<E> set = createWithExpectedSize(collection.size());
        set.addAll(collection);
        return set;
    }

    @SafeVarargs
    public static <E> CompactHashSet<E> create(E... elements) {
        CompactHashSet<E> set = createWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> CompactHashSet<E> createWithExpectedSize(int expectedSize) {
        return new CompactHashSet<>(expectedSize);
    }

    public CompactHashSet() {
        init(3);
    }

    public CompactHashSet(int expectedSize) {
        init(expectedSize);
    }

    public void init(int expectedSize) {
        Preconditions.checkArgument(expectedSize >= 0, "Expected size must be >= 0");
        this.metadata = Ints.constrainToRange(expectedSize, 1, 1073741823);
    }

    boolean needsAllocArrays() {
        return this.table == null;
    }

    public int allocArrays() {
        Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
        int expectedSize = this.metadata;
        int buckets = CompactHashing.tableSize(expectedSize);
        this.table = CompactHashing.createTable(buckets);
        setHashTableMask(buckets - 1);
        this.entries = new int[expectedSize];
        this.elements = new Object[expectedSize];
        return expectedSize;
    }

    @NullableDecl
    Set<E> delegateOrNull() {
        Object obj = this.table;
        if (obj instanceof Set) {
            return (Set) obj;
        }
        return null;
    }

    private Set<E> createHashFloodingResistantDelegate(int tableSize) {
        return new LinkedHashSet(tableSize, 1.0f);
    }

    public Set<E> convertToHashFloodingResistantImplementation() {
        Set<E> newDelegate = createHashFloodingResistantDelegate(hashTableMask() + 1);
        int i = firstEntryIndex();
        while (i >= 0) {
            newDelegate.add((E) this.elements[i]);
            i = getSuccessor(i);
        }
        this.table = newDelegate;
        this.entries = null;
        this.elements = null;
        incrementModCount();
        return newDelegate;
    }

    boolean isUsingHashFloodingResistance() {
        return delegateOrNull() != null;
    }

    private void setHashTableMask(int mask) {
        this.metadata = CompactHashing.maskCombine(this.metadata, 32 - Integer.numberOfLeadingZeros(mask), 31);
    }

    private int hashTableMask() {
        return (1 << (this.metadata & 31)) - 1;
    }

    void incrementModCount() {
        this.metadata += 32;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(@NullableDecl E object) {
        int entryIndex;
        int entry;
        if (needsAllocArrays()) {
            allocArrays();
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.add(object);
        }
        int[] entries = this.entries;
        Object[] elements = this.elements;
        int newEntryIndex = this.size;
        int newSize = newEntryIndex + 1;
        int hash = Hashing.smearedHash(object);
        int mask = hashTableMask();
        int tableIndex = hash & mask;
        int next = CompactHashing.tableGet(this.table, tableIndex);
        if (next != 0) {
            int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
            int bucketLength = 0;
            do {
                entryIndex = next - 1;
                entry = entries[entryIndex];
                if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(object, elements[entryIndex])) {
                    return false;
                }
                next = CompactHashing.getNext(entry, mask);
                bucketLength++;
            } while (next != 0);
            if (bucketLength >= 9) {
                return convertToHashFloodingResistantImplementation().add(object);
            }
            if (newSize > mask) {
                mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
            } else {
                entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
            }
        } else if (newSize > mask) {
            mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
        } else {
            CompactHashing.tableSet(this.table, tableIndex, newEntryIndex + 1);
        }
        resizeMeMaybe(newSize);
        insertEntry(newEntryIndex, object, hash, mask);
        this.size = newSize;
        incrementModCount();
        return true;
    }

    public void insertEntry(int entryIndex, @NullableDecl E object, int hash, int mask) {
        this.entries[entryIndex] = CompactHashing.maskCombine(hash, 0, mask);
        this.elements[entryIndex] = object;
    }

    private void resizeMeMaybe(int newSize) {
        int newCapacity;
        int entriesSize = this.entries.length;
        if (newSize > entriesSize && (newCapacity = Math.min(1073741823, (Math.max(1, entriesSize >>> 1) + entriesSize) | 1)) != entriesSize) {
            resizeEntries(newCapacity);
        }
    }

    public void resizeEntries(int newCapacity) {
        this.entries = Arrays.copyOf(this.entries, newCapacity);
        this.elements = Arrays.copyOf(this.elements, newCapacity);
    }

    private int resizeTable(int mask, int newCapacity, int targetHash, int targetEntryIndex) {
        Object newTable = CompactHashing.createTable(newCapacity);
        int newMask = newCapacity - 1;
        if (targetEntryIndex != 0) {
            CompactHashing.tableSet(newTable, targetHash & newMask, targetEntryIndex + 1);
        }
        Object table = this.table;
        int[] entries = this.entries;
        for (int tableIndex = 0; tableIndex <= mask; tableIndex++) {
            int next = CompactHashing.tableGet(table, tableIndex);
            while (next != 0) {
                int entryIndex = next - 1;
                int entry = entries[entryIndex];
                int hash = CompactHashing.getHashPrefix(entry, mask) | tableIndex;
                int newTableIndex = hash & newMask;
                int newNext = CompactHashing.tableGet(newTable, newTableIndex);
                CompactHashing.tableSet(newTable, newTableIndex, next);
                entries[entryIndex] = CompactHashing.maskCombine(hash, newNext, newMask);
                next = CompactHashing.getNext(entry, mask);
            }
        }
        this.table = newTable;
        setHashTableMask(newMask);
        return newMask;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@NullableDecl Object object) {
        if (needsAllocArrays()) {
            return false;
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.contains(object);
        }
        int hash = Hashing.smearedHash(object);
        int mask = hashTableMask();
        int next = CompactHashing.tableGet(this.table, hash & mask);
        if (next == 0) {
            return false;
        }
        int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
        do {
            int entryIndex = next - 1;
            int entry = this.entries[entryIndex];
            if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(object, this.elements[entryIndex])) {
                return true;
            }
            next = CompactHashing.getNext(entry, mask);
        } while (next != 0);
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(@NullableDecl Object object) {
        if (needsAllocArrays()) {
            return false;
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.remove(object);
        }
        int mask = hashTableMask();
        int index = CompactHashing.remove(object, null, mask, this.table, this.entries, this.elements, null);
        if (index == -1) {
            return false;
        }
        moveLastEntry(index, mask);
        this.size--;
        incrementModCount();
        return true;
    }

    public void moveLastEntry(int dstIndex, int mask) {
        int entryIndex;
        int entry;
        int srcIndex = size() - 1;
        if (dstIndex < srcIndex) {
            Object[] objArr = this.elements;
            Object object = objArr[srcIndex];
            objArr[dstIndex] = object;
            objArr[srcIndex] = null;
            int[] iArr = this.entries;
            iArr[dstIndex] = iArr[srcIndex];
            iArr[srcIndex] = 0;
            int tableIndex = Hashing.smearedHash(object) & mask;
            int next = CompactHashing.tableGet(this.table, tableIndex);
            int srcNext = srcIndex + 1;
            if (next == srcNext) {
                CompactHashing.tableSet(this.table, tableIndex, dstIndex + 1);
                return;
            }
            do {
                entryIndex = next - 1;
                entry = this.entries[entryIndex];
                next = CompactHashing.getNext(entry, mask);
            } while (next != srcNext);
            this.entries[entryIndex] = CompactHashing.maskCombine(entry, dstIndex + 1, mask);
            return;
        }
        this.elements[dstIndex] = null;
        this.entries[dstIndex] = 0;
    }

    int firstEntryIndex() {
        return isEmpty() ? -1 : 0;
    }

    int getSuccessor(int entryIndex) {
        if (entryIndex + 1 < this.size) {
            return entryIndex + 1;
        }
        return -1;
    }

    int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
        return indexBeforeRemove - 1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
    public Iterator<E> iterator() {
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return delegate.iterator();
        }
        return new Iterator<E>() { // from class: com.google.common.collect.CompactHashSet.1
            int currentIndex;
            int expectedMetadata;
            int indexToRemove = -1;

            {
                this.expectedMetadata = CompactHashSet.this.metadata;
                this.currentIndex = CompactHashSet.this.firstEntryIndex();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.currentIndex >= 0;
            }

            @Override // java.util.Iterator
            public E next() {
                checkForConcurrentModification();
                if (hasNext()) {
                    this.indexToRemove = this.currentIndex;
                    Object[] objArr = CompactHashSet.this.elements;
                    int i = this.currentIndex;
                    E result = (E) objArr[i];
                    this.currentIndex = CompactHashSet.this.getSuccessor(i);
                    return result;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                checkForConcurrentModification();
                CollectPreconditions.checkRemove(this.indexToRemove >= 0);
                incrementExpectedModCount();
                CompactHashSet compactHashSet = CompactHashSet.this;
                compactHashSet.remove(compactHashSet.elements[this.indexToRemove]);
                this.currentIndex = CompactHashSet.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
                this.indexToRemove = -1;
            }

            void incrementExpectedModCount() {
                this.expectedMetadata += 32;
            }

            private void checkForConcurrentModification() {
                if (CompactHashSet.this.metadata != this.expectedMetadata) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        Set<E> delegate = delegateOrNull();
        return delegate != null ? delegate.size() : this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        if (needsAllocArrays()) {
            return new Object[0];
        }
        Set<E> delegate = delegateOrNull();
        return delegate != null ? delegate.toArray() : Arrays.copyOf(this.elements, this.size);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] a2) {
        if (needsAllocArrays()) {
            if (a2.length > 0) {
                a2[0] = null;
            }
            return a2;
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
            return (T[]) delegate.toArray(a2);
        }
        return (T[]) ObjectArrays.toArrayImpl(this.elements, 0, this.size, a2);
    }

    /* JADX INFO: Multiple debug info for r1v0 int: [D('size' int), D('newDelegate' java.util.Set<E>)] */
    public void trimToSize() {
        if (!needsAllocArrays()) {
            Set<E> delegate = delegateOrNull();
            if (delegate != null) {
                Set<E> newDelegate = createHashFloodingResistantDelegate(size());
                newDelegate.addAll(delegate);
                this.table = newDelegate;
                return;
            }
            int size = this.size;
            if (size < this.entries.length) {
                resizeEntries(size);
            }
            int minimumTableSize = CompactHashing.tableSize(size);
            int mask = hashTableMask();
            if (minimumTableSize < mask) {
                resizeTable(mask, minimumTableSize, 0, 0);
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (!needsAllocArrays()) {
            incrementModCount();
            Set<E> delegate = delegateOrNull();
            if (delegate != null) {
                this.metadata = Ints.constrainToRange(size(), 3, 1073741823);
                delegate.clear();
                this.table = null;
                this.size = 0;
                return;
            }
            Arrays.fill(this.elements, 0, this.size, (Object) null);
            CompactHashing.tableClear(this.table);
            Arrays.fill(this.entries, 0, this.size, 0);
            this.size = 0;
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(size());
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            stream.writeObject(it.next());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int elementCount = stream.readInt();
        if (elementCount >= 0) {
            init(elementCount);
            for (int i = 0; i < elementCount; i++) {
                add(stream.readObject());
            }
            return;
        }
        throw new InvalidObjectException("Invalid size: " + elementCount);
    }
}
