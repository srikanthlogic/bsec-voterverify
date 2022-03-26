package com.google.common.collect;

import java.io.Serializable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NullsLastOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Ordering<? super T> ordering;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NullsLastOrdering(Ordering<? super T> ordering) {
        this.ordering = ordering;
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(@NullableDecl T left, @NullableDecl T right) {
        if (left == right) {
            return 0;
        }
        if (left == null) {
            return 1;
        }
        if (right == null) {
            return -1;
        }
        return this.ordering.compare(left, right);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends T> Ordering<S> reverse() {
        return this.ordering.reverse().nullsFirst();
    }

    @Override // com.google.common.collect.Ordering
    public <S extends T> Ordering<S> nullsFirst() {
        return this.ordering.nullsFirst();
    }

    @Override // com.google.common.collect.Ordering
    public <S extends T> Ordering<S> nullsLast() {
        return this;
    }

    @Override // java.util.Comparator, java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof NullsLastOrdering) {
            return this.ordering.equals(((NullsLastOrdering) object).ordering);
        }
        return false;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.ordering.hashCode() ^ -921210296;
    }

    @Override // java.lang.Object
    public String toString() {
        return this.ordering + ".nullsLast()";
    }
}
