package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class NaturalOrdering extends Ordering<Comparable> implements Serializable {
    static final NaturalOrdering INSTANCE = new NaturalOrdering();
    private static final long serialVersionUID;
    @NullableDecl
    private transient Ordering<Comparable> nullsFirst;
    @NullableDecl
    private transient Ordering<Comparable> nullsLast;

    public int compare(Comparable left, Comparable right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        return left.compareTo(right);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable> Ordering<S> nullsFirst() {
        Ordering<S> ordering = (Ordering<S>) this.nullsFirst;
        if (ordering != null) {
            return ordering;
        }
        Ordering<S> nullsFirst = super.nullsFirst();
        this.nullsFirst = nullsFirst;
        return nullsFirst;
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable> Ordering<S> nullsLast() {
        Ordering<S> ordering = (Ordering<S>) this.nullsLast;
        if (ordering != null) {
            return ordering;
        }
        Ordering<S> nullsLast = super.nullsLast();
        this.nullsLast = nullsLast;
        return nullsLast;
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable> Ordering<S> reverse() {
        return ReverseNaturalOrdering.INSTANCE;
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override // java.lang.Object
    public String toString() {
        return "Ordering.natural()";
    }

    private NaturalOrdering() {
    }
}
