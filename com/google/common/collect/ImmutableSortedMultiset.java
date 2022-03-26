package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.math.IntMath;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public abstract class ImmutableSortedMultiset<E> extends ImmutableSortedMultisetFauxverideShim<E> implements SortedMultiset<E> {
    @LazyInit
    transient ImmutableSortedMultiset<E> descendingMultiset;

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public abstract ImmutableSortedSet<E> elementSet();

    @Override // com.google.common.collect.SortedMultiset
    public abstract ImmutableSortedMultiset<E> headMultiset(E e, BoundType boundType);

    @Override // com.google.common.collect.SortedMultiset
    public abstract ImmutableSortedMultiset<E> tailMultiset(E e, BoundType boundType);

    public static <E> ImmutableSortedMultiset<E> of() {
        return (ImmutableSortedMultiset<E>) RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
    }

    public static ImmutableSortedMultiset of(Comparable comparable) {
        return new RegularImmutableSortedMultiset((RegularImmutableSortedSet) ImmutableSortedSet.of(comparable), new long[]{0, 1}, 0, 1);
    }

    public static ImmutableSortedMultiset of(Comparable comparable, Comparable comparable2) {
        return copyOf(Ordering.natural(), Arrays.asList(comparable, comparable2));
    }

    public static ImmutableSortedMultiset of(Comparable comparable, Comparable comparable2, Comparable comparable3) {
        return copyOf(Ordering.natural(), Arrays.asList(comparable, comparable2, comparable3));
    }

    public static ImmutableSortedMultiset of(Comparable comparable, Comparable comparable2, Comparable comparable3, Comparable comparable4) {
        return copyOf(Ordering.natural(), Arrays.asList(comparable, comparable2, comparable3, comparable4));
    }

    public static ImmutableSortedMultiset of(Comparable comparable, Comparable comparable2, Comparable comparable3, Comparable comparable4, Comparable comparable5) {
        return copyOf(Ordering.natural(), Arrays.asList(comparable, comparable2, comparable3, comparable4, comparable5));
    }

    public static ImmutableSortedMultiset of(Comparable comparable, Comparable comparable2, Comparable comparable3, Comparable comparable4, Comparable comparable5, Comparable comparable6, Comparable... comparableArr) {
        List<E> all = Lists.newArrayListWithCapacity(comparableArr.length + 6);
        Collections.addAll(all, comparable, comparable2, comparable3, comparable4, comparable5, comparable6);
        Collections.addAll(all, comparableArr);
        return copyOf(Ordering.natural(), all);
    }

    public static ImmutableSortedMultiset copyOf(Comparable[] comparableArr) {
        return copyOf(Ordering.natural(), Arrays.asList(comparableArr));
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> elements) {
        return copyOf(Ordering.natural(), elements);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> elements) {
        return copyOf(Ordering.natural(), elements);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        return new Builder(comparator).addAll((Iterator) elements).build();
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        if (elements instanceof ImmutableSortedMultiset) {
            ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset) elements;
            if (comparator.equals(multiset.comparator())) {
                if (multiset.isPartialView()) {
                    return copyOfSortedEntries(comparator, multiset.entrySet().asList());
                }
                return multiset;
            }
        }
        return new Builder(comparator).addAll((Iterable) elements).build();
    }

    public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
        return copyOfSortedEntries(sortedMultiset.comparator(), Lists.newArrayList(sortedMultiset.entrySet()));
    }

    private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> comparator, Collection<Multiset.Entry<E>> entries) {
        if (entries.isEmpty()) {
            return emptyMultiset(comparator);
        }
        ImmutableList.Builder<E> elementsBuilder = new ImmutableList.Builder<>(entries.size());
        long[] cumulativeCounts = new long[entries.size() + 1];
        int i = 0;
        for (Multiset.Entry<E> entry : entries) {
            elementsBuilder.add((ImmutableList.Builder<E>) entry.getElement());
            cumulativeCounts[i + 1] = cumulativeCounts[i] + ((long) entry.getCount());
            i++;
        }
        return new RegularImmutableSortedMultiset(new RegularImmutableSortedSet(elementsBuilder.build(), comparator), cumulativeCounts, 0, entries.size());
    }

    static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return (ImmutableSortedMultiset<E>) RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
        }
        return new RegularImmutableSortedMultiset(comparator);
    }

    @Override // com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public final Comparator<? super E> comparator() {
        return elementSet().comparator();
    }

    @Override // com.google.common.collect.SortedMultiset
    public ImmutableSortedMultiset<E> descendingMultiset() {
        ImmutableSortedMultiset<E> result = this.descendingMultiset;
        if (result != null) {
            return result;
        }
        ImmutableSortedMultiset<E> emptyMultiset = isEmpty() ? emptyMultiset(Ordering.from(comparator()).reverse()) : new DescendingImmutableSortedMultiset<>(this);
        this.descendingMultiset = emptyMultiset;
        return emptyMultiset;
    }

    @Override // com.google.common.collect.SortedMultiset
    @Deprecated
    public final Multiset.Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.SortedMultiset
    @Deprecated
    public final Multiset.Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.SortedMultiset
    public ImmutableSortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
        Preconditions.checkArgument(comparator().compare(lowerBound, upperBound) <= 0, "Expected lowerBound <= upperBound but %s > %s", lowerBound, upperBound);
        return tailMultiset((ImmutableSortedMultiset<E>) lowerBound, lowerBoundType).headMultiset((ImmutableSortedMultiset<E>) upperBound, upperBoundType);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder<>(comparator);
    }

    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder<>(Ordering.natural().reverse());
    }

    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder<>(Ordering.natural());
    }

    /* loaded from: classes.dex */
    public static class Builder<E> extends ImmutableMultiset.Builder<E> {
        private final Comparator<? super E> comparator;
        private boolean forceCopyElements;
        private int length;
        E[] elements = (E[]) new Object[4];
        private int[] counts = new int[4];

        public Builder(Comparator<? super E> comparator) {
            super(true);
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        private void maintenance() {
            int i = this.length;
            E[] eArr = this.elements;
            if (i == eArr.length) {
                dedupAndCoalesce(true);
            } else if (this.forceCopyElements) {
                this.elements = (E[]) Arrays.copyOf(eArr, eArr.length);
            }
            this.forceCopyElements = false;
        }

        private void dedupAndCoalesce(boolean maybeExpand) {
            int i = this.length;
            if (i != 0) {
                E[] sortedElements = (E[]) Arrays.copyOf(this.elements, i);
                Arrays.sort(sortedElements, this.comparator);
                int uniques = 1;
                for (int i2 = 1; i2 < sortedElements.length; i2++) {
                    if (this.comparator.compare((Object) sortedElements[uniques - 1], (Object) sortedElements[i2]) < 0) {
                        sortedElements[uniques] = sortedElements[i2];
                        uniques++;
                    }
                }
                Arrays.fill(sortedElements, uniques, this.length, (Object) null);
                if (maybeExpand) {
                    int i3 = uniques * 4;
                    int i4 = this.length;
                    if (i3 > i4 * 3) {
                        sortedElements = (E[]) Arrays.copyOf(sortedElements, IntMath.saturatedAdd(i4, (i4 / 2) + 1));
                    }
                }
                int[] sortedCounts = new int[sortedElements.length];
                for (int i5 = 0; i5 < this.length; i5++) {
                    int index = Arrays.binarySearch(sortedElements, 0, uniques, this.elements[i5], this.comparator);
                    int[] iArr = this.counts;
                    if (iArr[i5] >= 0) {
                        sortedCounts[index] = sortedCounts[index] + iArr[i5];
                    } else {
                        sortedCounts[index] = ~iArr[i5];
                    }
                }
                this.elements = sortedElements;
                this.counts = sortedCounts;
                this.length = uniques;
            }
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E element) {
            return addCopies((Builder<E>) element, 1);
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... elements) {
            for (E element : elements) {
                add((Builder<E>) element);
            }
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder
        public Builder<E> addCopies(E element, int occurrences) {
            Preconditions.checkNotNull(element);
            CollectPreconditions.checkNonnegative(occurrences, "occurrences");
            if (occurrences == 0) {
                return this;
            }
            maintenance();
            E[] eArr = this.elements;
            int i = this.length;
            eArr[i] = element;
            this.counts[i] = occurrences;
            this.length = i + 1;
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder
        public Builder<E> setCount(E element, int count) {
            Preconditions.checkNotNull(element);
            CollectPreconditions.checkNonnegative(count, "count");
            maintenance();
            E[] eArr = this.elements;
            int i = this.length;
            eArr[i] = element;
            this.counts[i] = ~count;
            this.length = i + 1;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> elements) {
            if (elements instanceof Multiset) {
                for (Multiset.Entry<E> entry : ((Multiset) elements).entrySet()) {
                    addCopies((Builder<E>) entry.getElement(), entry.getCount());
                }
            } else {
                Iterator<? extends E> it = elements.iterator();
                while (it.hasNext()) {
                    add((Builder<E>) it.next());
                }
            }
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> elements) {
            while (elements.hasNext()) {
                add((Builder<E>) elements.next());
            }
            return this;
        }

        private void dedupAndCoalesceAndDeleteEmpty() {
            dedupAndCoalesce(false);
            int size = 0;
            int i = 0;
            while (true) {
                int i2 = this.length;
                if (i < i2) {
                    int[] iArr = this.counts;
                    if (iArr[i] > 0) {
                        E[] eArr = this.elements;
                        eArr[size] = eArr[i];
                        iArr[size] = iArr[i];
                        size++;
                    }
                    i++;
                } else {
                    Arrays.fill(this.elements, size, i2, (Object) null);
                    Arrays.fill(this.counts, size, this.length, 0);
                    this.length = size;
                    return;
                }
            }
        }

        @Override // com.google.common.collect.ImmutableMultiset.Builder, com.google.common.collect.ImmutableCollection.Builder
        public ImmutableSortedMultiset<E> build() {
            dedupAndCoalesceAndDeleteEmpty();
            int i = this.length;
            if (i == 0) {
                return ImmutableSortedMultiset.emptyMultiset(this.comparator);
            }
            RegularImmutableSortedSet<E> elementSet = (RegularImmutableSortedSet) ImmutableSortedSet.construct(this.comparator, i, this.elements);
            long[] cumulativeCounts = new long[this.length + 1];
            int i2 = 0;
            while (true) {
                int i3 = this.length;
                if (i2 < i3) {
                    cumulativeCounts[i2 + 1] = cumulativeCounts[i2] + ((long) this.counts[i2]);
                    i2++;
                } else {
                    this.forceCopyElements = true;
                    return new RegularImmutableSortedMultiset(elementSet, cumulativeCounts, 0, i3);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class SerializedForm<E> implements Serializable {
        final Comparator<? super E> comparator;
        final int[] counts;
        final E[] elements;

        SerializedForm(SortedMultiset<E> multiset) {
            this.comparator = multiset.comparator();
            int n = multiset.entrySet().size();
            this.elements = (E[]) new Object[n];
            this.counts = new int[n];
            int i = 0;
            for (Multiset.Entry<E> entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                i++;
            }
        }

        Object readResolve() {
            int n = this.elements.length;
            Builder<E> builder = new Builder<>(this.comparator);
            for (int i = 0; i < n; i++) {
                builder.addCopies((Builder<E>) this.elements[i], this.counts[i]);
            }
            return builder.build();
        }
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this);
    }
}
