package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;
/* loaded from: classes.dex */
final class ReverseNaturalOrdering extends Ordering<Comparable> implements Serializable {
    static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
    private static final long serialVersionUID = 0;

    public int compare(Comparable left, Comparable right) {
        Preconditions.checkNotNull(left);
        if (left == right) {
            return 0;
        }
        return right.compareTo(left);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable> Ordering<S> reverse() {
        return Ordering.natural();
    }

    public <E extends Comparable> E min(E a2, E b) {
        return (E) ((Comparable) NaturalOrdering.INSTANCE.max(a2, b));
    }

    public <E extends Comparable> E min(E a2, E b, E c, E... rest) {
        return (E) ((Comparable) NaturalOrdering.INSTANCE.max(a2, b, c, rest));
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable> E min(Iterator<E> iterator) {
        return (E) ((Comparable) NaturalOrdering.INSTANCE.max(iterator));
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable> E min(Iterable<E> iterable) {
        return (E) ((Comparable) NaturalOrdering.INSTANCE.max(iterable));
    }

    public <E extends Comparable> E max(E a2, E b) {
        return (E) ((Comparable) NaturalOrdering.INSTANCE.min(a2, b));
    }

    public <E extends Comparable> E max(E a2, E b, E c, E... rest) {
        return (E) ((Comparable) NaturalOrdering.INSTANCE.min(a2, b, c, rest));
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable> E max(Iterator<E> iterator) {
        return (E) ((Comparable) NaturalOrdering.INSTANCE.min(iterator));
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable> E max(Iterable<E> iterable) {
        return (E) ((Comparable) NaturalOrdering.INSTANCE.min(iterable));
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override // java.lang.Object
    public String toString() {
        return "Ordering.natural().reverse()";
    }

    private ReverseNaturalOrdering() {
    }
}
