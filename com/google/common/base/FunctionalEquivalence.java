package com.google.common.base;

import java.io.Serializable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
final class FunctionalEquivalence<F, T> extends Equivalence<F> implements Serializable {
    private static final long serialVersionUID = 0;
    private final Function<F, ? extends T> function;
    private final Equivalence<T> resultEquivalence;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FunctionalEquivalence(Function<F, ? extends T> function, Equivalence<T> resultEquivalence) {
        this.function = (Function) Preconditions.checkNotNull(function);
        this.resultEquivalence = (Equivalence) Preconditions.checkNotNull(resultEquivalence);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.base.Equivalence
    protected boolean doEquivalent(F a2, F b) {
        return this.resultEquivalence.equivalent(this.function.apply(a2), this.function.apply(b));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.base.Equivalence
    protected int doHash(F a2) {
        return this.resultEquivalence.hash(this.function.apply(a2));
    }

    @Override // java.lang.Object
    public boolean equals(@NullableDecl Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FunctionalEquivalence)) {
            return false;
        }
        FunctionalEquivalence<?, ?> that = (FunctionalEquivalence) obj;
        if (!this.function.equals(that.function) || !this.resultEquivalence.equals(that.resultEquivalence)) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return Objects.hashCode(this.function, this.resultEquivalence);
    }

    @Override // java.lang.Object
    public String toString() {
        return this.resultEquivalence + ".onResultOf(" + this.function + ")";
    }
}
