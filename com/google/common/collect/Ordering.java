package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class Ordering<T> implements Comparator<T> {
    static final int LEFT_IS_GREATER;
    static final int RIGHT_IS_GREATER;

    @Override // java.util.Comparator
    public abstract int compare(@NullableDecl T t, @NullableDecl T t2);

    public static <C extends Comparable> Ordering<C> natural() {
        return NaturalOrdering.INSTANCE;
    }

    public static <T> Ordering<T> from(Comparator<T> comparator) {
        return comparator instanceof Ordering ? (Ordering) comparator : new ComparatorOrdering(comparator);
    }

    @Deprecated
    public static <T> Ordering<T> from(Ordering<T> ordering) {
        return (Ordering) Preconditions.checkNotNull(ordering);
    }

    public static <T> Ordering<T> explicit(List<T> valuesInOrder) {
        return new ExplicitOrdering(valuesInOrder);
    }

    public static <T> Ordering<T> explicit(T leastValue, T... remainingValuesInOrder) {
        return explicit(Lists.asList(leastValue, remainingValuesInOrder));
    }

    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }

    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }

    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }

    /* loaded from: classes.dex */
    private static class ArbitraryOrderingHolder {
        static final Ordering<Object> ARBITRARY_ORDERING = new ArbitraryOrdering();

        private ArbitraryOrderingHolder() {
        }
    }

    /* loaded from: classes.dex */
    static class ArbitraryOrdering extends Ordering<Object> {
        private final AtomicInteger counter = new AtomicInteger(0);
        private final ConcurrentMap<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeMap();

        ArbitraryOrdering() {
        }

        private Integer getUid(Object obj) {
            Integer uid = this.uids.get(obj);
            if (uid != null) {
                return uid;
            }
            Integer uid2 = Integer.valueOf(this.counter.getAndIncrement());
            Integer alreadySet = this.uids.putIfAbsent(obj, uid2);
            if (alreadySet != null) {
                return alreadySet;
            }
            return uid2;
        }

        @Override // com.google.common.collect.Ordering, java.util.Comparator
        public int compare(Object left, Object right) {
            if (left == right) {
                return 0;
            }
            if (left == null) {
                return -1;
            }
            if (right == null) {
                return 1;
            }
            int leftCode = identityHashCode(left);
            int rightCode = identityHashCode(right);
            if (leftCode == rightCode) {
                int result = getUid(left).compareTo(getUid(right));
                if (result != 0) {
                    return result;
                }
                throw new AssertionError();
            } else if (leftCode < rightCode) {
                return -1;
            } else {
                return 1;
            }
        }

        @Override // java.lang.Object
        public String toString() {
            return "Ordering.arbitrary()";
        }

        int identityHashCode(Object object) {
            return System.identityHashCode(object);
        }
    }

    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }

    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering(this);
    }

    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering(this);
    }

    public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
        return new ByFunctionOrdering(function, this);
    }

    public <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
        return (Ordering<Map.Entry<T2, ?>>) onResultOf(Maps.keyFunction());
    }

    public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator) {
        return new CompoundOrdering(this, (Comparator) Preconditions.checkNotNull(secondaryComparator));
    }

    public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators) {
        return new CompoundOrdering(comparators);
    }

    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return new LexicographicalOrdering(this);
    }

    public <E extends T> E min(Iterator<E> iterator) {
        E minSoFar = iterator.next();
        while (iterator.hasNext()) {
            minSoFar = (E) min(minSoFar, iterator.next());
        }
        return minSoFar;
    }

    public <E extends T> E min(Iterable<E> iterable) {
        return (E) min(iterable.iterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <E extends T> E min(@NullableDecl E a2, @NullableDecl E b) {
        return compare(a2, b) <= 0 ? a2 : b;
    }

    public <E extends T> E min(@NullableDecl E a2, @NullableDecl E b, @NullableDecl E c, E... rest) {
        E minSoFar = (E) min(min(a2, b), c);
        for (E r : rest) {
            minSoFar = (E) min(minSoFar, r);
        }
        return minSoFar;
    }

    public <E extends T> E max(Iterator<E> iterator) {
        E maxSoFar = iterator.next();
        while (iterator.hasNext()) {
            maxSoFar = (E) max(maxSoFar, iterator.next());
        }
        return maxSoFar;
    }

    public <E extends T> E max(Iterable<E> iterable) {
        return (E) max(iterable.iterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <E extends T> E max(@NullableDecl E a2, @NullableDecl E b) {
        return compare(a2, b) >= 0 ? a2 : b;
    }

    public <E extends T> E max(@NullableDecl E a2, @NullableDecl E b, @NullableDecl E c, E... rest) {
        E maxSoFar = (E) max(max(a2, b), c);
        for (E r : rest) {
            maxSoFar = (E) max(maxSoFar, r);
        }
        return maxSoFar;
    }

    public <E extends T> List<E> leastOf(Iterable<E> iterable, int k) {
        if (iterable instanceof Collection) {
            Collection<E> collection = (Collection) iterable;
            if (((long) collection.size()) <= ((long) k) * 2) {
                Object[] array = collection.toArray();
                Arrays.sort(array, this);
                if (array.length > k) {
                    array = Arrays.copyOf(array, k);
                }
                return Collections.unmodifiableList(Arrays.asList(array));
            }
        }
        return leastOf(iterable.iterator(), k);
    }

    public <E extends T> List<E> leastOf(Iterator<E> iterator, int k) {
        Preconditions.checkNotNull(iterator);
        CollectPreconditions.checkNonnegative(k, "k");
        if (k == 0 || !iterator.hasNext()) {
            return Collections.emptyList();
        }
        if (k >= 1073741823) {
            ArrayList<E> list = Lists.newArrayList(iterator);
            Collections.sort(list, this);
            if (list.size() > k) {
                list.subList(k, list.size()).clear();
            }
            list.trimToSize();
            return Collections.unmodifiableList(list);
        }
        TopKSelector<E> selector = TopKSelector.least(k, this);
        selector.offerAll((Iterator<? extends E>) iterator);
        return selector.topK();
    }

    public <E extends T> List<E> greatestOf(Iterable<E> iterable, int k) {
        return reverse().leastOf(iterable, k);
    }

    public <E extends T> List<E> greatestOf(Iterator<E> iterator, int k) {
        return reverse().leastOf(iterator, k);
    }

    public <E extends T> List<E> sortedCopy(Iterable<E> elements) {
        Object[] array = Iterables.toArray(elements);
        Arrays.sort(array, this);
        return Lists.newArrayList(Arrays.asList(array));
    }

    public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> elements) {
        return ImmutableList.sortedCopyOf(this, elements);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isOrdered(Iterable<? extends T> iterable) {
        Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return true;
        }
        Object next = it.next();
        while (it.hasNext()) {
            Object next2 = it.next();
            if (compare(next, next2) > 0) {
                return false;
            }
            next = next2;
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
        Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return true;
        }
        Object next = it.next();
        while (it.hasNext()) {
            Object next2 = it.next();
            if (compare(next, next2) >= 0) {
                return false;
            }
            next = next2;
        }
        return true;
    }

    @Deprecated
    public int binarySearch(List<? extends T> sortedList, @NullableDecl T key) {
        return Collections.binarySearch(sortedList, key, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class IncomparableValueException extends ClassCastException {
        private static final long serialVersionUID;
        final Object value;

        public IncomparableValueException(Object value) {
            super("Cannot compare value: " + value);
            this.value = value;
        }
    }
}
