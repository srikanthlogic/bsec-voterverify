package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 0;
    transient ObjectCountHashMap<E> backingMap;
    transient long size;

    abstract void init(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractMapBasedMultiset(int distinctElements) {
        init(distinctElements);
    }

    @Override // com.google.common.collect.Multiset
    public final int count(@NullableDecl Object element) {
        return this.backingMap.get(element);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public final int add(@NullableDecl E element, int occurrences) {
        if (occurrences == 0) {
            return count(element);
        }
        boolean z = true;
        Preconditions.checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
        int entryIndex = this.backingMap.indexOf(element);
        if (entryIndex == -1) {
            this.backingMap.put(element, occurrences);
            this.size += (long) occurrences;
            return 0;
        }
        int oldCount = this.backingMap.getValue(entryIndex);
        long newCount = ((long) oldCount) + ((long) occurrences);
        if (newCount > 2147483647L) {
            z = false;
        }
        Preconditions.checkArgument(z, "too many occurrences: %s", newCount);
        this.backingMap.setValue(entryIndex, (int) newCount);
        this.size += (long) occurrences;
        return oldCount;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public final int remove(@NullableDecl Object element, int occurrences) {
        int numberRemoved;
        if (occurrences == 0) {
            return count(element);
        }
        Preconditions.checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
        int entryIndex = this.backingMap.indexOf(element);
        if (entryIndex == -1) {
            return 0;
        }
        int oldCount = this.backingMap.getValue(entryIndex);
        if (oldCount > occurrences) {
            numberRemoved = occurrences;
            this.backingMap.setValue(entryIndex, oldCount - occurrences);
        } else {
            numberRemoved = oldCount;
            this.backingMap.removeEntry(entryIndex);
        }
        this.size -= (long) numberRemoved;
        return oldCount;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public final int setCount(@NullableDecl E element, int count) {
        CollectPreconditions.checkNonnegative(count, "count");
        ObjectCountHashMap<E> objectCountHashMap = this.backingMap;
        int oldCount = count == 0 ? objectCountHashMap.remove(element) : objectCountHashMap.put(element, count);
        this.size += (long) (count - oldCount);
        return oldCount;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public final boolean setCount(@NullableDecl E element, int oldCount, int newCount) {
        CollectPreconditions.checkNonnegative(oldCount, "oldCount");
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        int entryIndex = this.backingMap.indexOf(element);
        if (entryIndex == -1) {
            if (oldCount != 0) {
                return false;
            }
            if (newCount > 0) {
                this.backingMap.put(element, newCount);
                this.size += (long) newCount;
            }
            return true;
        } else if (this.backingMap.getValue(entryIndex) != oldCount) {
            return false;
        } else {
            if (newCount == 0) {
                this.backingMap.removeEntry(entryIndex);
                this.size -= (long) oldCount;
            } else {
                this.backingMap.setValue(entryIndex, newCount);
                this.size += (long) (newCount - oldCount);
            }
            return true;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public final void clear() {
        this.backingMap.clear();
        this.size = 0;
    }

    /* loaded from: classes.dex */
    abstract class Itr<T> implements Iterator<T> {
        int entryIndex;
        int expectedModCount;
        int toRemove = -1;

        abstract T result(int i);

        Itr() {
            this.entryIndex = AbstractMapBasedMultiset.this.backingMap.firstIndex();
            this.expectedModCount = AbstractMapBasedMultiset.this.backingMap.modCount;
        }

        private void checkForConcurrentModification() {
            if (AbstractMapBasedMultiset.this.backingMap.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            checkForConcurrentModification();
            return this.entryIndex >= 0;
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext()) {
                T result = result(this.entryIndex);
                this.toRemove = this.entryIndex;
                this.entryIndex = AbstractMapBasedMultiset.this.backingMap.nextIndex(this.entryIndex);
                return result;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.toRemove != -1);
            AbstractMapBasedMultiset.this.size -= (long) AbstractMapBasedMultiset.this.backingMap.removeEntry(this.toRemove);
            this.entryIndex = AbstractMapBasedMultiset.this.backingMap.nextIndexAfterRemove(this.entryIndex, this.toRemove);
            this.toRemove = -1;
            this.expectedModCount = AbstractMapBasedMultiset.this.backingMap.modCount;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset
    final Iterator<E> elementIterator() {
        return new AbstractMapBasedMultiset<E>.Itr() { // from class: com.google.common.collect.AbstractMapBasedMultiset.1
            @Override // com.google.common.collect.AbstractMapBasedMultiset.Itr
            E result(int entryIndex) {
                return AbstractMapBasedMultiset.this.backingMap.getKey(entryIndex);
            }
        };
    }

    @Override // com.google.common.collect.AbstractMultiset
    final Iterator<Multiset.Entry<E>> entryIterator() {
        return new AbstractMapBasedMultiset<E>.Itr() { // from class: com.google.common.collect.AbstractMapBasedMultiset.2
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.AbstractMapBasedMultiset.Itr
            public Multiset.Entry<E> result(int entryIndex) {
                return AbstractMapBasedMultiset.this.backingMap.getEntry(entryIndex);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addTo(Multiset<? super E> target) {
        Preconditions.checkNotNull(target);
        int i = this.backingMap.firstIndex();
        while (i >= 0) {
            target.add((E) this.backingMap.getKey(i), this.backingMap.getValue(i));
            i = this.backingMap.nextIndex(i);
        }
    }

    @Override // com.google.common.collect.AbstractMultiset
    final int distinctElements() {
        return this.backingMap.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public final Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final int size() {
        return Ints.saturatedCast(this.size);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMultiset(this, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int distinctElements = Serialization.readCount(stream);
        init(3);
        Serialization.populateMultiset(this, stream, distinctElements);
    }
}
