package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Enum;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class EnumMultiset<E extends Enum<E>> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 0;
    private transient int[] counts;
    private transient int distinctElements;
    private transient E[] enumConstants;
    private transient long size;
    private transient Class<E> type;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ int add(Object obj, int i) {
        return add((EnumMultiset<E>) ((Enum) obj), i);
    }

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

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ int setCount(Object obj, int i) {
        return setCount((EnumMultiset<E>) ((Enum) obj), i);
    }

    static /* synthetic */ int access$210(EnumMultiset x0) {
        int i = x0.distinctElements;
        x0.distinctElements = i - 1;
        return i;
    }

    public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> type) {
        return new EnumMultiset<>(type);
    }

    public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements) {
        Iterator<E> iterator = elements.iterator();
        Preconditions.checkArgument(iterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
        EnumMultiset<E> multiset = new EnumMultiset<>(iterator.next().getDeclaringClass());
        Iterables.addAll(multiset, elements);
        return multiset;
    }

    public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements, Class<E> type) {
        EnumMultiset<E> result = create(type);
        Iterables.addAll(result, elements);
        return result;
    }

    private EnumMultiset(Class<E> type) {
        this.type = type;
        Preconditions.checkArgument(type.isEnum());
        this.enumConstants = type.getEnumConstants();
        this.counts = new int[this.enumConstants.length];
    }

    private boolean isActuallyE(@NullableDecl Object o) {
        if (!(o instanceof Enum)) {
            return false;
        }
        Enum<?> e = (Enum) o;
        int index = e.ordinal();
        E[] eArr = this.enumConstants;
        if (index >= eArr.length || eArr[index] != e) {
            return false;
        }
        return true;
    }

    void checkIsE(@NullableDecl Object element) {
        Preconditions.checkNotNull(element);
        if (!isActuallyE(element)) {
            throw new ClassCastException("Expected an " + this.type + " but got " + element);
        }
    }

    @Override // com.google.common.collect.AbstractMultiset
    int distinctElements() {
        return this.distinctElements;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        return Ints.saturatedCast(this.size);
    }

    @Override // com.google.common.collect.Multiset
    public int count(@NullableDecl Object element) {
        if (!isActuallyE(element)) {
            return 0;
        }
        return this.counts[((Enum) element).ordinal()];
    }

    public int add(E element, int occurrences) {
        checkIsE(element);
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return count(element);
        }
        int index = element.ordinal();
        int oldCount = this.counts[index];
        long newCount = ((long) oldCount) + ((long) occurrences);
        Preconditions.checkArgument(newCount <= 2147483647L, "too many occurrences: %s", newCount);
        this.counts[index] = (int) newCount;
        if (oldCount == 0) {
            this.distinctElements++;
        }
        this.size += (long) occurrences;
        return oldCount;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int remove(@NullableDecl Object element, int occurrences) {
        if (!isActuallyE(element)) {
            return 0;
        }
        Enum<?> e = (Enum) element;
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return count(element);
        }
        int index = e.ordinal();
        int[] iArr = this.counts;
        int oldCount = iArr[index];
        if (oldCount == 0) {
            return 0;
        }
        if (oldCount <= occurrences) {
            iArr[index] = 0;
            this.distinctElements--;
            this.size -= (long) oldCount;
        } else {
            iArr[index] = oldCount - occurrences;
            this.size -= (long) occurrences;
        }
        return oldCount;
    }

    public int setCount(E element, int count) {
        checkIsE(element);
        CollectPreconditions.checkNonnegative(count, "count");
        int index = element.ordinal();
        int[] iArr = this.counts;
        int oldCount = iArr[index];
        iArr[index] = count;
        this.size += (long) (count - oldCount);
        if (oldCount == 0 && count > 0) {
            this.distinctElements++;
        } else if (oldCount > 0 && count == 0) {
            this.distinctElements--;
        }
        return oldCount;
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        Arrays.fill(this.counts, 0);
        this.size = 0;
        this.distinctElements = 0;
    }

    /* loaded from: classes.dex */
    abstract class Itr<T> implements Iterator<T> {
        int index = 0;
        int toRemove = -1;

        abstract T output(int i);

        Itr() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            while (this.index < EnumMultiset.this.enumConstants.length) {
                int[] iArr = EnumMultiset.this.counts;
                int i = this.index;
                if (iArr[i] > 0) {
                    return true;
                }
                this.index = i + 1;
            }
            return false;
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext()) {
                T result = output(this.index);
                int i = this.index;
                this.toRemove = i;
                this.index = i + 1;
                return result;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(this.toRemove >= 0);
            if (EnumMultiset.this.counts[this.toRemove] > 0) {
                EnumMultiset.access$210(EnumMultiset.this);
                EnumMultiset.this.size -= (long) EnumMultiset.this.counts[this.toRemove];
                EnumMultiset.this.counts[this.toRemove] = 0;
            }
            this.toRemove = -1;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<E> elementIterator() {
        return new EnumMultiset<E>.Itr() { // from class: com.google.common.collect.EnumMultiset.1
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.EnumMultiset.Itr
            public E output(int index) {
                return (E) EnumMultiset.this.enumConstants[index];
            }
        };
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<Multiset.Entry<E>> entryIterator() {
        return new EnumMultiset<E>.Itr() { // from class: com.google.common.collect.EnumMultiset.2
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.EnumMultiset.Itr
            public Multiset.Entry<E> output(final int index) {
                return new Multisets.AbstractEntry<E>() { // from class: com.google.common.collect.EnumMultiset.2.1
                    @Override // com.google.common.collect.Multiset.Entry
                    public E getElement() {
                        return (E) EnumMultiset.this.enumConstants[index];
                    }

                    @Override // com.google.common.collect.Multiset.Entry
                    public int getCount() {
                        return EnumMultiset.this.counts[index];
                    }
                };
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.type);
        Serialization.writeMultiset(this, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.type = (Class) stream.readObject();
        this.enumConstants = this.type.getEnumConstants();
        this.counts = new int[this.enumConstants.length];
        Serialization.populateMultiset(this, stream);
    }
}
