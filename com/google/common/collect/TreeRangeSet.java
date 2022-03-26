package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public class TreeRangeSet<C extends Comparable<?>> extends AbstractRangeSet<C> implements Serializable {
    @NullableDecl
    private transient Set<Range<C>> asDescendingSetOfRanges;
    @NullableDecl
    private transient Set<Range<C>> asRanges;
    @NullableDecl
    private transient RangeSet<C> complement;
    final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void addAll(RangeSet rangeSet) {
        super.addAll(rangeSet);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void addAll(Iterable iterable) {
        super.addAll(iterable);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean contains(Comparable comparable) {
        return super.contains(comparable);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean enclosesAll(RangeSet rangeSet) {
        return super.enclosesAll(rangeSet);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean enclosesAll(Iterable iterable) {
        return super.enclosesAll(iterable);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean equals(@NullableDecl Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void removeAll(RangeSet rangeSet) {
        super.removeAll(rangeSet);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void removeAll(Iterable iterable) {
        super.removeAll(iterable);
    }

    public static <C extends Comparable<?>> TreeRangeSet<C> create() {
        return new TreeRangeSet<>(new TreeMap());
    }

    public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet) {
        TreeRangeSet<C> result = create();
        result.addAll(rangeSet);
        return result;
    }

    public static <C extends Comparable<?>> TreeRangeSet<C> create(Iterable<Range<C>> ranges) {
        TreeRangeSet<C> result = create();
        result.addAll(ranges);
        return result;
    }

    private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
        this.rangesByLowerBound = rangesByLowerCut;
    }

    @Override // com.google.common.collect.RangeSet
    public Set<Range<C>> asRanges() {
        Set<Range<C>> result = this.asRanges;
        if (result != null) {
            return result;
        }
        AsRanges asRanges = new AsRanges(this.rangesByLowerBound.values());
        this.asRanges = asRanges;
        return asRanges;
    }

    @Override // com.google.common.collect.RangeSet
    public Set<Range<C>> asDescendingSetOfRanges() {
        Set<Range<C>> result = this.asDescendingSetOfRanges;
        if (result != null) {
            return result;
        }
        AsRanges asRanges = new AsRanges(this.rangesByLowerBound.descendingMap().values());
        this.asDescendingSetOfRanges = asRanges;
        return asRanges;
    }

    /* loaded from: classes.dex */
    final class AsRanges extends ForwardingCollection<Range<C>> implements Set<Range<C>> {
        final Collection<Range<C>> delegate;

        AsRanges(Collection<Range<C>> delegate) {
            this.delegate = delegate;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Collection<Range<C>> delegate() {
            return (Collection<Range<C>>) this.delegate;
        }

        @Override // java.util.Collection, java.lang.Object, java.util.Set
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        @Override // java.util.Collection, java.lang.Object, java.util.Set
        public boolean equals(@NullableDecl Object o) {
            return Sets.equalsImpl(this, o);
        }
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @NullableDecl
    public Range<C> rangeContaining(C value) {
        Preconditions.checkNotNull(value);
        Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(Cut.belowValue(value));
        if (floorEntry == null || !floorEntry.getValue().contains(value)) {
            return null;
        }
        return floorEntry.getValue();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public boolean intersects(Range<C> range) {
        Preconditions.checkNotNull(range);
        Map.Entry<Cut<C>, Range<C>> ceilingEntry = this.rangesByLowerBound.ceilingEntry(range.lowerBound);
        if (ceilingEntry != null && ceilingEntry.getValue().isConnected(range) && !ceilingEntry.getValue().intersection(range).isEmpty()) {
            return true;
        }
        Map.Entry<Cut<C>, Range<C>> priorEntry = this.rangesByLowerBound.lowerEntry(range.lowerBound);
        if (priorEntry == null || !priorEntry.getValue().isConnected(range) || priorEntry.getValue().intersection(range).isEmpty()) {
            return false;
        }
        return true;
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public boolean encloses(Range<C> range) {
        Preconditions.checkNotNull(range);
        Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
        return floorEntry != null && floorEntry.getValue().encloses(range);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @NullableDecl
    public Range<C> rangeEnclosing(Range<C> range) {
        Preconditions.checkNotNull(range);
        Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
        if (floorEntry == null || !floorEntry.getValue().encloses(range)) {
            return null;
        }
        return floorEntry.getValue();
    }

    @Override // com.google.common.collect.RangeSet
    public Range<C> span() {
        Map.Entry<Cut<C>, Range<C>> firstEntry = this.rangesByLowerBound.firstEntry();
        Map.Entry<Cut<C>, Range<C>> lastEntry = this.rangesByLowerBound.lastEntry();
        if (firstEntry != null) {
            return Range.create(firstEntry.getValue().lowerBound, lastEntry.getValue().upperBound);
        }
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public void add(Range<C> rangeToAdd) {
        Preconditions.checkNotNull(rangeToAdd);
        if (!rangeToAdd.isEmpty()) {
            Cut<C> lbToAdd = rangeToAdd.lowerBound;
            Cut<C> ubToAdd = rangeToAdd.upperBound;
            Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(lbToAdd);
            if (entryBelowLB != null) {
                Range<C> rangeBelowLB = entryBelowLB.getValue();
                if (rangeBelowLB.upperBound.compareTo(lbToAdd) >= 0) {
                    if (rangeBelowLB.upperBound.compareTo(ubToAdd) >= 0) {
                        ubToAdd = rangeBelowLB.upperBound;
                    }
                    lbToAdd = rangeBelowLB.lowerBound;
                }
            }
            Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(ubToAdd);
            if (entryBelowUB != null) {
                Range<C> rangeBelowUB = entryBelowUB.getValue();
                if (rangeBelowUB.upperBound.compareTo(ubToAdd) >= 0) {
                    ubToAdd = rangeBelowUB.upperBound;
                }
            }
            this.rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
            replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
        }
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public void remove(Range<C> rangeToRemove) {
        Preconditions.checkNotNull(rangeToRemove);
        if (!rangeToRemove.isEmpty()) {
            Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
            if (entryBelowLB != null) {
                Range<C> rangeBelowLB = entryBelowLB.getValue();
                if (rangeBelowLB.upperBound.compareTo(rangeToRemove.lowerBound) >= 0) {
                    if (rangeToRemove.hasUpperBound() && rangeBelowLB.upperBound.compareTo(rangeToRemove.upperBound) >= 0) {
                        replaceRangeWithSameLowerBound(Range.create(rangeToRemove.upperBound, rangeBelowLB.upperBound));
                    }
                    replaceRangeWithSameLowerBound(Range.create(rangeBelowLB.lowerBound, rangeToRemove.lowerBound));
                }
            }
            Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(rangeToRemove.upperBound);
            if (entryBelowUB != null) {
                Range<C> rangeBelowUB = entryBelowUB.getValue();
                if (rangeToRemove.hasUpperBound() && rangeBelowUB.upperBound.compareTo(rangeToRemove.upperBound) >= 0) {
                    replaceRangeWithSameLowerBound(Range.create(rangeToRemove.upperBound, rangeBelowUB.upperBound));
                }
            }
            this.rangesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
        }
    }

    private void replaceRangeWithSameLowerBound(Range<C> range) {
        if (range.isEmpty()) {
            this.rangesByLowerBound.remove(range.lowerBound);
        } else {
            this.rangesByLowerBound.put(range.lowerBound, range);
        }
    }

    @Override // com.google.common.collect.RangeSet
    public RangeSet<C> complement() {
        RangeSet<C> result = this.complement;
        if (result != null) {
            return result;
        }
        Complement complement = new Complement();
        this.complement = complement;
        return complement;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class RangesByUpperBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
        private final Range<Cut<C>> upperBoundWindow;

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap headMap(Object obj, boolean z) {
            return headMap((Cut) ((Cut) obj), z);
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap subMap(Object obj, boolean z, Object obj2, boolean z2) {
            return subMap((Cut) ((Cut) obj), z, (Cut) ((Cut) obj2), z2);
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap tailMap(Object obj, boolean z) {
            return tailMap((Cut) ((Cut) obj), z);
        }

        RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
            this.rangesByLowerBound = rangesByLowerBound;
            this.upperBoundWindow = Range.all();
        }

        private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, Range<Cut<C>> upperBoundWindow) {
            this.rangesByLowerBound = rangesByLowerBound;
            this.upperBoundWindow = upperBoundWindow;
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
            if (window.isConnected(this.upperBoundWindow)) {
                return new RangesByUpperBound(this.rangesByLowerBound, window.intersection(this.upperBoundWindow));
            }
            return ImmutableSortedMap.of();
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
            return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
            return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
            return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
        }

        @Override // java.util.SortedMap
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@NullableDecl Object key) {
            return get(key) != null;
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        public Range<C> get(@NullableDecl Object key) {
            Map.Entry<Cut<C>, Range<C>> candidate;
            if (key instanceof Cut) {
                try {
                    Cut<C> cut = (Cut) key;
                    if (this.upperBoundWindow.contains(cut) && (candidate = this.rangesByLowerBound.lowerEntry(cut)) != null && candidate.getValue().upperBound.equals(cut)) {
                        return candidate.getValue();
                    }
                } catch (ClassCastException e) {
                    return null;
                }
            }
            return null;
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            final Iterator<Range<C>> backingItr;
            if (!this.upperBoundWindow.hasLowerBound()) {
                backingItr = this.rangesByLowerBound.values().iterator();
            } else {
                Map.Entry<Cut<C>, Range<C>> lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
                if (lowerEntry == null) {
                    backingItr = this.rangesByLowerBound.values().iterator();
                } else if (this.upperBoundWindow.lowerBound.isLessThan(lowerEntry.getValue().upperBound)) {
                    backingItr = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
                } else {
                    backingItr = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
                }
            }
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() { // from class: com.google.common.collect.TreeRangeSet.RangesByUpperBound.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.AbstractIterator
                public Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (!backingItr.hasNext()) {
                        return (Map.Entry) endOfData();
                    }
                    Range<C> range = (Range) backingItr.next();
                    if (RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound)) {
                        return (Map.Entry) endOfData();
                    }
                    return Maps.immutableEntry(range.upperBound, range);
                }
            };
        }

        @Override // com.google.common.collect.AbstractNavigableMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            Collection<Range<C>> candidates;
            if (this.upperBoundWindow.hasUpperBound()) {
                candidates = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
            } else {
                candidates = this.rangesByLowerBound.descendingMap().values();
            }
            final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator(candidates.iterator());
            if (backingItr.hasNext() && this.upperBoundWindow.upperBound.isLessThan(backingItr.peek().upperBound)) {
                backingItr.next();
            }
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() { // from class: com.google.common.collect.TreeRangeSet.RangesByUpperBound.2
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.AbstractIterator
                public Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (!backingItr.hasNext()) {
                        return (Map.Entry) endOfData();
                    }
                    Range<C> range = (Range) backingItr.next();
                    if (RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound)) {
                        return Maps.immutableEntry(range.upperBound, range);
                    }
                    return (Map.Entry) endOfData();
                }
            };
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            if (this.upperBoundWindow.equals(Range.all())) {
                return this.rangesByLowerBound.size();
            }
            return Iterators.size(entryIterator());
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            if (this.upperBoundWindow.equals(Range.all())) {
                return this.rangesByLowerBound.isEmpty();
            }
            return !entryIterator().hasNext();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ComplementRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final Range<Cut<C>> complementLowerBoundWindow;
        private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
        private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap headMap(Object obj, boolean z) {
            return headMap((Cut) ((Cut) obj), z);
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap subMap(Object obj, boolean z, Object obj2, boolean z2) {
            return subMap((Cut) ((Cut) obj), z, (Cut) ((Cut) obj2), z2);
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap tailMap(Object obj, boolean z) {
            return tailMap((Cut) ((Cut) obj), z);
        }

        ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound) {
            this(positiveRangesByLowerBound, Range.all());
        }

        private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, Range<Cut<C>> window) {
            this.positiveRangesByLowerBound = positiveRangesByLowerBound;
            this.positiveRangesByUpperBound = new RangesByUpperBound(positiveRangesByLowerBound);
            this.complementLowerBoundWindow = window;
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
            if (!this.complementLowerBoundWindow.isConnected(subWindow)) {
                return ImmutableSortedMap.of();
            }
            return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, subWindow.intersection(this.complementLowerBoundWindow));
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
            return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
            return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
            return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
        }

        @Override // java.util.SortedMap
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            Collection<Range<C>> positiveRanges;
            final Cut<C> firstComplementRangeLowerBound;
            if (this.complementLowerBoundWindow.hasLowerBound()) {
                positiveRanges = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values();
            } else {
                positiveRanges = this.positiveRangesByUpperBound.values();
            }
            final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRanges.iterator());
            if (this.complementLowerBoundWindow.contains(Cut.belowAll()) && (!positiveItr.hasNext() || positiveItr.peek().lowerBound != Cut.belowAll())) {
                firstComplementRangeLowerBound = Cut.belowAll();
            } else if (!positiveItr.hasNext()) {
                return Iterators.emptyIterator();
            } else {
                firstComplementRangeLowerBound = positiveItr.next().upperBound;
            }
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() { // from class: com.google.common.collect.TreeRangeSet.ComplementRangesByLowerBound.1
                Cut<C> nextComplementRangeLowerBound;

                {
                    this.nextComplementRangeLowerBound = firstComplementRangeLowerBound;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.AbstractIterator
                public Map.Entry<Cut<C>, Range<C>> computeNext() {
                    Range<C> negativeRange;
                    if (ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound) || this.nextComplementRangeLowerBound == Cut.aboveAll()) {
                        return (Map.Entry) endOfData();
                    }
                    if (positiveItr.hasNext()) {
                        Range<C> positiveRange = (Range) positiveItr.next();
                        negativeRange = Range.create(this.nextComplementRangeLowerBound, positiveRange.lowerBound);
                        this.nextComplementRangeLowerBound = positiveRange.upperBound;
                    } else {
                        negativeRange = Range.create(this.nextComplementRangeLowerBound, Cut.aboveAll());
                        this.nextComplementRangeLowerBound = Cut.aboveAll();
                    }
                    return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
                }
            };
        }

        @Override // com.google.common.collect.AbstractNavigableMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            Cut<C> startingPoint;
            Cut<C> cut;
            if (this.complementLowerBoundWindow.hasUpperBound()) {
                startingPoint = this.complementLowerBoundWindow.upperEndpoint();
            } else {
                startingPoint = Cut.aboveAll();
            }
            final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(this.positiveRangesByUpperBound.headMap(startingPoint, this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED).descendingMap().values().iterator());
            if (positiveItr.hasNext()) {
                if (positiveItr.peek().upperBound == Cut.aboveAll()) {
                    cut = positiveItr.next().lowerBound;
                } else {
                    cut = this.positiveRangesByLowerBound.higherKey(positiveItr.peek().upperBound);
                }
            } else if (!this.complementLowerBoundWindow.contains(Cut.belowAll()) || this.positiveRangesByLowerBound.containsKey(Cut.belowAll())) {
                return Iterators.emptyIterator();
            } else {
                cut = this.positiveRangesByLowerBound.higherKey(Cut.belowAll());
            }
            final Cut<C> firstComplementRangeUpperBound = (Cut) MoreObjects.firstNonNull(cut, Cut.aboveAll());
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() { // from class: com.google.common.collect.TreeRangeSet.ComplementRangesByLowerBound.2
                Cut<C> nextComplementRangeUpperBound;

                {
                    this.nextComplementRangeUpperBound = firstComplementRangeUpperBound;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.AbstractIterator
                public Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (this.nextComplementRangeUpperBound == Cut.belowAll()) {
                        return (Map.Entry) endOfData();
                    }
                    if (positiveItr.hasNext()) {
                        Range<C> positiveRange = (Range) positiveItr.next();
                        Range<C> negativeRange = Range.create(positiveRange.upperBound, this.nextComplementRangeUpperBound);
                        this.nextComplementRangeUpperBound = positiveRange.lowerBound;
                        if (ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(negativeRange.lowerBound)) {
                            return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
                        }
                    } else if (ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
                        Range<C> negativeRange2 = Range.create(Cut.belowAll(), this.nextComplementRangeUpperBound);
                        this.nextComplementRangeUpperBound = Cut.belowAll();
                        return Maps.immutableEntry(Cut.belowAll(), negativeRange2);
                    }
                    return (Map.Entry) endOfData();
                }
            };
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return Iterators.size(entryIterator());
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @NullableDecl
        public Range<C> get(Object key) {
            if (key instanceof Cut) {
                try {
                    Cut<C> cut = (Cut) key;
                    Map.Entry<Cut<C>, Range<C>> firstEntry = tailMap((Cut) cut, true).firstEntry();
                    if (firstEntry != null && firstEntry.getKey().equals(cut)) {
                        return firstEntry.getValue();
                    }
                } catch (ClassCastException e) {
                    return null;
                }
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object key) {
            return get(key) != null;
        }
    }

    /* loaded from: classes.dex */
    private final class Complement extends TreeRangeSet<C> {
        Complement() {
            super(new ComplementRangesByLowerBound(TreeRangeSet.this.rangesByLowerBound));
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void add(Range<C> rangeToAdd) {
            TreeRangeSet.this.remove(rangeToAdd);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void remove(Range<C> rangeToRemove) {
            TreeRangeSet.this.add(rangeToRemove);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public boolean contains(C value) {
            return !TreeRangeSet.this.contains(value);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.RangeSet
        public RangeSet<C> complement() {
            return TreeRangeSet.this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final Range<Cut<C>> lowerBoundWindow;
        private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
        private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
        private final Range<C> restriction;

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap headMap(Object obj, boolean z) {
            return headMap((Cut) ((Cut) obj), z);
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap subMap(Object obj, boolean z, Object obj2, boolean z2) {
            return subMap((Cut) ((Cut) obj), z, (Cut) ((Cut) obj2), z2);
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap tailMap(Object obj, boolean z) {
            return tailMap((Cut) ((Cut) obj), z);
        }

        private SubRangeSetRangesByLowerBound(Range<Cut<C>> lowerBoundWindow, Range<C> restriction, NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
            this.lowerBoundWindow = (Range) Preconditions.checkNotNull(lowerBoundWindow);
            this.restriction = (Range) Preconditions.checkNotNull(restriction);
            this.rangesByLowerBound = (NavigableMap) Preconditions.checkNotNull(rangesByLowerBound);
            this.rangesByUpperBound = new RangesByUpperBound(rangesByLowerBound);
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
            if (!window.isConnected(this.lowerBoundWindow)) {
                return ImmutableSortedMap.of();
            }
            return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow.intersection(window), this.restriction, this.rangesByLowerBound);
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
            return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
            return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
            return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
        }

        @Override // java.util.SortedMap
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@NullableDecl Object key) {
            return get(key) != null;
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @NullableDecl
        public Range<C> get(@NullableDecl Object key) {
            if (key instanceof Cut) {
                try {
                    Cut<C> cut = (Cut) key;
                    if (this.lowerBoundWindow.contains(cut) && cut.compareTo(this.restriction.lowerBound) >= 0 && cut.compareTo(this.restriction.upperBound) < 0) {
                        if (cut.equals(this.restriction.lowerBound)) {
                            Range<C> candidate = (Range) Maps.valueOrNull(this.rangesByLowerBound.floorEntry(cut));
                            if (candidate != null && candidate.upperBound.compareTo((Cut) this.restriction.lowerBound) > 0) {
                                return candidate.intersection(this.restriction);
                            }
                        } else {
                            Range<C> result = (Range) this.rangesByLowerBound.get(cut);
                            if (result != null) {
                                return result.intersection(this.restriction);
                            }
                        }
                    }
                    return null;
                } catch (ClassCastException e) {
                    return null;
                }
            }
            return null;
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            final Iterator<Range<C>> completeRangeItr;
            if (this.restriction.isEmpty()) {
                return Iterators.emptyIterator();
            }
            if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound)) {
                return Iterators.emptyIterator();
            }
            boolean z = false;
            if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
                completeRangeItr = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
            } else {
                NavigableMap<Cut<C>, Range<C>> navigableMap = this.rangesByLowerBound;
                Cut<C> endpoint = this.lowerBoundWindow.lowerBound.endpoint();
                if (this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED) {
                    z = true;
                }
                completeRangeItr = navigableMap.tailMap(endpoint, z).values().iterator();
            }
            final Cut<Cut<C>> upperBoundOnLowerBounds = (Cut) Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() { // from class: com.google.common.collect.TreeRangeSet.SubRangeSetRangesByLowerBound.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.AbstractIterator
                public Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (!completeRangeItr.hasNext()) {
                        return (Map.Entry) endOfData();
                    }
                    Range<C> nextRange = (Range) completeRangeItr.next();
                    if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound)) {
                        return (Map.Entry) endOfData();
                    }
                    Range<C> nextRange2 = nextRange.intersection(SubRangeSetRangesByLowerBound.this.restriction);
                    return Maps.immutableEntry(nextRange2.lowerBound, nextRange2);
                }
            };
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.AbstractNavigableMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            if (this.restriction.isEmpty()) {
                return Iterators.emptyIterator();
            }
            Cut<Cut<C>> upperBoundOnLowerBounds = (Cut) Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            final Iterator<Range<C>> completeRangeItr = this.rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED).descendingMap().values().iterator();
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() { // from class: com.google.common.collect.TreeRangeSet.SubRangeSetRangesByLowerBound.2
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.AbstractIterator
                public Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (!completeRangeItr.hasNext()) {
                        return (Map.Entry) endOfData();
                    }
                    Range<C> nextRange = (Range) completeRangeItr.next();
                    if (SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo((Cut) nextRange.upperBound) >= 0) {
                        return (Map.Entry) endOfData();
                    }
                    Range<C> nextRange2 = nextRange.intersection(SubRangeSetRangesByLowerBound.this.restriction);
                    if (SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(nextRange2.lowerBound)) {
                        return Maps.immutableEntry(nextRange2.lowerBound, nextRange2);
                    }
                    return (Map.Entry) endOfData();
                }
            };
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return Iterators.size(entryIterator());
        }
    }

    @Override // com.google.common.collect.RangeSet
    public RangeSet<C> subRangeSet(Range<C> view) {
        return view.equals(Range.all()) ? this : new SubRangeSet(view);
    }

    /* loaded from: classes.dex */
    private final class SubRangeSet extends TreeRangeSet<C> {
        private final Range<C> restriction;

        SubRangeSet(Range<C> restriction) {
            super(new SubRangeSetRangesByLowerBound(Range.all(), restriction, TreeRangeSet.this.rangesByLowerBound));
            this.restriction = restriction;
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public boolean encloses(Range<C> range) {
            Range rangeEnclosing;
            if (this.restriction.isEmpty() || !this.restriction.encloses(range) || (rangeEnclosing = TreeRangeSet.this.rangeEnclosing(range)) == null || rangeEnclosing.intersection(this.restriction).isEmpty()) {
                return false;
            }
            return true;
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        @NullableDecl
        public Range<C> rangeContaining(C value) {
            Range<C> result;
            if (this.restriction.contains(value) && (result = TreeRangeSet.this.rangeContaining(value)) != null) {
                return result.intersection((Range<C>) this.restriction);
            }
            return null;
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void add(Range<C> rangeToAdd) {
            Preconditions.checkArgument(this.restriction.encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", rangeToAdd, this.restriction);
            TreeRangeSet.super.add(rangeToAdd);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void remove(Range<C> rangeToRemove) {
            if (rangeToRemove.isConnected(this.restriction)) {
                TreeRangeSet.this.remove(rangeToRemove.intersection(this.restriction));
            }
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public boolean contains(C value) {
            return this.restriction.contains(value) && TreeRangeSet.this.contains(value);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void clear() {
            TreeRangeSet.this.remove(this.restriction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.RangeSet
        public RangeSet<C> subRangeSet(Range<C> view) {
            if (view.encloses(this.restriction)) {
                return this;
            }
            if (view.isConnected(this.restriction)) {
                return new SubRangeSet(this.restriction.intersection(view));
            }
            return ImmutableRangeSet.of();
        }
    }
}
