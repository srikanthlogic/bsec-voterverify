package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Serialization;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class TreeMultiset<E> extends AbstractSortedMultiset<E> implements Serializable {
    private static final long serialVersionUID = 1;
    private final transient AvlNode<E> header;
    private final transient GeneralRange<E> range;
    private final transient Reference<AvlNode<E>> rootReference;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum Aggregate {
        SIZE {
            @Override // com.google.common.collect.TreeMultiset.Aggregate
            int nodeAggregate(AvlNode<?> node) {
                return ((AvlNode) node).elemCount;
            }

            @Override // com.google.common.collect.TreeMultiset.Aggregate
            long treeAggregate(@NullableDecl AvlNode<?> root) {
                if (root == null) {
                    return 0;
                }
                return ((AvlNode) root).totalCount;
            }
        },
        DISTINCT {
            @Override // com.google.common.collect.TreeMultiset.Aggregate
            int nodeAggregate(AvlNode<?> node) {
                return 1;
            }

            @Override // com.google.common.collect.TreeMultiset.Aggregate
            long treeAggregate(@NullableDecl AvlNode<?> root) {
                if (root == null) {
                    return 0;
                }
                return (long) ((AvlNode) root).distinctElements;
            }
        };

        abstract int nodeAggregate(AvlNode<?> avlNode);

        abstract long treeAggregate(@NullableDecl AvlNode<?> avlNode);
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Comparator comparator() {
        return super.comparator();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ boolean contains(@NullableDecl Object obj) {
        return super.contains(obj);
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset descendingMultiset() {
        return super.descendingMultiset();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ NavigableSet elementSet() {
        return super.elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ Multiset.Entry firstEntry() {
        return super.firstEntry();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ Multiset.Entry lastEntry() {
        return super.lastEntry();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ Multiset.Entry pollFirstEntry() {
        return super.pollFirstEntry();
    }

    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ Multiset.Entry pollLastEntry() {
        return super.pollLastEntry();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset subMultiset(@NullableDecl Object obj, BoundType boundType, @NullableDecl Object obj2, BoundType boundType2) {
        return super.subMultiset(obj, boundType, obj2, boundType2);
    }

    public static <E extends Comparable> TreeMultiset<E> create() {
        return new TreeMultiset<>(Ordering.natural());
    }

    public static <E> TreeMultiset<E> create(@NullableDecl Comparator<? super E> comparator) {
        return comparator == null ? new TreeMultiset<>(Ordering.natural()) : new TreeMultiset<>(comparator);
    }

    public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
        TreeMultiset<E> multiset = create();
        Iterables.addAll(multiset, elements);
        return multiset;
    }

    TreeMultiset(Reference<AvlNode<E>> rootReference, GeneralRange<E> range, AvlNode<E> endLink) {
        super(range.comparator());
        this.rootReference = rootReference;
        this.range = range;
        this.header = endLink;
    }

    TreeMultiset(Comparator<? super E> comparator) {
        super(comparator);
        this.range = GeneralRange.all(comparator);
        this.header = new AvlNode<>(null, 1);
        AvlNode<E> avlNode = this.header;
        successor(avlNode, avlNode);
        this.rootReference = new Reference<>();
    }

    private long aggregateForEntries(Aggregate aggr) {
        AvlNode<E> root = this.rootReference.get();
        long total = aggr.treeAggregate(root);
        if (this.range.hasLowerBound()) {
            total -= aggregateBelowRange(aggr, root);
        }
        if (this.range.hasUpperBound()) {
            return total - aggregateAboveRange(aggr, root);
        }
        return total;
    }

    private long aggregateBelowRange(Aggregate aggr, @NullableDecl AvlNode<E> node) {
        if (node == null) {
            return 0;
        }
        int cmp = comparator().compare(this.range.getLowerEndpoint(), ((AvlNode) node).elem);
        if (cmp < 0) {
            return aggregateBelowRange(aggr, ((AvlNode) node).left);
        }
        if (cmp != 0) {
            return aggr.treeAggregate(((AvlNode) node).left) + ((long) aggr.nodeAggregate(node)) + aggregateBelowRange(aggr, ((AvlNode) node).right);
        }
        int i = AnonymousClass4.$SwitchMap$com$google$common$collect$BoundType[this.range.getLowerBoundType().ordinal()];
        if (i == 1) {
            return ((long) aggr.nodeAggregate(node)) + aggr.treeAggregate(((AvlNode) node).left);
        }
        if (i == 2) {
            return aggr.treeAggregate(((AvlNode) node).left);
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.collect.TreeMultiset$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$collect$BoundType = new int[BoundType.values().length];

        static {
            try {
                $SwitchMap$com$google$common$collect$BoundType[BoundType.OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$collect$BoundType[BoundType.CLOSED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private long aggregateAboveRange(Aggregate aggr, @NullableDecl AvlNode<E> node) {
        if (node == null) {
            return 0;
        }
        int cmp = comparator().compare(this.range.getUpperEndpoint(), ((AvlNode) node).elem);
        if (cmp > 0) {
            return aggregateAboveRange(aggr, ((AvlNode) node).right);
        }
        if (cmp != 0) {
            return aggr.treeAggregate(((AvlNode) node).right) + ((long) aggr.nodeAggregate(node)) + aggregateAboveRange(aggr, ((AvlNode) node).left);
        }
        int i = AnonymousClass4.$SwitchMap$com$google$common$collect$BoundType[this.range.getUpperBoundType().ordinal()];
        if (i == 1) {
            return ((long) aggr.nodeAggregate(node)) + aggr.treeAggregate(((AvlNode) node).right);
        }
        if (i == 2) {
            return aggr.treeAggregate(((AvlNode) node).right);
        }
        throw new AssertionError();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
    }

    @Override // com.google.common.collect.AbstractMultiset
    int distinctElements() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
    }

    static int distinctElements(@NullableDecl AvlNode<?> node) {
        if (node == null) {
            return 0;
        }
        return ((AvlNode) node).distinctElements;
    }

    @Override // com.google.common.collect.Multiset
    public int count(@NullableDecl Object element) {
        try {
            AvlNode<E> root = this.rootReference.get();
            if (this.range.contains(element) && root != null) {
                return root.count(comparator(), element);
            }
            return 0;
        } catch (ClassCastException | NullPointerException e) {
            return 0;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int add(@NullableDecl E element, int occurrences) {
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return count(element);
        }
        Preconditions.checkArgument(this.range.contains(element));
        AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            comparator().compare(element, element);
            AvlNode<E> newRoot = new AvlNode<>(element, occurrences);
            AvlNode<E> avlNode = this.header;
            successor(avlNode, newRoot, avlNode);
            this.rootReference.checkAndSet(root, newRoot);
            return 0;
        }
        int[] result = new int[1];
        this.rootReference.checkAndSet(root, root.add(comparator(), element, occurrences, result));
        return result[0];
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int remove(@NullableDecl Object element, int occurrences) {
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return count(element);
        }
        AvlNode<E> root = this.rootReference.get();
        int[] result = new int[1];
        try {
            if (this.range.contains(element) && root != null) {
                this.rootReference.checkAndSet(root, root.remove(comparator(), element, occurrences, result));
                return result[0];
            }
            return 0;
        } catch (ClassCastException | NullPointerException e) {
            return 0;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int setCount(@NullableDecl E element, int count) {
        CollectPreconditions.checkNonnegative(count, "count");
        boolean z = true;
        if (!this.range.contains(element)) {
            if (count != 0) {
                z = false;
            }
            Preconditions.checkArgument(z);
            return 0;
        }
        AvlNode<E> root = this.rootReference.get();
        if (root == null) {
            if (count > 0) {
                add(element, count);
            }
            return 0;
        }
        int[] result = new int[1];
        this.rootReference.checkAndSet(root, root.setCount(comparator(), element, count, result));
        return result[0];
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public boolean setCount(@NullableDecl E element, int oldCount, int newCount) {
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        CollectPreconditions.checkNonnegative(oldCount, "oldCount");
        Preconditions.checkArgument(this.range.contains(element));
        AvlNode<E> root = this.rootReference.get();
        if (root != null) {
            int[] result = new int[1];
            this.rootReference.checkAndSet(root, root.setCount(comparator(), element, oldCount, newCount, result));
            return result[0] == oldCount;
        } else if (oldCount != 0) {
            return false;
        } else {
            if (newCount > 0) {
                add(element, newCount);
            }
            return true;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        if (this.range.hasLowerBound() || this.range.hasUpperBound()) {
            Iterators.clear(entryIterator());
            return;
        }
        AvlNode<E> current = ((AvlNode) this.header).succ;
        while (true) {
            AvlNode<E> avlNode = this.header;
            if (current != avlNode) {
                AvlNode<E> next = ((AvlNode) current).succ;
                ((AvlNode) current).elemCount = 0;
                ((AvlNode) current).left = null;
                ((AvlNode) current).right = null;
                ((AvlNode) current).pred = null;
                ((AvlNode) current).succ = null;
                current = next;
            } else {
                successor(avlNode, avlNode);
                this.rootReference.clear();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
        return new Multisets.AbstractEntry<E>() { // from class: com.google.common.collect.TreeMultiset.1
            @Override // com.google.common.collect.Multiset.Entry
            public E getElement() {
                return (E) baseEntry.getElement();
            }

            @Override // com.google.common.collect.Multiset.Entry
            public int getCount() {
                int result = baseEntry.getCount();
                if (result == 0) {
                    return TreeMultiset.this.count(getElement());
                }
                return result;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    @NullableDecl
    public AvlNode<E> firstNode() {
        AvlNode<E> node;
        if (this.rootReference.get() == null) {
            return null;
        }
        if (this.range.hasLowerBound()) {
            E endpoint = this.range.getLowerEndpoint();
            AvlNode<E> node2 = this.rootReference.get().ceiling(comparator(), endpoint);
            if (node2 == null) {
                return null;
            }
            if (this.range.getLowerBoundType() == BoundType.OPEN && comparator().compare(endpoint, node2.getElement()) == 0) {
                node2 = ((AvlNode) node2).succ;
            }
            node = node2;
        } else {
            node = ((AvlNode) this.header).succ;
        }
        if (node == this.header || !this.range.contains(node.getElement())) {
            return null;
        }
        return node;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @NullableDecl
    public AvlNode<E> lastNode() {
        AvlNode<E> node;
        if (this.rootReference.get() == null) {
            return null;
        }
        if (this.range.hasUpperBound()) {
            E endpoint = this.range.getUpperEndpoint();
            AvlNode<E> node2 = this.rootReference.get().floor(comparator(), endpoint);
            if (node2 == null) {
                return null;
            }
            if (this.range.getUpperBoundType() == BoundType.OPEN && comparator().compare(endpoint, node2.getElement()) == 0) {
                node2 = ((AvlNode) node2).pred;
            }
            node = node2;
        } else {
            node = ((AvlNode) this.header).pred;
        }
        if (node == this.header || !this.range.contains(node.getElement())) {
            return null;
        }
        return node;
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<E> elementIterator() {
        return Multisets.elementIterator(entryIterator());
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<Multiset.Entry<E>> entryIterator() {
        return new Iterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.TreeMultiset.2
            AvlNode<E> current;
            @NullableDecl
            Multiset.Entry<E> prevEntry;

            {
                this.current = TreeMultiset.this.firstNode();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.current == null) {
                    return false;
                }
                if (!TreeMultiset.this.range.tooHigh(this.current.getElement())) {
                    return true;
                }
                this.current = null;
                return false;
            }

            @Override // java.util.Iterator
            public Multiset.Entry<E> next() {
                if (hasNext()) {
                    Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
                    this.prevEntry = result;
                    if (((AvlNode) this.current).succ == TreeMultiset.this.header) {
                        this.current = null;
                    } else {
                        this.current = ((AvlNode) this.current).succ;
                    }
                    return result;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                CollectPreconditions.checkRemove(this.prevEntry != null);
                TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
                this.prevEntry = null;
            }
        };
    }

    @Override // com.google.common.collect.AbstractSortedMultiset
    Iterator<Multiset.Entry<E>> descendingEntryIterator() {
        return new Iterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.TreeMultiset.3
            AvlNode<E> current;
            Multiset.Entry<E> prevEntry = null;

            {
                this.current = TreeMultiset.this.lastNode();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.current == null) {
                    return false;
                }
                if (!TreeMultiset.this.range.tooLow(this.current.getElement())) {
                    return true;
                }
                this.current = null;
                return false;
            }

            @Override // java.util.Iterator
            public Multiset.Entry<E> next() {
                if (hasNext()) {
                    Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
                    this.prevEntry = result;
                    if (((AvlNode) this.current).pred == TreeMultiset.this.header) {
                        this.current = null;
                    } else {
                        this.current = ((AvlNode) this.current).pred;
                    }
                    return result;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                CollectPreconditions.checkRemove(this.prevEntry != null);
                TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
                this.prevEntry = null;
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> headMultiset(@NullableDecl E upperBound, BoundType boundType) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.upTo(comparator(), upperBound, boundType)), this.header);
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> tailMultiset(@NullableDecl E lowerBound, BoundType boundType) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.downTo(comparator(), lowerBound, boundType)), this.header);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Reference<T> {
        @NullableDecl
        private T value;

        private Reference() {
        }

        @NullableDecl
        public T get() {
            return this.value;
        }

        public void checkAndSet(@NullableDecl T expected, T newValue) {
            if (this.value == expected) {
                this.value = newValue;
                return;
            }
            throw new ConcurrentModificationException();
        }

        void clear() {
            this.value = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class AvlNode<E> {
        private int distinctElements;
        @NullableDecl
        private final E elem;
        private int elemCount;
        private int height;
        @NullableDecl
        private AvlNode<E> left;
        @NullableDecl
        private AvlNode<E> pred;
        @NullableDecl
        private AvlNode<E> right;
        @NullableDecl
        private AvlNode<E> succ;
        private long totalCount;

        AvlNode(@NullableDecl E elem, int elemCount) {
            Preconditions.checkArgument(elemCount > 0);
            this.elem = elem;
            this.elemCount = elemCount;
            this.totalCount = (long) elemCount;
            this.distinctElements = 1;
            this.height = 1;
            this.left = null;
            this.right = null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int count(Comparator<? super E> comparator, E e) {
            int cmp = comparator.compare(e, (E) this.elem);
            if (cmp < 0) {
                AvlNode<E> avlNode = this.left;
                if (avlNode == null) {
                    return 0;
                }
                return avlNode.count(comparator, e);
            } else if (cmp <= 0) {
                return this.elemCount;
            } else {
                AvlNode<E> avlNode2 = this.right;
                if (avlNode2 == null) {
                    return 0;
                }
                return avlNode2.count(comparator, e);
            }
        }

        private AvlNode<E> addRightChild(E e, int count) {
            this.right = new AvlNode<>(e, count);
            TreeMultiset.successor(this, this.right, this.succ);
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += (long) count;
            return this;
        }

        private AvlNode<E> addLeftChild(E e, int count) {
            this.left = new AvlNode<>(e, count);
            TreeMultiset.successor(this.pred, this.left, this);
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += (long) count;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        AvlNode<E> add(Comparator<? super E> comparator, @NullableDecl E e, int count, int[] result) {
            int cmp = comparator.compare(e, (E) this.elem);
            boolean z = true;
            if (cmp < 0) {
                AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return addLeftChild(e, count);
                }
                int initHeight = initLeft.height;
                this.left = initLeft.add(comparator, e, count, result);
                if (result[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += (long) count;
                return this.left.height == initHeight ? this : rebalance();
            } else if (cmp > 0) {
                AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return addRightChild(e, count);
                }
                int initHeight2 = initRight.height;
                this.right = initRight.add(comparator, e, count, result);
                if (result[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += (long) count;
                return this.right.height == initHeight2 ? this : rebalance();
            } else {
                int i = this.elemCount;
                result[0] = i;
                if (((long) i) + ((long) count) > 2147483647L) {
                    z = false;
                }
                Preconditions.checkArgument(z);
                this.elemCount += count;
                this.totalCount += (long) count;
                return this;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        AvlNode<E> remove(Comparator<? super E> comparator, @NullableDecl E e, int count, int[] result) {
            int cmp = comparator.compare(e, (E) this.elem);
            if (cmp < 0) {
                AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return this;
                }
                this.left = initLeft.remove(comparator, e, count, result);
                if (result[0] > 0) {
                    if (count >= result[0]) {
                        this.distinctElements--;
                        this.totalCount -= (long) result[0];
                    } else {
                        this.totalCount -= (long) count;
                    }
                }
                return result[0] == 0 ? this : rebalance();
            } else if (cmp > 0) {
                AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return this;
                }
                this.right = initRight.remove(comparator, e, count, result);
                if (result[0] > 0) {
                    if (count >= result[0]) {
                        this.distinctElements--;
                        this.totalCount -= (long) result[0];
                    } else {
                        this.totalCount -= (long) count;
                    }
                }
                return rebalance();
            } else {
                int i = this.elemCount;
                result[0] = i;
                if (count >= i) {
                    return deleteMe();
                }
                this.elemCount = i - count;
                this.totalCount -= (long) count;
                return this;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        AvlNode<E> setCount(Comparator<? super E> comparator, @NullableDecl E e, int count, int[] result) {
            int cmp = comparator.compare(e, (E) this.elem);
            if (cmp < 0) {
                AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    return count > 0 ? addLeftChild(e, count) : this;
                }
                this.left = initLeft.setCount(comparator, e, count, result);
                if (count == 0 && result[0] != 0) {
                    this.distinctElements--;
                } else if (count > 0 && result[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += (long) (count - result[0]);
                return rebalance();
            } else if (cmp > 0) {
                AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    return count > 0 ? addRightChild(e, count) : this;
                }
                this.right = initRight.setCount(comparator, e, count, result);
                if (count == 0 && result[0] != 0) {
                    this.distinctElements--;
                } else if (count > 0 && result[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += (long) (count - result[0]);
                return rebalance();
            } else {
                int i = this.elemCount;
                result[0] = i;
                if (count == 0) {
                    return deleteMe();
                }
                this.totalCount += (long) (count - i);
                this.elemCount = count;
                return this;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        AvlNode<E> setCount(Comparator<? super E> comparator, @NullableDecl E e, int expectedCount, int newCount, int[] result) {
            int cmp = comparator.compare(e, (E) this.elem);
            if (cmp < 0) {
                AvlNode<E> initLeft = this.left;
                if (initLeft == null) {
                    result[0] = 0;
                    if (expectedCount != 0 || newCount <= 0) {
                        return this;
                    }
                    return addLeftChild(e, newCount);
                }
                this.left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
                if (result[0] == expectedCount) {
                    if (newCount == 0 && result[0] != 0) {
                        this.distinctElements--;
                    } else if (newCount > 0 && result[0] == 0) {
                        this.distinctElements++;
                    }
                    this.totalCount += (long) (newCount - result[0]);
                }
                return rebalance();
            } else if (cmp > 0) {
                AvlNode<E> initRight = this.right;
                if (initRight == null) {
                    result[0] = 0;
                    if (expectedCount != 0 || newCount <= 0) {
                        return this;
                    }
                    return addRightChild(e, newCount);
                }
                this.right = initRight.setCount(comparator, e, expectedCount, newCount, result);
                if (result[0] == expectedCount) {
                    if (newCount == 0 && result[0] != 0) {
                        this.distinctElements--;
                    } else if (newCount > 0 && result[0] == 0) {
                        this.distinctElements++;
                    }
                    this.totalCount += (long) (newCount - result[0]);
                }
                return rebalance();
            } else {
                int i = this.elemCount;
                result[0] = i;
                if (expectedCount == i) {
                    if (newCount == 0) {
                        return deleteMe();
                    }
                    this.totalCount += (long) (newCount - i);
                    this.elemCount = newCount;
                }
                return this;
            }
        }

        private AvlNode<E> deleteMe() {
            int oldElemCount = this.elemCount;
            this.elemCount = 0;
            TreeMultiset.successor(this.pred, this.succ);
            AvlNode<E> avlNode = this.left;
            if (avlNode == null) {
                return this.right;
            }
            AvlNode<E> avlNode2 = this.right;
            if (avlNode2 == null) {
                return avlNode;
            }
            if (avlNode.height >= avlNode2.height) {
                AvlNode<E> newTop = this.pred;
                newTop.left = avlNode.removeMax(newTop);
                newTop.right = this.right;
                newTop.distinctElements = this.distinctElements - 1;
                newTop.totalCount = this.totalCount - ((long) oldElemCount);
                return newTop.rebalance();
            }
            AvlNode<E> newTop2 = this.succ;
            newTop2.right = avlNode2.removeMin(newTop2);
            newTop2.left = this.left;
            newTop2.distinctElements = this.distinctElements - 1;
            newTop2.totalCount = this.totalCount - ((long) oldElemCount);
            return newTop2.rebalance();
        }

        private AvlNode<E> removeMin(AvlNode<E> node) {
            AvlNode<E> avlNode = this.left;
            if (avlNode == null) {
                return this.right;
            }
            this.left = avlNode.removeMin(node);
            this.distinctElements--;
            this.totalCount -= (long) node.elemCount;
            return rebalance();
        }

        private AvlNode<E> removeMax(AvlNode<E> node) {
            AvlNode<E> avlNode = this.right;
            if (avlNode == null) {
                return this.left;
            }
            this.right = avlNode.removeMax(node);
            this.distinctElements--;
            this.totalCount -= (long) node.elemCount;
            return rebalance();
        }

        private void recomputeMultiset() {
            this.distinctElements = TreeMultiset.distinctElements(this.left) + 1 + TreeMultiset.distinctElements(this.right);
            this.totalCount = ((long) this.elemCount) + totalCount(this.left) + totalCount(this.right);
        }

        private void recomputeHeight() {
            this.height = Math.max(height(this.left), height(this.right)) + 1;
        }

        private void recompute() {
            recomputeMultiset();
            recomputeHeight();
        }

        private AvlNode<E> rebalance() {
            int balanceFactor = balanceFactor();
            if (balanceFactor == -2) {
                if (this.right.balanceFactor() > 0) {
                    this.right = this.right.rotateRight();
                }
                return rotateLeft();
            } else if (balanceFactor != 2) {
                recomputeHeight();
                return this;
            } else {
                if (this.left.balanceFactor() < 0) {
                    this.left = this.left.rotateLeft();
                }
                return rotateRight();
            }
        }

        private int balanceFactor() {
            return height(this.left) - height(this.right);
        }

        private AvlNode<E> rotateLeft() {
            Preconditions.checkState(this.right != null);
            AvlNode<E> newTop = this.right;
            this.right = newTop.left;
            newTop.left = this;
            newTop.totalCount = this.totalCount;
            newTop.distinctElements = this.distinctElements;
            recompute();
            newTop.recomputeHeight();
            return newTop;
        }

        private AvlNode<E> rotateRight() {
            Preconditions.checkState(this.left != null);
            AvlNode<E> newTop = this.left;
            this.left = newTop.right;
            newTop.right = this;
            newTop.totalCount = this.totalCount;
            newTop.distinctElements = this.distinctElements;
            recompute();
            newTop.recomputeHeight();
            return newTop;
        }

        private static long totalCount(@NullableDecl AvlNode<?> node) {
            if (node == null) {
                return 0;
            }
            return ((AvlNode) node).totalCount;
        }

        private static int height(@NullableDecl AvlNode<?> node) {
            if (node == null) {
                return 0;
            }
            return ((AvlNode) node).height;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        @NullableDecl
        public AvlNode<E> ceiling(Comparator<? super E> comparator, E e) {
            int cmp = comparator.compare(e, (E) this.elem);
            if (cmp < 0) {
                AvlNode<E> avlNode = this.left;
                return avlNode == null ? this : (AvlNode) MoreObjects.firstNonNull(avlNode.ceiling(comparator, e), this);
            } else if (cmp == 0) {
                return this;
            } else {
                AvlNode<E> avlNode2 = this.right;
                if (avlNode2 == null) {
                    return null;
                }
                return avlNode2.ceiling(comparator, e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        @NullableDecl
        public AvlNode<E> floor(Comparator<? super E> comparator, E e) {
            int cmp = comparator.compare(e, (E) this.elem);
            if (cmp > 0) {
                AvlNode<E> avlNode = this.right;
                return avlNode == null ? this : (AvlNode) MoreObjects.firstNonNull(avlNode.floor(comparator, e), this);
            } else if (cmp == 0) {
                return this;
            } else {
                AvlNode<E> avlNode2 = this.left;
                if (avlNode2 == null) {
                    return null;
                }
                return avlNode2.floor(comparator, e);
            }
        }

        E getElement() {
            return this.elem;
        }

        int getCount() {
            return this.elemCount;
        }

        public String toString() {
            return Multisets.immutableEntry(getElement(), getCount()).toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void successor(AvlNode<T> a2, AvlNode<T> b) {
        ((AvlNode) a2).succ = b;
        ((AvlNode) b).pred = a2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void successor(AvlNode<T> a2, AvlNode<T> b, AvlNode<T> c) {
        successor(a2, b);
        successor(b, c);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(elementSet().comparator());
        Serialization.writeMultiset(this, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        Comparator<? super E> comparator = (Comparator) stream.readObject();
        Serialization.getFieldSetter(AbstractSortedMultiset.class, "comparator").set((Serialization.FieldSetter) this, (Object) comparator);
        Serialization.getFieldSetter(TreeMultiset.class, "range").set((Serialization.FieldSetter) this, (Object) GeneralRange.all(comparator));
        Serialization.getFieldSetter(TreeMultiset.class, "rootReference").set((Serialization.FieldSetter) this, (Object) new Reference());
        AvlNode<E> header = new AvlNode<>(null, 1);
        Serialization.getFieldSetter(TreeMultiset.class, "header").set((Serialization.FieldSetter) this, (Object) header);
        successor(header, header);
        Serialization.populateMultiset(this, stream);
    }
}
