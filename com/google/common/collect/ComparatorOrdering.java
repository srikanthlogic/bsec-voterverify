package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ComparatorOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Comparator<T> comparator;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComparatorOrdering(Comparator<T> comparator) {
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(T a2, T b) {
        return this.comparator.compare(a2, b);
    }

    @Override // java.util.Comparator, java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ComparatorOrdering) {
            return this.comparator.equals(((ComparatorOrdering) object).comparator);
        }
        return false;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.comparator.hashCode();
    }

    @Override // java.lang.Object
    public String toString() {
        return this.comparator.toString();
    }
}
