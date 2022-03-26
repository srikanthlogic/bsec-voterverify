package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements NavigableSet<E>, SortedIterable<E> {
    final transient Comparator<? super E> comparator;
    @LazyInit
    transient ImmutableSortedSet<E> descendingSet;

    abstract ImmutableSortedSet<E> createDescendingSet();

    @Override // java.util.NavigableSet
    public abstract UnmodifiableIterator<E> descendingIterator();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract ImmutableSortedSet<E> headSetImpl(E e, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int indexOf(@NullableDecl Object obj);

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public abstract UnmodifiableIterator<E> iterator();

    abstract ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2);

    abstract ImmutableSortedSet<E> tailSetImpl(E e, boolean z);

    public static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return (RegularImmutableSortedSet<E>) RegularImmutableSortedSet.NATURAL_EMPTY_SET;
        }
        return new RegularImmutableSortedSet<>(ImmutableList.of(), comparator);
    }

    public static <E> ImmutableSortedSet<E> of() {
        return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
    }

    public static ImmutableSortedSet of(Comparable comparable) {
        return new RegularImmutableSortedSet(ImmutableList.of(comparable), Ordering.natural());
    }

    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2) {
        return construct(Ordering.natural(), 2, comparable, comparable2);
    }

    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2, Comparable comparable3) {
        return construct(Ordering.natural(), 3, comparable, comparable2, comparable3);
    }

    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2, Comparable comparable3, Comparable comparable4) {
        return construct(Ordering.natural(), 4, comparable, comparable2, comparable3, comparable4);
    }

    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2, Comparable comparable3, Comparable comparable4, Comparable comparable5) {
        return construct(Ordering.natural(), 5, comparable, comparable2, comparable3, comparable4, comparable5);
    }

    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2, Comparable comparable3, Comparable comparable4, Comparable comparable5, Comparable comparable6, Comparable... comparableArr) {
        Comparable[] contents = new Comparable[comparableArr.length + 6];
        contents[0] = comparable;
        contents[1] = comparable2;
        contents[2] = comparable3;
        contents[3] = comparable4;
        contents[4] = comparable5;
        contents[5] = comparable6;
        System.arraycopy(comparableArr, 0, contents, 6, comparableArr.length);
        return construct(Ordering.natural(), contents.length, contents);
    }

    public static ImmutableSortedSet copyOf(Comparable[] comparableArr) {
        return construct(Ordering.natural(), comparableArr.length, (Object[]) comparableArr.clone());
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
        return copyOf(Ordering.natural(), elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
        return copyOf((Comparator) Ordering.natural(), (Collection) elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
        return copyOf(Ordering.natural(), elements);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
        return new Builder(comparator).addAll((Iterator) elements).build();
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        if (SortedIterables.hasSameComparator(comparator, elements) && (elements instanceof ImmutableSortedSet)) {
            ImmutableSortedSet<E> original = (ImmutableSortedSet) elements;
            if (!original.isPartialView()) {
                return original;
            }
        }
        Object[] array = Iterables.toArray(elements);
        return construct(comparator, array.length, array);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
        return copyOf((Comparator) comparator, (Iterable) elements);
    }

    public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
        Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
        ImmutableList<E> list = ImmutableList.copyOf((Collection) sortedSet);
        if (list.isEmpty()) {
            return emptySet(comparator);
        }
        return new RegularImmutableSortedSet(list, comparator);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents) {
        if (n == 0) {
            return emptySet(comparator);
        }
        ObjectArrays.checkElementsNotNull(contents, n);
        Arrays.sort(contents, 0, n, comparator);
        int uniques = 1;
        for (int i = 1; i < n; i++) {
            Object obj = (Object) contents[i];
            if (comparator.compare(obj, (Object) contents[uniques - 1]) != 0) {
                contents[uniques] = obj;
                uniques++;
            }
        }
        Arrays.fill(contents, uniques, n, (Object) null);
        int length = contents.length / 2;
        E[] contents2 = contents;
        if (uniques < length) {
            contents2 = (E[]) Arrays.copyOf(contents, uniques);
        }
        return new RegularImmutableSortedSet(ImmutableList.asImmutableList(contents2, uniques), comparator);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder<>(comparator);
    }

    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder<>(Collections.reverseOrder());
    }

    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder<>(Ordering.natural());
    }

    /* loaded from: classes.dex */
    public static final class Builder<E> extends ImmutableSet.Builder<E> {
        private final Comparator<? super E> comparator;

        public Builder(Comparator<? super E> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E element) {
            super.add((Builder<E>) element);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... elements) {
            super.add((Object[]) elements);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> elements) {
            super.addAll((Iterable) elements);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll((Iterator) elements);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public ImmutableSortedSet<E> build() {
            ImmutableSortedSet<E> result = ImmutableSortedSet.construct(this.comparator, this.size, this.contents);
            this.size = result.size();
            this.forceCopy = true;
            return result;
        }
    }

    int unsafeCompare(Object a2, Object b) {
        return unsafeCompare(this.comparator, a2, b);
    }

    static int unsafeCompare(Comparator<?> comparator, Object a2, Object b) {
        return comparator.compare(a2, b);
    }

    public ImmutableSortedSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override // java.util.SortedSet, com.google.common.collect.SortedIterable
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    @Override // java.util.NavigableSet, java.util.SortedSet
    public ImmutableSortedSet<E> headSet(E toElement) {
        return headSet((ImmutableSortedSet<E>) toElement, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
        return headSetImpl(Preconditions.checkNotNull(toElement), inclusive);
    }

    @Override // java.util.NavigableSet, java.util.SortedSet
    public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
        return subSet((boolean) fromElement, true, (boolean) toElement, false);
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        Preconditions.checkNotNull(fromElement);
        Preconditions.checkNotNull(toElement);
        Preconditions.checkArgument(this.comparator.compare(fromElement, toElement) <= 0);
        return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override // java.util.NavigableSet, java.util.SortedSet
    public ImmutableSortedSet<E> tailSet(E fromElement) {
        return tailSet((ImmutableSortedSet<E>) fromElement, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
        return tailSetImpl(Preconditions.checkNotNull(fromElement), inclusive);
    }

    @Override // java.util.NavigableSet
    public E lower(E e) {
        return (E) Iterators.getNext(headSet((ImmutableSortedSet<E>) e, false).descendingIterator(), null);
    }

    @Override // java.util.NavigableSet
    public E floor(E e) {
        return (E) Iterators.getNext(headSet((ImmutableSortedSet<E>) e, true).descendingIterator(), null);
    }

    @Override // java.util.NavigableSet
    public E ceiling(E e) {
        return (E) Iterables.getFirst(tailSet((ImmutableSortedSet<E>) e, true), null);
    }

    @Override // java.util.NavigableSet
    public E higher(E e) {
        return (E) Iterables.getFirst(tailSet((ImmutableSortedSet<E>) e, false), null);
    }

    @Override // java.util.SortedSet
    public E first() {
        return iterator().next();
    }

    @Override // java.util.SortedSet
    public E last() {
        return descendingIterator().next();
    }

    @Override // java.util.NavigableSet
    @Deprecated
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableSet
    @Deprecated
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> result = this.descendingSet;
        if (result != null) {
            return result;
        }
        ImmutableSortedSet<E> result2 = createDescendingSet();
        this.descendingSet = result2;
        result2.descendingSet = this;
        return result2;
    }

    /* loaded from: classes.dex */
    private static class SerializedForm<E> implements Serializable {
        private static final long serialVersionUID;
        final Comparator<? super E> comparator;
        final Object[] elements;

        public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
            this.comparator = comparator;
            this.elements = elements;
        }

        /* JADX WARN: Multi-variable type inference failed */
        Object readResolve() {
            return new Builder(this.comparator).add(this.elements).build();
        }
    }

    private void readObject(ObjectInputStream unused) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this.comparator, toArray());
    }
}
