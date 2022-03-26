package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Serialization;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 1;
    private final transient ConcurrentMap<E, AtomicInteger> countMap;

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ boolean contains(@NullableDecl Object obj) {
        return super.contains(obj);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return super.elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    /* loaded from: classes.dex */
    private static class FieldSettersHolder {
        static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");

        private FieldSettersHolder() {
        }
    }

    public static <E> ConcurrentHashMultiset<E> create() {
        return new ConcurrentHashMultiset<>(new ConcurrentHashMap());
    }

    public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
        ConcurrentHashMultiset<E> multiset = create();
        Iterables.addAll(multiset, elements);
        return multiset;
    }

    public static <E> ConcurrentHashMultiset<E> create(ConcurrentMap<E, AtomicInteger> countMap) {
        return new ConcurrentHashMultiset<>(countMap);
    }

    ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
        Preconditions.checkArgument(countMap.isEmpty(), "the backing map (%s) must be empty", countMap);
        this.countMap = countMap;
    }

    @Override // com.google.common.collect.Multiset
    public int count(@NullableDecl Object element) {
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return 0;
        }
        return existingCounter.get();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        long sum = 0;
        for (AtomicInteger value : this.countMap.values()) {
            sum += (long) value.get();
        }
        return Ints.saturatedCast(sum);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return snapshot().toArray();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] array) {
        return (T[]) snapshot().toArray(array);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private List<E> snapshot() {
        ArrayList newArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(size());
        for (Multiset.Entry<E> entry : entrySet()) {
            E element = entry.getElement();
            for (int i = entry.getCount(); i > 0; i--) {
                newArrayListWithExpectedSize.add(element);
            }
        }
        return newArrayListWithExpectedSize;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x005e, code lost:
        r3 = new java.util.concurrent.atomic.AtomicInteger(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0069, code lost:
        if (r6.countMap.putIfAbsent(r7, r3) == null) goto L_0x0075;
     */
    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    /* Code decompiled incorrectly, please refer to instructions dump */
    public int add(E element, int occurrences) {
        AtomicInteger existingCounter;
        AtomicInteger newCounter;
        Preconditions.checkNotNull(element);
        if (occurrences == 0) {
            return count(element);
        }
        CollectPreconditions.checkPositive(occurrences, "occurences");
        do {
            existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
            if (existingCounter != null || (existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(occurrences))) != null) {
                while (true) {
                    int oldValue = existingCounter.get();
                    if (oldValue == 0) {
                        break;
                    }
                    try {
                        if (existingCounter.compareAndSet(oldValue, IntMath.checkedAdd(oldValue, occurrences))) {
                            return oldValue;
                        }
                    } catch (ArithmeticException e) {
                        throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
                    }
                }
            } else {
                return 0;
            }
        } while (!this.countMap.replace(element, existingCounter, newCounter));
        return 0;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int remove(@NullableDecl Object element, int occurrences) {
        int oldValue;
        int newValue;
        if (occurrences == 0) {
            return count(element);
        }
        CollectPreconditions.checkPositive(occurrences, "occurences");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return 0;
        }
        do {
            oldValue = existingCounter.get();
            if (oldValue == 0) {
                return 0;
            }
            newValue = Math.max(0, oldValue - occurrences);
        } while (!existingCounter.compareAndSet(oldValue, newValue));
        if (newValue == 0) {
            this.countMap.remove(element, existingCounter);
        }
        return oldValue;
    }

    public boolean removeExactly(@NullableDecl Object element, int occurrences) {
        int oldValue;
        int newValue;
        if (occurrences == 0) {
            return true;
        }
        CollectPreconditions.checkPositive(occurrences, "occurences");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter == null) {
            return false;
        }
        do {
            oldValue = existingCounter.get();
            if (oldValue < occurrences) {
                return false;
            }
            newValue = oldValue - occurrences;
        } while (!existingCounter.compareAndSet(oldValue, newValue));
        if (newValue == 0) {
            this.countMap.remove(element, existingCounter);
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x002d, code lost:
        if (r7 != 0) goto L_0x0030;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002f, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0030, code lost:
        r3 = new java.util.concurrent.atomic.AtomicInteger(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x003b, code lost:
        if (r5.countMap.putIfAbsent(r6, r3) == null) goto L_0x0048;
     */
    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    /* Code decompiled incorrectly, please refer to instructions dump */
    public int setCount(E element, int count) {
        AtomicInteger existingCounter;
        AtomicInteger newCounter;
        Preconditions.checkNotNull(element);
        CollectPreconditions.checkNonnegative(count, "count");
        do {
            existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
            if (existingCounter == null && (count == 0 || (existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(count))) == null)) {
                return 0;
            }
            while (true) {
                int oldValue = existingCounter.get();
                if (oldValue == 0) {
                    break;
                } else if (existingCounter.compareAndSet(oldValue, count)) {
                    if (count == 0) {
                        this.countMap.remove(element, existingCounter);
                    }
                    return oldValue;
                }
            }
        } while (!this.countMap.replace(element, existingCounter, newCounter));
        return 0;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public boolean setCount(E element, int expectedOldCount, int newCount) {
        Preconditions.checkNotNull(element);
        CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        AtomicInteger existingCounter = (AtomicInteger) Maps.safeGet(this.countMap, element);
        if (existingCounter != null) {
            int oldValue = existingCounter.get();
            if (oldValue == expectedOldCount) {
                if (oldValue == 0) {
                    if (newCount == 0) {
                        this.countMap.remove(element, existingCounter);
                        return true;
                    }
                    AtomicInteger newCounter = new AtomicInteger(newCount);
                    return this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter);
                } else if (existingCounter.compareAndSet(oldValue, newCount)) {
                    if (newCount == 0) {
                        this.countMap.remove(element, existingCounter);
                    }
                    return true;
                }
            }
            return false;
        } else if (expectedOldCount != 0) {
            return false;
        } else {
            return newCount == 0 || this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset
    Set<E> createElementSet() {
        final Set<E> delegate = this.countMap.keySet();
        return new ForwardingSet<E>() { // from class: com.google.common.collect.ConcurrentHashMultiset.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
            public Set<E> delegate() {
                return delegate;
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public boolean contains(@NullableDecl Object object) {
                return object != null && Collections2.safeContains(delegate, object);
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public boolean containsAll(Collection<?> collection) {
                return standardContainsAll(collection);
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public boolean remove(Object object) {
                return object != null && Collections2.safeRemove(delegate, object);
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
            public boolean removeAll(Collection<?> c) {
                return standardRemoveAll(c);
            }
        };
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<E> elementIterator() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.AbstractMultiset
    @Deprecated
    public Set<Multiset.Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    @Override // com.google.common.collect.AbstractMultiset
    int distinctElements() {
        return this.countMap.size();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.countMap.isEmpty();
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.ConcurrentHashMultiset.2
            private final Iterator<Map.Entry<E, AtomicInteger>> mapEntries;

            {
                this.mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.AbstractIterator
            public Multiset.Entry<E> computeNext() {
                while (this.mapEntries.hasNext()) {
                    Map.Entry<E, AtomicInteger> mapEntry = this.mapEntries.next();
                    int count = mapEntry.getValue().get();
                    if (count != 0) {
                        return Multisets.immutableEntry(mapEntry.getKey(), count);
                    }
                }
                return endOfData();
            }
        };
        return new ForwardingIterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.ConcurrentHashMultiset.3
            @NullableDecl
            private Multiset.Entry<E> last;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingIterator, com.google.common.collect.ForwardingObject
            public Iterator<Multiset.Entry<E>> delegate() {
                return readOnlyIterator;
            }

            @Override // com.google.common.collect.ForwardingIterator, java.util.Iterator
            public Multiset.Entry<E> next() {
                this.last = (Multiset.Entry) super.next();
                return this.last;
            }

            @Override // com.google.common.collect.ForwardingIterator, java.util.Iterator
            public void remove() {
                CollectPreconditions.checkRemove(this.last != null);
                ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
                this.last = null;
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.countMap.clear();
    }

    /* loaded from: classes.dex */
    private class EntrySet extends AbstractMultiset<E>.EntrySet {
        private EntrySet() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.AbstractMultiset.EntrySet, com.google.common.collect.Multisets.EntrySet
        public ConcurrentHashMultiset<E> multiset() {
            return ConcurrentHashMultiset.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return snapshot().toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] array) {
            return (T[]) snapshot().toArray(array);
        }

        private List<Multiset.Entry<E>> snapshot() {
            List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
            Iterators.addAll(list, iterator());
            return list;
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.countMap);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set((Serialization.FieldSetter<ConcurrentHashMultiset>) this, (Object) ((ConcurrentMap) stream.readObject()));
    }
}
