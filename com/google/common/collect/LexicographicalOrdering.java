package com.google.common.collect;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
final class LexicographicalOrdering<T> extends Ordering<Iterable<T>> implements Serializable {
    private static final long serialVersionUID = 0;
    final Comparator<? super T> elementOrder;

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
        return compare((Iterable) ((Iterable) obj), (Iterable) ((Iterable) obj2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LexicographicalOrdering(Comparator<? super T> elementOrder) {
        this.elementOrder = elementOrder;
    }

    public int compare(Iterable<T> leftIterable, Iterable<T> rightIterable) {
        Iterator<T> right = rightIterable.iterator();
        for (T t : leftIterable) {
            if (!right.hasNext()) {
                return 1;
            }
            int result = this.elementOrder.compare(t, right.next());
            if (result != 0) {
                return result;
            }
        }
        if (right.hasNext()) {
            return -1;
        }
        return 0;
    }

    @Override // java.util.Comparator, java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof LexicographicalOrdering) {
            return this.elementOrder.equals(((LexicographicalOrdering) object).elementOrder);
        }
        return false;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.elementOrder.hashCode() ^ 2075626741;
    }

    @Override // java.lang.Object
    public String toString() {
        return this.elementOrder + ".lexicographical()";
    }
}
