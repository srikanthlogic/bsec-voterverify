package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;
import java.lang.Comparable;
import java.util.NoSuchElementException;
/* loaded from: classes.dex */
public abstract class ContiguousSet<C extends Comparable> extends ImmutableSortedSet<C> {
    final DiscreteDomain<C> domain;

    abstract ContiguousSet<C> headSetImpl(C c, boolean z);

    public abstract ContiguousSet<C> intersection(ContiguousSet<C> contiguousSet);

    public abstract Range<C> range();

    public abstract Range<C> range(BoundType boundType, BoundType boundType2);

    abstract ContiguousSet<C> subSetImpl(C c, boolean z, C c2, boolean z2);

    abstract ContiguousSet<C> tailSetImpl(C c, boolean z);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedSet
    public /* bridge */ /* synthetic */ ImmutableSortedSet headSetImpl(Object obj, boolean z) {
        return headSetImpl((ContiguousSet<C>) ((Comparable) obj), z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedSet
    /* bridge */ /* synthetic */ ImmutableSortedSet subSetImpl(Object obj, boolean z, Object obj2, boolean z2) {
        return subSetImpl((boolean) ((Comparable) obj), z, (boolean) ((Comparable) obj2), z2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedSet
    /* bridge */ /* synthetic */ ImmutableSortedSet tailSetImpl(Object obj, boolean z) {
        return tailSetImpl((ContiguousSet<C>) ((Comparable) obj), z);
    }

    public static <C extends Comparable> ContiguousSet<C> create(Range<C> range, DiscreteDomain<C> domain) {
        Preconditions.checkNotNull(range);
        Preconditions.checkNotNull(domain);
        Range<C> effectiveRange = range;
        try {
            if (!range.hasLowerBound()) {
                effectiveRange = effectiveRange.intersection(Range.atLeast(domain.minValue()));
            }
            if (!range.hasUpperBound()) {
                effectiveRange = effectiveRange.intersection(Range.atMost(domain.maxValue()));
            }
            return effectiveRange.isEmpty() || Range.compareOrThrow(range.lowerBound.leastValueAbove(domain), range.upperBound.greatestValueBelow(domain)) > 0 ? new EmptyContiguousSet(domain) : new RegularContiguousSet(effectiveRange, domain);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static ContiguousSet<Integer> closed(int lower, int upper) {
        return create(Range.closed(Integer.valueOf(lower), Integer.valueOf(upper)), DiscreteDomain.integers());
    }

    public static ContiguousSet<Long> closed(long lower, long upper) {
        return create(Range.closed(Long.valueOf(lower), Long.valueOf(upper)), DiscreteDomain.longs());
    }

    public static ContiguousSet<Integer> closedOpen(int lower, int upper) {
        return create(Range.closedOpen(Integer.valueOf(lower), Integer.valueOf(upper)), DiscreteDomain.integers());
    }

    public static ContiguousSet<Long> closedOpen(long lower, long upper) {
        return create(Range.closedOpen(Long.valueOf(lower), Long.valueOf(upper)), DiscreteDomain.longs());
    }

    public ContiguousSet(DiscreteDomain<C> domain) {
        super(Ordering.natural());
        this.domain = domain;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ContiguousSet<C> headSet(C toElement) {
        return headSetImpl((ContiguousSet<C>) ((Comparable) Preconditions.checkNotNull(toElement)), false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ContiguousSet<C> headSet(C toElement, boolean inclusive) {
        return headSetImpl((ContiguousSet<C>) ((Comparable) Preconditions.checkNotNull(toElement)), inclusive);
    }

    public ContiguousSet<C> subSet(C fromElement, C toElement) {
        Preconditions.checkNotNull(fromElement);
        Preconditions.checkNotNull(toElement);
        Preconditions.checkArgument(comparator().compare(fromElement, toElement) <= 0);
        return subSetImpl((boolean) fromElement, true, (boolean) toElement, false);
    }

    public ContiguousSet<C> subSet(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
        Preconditions.checkNotNull(fromElement);
        Preconditions.checkNotNull(toElement);
        Preconditions.checkArgument(comparator().compare(fromElement, toElement) <= 0);
        return subSetImpl((boolean) fromElement, fromInclusive, (boolean) toElement, toInclusive);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ContiguousSet<C> tailSet(C fromElement) {
        return tailSetImpl((ContiguousSet<C>) ((Comparable) Preconditions.checkNotNull(fromElement)), true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ContiguousSet<C> tailSet(C fromElement, boolean inclusive) {
        return tailSetImpl((ContiguousSet<C>) ((Comparable) Preconditions.checkNotNull(fromElement)), inclusive);
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<C> createDescendingSet() {
        return new DescendingImmutableSortedSet(this);
    }

    @Override // java.util.AbstractCollection, java.lang.Object
    public String toString() {
        return range().toString();
    }

    @Deprecated
    public static <E> ImmutableSortedSet.Builder<E> builder() {
        throw new UnsupportedOperationException();
    }
}
