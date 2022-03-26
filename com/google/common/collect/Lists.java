package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class Lists {
    private Lists() {
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        Preconditions.checkNotNull(elements);
        ArrayList<E> list = new ArrayList<>(computeArrayListCapacity(elements.length));
        Collections.addAll(list, elements);
        return list;
    }

    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
        Preconditions.checkNotNull(elements);
        if (elements instanceof Collection) {
            return new ArrayList<>(Collections2.cast(elements));
        }
        return newArrayList(elements.iterator());
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
        ArrayList<E> list = newArrayList();
        Iterators.addAll(list, elements);
        return list;
    }

    static int computeArrayListCapacity(int arraySize) {
        CollectPreconditions.checkNonnegative(arraySize, "arraySize");
        return Ints.saturatedCast(((long) arraySize) + 5 + ((long) (arraySize / 10)));
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
        CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
        return new ArrayList<>(initialArraySize);
    }

    public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
        return new ArrayList<>(computeArrayListCapacity(estimatedSize));
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
        LinkedList<E> list = newLinkedList();
        Iterables.addAll(list, elements);
        return list;
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<>();
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
        return new CopyOnWriteArrayList<>(elements instanceof Collection ? Collections2.cast(elements) : newArrayList(elements));
    }

    public static <E> List<E> asList(@NullableDecl E first, E[] rest) {
        return new OnePlusArrayList(first, rest);
    }

    public static <E> List<E> asList(@NullableDecl E first, @NullableDecl E second, E[] rest) {
        return new TwoPlusArrayList(first, second, rest);
    }

    /* loaded from: classes.dex */
    public static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        private static final long serialVersionUID;
        @NullableDecl
        final E first;
        final E[] rest;

        OnePlusArrayList(@NullableDecl E first, E[] rest) {
            this.first = first;
            this.rest = (E[]) ((Object[]) Preconditions.checkNotNull(rest));
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return IntMath.saturatedAdd(this.rest.length, 1);
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int index) {
            Preconditions.checkElementIndex(index, size());
            return index == 0 ? this.first : this.rest[index - 1];
        }
    }

    /* loaded from: classes.dex */
    private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        private static final long serialVersionUID;
        @NullableDecl
        final E first;
        final E[] rest;
        @NullableDecl
        final E second;

        TwoPlusArrayList(@NullableDecl E first, @NullableDecl E second, E[] rest) {
            this.first = first;
            this.second = second;
            this.rest = (E[]) ((Object[]) Preconditions.checkNotNull(rest));
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return IntMath.saturatedAdd(this.rest.length, 2);
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int index) {
            if (index == 0) {
                return this.first;
            }
            if (index == 1) {
                return this.second;
            }
            Preconditions.checkElementIndex(index, size());
            return this.rest[index - 2];
        }
    }

    public static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) {
        return CartesianList.create(lists);
    }

    @SafeVarargs
    public static <B> List<List<B>> cartesianProduct(List<? extends B>... lists) {
        return cartesianProduct(Arrays.asList(lists));
    }

    public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
        return fromList instanceof RandomAccess ? new TransformingRandomAccessList(fromList, function) : new TransformingSequentialList(fromList, function);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable {
        private static final long serialVersionUID;
        final List<F> fromList;
        final Function<? super F, ? extends T> function;

        TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
            this.fromList = (List) Preconditions.checkNotNull(fromList);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection, java.util.AbstractList
        public void clear() {
            this.fromList.clear();
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return this.fromList.size();
        }

        @Override // java.util.AbstractSequentialList, java.util.List, java.util.AbstractList
        public ListIterator<T> listIterator(int index) {
            return new TransformedListIterator<F, T>(this.fromList.listIterator(index)) { // from class: com.google.common.collect.Lists.TransformingSequentialList.1
                @Override // com.google.common.collect.TransformedIterator
                T transform(F from) {
                    return (T) TransformingSequentialList.this.function.apply(from);
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {
        private static final long serialVersionUID;
        final List<F> fromList;
        final Function<? super F, ? extends T> function;

        TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
            this.fromList = (List) Preconditions.checkNotNull(fromList);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.List, java.util.Collection
        public void clear() {
            this.fromList.clear();
        }

        @Override // java.util.AbstractList, java.util.List
        public T get(int index) {
            return (T) this.function.apply((F) this.fromList.get(index));
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.List, java.util.Collection, java.lang.Iterable
        public Iterator<T> iterator() {
            return listIterator();
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<T> listIterator(int index) {
            return new TransformedListIterator<F, T>(this.fromList.listIterator(index)) { // from class: com.google.common.collect.Lists.TransformingRandomAccessList.1
                @Override // com.google.common.collect.TransformedIterator
                T transform(F from) {
                    return (T) TransformingRandomAccessList.this.function.apply(from);
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }

        @Override // java.util.AbstractList, java.util.List
        public T remove(int index) {
            return (T) this.function.apply((F) this.fromList.remove(index));
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return this.fromList.size();
        }
    }

    public static <T> List<List<T>> partition(List<T> list, int size) {
        Preconditions.checkNotNull(list);
        Preconditions.checkArgument(size > 0);
        return list instanceof RandomAccess ? new RandomAccessPartition(list, size) : new Partition(list, size);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Partition<T> extends AbstractList<List<T>> {
        final List<T> list;
        final int size;

        Partition(List<T> list, int size) {
            this.list = list;
            this.size = size;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<T> get(int index) {
            Preconditions.checkElementIndex(index, size());
            int i = this.size;
            int start = index * i;
            return this.list.subList(start, Math.min(i + start, this.list.size()));
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public boolean isEmpty() {
            return this.list.isEmpty();
        }
    }

    /* loaded from: classes.dex */
    private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
        RandomAccessPartition(List<T> list, int size) {
            super(list, size);
        }
    }

    public static ImmutableList<Character> charactersOf(String string) {
        return new StringAsImmutableList((String) Preconditions.checkNotNull(string));
    }

    public static List<Character> charactersOf(CharSequence sequence) {
        return new CharSequenceAsList((CharSequence) Preconditions.checkNotNull(sequence));
    }

    /* loaded from: classes.dex */
    public static final class StringAsImmutableList extends ImmutableList<Character> {
        private final String string;

        StringAsImmutableList(String string) {
            this.string = string;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public int indexOf(@NullableDecl Object object) {
            if (object instanceof Character) {
                return this.string.indexOf(((Character) object).charValue());
            }
            return -1;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public int lastIndexOf(@NullableDecl Object object) {
            if (object instanceof Character) {
                return this.string.lastIndexOf(((Character) object).charValue());
            }
            return -1;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public ImmutableList<Character> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
            return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return false;
        }

        @Override // java.util.List
        public Character get(int index) {
            Preconditions.checkElementIndex(index, size());
            return Character.valueOf(this.string.charAt(index));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.string.length();
        }
    }

    /* loaded from: classes.dex */
    private static final class CharSequenceAsList extends AbstractList<Character> {
        private final CharSequence sequence;

        CharSequenceAsList(CharSequence sequence) {
            this.sequence = sequence;
        }

        @Override // java.util.AbstractList, java.util.List
        public Character get(int index) {
            Preconditions.checkElementIndex(index, size());
            return Character.valueOf(this.sequence.charAt(index));
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return this.sequence.length();
        }
    }

    public static <T> List<T> reverse(List<T> list) {
        if (list instanceof ImmutableList) {
            return ((ImmutableList) list).reverse();
        }
        if (list instanceof ReverseList) {
            return ((ReverseList) list).getForwardList();
        }
        if (list instanceof RandomAccess) {
            return new RandomAccessReverseList(list);
        }
        return new ReverseList(list);
    }

    /* loaded from: classes.dex */
    public static class ReverseList<T> extends AbstractList<T> {
        private final List<T> forwardList;

        ReverseList(List<T> forwardList) {
            this.forwardList = (List) Preconditions.checkNotNull(forwardList);
        }

        List<T> getForwardList() {
            return this.forwardList;
        }

        private int reverseIndex(int index) {
            int size = size();
            Preconditions.checkElementIndex(index, size);
            return (size - 1) - index;
        }

        public int reversePosition(int index) {
            int size = size();
            Preconditions.checkPositionIndex(index, size);
            return size - index;
        }

        @Override // java.util.AbstractList, java.util.List
        public void add(int index, @NullableDecl T element) {
            this.forwardList.add(reversePosition(index), element);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.List, java.util.Collection
        public void clear() {
            this.forwardList.clear();
        }

        @Override // java.util.AbstractList, java.util.List
        public T remove(int index) {
            return this.forwardList.remove(reverseIndex(index));
        }

        @Override // java.util.AbstractList
        protected void removeRange(int fromIndex, int toIndex) {
            subList(fromIndex, toIndex).clear();
        }

        @Override // java.util.AbstractList, java.util.List
        public T set(int index, @NullableDecl T element) {
            return this.forwardList.set(reverseIndex(index), element);
        }

        @Override // java.util.AbstractList, java.util.List
        public T get(int index) {
            return this.forwardList.get(reverseIndex(index));
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return this.forwardList.size();
        }

        @Override // java.util.AbstractList, java.util.List
        public List<T> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
            return Lists.reverse(this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.List, java.util.Collection, java.lang.Iterable
        public Iterator<T> iterator() {
            return listIterator();
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<T> listIterator(int index) {
            final ListIterator<T> forwardIterator = this.forwardList.listIterator(reversePosition(index));
            return new ListIterator<T>() { // from class: com.google.common.collect.Lists.ReverseList.1
                boolean canRemoveOrSet;

                @Override // java.util.ListIterator
                public void add(T e) {
                    forwardIterator.add(e);
                    forwardIterator.previous();
                    this.canRemoveOrSet = false;
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public boolean hasNext() {
                    return forwardIterator.hasPrevious();
                }

                @Override // java.util.ListIterator
                public boolean hasPrevious() {
                    return forwardIterator.hasNext();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public T next() {
                    if (hasNext()) {
                        this.canRemoveOrSet = true;
                        return (T) forwardIterator.previous();
                    }
                    throw new NoSuchElementException();
                }

                @Override // java.util.ListIterator
                public int nextIndex() {
                    return ReverseList.this.reversePosition(forwardIterator.nextIndex());
                }

                @Override // java.util.ListIterator
                public T previous() {
                    if (hasPrevious()) {
                        this.canRemoveOrSet = true;
                        return (T) forwardIterator.next();
                    }
                    throw new NoSuchElementException();
                }

                @Override // java.util.ListIterator
                public int previousIndex() {
                    return nextIndex() - 1;
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public void remove() {
                    CollectPreconditions.checkRemove(this.canRemoveOrSet);
                    forwardIterator.remove();
                    this.canRemoveOrSet = false;
                }

                @Override // java.util.ListIterator
                public void set(T e) {
                    Preconditions.checkState(this.canRemoveOrSet);
                    forwardIterator.set(e);
                }
            };
        }
    }

    /* loaded from: classes.dex */
    public static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {
        RandomAccessReverseList(List<T> forwardList) {
            super(forwardList);
        }
    }

    public static int hashCodeImpl(List<?> list) {
        int hashCode = 1;
        Iterator<?> it = list.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            hashCode = ~(~((hashCode * 31) + (o == null ? 0 : o.hashCode())));
        }
        return hashCode;
    }

    public static boolean equalsImpl(List<?> thisList, @NullableDecl Object other) {
        if (other == Preconditions.checkNotNull(thisList)) {
            return true;
        }
        if (!(other instanceof List)) {
            return false;
        }
        List<?> otherList = (List) other;
        int size = thisList.size();
        if (size != otherList.size()) {
            return false;
        }
        if (!(thisList instanceof RandomAccess) || !(otherList instanceof RandomAccess)) {
            return Iterators.elementsEqual(thisList.iterator(), otherList.iterator());
        }
        for (int i = 0; i < size; i++) {
            if (!Objects.equal(thisList.get(i), otherList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
        boolean changed = false;
        ListIterator<E> listIterator = list.listIterator(index);
        for (E e : elements) {
            listIterator.add(e);
            changed = true;
        }
        return changed;
    }

    public static int indexOfImpl(List<?> list, @NullableDecl Object element) {
        if (list instanceof RandomAccess) {
            return indexOfRandomAccess(list, element);
        }
        ListIterator<?> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (Objects.equal(element, listIterator.next())) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }

    private static int indexOfRandomAccess(List<?> list, @NullableDecl Object element) {
        int size = list.size();
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (list.get(i) == null) {
                    return i;
                }
            }
            return -1;
        }
        for (int i2 = 0; i2 < size; i2++) {
            if (element.equals(list.get(i2))) {
                return i2;
            }
        }
        return -1;
    }

    public static int lastIndexOfImpl(List<?> list, @NullableDecl Object element) {
        if (list instanceof RandomAccess) {
            return lastIndexOfRandomAccess(list, element);
        }
        ListIterator<?> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            if (Objects.equal(element, listIterator.previous())) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }

    private static int lastIndexOfRandomAccess(List<?> list, @NullableDecl Object element) {
        if (element == null) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i) == null) {
                    return i;
                }
            }
            return -1;
        }
        for (int i2 = list.size() - 1; i2 >= 0; i2--) {
            if (element.equals(list.get(i2))) {
                return i2;
            }
        }
        return -1;
    }

    public static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) {
        return new AbstractListWrapper(list).listIterator(index);
    }

    public static <E> List<E> subListImpl(List<E> list, int fromIndex, int toIndex) {
        List<E> wrapper;
        if (list instanceof RandomAccess) {
            wrapper = new RandomAccessListWrapper<E>(list) { // from class: com.google.common.collect.Lists.1
                private static final long serialVersionUID;

                @Override // java.util.AbstractList, java.util.List
                public ListIterator<E> listIterator(int index) {
                    return this.backingList.listIterator(index);
                }
            };
        } else {
            wrapper = new AbstractListWrapper<E>(list) { // from class: com.google.common.collect.Lists.2
                private static final long serialVersionUID;

                @Override // java.util.AbstractList, java.util.List
                public ListIterator<E> listIterator(int index) {
                    return this.backingList.listIterator(index);
                }
            };
        }
        return wrapper.subList(fromIndex, toIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AbstractListWrapper<E> extends AbstractList<E> {
        final List<E> backingList;

        AbstractListWrapper(List<E> backingList) {
            this.backingList = (List) Preconditions.checkNotNull(backingList);
        }

        @Override // java.util.AbstractList, java.util.List
        public void add(int index, E element) {
            this.backingList.add(index, element);
        }

        @Override // java.util.AbstractList, java.util.List
        public boolean addAll(int index, Collection<? extends E> c) {
            return this.backingList.addAll(index, c);
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int index) {
            return this.backingList.get(index);
        }

        @Override // java.util.AbstractList, java.util.List
        public E remove(int index) {
            return this.backingList.remove(index);
        }

        @Override // java.util.AbstractList, java.util.List
        public E set(int index, E element) {
            return this.backingList.set(index, element);
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public boolean contains(Object o) {
            return this.backingList.contains(o);
        }

        @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
        public int size() {
            return this.backingList.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class RandomAccessListWrapper<E> extends AbstractListWrapper<E> implements RandomAccess {
        RandomAccessListWrapper(List<E> backingList) {
            super(backingList);
        }
    }

    public static <T> List<T> cast(Iterable<T> iterable) {
        return (List) iterable;
    }
}
