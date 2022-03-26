package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ByFunctionOrdering<F, T> extends Ordering<F> implements Serializable {
    private static final long serialVersionUID = 0;
    final Function<F, ? extends T> function;
    final Ordering<T> ordering;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByFunctionOrdering(Function<F, ? extends T> function, Ordering<T> ordering) {
        this.function = (Function) Preconditions.checkNotNull(function);
        this.ordering = (Ordering) Preconditions.checkNotNull(ordering);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(F left, F right) {
        return this.ordering.compare(this.function.apply(left), this.function.apply(right));
    }

    @Override // java.util.Comparator, java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ByFunctionOrdering)) {
            return false;
        }
        ByFunctionOrdering<?, ?> that = (ByFunctionOrdering) object;
        if (!this.function.equals(that.function) || !this.ordering.equals(that.ordering)) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return Objects.hashCode(this.function, this.ordering);
    }

    @Override // java.lang.Object
    public String toString() {
        return this.ordering + ".onResultOf(" + this.function + ")";
    }
}
